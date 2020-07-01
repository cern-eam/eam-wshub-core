package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.AssetService;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.EntityId;
import ch.cern.eam.wshub.core.services.userdefinedscreens.impl.UserDefinedListServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.annotations.BooleanType;
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
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.readAssetParent;
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.readPositionParent;
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.readPrimarySystemParent;
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.readLocationParent;
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.readSystemsParent;
import static ch.cern.eam.wshub.core.services.equipment.impl.EquipmentHierarchyTools.readHierarchyType;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("Duplicates")
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
        if (isEmpty(organization)) {
            getAssetEquipmentDefault_001.setORGANIZATIONID(tools.getOrganization(context));
        } else {
            getAssetEquipmentDefault_001.setORGANIZATIONID(new ORGANIZATIONID_Type());
            getAssetEquipmentDefault_001.getORGANIZATIONID().setORGANIZATIONCODE(organization);
        }

        MP0305_GetAssetEquipmentDefault_001_Result result =
                tools.performInforOperation(context, inforws::getAssetEquipmentDefaultOp, getAssetEquipmentDefault_001);

        Equipment equipment = tools.getInforFieldTools().transformInforObject(new Equipment(), result.getResultData().getAssetEquipment());
        equipment.setUserDefinedList(new HashMap<>());
        return equipment;
    }

    public Equipment readAsset(InforContext context, String assetCode) throws InforException {
        AssetEquipment assetEquipment = readInforAsset(context, assetCode);
        //
        Equipment asset = tools.getInforFieldTools().transformInforObject(new Equipment(), assetEquipment);
        asset.setSystemTypeCode("A");

        // ID
        if (assetEquipment.getASSETID() != null) {
            asset.setCode(assetEquipment.getASSETID().getEQUIPMENTCODE());
            asset.setDescription(assetEquipment.getASSETID().getDESCRIPTION());
        }

        // DESCRIPTIONS
        tools.processRunnables(
                () -> asset.setManufacturerDesc(tools.getFieldDescriptionsTools().readManufacturerDesc(context, asset.getManufacturerCode())),
                () -> asset.setBinDesc(tools.getFieldDescriptionsTools().readBinDesc(context, asset.getStoreCode(), asset.getBin())),
                () -> userDefinedListService.readUDLToEntity(context, asset, new EntityId("OBJ", assetCode))
        );

        // HIERARCHY
        assetEquipment.setAssetParentHierarchy(readInforAssetHierarchy(context, assetCode));

        if (assetEquipment.getAssetParentHierarchy().getLOCATIONID() != null) {
            asset.setHierarchyLocationCode(assetEquipment.getAssetParentHierarchy().getLOCATIONID().getLOCATIONCODE());
            asset.setHierarchyLocationDesc(assetEquipment.getAssetParentHierarchy().getLOCATIONID().getDESCRIPTION());
        }

        if (assetEquipment.getAssetParentHierarchy().getPositionDependency() != null) {
            //
            PositionDependency positionDep = assetEquipment.getAssetParentHierarchy().getPositionDependency();

            // Dependent position
            asset.setHierarchyPositionCode(positionDep.getDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
            asset.setHierarchyPositionDesc(positionDep.getDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
            asset.setHierarchyPositionDependent(true);
            asset.setHierarchyPositionCostRollUp(decodeBoolean(positionDep.getDEPENDENTPOSITION().getCOSTROLLUP()));

            // Non dependent asset
            if (positionDep.getNONDEPENDENTASSET() != null) {
                //
                asset.setHierarchyAssetCode(positionDep.getNONDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
                asset.setHierarchyAssetDesc(positionDep.getNONDEPENDENTASSET().getASSETID().getDESCRIPTION());
                asset.setHierarchyAssetCostRollUp(decodeBoolean(positionDep.getNONDEPENDENTASSET().getCOSTROLLUP()));
                asset.setHierarchyAssetDependent(false);
            }

            // Non dependent primary system
            if (positionDep.getNONDEPENDENTPRIMARYSYSTEM() != null) {
                asset.setHierarchyPrimarySystemCode(positionDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
                asset.setHierarchyPrimarySystemDesc(positionDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
                asset.setHierarchyPrimarySystemCostRollUp(decodeBoolean(positionDep.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP()));
                asset.setHierarchyPrimarySystemDependent(false);
            }

        } else if (assetEquipment.getAssetParentHierarchy().getAssetDependency() != null) {
            AssetDependency assetDep = assetEquipment.getAssetParentHierarchy().getAssetDependency();

            // Dependent Asset
            asset.setHierarchyAssetCode(assetDep.getDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
            asset.setHierarchyAssetDesc(assetDep.getDEPENDENTASSET().getASSETID().getDESCRIPTION());
            asset.setHierarchyAssetCostRollUp(decodeBoolean(assetDep.getDEPENDENTASSET().getCOSTROLLUP()));
            asset.setHierarchyAssetDependent(true);

            // Non dependent position
            if (assetDep.getNONDEPENDENTPOSITION() != null) {
                asset.setHierarchyPositionCode(assetDep.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
                asset.setHierarchyPositionDesc(assetDep.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
                asset.setHierarchyPositionDependent(false);
                asset.setHierarchyPositionCostRollUp(decodeBoolean(assetDep.getNONDEPENDENTPOSITION().getCOSTROLLUP()));
            }

            // Non dependent primary system
            if (assetDep.getNONDEPENDENTPRIMARYSYSTEM() != null) {
                asset.setHierarchyPrimarySystemCode(assetDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
                asset.setHierarchyPrimarySystemDesc(assetDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
                asset.setHierarchyPrimarySystemCostRollUp(decodeBoolean(assetDep.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP()));
                asset.setHierarchyPrimarySystemDependent(false);
            }

        } else if (assetEquipment.getAssetParentHierarchy().getSystemDependency() != null) {
            SystemDependency systemDep = assetEquipment.getAssetParentHierarchy().getSystemDependency();

            // Dependent System
            asset.setHierarchySystemCode(systemDep.getDEPENDENTSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
            asset.setHierarchySystemDesc(systemDep.getDEPENDENTSYSTEM().getSYSTEMID().getDESCRIPTION());
            asset.setHierarchySystemCostRollUp(decodeBoolean(systemDep.getDEPENDENTSYSTEM().getCOSTROLLUP()));
            asset.setHierarchySystemDependent(true);

            // Non dependent position
            if (systemDep.getNONDEPENDENTPOSITION() != null) {
                asset.setHierarchyPositionCode(systemDep.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
                asset.setHierarchyPositionDesc(systemDep.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
                asset.setHierarchyPositionDependent(false);
                asset.setHierarchyPositionCostRollUp(decodeBoolean(systemDep.getNONDEPENDENTPOSITION().getCOSTROLLUP()));
            }

            // Non dependent asset
            if (systemDep.getNONDEPENDENTASSET() != null) {
                asset.setHierarchyAssetCode(systemDep.getNONDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
                asset.setHierarchyAssetDesc(systemDep.getNONDEPENDENTASSET().getASSETID().getDESCRIPTION());
                asset.setHierarchyAssetCostRollUp(decodeBoolean(systemDep.getNONDEPENDENTASSET().getCOSTROLLUP()));
                asset.setHierarchyAssetDependent(false);
            }

            // Non dependent primary system
            if (systemDep.getNONDEPENDENTPRIMARYSYSTEM() != null) {
                asset.setHierarchyPrimarySystemCode(systemDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
                asset.setHierarchyPrimarySystemDesc(systemDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
                asset.setHierarchyPrimarySystemCostRollUp(decodeBoolean(systemDep.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP()));
                asset.setHierarchyPrimarySystemDependent(false);
            }

        } else if (assetEquipment.getAssetParentHierarchy().getPrimarySystemDependency() != null) {
            PrimarySystemDependency primarySystemDependency = assetEquipment.getAssetParentHierarchy().getPrimarySystemDependency();

            // Dependent Primary System
            asset.setHierarchyPrimarySystemCode(primarySystemDependency.getDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
            asset.setHierarchyPrimarySystemDesc(primarySystemDependency.getDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
            asset.setHierarchyPrimarySystemCostRollUp(decodeBoolean(primarySystemDependency.getDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP()));
            asset.setHierarchyPrimarySystemDependent(true);

            // Non dependent position
            if (primarySystemDependency.getNONDEPENDENTPOSITION() != null) {
                asset.setHierarchyPositionCode(primarySystemDependency.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
                asset.setHierarchyPositionDesc(primarySystemDependency.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
                asset.setHierarchyPositionDependent(false);
                asset.setHierarchyPositionCostRollUp(decodeBoolean(primarySystemDependency.getNONDEPENDENTPOSITION().getCOSTROLLUP()));
            }

            // Non dependent asset
            if (primarySystemDependency.getNONDEPENDENTASSET() != null) {
                asset.setHierarchyAssetCode(primarySystemDependency.getNONDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
                asset.setHierarchyAssetDesc(primarySystemDependency.getNONDEPENDENTASSET().getASSETID().getDESCRIPTION());
                asset.setHierarchyAssetCostRollUp(decodeBoolean(primarySystemDependency.getNONDEPENDENTASSET().getCOSTROLLUP()));
                asset.setHierarchyAssetDependent(false);
            }

        } else if (assetEquipment.getAssetParentHierarchy().getNonDependentParents() != null) {
            NonDependentParents_Type nonDepParents = assetEquipment.getAssetParentHierarchy().getNonDependentParents();

            // Non dependent asset
            if (nonDepParents.getNONDEPENDENTASSET() != null) {
                asset.setHierarchyAssetCode(nonDepParents.getNONDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
                asset.setHierarchyAssetDesc(nonDepParents.getNONDEPENDENTASSET().getASSETID().getDESCRIPTION());
                asset.setHierarchyAssetCostRollUp(decodeBoolean(nonDepParents.getNONDEPENDENTASSET().getCOSTROLLUP()));
                asset.setHierarchyAssetDependent(false);
            }

            // Non dependent position
            if (nonDepParents.getNONDEPENDENTPOSITION() != null) {
                asset.setHierarchyPositionCode(nonDepParents.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
                asset.setHierarchyPositionDesc(nonDepParents.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
                asset.setHierarchyPositionDependent(false);
                asset.setHierarchyPositionCostRollUp(decodeBoolean(nonDepParents.getNONDEPENDENTPOSITION().getCOSTROLLUP()));
            }

            // Non dependent primary system
            if (nonDepParents.getNONDEPENDENTPRIMARYSYSTEM() != null) {
                asset.setHierarchyPrimarySystemCode(nonDepParents.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
                asset.setHierarchyPrimarySystemDesc(nonDepParents.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
                asset.setHierarchyPrimarySystemCostRollUp(decodeBoolean(nonDepParents.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP()));
                asset.setHierarchyPrimarySystemDependent(false);
            }

        } else if (assetEquipment.getAssetParentHierarchy().getLocationDependency() != null) {
            LocationDependency locationDep = assetEquipment.getAssetParentHierarchy().getLocationDependency();

            // Location dependent
            asset.setHierarchyLocationCode(locationDep.getDEPENDENTLOCATION().getLOCATIONID().getLOCATIONCODE());
            asset.setHierarchyLocationDesc(locationDep.getDEPENDENTLOCATION().getLOCATIONID().getDESCRIPTION());

            // Non dependent asset
            if (locationDep.getNONDEPENDENTASSET() != null) {
                asset.setHierarchyAssetCode(locationDep.getNONDEPENDENTASSET().getASSETID().getEQUIPMENTCODE());
                asset.setHierarchyAssetDesc(locationDep.getNONDEPENDENTASSET().getASSETID().getDESCRIPTION());
                asset.setHierarchyAssetCostRollUp(decodeBoolean(locationDep.getNONDEPENDENTASSET().getCOSTROLLUP()));
                asset.setHierarchyAssetDependent(false);
            }

            // Non dependent position
            if (locationDep.getNONDEPENDENTPOSITION() != null) {
                asset.setHierarchyPositionCode(locationDep.getNONDEPENDENTPOSITION().getPOSITIONID().getEQUIPMENTCODE());
                asset.setHierarchyPositionDesc(locationDep.getNONDEPENDENTPOSITION().getPOSITIONID().getDESCRIPTION());
                asset.setHierarchyPositionDependent(false);
                asset.setHierarchyPositionCostRollUp(decodeBoolean(locationDep.getNONDEPENDENTPOSITION().getCOSTROLLUP()));
            }

            // Non dependent primary system
            if (locationDep.getNONDEPENDENTPRIMARYSYSTEM() != null) {
                asset.setHierarchyPrimarySystemCode(locationDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getEQUIPMENTCODE());
                asset.setHierarchyPrimarySystemDesc(locationDep.getNONDEPENDENTPRIMARYSYSTEM().getSYSTEMID().getDESCRIPTION());
                asset.setHierarchyPrimarySystemCostRollUp(decodeBoolean(locationDep.getNONDEPENDENTPRIMARYSYSTEM().getCOSTROLLUP()));
                asset.setHierarchyPrimarySystemDependent(false);
            }
        }

        return asset;
    }

    private AssetParentHierarchy readInforAssetHierarchy(InforContext context, String assetCode)
            throws InforException {
        MP0327_GetAssetParentHierarchy_001 getassetph = new MP0327_GetAssetParentHierarchy_001();
        getassetph.setASSETID(new EQUIPMENTID_Type());
        getassetph.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
        getassetph.getASSETID().setEQUIPMENTCODE(assetCode);

        MP0327_GetAssetParentHierarchy_001_Result result = tools.performInforOperation(context, inforws::getAssetParentHierarchyOp, getassetph);

        return result.getResultData().getAssetParentHierarchy();

    }

    private AssetEquipment readInforAsset(InforContext context, String assetCode)
            throws InforException {
        MP0302_GetAssetEquipment_001 getAsset = new MP0302_GetAssetEquipment_001();
        getAsset.setASSETID(new EQUIPMENTID_Type());
        getAsset.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
        getAsset.getASSETID().setEQUIPMENTCODE(assetCode);

        MP0302_GetAssetEquipment_001_Result getAssetResult = tools.performInforOperation(context, inforws::getAssetEquipmentOp, getAsset);
        getAssetResult.getResultData().getAssetEquipment().setAssetParentHierarchy(readInforAssetHierarchy(context, assetCode));

        return getAssetResult.getResultData().getAssetEquipment();
    }

    private void updateInforAsset(InforContext context, AssetEquipment assetEquipment)
            throws InforException {
        MP0303_SyncAssetEquipment_001 syncAsset = new MP0303_SyncAssetEquipment_001();
        syncAsset.setAssetEquipment(assetEquipment);
        tools.performInforOperation(context, inforws::syncAssetEquipmentOp, syncAsset);
    }

    public String updateAsset(InforContext context, Equipment assetParam) throws InforException {
        AssetEquipment assetEquipment = readInforAsset(context, assetParam.getCode());

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
        if (assetParam.getPartCode() != null && assetParam.getPartCode().equals("")
                    && assetEquipment.getPartAssociation() != null) {
            assetEquipment.getPartAssociation().setSTORELOCATION(null);
            assetEquipment.getPartAssociation().getPARTID().getORGANIZATIONID().setORGANIZATIONCODE("");
        }
        //
        // UPDATE EQUIPMENT
        //
        this.updateInforAsset(context, assetEquipment);
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

    public String deleteAsset(InforContext context, String assetCode) throws InforException {
        MP0304_DeleteAssetEquipment_001 deleteAsset = new MP0304_DeleteAssetEquipment_001();
        deleteAsset.setASSETID(new EQUIPMENTID_Type());
        deleteAsset.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
        deleteAsset.getASSETID().setEQUIPMENTCODE(assetCode);

        tools.performInforOperation(context, inforws::deleteAssetEquipmentOp, deleteAsset);
        userDefinedListService.deleteUDLFromEntity(context, new EntityId("OBJ", assetCode));
        return assetCode;
    }

    private void initializeAssetObject(AssetEquipment assetInfor, Equipment assetParam, InforContext context) throws InforException {
        // == null means Asset creation
        if (assetInfor.getASSETID() == null) {
            assetInfor.setASSETID(new EQUIPMENTID_Type());
            assetInfor.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
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
            initializeAssetHierarchy(assetInfor, assetParam, context);
        }
    }

    enum HIERARCHY_TYPE {ASSET_DEP, POSITION_DEP, PRIM_SYSTEM_DEP, LOCATION_DEP, SYSTEM_DEP, NON_DEP_PARENTS};

    private void initializeAssetHierarchy(AssetEquipment assetInfor, Equipment assetParam, InforContext context) throws InforException {
        AssetParentHierarchy hierarchy = new AssetParentHierarchy();

        hierarchy.setASSETID(new EQUIPMENTID_Type());
        hierarchy.getASSETID().setEQUIPMENTCODE(assetParam.getCode());
        hierarchy.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
        hierarchy.setTYPE(new TYPE_Type());
        hierarchy.getTYPE().setTYPECODE("A");

        //
        ASSETPARENT_Type assetParent = readAssetParent(assetInfor.getAssetParentHierarchy());
        POSITIONPARENT_Type positionParent = readPositionParent(assetInfor.getAssetParentHierarchy());
        SYSTEMPARENT_Type primarySystemParent = readPrimarySystemParent(assetInfor.getAssetParentHierarchy());
        LOCATIONPARENT_Type locationParent = readLocationParent(assetInfor.getAssetParentHierarchy());
        List<SYSTEMPARENT_Type> systemParents = readSystemsParent(assetInfor.getAssetParentHierarchy());
        HIERARCHY_TYPE currentHierarchyType = readHierarchyType(assetInfor.getAssetParentHierarchy());

        assetParent = createHierarchyAsset(context, assetParam.getHierarchyAssetCode(), assetParam.getHierarchyAssetCostRollUp(), assetParent);
        positionParent = createHierarchyPosition(context, assetParam.getHierarchyPositionCode(), assetParam.getHierarchyPositionCostRollUp(), positionParent);
        primarySystemParent = createHierarchyPrimarySystem(context, assetParam.getHierarchyPrimarySystemCode(), assetParam.getHierarchyPrimarySystemCostRollUp(), primarySystemParent);
        locationParent = createHierarchyLocation(context, assetParam.getHierarchyLocationCode(), locationParent);

        switch (getNewHierarchyType(assetParam, currentHierarchyType)) {
            case ASSET_DEP:
                hierarchy.setAssetDependency(createAssetDependency(assetParent, positionParent, primarySystemParent, systemParents));
                break;
            case POSITION_DEP:
                hierarchy.setPositionDependency(createPositionDependency(assetParent, positionParent, primarySystemParent, systemParents));
                break;
            case PRIM_SYSTEM_DEP:
                hierarchy.setPrimarySystemDependency(createPrimarySystemDependency(assetParent, positionParent, primarySystemParent, systemParents));
                break;
            case LOCATION_DEP:
                hierarchy.setLocationDependency(createLocationDependency(assetParent, positionParent, primarySystemParent, systemParents, locationParent));
                break;
            default:
                hierarchy.setNonDependentParents(createNonDependentParents(assetParent, positionParent, primarySystemParent, systemParents));
        }

        assetInfor.setAssetParentHierarchy(hierarchy);
    }

    //TODO complete the logic determining new hierarchy type based on the old one and input params
    private HIERARCHY_TYPE getNewHierarchyType(Equipment assetParam, HIERARCHY_TYPE currentHierarchyType) {
        if (assetParam.getHierarchyAssetDependent() != null && assetParam.getHierarchyAssetDependent()) {
            return HIERARCHY_TYPE.ASSET_DEP;
        } else if (assetParam.getHierarchyPositionDependent() != null && assetParam.getHierarchyPositionDependent()) {
            return HIERARCHY_TYPE.POSITION_DEP;
        } else if (assetParam.getHierarchyPrimarySystemDependent() != null && assetParam.getHierarchyPrimarySystemDependent()) {
            return HIERARCHY_TYPE.PRIM_SYSTEM_DEP;
        } else if (isNotEmpty(assetParam.getHierarchyLocationCode())) {
            return HIERARCHY_TYPE.LOCATION_DEP;
        } else if (currentHierarchyType == HIERARCHY_TYPE.ASSET_DEP && assetParam.getHierarchyAssetDependent() == null){
            return HIERARCHY_TYPE.ASSET_DEP;
        } else if (currentHierarchyType == HIERARCHY_TYPE.POSITION_DEP && assetParam.getHierarchyPositionDependent() == null){
            return HIERARCHY_TYPE.POSITION_DEP;
        } else if (currentHierarchyType == HIERARCHY_TYPE.PRIM_SYSTEM_DEP && assetParam.getHierarchyPrimarySystemDependent() == null){
            return HIERARCHY_TYPE.PRIM_SYSTEM_DEP;
        } else if (currentHierarchyType == HIERARCHY_TYPE.LOCATION_DEP){
            return HIERARCHY_TYPE.LOCATION_DEP;
        } else if (currentHierarchyType == HIERARCHY_TYPE.SYSTEM_DEP){
            return HIERARCHY_TYPE.SYSTEM_DEP;
        } else {
            return HIERARCHY_TYPE.NON_DEP_PARENTS;
        }
    }

    private AssetDependency createAssetDependency(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        AssetDependency assetDependency = new AssetDependency();
        assetDependency.setDEPENDENTASSET(assetParent);
        assetDependency.setNONDEPENDENTPOSITION(positionParent);
        assetDependency.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        assetDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        return assetDependency;
    }

    private PositionDependency createPositionDependency(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        PositionDependency positionDependency = new PositionDependency();
        positionDependency.setNONDEPENDENTASSET(assetParent);
        positionDependency.setDEPENDENTPOSITION(positionParent);
        positionDependency.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        positionDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        return positionDependency;
    }

    private PrimarySystemDependency createPrimarySystemDependency(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        PrimarySystemDependency positionDependency = new PrimarySystemDependency();
        positionDependency.setNONDEPENDENTASSET(assetParent);
        positionDependency.setNONDEPENDENTPOSITION(positionParent);
        positionDependency.setDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        positionDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        return positionDependency;
    }

    private LocationDependency createLocationDependency(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents, LOCATIONPARENT_Type locationParent) {
        LocationDependency locationDependency = new LocationDependency();
        locationDependency.setNONDEPENDENTASSET(assetParent);
        locationDependency.setNONDEPENDENTPOSITION(positionParent);
        locationDependency.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        locationDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        locationDependency.setDEPENDENTLOCATION(locationParent);
        return locationDependency;
    }

    private NonDependentParents_Type createNonDependentParents(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        NonDependentParents_Type nonDependentParents = new NonDependentParents_Type();
        nonDependentParents.setNONDEPENDENTASSET(assetParent);
        nonDependentParents.setNONDEPENDENTPOSITION(positionParent);
        nonDependentParents.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        nonDependentParents.getNONDEPENDENTSYSTEM().addAll(systemParents);
        return nonDependentParents;
    }

    private ASSETPARENT_Type createHierarchyAsset(InforContext context, String assetCode, Boolean costRollUp, ASSETPARENT_Type oldHierarchyAsset) {
        if (assetCode == null) {
            return oldHierarchyAsset;
        }

        if (assetCode.equals("")) {
            return null;
        }

        ASSETPARENT_Type assetType = new ASSETPARENT_Type();
        assetType.setASSETID(new EQUIPMENTID_Type());
        assetType.getASSETID().setEQUIPMENTCODE(assetCode);
        assetType.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
        assetType.setCOSTROLLUP(encodeBoolean(costRollUp, BooleanType.TRUE_FALSE));
        return assetType;
    }

    private POSITIONPARENT_Type createHierarchyPosition(InforContext context, String positionCode, Boolean costRollUp, POSITIONPARENT_Type oldHierarchyPosition) {
        if (positionCode == null) {
            return oldHierarchyPosition;
        }

        if (positionCode.equals("")) {
            return null;
        }

        POSITIONPARENT_Type positionType = new POSITIONPARENT_Type();
        positionType.setPOSITIONID(new EQUIPMENTID_Type());
        positionType.getPOSITIONID().setEQUIPMENTCODE(positionCode);
        positionType.getPOSITIONID().setORGANIZATIONID(tools.getOrganization(context));
        positionType.setCOSTROLLUP(encodeBoolean(costRollUp, BooleanType.TRUE_FALSE));
        return positionType;
    }

    private SYSTEMPARENT_Type createHierarchyPrimarySystem(InforContext context, String systemCode, Boolean costRollUp, SYSTEMPARENT_Type oldSystemHierarchy) {
        if (systemCode == null) {
            return oldSystemHierarchy;
        }

        if (systemCode.equals("")) {
            return null;
        }

        SYSTEMPARENT_Type systemType = new SYSTEMPARENT_Type();
        systemType.setSYSTEMID(new EQUIPMENTID_Type());
        systemType.getSYSTEMID().setEQUIPMENTCODE(systemCode);
        systemType.getSYSTEMID().setORGANIZATIONID(tools.getOrganization(context));
        systemType.setCOSTROLLUP(encodeBoolean(costRollUp, BooleanType.TRUE_FALSE));
        return systemType;
    }

    private LOCATIONPARENT_Type createHierarchyLocation(InforContext context, String locationCode, LOCATIONPARENT_Type oldLocationHierarchy) {
        if (locationCode == null) {
            return oldLocationHierarchy;
        }

        if (locationCode.equals("")) {
            return null;
        }

        LOCATIONPARENT_Type locationType = new LOCATIONPARENT_Type();
        locationType.setLOCATIONID(new LOCATIONID_Type());
        locationType.getLOCATIONID().setLOCATIONCODE(locationCode);
        locationType.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
        return locationType;
    }

}
