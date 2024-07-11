package ch.cern.eam.wshub.core.services.administration.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.administration.UserSetupService;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.administration.entities.EAMUser;
import ch.cern.eam.wshub.core.services.entities.Department;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import static ch.cern.eam.wshub.core.tools.GridTools.extractSingleResult;
import static ch.cern.eam.wshub.core.tools.GridTools.convertGridResultToMap;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.USERID_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0601_001.MP0601_GetUserSetup_001;
import net.datastream.schemas.mp_functions.mp0602_001.MP0602_AddUserSetup_001;
import net.datastream.schemas.mp_functions.mp0603_001.MP0603_SyncUserSetup_001;
import net.datastream.schemas.mp_functions.mp0604_001.MP0604_DeleteUserSetup_001;
import net.datastream.schemas.mp_functions.mp9532_001.MP9532_RunEmptyOp_001;
import net.datastream.schemas.mp_results.mp0601_001.MP0601_GetUserSetup_001_Result;
import net.datastream.schemas.mp_results.mp0602_001.MP0602_AddUserSetup_001_Result;
import net.datastream.schemas.mp_results.mp0603_001.MP0603_SyncUserSetup_001_Result;
import net.datastream.schemas.mp_results.mp9532_001.MP9532_RunEmptyOp_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import jakarta.xml.ws.Holder;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;

