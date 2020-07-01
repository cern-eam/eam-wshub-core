package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.PositionService;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.EntityId;
import ch.cern.eam.wshub.core.services.userdefinedscreens.impl.UserDefinedListServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.positionequipment_001.PositionEquipment;
import net.datastream.schemas.mp_entities.positionhierarchy_002.*;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0306_001.MP0306_AddPositionEquipment_001;
import net.datastream.schemas.mp_functions.mp0307_001.MP0307_GetPositionEquipment_001;
import net.datastream.schemas.mp_functions.mp0308_001.MP0308_SyncPositionEquipment_001;
import net.datastream.schemas.mp_functions.mp0309_001.MP0309_DeletePositionEquipment_001;
import net.datastream.schemas.mp_functions.mp0310_001.MP0310_GetPositionEquipmentDefault_001;
import net.datastream.schemas.mp_functions.mp0328_002.MP0328_GetPositionParentHierarchy_002;
import net.datastream.schemas.mp_results.mp0306_001.MP0306_AddPositionEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0307_001.MP0307_GetPositionEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0310_001.MP0310_GetPositionEquipmentDefault_001_Result;
import net.datastream.schemas.mp_results.mp0328_002.MP0328_GetPositionParentHierarchy_002_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.Arrays;
import java.util.HashMap;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;


