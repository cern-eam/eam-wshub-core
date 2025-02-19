package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.AssetService;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.EntityId;
import ch.cern.eam.wshub.core.services.userdefinedscreens.impl.UserDefinedListServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
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
import net.datastream.wsdls.inforws.InforWebServicesPT;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.*;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class AssetServiceImpl implements AssetService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;
    private UserDefinedListService userDefinedListService;

    public AssetServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        this.userDefinedListService = new UserDefinedListServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
    }

    public Equipment readAssetDefault(InforContext context, String organization) throws InforException {
        MP0305_GetAssetEquipmentDefault_001 getAssetEquipmentDefault_001 = new MP0305_GetAssetEquipmentDefault_001();
        getAssetEquipmentDefault_001.setORGANIZATIONID(tools.getOrganization(context , organization));
        MP0305_GetAssetEquipmentDefault_001_Result result = tools.performInforOperation(context, inforws::getAssetEquipmentDefaultOp, getAssetEquipmentDefault_001);

        Equipment equipment = tools.getInforFieldTools().transformInforObject(new Equipment(), result.getResultData().getAssetEquipment(), context);
        equipment.setUserDefinedList(new HashMap<>());
        return equipment;
    }

    public Equipment readAsset(InforContext context, String assetCode, String organization) throws InforException {
        AssetEquipment assetEquipment = readInforAsset(context, assetCode, organization);
        //
        Equipment asset = tools.getInforFieldTools().transformInforObject(new Equipment(), assetEquipment, context);
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

    private AssetParentHierarchy readInforAssetHierarchy(InforContext context, String assetCode, String organization) throws InforException {
        MP0327_GetAssetParentHierarchy_001 getassetph = new MP0327_GetAssetParentHierarchy_001();
        getassetph.setASSETID(new EQUIPMENTID_Type());
        getassetph.getASSETID().setORGANIZATIONID(tools.getOrganization(context, organization));
        getassetph.getASSETID().setEQUIPMENTCODE(assetCode);

        MP0327_GetAssetParentHierarchy_001_Result result = tools.performInforOperation(context, inforws::getAssetParentHierarchyOp, getassetph);

        return result.getResultData().getAssetParentHierarchy();
    }

    private AssetEquipment readInforAsset(InforContext context, String assetCode, String organization)
            throws InforException {
        MP0302_GetAssetEquipment_001 getAsset = new MP0302_GetAssetEquipment_001();
        getAsset.setASSETID(new EQUIPMENTID_Type());
        getAsset.getASSETID().setORGANIZATIONID(tools.getOrganization(context, organization));
        getAsset.getASSETID().setEQUIPMENTCODE(assetCode);

        MP0302_GetAssetEquipment_001_Result getAssetResult = tools.performInforOperation(context, inforws::getAssetEquipmentOp, getAsset);
        getAssetResult.getResultData().getAssetEquipment().setAssetParentHierarchy(readInforAssetHierarchy(context, assetCode, organization));

        return getAssetResult.getResultData().getAssetEquipment();
    }

    public String updateAsset(InforContext context, Equipment assetParam) throws InforException {
        AssetEquipment assetEquipment = readInforAsset(context, assetParam.getCode(), assetParam.getOrganization());

        //
        assetEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
            context,
            toCodeString(assetEquipment.getCLASSID()),
            assetEquipment.getUSERDEFINEDAREA(),
            assetParam.getClassCode(),
            "OBJ")
        );

        initializeAssetObject(assetEquipment, assetParam, context);
        tools.getInforFieldTools().transformWSHubObject(assetEquipment, assetParam, context);

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
        tools.performInforOperation(context, inforws::syncAssetEquipmentOp, syncAsset);
        userDefinedListService.writeUDLToEntity(context, assetParam, new EntityId("OBJ", assetParam.getCode()));

        return assetParam.getCode();
    }

    public String createAsset(InforContext context, Equipment assetParam) throws InforException {

        AssetEquipment assetEquipment = new AssetEquipment();
        //
        assetEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
            context,
            toCodeString(assetEquipment.getCLASSID()),
            assetEquipment.getUSERDEFINEDAREA(),
            assetParam.getClassCode(),
            "OBJ"));

        //
        initializeAssetObject(assetEquipment, assetParam, context);
        tools.getInforFieldTools().transformWSHubObject(assetEquipment, assetParam, context);
        //
        MP0301_AddAssetEquipment_001 addAsset = new MP0301_AddAssetEquipment_001();
        addAsset.setAssetEquipment(assetEquipment);
        MP0301_AddAssetEquipment_001_Result addAssetResult =
            tools.performInforOperation(context, inforws::addAssetEquipmentOp, addAsset);
        String equipmentCode = addAssetResult.getResultData().getASSETID().getEQUIPMENTCODE();
        userDefinedListService.writeUDLToEntityCopyFrom(context, assetParam, new EntityId("OBJ", equipmentCode));
        return equipmentCode;
    }

    public String deleteAsset(InforContext context, String assetCode, String organization) throws InforException {
        MP0304_DeleteAssetEquipment_001 deleteAsset = new MP0304_DeleteAssetEquipment_001();
        deleteAsset.setASSETID(new EQUIPMENTID_Type());
        deleteAsset.getASSETID().setORGANIZATIONID(tools.getOrganization(context, organization));
        deleteAsset.getASSETID().setEQUIPMENTCODE(assetCode);

        tools.performInforOperation(context, inforws::deleteAssetEquipmentOp, deleteAsset);
        userDefinedListService.deleteUDLFromEntity(context, new EntityId("OBJ", assetCode));
        return assetCode;
    }

    private void initializeAssetObject(AssetEquipment assetInfor, Equipment assetParam, InforContext context) throws InforException {
        // == null means Asset creation
        if (assetInfor.getASSETID() == null) {
            assetInfor.setASSETID(new EQUIPMENTID_Type());
            assetInfor.getASSETID().setORGANIZATIONID(tools.getOrganization(context, assetParam.getOrganization()));
            assetInfor.getASSETID().setEQUIPMENTCODE(assetParam.getCode().toUpperCase().trim());
        }

        if (assetParam.getDescription() != null) {
            assetInfor.getASSETID().setDESCRIPTION(assetParam.getDescription());
        }

        // HIERARCHY
        if (assetParam.getHierarchyAssetCode() != null
                || assetParam.getHierarchyPositionCode() != null
                || assetParam.getHierarchyPrimarySystemCode() != null
                || assetParam.getHierarchyLocationCode() != null) {
            try {
                initializeAssetHierarchy(assetInfor, assetParam, context);
            } catch (Exception e) {
                tools.log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void initializeAssetHierarchy(AssetEquipment assetInfor, Equipment assetParam, InforContext context) throws InforException {
        AssetParentHierarchy hierarchy = new AssetParentHierarchy();

        hierarchy.setASSETID(new EQUIPMENTID_Type());
        hierarchy.getASSETID().setEQUIPMENTCODE(assetParam.getCode());
        hierarchy.getASSETID().setORGANIZATIONID(tools.getOrganization(context, assetParam.getOrganization()));
        hierarchy.setTYPE(new TYPE_Type());
        hierarchy.getTYPE().setTYPECODE("A");

        // Fetch all possible parent types that are present in only one object that indicates the current hierarchy type
        ASSETPARENT_Type assetParent = readAssetParent(assetInfor.getAssetParentHierarchy());
        POSITIONPARENT_Type positionParent = readPositionParent(assetInfor.getAssetParentHierarchy());
        SYSTEMPARENT_Type primarySystemParent = readPrimarySystemParent(assetInfor.getAssetParentHierarchy());
        LOCATIONPARENT_Type locationParent = readLocationParent(assetInfor.getAssetParentHierarchy());
        List<SYSTEMPARENT_Type> systemParents = readSystemsParent(assetInfor.getAssetParentHierarchy());
        HIERARCHY_TYPE currentHierarchyType = readHierarchyType(assetInfor.getAssetParentHierarchy());

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

        assetInfor.setAssetParentHierarchy(hierarchy);
    }

}
