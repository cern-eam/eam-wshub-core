package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.BigIntegerAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.InforField;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class WorkOrderAdditionalCosts {

	@InforField(xpath = "DESCRIPTION")
	private String costDescription;

	@InforField(xpath = "ACTIVITYID/ACTIVITYCODE/value")
	private BigInteger activityCode;

	@InforField(xpath = "COSTTYPEID/COSTTYPECODE")
	private String costType;

	@InforField(xpath = "CREATEDDATE")
	private Date date;

	@InforField(xpath = "UNITPRICE")
	private BigDecimal cost;

	@InforField(xpath = "ACTIVITYID/WORKORDERID/JOBNUM")
	private String workOrderNumber;

	@InforField(xpath = "WOADDITIONALCOSTQTY")
	private BigDecimal quantity;

	public String getCostDescription() {
		return costDescription;
	}
	public void setCostDescription(String costDescription) {
		this.costDescription = costDescription;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getWorkOrderNumber() {
		return workOrderNumber;
	}
	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	@XmlJavaTypeAdapter(BigIntegerAdapter.class)
	public BigInteger getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(BigInteger activityCode) {
		this.activityCode = activityCode;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	
}
