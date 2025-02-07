package ch.cern.eam.wshub.core.tools;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.entities.BatchSingleResponse;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.services.entities.EntityOrganizationCodePair;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.MessageConfigType;
import net.datastream.schemas.mp_functions.MessageItemConfigType;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import org.w3c.dom.NodeList;
import org.xmlsoap.schemas.ws._2002._04.secext.Password;
import org.xmlsoap.schemas.ws._2002._04.secext.Username;
import org.xmlsoap.schemas.ws._2002._04.secext.UsernameToken;
import org.xmlsoap.schemas.ws._2002._04.secext.Security;
import org.xmlsoap.schemas.ws._2002._04.secext.ObjectFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.soap.SOAPFaultException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;

public class Tools {

	private ApplicationData applicationData;
	private EAMWebServicesPT eamws;

	private ExecutorService executorService;
	private DataSource dataSource;
	private EntityManagerFactory entityManagerFactory;

	private Logger logger;

	private CustomFieldsTools customFieldsTools;
	private DataTypeTools dataTypeTools;
	private FieldDescriptionTools fieldDescriptionsTools;
	private GridTools gridTools;
	private EAMFieldTools eamFieldTools;

	public Tools(ApplicationData applicationData,
				 EAMWebServicesPT eamWebServicesToolkitClient,
				 ExecutorService executorService,
				 DataSource dataSource,
				 EntityManagerFactory entityManagerFactory,
				 Logger logger) {
		//
		this.applicationData = applicationData;
		this.eamws = eamWebServicesToolkitClient;
		this.executorService = executorService;
		this.dataSource = dataSource;
		this.entityManagerFactory = entityManagerFactory;
		// Init specific tool classes
		this.customFieldsTools = new CustomFieldsTools(this, applicationData, eamws);
		this.eamFieldTools = new EAMFieldTools(customFieldsTools, this);
		this.dataTypeTools = new DataTypeTools(this);
		this.fieldDescriptionsTools = new FieldDescriptionTools(this, applicationData, eamws);
		this.gridTools = new GridTools(this);

		//
		this.logger = logger;
	}

	//
	//
	//
	public EntityManager getEntityManager() {
		return this.entityManagerFactory.createEntityManager();
	}

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public CustomFieldsTools getCustomFieldsTools() {return this.customFieldsTools; }

	public DataTypeTools getDataTypeTools() {return this.dataTypeTools; }

	public FieldDescriptionTools getFieldDescriptionsTools() {return fieldDescriptionsTools; }

	public GridTools getGridTools() {return gridTools;}

	public EAMFieldTools getEAMFieldTools() {return eamFieldTools;}

	//
	//
	//
	public void log(Level logLevel, String message) {
		if (logger != null) {
			logger.log(logLevel, message);
		}
	}

	//
	// SECURITY AND SESSION
	//
	public Security createSecurityHeader(EAMContext context) throws EAMException {
		if (context == null || context.getCredentials() == null) {
			throw generateFault("Credentials must be initialized.");
		}

		ObjectFactory of = new ObjectFactory();
		Security security = of.createSecurity();
		Username un = of.createUsername();
		un.setValue(context.getCredentials().getUsername().toUpperCase() + "@" + getTenant(context));
		Password pass = of.createPassword();
		pass.setValue(context.getCredentials().getPassword());

		UsernameToken unt = of.createUsernameToken();
		unt.setPassword(pass);
		unt.setUsername(un);

		security.getAny().add(unt);

		return security;
	}

	public SessionType createEAMSession(EAMContext context) {
		SessionType session = new SessionType();
		if (context.getSessionID() != null) {
			session.setSessionId(context.getSessionID());
		} else if (context.getAuthToken() != null) {
			// Set it temporarily in order to read it (and remove from the SOAP Envelope) in the AuthenticationHandler
			session.setSessionId("Bearer " + context.getAuthToken());
		}
		return session;
	}

	/**
	 * Generates MessageConfig element that can be added to the request to tell the
	 * server to omit the InformationAlert and WarningAlert elements from the
	 * response.
	 *
	 * @return MessageConfig
	 */
	public MessageConfigType createMessageConfig() {
		MessageConfigType messageConfigType = new MessageConfigType();
		MessageItemConfigType returnAlertsMessageItemConfigType = new MessageItemConfigType();
		returnAlertsMessageItemConfigType.setName("returnAlerts");
		returnAlertsMessageItemConfigType.setValue("false");
		messageConfigType.getConfigItem().add(returnAlertsMessageItemConfigType);
		return messageConfigType;
	}

