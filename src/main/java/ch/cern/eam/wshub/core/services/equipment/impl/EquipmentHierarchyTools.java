package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import net.datastream.schemas.mp_entities.assetequipment_001.*;
import net.datastream.schemas.mp_fields.ASSETPARENT_Type;
import net.datastream.schemas.mp_fields.LOCATIONPARENT_Type;
import net.datastream.schemas.mp_fields.POSITIONPARENT_Type;
import net.datastream.schemas.mp_fields.SYSTEMPARENT_Type;

import java.util.List;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;

public class EquipmentHierarchyTools {

    //
    // TODO: find better way to implement the various readParent methods
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

    public static AssetServiceImpl.HIERARCHY_TYPE readHierarchyType(AssetParentHierarchy hierarchy) {
        if (hierarchy == null) {
            return null;
        } else if (hierarchy.getAssetDependency() != null) {
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

    //
    //
    //

    public static AssetDependency createAssetDependency(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        AssetDependency assetDependency = new AssetDependency();
        assetDependency.setDEPENDENTASSET(assetParent);
        assetDependency.setNONDEPENDENTPOSITION(positionParent);
        assetDependency.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            assetDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        return assetDependency;
    }

    public static PositionDependency createPositionDependency(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        PositionDependency positionDependency = new PositionDependency();
        positionDependency.setNONDEPENDENTASSET(assetParent);
        positionDependency.setDEPENDENTPOSITION(positionParent);
        positionDependency.setNONDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            positionDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        return positionDependency;
    }

    public static PrimarySystemDependency createPrimarySystemDependency(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
        PrimarySystemDependency positionDependency = new PrimarySystemDependency();
        positionDependency.setNONDEPENDENTASSET(assetParent);
        positionDependency.setNONDEPENDENTPOSITION(positionParent);
        positionDependency.setDEPENDENTPRIMARYSYSTEM(primarySystemParent);
        if (systemParents != null) {
            positionDependency.getNONDEPENDENTSYSTEM().addAll(systemParents);
        }
        return positionDependency;
    }

    public static LocationDependency createLocationDependency(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents, LOCATIONPARENT_Type locationParent) {
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

    public static NonDependentParents_Type createNonDependentParents(ASSETPARENT_Type assetParent, POSITIONPARENT_Type positionParent, SYSTEMPARENT_Type primarySystemParent, List<SYSTEMPARENT_Type> systemParents) {
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

    //TODO complete the logic determining new hierarchy type based on the old one and the input params
    public static AssetServiceImpl.HIERARCHY_TYPE getNewHierarchyType(Equipment assetParam, AssetServiceImpl.HIERARCHY_TYPE currentHierarchyType) {
        if (assetParam.getHierarchyAssetDependent() != null && assetParam.getHierarchyAssetDependent()) {
            return AssetServiceImpl.HIERARCHY_TYPE.ASSET_DEP;
        } else if (assetParam.getHierarchyPositionDependent() != null && assetParam.getHierarchyPositionDependent()) {
            return AssetServiceImpl.HIERARCHY_TYPE.POSITION_DEP;
        } else if (assetParam.getHierarchyPrimarySystemDependent() != null && assetParam.getHierarchyPrimarySystemDependent()) {
            return AssetServiceImpl.HIERARCHY_TYPE.PRIM_SYSTEM_DEP;
        } else if (isNotEmpty(assetParam.getHierarchyLocationCode())) {
            return AssetServiceImpl.HIERARCHY_TYPE.LOCATION_DEP;
        } else if (currentHierarchyType == AssetServiceImpl.HIERARCHY_TYPE.ASSET_DEP && assetParam.getHierarchyAssetDependent() == null){
            return AssetServiceImpl.HIERARCHY_TYPE.ASSET_DEP;
        } else if (currentHierarchyType == AssetServiceImpl.HIERARCHY_TYPE.POSITION_DEP && assetParam.getHierarchyPositionDependent() == null){
            return AssetServiceImpl.HIERARCHY_TYPE.POSITION_DEP;
        } else if (currentHierarchyType == AssetServiceImpl.HIERARCHY_TYPE.PRIM_SYSTEM_DEP && assetParam.getHierarchyPrimarySystemDependent() == null){
            return AssetServiceImpl.HIERARCHY_TYPE.PRIM_SYSTEM_DEP;
        } else if (currentHierarchyType == AssetServiceImpl.HIERARCHY_TYPE.LOCATION_DEP && !"".equals(assetParam.getHierarchyLocationCode())){
            return AssetServiceImpl.HIERARCHY_TYPE.LOCATION_DEP;
        } else if (currentHierarchyType == AssetServiceImpl.HIERARCHY_TYPE.SYSTEM_DEP){
            return AssetServiceImpl.HIERARCHY_TYPE.SYSTEM_DEP;
        } else {
            return AssetServiceImpl.HIERARCHY_TYPE.NON_DEP_PARENTS;
        }
    }

}
