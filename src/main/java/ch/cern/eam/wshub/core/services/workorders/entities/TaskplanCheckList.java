package ch.cern.eam.wshub.core.services.workorders.entities;

public class TaskplanCheckList {

	private String taskPlanCode;
	private String taskPlanRevision;
	private String checklistCode;
	private String checklistDesc;
	private String sequence;
	private String type;
	private String requiredEntry;
	private String equipmentLevel;
	private String UOM;
	private String aspectCode;
	private String pointType;
	private String repeatingOccurrences;
	private String followUpTaskPlan;
	private String followUpTaskPlanRevision;
	private String findings;
	private String categoryCode;
	private String classCode;

	private String equipmentFilter;
	
	public String getTaskPlanCode() {
		return taskPlanCode;
	}
	public void setTaskPlanCode(String taskPlanCode) {
		this.taskPlanCode = taskPlanCode;
	}
	public String getTaskPlanRevision() {
		return taskPlanRevision;
	}
	public void setTaskPlanRevision(String taskPlanRevision) {
		this.taskPlanRevision = taskPlanRevision;
	}
	public String getChecklistCode() {
		return checklistCode;
	}
	public void setChecklistCode(String checklistCode) {
		this.checklistCode = checklistCode;
	}
	public String getChecklistDesc() {
		return checklistDesc;
	}
	public void setChecklistDesc(String checklistDesc) {
		this.checklistDesc = checklistDesc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRequiredEntry() {
		return requiredEntry;
	}
	public void setRequiredEntry(String requiredEntry) {
		this.requiredEntry = requiredEntry;
	}
	public String getEquipmentLevel() {
		return equipmentLevel;
	}
	public void setEquipmentLevel(String equipmentLevel) {
		this.equipmentLevel = equipmentLevel;
	}
	public String getUOM() {
		return UOM;
	}
	public void setUOM(String uOM) {
		UOM = uOM;
	}
	public String getAspectCode() {
		return aspectCode;
	}
	public void setAspectCode(String aspectCode) {
		this.aspectCode = aspectCode;
	}
	public String getPointType() {
		return pointType;
	}
	public void setPointType(String pointType) {
		this.pointType = pointType;
	}
	public String getRepeatingOccurrences() {
		return repeatingOccurrences;
	}
	public void setRepeatingOccurrences(String repeatingOccurrences) {
		this.repeatingOccurrences = repeatingOccurrences;
	}
	public String getFollowUpTaskPlan() {
		return followUpTaskPlan;
	}
	public void setFollowUpTaskPlan(String followUpTaskPlan) {
		this.followUpTaskPlan = followUpTaskPlan;
	}
	public String getFindings() {
		return findings;
	}
	public void setFindings(String findings) {
		this.findings = findings;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getFollowUpTaskPlanRevision() {
		return followUpTaskPlanRevision;
	}
	public void setFollowUpTaskPlanRevision(String followUpTaskPlanRevision) {
		this.followUpTaskPlanRevision = followUpTaskPlanRevision;
	}

	public String getEquipmentFilter() {
		return equipmentFilter;
	}

	public void setEquipmentFilter(String equipmentFilter) {
		this.equipmentFilter = equipmentFilter;
	}

	@Override
	public String toString() {
		return "TaskplanCheckList ["
				+ (taskPlanCode != null ? "taskPlanCode=" + taskPlanCode + ", "
						: "")
				+ (taskPlanRevision != null ? "taskPlanRevision="
						+ taskPlanRevision + ", " : "")
				+ (checklistCode != null ? "checklistCode=" + checklistCode
						+ ", " : "")
				+ (checklistDesc != null ? "checklistDesc=" + checklistDesc
						+ ", " : "")
				+ (sequence != null ? "sequence=" + sequence + ", " : "")
				+ (type != null ? "type=" + type + ", " : "")
				+ (requiredEntry != null ? "requiredEntry=" + requiredEntry
						+ ", " : "")
				+ (equipmentLevel != null ? "equipmentLevel=" + equipmentLevel
						+ ", " : "")
				+ (UOM != null ? "UOM=" + UOM + ", " : "")
				+ (aspectCode != null ? "aspectCode=" + aspectCode + ", " : "")
				+ (pointType != null ? "pointType=" + pointType + ", " : "")
				+ (repeatingOccurrences != null ? "repeatingOccurrences="
						+ repeatingOccurrences + ", " : "")
				+ (followUpTaskPlan != null ? "followUpTaskPlan="
						+ followUpTaskPlan + ", " : "")
				+ (followUpTaskPlanRevision != null ? "followUpTaskPlanRevision="
						+ followUpTaskPlanRevision + ", "
						: "")
				+ (findings != null ? "findings=" + findings : "") + "]";
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	
	
	
}
