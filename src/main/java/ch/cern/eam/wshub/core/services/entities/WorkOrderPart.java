package ch.cern.eam.wshub.core.services.entities;

public class WorkOrderPart {

	private String PartCode;
	private String PlannedQty;
	private String ReservedQty;
	private String ActivityCode;
	private String TradeCode;
	private String StoreCode;
	private String AllocatedQty;
	private String PlannedSource;
	private String Available;
	private String WorkOrderNumber;

	public String getPartCode() {
		return PartCode;
	}
	public void setPartCode(String partCode) {
		PartCode = partCode;
	}
	public String getPlannedQty() {
		return PlannedQty;
	}
	public void setPlannedQty(String plannedQty) {
		PlannedQty = plannedQty;
	}
	public String getReservedQty() {
		return ReservedQty;
	}
	public void setReservedQty(String reservedQty) {
		ReservedQty = reservedQty;
	}
	public String getActivityCode() {
		return ActivityCode;
	}
	public void setActivityCode(String activityCode) {
		ActivityCode = activityCode;
	}
	public String getTradeCode() {
		return TradeCode;
	}
	public void setTradeCode(String tradeCode) {
		TradeCode = tradeCode;
	}
	public String getStoreCode() {
		return StoreCode;
	}
	public void setStoreCode(String storeCode) {
		StoreCode = storeCode;
	}
	public String getAllocatedQty() {
		return AllocatedQty;
	}
	public void setAllocatedQty(String allocatedQty) {
		AllocatedQty = allocatedQty;
	}
	public String getPlannedSource() {
		return PlannedSource;
	}
	public void setPlannedSource(String plannedSource) {
		PlannedSource = plannedSource;
	}
	public String getAvailable() {
		return Available;
	}
	public void setAvailable(String available) {
		Available = available;
	}
	public String getWorkOrderNumber() {
		return WorkOrderNumber;
	}
	public void setWorkOrderNumber(String workOrderNumber) {
		WorkOrderNumber = workOrderNumber;
	}
	
	
	
}
