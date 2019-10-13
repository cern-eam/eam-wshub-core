package ch.cern.eam.wshub.core.services.administration.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.administration.UserSetupService;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.EAMUser;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.CLASSID_Type;
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

import javax.persistence.EntityManager;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class UserSetupServiceImpl implements UserSetupService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public UserSetupServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
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
		// User Setup result
		MP0601_GetUserSetup_001_Result getUserSetupResult = null;
		// The user to be readed
		MP0601_GetUserSetup_001 getUserSetup = new MP0601_GetUserSetup_001();
		getUserSetup.setUSERID(new USERID_Type());
		getUserSetup.getUSERID().setUSERCODE(userCode);

		// Execute operation of reading
		if (context.getCredentials() != null) {
			getUserSetupResult = inforws.getUserSetupOp(getUserSetup, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));

		} else {
			getUserSetupResult = inforws.getUserSetupOp(getUserSetup, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		net.datastream.schemas.mp_entities.usersetup_001.UserSetup userInfor = getUserSetupResult.getResultData()
				.getUserSetup();

		// Populate 'EAMUser' Object
		EAMUser user = new EAMUser();

		// User code and description
		user.setUserCode(userCode);
		// User description
		if (userInfor.getUSERID() != null) {
			user.setUserCode(userInfor.getUSERID().getUSERCODE());
			user.setUserDesc(userInfor.getUSERID().getDESCRIPTION());
		}

		// User group
		if (userInfor.getUSERGROUP() != null) {
			user.setUserGroup(userInfor.getUSERGROUP());
		}

		// User expiration date
		if (userInfor.getUSEREXPIREDATE() != null) {
			user.setUserIDExpirationDate(tools.getDataTypeTools().decodeInforDate(userInfor.getUSEREXPIREDATE()));
		}

		// Password
		if (userInfor.getPASSWORD() != null) {
			user.setPassword(userInfor.getPASSWORD());
		}

		// Password expiration date
		if (userInfor.getPASSEXPIREDATE() != null) {
			user.setPasswordExpirationDate(tools.getDataTypeTools().decodeInforDate(userInfor.getPASSEXPIREDATE()));
		}

		// Email address
		if (userInfor.getEMAIL() != null) {
			user.setEmailAddress(userInfor.getEMAIL());
		}

		// Department
		if (userInfor.getDEPARTMENTCODE() != null) {
			user.setDepartment(userInfor.getDEPARTMENTCODE());
		}

		// Class
		if (userInfor.getCLASSID() != null) {
			user.setClassCode(userInfor.getCLASSID().getCLASSCODE());
		}

		// CUSTOM FIELDS
		user.setCustomFields(tools.getCustomFieldsTools().readInforCustomFields(userInfor.getUSERDEFINEDAREA()));

		// USER DEFINED FIELDS
		user.setUserDefinedFields(tools.getUDFTools().readInforUserDefinedFields(userInfor.getStandardUserDefinedFields()));
		// User defined fields that are as attributes
		user.setUdfchar01(user.getUserDefinedFields().getUdfchar01());
		user.setUdfchar02(user.getUserDefinedFields().getUdfchar02());
		user.setUdfchar03(user.getUserDefinedFields().getUdfchar03());
		user.setUdfchar04(user.getUserDefinedFields().getUdfchar04());
		user.setUdfchar05(user.getUserDefinedFields().getUdfchar05());
		user.setUdfchar06(user.getUserDefinedFields().getUdfchar06());
		user.setUdfchar07(user.getUserDefinedFields().getUdfchar07());
		user.setUdfchar08(user.getUserDefinedFields().getUdfchar08());
		user.setUdfchar09(user.getUserDefinedFields().getUdfchar09());
		// Cern ID, comming from the custom fields
		user.setCernId(Arrays.asList(user.getCustomFields()).stream().filter(cf -> "0002".equals(cf.getCode()))
				.map(CustomField::getValue).findFirst().orElse(null));
		// Return the user
		return user;
	}

	public String createUserSetup(InforContext context, EAMUser userParam) throws InforException {
		// Create user infor
		net.datastream.schemas.mp_entities.usersetup_001.UserSetup userInfor = new net.datastream.schemas.mp_entities.usersetup_001.UserSetup();

		// Check custom fields
		if (userParam.getCustomFields() != null && userParam.getCustomFields().length > 0) {
			if (userParam.getClassCode() != null && !userParam.getClassCode().trim().equals("")) {
				userInfor.setUSERDEFINEDAREA(
						tools.getCustomFieldsTools().getInforCustomFields(context, "USER", userParam.getClassCode()));
			} else {
				userInfor.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(context, "USER", "*"));
			}
		}

		// Init object for creation
		initializeInforUserObject(userInfor, userParam, context);

		// Add user
		MP0602_AddUserSetup_001 addUser = new MP0602_AddUserSetup_001();
		addUser.setUserSetup(userInfor);

		MP0602_AddUserSetup_001_Result result = null;

		// Execute operation
		if (context.getCredentials() != null) {
			result = inforws.addUserSetupOp(addUser, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			result = inforws.addUserSetupOp(addUser, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

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
		if (context.getCredentials() != null) {
			getUserSetupResult = inforws.getUserSetupOp(getUserSetup, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));

		} else {
			getUserSetupResult = inforws.getUserSetupOp(getUserSetup, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		// Assign the result
		net.datastream.schemas.mp_entities.usersetup_001.UserSetup userInfor = getUserSetupResult.getResultData()
				.getUserSetup();

		// If there are custom fields
		if (userParam.getClassCode() != null && (userInfor.getCLASSID() == null
				|| !userParam.getClassCode().toUpperCase().equals(userInfor.getCLASSID().getCLASSCODE()))) {
			userInfor.setUSERDEFINEDAREA(
					tools.getCustomFieldsTools().getInforCustomFields(context, "USER", userParam.getClassCode().toUpperCase()));
		}
		// Init object for update
		initializeInforUserObject(userInfor, userParam, context);

		// Update user Sync User Setup
		MP0603_SyncUserSetup_001 syncUser = new MP0603_SyncUserSetup_001();
		syncUser.setUserSetup(userInfor);

		// Execute the operation of sync user
		MP0603_SyncUserSetup_001_Result result = null;
		if (context.getCredentials() != null) {
			result = inforws.syncUserSetupOp(syncUser, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			result = inforws.syncUserSetupOp(syncUser, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		// Return the result of the update
		return result.getUSERID().getUSERCODE();
	}

	public String deleteUserSetup(InforContext context, String userCode) throws InforException {
		MP0604_DeleteUserSetup_001 deleteUser = new MP0604_DeleteUserSetup_001();
		deleteUser.setUSERID(new USERID_Type());
		deleteUser.getUSERID().setUSERCODE(userCode);

		if (context.getCredentials() != null) {
			inforws.deleteUserSetupOp(deleteUser, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.deleteUserSetupOp(deleteUser, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		return "success";
	}

	private void initializeInforUserObject(net.datastream.schemas.mp_entities.usersetup_001.UserSetup userInfor,
										   EAMUser userParam, InforContext context) throws InforException {
		// == null means User creation
		if (userInfor.getUSERID() == null) {
			userInfor.setUSERID(new USERID_Type());
			userInfor.getUSERID().setUSERCODE(userParam.getUserCode().toUpperCase().trim());
			// Parameters for the creation that does not exist in the EAMUser
			// Language
			userInfor.setLANGUAGE("EN");
			// Success Msg. Timeout
			userInfor.setMSGTIMEOUT(tools.getDataTypeTools().encodeAmount("20", "Success Msg. Timeout"));
		}

		// Description
		if (userParam.getUserDesc() != null) {
			userInfor.getUSERID().setDESCRIPTION(userParam.getUserDesc());
		}

		// User group
		if (userParam.getUserGroup() != null) {
			userInfor.setUSERGROUP(userParam.getUserGroup());
		}

		// User ID expiration Date
		if (userParam.getUserIDExpirationDate() != null) {
			userInfor.setUSEREXPIREDATE(
					tools.getDataTypeTools().encodeInforDate(userParam.getUserIDExpirationDate(), "User Id expiration date"));
		}
		// Password
		if (userParam.getPassword() != null) {
			userInfor.setPASSWORD(userParam.getPassword());
		}
		// Password expiration date
		if (userParam.getPasswordExpirationDate() != null) {
			userInfor.setPASSEXPIREDATE(
					tools.getDataTypeTools().encodeInforDate(userParam.getPasswordExpirationDate(), "Password Expiration Date"));
		}
		// Email address
		if (userParam.getEmailAddress() != null) {
			userInfor.setEMAIL(userParam.getEmailAddress());
		}
		// Department
		if (userParam.getDepartment() != null) {
			userInfor.setDEPARTMENTCODE(userParam.getDepartment());
		}

		// Connector
		userInfor.setISCONNECTOR("+");

		// ClassCode
		if (userParam.getClassCode() != null) {
			if (userParam.getClassCode().trim().equals("")) {
				userInfor.setCLASSID(null);
			} else {
				userInfor.setCLASSID(new CLASSID_Type());
				userInfor.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
				userInfor.getCLASSID().setCLASSCODE(userParam.getClassCode().toUpperCase().trim());
			}
		}

		// Custom fields
		tools.getCustomFieldsTools().updateInforCustomFields(userInfor.getUSERDEFINEDAREA(), userParam.getCustomFields());

		// User defined fields
		tools.getUDFTools().updateInforUserDefinedFields(userInfor.getStandardUserDefinedFields(),
				userParam.getUserDefinedFields());
	}

}
