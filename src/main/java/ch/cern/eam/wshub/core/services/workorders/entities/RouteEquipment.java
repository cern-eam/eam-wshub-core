package ch.cern.eam.wshub.core.services.workorders.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="R5ROUTOBJECTS")
@NamedNativeQueries({
@NamedNativeQuery(name = "FINDEQPROUTES", query = "select * from R5ROUTOBJECTS where rob_route = :route order by ROB_LINE", resultClass=RouteEquipment.class),
@NamedNativeQuery(name = "FINDUDFROUTES", 
query = " select EQR_CODE ROB_ROUTE, EQUIPMENT ROB_OBJECT, EQPTYPE ROB_OBTYPE, EQPTYPE ROB_OBRTYPE, '0' ROB_LINE, '0' ROB_REVISION from (" + 
		"  select stc_child as EQUIPMENT, STC_CHILDTYPE AS EQPTYPE, EQR_CODE " + 
		"    from r5structures, U5EQUIPMENTROUTES " + 
		"    where stc_parent_org = '*' " + 
		"          and stc_child_org = '*' " + 
		"          and stc_child not in (select rob_object from r5routobjects where rob_route =  eqr_code) " + 
		"            start with stc_parent = eqr_parenteqp " + 
		"            connect by nocycle prior stc_child = stc_parent " + 
		" union " + 
		"  select obj_code as EQUIPMENT, obj_obtype AS EQPTYPE, EQR_CODE " + 
		"    from r5objects, U5EQUIPMENTROUTES " + 
		"    where obj_code = eqr_parenteqp " + 
		"          and obj_code not in (select rob_object from r5routobjects where rob_route =  eqr_code)) " + 
		" group by eqr_code, equipment, eqptype order by eqr_code",
		resultClass=RouteEquipment.class)
})
@IdClass(RouteEquipmentPK.class)
public class RouteEquipment implements Serializable {

	
	@Id
	@Column(name="ROB_ROUTE")
	private String routeCode;
	@Id
	@Column(name="ROB_REVISION")
	private String routeRevision;
	@Id
	@Column(name="ROB_LINE")
	private String routeEquipmentSequence;
	@Id
	@Column(name="ROB_OBJECT")
	private String equipmentCode;
	@Column(name="ROB_OBTYPE")
	private String objType;
	@Column(name="ROB_OBRTYPE")
	private String objRType;
	
	public String getRouteCode() {
		return routeCode;
	}
	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}
	public String getRouteRevision() {
		return routeRevision;
	}
	public void setRouteRevision(String routeRevision) {
		this.routeRevision = routeRevision;
	}
	public String getRouteEquipmentSequence() {
		return routeEquipmentSequence;
	}
	public void setRouteEquipmentSequence(String routeEquipmentSequence) {
		this.routeEquipmentSequence = routeEquipmentSequence;
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	public String getObjType() {
		return objType;
	}
	public void setObjType(String objType) {
		this.objType = objType;
	}
	public String getObjRType() {
		return objRType;
	}
	public void setObjRType(String objRType) {
		this.objRType = objRType;
	}
	
	@Override
	public String toString() {
		return "RouteEquipment [" + (routeCode != null ? "routeCode=" + routeCode + ", " : "")
				+ (routeRevision != null ? "routeRevision=" + routeRevision + ", " : "")
				+ (routeEquipmentSequence != null ? "routeEquipmentSequence=" + routeEquipmentSequence + ", " : "")
				+ (equipmentCode != null ? "equipmentCode=" + equipmentCode + ", " : "")
				+ (objType != null ? "objType=" + objType + ", " : "")
				+ (objRType != null ? "objRType=" + objRType : "") + "]";
	}
		
}

class RouteEquipmentPK implements Serializable {

	private String routeCode;
	private String routeRevision;
	private String routeEquipmentSequence;
	private String equipmentCode;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((equipmentCode == null) ? 0 : equipmentCode.hashCode());
		result = prime * result + ((routeCode == null) ? 0 : routeCode.hashCode());
		result = prime * result + ((routeEquipmentSequence == null) ? 0 : routeEquipmentSequence.hashCode());
		result = prime * result + ((routeRevision == null) ? 0 : routeRevision.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RouteEquipmentPK other = (RouteEquipmentPK) obj;
		if (equipmentCode == null) {
			if (other.equipmentCode != null)
				return false;
		} else if (!equipmentCode.equals(other.equipmentCode))
			return false;
		if (routeCode == null) {
			if (other.routeCode != null)
				return false;
		} else if (!routeCode.equals(other.routeCode))
			return false;
		if (routeEquipmentSequence == null) {
			if (other.routeEquipmentSequence != null)
				return false;
		} else if (!routeEquipmentSequence.equals(other.routeEquipmentSequence))
			return false;
		if (routeRevision == null) {
			if (other.routeRevision != null)
				return false;
		} else if (!routeRevision.equals(other.routeRevision))
			return false;
		return true;
	}

	
	
	
}
