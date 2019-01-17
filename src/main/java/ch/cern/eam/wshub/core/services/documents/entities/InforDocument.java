package ch.cern.eam.wshub.core.services.documents.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "R5DOCUMENTS")
@NamedQueries({ 
	@NamedQuery(name = InforDocument.GET_DOCUMENTS, query = "select doc from InforDocument doc, InforDocEntity dae where"
		+ " dae.code = :code and dae.entity = :entity and doc.code = dae.document order by doc.code asc") 
})
public class InforDocument implements Serializable {
	private static final long serialVersionUID = 2632244342851353370L;

	public static final String GET_DOCUMENTS = "InforDocument.GET_DOCUMENTS";
	
	@Id
	@Column(name = "DOC_CODE")
	private String code;

	@Column(name = "DOC_DESC")
	private String description;

	@Column(name = "DOC_CLASS")
	private String docClass;

	@Column(name = "DOC_FILETYPE")
	private String filetype;
	
	@Column(name = "DOC_FILENAME")
	private String filename;
	
	@Column(name = "DOC_TYPE")
	private String type;
	
	public InforDocument() {
	}

	public InforDocument(String code) {
		super();
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDocClass() {
		return docClass;
	}

	public void setDocClass(String docClass) {
		this.docClass = docClass;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((docClass == null) ? 0 : docClass.hashCode());
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((filetype == null) ? 0 : filetype.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		InforDocument other = (InforDocument) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (docClass == null) {
			if (other.docClass != null)
				return false;
		} else if (!docClass.equals(other.docClass))
			return false;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (filetype == null) {
			if (other.filetype != null)
				return false;
		} else if (!filetype.equals(other.filetype))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}