	//
	// ORGANIZATION
	//
	public ORGANIZATIONID_Type getOrganization(EAMContext eamContext) {
		ORGANIZATIONID_Type org = new ORGANIZATIONID_Type();
		org.setORGANIZATIONCODE(getOrganizationCode(eamContext));
		return org;
	}

	public ORGANIZATIONID_Type getOrganization(EAMContext eamContext, String organizationCode) {
		ORGANIZATIONID_Type org = new ORGANIZATIONID_Type();
		org.setORGANIZATIONCODE(getOrganizationCode(eamContext, organizationCode));
		return org;
	}

	public String getOrganizationCode(EAMContext context, String organizationCode) {
		if (isNotEmpty(organizationCode)) {
			return organizationCode;
		}

		return getOrganizationCode(context);
	}

	public String getOrganizationCode(EAMContext eamContext) {
		if (eamContext != null && eamContext.getOrganizationCode() != null) {
			return eamContext.getOrganizationCode();
		} else {
			return applicationData.getOrganization();
		}
	}

	public static String extractEntityCode(String code) {
		if (isNotEmpty(code) && code.contains("#")) {
			return code.split("#")[0];
		}
		return code;
	}

	public static String extractOrganizationCode(String code) {
		if (isNotEmpty(code) && code.contains("#") && code.split("#").length > 1) {
			return code.split("#")[1];
		}
		return null;
	}

	public static EntityOrganizationCodePair extractEntityOrganizationCodePair(String code) {
		if (isEmpty(code)) {
			return new EntityOrganizationCodePair();
		}
		String[] parts = code.split("#");
		if (parts.length == 1) {
			return new EntityOrganizationCodePair(code);
		}
		return new EntityOrganizationCodePair(parts[0], parts[1]);
	}

	//
	// TENANT
	//
	public String getTenant(EAMContext eamContext) {
		if (eamContext != null && eamContext.getTenant() != null) {
			return eamContext.getTenant();
		} else {
			return applicationData.getTenant();
		}
	}

	public static EAMException generateFault(String reason) {
		return new EAMException(reason, null, null);
	}

	public static EAMException generateFault(String reason, ExceptionInfo[] errors) {
		return new EAMException(reason, null, errors);
	}

	//
	// BATCH PROCESSING
	//
	public <T> BatchResponse<T> processCallables(List<Callable<T>> mylist) {
		List<BatchSingleResponse<T>> responseList = null;

		try {
			List<Future<T>> result = executorService.invokeAll(mylist, 2, TimeUnit.MINUTES);
			responseList = (List<BatchSingleResponse<T>>) result.stream().<BatchSingleResponse<T>>map(future -> {
				try {
					return new BatchSingleResponse(future.get(), null);
				}
				catch (ExecutionException exception) {
					if (exception.getCause() instanceof SOAPFaultException) {
						SOAPFaultException soapFaultException = (SOAPFaultException) exception.getCause();
						return new BatchSingleResponse(null, decodeExceptionInfoList(soapFaultException));
					} else {
						return new BatchSingleResponse(null, exception.getCause().getMessage());
					}
				}
				catch (Exception exception) {
					return new BatchSingleResponse(null, "Server error");
				}
			}).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}

		BatchResponse<T> response = new BatchResponse<>();
		response.setResponseList(responseList);
		return response;
	}

	public void processRunnables(Runnable... runnables) throws EAMException {
		processRunnables(Arrays.asList(runnables));
	}

	public void processRunnables(List<Runnable> mylist) throws EAMException {
		try {
			executorService.invokeAll(mylist.stream().map(runnable -> Executors.callable(runnable)).collect(Collectors.toList()), 2, TimeUnit.MINUTES);
		} catch (Exception exception) {
			log(Level.SEVERE, "Error during Tools.processRunnables() execution: " + exception.getMessage());
		}
	}

