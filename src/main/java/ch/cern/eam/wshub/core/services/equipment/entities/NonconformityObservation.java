package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.GridField;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NonconformityObservation implements Serializable {


    // Panel 1
    @GridField(name = "nonconformitypk")
    @InforField(xpath = "NONCONFORMITYOBSERVATIONID/OBSERVATIONPK")
    private String observationPk;

    @GridField(name = "organization")
    @InforField(xpath = "NONCONFORMITYOBSERVATIONID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String organizationCode;

    @GridField(name = "description")
    @InforField(xpath = "NONCONFORMITYID/DESCRIPTION")
    private String description;

    @GridField(name = "nonconformity")
    @InforField(xpath = {"NONCONFORMITYOBSERVATIONID/NONCONFORMITYCODE", "NONCONFORMITYID/STANDARDENTITYCODE"})
    private String nonConformityCode;

    @GridField(name = "nonconformity")
    @InforField(xpath = "NONCONFORMITYOBSERVATIONID/NONCONFORMITYCODE")
    private String code;


    //Panel Nonconformity Details
    @GridField(name = "equipment")
    @InforField(xpath = "EQUIPMENTID/EQUIPMENTCODE")
    private String equipmentCode;

    @GridField(name = "equipmentorg")
    @InforField(xpath = "EQUIPMENTID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String equipmentOrg;

    @GridField(name = "locationdesc")
    @InforField(xpath = "LOCATIONID/DESCRIPTION")
    private String locationDesc;

    @GridField(name = "location")
    @InforField(xpath = "LOCATIONID/LOCATIONCODE")
    private String locationCode;

    @GridField(name = "locationorg")
    @InforField(xpath = "LOCATIONID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String locationOrgCode;

    @GridField(name = "department")
    @InforField(xpath = "DEPARTMENTID/DEPARTMENTCODE")
    private String department;

    @GridField(name = "part")
    @InforField(xpath = "PARTID/PARTCODE")
    private String partCode;

    @GridField(name = "partdesc")
    @InforField(xpath = "PARTID/DESCRIPTION")
    private String partDesc;

    @GridField(name = "type")
    @InforField(xpath = "NONCONFORMITYTYPEID/NONCONFORMITYTYPECODE")
    private String typeCode;

    @GridField(name = "typedesc")
    @InforField(xpath = "NONCONFORMITYTYPEID/DESCRIPTION")
    private String typeDesc;

    @GridField(name = "typeorg")
    @InforField(xpath = "NONCONFORMITYTYPEID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String typeOrgCode;

    @GridField(name = "classCode")
    @InforField(xpath = "CLASSID/CLASSCODE")
    private String classCode;

    @GridField(name = "classorg")
    @InforField(xpath = "CLASSID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String classOrgCode;

    @GridField(name = "materialtype")
    @InforField(xpath = "MATERIALTYPE/USERDEFINEDCODE")
    private String materialType;

    @GridField(name = "status")
    @InforField(xpath = "STATUS/STATUSCODE")
    private String statusCode;

    @GridField(name = "priority")
    @InforField(xpath = "PRIORITY/PRIORITYCODE")
    private String priority;

    @GridField(name = "syslevel")
    @InforField(xpath = "VMRSCODE/SYSTEMLEVELID/SYSTEMLEVELCODE")
    private String syslevel;

    @GridField(name = "asslevel")
    @InforField(xpath = "VMRSCODE/VMRSASSEMBLYID/ASSEMBLYLEVELID/ASSEMBLYLEVELCODE")
    private String asslevel;

    @GridField(name = "complevel")
    @InforField(xpath = "VMRSCODE/VMRSCOMPNENTID/COMPONENTLEVELID/COMPONENTLEVELCODE")
    private String complevel;

    @GridField(name = "aspectcode")
    @InforField(xpath = "ASPECTID/ASPECTCODE")
    private String aspectCode;

    @GridField(name = "aspectdescription")
    @InforField(xpath = "ASPECTID/DESCRIPTION")
    private String aspectDescription;

    @GridField(name = "nonconformitynote")
    @InforField(xpath = "NONCONFORMITYNOTE")
    private String nonConformityNote;

    //Panel observation details
    @GridField(name = "observation")
    @InforField(xpath = "NONCONFORMITYOBSERVATIONID/OBSERVATIONNUM")
    private String observationNum;

    @GridField(name = "rstatus")
    @InforField(xpath = "OBSERVATIONSTATUS/STATUSCODE")
    private String observationStatusCode;

    @GridField(name = "taskplanchlist")
    @InforField(xpath = "TASKLISTID/TASKCODE")
    private String taskCode;

    @GridField(name = "taskplanchlistrev\n")
    @InforField(xpath = "TASKLISTID/TASKREVISION")
    private String taskRevision;

    @GridField(name = "note")
    @InforField(xpath = "NOTE")
    private String note;

    @GridField(name = "recordedby")
    @InforField(xpath = "RECORDEDBY/EMPLOYEECODE")
    private String employeeCode;

    @GridField(name = "created")
    @InforField(xpath = "RECORDEDDATE")
    private String recoredDate;

    @GridField(name = "severity_display")
    @InforField(xpath = "OBSERVATIONSEVERITY/USERDEFINEDCODE")
    private String severity;

    @GridField(name = "intensity_display")
    @InforField(xpath = "OBSERVATIONINTENSITY/USERDEFINEDCODE")
    private String intensity;

    @GridField(name = "ncfsize")
    @InforField(xpath = "OBSERVATIONSIZE")
    private String size;

    @GridField(name = "importance")
    @InforField(xpath = "OBSERVATIONIMPORTANCE/USERDEFINEDCODE")
    private String importance;

    @GridField(name = "workordernum")
    @InforField(xpath = {"WORKORDERID/JOBNUM", "JOBNUM"})
    private String jobNum;

    @GridField(name = "nonconformitybeforemerge")
    @InforField(xpath = "NONCONFORMITYCODEBEFOREMERGE")
    private String beforeMerge;

    @GridField(name = "massacknowledgeddesc")
    @InforField(xpath = "MASSACKNOWLEDGED/DESCRIPTION")
    private String acknowledgedDescription;

    @GridField(name = "acknowledgedby")
    @InforField(xpath = "ACKNOWLEDGEDBY/USERCODE")
    private String acknowledgedBy;

    @GridField(name = "dateacknowledged")
    @InforField(xpath = "DATEACKNOWLEDGED")
    private String acknowledgedDate;

    @GridField(name = "mobiledateacknowledged")
    @InforField(xpath = "MOBILEDATEACKNOWLEDGED")
    private String mobileDateAcknowledged;

    @GridField(name = "acknowledgedcopiedfromdes")
    @InforField(xpath = "ACKNOWLEDGEDCOPIEDFROM/DESCRIPTION")
    private String acknowledgedCopiedFrom;

    @GridField(name = "acknowledgedsourceobservseq")
    @InforField(xpath = "ACKNOWLEDGEDSOURCEOBSERVATION/OBSERVATIONNUM")
    private String acknowledgedSourceObservation;

    @GridField(name = "conditionscore")
    @InforField(xpath = "TrackingDetails/CONDITIONSCORE")
    private BigDecimal conditionScore;

    @GridField(name = "conditionindex")
    @InforField(xpath = "TrackingDetails/CONDITIONINDEX/DESCRIPTION")
    private String conditionDescription;

    @GridField(name = "nextinspectdate")
    @InforField(xpath = "TrackingDetails/NEXTINSPECTIONDATE")
    private Date nextInspectDate;

    @Setter
    @GridField(name = "nextinspectdateoverride")
    @InforField(xpath = "TrackingDetails/NEXTINSPECTIONDATEOVERRIDE")
    private Date nextInspectDateOverride;

    @GridField(name = "repairdate")
    @InforField(xpath = "TrackingDetails/RECOMMENDEDREPAIRDATE")
    private Date repairDate;

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

    @GridField(name = "createdby")
    @InforField(xpath = "TrackingDetails/CREATEDBY/USERCODE")
    private String createdBy;

    @GridField(name = "created")
    @InforField(xpath = "TrackingDetails/CREATEDDATE")
    private Date createdDate;

    @GridField(name = "updatedby")
    @InforField(xpath = "TrackingDetails/UPDATEDBY/USERCODE")
    private String updatedBy;

    @Setter
    @GridField(name = "updated")
    @InforField(xpath = "TrackingDetails/DATEUPDATED")
    private Date updatedDate;

    //Custom fields panel
    @Transient
    @InforField(xpath = "USERDEFINEDAREA")
    private CustomField[] customFields;

    public void setCustomFields(CustomField[] customFields) {
        this.customFields = customFields;
    }


}
