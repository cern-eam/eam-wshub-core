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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlsoap.schemas.ws._2002._04.secext.Password;
import org.xmlsoap.schemas.ws._2002._04.secext.Username;
import org.xmlsoap.schemas.ws._2002._04.secext.UsernameToken;
import org.xmlsoap.schemas.ws._2002._04.secext.Security;
import org.xmlsoap.schemas.ws._2002._04.secext.ObjectFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.xml.ws.soap.SOAPFaultException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.nonNullOrDefault;

public class Tools {

	private ApplicationData applicationData;
	private InforWebServicesPT inforws;

	private ExecutorService executorService;
	private DataSource dataSource;
	private EntityManagerFactory entityManagerFactory;

	private Logger logger;

	private CustomFieldsTools customFieldsTools;
	private DataTypeTools dataTypeTools;
	private UserDefinedFieldsTools udfTools;
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
		this.udfTools = new UserDefinedFieldsTools(this);
		this.fieldDescriptionsTools = new FieldDescriptionTools(this);
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

	public UserDefinedFieldsTools getUDFTools() {
		return udfTools;
	}

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
			List<Future<T>> result = executorService.invokeAll(mylist);
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


	public void processRunnables(List<Runnable> mylist) throws InforException {
		try {
			executorService.invokeAll(mylist.stream().map(runnable -> Executors.callable(runnable)).collect(Collectors.toList()));
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

	public USERDEFINEDAREA getInforCustomFields(
			InforContext context,
			String previousClass,
			USERDEFINEDAREA previousCustomFields,
			String targetClass,
			String entityType)
			throws InforException {

		/*	Table with all possible cases of inputs
			IAE = IllegalArgumentException
			prevC = previousClass
			prevCFs = previousCustomFields
			targetC = targetClass

			+----+-------+---------+---------+----------------+---------------------------------+
			| ID | prevC | prevCFs | targetC | description    | usage                           |
			+----+-------+---------+---------+----------------+---------------------------------+
			| 1  | null  | CF      | null    | prevCFs        | no previous class nor target    | [4]
			| 2  | null  | CF      | ""      | merge with "*" | nullifying null class           | [3]
			| 3  | null  | CF      | "D"     | merge with "D" | merge "*" into "D"              | [3]
			| 4  | ""    | CF      | null    | prevCFs        | no previous class nor target    | [5]
			| 5  | ""    | CF      | ""      | merge with "*" | nullifying null class           | [3]
			| 6  | ""    | CF      | "D"     | merge with "D" | merge "*" into "D"              | [3]
			| 7  | "C"   | CF      | null    | prevCFs        | null [non-]update               | [4]
			| 8  | "C"   | CF      | ""      | merge with "*" | merge "C" into "*"              | [3]
			| 9  | "C"   | CF      | "D"     | merge with "D" | merge "C" into "D"              | [3]
			| 10 | null  | null    | null    | "*" CFs        | null constructor                | [5]
			| 11 | null  | null    | ""      | "*" CFs        | "" constructor                  | [2]
			| 12 | null  | null    | "D"     | "D" CFs        | constructor with "D" CFs        | [2]
			| 13 | ""    | null    | null    | IAE            | illegal argument                | [1]
			| 14 | ""    | null    | ""      | IAE            | illegal argument                | [1]
			| 15 | ""    | null    | "D"     | IAE            | illegal argument                | [1]
			| 16 | "C"   | null    | null    | IAE            | illegal argument                | [1]
			| 17 | "C"   | null    | ""      | IAE            | illegal argument                | [1]
			| 18 | "C"   | null    | "D"     | IAE            | illegal argument                | [1]
			+----+-------+---------+---------+----------------+---------------------------------+
		*/

		// [1] handle cases 13 to 18
		if(previousCustomFields == null && previousClass != null) {
			throw new IllegalArgumentException("Unable to create an object that already has a previous class.");
		}

		if(targetClass != null) {
			// this determines whether the class we should use is "*"or newClass
			// this separates cases 2 and 3, 5 and 6, 8 and 9
			String newClass = targetClass.length() == 0 ? "*" : targetClass;

			USERDEFINEDAREA classCustomFields =
				getCustomFieldsTools().getInforCustomFields(context, entityType, newClass);

			// [2] handle case 11 and 12
			if(previousCustomFields == null) return classCustomFields;

			// [3] handle cases 2, 3, 5, 6, 8 and 9
			return merge(classCustomFields, previousCustomFields);
		}

		// we can now assume that targetClass is null

		// [4] handle case 1 and 7
		if(previousCustomFields != null) return previousCustomFields;

		// [5] handle case 4 and 10
		return getCustomFieldsTools().getInforCustomFields(context, entityType, "*");
	}

	// IMPORTANT: this method mutates the argument called "base"
	private USERDEFINEDAREA merge(USERDEFINEDAREA base, USERDEFINEDAREA toppings) {
		// this hashmap turns the merge into a linear time operation
		HashMap<String, CUSTOMFIELD> codeToTopping = new HashMap<>();
		toppings.getCUSTOMFIELD().stream().forEach(topping -> codeToTopping.put(topping.getPROPERTYCODE(), topping));

		base.getCUSTOMFIELD().stream().forEach(baseCustomField -> {
			CUSTOMFIELD toppingCustomField = codeToTopping.get(baseCustomField.getPROPERTYCODE());

			CUSTOMFIELD sourceCustomField = toppingCustomField;
			if(toppingCustomField == null) sourceCustomField = baseCustomField;

			baseCustomField.setPROPERTYCODE(sourceCustomField.getPROPERTYCODE());

			baseCustomField.setDATEFIELD(
					nonNullOrDefault(sourceCustomField.getDATEFIELD(), baseCustomField.getDATEFIELD()));
			baseCustomField.setDATETIMEFIELD(
					nonNullOrDefault(sourceCustomField.getDATETIMEFIELD(), baseCustomField.getDATETIMEFIELD()));
			baseCustomField.setNUMBERFIELD(
					nonNullOrDefault(sourceCustomField.getNUMBERFIELD(), baseCustomField.getNUMBERFIELD()));
			baseCustomField.setTEXTFIELD(
					nonNullOrDefault(sourceCustomField.getTEXTFIELD(), baseCustomField.getTEXTFIELD()));
			baseCustomField.setCODEDESCFIELD(
					nonNullOrDefault(sourceCustomField.getCODEDESCFIELD(), baseCustomField.getCODEDESCFIELD()));
			baseCustomField.setENTITYCODEFIELD(
					nonNullOrDefault(sourceCustomField.getENTITYCODEFIELD(), baseCustomField.getENTITYCODEFIELD()));
		});

		return base;
	}
}
