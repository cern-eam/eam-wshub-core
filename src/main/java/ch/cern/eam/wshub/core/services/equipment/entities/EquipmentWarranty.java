package ch.cern.eam.wshub.core.services.equipment.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="R5WARCOVERAGES")
@NamedQuery(name = EquipmentWarranty.GETEQPWARRANTY, query = "SELECT ew FROM EquipmentWarranty ew WHERE ew.equipmentCode = :equipmentCode AND (ew.warrantyCode = :warrantyCode OR :warrantyCode IS NULL)")
public class EquipmentWarranty implements Serializable {

	public static final String GETEQPWARRANTY = "GETEQPWARRANTY";
	@Id
	@Column(name="WCV_SEQNO")
	private long sequenceNumber;
	@Column(name="WCV_OBJECT")
	private String equipmentCode;
	@Column(name="WCV_WARRANTY")
	private String warrantyCode;
	@Transient
	private String coverageType;
	@Transient
	private String duration;
	@Transient
	private String threshold;
	@Transient
	private String startDate;
	@Transient
	private String expirationDate;
	@Transient
	private String active;
	
	public String getWarrantyCode() {
		return warrantyCode;
	}
	public void setWarrantyCode(String warrantyCode) {
		this.warrantyCode = warrantyCode;
	}
	public String getCoverageType() {
		return coverageType;
	}
	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	@Override
	public String toString() {
		return "EquipmentWarranty [sequenceNumber=" + sequenceNumber + ", "
				+ (equipmentCode != null ? "equipmentCode=" + equipmentCode + ", " : "")
				+ (warrantyCode != null ? "warrantyCode=" + warrantyCode + ", " : "")
				+ (coverageType != null ? "coverageType=" + coverageType + ", " : "")
				+ (duration != null ? "duration=" + duration + ", " : "")
				+ (threshold != null ? "threshold=" + threshold + ", " : "")
				+ (startDate != null ? "startDate=" + startDate + ", " : "")
				+ (expirationDate != null ? "expirationDate=" + expirationDate + ", " : "")
				+ (active != null ? "active=" + active : "") + "]";
	}
	
	
}
