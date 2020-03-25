package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.SystemService;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.systemequipment_001.*;
import net.datastream.schemas.mp_entities.systemequipment_001.SystemEquipment.DORMANT;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0310_001.MP0310_GetPositionEquipmentDefault_001;
import net.datastream.schemas.mp_functions.mp0311_001.MP0311_AddSystemEquipment_001;
import net.datastream.schemas.mp_functions.mp0312_001.MP0312_GetSystemEquipment_001;
import net.datastream.schemas.mp_functions.mp0313_001.MP0313_SyncSystemEquipment_001;
import net.datastream.schemas.mp_functions.mp0314_001.MP0314_DeleteSystemEquipment_001;
import net.datastream.schemas.mp_functions.mp0315_001.MP0315_GetSystemEquipmentDefault_001;
import net.datastream.schemas.mp_functions.mp0329_001.MP0329_GetSystemParentHierarchy_001;
import net.datastream.schemas.mp_results.mp0310_001.MP0310_GetPositionEquipmentDefault_001_Result;
import net.datastream.schemas.mp_results.mp0311_001.MP0311_AddSystemEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0312_001.MP0312_GetSystemEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0315_001.MP0315_GetSystemEquipmentDefault_001_Result;
import net.datastream.schemas.mp_results.mp0329_001.MP0329_GetSystemParentHierarchy_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;

