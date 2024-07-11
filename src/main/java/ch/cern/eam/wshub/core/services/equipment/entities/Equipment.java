package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.BooleanAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListHelpable;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLValue;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Entity
@Table(name = "R5OBJECTS")
public class Equipment implements Serializable, UserDefinedListHelpable {

    /**
     *
     */
    private static final long serialVersionUID = 7865040704362527306L;

    @Column(name = "OBJ_DESC")
    @EAMField(xpath = {
            "ASSETID/DESCRIPTION",
            "POSITIONID/DESCRIPTION",
            "SYSTEMID/DESCRIPTION"
    }, readOnly = true)
    private String description;
    @Id
    @Column(name = "OBJ_CODE")
    @EAMField(xpath = {
            "ASSETID/EQUIPMENTCODE",
            "POSITIONID/EQUIPMENTCODE",
            "SYSTEMID/EQUIPMENTCODE"
    }, readOnly = true)
    private String code;

    @Transient
    @EAMField(xpath = {
            "ASSETID/ORGANIZATIONID/ORGANIZATIONCODE",
            "POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE",
            "SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE"
    }, readOnly = true)
    private String organization;

    @Column(name = "OBJ_OBTYPE")
    @EAMField(xpath = "TYPE/TYPECODE")
    private String typeCode;
    @EAMField(xpath = "TYPE/DESCRIPTION", readOnly = true)
    private String typeDesc;

    private String systemTypeCode;

    @Transient
    @EAMField(xpath = "EQUIPMENTALIAS")
    private String alias;

    @Transient
    @EAMField(xpath = "CLASSID/CLASSCODE", nullifyParentLevel = 1)
    private String classCode;
    @Transient
    @EAMField(xpath = "CLASSID/DESCRIPTION", readOnly = true)
    private String classDesc;
    @Transient
    @EAMField(xpath = "CATEGORYID/CATEGORYCODE", nullifyParentLevel = 1)
    private String categoryCode;
    @Transient
    @EAMField(xpath = "CATEGORYID/DESCRIPTION", readOnly = true)
    private String categoryDesc;
    @Transient
    @EAMField(xpath = "recordid")
    private BigInteger updateCount;
    @Transient
    @EAMField(xpath = "OUTOFSERVICE")
    private Boolean outOfService;
    @Transient
    @EAMField(xpath = "INPRODUCTION")
    private Boolean inProduction;
    @Transient
    @EAMField(xpath = "PROFILEID/OBJECTCODE")
    private String profileCode;
    //
    @Transient
    @EAMField(xpath = "STATUS/STATUSCODE", nullifyParentLevel = 1)
    private String statusCode;
    @Transient
    @EAMField(xpath = "STATUS/DESCRIPTION", readOnly = true)
    private String statusDesc;

    @Transient
    private String systemStatusCode;

    public String getSystemStatusCode() {
        return systemStatusCode;
    }

    public void setSystemStatusCode(String systemStatusCode) {
        this.systemStatusCode = systemStatusCode;
    }

    //
    @Transient
    @EAMField(xpath = "COSTCODEID/COSTCODE", nullifyParentLevel = 1)
    private String costCode;
    @Transient
    @EAMField(xpath = "COSTCODEID/DESCRIPTION", nullifyParentLevel = 0)
    private String costCodeDesc;

    @Transient
    @EAMField(xpath = "DEPARTMENTID/DEPARTMENTCODE", nullifyParentLevel = 1)
    private String departmentCode;
    @Transient
    @EAMField(xpath = "DEPARTMENTID/DESCRIPTION", nullifyParentLevel = 0)
    private String departmentDesc;
    //
    @Transient
    @EAMField(xpath = "USERDEFINEDAREA")
    private CustomField[] customFields;

    //
    @Transient
    @EAMField(xpath = "COMMISSIONDATE")
    private Date comissionDate;
    @Transient
    @EAMField(xpath = "ASSETVALUE")
    private BigDecimal equipmentValue;
    @Transient
    @EAMField(xpath = "ASSIGNEDTO/PERSONCODE", nullifyParentLevel = 1)
    private String assignedTo;
    @Transient
    @EAMField(xpath = "ASSIGNEDTO/DESCRIPTION", readOnly = true)
    private String assignedToDesc;
    @Transient
    @EAMField(xpath = "METERUNIT")
    private String meterUnit;
    @Transient
    @EAMField(xpath = "CRITICALITYID/CRITICALITY", nullifyParentLevel = 1)
    private String criticality;
    @Transient
    @EAMField(xpath = "CGMP")
    private String cGMP;
    @Transient
    @EAMField(xpath = "ORIGINALRECEIPTDATE")
    private Date originalReceiptDate;
    @Transient
    @EAMField(xpath = "EQUIPMENTSTATEID/STATECODE", enforceValidXpath = false, nullifyParentLevel = 1)
    private String stateCode;
    @Transient
    @EAMField(xpath = "EQUIPMENTSTATEID/DESCRIPTION", readOnly = true)
    private String stateDesc;

    @Transient
    @EAMField(xpath = "ManufacturerInfo/MANUFACTURERCODE", nullifyParentLevel = 0)
    private String manufacturerCode;
    @Transient
    private String manufacturerDesc;
    @Transient
    @EAMField(xpath = "ManufacturerInfo/SERIALNUMBER", nullifyParentLevel = 0)
    private String serialNumber;
    @Transient
    @EAMField(xpath = "ManufacturerInfo/MODEL", nullifyParentLevel = 0)
    private String model;
    @Transient
    @EAMField(xpath = "ManufacturerInfo/MODELREVISION", nullifyParentLevel = 0)
    private String revision;
    @Transient
    @EAMField(xpath = "ManufacturerInfo/XCOORDINATE", nullifyParentLevel = 0)
    private BigDecimal xCoordinate;
    @Transient
    @EAMField(xpath = "ManufacturerInfo/YCOORDINATE", nullifyParentLevel = 0)
    private BigDecimal yCoordinate;
    @Transient
    @EAMField(xpath = "ManufacturerInfo/ZCOORDINATE", nullifyParentLevel = 0)
    private BigDecimal zCoordinate;
    @Transient
    @EAMField(xpath = "VENDOR", nullifyParentLevel = 0)
    private String vendor;

    @Transient
    @EAMField(xpath = "FacilityConditionIndex/COSTOFNEEDEDREPAIRS", nullifyParentLevel = 0)
    private BigDecimal costOfNeededRepairs;
    @Transient
    @EAMField(xpath = "FacilityConditionIndex/REPLACEMENTVALUE", nullifyParentLevel = 0)
    private BigDecimal replacementValue;
    @Transient
    @EAMField(xpath = "FacilityConditionIndex/FACILITYCONDITIONINDEX", nullifyParentLevel = 0)
    private BigDecimal facilityConditionIndex;
    @Transient
    @EAMField(xpath = "FacilityConditionIndex/YEARBUILT", nullifyParentLevel = 0)
    private BigDecimal yearBuilt;
    @Transient
    @EAMField(xpath = "FacilityConditionIndex/SERVICELIFE", nullifyParentLevel = 0)
    private BigDecimal serviceLifetime;

