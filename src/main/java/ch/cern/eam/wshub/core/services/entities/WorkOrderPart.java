package ch.cern.eam.wshub.core.services.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.annotations.GridField;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;

public class WorkOrderPart implements Cloneable {

	@GridField(name = "partcode")
	private String partCode;

	@GridField(name = "partdescription")
	private String partDesc;

	@GridField(name = "plannedqty")
	private BigDecimal plannedQty;
	private BigDecimal reservedQty;
	private BigDecimal allocatedQty;

	@GridField(name = "usedqty")
	private BigDecimal usedQty;
	private BigDecimal quantity;
	private String activityCode;

	@GridField(name = "activity_display")
	private String activityDesc;
	private String tradeCode;

	@GridField(name = "storecode")
	private String storeCode;

	@GridField(name = "partuom")
	private String partUoM;

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

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getPlannedQty() {
		return plannedQty;
	}
	public void setPlannedQty(BigDecimal plannedQty) {
		this.plannedQty = plannedQty;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getReservedQty() {
		return reservedQty;
	}
	public void setReservedQty(BigDecimal reservedQty) {
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

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getAllocatedQty() {
		return allocatedQty;
	}
	public void setAllocatedQty(BigDecimal allocatedQty) {
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

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getUsedQty() { return usedQty; }
	public void setUsedQty(BigDecimal usedQty) { this.usedQty = usedQty; }

	public String getTransType() { return transType; }
	public void setTransType(String transType) { this.transType = transType; }

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getQuantity() { return quantity; }

	public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

	public String getPartUoM() {
		return partUoM;
	}

	public void setPartUoM(String partUoM) {
		this.partUoM = partUoM;
	}

	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
