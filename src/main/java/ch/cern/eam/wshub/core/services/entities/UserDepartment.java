/**
 * 
 */
package ch.cern.eam.wshub.core.services.entities;

import javax.persistence.*;

/**
 * Departments of a user
 * 
 * @author jmesapol
 *
 */
@Entity
@Table(name = "U5USERDEPARTMENTS")
@IdClass(UserDepartmentPK.class)
@NamedQuery(name = UserDepartment.GET_USER_DEPARTMENTS, query = "SELECT dep FROM UserDepartment dep WHERE "
		+ "dep.userId = :userId and dep.outOfService <> '+'")
public class UserDepartment {

	public static final String GET_USER_DEPARTMENTS = "UserDepartment.GET_USER_DEPARTMENTS";

	@Id
	@Column(name = "UDE_CODE")
	private String userId;
	@Id
	@Column(name = "UDE_MRC")
	private String mrcCode;

	@Column(name = "UDE_NOTUSED")
	private String outOfService;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the mrcCode
	 */
	public String getMrcCode() {
		return mrcCode;
	}

	/**
	 * @param mrcCode
	 *            the mrcCode to set
	 */
	public void setMrcCode(String mrcCode) {
		this.mrcCode = mrcCode;
	}

	/**
	 * @return the outOfService
	 */
	public String getOutOfService() {
		return outOfService;
	}

	/**
	 * @param outOfService
	 *            the outOfService to set
	 */
	public void setOutOfService(String outOfService) {
		this.outOfService = outOfService;
	}

}
