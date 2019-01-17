package ch.cern.eam.wshub.core.services.workorders.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="R5FINDINGS")
public class Finding implements Serializable {

	private static final long serialVersionUID = -6167336466688574437L;
	@Id
	@Column(name="FND_CODE")
	private String code;
	@Column(name="FND_DESC")
	private String desc;
	@Column(name="FND_GEN")
	private String generic;
	
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public String getDesc() {
	return desc;
}
public void setDesc(String desc) {
	this.desc = desc;
}
public String getGeneric() {
	return generic;
}
public void setGeneric(String generic) {
	this.generic = generic;
}

}
