package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.GridField;
import ch.cern.eam.wshub.core.annotations.InforField;

import javax.persistence.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.Date;

@Entity
@Table(name = "R5BOOKEDHOURS")
@NamedNativeQuery(name = LaborBooking.GETBOOKEDLABOR, query = "select r5bookedhours.*, r5personnel.*, boo_date from r5bookedhours, r5personnel where r5bookedhours.boo_person = r5personnel.per_code and boo_event = :workOrder", resultClass = LaborBooking.class)
public class LaborBooking implements Serializable, Comparable<LaborBooking> {

	private static final long serialVersionUID = -7876074587300796418L;

	public static final String GETBOOKEDLABOR = "LaborBooking.GETBOOKEDLABOR";

	@Id
	@Column(name = "BOO_CODE")
	private String code;

	@Column(name = "BOO_OCRTYPE")
	@InforField(xpath = "OCCUPATIONTYPE/OCCUPATIONTYPECODE")
	@GridField(name="octype")
	private String typeOfHours;

	@Column(name = "BOO_HOURS")
	@InforField(xpath = "HOURSBOOKED")
	@GridField(name="hours")
	private BigDecimal hoursWorked;

	@Column(name = "BOO_DATE")
	@InforField(xpath = "DATEWORKED")
	@GridField(name="boodate")
	private Date dateWorked;

	@Column(name = "BOO_MRC")
	@InforField(xpath = "DEPARTMENTID/DEPARTMENTCODE")
	@GridField(name="department")
	private String departmentCode;

	@Column(name = "BOO_PERSON")
	@InforField(xpath = "EMPLOYEE/PERSONCODE")
	@GridField(name="employee")
	private String employeeCode;

	@Column(name = "PER_DESC")
	@InforField(xpath = "EMPLOYEE/PERSONDESC")
	@GridField(name="employeedesc")
	private String employeeDesc;

	@Column(name = "BOO_ACT")
	@InforField(xpath = "ACTIVITYID/ACTIVITYCODE/Content")
	@GridField(name="booactivity")
	private String activityCode;

	@Column(name = "BOO_ON")
	@InforField(xpath = "ACTUALSTARTTIME")
	@GridField(name="actstarttime")
	private BigInteger startTime;

	@Column(name = "BOO_OFF")
	@InforField(xpath = "ACTUALENDTIME")
	@GridField(name="actendtime")
	private BigInteger endTime;

	public BigInteger getStartTime() {
		return startTime;
	}

	public void setStartTime(BigInteger startTime) {
		this.startTime = startTime;
	}

	public BigInteger getEndTime() {
		return endTime;
	}

	public void setEndTime(BigInteger endTime) {
		this.endTime = endTime;
	}

	@Column(name = "BOO_TRADE")
	@GridField(name="emptrade")
	private String tradeCode;

	@Column(name = "BOO_EVENT")
	@GridField(name="event")
	private String workOrderNumber;

	public String getTypeOfHours() {
		return typeOfHours;
	}

	public void setTypeOfHours(String typeOfHours) {
		this.typeOfHours = typeOfHours;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getHoursWorked() {
		return hoursWorked;
	}

	public void setHoursWorked(BigDecimal hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)

	public Date getDateWorked() {
		return dateWorked;
	}

	public void setDateWorked(Date dateWorked) {
		this.dateWorked = dateWorked;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
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

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	@Override
	public String toString() {
		return "LaborBooking [" + (typeOfHours != null ? "typeOfHours=" + typeOfHours + ", " : "")
				+ (hoursWorked != null ? "hoursWorked=" + hoursWorked + ", " : "")
				+ (dateWorked != null ? "dateWorked=" + dateWorked + ", " : "")
				+ (departmentCode != null ? "departmentCode=" + departmentCode + ", " : "")
				+ (employeeCode != null ? "employeeCode=" + employeeCode + ", " : "")
				+ (activityCode != null ? "activityCode=" + activityCode + ", " : "")
				+ (tradeCode != null ? "tradeCode=" + tradeCode + ", " : "")
				+ (workOrderNumber != null ? "workOrderNumber=" + workOrderNumber : "") + "]";
	}

	public String getEmployeeDesc() {
		return employeeDesc;
	}

	public void setEmployeeDesc(String employeeDesc) {
		this.employeeDesc = employeeDesc;
	}

	@Override
	public int compareTo(LaborBooking o) {
		return Comparator.comparing(LaborBooking::getActivityCode).thenComparing(LaborBooking::getDateWorked)
				.thenComparing(LaborBooking::getEmployeeCode).compare(this, o);
	}
}
