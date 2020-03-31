package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

    @Column(name = "TRA_CREATED")
    //@InforField(xpath = "")
    private Date dateCreated;

    //@Column(name = "")
    @InforField(xpath = "STOCKDATE")
    private Date physicalInventoryDate;

    @Transient
    @InforField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;

    List<PhysicalInventoryRow> rows;

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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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

    public List<PhysicalInventoryRow> getRows() {
        return rows;
    }

    public void setRows(List<PhysicalInventoryRow> rows) {
        this.rows = rows;
    }
}
