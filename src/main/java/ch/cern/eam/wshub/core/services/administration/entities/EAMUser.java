package ch.cern.eam.wshub.core.services.administration.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.Department;
import ch.cern.eam.wshub.core.services.entities.LocaleInfo;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "R5USERS")
@NamedQueries({
		@NamedQuery(name = EAMUser.ALL_ACTIVE_USERS, query = "Select user from EAMUser user WHERE user.classCode IS NULL OR user.classCode <> '*INA'"),
		@NamedQuery(name = EAMUser.ALL_GROUP_MEMBER_USERNAMES, query = "Select userCode from EAMUser user WHERE user.userGroup = :group") })
public class EAMUser implements Serializable {

	private static final long serialVersionUID = 1149632956701915140L;
	public static final String ALL_ACTIVE_USERS = "ALL_ACTIVE_USERS";
	public static final String ALL_GROUP_MEMBER_USERNAMES = "ALL_GROUP_MEMBER_USERNAMES";

	@Id
	@Column(name = "USR_CODE")
	@InforField(xpath = "USERID/USERCODE")
	private String userCode;

	@Column(name = "USR_DESC")
	@InforField(xpath = "USERID/DESCRIPTION")
	private String userDesc;

	@Column(name = "USR_GROUP")
	@InforField(xpath = "USERGROUP")
	private String userGroup;

	@Column(name = "USR_EXPUSER")
	@Temporal(TemporalType.DATE)
	@InforField(xpath = "USEREXPIREDATE")
	private Date userIDExpirationDate;

	@XmlTransient
	@Transient
	@InforField(xpath = "PASSWORD")
	private String password;

	@Transient
	@InforField(xpath = "PASSEXPIREDATE")
	private Date passwordExpirationDate;

	@Column(name = "USR_EMAILADDRESS")
	@InforField(xpath = "EMAIL")
	private String emailAddress;

	@Column(name = "USR_MRC")
	@InforField(xpath = "DEPARTMENTCODE")
	private String department;

	@Column(name = "USR_CLASS")
	@InforField(xpath = "CLASSID/CLASSCODE")
	private String classCode;

	@Transient
	private String employeeCode;

	@Transient
	private String employeeDesc;

	@Transient
	@InforField(xpath = "USERDEFINEDAREA")
	private CustomField[] customFields;

	@Transient
	@InforField(xpath = "StandardUserDefinedFields")
	private UserDefinedFields userDefinedFields;

	@Transient
	@InforField(xpath = "LOCALE")
	private String localeCode;

	@Transient
	private LocaleInfo localeInfo;

	@Transient
	private Map<String, Department> departmentalSecurity;

	public LocaleInfo getLocaleInfo() {
		return localeInfo;
	}

	public void setLocaleInfo(LocaleInfo localeInfo) {
		this.localeInfo = localeInfo;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserDesc() {
		return userDesc;
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@XmlElementWrapper(name = "customFields")
	@XmlElement(name = "customField")
	public CustomField[] getCustomFields() {
		return customFields;
	}

	public void setCustomFields(CustomField[] customFields) {
		this.customFields = customFields;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUserIDExpirationDate() {
		return userIDExpirationDate;
	}

	public void setUserIDExpirationDate(Date userIDExpirationDate) {
		this.userIDExpirationDate = userIDExpirationDate;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getPasswordExpirationDate() {
		return passwordExpirationDate;
	}

	public void setPasswordExpirationDate(Date passwordExpirationDate) {
		this.passwordExpirationDate = passwordExpirationDate;
	}

	public List<String> getUserDepartments() {
		List<String> userDepartments = new LinkedList<>();

		if (getDepartment() != null) {
			userDepartments.add(getDepartment());
		}

		if (getUserDefinedFields() != null && getUserDefinedFields().getUdfchar10() != null) {
			String[] udfDepartments = getUserDefinedFields().getUdfchar10().replaceAll("\\s+", "").trim().split(",");
			userDepartments.addAll(Arrays.asList(udfDepartments));
		}
		Collections.sort(userDepartments);
		return userDepartments;
	}

	/**
	 * @return the userDefinedFields
	 */
	public UserDefinedFields getUserDefinedFields() {
		return userDefinedFields;
	}

	/**
	 * @param userDefinedFields
	 *            the userDefinedFields to set
	 */
	public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeDesc() {
		return employeeDesc;
	}

	public void setEmployeeDesc(String employeeDesc) {
		this.employeeDesc = employeeDesc;
	}

	public Map<String, Department> getDepartmentalSecurity() {
		return departmentalSecurity;
	}

	public void setDepartmentalSecurity(Map<String, Department> departmentalSecurity) {
		this.departmentalSecurity = departmentalSecurity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EAMUser [" + (userCode != null ? "userCode=" + userCode + ", " : "")
				+ (userDesc != null ? "userDesc=" + userDesc + ", " : "")
				+ (userGroup != null ? "userGroup=" + userGroup + ", " : "")
				+ (userIDExpirationDate != null ? "userIDExpirationDate=" + userIDExpirationDate + ", " : "")
				+ (password != null ? "password=" + password + ", " : "")
				+ (passwordExpirationDate != null ? "passwordExpirationDate=" + passwordExpirationDate + ", " : "")
				+ (emailAddress != null ? "emailAddress=" + emailAddress + ", " : "")
				+ (department != null ? "department=" + department + ", " : "")
				+ (classCode != null ? "classCode=" + classCode + ", " : "")
				+ (customFields != null ? "customFields=" + Arrays.toString(customFields) + ", " : "")
				+ (userDefinedFields != null ? "userDefinedFields=" + userDefinedFields : "") + "]";
	}

}
