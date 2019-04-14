package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;

@XmlAccessorType(XmlAccessType.PROPERTY)
//@Dependent
@Entity
@Table(name = "R5EVENTS")
@NamedQuery(name = WorkOrder.FETCH_VSCWO, query = "SELECT wo FROM WorkOrder wo WHERE wo.equipmentCode = 'V$-DAILY-MONITORING' AND wo.departmentCode = 'V01' AND wo.statusCode = 'R' AND wo.origWO IS NULL")
public class WorkOrder implements Serializable {

	public static final String FETCH_VSCWO = "WorkOrder.FETCH_VSCWO";
	/**
	 * 
	 */
	private static final long serialVersionUID = -2035481937868647264L;
	@Column(name = "EVT_CODE")
	@Id
	private String number;
	@Column(name = "EVT_DESC")
	private String description;
	@Transient
	private String classCode;
	@Transient
	private String classDesc;
	@Transient
	private String updateCount;
	@Transient
	private String eventType;
	//
	@Column(name = "EVT_STATUS")
	private String statusCode;
	@Transient
	private String statusDesc;
	//
	@Transient
	private String typeCode;
	@Transient
	private String typeDesc;
	//
	@Column(name = "EVT_MRC")
	private String departmentCode;
	@Transient
	private String departmentDesc;
	//
	@Column(name = "EVT_OBJECT")
	private String equipmentCode;
	@Transient
	private String equipmentDesc;
	//
	@Transient
	private String projectCode;
	@Transient
	private String projectDesc;
	@Transient
	private String projectBudget;
	//
	@Transient
	private String locationCode;
	@Transient
	private String locationDesc;
	//
	@Transient
	private String priorityCode;
	@Transient
	private String priorityDesc;
	//
	@Transient
	private CustomField[] customFields;
	//
	@Transient
	private Date reportedDate;
	@Transient
	private Date requestedEndDate;
	@Transient
	private Date requestedStartDate;
	@Transient
	private Date scheduledStartDate; // target date
	@Transient
	private Date scheduledEndDate;
	@Transient
	private Date startDate;
	@Transient
	private Date completedDate;
	@Transient
	private Date dueDate;
	@Transient
	private Date createdDate;
	@Column(name = "EVT_CREATEDBY")
	private String createdBy;

	@Transient
	private String problemCode;
	@Transient
	private String failureCode;
	@Transient
	private String causeCode;
	@Transient
	private String actionCode;
	@Transient
	private String costCode;
	@Transient
	private String costCodeDesc;

	@Transient
	private String assignedBy; // schedgroup
	@Transient
	private String reportedBy; // requested by
	@Transient
	private String reportedByDesc;
	@Transient
	private String assignedTo;
	@Transient
	private String assignedToDesc;
	@Transient
	private String standardWO;
	@Transient
	private String parentWO;
	@Transient
	private String parentWODesc;
	@Transient
	private String route;
	@Transient
	private String comment;
	@Transient
	private String targetValue;
	@Transient
	private UserDefinedFields userDefinedFields;

