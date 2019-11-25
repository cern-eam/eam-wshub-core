package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;

public class LinearReference implements Serializable {

	private String ID;
	private String typeCode;
	private String equipmentCode;
	private String relatedEquipmentCode;
	private String description;
	private BigDecimal fromPoint;
	private BigDecimal toPoint;
	private String geographicalReference;
	private String updateCount;
	private String classCode;
	private String colorCode;
	private String iconCode;
	private String iconPath;
	private String displayOnOverview;
	
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getFromPoint() {
		return fromPoint;
	}
	public void setFromPoint(BigDecimal fromPoint) {
		this.fromPoint = fromPoint;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getToPoint() {
		return toPoint;
	}
	public void setToPoint(BigDecimal toPoint) {
		this.toPoint = toPoint;
	}

	public String getGeographicalReference() {
		return geographicalReference;
	}
	public void setGeographicalReference(String geographicalReference) {
		this.geographicalReference = geographicalReference;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getUpdateCount() {
		return updateCount;
	}
	public void setUpdateCount(String updateCount) {
		this.updateCount = updateCount;
	}
	public String getRelatedEquipmentCode() {
		return relatedEquipmentCode;
	}
	public void setRelatedEquipmentCode(String relatedEquipmentCode) {
		this.relatedEquipmentCode = relatedEquipmentCode;
	}
	
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	public String getIconCode() {
		return iconCode;
	}
	public void setIconCode(String iconCode) {
		this.iconCode = iconCode;
	}
	public String getIconPath() {
		return iconPath;
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	@Override
	public String toString() {
		return "LinearReference ["
				+ (ID != null ? "ID=" + ID + ", " : "")
				+ (typeCode != null ? "typeCode=" + typeCode + ", " : "")
				+ (equipmentCode != null ? "equipmentCode=" + equipmentCode
						+ ", " : "")
				+ (relatedEquipmentCode != null ? "relatedEquipmentCode="
						+ relatedEquipmentCode + ", " : "")
				+ (description != null ? "description="
						+ description + ", " : "")
				+ (fromPoint != null ? "fromPoint=" + fromPoint + ", " : "")
				+ (toPoint != null ? "toPoint=" + toPoint + ", " : "")
				+ (geographicalReference != null ? "geographicalReference="
						+ geographicalReference + ", " : "")
				+ (updateCount != null ? "updateCount=" + updateCount + ", "
						: "")
				+ (classCode != null ? "classCode=" + classCode + ", " : "")
				+ (colorCode != null ? "colorCode=" + colorCode + ", " : "")
				+ (iconCode != null ? "iconCode=" + iconCode + ", " : "")
				+ (iconPath != null ? "iconPath=" + iconPath : "") + "]";
	}
	public String getDisplayOnOverview() {
		return displayOnOverview;
	}
	public void setDisplayOnOverview(String displayOnOverview) {
		this.displayOnOverview = displayOnOverview;
	}

	
	
}
