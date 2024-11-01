package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.GridField;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NonConformity {
    @GridField(name = "description")
    @InforField(xpath = "NONCONFORMITYID/DESCRIPTION")
    private String description;

    @GridField(name = "organization")
    @InforField(xpath = "NONCONFORMITYID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String organizationCode;

    @GridField(name = "nonconformity")
    @InforField(xpath = "NONCONFORMITYID/STANDARDENTITYCODE")
    private String code;

    @GridField(name = "aspectcode")
    @InforField(xpath = "ASPECTID/ASPECTCODE")
    private String aspectCode;

    @GridField(name = "aspectdescription")
    @InforField(xpath = "ASPECTID/DESCRIPTION")
    private String aspectDescription;

    @GridField(name = "classCode")
    @InforField(xpath = "CLASSID/CLASSCODE")
    private String classCode;

    @GridField(name = "classorg")
    @InforField(xpath = "CLASSID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String classOrgCode;

    @GridField(name = "department")
    @InforField(xpath = "DEPARTMENTID/DEPARTMENTCODE")
    private String department;

    @GridField(name = "equipmentdesc")
    @InforField(xpath = "EQUIPMENTID/DESCRIPTION")
    private String equipmentDesc;

    @GridField(name = "equipment")
    @InforField(xpath = "EQUIPMENTID/EQUIPMENTCODE")
    private String equipmentCode;

    @GridField(name = "equipmentorg")
    @InforField(xpath = "EQUIPMENTID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String equipmentOrg;

    @GridField(name = "equipmentassignedto")
    @InforField(xpath = "EquipmentDetails/ASSIGNEDTO/PERSONCODE")
    private String equipmentAssignedTo;

    @GridField(name = "equipmentcategory")
    @InforField(xpath = "EquipmentDetails/CATEGORYID/CATEGORYCODE")
    private String equipmentCategory;

    @GridField(name = "equipmentcriticality")
    @InforField(xpath = "EquipmentDetails/CRITICALITYID/CRITICALITY")
    private String equipmentCriticality;

    @GridField(name = "equipmentcriticalitydesc")
    @InforField(xpath = "EquipmentDetails/CRITICALITYID/DESCRIPTION")
    private String equipmentCriticalityDesc;

    @GridField(name = "equipmentdepartment")
    @InforField(xpath = "EquipmentDetails/DEPARTMENTID/DEPARTMENTCODE")
    private String equipmentDepartment;

    @GridField(name = "equipmentclass")
    @InforField(xpath = "EquipmentDetails/EQUIPMENTCLASSID/CLASSCODE")
    private String equipmentClassCode;

    @GridField(name = "equipmentclassorg")
    @InforField(xpath = "EquipmentDetails/EQUIPMENTCLASSID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String equipmentClassOrg;

    @GridField(name = "equipmentstatusdesc")
    @InforField(xpath = "EquipmentDetails/EQUIPMENTSTATUS/DESCRIPTION")
    private String equipmentStatusDesc;

    @GridField(name = "equipmentstatus")
    @InforField(xpath = "EquipmentDetails/EQUIPMENTSTATUS/STATUSCODE")
    private String equipmentStatus;

    @GridField(name = "equipmentmanufacturer")
    @InforField(xpath = "EquipmentDetails/MANUFACTURERID/MANUFACTURERCODE")
    private String equipmentManufacturer;

    @GridField(name = "equipmentmodel")
    @InforField(xpath = "EquipmentDetails/MODEL")
    private String equipmentModel;

    @GridField(name = "equipmentoperationalstatusdesc")
    @InforField(xpath = "EquipmentDetails/OPERATIONALSTATUS/DESCRIPTION")
    private String equipmentOperationalStatusDesc;

    @GridField(name = "equipmentoperationalstatus")
    @InforField(xpath = "EquipmentDetails/OPERATIONALSTATUS/STATUSCODE")
    private String equipmentOperationalStatus;

    @GridField(name = "equipmentsafety")
    @InforField(xpath = "EquipmentDetails/SAFETY")
    private Boolean equipmentSafety;

    @GridField(name = "equipmenttypedesc")
    @InforField(xpath = "EquipmentDetails/TYPE/DESCRIPTION")
    private String equipmenTtypeDesc;

    @GridField(name = "equipmenttype")
    @InforField(xpath = "EquipmentDetails/TYPE/TYPECODE")
    private String equipmentType;

    @GridField(name = "highestobservation")
    @InforField(xpath = "HIGHESTOBSERVATIONNUM")
    private BigDecimal highestObservation;

    @GridField(name = "locationdesc")
    @InforField(xpath = "LOCATIONID/DESCRIPTION")
    private String locationDesc;

    @GridField(name = "location")
    @InforField(xpath = "LOCATIONID/LOCATIONCODE")
    private String locationCode;

    @GridField(name = "locationorg")
    @InforField(xpath = "LOCATIONID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String locationOrgCode;

    @GridField(name = "flow")
    @InforField(xpath = "LinearReferenceDetails/FLOWCODE")
    private String flow;

    @GridField(name = "inspectiondirection")
    @InforField(xpath = "LinearReferenceDetails/INSPECTIONDIRECTIONCODE")
    private String inspectionDirection;

    @GridField(name = "fromxcoordinate")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMCOORDINATE/X")
    private BigDecimal fromxCoordinate;

    @GridField(name = "fromycoordinate")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMCOORDINATE/Y")
    private BigDecimal fromyCoordinate;

    @GridField(name = "ncffromgeoref")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMGEOREF")
    private String ncfFromGeoref;

    @GridField(name = "fromhorizontaloffset")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/HORIZONTALOFFSET")
    private BigDecimal fromHorizontalOffset;

    @GridField(name = "fromhoroffsettype")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/HORIZONTALOFFSETTYPEID/TYPECODE")
    private String fromoroffsettype;

    @GridField(name = "fromhoroffsetuom")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/HORIZONTALOFFSETUOM/LINEARREFUOM")
    private String fromhoroffsetuom;

    @GridField(name = "fromrelationshiptype")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/RELATIONSHIPTYPEID/TYPECODE")
    private String fromrelationshiptype;

    @GridField(name = "fromverticaloffset")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/VERTICALOFFSET")
    private BigDecimal fromverticaloffset;

    @GridField(name = "fromverticaloffsettype")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/VERTICALOFFSETTYPEID/TYPECODE")
    private String fromverticaloffsettype;

    @GridField(name = "fromverticaloffsetuom")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMHORIZONTALVERTICALDETAILS/VERTICALOFFSETUOM/LINEARREFUOM")
    private String fromverticaloffsetuom;

    @GridField(name = "fromlatitude")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMLATITUDE")
    private BigDecimal fromlatitude;

    @GridField(name = "fromlongitude")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMLONGITUDE")
    private BigDecimal fromlongitude;

    @GridField(name = "fromoffset")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMOFFSET")
    private BigDecimal fromoffset;

    @GridField(name = "fromoffsetdirection")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMOFFSETDIRECTION/UCODE")
    private String fromoffsetdirection;

    @GridField(name = "fromoffsetpercent")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMOFFSETPERCENTAGE")
    private BigDecimal fromoffsetpercent;

    @GridField(name = "ncffrompoint")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMPOINT")
    private BigDecimal ncffrompoint;

    @GridField(name = "ncffromrefdesc")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMREFDESC")
    private String ncffromrefdesc;

    @GridField(name = "fromreferencepoint")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMREFERENCEID/DESCRIPTION")
    private String fromreferencepoint;

    @GridField(name = "relatedfromreference")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/FROMREFERENCEID/LINEARREFERENCECODE")
    private String relatedfromreference;

    @GridField(name = "relationshiptype")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/RELATIONSHIPTYPEID/TYPECODE")
    private String relationshiptype;

    @GridField(name = "toxcoordinate")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOCOORDINATE/X")
    private BigDecimal toXcoordinate;

    @GridField(name = "toycoordinate")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOCOORDINATE/Y")
    private BigDecimal toYcoordinate;

    @GridField(name = "ncftogeoref")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOGEOREF")
    private String ncfToGeoref;

    @GridField(name = "tohorizontaloffset")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/HORIZONTALOFFSET")
    private BigDecimal toHorizontalOffset;

    @GridField(name = "tohorizontaloffsettype")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/HORIZONTALOFFSETTYPEID/TYPECODE")
    private String toHorizontalOffseTtype;

    @GridField(name = "tohorizontaloffsetuom")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/HORIZONTALOFFSETUOM/LINEARREFUOM")
    private String toHorizontalOffsetUOM;

    @GridField(name = "torelationshiptype")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/RELATIONSHIPTYPEID/TYPECODE")
    private String toRelationshipType;

    @GridField(name = "toverticaloffset")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/VERTICALOFFSET")
    private BigDecimal toVerticalOffset;

    @GridField(name = "toverticaloffsettype")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/VERTICALOFFSETTYPEID/TYPECODE")
    private String toBerticalOffsetType;

    @GridField(name = "toverticaloffsetuom")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOHORIZONTALVERTICALDETAILS/VERTICALOFFSETUOM/LINEARREFUOM")
    private String toVerticalOffsetUOM;

    @GridField(name = "tolatitude")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOLATITUDE")
    private BigDecimal toLatitude;

    @GridField(name = "tolongitude")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOLONGITUDE")
    private BigDecimal toLongitude;

    @GridField(name = "tooffset")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOOFFSET")
    private BigDecimal toOffset;

    @GridField(name = "tooffsetdirection")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOOFFSETDIRECTION/UCODE")
    private String tOoffsetDirection;

    @GridField(name = "tooffsetpercent")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOOFFSETPERCENTAGE")
    private BigDecimal toOffsetPercent;

    @GridField(name = "ncftopoint")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOPOINT")
    private BigDecimal ncfToPoint;

    @GridField(name = "ncftorefdesc")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOREFDESC")
    private String ncfTorefDesc;

    @GridField(name = "toreferencepoint")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOREFERENCEID/DESCRIPTION")
    private String toReferencePoint;

    @GridField(name = "relatedtoreference")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT/TOREFERENCEID/LINEARREFERENCECODE")
    private String relatedToReference;

    @GridField(name = "ncftopointuom")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFUOM")
    private String ncfToPointUOM;

    @GridField(name = "ncffrompointuom")
    @InforField(xpath = "LinearReferenceDetails/LINEARREFUOM")
    private String ncfFromPointUOM;

    @GridField(name = "materialtype")
    @InforField(xpath = "MATERIALTYPE/USERDEFINEDCODE")
    private String materialType;

    @GridField(name = "importance")
    @InforField(xpath = "NONCONFORMITYIMPORTANCE/USERDEFINEDCODE")
    private String importance;

    @GridField(name = "intensity")
    @InforField(xpath = "NONCONFORMITYINTENSITY/USERDEFINEDCODE")
    private String intensity;

    @GridField(name = "nonconformitynote")
    @InforField(xpath = "NONCONFORMITYNOTE")
    private String nonConformityNote;

    @GridField(name = "severity")
    @InforField(xpath = "NONCONFORMITYSEVERITY/USERDEFINEDCODE")
    private String severity;

    @GridField(name = "ncfsize")
    @InforField(xpath = "NONCONFORMITYSIZE")
    private BigDecimal ncfSize;

    @GridField(name = "typedesc")
    @InforField(xpath = "NONCONFORMITYTYPEID/DESCRIPTION")
    private String typeDesc;

    @GridField(name = "type")
    @InforField(xpath = "NONCONFORMITYTYPEID/NONCONFORMITYTYPECODE")
    private String typeCode;

    @GridField(name = "typeorg")
    @InforField(xpath = "NONCONFORMITYTYPEID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String typeOrgCode;

    @GridField(name = "partdesc")
    @InforField(xpath = "PARTID/DESCRIPTION")
    private String partDesc;

    @GridField(name = "partorg")
    @InforField(xpath = "PARTID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String partorg;

    @GridField(name = "part")
    @InforField(xpath = "PARTID/PARTCODE")
    private String partCode;

    @GridField(name = "priority")
    @InforField(xpath = "PRIORITY/PRIORITYCODE")
    private String priority;

    @GridField(name = "status")
    @InforField(xpath = "STATUS/STATUSCODE")
    private String statusCode;

    @GridField(name = "conditionindex")
    @InforField(xpath = "TrackingDetails/CONDITIONINDEX/USERDEFINEDCODE")
    private String conditionIndex;

    @GridField(name = "conditionscore")
    @InforField(xpath = "TrackingDetails/CONDITIONSCORE")
    private BigDecimal conditionScore;

    @GridField(name = "createdby")
    @InforField(xpath = "TrackingDetails/CREATEDBY/USERCODE")
    private String createdBy;

    @GridField(name = "created")
    @InforField(xpath = "TrackingDetails/CREATEDDATE")
    private Date createdDate;

    @Setter
    @GridField(name = "updated")
    @InforField(xpath = "TrackingDetails/DATEUPDATED")
    private Date updatedDate;

    @GridField(name = "ncfestlaborcost")
    @InforField(xpath = "TrackingDetails/ESTIMATEDLABORCOST")
    private BigDecimal ncfestlaborcost;

    @GridField(name = "ncfestmatlcost")
    @InforField(xpath = "TrackingDetails/ESTIMATEDMATERIALCOST")
    private BigDecimal ncfestmatlcost;

    @GridField(name = "ncfestmisccost")
    @InforField(xpath = "TrackingDetails/ESTIMATEDMISCELLANEOUSCOST")
    private BigDecimal ncfestmisccost;

    @GridField(name = "ncftotalestcost")
    @InforField(xpath = "TrackingDetails/ESTIMATEDTOTALCOST")
    private BigDecimal ncftotalestcost;

    @Setter
    @GridField(name = "nextinspectdate")
    @InforField(xpath = "TrackingDetails/NEXTINSPECTIONDATE")
    private Date nextInspectDate;

    @Setter
    @GridField(name = "nextinspectdateoverride")
    @InforField(xpath = "TrackingDetails/NEXTINSPECTIONDATEOVERRIDE")
    private Date nextInspectDateOverride;

    @GridField(name = "mergedinto")
    @InforField(xpath = "TrackingDetails/NONCONFORMITYMERGEINTO")
    private String mergedinto;

    @GridField(name = "observationoverride")
    @InforField(xpath = "TrackingDetails/OVERRIDEOBSERVATION")
    private Boolean observationoverride;

    @GridField(name = "repairdate")
    @InforField(xpath = "TrackingDetails/RECOMMENDEDREPAIRDATE")
    private Date repairDate;

    @GridField(name = "sourceobservation")
    @InforField(xpath = "TrackingDetails/SOURCEOBSERVATIONNUM")
    private BigInteger sourceobservation;

    @GridField(name = "updatedby")
    @InforField(xpath = "TrackingDetails/UPDATEDBY/USERCODE")
    private String updatedBy;

    @GridField(name = "syslevel")
    @InforField(xpath = "VMRSCODE/SYSTEMLEVELID/SYSTEMLEVELCODE")
    private String syslevel;

    @GridField(name = "asslevel")
    @InforField(xpath = "VMRSCODE/VMRSASSEMBLYID/ASSEMBLYLEVELID/ASSEMBLYLEVELCODE")
    private String asslevel;

    @GridField(name = "complevel")
    @InforField(xpath = "VMRSCODE/VMRSCOMPNENTID/COMPONENTLEVELID/COMPONENTLEVELCODE")
    private String complevel;

    @GridField(name = "recordid")
    @InforField(xpath = "recordid")
    private BigInteger recordid;

    @InforField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;

    @Transient
    @InforField(xpath = "USERDEFINEDAREA")
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
