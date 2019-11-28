package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

@Entity
@NamedNativeQuery(name = InforCaseTask.GET_TASKS_FOR_CASE,
				  query = "select TCM_CHKLST_TASK,\n" + 
				  		"TCM_ESTIMATEDCOST,\n" + 
				  		"TCM_CALCULATEDCOST,\n" + 
				  		"TCM_COSTREFRESHREQ,\n" + 
				  		"TCM_FROMSERVICECODE,\n" + 
				  		"TCM_UDFCHAR01 as UDFCHAR01,\n" + 
				  		"TCM_UDFCHAR02 as UDFCHAR02,\n" + 
				  		"TCM_UDFCHAR03 as UDFCHAR03,\n" + 
				  		"TCM_UDFCHAR04 as UDFCHAR04,\n" + 
				  		"TCM_UDFCHAR05 as UDFCHAR05,\n" + 
				  		"TCM_UDFCHAR06 as UDFCHAR06,\n" + 
				  		"TCM_UDFCHAR07 as UDFCHAR07,\n" + 
				  		"TCM_UDFCHAR08 as UDFCHAR08,\n" + 
				  		"TCM_UDFCHAR09 as UDFCHAR09,\n" + 
				  		"TCM_UDFCHAR10 as UDFCHAR10,\n" + 
				  		"TCM_UDFCHAR11 as UDFCHAR11,\n" + 
				  		"TCM_UDFCHAR12 as UDFCHAR12,\n" + 
				  		"TCM_UDFCHAR13 as UDFCHAR13,\n" + 
				  		"TCM_UDFCHAR14 as UDFCHAR14,\n" + 
				  		"TCM_UDFCHAR15 as UDFCHAR15,\n" + 
				  		"TCM_UDFCHAR16 as UDFCHAR16,\n" + 
				  		"TCM_UDFCHAR17 as UDFCHAR17,\n" + 
				  		"TCM_UDFCHAR18 as UDFCHAR18,\n" + 
				  		"TCM_UDFCHAR19 as UDFCHAR19,\n" + 
				  		"TCM_UDFCHAR20 as UDFCHAR20,\n" + 
				  		"TCM_UDFCHAR21 as UDFCHAR21,\n" + 
				  		"TCM_UDFCHAR22 as UDFCHAR22,\n" + 
				  		"TCM_UDFCHAR23 as UDFCHAR23,\n" + 
				  		"TCM_UDFCHAR24 as UDFCHAR24,\n" + 
				  		"TCM_UDFCHAR25 as UDFCHAR25,\n" + 
				  		"TCM_UDFCHAR26 as UDFCHAR26,\n" + 
				  		"TCM_UDFCHAR27 as UDFCHAR27,\n" + 
				  		"TCM_UDFCHAR28 as UDFCHAR28,\n" + 
				  		"TCM_UDFCHAR29 as UDFCHAR29,\n" + 
				  		"TCM_UDFCHAR30 as UDFCHAR30,\n" + 
				  		"NULL as UDFCHAR31,\n" + 
				  		"NULL as UDFCHAR32,\n" + 
				  		"NULL as UDFCHAR33,\n" + 
				  		"NULL as UDFCHAR34,\n" + 
				  		"NULL as UDFCHAR35,\n" + 
				  		"NULL as UDFCHAR36,\n" + 
				  		"NULL as UDFCHAR37,\n" + 
				  		"NULL as UDFCHAR38,\n" + 
				  		"NULL as UDFCHAR39,\n" + 
				  		"NULL as UDFCHAR40,\n" + 
				  		"NULL as UDFCHAR41,\n" + 
				  		"NULL as UDFCHAR42,\n" + 
				  		"NULL as UDFCHAR43,\n" + 
				  		"NULL as UDFCHAR44,\n" + 
				  		"NULL as UDFCHAR45,\n" + 
				  		"TCM_UDFNUM01 as UDFNUM01,\n" + 
				  		"TCM_UDFNUM02 as UDFNUM02,\n" + 
				  		"TCM_UDFNUM03 as UDFNUM03,\n" + 
				  		"TCM_UDFNUM04 as UDFNUM04,\n" + 
				  		"TCM_UDFNUM05 as UDFNUM05,\n" + 
				  		"NULL as UDFNUM06,\n" + 
				  		"NULL as UDFNUM07,\n" + 
				  		"NULL as UDFNUM08,\n" + 
				  		"NULL as UDFNUM09,\n" + 
				  		"NULL as UDFNUM10,\n" + 
				  		"TCM_UDFDATE01 as UDFDATE01,\n" + 
				  		"TCM_UDFDATE02 as UDFDATE02,\n" + 
				  		"TCM_UDFDATE03 as UDFDATE03,\n" + 
				  		"TCM_UDFDATE04 as UDFDATE04,\n" + 
				  		"TCM_UDFDATE05 as UDFDATE05,\n" + 
				  		"NULL as UDFDATE06,\n" + 
				  		"NULL as UDFDATE07,\n" + 
				  		"NULL as UDFDATE08,\n" + 
				  		"NULL as UDFDATE09,\n" + 
				  		"NULL as UDFDATE10,\n" + 
				  		"TCM_UDFCHKBOX01 as UDFCHKBOX01,\n" + 
				  		"TCM_UDFCHKBOX02 as UDFCHKBOX02,\n" + 
				  		"TCM_UDFCHKBOX03 as UDFCHKBOX03,\n" + 
				  		"TCM_UDFCHKBOX04 as UDFCHKBOX04,\n" + 
				  		"TCM_UDFCHKBOX05 as UDFCHKBOX05,\n" + 
				  		"NULL as UDFCHKBOX06,\n" + 
				  		"NULL as UDFCHKBOX07,\n" + 
				  		"NULL as UDFCHKBOX08,\n" + 
				  		"NULL as UDFCHKBOX09,\n" + 
				  		"NULL as UDFCHKBOX10,\n" + 
				  		"TCM_CODE,\n" + 
				  		"TCM_DESC,\n" + 
				  		"TCM_CASEMANAGEMENT,\n" + 
				  		"TCM_SEQUENCE,\n" + 
				  		"TCM_STEP,\n" + 
				  		"TCM_ASSIGNEDTO,\n" +
				  		"TCM_ASSIGNEDTO_NAME,\n" +
				  		"TCM_ASSIGNEDTO_EMAIL,\n" +
				  		"TCM_STARTDATE,\n" +
				  		"TCM_DATECOMPLETED,\n" +
				  		"TCM_SCHD_STARTDATE,\n" +
				  		"TCM_SCHD_ENDDATE,\n" +
				  		"TCM_REQ_STARTDATE,\n" +
				  		"TCM_REQ_ENDDATE,\n" +
				  		"TCM_PLANNEDDURATION,\n" +
				  		"TCM_DURATION_UNIT,\n" +
				  		"TCM_PRIORITY from R5CASEMANAGEMENTTASKS where TCM_CASEMANAGEMENT = :caseID",
				  resultClass=InforCaseTask.class)