public class SystemServiceImpl implements SystemService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public SystemServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
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

		return tools.getInforFieldTools().transformInforObject(new Equipment(), result.getResultData().getSystemEquipment());
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
		return result.getResultData().getSYSTEMID().getEQUIPMENTCODE();

	}

	public String deleteSystem(InforContext context, String systemCode) throws InforException {

		MP0314_DeleteSystemEquipment_001 deleteSystem = new MP0314_DeleteSystemEquipment_001();
		deleteSystem.setSYSTEMID(new EQUIPMENTID_Type());
		deleteSystem.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context));
		deleteSystem.getSYSTEMID().setEQUIPMENTCODE(systemCode);

		tools.performInforOperation(context, inforws::deleteSystemEquipmentOp, deleteSystem);

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

		if (systemParam.getManufacturerCode() != null || systemParam.getSerialNumber() != null
				|| systemParam.getModel() != null || systemParam.getRevision() != null
				|| systemParam.getxCoordinate() != null || systemParam.getyCoordinate() != null
				|| systemParam.getzCoordinate() != null) {
			if (systemInfor.getManufacturerInfo() == null) {
				systemInfor.setManufacturerInfo(new ManufacturerInfo());
			}
			if (systemParam.getManufacturerCode() != null) {
				systemInfor.getManufacturerInfo().setMANUFACTURERCODE(systemParam.getManufacturerCode().toUpperCase());
			}
			if (systemParam.getModel() != null) {
				systemInfor.getManufacturerInfo().setMODEL(systemParam.getModel());
			}
			if (systemParam.getRevision() != null) {
				systemInfor.getManufacturerInfo().setMODELREVISION(systemParam.getRevision());
			}
			if (systemParam.getSerialNumber() != null) {
				systemInfor.getManufacturerInfo().setSERIALNUMBER(systemParam.getSerialNumber());
			}
			if (systemParam.getxCoordinate() != null) {
				systemInfor.getManufacturerInfo()
						.setXCOORDINATE(tools.getDataTypeTools().encodeQuantity(systemParam.getxCoordinate(), "X-Coordinate"));
			}
			if (systemParam.getyCoordinate() != null) {
				systemInfor.getManufacturerInfo()
						.setYCOORDINATE(tools.getDataTypeTools().encodeQuantity(systemParam.getyCoordinate(), "Y-Coordinate"));
			}
			if (systemParam.getzCoordinate() != null) {
				systemInfor.getManufacturerInfo()
						.setZCOORDINATE(tools.getDataTypeTools().encodeQuantity(systemParam.getzCoordinate(), "Z-Coordiante"));
			}
		}

		// LINEAR REFERENCE
		if (systemParam.getLinearRefGeographicalRef() != null || systemParam.getLinearRefEquipmentLength() != null
				|| systemParam.getLinearRefEquipmentLengthUOM() != null || systemParam.getLinearRefPrecision() != null
				|| systemParam.getLinearRefUOM() != null) {
			systemInfor.setLINEARREFERENCEDETAILS(new LINEARREFERENCEDETAILS_Type());
			systemInfor.getLINEARREFERENCEDETAILS().setEQUIPMENTLENGTH(
					tools.getDataTypeTools().encodeQuantity(systemParam.getLinearRefEquipmentLength(), "Linear Ref. Equipment Length"));
			systemInfor.getLINEARREFERENCEDETAILS().setEQUIPMENTLENGTHUOM(systemParam.getLinearRefEquipmentLengthUOM());
			systemInfor.getLINEARREFERENCEDETAILS().setGEOGRAPHICALREFERENCE(systemParam.getLinearRefGeographicalRef());
			systemInfor.getLINEARREFERENCEDETAILS().setLINEARREFPRECISION(
					tools.getDataTypeTools().encodeBigInteger(systemParam.getLinearRefPrecision(), "Linear Ref. Precision"));
			systemInfor.getLINEARREFERENCEDETAILS().setLINEARREFUOM(systemParam.getLinearRefUOM());
		}

		//
		systemInfor.setVariables(new Variables());
		if (systemParam.getVariable1() != null) {
			systemInfor.getVariables().setVARIABLE1(systemParam.getVariable1());
		}
		if (systemParam.getVariable2() != null) {
			systemInfor.getVariables().setVARIABLE2(systemParam.getVariable2());
		}
		if (systemParam.getVariable3() != null) {
			systemInfor.getVariables().setVARIABLE3(systemParam.getVariable3());
		}
		if (systemParam.getVariable4() != null) {
			systemInfor.getVariables().setVARIABLE4(systemParam.getVariable4());
		}
		if (systemParam.getVariable5() != null) {
			systemInfor.getVariables().setVARIABLE5(systemParam.getVariable5());
		}
		if (systemParam.getVariable6() != null) {
			systemInfor.getVariables().setVARIABLE6(systemParam.getVariable6());
		}

		// FACILITY DETAILS
		if (systemParam.getCostOfNeededRepairs() != null || systemParam.getReplacementValue() != null
				|| systemParam.getFacilityConditionIndex() != null || systemParam.getServiceLifetime() != null
				|| systemParam.getYearBuilt() != null) {
			if (systemInfor.getFacilityConditionIndex() == null) {
				systemInfor.setFacilityConditionIndex(new FacilityConditionIndex());
			}

			if (systemParam.getCostOfNeededRepairs() != null) {
				systemInfor.getFacilityConditionIndex().setCOSTOFNEEDEDREPAIRS(
						tools.getDataTypeTools().encodeAmount(systemParam.getCostOfNeededRepairs(), "Cost of Needed Repairs"));
			}

			if (systemParam.getReplacementValue() != null) {
				systemInfor.getFacilityConditionIndex().setREPLACEMENTVALUE(
						tools.getDataTypeTools().encodeAmount(systemParam.getReplacementValue(), "Replacement Value"));
			}

			if (systemParam.getFacilityConditionIndex() != null) {
				systemInfor.getFacilityConditionIndex().setFACILITYCONDITIONINDEX(
						tools.getDataTypeTools().encodeAmount(systemParam.getFacilityConditionIndex(), "Facility Condition Index"));
			}

			if (systemParam.getServiceLifetime() != null) {
				systemInfor.getFacilityConditionIndex()
						.setSERVICELIFE(tools.getDataTypeTools().encodeQuantity(systemParam.getServiceLifetime(), "Service Life Time"));
			}

			if (systemParam.getYearBuilt() != null) {
				systemInfor.getFacilityConditionIndex()
						.setYEARBUILT(tools.getDataTypeTools().encodeQuantity(systemParam.getYearBuilt(), "Service Life Time"));
			}
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