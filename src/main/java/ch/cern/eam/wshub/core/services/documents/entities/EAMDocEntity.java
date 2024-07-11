/**
 * 
 */
package ch.cern.eam.wshub.core.services.documents.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "R5DOCENTITIES")
@IdClass(EAMDocEntityPK.class)
public class EAMDocEntity implements Serializable {
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

	public EAMDocEntity() {
	}

	public EAMDocEntity(String document, String code, String entity) {
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
