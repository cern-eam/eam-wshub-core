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
import net.datastream.schemas.mp_functions.mp0327_001.MP0327_GetAssetParentHierarchy_001;
import net.datastream.schemas.mp_results.mp0301_001.MP0301_AddAssetEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0302_001.MP0302_GetAssetEquipment_001_Result;
import net.datastream.schemas.mp_results.mp0327_001.MP0327_GetAssetParentHierarchy_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;

import javax.xml.ws.Holder;

public class AssetServiceImpl implements AssetService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public AssetServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    // Replace Equipment modes
    private static final String STANDARD = "Standard";
    private static final String SWAPPING = "Swapping";


    public Equipment readAsset(InforContext context, String assetCode) throws InforException {
        AssetEquipment assetEquipment = readInforAsset(context, assetCode);
        //
        Equipment asset = tools.getInforFieldTools().transformInforObject(new Equipment(), assetEquipment);

        if (assetEquipment.getASSETID() != null) {
            asset.setCode(assetEquipment.getASSETID().getEQUIPMENTCODE());
            asset.setDescription(assetEquipment.getASSETID().getDESCRIPTION());
        }

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

        MP0327_GetAssetParentHierarchy_001_Result result;
        if (context.getCredentials() != null) {
            result = inforws.getAssetParentHierarchyOp(getassetph, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    null, tools.getTenant(context));
        } else {
            result = inforws.getAssetParentHierarchyOp(getassetph, tools.getOrganizationCode(context), null, null,
                    new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
        }

        return result.getResultData().getAssetParentHierarchy();

    }

    private AssetEquipment readInforAsset(InforContext context, String assetCode)
            throws InforException {
        MP0302_GetAssetEquipment_001 getAsset = new MP0302_GetAssetEquipment_001();
        getAsset.setASSETID(new EQUIPMENTID_Type());
        getAsset.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
        getAsset.getASSETID().setEQUIPMENTCODE(assetCode);
        MP0302_GetAssetEquipment_001_Result getAssetResult = new MP0302_GetAssetEquipment_001_Result();

        if (context.getCredentials() != null) {
            getAssetResult = inforws.getAssetEquipmentOp(getAsset,
                    tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), tools.getTenant(context));
        } else {
            getAssetResult = inforws.getAssetEquipmentOp(getAsset,
                    tools.getOrganizationCode(context), null, null,
                    new Holder<SessionType>(tools.createInforSession(context)),
                    tools.createMessageConfig(),
                    tools.getTenant(context));
        }
        return getAssetResult.getResultData().getAssetEquipment();
    }

    private void updateInforAsset(InforContext context, AssetEquipment assetEquipment)
            throws InforException {
        MP0303_SyncAssetEquipment_001 syncAsset = new MP0303_SyncAssetEquipment_001();
        syncAsset.setAssetEquipment(assetEquipment);
        if (context.getCredentials() != null) {
            inforws.syncAssetEquipmentOp(syncAsset, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), tools.getTenant(context));
        } else {
            inforws.syncAssetEquipmentOp(syncAsset, tools.getOrganizationCode(context), null, null,
                    new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
        }
    }

    public String updateAsset(InforContext context, Equipment assetParam) throws InforException {
        AssetEquipment assetEquipment = readInforAsset(context, assetParam.getCode());

        //
        if (assetParam.getClassCode() != null &&
                (assetEquipment.getCLASSID() == null || !assetParam.getClassCode().toUpperCase().equals(assetEquipment.getCLASSID().getCLASSCODE()))) {
            assetEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(context, "OBJ", assetParam.getClassCode().toUpperCase()));
        }

        initializeAssetObject(assetEquipment, assetParam, context);
        tools.getInforFieldTools().transformWSHubObject(assetEquipment, assetParam, context);
        //
        // UPDATE EQUIPMENT
        //
        this.updateInforAsset(context, assetEquipment);

        return assetParam.getCode();
    }

    public String createAsset(InforContext context, Equipment assetParam) throws InforException {

        AssetEquipment assetEquipment = new AssetEquipment();
        //
        if (assetParam.getCustomFields() != null && assetParam.getCustomFields().length > 0) {
            if (isNotEmpty(assetParam.getClassCode())) {
                assetEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(context, "OBJ", assetParam.getClassCode()));
            } else {
                assetEquipment.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(context, "OBJ", "*"));
            }
        }

        //
        initializeAssetObject(assetEquipment, assetParam, context);
        tools.getInforFieldTools().transformWSHubObject(assetEquipment, assetParam, context);
        //
        MP0301_AddAssetEquipment_001 addAsset = new MP0301_AddAssetEquipment_001();
        addAsset.setAssetEquipment(assetEquipment);
        MP0301_AddAssetEquipment_001_Result addAssetResult = null;

        if (context.getCredentials() != null) {
            addAssetResult = inforws.addAssetEquipmentOp(addAsset, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), tools.getTenant(context));
        } else {
            addAssetResult = inforws.addAssetEquipmentOp(addAsset, tools.getOrganizationCode(context), null, null,
                    new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
        }

        return addAssetResult.getResultData().getASSETID().getEQUIPMENTCODE();
    }

    public String deleteAsset(InforContext context, String assetCode) throws InforException {
        MP0304_DeleteAssetEquipment_001 deleteAsset = new MP0304_DeleteAssetEquipment_001();
        deleteAsset.setASSETID(new EQUIPMENTID_Type());
        deleteAsset.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
        deleteAsset.getASSETID().setEQUIPMENTCODE(assetCode);

        if (context.getCredentials() != null) {
            inforws.deleteAssetEquipmentOp(deleteAsset, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    null, tools.getTenant(context));
        } else {
            inforws.deleteAssetEquipmentOp(deleteAsset, tools.getOrganizationCode(context), null, null,
                    new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
        }
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

        if (assetParam.getManufacturerCode() != null || assetParam.getSerialNumber() != null
                || assetParam.getModel() != null || assetParam.getRevision() != null
                || assetParam.getxCoordinate() != null || assetParam.getyCoordinate() != null
                || assetParam.getzCoordinate() != null) {
            if (assetInfor.getManufacturerInfo() == null) {
                assetInfor.setManufacturerInfo(new ManufacturerInfo());
            }

            if (assetParam.getManufacturerCode() != null) {
                assetInfor.getManufacturerInfo().setMANUFACTURERCODE(assetParam.getManufacturerCode().toUpperCase());
            }
            if (assetParam.getModel() != null) {
                assetInfor.getManufacturerInfo().setMODEL(assetParam.getModel());
            }
            if (assetParam.getRevision() != null) {
                assetInfor.getManufacturerInfo().setMODELREVISION(assetParam.getRevision());
            }
            if (assetParam.getSerialNumber() != null) {
                assetInfor.getManufacturerInfo().setSERIALNUMBER(assetParam.getSerialNumber());
            }
            if (assetParam.getxCoordinate() != null) {
                assetInfor.getManufacturerInfo()
                        .setXCOORDINATE(tools.getDataTypeTools().encodeQuantity(assetParam.getxCoordinate(), "X-Coordinate"));
            }
            if (assetParam.getyCoordinate() != null) {
                assetInfor.getManufacturerInfo()
                        .setYCOORDINATE(tools.getDataTypeTools().encodeQuantity(assetParam.getyCoordinate(), "Y-Coordinate"));
            }
            if (assetParam.getzCoordinate() != null) {
                assetInfor.getManufacturerInfo()
                        .setZCOORDINATE(tools.getDataTypeTools().encodeQuantity(assetParam.getzCoordinate(), "Z-Coordiante"));
            }
        }

        // PART ASSOCIATION
        if (assetParam.getPartCode() != null) {
            if (assetParam.getPartCode().equals("") && assetInfor.getPartAssociation() == null) {
                // TODO: do nothing for new, refactor it later ...
            } else if (assetParam.getPartCode().equals("") && assetInfor.getPartAssociation() != null
                    && assetInfor.getPartAssociation().getPARTID() != null) {
                assetInfor.getPartAssociation().getPARTID().setPARTCODE("");
                assetInfor.getPartAssociation().getPARTID().getORGANIZATIONID().setORGANIZATIONCODE("");
                assetInfor.getPartAssociation().getPARTID().setDESCRIPTION(null);
                assetInfor.getPartAssociation().setSTORELOCATION(null);
            } else {
                assetInfor.setPartAssociation(new PartAssociation());
                assetInfor.getPartAssociation().setPARTID(new PARTID_Type());
                assetInfor.getPartAssociation().getPARTID().setORGANIZATIONID(tools.getOrganization(context));
                assetInfor.getPartAssociation().getPARTID().setPARTCODE(assetParam.getPartCode().toUpperCase().trim());

                if (assetParam.getStoreCode() != null || assetParam.getLot() != null || assetParam.getBin() != null) {
                    assetInfor.getPartAssociation().setSTORELOCATION(new STORELOCATION());
                    
                    if (assetParam.getStoreCode() != null) {
                        assetInfor.getPartAssociation().getSTORELOCATION().setSTOREID(new STOREID_Type());
                        assetInfor.getPartAssociation().getSTORELOCATION().getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
                        assetInfor.getPartAssociation().getSTORELOCATION().getSTOREID().setSTORECODE(assetParam.getStoreCode().trim().toUpperCase());
                    }
                    if (assetParam.getBin() != null) {
                        assetInfor.getPartAssociation().getSTORELOCATION().setBIN(assetParam.getBin());
                    }
                    if (assetParam.getLot() != null) {
                        assetInfor.getPartAssociation().getSTORELOCATION().setLOT(assetParam.getLot());
                    }
                }
            }
        }

        // LINEAR REFERENCE
        if (assetParam.getLinearRefGeographicalRef() != null || assetParam.getLinearRefEquipmentLength() != null
                || assetParam.getLinearRefEquipmentLengthUOM() != null || assetParam.getLinearRefPrecision() != null
                || assetParam.getLinearRefUOM() != null) {
            assetInfor.setLINEARREFERENCEDETAILS(new LINEARREFERENCEDETAILS_Type());
            assetInfor.getLINEARREFERENCEDETAILS().setEQUIPMENTLENGTH(
                    tools.getDataTypeTools().encodeQuantity(assetParam.getLinearRefEquipmentLength(), "Linear Ref. Equipment Length"));
            assetInfor.getLINEARREFERENCEDETAILS().setEQUIPMENTLENGTHUOM(assetParam.getLinearRefEquipmentLengthUOM());
            assetInfor.getLINEARREFERENCEDETAILS().setGEOGRAPHICALREFERENCE(assetParam.getLinearRefGeographicalRef());
            assetInfor.getLINEARREFERENCEDETAILS().setLINEARREFPRECISION(
                    tools.getDataTypeTools().encodeBigInteger(assetParam.getLinearRefPrecision(), "Linear Ref. Precision"));
            assetInfor.getLINEARREFERENCEDETAILS().setLINEARREFUOM(assetParam.getLinearRefUOM());

        }

        // VARIABLES
        if (assetInfor.getVariables() == null) {
            assetInfor.setVariables(new Variables());
        }

        if (assetParam.getVariable1() != null) {
            assetInfor.getVariables().setVARIABLE1(assetParam.getVariable1());
        }
        if (assetParam.getVariable2() != null) {
            assetInfor.getVariables().setVARIABLE2(assetParam.getVariable2());
        }
        if (assetParam.getVariable3() != null) {
            assetInfor.getVariables().setVARIABLE3(assetParam.getVariable3());
        }
        if (assetParam.getVariable4() != null) {
            assetInfor.getVariables().setVARIABLE4(assetParam.getVariable4());
        }
        if (assetParam.getVariable5() != null) {
            assetInfor.getVariables().setVARIABLE5(assetParam.getVariable5());
        }
        if (assetParam.getVariable6() != null) {
            assetInfor.getVariables().setVARIABLE6(assetParam.getVariable6());
        }

        // FACILITY DETAILS
        if (assetParam.getCostOfNeededRepairs() != null || assetParam.getReplacementValue() != null
                || assetParam.getFacilityConditionIndex() != null || assetParam.getServiceLifetime() != null
                || assetParam.getYearBuilt() != null) {
            if (assetInfor.getFacilityConditionIndex() == null) {
                assetInfor.setFacilityConditionIndex(new FacilityConditionIndex());
            }

            if (assetParam.getCostOfNeededRepairs() != null) {
                assetInfor.getFacilityConditionIndex().setCOSTOFNEEDEDREPAIRS(
                        tools.getDataTypeTools().encodeAmount(assetParam.getCostOfNeededRepairs(), "Cost of Needed Repairs"));
            }

            if (assetParam.getReplacementValue() != null) {
                assetInfor.getFacilityConditionIndex()
                        .setREPLACEMENTVALUE(tools.getDataTypeTools().encodeAmount(assetParam.getReplacementValue(), "Replacement Value"));
            }

            if (assetParam.getFacilityConditionIndex() != null) {
                assetInfor.getFacilityConditionIndex().setFACILITYCONDITIONINDEX(
                        tools.getDataTypeTools().encodeAmount(assetParam.getFacilityConditionIndex(), "Facility Condition Index"));
            }

            if (assetParam.getServiceLifetime() != null) {
                assetInfor.getFacilityConditionIndex()
                        .setSERVICELIFE(tools.getDataTypeTools().encodeQuantity(assetParam.getServiceLifetime(), "Service Life Time"));
            }

            if (assetParam.getYearBuilt() != null) {
                assetInfor.getFacilityConditionIndex()
                        .setYEARBUILT(tools.getDataTypeTools().encodeQuantity(assetParam.getYearBuilt(), "Service Life Time"));
            }
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
        if (assetParam.getHierarchyAssetDependent() && tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyAssetCode())) {

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
        else if (assetParam.getHierarchyPositionDependent() && tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyPositionCode())) {
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
        else if (assetParam.getHierarchyPrimarySystemDependent() && tools.getDataTypeTools().isNotEmpty(assetParam.getHierarchyPrimarySystemCode())) {

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
