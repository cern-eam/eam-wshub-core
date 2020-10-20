package ch.cern.eam.wshub.core.services.equipment.impl;

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

import java.util.HashMap;
import java.util.List;

import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.*;
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.readHierarchyType;
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
		if (positionEquipment.getPositionParentHierarchy().getLOCATIONID() != null) {
			position.setHierarchyLocationCode(positionEquipment.getPositionParentHierarchy().getLOCATIONID().getLOCATIONCODE());
			position.setHierarchyLocationDesc(positionEquipment.getPositionParentHierarchy().getLOCATIONID().getDESCRIPTION());
		}
		position.setHierarchyAssetDependent(positionEquipment.getPositionParentHierarchy().getAssetDependency() != null);
		position.setHierarchyPositionDependent(positionEquipment.getPositionParentHierarchy().getPositionDependency() != null);
		position.setHierarchyPrimarySystemDependent(positionEquipment.getPositionParentHierarchy().getPrimarySystemDependency() != null);

		userDefinedListService.readUDLToEntity(context, position, new EntityId("OBJ", positionCode));
		return position;
	}

	private PositionEquipment readInforPosition(InforContext context, String positionCode) throws InforException {
		MP0307_GetPositionEquipment_001 getPosition = new MP0307_GetPositionEquipment_001();
		getPosition.setPOSITIONID(new EQUIPMENTID_Type());
		getPosition.getPOSITIONID().setORGANIZATIONID(tools.getOrganization(context));
		getPosition.getPOSITIONID().setEQUIPMENTCODE(positionCode);
		MP0307_GetPositionEquipment_001_Result getAssetResult = tools.performInforOperation(context, inforws::getPositionEquipmentOp, getPosition);
		getAssetResult.getResultData().getPositionEquipment().setPositionParentHierarchy(readInforPositionParentHierarchy(context, positionCode));

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
				|| positionParam.getHierarchyPrimarySystemCode() != null
				|| positionParam.getHierarchyLocationCode() != null) {
			initializePositionHierarchy(positionInfor, positionParam, context);
		}
	}

	private void initializePositionHierarchy(PositionEquipment positionInfor, Equipment positionParam, InforContext context) {
		PositionParentHierarchy hierarchy = new PositionParentHierarchy();

		hierarchy.setPOSITIONID(new EQUIPMENTID_Type());
		hierarchy.getPOSITIONID().setEQUIPMENTCODE(positionParam.getCode());
		hierarchy.getPOSITIONID().setORGANIZATIONID(tools.getOrganization(context));
		hierarchy.setTYPE(new TYPE_Type());
		hierarchy.getTYPE().setTYPECODE("A");

		// Fetch all possible parent types that are present in only one object that indicates the current hierarchy type
		ASSETPARENT_Type assetParent = readAssetParent(positionInfor.getPositionParentHierarchy());
		POSITIONPARENT_Type positionParent = readPositionParent(positionInfor.getPositionParentHierarchy());
		SYSTEMPARENT_Type primarySystemParent = readPrimarySystemParent(positionInfor.getPositionParentHierarchy());
		LOCATIONPARENT_Type locationParent = readLocationParent(positionInfor.getPositionParentHierarchy());
		List<SYSTEMPARENT_Type> systemParents = readSystemsParent(positionInfor.getPositionParentHierarchy());
		HIERARCHY_TYPE currentHierarchyType = readHierarchyType(positionInfor.getPositionParentHierarchy());

		// Incorporate user changes into the parent types
		assetParent = createAssetParent(tools.getOrganizationCode(context), positionParam.getHierarchyAssetCode(), positionParam.getHierarchyAssetCostRollUp(), assetParent);
		positionParent = createPositionParent(tools.getOrganizationCode(context), positionParam.getHierarchyPositionCode(), positionParam.getHierarchyPositionCostRollUp(), positionParent);
		primarySystemParent = createPrimarySystemParent(tools.getOrganizationCode(context), positionParam.getHierarchyPrimarySystemCode(), positionParam.getHierarchyPrimarySystemCostRollUp(), primarySystemParent);
		locationParent = createLocationParent(tools.getOrganizationCode(context), positionParam.getHierarchyLocationCode(), locationParent);

		// Init new hierarchy
		switch (getNewHierarchyType(positionParam, currentHierarchyType)) {
			case ASSET_DEP:
				hierarchy.setAssetDependency(createAssetDependencyForPosition(assetParent, positionParent, primarySystemParent, systemParents));
				break;
			case POSITION_DEP:
				hierarchy.setPositionDependency(createPositionDependencyForPosition(assetParent, positionParent, primarySystemParent, systemParents));
				break;
			case PRIM_SYSTEM_DEP:
				hierarchy.setPrimarySystemDependency(createPrimarySystemDependencyForPosition(assetParent, positionParent, primarySystemParent, systemParents));
				break;
			case LOCATION_DEP:
				hierarchy.setLocationDependency(createLocationDependencyForPosition(assetParent, positionParent, primarySystemParent, systemParents, locationParent));
				break;
			default:
				hierarchy.setNonDependentParents(createNonDependentParentsForPosition(assetParent, positionParent, primarySystemParent, systemParents));
		}

		positionInfor.setPositionParentHierarchy(hierarchy);
	}

}
