package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@Table(name = "R5CASEMANAGEMENT")
public class InforCase implements Serializable, Cloneable {
	private static final long serialVersionUID = 2632244342851353370L;

	@Id
	@Column(name = "CSM_CODE")
	private String code;

	@Column(name = "CSM_DESC")
	private String description;

	@Column(name = "CSM_CLASS")
	private String classCode;

	@Transient
	private String classDesc;

	@Transient
	private String equipmentCode;
	@Transient
	private String equipmentDesc;
	@Transient
	private String typeCode;
	@Transient
	private String typeDesc;
	@Transient
	private String departmentCode;
	@Transient
	private String departmentDesc;
	@Transient
	private String statusCode;
	@Transient
	private String statusDesc;
	@Transient
	private String locationCode;
	@Transient
	private String locationDesc;
	@Transient
	private String workaddress;

	@Transient
	private String responsibleCode;
	@Transient
	private String responsibleDesc;
	@Transient
	private String responsibleEMail;
	@Transient
	private String assignedToCode;
	@Transient
	private String assignedToDesc;
	@Transient
	private String assignedToEMail;
	@Transient
	private String createdBy;
	@Transient
	private String createdByName;
	@Transient
	private String updatedBy;
	@Transient
	private String updatedByName;
	@Transient
	private String priority;
	
	//
	// SCHEDULING
	//
	@Transient
	private Date scheduledStartDate;

	@Transient
	private Date scheduledEndDate;

	@Transient
	private Date requestedStartDate;

	@Transient
	private Date requestedEndDate;

	@Transient
	private Date startDate;

	@Transient
	private Date completedDate;

	@Transient
	private Date createDate;

	@Transient
	private Date updatedDate;

	@Transient
	private Date daterequested;
	@Transient
	private Date eventstartdate;

	@Transient
	private Date eventenddate;

	@Transient
	@InforField(xpath = "USERDEFINEDAREA")
	private CustomField[] customFields;

	@Transient
	@InforField(xpath = "StandardUserDefinedFields")
	private UserDefinedFields userDefinedFields;

	//
	// Counters
	//
	@Transient
	private int needsDone;
	@Transient
	private int needsUndone;
	@Transient
	private int needs;
	@Transient
	private int links;
	@Transient
	private int comments;
	@Transient
	private int tasks;
	@Transient
	private int uncompletedTasks;
	@Transient
	private int completedTasks;
	
	//
	// Integration study need - dependency
	//
	@Transient
	private String intStudyNeedDetails;
	@Transient
	private String intStudyNeedReqFlag;
	
	//
	// EDMS docs need - dependency
	//
	@Transient
	private String docecrNeedDetails;
	@Transient
	private String docecrNeedReqFlag;
	@Transient
	private String docsrrNeedDetails;
	@Transient
	private String docsrrNeedReqFlag;
	
	public InforCase copy() {
		try {
			InforCase clone = (InforCase) this.clone();
			if (this.userDefinedFields != null) {
				clone.setUserDefinedFields(this.userDefinedFields.copy());
			}
			if (this.getScheduledEndDate() != null) {
				clone.setScheduledEndDate(new Date(this.getScheduledEndDate().getTime()));
			}
			if (this.getScheduledStartDate() != null) {
				clone.setScheduledStartDate(new Date(this.getScheduledStartDate().getTime()));
			}
			if (this.getRequestedStartDate() != null) {
				clone.setRequestedStartDate(new Date(this.getRequestedStartDate().getTime()));
			}
			if (this.getRequestedEndDate() != null) {
				clone.setRequestedEndDate(new Date(this.getRequestedEndDate().getTime()));
			}
			if (this.getStartDate() != null) {
				clone.setStartDate(new Date(this.getStartDate().getTime()));
			}
			if (this.getCompletedDate() != null) {
				clone.setCompletedDate(new Date(clone.getCompletedDate().getTime()));
			}
			if (this.getCreateDate() != null) {
				clone.setCreateDate(new Date(this.getCreateDate().getTime()));
			}
			if (this.getUpdatedDate() != null) {
				clone.setUpdatedDate(new Date(this.getUpdatedDate().getTime()));
			}
			if (this.getDaterequested() != null) {
				clone.setDaterequested(new Date(this.getDaterequested().getTime()));
			}
			if (this.getEventstartdate() != null) {
				clone.setEventstartdate(new Date(this.getEventstartdate().getTime()));
			}
			if (this.getEventenddate() != null) {
				clone.setEventenddate(new Date(this.getEventenddate().getTime()));
			}
			
			if (this.customFields != null) {
				clone.customFields = new CustomField[this.customFields.length];
				for (int i = 0 ; i < this.customFields.length ; i++) {
					clone.customFields[i] = this.customFields[i];
				}
			}
			
			return clone;
		} catch (CloneNotSupportedException e) {
//			tools.log(Level.SEVERE,"Error during cloning ", e);
			return null;
		}
	}
	
	/**
	 * Update count to prevent case from parallel edition
	 */
	@Transient
	private long updateCount;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public String getEquipmentDesc() {
		return equipmentDesc;
	}

