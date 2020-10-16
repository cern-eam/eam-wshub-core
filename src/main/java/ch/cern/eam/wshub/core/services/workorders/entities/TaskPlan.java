package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.annotations.InforField;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TaskPlan {

	@InforField(xpath = "TASKLISTID/TASKCODE")
	private String code;

	@InforField(xpath = "TASKLISTID/TASKREVISION")
	private BigInteger taskRevision;

	@InforField(xpath = "CHECKLISTPERFORMEDBYREQUIRED")
	private  Boolean performedByRequired;

	@InforField(xpath = "CHECKLISTREVIEWEDBYREQUIRED")
	private Boolean reviewedByRequired;

	@InforField(xpath = "StandardUserDefinedFields/UDFCHKBOX03")
	private Boolean forceChecklistExpansion;

	private String description;
	private String tradeCode;
	private String classCode;
	private BigDecimal estimatedHours;
	private String peopleRequired;
	private String typeCode;
	private String revisionStatus;
	private String revision;
	private String outOfService;
	private String equipmentType;
	private String equipmentClass;
	private String materialList;
	
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
	public String getTradeCode() {
		return tradeCode;
	}
	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	@Override
	public String toString() {
		return "TaskPlan [" + (code != null ? "code=" + code + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (tradeCode != null ? "tradeCode=" + tradeCode + ", " : "")
				+ (classCode != null ? "classCode=" + classCode + ", " : "")
				+ (estimatedHours != null ? "estimatedHours=" + estimatedHours + ", " : "")
				+ (peopleRequired != null ? "peopleRequired=" + peopleRequired + ", " : "")
				+ (typeCode != null ? "typeCode=" + typeCode + ", " : "")
				+ (revisionStatus != null ? "revisionStatus=" + revisionStatus + ", " : "")
				+ (revision != null ? "revision=" + revision + ", " : "")
				+ (outOfService != null ? "outOfService=" + outOfService + ", " : "")
				+ (equipmentType != null ? "equipmenttype=" + equipmentType + ", " : "")
				+ (equipmentClass != null ? "equipmentClass=" + equipmentClass + ", " : "")
				+ (materialList != null ? "materialList=" + materialList : "") + "]";
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
	public String getPeopleRequired() {
		return peopleRequired;
	}
	public void setPeopleRequired(String peopleRequired) {
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
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getOutOfService() {
		return outOfService;
	}
	public void setOutOfService(String outOfService) {
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
	public BigInteger getTaskRevision() { return taskRevision; }
	public void setTaskRevision(BigInteger taskRevision) { this.taskRevision = taskRevision; }

	public Boolean getPerformedByRequired() {
		return performedByRequired;
	}

	public void setPerformedByRequired(Boolean performedByRequired) {
		this.performedByRequired = performedByRequired;
	}

	public Boolean getReviewedByRequired() {
		return reviewedByRequired;
	}

	public void setReviewedByRequired(Boolean reviewedByRequired) {
		this.reviewedByRequired = reviewedByRequired;
	}

	public Boolean getForceChecklistExpansion() {
		return forceChecklistExpansion;
	}

	public void setForceChecklistExpansion(Boolean forceChecklistExpansion) {
		this.forceChecklistExpansion = forceChecklistExpansion;
	}
}
