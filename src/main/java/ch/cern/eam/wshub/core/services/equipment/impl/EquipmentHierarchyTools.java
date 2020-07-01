package ch.cern.eam.wshub.core.services.equipment.impl;

import net.datastream.schemas.mp_entities.assetequipment_001.AssetParentHierarchy;
import net.datastream.schemas.mp_fields.ASSETPARENT_Type;
import net.datastream.schemas.mp_fields.LOCATIONPARENT_Type;
import net.datastream.schemas.mp_fields.POSITIONPARENT_Type;
import net.datastream.schemas.mp_fields.SYSTEMPARENT_Type;

import java.util.List;

public class EquipmentHierarchyTools {

    //
    // TODO: find better way to implement the various readParent methods
    //

    public static ASSETPARENT_Type readAssetParent(AssetParentHierarchy hierarchy) {
        if (hierarchy.getAssetDependency() != null) {
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
        if (hierarchy.getAssetDependency() != null) {
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
        if (hierarchy.getAssetDependency() != null) {
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
         if (hierarchy.getLocationDependency() != null) {
            return hierarchy.getLocationDependency().getDEPENDENTLOCATION();
         }
        return null;
    }

    public static List<SYSTEMPARENT_Type> readSystemsParent(AssetParentHierarchy hierarchy) {
        if (hierarchy.getAssetDependency() != null) {
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

    public static AssetServiceImpl.HIERARCHY_TYPE readHierarchyType(AssetParentHierarchy hierarchy) {
        if (hierarchy.getAssetDependency() != null) {
            return AssetServiceImpl.HIERARCHY_TYPE.ASSET_DEP;
        } else if (hierarchy.getPositionDependency() != null) {
            return AssetServiceImpl.HIERARCHY_TYPE.POSITION_DEP;
        } else if (hierarchy.getPrimarySystemDependency() != null) {
            return AssetServiceImpl.HIERARCHY_TYPE.PRIM_SYSTEM_DEP;
        } else if (hierarchy.getSystemDependency() != null) {
            return AssetServiceImpl.HIERARCHY_TYPE.SYSTEM_DEP;
        } else if (hierarchy.getLocationDependency() != null) {
            return AssetServiceImpl.HIERARCHY_TYPE.LOCATION_DEP;
        } else if (hierarchy.getNonDependentParents() != null) {
            return AssetServiceImpl.HIERARCHY_TYPE.NON_DEP_PARENTS;
        }
        return null;
    }

}