@Table(name = "R5CASEMANAGEMENTTASKS")
public class InforCaseTask implements Serializable {

	private static final long serialVersionUID = -6048773043890563354L;
	
	public static final String GET_TASKS_FOR_CASE = "InforCaseTask.GET_TASKS_FOR_CASE";

	@Id
	@Column(name = "TCM_CODE")
	private String taskCode;
	/**Link to a case**/
	@Column(name = "TCM_CASEMANAGEMENT")
	private String caseCode;
	@Column(name = "TCM_DESC")
	private String description;
	@Column(name = "TCM_SEQUENCE")
	private Long sequence;
	@Column(name = "TCM_STEP")
	private Long step;
	@Column(name = "TCM_CHKLST_TASK")
	private String taskPlanForChecklist;
	@Column(name = "TCM_ESTIMATEDCOST")
	private BigDecimal estimatedCosts;
	@Transient
	private String estimatedCurr;
	@Column(name = "TCM_CALCULATEDCOST")
	private String calculatedCosts;
	@Transient
	private String calculatedCostsCurr;
	@Column(name = "TCM_COSTREFRESHREQ")
	private String refreshCalculatedCosts;
	@Column(name = "TCM_PRIORITY")
	private String priority;
	@Column(name = "TCM_FROMSERVICECODE")
	private String fromServiceCode;
	
