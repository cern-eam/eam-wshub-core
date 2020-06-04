package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.annotations.BooleanType;
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

import java.util.Arrays;
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

	public Equipment readSystem(InforContext context, String systemCode) throws InforException {

		SystemEquipment systemEquipment = readSystemInfor(context, systemCode);

		Equipment system = tools.getInforFieldTools().transformInforObject(new Equipment(), systemEquipment);
		system.setSystemTypeCode("S");

		if (systemEquipment.getSYSTEMID() != null) {
			system.setCode(systemEquipment.getSYSTEMID().getEQUIPMENTCODE());
			system.setDescription(systemEquipment.getSYSTEMID().getDESCRIPTION());
		}

		// HIERARCHY
		MP0329_GetSystemParentHierarchy_001 getsystemh = new MP0329_GetSystemParentHierarchy_001();
		getsystemh.setSYSTEMID(systemEquipment.getSYSTEMID());
		MP0329_GetSystemParentHierarchy_001_Result gethresult =
			tools.performInforOperation(context, inforws::getSystemParentHierarchyOp, getsystemh);
		// System parent hierarchy
		systemEquipment.setSystemParentHierarchy(gethresult.getResultData().getSystemParentHierarchy());

		// Location
		if (systemEquipment.getSystemParentHierarchy().getLOCATIONID() != null) {
			system.setHierarchyLocationCode(
					systemEquipment.getSystemParentHierarchy().getLOCATIONID().getLOCATIONCODE());
			system.setHierarchyLocationDesc(
					systemEquipment.getSystemParentHierarchy().getLOCATIONID().getDESCRIPTION());
		}
		// Dependent primary system
		if (systemEquipment.getSystemParentHierarchy().getDEPENDENTPRIMARYSYSTEM() != null) {
			system.setHierarchyPrimarySystemCode(systemEquipment.getSystemParentHierarchy().getDEPENDENTPRIMARYSYSTEM()
					.getSYSTEMID().getEQUIPMENTCODE());
			system.setHierarchyPrimarySystemDesc(systemEquipment.getSystemParentHierarchy().getDEPENDENTPRIMARYSYSTEM()
					.getSYSTEMID().getDESCRIPTION());
			system.setHierarchyPrimarySystemDependent(true);
			system.setHierarchyPrimarySystemCostRollUp(decodeBoolean(
					systemEquipment.getSystemParentHierarchy().getDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP()));
		}
		// Non Dependent primary system
		else if (systemEquipment.getSystemParentHierarchy().getNONDEPENDENTPRIMARYSYSTEM() != null) {
			system.setHierarchyPrimarySystemCode(systemEquipment.getSystemParentHierarchy()
					.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
			system.setHierarchyPrimarySystemDesc(systemEquipment.getSystemParentHierarchy()
					.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
			system.setHierarchyPrimarySystemDependent(false);
			system.setHierarchyPrimarySystemCostRollUp(decodeBoolean(
					systemEquipment.getSystemParentHierarchy().getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP()));
		}

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
		if (isNotEmpty(systemParam.getHierarchyLocationCode()) || isNotEmpty(systemParam.getHierarchyPrimarySystemCode())) {
			populateSystemHierarchy(context, systemParam, systemInfor);
		}

	}

	private void populateSystemHierarchy(InforContext context, Equipment systemParam, SystemEquipment systemInfor) {
		SystemParentHierarchy systemParentHierarchy = new SystemParentHierarchy();

		systemParentHierarchy.setSYSTEMID(new EQUIPMENTID_Type());
		systemParentHierarchy.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context));
		systemParentHierarchy.getSYSTEMID().setEQUIPMENTCODE(systemParam.getCode());
		systemParentHierarchy.setTYPE(new TYPE_Type());
		systemParentHierarchy.getTYPE().setTYPECODE("S");

		// HIERARCHY - PRIMARY SYSTEM
		if (tools.getDataTypeTools().isNotEmpty(systemParam.getHierarchyPrimarySystemCode())) {

			if (systemParam.getHierarchyPrimarySystemDependent() == null || !systemParam.getHierarchyPrimarySystemDependent()) {
				systemParam.setHierarchyAssetDependent(false);
			}
			// System
			EQUIPMENTID_Type hierarchySystem = new EQUIPMENTID_Type();
			hierarchySystem.setORGANIZATIONID(tools.getOrganization(context));
			hierarchySystem.setEQUIPMENTCODE(systemParam.getHierarchyPrimarySystemCode());
			// System dependent
			if (systemParam.getHierarchyPrimarySystemDependent() != null && systemParam.getHierarchyPrimarySystemDependent()) {
				SYSTEMPARENT_Type systemType = new SYSTEMPARENT_Type();
				systemType.setSYSTEMID(hierarchySystem);
				systemType.setCOSTROLLUP(encodeBoolean(systemParam.getHierarchyPrimarySystemCostRollUp(), BooleanType.TRUE_FALSE));
				systemParentHierarchy.setDEPENDENTPRIMARYSYSTEM(systemType);

				// Check for location
				if (tools.getDataTypeTools().isNotEmpty(systemParam.getHierarchyLocationCode())) {
					systemParentHierarchy.setLOCATIONID(new LOCATIONID_Type());
					systemParentHierarchy.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
					systemParentHierarchy.getLOCATIONID().setLOCATIONCODE(systemParam.getHierarchyLocationCode());
				}

			} else {
				// Non Dependent system
				SYSTEMPARENT_Type systemType = new SYSTEMPARENT_Type();
				systemType.setSYSTEMID(hierarchySystem);
				systemType.setCOSTROLLUP(encodeBoolean(systemParam.getHierarchyPrimarySystemCostRollUp(), BooleanType.TRUE_FALSE));
				systemParentHierarchy.setNONDEPENDENTPRIMARYSYSTEM(systemType);

				// Check for location
				if (tools.getDataTypeTools().isNotEmpty(systemParam.getHierarchyLocationCode())) {
					// Dependent location
					systemParentHierarchy.setDEPENDENTLOCATION(new LOCATIONPARENT_Type());
					systemParentHierarchy.getDEPENDENTLOCATION().setLOCATIONID(new LOCATIONID_Type());
					systemParentHierarchy.getDEPENDENTLOCATION().getLOCATIONID()
							.setORGANIZATIONID(tools.getOrganization(context));
					systemParentHierarchy.getDEPENDENTLOCATION().getLOCATIONID()
							.setLOCATIONCODE(systemParam.getHierarchyLocationCode());
				}
			}
		}
		// Just Locations
		else if (tools.getDataTypeTools().isNotEmpty(systemParam.getHierarchyLocationCode())) {
			// Dependent location
			systemParentHierarchy.setDEPENDENTLOCATION(new LOCATIONPARENT_Type());
			systemParentHierarchy.getDEPENDENTLOCATION().setLOCATIONID(new LOCATIONID_Type());
			systemParentHierarchy.getDEPENDENTLOCATION().getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
			systemParentHierarchy.getDEPENDENTLOCATION().getLOCATIONID()
					.setLOCATIONCODE(systemParam.getHierarchyLocationCode());
		}

		systemInfor.setSystemParentHierarchy(systemParentHierarchy);
	}
}