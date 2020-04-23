package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.AssetService;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.assetequipment_001.*;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
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

import javax.xml.ws.Holder;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class AssetServiceImpl implements AssetService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public AssetServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
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

        return tools.getInforFieldTools().transformInforObject(new Equipment(), result.getResultData().getAssetEquipment());
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
        List<Runnable> runnables = new LinkedList<>();
        runnables.add(() -> asset.setManufacturerDesc(tools.getFieldDescriptionsTools().readManufacturerDesc(context, asset.getManufacturerCode())));
        runnables.add(() -> asset.setBinDesc(tools.getFieldDescriptionsTools().readBinDesc(context, asset.getStoreCode(), asset.getBin())));
        tools.processRunnables(runnables);

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

        MP0327_GetAssetParentHierarchy_001_Result result =
            tools.performInforOperation(context, inforws::getAssetParentHierarchyOp, getassetph);

        return result.getResultData().getAssetParentHierarchy();

    }

    private AssetEquipment readInforAsset(InforContext context, String assetCode)
            throws InforException {
        MP0302_GetAssetEquipment_001 getAsset = new MP0302_GetAssetEquipment_001();
        getAsset.setASSETID(new EQUIPMENTID_Type());
        getAsset.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
        getAsset.getASSETID().setEQUIPMENTCODE(assetCode);

        MP0302_GetAssetEquipment_001_Result getAssetResult =
            tools.performInforOperation(context, inforws::getAssetEquipmentOp, getAsset);

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

        return addAssetResult.getResultData().getASSETID().getEQUIPMENTCODE();
    }

    public String deleteAsset(InforContext context, String assetCode) throws InforException {
        MP0304_DeleteAssetEquipment_001 deleteAsset = new MP0304_DeleteAssetEquipment_001();
        deleteAsset.setASSETID(new EQUIPMENTID_Type());
        deleteAsset.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
        deleteAsset.getASSETID().setEQUIPMENTCODE(assetCode);

        tools.performInforOperation(context, inforws::deleteAssetEquipmentOp, deleteAsset);
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
        } else {
            // Setting to null won't touch the existing structure
            assetInfor.setAssetParentHierarchy(null);
        }
    }

    private void initializeAssetHierarchy(AssetEquipment assetInfor, Equipment assetParam, InforContext context) {
        AssetParentHierarchy assetParentHierarchy = new AssetParentHierarchy();

        assetParentHierarchy.setASSETID(new EQUIPMENTID_Type());
        assetParentHierarchy.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
        assetParentHierarchy.getASSETID().setEQUIPMENTCODE(assetParam.getCode());
        assetParentHierarchy.setTYPE(new TYPE_Type());
        assetParentHierarchy.getTYPE().setTYPECODE(assetParam.getTypeCode());

        EQUIPMENTID_Type hierarchyAsset = new EQUIPMENTID_Type();
        hierarchyAsset.setORGANIZATIONID(tools.getOrganization(context));
        hierarchyAsset.setEQUIPMENTCODE(assetParam.getHierarchyAssetCode());

        EQUIPMENTID_Type hierarchyPosition = new EQUIPMENTID_Type();
        hierarchyPosition.setORGANIZATIONID(tools.getOrganization(context));
        hierarchyPosition.setEQUIPMENTCODE(assetParam.getHierarchyPositionCode());

        EQUIPMENTID_Type hierarchySystem = new EQUIPMENTID_Type();
        hierarchySystem.setORGANIZATIONID(tools.getOrganization(context));
        hierarchySystem.setEQUIPMENTCODE(assetParam.getHierarchyPrimarySystemCode());

        // Asset dependent
        if (assetParam.getHierarchyAssetDependent() != null && assetParam.getHierarchyAssetDependent() && tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyAssetCode())) {

            assetParentHierarchy.setAssetDependency(new AssetDependency());
            // Non dependent position
            if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyPositionCode())) {
                assetParentHierarchy.getAssetDependency().setNONDEPENDENTPOSITION(this.createHierarchyPosition(assetParam, hierarchyPosition));
            }

            // Non dependent system
            if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyPrimarySystemCode())) {
                assetParentHierarchy.getAssetDependency().setNONDEPENDENTPRIMARYSYSTEM(this.createHierarchyPrymarySystem(assetParam, hierarchySystem));
            }

            // Dependent asset
            assetParentHierarchy.getAssetDependency().setDEPENDENTASSET(this.createHierarchyAsset(assetParam, hierarchyAsset));
        }
        // Position dependent
        else if (assetParam.getHierarchyPositionDependent() != null && assetParam.getHierarchyPositionDependent() && tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyPositionCode())) {
            assetParentHierarchy.setPositionDependency(new PositionDependency());

            // Non dependent asset
            if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyAssetCode())) {
                assetParentHierarchy.getPositionDependency()
                        .setNONDEPENDENTASSET(this.createHierarchyAsset(assetParam, hierarchyAsset));
            }

            // Non dependent system
            if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyPrimarySystemCode())) {
                assetParentHierarchy.getPositionDependency()
                        .setNONDEPENDENTPRIMARYSYSTEM(this.createHierarchyPrymarySystem(assetParam, hierarchySystem));
            }

            // Dependent position
            assetParentHierarchy.getPositionDependency()
                    .setDEPENDENTPOSITION(this.createHierarchyPosition(assetParam, hierarchyPosition));
        }
        // System dependent
        else if (assetParam.getHierarchyPrimarySystemDependent() != null && assetParam.getHierarchyPrimarySystemDependent() && tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyPrimarySystemCode())) {

            assetParentHierarchy.setPrimarySystemDependency(new PrimarySystemDependency());
            // Non dependent position
            if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyPositionCode())) {
                assetParentHierarchy.getPrimarySystemDependency()
                        .setNONDEPENDENTPOSITION(this.createHierarchyPosition(assetParam, hierarchyPosition));
            }

            // Non dependent asset
            if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyAssetCode())) {
                assetParentHierarchy.getPrimarySystemDependency()
                        .setNONDEPENDENTASSET(this.createHierarchyAsset(assetParam, hierarchyAsset));
            }

            // Dependent System
            assetParentHierarchy.getPrimarySystemDependency()
                    .setDEPENDENTPRIMARYSYSTEM(this.createHierarchyPrymarySystem(assetParam, hierarchySystem));
        }
        // Location dependent
        else if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyLocationCode())) {

            assetParentHierarchy.setLocationDependency(new LocationDependency());
            // Dependent location
            assetParentHierarchy.getLocationDependency().setDEPENDENTLOCATION(new LOCATIONPARENT_Type());
            assetParentHierarchy.getLocationDependency().getDEPENDENTLOCATION().setLOCATIONID(new LOCATIONID_Type());
            assetParentHierarchy.getLocationDependency().getDEPENDENTLOCATION().getLOCATIONID()
                    .setORGANIZATIONID(tools.getOrganization(context));
            assetParentHierarchy.getLocationDependency().getDEPENDENTLOCATION().getLOCATIONID()
                    .setLOCATIONCODE(assetParam.getHierarchyLocationCode());

            // There is position
            if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyPositionCode())) {
                assetParentHierarchy.getLocationDependency()
                        .setNONDEPENDENTPOSITION(this.createHierarchyPosition(assetParam, hierarchyPosition));
            }

            // There is asset
            if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyAssetCode())) {
                assetParentHierarchy.getLocationDependency()
                        .setNONDEPENDENTASSET(this.createHierarchyAsset(assetParam, hierarchyAsset));
            }

            // There is system
            if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyPrimarySystemCode())) {
                assetParentHierarchy.getLocationDependency().setNONDEPENDENTPRIMARYSYSTEM(
                        this.createHierarchyPrymarySystem(assetParam, hierarchySystem));
            }

        }
        // Non-dependent parents
        else {
            assetParentHierarchy.setNonDependentParents(new NonDependentParents_Type());

            // There is position
            if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyPositionCode())) {
                assetParentHierarchy.getNonDependentParents()
                        .setNONDEPENDENTPOSITION(this.createHierarchyPosition(assetParam, hierarchyPosition));
            }

            // There is asset
            if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyAssetCode())) {
                assetParentHierarchy.getNonDependentParents()
                        .setNONDEPENDENTASSET(this.createHierarchyAsset(assetParam, hierarchyAsset));
            }

            // There is system
            if (tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyPrimarySystemCode())) {
                assetParentHierarchy.getNonDependentParents().setNONDEPENDENTPRIMARYSYSTEM(
                        this.createHierarchyPrymarySystem(assetParam, hierarchySystem));
            }
        }

        assetInfor.setAssetParentHierarchy(assetParentHierarchy);
    }

    private ASSETPARENT_Type createHierarchyAsset(Equipment assetParam, EQUIPMENTID_Type hierarchyAsset) {
        ASSETPARENT_Type assetType = new ASSETPARENT_Type();
        assetType.setASSETID(hierarchyAsset);
        assetType.setCOSTROLLUP(encodeBoolean(assetParam.getHierarchyAssetCostRollUp(), BooleanType.TRUE_FALSE));
        return assetType;
    }

    private POSITIONPARENT_Type createHierarchyPosition(Equipment assetParam, EQUIPMENTID_Type hierarchyPosition) {
        POSITIONPARENT_Type positionType = new POSITIONPARENT_Type();
        positionType.setPOSITIONID(hierarchyPosition);
        positionType.setCOSTROLLUP(encodeBoolean(assetParam.getHierarchyPositionCostRollUp(), BooleanType.TRUE_FALSE));
        return positionType;
    }

    private SYSTEMPARENT_Type createHierarchyPrymarySystem(Equipment assetParam, EQUIPMENTID_Type hierarchySystem) {
        SYSTEMPARENT_Type systemType = new SYSTEMPARENT_Type();
        systemType.setSYSTEMID(hierarchySystem);
        systemType.setCOSTROLLUP(encodeBoolean(assetParam.getHierarchyPrimarySystemCostRollUp(), BooleanType.TRUE_FALSE));
        return systemType;
    }


}
