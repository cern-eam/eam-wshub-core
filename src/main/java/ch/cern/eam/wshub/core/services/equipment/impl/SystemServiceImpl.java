package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.equipment.SystemService;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.EntityId;
import ch.cern.eam.wshub.core.services.userdefinedscreens.impl.UserDefinedListServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.systemequipment_001.*;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0311_001.MP0311_AddSystemEquipment_001;
import net.datastream.schemas.mp_functions.mp0312_001.MP0312_GetSystemEquipment_001;
import net.datastream.schemas.mp_functions.mp0313_001.MP0313_SyncSystemEquipment_001;
import net.datastream.schemas.mp_functions.mp0314_001.MP0314_DeleteSystemEquipment_001;
import net.datastream.schemas.mp_functions.mp0315_001.MP0315_GetSystemEquipmentDefault_001;
import net.datastream.schemas.mp_functions.mp0329_001.MP0329_GetSystemParentHierarchy_001;
import net.datastream.schemas.mp_results.mp0311_001.MP0311_AddSystemEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0312_001.MP0312_GetSystemEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0315_001.MP0315_GetSystemEquipmentDefault_001_Result;
import net.datastream.schemas.mp_results.mp0329_001.MP0329_GetSystemParentHierarchy_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.createPrimarySystemParent;
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.createLocationParent;
import java.util.HashMap;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;

