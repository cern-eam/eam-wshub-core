package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.SystemService;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.EntityId;
import ch.cern.eam.wshub.core.services.userdefinedscreens.impl.UserDefinedListServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
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
import net.datastream.wsdls.inforws.InforWebServicesPT;
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.createPrimarySystemParent;
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.createLocationParent;
import java.util.HashMap;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;

public class SystemServiceImpl implements SystemService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;
	private UserDefinedListService userDefinedListService;

	public SystemServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.userDefinedListService = new UserDefinedListServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
	}

	public Equipment readSystemDefault(InforContext context, String organization) throws InforException {

		MP0315_GetSystemEquipmentDefault_001 getSystemEquipmentDefault_001 = new MP0315_GetSystemEquipmentDefault_001();
		if (isEmpty(organization)) {
			getSystemEquipmentDefault_001.setORGANIZATIONID(tools.getOrganization(context));
		} else {
			getSystemEquipmentDefault_001.setORGANIZATIONID(new ORGANIZATIONID_Type());
			getSystemEquipmentDefault_001.getORGANIZATIONID().setORGANIZATIONCODE(organization);
		}

		MP0315_GetSystemEquipmentDefault_001_Result result =
				tools.performInforOperation(context, inforws::getSystemEquipmentDefaultOp, getSystemEquipmentDefault_001);

		Equipment equipment = tools.getInforFieldTools().transformInforObject(new Equipment(), result.getResultData().getSystemEquipment());
		equipment.setUserDefinedList(new HashMap<>());
		return equipment;
	}

	private SystemParentHierarchy readHierarchyInfor(InforContext context, String systemCode) throws InforException {
		MP0329_GetSystemParentHierarchy_001 getsystemh = new MP0329_GetSystemParentHierarchy_001();
		getsystemh.setSYSTEMID(new EQUIPMENTID_Type());
		getsystemh.getSYSTEMID().setEQUIPMENTCODE(systemCode);
		getsystemh.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context));
		MP0329_GetSystemParentHierarchy_001_Result result = tools.performInforOperation(context, inforws::getSystemParentHierarchyOp, getsystemh);
		return result.getResultData().getSystemParentHierarchy();
	}

	public Equipment readSystem(InforContext context, String systemCode) throws InforException {

		SystemEquipment systemEquipment = readSystemInfor(context, systemCode);

		Equipment system = tools.getInforFieldTools().transformInforObject(new Equipment(), systemEquipment);
		system.setSystemTypeCode("S");

		if (systemEquipment.getSYSTEMID() != null) {
			system.setCode(systemEquipment.getSYSTEMID().getEQUIPMENTCODE());
			system.setDescription(systemEquipment.getSYSTEMID().getDESCRIPTION());
		}

		// HIERARCHY
		if (systemEquipment.getSystemParentHierarchy().getLOCATIONID() != null) {
			system.setHierarchyLocationCode(systemEquipment.getSystemParentHierarchy().getLOCATIONID().getLOCATIONCODE());
			system.setHierarchyLocationDesc(systemEquipment.getSystemParentHierarchy().getLOCATIONID().getDESCRIPTION());
		}
		system.setHierarchyPrimarySystemDependent(systemEquipment.getSystemParentHierarchy().getDEPENDENTPRIMARYSYSTEM() != null);


		userDefinedListService.readUDLToEntity(context, system, new EntityId("OBJ", systemCode));
		return system;
	}

	public SystemEquipment readSystemInfor(InforContext context, String systemCode) throws InforException {
		MP0312_GetSystemEquipment_001 getSystem = new MP0312_GetSystemEquipment_001();
		getSystem.setSYSTEMID(new EQUIPMENTID_Type());
		getSystem.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context));
		getSystem.getSYSTEMID().setEQUIPMENTCODE(systemCode);
		MP0312_GetSystemEquipment_001_Result getAssetResult =
			tools.performInforOperation(context, inforws::getSystemEquipmentOp, getSystem);
		getAssetResult.getResultData().getSystemEquipment().setSystemParentHierarchy(readHierarchyInfor(context, systemCode));
		return getAssetResult.getResultData().getSystemEquipment();
	}

	public String updateSystem(InforContext context, Equipment systemParam) throws InforException {

			SystemEquipment systemEquipment = readSystemInfor(context, systemParam.getCode());
			//
			systemEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
				context,
				toCodeString(systemEquipment.getCLASSID()),
				systemEquipment.getUSERDEFINEDAREA(),
				systemParam.getClassCode(),
				"OBJ"));

			initializeSystemObject(systemEquipment, systemParam, context);
			tools.getInforFieldTools().transformWSHubObject(systemEquipment, systemParam, context);

			MP0313_SyncSystemEquipment_001 syncPosition = new MP0313_SyncSystemEquipment_001();
			syncPosition.setSystemEquipment(systemEquipment);
			tools.performInforOperation(context, inforws::syncSystemEquipmentOp, syncPosition);
			userDefinedListService.writeUDLToEntity(context,
				systemParam, new EntityId("OBJ", systemParam.getCode()));
			return systemParam.getCode();

	}

	public String createSystem(InforContext context, Equipment systemParam) throws InforException {

		SystemEquipment systemEquipment = new SystemEquipment();
		//
		systemEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
			context,
			toCodeString(systemEquipment.getCLASSID()),
			systemEquipment.getUSERDEFINEDAREA(),
			systemParam.getClassCode(),
			"OBJ"));

		//
		initializeSystemObject(systemEquipment, systemParam, context);
		tools.getInforFieldTools().transformWSHubObject(systemEquipment, systemParam, context);
		//
		MP0311_AddSystemEquipment_001 addPosition = new MP0311_AddSystemEquipment_001();
		addPosition.setSystemEquipment(systemEquipment);
		MP0311_AddSystemEquipment_001_Result result =
			tools.performInforOperation(context, inforws::addSystemEquipmentOp, addPosition);
		String systemCode = result.getResultData().getSYSTEMID().getEQUIPMENTCODE();
		userDefinedListService.writeUDLToEntityCopyFrom(context,
			systemParam, new EntityId("OBJ", systemCode));
		return systemCode;

	}

	public String deleteSystem(InforContext context, String systemCode) throws InforException {

		MP0314_DeleteSystemEquipment_001 deleteSystem = new MP0314_DeleteSystemEquipment_001();
		deleteSystem.setSYSTEMID(new EQUIPMENTID_Type());
		deleteSystem.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context));
		deleteSystem.getSYSTEMID().setEQUIPMENTCODE(systemCode);

		tools.performInforOperation(context, inforws::deleteSystemEquipmentOp, deleteSystem);
		userDefinedListService.deleteUDLFromEntity(context, new EntityId("OBJ", systemCode));
		return systemCode;
	}

	private void initializeSystemObject(SystemEquipment systemInfor, Equipment systemParam, InforContext context) throws InforException {
		if (systemInfor.getSYSTEMID() == null) {
			systemInfor.setSYSTEMID(new EQUIPMENTID_Type());
			systemInfor.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context));
			systemInfor.getSYSTEMID().setEQUIPMENTCODE(systemParam.getCode());
		}

		if (systemParam.getDescription() != null) {
			systemInfor.getSYSTEMID().setDESCRIPTION(systemParam.getDescription());
		}

		// HIERARCHY
		if (systemParam.getHierarchyLocationCode() != null || systemParam.getHierarchyPrimarySystemCode() != null) {
			populateSystemHierarchy(context, systemParam, systemInfor);
		}

	}

	private void populateSystemHierarchy(InforContext context, Equipment systemParam, SystemEquipment systemInfor) {
		SystemParentHierarchy systemParentHierarchy = systemInfor.getSystemParentHierarchy();

		if (systemParam.getHierarchyPrimarySystemDependent() != null && systemParam.getHierarchyPrimarySystemDependent() ||
			systemParam.getHierarchyPrimarySystemDependent() == null && systemParentHierarchy.getDEPENDENTPRIMARYSYSTEM() != null) {
			systemParentHierarchy.setDEPENDENTLOCATION(null);
			systemParentHierarchy.setDEPENDENTPRIMARYSYSTEM(createPrimarySystemParent(tools.getOrganizationCode(context), systemParam.getHierarchyPrimarySystemCode(), systemParam.getHierarchyPrimarySystemCostRollUp(), systemParentHierarchy.getDEPENDENTPRIMARYSYSTEM()));
		} else {
			systemParentHierarchy.setDEPENDENTPRIMARYSYSTEM(null);
			systemParentHierarchy.setDEPENDENTLOCATION(createLocationParent(tools.getOrganizationCode(context), systemParam.getHierarchyLocationCode(), systemParentHierarchy.getDEPENDENTLOCATION()));
			systemParentHierarchy.setNONDEPENDENTPRIMARYSYSTEM(createPrimarySystemParent(tools.getOrganizationCode(context), systemParam.getHierarchyPrimarySystemCode(), systemParam.getHierarchyPrimarySystemCostRollUp(), systemParentHierarchy.getNONDEPENDENTPRIMARYSYSTEM()));
		}
	}
}