package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class WorkOrderActivityCheckList implements Serializable {
	public static final class CheckListType {
		public static final String CHECKLIST_ITEM = "01";
		public static final String QUESTION_YES_NO = "02";
		public static final String QUALITATIVE = "03";
		public static final String QUANTITATIVE = "04";
		public static final String METER_READING = "05";
		public static final String INSPECTION = "06";
		public static final String OK_REPAIR_NEEDED = "07";
		public static final String GOOD_POOR = "08";
		public static final String OK_ADJUSTED = "09";
		public static final String OK_ADJUSTED_MEASUREMENT = "10";
		public static final String NONCONFORMITY_CHECK = "11";
		public static final String NONCONFORMITY_MEASUREMENT = "12";
		public static final String DATE = "13";
		public static final String DATETIME = "14";
		public static final String FREE_TEXT = "15";
		public static final String ENTITY = "16";
		public static final String DUAL_QUANTITATIVE = "17";
	}

	public static final class ReturnType {
		public static final String NULL = null;
		public static final String YES = "YES";
		public static final String NO = "NO";
		public static final String OK = "OK";
		public static final String COMPLETED = "COMPLETED";
		public static final String GOOD = "GOOD";
		public static final String POOR = "POOR";
		public static final String NONCONFORMITY = "NONCONFORMITY";
		public static final String ADJUSTED = "ADJUSTED";
		public static final String REPAIRSNEEDED = "REPAIRSNEEDED";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4618078230002480288L;

	private String checkListCode;
	private String workOrderCode;
	private String activityCode;
	private String sequence;
	private String occurrence;
	private String finalOccurrence;
	private String desc;
	private String updateCount;
	private String type;
	private String result;
	private BigDecimal numericValue;
	private BigDecimal numericValue2;
	private String finding;
	private String notes;
	private String UOM;
	private String UOM2;
	private String equipmentCode;
	private String equipmentDesc;
	private String entityType;
	private String entityCode;
	private String entityCodeOrg;
	private Boolean followUp = false;
	private String followUpWorkOrder;
	private Boolean requiredToClose = false;
	private Boolean hideFollowUp;
	private String color;
	private String freeText;
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date date;
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date dateTime;

	private String newCheckListCode;
	private String newWorkOrderCode;
	private List<Finding> possibleFindings;

	private BigInteger minimumValue;
	private BigInteger maximumValue;

	private String notApplicableOption;

	private String checklistDefinitionCode;

	public String getCheckListCode() {
		return checkListCode;
	}

	public void setCheckListCode(String checkListCode) {
		this.checkListCode = checkListCode;
	}

	public String getWorkOrderCode() {
		return workOrderCode;
	}

	public void setWorkOrderCode(String workOrderCode) {
		this.workOrderCode = workOrderCode;
	}

	public String getNewCheckListCode() {
		return newCheckListCode;
	}

	public void setNewCheckListCode(String newCheckListCode) {
		this.newCheckListCode = newCheckListCode;
	}

	public String getNewWorkOrderCode() {
		return newWorkOrderCode;
	}

	public void setNewWorkOrderCode(String newWorkOrderCode) {
		this.newWorkOrderCode = newWorkOrderCode;
	}

	public String getOccurrence() {
		return occurrence;
	}

	public void setOccurrence(String occurrence) {
		this.occurrence = occurrence;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getFinalOccurrence() {
		return finalOccurrence;
	}

	public void setFinalOccurrence(String finalOccurrence) {
		this.finalOccurrence = finalOccurrence;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(String updateCount) {
		this.updateCount = updateCount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public BigDecimal getNumericValue() {
		return numericValue;
	}

	public void setNumericValue(BigDecimal numericValue) {
		this.numericValue = numericValue;
	}

	public BigDecimal getNumericValue2() {
		return numericValue2;
	}

	public void setNumericValue2(BigDecimal numericValue2) {
		this.numericValue2 = numericValue2;
	}

	public Boolean getFollowUp() {
		return followUp;
	}

	public void setFollowUp(Boolean followUp) {
		this.followUp = followUp;
	}

	public String getFollowUpWorkOrder() {
		return followUpWorkOrder;
	}

	public void setFollowUpWorkOrder(String followUpWorkOrder) {
		this.followUpWorkOrder = followUpWorkOrder;
	}

	public Boolean getRequiredToClose() {
		return requiredToClose;
	}

	public void setRequiredToClose(Boolean requiredToClose) {
		this.requiredToClose = requiredToClose;
	}

	public Boolean getHideFollowUp() {
		return hideFollowUp;
	}

	public void setHideFollowUp(Boolean hideFollowUp) {
		this.hideFollowUp = hideFollowUp;
	}

	//
	//
	//
	public void setSelectedItems(String[] selectedItems) {
		if ("03".equals(type) || "06".equals(type)) {
			if (selectedItems.length == 0) {
				finding = null;
			} else if (selectedItems.length == 1) {
				finding = selectedItems[0];
			} else if (selectedItems.length == 2) {
				if (finding != null && Arrays.asList(selectedItems).contains(finding)) {
					// Return another element in this two elements array that is
					// not 'result'
					finding = selectedItems[(Arrays.asList(selectedItems).indexOf(finding) + 1) % 2];
				}
			}
		} else {
			if (selectedItems.length == 0) {
				result = null;
			} else if (selectedItems.length == 1) {
				result = selectedItems[0];
			} else if (selectedItems.length == 2) {
				if (result != null && Arrays.asList(selectedItems).contains(result)) {
					// Return another element in this two elements array that is
					// not 'result'
					result = selectedItems[(Arrays.asList(selectedItems).indexOf(result) + 1) % 2];
				}
			}
		}

	}

	public String[] getSelectedItems() {
		if ("03".equals(type) || "06".equals(type)) {
			return new String[] { finding };
		} else {
			return new String[] { result };
		}
	}

	//
	//
	//
	public void setCompleted(boolean completed) {
		if (completed) {
			result = "COMPLETED";
		} else {
			result = null;
		}
	}

	public boolean isCompleted() {
		return "COMPLETED".equalsIgnoreCase(result);
	}

	public List<Finding> getPossibleFindings() {
		return possibleFindings;
	}

	public void setPossibleFindings(List<Finding> possibleFindings) {
		this.possibleFindings = possibleFindings;
	}

	public String getUOM() {
		return UOM;
	}

	public void setUOM(String uOM) {
		UOM = uOM;
	}

	public String getUOM2() {
		return UOM2;
	}

	public void setUOM2(String UOM2) {
		this.UOM2 = UOM2;
	}

	public String getFinding() {
		return finding;
	}

	public void setFinding(String finding) {
		this.finding = finding;
	}

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public String getEquipmentDesc() {
		return equipmentDesc;
	}

	public void setEquipmentDesc(String equipmentDesc) {
		this.equipmentDesc = equipmentDesc;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getEntityCodeOrg() {
		return entityCodeOrg;
	}

	public void setEntityCodeOrg(String entityCodeOrg) {
		this.entityCodeOrg = entityCodeOrg;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public BigInteger getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(BigInteger minimumValue) {
		this.minimumValue = minimumValue;
	}

	public BigInteger getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(BigInteger maximumValue) {
		this.maximumValue = maximumValue;
	}

	public String getNotApplicableOption() {
		return notApplicableOption;
	}

	public void setNotApplicableOption(String notApplicableOption) {
		this.notApplicableOption = notApplicableOption;
	}

	public String getChecklistDefinitionCode() {
		return checklistDefinitionCode;
	}

	public void setChecklistDefinitionCode(String checklistDefinitionCode) {
		this.checklistDefinitionCode = checklistDefinitionCode;
	}

	public String getFreeText() { return freeText; }

	public void setFreeText(String freeText) { this.freeText = freeText; }

	public Date getDate() { return date; }

	public void setDate(Date date) { this.date = date; }

	public Date getDateTime() { return dateTime; }

	public void setDateTime(Date dateTime) { this.dateTime = dateTime; }

	@Override
	public String toString() {
		return "WorkOrderActivityCheckList{" +
				"checkListCode='" + checkListCode + '\'' +
				", workOrderCode='" + workOrderCode + '\'' +
				", activityCode='" + activityCode + '\'' +
				", sequence='" + sequence + '\'' +
				", occurrence='" + occurrence + '\'' +
				", finalOccurrence='" + finalOccurrence + '\'' +
				", desc='" + desc + '\'' +
				", updateCount='" + updateCount + '\'' +
				", type='" + type + '\'' +
				", result='" + result + '\'' +
				", numericValue=" + numericValue +
				", numericValue2=" + numericValue2 +
				", finding='" + finding + '\'' +
				", notes='" + notes + '\'' +
				", UOM='" + UOM + '\'' +
				", UOM2='" + UOM + '\'' +
				", equipmentCode='" + equipmentCode + '\'' +
				", equipmentDesc='" + equipmentDesc + '\'' +
				", followUp='" + followUp + '\'' +
				", followUWorkOrder='" + followUpWorkOrder + '\'' +
				", requiredToClose='" + requiredToClose + '\'' +
				", newCheckListCode='" + newCheckListCode + '\'' +
				", newWorkOrderCode='" + newWorkOrderCode + '\'' +
				", possibleFindings='" + possibleFindings + '\'' +
				", color='" + color + '\'' +
				", minimumValue='" + minimumValue + '\'' +
				", maximumValue='" + maximumValue + '\'' +
				", notApplicableOption='" + notApplicableOption + '\'' +
				", checklistDefinitionCode='" + checklistDefinitionCode + '\'' +
				", freeText='" + freeText + '\'' +
				", date='" + date + '\'' +
				", dateTime='" + dateTime + '\'' +
				'}';
	}
}
