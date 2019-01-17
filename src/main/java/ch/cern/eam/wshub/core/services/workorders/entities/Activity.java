package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@Entity
@NamedNativeQuery(name = "FINDACT", query = "select * from R5ACTIVITIES LEFT OUTER JOIN R5TASKS ON R5ACTIVITIES.ACT_TASK = R5TASKS.TSK_CODE WHERE ACT_EVENT = :workOrder ORDER BY ACT_ACT", resultClass = Activity.class)
@Table(name = "R5ACTIVITIES")
public class Activity implements Serializable {

	@Transient
	private static final long serialVersionUID = 2747408324574455910L;

	@Id
	@Column(name = "ACT_ACT")
	private String activityCode;
	@Column(name = "ACT_EVENT")
	private String workOrderNumber;
	@Column(name = "ACT_PERSONS")
	private String peopleRequired;
	@Column(name = "ACT_EST")
	private String estimatedHours;
	@Column(name = "ACT_REM")
	private String hoursRemaining;
	@Column(name = "ACT_START")
	private Date startDate;
	@Transient
	private Date endDate;
	@Column(name = "ACT_MATLIST")
	private String materialList;
	@Column(name = "ACT_TASK")
	private String taskCode;
	@Column(name = "TSK_DESC")
	private String taskDesc;
	@Column(name = "ACT_TRADE")
	private String tradeCode;
	@Column(name = "ACT_QTY")
	private String taskQty;

	@Transient
	private WorkOrderActivityCheckList[] checklists;

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public String getPeopleRequired() {
		return peopleRequired;
	}

	public void setPeopleRequired(String peopleRequired) {
		this.peopleRequired = peopleRequired;
	}

	public String getEstimatedHours() {
		return estimatedHours;
	}

	public void setEstimatedHours(String estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getMaterialList() {
		return materialList;
	}

	public void setMaterialList(String materialList) {
		this.materialList = materialList;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getHoursRemaining() {
		return hoursRemaining;
	}

	public void setHoursRemaining(String hoursRemaining) {
		this.hoursRemaining = hoursRemaining;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public WorkOrderActivityCheckList[] getChecklists() {
		return checklists;
	}

	public void setChecklists(WorkOrderActivityCheckList[] checklists) {
		this.checklists = checklists;
	}

	public String getTaskQty() {
		return taskQty;
	}

	public void setTaskQty(String taskQty) {
		this.taskQty = taskQty;
	}

	@Override
	public String toString() {
		return "Activity{" +
				"activityCode='" + activityCode + '\'' +
				", workOrderNumber='" + workOrderNumber + '\'' +
				", peopleRequired='" + peopleRequired + '\'' +
				", estimatedHours='" + estimatedHours + '\'' +
				", hoursRemaining='" + hoursRemaining + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", materialList='" + materialList + '\'' +
				", taskCode='" + taskCode + '\'' +
				", taskDesc='" + taskDesc + '\'' +
				", tradeCode='" + tradeCode + '\'' +
				", taskQty='" + taskQty + '\'' +
				", checklists=" + Arrays.toString(checklists) +
				'}';
	}
}