	// CREATION/UPDATE INFO
	@Transient
	private String createdBy;
	@Transient
	private Date dateCreated;
	@Transient
	private String updatedBy;
	@Transient
	private Date dateUpdated;
	// ASSIGNED TO
	@Column(name = "TCM_ASSIGNEDTO")
	private String assignedTo;
	@Column(name = "TCM_ASSIGNEDTO_NAME")
	private String assignedToDesc;
	@Column(name = "TCM_ASSIGNEDTO_EMAIL")
	private String assignedToEMail;
	// SCHEDULING
	@Column(name = "TCM_STARTDATE")
	private Date startDate;
	@Column(name = "TCM_DATECOMPLETED")
	private Date completedDate;
	@Column(name = "TCM_SCHD_STARTDATE")
	private Date scheduledStartDate;
	@Column(name = "TCM_SCHD_ENDDATE")
	private Date scheduledEndDate;
	@Column(name = "TCM_PLANNEDDURATION")
	private BigDecimal plannedDuration;
	@Column(name = "TCM_DURATION_UNIT")
	private String plannedDurationUnit;
	@Column(name = "TCM_REQ_STARTDATE")
	private Date requestedStartDate;
	@Column(name = "TCM_REQ_ENDDATE")
	private Date requestedEndDate;
	// USER DEFINED DATA
	@Transient
	private CustomField[] customFields;
	@Embedded
	private UserDefinedFields userDefinedFields;

