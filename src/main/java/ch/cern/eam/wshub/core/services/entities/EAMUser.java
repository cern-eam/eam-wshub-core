package ch.cern.eam.wshub.core.services.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
	private String userCode;
	@Column(name = "USR_DESC")
	private String userDesc;
	@Column(name = "USR_GROUP")
	private String userGroup;
	@Column(name = "USR_EXPUSER")
	@Temporal(TemporalType.DATE)
	private Date userIDExpirationDate;
	@Transient
	private String password;
	@Transient
	private Date passwordExpirationDate;
	@Column(name = "USR_EMAILADDRESS")
	private String emailAddress;
	@Column(name = "USR_MRC")
	private String department;
	@Column(name = "USR_CLASS")
	private String classCode;
	@Transient
	private CustomField[] customFields;
	@Column(name = "USR_UDFCHAR01")
	private String udfchar01;
	@Column(name = "USR_UDFCHAR02")
	private String udfchar02;
	@Column(name = "USR_UDFCHAR03")
	private String udfchar03;
	@Column(name = "USR_UDFCHAR04")
	private String udfchar04;
	@Column(name = "USR_UDFCHAR05")
	private String udfchar05;
	@Column(name = "USR_UDFCHAR06")
	private String udfchar06;
	@Column(name = "USR_UDFCHAR07")
	private String udfchar07;
	@Column(name = "USR_UDFCHAR08")
	private String udfchar08;
	@Column(name = "USR_UDFCHAR09")
	private String udfchar09;
	@Transient
	private String cernId;
	@Transient
	private UserDefinedFields userDefinedFields;

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
	 * @return the udfchar01 (Department 01)
	 */
	public String getUdfchar01() {
		return udfchar01;
	}

	/**
	 * @param udfchar01
	 *            the udfchar01 to set
	 */
	public void setUdfchar01(String udfchar01) {
		this.udfchar01 = udfchar01;
	}

	/**
	 * @return the udfchar02 (Department 02)
	 */
	public String getUdfchar02() {
		return udfchar02;
	}

	/**
	 * @param udfchar02
	 *            the udfchar02 to set
	 */
	public void setUdfchar02(String udfchar02) {
		this.udfchar02 = udfchar02;
	}

	/**
	 * @return the udfchar03 (Department 03)
	 */
	public String getUdfchar03() {
		return udfchar03;
	}

	/**
	 * @param udfchar03
	 *            the udfchar03 to set
	 */
	public void setUdfchar03(String udfchar03) {
		this.udfchar03 = udfchar03;
	}

	/**
	 * @return the udfchar04 (Department 04)
	 */
	public String getUdfchar04() {
		return udfchar04;
	}

	/**
	 * @param udfchar04
	 *            the udfchar04 to set
	 */
	public void setUdfchar04(String udfchar04) {
		this.udfchar04 = udfchar04;
	}

	/**
	 * @return the udfchar05 (Default Work Order screen)
	 */
	public String getUdfchar05() {
		return udfchar05;
	}

	/**
	 * @param udfchar05
	 *            the udfchar05 to set
	 */
	public void setUdfchar05(String udfchar05) {
		this.udfchar05 = udfchar05;
	}

	/**
	 * @return the udfchar06 (Default Part Screen)
	 */
	public String getUdfchar06() {
		return udfchar06;
	}

	/**
	 * @param udfchar06
	 *            the udfchar06 to set
	 */
	public void setUdfchar06(String udfchar06) {
		this.udfchar06 = udfchar06;
	}

	/**
	 * @return the udfchar07 (Default Asset Screen)
	 */
	public String getUdfchar07() {
		return udfchar07;
	}

	/**
	 * @param udfchar07
	 *            the udfchar07 to set
	 */
	public void setUdfchar07(String udfchar07) {
		this.udfchar07 = udfchar07;
	}

	/**
	 * @return the udfchar08 (Default Position Screen)
	 */
	public String getUdfchar08() {
		return udfchar08;
	}

	/**
	 * @param udfchar08
	 *            the udfchar08 to set
	 */
	public void setUdfchar08(String udfchar08) {
		this.udfchar08 = udfchar08;
	}

	/**
	 * @return the udfchar09 (Default System screen)
	 */
	public String getUdfchar09() {
		return udfchar09;
	}

	/**
	 * @param udfchar09
	 *            the udfchar09 to set
	 */
	public void setUdfchar09(String udfchar09) {
		this.udfchar09 = udfchar09;
	}

	/**
	 * @return the cernId
	 */
	public String getCernId() {
		return cernId;
	}

	/**
	 * @param cernId
	 *            the cernId to set
	 */
	public void setCernId(String cernId) {
		this.cernId = cernId;
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
				+ (udfchar01 != null ? "udfchar01=" + udfchar01 + ", " : "")
				+ (udfchar02 != null ? "udfchar02=" + udfchar02 + ", " : "")
				+ (udfchar03 != null ? "udfchar03=" + udfchar03 + ", " : "")
				+ (udfchar04 != null ? "udfchar04=" + udfchar04 + ", " : "")
				+ (udfchar05 != null ? "udfchar05=" + udfchar05 + ", " : "")
				+ (udfchar06 != null ? "udfchar06=" + udfchar06 + ", " : "")
				+ (udfchar07 != null ? "udfchar07=" + udfchar07 + ", " : "")
				+ (udfchar08 != null ? "udfchar08=" + udfchar08 + ", " : "")
				+ (udfchar09 != null ? "udfchar09=" + udfchar09 + ", " : "")
				+ (cernId != null ? "cernId=" + cernId + ", " : "")
				+ (userDefinedFields != null ? "userDefinedFields=" + userDefinedFields : "") + "]";
	}

}
