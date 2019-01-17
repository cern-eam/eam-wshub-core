package ch.cern.eam.wshub.core.services.workorders.entities;

public class WorkOrderAdditionalCosts {

	private String CostDescription;
	private String ActivityCode;
	private String TradeCode;
	private String CostType;
	private String Date;
	private String Cost;
	private String WorkOrderNumber;
	public String getCostDescription() {
		return CostDescription;
	}
	public void setCostDescription(String costDescription) {
		CostDescription = costDescription;
	}
	public String getTradeCode() {
		return TradeCode;
	}
	public void setTradeCode(String tradeCode) {
		TradeCode = tradeCode;
	}
	public String getCostType() {
		return CostType;
	}
	public void setCostType(String costType) {
		CostType = costType;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getWorkOrderNumber() {
		return WorkOrderNumber;
	}
	public void setWorkOrderNumber(String workOrderNumber) {
		WorkOrderNumber = workOrderNumber;
	}
	public String getCost() {
		return Cost;
	}
	public void setCost(String cost) {
		Cost = cost;
	}
	public String getActivityCode() {
		return ActivityCode;
	}
	public void setActivityCode(String activityCode) {
		ActivityCode = activityCode;
	}
	
	
}
