package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.PositionService;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.positionequipment_001.ManufacturerInfo;
import net.datastream.schemas.mp_entities.positionequipment_001.PositionEquipment;
import net.datastream.schemas.mp_entities.positionequipment_001.PositionEquipment.DORMANT;
import net.datastream.schemas.mp_entities.positionequipment_001.Variables;
import net.datastream.schemas.mp_entities.positionhierarchy_002.*;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0306_001.MP0306_AddPositionEquipment_001;
import net.datastream.schemas.mp_functions.mp0307_001.MP0307_GetPositionEquipment_001;
import net.datastream.schemas.mp_functions.mp0308_001.MP0308_SyncPositionEquipment_001;
import net.datastream.schemas.mp_functions.mp0309_001.MP0309_DeletePositionEquipment_001;
import net.datastream.schemas.mp_functions.mp0328_002.MP0328_GetPositionParentHierarchy_002;
import net.datastream.schemas.mp_results.mp0306_001.MP0306_AddPositionEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0307_001.MP0307_GetPositionEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0328_002.MP0328_GetPositionParentHierarchy_002_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;

public class PositionServiceImpl implements PositionService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public PositionServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public String createPosition(InforContext context, Equipment positionParam) throws InforException {

		PositionEquipment positionEquipment = new PositionEquipment();
		//
		if (positionParam.getCustomFields() != null && positionParam.getCustomFields().length > 0) {
			if (positionParam.getClassCode() != null && !positionParam.getClassCode().trim().equals("")) {
				positionEquipment.setUSERDEFINEDAREA(
						tools.getCustomFieldsTools().getInforCustomFields(context, "OBJ", positionParam.getClassCode()));
			} else {
				positionEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(context, "OBJ", "*"));
			}
		}
		//
		positionEquipment.setUserDefinedFields(new net.datastream.schemas.mp_entities.positionequipment_001.UserDefinedFields());
		//
		initializePositionObject(context, positionEquipment, positionParam, true);
		//
		MP0306_AddPositionEquipment_001 addPosition = new MP0306_AddPositionEquipment_001();
		addPosition.setPositionEquipment(positionEquipment);
		MP0306_AddPositionEquipment_001_Result result;

		if (context.getCredentials() != null) {
			result = inforws.addPositionEquipmentOp(addPosition, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			result = inforws.addPositionEquipmentOp(addPosition, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		//TODO Update CERN properties
		//equipmentOther.updateEquipmentCERNProperties(positionParam);
		return result.getResultData().getPOSITIONID().getEQUIPMENTCODE();
	}

	public String deletePosition(InforContext context, String positionCode) throws InforException {

		MP0309_DeletePositionEquipment_001 deletePosition = new MP0309_DeletePositionEquipment_001();
		deletePosition.setPOSITIONID(new EQUIPMENTID_Type());
		deletePosition.getPOSITIONID().setORGANIZATIONID(tools.getOrganization(context));
		deletePosition.getPOSITIONID().setEQUIPMENTCODE(positionCode);

		if (context.getCredentials() != null) {
			inforws.deletePositionEquipmentOp(deletePosition, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.deletePositionEquipmentOp(deletePosition, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return positionCode;
	}

	private PositionParentHierarchy readInforPositionParentHierarchy(InforContext context, String assetCode) throws InforException {

		MP0328_GetPositionParentHierarchy_002 getpositionph = new MP0328_GetPositionParentHierarchy_002();
		getpositionph.setPOSITIONID(new EQUIPMENTID_Type());
		getpositionph.getPOSITIONID().setORGANIZATIONID(tools.getOrganization(context));
		getpositionph.getPOSITIONID().setEQUIPMENTCODE(assetCode);

		MP0328_GetPositionParentHierarchy_002_Result result;
		if (context.getCredentials() != null) {
			result = inforws.getPositionParentHierarchyOp(getpositionph, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			result = inforws.getPositionParentHierarchyOp(getpositionph, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return result.getResultData().getPositionParentHierarchy();
	}

	public Equipment readPosition(InforContext context, String positionCode) throws InforException {

		MP0307_GetPositionEquipment_001 getPosition = new MP0307_GetPositionEquipment_001();
		getPosition.setPOSITIONID(new EQUIPMENTID_Type());
		getPosition.getPOSITIONID().setORGANIZATIONID(tools.getOrganization(context));
		getPosition.getPOSITIONID().setEQUIPMENTCODE(positionCode);
		MP0307_GetPositionEquipment_001_Result getPositionResult = new MP0307_GetPositionEquipment_001_Result();

		if (context.getCredentials() != null) {
			getPositionResult = inforws.getPositionEquipmentOp(getPosition, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			getPositionResult = inforws.getPositionEquipmentOp(getPosition, tools.getOrganizationCode(context), null,
					null, new Holder<SessionType>(tools.createInforSession(context)), null,
					tools.getTenant(context));
		}
		PositionEquipment positionEquipment = getPositionResult.getResultData().getPositionEquipment();

		Equipment position = new Equipment();
		// POSITION ID
		if (positionEquipment.getPOSITIONID() != null) {
			position.setCode(positionEquipment.getPOSITIONID().getEQUIPMENTCODE());
			position.setDescription(positionEquipment.getPOSITIONID().getDESCRIPTION());
		}

		if (positionEquipment.getEQUIPMENTALIAS() != null) {
			position.setAlias(positionEquipment.getEQUIPMENTALIAS());
		}

		// STATUS ID
		if (positionEquipment.getSTATUS() != null) {
			position.setStatusCode(positionEquipment.getSTATUS().getSTATUSCODE());
			position.setStatusDesc(positionEquipment.getSTATUS().getDESCRIPTION());
		}

		// TYPE
		if (positionEquipment.getTYPE() != null) {
			position.setTypeCode(positionEquipment.getTYPE().getTYPECODE());
			position.setTypeDesc(positionEquipment.getTYPE().getDESCRIPTION());
		}

		// CLASS ID
		if (positionEquipment.getCLASSID() != null) {
			position.setClassCode(positionEquipment.getCLASSID().getCLASSCODE());
			position.setClassDesc(positionEquipment.getCLASSID().getDESCRIPTION());
		}

		// CATEGORY ID
		if (positionEquipment.getCATEGORYID() != null) {
			position.setCategoryCode(positionEquipment.getCATEGORYID().getCATEGORYCODE());
			position.setCategoryDesc(positionEquipment.getCATEGORYID().getDESCRIPTION());
		}

		// ASSIGNED TO
		if (positionEquipment.getASSIGNEDTO() != null) {
			position.setAssignedTo(positionEquipment.getASSIGNEDTO().getPERSONCODE());
			position.setAssignedToDesc(tools.getFieldDescriptionsTools().readPersonDesc(position.getAssignedTo()));
		}

		// DEPARTMENT
		if (positionEquipment.getDEPARTMENTID() != null) {
			position.setDepartmentCode(positionEquipment.getDEPARTMENTID().getDEPARTMENTCODE());
			position.setDepartmentDesc(positionEquipment.getDEPARTMENTID().getDESCRIPTION());
		}

		// CRITICATLI YDI
		if (positionEquipment.getCRITICALITYID() != null) {
			position.setCriticality(positionEquipment.getCRITICALITYID().getCRITICALITY());
		}

		// COMISSION DATE
		if (positionEquipment.getCOMMISSIONDATE() != null) {
			position.setComissionDate(tools.getDataTypeTools().decodeInforDate(positionEquipment.getCOMMISSIONDATE()));
		}

		// MANUFACTURER
		if (positionEquipment.getManufacturerInfo() != null) {
			position.setManufacturerCode(positionEquipment.getManufacturerInfo().getMANUFACTURERCODE());
			position.setSerialNumber(positionEquipment.getManufacturerInfo().getSERIALNUMBER());
			position.setModel(positionEquipment.getManufacturerInfo().getMODEL());
			position.setManufacturerDesc(tools.getFieldDescriptionsTools().readManufacturerDesc(position.getManufacturerCode()));
		}

		// CUSTOM FIELDS
		position.setCustomFields(tools.getCustomFieldsTools().readInforCustomFields(positionEquipment.getUSERDEFINEDAREA()));

		// USER DEFINED FIELDS
		position.setUserDefinedFields(tools.getUDFTools().readInforUserDefinedFields(positionEquipment.getUserDefinedFields()));

		// HIERARCHY
		positionEquipment.setPositionParentHierarchy(readInforPositionParentHierarchy(context, positionCode));

		if (positionEquipment.getPositionParentHierarchy().getLOCATIONID() != null) {
			position.setHierarchyLocationCode(
					positionEquipment.getPositionParentHierarchy().getLOCATIONID().getLOCATIONCODE());
			position.setHierarchyLocationDesc(
					positionEquipment.getPositionParentHierarchy().getLOCATIONID().getDESCRIPTION());
		}

		if (positionEquipment.getPositionParentHierarchy().getPositionDependency() != null) {
			//
			PositionDependency positionDep = positionEquipment.getPositionParentHierarchy().getPositionDependency();

			// Dependent position
			position.setHierarchyPositionCode(positionDep.getDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
			position.setHierarchyPositionDesc(positionDep.getDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
			position.setHierarchyPositionDependent("true");
			position.setHierarchyPositionCostRollUp(positionDep.getDEPENDENTPOSITION().getCOSTROLLUP());

			// Non dependent asset
			if (positionDep.getNONDEPENDENTASSET() != null) {
				//
				position.setHierarchyAssetCode(positionDep.getNONDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
				position.setHierarchyAssetDesc(positionDep.getNONDEPENDENTASSET().getASSETID().getDESCRIPTION());
				position.setHierarchyAssetCostRollUp(positionDep.getNONDEPENDENTASSET().getCOSTROLLUP());
				position.setHierarchyAssetDependent("false");
			}

			// Non dependent system
			if (positionDep.getNONDEPENDENTPRIMARYSYSTEM() != null) {
				//
				position.setHierarchyPrimarySystemCode(
						positionDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
				position.setHierarchyPrimarySystemDesc(
						positionDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
				position.setHierarchyPrimarySystemCostRollUp(
						positionDep.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP());
				position.setHierarchyPrimarySystemDependent("false");
			}

		} else if (positionEquipment.getPositionParentHierarchy().getAssetDependency() != null) {
			AssetDependency assetDep = positionEquipment.getPositionParentHierarchy().getAssetDependency();

			// Dependent Asset
			position.setHierarchyAssetCode(assetDep.getDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
			position.setHierarchyAssetDesc(assetDep.getDEPENDENTASSET().getASSETID().getDESCRIPTION());
			position.setHierarchyAssetCostRollUp(assetDep.getDEPENDENTASSET().getCOSTROLLUP());
			position.setHierarchyAssetDependent("true");

			// Non dependent position
			if (assetDep.getNONDEPENDENTPOSITION() != null) {
				position.setHierarchyPositionCode(
						assetDep.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
				position.setHierarchyPositionDesc(assetDep.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
				position.setHierarchyPositionDependent("false");
				position.setHierarchyPositionCostRollUp(assetDep.getNONDEPENDENTPOSITION().getCOSTROLLUP());
			}

			// Non dependent system
			if (assetDep.getNONDEPENDENTPRIMARYSYSTEM() != null) {
				position.setHierarchyPrimarySystemCode(
						assetDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
				position.setHierarchyPrimarySystemDesc(
						assetDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
				position.setHierarchyPrimarySystemCostRollUp(assetDep.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP());
				position.setHierarchyPrimarySystemDependent("false");
			}

		} else if (positionEquipment.getPositionParentHierarchy().getSystemDependency() != null) {
			SystemDependency systemDep = positionEquipment.getPositionParentHierarchy().getSystemDependency();

			// Dependent System
			position.setHierarchyPrimarySystemCode(systemDep.getDEPENDENTSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
			position.setHierarchyPrimarySystemDesc(systemDep.getDEPENDENTSYSTEM().getSYSTEMID().getDESCRIPTION());
			position.setHierarchyPrimarySystemCostRollUp(systemDep.getDEPENDENTSYSTEM().getCOSTROLLUP());
			position.setHierarchyPrimarySystemDependent("true");

			// Non dependent position
			if (systemDep.getNONDEPENDENTPOSITION() != null) {
				position.setHierarchyPositionCode(
						systemDep.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
				position.setHierarchyPositionDesc(systemDep.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
				position.setHierarchyPositionDependent("false");
				position.setHierarchyPositionCostRollUp(systemDep.getNONDEPENDENTPOSITION().getCOSTROLLUP());
			}

			// Non dependent asset
			if (systemDep.getNONDEPENDENTASSET() != null) {
				//
				position.setHierarchyAssetCode(systemDep.getNONDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
				position.setHierarchyAssetDesc(systemDep.getNONDEPENDENTASSET().getASSETID().getDESCRIPTION());
				position.setHierarchyAssetCostRollUp(systemDep.getNONDEPENDENTASSET().getCOSTROLLUP());
				position.setHierarchyAssetDependent("false");
			}

		} else if (positionEquipment.getPositionParentHierarchy().getNonDependentParents() != null) {
			NonDependentParents_Type nonDepParents = positionEquipment.getPositionParentHierarchy()
					.getNonDependentParents();

			// Non dependent asset
			if (nonDepParents.getNONDEPENDENTASSET() != null) {
				position.setHierarchyAssetCode(nonDepParents.getNONDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
				position.setHierarchyAssetDesc(nonDepParents.getNONDEPENDENTASSET().getASSETID().getDESCRIPTION());
				position.setHierarchyAssetCostRollUp(nonDepParents.getNONDEPENDENTASSET().getCOSTROLLUP());
				position.setHierarchyAssetDependent("false");
			}

			// Non dependent position
			if (nonDepParents.getNONDEPENDENTPOSITION() != null) {
				position.setHierarchyPositionCode(
						nonDepParents.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
				position.setHierarchyPositionDesc(
						nonDepParents.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
				position.setHierarchyPositionDependent("false");
				position.setHierarchyPositionCostRollUp(nonDepParents.getNONDEPENDENTPOSITION().getCOSTROLLUP());
			}

			// Non dependent system
			if (nonDepParents.getNONDEPENDENTPRIMARYSYSTEM() != null) {
				position.setHierarchyPrimarySystemCode(
						nonDepParents.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
				position.setHierarchyPrimarySystemDesc(
						nonDepParents.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
				position.setHierarchyPrimarySystemCostRollUp(
						nonDepParents.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP());
				position.setHierarchyPrimarySystemDependent("false");
			}

		} else if (positionEquipment.getPositionParentHierarchy().getLocationDependency() != null) {
			LocationDependency locationDep = positionEquipment.getPositionParentHierarchy().getLocationDependency();

			// Location dependent
			position.setHierarchyLocationCode(locationDep.getDEPENDENTLOCATION().getLOCATIONID().getLOCATIONCODE());
			position.setHierarchyLocationDesc(locationDep.getDEPENDENTLOCATION().getLOCATIONID().getDESCRIPTION());

			// Non dependent asset
			if (locationDep.getNONDEPENDENTASSET() != null) {
				position.setHierarchyAssetCode(locationDep.getNONDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
				position.setHierarchyAssetDesc(locationDep.getNONDEPENDENTASSET().getASSETID().getDESCRIPTION());
				position.setHierarchyAssetCostRollUp(locationDep.getNONDEPENDENTASSET().getCOSTROLLUP());
				position.setHierarchyAssetDependent("false");
			}

			// Non dependent position
			if (locationDep.getNONDEPENDENTPOSITION() != null) {
				position.setHierarchyPositionCode(
						locationDep.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
				position.setHierarchyPositionDesc(
						locationDep.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
				position.setHierarchyPositionDependent("false");
				position.setHierarchyPositionCostRollUp(locationDep.getNONDEPENDENTPOSITION().getCOSTROLLUP());
			}

			// Non dependent system
			if (locationDep.getNONDEPENDENTPRIMARYSYSTEM() != null) {
				position.setHierarchyPrimarySystemCode(
						locationDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
				position.setHierarchyPrimarySystemDesc(
						locationDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
				position.setHierarchyPrimarySystemCostRollUp(
						locationDep.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP());
				position.setHierarchyPrimarySystemDependent("false");
			}
		}

		if (positionEquipment.getVariables() != null) {
			position.setVariable1(positionEquipment.getVariables().getVARIABLE1());
			position.setVariable2(positionEquipment.getVariables().getVARIABLE2());
			position.setVariable3(positionEquipment.getVariables().getVARIABLE3());
			position.setVariable4(positionEquipment.getVariables().getVARIABLE4());
			position.setVariable5(positionEquipment.getVariables().getVARIABLE5());
			position.setVariable6(positionEquipment.getVariables().getVARIABLE6());
		}

		// IN PRODUCTION
		position.setInProduction(positionEquipment.getINPRODUCTION());

		return position;
	}

	private PositionEquipment readInforPosition(InforContext context, String positionCode) throws InforException {
		MP0307_GetPositionEquipment_001 getPosition = new MP0307_GetPositionEquipment_001();
		getPosition.setPOSITIONID(new EQUIPMENTID_Type());
		getPosition.getPOSITIONID().setORGANIZATIONID(tools.getOrganization(context));
		getPosition.getPOSITIONID().setEQUIPMENTCODE(positionCode);
		MP0307_GetPositionEquipment_001_Result getAssetResult = new MP0307_GetPositionEquipment_001_Result();

		if (context.getCredentials() != null) {
			getAssetResult = inforws.getPositionEquipmentOp(getPosition, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			getAssetResult = inforws.getPositionEquipmentOp(getPosition, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		return getAssetResult.getResultData().getPositionEquipment();

	}

	public String updatePosition(InforContext context, Equipment positionParam) throws InforException {
		// Read position
		PositionEquipment positionEquipment = readInforPosition(context, positionParam.getCode());
		//
		//
		//
		if (positionParam.getClassCode() != null && (positionEquipment.getCLASSID() == null
				|| !positionParam.getClassCode().toUpperCase().equals(positionEquipment.getCLASSID().getCLASSCODE()))) {
			positionEquipment.setUSERDEFINEDAREA(
					tools.getCustomFieldsTools().getInforCustomFields(context, "OBJ", positionParam.getClassCode().toUpperCase()));
		}
		// Update 'positionEquipment' with supplied values
		initializePositionObject(context, positionEquipment, positionParam, false);
		// Update it
		MP0308_SyncPositionEquipment_001 syncPosition = new MP0308_SyncPositionEquipment_001();
		syncPosition.setPositionEquipment(positionEquipment);
		if (context.getCredentials() != null) {
			inforws.syncPositionEquipmentOp(syncPosition, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.syncPositionEquipmentOp(syncPosition, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		//TODO Update CERN properties
		//equipmentOther.updateEquipmentCERNProperties(positionParam);
		return positionParam.getCode();
	}

	private void initializePositionObject(InforContext context, PositionEquipment positionInfor, Equipment positionParam, boolean creationRequest) throws InforException {
		// == null means Position creation
		if (positionInfor.getPOSITIONID() == null) {
			positionInfor.setPOSITIONID(new EQUIPMENTID_Type());
			positionInfor.getPOSITIONID().setORGANIZATIONID(tools.getOrganization(context));
			positionInfor.getPOSITIONID().setEQUIPMENTCODE(positionParam.getCode().toUpperCase().trim());
		}

		if (positionParam.getAlias() != null) {
			positionInfor.setEQUIPMENTALIAS(positionParam.getAlias());
		}

		if (positionParam.getDescription() != null) {
			positionInfor.getPOSITIONID().setDESCRIPTION(positionParam.getDescription());
		}

		if (positionParam.getStatusCode() != null) {
			positionInfor.setSTATUS(new STATUS_Type());
			positionInfor.getSTATUS().setSTATUSCODE(positionParam.getStatusCode().toUpperCase().trim());
		}

		if (positionParam.getClassCode() != null) {
			if (positionParam.getClassCode().trim().equals("")) {
				positionInfor.setCLASSID(null);
			} else {
				positionInfor.setCLASSID(new CLASSID_Type());
				positionInfor.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
				positionInfor.getCLASSID().setCLASSCODE(positionParam.getClassCode().toUpperCase().trim());
			}
		}

		if (positionParam.getCategoryCode() != null) {
			positionInfor.setCATEGORYID(new CATEGORYID());
			positionInfor.getCATEGORYID().setCATEGORYCODE(positionParam.getCategoryCode().toUpperCase().trim());
		}

		if (positionParam.getComissionDate() != null) {
			positionInfor
					.setCOMMISSIONDATE(tools.getDataTypeTools().encodeInforDate(positionParam.getComissionDate(), "Comissioning Date"));
		}

		if (positionParam.getCostCode() != null) {
			positionInfor.setCOSTCODEID(new COSTCODEID_Type());
			positionInfor.getCOSTCODEID().setCOSTCODE(positionParam.getCostCode().toUpperCase().trim());
		}

		if (positionParam.getCriticality() != null) {
			positionInfor.setCRITICALITYID(new CRITICALITYID_Type());
			positionInfor.getCRITICALITYID().setCRITICALITY(positionParam.getCriticality().toUpperCase().trim());
		}

		if (positionParam.getDepartmentCode() != null) {
			positionInfor.setDEPARTMENTID(new DEPARTMENTID_Type());
			positionInfor.getDEPARTMENTID().setORGANIZATIONID(tools.getOrganization(context));
			positionInfor.getDEPARTMENTID().setDEPARTMENTCODE(positionParam.getDepartmentCode().toUpperCase().trim());
		}

		if (positionParam.getProfileCode() != null) {
			positionInfor.setPROFILEID(new OBJECT_Type());
			positionInfor.getPROFILEID().setORGANIZATIONID(tools.getOrganization(context));
			positionInfor.getPROFILEID().setOBJECTCODE(positionParam.getProfileCode());
		}

		if (positionParam.getManufacturerCode() != null || positionParam.getSerialNumber() != null
				|| positionParam.getModel() != null || positionParam.getRevision() != null
				|| positionParam.getxCoordinate() != null || positionParam.getyCoordinate() != null
				|| positionParam.getzCoordinate() != null) {
			if (positionInfor.getManufacturerInfo() == null) {
				positionInfor.setManufacturerInfo(new ManufacturerInfo());
			}
			if (positionParam.getManufacturerCode() != null) {
				positionInfor.getManufacturerInfo()
						.setMANUFACTURERCODE(positionParam.getManufacturerCode().toUpperCase());
			}
			if (positionParam.getModel() != null) {
				positionInfor.getManufacturerInfo().setMODEL(positionParam.getModel());
			}
			if (positionParam.getRevision() != null) {
				positionInfor.getManufacturerInfo().setMODELREVISION(positionParam.getRevision());
			}
			if (positionParam.getSerialNumber() != null) {
				positionInfor.getManufacturerInfo().setSERIALNUMBER(positionParam.getSerialNumber());
			}
			if (positionParam.getxCoordinate() != null) {
				positionInfor.getManufacturerInfo()
						.setXCOORDINATE(tools.getDataTypeTools().encodeQuantity(positionParam.getxCoordinate(), "X-Coordinate"));
			}
			if (positionParam.getyCoordinate() != null) {
				positionInfor.getManufacturerInfo()
						.setYCOORDINATE(tools.getDataTypeTools().encodeQuantity(positionParam.getyCoordinate(), "Y-Coordinate"));
			}
			if (positionParam.getzCoordinate() != null) {
				positionInfor.getManufacturerInfo()
						.setZCOORDINATE(tools.getDataTypeTools().encodeQuantity(positionParam.getzCoordinate(), "Z-Coordiante"));
			}
		}

		if (positionParam.getUpdateCount() != null) {
			positionInfor.setRecordid(Long.decode(positionParam.getUpdateCount()));
		}

		if (positionParam.getMeterUnit() != null) {
			positionInfor.setMETERUNIT(positionParam.getMeterUnit());
		}

		if (positionParam.getEquipmentValue() != null) {
			// positionInfor.sete
		}

		// LINEAR REFERENCE
		if (positionParam.getLinearRefGeographicalRef() != null || positionParam.getLinearRefEquipmentLength() != null
				|| positionParam.getLinearRefEquipmentLengthUOM() != null
				|| positionParam.getLinearRefPrecision() != null || positionParam.getLinearRefUOM() != null) {
			positionInfor.setLINEARREFERENCEDETAILS(new LINEARREFERENCEDETAILS_Type());
			positionInfor.getLINEARREFERENCEDETAILS().setEQUIPMENTLENGTH(
					tools.getDataTypeTools().encodeQuantity(positionParam.getLinearRefEquipmentLength(), "Linear Ref. Equipment Length"));
			positionInfor.getLINEARREFERENCEDETAILS()
					.setEQUIPMENTLENGTHUOM(positionParam.getLinearRefEquipmentLengthUOM());
			positionInfor.getLINEARREFERENCEDETAILS()
					.setGEOGRAPHICALREFERENCE(positionParam.getLinearRefGeographicalRef());
			positionInfor.getLINEARREFERENCEDETAILS().setLINEARREFPRECISION(
					tools.getDataTypeTools().encodeBigInteger(positionParam.getLinearRefPrecision(), "Linear Ref. Precision"));
			positionInfor.getLINEARREFERENCEDETAILS().setLINEARREFUOM(positionParam.getLinearRefUOM());
		}

		//
		if (positionParam.getAssignedTo() != null) {
			if (positionParam.getAssignedTo().trim().equals("")) {
				positionInfor.setASSIGNEDTO(null);
			} else {
				positionInfor.setASSIGNEDTO(new PERSONID_Type());
				positionInfor.getASSIGNEDTO().setPERSONCODE(positionParam.getAssignedTo());
			}
		}

		//
		if (positionParam.getcGMP() != null) {
			positionInfor.setCGMP(positionParam.getcGMP());
		}

		//
		if (positionParam.getDormantStart() != null || positionParam.getDormantEnd() != null
				|| positionParam.getDormantReusePeriod() != null) {
			positionInfor.setDORMANT(new DORMANT());
			positionInfor.getDORMANT()
					.setDORMANTSTART(tools.getDataTypeTools().formatDate(positionParam.getDormantStart(), "Dormant Start"));
			positionInfor.getDORMANT().setDORMANTEND(tools.getDataTypeTools().formatDate(positionParam.getDormantEnd(), "Dormant End"));
			positionInfor.getDORMANT().setDORMANTREUSE(positionParam.getDormantReusePeriod());
		}

		//
		if (positionInfor.getVariables() == null) {
			positionInfor.setVariables(new Variables());
		}

		if (positionParam.getVariable1() != null) {
			positionInfor.getVariables().setVARIABLE1(positionParam.getVariable1());
		}
		if (positionParam.getVariable2() != null) {
			positionInfor.getVariables().setVARIABLE2(positionParam.getVariable2());
		}
		if (positionParam.getVariable3() != null) {
			positionInfor.getVariables().setVARIABLE3(positionParam.getVariable3());
		}
		if (positionParam.getVariable4() != null) {
			positionInfor.getVariables().setVARIABLE4(positionParam.getVariable4());
		}
		if (positionParam.getVariable5() != null) {
			positionInfor.getVariables().setVARIABLE5(positionParam.getVariable5());
		}
		if (positionParam.getVariable6() != null) {
			positionInfor.getVariables().setVARIABLE6(positionParam.getVariable6());
		}

		//
		tools.getCustomFieldsTools().updateInforCustomFields(positionInfor.getUSERDEFINEDAREA(), positionParam.getCustomFields());

		//
		tools.getUDFTools().updateInforUserDefinedFields(positionInfor.getUserDefinedFields(),
				positionParam.getUserDefinedFields());

		// FACILITY DETAILS
		if (positionParam.getCostOfNeededRepairs() != null || positionParam.getReplacementValue() != null
				|| positionParam.getFacilityConditionIndex() != null || positionParam.getServiceLifetime() != null
				|| positionParam.getYearBuilt() != null) {
			if (positionInfor.getFacilityConditionIndex() == null) {
				positionInfor.setFacilityConditionIndex(
						new net.datastream.schemas.mp_entities.positionequipment_001.FacilityConditionIndex());
			}

			if (positionParam.getCostOfNeededRepairs() != null) {
				positionInfor.getFacilityConditionIndex().setCOSTOFNEEDEDREPAIRS(
						tools.getDataTypeTools().encodeAmount(positionParam.getCostOfNeededRepairs(), "Cost of Needed Repairs"));
			}

			if (positionParam.getReplacementValue() != null) {
				positionInfor.getFacilityConditionIndex().setREPLACEMENTVALUE(
						tools.getDataTypeTools().encodeAmount(positionParam.getReplacementValue(), "Replacement Value"));
			}

			if (positionParam.getFacilityConditionIndex() != null) {
				positionInfor.getFacilityConditionIndex().setFACILITYCONDITIONINDEX(
						tools.getDataTypeTools().encodeAmount(positionParam.getFacilityConditionIndex(), "Facility Condition Index"));
			}

			if (positionParam.getServiceLifetime() != null) {
				positionInfor.getFacilityConditionIndex()
						.setSERVICELIFE(tools.getDataTypeTools().encodeQuantity(positionParam.getServiceLifetime(), "Service Life Time"));
			}

			if (positionParam.getYearBuilt() != null) {
				positionInfor.getFacilityConditionIndex()
						.setYEARBUILT(tools.getDataTypeTools().encodeQuantity(positionParam.getYearBuilt(), "Service Life Time"));
			}
		}

		// HIERARCHY
		if (positionParam.getHierarchyAssetCode() != null || positionParam.getHierarchyPositionCode() != null
				|| positionParam.getHierarchyLocationCode() != null) {
			initializePositionHierarchy(positionInfor, positionParam, context);
		}

		// OUT OF SERVICE
		if (positionParam.getOutOfService() != null) {
			positionInfor.setOUTOFSERVICE(positionParam.getOutOfService());
		}

		// TYPE
		if (positionParam.getTypeCode() != null) {
			positionInfor.setTYPE(new TYPE_Type());
			positionInfor.getTYPE().setTYPECODE(positionParam.getTypeCode());
		}

		if (positionParam.getInProduction() != null) {
			positionInfor.setINPRODUCTION(positionParam.getInProduction());
		}

		// ORIGINAL RECEIPT DATE
		if (positionParam.getOriginalReceiptDate() != null) {
			positionInfor.setORIGINALRECEIPTDATE(
					tools.getDataTypeTools().formatDate(positionParam.getOriginalReceiptDate(), "Original Receipt Date"));
		}

		// SAFETY
		if (positionParam.getSafety() != null) {
			positionInfor.setSAFETY(positionParam.getSafety());
		}

		// ORIGINAL INSTALL DATE
		if (positionParam.getOriginalInstallDate() != null) {
			positionInfor.setORIGINALINSTALLDATE(
					tools.getDataTypeTools().encodeInforDate(positionParam.getOriginalInstallDate(), "Original Install Date"));
		}

	}

	private void initializePositionHierarchy(PositionEquipment positionInfor, Equipment positionParam, InforContext context) {

		PositionParentHierarchy positionParentHierarchy = new PositionParentHierarchy();

		if (tools.getDataTypeTools().isEmpty(positionParam.getHierarchyAssetDependent())) {
			positionParam.setHierarchyAssetDependent("FALSE");
		}

		if (tools.getDataTypeTools().isEmpty(positionParam.getHierarchyPositionDependent())) {
			positionParam.setHierarchyPositionDependent("FALSE");
		}

		if (tools.getDataTypeTools().isEmpty(positionParam.getHierarchyPrimarySystemDependent())) {
			positionParam.setHierarchyPrimarySystemDependent("FALSE");
		}

		positionParentHierarchy.setPOSITIONID(new EQUIPMENTID_Type());
		positionParentHierarchy.getPOSITIONID().setORGANIZATIONID(tools.getOrganization(context));
		positionParentHierarchy.getPOSITIONID().setEQUIPMENTCODE(positionParam.getCode());
		positionParentHierarchy.setTYPE(new TYPE_Type());
		positionParentHierarchy.getTYPE().setTYPECODE(positionParam.getTypeCode());

		EQUIPMENTID_Type hierarchyAsset = new EQUIPMENTID_Type();
		hierarchyAsset.setORGANIZATIONID(tools.getOrganization(context));
		hierarchyAsset.setEQUIPMENTCODE(positionParam.getHierarchyAssetCode());

		EQUIPMENTID_Type hierarchyPosition = new EQUIPMENTID_Type();
		hierarchyPosition.setORGANIZATIONID(tools.getOrganization(context));
		hierarchyPosition.setEQUIPMENTCODE(positionParam.getHierarchyPositionCode());

		EQUIPMENTID_Type hierarchySystem = new EQUIPMENTID_Type();
		hierarchySystem.setORGANIZATIONID(tools.getOrganization(context));
		hierarchySystem.setEQUIPMENTCODE(positionParam.getHierarchyPrimarySystemCode());

		// Asset dependent
		if (tools.getDataTypeTools().isTrueValue(positionParam.getHierarchyAssetDependent())) {

			positionParentHierarchy.setAssetDependency(new AssetDependency());
			// Non dependent position
			if (tools.getDataTypeTools().isNotEmpty(positionParam.getHierarchyPositionCode())) {
				positionParentHierarchy.getAssetDependency()
						.setNONDEPENDENTPOSITION(this.createHierarchyPosition(positionParam, hierarchyPosition));
			}

			// Non dependent system
			if (tools.getDataTypeTools().isNotEmpty(positionParam.getHierarchyPrimarySystemCode())) {
				positionParentHierarchy.getAssetDependency().setNONDEPENDENTPRIMARYSYSTEM(
						this.createHierarchyPrymarySystem(positionParam, hierarchySystem));
			}

			// Dependent asset
			positionParentHierarchy.getAssetDependency()
					.setDEPENDENTASSET(this.createHierarchyAsset(positionParam, hierarchyAsset));
		}
		// Position dependent
		else if (tools.getDataTypeTools().isTrueValue(positionParam.getHierarchyPositionDependent())) {
			positionParentHierarchy.setPositionDependency(new PositionDependency());

			// Non dependent asset
			if (tools.getDataTypeTools().isNotEmpty(positionParam.getHierarchyAssetCode())) {
				positionParentHierarchy.getPositionDependency()
						.setNONDEPENDENTASSET(this.createHierarchyAsset(positionParam, hierarchyAsset));
			}

			// Non dependent system
			if (tools.getDataTypeTools().isNotEmpty(positionParam.getHierarchyPrimarySystemCode())) {
				positionParentHierarchy.getPositionDependency().setNONDEPENDENTPRIMARYSYSTEM(
						this.createHierarchyPrymarySystem(positionParam, hierarchySystem));
			}

			// Dependent position
			positionParentHierarchy.getPositionDependency()
					.setDEPENDENTPOSITION(this.createHierarchyPosition(positionParam, hierarchyPosition));
		}
		// System dependent
		else if (tools.getDataTypeTools().isTrueValue(positionParam.getHierarchyPrimarySystemDependent())) {

			positionParentHierarchy.setPrimarySystemDependency(new PrimarySystemDependency());
			// Non dependent position
			if (tools.getDataTypeTools().isNotEmpty(positionParam.getHierarchyPositionCode())) {
				positionParentHierarchy.getPrimarySystemDependency()
						.setNONDEPENDENTPOSITION(this.createHierarchyPosition(positionParam, hierarchyPosition));
			}

			// Non dependent asset
			if (tools.getDataTypeTools().isNotEmpty(positionParam.getHierarchyAssetCode())) {
				positionParentHierarchy.getPrimarySystemDependency()
						.setNONDEPENDENTASSET(this.createHierarchyAsset(positionParam, hierarchyAsset));
			}

			// Dependent System
			positionParentHierarchy.getPrimarySystemDependency()
					.setDEPENDENTPRIMARYSYSTEM(this.createHierarchyPrymarySystem(positionParam, hierarchySystem));
		}
		// All non dependents
		else if (tools.getDataTypeTools().isFalseValue(positionParam.getHierarchyAssetDependent())
				&& tools.getDataTypeTools().isFalseValue(positionParam.getHierarchyPositionDependent())
				&& tools.getDataTypeTools().isFalseValue(positionParam.getHierarchyPrimarySystemDependent())) {

			// Non location
			if (positionParam.getHierarchyLocationCode() == null) {

				positionParentHierarchy.setNonDependentParents(new NonDependentParents_Type());

				// There is position
				if (tools.getDataTypeTools().isNotEmpty(positionParam.getHierarchyPositionCode())) {
					positionParentHierarchy.getNonDependentParents()
							.setNONDEPENDENTPOSITION(this.createHierarchyPosition(positionParam, hierarchyPosition));
				}

				// There is asset
				if (tools.getDataTypeTools().isNotEmpty(positionParam.getHierarchyAssetCode())) {
					positionParentHierarchy.getNonDependentParents()
							.setNONDEPENDENTASSET(this.createHierarchyAsset(positionParam, hierarchyAsset));
				}

				// There is system
				if (tools.getDataTypeTools().isNotEmpty(positionParam.getHierarchyPrimarySystemCode())) {
					positionParentHierarchy.getNonDependentParents().setNONDEPENDENTPRIMARYSYSTEM(
							this.createHierarchyPrymarySystem(positionParam, hierarchySystem));
				}

			}
			// There is location
			else {
				positionParentHierarchy.setLocationDependency(new LocationDependency());

				// Dependent location
				positionParentHierarchy.getLocationDependency().setDEPENDENTLOCATION(new LOCATIONPARENT_Type());
				positionParentHierarchy.getLocationDependency().getDEPENDENTLOCATION()
						.setLOCATIONID(new LOCATIONID_Type());
				positionParentHierarchy.getLocationDependency().getDEPENDENTLOCATION().getLOCATIONID()
						.setORGANIZATIONID(tools.getOrganization(context));
				positionParentHierarchy.getLocationDependency().getDEPENDENTLOCATION().getLOCATIONID()
						.setLOCATIONCODE(positionParam.getHierarchyLocationCode());

				// There is position
				if (tools.getDataTypeTools().isNotEmpty(positionParam.getHierarchyPositionCode())) {
					positionParentHierarchy.getLocationDependency()
							.setNONDEPENDENTPOSITION(this.createHierarchyPosition(positionParam, hierarchyPosition));
				}

				// There is asset
				if (tools.getDataTypeTools().isNotEmpty(positionParam.getHierarchyAssetCode())) {
					positionParentHierarchy.getLocationDependency()
							.setNONDEPENDENTASSET(this.createHierarchyAsset(positionParam, hierarchyAsset));
				}

				// There is system
				if (tools.getDataTypeTools().isNotEmpty(positionParam.getHierarchyPrimarySystemCode())) {
					positionParentHierarchy.getLocationDependency().setNONDEPENDENTPRIMARYSYSTEM(
							this.createHierarchyPrymarySystem(positionParam, hierarchySystem));
				}
			}
		}
		positionInfor.setPositionParentHierarchy(positionParentHierarchy);
	}

	private ASSETPARENT_Type createHierarchyAsset(Equipment positionParam, EQUIPMENTID_Type hierarchyAsset) {
		ASSETPARENT_Type assetType = new ASSETPARENT_Type();
		assetType.setASSETID(hierarchyAsset);
		assetType.setCOSTROLLUP(positionParam.getHierarchyAssetCostRollUp());
		return assetType;
	}

	private POSITIONPARENT_Type createHierarchyPosition(Equipment positionParam, EQUIPMENTID_Type hierarchyPosition) {
		POSITIONPARENT_Type positionType = new POSITIONPARENT_Type();
		positionType.setPOSITIONID(hierarchyPosition);
		positionType.setCOSTROLLUP(positionParam.getHierarchyPositionCostRollUp());
		return positionType;
	}

	private SYSTEMPARENT_Type createHierarchyPrymarySystem(Equipment positionParam, EQUIPMENTID_Type hierarchySystem) {
		SYSTEMPARENT_Type systemType = new SYSTEMPARENT_Type();
		systemType.setSYSTEMID(hierarchySystem);
		systemType.setCOSTROLLUP(positionParam.getHierarchyPrimarySystemCostRollUp());
		return systemType;
	}
}
