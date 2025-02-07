package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.annotations.GridField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NonConformityObservation implements Serializable {


    // Panel 1
    @GridField(name = "nonconformitypk")
    @EAMField(xpath = "NONCONFORMITYOBSERVATIONID/OBSERVATIONPK")
    private String observationPk;

    @GridField(name = "organization")
    @EAMField(xpath = "NONCONFORMITYOBSERVATIONID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String organizationCode;

    @GridField(name = "description")
    @EAMField(xpath = "NONCONFORMITYID/DESCRIPTION")
    private String description;

    @GridField(name = "nonconformity")
    @EAMField(xpath = {"NONCONFORMITYOBSERVATIONID/NONCONFORMITYCODE", "NONCONFORMITYID/STANDARDENTITYCODE"})
    private String nonConformityCode;

    @GridField(name = "nonconformity")
    @EAMField(xpath = "NONCONFORMITYOBSERVATIONID/NONCONFORMITYCODE")
    private String code;


    //Panel Nonconformity Details
    @GridField(name = "equipment")
    @EAMField(xpath = "EQUIPMENTID/EQUIPMENTCODE")
    private String equipmentCode;

    @GridField(name = "equipmentorg")
    @EAMField(xpath = "EQUIPMENTID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String equipmentOrg;

    @GridField(name = "locationdesc")
    @EAMField(xpath = "LOCATIONID/DESCRIPTION")
    private String locationDesc;

    @GridField(name = "location")
    @EAMField(xpath = "LOCATIONID/LOCATIONCODE")
    private String locationCode;

    @GridField(name = "locationorg")
    @EAMField(xpath = "LOCATIONID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String locationOrgCode;

    @GridField(name = "department")
    @EAMField(xpath = "DEPARTMENTID/DEPARTMENTCODE")
    private String department;

    @GridField(name = "part")
    @EAMField(xpath = "PARTID/PARTCODE")
    private String partCode;

    @GridField(name = "partdesc")
    @EAMField(xpath = "PARTID/DESCRIPTION")
    private String partDesc;

    @GridField(name = "type")
    @EAMField(xpath = "NONCONFORMITYTYPEID/NONCONFORMITYTYPECODE")
    private String typeCode;

    @GridField(name = "typedesc")
    @EAMField(xpath = "NONCONFORMITYTYPEID/DESCRIPTION")
    private String typeDesc;

    @GridField(name = "typeorg")
    @EAMField(xpath = "NONCONFORMITYTYPEID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String typeOrgCode;

    @GridField(name = "classCode")
    @EAMField(xpath = "CLASSID/CLASSCODE")
    private String classCode;

    @GridField(name = "classorg")
    @EAMField(xpath = "CLASSID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String classOrgCode;

    @GridField(name = "materialtype")
    @EAMField(xpath = "MATERIALTYPE/USERDEFINEDCODE")
    private String materialType;

    @GridField(name = "status")
    @EAMField(xpath = "STATUS/STATUSCODE")
    private String statusCode;

    @GridField(name = "priority")
    @EAMField(xpath = "PRIORITY/PRIORITYCODE")
    private String priority;

    @GridField(name = "syslevel")
    @EAMField(xpath = "VMRSCODE/SYSTEMLEVELID/SYSTEMLEVELCODE")
    private String syslevel;

    @GridField(name = "asslevel")
    @EAMField(xpath = "VMRSCODE/VMRSASSEMBLYID/ASSEMBLYLEVELID/ASSEMBLYLEVELCODE")
    private String asslevel;

    @GridField(name = "complevel")
    @EAMField(xpath = "VMRSCODE/VMRSCOMPNENTID/COMPONENTLEVELID/COMPONENTLEVELCODE")
    private String complevel;

    @GridField(name = "aspectcode")
    @EAMField(xpath = "ASPECTID/ASPECTCODE")
    private String aspectCode;

    @GridField(name = "aspectdescription")
    @EAMField(xpath = "ASPECTID/DESCRIPTION")
    private String aspectDescription;

    @GridField(name = "nonconformitynote")
    @EAMField(xpath = "NONCONFORMITYNOTE")
    private String nonConformityNote;

    //Panel observation details
    @GridField(name = "observation")
    @EAMField(xpath = "NONCONFORMITYOBSERVATIONID/OBSERVATIONNUM")
    private String observationNum;

    @GridField(name = "rstatus")
    @EAMField(xpath = "OBSERVATIONSTATUS/STATUSCODE")
    private String observationStatusCode;

    @GridField(name = "taskplanchlist")
    @EAMField(xpath = "TASKLISTID/TASKCODE")
    private String taskCode;

    @GridField(name = "taskplanchlistrev\n")
    @EAMField(xpath = "TASKLISTID/TASKREVISION")
    private String taskRevision;

    @GridField(name = "note")
    @EAMField(xpath = "NOTE")
    private String note;

    @GridField(name = "recordedby")
    @EAMField(xpath = "RECORDEDBY/EMPLOYEECODE")
    private String employeeCode;

    @GridField(name = "created")
    @EAMField(xpath = "RECORDEDDATE")
    private String recoredDate;

    @GridField(name = "severity_display")
    @EAMField(xpath = "OBSERVATIONSEVERITY/USERDEFINEDCODE")
    private String severity;

    @GridField(name = "intensity_display")
    @EAMField(xpath = "OBSERVATIONINTENSITY/USERDEFINEDCODE")
    private String intensity;

    @GridField(name = "ncfsize")
    @EAMField(xpath = "OBSERVATIONSIZE")
    private String size;

    @GridField(name = "importance")
    @EAMField(xpath = "OBSERVATIONIMPORTANCE/USERDEFINEDCODE")
    private String importance;

    @GridField(name = "workordernum")
    @EAMField(xpath = {"WORKORDERID/JOBNUM", "JOBNUM"})
    private String jobNum;

    @GridField(name = "nonconformitybeforemerge")
    @EAMField(xpath = "NONCONFORMITYCODEBEFOREMERGE")
    private String beforeMerge;

    @GridField(name = "massacknowledgeddesc")
    @EAMField(xpath = "MASSACKNOWLEDGED/DESCRIPTION")
    private String acknowledgedDescription;

    @GridField(name = "acknowledgedby")
    @EAMField(xpath = "ACKNOWLEDGEDBY/USERCODE")
    private String acknowledgedBy;

    @GridField(name = "dateacknowledged")
    @EAMField(xpath = "DATEACKNOWLEDGED")
    private String acknowledgedDate;

    @GridField(name = "mobiledateacknowledged")
    @EAMField(xpath = "MOBILEDATEACKNOWLEDGED")
    private String mobileDateAcknowledged;

    @GridField(name = "acknowledgedcopiedfromdes")
    @EAMField(xpath = "ACKNOWLEDGEDCOPIEDFROM/DESCRIPTION")
    private String acknowledgedCopiedFrom;

    @GridField(name = "acknowledgedsourceobservseq")
    @EAMField(xpath = "ACKNOWLEDGEDSOURCEOBSERVATION/OBSERVATIONNUM")
    private String acknowledgedSourceObservation;

    @GridField(name = "conditionscore")
    @EAMField(xpath = "TrackingDetails/CONDITIONSCORE")
    private BigDecimal conditionScore;

    @GridField(name = "conditionindex")
    @EAMField(xpath = "TrackingDetails/CONDITIONINDEX/DESCRIPTION")
    private String conditionDescription;

    @GridField(name = "nextinspectdate")
    @EAMField(xpath = "TrackingDetails/NEXTINSPECTIONDATE")
    private Date nextInspectDate;

    @Setter
    @GridField(name = "nextinspectdateoverride")
    @EAMField(xpath = "TrackingDetails/NEXTINSPECTIONDATEOVERRIDE")
    private Date nextInspectDateOverride;

    @GridField(name = "repairdate")
    @EAMField(xpath = "TrackingDetails/RECOMMENDEDREPAIRDATE")
    private Date repairDate;

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

    @GridField(name = "createdby")
    @EAMField(xpath = "TrackingDetails/CREATEDBY/USERCODE")
    private String createdBy;

    @GridField(name = "created")
    @EAMField(xpath = "TrackingDetails/CREATEDDATE")
    private Date createdDate;

    @GridField(name = "updatedby")
    @EAMField(xpath = "TrackingDetails/UPDATEDBY/USERCODE")
    private String updatedBy;

    @Setter
    @GridField(name = "updated")
    @EAMField(xpath = "TrackingDetails/DATEUPDATED")
    private Date updatedDate;

    //Custom fields panel
    @Transient
    @EAMField(xpath = "USERDEFINEDAREA")
    private CustomField[] customFields;

}

