package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.BooleanAdapter;
import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
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
	@InforField(xpath = "PARTID/PARTCODE")
	private String code;

	@Transient
	private String newCode;

	@Column(name = "PAR_DESC")
	@InforField(xpath = "PARTID/DESCRIPTION")
	private String description;

	@Column(name = "PAR_UOM")
	@InforField(xpath = "UOMID/UOMCODE")
	private String UOM;
	@Transient
	@InforField(xpath = "UOMID/DESCRIPTION")
	private String UOMDesc;

	@Column(name = "PAR_CLASS")
	@InforField(xpath = "CLASSID/CLASSCODE")
	private String classCode;
	@Transient
	@InforField(xpath = "CLASSID/DESCRIPTION")
	private String classDesc;

	@Transient
	@InforField(xpath = "CATEGORYID/CATEGORYCODE")
	private String categoryCode;
	@Transient
	@InforField(xpath = "CATEGORYID/DESCRIPTION")
	private String categoryDesc;
	@Transient
	@InforField(xpath = "PRIMARYCOMMODITY/COMMODITYCODE")
	private String commodityCode;
	@Transient
	@InforField(xpath = "PRIMARYCOMMODITY/DESCRIPTION")
	private String commodityDesc;
	@Transient
	@InforField(xpath = "TRACKMETHOD/TYPECODE")
	private String trackingMethod;
	@Transient
	@InforField(xpath = "PRICETYPE/TYPECODE")
	private String priceType;
	@Transient
	@InforField(xpath = "BASEPRICE")
	private BigDecimal basePrice;
	@Transient
	@InforField(xpath = "AVERAGEPRICE")
	private BigDecimal averagePrice;
	@Transient
	@InforField(xpath = "STANDARDPRICE")
	private BigDecimal standardPrice;
	@Transient
	@InforField(xpath = "LASTPRICE")
	private BigDecimal lastPrice;
	@Transient
	@InforField(xpath = "BYASSET", booleanType = BooleanType.PLUS_MINUS)
	private Boolean trackByAsset = false;
	@Transient
	@InforField(xpath = "KIT")
	private Boolean trackAsKit = false;
	@Transient
	@InforField(xpath = "REPAIRABLE")
	private Boolean trackCores = false;
	@Transient
	@InforField(xpath = "OUTOFSERVICE")
	private Boolean outOfService = false;
	@Transient
	@InforField(xpath = "BYLOT", booleanType = BooleanType.PLUS_MINUS)
	private Boolean trackByLot = false;
	@Transient
	@InforField(xpath = "PREVENTREORDERS")
	private Boolean preventReorders = false;
	@Transient
	@InforField(xpath = "BUYER/USERCODE")
	private String buyerCode;
	@Transient
	@InforField(xpath = "PREFERREDSUPPLIER/SUPPLIERCODE")
	private String preferredSupplier;
	@Transient
	@InforField(xpath = "LONGDESCRIPTION")
	private String longDescription;

	@Transient
	@InforField(xpath = "USERDEFINEDAREA")
	private CustomField[] customFields;
	@Transient
	@InforField(xpath = "UserDefinedFields")
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

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getStandardPrice() {
		return standardPrice;
	}

	public void setStandardPrice(BigDecimal standardPrice) {
		this.standardPrice = standardPrice;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getTrackByAsset() {
		return trackByAsset;
	}

	public void setTrackByAsset(Boolean trackByAsset) {
		this.trackByAsset = trackByAsset;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getTrackAsKit() {
		return trackAsKit;
	}

	public void setTrackAsKit(Boolean trackAsKit) {
		this.trackAsKit = trackAsKit;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getTrackCores() {
		return trackCores;
	}

	public void setTrackCores(Boolean trackCores) {
		this.trackCores = trackCores;
	}

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getOutOfService() {
		return outOfService;
	}

	public void setOutOfService(Boolean outOfService) {
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

	public Boolean getTrackByLot() {
		return trackByLot;
	}

	public void setTrackByLot(Boolean trackByLot) {
		this.trackByLot = trackByLot;
	}

	public Boolean getPreventReorders() {
		return preventReorders;
	}

	public void setPreventReorders(Boolean preventReorders) {
		this.preventReorders = preventReorders;
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