	public void setEquipmentDesc(String equipmentDesc) {
		this.equipmentDesc = equipmentDesc;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentDesc() {
		return departmentDesc;
	}

	public void setDepartmentDesc(String departmentDesc) {
		this.departmentDesc = departmentDesc;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getResponsibleCode() {
		return responsibleCode;
	}

	public void setResponsibleCode(String responsibleCode) {
		this.responsibleCode = responsibleCode;
	}

	public String getResponsibleDesc() {
		return responsibleDesc;
	}

	public void setResponsibleDesc(String responsibleDesc) {
		this.responsibleDesc = responsibleDesc;
	}

	public String getResponsibleEMail() {
		return responsibleEMail;
	}

	public void setResponsibleEMail(String responsibleEMail) {
		this.responsibleEMail = responsibleEMail;
	}

	public String getAssignedToCode() {
		return assignedToCode;
	}

	public void setAssignedToCode(String assignedToCode) {
		this.assignedToCode = assignedToCode;
	}

	public String getAssignedToDesc() {
		return assignedToDesc;
	}

	public void setAssignedToDesc(String assignedToDesc) {
		this.assignedToDesc = assignedToDesc;
	}

	public String getAssignedToEMail() {
		return assignedToEMail;
	}

	public void setAssignedToEMail(String assignedToEMail) {
		this.assignedToEMail = assignedToEMail;
	}

	@XmlElementWrapper(name = "customFields")
	@XmlElement(name = "customField")
	public CustomField[] getCustomFields() {
		return customFields;
	}

	public void setCustomFields(CustomField[] customFields) {
		this.customFields = customFields;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocationDesc() {
		return locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getScheduledStartDate() {
		return scheduledStartDate;
	}

	public void setScheduledStartDate(Date scheduledStartDate) {
		this.scheduledStartDate = scheduledStartDate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getScheduledEndDate() {
		return scheduledEndDate;
	}

	public void setScheduledEndDate(Date scheduledEndDate) {
		this.scheduledEndDate = scheduledEndDate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getRequestedStartDate() {
		return requestedStartDate;
	}

	public void setRequestedStartDate(Date requestedStartDate) {
		this.requestedStartDate = requestedStartDate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getRequestedEndDate() {
		return requestedEndDate;
	}

	public void setRequestedEndDate(Date requestedEndDate) {
		this.requestedEndDate = requestedEndDate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUpdatedDate() {
		return updatedDate;
	}
	
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getDaterequested() {
		return daterequested;
	}
	
	public void setDaterequested(Date daterequested) {
		this.daterequested = daterequested;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getEventenddate() {
		return eventenddate;
	}
	
	public void setEventenddate(Date eventenddate) {
		this.eventenddate = eventenddate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getEventstartdate() {
		return eventstartdate;
	}
	
	public void setEventstartdate(Date eventstartdate) {
		this.eventstartdate = eventstartdate;
	}

	public UserDefinedFields getUserDefinedFields() {
		if(userDefinedFields == null)
			userDefinedFields = new UserDefinedFields();
		return userDefinedFields;
	}

	public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = classDesc;
	}

	public int getNeedsDone() {
		return needsDone;
	}

	public void setNeedsDone(int needsDone) {
		this.needsDone = needsDone;
	}

	public int getNeedsUndone() {
		return needsUndone;
	}

	public void setNeedsUndone(int needsUndone) {
		this.needsUndone = needsUndone;
	}

	public int getNeeds() {
		return needs;
	}

	public void setNeeds(int needs) {
		this.needs = needs;
	}

	public int getLinks() {
		return links;
	}

	public void setLinks(int links) {
		this.links = links;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getTasks() {
		return tasks;
	}

	public void setTasks(int tasks) {
		this.tasks = tasks;
	}

	public int getUncompletedTasks() {
		return uncompletedTasks;
	}

	public void setUncompletedTasks(int uncompletedTasks) {
		this.uncompletedTasks = uncompletedTasks;
	}

	public int getCompletedTasks() {
		return completedTasks;
	}

	public void setCompletedTasks(int completedTasks) {
		this.completedTasks = completedTasks;
	}
	
	public String getWorkaddress() {
		return workaddress;
	}
	
	public void setWorkaddress(String workaddress) {
		this.workaddress = workaddress;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getCreatedByName() {
		return createdByName;
	}
	
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	
	public String getUpdatedBy() {
		return updatedBy;
	}
	
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public String getUpdatedByName() {
		return updatedByName;
	}
	
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	public String getIntStudyNeedDetails() {
		return intStudyNeedDetails;
	}

	public void setIntStudyNeedDetails(String intStudyNeedDetails) {
		this.intStudyNeedDetails = intStudyNeedDetails;
	}

	public String getIntStudyNeedReqFlag() {
		return intStudyNeedReqFlag;
	}

	public void setIntStudyNeedReqFlag(String intStudyNeedReqFlag) {
		this.intStudyNeedReqFlag = intStudyNeedReqFlag;
	}

	public String getDocecrNeedDetails() {
		return docecrNeedDetails;
	}

	public void setDocecrNeedDetails(String docecrNeedDetails) {
		this.docecrNeedDetails = docecrNeedDetails;
	}

	public String getDocecrNeedReqFlag() {
		return docecrNeedReqFlag;
	}

	public void setDocecrNeedReqFlag(String docecrNeedReqFlag) {
		this.docecrNeedReqFlag = docecrNeedReqFlag;
	}

	public String getDocsrrNeedDetails() {
		return docsrrNeedDetails;
	}

	public void setDocsrrNeedDetails(String docsrrNeedDetails) {
		this.docsrrNeedDetails = docsrrNeedDetails;
	}

	public String getDocsrrNeedReqFlag() {
		return docsrrNeedReqFlag;
	}

	public void setDocsrrNeedReqFlag(String docsrrNeedReqFlag) {
		this.docsrrNeedReqFlag = docsrrNeedReqFlag;
	}

	public long getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(long updateCount) {
		this.updateCount = updateCount;
	}
	
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public String getPriority() {
		return priority;
	}

	@Override
	public String toString() {
		return "InforCase [" + (code != null ? "code=" + code + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (classCode != null ? "classCode=" + classCode + ", " : "")
				+ (classDesc != null ? "classDesc=" + classDesc + ", " : "")
				+ (equipmentCode != null ? "equipmentCode=" + equipmentCode + ", " : "")
				+ (equipmentDesc != null ? "equipmentDesc=" + equipmentDesc + ", " : "")
				+ (typeCode != null ? "typeCode=" + typeCode + ", " : "")
				+ (typeDesc != null ? "typeDesc=" + typeDesc + ", " : "")
				+ (departmentCode != null ? "departmentCode=" + departmentCode + ", " : "")
				+ (departmentDesc != null ? "departmentDesc=" + departmentDesc + ", " : "")
				+ (statusCode != null ? "statusCode=" + statusCode + ", " : "")
				+ (statusDesc != null ? "statusDesc=" + statusDesc + ", " : "")
				+ (locationCode != null ? "locationCode=" + locationCode + ", " : "")
				+ (locationDesc != null ? "locationDesc=" + locationDesc + ", " : "")
				+ (workaddress != null ? "workaddress=" + workaddress + ", " : "")
				+ (responsibleCode != null ? "responsibleCode=" + responsibleCode + ", " : "")
				+ (responsibleDesc != null ? "responsibleDesc=" + responsibleDesc + ", " : "")
				+ (responsibleEMail != null ? "responsibleEMail=" + responsibleEMail + ", " : "")
				+ (assignedToCode != null ? "assignedToCode=" + assignedToCode + ", " : "")
				+ (assignedToDesc != null ? "assignedToDesc=" + assignedToDesc + ", " : "")
				+ (assignedToEMail != null ? "assignedToEMail=" + assignedToEMail + ", " : "")
				+ (createdBy != null ? "createdBy=" + createdBy + ", " : "")
				+ (scheduledStartDate != null ? "scheduledStartDate=" + scheduledStartDate + ", " : "")
				+ (scheduledEndDate != null ? "scheduledEndDate=" + scheduledEndDate + ", " : "")
				+ (requestedStartDate != null ? "requestedStartDate=" + requestedStartDate + ", " : "")
				+ (requestedEndDate != null ? "requestedEndDate=" + requestedEndDate + ", " : "")
				+ (startDate != null ? "startDate=" + startDate + ", " : "")
				+ (completedDate != null ? "completedDate=" + completedDate + ", " : "")
				+ (createDate != null ? "createDate=" + createDate + ", " : "")
				+ (daterequested != null ? "daterequested=" + daterequested + ", " : "")
				+ (eventstartdate != null ? "eventstartdate=" + eventstartdate + ", " : "")
				+ (eventenddate != null ? "eventenddate=" + eventenddate + ", " : "")
				+ (customFields != null ? "customFields=" + Arrays.toString(customFields) + ", " : "")
				+ (userDefinedFields != null ? "userDefinedFields=" + userDefinedFields + ", " : "") + "needsDone="
				+ needsDone + ", needsUndone=" + needsUndone + ", needs=" + needs + ", links=" + links + ", comments="
				+ comments + ", tasks=" + tasks + ", uncompletedTasks=" + uncompletedTasks + ", completedTasks="
				+ completedTasks + ", "
				+ (intStudyNeedDetails != null ? "intStudyNeedDetails=" + intStudyNeedDetails + ", " : "")
				+ (intStudyNeedReqFlag != null ? "intStudyNeedReqFlag=" + intStudyNeedReqFlag + ", " : "")
				+ (docecrNeedDetails != null ? "docecrNeedDetails=" + docecrNeedDetails + ", " : "")
				+ (docecrNeedReqFlag != null ? "docecrNeedReqFlag=" + docecrNeedReqFlag + ", " : "")
				+ (docsrrNeedDetails != null ? "docsrrNeedDetails=" + docsrrNeedDetails + ", " : "")
				+ (docsrrNeedReqFlag != null ? "docsrrNeedReqFlag=" + docsrrNeedReqFlag + ", " : "") + "updateCount="
				+ updateCount + "]";
	}
}
