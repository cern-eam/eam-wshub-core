package ch.cern.eam.wshub.core.services.casemanagement.entities;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.datastream.schemas.mp_fields.StandardUserDefinedFields;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class EAMCaseManagement implements Serializable {
	@InforField(xpath = "CASEID/CASECODE")
	protected String caseCode;
	@InforField(xpath = "CASEID/DESCRIPTION", nullifyParentLevel = 0)
	protected String caseDescription;
	@InforField(xpath = "EQUIPMENTID/EQUIPMENTCODE")
	protected String equipmentCode;
	@InforField(xpath = "CASETYPE/TYPECODE")
	protected String caseType;
	@InforField(xpath = "DEPARTMENTID/DEPARTMENTCODE")
	protected String departmentCode;
	@InforField(xpath = "STATUS/STATUSCODE")
	protected String statusCode;
	@InforField(xpath = "RSTATUS/STATUSCODE")
	protected String systemStatusCode;
	@InforField(xpath = "REVIEWEDBYESIGN/ESIGNATURE/USERID/DESCRIPTION")
	protected String reviewedByDesign;
	@InforField(xpath = "DATEREVIEWED")
	protected Date dateReviewed;
	@InforField(xpath = "CREATEDBY/USERCODE")
	protected String createdBy;
	@InforField(xpath = "CREATEDDATE")
	protected Date createdDate;
	@InforField(xpath = "UPDATEDBY/USERCODE")
	protected String updatedby;
	@InforField(xpath = "DATEUPDATED")
	protected Date dateUpdated;

	//CASE DETAILS
	@InforField(xpath = "CaseDetails/CASECLASSID/CLASSCODE", nullifyParentLevel = 1)
	protected String classCode;
	@InforField(xpath = "CaseDetails/LOCATIONID/LOCATIONCODE", nullifyParentLevel = 1)
	protected String locationCode;
	@InforField(xpath = "CaseDetails/SERVICEPROBLEMID/SERVICEPROBLEMCODE", nullifyParentLevel = 1)
	protected String serviceProblemCode;
	@InforField(xpath = "CaseDetails/AREA", nullifyParentLevel = 0)
	protected String area;
	@InforField(xpath = "CaseDetails/WORKADDRESS", nullifyParentLevel = 0)
	protected String workaddress;
	@InforField(xpath = "CaseDetails/CASEPRIORITY/USERDEFINEDCODE", nullifyParentLevel = 1)
	protected String casePriorityCode;
	@InforField(xpath = "CaseDetails/CASEPRIORITY/ENTITY", nullifyParentLevel = 0)
	protected String casePriorityEntity;
	@InforField(xpath = "CaseDetails/COSTCODEID/COSTCODE", nullifyParentLevel = 1)
	protected String costCode;
	@InforField(xpath = "CaseDetails/EVENTSTARTDATE", nullifyParentLevel = 0)
	protected Date eventStartDate;
	@InforField(xpath = "CaseDetails/EVENTENDDATE", nullifyParentLevel = 0)
	protected Date eventEndDate;
	@InforField(xpath = "CaseDetails/REGULATORY", booleanType = BooleanType.TRUE_FALSE, nullifyParentLevel = 0)
	protected Boolean regulatory;
	@InforField(xpath = "CaseDetails/CASEFOLLOWUPREQUIRED", booleanType = BooleanType.TRUE_FALSE, nullifyParentLevel
			= 0)
	protected Boolean followUpRequired;
	@InforField(xpath = "CaseDetails/ISHAZARDOUSMATERIAL", booleanType = BooleanType.TRUE_FALSE, nullifyParentLevel = 0)
	protected Boolean isHazardousMaterial;
	@InforField(xpath = "CaseDetails/ESTIMATEDTOTALCOST", nullifyParentLevel = 0)
	protected BigDecimal estimatedTotalCost;
	@InforField(xpath = "CaseDetails/TOTALCOST", nullifyParentLevel = 0)
	protected BigDecimal totalCost;
	@InforField(xpath = "CaseDetails/CURRENCYCODE", nullifyParentLevel = 0)
	protected String currencyCode;
	@InforField(xpath = "CaseDetails/COSTREFRESHREQUIRED", booleanType = BooleanType.TRUE_FALSE, nullifyParentLevel = 0)
	protected Boolean costRefreshRequired;
	@InforField(xpath = "CaseDetails/CASEPARENT/CASECODE", nullifyParentLevel = 1)
	protected String caseParentCode;
	@InforField(xpath = "CaseDetails/WORKORDERID/JOBNUM", nullifyParentLevel = 1)
	protected String workOrderCode;
	@InforField(xpath = "CaseDetails/PERMITTOWORKID/PERMITTOWORKCODE", nullifyParentLevel = 1)
	protected String permitToWorkCode;
	@InforField(xpath = "CaseDetails/SHIFTID/SHIFTCODE", nullifyParentLevel = 1)
	protected String shiftCode;
	@InforField(xpath = "CaseDetails/PROJECTID/PROJECTCODE", nullifyParentLevel = 1)
	protected String projectCode;
	@InforField(xpath = "CaseDetails/CAMPAIGNID/CAMPAIGNCODE", nullifyParentLevel = 1)
	protected String campaignCode;
	// --- //

	// Liner Reference Details
	@InforField(xpath = "LinearReferenceDetails/LINEARREFUOM", nullifyParentLevel = 0)
	protected String linearRefUom;
//	@InforField(xpath = "LinearReferenceDetails/LINEARREFERENCEEVENT", nullifyParentLevel = 0)
//	protected LINEARREFERENCEEVENT_Type linearreferenceevent;
	@InforField(xpath = "LinearReferenceDetails/INSPECTIONDIRECTIONCODE", nullifyParentLevel = 0)
	protected String inspectionDirectionCode;
	@InforField(xpath = "LinearReferenceDetails/FLOWCODE", nullifyParentLevel = 0)
	protected String flowCode;
	@InforField(xpath = "LinearReferenceDetails/STARTINGAT", nullifyParentLevel = 0)
	protected BigDecimal startingAt;
	@InforField(xpath = "LinearReferenceDetails/EQUIPMENTLENGTH", nullifyParentLevel = 0)
	protected BigDecimal equipmentLength;
	// --- //

	// Tracking Details
	@InforField(xpath = "TrackingDetails/REQUESTEDBY/PERSONCODE", nullifyParentLevel = 1)
	protected String requestedBy;
	@InforField(xpath = "TrackingDetails/DATEREQUESTED", nullifyParentLevel = 0)
	protected Date dateRequested;
	@InforField(xpath = "TrackingDetails/PERSONRESPONSIBLE/EMPLOYEECODE", nullifyParentLevel = 1)
	protected String personResponsible;
	@InforField(xpath = "TrackingDetails/EMAIL", nullifyParentLevel = 0)
	protected String email;
	@InforField(xpath = "TrackingDetails/PREPAREDBY/USERCODE", nullifyParentLevel = 1)
	protected String preparedBy;
	@InforField(xpath = "TrackingDetails/PREPAREDBYEMAIL", nullifyParentLevel = 0)
	protected String preparedByEmail;
	@InforField(xpath = "TrackingDetails/ASSIGNEDTO/PERSONCODE", nullifyParentLevel = 1)
	protected String assignedTo;
	@InforField(xpath = "TrackingDetails/ASSIGNEDTOEMAIL", nullifyParentLevel = 0)
	protected String assignedToEmail;
	@InforField(xpath = "TrackingDetails/SCHEDULEDSTARTDATE", nullifyParentLevel = 0)
	protected Date scheduledStartDate;
	@InforField(xpath = "TrackingDetails/SCHEDULEDENDDATE", nullifyParentLevel = 0)
	protected Date scheduledEndDate;
	@InforField(xpath = "TrackingDetails/REQUESTEDSTART", nullifyParentLevel = 0)
	protected Date requestedStart;
	@InforField(xpath = "TrackingDetails/REQUESTEDEND", nullifyParentLevel = 0)
	protected Date requestedEnd;
	@InforField(xpath = "TrackingDetails/STARTDATE", nullifyParentLevel = 0)
	protected Date startDate;
	@InforField(xpath = "TrackingDetails/COMPLETEDDATE", nullifyParentLevel = 0)
	protected Date completedDate;
	@InforField(xpath = "TrackingDetails/CONTACTRECORDID/CONTACTRECORDCODE", nullifyParentLevel = 1)
	protected String contactRecordCode;
	@InforField(xpath = "TrackingDetails/CONTACTRECORDSTATUS/STATUSCODE", nullifyParentLevel = 1)
	protected String contactRecordStatus;
	@InforField(xpath = "TrackingDetails/SOURCETYPE/TYPECODE", nullifyParentLevel = 1)
	protected String sourceType;
	// --- //

	// Follow-Up Details
	@InforField(xpath = "FollowupWODetails/WODESCRIPTION", nullifyParentLevel = 0)
	protected String woDescription;
	@InforField(xpath = "FollowupWODetails/STANDARDWO/STDWOCODE", nullifyParentLevel = 1)
	protected String standardWo;
	@InforField(xpath = "FollowupWODetails/WORKORDERTYPE/TYPECODE", nullifyParentLevel = 1)
	protected String workOrderType;
	@InforField(xpath = "FollowupWODetails/WORKORDERCLASSID/CLASSCODE", nullifyParentLevel = 1)
	protected String workorderClassCode;
	@InforField(xpath = "FollowupWODetails/WORKORDERSTATUS/STATUSCODE", nullifyParentLevel = 1)
	protected String workorderStatus;
	@InforField(xpath = "FollowupWODetails/WOPRIORITY/PRIORITYCODE", nullifyParentLevel = 1)
	protected String woPriority;
	@InforField(xpath = "FollowupWODetails/TASKSID/TASKCODE", nullifyParentLevel = 1)
	protected String taskCode;
	@InforField(xpath = "FollowupWODetails/CASETASKJOBPLANID/TASKCODE", nullifyParentLevel = 0)
	protected String casetaskJobPlan;
	@InforField(xpath = "FollowupWODetails/TRADEID/TRADECODE", nullifyParentLevel = 0)
	protected String tradeCode;
	@InforField(xpath = "FollowupWODetails/ESTIMATEDHOURS", nullifyParentLevel = 0)
	protected BigDecimal estimatedHours;
	@InforField(xpath = "FollowupWODetails/PERSONS", nullifyParentLevel = 0)
	protected BigInteger persons;
	@InforField(xpath = "FollowupWODetails/FOLLOWUPWORKORDER/JOBNUM", nullifyParentLevel = 1)
	protected String followupWorkOrder;
	// --- //

	// Root Cause Details
	@InforField(xpath = "RouteCauseAnalysisDetails/DOWNTIMEHOURS", nullifyParentLevel = 0)
	private BigDecimal downtimehours;
	@InforField(xpath = "RouteCauseAnalysisDetails/DOWNTIMECOSTVALUE", nullifyParentLevel = 0)
	private BigDecimal downtimecostvalue;
	@InforField(xpath = "RouteCauseAnalysisDetails/TOTALCOST", nullifyParentLevel = 0)
	private BigDecimal totalcost;
	@InforField(xpath = "RouteCauseAnalysisDetails/LOSTPRODUCTIVITYHOURS", nullifyParentLevel = 0)
	private BigDecimal lostproductivityhours;
	@InforField(xpath = "RouteCauseAnalysisDetails/PROBLEMDESCRIPTION", nullifyParentLevel = 0)
	private String problemdescription;
	@InforField(xpath = "RouteCauseAnalysisDetails/WHATHAPPENED", nullifyParentLevel = 0)
	private String whathappened;
	@InforField(xpath = "RouteCauseAnalysisDetails/WHY1", nullifyParentLevel = 0)
	private String why1;
	@InforField(xpath = "RouteCauseAnalysisDetails/WHY2", nullifyParentLevel = 0)
	private String why2;
	@InforField(xpath = "RouteCauseAnalysisDetails/WHY3", nullifyParentLevel = 0)
	private String why3;
	@InforField(xpath = "RouteCauseAnalysisDetails/WHY4", nullifyParentLevel = 0)
	private String why4;
	@InforField(xpath = "RouteCauseAnalysisDetails/WHY5", nullifyParentLevel = 0)
	private String why5;
	@InforField(xpath = "RouteCauseAnalysisDetails/SOLUTION", nullifyParentLevel = 0)
	private String solution;
	// --- //


	@JsonIgnore
	@InforField(xpath = "USERDEFINEDAREA")
	private CustomField[] customFields;

	private Map<String, String> customFieldMap;

	@InforField(xpath = "StandardUserDefinedFields")
	private UserDefinedFields userDefinedFields;

	@InforField(xpath = "recordid")
	private BigInteger updateCount;

	@InforField(xpath = "has_department_security")
	protected String hasDepartmentSecurity;
	@InforField(xpath = "is_enhancedplanning_task")
	protected String isEnhancedPlanningTask;
	@InforField(xpath = "is_casehavetasks")
	protected String isCaseHaveTasks;

	public String getCaseCode() {
		return caseCode;
	}

	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}

	public String getCaseDescription() {
		return caseDescription;
	}

	public void setCaseDescription(String caseDescription) {
		this.caseDescription = caseDescription;
	}

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getSystemStatusCode() {
		return systemStatusCode;
	}

	public void setSystemStatusCode(String systemStatusCode) {
		this.systemStatusCode = systemStatusCode;
	}

	public String getReviewedByDesign() {
		return reviewedByDesign;
	}

	public void setReviewedByDesign(String reviewedByDesign) {
		this.reviewedByDesign = reviewedByDesign;
	}

	public Date getDateReviewed() {
		return dateReviewed;
	}

	public void setDateReviewed(Date dateReviewed) {
		this.dateReviewed = dateReviewed;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedby() {
		return updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getServiceProblemCode() {
		return serviceProblemCode;
	}

	public void setServiceProblemCode(String serviceProblemCode) {
		this.serviceProblemCode = serviceProblemCode;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getWorkaddress() {
		return workaddress;
	}

	public void setWorkaddress(String workaddress) {
		this.workaddress = workaddress;
	}

	public String getCasePriorityCode() {
		return casePriorityCode;
	}

	public void setCasePriorityCode(String casePriorityCode) {
		this.casePriorityCode = casePriorityCode;
	}

	public String getCasePriorityEntity() {
		return casePriorityEntity;
	}

	public void setCasePriorityEntity(String casePriorityEntity) {
		this.casePriorityEntity = casePriorityEntity;
	}

	public String getCostCode() {
		return costCode;
	}

	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}

	public Date getEventStartDate() {
		return eventStartDate;
	}

	public void setEventStartDate(Date eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	public Date getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(Date eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

	public Boolean getRegulatory() {
		return regulatory;
	}

	public void setRegulatory(Boolean regulatory) {
		this.regulatory = regulatory;
	}

	public Boolean getFollowUpRequired() {
		return followUpRequired;
	}

	public void setFollowUpRequired(Boolean followUpRequired) {
		this.followUpRequired = followUpRequired;
	}

	public Boolean getHazardousMaterial() {
		return isHazardousMaterial;
	}

	public void setHazardousMaterial(Boolean hazardousMaterial) {
		isHazardousMaterial = hazardousMaterial;
	}

	public BigDecimal getEstimatedTotalCost() {
		return estimatedTotalCost;
	}

	public void setEstimatedTotalCost(BigDecimal estimatedTotalCost) {
		this.estimatedTotalCost = estimatedTotalCost;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Boolean getCostRefreshRequired() {
		return costRefreshRequired;
	}

	public void setCostRefreshRequired(Boolean costRefreshRequired) {
		this.costRefreshRequired = costRefreshRequired;
	}

	public String getCaseParentCode() {
		return caseParentCode;
	}

	public void setCaseParentCode(String caseParentCode) {
		this.caseParentCode = caseParentCode;
	}

	public String getWorkOrderCode() {
		return workOrderCode;
	}

	public void setWorkOrderCode(String workOrderCode) {
		this.workOrderCode = workOrderCode;
	}

	public String getPermitToWorkCode() {
		return permitToWorkCode;
	}

	public void setPermitToWorkCode(String permitToWorkCode) {
		this.permitToWorkCode = permitToWorkCode;
	}

	public String getShiftCode() {
		return shiftCode;
	}

	public void setShiftCode(String shiftCode) {
		this.shiftCode = shiftCode;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getLinearRefUom() {
		return linearRefUom;
	}

	public void setLinearRefUom(String linearRefUom) {
		this.linearRefUom = linearRefUom;
	}

	public String getInspectionDirectionCode() {
		return inspectionDirectionCode;
	}

	public void setInspectionDirectionCode(String inspectionDirectionCode) {
		this.inspectionDirectionCode = inspectionDirectionCode;
	}

	public String getFlowCode() {
		return flowCode;
	}

	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}

	public BigDecimal getStartingAt() {
		return startingAt;
	}

	public void setStartingAt(BigDecimal startingAt) {
		this.startingAt = startingAt;
	}

	public BigDecimal getEquipmentLength() {
		return equipmentLength;
	}

	public void setEquipmentLength(BigDecimal equipmentLength) {
		this.equipmentLength = equipmentLength;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public Date getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(Date dateRequested) {
		this.dateRequested = dateRequested;
	}

	public String getPersonResponsible() {
		return personResponsible;
	}

	public void setPersonResponsible(String personResponsible) {
		this.personResponsible = personResponsible;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}

	public String getPreparedByEmail() {
		return preparedByEmail;
	}

	public void setPreparedByEmail(String preparedByEmail) {
		this.preparedByEmail = preparedByEmail;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getAssignedToEmail() {
		return assignedToEmail;
	}

	public void setAssignedToEmail(String assignedToEmail) {
		this.assignedToEmail = assignedToEmail;
	}

	public Date getScheduledStartDate() {
		return scheduledStartDate;
	}

	public void setScheduledStartDate(Date scheduledStartDate) {
		this.scheduledStartDate = scheduledStartDate;
	}

	public Date getScheduledEndDate() {
		return scheduledEndDate;
	}

	public void setScheduledEndDate(Date scheduledEndDate) {
		this.scheduledEndDate = scheduledEndDate;
	}

	public Date getRequestedStart() {
		return requestedStart;
	}

	public void setRequestedStart(Date requestedStart) {
		this.requestedStart = requestedStart;
	}

	public Date getRequestedEnd() {
		return requestedEnd;
	}

	public void setRequestedEnd(Date requestedEnd) {
		this.requestedEnd = requestedEnd;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public String getContactRecordCode() {
		return contactRecordCode;
	}

	public void setContactRecordCode(String contactRecordCode) {
		this.contactRecordCode = contactRecordCode;
	}

	public String getContactRecordStatus() {
		return contactRecordStatus;
	}

	public void setContactRecordStatus(String contactRecordStatus) {
		this.contactRecordStatus = contactRecordStatus;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getWoDescription() {
		return woDescription;
	}

	public void setWoDescription(String woDescription) {
		this.woDescription = woDescription;
	}

	public String getStandardWo() {
		return standardWo;
	}

	public void setStandardWo(String standardWo) {
		this.standardWo = standardWo;
	}

	public String getWorkOrderType() {
		return workOrderType;
	}

	public void setWorkOrderType(String workOrderType) {
		this.workOrderType = workOrderType;
	}

	public String getWorkorderClassCode() {
		return workorderClassCode;
	}

	public void setWorkorderClassCode(String workorderClassCode) {
		this.workorderClassCode = workorderClassCode;
	}

	public String getWorkorderStatus() {
		return workorderStatus;
	}

	public void setWorkorderStatus(String workorderStatus) {
		this.workorderStatus = workorderStatus;
	}

	public String getWoPriority() {
		return woPriority;
	}

	public void setWoPriority(String woPriority) {
		this.woPriority = woPriority;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getCasetaskJobPlan() {
		return casetaskJobPlan;
	}

	public void setCasetaskJobPlan(String casetaskJobPlan) {
		this.casetaskJobPlan = casetaskJobPlan;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public BigDecimal getEstimatedHours() {
		return estimatedHours;
	}

	public void setEstimatedHours(BigDecimal estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public BigInteger getPersons() {
		return persons;
	}

	public void setPersons(BigInteger persons) {
		this.persons = persons;
	}

	public String getFollowupWorkOrder() {
		return followupWorkOrder;
	}

	public void setFollowupWorkOrder(String followupWorkOrder) {
		this.followupWorkOrder = followupWorkOrder;
	}

	public CustomField[] getCustomFields() {
		return customFields;
	}

	public void setCustomFields(CustomField[] customFields) {
		this.customFields = customFields;
	}

	public Map<String, String> getCustomFieldMap() {
		return customFieldMap;
	}

	public void setCustomFieldMap(Map<String, String> customFieldMap) {
		this.customFieldMap = customFieldMap;
	}

	public UserDefinedFields getUserDefinedFields() {
		return userDefinedFields;
	}

	public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}

	public BigInteger getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(BigInteger updateCount) {
		this.updateCount = updateCount;
	}

	public String getHasDepartmentSecurity() {
		return hasDepartmentSecurity;
	}

	public void setHasDepartmentSecurity(String hasDepartmentSecurity) {
		this.hasDepartmentSecurity = hasDepartmentSecurity;
	}

	public String getIsEnhancedPlanningTask() {
		return isEnhancedPlanningTask;
	}

	public void setIsEnhancedPlanningTask(String isEnhancedPlanningTask) {
		this.isEnhancedPlanningTask = isEnhancedPlanningTask;
	}

	public String getIsCaseHaveTasks() {
		return isCaseHaveTasks;
	}

	public void setIsCaseHaveTasks(String isCaseHaveTasks) {
		this.isCaseHaveTasks = isCaseHaveTasks;
	}

	public BigDecimal getDowntimehours() {
		return downtimehours;
	}

	public void setDowntimehours(BigDecimal downtimehours) {
		this.downtimehours = downtimehours;
	}

	public BigDecimal getDowntimecostvalue() {
		return downtimecostvalue;
	}

	public void setDowntimecostvalue(BigDecimal downtimecostvalue) {
		this.downtimecostvalue = downtimecostvalue;
	}

	public BigDecimal getTotalcost() {
		return totalcost;
	}

	public void setTotalcost(BigDecimal totalcost) {
		this.totalcost = totalcost;
	}

	public BigDecimal getLostproductivityhours() {
		return lostproductivityhours;
	}

	public void setLostproductivityhours(BigDecimal lostproductivityhours) {
		this.lostproductivityhours = lostproductivityhours;
	}

	public String getProblemdescription() {
		return problemdescription;
	}

	public void setProblemdescription(String problemdescription) {
		this.problemdescription = problemdescription;
	}

	public String getWhathappened() {
		return whathappened;
	}

	public void setWhathappened(String whathappened) {
		this.whathappened = whathappened;
	}

	public String getWhy1() {
		return why1;
	}

	public void setWhy1(String why1) {
		this.why1 = why1;
	}

	public String getWhy2() {
		return why2;
	}

	public void setWhy2(String why2) {
		this.why2 = why2;
	}

	public String getWhy3() {
		return why3;
	}

	public void setWhy3(String why3) {
		this.why3 = why3;
	}

	public String getWhy4() {
		return why4;
	}

	public void setWhy4(String why4) {
		this.why4 = why4;
	}

	public String getWhy5() {
		return why5;
	}

	public void setWhy5(String why5) {
		this.why5 = why5;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	@Override
	public String toString() {
		return "EAMCaseManagement{" +
				"caseCode='" + caseCode + '\'' +
				", caseDescription='" + caseDescription + '\'' +
				", equipmentCode='" + equipmentCode + '\'' +
				", caseType='" + caseType + '\'' +
				", departmentCode='" + departmentCode + '\'' +
				", statusCode='" + statusCode + '\'' +
				", systemStatusCode='" + systemStatusCode + '\'' +
				", reviewedByDesign='" + reviewedByDesign + '\'' +
				", dateReviewed=" + dateReviewed +
				", createdBy='" + createdBy + '\'' +
				", createdDate=" + createdDate +
				", updatedby='" + updatedby + '\'' +
				", dateUpdated=" + dateUpdated +
				", classCode='" + classCode + '\'' +
				", locationCode='" + locationCode + '\'' +
				", serviceProblemCode='" + serviceProblemCode + '\'' +
				", area='" + area + '\'' +
				", workaddress='" + workaddress + '\'' +
				", casePriorityCode='" + casePriorityCode + '\'' +
				", casePriorityEntity='" + casePriorityEntity + '\'' +
				", costCode='" + costCode + '\'' +
				", eventStartDate=" + eventStartDate +
				", eventEndDate=" + eventEndDate +
				", regulatory='" + regulatory + '\'' +
				", followUpRequired=" + followUpRequired +
				", isHazardousMaterial=" + isHazardousMaterial +
				", estimatedTotalCost=" + estimatedTotalCost +
				", totalCost=" + totalCost +
				", currencyCode='" + currencyCode + '\'' +
				", costRefreshRequired='" + costRefreshRequired + '\'' +
				", caseParentCode='" + caseParentCode + '\'' +
				", workOrderCode='" + workOrderCode + '\'' +
				", permitToWorkCode='" + permitToWorkCode + '\'' +
				", shiftCode='" + shiftCode + '\'' +
				", projectCode='" + projectCode + '\'' +
				", campaignCode='" + campaignCode + '\'' +
				", linearRefUom='" + linearRefUom + '\'' +
				", inspectionDirectionCode='" + inspectionDirectionCode + '\'' +
				", flowCode='" + flowCode + '\'' +
				", startingAt=" + startingAt +
				", equipmentLength=" + equipmentLength +
				", requestedBy='" + requestedBy + '\'' +
				", dateRequested=" + dateRequested +
				", personResponsible='" + personResponsible + '\'' +
				", email='" + email + '\'' +
				", preparedBy='" + preparedBy + '\'' +
				", preparedByEmail='" + preparedByEmail + '\'' +
				", assignedTo='" + assignedTo + '\'' +
				", assignedToEmail='" + assignedToEmail + '\'' +
				", scheduledStartDate=" + scheduledStartDate +
				", scheduledEndDate=" + scheduledEndDate +
				", requestedStart=" + requestedStart +
				", requestedEnd=" + requestedEnd +
				", startDate=" + startDate +
				", completedDate=" + completedDate +
				", contactRecordCode='" + contactRecordCode + '\'' +
				", contactRecordStatus='" + contactRecordStatus + '\'' +
				", sourceType='" + sourceType + '\'' +
				", woDescription='" + woDescription + '\'' +
				", standardWo='" + standardWo + '\'' +
				", workOrderType='" + workOrderType + '\'' +
				", workorderClassCode='" + workorderClassCode + '\'' +
				", workorderStatus='" + workorderStatus + '\'' +
				", woPriority='" + woPriority + '\'' +
				", taskCode='" + taskCode + '\'' +
				", casetaskJobPlan='" + casetaskJobPlan + '\'' +
				", tradeCode='" + tradeCode + '\'' +
				", estimatedHours=" + estimatedHours +
				", persons=" + persons +
				", followupWorkOrder='" + followupWorkOrder + '\'' +
				", customFieldMap=" + customFieldMap +
				", userDefinedFields=" + userDefinedFields +
				", updateCount=" + updateCount +
				", hasDepartmentSecurity='" + hasDepartmentSecurity + '\'' +
				", isEnhancedPlanningTask='" + isEnhancedPlanningTask + '\'' +
				", isCaseHaveTasks='" + isCaseHaveTasks + '\'' +
				'}';
	}
}
