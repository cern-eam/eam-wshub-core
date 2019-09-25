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
		Security security;

		ObjectFactory of = new ObjectFactory();
		security = of.createSecurity();
		Username un = of.createUsername();
		un.setValue(context.getCredentials().getUsername().toUpperCase());
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

	public InforException generateFault(String reason) {
		return new InforException(reason, null, null);
	}

	public InforException generateFault(String reason, ExceptionInfo[] errors) {
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

}