public class PositionServiceImpl implements PositionService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;
	private UserDefinedListService userDefinedListService;

	public PositionServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.userDefinedListService = new UserDefinedListServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
	}

	public String createPosition(InforContext context, Equipment positionParam) throws InforException {

		PositionEquipment positionEquipment = new PositionEquipment();
		//
		positionEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
			context,
			toCodeString(positionEquipment.getCLASSID()),
			positionEquipment.getUSERDEFINEDAREA(),
			positionParam.getClassCode(),
			"OBJ"));

		//
		initializePositionObject(context, positionEquipment, positionParam, true);
		tools.getInforFieldTools().transformWSHubObject(positionEquipment, positionParam, context);
		//
		MP0306_AddPositionEquipment_001 addPosition = new MP0306_AddPositionEquipment_001();
		addPosition.setPositionEquipment(positionEquipment);
		MP0306_AddPositionEquipment_001_Result result =
			tools.performInforOperation(context, inforws::addPositionEquipmentOp, addPosition);
		String equipmentCode = result.getResultData().getPOSITIONID().getEQUIPMENTCODE();
		userDefinedListService.writeUDLToEntityCopyFrom(context, positionParam, new EntityId("OBJ", equipmentCode));
		//TODO Update CERN properties
		//equipmentOther.updateEquipmentCERNProperties(positionParam);
		return equipmentCode;
	}

	public String deletePosition(InforContext context, String positionCode) throws InforException {

		MP0309_DeletePositionEquipment_001 deletePosition = new MP0309_DeletePositionEquipment_001();
		deletePosition.setPOSITIONID(new EQUIPMENTID_Type());
		deletePosition.getPOSITIONID().setORGANIZATIONID(tools.getOrganization(context));
		deletePosition.getPOSITIONID().setEQUIPMENTCODE(positionCode);

		tools.performInforOperation(context, inforws::deletePositionEquipmentOp, deletePosition);
		userDefinedListService.deleteUDLFromEntity(context, new EntityId("OBJ", positionCode));
		return positionCode;
	}

	private PositionParentHierarchy readInforPositionParentHierarchy(InforContext context, String assetCode) throws InforException {

		MP0328_GetPositionParentHierarchy_002 getpositionph = new MP0328_GetPositionParentHierarchy_002();
		getpositionph.setPOSITIONID(new EQUIPMENTID_Type());
		getpositionph.getPOSITIONID().setORGANIZATIONID(tools.getOrganization(context));
		getpositionph.getPOSITIONID().setEQUIPMENTCODE(assetCode);

		MP0328_GetPositionParentHierarchy_002_Result result =
			tools.performInforOperation(context, inforws::getPositionParentHierarchyOp, getpositionph);

		return result.getResultData().getPositionParentHierarchy();
	}

	public Equipment readPositionDefault(InforContext context, String organization) throws InforException {

		MP0310_GetPositionEquipmentDefault_001 getPositionEquipmentDefault_001 = new MP0310_GetPositionEquipmentDefault_001();
		if (isEmpty(organization)) {
			getPositionEquipmentDefault_001.setORGANIZATIONID(tools.getOrganization(context));
		} else {
			getPositionEquipmentDefault_001.setORGANIZATIONID(new ORGANIZATIONID_Type());
			getPositionEquipmentDefault_001.getORGANIZATIONID().setORGANIZATIONCODE(organization);
		}

		MP0310_GetPositionEquipmentDefault_001_Result result =
				tools.performInforOperation(context, inforws::getPositionEquipmentDefaultOp, getPositionEquipmentDefault_001);

		Equipment equipment = tools.getInforFieldTools().transformInforObject(new Equipment(), result.getResultData().getPositionEquipment());
		equipment.setUserDefinedList(new HashMap<>());
		return equipment;
	}

	public Equipment readPosition(InforContext context, String positionCode) throws InforException {
		PositionEquipment positionEquipment = readInforPosition(context, positionCode);
		Equipment position = tools.getInforFieldTools().transformInforObject(new Equipment(), positionEquipment);
		position.setSystemTypeCode("P");

		// POSITION ID
		if (positionEquipment.getPOSITIONID() != null) {
			position.setCode(positionEquipment.getPOSITIONID().getEQUIPMENTCODE());
			position.setDescription(positionEquipment.getPOSITIONID().getDESCRIPTION());
		}

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
			position.setHierarchyPositionDependent(true);
			position.setHierarchyPositionCostRollUp(decodeBoolean(positionDep.getDEPENDENTPOSITION().getCOSTROLLUP()));

			// Non dependent asset
			if (positionDep.getNONDEPENDENTASSET() != null) {
				//
				position.setHierarchyAssetCode(positionDep.getNONDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
				position.setHierarchyAssetDesc(positionDep.getNONDEPENDENTASSET().getASSETID().getDESCRIPTION());
				position.setHierarchyAssetCostRollUp(decodeBoolean(positionDep.getNONDEPENDENTASSET().getCOSTROLLUP()));
				position.setHierarchyAssetDependent(false);
			}

			// Non dependent system
			if (positionDep.getNONDEPENDENTPRIMARYSYSTEM() != null) {
				//
				position.setHierarchyPrimarySystemCode(
						positionDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
				position.setHierarchyPrimarySystemDesc(
						positionDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
				position.setHierarchyPrimarySystemCostRollUp(
						decodeBoolean(positionDep.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP()));
				position.setHierarchyPrimarySystemDependent(false);
			}

		} else if (positionEquipment.getPositionParentHierarchy().getAssetDependency() != null) {
			AssetDependency assetDep = positionEquipment.getPositionParentHierarchy().getAssetDependency();

			// Dependent Asset
			position.setHierarchyAssetCode(assetDep.getDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
			position.setHierarchyAssetDesc(assetDep.getDEPENDENTASSET().getASSETID().getDESCRIPTION());
			position.setHierarchyAssetCostRollUp(decodeBoolean(assetDep.getDEPENDENTASSET().getCOSTROLLUP()));
			position.setHierarchyAssetDependent(true);

			// Non dependent position
			if (assetDep.getNONDEPENDENTPOSITION() != null) {
				position.setHierarchyPositionCode(
						assetDep.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
				position.setHierarchyPositionDesc(assetDep.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
				position.setHierarchyPositionDependent(false);
				position.setHierarchyPositionCostRollUp(decodeBoolean(assetDep.getNONDEPENDENTPOSITION().getCOSTROLLUP()));
			}

			// Non dependent system
			if (assetDep.getNONDEPENDENTPRIMARYSYSTEM() != null) {
				position.setHierarchyPrimarySystemCode(
						assetDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
				position.setHierarchyPrimarySystemDesc(
						assetDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
				position.setHierarchyPrimarySystemCostRollUp(decodeBoolean(assetDep.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP()));
				position.setHierarchyPrimarySystemDependent(false);
			}

		} else if (positionEquipment.getPositionParentHierarchy().getSystemDependency() != null) {
			SystemDependency systemDep = positionEquipment.getPositionParentHierarchy().getSystemDependency();

			// Dependent System
			position.setHierarchyPrimarySystemCode(systemDep.getDEPENDENTSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
			position.setHierarchyPrimarySystemDesc(systemDep.getDEPENDENTSYSTEM().getSYSTEMID().getDESCRIPTION());
			position.setHierarchyPrimarySystemCostRollUp(decodeBoolean(systemDep.getDEPENDENTSYSTEM().getCOSTROLLUP()));
			position.setHierarchyPrimarySystemDependent(true);

			// Non dependent position
			if (systemDep.getNONDEPENDENTPOSITION() != null) {
				position.setHierarchyPositionCode(
						systemDep.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
				position.setHierarchyPositionDesc(systemDep.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
				position.setHierarchyPositionDependent(false);
				position.setHierarchyPositionCostRollUp(decodeBoolean(systemDep.getNONDEPENDENTPOSITION().getCOSTROLLUP()));
			}

			// Non dependent asset
			if (systemDep.getNONDEPENDENTASSET() != null) {
				//
				position.setHierarchyAssetCode(systemDep.getNONDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
				position.setHierarchyAssetDesc(systemDep.getNONDEPENDENTASSET().getASSETID().getDESCRIPTION());
				position.setHierarchyAssetCostRollUp(decodeBoolean(systemDep.getNONDEPENDENTASSET().getCOSTROLLUP()));
				position.setHierarchyAssetDependent(false);
			}

		} else if (positionEquipment.getPositionParentHierarchy().getNonDependentParents() != null) {
			NonDependentParents_Type nonDepParents = positionEquipment.getPositionParentHierarchy()
					.getNonDependentParents();

			// Non dependent asset
			if (nonDepParents.getNONDEPENDENTASSET() != null) {
				position.setHierarchyAssetCode(nonDepParents.getNONDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
				position.setHierarchyAssetDesc(nonDepParents.getNONDEPENDENTASSET().getASSETID().getDESCRIPTION());
				position.setHierarchyAssetCostRollUp(decodeBoolean(nonDepParents.getNONDEPENDENTASSET().getCOSTROLLUP()));
				position.setHierarchyAssetDependent(false);
			}

			// Non dependent position
			if (nonDepParents.getNONDEPENDENTPOSITION() != null) {
				position.setHierarchyPositionCode(
						nonDepParents.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
				position.setHierarchyPositionDesc(
						nonDepParents.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
				position.setHierarchyPositionDependent(false);
				position.setHierarchyPositionCostRollUp(decodeBoolean(nonDepParents.getNONDEPENDENTPOSITION().getCOSTROLLUP()));
			}

			// Non dependent system
			if (nonDepParents.getNONDEPENDENTPRIMARYSYSTEM() != null) {
				position.setHierarchyPrimarySystemCode(
						nonDepParents.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
				position.setHierarchyPrimarySystemDesc(
						nonDepParents.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
				position.setHierarchyPrimarySystemCostRollUp(
						decodeBoolean(nonDepParents.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP()));
				position.setHierarchyPrimarySystemDependent(false);
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
				position.setHierarchyAssetCostRollUp(decodeBoolean(locationDep.getNONDEPENDENTASSET().getCOSTROLLUP()));
				position.setHierarchyAssetDependent(false);
			}

			// Non dependent position
			if (locationDep.getNONDEPENDENTPOSITION() != null) {
				position.setHierarchyPositionCode(
						locationDep.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
				position.setHierarchyPositionDesc(
						locationDep.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
				position.setHierarchyPositionDependent(false);
				position.setHierarchyPositionCostRollUp(decodeBoolean(locationDep.getNONDEPENDENTPOSITION().getCOSTROLLUP()));
			}

			// Non dependent system
			if (locationDep.getNONDEPENDENTPRIMARYSYSTEM() != null) {
				position.setHierarchyPrimarySystemCode(
						locationDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
				position.setHierarchyPrimarySystemDesc(
						locationDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
				position.setHierarchyPrimarySystemCostRollUp(
						decodeBoolean(locationDep.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP()));
				position.setHierarchyPrimarySystemDependent(false);
			}
		}

		userDefinedListService.readUDLToEntity(context, position, new EntityId("OBJ", positionCode));
		return position;
	}

	private PositionEquipment readInforPosition(InforContext context, String positionCode) throws InforException {
		MP0307_GetPositionEquipment_001 getPosition = new MP0307_GetPositionEquipment_001();
		getPosition.setPOSITIONID(new EQUIPMENTID_Type());
		getPosition.getPOSITIONID().setORGANIZATIONID(tools.getOrganization(context));
		getPosition.getPOSITIONID().setEQUIPMENTCODE(positionCode);
		MP0307_GetPositionEquipment_001_Result getAssetResult =
			tools.performInforOperation(context, inforws::getPositionEquipmentOp, getPosition);
		return getAssetResult.getResultData().getPositionEquipment();

	}

	public String updatePosition(InforContext context, Equipment positionParam) throws InforException {
		// Read position
		PositionEquipment positionEquipment = readInforPosition(context, positionParam.getCode());
		//
		//
		//
		positionEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
			context,
			toCodeString(positionEquipment.getCLASSID()),
			positionEquipment.getUSERDEFINEDAREA(),
			positionParam.getClassCode(),
			"OBJ"));

		initializePositionObject(context, positionEquipment, positionParam, false);
		tools.getInforFieldTools().transformWSHubObject(positionEquipment, positionParam, context);
		// Update it
		MP0308_SyncPositionEquipment_001 syncPosition = new MP0308_SyncPositionEquipment_001();
		syncPosition.setPositionEquipment(positionEquipment);
		tools.performInforOperation(context, inforws::syncPositionEquipmentOp, syncPosition);
		userDefinedListService.writeUDLToEntity(context, positionParam, new EntityId("OBJ", positionParam.getCode()));
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

		if (positionParam.getDescription() != null) {
			positionInfor.getPOSITIONID().setDESCRIPTION(positionParam.getDescription());
		}

		// HIERARCHY
		if (positionParam.getHierarchyAssetCode() != null
				|| positionParam.getHierarchyPositionCode() != null
				|| positionParam.getHierarchyLocationCode() != null) {
			initializePositionHierarchy(positionInfor, positionParam, context);
		}
	}

	private void initializePositionHierarchy(PositionEquipment positionInfor, Equipment positionParam, InforContext context) {

		PositionParentHierarchy positionParentHierarchy = new PositionParentHierarchy();

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
		if (positionParam.getHierarchyAssetDependent() != null && positionParam.getHierarchyAssetDependent()) {

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
		else if (positionParam.getHierarchyPositionDependent() != null && positionParam.getHierarchyPositionDependent()) {
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
		else if (positionParam.getHierarchyPrimarySystemDependent() != null && positionParam.getHierarchyPrimarySystemDependent()) {

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
		else if (
            (positionParam.getHierarchyAssetDependent() == null || !positionParam.getHierarchyAssetDependent())
				&& (positionParam.getHierarchyPositionDependent() == null || !positionParam.getHierarchyPositionDependent())
				&& (positionParam.getHierarchyPrimarySystemDependent() == null || !positionParam.getHierarchyPrimarySystemDependent())) {

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
		assetType.setCOSTROLLUP(encodeBoolean(positionParam.getHierarchyAssetCostRollUp(), BooleanType.TRUE_FALSE));
		return assetType;
	}

	private POSITIONPARENT_Type createHierarchyPosition(Equipment positionParam, EQUIPMENTID_Type hierarchyPosition) {
		POSITIONPARENT_Type positionType = new POSITIONPARENT_Type();
		positionType.setPOSITIONID(hierarchyPosition);
		positionType.setCOSTROLLUP(encodeBoolean(positionParam.getHierarchyPositionCostRollUp(), BooleanType.TRUE_FALSE));
		return positionType;
	}

	private SYSTEMPARENT_Type createHierarchyPrymarySystem(Equipment positionParam, EQUIPMENTID_Type hierarchySystem) {
		SYSTEMPARENT_Type systemType = new SYSTEMPARENT_Type();
		systemType.setSYSTEMID(hierarchySystem);
		systemType.setCOSTROLLUP(encodeBoolean(positionParam.getHierarchyPrimarySystemCostRollUp(), BooleanType.TRUE_FALSE));
		return systemType;
	}
}