public class UserSetupServiceImpl implements UserSetupService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;
	private GridsService gridsService;

	public UserSetupServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
		this.gridsService = new GridsServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
	}

	public String login(EAMContext context, String userCode) throws EAMException {
		return login(context, userCode, tools, eamws);
	}
	
	public static String login(EAMContext context, String userCode, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) throws EAMException {
		MP9532_RunEmptyOp_001 runEmptyOp = new MP9532_RunEmptyOp_001();
		if (context != null && context.getCredentials() != null) {
			String sessionTerminationScenario = "terminate";
			if (context.getKeepSession() != null && context.getKeepSession()) {
				sessionTerminationScenario = null;
			}
			Holder<SessionType> sessionTypeHolder = new Holder<>();
			MP9532_RunEmptyOp_001_Result result =  eamWebServicesToolkitClient.runEmptyOpOp(runEmptyOp, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), sessionTerminationScenario, sessionTypeHolder, null,
					tools.getTenant(context));
			if (sessionTypeHolder.value != null && sessionTypeHolder.value.getSessionId() != null) {
				return sessionTypeHolder.value.getSessionId();
			} else {
				return "SUCCESS";
			}
		} else {
			throw tools.generateFault("Please supply valid credentials");
		}
	}

	public EAMUser readUserSetup(EAMContext context, String userCode) throws EAMException {
		// The user to be readed
		MP0601_GetUserSetup_001 getUserSetup = new MP0601_GetUserSetup_001();
		getUserSetup.setUSERID(new USERID_Type());
		getUserSetup.getUSERID().setUSERCODE(userCode);

		// Execute operation of reading
		MP0601_GetUserSetup_001_Result getUserSetupResult = tools.performEAMOperation(context, eamws::getUserSetupOp, getUserSetup);

		net.datastream.schemas.mp_entities.usersetup_001.UserSetup userEAM = getUserSetupResult.getResultData().getUserSetup();

		// Populate 'EAMUser' Object
		EAMUser user = tools.getEAMFieldTools().transformEAMObject(new EAMUser(), userEAM, context);

		// Fetch corresponding employee code and description
		GridRequest employeeGridRequest = new GridRequest("WSEMPS", GridRequest.GRIDTYPE.LIST);
		employeeGridRequest.setUserFunctionName("WSEMPS");
		employeeGridRequest.setUseNative(false);
		employeeGridRequest.addFilter("associateduser", userCode, "=");
		GridRequestResult employeeGridResult = gridsService.executeQuery(context, employeeGridRequest);
		user.setEmployeeCode(extractSingleResult(employeeGridResult, "employee"));
		user.setEmployeeDesc(extractSingleResult(employeeGridResult, "employeedescription"));

		// Fetch user's departmental security rights
		GridRequest departmentsGridRequest = new GridRequest("BSUSER_DSE", GridRequest.GRIDTYPE.LIST, 1000);
		departmentsGridRequest.setUseNative(false);
		departmentsGridRequest.addParam("param.usercode", userCode);
		user.setDepartmentalSecurity(convertGridResultToMap(Department.class,
				"departmentcode",
				null,
				gridsService.executeQuery(context, departmentsGridRequest)));

		return user;
	}

	public String createUserSetup(EAMContext context, EAMUser userParam) throws EAMException {
		// Create user EAM
		net.datastream.schemas.mp_entities.usersetup_001.UserSetup userEAM = new net.datastream.schemas.mp_entities.usersetup_001.UserSetup();

		// Check custom fields
		userEAM.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
			context,
			toCodeString(userEAM.getCLASSID()),
			userEAM.getUSERDEFINEDAREA(),
			userParam.getClassCode(),
			"USER"));

		// Init object for creation
		tools.getEAMFieldTools().transformWSHubObject(userEAM, userParam, context);
		userEAM.setLANGUAGE("EN");
		userEAM.setMSGTIMEOUT(tools.getDataTypeTools().encodeAmount(BigDecimal.TEN, "Success Msg. Timeout"));
		userEAM.setISCONNECTOR("+");

		// Add user
		MP0602_AddUserSetup_001 addUser = new MP0602_AddUserSetup_001();
		addUser.setUserSetup(userEAM);

		MP0602_AddUserSetup_001_Result result = null;

		// Execute operation
		result = tools.performEAMOperation(context, eamws::addUserSetupOp, addUser);

		// Return result of adding the user
		return result.getUSERID().getUSERCODE();
	}

	public String updateUserSetup(EAMContext context, EAMUser userParam) throws EAMException {

		// User Setup result
		MP0601_GetUserSetup_001_Result getUserSetupResult = null;

		MP0601_GetUserSetup_001 getUserSetup = new MP0601_GetUserSetup_001();
		getUserSetup.setUSERID(new USERID_Type());
		getUserSetup.getUSERID().setUSERCODE(userParam.getUserCode());

		// Execute operation of reading
		getUserSetupResult = tools.performEAMOperation(context, eamws::getUserSetupOp, getUserSetup);

		// Assign the result
		net.datastream.schemas.mp_entities.usersetup_001.UserSetup userEAM = getUserSetupResult.getResultData()
				.getUserSetup();

		// If there are custom fields
		userEAM.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
			context,
			toCodeString(userEAM.getCLASSID()),
			userEAM.getUSERDEFINEDAREA(),
			userParam.getClassCode(),
			"USER"));

		// Init object for update
		tools.getEAMFieldTools().transformWSHubObject(userEAM, userParam, context);

		// Update user Sync User Setup
		MP0603_SyncUserSetup_001 syncUser = new MP0603_SyncUserSetup_001();
		syncUser.setUserSetup(userEAM);

		// Execute the operation of sync user
		MP0603_SyncUserSetup_001_Result result =
			tools.performEAMOperation(context, eamws::syncUserSetupOp, syncUser);

		// Return the result of the update
		return result.getUSERID().getUSERCODE();
	}

	public BatchResponse<String> updateUserSetupBatch(EAMContext context, List<EAMUser> eamUsers)
			throws EAMException {
		List<Callable<String>> callableList = eamUsers.stream()
				.<Callable<String>>map(eamUser -> () -> updateUserSetup(context, eamUser))
				.collect(Collectors.toList());
		return tools.processCallables(callableList);
	}

	public String deleteUserSetup(EAMContext context, String userCode) throws EAMException {
		MP0604_DeleteUserSetup_001 deleteUser = new MP0604_DeleteUserSetup_001();
		deleteUser.setUSERID(new USERID_Type());
		deleteUser.getUSERID().setUSERCODE(userCode);

		tools.performEAMOperation(context, eamws::deleteUserSetupOp, deleteUser);
		
		return "success";
	}

}
