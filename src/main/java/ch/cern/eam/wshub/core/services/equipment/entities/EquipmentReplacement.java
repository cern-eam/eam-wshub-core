/**
 * 
 */
package ch.cern.eam.wshub.core.services.equipment.entities;

import java.io.Serializable;

/**
 * Entity for the functionality of the equipment replacement
 * 
 * @author jmesapol
 *
 */
public class EquipmentReplacement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3050639863872974781L;

	/**
	 * Old equipment
	 */
	private String oldEquipment;
	private String oldEquipmentDesc;

	/**
	 * Old Equipment (New status)
	 */
	private String oldEquipmentStatus;

	/**
	 * New Equipment
	 */
	private String newEquipment;
	private String newEquipmentDesc;

	/**
	 * New Equipment (New status) - Default I
	 */
	private String newEquipmentStatus = "I";

	/**
	 * Replacement mode:<br/>
	 * 1. Standard: Move the hierarchy normally and the children of the old
	 * equipment will go with him<br/>
	 * 2. Swaping: It requires that the new equipment is the last on his
	 * structure<br/>
	 */
	private String replacementMode = "Standard";

	/**
	 * Default constructor
	 */
	public EquipmentReplacement() {

	}

	/**
	 * Constructor with all parameters
	 * 
	 * @param oldEquipment
	 * @param oldEquipmentStatus
	 * @param newEquipment
	 * @param newEquipmentStatus
	 * @param replacementMode
	 */
	public EquipmentReplacement(String oldEquipment, String oldEquipmentStatus, String newEquipment,
			String newEquipmentStatus, String replacementMode) {
		this.oldEquipment = oldEquipment;
		this.oldEquipmentStatus = oldEquipmentStatus;
		this.newEquipment = newEquipment;
		this.newEquipmentStatus = newEquipmentStatus;
		this.replacementMode = replacementMode;
	}

	/**
	 * Constructor with parameters that don't have a default value
	 * 
	 * @param oldEquipment
	 * @param oldEquipmentStatus
	 * @param newEquipment
	 */
	public EquipmentReplacement(String oldEquipment, String oldEquipmentStatus, String newEquipment) {
		this.oldEquipment = oldEquipment;
		this.oldEquipmentStatus = oldEquipmentStatus;
		this.newEquipment = newEquipment;
	}

	/**
	 * @return the oldEquipment
	 */
	public String getOldEquipment() {
		return oldEquipment;
	}

	/**
	 * @param oldEquipment
	 *            the oldEquipment to set
	 */
	public void setOldEquipment(String oldEquipment) {
		this.oldEquipment = oldEquipment;
	}

	/**
	 * @return the oldEquipmentStatus
	 */
	public String getOldEquipmentStatus() {
		return oldEquipmentStatus;
	}

	/**
	 * @param oldEquipmentStatus
	 *            the oldEquipmentStatus to set
	 */
	public void setOldEquipmentStatus(String oldEquipmentStatus) {
		this.oldEquipmentStatus = oldEquipmentStatus;
	}

	/**
	 * @return the newEquipment
	 */
	public String getNewEquipment() {
		return newEquipment;
	}

	/**
	 * @param newEquipment
	 *            the newEquipment to set
	 */
	public void setNewEquipment(String newEquipment) {
		this.newEquipment = newEquipment;
	}

	/**
	 * @return the newEquipmentStatus
	 */
	public String getNewEquipmentStatus() {
		return newEquipmentStatus;
	}

	/**
	 * @param newEquipmentStatus
	 *            the newEquipmentStatus to set
	 */
	public void setNewEquipmentStatus(String newEquipmentStatus) {
		this.newEquipmentStatus = newEquipmentStatus;
	}

	/**
	 * @return the replacementMode
	 */
	public String getReplacementMode() {
		return replacementMode;
	}

	/**
	 * @param replacementMode
	 *            the replacementMode to set
	 */
	public void setReplacementMode(String replacementMode) {
		this.replacementMode = replacementMode;
	}

	/**
	 * @return the oldEquipmentDesc
	 */
	public String getOldEquipmentDesc() {
		return oldEquipmentDesc;
	}

	/**
	 * @param oldEquipmentDesc
	 *            the oldEquipmentDesc to set
	 */
	public void setOldEquipmentDesc(String oldEquipmentDesc) {
		this.oldEquipmentDesc = oldEquipmentDesc;
	}

	/**
	 * @return the newEquipmentDesc
	 */
	public String getNewEquipmentDesc() {
		return newEquipmentDesc;
	}

	/**
	 * @param newEquipmentDesc
	 *            the newEquipmentDesc to set
	 */
	public void setNewEquipmentDesc(String newEquipmentDesc) {
		this.newEquipmentDesc = newEquipmentDesc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EquipmentReplacement [" + (oldEquipment != null ? "oldEquipment=" + oldEquipment + ", " : "")
				+ (oldEquipmentDesc != null ? "oldEquipmentDesc=" + oldEquipmentDesc + ", " : "")
				+ (oldEquipmentStatus != null ? "oldEquipmentStatus=" + oldEquipmentStatus + ", " : "")
				+ (newEquipment != null ? "newEquipment=" + newEquipment + ", " : "")
				+ (newEquipmentDesc != null ? "newEquipmentDesc=" + newEquipmentDesc + ", " : "")
				+ (newEquipmentStatus != null ? "newEquipmentStatus=" + newEquipmentStatus + ", " : "")
				+ (replacementMode != null ? "replacementMode=" + replacementMode : "") + "]";
	}

}
