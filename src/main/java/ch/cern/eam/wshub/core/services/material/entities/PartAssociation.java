package ch.cern.eam.wshub.core.services.material.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="R5ENTITYPARTS")
@NamedQuery(name=PartAssociation.GET_PART_ASSOCIATION, query = "SELECT partass FROM PartAssociation partass WHERE (partass.partCode = :partCode OR :partCode IS NULL) AND partass.equipmentCode = :equipmentCode")
public class PartAssociation implements Serializable {

	public static final String GET_PART_ASSOCIATION = "GET_PART_ASSOCIATION";
	@Id
	@Column(name="EPA_PK")
	private String pk;
	@Column(name="EPA_PART")
	private String partCode;
	@Column(name="EPA_CODE")
	private String equipmentCode;
	@Transient private String quantity;
	@Transient private String UOM;
	@Transient private String associationEntity;
	
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getUOM() {
		return UOM;
	}
	public void setUOM(String uOM) {
		UOM = uOM;
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	public String getAssociationEntity() {
		return associationEntity;
	}
	public void setAssociationEntity(String associationEntity) {
		this.associationEntity = associationEntity;
	}
	@Override
	public String toString() {
		return "PartAssociation ["
				+ (partCode != null ? "partCode=" + partCode + ", " : "")
				+ (quantity != null ? "quantity=" + quantity + ", " : "")
				+ (UOM != null ? "UOM=" + UOM + ", " : "")
				+ (equipmentCode != null ? "equipmentCode=" + equipmentCode
						+ ", " : "")
				+ (associationEntity != null ? "associationEntity="
						+ associationEntity : "") + "]";
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	
	
	
}
