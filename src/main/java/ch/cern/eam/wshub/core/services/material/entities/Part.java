package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.BooleanAdapter;
import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListHelpable;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLValue;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap.XmlHashMapAdapter;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "R5PARTS")
public class Part implements Serializable, UserDefinedListHelpable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7865040704362527306L;

	@Id
	@Column(name = "PAR_CODE")
	@EAMField(xpath = "PARTID/PARTCODE")
	private String code;

	@Transient
	private String newCode;

	@Column(name = "PAR_DESC")
	@EAMField(xpath = "PARTID/DESCRIPTION")
	private String description;

	@Column(name = "PAR_ORG")
	@EAMField(xpath = "PARTID/ORGANIZATIONID/ORGANIZATIONCODE")
	private String organization;

	@Column(name = "PAR_UOM")
	@EAMField(xpath = "UOMID/UOMCODE")
	private String UOM;
	@Transient
	@EAMField(xpath = "UOMID/DESCRIPTION", readOnly = true)
	private String UOMDesc;

	@Column(name = "PAR_CLASS")
	@EAMField(xpath = "CLASSID/CLASSCODE")
	private String classCode;
	@Transient
	@EAMField(xpath = "CLASSID/DESCRIPTION", readOnly = true)
	private String classDesc;

	@Transient
	@EAMField(xpath = "CATEGORYID/CATEGORYCODE")
	private String categoryCode;
	@Transient
	@EAMField(xpath = "CATEGORYID/DESCRIPTION", readOnly = true)
	private String categoryDesc;
	@Transient
	@EAMField(xpath = "PRIMARYCOMMODITY/COMMODITYCODE")
	private String commodityCode;
	@Transient
	@EAMField(xpath = "PRIMARYCOMMODITY/DESCRIPTION", readOnly = true)
	private String commodityDesc;
	@Transient
	@EAMField(xpath = "TRACKMETHOD/TYPECODE")
	private String trackingMethod;
	@Transient
	@EAMField(xpath = "PRICETYPE/TYPECODE")
	private String priceType;
	@Transient
	@EAMField(xpath = "BASEPRICE")
	private BigDecimal basePrice;
	@Transient
	@EAMField(xpath = "AVERAGEPRICE")
	private BigDecimal averagePrice;
	@Transient
	@EAMField(xpath = "STANDARDPRICE")
	private BigDecimal standardPrice;
	@Transient
	@EAMField(xpath = "LASTPRICE")
	private BigDecimal lastPrice;
	@Transient
	@EAMField(xpath = "BYASSET", booleanType = BooleanType.PLUS_MINUS)
	private Boolean trackByAsset;
	@Transient
	@EAMField(xpath = "KIT")
	private Boolean trackAsKit;
	@Transient
	@EAMField(xpath = "REPAIRABLE")
	private Boolean trackCores;
	@Transient
	@EAMField(xpath = "OUTOFSERVICE")
	private Boolean outOfService;
	@Transient
	@EAMField(xpath = "BYLOT", booleanType = BooleanType.PLUS_MINUS)
	private Boolean trackByLot;
	@Transient
	@EAMField(xpath = "PREVENTREORDERS")
	private Boolean preventReorders;
	@Transient
	@EAMField(xpath = "BUYER/USERCODE")
	private String buyerCode;
	@Transient
	@EAMField(xpath = "PREFERREDSUPPLIER/SUPPLIERCODE")
	private String preferredSupplier;
	@Transient
	@EAMField(xpath = "LONGDESCRIPTION")
	private String longDescription;

	@Transient
	@EAMField(xpath = "USERDEFINEDAREA")
	private CustomField[] customFields;
	@Transient
	@EAMField(xpath = "UserDefinedFields")
	private UserDefinedFields userDefinedFields;

	@Transient
	private String copyFrom;

	@Transient
	private HashMap<String, ArrayList<UDLValue>> userDefinedList;

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

	@JsonProperty("customField")
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

	@XmlJavaTypeAdapter(BooleanAdapter.class)
	public Boolean getPreventReorders() {
		return preventReorders;
	}

	public void setPreventReorders(Boolean preventReorders) {
		this.preventReorders = preventReorders;
	}

	@Override
	public String getCopyFrom() {
		return copyFrom;
	}

	public void setCopyFrom(String copyFrom) {
		this.copyFrom = copyFrom;
	}

	@Override
	public HashMap<String, ArrayList<UDLValue>> getUserDefinedList() {
		return userDefinedList;
	}

	@Override
	public void setUserDefinedList(HashMap<String, ArrayList<UDLValue>> userDefinedList) {
		this.userDefinedList = userDefinedList;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
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
				", copyFrom='" + copyFrom + '\'' +
				", userDefinedList='" + userDefinedList + '\'' +
				'}';
	}
}
