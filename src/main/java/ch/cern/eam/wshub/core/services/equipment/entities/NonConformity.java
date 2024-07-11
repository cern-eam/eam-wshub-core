package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.annotations.GridField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@ToString
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class NonConformity {
    @GridField(name = "description")
    @EAMField(xpath = "NONCONFORMITYID/DESCRIPTION")
    private String description;

    @GridField(name = "organization")
    @EAMField(xpath = "NONCONFORMITYID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String organizationCode;

    @GridField(name = "nonconformity")
    @EAMField(xpath = "NONCONFORMITYID/STANDARDENTITYCODE")
    private String code;

    @GridField(name = "aspectcode")
    @EAMField(xpath = "ASPECTID/ASPECTCODE")
    private String aspectCode;

    @GridField(name = "aspectdescription")
    @EAMField(xpath = "ASPECTID/DESCRIPTION")
    private String aspectDescription;

    @GridField(name = "classCode")
    @EAMField(xpath = "CLASSID/CLASSCODE")
    private String classCode;

    @GridField(name = "classorg")
    @EAMField(xpath = "CLASSID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String classOrgCode;

    @GridField(name = "department")
    @EAMField(xpath = "DEPARTMENTID/DEPARTMENTCODE")
    private String department;

    @GridField(name = "equipmentdesc")
    @EAMField(xpath = "EQUIPMENTID/DESCRIPTION")
    private String equipmentDesc;

    @GridField(name = "equipment")
    @EAMField(xpath = "EQUIPMENTID/EQUIPMENTCODE")
    private String equipmentCode;

    @GridField(name = "equipmentorg")
    @EAMField(xpath = "EQUIPMENTID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String equipmentOrg;

    @GridField(name = "equipmentassignedto")
    @EAMField(xpath = "EquipmentDetails/ASSIGNEDTO/PERSONCODE")
    private String equipmentAssignedTo;

    @GridField(name = "equipmentcategory")
    @EAMField(xpath = "EquipmentDetails/CATEGORYID/CATEGORYCODE")
    private String equipmentCategory;

    @GridField(name = "equipmentcriticality")
    @EAMField(xpath = "EquipmentDetails/CRITICALITYID/CRITICALITY")
    private String equipmentCriticality;

    @GridField(name = "equipmentcriticalitydesc")
    @EAMField(xpath = "EquipmentDetails/CRITICALITYID/DESCRIPTION")
    private String equipmentCriticalityDesc;

    @GridField(name = "equipmentdepartment")
    @EAMField(xpath = "EquipmentDetails/DEPARTMENTID/DEPARTMENTCODE")
    private String equipmentDepartment;

    @GridField(name = "equipmentclass")
    @EAMField(xpath = "EquipmentDetails/EQUIPMENTCLASSID/CLASSCODE")
    private String equipmentClassCode;

    @GridField(name = "equipmentclassorg")
    @EAMField(xpath = "EquipmentDetails/EQUIPMENTCLASSID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String equipmentClassOrg;

    @GridField(name = "equipmentstatusdesc")
    @EAMField(xpath = "EquipmentDetails/EQUIPMENTSTATUS/DESCRIPTION")
    private String equipmentStatusDesc;

    @GridField(name = "equipmentstatus")
    @EAMField(xpath = "EquipmentDetails/EQUIPMENTSTATUS/STATUSCODE")
    private String equipmentStatus;

    @GridField(name = "equipmentmanufacturer")
    @EAMField(xpath = "EquipmentDetails/MANUFACTURERID/MANUFACTURERCODE")
    private String equipmentManufacturer;

    @GridField(name = "equipmentmodel")
    @EAMField(xpath = "EquipmentDetails/MODEL")
    private String equipmentModel;

    @GridField(name = "equipmentoperationalstatusdesc")
    @EAMField(xpath = "EquipmentDetails/OPERATIONALSTATUS/DESCRIPTION")
    private String equipmentOperationalStatusDesc;

    @GridField(name = "equipmentoperationalstatus")
    @EAMField(xpath = "EquipmentDetails/OPERATIONALSTATUS/STATUSCODE")
    private String equipmentOperationalStatus;

    @GridField(name = "equipmentsafety")
    @EAMField(xpath = "EquipmentDetails/SAFETY")
    private Boolean equipmentSafety;

    @GridField(name = "equipmenttypedesc")
    @EAMField(xpath = "EquipmentDetails/TYPE/DESCRIPTION")
    private String equipmenTtypeDesc;

    @GridField(name = "equipmenttype")
    @EAMField(xpath = "EquipmentDetails/TYPE/TYPECODE")
    private String equipmentType;

    @GridField(name = "highestobservation")
    @EAMField(xpath = "HIGHESTOBSERVATIONNUM")
    private BigDecimal highestObservation;

    @GridField(name = "locationdesc")
    @EAMField(xpath = "LOCATIONID/DESCRIPTION")
    private String locationDesc;

    @GridField(name = "location")
    @EAMField(xpath = "LOCATIONID/LOCATIONCODE")
    private String locationCode;

    @GridField(name = "locationorg")
    @EAMField(xpath = "LOCATIONID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String locationOrgCode;

    @GridField(name = "flow")
    @EAMField(xpath = "LinearReferenceDetails/FLOWCODE")
    private String flow;

    @GridField(name = "inspectiondirection")
    @EAMField(xpath = "LinearReferenceDetails/INSPECTIONDIRECTIONCODE")
    private String inspectionDirection;

    @GridField(name = "fromxcoordinate")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMCOORDINATE/X")
    private BigDecimal fromxCoordinate;

    @GridField(name = "fromycoordinate")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMCOORDINATE/Y")
    private BigDecimal fromyCoordinate;

    @GridField(name = "ncffromgeoref")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMGEOREF")
    private String ncfFromGeoref;

    @GridField(name = "fromhorizontaloffset")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/HORIZONTALOFFSET")
    private BigDecimal fromHorizontalOffset;

    @GridField(name = "fromhoroffsettype")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/HORIZONTALOFFSETTYPEID/TYPECODE")
    private String fromoroffsettype;

    @GridField(name = "fromhoroffsetuom")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/HORIZONTALOFFSETUOM/LINEARREFUOM")
    private String fromhoroffsetuom;

    @GridField(name = "fromrelationshiptype")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/RELATIONSHIPTYPEID/TYPECODE")
    private String fromrelationshiptype;

    @GridField(name = "fromverticaloffset")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/VERTICALOFFSET")
    private BigDecimal fromverticaloffset;

    @GridField(name = "fromverticaloffsettype")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/VERTICALOFFSETTYPEID/TYPECODE")
    private String fromverticaloffsettype;

    @GridField(name = "fromverticaloffsetuom")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/VERTICALOFFSETUOM/LINEARREFUOM")
    private String fromverticaloffsetuom;

    @GridField(name = "fromlatitude")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMLATITUDE")
    private BigDecimal fromlatitude;

    @GridField(name = "fromlongitude")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMLONGITUDE")
    private BigDecimal fromlongitude;

    @GridField(name = "fromoffset")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMOFFSET")
    private BigDecimal fromoffset;

    @GridField(name = "fromoffsetdirection")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMOFFSETDIRECTION/UCODE")
    private String fromoffsetdirection;

    @GridField(name = "fromoffsetpercent")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMOFFSETPERCENTAGE")
    private BigDecimal fromoffsetpercent;

    @GridField(name = "ncffrompoint")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMPOINT")
    private BigDecimal ncffrompoint;

    @GridField(name = "ncffromrefdesc")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMREFDESC")
    private String ncffromrefdesc;

    @GridField(name = "fromreferencepoint")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMREFERENCEID/DESCRIPTION")
    private String fromreferencepoint;

    @GridField(name = "relatedfromreference")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMREFERENCEID/LINEARREFERENCECODE")
    private String relatedfromreference;

    @GridField(name = "relationshiptype")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/RELATIONSHIPTYPEID/TYPECODE")
    private String relationshiptype;

    @GridField(name = "toxcoordinate")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOCOORDINATE/X")
    private BigDecimal toXcoordinate;

    @GridField(name = "toycoordinate")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOCOORDINATE/Y")
    private BigDecimal toYcoordinate;

    @GridField(name = "ncftogeoref")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOGEOREF")
    private String ncfToGeoref;

    @GridField(name = "tohorizontaloffset")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/HORIZONTALOFFSET")
    private BigDecimal toHorizontalOffset;

    @GridField(name = "tohorizontaloffsettype")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/HORIZONTALOFFSETTYPEID/TYPECODE")
    private String toHorizontalOffseTtype;

    @GridField(name = "tohorizontaloffsetuom")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/HORIZONTALOFFSETUOM/LINEARREFUOM")
    private String toHorizontalOffsetUOM;

    @GridField(name = "torelationshiptype")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/RELATIONSHIPTYPEID/TYPECODE")
    private String toRelationshipType;

    @GridField(name = "toverticaloffset")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/VERTICALOFFSET")
    private BigDecimal toVerticalOffset;

    @GridField(name = "toverticaloffsettype")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/VERTICALOFFSETTYPEID/TYPECODE")
    private String toBerticalOffsetType;

    @GridField(name = "toverticaloffsetuom")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/VERTICALOFFSETUOM/LINEARREFUOM")
    private String toVerticalOffsetUOM;

    @GridField(name = "tolatitude")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOLATITUDE")
    private BigDecimal toLatitude;

    @GridField(name = "tolongitude")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOLONGITUDE")
    private BigDecimal toLongitude;

    @GridField(name = "tooffset")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOOFFSET")
    private BigDecimal toOffset;

    @GridField(name = "tooffsetdirection")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOOFFSETDIRECTION/UCODE")
    private String tOoffsetDirection;

    @GridField(name = "tooffsetpercent")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOOFFSETPERCENTAGE")
    private BigDecimal toOffsetPercent;

    @GridField(name = "ncftopoint")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOPOINT")
    private BigDecimal ncfToPoint;

    @GridField(name = "ncftorefdesc")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOREFDESC")
    private String ncfTorefDesc;

    @GridField(name = "toreferencepoint")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOREFERENCEID/DESCRIPTION")
    private String toReferencePoint;

    @GridField(name = "relatedtoreference")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOREFERENCEID/LINEARREFERENCECODE")
    private String relatedToReference;

    @GridField(name = "ncftopointuom")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFUOM")
    private String ncfToPointUOM;

    @GridField(name = "ncffrompointuom")
    @EAMField(xpath = "LinearReferenceDetails/LINEARREFUOM")
    private String ncfFromPointUOM;

    @GridField(name = "materialtype")
    @EAMField(xpath = "MATERIALTYPE/USERDEFINEDCODE")
    private String materialType;

    @GridField(name = "importance")
    @EAMField(xpath = "NONCONFORMITYIMPORTANCE/USERDEFINEDCODE")
    private String importance;

    @GridField(name = "intensity")
    @EAMField(xpath = "NONCONFORMITYINTENSITY/USERDEFINEDCODE")
    private String intensity;

    @GridField(name = "nonconformitynote")
    @EAMField(xpath = "NONCONFORMITYNOTE")
    private String nonConformityNote;

    @GridField(name = "severity")
    @EAMField(xpath = "NONCONFORMITYSEVERITY/USERDEFINEDCODE")
    private String severity;

    @GridField(name = "ncfsize")
    @EAMField(xpath = "NONCONFORMITYSIZE")
    private BigDecimal ncfSize;

    @GridField(name = "typedesc")
    @EAMField(xpath = "NONCONFORMITYTYPEID/DESCRIPTION")
    private String typeDesc;

    @GridField(name = "type")
    @EAMField(xpath = "NONCONFORMITYTYPEID/NONCONFORMITYTYPECODE")
    private String typeCode;

    @GridField(name = "typeorg")
    @EAMField(xpath = "NONCONFORMITYTYPEID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String typeOrgCode;

    @GridField(name = "partdesc")
    @EAMField(xpath = "PARTID/DESCRIPTION")
    private String partDesc;

    @GridField(name = "partorg")
    @EAMField(xpath = "PARTID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String partorg;

    @GridField(name = "part")
    @EAMField(xpath = "PARTID/PARTCODE")
    private String partCode;

    @GridField(name = "priority")
    @EAMField(xpath = "PRIORITY/PRIORITYCODE")
    private String priority;

    @GridField(name = "status")
    @EAMField(xpath = "STATUS/STATUSCODE")
    private String statusCode;

    @GridField(name = "conditionindex")
    @EAMField(xpath = "TrackingDetails/CONDITIONINDEX/USERDEFINEDCODE")
    private String conditionIndex;

    @GridField(name = "conditionscore")
    @EAMField(xpath = "TrackingDetails/CONDITIONSCORE")
    private BigDecimal conditionScore;

    @GridField(name = "createdby")
    @EAMField(xpath = "TrackingDetails/CREATEDBY/USERCODE")
    private String createdBy;

    @GridField(name = "created")
    @EAMField(xpath = "TrackingDetails/CREATEDDATE")
    private Date createdDate;

    @Setter
    @GridField(name = "updated")
    @EAMField(xpath = "TrackingDetails/DATEUPDATED")
    private Date updatedDate;

    @GridField(name = "ncfestlaborcost")
    @EAMField(xpath = "TrackingDetails/ESTIMATEDLABORCOST")
    private BigDecimal ncfestlaborcost;

    @GridField(name = "ncfestmatlcost")
    @EAMField(xpath = "TrackingDetails/ESTIMATEDMATERIALCOST")
    private BigDecimal ncfestmatlcost;

    @GridField(name = "ncfestmisccost")
    @EAMField(xpath = "TrackingDetails/ESTIMATEDMISCELLANEOUSCOST")
    private BigDecimal ncfestmisccost;

    @GridField(name = "ncftotalestcost")
    @EAMField(xpath = "TrackingDetails/ESTIMATEDTOTALCOST")
    private BigDecimal ncftotalestcost;

    @Setter
    @GridField(name = "nextinspectdate")
    @EAMField(xpath = "TrackingDetails/NEXTINSPECTIONDATE")
    private Date nextInspectDate;

    @Setter
    @GridField(name = "nextinspectdateoverride")
    @EAMField(xpath = "TrackingDetails/NEXTINSPECTIONDATEOVERRIDE")
    private Date nextInspectDateOverride;

    @GridField(name = "mergedinto")
    @EAMField(xpath = "TrackingDetails/NONCONFORMITYMERGEINTO")
    private String mergedinto;

    @GridField(name = "observationoverride")
    @EAMField(xpath = "TrackingDetails/OVERRIDEOBSERVATION")
    private Boolean observationoverride;

    @GridField(name = "repairdate")
    @EAMField(xpath = "TrackingDetails/RECOMMENDEDREPAIRDATE")
    private Date repairDate;

    @GridField(name = "sourceobservation")
    @EAMField(xpath = "TrackingDetails/SOURCEOBSERVATIONNUM")
    private BigDecimal sourceobservation;

    @GridField(name = "updatedby")
    @EAMField(xpath = "TrackingDetails/UPDATEDBY/USERCODE")
    private String updatedBy;

    @GridField(name = "syslevel")
    @EAMField(xpath = "VMRSCODE/SYSTEMLEVELID/SYSTEMLEVELCODE")
    private String syslevel;

    @GridField(name = "asslevel")
    @EAMField(xpath = "VMRSCODE/VMRSASSEMBLYID/ASSEMBLYLEVELID/ASSEMBLYLEVELCODE")
    private String asslevel;

    @GridField(name = "complevel")
    @EAMField(xpath = "VMRSCODE/VMRSCOMPNENTID/COMPONENTLEVELID/COMPONENTLEVELCODE")
    private String complevel;

    @GridField(name = "recordid")
    @EAMField(xpath = "recordid")
    private BigInteger recordid;

    @EAMField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;

    @Transient
    @EAMField(xpath = "USERDEFINEDAREA")
    private CustomField[] customFields;

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getNextInspectDateOverride() {
        return nextInspectDateOverride;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getNextInspectDate() {
        return nextInspectDate;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getUpdatedDate() {
        return updatedDate;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getCreatedDate() {
        return createdDate;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getRepairDate() {
        return repairDate;
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

}
