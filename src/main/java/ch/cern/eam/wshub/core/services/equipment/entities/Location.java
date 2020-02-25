package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.Arrays;

public class Location implements Serializable {
	private static final long serialVersionUID = 3553041780598655021L;

	@Id
	@Column(name = "OBJ_CODE")
	@InforField(xpath = "LOCATIONID/LOCATIONCODE")
	private String code;

	@Column(name = "OBJ_DESC")
	@InforField(xpath = "LOCATIONID/DESCRIPTION")
	private String description;

	@Transient
	@InforField(xpath = "USERDEFINEDAREA")
	private CustomField[] customFields;

	@Transient
	@InforField(xpath = "DEPARTMENTID/DEPARTMENTCODE")
	private String departmentCode;

	@Transient
	@InforField(xpath = "DEPARTMENTID/DESCRIPTION")
	private String departmentDesc;

	@Transient
	@InforField(xpath = "CLASSID/CLASSCODE")
	private String classCode;
	@Transient
	@InforField(xpath = "CLASSID/DESCRIPTION", readOnly = true)
	private String classDesc;

	@Transient
	@InforField(xpath = "SAFETY", booleanType = BooleanType.TRUE_FALSE)
	private Boolean safety;

	@Transient
	@InforField(xpath = "OUTOFSERVICE", booleanType = BooleanType.TRUE_FALSE)
	private Boolean outOfService;

	@Transient
	@InforField(xpath = "COSTCODEID/COSTCODE")
	private String costCode;

	@Transient
	@InforField(xpath = "UserDefinedFields")
	private UserDefinedFields userDefinedFields;

	@Transient
	private String hierarchyLocationCode;

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
				+ (userDefinedFields != null ? "userDefinedFields=" + userDefinedFields : "")
				+ "]";
	}
}
