package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.BooleanAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.Date;

public class SalesPrice {

    @InforField(xpath = "CUSTOMERCONTRACTSALESPRICEID/CUSTOMERCONTRACTSALESPRICECODE")
    private String salesPriceCode;
    @InforField(xpath = "USERCONTRACTID/USERCONTRACTCODE")
    private String customerContractCode;
    @InforField(xpath = "USERCONTRACTID/USERCONTRACTREVISION")
    private BigDecimal customerContractRevision;
    @InforField(xpath = "ENTITYID/ENTITY")
    private String entityCode;
    @InforField(xpath = "ENTITYCODEID/CODE")
    private String code;
    @InforField(xpath = "DATEEFFECTIVE")
    private Date dateEffective;
    @InforField(xpath = "DATEEXPIRED")
    private Date dateExpired;
    @InforField(xpath = "SALESPRICE")
    private BigDecimal salesPrice;
    @InforField(xpath = "STOREID/STORECODE")
    private String storeCode;
    @InforField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;

    public String getCustomerContractCode() {
        return customerContractCode;
    }

    public void setCustomerContractCode(String customerContractCode) {
        this.customerContractCode = customerContractCode;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(Date dateEffective) {
        this.dateEffective = dateEffective;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getSalesPriceCode() {
        return salesPriceCode;
    }

    public void setSalesPriceCode(String salesPriceCode) {
        this.salesPriceCode = salesPriceCode;
    }

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
    public BigDecimal getCustomerContractRevision() {
        return customerContractRevision;
    }

    public void setCustomerContractRevision(BigDecimal customerContractRevision) {
        this.customerContractRevision = customerContractRevision;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDateExpired() {
        return dateExpired;
    }

    public void setDateExpired(Date dateExpired) {
        this.dateExpired = dateExpired;
    }

    public UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }

    public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }
}
