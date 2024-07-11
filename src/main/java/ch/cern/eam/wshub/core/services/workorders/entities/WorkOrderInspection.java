package ch.cern.eam.wshub.core.services.workorders.entities;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import java.util.Arrays;

public class WorkOrderInspection {

	private String code;
	private String updateCount;
	private String workOrderNumber;
	private String pointCode;
	private String pointType;
	private String pointDesc;
	private String aspectCode;
	private String aspectDesc;
	private String aspectClassCode;
	private String aspectClassDesc;
	private String date;
	private String value;
	private String sequenceNumber;
	private String findingCode;
	private String findingDesc;
	private String note;
	private String equipmentCode;
	private String equipmentDesc;
	private String UOM;
	private String showValue;
	private String showFindings;
	private String[] findingCodes;
	private String[] findingDescs;
	public String getUpdateCount() {
		return updateCount;
	}
	public void setUpdateCount(String updateCount) {
		this.updateCount = updateCount;
	}
	public String getWorkOrderNumber() {
		return workOrderNumber;
	}
	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}
	public String getPointCode() {
		return pointCode;
	}
	public void setPointCode(String pointCode) {
		this.pointCode = pointCode;
	}
	public String getPointType() {
		return pointType;
	}
	public void setPointType(String pointType) {
		this.pointType = pointType;
	}
	public String getPointDesc() {
		return pointDesc;
	}
	public void setPointDesc(String pointDesc) {
		this.pointDesc = pointDesc;
	}
	public String getAspectCode() {
		return aspectCode;
	}
	public void setAspectCode(String aspectCode) {
		this.aspectCode = aspectCode;
	}
	public String getAspectDesc() {
		return aspectDesc;
	}
	public void setAspectDesc(String aspectDesc) {
		this.aspectDesc = aspectDesc;
	}
	public String getAspectClassCode() {
		return aspectClassCode;
	}
	public void setAspectClassCode(String aspectClassCode) {
		this.aspectClassCode = aspectClassCode;
	}
	public String getAspectClassDesc() {
		return aspectClassDesc;
	}
	public void setAspectClassDesc(String aspectClassDesc) {
		this.aspectClassDesc = aspectClassDesc;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getFindingCode() {
		return findingCode;
	}
	public void setFindingCode(String findingCode) {
		this.findingCode = findingCode;
	}
	public String getFindingDesc() {
		return findingDesc;
	}
	public void setFindingDesc(String findingDesc) {
		this.findingDesc = findingDesc;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	@XmlElementWrapper(name="findingCodes") 
    @XmlElement(name="code")
	public String[] getFindingCodes() {
		return findingCodes;
	}
	public void setFindingCodes(String[] findingCodes) {
		this.findingCodes = findingCodes;
	}
	@XmlElementWrapper(name="findingDescs") 
    @XmlElement(name="desc")
	public String[] getFindingDescs() {
		return findingDescs;
	}
	public void setFindingDescs(String[] findingDescs) {
		this.findingDescs = findingDescs;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getEquipmentDesc() {
		return equipmentDesc;
	}
	public void setEquipmentDesc(String equipmentDesc) {
		this.equipmentDesc = equipmentDesc;
	}
	public String getUOM() {
		return UOM;
	}
	public void setUOM(String uOM) {
		UOM = uOM;
	}
	public String getShowValue() {
		return showValue;
	}
	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}
	public String getShowFindings() {
		return showFindings;
	}
	public void setShowFindings(String showFindings) {
		this.showFindings = showFindings;
	}
	@Override
	public String toString() {
		return "WorkOrderInspection ["
				+ (code != null ? "code=" + code + ", " : "")
				+ (updateCount != null ? "updateCount=" + updateCount + ", "
						: "")
				+ (workOrderNumber != null ? "workOrderNumber="
						+ workOrderNumber + ", " : "")
				+ (pointCode != null ? "pointCode=" + pointCode + ", " : "")
				+ (pointType != null ? "pointType=" + pointType + ", " : "")
				+ (pointDesc != null ? "pointDesc=" + pointDesc + ", " : "")
				+ (aspectCode != null ? "aspectCode=" + aspectCode + ", " : "")
				+ (aspectDesc != null ? "aspectDesc=" + aspectDesc + ", " : "")
				+ (aspectClassCode != null ? "aspectClassCode="
						+ aspectClassCode + ", " : "")
				+ (aspectClassDesc != null ? "aspectClassDesc="
						+ aspectClassDesc + ", " : "")
				+ (date != null ? "date=" + date + ", " : "")
				+ (value != null ? "value=" + value + ", " : "")
				+ (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber
						+ ", " : "")
				+ (findingCode != null ? "findingCode=" + findingCode + ", "
						: "")
				+ (findingDesc != null ? "findingDesc=" + findingDesc + ", "
						: "")
				+ (note != null ? "note=" + note + ", " : "")
				+ (equipmentCode != null ? "equipmentCode=" + equipmentCode
						+ ", " : "")
				+ (equipmentDesc != null ? "equipmentDesc=" + equipmentDesc
						+ ", " : "")
				+ (UOM != null ? "UOM=" + UOM + ", " : "")
				+ (showValue != null ? "showValue=" + showValue + ", " : "")
				+ (showFindings != null ? "showFindings=" + showFindings + ", "
						: "")
				+ (findingCodes != null ? "findingCodes="
						+ Arrays.toString(findingCodes) + ", " : "")
				+ (findingDescs != null ? "findingDescs="
						+ Arrays.toString(findingDescs) : "") + "]";
	}
	
}