    // Hierarchy
    // Asset
    @Transient
    @EAMField(xpath = {
            "AssetParentHierarchy/AssetDependency/DEPENDENTASSET/ASSETID/EQUIPMENTCODE",
            "AssetParentHierarchy/PositionDependency/NONDEPENDENTASSET/ASSETID/EQUIPMENTCODE",
            "AssetParentHierarchy/PrimarySystemDependency/NONDEPENDENTASSET/ASSETID/EQUIPMENTCODE",
            "AssetParentHierarchy/SystemDependency/NONDEPENDENTASSET/ASSETID/EQUIPMENTCODE",
            "AssetParentHierarchy/LocationDependency/NONDEPENDENTASSET/ASSETID/EQUIPMENTCODE",
            "AssetParentHierarchy/NonDependentParents/NONDEPENDENTASSET/ASSETID/EQUIPMENTCODE",

            "PositionParentHierarchy/AssetDependency/DEPENDENTASSET/ASSETID/EQUIPMENTCODE",
            "PositionParentHierarchy/PositionDependency/NONDEPENDENTASSET/ASSETID/EQUIPMENTCODE",
            "PositionParentHierarchy/PrimarySystemDependency/NONDEPENDENTASSET/ASSETID/EQUIPMENTCODE",
            "PositionParentHierarchy/SystemDependency/NONDEPENDENTASSET/ASSETID/EQUIPMENTCODE",
            "PositionParentHierarchy/LocationDependency/NONDEPENDENTASSET/ASSETID/EQUIPMENTCODE",
            "PositionParentHierarchy/NonDependentParents/NONDEPENDENTASSET/ASSETID/EQUIPMENTCODE"},
            readOnly = true)
    private String hierarchyAssetCode;
    @Transient
    @EAMField(xpath = {
            "AssetParentHierarchy/AssetDependency/DEPENDENTASSET/ASSETID/DESCRIPTION",
            "AssetParentHierarchy/PositionDependency/NONDEPENDENTASSET/ASSETID/DESCRIPTION",
            "AssetParentHierarchy/PrimarySystemDependency/NONDEPENDENTASSET/ASSETID/DESCRIPTION",
            "AssetParentHierarchy/SystemDependency/NONDEPENDENTASSET/ASSETID/DESCRIPTION",
            "AssetParentHierarchy/LocationDependency/NONDEPENDENTASSET/ASSETID/DESCRIPTION",
            "AssetParentHierarchy/NonDependentParents/NONDEPENDENTASSET/ASSETID/DESCRIPTION",

            "PositionParentHierarchy/AssetDependency/DEPENDENTASSET/ASSETID/DESCRIPTION",
            "PositionParentHierarchy/PositionDependency/NONDEPENDENTASSET/ASSETID/DESCRIPTION",
            "PositionParentHierarchy/PrimarySystemDependency/NONDEPENDENTASSET/ASSETID/DESCRIPTION",
            "PositionParentHierarchy/SystemDependency/NONDEPENDENTASSET/ASSETID/DESCRIPTION",
            "PositionParentHierarchy/LocationDependency/NONDEPENDENTASSET/ASSETID/DESCRIPTION",
            "PositionParentHierarchy/NonDependentParents/NONDEPENDENTASSET/ASSETID/DESCRIPTION"},
            readOnly = true)
    private String hierarchyAssetDesc;

    @Transient
    @EAMField(xpath = {
            "AssetParentHierarchy/AssetDependency/DEPENDENTASSET/ASSETID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/PositionDependency/NONDEPENDENTASSET/ASSETID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/PrimarySystemDependency/NONDEPENDENTASSET/ASSETID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/SystemDependency/NONDEPENDENTASSET/ASSETID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/LocationDependency/NONDEPENDENTASSET/ASSETID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/NonDependentParents/NONDEPENDENTASSET/ASSETID/ORGANIZATIONID/ORGANIZATIONCODE",

            "PositionParentHierarchy/AssetDependency/DEPENDENTASSET/ASSETID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/PositionDependency/NONDEPENDENTASSET/ASSETID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/PrimarySystemDependency/NONDEPENDENTASSET/ASSETID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/SystemDependency/NONDEPENDENTASSET/ASSETID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/LocationDependency/NONDEPENDENTASSET/ASSETID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/NonDependentParents/NONDEPENDENTASSET/ASSETID/ORGANIZATIONID/ORGANIZATIONCODE"},
            readOnly = true)
    private String hierarchyAssetOrg;
    @Transient
    private Boolean hierarchyAssetDependent;
    @Transient
    @EAMField(xpath = {
            "AssetParentHierarchy/AssetDependency/DEPENDENTASSET/COSTROLLUP",
            "AssetParentHierarchy/PositionDependency/NONDEPENDENTASSET/COSTROLLUP",
            "AssetParentHierarchy/PrimarySystemDependency/NONDEPENDENTASSET/COSTROLLUP",
            "AssetParentHierarchy/SystemDependency/NONDEPENDENTASSET/COSTROLLUP",
            "AssetParentHierarchy/LocationDependency/NONDEPENDENTASSET/COSTROLLUP",
            "AssetParentHierarchy/NonDependentParents/NONDEPENDENTASSET/COSTROLLUP",

            "PositionParentHierarchy/AssetDependency/DEPENDENTASSET/COSTROLLUP",
            "PositionParentHierarchy/PositionDependency/NONDEPENDENTASSET/COSTROLLUP",
            "PositionParentHierarchy/PrimarySystemDependency/NONDEPENDENTASSET/COSTROLLUP",
            "PositionParentHierarchy/SystemDependency/NONDEPENDENTASSET/COSTROLLUP",
            "PositionParentHierarchy/LocationDependency/NONDEPENDENTASSET/COSTROLLUP",
            "PositionParentHierarchy/NonDependentParents/NONDEPENDENTASSET/COSTROLLUP"},
            readOnly = true)
    private Boolean hierarchyAssetCostRollUp;
    // Position
    @Transient
    @EAMField(xpath = {
            "AssetParentHierarchy/AssetDependency/NONDEPENDENTPOSITION/POSITIONID/EQUIPMENTCODE",
            "AssetParentHierarchy/PositionDependency/DEPENDENTPOSITION/POSITIONID/EQUIPMENTCODE",
            "AssetParentHierarchy/PrimarySystemDependency/NONDEPENDENTPOSITION/POSITIONID/EQUIPMENTCODE",
            "AssetParentHierarchy/SystemDependency/NONDEPENDENTPOSITION/POSITIONID/EQUIPMENTCODE",
            "AssetParentHierarchy/LocationDependency/NONDEPENDENTPOSITION/POSITIONID/EQUIPMENTCODE",
            "AssetParentHierarchy/NonDependentParents/NONDEPENDENTPOSITION/POSITIONID/EQUIPMENTCODE",

            "PositionParentHierarchy/AssetDependency/NONDEPENDENTPOSITION/POSITIONID/EQUIPMENTCODE",
            "PositionParentHierarchy/PositionDependency/DEPENDENTPOSITION/POSITIONID/EQUIPMENTCODE",
            "PositionParentHierarchy/PrimarySystemDependency/NONDEPENDENTPOSITION/POSITIONID/EQUIPMENTCODE",
            "PositionParentHierarchy/SystemDependency/NONDEPENDENTPOSITION/POSITIONID/EQUIPMENTCODE",
            "PositionParentHierarchy/LocationDependency/NONDEPENDENTPOSITION/POSITIONID/EQUIPMENTCODE",
            "PositionParentHierarchy/NonDependentParents/NONDEPENDENTPOSITION/POSITIONID/EQUIPMENTCODE"},
            readOnly = true)
    private String hierarchyPositionCode;
    @Transient
    @EAMField(xpath = {
            "AssetParentHierarchy/AssetDependency/NONDEPENDENTPOSITION/POSITIONID/DESCRIPTION",
            "AssetParentHierarchy/PositionDependency/DEPENDENTPOSITION/POSITIONID/DESCRIPTION",
            "AssetParentHierarchy/PrimarySystemDependency/NONDEPENDENTPOSITION/POSITIONID/DESCRIPTION",
            "AssetParentHierarchy/SystemDependency/NONDEPENDENTPOSITION/POSITIONID/DESCRIPTION",
            "AssetParentHierarchy/LocationDependency/NONDEPENDENTPOSITION/POSITIONID/DESCRIPTION",
            "AssetParentHierarchy/NonDependentParents/NONDEPENDENTPOSITION/POSITIONID/DESCRIPTION",

            "PositionParentHierarchy/AssetDependency/NONDEPENDENTPOSITION/POSITIONID/DESCRIPTION",
            "PositionParentHierarchy/PositionDependency/DEPENDENTPOSITION/POSITIONID/DESCRIPTION",
            "PositionParentHierarchy/PrimarySystemDependency/NONDEPENDENTPOSITION/POSITIONID/DESCRIPTION",
            "PositionParentHierarchy/SystemDependency/NONDEPENDENTPOSITION/POSITIONID/DESCRIPTION",
            "PositionParentHierarchy/LocationDependency/NONDEPENDENTPOSITION/POSITIONID/DESCRIPTION",
            "PositionParentHierarchy/NonDependentParents/NONDEPENDENTPOSITION/POSITIONID/DESCRIPTION"},
            readOnly = true)
    private String hierarchyPositionDesc;

