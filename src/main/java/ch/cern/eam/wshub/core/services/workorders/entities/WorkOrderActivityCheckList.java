package ch.cern.eam.wshub.core.services.workorders.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Arrays;

@XmlAccessorType(XmlAccessType.FIELD)
public class WorkOrderActivityCheckList implements Serializable {

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
	private String finding;
	private String notes;
	private String UOM;
	private String equipmentCode;
	private String equipmentDesc;
	private String followUp;
	private String followUpWorkOrder;
	private String requiredToClose;

	private String newCheckListCode;
	private String newWorkOrderCode;
	private Finding[] possibleFindings;

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

	public String getFollowUp() {
		return followUp;
	}

	public void setFollowUp(String followUp) {
		this.followUp = followUp;
	}

	public void setFollowUpWorkOrder(String followUpWorkOrder) {
		this.followUpWorkOrder = followUpWorkOrder;
	}

	public String getFollowUpWorkOrder() {
		return followUpWorkOrder;
	}

	public String getRequiredToClose() {
		return requiredToClose;
	}

	public void setRequiredToClose(String requiredToClose) {
		this.requiredToClose = requiredToClose;
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


	public Double getNumberResult() {
		if (result != null) {
			return new Double(result);
		} else {
			return null;
		}
	}

	public void setNumberResult(Double doubleResult) {
		result = doubleResult.toString();
	}

	public boolean isCompleted() {
		return "COMPLETED".equalsIgnoreCase(result);
	}

	public Finding[] getPossibleFindings() {
		return possibleFindings;
	}

	public void setPossibleFindings(Finding[] possibleFindings) {
		this.possibleFindings = possibleFindings;
	}

	public String getUOM() {
		return UOM;
	}

	public void setUOM(String uOM) {
		UOM = uOM;
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
				", finding='" + finding + '\'' +
				", notes='" + notes + '\'' +
				", UOM='" + UOM + '\'' +
				", equipmentCode='" + equipmentCode + '\'' +
				", equipmentDesc='" + equipmentDesc + '\'' +
				", followUp='" + followUp + '\'' +
				", followUWorkOrder='" + followUpWorkOrder + '\'' +
				", requiredToClose='" + requiredToClose + '\'' +
				", newCheckListCode='" + newCheckListCode + '\'' +
				", newWorkOrderCode='" + newWorkOrderCode + '\'' +
				", possibleFindings=" + Arrays.toString(possibleFindings) +
				'}';
	}
}
