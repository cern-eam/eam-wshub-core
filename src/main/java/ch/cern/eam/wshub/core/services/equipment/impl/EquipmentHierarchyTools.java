package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import net.datastream.schemas.mp_entities.assetequipment_001.*;
import net.datastream.schemas.mp_entities.positionhierarchy_002.PositionParentHierarchy;
import net.datastream.schemas.mp_fields.*;
import java.util.List;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.encodeBoolean;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;

public class EquipmentHierarchyTools {

    public enum HIERARCHY_TYPE {ASSET_DEP, POSITION_DEP, PRIM_SYSTEM_DEP, LOCATION_DEP, SYSTEM_DEP, NON_DEP_PARENTS};

    //
    // GET PARENTS FOR ASSET PARENT
    //

    public static ASSETPARENT_Type readAssetParent(AssetParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getAssetDependency() != null) {
            return hierarchy.getAssetDependency().getDEPENDENTASSET();
        } else if (hierarchy.getPositionDependency() != null) {
            return hierarchy.getPositionDependency().getNONDEPENDENTASSET();
        } else if (hierarchy.getPrimarySystemDependency() != null) {
            return hierarchy.getPrimarySystemDependency().getNONDEPENDENTASSET();
        } else if (hierarchy.getSystemDependency() != null) {
            return hierarchy.getSystemDependency().getNONDEPENDENTASSET();
        } else if (hierarchy.getLocationDependency() != null) {
            return hierarchy.getLocationDependency().getNONDEPENDENTASSET();
        } else if (hierarchy.getNonDependentParents() != null) {
            return hierarchy.getNonDependentParents().getNONDEPENDENTASSET();
        }
        return null;
    }

    public static POSITIONPARENT_Type readPositionParent(AssetParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getAssetDependency() != null) {
            return hierarchy.getAssetDependency().getNONDEPENDENTPOSITION();
        } else if (hierarchy.getPositionDependency() != null) {
            return hierarchy.getPositionDependency().getDEPENDENTPOSITION();
        } else if (hierarchy.getPrimarySystemDependency() != null) {
            return hierarchy.getPrimarySystemDependency().getNONDEPENDENTPOSITION();
        } else if (hierarchy.getSystemDependency() != null) {
            return hierarchy.getSystemDependency().getNONDEPENDENTPOSITION();
        } else if (hierarchy.getLocationDependency() != null) {
            return hierarchy.getLocationDependency().getNONDEPENDENTPOSITION();
        } else if (hierarchy.getNonDependentParents() != null) {
            return hierarchy.getNonDependentParents().getNONDEPENDENTPOSITION();
        }
        return null;
    }

    public static SYSTEMPARENT_Type readPrimarySystemParent(AssetParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getAssetDependency() != null) {
            return hierarchy.getAssetDependency().getNONDEPENDENTPRIMARYSYSTEM();
        } else if (hierarchy.getPositionDependency() != null) {
            return hierarchy.getPositionDependency().getNONDEPENDENTPRIMARYSYSTEM();
        } else if (hierarchy.getPrimarySystemDependency() != null) {
            return hierarchy.getPrimarySystemDependency().getDEPENDENTPRIMARYSYSTEM();
        } else if (hierarchy.getSystemDependency() != null) {
            return hierarchy.getSystemDependency().getNONDEPENDENTPRIMARYSYSTEM();
        } else if (hierarchy.getLocationDependency() != null) {
            return hierarchy.getLocationDependency().getNONDEPENDENTPRIMARYSYSTEM();
        } else if (hierarchy.getNonDependentParents() != null) {
            return hierarchy.getNonDependentParents().getNONDEPENDENTPRIMARYSYSTEM();
        }
        return null;
    }

    public static LOCATIONPARENT_Type readLocationParent(AssetParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getLocationDependency() != null) {
            return hierarchy.getLocationDependency().getDEPENDENTLOCATION();
         }
        return null;
    }

    public static List<SYSTEMPARENT_Type> readSystemsParent(AssetParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getAssetDependency() != null) {
            return hierarchy.getAssetDependency().getNONDEPENDENTSYSTEM();
        } else if (hierarchy.getPositionDependency() != null) {
            return hierarchy.getPositionDependency().getNONDEPENDENTSYSTEM();
        } else if (hierarchy.getPrimarySystemDependency() != null) {
            return hierarchy.getPrimarySystemDependency().getNONDEPENDENTSYSTEM();
        } else if (hierarchy.getSystemDependency() != null) {
            return hierarchy.getSystemDependency().getNONDEPENDENTSYSTEM();
        } else if (hierarchy.getLocationDependency() != null) {
            return hierarchy.getLocationDependency().getNONDEPENDENTSYSTEM();
        } else if (hierarchy.getNonDependentParents() != null) {
            return hierarchy.getNonDependentParents().getNONDEPENDENTSYSTEM();
        }
        return null;
    }

    //
    // GET PARENTS FOR POSITION PARENT
    //

    public static ASSETPARENT_Type readAssetParent(PositionParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getAssetDependency() != null) {
            return hierarchy.getAssetDependency().getDEPENDENTASSET();
        } else if (hierarchy.getPositionDependency() != null) {
            return hierarchy.getPositionDependency().getNONDEPENDENTASSET();
        } else if (hierarchy.getPrimarySystemDependency() != null) {
            return hierarchy.getPrimarySystemDependency().getNONDEPENDENTASSET();
        } else if (hierarchy.getSystemDependency() != null) {
            return hierarchy.getSystemDependency().getNONDEPENDENTASSET();
        } else if (hierarchy.getLocationDependency() != null) {
            return hierarchy.getLocationDependency().getNONDEPENDENTASSET();
        } else if (hierarchy.getNonDependentParents() != null) {
            return hierarchy.getNonDependentParents().getNONDEPENDENTASSET();
        }
        return null;
    }

    public static POSITIONPARENT_Type readPositionParent(PositionParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getAssetDependency() != null) {
            return hierarchy.getAssetDependency().getNONDEPENDENTPOSITION();
        } else if (hierarchy.getPositionDependency() != null) {
            return hierarchy.getPositionDependency().getDEPENDENTPOSITION();
        } else if (hierarchy.getPrimarySystemDependency() != null) {
            return hierarchy.getPrimarySystemDependency().getNONDEPENDENTPOSITION();
        } else if (hierarchy.getSystemDependency() != null) {
            return hierarchy.getSystemDependency().getNONDEPENDENTPOSITION();
        } else if (hierarchy.getLocationDependency() != null) {
            return hierarchy.getLocationDependency().getNONDEPENDENTPOSITION();
        } else if (hierarchy.getNonDependentParents() != null) {
            return hierarchy.getNonDependentParents().getNONDEPENDENTPOSITION();
        }
        return null;
    }

    public static SYSTEMPARENT_Type readPrimarySystemParent(PositionParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getAssetDependency() != null) {
            return hierarchy.getAssetDependency().getNONDEPENDENTPRIMARYSYSTEM();
        } else if (hierarchy.getPositionDependency() != null) {
            return hierarchy.getPositionDependency().getNONDEPENDENTPRIMARYSYSTEM();
        } else if (hierarchy.getPrimarySystemDependency() != null) {
            return hierarchy.getPrimarySystemDependency().getDEPENDENTPRIMARYSYSTEM();
        } else if (hierarchy.getSystemDependency() != null) {
            return hierarchy.getSystemDependency().getNONDEPENDENTPRIMARYSYSTEM();
        } else if (hierarchy.getLocationDependency() != null) {
            return hierarchy.getLocationDependency().getNONDEPENDENTPRIMARYSYSTEM();
        } else if (hierarchy.getNonDependentParents() != null) {
            return hierarchy.getNonDependentParents().getNONDEPENDENTPRIMARYSYSTEM();
        }
        return null;
    }

    public static LOCATIONPARENT_Type readLocationParent(PositionParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getLocationDependency() != null) {
            return hierarchy.getLocationDependency().getDEPENDENTLOCATION();
        }
        return null;
    }

    public static List<SYSTEMPARENT_Type> readSystemsParent(PositionParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getAssetDependency() != null) {
            return hierarchy.getAssetDependency().getNONDEPENDENTSYSTEM();
        } else if (hierarchy.getPositionDependency() != null) {
            return hierarchy.getPositionDependency().getNONDEPENDENTSYSTEM();
        } else if (hierarchy.getPrimarySystemDependency() != null) {
            return hierarchy.getPrimarySystemDependency().getNONDEPENDENTSYSTEM();
        } else if (hierarchy.getSystemDependency() != null) {
            return hierarchy.getSystemDependency().getNONDEPENDENTSYSTEM();
        } else if (hierarchy.getLocationDependency() != null) {
            return hierarchy.getLocationDependency().getNONDEPENDENTSYSTEM();
        } else if (hierarchy.getNonDependentParents() != null) {
            return hierarchy.getNonDependentParents().getNONDEPENDENTSYSTEM();
        }
        return null;
    }

    //
    // HIERARCHY TYPE
    //

    /**
     * Determine hierarchy type for Assets
     *
     * @param hierarchy
     * @return
     */
    public static HIERARCHY_TYPE readHierarchyType(AssetParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getAssetDependency() != null) {
            return HIERARCHY_TYPE.ASSET_DEP;
        } else if (hierarchy.getPositionDependency() != null) {
            return HIERARCHY_TYPE.POSITION_DEP;
        } else if (hierarchy.getPrimarySystemDependency() != null) {
            return HIERARCHY_TYPE.PRIM_SYSTEM_DEP;
        } else if (hierarchy.getSystemDependency() != null) {
            return HIERARCHY_TYPE.SYSTEM_DEP;
        } else if (hierarchy.getLocationDependency() != null) {
            return HIERARCHY_TYPE.LOCATION_DEP;
        } else if (hierarchy.getNonDependentParents() != null) {
            return HIERARCHY_TYPE.NON_DEP_PARENTS;
        }
        return null;
    }

    /**
     * Determine hierarchy type for Positions
     *
     * @param hierarchy
     * @return
     */
    public static HIERARCHY_TYPE readHierarchyType(PositionParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getAssetDependency() != null) {
            return HIERARCHY_TYPE.ASSET_DEP;
        } else if (hierarchy.getPositionDependency() != null) {
            return HIERARCHY_TYPE.POSITION_DEP;
        } else if (hierarchy.getPrimarySystemDependency() != null) {
            return HIERARCHY_TYPE.PRIM_SYSTEM_DEP;
        } else if (hierarchy.getSystemDependency() != null) {
            return HIERARCHY_TYPE.SYSTEM_DEP;
        } else if (hierarchy.getLocationDependency() != null) {
            return HIERARCHY_TYPE.LOCATION_DEP;
        } else if (hierarchy.getNonDependentParents() != null) {
            return HIERARCHY_TYPE.NON_DEP_PARENTS;
        }
        return null;
    }

    //
    //
    //

    public static AssetDependency createAssetDependencyForAsset(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        if (assetParent == null) {
            return null;
        }
        AssetDependency assetDependency = new AssetDependency();
        assetDependency.setDEPENDENTASSET(assetParent);
        assetDependency.setNONDEPENDENTPOSITION(positionParent);
        assetDependency.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            assetDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        return assetDependency;
    }

    public static PositionDependency createPositionDependencyForAsset(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        if (positionParent == null) {
            return null;
        }
        PositionDependency positionDependency = new PositionDependency();
        positionDependency.setNONDEPENDENTASSET(assetParent);
        positionDependency.setDEPENDENTPOSITION(positionParent);
        positionDependency.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            positionDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        return positionDependency;
    }

    public static PrimarySystemDependency createPrimarySystemDependencyForAsset(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        if (primarySystemParent == null) {
            return null;
        }
        PrimarySystemDependency positionDependency = new PrimarySystemDependency();
        positionDependency.setNONDEPENDENTASSET(assetParent);
        positionDependency.setNONDEPENDENTPOSITION(positionParent);
        positionDependency.setDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            positionDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        return positionDependency;
    }

    public static LocationDependency createLocationDependencyForAsset(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents, LOCATIONPARENT_Type locationParent) {
        if (assetParent == null && positionParent == null && primarySystemParent == null && systemParents == null && locationParent == null) {
            return null;
        }
        LocationDependency locationDependency = new LocationDependency();
        locationDependency.setNONDEPENDENTASSET(assetParent);
        locationDependency.setNONDEPENDENTPOSITION(positionParent);
        locationDependency.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            locationDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        locationDependency.setDEPENDENTLOCATION(locationParent);
        return locationDependency;
    }

    public static NonDependentParents_Type createNonDependentParentsForAsset(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        if (assetParent == null && positionParent == null && primarySystemParent == null && systemParents == null) {
            return null;
        }
        NonDependentParents_Type nonDependentParents = new NonDependentParents_Type();
        nonDependentParents.setNONDEPENDENTASSET(assetParent);
        nonDependentParents.setNONDEPENDENTPOSITION(positionParent);
        nonDependentParents.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            nonDependentParents.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        return nonDependentParents;
    }

    //
    //
    //

    public static net.datastream.schemas.mp_entities.positionhierarchy_002.AssetDependency createAssetDependencyForPosition(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        if (assetParent == null) {
            return null;
        }
        net.datastream.schemas.mp_entities.positionhierarchy_002.AssetDependency assetDependency = new net.datastream.schemas.mp_entities.positionhierarchy_002.AssetDependency();
        assetDependency.setDEPENDENTASSET(assetParent);
        assetDependency.setNONDEPENDENTPOSITION(positionParent);
        assetDependency.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            assetDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        return assetDependency;
    }

    public static net.datastream.schemas.mp_entities.positionhierarchy_002.PositionDependency createPositionDependencyForPosition(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        if (positionParent == null) {
            return null;
        }
        net.datastream.schemas.mp_entities.positionhierarchy_002.PositionDependency positionDependency = new net.datastream.schemas.mp_entities.positionhierarchy_002.PositionDependency();
        positionDependency.setNONDEPENDENTASSET(assetParent);
        positionDependency.setDEPENDENTPOSITION(positionParent);
        positionDependency.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            positionDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        return positionDependency;
    }

    public static net.datastream.schemas.mp_entities.positionhierarchy_002.PrimarySystemDependency createPrimarySystemDependencyForPosition(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        if (primarySystemParent == null) {
            return null;
        }
        net.datastream.schemas.mp_entities.positionhierarchy_002.PrimarySystemDependency positionDependency = new net.datastream.schemas.mp_entities.positionhierarchy_002.PrimarySystemDependency();
        positionDependency.setNONDEPENDENTASSET(assetParent);
        positionDependency.setNONDEPENDENTPOSITION(positionParent);
        positionDependency.setDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            positionDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        return positionDependency;
    }

    public static net.datastream.schemas.mp_entities.positionhierarchy_002.LocationDependency createLocationDependencyForPosition(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents, LOCATIONPARENT_Type locationParent) {
        if (assetParent == null && positionParent == null && primarySystemParent == null && systemParents == null && locationParent == null) {
            return null;
        }
        net.datastream.schemas.mp_entities.positionhierarchy_002.LocationDependency locationDependency = new net.datastream.schemas.mp_entities.positionhierarchy_002.LocationDependency();
        locationDependency.setNONDEPENDENTASSET(assetParent);
        locationDependency.setNONDEPENDENTPOSITION(positionParent);
        locationDependency.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            locationDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        locationDependency.setDEPENDENTLOCATION(locationParent);
        return locationDependency;
    }

    public static net.datastream.schemas.mp_entities.positionhierarchy_002.NonDependentParents_Type createNonDependentParentsForPosition(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        if (assetParent == null && positionParent == null && primarySystemParent == null && systemParents == null) {
            return null;
        }
        net.datastream.schemas.mp_entities.positionhierarchy_002.NonDependentParents_Type nonDependentParents = new net.datastream.schemas.mp_entities.positionhierarchy_002.NonDependentParents_Type();
        nonDependentParents.setNONDEPENDENTASSET(assetParent);
        nonDependentParents.setNONDEPENDENTPOSITION(positionParent);
        nonDependentParents.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            nonDependentParents.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        return nonDependentParents;
    }

    //
    //
    //

    public static ASSETPARENT_Type createAssetParent(String organizationCode, String assetCode, Boolean costRollUp, ASSETPARENT_Type oldHierarchyAsset) {
        if (assetCode == null) {
            return oldHierarchyAsset;
        }

        if (assetCode.equals("")) {
            return null;
        }

        ASSETPARENT_Type assetType = new ASSETPARENT_Type();
        assetType.setASSETID(new EQUIPMENTID_Type());
        assetType.getASSETID().setEQUIPMENTCODE(assetCode);
        assetType.getASSETID().setORGANIZATIONID(new ORGANIZATIONID_Type());
        assetType.getASSETID().getORGANIZATIONID().setORGANIZATIONCODE(organizationCode);
        if (costRollUp == null && oldHierarchyAsset != null) {
            assetType.setCOSTROLLUP(oldHierarchyAsset.getCOSTROLLUP());
        } else {
            assetType.setCOSTROLLUP(encodeBoolean(costRollUp, BooleanType.TRUE_FALSE));
        }
        return assetType;
    }

    public static POSITIONPARENT_Type createPositionParent(String organizationCode, String positionCode, Boolean costRollUp, POSITIONPARENT_Type oldHierarchyPosition) {
        if (positionCode == null) {
            return oldHierarchyPosition;
        }

        if (positionCode.equals("")) {
            return null;
        }

        POSITIONPARENT_Type positionType = new POSITIONPARENT_Type();
        positionType.setPOSITIONID(new EQUIPMENTID_Type());
        positionType.getPOSITIONID().setEQUIPMENTCODE(positionCode);
        positionType.getPOSITIONID().setORGANIZATIONID(new ORGANIZATIONID_Type());
        positionType.getPOSITIONID().getORGANIZATIONID().setORGANIZATIONCODE(organizationCode);
        if (costRollUp == null && oldHierarchyPosition != null) {
            positionType.setCOSTROLLUP(oldHierarchyPosition.getCOSTROLLUP());
        } else {
            positionType.setCOSTROLLUP(encodeBoolean(costRollUp, BooleanType.TRUE_FALSE));
        }
        return positionType;
    }

    public static SYSTEMPARENT_Type createPrimarySystemParent(String organizationCode, String systemCode, Boolean costRollUp, SYSTEMPARENT_Type oldSystemHierarchy) {

        if (systemCode == null) {
            return oldSystemHierarchy;
        }

        if (systemCode.equals("")) {
            return null;
        }

        SYSTEMPARENT_Type systemType = new SYSTEMPARENT_Type();
        systemType.setSYSTEMID(new EQUIPMENTID_Type());
        systemType.getSYSTEMID().setEQUIPMENTCODE(systemCode);
        systemType.getSYSTEMID().setORGANIZATIONID(new ORGANIZATIONID_Type());
        systemType.getSYSTEMID().getORGANIZATIONID().setORGANIZATIONCODE(organizationCode);
        if (costRollUp == null && oldSystemHierarchy != null) {
            systemType.setCOSTROLLUP(oldSystemHierarchy.getCOSTROLLUP());
        } else {
            systemType.setCOSTROLLUP(encodeBoolean(costRollUp, BooleanType.TRUE_FALSE));
        }

        return systemType;
    }

    public static LOCATIONPARENT_Type createLocationParent(String organizationCode, String locationCode, LOCATIONPARENT_Type oldLocationHierarchy) {
        if (locationCode == null) {
            return oldLocationHierarchy;
        }

        if (locationCode.equals("")) {
            return null;
        }

        LOCATIONPARENT_Type locationType = new LOCATIONPARENT_Type();
        locationType.setLOCATIONID(new LOCATIONID_Type());
        locationType.getLOCATIONID().setLOCATIONCODE(locationCode);
        locationType.getLOCATIONID().setORGANIZATIONID(new ORGANIZATIONID_Type());
        locationType.getLOCATIONID().getORGANIZATIONID().setORGANIZATIONCODE(organizationCode);
        return locationType;
    }

    /**
     *
     * @param assetParam
     * @param currentHierarchyType
     * @return
     */
    public static HIERARCHY_TYPE getNewHierarchyType(Equipment assetParam, HIERARCHY_TYPE currentHierarchyType) {
        if (assetParam.getHierarchyAssetDependent() != null && assetParam.getHierarchyAssetDependent() && !"".equals(assetParam.getHierarchyAssetCode())) {
            return HIERARCHY_TYPE.ASSET_DEP;
        } else if (assetParam.getHierarchyPositionDependent() != null && assetParam.getHierarchyPositionDependent() && !"".equals(assetParam.getHierarchyPositionCode())) {
            return HIERARCHY_TYPE.POSITION_DEP;
        } else if (assetParam.getHierarchyPrimarySystemDependent() != null && assetParam.getHierarchyPrimarySystemDependent() && !"".equals(assetParam.getHierarchyPrimarySystemCode())) {
            return HIERARCHY_TYPE.PRIM_SYSTEM_DEP;
        } else if (currentHierarchyType == HIERARCHY_TYPE.ASSET_DEP && assetParam.getHierarchyAssetDependent() == null && !"".equals(assetParam.getHierarchyAssetCode())){
            return HIERARCHY_TYPE.ASSET_DEP;
        } else if (currentHierarchyType == HIERARCHY_TYPE.POSITION_DEP && assetParam.getHierarchyPositionDependent() == null && !"".equals(assetParam.getHierarchyPositionCode())){
            return HIERARCHY_TYPE.POSITION_DEP;
        } else if (currentHierarchyType == HIERARCHY_TYPE.PRIM_SYSTEM_DEP && assetParam.getHierarchyPrimarySystemDependent() == null && !"".equals(assetParam.getHierarchyPrimarySystemCode())){
            return HIERARCHY_TYPE.PRIM_SYSTEM_DEP;
        } else if (currentHierarchyType == HIERARCHY_TYPE.LOCATION_DEP && !"".equals(assetParam.getHierarchyLocationCode()) || isNotEmpty(assetParam.getHierarchyLocationCode())){
            return HIERARCHY_TYPE.LOCATION_DEP;
        } else if (currentHierarchyType == HIERARCHY_TYPE.SYSTEM_DEP){
            return HIERARCHY_TYPE.SYSTEM_DEP;
        } else {
            return HIERARCHY_TYPE.NON_DEP_PARENTS;
        }
    }

}

