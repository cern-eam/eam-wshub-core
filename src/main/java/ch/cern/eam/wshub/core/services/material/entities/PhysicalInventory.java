package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "R5TRANSACTIONS")
public class PhysicalInventory implements Serializable {
    private static final long serialVersionUID = -6871230766779988176L;

    @Id
    @Column(name = "TRA_CODE")
    @InforField(xpath = "TRANSACTIONID/TRANSACTIONCODE")
    private String code;

    @Column(name = "TRA_DESC")
    @InforField(xpath = "TRANSACTIONID/DESCRIPTION")
    private String description;

    @Column(name = "TRA_FROMCODE")
    @InforField(xpath = "STOREID/STORECODE")
    private String store;

    @Column(name = "TRA_AUTH")
    //@InforField(xpath = "")
    private String approvedBy;

    @Column(name = "TRA_PERS")
    @InforField(xpath = "ASSIGNEDTO/PERSONCODE")
    private String assignedTo;

    @Column(name = "TRA_STATUS")
    @InforField(xpath = "TRANSACTIONSTATUS/STATUSCODE")
    private String status;

    @Transient
    @InforField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;

    // the fields below are not stored on the database and are only used for creation
    @InforField(xpath = "PARTID/PARTCODE")
    private String part;

    @InforField(xpath = "PARTCLASSID/CLASSCODE")
    private String partClass;

    @InforField(xpath = "STOCKCLASSID/CLASSCODE")
    private String stockClass;

    @InforField(xpath = "ABCCODE")
    private String abcClass;

    @InforField(xpath = "INCLUDECONSIGNMENTITEM")
    private Boolean includeConsignmentItem;

    @InforField(xpath = "FROMBIN/BIN")
    private String fromBin;

    @InforField(xpath = "TOBIN/BIN")
    private String toBin;

    @InforField(xpath = "STOCKDATE")
    private Date physicalInventoryDate;

    @InforField(xpath = "INCLUDECHILDSTORES")
    private Boolean includeChildStores;

    @InforField(xpath = "INCLUDEOUTOFSERVICEITEM")
    private Boolean includeOutOfServiceItem;

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

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPhysicalInventoryDate() {
        return physicalInventoryDate;
    }

    public void setPhysicalInventoryDate(Date physicalInventoryDate) {
        this.physicalInventoryDate = physicalInventoryDate;
    }

    public UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }

    public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getPartClass() {
        return partClass;
    }

    public void setPartClass(String partClass) {
        this.partClass = partClass;
    }

    public String getStockClass() {
        return stockClass;
    }

    public void setStockClass(String stockClass) {
        this.stockClass = stockClass;
    }

    public String getAbcClass() {
        return abcClass;
    }

    public void setAbcClass(String abcClass) {
        this.abcClass = abcClass;
    }

    public Boolean getIncludeConsignmentItem() {
        return includeConsignmentItem;
    }

    public void setIncludeConsignmentItem(Boolean includeConsignmentItem) {
        this.includeConsignmentItem = includeConsignmentItem;
    }

    public String getFromBin() {
        return fromBin;
    }

    public void setFromBin(String fromBin) {
        this.fromBin = fromBin;
    }

    public String getToBin() {
        return toBin;
    }

    public void setToBin(String toBin) {
        this.toBin = toBin;
    }

    public Boolean getIncludeChildStores() {
        return includeChildStores;
    }

    public void setIncludeChildStores(Boolean includeChildStores) {
        this.includeChildStores = includeChildStores;
    }

    public Boolean getIncludeOutOfServiceItem() {
        return includeOutOfServiceItem;
    }

    public void setIncludeOutOfServiceItem(Boolean includeOutOfServiceItem) {
        this.includeOutOfServiceItem = includeOutOfServiceItem;
    }
}
