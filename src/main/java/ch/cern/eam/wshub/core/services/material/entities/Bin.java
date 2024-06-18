package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.adapters.BooleanAdapter;
import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.annotations.InforField;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

public class Bin implements Serializable {

	@InforField(xpath = "STOREBINID/STOREID/STORECODE", nullifyParentLevel = 1)
	private String storeCode;

	@InforField(xpath = "STOREBINID/BINID/BIN", nullifyParentLevel = 1)
	private String binCode;

	@InforField(xpath = "STOREBINID/BINID/DESCRIPTION", nullifyParentLevel = 0)
	private String binDesc;

	@InforField(xpath = "OUTOFSERVICE", booleanType = BooleanType.PLUS_MINUS)
	private Boolean outOfService;

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getBinCode() {
		return binCode;
	}

	public void setBinCode(String binCode) {
		this.binCode = binCode;
	}

	public String getBinDesc() {
		return binDesc;
	}

	public void setBinDesc(String binDesc) {
		this.binDesc = binDesc;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getOutOfService() {
		return outOfService;
	}

	public void setOutOfService(Boolean outOfService) {
		this.outOfService = outOfService;
	}
}
