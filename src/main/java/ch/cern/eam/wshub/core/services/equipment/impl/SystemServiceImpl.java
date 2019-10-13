package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.SystemService;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.systemequipment_001.*;
import net.datastream.schemas.mp_entities.systemequipment_001.SystemEquipment.DORMANT;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0311_001.MP0311_AddSystemEquipment_001;
import net.datastream.schemas.mp_functions.mp0312_001.MP0312_GetSystemEquipment_001;
import net.datastream.schemas.mp_functions.mp0313_001.MP0313_SyncSystemEquipment_001;
import net.datastream.schemas.mp_functions.mp0314_001.MP0314_DeleteSystemEquipment_001;
import net.datastream.schemas.mp_functions.mp0329_001.MP0329_GetSystemParentHierarchy_001;
import net.datastream.schemas.mp_results.mp0311_001.MP0311_AddSystemEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0312_001.MP0312_GetSystemEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0329_001.MP0329_GetSystemParentHierarchy_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;

public class SystemServiceImpl implements SystemService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public SystemServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public Equipment readSystem(InforContext context, String systemCode) throws InforException {
		MP0312_GetSystemEquipment_001 getSystem = new MP0312_GetSystemEquipment_001();
		getSystem.setSYSTEMID(new EQUIPMENTID_Type());
		getSystem.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context));
		getSystem.getSYSTEMID().setEQUIPMENTCODE(systemCode);
		MP0312_GetSystemEquipment_001_Result getAssetResult = new MP0312_GetSystemEquipment_001_Result();

		if (context.getCredentials() != null)
			getAssetResult = inforws.getSystemEquipmentOp(getSystem, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		else {
			getAssetResult = inforws.getSystemEquipmentOp(getSystem, "*", null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		SystemEquipment systemEquipment = getAssetResult.getResultData().getSystemEquipment();

		Equipment system = new Equipment();

		if (systemEquipment.getSYSTEMID() != null) {
			system.setCode(systemEquipment.getSYSTEMID().getEQUIPMENTCODE());
			system.setDescription(systemEquipment.getSYSTEMID().getDESCRIPTION());
		}

		if (systemEquipment.getEQUIPMENTALIAS() != null) {
			system.setAlias(systemEquipment.getEQUIPMENTALIAS());
		}

		if (systemEquipment.getSTATUS() != null) {
			system.setStatusCode(systemEquipment.getSTATUS().getSTATUSCODE());
			system.setStatusDesc(systemEquipment.getSTATUS().getDESCRIPTION());
		}

		if (systemEquipment.getCLASSID() != null) {
			system.setClassCode(systemEquipment.getCLASSID().getCLASSCODE());
			system.setClassDesc(systemEquipment.getCLASSID().getDESCRIPTION());
		}

		if (systemEquipment.getCATEGORYID() != null) {
			system.setCategoryCode(systemEquipment.getCATEGORYID().getCATEGORYCODE());
			system.setCategoryDesc(systemEquipment.getCATEGORYID().getDESCRIPTION());
		}

		if (systemEquipment.getASSIGNEDTO() != null) {
			system.setAssignedTo(systemEquipment.getASSIGNEDTO().getPERSONCODE());
			system.setAssignedToDesc(tools.getFieldDescriptionsTools().readPersonDesc(system.getAssignedTo()));
		}

		if (systemEquipment.getTYPE() != null) {
			system.setTypeCode(systemEquipment.getTYPE().getTYPECODE());
			system.setTypeDesc(systemEquipment.getTYPE().getDESCRIPTION());
		}

		if (systemEquipment.getDEPARTMENTID() != null) {
			system.setDepartmentCode(systemEquipment.getDEPARTMENTID().getDEPARTMENTCODE());
			system.setDepartmentDesc(systemEquipment.getDEPARTMENTID().getDESCRIPTION());
		}

		if (systemEquipment.getCRITICALITYID() != null) {
			system.setCriticality(systemEquipment.getCRITICALITYID().getCRITICALITY());
		}

		if (systemEquipment.getCOMMISSIONDATE() != null) {
			system.setComissionDate(tools.getDataTypeTools().decodeInforDate(systemEquipment.getCOMMISSIONDATE()));
		}

		if (systemEquipment.getManufacturerInfo() != null) {
			system.setManufacturerCode(systemEquipment.getManufacturerInfo().getMANUFACTURERCODE());
			system.setSerialNumber(systemEquipment.getManufacturerInfo().getSERIALNUMBER());
			system.setModel(systemEquipment.getManufacturerInfo().getMODEL());
			system.setManufacturerDesc(tools.getFieldDescriptionsTools().readManufacturerDesc(system.getManufacturerCode()));
		}

		system.setCustomFields(tools.getCustomFieldsTools().readInforCustomFields(systemEquipment.getUSERDEFINEDAREA()));

		// USER DEFINED FIELDS
		system.setUserDefinedFields(tools.getUDFTools().readInforUserDefinedFields(systemEquipment.getUserDefinedFields()));

		// HIERARCHY
		MP0329_GetSystemParentHierarchy_001 getsystemh = new MP0329_GetSystemParentHierarchy_001();
		getsystemh.setSYSTEMID(systemEquipment.getSYSTEMID());
		MP0329_GetSystemParentHierarchy_001_Result gethresult;
		if (context.getCredentials() != null)
			gethresult = inforws.getSystemParentHierarchyOp(getsystemh, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		else {
			gethresult = inforws.getSystemParentHierarchyOp(getsystemh, "*", null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
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
			system.setHierarchyPrimarySystemDependent("true");
			system.setHierarchyPrimarySystemCostRollUp(
					systemEquipment.getSystemParentHierarchy().getDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP());
		}
		// Non Dependent primary system
		else if (systemEquipment.getSystemParentHierarchy().getNONDEPENDENTPRIMARYSYSTEM() != null) {
			system.setHierarchyPrimarySystemCode(systemEquipment.getSystemParentHierarchy()
					.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
			system.setHierarchyPrimarySystemDesc(systemEquipment.getSystemParentHierarchy()
					.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
			system.setHierarchyPrimarySystemDependent("false");
			system.setHierarchyPrimarySystemCostRollUp(
					systemEquipment.getSystemParentHierarchy().getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP());
		}

		if (systemEquipment.getVariables() != null) {
			system.setVariable1(systemEquipment.getVariables().getVARIABLE1());
			system.setVariable2(systemEquipment.getVariables().getVARIABLE2());
			system.setVariable3(systemEquipment.getVariables().getVARIABLE3());
			system.setVariable4(systemEquipment.getVariables().getVARIABLE4());
			system.setVariable5(systemEquipment.getVariables().getVARIABLE5());
			system.setVariable6(systemEquipment.getVariables().getVARIABLE6());
		}

		// IN PRODUCTION
		system.setInProduction(systemEquipment.getINPRODUCTION());

		return system;
	}

	public String updateSystem(InforContext context, Equipment systemParam) throws InforException {
		SystemEquipment systemEquipment = null;

		MP0312_GetSystemEquipment_001 getSystem = new MP0312_GetSystemEquipment_001();
		getSystem.setSYSTEMID(new EQUIPMENTID_Type());
		getSystem.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context));
		getSystem.getSYSTEMID().setEQUIPMENTCODE(systemParam.getCode());
		MP0312_GetSystemEquipment_001_Result getAssetResult = new MP0312_GetSystemEquipment_001_Result();

		if (context.getCredentials() != null)
			getAssetResult = inforws.getSystemEquipmentOp(getSystem, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		else {
			getAssetResult = inforws.getSystemEquipmentOp(getSystem, "*", null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		systemEquipment = getAssetResult.getResultData().getSystemEquipment();
		//
		if (systemParam.getClassCode() != null && (systemEquipment.getCLASSID() == null
				|| !systemParam.getClassCode().toUpperCase().equals(systemEquipment.getCLASSID().getCLASSCODE()))) {
			systemEquipment.setUSERDEFINEDAREA(
					tools.getCustomFieldsTools().getInforCustomFields(context, "OBJ", systemParam.getClassCode().toUpperCase()));
		}

		initializeSystemObject(systemEquipment, systemParam, context);

		MP0313_SyncSystemEquipment_001 syncPosition = new MP0313_SyncSystemEquipment_001();
		syncPosition.setSystemEquipment(systemEquipment);
		if (context.getCredentials() != null) {
			inforws.syncSystemEquipmentOp(syncPosition, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.syncSystemEquipmentOp(syncPosition, "*", null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		//TODO: Update CERN properties
		//equipmentOther.updateEquipmentCERNProperties(systemParam);
		return systemParam.getCode();
	}

	public String createSystem(InforContext context, Equipment systemParam) throws InforException {

		SystemEquipment systemEquipment = new SystemEquipment();
		//
		if (systemParam.getCustomFields() != null && systemParam.getCustomFields().length > 0) {
			if (systemParam.getClassCode() != null && !systemParam.getClassCode().trim().equals("")) {
				systemEquipment.setUSERDEFINEDAREA(
						tools.getCustomFieldsTools().getInforCustomFields(context, "OBJ", systemParam.getClassCode()));
			} else {
				systemEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(context, "OBJ", "*"));
			}
		}
		//
		systemEquipment.setUserDefinedFields(new UserDefinedFields());
		//
		initializeSystemObject(systemEquipment, systemParam, context);
		//
		MP0311_AddSystemEquipment_001 addPosition = new MP0311_AddSystemEquipment_001();
		addPosition.setSystemEquipment(systemEquipment);
		MP0311_AddSystemEquipment_001_Result result;
		if (context.getCredentials() != null) {
			result = inforws.addSystemEquipmentOp(addPosition, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			result = inforws.addSystemEquipmentOp(addPosition, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		return result.getResultData().getSYSTEMID().getEQUIPMENTCODE();
	}

	public String deleteSystem(InforContext context, String systemCode) throws InforException {

		MP0314_DeleteSystemEquipment_001 deleteSystem = new MP0314_DeleteSystemEquipment_001();
		deleteSystem.setSYSTEMID(new EQUIPMENTID_Type());
		deleteSystem.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context));
		deleteSystem.getSYSTEMID().setEQUIPMENTCODE(systemCode);

		if (context.getCredentials() != null) {
			inforws.deleteSystemEquipmentOp(deleteSystem, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.deleteSystemEquipmentOp(deleteSystem, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return systemCode;
	}

	private void initializeSystemObject(SystemEquipment systemInfor, Equipment systemParam, InforContext context) throws InforException {
		if (systemInfor.getSYSTEMID() == null) {
			systemInfor.setSYSTEMID(new EQUIPMENTID_Type());
			systemInfor.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context));
			systemInfor.getSYSTEMID().setEQUIPMENTCODE(systemParam.getCode());
		}

		if (systemParam.getAlias() != null) {
			systemInfor.setEQUIPMENTALIAS(systemParam.getAlias());
		}

		if (systemParam.getDescription() != null) {
			systemInfor.getSYSTEMID().setDESCRIPTION(systemParam.getDescription());
		}

		if (systemParam.getTypeCode() != null) {
			systemInfor.setTYPE(new TYPE_Type());
			systemInfor.getTYPE().setTYPECODE(systemParam.getTypeCode());
		}

		if (systemParam.getStatusCode() != null) {
			systemInfor.setSTATUS(new STATUS_Type());
			systemInfor.getSTATUS().setSTATUSCODE(systemParam.getStatusCode());
		}

		if (systemParam.getClassCode() != null) {
			if (systemParam.getClassCode().trim().equals("")) {
				systemInfor.setCLASSID(null);
			} else {
				systemInfor.setCLASSID(new CLASSID_Type());
				systemInfor.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
				systemInfor.getCLASSID().setCLASSCODE(systemParam.getClassCode());
			}
		}

		if (systemParam.getCategoryCode() != null) {
			systemInfor.setCATEGORYID(new CATEGORYID());
			systemInfor.getCATEGORYID().setCATEGORYCODE(systemParam.getCategoryCode());
		}

		if (systemParam.getComissionDate() != null) {
			systemInfor.setCOMMISSIONDATE(tools.getDataTypeTools().encodeInforDate(systemParam.getComissionDate(), "Commission Date"));
		}

		if (systemParam.getCostCode() != null) {
			systemInfor.setCOSTCODEID(new COSTCODEID_Type());
			systemInfor.getCOSTCODEID().setCOSTCODE(systemParam.getCostCode());
		}

		if (systemParam.getCriticality() != null) {
			systemInfor.setCRITICALITYID(new CRITICALITYID_Type());
			systemInfor.getCRITICALITYID().setCRITICALITY(systemParam.getCriticality());
		}

		if (systemParam.getDepartmentCode() != null) {
			systemInfor.setDEPARTMENTID(new DEPARTMENTID_Type());
			systemInfor.getDEPARTMENTID().setORGANIZATIONID(tools.getOrganization(context));
			systemInfor.getDEPARTMENTID().setDEPARTMENTCODE(systemParam.getDepartmentCode());
		}

		if (systemParam.getProfileCode() != null) {
			systemInfor.setPROFILEID(new OBJECT_Type());
			systemInfor.getPROFILEID().setORGANIZATIONID(tools.getOrganization(context));
			systemInfor.getPROFILEID().setOBJECTCODE(systemParam.getProfileCode());
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

		if (systemParam.getUpdateCount() != null) {
			systemInfor.setRecordid(Long.decode(systemParam.getUpdateCount()));
		}

		if (systemParam.getMeterUnit() != null) {
			systemInfor.setMETERUNIT(systemParam.getMeterUnit());
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
		if (systemParam.getAssignedTo() != null) {
			if (systemParam.getAssignedTo().trim().equals("")) {
				systemInfor.setASSIGNEDTO(null);
			} else {
				systemInfor.setASSIGNEDTO(new PERSONID_Type());
				systemInfor.getASSIGNEDTO().setPERSONCODE(systemParam.getAssignedTo());
			}
		}

		//
		if (systemParam.getcGMP() != null) {
			systemInfor.setCGMP(systemParam.getcGMP());
		}

		//
		if (systemParam.getDormantStart() != null || systemParam.getDormantEnd() != null
				|| systemParam.getDormantReusePeriod() != null) {
			systemInfor.setDORMANT(new DORMANT());
			systemInfor.getDORMANT().setDORMANTSTART(tools.getDataTypeTools().formatDate(systemParam.getDormantStart(), "Dormant Start"));
			systemInfor.getDORMANT().setDORMANTEND(tools.getDataTypeTools().formatDate(systemParam.getDormantEnd(), "Dormant End"));
			systemInfor.getDORMANT().setDORMANTREUSE(systemParam.getDormantReusePeriod());
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

		tools.getCustomFieldsTools().updateInforCustomFields(systemInfor.getUSERDEFINEDAREA(), systemParam.getCustomFields());

		//
		tools.getUDFTools().updateInforUserDefinedFields(systemInfor.getUserDefinedFields(), systemParam.getUserDefinedFields());

		// OUT OF SERVICE
		if (systemParam.getOutOfService() != null) {
			systemInfor.setOUTOFSERVICE(systemParam.getOutOfService());
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
		if (systemInfor.getTYPE().getTYPECODE().equals("S")) {
			populateSystemHierarchy(context, systemParam, systemInfor);
		}

		if (systemParam.getInProduction() != null) {
			systemInfor.setINPRODUCTION(systemParam.getInProduction());
		}

		// ORIGINAL RECEIPT DATE
		if (systemParam.getOriginalReceiptDate() != null) {
			systemInfor.setORIGINALRECEIPTDATE(
					tools.getDataTypeTools().formatDate(systemParam.getOriginalReceiptDate(), "Original Receipt Date"));
		}

		// SAFETY
		if (systemParam.getSafety() != null) {
			systemInfor.setSAFETY(systemParam.getSafety());
		}

		// ORIGINAL INSTALL DATE
		if (systemParam.getOriginalInstallDate() != null) {
			systemInfor.setORIGINALINSTALLDATE(
					tools.getDataTypeTools().encodeInforDate(systemParam.getOriginalInstallDate(), "Original Install Date"));
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
			if (tools.getDataTypeTools().isEmpty(systemParam.getHierarchyPrimarySystemDependent())) {
				systemParam.setHierarchyAssetDependent("FALSE");
			}
			// System
			EQUIPMENTID_Type hierarchySystem = new EQUIPMENTID_Type();
			hierarchySystem.setORGANIZATIONID(tools.getOrganization(context));
			hierarchySystem.setEQUIPMENTCODE(systemParam.getHierarchyPrimarySystemCode());
			// System dependent
			if (tools.getDataTypeTools().isTrueValue(systemParam.getHierarchyPrimarySystemDependent())) {
				SYSTEMPARENT_Type systemType = new SYSTEMPARENT_Type();
				systemType.setSYSTEMID(hierarchySystem);
				systemType.setCOSTROLLUP(systemParam.getHierarchyPrimarySystemCostRollUp());
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
				systemType.setCOSTROLLUP(systemParam.getHierarchyPrimarySystemCostRollUp());
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