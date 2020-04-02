package ch.cern.eam.wshub.core.services.administration.impl;

import ch.cern.eam.wshub.core.client.InforContext;
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
import ch.cern.eam.wshub.core.tools.InforException;
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
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.xml.ws.Holder;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;

public class UserSetupServiceImpl implements UserSetupService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;
	private GridsService gridsService;

	public UserSetupServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
	}

	public String login(InforContext context, String userCode) throws InforException {
		MP9532_RunEmptyOp_001 runEmptyOp = new MP9532_RunEmptyOp_001();
		if (context != null && context.getCredentials() != null) {
			Holder<SessionType> sessionTypeHolder = new Holder<>();
			MP9532_RunEmptyOp_001_Result result =  inforws.runEmptyOpOp(runEmptyOp, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "", sessionTypeHolder, null,
					tools.getTenant(context));
			return sessionTypeHolder.value.getSessionId();
		} else {
			throw tools.generateFault("Please supply valid credentials");
		}
	}

	public EAMUser readUserSetup(InforContext context, String userCode) throws InforException {
		// The user to be readed
		MP0601_GetUserSetup_001 getUserSetup = new MP0601_GetUserSetup_001();
		getUserSetup.setUSERID(new USERID_Type());
		getUserSetup.getUSERID().setUSERCODE(userCode);

		// Execute operation of reading
		MP0601_GetUserSetup_001_Result getUserSetupResult = tools.performInforOperation(context, inforws::getUserSetupOp, getUserSetup);

		net.datastream.schemas.mp_entities.usersetup_001.UserSetup userInfor = getUserSetupResult.getResultData().getUserSetup();

		// Populate 'EAMUser' Object
		EAMUser user = tools.getInforFieldTools().transformInforObject(new EAMUser(), userInfor);

		// Fetch corresponding employee code and description
		GridRequest employeeGridRequest = new GridRequest("WSEMPS", GridRequest.GRIDTYPE.LIST);
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

	public String createUserSetup(InforContext context, EAMUser userParam) throws InforException {
		// Create user infor
		net.datastream.schemas.mp_entities.usersetup_001.UserSetup userInfor = new net.datastream.schemas.mp_entities.usersetup_001.UserSetup();

		// Check custom fields
		userInfor.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
			context,
			toCodeString(userInfor.getCLASSID()),
			userInfor.getUSERDEFINEDAREA(),
			userParam.getClassCode(),
			"USER"));

		// Init object for creation
		tools.getInforFieldTools().transformWSHubObject(userInfor, userParam, context);
		userInfor.setLANGUAGE("EN");
		userInfor.setMSGTIMEOUT(tools.getDataTypeTools().encodeAmount(BigDecimal.TEN, "Success Msg. Timeout"));
		userInfor.setISCONNECTOR("+");

		// Add user
		MP0602_AddUserSetup_001 addUser = new MP0602_AddUserSetup_001();
		addUser.setUserSetup(userInfor);

		MP0602_AddUserSetup_001_Result result = null;

		// Execute operation
		result = tools.performInforOperation(context, inforws::addUserSetupOp, addUser);

		// Return result of adding the user
		return result.getUSERID().getUSERCODE();
	}

	public String updateUserSetup(InforContext context, EAMUser userParam) throws InforException {

		// User Setup result
		MP0601_GetUserSetup_001_Result getUserSetupResult = null;

		MP0601_GetUserSetup_001 getUserSetup = new MP0601_GetUserSetup_001();
		getUserSetup.setUSERID(new USERID_Type());
		getUserSetup.getUSERID().setUSERCODE(userParam.getUserCode());

		// Execute operation of reading
		getUserSetupResult = tools.performInforOperation(context, inforws::getUserSetupOp, getUserSetup);

		// Assign the result
		net.datastream.schemas.mp_entities.usersetup_001.UserSetup userInfor = getUserSetupResult.getResultData()
				.getUserSetup();

		// If there are custom fields
		userInfor.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
			context,
			toCodeString(userInfor.getCLASSID()),
			userInfor.getUSERDEFINEDAREA(),
			userParam.getClassCode(),
			"USER"));

		// Init object for update
		tools.getInforFieldTools().transformWSHubObject(userInfor, userParam, context);

		// Update user Sync User Setup
		MP0603_SyncUserSetup_001 syncUser = new MP0603_SyncUserSetup_001();
		syncUser.setUserSetup(userInfor);

		// Execute the operation of sync user
		MP0603_SyncUserSetup_001_Result result =
			tools.performInforOperation(context, inforws::syncUserSetupOp, syncUser);

		// Return the result of the update
		return result.getUSERID().getUSERCODE();
	}

	public BatchResponse<String> updateUserSetupBatch(InforContext context, List<EAMUser> eamUsers)
			throws InforException {
		List<Callable<String>> callableList = eamUsers.stream()
				.<Callable<String>>map(eamUser -> () -> updateUserSetup(context, eamUser))
				.collect(Collectors.toList());
		return tools.processCallables(callableList);
	}

	public String deleteUserSetup(InforContext context, String userCode) throws InforException {
		MP0604_DeleteUserSetup_001 deleteUser = new MP0604_DeleteUserSetup_001();
		deleteUser.setUSERID(new USERID_Type());
		deleteUser.getUSERID().setUSERCODE(userCode);

		tools.performInforOperation(context, inforws::deleteUserSetupOp, deleteUser);
		
		return "success";
	}

}
