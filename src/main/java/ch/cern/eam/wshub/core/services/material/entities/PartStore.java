package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;

public class PartStore implements Serializable {

	private String storeCode;
	private String preferredSupplier;
	private String preferredStore;
	private String abcClass;
	private BigDecimal reorderLevel;
	private BigDecimal orderQty;
	private BigDecimal minimumQty;
	private String defaultBin;
	private String defaultReturnBin;
	private String partCode;
	private String preventIssueFromDefaultReturnBin;
	private String stockMethod;
	
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getPreferredSupplier() {
		return preferredSupplier;
	}
	public void setPreferredSupplier(String preferredSupplier) {
		this.preferredSupplier = preferredSupplier;
	}
	public String getAbcClass() {
		return abcClass;
	}
	public void setAbcClass(String abcClass) {
		this.abcClass = abcClass;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getReorderLevel() {
		return reorderLevel;
	}
	public void setReorderLevel(BigDecimal reorderLevel) {
		this.reorderLevel = reorderLevel;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}
	public String getDefaultBin() {
		return defaultBin;
	}
	public void setDefaultBin(String defaultBin) {
		this.defaultBin = defaultBin;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getDefaultReturnBin() {
		return defaultReturnBin;
	}
	public void setDefaultReturnBin(String defaultReturnBin) {
		this.defaultReturnBin = defaultReturnBin;
	}
	@Override
	public String toString() {
		return "PartStore ["
				+ (storeCode != null ? "storeCode=" + storeCode + ", " : "")
				+ (preferredSupplier != null ? "preferredSupplier="
						+ preferredSupplier + ", " : "")
				+ (preferredStore != null ? "preferredStore=" + preferredStore
						+ ", " : "")
				+ (abcClass != null ? "abcClass=" + abcClass + ", " : "")
				+ (reorderLevel != null ? "reorderLevel=" + reorderLevel + ", "
						: "")
				+ (orderQty != null ? "orderQty=" + orderQty + ", " : "")
				+ (minimumQty != null ? "minimumQty=" + minimumQty + ", " : "")
				+ (defaultBin != null ? "defaultBin=" + defaultBin + ", " : "")
				+ (defaultReturnBin != null ? "defaultReturnBin="
						+ defaultReturnBin + ", " : "")
				+ (partCode != null ? "partCode=" + partCode + ", " : "")
				+ (preventIssueFromDefaultReturnBin != null ? "preventIssueFromDefaultReturnBin="
						+ preventIssueFromDefaultReturnBin + ", "
						: "")
				+ (stockMethod != null ? "stockMethod=" + stockMethod : "")
				+ "]";
	}

	public String getPreferredStore() {
		return preferredStore;
	}
	public void setPreferredStore(String preferredStore) {
		this.preferredStore = preferredStore;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getMinimumQty() {
		return minimumQty;
	}
	public void setMinimumQty(BigDecimal minimumQty) {
		this.minimumQty = minimumQty;
	}
	public String getPreventIssueFromDefaultReturnBin() {
		return preventIssueFromDefaultReturnBin;
	}
	public void setPreventIssueFromDefaultReturnBin(
			String preventIssueFromDefaultReturnBin) {
		this.preventIssueFromDefaultReturnBin = preventIssueFromDefaultReturnBin;
	}
	public String getStockMethod() {
		return stockMethod;
	}
	public void setStockMethod(String stockMethod) {
		this.stockMethod = stockMethod;
	}
	
	
}