    @Transient
    @EAMField(xpath = {
            "AssetParentHierarchy/AssetDependency/NONDEPENDENTPOSITION/POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/PositionDependency/DEPENDENTPOSITION/POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/PrimarySystemDependency/NONDEPENDENTPOSITION/POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/SystemDependency/NONDEPENDENTPOSITION/POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/LocationDependency/NONDEPENDENTPOSITION/POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/NonDependentParents/NONDEPENDENTPOSITION/POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE",

            "PositionParentHierarchy/AssetDependency/NONDEPENDENTPOSITION/POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/PositionDependency/DEPENDENTPOSITION/POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/PrimarySystemDependency/NONDEPENDENTPOSITION/POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/SystemDependency/NONDEPENDENTPOSITION/POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/LocationDependency/NONDEPENDENTPOSITION/POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/NonDependentParents/NONDEPENDENTPOSITION/POSITIONID/ORGANIZATIONID/ORGANIZATIONCODE"},
            readOnly = true)
    private String hierarchyPositionOrg;
    @Transient
    private Boolean hierarchyPositionDependent;
    @Transient
    @EAMField(xpath = {
            "AssetParentHierarchy/AssetDependency/NONDEPENDENTPOSITION/COSTROLLUP",
            "AssetParentHierarchy/PositionDependency/DEPENDENTPOSITION/COSTROLLUP",
            "AssetParentHierarchy/PrimarySystemDependency/NONDEPENDENTPOSITION/COSTROLLUP",
            "AssetParentHierarchy/SystemDependency/NONDEPENDENTPOSITION/COSTROLLUP",
            "AssetParentHierarchy/LocationDependency/NONDEPENDENTPOSITION/COSTROLLUP",
            "AssetParentHierarchy/NonDependentParents/NONDEPENDENTPOSITION/COSTROLLUP",

            "PositionParentHierarchy/AssetDependency/NONDEPENDENTPOSITION/COSTROLLUP",
            "PositionParentHierarchy/PositionDependency/DEPENDENTPOSITION/COSTROLLUP",
            "PositionParentHierarchy/PrimarySystemDependency/NONDEPENDENTPOSITION/COSTROLLUP",
            "PositionParentHierarchy/SystemDependency/NONDEPENDENTPOSITION/COSTROLLUP",
            "PositionParentHierarchy/LocationDependency/NONDEPENDENTPOSITION/COSTROLLUP",
            "PositionParentHierarchy/NonDependentParents/NONDEPENDENTPOSITION/COSTROLLUP"},
            readOnly = true)
    private Boolean hierarchyPositionCostRollUp;
    // Primary System
    @Transient
    @EAMField(xpath = {
            "AssetParentHierarchy/AssetDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",
            "AssetParentHierarchy/PositionDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",
            "AssetParentHierarchy/PrimarySystemDependency/DEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",
            "AssetParentHierarchy/SystemDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",
            "AssetParentHierarchy/LocationDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",
            "AssetParentHierarchy/NonDependentParents/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",

            "PositionParentHierarchy/AssetDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",
            "PositionParentHierarchy/PositionDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",
            "PositionParentHierarchy/PrimarySystemDependency/DEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",
            "PositionParentHierarchy/SystemDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",
            "PositionParentHierarchy/LocationDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",
            "PositionParentHierarchy/NonDependentParents/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",

            "SystemParentHierarchy/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE",
            "SystemParentHierarchy/DEPENDENTPRIMARYSYSTEM/SYSTEMID/EQUIPMENTCODE"},
            readOnly = true)
    private String hierarchyPrimarySystemCode;
    @Transient
    @EAMField(xpath = {
            "AssetParentHierarchy/AssetDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",
            "AssetParentHierarchy/PositionDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",
            "AssetParentHierarchy/PrimarySystemDependency/DEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",
            "AssetParentHierarchy/SystemDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",
            "AssetParentHierarchy/LocationDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",
            "AssetParentHierarchy/NonDependentParents/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",

            "PositionParentHierarchy/AssetDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",
            "PositionParentHierarchy/PositionDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",
            "PositionParentHierarchy/PrimarySystemDependency/DEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",
            "PositionParentHierarchy/SystemDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",
            "PositionParentHierarchy/LocationDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",
            "PositionParentHierarchy/NonDependentParents/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",

            "SystemParentHierarchy/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION",
            "SystemParentHierarchy/DEPENDENTPRIMARYSYSTEM/SYSTEMID/DESCRIPTION"},
            readOnly = true)
    private String hierarchyPrimarySystemDesc;

