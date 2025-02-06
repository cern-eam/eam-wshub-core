package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.equipment.AssetService;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.EntityId;
import ch.cern.eam.wshub.core.services.userdefinedscreens.impl.UserDefinedListServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.assetequipment_001.*;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0301_001.MP0301_AddAssetEquipment_001;
import net.datastream.schemas.mp_functions.mp0302_001.MP0302_GetAssetEquipment_001;
import net.datastream.schemas.mp_functions.mp0303_001.MP0303_SyncAssetEquipment_001;
import net.datastream.schemas.mp_functions.mp0304_001.MP0304_DeleteAssetEquipment_001;
import net.datastream.schemas.mp_functions.mp0305_001.MP0305_GetAssetEquipmentDefault_001;
import net.datastream.schemas.mp_functions.mp0327_001.MP0327_GetAssetParentHierarchy_001;
import net.datastream.schemas.mp_results.mp0301_001.MP0301_AddAssetEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0302_001.MP0302_GetAssetEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0305_001.MP0305_GetAssetEquipmentDefault_001_Result;
import net.datastream.schemas.mp_results.mp0327_001.MP0327_GetAssetParentHierarchy_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.*;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class AssetServiceImpl implements AssetService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;
    private UserDefinedListService userDefinedListService;

    public AssetServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
        this.userDefinedListService = new UserDefinedListServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
    }

    public Equipment readAssetDefault(EAMContext context, String organization) throws EAMException {
        MP0305_GetAssetEquipmentDefault_001 getAssetEquipmentDefault_001 = new MP0305_GetAssetEquipmentDefault_001();
        getAssetEquipmentDefault_001.setORGANIZATIONID(tools.getOrganization(context , organization));
        MP0305_GetAssetEquipmentDefault_001_Result result = tools.performEAMOperation(context, eamws::getAssetEquipmentDefaultOp, getAssetEquipmentDefault_001);

        Equipment equipment = tools.getEAMFieldTools().transformEAMObject(new Equipment(), result.getResultData().getAssetEquipment(), context);
        equipment.setUserDefinedList(new HashMap<>());
        return equipment;
    }

    public Equipment readAsset(EAMContext context, String assetCode, String organization) throws EAMException {
        AssetEquipment assetEquipment = readEAMAsset(context, assetCode, organization);
        //
        Equipment asset = tools.getEAMFieldTools().transformEAMObject(new Equipment(), assetEquipment, context);
        asset.setSystemTypeCode("A");

        // DESCRIPTIONS
        tools.processRunnables(
                () -> asset.setManufacturerDesc(tools.getFieldDescriptionsTools().readManufacturerDesc(context, asset.getManufacturerCode())),
                () -> asset.setBinDesc(tools.getFieldDescriptionsTools().readBinDesc(context, asset.getStoreCode(), asset.getBin())),
                () -> asset.setSystemStatusCode(tools.getFieldDescriptionsTools().readSystemCodeForUserCode(context, "OBST", asset.getStatusCode())),
                () -> { if(tools.isDatabaseConnectionConfigured()) userDefinedListService.readUDLToEntity(context, asset, new EntityId("OBJ", assetCode)); }
        );

        // HIERARCHY
        if (assetEquipment.getAssetParentHierarchy().getLOCATIONID() != null) {
            asset.setHierarchyLocationCode(assetEquipment.getAssetParentHierarchy().getLOCATIONID().getLOCATIONCODE());
            asset.setHierarchyLocationDesc(assetEquipment.getAssetParentHierarchy().getLOCATIONID().getDESCRIPTION());
        }
        asset.setHierarchyAssetDependent(assetEquipment.getAssetParentHierarchy().getAssetDependency() != null);
        asset.setHierarchyPositionDependent(assetEquipment.getAssetParentHierarchy().getPositionDependency() != null);
        asset.setHierarchyPrimarySystemDependent(assetEquipment.getAssetParentHierarchy().getPrimarySystemDependency() != null);

        return asset;
    }

    private AssetParentHierarchy readEAMAssetHierarchy(EAMContext context, String assetCode, String organization) throws EAMException {
        MP0327_GetAssetParentHierarchy_001 getassetph = new MP0327_GetAssetParentHierarchy_001();
        getassetph.setASSETID(new EQUIPMENTID_Type());
        getassetph.getASSETID().setORGANIZATIONID(tools.getOrganization(context, organization));
        getassetph.getASSETID().setEQUIPMENTCODE(assetCode);

        MP0327_GetAssetParentHierarchy_001_Result result = tools.performEAMOperation(context, eamws::getAssetParentHierarchyOp, getassetph);

        return result.getResultData().getAssetParentHierarchy();
    }

    private AssetEquipment readEAMAsset(EAMContext context, String assetCode, String organization)
            throws EAMException {
        MP0302_GetAssetEquipment_001 getAsset = new MP0302_GetAssetEquipment_001();
        getAsset.setASSETID(new EQUIPMENTID_Type());
        getAsset.getASSETID().setORGANIZATIONID(tools.getOrganization(context, organization));
        getAsset.getASSETID().setEQUIPMENTCODE(assetCode);

        MP0302_GetAssetEquipment_001_Result getAssetResult = tools.performEAMOperation(context, eamws::getAssetEquipmentOp, getAsset);
        getAssetResult.getResultData().getAssetEquipment().setAssetParentHierarchy(readEAMAssetHierarchy(context, assetCode, organization));

        return getAssetResult.getResultData().getAssetEquipment();
    }

    public String updateAsset(EAMContext context, Equipment assetParam) throws EAMException {
        AssetEquipment assetEquipment = readEAMAsset(context, assetParam.getCode(), assetParam.getOrganization());

        //
        assetEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
            context,
            toCodeString(assetEquipment.getCLASSID()),
            assetEquipment.getUSERDEFINEDAREA(),
            assetParam.getClassCode(),
            "OBJ")
        );

        initializeAssetObject(assetEquipment, assetParam, context);
        tools.getEAMFieldTools().transformWSHubObject(assetEquipment, assetParam, context);

        // PART ASSOCIATION
        if (assetParam.getPartCode() != null) {
            if (assetParam.getPartCode().equals("")
                    && assetEquipment.getPartAssociation() != null) {
                assetEquipment.getPartAssociation().setSTORELOCATION(null);
                assetEquipment.getPartAssociation().getPARTID().getORGANIZATIONID().setORGANIZATIONCODE("");
            } else if (!"".equals(assetParam.getPartCode())) {
                if (assetEquipment.getPartAssociation().getSTORELOCATION() == null) {
                    assetEquipment.getPartAssociation().setSTORELOCATION(new STORELOCATION());
                }
                if (assetEquipment.getPartAssociation().getSTORELOCATION().getLOT() == null) {
                    assetEquipment.getPartAssociation().getSTORELOCATION().setLOT("*");
                }
            }
        }

        //
        // UPDATE EQUIPMENT
        //
        MP0303_SyncAssetEquipment_001 syncAsset = new MP0303_SyncAssetEquipment_001();
        syncAsset.setAssetEquipment(assetEquipment);
        syncAsset.setConfirm_Availability_Status("confirmed");
        tools.performEAMOperation(context, eamws::syncAssetEquipmentOp, syncAsset);
        userDefinedListService.writeUDLToEntity(context, assetParam, new EntityId("OBJ", assetParam.getCode()));

        return assetParam.getCode();
    }

    public String createAsset(EAMContext context, Equipment assetParam) throws EAMException {

        AssetEquipment assetEquipment = new AssetEquipment();
        //
        assetEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
            context,
            toCodeString(assetEquipment.getCLASSID()),
            assetEquipment.getUSERDEFINEDAREA(),
            assetParam.getClassCode(),
            "OBJ"));

        //
        initializeAssetObject(assetEquipment, assetParam, context);
        tools.getEAMFieldTools().transformWSHubObject(assetEquipment, assetParam, context);
        //
        MP0301_AddAssetEquipment_001 addAsset = new MP0301_AddAssetEquipment_001();
        addAsset.setAssetEquipment(assetEquipment);
        MP0301_AddAssetEquipment_001_Result addAssetResult =
            tools.performEAMOperation(context, eamws::addAssetEquipmentOp, addAsset);
        String equipmentCode = addAssetResult.getResultData().getASSETID().getEQUIPMENTCODE();
        userDefinedListService.writeUDLToEntityCopyFrom(context, assetParam, new EntityId("OBJ", equipmentCode));
        return equipmentCode;
    }

    public String deleteAsset(EAMContext context, String assetCode, String organization) throws EAMException {
        MP0304_DeleteAssetEquipment_001 deleteAsset = new MP0304_DeleteAssetEquipment_001();
        deleteAsset.setASSETID(new EQUIPMENTID_Type());
        deleteAsset.getASSETID().setORGANIZATIONID(tools.getOrganization(context, organization));
        deleteAsset.getASSETID().setEQUIPMENTCODE(assetCode);

        tools.performEAMOperation(context, eamws::deleteAssetEquipmentOp, deleteAsset);
        userDefinedListService.deleteUDLFromEntity(context, new EntityId("OBJ", assetCode));
        return assetCode;
    }

    private void initializeAssetObject(AssetEquipment assetEAM, Equipment assetParam, EAMContext context) throws EAMException {
        // == null means Asset creation
        if (assetEAM.getASSETID() == null) {
            assetEAM.setASSETID(new EQUIPMENTID_Type());
            assetEAM.getASSETID().setORGANIZATIONID(tools.getOrganization(context, assetParam.getOrganization()));
            assetEAM.getASSETID().setEQUIPMENTCODE(assetParam.getCode().toUpperCase().trim());
        }

        if (assetParam.getDescription() != null) {
            assetEAM.getASSETID().setDESCRIPTION(assetParam.getDescription());
        }

        // HIERARCHY
        if (assetParam.getHierarchyAssetCode() != null
                || assetParam.getHierarchyPositionCode() != null
                || assetParam.getHierarchyPrimarySystemCode() != null
                || assetParam.getHierarchyLocationCode() != null) {
            try {
                initializeAssetHierarchy(assetEAM, assetParam, context);
            } catch (Exception e) {
                tools.log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void initializeAssetHierarchy(AssetEquipment assetEAM, Equipment assetParam, EAMContext context) throws EAMException {
        AssetParentHierarchy hierarchy = new AssetParentHierarchy();

        hierarchy.setASSETID(new EQUIPMENTID_Type());
        hierarchy.getASSETID().setEQUIPMENTCODE(assetParam.getCode());
        hierarchy.getASSETID().setORGANIZATIONID(tools.getOrganization(context, assetParam.getOrganization()));
        hierarchy.setTYPE(new TYPE_Type());
        hierarchy.getTYPE().setTYPECODE("A");

        // Fetch all possible parent types that are present in only one object that indicates the current hierarchy type
        ASSETPARENT_Type assetParent = readAssetParent(assetEAM.getAssetParentHierarchy());
        POSITIONPARENT_Type positionParent = readPositionParent(assetEAM.getAssetParentHierarchy());
        SYSTEMPARENT_Type primarySystemParent = readPrimarySystemParent(assetEAM.getAssetParentHierarchy());
        LOCATIONPARENT_Type locationParent = readLocationParent(assetEAM.getAssetParentHierarchy());
        List<SYSTEMPARENT_Type> systemParents = readSystemsParent(assetEAM.getAssetParentHierarchy());
        HIERARCHY_TYPE currentHierarchyType = readHierarchyType(assetEAM.getAssetParentHierarchy());

        // Incorporate user changes into the parent types
        assetParent = createAssetParent(tools.getOrganizationCode(context, assetParam.getHierarchyAssetOrg()), assetParam.getHierarchyAssetCode(), assetParam.getHierarchyAssetCostRollUp(), assetParent);
        positionParent = createPositionParent(tools.getOrganizationCode(context, assetParam.getHierarchyPositionOrg()), assetParam.getHierarchyPositionCode(), assetParam.getHierarchyPositionCostRollUp(), positionParent);
        primarySystemParent = createPrimarySystemParent(tools.getOrganizationCode(context, assetParam.getHierarchyPrimarySystemOrg()), assetParam.getHierarchyPrimarySystemCode(), assetParam.getHierarchyPrimarySystemCostRollUp(), primarySystemParent);
        locationParent = createLocationParent(tools.getOrganizationCode(context), assetParam.getHierarchyLocationCode(), locationParent);

        // Init new hierarchy
        switch (getNewHierarchyType(assetParam, currentHierarchyType)) {
            case ASSET_DEP:
                hierarchy.setAssetDependency(createAssetDependencyForAsset(assetParent, positionParent, primarySystemParent, systemParents));
                break;
            case POSITION_DEP:
                hierarchy.setPositionDependency(createPositionDependencyForAsset(assetParent, positionParent, primarySystemParent, systemParents));
                break;
            case PRIM_SYSTEM_DEP:
                hierarchy.setPrimarySystemDependency(createPrimarySystemDependencyForAsset(assetParent, positionParent, primarySystemParent, systemParents));
                break;
            case LOCATION_DEP:
                hierarchy.setLocationDependency(createLocationDependencyForAsset(assetParent, positionParent, primarySystemParent, systemParents, locationParent));
                break;
            default:
                hierarchy.setNonDependentParents(createNonDependentParentsForAsset(assetParent, positionParent, primarySystemParent, systemParents));
        }

        assetEAM.setAssetParentHierarchy(hierarchy);
    }

}