	@Column(name = "EVT_ORIGWO")
	private String origWO;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public String getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(String updateCount) {
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

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
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

	public String getEquipmentDesc() {
		return equipmentDesc;
	}

	public void setEquipmentDesc(String equipmentDesc) {
		this.equipmentDesc = equipmentDesc;
	}

	public String getPriorityCode() {
		return priorityCode;
	}

	public void setPriorityCode(String priorityCode) {
		this.priorityCode = priorityCode;
	}

	public String getPriorityDesc() {
		return priorityDesc;
	}

	public void setPriorityDesc(String priorityDesc) {
		this.priorityDesc = priorityDesc;
	}

	@XmlElementWrapper(name = "customFields")
	@XmlElement(name = "customField")
	public CustomField[] getCustomFields() {
		return customFields;
	}

	public void setCustomFields(CustomField[] customFields) {
		this.customFields = customFields;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getReportedDate() {
		return reportedDate;
	}

	public void setReportedDate(Date reportedDate) {
		this.reportedDate = reportedDate;
	}
	@XmlJavaTypeAdapter(DateAdapter.class)

	public Date getRequestedEndDate() {
		return requestedEndDate;
	}

	public void setRequestedEndDate(Date requestedEndDate) {
		this.requestedEndDate = requestedEndDate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getRequestedStartDate() {
		return requestedStartDate;
	}

	public void setRequestedStartDate(Date requestedStartDate) {
		this.requestedStartDate = requestedStartDate;
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

	@XmlJavaTypeAdapter(DateAdapter.class)

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public String getProblemCode() {
		return problemCode;
	}

	public void setProblemCode(String problemCode) {
		this.problemCode = problemCode;
	}

	public String getFailureCode() {
		return failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	public String getCauseCode() {
		return causeCode;
	}

	public void setCauseCode(String causeCode) {
		this.causeCode = causeCode;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(String reportedBy) {
		this.reportedBy = reportedBy;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getStandardWO() {
		return standardWO;
	}

	public void setStandardWO(String standardWO) {
		this.standardWO = standardWO;
	}

	public String getParentWO() {
		return parentWO;
	}

	public void setParentWO(String parentWO) {
		this.parentWO = parentWO;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocationDesc() {
		return locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	public String getCostCode() {
		return costCode;
	}

	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public UserDefinedFields getUserDefinedFields() {
		return userDefinedFields;
	}

	public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectDesc() {
		return projectDesc;
	}

	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getOrigWO() {
		return origWO;
	}

	public void setOrigWO(String origWO) {
		this.origWO = origWO;
	}

	public String getProjectBudget() {
		return projectBudget;
	}

	public void setProjectBudget(String projectBudget) {
		this.projectBudget = projectBudget;
	}

	public String getReportedByDesc() {
		return reportedByDesc;
	}

	public void setReportedByDesc(String reportedByDesc) {
		this.reportedByDesc = reportedByDesc;
	}

	public String getAssignedToDesc() {
		return assignedToDesc;
	}

	public void setAssignedToDesc(String assignedToDesc) {
		this.assignedToDesc = assignedToDesc;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getCostCodeDesc() {
		return costCodeDesc;
	}

	public void setCostCodeDesc(String costCodeDesc) {
		this.costCodeDesc = costCodeDesc;
	}

	public String getParentWODesc() {
		return parentWODesc;
	}

	public void setParentWODesc(String parentWODesc) {
		this.parentWODesc = parentWODesc;
	}

	public String getTargetValue() { return targetValue; }

	public void setTargetValue(String targetValue) { this.targetValue = targetValue; }

	@Override
	public String toString() {
		return "WorkOrder [" + (number != null ? "number=" + number + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (classCode != null ? "classCode=" + classCode + ", " : "")
				+ (classDesc != null ? "classDesc=" + classDesc + ", " : "")
				+ (updateCount != null ? "updateCount=" + updateCount + ", " : "")
				+ (eventType != null ? "eventType=" + eventType + ", " : "")
				+ (statusCode != null ? "statusCode=" + statusCode + ", " : "")
				+ (statusDesc != null ? "statusDesc=" + statusDesc + ", " : "")
				+ (typeCode != null ? "typeCode=" + typeCode + ", " : "")
				+ (typeDesc != null ? "typeDesc=" + typeDesc + ", " : "")
				+ (departmentCode != null ? "departmentCode=" + departmentCode + ", " : "")
				+ (departmentDesc != null ? "departmentDesc=" + departmentDesc + ", " : "")
				+ (equipmentCode != null ? "equipmentCode=" + equipmentCode + ", " : "")
				+ (equipmentDesc != null ? "equipmentDesc=" + equipmentDesc + ", " : "")
				+ (projectCode != null ? "projectCode=" + projectCode + ", " : "")
				+ (projectDesc != null ? "projectDesc=" + projectDesc + ", " : "")
				+ (projectBudget != null ? "projectBudget=" + projectBudget + ", " : "")
				+ (locationCode != null ? "locationCode=" + locationCode + ", " : "")
				+ (locationDesc != null ? "locationDesc=" + locationDesc + ", " : "")
				+ (priorityCode != null ? "priorityCode=" + priorityCode + ", " : "")
				+ (priorityDesc != null ? "priorityDesc=" + priorityDesc + ", " : "")
				+ (customFields != null ? "customFields=" + Arrays.toString(customFields) + ", " : "")
				+ (reportedDate != null ? "reportedDate=" + reportedDate + ", " : "")
				+ (requestedEndDate != null ? "requestedEndDate=" + requestedEndDate + ", " : "")
				+ (requestedStartDate != null ? "requestedStartDate=" + requestedStartDate + ", " : "")
				+ (scheduledStartDate != null ? "scheduledStartDate=" + scheduledStartDate + ", " : "")
				+ (scheduledEndDate != null ? "scheduledEndDate=" + scheduledEndDate + ", " : "")
				+ (startDate != null ? "startDate=" + startDate + ", " : "")
				+ (completedDate != null ? "completedDate=" + completedDate + ", " : "")
				+ (dueDate != null ? "dueDate=" + dueDate + ", " : "")
				+ (createdDate != null ? "createdDate=" + createdDate + ", " : "")
				+ (createdBy != null ? "createdBy=" + createdBy + ", " : "")
				+ (problemCode != null ? "problemCode=" + problemCode + ", " : "")
				+ (failureCode != null ? "failureCode=" + failureCode + ", " : "")
				+ (causeCode != null ? "causeCode=" + causeCode + ", " : "")
				+ (actionCode != null ? "actionCode=" + actionCode + ", " : "")
				+ (costCode != null ? "costCode=" + costCode + ", " : "")
				+ (costCodeDesc != null ? "costCodeDesc=" + costCodeDesc + ", " : "")
				+ (assignedBy != null ? "assignedBy=" + assignedBy + ", " : "")
				+ (reportedBy != null ? "reportedBy=" + reportedBy + ", " : "")
				+ (reportedByDesc != null ? "reportedByDesc=" + reportedByDesc + ", " : "")
				+ (assignedTo != null ? "assignedTo=" + assignedTo + ", " : "")
				+ (assignedToDesc != null ? "assignedToDesc=" + assignedToDesc + ", " : "")
				+ (standardWO != null ? "standardWO=" + standardWO + ", " : "")
				+ (parentWO != null ? "parentWO=" + parentWO + ", " : "")
				+ (parentWODesc != null ? "parentWODesc=" + parentWODesc + ", " : "")
				+ (route != null ? "route=" + route + ", " : "") + (comment != null ? "comment=" + comment + ", " : "")
				+ (targetValue != null ? "targetValue=" + targetValue + ", " : "")
				+ (userDefinedFields != null ? "userDefinedFields=" + userDefinedFields + ", " : "")
				+ (origWO != null ? "origWO=" + origWO : "") + "]";
	}
}
