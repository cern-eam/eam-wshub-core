package ch.cern.eam.wshub.core.services.material.entities;

import java.io.Serializable;

import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

public class PurchaseOrder implements Serializable {
	private static final long serialVersionUID = 7260288685572669628L; 

	private String purchaseOrderId;
	private String statusCode;

	private UserDefinedFields userDefinedFields;
	
	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(String purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public UserDefinedFields getUserDefinedFields() {
		return userDefinedFields;
	}
	
	public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}

	public String getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
}
