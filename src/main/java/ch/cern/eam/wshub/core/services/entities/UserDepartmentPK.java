package ch.cern.eam.wshub.core.services.entities;

import java.io.Serializable;

/**
 * Represents the primary key of the table U5USERDEPARTMENTS
 * 
 * @author jmesapol
 *
 */
public class UserDepartmentPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Id of the user
	 */
	private String userId;

	/**
	 * Code of the department
	 */
	private String mrcCode;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mrcCode == null) ? 0 : mrcCode.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserDepartmentPK))
			return false;
		UserDepartmentPK other = (UserDepartmentPK) obj;
		if (mrcCode == null) {
			if (other.mrcCode != null)
				return false;
		} else if (!mrcCode.equals(other.mrcCode))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}