	private String decodeExceptionInfoList(SOAPFaultException soapFaultException) {
		String errorMessage = soapFaultException.getMessage();
		try {
			NodeList nodeList = soapFaultException.getFault().getDetail().getFirstChild().getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				errorMessage += ", " + nodeList.item(i).getFirstChild().getLastChild().getTextContent();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return errorMessage;
	}

	//
	// EAM CONTEXT
	//
	public EAMContext getEAMContext(Credentials credentials, String sessionID) {
		if (credentials != null) {
			return new EAMContext(credentials);
		} else {
			return new EAMContext(sessionID);
		}
	}

	public EAMContext getEAMContext(Credentials credentials) {
		return new EAMContext(credentials);
	}

	public EAMContext getEAMContext(String username, String password) {
		Credentials credentials = new Credentials();
		credentials.setUsername(username);
		credentials.setPassword(password);
		return getEAMContext(credentials);
	}

	/**
	 * Close the connection with the database
	 *
	 * @param connection
	 *            Connection to be closed
	 * @param statement
	 *            statement to be closed
	 * @param resultSet
	 *            Resultset to be closed
	 */
	public void closeConnection(Connection connection, Statement statement, ResultSet resultSet) {
		try {
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		} catch (Exception e) {/* Error closing connection */
			e.printStackTrace();
			log(Level.SEVERE, "Couldn't close the DB connection: " + e.getMessage());
		}
	}

	public void demandDatabaseConnection() throws EAMException {
		if (this.entityManagerFactory == null || getDataSource() == null) {
			throw generateFault("This operation requires DB connection.");
		}
	}

	public boolean isDatabaseConnectionConfigured() {
		return (this.entityManagerFactory != null && getDataSource() != null);
	}


	public <A, R> BatchResponse<R> batchOperation(EAMContext context, WSHubOperation<A, R> operation, List<A> arguments) {
		List<Callable<R>> callableList = arguments.stream()
				.<Callable<R>>map(argument -> () -> operation.apply(context, argument))
				.collect(Collectors.toList());

		return processCallables(callableList);
	}

	/*
		Use case:
		You have a map, named map, of user codes to work order numbers Map<String, String>
		You wish to get all work orders at once using a batch operation, but still have it in a map
		of user codes to work orders: Map<String, WorkOrder>.
		Doing it the normal way yields:
		List<WorkOrder> = readWorkOrderBatch(context, map.values());

		With this method, one can do:
		List<String> workOrderNumbers = map.values();
		List<WorkOrder> workOrders = readWorkOrderBatch(context, workOrderNumbers);
		Map<String, WorkOrder> finalMap = batchOperationToMap(workOrderNumbers, workOrders, map);
	 */
	public <A, R, T> Map<T, R> batchOperationToMap(List<A> arguments, BatchResponse<R> results, Map<T, A> map) {
		List<R> responses = results.getResponseList().stream()
				.map(response -> response.getResponse()).collect(Collectors.toList());

		if (arguments.size() != responses.size()) {
			throw new RuntimeException("The size of the results and the arguments does not match");
		}

		Function<A, List<T>> getKeysFromValue = value -> map.entrySet().stream()
				.filter(entry -> entry.getValue() == value)
				.map(entry -> entry.getKey())
				.collect(Collectors.toList());

		Map<T, R> returnMap = new HashMap<>();

		for(int i = 0; i < responses.size(); ++i) {
			List<T> keys = getKeysFromValue.apply(arguments.get(i));
			R result = responses.get(i);

			keys.forEach(key -> returnMap.put(key, result));
		}

		return returnMap;
	}

	public <A, R> R performEAMOperation(EAMContext context, EAMOperation<A, R> operation, A argument)
			throws EAMException {
		Security security = null;
		String organization = getOrganizationCode(context);
		String sessionTerminationScenario = "terminate";
		Holder holder = null;
		MessageConfigType messageConfigType = createMessageConfig();

		if (context.getKeepSession() != null && context.getKeepSession()) {
			sessionTerminationScenario = null;
		}

		if(context.getCredentials() != null) {
			security = createSecurityHeader(context);
		} else {
			holder = new Holder<>(createEAMSession(context));
		}

		// Every MP class extending BaseSchemaRequestElement has ESIGNATURE property
		if (argument instanceof BaseSchemaRequestElement && context.getSignature() != null) {
			((BaseSchemaRequestElement) argument).setESIGNATURE(eamFieldTools.transformWSHubObject(new ESIGNATURE(), context.getSignature(), context));
		}

		String tenant = getTenant(context);

		return operation.apply(argument, organization, security, sessionTerminationScenario, holder, messageConfigType, tenant);
	}
}