    @Transient
    @EAMField(xpath = {
            "AssetParentHierarchy/AssetDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/PositionDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/PrimarySystemDependency/DEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/SystemDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/LocationDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",
            "AssetParentHierarchy/NonDependentParents/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",

            "PositionParentHierarchy/AssetDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/PositionDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/PrimarySystemDependency/DEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/SystemDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/LocationDependency/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",
            "PositionParentHierarchy/NonDependentParents/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",

            "SystemParentHierarchy/NONDEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE",
            "SystemParentHierarchy/DEPENDENTPRIMARYSYSTEM/SYSTEMID/ORGANIZATIONID/ORGANIZATIONCODE"},
            readOnly = true)
    private String hierarchyPrimarySystemOrg;
    @Transient
    private Boolean hierarchyPrimarySystemDependent;
    @Transient
    @EAMField(xpath = {
            "AssetParentHierarchy/AssetDependency/NONDEPENDENTPRIMARYSYSTEM/COSTROLLUP",
            "AssetParentHierarchy/PositionDependency/NONDEPENDENTPRIMARYSYSTEM/COSTROLLUP",
            "AssetParentHierarchy/PrimarySystemDependency/DEPENDENTPRIMARYSYSTEM/COSTROLLUP",
            "AssetParentHierarchy/SystemDependency/NONDEPENDENTPRIMARYSYSTEM/COSTROLLUP",
            "AssetParentHierarchy/LocationDependency/NONDEPENDENTPRIMARYSYSTEM/COSTROLLUP",
            "AssetParentHierarchy/NonDependentParents/NONDEPENDENTPRIMARYSYSTEM/COSTROLLUP",

            "PositionParentHierarchy/AssetDependency/NONDEPENDENTPRIMARYSYSTEM/COSTROLLUP",
            "PositionParentHierarchy/PositionDependency/NONDEPENDENTPRIMARYSYSTEM/COSTROLLUP",
            "PositionParentHierarchy/PrimarySystemDependency/DEPENDENTPRIMARYSYSTEM/COSTROLLUP",
            "PositionParentHierarchy/SystemDependency/NONDEPENDENTPRIMARYSYSTEM/COSTROLLUP",
            "PositionParentHierarchy/LocationDependency/NONDEPENDENTPRIMARYSYSTEM/COSTROLLUP",
            "PositionParentHierarchy/NonDependentParents/NONDEPENDENTPRIMARYSYSTEM/COSTROLLUP",

            "SystemParentHierarchy/NONDEPENDENTPRIMARYSYSTEM/COSTROLLUP",
            "SystemParentHierarchy/DEPENDENTPRIMARYSYSTEM/COSTROLLUP"},
            readOnly = true)
    private Boolean hierarchyPrimarySystemCostRollUp;
    // System
    @Transient
    private String hierarchySystemCode;
    @Transient
    private String hierarchySystemDesc;
    @Transient
    private Boolean hierarchySystemDependent;
    @Transient
    private Boolean hierarchySystemCostRollUp;
    // Location
    @Transient
    private String hierarchyLocationCode;
    @Transient
    private String hierarchyLocationDesc;

    // Part Association
    @Transient
    @EAMField(xpath = "PartAssociation/PARTID/PARTCODE", enforceValidXpath = false, nullifyParentLevel = 0)
    private String partCode;
    @Transient
    @EAMField(xpath = "PartAssociation/PARTID/DESCRIPTION", enforceValidXpath = false, readOnly = true)
    private String partDesc;
    @Transient
    @EAMField(xpath = "PartAssociation/STORELOCATION/STOREID/STORECODE", enforceValidXpath = false, nullifyParentLevel = 0)
    private String storeCode;
    @Transient
    @EAMField(xpath = "PartAssociation/STORELOCATION/STOREID/DESCRIPTION", enforceValidXpath = false, readOnly = true)
    private String storeDesc;
    @Transient
    @EAMField(xpath = "PartAssociation/STORELOCATION/BIN", enforceValidXpath = false, nullifyParentLevel = 0)
    private String bin;
    @Transient
    private String binDesc;
    @Transient
    @EAMField(xpath = "PartAssociation/STORELOCATION/LOT", enforceValidXpath = false, nullifyParentLevel = 0)
    private String lot;

    // Linear Reference
    @Transient
    @EAMField(xpath = "LINEARREFERENCEDETAILS/LINEARREFPRECISION", nullifyParentLevel = 0)
    private String linearRefPrecision;
    @Transient
    @EAMField(xpath = "LINEARREFERENCEDETAILS/LINEARREFUOM", nullifyParentLevel = 0)
    private String linearRefUOM;
    @Transient
    @EAMField(xpath = "LINEARREFERENCEDETAILS/EQUIPMENTLENGTH", nullifyParentLevel = 0)
    private BigDecimal linearRefEquipmentLength;
    @Transient
    @EAMField(xpath = "LINEARREFERENCEDETAILS/EQUIPMENTLENGTHUOM", nullifyParentLevel = 0)
    private String linearRefEquipmentLengthUOM;
    @Transient
    @EAMField(xpath = "LINEARREFERENCEDETAILS/GEOGRAPHICALREFERENCE", nullifyParentLevel = 0)
    private String linearRefGeographicalRef;

    // Variables
    @Transient
    @EAMField(xpath = "Variables/VARIABLE1", nullifyParentLevel = 0)
    private String variable1;
    @Transient
    @EAMField(xpath = "Variables/VARIABLE2", nullifyParentLevel = 0)
    private String variable2;
    @Transient
    @EAMField(xpath = "Variables/VARIABLE3", nullifyParentLevel = 0)
    private String variable3;
    @Transient
    @EAMField(xpath = "Variables/VARIABLE4", nullifyParentLevel = 0)
    private String variable4;
    @Transient
    @EAMField(xpath = "Variables/VARIABLE5", nullifyParentLevel = 0)
    private String variable5;
    @Transient
    @EAMField(xpath = "Variables/VARIABLE6", nullifyParentLevel = 0)
    private String variable6;

    // Dormant
    @Transient
    @EAMField(xpath = "DORMANT/DORMANTSTART")
    private Date dormantStart;
    @Transient
    @EAMField(xpath = "DORMANT/DORMANTEND")
    private Date dormantEnd;
    @Transient
    @EAMField(xpath = "DORMANT/DORMANTREUSE")
    private String dormantReusePeriod;

    @Transient
    @EAMField(xpath = "UserDefinedFields")
    private UserDefinedFields userDefinedFields;

    @Transient
    @EAMField(xpath = "SAFETY")
    private String safety;

    @Transient
    @EAMField(xpath = "ORIGINALINSTALLDATE")
    private Date originalInstallDate;

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Transient
    @EAMField(xpath = "XLOCATION")
    private BigDecimal xLocation;

