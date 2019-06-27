package ch.cern.eam.wshub.core.services.entities;

public class WorkOrderPart {

	private String partCode;
	private String partDesc;
	private String plannedQty;
	private String reservedQty;
	private String allocatedQty;
	private String usedQty;
	private String quantity;
	private String activityCode;
	private String activityDesc;
	private String tradeCode;
	private String storeCode;

	private String plannedSource;
	private String available;
	private String workOrderNumber;
	private String transType;

	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getPlannedQty() {
		return plannedQty;
	}
	public void setPlannedQty(String plannedQty) {
		this.plannedQty = plannedQty;
	}
	public String getReservedQty() {
		return reservedQty;
	}
	public void setReservedQty(String reservedQty) {
		this.reservedQty = reservedQty;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public String getTradeCode() {
		return tradeCode;
	}
	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getAllocatedQty() {
		return allocatedQty;
	}
	public void setAllocatedQty(String allocatedQty) {
		this.allocatedQty = allocatedQty;
	}
	public String getPlannedSource() {
		return plannedSource;
	}
	public void setPlannedSource(String plannedSource) {
		this.plannedSource = plannedSource;
	}
	public String getAvailable() {
		return available;
	}
	public void setAvailable(String available) {
		this.available = available;
	}
	public String getWorkOrderNumber() {
		return workOrderNumber;
	}
	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public String getPartDesc() { return partDesc; }
	public void setPartDesc(String partDesc) { this.partDesc = partDesc; }

	public String getActivityDesc() { return activityDesc; }
	public void setActivityDesc(String activityDesc) { this.activityDesc = activityDesc; }

	public String getUsedQty() { return usedQty; }
	public void setUsedQty(String usedQty) { this.usedQty = usedQty; }

	public String getTransType() { return transType; }
	public void setTransType(String transType) { this.transType = transType; }

	public String getQuantity() { return quantity; }

	public void setQuantity(String quantity) { this.quantity = quantity; }
}
