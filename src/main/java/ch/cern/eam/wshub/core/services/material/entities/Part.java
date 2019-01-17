package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.Arrays;

@Entity
@Table(name = "R5PARTS")
public class Part implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7865040704362527306L;

	@Id
	@Column(name = "PAR_CODE")
	private String code;

	@Transient
	private String newCode;

	@Column(name = "PAR_DESC")
	private String description;

	@Column(name = "PAR_UOM")
	private String UOM;
	@Transient
	private String UOMDesc;

	@Column(name = "PAR_CLASS")
	private String classCode;
	@Transient
	private String classDesc;

	@Transient
	private String categoryCode;
	@Transient
	private String categoryDesc;
	@Transient
	private String commodityCode;
	@Transient
	private String commodityDesc;
	@Transient
	private String trackingMethod;
	@Transient
	private String priceType;
	@Transient
	private String basePrice;
	@Transient
	private String averagePrice;
	@Transient
	private String standardPrice;
	@Transient
	private String lastPrice;

	@Transient
	private String trackByAsset;
	@Transient
	private String trackAsKit;
	@Transient
	private String trackCores;
	@Transient
	private String outOfService;
	@Transient
	private String trackByLot;
	@Transient
	private String preventReorders;

	public String getTrackByLot() {
		return trackByLot;
	}

	public void setTrackByLot(String trackByLot) {
		this.trackByLot = trackByLot;
	}

	public String getPreventReorders() {
		return preventReorders;
	}

	public void setPreventReorders(String preventReorders) {
		this.preventReorders = preventReorders;
	}

	@Transient
	private String buyerCode;
	@Transient
	private String preferredSupplier;
	@Transient
	private String longDescription;

	@Transient
	private CustomField[] customFields;
	@Transient
	private UserDefinedFields userDefinedFields;

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

	public String getUOM() {
		return UOM;
	}

	public void setUOM(String uOM) {
		UOM = uOM;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	@XmlElementWrapper(name = "customFields")
	@XmlElement(name = "customField")
	public CustomField[] getCustomFields() {
		return customFields;
	}

	public void setCustomFields(CustomField[] customFields) {
		this.customFields = customFields;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getTrackingMethod() {
		return trackingMethod;
	}

	public void setTrackingMethod(String trackingMethod) {
		this.trackingMethod = trackingMethod;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}

	public String getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(String averagePrice) {
		this.averagePrice = averagePrice;
	}

	public String getStandardPrice() {
		return standardPrice;
	}

	public void setStandardPrice(String standardPrice) {
		this.standardPrice = standardPrice;
	}

	public String getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(String lastPrice) {
		this.lastPrice = lastPrice;
	}

	public String getTrackByAsset() {
		return trackByAsset;
	}

	public void setTrackByAsset(String trackByAsset) {
		this.trackByAsset = trackByAsset;
	}

	public String getTrackAsKit() {
		return trackAsKit;
	}

	public void setTrackAsKit(String trackAsKit) {
		this.trackAsKit = trackAsKit;
	}

	public String getTrackCores() {
		return trackCores;
	}

	public void setTrackCores(String trackCores) {
		this.trackCores = trackCores;
	}

	public String getOutOfService() {
		return outOfService;
	}

	public void setOutOfService(String outOfService) {
		this.outOfService = outOfService;
	}

	public String getBuyerCode() {
		return buyerCode;
	}

	public void setBuyerCode(String buyerCode) {
		this.buyerCode = buyerCode;
	}

	public String getPreferredSupplier() {
		return preferredSupplier;
	}

	public void setPreferredSupplier(String preferredSupplier) {
		this.preferredSupplier = preferredSupplier;
	}

	public UserDefinedFields getUserDefinedFields() {
		return userDefinedFields;
	}

	public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}

	public String getNewCode() {
		return newCode;
	}

	public void setNewCode(String newCode) {
		this.newCode = newCode;
	}

	public String getUOMDesc() {
		return UOMDesc;
	}

	public void setUOMDesc(String uOMDesc) {
		UOMDesc = uOMDesc;
	}

	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = classDesc;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getCommodityCode() {
		return commodityCode;
	}

	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}

	public String getCommodityDesc() {
		return commodityDesc;
	}

	public void setCommodityDesc(String commodityDesc) {
		this.commodityDesc = commodityDesc;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	@Override
	public String toString() {
		return "Part{" +
				"code='" + code + '\'' +
				", newCode='" + newCode + '\'' +
				", description='" + description + '\'' +
				", UOM='" + UOM + '\'' +
				", UOMDesc='" + UOMDesc + '\'' +
				", classCode='" + classCode + '\'' +
				", classDesc='" + classDesc + '\'' +
				", categoryCode='" + categoryCode + '\'' +
				", categoryDesc='" + categoryDesc + '\'' +
				", commodityCode='" + commodityCode + '\'' +
				", commodityDesc='" + commodityDesc + '\'' +
				", trackingMethod='" + trackingMethod + '\'' +
				", priceType='" + priceType + '\'' +
				", basePrice='" + basePrice + '\'' +
				", averagePrice='" + averagePrice + '\'' +
				", standardPrice='" + standardPrice + '\'' +
				", lastPrice='" + lastPrice + '\'' +
				", trackByAsset='" + trackByAsset + '\'' +
				", trackAsKit='" + trackAsKit + '\'' +
				", trackCores='" + trackCores + '\'' +
				", outOfService='" + outOfService + '\'' +
				", trackByLot='" + trackByLot + '\'' +
				", preventReorders='" + preventReorders + '\'' +
				", buyerCode='" + buyerCode + '\'' +
				", preferredSupplier='" + preferredSupplier + '\'' +
				", longDescription='" + longDescription + '\'' +
				", customFields=" + Arrays.toString(customFields) +
				", userDefinedFields=" + userDefinedFields +
				'}';
	}
}