    @Transient
    @EAMField(xpath = "YLOCATION")
    private BigDecimal yLocation;

    @Transient
    private String lastLocationCode;
    @Transient
    private String lastLocationDesc;

    // CERN Properties
    @Transient
    private String cernMachine;
    @Transient
    private Integer cernCao;
    @Transient
    private Double cernX;
    @Transient
    private Double cernY;
    @Transient
    private Double cernZ;
    @Transient
    private String cernPos;
    @Transient
    private String cernFonc;

    @Transient
    private String copyFrom;

    @Transient
    private HashMap<String, ArrayList<UDLValue>> userDefinedList;

    @Transient
    @EAMField(xpath = "FleetVehicleInfo/ISVEHICLE", nullifyParentLevel = 0)
    private Boolean vehicle;

    @Transient
    @EAMField(xpath = "FleetVehicleInfo/ISRENTAL", nullifyParentLevel = 0)
    private Boolean rental;

    @Transient
    @EAMField(xpath = "FleetVehicleInfo/VEHICLETYPE/TYPECODE", nullifyParentLevel = 1)
    private String vehicleTypeCode;

    @Transient
    @EAMField(xpath = "FleetVehicleInfo/AVAILABILITYSTATUS/STATUSCODE", nullifyParentLevel = 1)
    private String availabilityStatus;

    @Transient
    @EAMField(xpath = "FleetVehicleInfo/ISSUETO/PERSONCODE", nullifyParentLevel = 1)
    private String issueTo;

    @Transient
    @EAMField(xpath = "FleetVehicleInfo/CUSTOMERID/CUSTOMERCODE", nullifyParentLevel = 1)
    private String customerCode;

    @Transient
    @EAMField(xpath = "FleetVehicleInfo/RENTALTEMPLATEID/RENTALTEMPLATECODE", nullifyParentLevel = 1)
    private String rentalTemplateCode;

    @Transient
    @EAMField(xpath = "FleetVehicleInfo/RENTALTEMPLATEID/DESCRIPTION", nullifyParentLevel = 1, readOnly = true)
    private String rentalTemplateDesc;

    @Transient
    @EAMField(xpath="CHECKLISTFILTER")
    private String equipmentFilter;

    @EAMField(xpath = "WORKSPACEID/WORKSPACENUMBER")
    private String workspaceNo;

    @EAMField(xpath = "FUELID/FUELCODE")
    private String primaryFuel;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassDesc() {
        return classDesc;
    }

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public BigInteger getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(BigInteger updateCount) {
        this.updateCount = updateCount;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentDesc() {
        return departmentDesc;
    }

    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }

    @JsonProperty("customField")
    @XmlElementWrapper(name = "customFields")
    @XmlElement(name = "customField")
    public CustomField[] getCustomFields() {
        return customFields;
    }

