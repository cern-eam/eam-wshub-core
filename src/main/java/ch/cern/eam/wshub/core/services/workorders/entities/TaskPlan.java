package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.BigIntegerAdapter;
import ch.cern.eam.wshub.core.adapters.BooleanAdapter;
import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TaskPlan {

	@EAMField(xpath = "TASKLISTID/TASKCODE")
	private String code;

	@EAMField(xpath = "TASKLISTID/DESCRIPTION")
	private String description;

	@EAMField(xpath = "TASKLISTID/TASKREVISION")
	private BigInteger taskRevision;

	@EAMField(xpath = "CHECKLISTPERFORMEDBYREQUIRED")
	private Boolean performedByRequired;

	@EAMField(xpath = "CHECKLISTREVIEWEDBYREQUIRED")
	private Boolean reviewedByRequired;

	@EAMField(xpath = "VIEWONLYRESPONSIBILITY/USERDEFINEDCODE")
	private String viewOnlyResponsibility;

	@EAMField(xpath = "REVIEWRESPONSIBILITY/USERDEFINEDCODE")
	private String reviewResponsibility;

	@EAMField(xpath = "PERFORMBYRESPONSIBILITY/USERDEFINEDCODE")
	private String performByResponsibility;

	@EAMField(xpath = "PERFORMBY2RESPONSIBILITY/USERDEFINEDCODE")
	private String performBy2Responsibility;

	@EAMField(xpath = "ACTIVECHECKLIST")
	private Boolean activeChecklist;

	@EAMField(xpath = "TRADEID/TRADECODE")

	private String tradeCode;

	@EAMField(xpath = "CLASSID/CLASSCODE")
	private String classCode;

	@EAMField(xpath = "HOURSREQUESTED")
	private BigDecimal estimatedHours;

	@EAMField(xpath = "PERSONS")
	private BigInteger peopleRequired;

	@EAMField(xpath = "TASKPLANTYPE")
	private String typeCode;

	@EAMField(xpath = "STATUS/STATUSCODE")
	private String revisionStatus;

	@EAMField(xpath = "OUTOFSERVICE")
	private Boolean outOfService;

	@EAMField(xpath = "EQUIPMENTTYPE")
	private String equipmentType;

	@EAMField(xpath = "EQUIPMENTCLASSID/CLASSCODE")
	private String equipmentClass;

	@EAMField(xpath = "MATERIALLISTID/MTLCODE")
	private String materialList;

	@EAMField(xpath = "StandardUserDefinedFields")
	private UserDefinedFields userDefinedFields;

	@EAMField(xpath = "WODESCRIPTION")
	private String workOrderDescription;

	@EAMField(xpath = "WOTYPE/TYPECODE")
	private String workOrderType;

	@EAMField(xpath = "WOCLASS/CLASSCODE")
	private String workOrderClass;

	@EAMField(xpath = "WOSTATUS")
	private String workOrderStatus;

	@EAMField(xpath = "WOPRIORITY/PRIORITYCODE")
	private String workOrderPriority;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigInteger getTaskRevision() {
		return taskRevision;
	}

	public void setTaskRevision(BigInteger taskRevision) {
		this.taskRevision = taskRevision;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getPerformedByRequired() {
		return performedByRequired;
	}

	public void setPerformedByRequired(Boolean performedByRequired) {
		this.performedByRequired = performedByRequired;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getReviewedByRequired() {
		return reviewedByRequired;
	}

	public void setReviewedByRequired(Boolean reviewedByRequired) {
		this.reviewedByRequired = reviewedByRequired;
	}

	public String getReviewResponsibility() {
		return reviewResponsibility;
	}

	public void setReviewResponsibility(String reviewResponsibility) {
		this.reviewResponsibility = reviewResponsibility;
	}

	public String getPerformByResponsibility() {
		return performByResponsibility;
	}

	public void setPerformByResponsibility(String performByResponsibility) {
		this.performByResponsibility = performByResponsibility;
	}

	public String getPerformBy2Responsibility() {
		return performBy2Responsibility;
	}

	public void setPerformBy2Responsibility(String performBy2Responsibility) {
		this.performBy2Responsibility = performBy2Responsibility;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getActiveChecklist() {
		return activeChecklist;
	}

	public void setActiveChecklist(Boolean activeChecklist) {
		this.activeChecklist = activeChecklist;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getEstimatedHours() {
		return estimatedHours;
	}

	public void setEstimatedHours(BigDecimal estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	@XmlJavaTypeAdapter(BigIntegerAdapter.class)
	public BigInteger getPeopleRequired() {
		return peopleRequired;
	}

	public void setPeopleRequired(BigInteger peopleRequired) {
		this.peopleRequired = peopleRequired;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getRevisionStatus() {
		return revisionStatus;
	}

	public void setRevisionStatus(String revisionStatus) {
		this.revisionStatus = revisionStatus;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getOutOfService() {
		return outOfService;
	}

	public void setOutOfService(Boolean outOfService) {
		this.outOfService = outOfService;
	}

	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}

	public String getEquipmentClass() {
		return equipmentClass;
	}

	public void setEquipmentClass(String equipmentClass) {
		this.equipmentClass = equipmentClass;
	}

	public String getMaterialList() {
		return materialList;
	}

	public void setMaterialList(String materialList) {
		this.materialList = materialList;
	}

	public UserDefinedFields getUserDefinedFields() {
		return userDefinedFields;
	}

	public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}

	public String getViewOnlyResponsibility() {
		return viewOnlyResponsibility;
	}

	public void setViewOnlyResponsibility(String viewOnlyResponsibility) {
		this.viewOnlyResponsibility = viewOnlyResponsibility;
	}

	public String getWorkOrderDescription() {
		return workOrderDescription;
	}

	public void setWorkOrderDescription(String workOrderDescription) {
		this.workOrderDescription = workOrderDescription;
	}

	public String getWorkOrderType() {
		return workOrderType;
	}

	public void setWorkOrderType(String workOrderType) {
		this.workOrderType = workOrderType;
	}

	public String getWorkOrderClass() {
		return workOrderClass;
	}

	public void setWorkOrderClass(String workOrderClass) {
		this.workOrderClass = workOrderClass;
	}

	public String getWorkOrderStatus() {
		return workOrderStatus;
	}

	public void setWorkOrderStatus(String workOrderStatus) {
		this.workOrderStatus = workOrderStatus;
	}

	public String getWorkOrderPriority() {
		return workOrderPriority;
	}

	public void setWorkOrderPriority(String workOrderPriority) {
		this.workOrderPriority = workOrderPriority;
	}

	@Override
	public String toString() {
		return "TaskPlan{" +
				"activeChecklist=" + activeChecklist +
				", classCode='" + classCode + '\'' +
				", code='" + code + '\'' +
				", description='" + description + '\'' +
				", equipmentClass='" + equipmentClass + '\'' +
				", equipmentType='" + equipmentType + '\'' +
				", estimatedHours=" + estimatedHours +
				", materialList='" + materialList + '\'' +
				", outOfService=" + outOfService +
				", peopleRequired=" + peopleRequired +
				", performBy2Responsibility='" + performBy2Responsibility + '\'' +
				", performByResponsibility='" + performByResponsibility + '\'' +
				", performedByRequired=" + performedByRequired +
				", reviewedByRequired=" + reviewedByRequired +
				", reviewResponsibility='" + reviewResponsibility + '\'' +
				", revisionStatus='" + revisionStatus + '\'' +
				", taskRevision=" + taskRevision +
				", tradeCode='" + tradeCode + '\'' +
				", typeCode='" + typeCode + '\'' +
				", userDefinedFields=" + userDefinedFields +
				", viewOnlyResponsibility='" + viewOnlyResponsibility + '\'' +
				", workOrderClass='" + workOrderClass + '\'' +
				", workOrderDescription='" + workOrderDescription + '\'' +
				", workOrderPriority='" + workOrderPriority + '\'' +
				", workOrderStatus='" + workOrderStatus + '\'' +
				", workOrderType='" + workOrderType + '\'' +
				'}';
	}
  
}
