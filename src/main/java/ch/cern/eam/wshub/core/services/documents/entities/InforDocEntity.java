/**
 * 
 */
package ch.cern.eam.wshub.core.services.documents.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "R5DOCENTITIES")
@IdClass(InforDocEntityPK.class)
public class InforDocEntity implements Serializable {
	private static final long serialVersionUID = -4636805288760053482L;
	
	@Id
	@Column(name = "DAE_DOCUMENT")
	private String document;
	@Id
	@Column(name = "DAE_CODE")
	private String code;
	@Id
	@Column(name = "DAE_RENTITY")
	private String entity;

	public InforDocEntity() {
	}

	public InforDocEntity(String document, String code, String entity) {
		this.document = document;
		this.code = code;
		this.entity = entity;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

}
