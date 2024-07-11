package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.BigIntegerAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.GridField;
import ch.cern.eam.wshub.core.annotations.EAMField;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Entity
@NamedNativeQuery(name = "FINDACT", query = "select * from R5ACTIVITIES LEFT OUTER JOIN R5TASKS ON R5ACTIVITIES.ACT_TASK = R5TASKS.TSK_CODE WHERE ACT_EVENT = :workOrder ORDER BY ACT_ACT", resultClass = Activity.class)
@Table(name = "R5ACTIVITIES")
public class Activity implements Serializable {

	@Transient
	private static final long serialVersionUID = 2747408324574455910L;

	@Id
	@Column(name = "ACT_ACT")
	@GridField(name="activity")
	@EAMField(xpath = "ACTIVITYID/ACTIVITYCODE/value")
	private BigInteger activityCode;

	@Column(name = "ACT_NOTE")
	@GridField(name="activitynote")
	@EAMField(xpath = "ACTIVITYID/ACTIVITYNOTE", nullifyParentLevel = 0)
	private String activityNote;

	@Column(name = "ACT_EVENT")
	@GridField(name="workordernum")
	@EAMField(xpath = "ACTIVITYID/WORKORDERID/JOBNUM")
	private String workOrderNumber;

	@Column(name = "ACT_PERSONS")
	@GridField(name="personsreq")
	@EAMField(xpath = "PERSONS")
	private BigInteger peopleRequired;

	@Column(name = "ACT_EST")
	@GridField(name="esthrs")
	@EAMField(xpath = "ESTIMATEDHOURS")
	private BigDecimal estimatedHours;

	@Column(name = "ACT_REM")
	@GridField(name="hrsremain")
	@EAMField(xpath = "HOURSREMAINING")
	private BigDecimal hoursRemaining;

	@Column(name = "ACT_START")
	@GridField(name="actstartdate")
	@EAMField(xpath = "ACTIVITYSTARTDATE")
	private Date startDate;

	@Transient
	@GridField(name="actenddate")
	@EAMField(xpath = "ACTIVITYENDDATE")
	private Date endDate;

	@Column(name = "ACT_MATLIST")
	@GridField(name="matlcode")
	@EAMField(xpath = "MATLIST/MTLCODE")
	private String materialList;

	@Column(name = "ACT_TASK")
	@GridField(name="task")
	@EAMField(xpath = "TASKSID/TASKCODE")
	private String taskCode;

	@Column(name = "TSK_DESC")
	@EAMField(xpath = "TASKSID/DESCRIPTION")
	private String taskDesc;

	@Column(name = "TSK_REVISION")
	@GridField(name = "taskrev")
	private String taskRev;

	@Column(name = "ACT_TRADE")
	@GridField(name="trade")
	@EAMField(xpath = "TRADEID/TRADECODE")
	private String tradeCode;

	@Column(name = "ACT_QTY")
	@GridField(name="taskqty")
	@EAMField(xpath = "TASKSID/TASKQUANTITY")
	private BigDecimal taskQty;

	@Transient
	private WorkOrderActivityCheckList[] checklists;

	@Transient
	private Map<String, WorkOrderActivityChecklistSignatureResult> signatures;

	@Transient
	private Boolean forceActivityExpansion;

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	@XmlJavaTypeAdapter(BigIntegerAdapter.class)
	public BigInteger getPeopleRequired() {
		return peopleRequired;
	}

	public void setPeopleRequired(BigInteger peopleRequired) {
		this.peopleRequired = peopleRequired;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getEstimatedHours() {
		return estimatedHours;
	}

	public void setEstimatedHours(BigDecimal estimatedHours) {
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

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getHoursRemaining() {
		return hoursRemaining;
	}

	public void setHoursRemaining(BigDecimal hoursRemaining) {
		this.hoursRemaining = hoursRemaining;
	}

	@XmlJavaTypeAdapter(BigIntegerAdapter.class)
	public BigInteger getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(BigInteger activityCode) {
		this.activityCode = activityCode;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getTaskRev() {
		return taskRev;
	}

	public void setTaskRev(String taskRev) {
		this.taskRev = taskRev;
	}

	public WorkOrderActivityCheckList[] getChecklists() {
		return checklists;
	}

	public void setChecklists(WorkOrderActivityCheckList[] checklists) {
		this.checklists = checklists;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getTaskQty() {
		return taskQty;
	}

	public void setTaskQty(BigDecimal taskQty) {
		this.taskQty = taskQty;
	}

	public String getActivityNote() {return activityNote;}

	public void setActivityNote(String activityNote) {this.activityNote = activityNote; }

	public Map<String, WorkOrderActivityChecklistSignatureResult> getSignatures() {
		return signatures;
	}

	public void setSignatures(Map<String, WorkOrderActivityChecklistSignatureResult> signatures) {
		this.signatures = signatures;
	}

	public Boolean getForceActivityExpansion() {
		return forceActivityExpansion;
	}

	public void setForceActivityExpansion(Boolean forceActivityExpansion) {
		this.forceActivityExpansion = forceActivityExpansion;
	}

	@Override
	public String toString() {
		return "Activity{" +
				"activityCode='" + activityCode + '\'' +
				", activityNote='" + activityNote + '\'' +
				", workOrderNumber='" + workOrderNumber + '\'' +
				", peopleRequired='" + peopleRequired + '\'' +
				", estimatedHours='" + estimatedHours + '\'' +
				", hoursRemaining='" + hoursRemaining + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", materialList='" + materialList + '\'' +
				", taskCode='" + taskCode + '\'' +
				", taskDesc='" + taskDesc + '\'' +
				", taskRev='" + taskRev + '\'' +
				", tradeCode='" + tradeCode + '\'' +
				", taskQty='" + taskQty + '\'' +
				", checklists=" + Arrays.toString(checklists) +
				"signatures=" + signatures +
				'}';
	}

}
