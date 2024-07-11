package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.Arrays;

public class Location implements Serializable {
	private static final long serialVersionUID = 3553041780598655021L;

	@Id
	@Column(name = "OBJ_CODE")
	@EAMField(xpath = "LOCATIONID/LOCATIONCODE")
	private String code;

	@Column(name = "OBJ_DESC")
	@EAMField(xpath = "LOCATIONID/DESCRIPTION")
	private String description;

	@Transient
	@EAMField(xpath = "USERDEFINEDAREA")
	private CustomField[] customFields;

	@Transient
	@EAMField(xpath = "DEPARTMENTID/DEPARTMENTCODE")
	private String departmentCode;

	@Transient
	@EAMField(xpath = "DEPARTMENTID/DESCRIPTION")
	private String departmentDesc;

	@Transient
	@EAMField(xpath = "CLASSID/CLASSCODE")
	private String classCode;
	@Transient
	@EAMField(xpath = "CLASSID/DESCRIPTION", readOnly = true)
	private String classDesc;

	@Transient
	@EAMField(xpath = "SAFETY", booleanType = BooleanType.TRUE_FALSE)
	private Boolean safety;

	@Transient
	@EAMField(xpath = "OUTOFSERVICE", booleanType = BooleanType.TRUE_FALSE)
	private Boolean outOfService;

	@Transient
	@EAMField(xpath = "COSTCODEID/COSTCODE")
	private String costCode;

	@Transient
	@EAMField(xpath = "UserDefinedFields")
	private UserDefinedFields userDefinedFields;

	@Transient
	private String hierarchyLocationCode;

	@Transient
	private String hierarchyLocationDesc;

	@Transient
	private String copyFrom;

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
	@XmlElementWrapper(name="customFields") 
    @XmlElement(name="customField")
	public CustomField[] getCustomFields() {
		return customFields;
	}
	public void setCustomFields(CustomField[] customFields) {
		this.customFields = customFields;
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
	public Boolean getSafety() {
		return safety;
	}
	public void setSafety(Boolean safety) {
		this.safety = safety;
	}
	public Boolean getOutOfService() {
		return outOfService;
	}
	public void setOutOfService(Boolean outOfService) {
		this.outOfService = outOfService;
	}
	public String getCostCode() {
		return costCode;
	}
	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}
	public UserDefinedFields getUserDefinedFields() {
		return userDefinedFields;
	}
	public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}

	public String getHierarchyLocationCode() {
		return hierarchyLocationCode;
	}

	public void setHierarchyLocationCode(String hierarchyLocationCode) {
		this.hierarchyLocationCode = hierarchyLocationCode;
	}

	public String getHierarchyLocationDesc() {
		return hierarchyLocationDesc;
	}

	public void setHierarchyLocationDesc(String hierarchyLocationDesc) {
		this.hierarchyLocationDesc = hierarchyLocationDesc;
	}

	public String getCopyFrom() {
		return copyFrom;
	}

	public void setCopyFrom(String copyFrom) {
		this.copyFrom = copyFrom;
	}

	@Override
	public String toString() {
		return "Location ["
				+ (code != null ? "code=" + code + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (customFields != null ? "customFields="
					+ Arrays.toString(customFields) + ", " : "")
				+ (departmentCode != null ? "departmentCode=" + departmentCode + ", " : "")
				+ (departmentDesc != null ? "departmentDesc=" + departmentDesc + ", " : "")
				+ (classCode != null ? "classCode=" + classCode + ", " : "")
				+ (classDesc != null ? "classDesc=" + classDesc + ", " : "")
				+ (safety != null ? "safety=" + safety + ", " : "")
				+ (outOfService != null ? "outOfService=" + outOfService + ", " : "")
				+ (costCode != null ? "costCode=" + costCode + ", " : "")
				+ (userDefinedFields != null ? "userDefinedFields=" + userDefinedFields + ", " : "")
				+ (copyFrom != null ? "copyFrom=" + copyFrom : "") + "]";
	}
}