    public void setCustomFields(CustomField[] customFields) {
        this.customFields = customFields;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getCostCodeDesc() {
        return costCodeDesc;
    }

    public void setCostCodeDesc(String costCodeDesc) {
        this.costCodeDesc = costCodeDesc;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getComissionDate() {
        return comissionDate;
    }

    public void setComissionDate(Date comissionDate) {
        this.comissionDate = comissionDate;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getEquipmentValue() {
        return equipmentValue;
    }

    public void setEquipmentValue(BigDecimal equipmentValue) {
        this.equipmentValue = equipmentValue;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getMeterUnit() {
        return meterUnit;
    }

    public void setMeterUnit(String meterUnit) {
        this.meterUnit = meterUnit;
    }

    public String getCriticality() {
        return criticality;
    }

    public void setCriticality(String criticality) {
        this.criticality = criticality;
    }

    public String getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(String manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(BigDecimal xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(BigDecimal yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getzCoordinate() {
        return zCoordinate;
    }

    public void setzCoordinate(BigDecimal zCoordinate) {
        this.zCoordinate = zCoordinate;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }

    public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }

    public String getHierarchyAssetCode() {
        return hierarchyAssetCode;
    }

    public void setHierarchyAssetCode(String hierarchyAssetCode) {
        this.hierarchyAssetCode = hierarchyAssetCode;
    }

    public String getHierarchyAssetDesc() {
        return hierarchyAssetDesc;
    }

    public void setHierarchyAssetDesc(String hierarchyAssetDesc) {
        this.hierarchyAssetDesc = hierarchyAssetDesc;
    }

    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean getHierarchyAssetDependent() {
        return hierarchyAssetDependent;
    }

    public void setHierarchyAssetDependent(Boolean hierarchyAssetDependent) {
        this.hierarchyAssetDependent = hierarchyAssetDependent;
    }

    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean getHierarchyAssetCostRollUp() {
        return hierarchyAssetCostRollUp;
    }

    public void setHierarchyAssetCostRollUp(Boolean hierarchyAssetCostRollUp) {
        this.hierarchyAssetCostRollUp = hierarchyAssetCostRollUp;
    }

    public String getHierarchyPositionCode() {
        return hierarchyPositionCode;
    }

    public void setHierarchyPositionCode(String hierarchyPositionCode) {
        this.hierarchyPositionCode = hierarchyPositionCode;
    }

    public String getHierarchyPositionDesc() {
        return hierarchyPositionDesc;
    }

    public void setHierarchyPositionDesc(String hierarchyPositionDesc) {
        this.hierarchyPositionDesc = hierarchyPositionDesc;
    }

    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean getHierarchyPositionDependent() {
        return hierarchyPositionDependent;
    }

    public void setHierarchyPositionDependent(Boolean hierarchyPositionDependent) {
        this.hierarchyPositionDependent = hierarchyPositionDependent;
    }

    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean getHierarchyPositionCostRollUp() {
        return hierarchyPositionCostRollUp;
    }

    public void setHierarchyPositionCostRollUp(Boolean hierarchyPositionCostRollUp) {
        this.hierarchyPositionCostRollUp = hierarchyPositionCostRollUp;
    }

    public String getHierarchyLocationCode() {
        return hierarchyLocationCode;
    }

    public void setHierarchyLocationCode(String hierarchyLocationCode) {
        this.hierarchyLocationCode = hierarchyLocationCode;
    }

    public String getHierarchyLocationDesc() {
        return hierarchyLocationDesc;
    }

    public void setHierarchyLocationDesc(String hierarchyLocationDesc) {
        this.hierarchyLocationDesc = hierarchyLocationDesc;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getSystemTypeCode() {
        return systemTypeCode;
    }

    public void setSystemTypeCode(String systemTypeCode) {
        this.systemTypeCode = systemTypeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getPartDesc() {
        return partDesc;
    }

    public void setPartDesc(String partDesc) {
        this.partDesc = partDesc;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getLinearRefPrecision() {
        return linearRefPrecision;
    }

    public void setLinearRefPrecision(String linearRefPrecision) {
        this.linearRefPrecision = linearRefPrecision;
    }

    public String getLinearRefUOM() {
        return linearRefUOM;
    }

    public void setLinearRefUOM(String linearRefUOM) {
        this.linearRefUOM = linearRefUOM;
    }

    public String getLinearRefGeographicalRef() {
        return linearRefGeographicalRef;
    }

    public void setLinearRefGeographicalRef(String linearRefGeographicalRef) {
        this.linearRefGeographicalRef = linearRefGeographicalRef;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getLinearRefEquipmentLength() {
        return linearRefEquipmentLength;
    }

    public void setLinearRefEquipmentLength(BigDecimal linearRefEquipmentLength) {
        this.linearRefEquipmentLength = linearRefEquipmentLength;
    }

    public String getLinearRefEquipmentLengthUOM() {
        return linearRefEquipmentLengthUOM;
    }

    public void setLinearRefEquipmentLengthUOM(String linearRefEquipmentLengthUOM) {
        this.linearRefEquipmentLengthUOM = linearRefEquipmentLengthUOM;
    }

    public String getVariable1() {
        return variable1;
    }

    public void setVariable1(String variable1) {
        this.variable1 = variable1;
    }

    public String getVariable2() {
        return variable2;
    }

    public void setVariable2(String variable2) {
        this.variable2 = variable2;
    }

    public String getVariable3() {
        return variable3;
    }

    public void setVariable3(String variable3) {
        this.variable3 = variable3;
    }

    public String getVariable4() {
        return variable4;
    }

    public void setVariable4(String variable4) {
        this.variable4 = variable4;
    }

    public String getVariable5() {
        return variable5;
    }

    public void setVariable5(String variable5) {
        this.variable5 = variable5;
    }

    public String getVariable6() {
        return variable6;
    }

    public void setVariable6(String variable6) {
        this.variable6 = variable6;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDormantStart() {
        return dormantStart;
    }

    public void setDormantStart(Date dormantStart) {
        this.dormantStart = dormantStart;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDormantEnd() {
        return dormantEnd;
    }

    public void setDormantEnd(Date dormantEnd) {
        this.dormantEnd = dormantEnd;
    }

    public String getDormantReusePeriod() {
        return dormantReusePeriod;
    }

    public void setDormantReusePeriod(String dormantReusePeriod) {
        this.dormantReusePeriod = dormantReusePeriod;
    }

    public String getcGMP() {
        return cGMP;
    }

    public void setcGMP(String cGMP) {
        this.cGMP = cGMP;
    }

    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean getOutOfService() {
        return outOfService;
    }

    public void setOutOfService(Boolean outOfService) {
        this.outOfService = outOfService;
    }

    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean getInProduction() {
        return inProduction;
    }

    public void setInProduction(Boolean inProduction) {
        this.inProduction = inProduction;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getCostOfNeededRepairs() {
        return costOfNeededRepairs;
    }

    public void setCostOfNeededRepairs(BigDecimal costOfNeededRepairs) {
        this.costOfNeededRepairs = costOfNeededRepairs;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getReplacementValue() {
        return replacementValue;
    }

    public void setReplacementValue(BigDecimal replacementValue) {
        this.replacementValue = replacementValue;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getFacilityConditionIndex() {
        return facilityConditionIndex;
    }

    public void setFacilityConditionIndex(BigDecimal facilityConditionIndex) {
        this.facilityConditionIndex = facilityConditionIndex;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(BigDecimal yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getServiceLifetime() {
        return serviceLifetime;
    }

    public void setServiceLifetime(BigDecimal serviceLifetime) {
        this.serviceLifetime = serviceLifetime;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getOriginalReceiptDate() {
        return originalReceiptDate;
    }

    public void setOriginalReceiptDate(Date originalReceiptDate) {
        this.originalReceiptDate = originalReceiptDate;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getOriginalInstallDate() {
        return originalInstallDate;
    }

    public void setOriginalInstallDate(Date originalInstallDate) {
        this.originalInstallDate = originalInstallDate;
    }

    public String getAssignedToDesc() {
        return assignedToDesc;
    }

    public void setAssignedToDesc(String assignedToDesc) {
        this.assignedToDesc = assignedToDesc;
    }

    public String getManufacturerDesc() {
        return manufacturerDesc;
    }

    public void setManufacturerDesc(String manufacturerDesc) {
        this.manufacturerDesc = manufacturerDesc;
    }

    public String getStoreDesc() {
        return storeDesc;
    }

    public void setStoreDesc(String storeDesc) {
        this.storeDesc = storeDesc;
    }

    public String getBinDesc() {
        return binDesc;
    }

    public void setBinDesc(String binDesc) {
        this.binDesc = binDesc;
    }

    /**
     * @return the lastLocationCode
     */
    public String getLastLocationCode() {
        return lastLocationCode;
    }

    /**
     * @param lastLocationCode the lastLocationCode to set
     */
    public void setLastLocationCode(String lastLocationCode) {
        this.lastLocationCode = lastLocationCode;
    }

    /**
     * @return the lastLocationDesc
     */
    public String getLastLocationDesc() {
        return lastLocationDesc;
    }

    /**
     * @param lastLocationDesc the lastLocationDesc to set
     */
    public void setLastLocationDesc(String lastLocationDesc) {
        this.lastLocationDesc = lastLocationDesc;
    }

    /**
     * @return the cernMachine
     */
    public String getCernMachine() {
        return cernMachine;
    }

    /**
     * @param cernMachine the cernMachine to set
     */
    public void setCernMachine(String cernMachine) {
        this.cernMachine = cernMachine;
    }

    /**
     * @return the cernCao
     */
    public Integer getCernCao() {
        return cernCao;
    }

    /**
     * @param cernCao the cernCao to set
     */
    public void setCernCao(Integer cernCao) {
        this.cernCao = cernCao;
    }

    /**
     * @return the cernX
     */
    public Double getCernX() {
        return cernX;
    }

    /**
     * @param cernX the cernX to set
     */
    public void setCernX(Double cernX) {
        this.cernX = cernX;
    }

    /**
     * @return the cernY
     */
    public Double getCernY() {
        return cernY;
    }

    /**
     * @param cernY the cernY to set
     */
    public void setCernY(Double cernY) {
        this.cernY = cernY;
    }

    /**
     * @return the cernZ
     */
    public Double getCernZ() {
        return cernZ;
    }

    /**
     * @param cernZ the cernZ to set
     */
    public void setCernZ(Double cernZ) {
        this.cernZ = cernZ;
    }

    /**
     * @return the cernPos
     */
    public String getCernPos() {
        return cernPos;
    }

    /**
     * @param cernPos the cernPos to set
     */
    public void setCernPos(String cernPos) {
        this.cernPos = cernPos;
    }

    /**
     * @return the cernFonc
     */
    public String getCernFonc() {
        return cernFonc;
    }

    /**
     * @param cernFonc the cernFonc to set
     */
    public void setCernFonc(String cernFonc) {
        this.cernFonc = cernFonc;
    }

    public String getHierarchyPrimarySystemCode() {
        return hierarchyPrimarySystemCode;
    }

    public void setHierarchyPrimarySystemCode(String hierarchyPrimarySystemCode) {
        this.hierarchyPrimarySystemCode = hierarchyPrimarySystemCode;
    }

    public String getHierarchyPrimarySystemDesc() {
        return hierarchyPrimarySystemDesc;
    }

    public void setHierarchyPrimarySystemDesc(String hierarchyPrimarySystemDesc) {
        this.hierarchyPrimarySystemDesc = hierarchyPrimarySystemDesc;
    }

    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean getHierarchyPrimarySystemDependent() {
        return hierarchyPrimarySystemDependent;
    }

    public void setHierarchyPrimarySystemDependent(Boolean hierarchyPrimarySystemDependent) {
        this.hierarchyPrimarySystemDependent = hierarchyPrimarySystemDependent;
    }

    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean getHierarchyPrimarySystemCostRollUp() {
        return hierarchyPrimarySystemCostRollUp;
    }

    public void setHierarchyPrimarySystemCostRollUp(Boolean hierarchyPrimarySystemCostRollUp) {
        this.hierarchyPrimarySystemCostRollUp = hierarchyPrimarySystemCostRollUp;
    }

    public BigDecimal getxLocation() {
        return xLocation;
    }

    public void setxLocation(BigDecimal xLocation) {
        this.xLocation = xLocation;
    }

    public BigDecimal getyLocation() {
        return yLocation;
    }

    public void setyLocation(BigDecimal yLocation) {
        this.yLocation = yLocation;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public String getHierarchySystemCode() {
        return hierarchySystemCode;
    }

    public void setHierarchySystemCode(String hierarchySystemCode) {
        this.hierarchySystemCode = hierarchySystemCode;
    }

    public String getHierarchySystemDesc() {
        return hierarchySystemDesc;
    }

    public void setHierarchySystemDesc(String hierarchySystemDesc) {
        this.hierarchySystemDesc = hierarchySystemDesc;
    }

    public Boolean getHierarchySystemDependent() {
        return hierarchySystemDependent;
    }

    public void setHierarchySystemDependent(Boolean hierarchySystemDependent) {
        this.hierarchySystemDependent = hierarchySystemDependent;
    }

    public Boolean getHierarchySystemCostRollUp() {
        return hierarchySystemCostRollUp;
    }

    public void setHierarchySystemCostRollUp(Boolean hierarchySystemCostRollUp) {
        this.hierarchySystemCostRollUp = hierarchySystemCostRollUp;
    }

    @Override
    public String getCopyFrom() {
        return copyFrom;
    }

    public void setCopyFrom(String copyFrom) {
        this.copyFrom = copyFrom;
    }

    @Override
    public HashMap<String, ArrayList<UDLValue>> getUserDefinedList() {
        return userDefinedList;
    }

    @Override
    public void setUserDefinedList(HashMap<String, ArrayList<UDLValue>> userDefinedList) {
        this.userDefinedList = userDefinedList;
    }

    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean getVehicle() {
        return vehicle;
    }

    public void setVehicle(Boolean vehicle) {
        this.vehicle = vehicle;
    }

    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean getRental() {
        return rental;
    }

    public void setRental(Boolean rental) {
        this.rental = rental;
    }

    public String getVehicleTypeCode() {
        return vehicleTypeCode;
    }

    public void setVehicleTypeCode(String vehicleTypeCode) {
        this.vehicleTypeCode = vehicleTypeCode;
    }

    public String getHierarchyAssetOrg() {
        return hierarchyAssetOrg;
    }

    public void setHierarchyAssetOrg(String hierarchyAssetOrg) {
        this.hierarchyAssetOrg = hierarchyAssetOrg;
    }

    public String getHierarchyPositionOrg() {
        return hierarchyPositionOrg;
    }

    public void setHierarchyPositionOrg(String hierarchyPositionOrg) {
        this.hierarchyPositionOrg = hierarchyPositionOrg;
    }

    public String getHierarchyPrimarySystemOrg() {
        return hierarchyPrimarySystemOrg;
    }

    public void setHierarchyPrimarySystemOrg(String hierarchyPrimarySystemOrg) {
        this.hierarchyPrimarySystemOrg = hierarchyPrimarySystemOrg;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(final String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getIssueTo() {
        return issueTo;
    }

    public void setIssueTo(final String issueTo) {
        this.issueTo = issueTo;
    }

    public String getEquipmentFilter() {
        return equipmentFilter;
    }

    public void setEquipmentFilter(String equipmentFilter) {
        this.equipmentFilter = equipmentFilter;
    }

    public String getWorkspaceNo() {
        return workspaceNo;
    }

    public void setWorkspaceNo(String workspaceNo) {
        this.workspaceNo = workspaceNo;
    }

    public String getPrimaryFuel() {
        return primaryFuel;
    }

    public void setPrimaryFuel(String primaryFuel) {
        this.primaryFuel = primaryFuel;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getRentalTemplateCode() {
        return rentalTemplateCode;
    }

    public void setRentalTemplateCode(String rentalTemplateCode) {
        this.rentalTemplateCode = rentalTemplateCode;
    }

    public String getRentalTemplateDesc() {
        return rentalTemplateDesc;
    }

    public void setRentalTemplateDesc(String rentalTemplateDesc) {
        this.rentalTemplateDesc = rentalTemplateDesc;
    }

    @Override
    public String toString() {
        return "Equipment [" + (description != null ? "description=" + description + ", " : "")
                + (code != null ? "code=" + code + ", " : "") + (typeCode != null ? "typeCode=" + typeCode + ", " : "")
                + (alias != null ? "alias=" + alias + ", " : "")
                + (typeDesc != null ? "typeDesc=" + typeDesc + ", " : "")
                + (classCode != null ? "classCode=" + classCode + ", " : "")
                + (classDesc != null ? "classDesc=" + classDesc + ", " : "")
                + (categoryCode != null ? "categoryCode=" + categoryCode + ", " : "")
                + (categoryDesc != null ? "categoryDesc=" + categoryDesc + ", " : "")
                + (updateCount != null ? "updateCount=" + updateCount + ", " : "")
                + (outOfService != null ? "outOfService=" + outOfService + ", " : "")
                + (inProduction != null ? "inProduction=" + inProduction + ", " : "")
                + (statusCode != null ? "statusCode=" + statusCode + ", " : "")
                + (statusDesc != null ? "statusDesc=" + statusDesc + ", " : "")
                + (costCode != null ? "costCode=" + costCode + ", " : "")
                + (departmentCode != null ? "departmentCode=" + departmentCode + ", " : "")
                + (departmentDesc != null ? "departmentDesc=" + departmentDesc + ", " : "")
                + (customFields != null ? "customFields=" + Arrays.toString(customFields) + ", " : "")
                + (comissionDate != null ? "comissionDate=" + comissionDate + ", " : "")
                + (equipmentValue != null ? "equipmentValue=" + equipmentValue + ", " : "")
                + (assignedTo != null ? "assignedTo=" + assignedTo + ", " : "")
                + (assignedToDesc != null ? "assignedToDesc=" + assignedToDesc + ", " : "")
                + (meterUnit != null ? "meterUnit=" + meterUnit + ", " : "")
                + (criticality != null ? "criticality=" + criticality + ", " : "")
                + (cGMP != null ? "cGMP=" + cGMP + ", " : "")
                + (cGMP != null ? "cGMP=" + cGMP + ", " : "")
                + (originalReceiptDate != null ? "originalReceiptDate=" + originalReceiptDate + ", " : "")
                + (manufacturerCode != null ? "manufacturerCode=" + manufacturerCode + ", " : "")
                + (manufacturerDesc != null ? "manufacturerDesc=" + manufacturerDesc + ", " : "")
                + (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "")
                + (model != null ? "model=" + model + ", " : "")
                + (revision != null ? "revision=" + revision + ", " : "")
                + (xCoordinate != null ? "xCoordinate=" + xCoordinate + ", " : "")
                + (yCoordinate != null ? "yCoordinate=" + yCoordinate + ", " : "")
                + (zCoordinate != null ? "zCoordinate=" + zCoordinate + ", " : "")
                + (costOfNeededRepairs != null ? "costOfNeededRepairs=" + costOfNeededRepairs + ", " : "")
                + (replacementValue != null ? "replacementValue=" + replacementValue + ", " : "")
                + (facilityConditionIndex != null ? "facilityConditionIndex=" + facilityConditionIndex + ", " : "")
                + (yearBuilt != null ? "yearBuilt=" + yearBuilt + ", " : "")
                + (serviceLifetime != null ? "serviceLifetime=" + serviceLifetime + ", " : "")
                + (hierarchyAssetCode != null ? "hierarchyAssetCode=" + hierarchyAssetCode + ", " : "")
                + (hierarchyAssetDesc != null ? "hierarchyAssetDesc=" + hierarchyAssetDesc + ", " : "")
                + (hierarchyAssetDependent != null ? "hierarchyAssetDependent=" + hierarchyAssetDependent + ", " : "")
                + (hierarchyAssetCostRollUp != null ? "hierarchyAssetCostRollUp=" + hierarchyAssetCostRollUp + ", "
                : "")
                + (hierarchyPositionCode != null ? "hierarchyPositionCode=" + hierarchyPositionCode + ", " : "")
                + (hierarchyPositionDesc != null ? "hierarchyPositionDesc=" + hierarchyPositionDesc + ", " : "")
                + (hierarchyPositionDependent != null
                ? "hierarchyPositionDependent=" + hierarchyPositionDependent + ", "
                : "")
                + (hierarchyPositionCostRollUp != null
                ? "hierarchyPositionCostRollUp=" + hierarchyPositionCostRollUp + ", "
                : "")
                + (hierarchyPrimarySystemCode != null
                ? "hierarchyPrimarySystemCode=" + hierarchyPrimarySystemCode + ", "
                : "")
                + (hierarchyPrimarySystemDesc != null
                ? "hierarchyPrimarySystemDesc=" + hierarchyPrimarySystemDesc + ", "
                : "")
                + (hierarchyPrimarySystemDependent != null
                ? "hierarchyPrimarySystemDependent=" + hierarchyPrimarySystemDependent + ", "
                : "")
                + (hierarchyPrimarySystemCostRollUp != null
                ? "hierarchyPrimarySystemCostRollUp=" + hierarchyPrimarySystemCostRollUp + ", "
                : "")
                + (hierarchyLocationCode != null ? "hierarchyLocationCode=" + hierarchyLocationCode + ", " : "")
                + (hierarchyLocationDesc != null ? "hierarchyLocationDesc=" + hierarchyLocationDesc + ", " : "")
                + (partCode != null ? "partCode=" + partCode + ", " : "")
                + (partDesc != null ? "partDesc=" + partDesc + ", " : "")
                + (storeCode != null ? "storeCode=" + storeCode + ", " : "")
                + (storeDesc != null ? "storeDesc=" + storeDesc + ", " : "") + (bin != null ? "bin=" + bin + ", " : "")
                + (binDesc != null ? "binDesc=" + binDesc + ", " : "") + (lot != null ? "lot=" + lot + ", " : "")
                + (linearRefPrecision != null ? "linearRefPrecision=" + linearRefPrecision + ", " : "")
                + (linearRefUOM != null ? "linearRefUOM=" + linearRefUOM + ", " : "")
                + (linearRefEquipmentLength != null ? "linearRefEquipmentLength=" + linearRefEquipmentLength + ", "
                : "")
                + (linearRefEquipmentLengthUOM != null
                ? "linearRefEquipmentLengthUOM=" + linearRefEquipmentLengthUOM + ", "
                : "")
                + (linearRefGeographicalRef != null ? "linearRefGeographicalRef=" + linearRefGeographicalRef + ", "
                : "")
                + (variable1 != null ? "variable1=" + variable1 + ", " : "")
                + (variable2 != null ? "variable2=" + variable2 + ", " : "")
                + (variable3 != null ? "variable3=" + variable3 + ", " : "")
                + (variable4 != null ? "variable4=" + variable4 + ", " : "")
                + (variable5 != null ? "variable5=" + variable5 + ", " : "")
                + (variable6 != null ? "variable6=" + variable6 + ", " : "")
                + (dormantStart != null ? "dormantStart=" + dormantStart + ", " : "")
                + (dormantEnd != null ? "dormantEnd=" + dormantEnd + ", " : "")
                + (dormantReusePeriod != null ? "dormantReusePeriod=" + dormantReusePeriod + ", " : "")
                + (userDefinedFields != null ? "userDefinedFields=" + userDefinedFields + ", " : "")
                + (safety != null ? "safety=" + safety + ", " : "")
                + (originalInstallDate != null ? "originalInstallDate=" + originalInstallDate + ", " : "")
                + (lastLocationCode != null ? "lastLocationCode=" + lastLocationCode + ", " : "")
                + (lastLocationDesc != null ? "lastLocationDesc=" + lastLocationDesc + ", " : "")
                + (cernMachine != null ? "cernMachine=" + cernMachine + ", " : "")
                + (cernCao != null ? "cernCao=" + cernCao + ", " : "") + (cernX != null ? "cernX=" + cernX + ", " : "")
                + (cernY != null ? "cernY=" + cernY + ", " : "") + (cernZ != null ? "cernZ=" + cernZ + ", " : "")
                + (cernPos != null ? "cernPos=" + cernPos + ", " : "")
                + (cernFonc != null ? "cernFonc=" + cernFonc + ", " : "")
                + (copyFrom != null ? "copyFrom=" + copyFrom + ", " : "")
                + (userDefinedList != null ? "userDefinedList=" + userDefinedList : "")
                + "]";
    }
}
