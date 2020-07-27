package ch.cern.eam.wshub.core.services.comments.entities;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

public class Comment implements Serializable {

	private static final long serialVersionUID = 7120094465433764972L;
	private String text;
	private String lineNumber;
	private String updateCount;
	private String creationUserCode;
	private String creationUserDesc;
	private String updateUserCode;
	private String updateUserDesc;
	private String creationDate;
	private String updateDate;
	private String typeCode;
	private String entityKeyCode;
	private String entityCode;
	private String print;
	
	public String getPk() {
		return this.getEntityKeyCode() + "C" + this.getLineNumber();
	}
	
	@XmlTransient
	private boolean updated;
	@XmlTransient
	private boolean created;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getUpdateCount() {
		return updateCount;
	}
	public void setUpdateCount(String updateCount) {
		this.updateCount = updateCount;
	}
	public String getCreationUserCode() {
		return creationUserCode;
	}
	public void setCreationUserCode(String creationUserCode) {
		this.creationUserCode = creationUserCode;
	}
	public String getCreationUserDesc() {
		return creationUserDesc;
	}
	public void setCreationUserDesc(String creationUserDesc) {
		this.creationUserDesc = creationUserDesc;
	}
	public String getUpdateUserCode() {
		return updateUserCode;
	}
	public void setUpdateUserCode(String updateUserCode) {
		this.updateUserCode = updateUserCode;
	}
	public String getUpdateUserDesc() {
		return updateUserDesc;
	}
	public void setUpdateUserDesc(String updateUserDesc) {
		this.updateUserDesc = updateUserDesc;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getEntityCode() {
		return entityCode;
	}
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getEntityKeyCode() {
		return entityKeyCode;
	}
	public void setEntityKeyCode(String entityKeyCode) {
		this.entityKeyCode = entityKeyCode;
	}
	@Override
	public String toString() {
		return "Comment ["
				+ (text != null ? "text=" + text + ", " : "")
				+ (lineNumber != null ? "lineNumber=" + lineNumber + ", " : "")
				+ (updateCount != null ? "updateCount=" + updateCount + ", "
						: "")
				+ (creationUserCode != null ? "creationUserCode="
						+ creationUserCode + ", " : "")
				+ (creationUserDesc != null ? "creationUserDesc="
						+ creationUserDesc + ", " : "")
				+ (updateUserCode != null ? "updateUserCode=" + updateUserCode
						+ ", " : "")
				+ (updateUserDesc != null ? "updateUserDesc=" + updateUserDesc
						+ ", " : "")
				+ (creationDate != null ? "creationDate=" + creationDate + ", "
						: "")
				+ (updateDate != null ? "updateDate=" + updateDate + ", " : "")
				+ (typeCode != null ? "typeCode=" + typeCode + ", " : "")
				+ (entityKeyCode != null ? "entityKeyCode=" + entityKeyCode
						+ ", " : "")
				+ (entityCode != null ? "entityCode=" + entityCode : "") + "]";
	}
	public boolean isUpdated() {
		return updated;
	}
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	public boolean isCreated() {
		return created;
	}
	public void setCreated(boolean created) {
		this.created = created;
	}

	public String getPrint() {
		return print;
	}

	public void setPrint(String print) {
		this.print = print;
	}
}