	public String getCaseCode() {
		return caseCode;
	}
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getSequence() {
		return sequence;
	}
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	@XmlElementWrapper(name="customFields")
	@XmlElement(name="customField")
	public CustomField[] getCustomFields() {
		return customFields;
	}
	public void setCustomFields(CustomField[] customFields) {
		this.customFields = customFields;
	}
	public Long getStep() {
		return step;
	}
	public void setStep(Long step) {
		this.step = step;
	}
	public String getTaskPlanForChecklist() {
		return taskPlanForChecklist;
	}
	public void setTaskPlanForChecklist(String taskPlanForChecklist) {
		this.taskPlanForChecklist = taskPlanForChecklist;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getEstimatedCosts() {
		return estimatedCosts;
	}
	public void setEstimatedCosts(BigDecimal estimatedCosts) {
		this.estimatedCosts = estimatedCosts;
	}
	public String getEstimatedCurr() {
		return estimatedCurr;
	}
	public void setEstimatedCurr(String estimatedCurr) {
		this.estimatedCurr = estimatedCurr;
	}
	public String getCalculatedCosts() {
		return calculatedCosts;
	}
	public void setCalculatedCosts(String calculatedCosts) {
		this.calculatedCosts = calculatedCosts;
	}
	public String getCalculatedCostsCurr() {
		return calculatedCostsCurr;
	}
	public void setCalculatedCostsCurr(String calculatedCostsCurr) {
		this.calculatedCostsCurr = calculatedCostsCurr;
	}
	public String getRefreshCalculatedCosts() {
		return refreshCalculatedCosts;
	}
	public void setRefreshCalculatedCosts(String refreshCalculatedCosts) {
		this.refreshCalculatedCosts = refreshCalculatedCosts;
	}
	public String getFromServiceCode() {
		return fromServiceCode;
	}
	public void setFromServiceCode(String fromServiceCode) {
		this.fromServiceCode = fromServiceCode;
	}
	
	public UserDefinedFields getUserDefinedFields() {
		return userDefinedFields;
	}
	public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Date getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getScheduledStartDate() {
		return scheduledStartDate;
	}
	public void setScheduledStartDate(Date scheduledStartDate) {
		this.scheduledStartDate = scheduledStartDate;
	}
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getScheduledEndDate() {
		return scheduledEndDate;
	}
	public void setScheduledEndDate(Date scheduledEndDate) {
		this.scheduledEndDate = scheduledEndDate;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getPlannedDuration() {
		return plannedDuration;
	}
	public void setPlannedDuration(BigDecimal plannedDuration) {
		this.plannedDuration = plannedDuration;
	}
	public String getPlannedDurationUnit() {
		return plannedDurationUnit;
	}
	public void setPlannedDurationUnit(String plannedDurationUnit) {
		this.plannedDurationUnit = plannedDurationUnit;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getAssignedToDesc() {
		return assignedToDesc;
	}
	public void setAssignedToDesc(String assignedToDesc) {
		this.assignedToDesc = assignedToDesc;
	}
	public String getAssignedToEMail() {
		return assignedToEMail;
	}
	public void setAssignedToEMail(String assignedToEMail) {
		this.assignedToEMail = assignedToEMail;
	}
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getRequestedStartDate() {
		return requestedStartDate;
	}
	public void setRequestedStartDate(Date requestedStartDate) {
		this.requestedStartDate = requestedStartDate;
	}
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getRequestedEndDate() {
		return requestedEndDate;
	}
	public void setRequestedEndDate(Date requestedEndDate) {
		this.requestedEndDate = requestedEndDate;
	}
	@Override
	public String toString() {
		return "InforCaseTask [" + (taskCode != null ? "taskCode=" + taskCode + ", " : "")
				+ (caseCode != null ? "caseCode=" + caseCode + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (sequence != null ? "sequence=" + sequence + ", " : "") + (step != null ? "step=" + step + ", " : "")
				+ (taskPlanForChecklist != null ? "taskPlanForChecklist=" + taskPlanForChecklist + ", " : "")
				+ (estimatedCosts != null ? "estimatedCosts=" + estimatedCosts + ", " : "")
				+ (estimatedCurr != null ? "estimatedCurr=" + estimatedCurr + ", " : "")
				+ (calculatedCosts != null ? "calculatedCosts=" + calculatedCosts + ", " : "")
				+ (calculatedCostsCurr != null ? "calculatedCostsCurr=" + calculatedCostsCurr + ", " : "")
				+ (refreshCalculatedCosts != null ? "refreshCalculatedCosts=" + refreshCalculatedCosts + ", " : "")
				+ (priority != null ? "priority=" + priority + ", " : "")
				+ (fromServiceCode != null ? "fromServiceCode=" + fromServiceCode + ", " : "")
				+ (createdBy != null ? "createdBy=" + createdBy + ", " : "")
				+ (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "")
				+ (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "")
				+ (dateUpdated != null ? "dateUpdated=" + dateUpdated + ", " : "")
				+ (assignedTo != null ? "assignedTo=" + assignedTo + ", " : "")
				+ (assignedToDesc != null ? "assignedToDesc=" + assignedToDesc + ", " : "")
				+ (assignedToEMail != null ? "assignedToEMail=" + assignedToEMail + ", " : "")
				+ (startDate != null ? "startDate=" + startDate + ", " : "")
				+ (completedDate != null ? "completedDate=" + completedDate + ", " : "")
				+ (scheduledStartDate != null ? "scheduledStartDate=" + scheduledStartDate + ", " : "")
				+ (scheduledEndDate != null ? "scheduledEndDate=" + scheduledEndDate + ", " : "")
				+ (plannedDuration != null ? "plannedDuration=" + plannedDuration + ", " : "")
				+ (plannedDurationUnit != null ? "plannedDurationUnit=" + plannedDurationUnit + ", " : "")
				+ (requestedStartDate != null ? "requestedStartDate=" + requestedStartDate + ", " : "")
				+ (requestedEndDate != null ? "requestedEndDate=" + requestedEndDate + ", " : "")
				+ (customFields != null ? "customFields=" + Arrays.toString(customFields) + ", " : "")
				+ (userDefinedFields != null ? "userDefinedFields=" + userDefinedFields : "") + "]";
	}

}
