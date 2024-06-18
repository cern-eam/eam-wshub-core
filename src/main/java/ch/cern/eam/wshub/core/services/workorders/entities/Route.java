package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.BooleanAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.annotations.InforField;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigInteger;
import java.util.Date;

public class Route {

    @InforField(xpath = "ROUTEID/ROUTECODE")
    private String code;

    @InforField(xpath = "ROUTEID/DESCRIPTION")
    private String desc;

    @InforField(xpath = "ROUTEID/ROUTEREVISION")
    private BigInteger revision;

    @InforField(xpath = "CLASSID/CLASSCODE")
    private String equipmentClassCode;

    @InforField(xpath = "CATEGORYID/CATEGORYCODE")
    private String categoryCode;

    @InforField(xpath = "REVISIONSTATUS/STATUSCODE")
    private String revisionStatusCode;

    @InforField(xpath = "ROUTETEMPLATE", booleanType = BooleanType.TRUE_FALSE)
    private Boolean template;

    @InforField(xpath = "REVISIONCONTROL/APPROVEDATE")
    private Date dateApproved;

    @InforField(xpath = "REVISIONCONTROL/DATEREQUESTED")
    private Date dateRequested;

    @InforField(xpath = "REVISIONCONTROL/REVISIONREASON")
    private String revisionReason;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BigInteger getRevision() {
        return revision;
    }

    public void setRevision(BigInteger revision) {
        this.revision = revision;
    }

    public String getEquipmentClassCode() {
        return equipmentClassCode;
    }

    public void setEquipmentClassCode(String equipmentClassCode) {
        this.equipmentClassCode = equipmentClassCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getRevisionStatusCode() {
        return revisionStatusCode;
    }

    public void setRevisionStatusCode(String revisionStatusCode) {
        this.revisionStatusCode = revisionStatusCode;
    }

    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean getTemplate() {
        return template;
    }

    public void setTemplate(Boolean template) {
        this.template = template;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(Date dateApproved) {
        this.dateApproved = dateApproved;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(Date dateRequested) {
        this.dateRequested = dateRequested;
    }

    public String getRevisionReason() {
        return revisionReason;
    }

    public void setRevisionReason(String revisionReason) {
        this.revisionReason = revisionReason;
    }
}
