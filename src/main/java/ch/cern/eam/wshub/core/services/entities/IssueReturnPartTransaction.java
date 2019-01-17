package ch.cern.eam.wshub.core.services.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IssueReturnPartTransaction implements Serializable {
	private static final long serialVersionUID = -4387702768430557993L;
	
	private IssueReturnPartTransactionType transactionOn;
	private List<IssueReturnPartTransactionLine> transactionlines;
	
	private String workOrderNumber;
	private String workOrderDesc;
	private String activityCode;
	private String activityDesc;
	
	private String projectCode;
	private String projectDesc;
	private String budgetCode;
	private String budgetDesc;
	
	private String equipmentCode;
	private String equipmentDesc;
	
	private String employeeCode;
	private String employeeDesc;

	private String storeCode;
	private String storeDesc;
	private String departmentCode;
	private String date;
	
	private String transactionType;
	

	public String getTransactionInfo() {
		if (transactionOn == null) {
			return "NULL :-(";
		}
		
		switch (transactionOn) {
		case EQUIPMENT:
			return "EQUIPMENT - " + equipmentCode + " (" + equipmentDesc + ")";
		case PROJECT:
			return "PROJECT - " + projectCode + " (" + projectDesc + ") BUDGET - " + budgetCode;
		case WORKORDER:
			return "WORK ORDER - " + workOrderNumber + " (" + workOrderDesc + ") ACTIVITY - " + activityCode;
		case EMPLOYEE:
			return "EMPLOYEE - " + employeeCode + " (" + employeeDesc + ") ";
		default:
			return "DEFAULT";	
		}
	}
	
	public String getWorkOrderNumber() {
		return workOrderNumber;
	}
	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getBudgetCode() {
		return budgetCode;
	}
	public void setBudgetCode(String budgetCode) {
		this.budgetCode = budgetCode;
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getStoreDesc() {
		return storeDesc;
	}
	public void setStoreDesc(String storeDesc) {
		this.storeDesc = storeDesc;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public List<IssueReturnPartTransactionLine> getTransactionlines() {
		return transactionlines;
	}
	public void setTransactionlines(List<IssueReturnPartTransactionLine> transactionlines) {
		this.transactionlines = transactionlines;
	}
	@Override
	public String toString() {
		String s = "";
		if(getTransactionlines() != null)
			s = Arrays.stream(getTransactionlines().toArray(new IssueReturnPartTransactionLine[getTransactionlines().size()])).map(line -> line.toString()).collect(Collectors.joining(";"));
		
		return "IssueReturnPartTransaction ["
				+ (workOrderNumber != null ? "workOrderNumber="
						+ workOrderNumber + ", " : "")
				+ (activityCode != null ? "activityCode=" + activityCode + ", "
						: "")
				+ (projectCode != null ? "projectCode=" + projectCode + ", "
						: "")
				+ (budgetCode != null ? "budgetCode=" + budgetCode + ", " : "")
				+ (equipmentCode != null ? "equipmentCode=" + equipmentCode
						+ ", " : "")
				+ (storeCode != null ? "storeCode=" + storeCode + ", " : "")
				+ (departmentCode != null ? "departmentCode=" + departmentCode
						+ ", " : "")
				+ (date != null ? "date=" + date + ", " : "")
				+ s
				+ (transactionType != null ? "transactionType="
						+ transactionType : "") + "]";
	}
	public String getEquipmentDesc() {
		return equipmentDesc;
	}
	public void setEquipmentDesc(String equipmentDesc) {
		this.equipmentDesc = equipmentDesc;
	}
	public String getWorkOrderDesc() {
		return workOrderDesc;
	}
	public void setWorkOrderDesc(String workOrderDesc) {
		this.workOrderDesc = workOrderDesc;
	}
	public String getActivityDesc() {
		return activityDesc;
	}
	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}
	public String getProjectDesc() {
		return projectDesc;
	}
	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}
	public String getBudgetDesc() {
		return budgetDesc;
	}
	public void setBudgetDesc(String budgetDesc) {
		this.budgetDesc = budgetDesc;
	}
	public IssueReturnPartTransactionType getTransactionOn() {
		return transactionOn;
	}
	public void setTransactionOn(IssueReturnPartTransactionType transactionOn) {
		this.transactionOn = transactionOn;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeDesc() {
		return employeeDesc;
	}
	public void setEmployeeDesc(String employeeDesc) {
		this.employeeDesc = employeeDesc;
	}
	
}
