package ch.cern.eam.wshub.core.tools;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.entities.BatchSingleResponse;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.MessageConfigType;
import net.datastream.schemas.mp_functions.MessageItemConfigType;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import org.w3c.dom.NodeList;
import org.xmlsoap.schemas.ws._2002._04.secext.Password;
import org.xmlsoap.schemas.ws._2002._04.secext.Username;
import org.xmlsoap.schemas.ws._2002._04.secext.UsernameToken;
import org.xmlsoap.schemas.ws._2002._04.secext.Security;
import org.xmlsoap.schemas.ws._2002._04.secext.ObjectFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPFaultException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Tools {

	private ApplicationData applicationData;
	private InforWebServicesPT inforws;

	private ExecutorService executorService;
	private DataSource dataSource;
	private EntityManagerFactory entityManagerFactory;

	private Logger logger;

	private CustomFieldsTools customFieldsTools;
	private DataTypeTools dataTypeTools;
	private FieldDescriptionTools fieldDescriptionsTools;
	private GridTools gridTools;
	private InforFieldTools inforFieldTools;

	public Tools(ApplicationData applicationData,
				 InforWebServicesPT inforWebServicesToolkitClient,
				 ExecutorService executorService,
				 DataSource dataSource,
				 EntityManagerFactory entityManagerFactory,
				 Logger logger) {
		//
		this.applicationData = applicationData;
		this.inforws = inforWebServicesToolkitClient;
		this.executorService = executorService;
		this.dataSource = dataSource;
		this.entityManagerFactory = entityManagerFactory;
		// Init specific tool classes
		this.customFieldsTools = new CustomFieldsTools(this, applicationData, inforws);
		this.inforFieldTools = new InforFieldTools(customFieldsTools, this);
		this.dataTypeTools = new DataTypeTools(this);
		this.fieldDescriptionsTools = new FieldDescriptionTools(this, applicationData, inforws);
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

	public InforFieldTools getInforFieldTools() {return inforFieldTools;}

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
	public Security createSecurityHeader(InforContext context) throws InforException {
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

	public SessionType createInforSession(InforContext context) {
		SessionType session = new SessionType();
		session.setSessionId(context.getSessionID());
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
	public ORGANIZATIONID_Type getOrganization(InforContext inforContext) {
		ORGANIZATIONID_Type org = new ORGANIZATIONID_Type();
		if (inforContext != null && inforContext.getOrganizationCode() != null) {
			org.setORGANIZATIONCODE(inforContext.getOrganizationCode());
		} else {
			org.setORGANIZATIONCODE(applicationData.getOrganization());
		}
		return org;
	}

	public String getOrganizationCode(InforContext inforContext) {
		if (inforContext != null && inforContext.getOrganizationCode() != null) {
			return inforContext.getOrganizationCode();
		} else {
			return applicationData.getOrganization();
		}
	}

	//
	// TENANT
	//
	public String getTenant(InforContext inforContext) {
		if (inforContext != null && inforContext.getTenant() != null) {
			return inforContext.getTenant();
		} else {
			return applicationData.getTenant();
		}
	}

	public static InforException generateFault(String reason) {
		return new InforException(reason, null, null);
	}

	public static InforException generateFault(String reason, ExceptionInfo[] errors) {
		return new InforException(reason, null, errors);
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

	public void processRunnables(Runnable... runnables) throws InforException {
		processRunnables(Arrays.asList(runnables));
	}

	public void processRunnables(List<Runnable> mylist) throws InforException {
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
	// INFOR CONTEXT
	//
	public InforContext getInforContext(Credentials credentials, String sessionID) {
		if (credentials != null) {
			return new InforContext(credentials);
		} else {
			return new InforContext(sessionID);
		}
	}

	public InforContext getInforContext(Credentials credentials) {
		return new InforContext(credentials);
	}

	public InforContext getInforContext(String username, String password) {
		Credentials credentials = new Credentials();
		credentials.setUsername(username);
		credentials.setPassword(password);
		return getInforContext(credentials);
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

	public void demandDatabaseConnection() throws InforException {
		if (this.entityManagerFactory == null || getDataSource() == null) {
			throw generateFault("This operation requires DB connection.");
		}
	}

	public boolean isDatabaseConnectionConfigured() {
		return (this.entityManagerFactory != null && getDataSource() != null);
	}


	public <A, R> BatchResponse<R> batchOperation(InforContext context, WSHubOperation<A, R> operation, List<A> arguments) {
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

	public <A, R> R performInforOperation(InforContext context, InforOperation<A, R> operation, A argument)
			throws InforException {
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
			holder = new Holder<>(createInforSession(context));
		}

		// Every MP class extending BaseSchemaRequestElement has ESIGNATURE property
		if (argument instanceof BaseSchemaRequestElement && context.getSignature() != null) {
			((BaseSchemaRequestElement) argument).setESIGNATURE(inforFieldTools.transformWSHubObject(new ESIGNATURE(), context.getSignature(), context));
		}

		String tenant = getTenant(context);

		return operation.apply(argument, organization, security, sessionTerminationScenario, holder, messageConfigType, tenant);
	}
}