public class SystemServiceImpl implements SystemService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;
	private UserDefinedListService userDefinedListService;

	public SystemServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
		this.userDefinedListService = new UserDefinedListServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
	}

	public Equipment readSystemDefault(EAMContext context, String organization) throws EAMException {

		MP0315_GetSystemEquipmentDefault_001 getSystemEquipmentDefault_001 = new MP0315_GetSystemEquipmentDefault_001();
		getSystemEquipmentDefault_001.setORGANIZATIONID(tools.getOrganization(context , organization));

		MP0315_GetSystemEquipmentDefault_001_Result result = tools.performEAMOperation(context, eamws::getSystemEquipmentDefaultOp, getSystemEquipmentDefault_001);

		Equipment equipment = tools.getEAMFieldTools().transformEAMObject(new Equipment(), result.getResultData().getSystemEquipment(), context);
		equipment.setUserDefinedList(new HashMap<>());
		return equipment;
	}

	private SystemParentHierarchy readHierarchyEAM(EAMContext context, String systemCode, String organization) throws EAMException {
		MP0329_GetSystemParentHierarchy_001 getsystemh = new MP0329_GetSystemParentHierarchy_001();
		getsystemh.setSYSTEMID(new EQUIPMENTID_Type());
		getsystemh.getSYSTEMID().setEQUIPMENTCODE(systemCode);
		getsystemh.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context, organization));
		MP0329_GetSystemParentHierarchy_001_Result result = tools.performEAMOperation(context, eamws::getSystemParentHierarchyOp, getsystemh);
		return result.getResultData().getSystemParentHierarchy();
	}

	public Equipment readSystem(EAMContext context, String systemCode, String organization) throws EAMException {

		SystemEquipment systemEquipment = readSystemEAM(context, systemCode, organization);

		Equipment system = tools.getEAMFieldTools().transformEAMObject(new Equipment(), systemEquipment, context);
		system.setSystemTypeCode("S");

		// HIERARCHY
		if (systemEquipment.getSystemParentHierarchy().getLOCATIONID() != null) {
			system.setHierarchyLocationCode(systemEquipment.getSystemParentHierarchy().getLOCATIONID().getLOCATIONCODE());
			system.setHierarchyLocationDesc(systemEquipment.getSystemParentHierarchy().getLOCATIONID().getDESCRIPTION());
		}
		system.setHierarchyPrimarySystemDependent(systemEquipment.getSystemParentHierarchy().getDEPENDENTPRIMARYSYSTEM() != null);

		tools.processRunnables(
				() -> { if(tools.isDatabaseConnectionConfigured()) userDefinedListService.readUDLToEntity(context, system, new EntityId("OBJ", systemCode)); },
				() -> system.setSystemStatusCode(tools.getFieldDescriptionsTools().readSystemCodeForUserCode(context, "OBST", system.getStatusCode()))
		);

		return system;
	}

	public SystemEquipment readSystemEAM(EAMContext context, String systemCode, String organization) throws EAMException {
		MP0312_GetSystemEquipment_001 getSystem = new MP0312_GetSystemEquipment_001();
		getSystem.setSYSTEMID(new EQUIPMENTID_Type());
		getSystem.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context, organization));
		getSystem.getSYSTEMID().setEQUIPMENTCODE(systemCode);
		MP0312_GetSystemEquipment_001_Result getAssetResult =
			tools.performEAMOperation(context, eamws::getSystemEquipmentOp, getSystem);
		getAssetResult.getResultData().getSystemEquipment().setSystemParentHierarchy(readHierarchyEAM(context, systemCode, organization));
		return getAssetResult.getResultData().getSystemEquipment();
	}

	public String updateSystem(EAMContext context, Equipment systemParam) throws EAMException {

			SystemEquipment systemEquipment = readSystemEAM(context, systemParam.getCode(), systemParam.getOrganization());
			//
			systemEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
				context,
				toCodeString(systemEquipment.getCLASSID()),
				systemEquipment.getUSERDEFINEDAREA(),
				systemParam.getClassCode(),
				"OBJ"));

			initializeSystemObject(systemEquipment, systemParam, context);
			tools.getEAMFieldTools().transformWSHubObject(systemEquipment, systemParam, context);

			MP0313_SyncSystemEquipment_001 syncPosition = new MP0313_SyncSystemEquipment_001();
			syncPosition.setSystemEquipment(systemEquipment);
			tools.performEAMOperation(context, eamws::syncSystemEquipmentOp, syncPosition);
			userDefinedListService.writeUDLToEntity(context,
				systemParam, new EntityId("OBJ", systemParam.getCode()));
			return systemParam.getCode();

	}

	public String createSystem(EAMContext context, Equipment systemParam) throws EAMException {
		SystemEquipment systemEquipment = new SystemEquipment();
		//
		systemEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
			context,
			toCodeString(systemEquipment.getCLASSID()),
			systemEquipment.getUSERDEFINEDAREA(),
			systemParam.getClassCode(),
			"OBJ"));

		//
		initializeSystemObject(systemEquipment, systemParam, context);
		tools.getEAMFieldTools().transformWSHubObject(systemEquipment, systemParam, context);
		//
		MP0311_AddSystemEquipment_001 addPosition = new MP0311_AddSystemEquipment_001();
		addPosition.setSystemEquipment(systemEquipment);
		MP0311_AddSystemEquipment_001_Result result =
			tools.performEAMOperation(context, eamws::addSystemEquipmentOp, addPosition);
		String systemCode = result.getResultData().getSYSTEMID().getEQUIPMENTCODE();
		userDefinedListService.writeUDLToEntityCopyFrom(context,
			systemParam, new EntityId("OBJ", systemCode));
		return systemCode;
	}

	public String deleteSystem(EAMContext context, String systemCode, String organization) throws EAMException {

		MP0314_DeleteSystemEquipment_001 deleteSystem = new MP0314_DeleteSystemEquipment_001();
		deleteSystem.setSYSTEMID(new EQUIPMENTID_Type());
		deleteSystem.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context, organization));
		deleteSystem.getSYSTEMID().setEQUIPMENTCODE(systemCode);

		tools.performEAMOperation(context, eamws::deleteSystemEquipmentOp, deleteSystem);
		userDefinedListService.deleteUDLFromEntity(context, new EntityId("OBJ", systemCode));
		return systemCode;
	}

	private void initializeSystemObject(SystemEquipment systemEAM, Equipment systemParam, EAMContext context) throws EAMException {
		if (systemEAM.getSYSTEMID() == null) {
			systemEAM.setSYSTEMID(new EQUIPMENTID_Type());
			systemEAM.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context, systemParam.getOrganization()));
			systemEAM.getSYSTEMID().setEQUIPMENTCODE(systemParam.getCode());
		}

		if (systemParam.getDescription() != null) {
			systemEAM.getSYSTEMID().setDESCRIPTION(systemParam.getDescription());
		}

		// HIERARCHY
		if (systemParam.getHierarchyLocationCode() != null || systemParam.getHierarchyPrimarySystemCode() != null) {
			if (systemEAM.getSystemParentHierarchy() == null) {
				systemEAM.setSystemParentHierarchy(new SystemParentHierarchy());
			}
			populateSystemHierarchy(context, systemParam, systemEAM);
		}

	}

	private void populateSystemHierarchy(EAMContext context, Equipment systemParam, SystemEquipment systemEAM) {
		SystemParentHierarchy systemParentHierarchy = systemEAM.getSystemParentHierarchy();

		if (systemParam.getHierarchyPrimarySystemDependent() != null && systemParam.getHierarchyPrimarySystemDependent() && !"".equals(systemParam.getHierarchyPrimarySystemCode()) ||
			systemParam.getHierarchyPrimarySystemDependent() == null && systemParentHierarchy.getDEPENDENTPRIMARYSYSTEM() != null && !"".equals(systemParam.getHierarchyPrimarySystemCode())) {
			systemParentHierarchy.setDEPENDENTLOCATION(null);
			systemParentHierarchy.setDEPENDENTPRIMARYSYSTEM(createPrimarySystemParent(tools.getOrganizationCode(context, systemParam.getHierarchyPrimarySystemOrg()), systemParam.getHierarchyPrimarySystemCode(), systemParam.getHierarchyPrimarySystemCostRollUp(), systemParentHierarchy.getDEPENDENTPRIMARYSYSTEM()));
		} else {
			systemParentHierarchy.setDEPENDENTPRIMARYSYSTEM(null);
			systemParentHierarchy.setDEPENDENTLOCATION(createLocationParent(tools.getOrganizationCode(context), systemParam.getHierarchyLocationCode(), systemParentHierarchy.getDEPENDENTLOCATION()));
			systemParentHierarchy.setNONDEPENDENTPRIMARYSYSTEM(createPrimarySystemParent(tools.getOrganizationCode(context, systemParam.getHierarchyPrimarySystemOrg()), systemParam.getHierarchyPrimarySystemCode(), systemParam.getHierarchyPrimarySystemCostRollUp(), systemParentHierarchy.getNONDEPENDENTPRIMARYSYSTEM()));
		}
	}
}