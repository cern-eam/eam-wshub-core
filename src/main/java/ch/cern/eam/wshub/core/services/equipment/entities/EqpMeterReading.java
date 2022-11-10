package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;
import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import net.datastream.schemas.mp_fields.READINGTYPEID_Type;
import net.datastream.schemas.mp_fields.StandardUserDefinedFields;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.Date;

public class EqpMeterReading {
    @InforField(xpath = "EQUIPMETERID/STANDARDENTITYCODE")
    private String readingCode;
    @InforField(xpath = "EQUIPMENTID/EQUIPMENTCODE")
    private String equipmentCode;
    @InforField(xpath = "TYPE/TYPECODE")
    private String type; //A,
    @InforField(xpath = "READINGTYPEID/READINGTYPECODE")
    private String readingType; //R,D
    @InforField(xpath = "UOMID/UOMCODE")
    private String UOM;
    @InforField(xpath = "READINGDATE")
    private Date readingDate;
    @InforField(xpath = "NEWVALUE")
    private BigDecimal quantity;

    @InforField(xpath = "WORKORDERID/JOBNUM")
    private String workorderId;
    @InforField(xpath = "RELATEDWORKORDERID/JOBNUM")
    private String relatedWorkorderId;
    @InforField(xpath = "CHILDREADING")
    private String childReading;
    @InforField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;

    public String getReadingCode() {
        return readingCode;
    }

    public void setReadingCode(final String readingCode) {
        this.readingCode = readingCode;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(final String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getReadingType() {
        return readingType;
    }

    public void setReadingType(final String readingType) {
        this.readingType = readingType;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(final String UOM) {
        this.UOM = UOM;
    }

    public Date getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(final Date readingDate) {
        this.readingDate = readingDate;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(final BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getWorkorderId() {
        return workorderId;
    }

    public void setWorkorderId(final String workorderId) {
        this.workorderId = workorderId;
    }

    public String getRelatedWorkorderId() {
        return relatedWorkorderId;
    }

    public void setRelatedWorkorderId(final String relatedWorkorderId) {
        this.relatedWorkorderId = relatedWorkorderId;
    }

    public String getChildReading() {
        return childReading;
    }

    public void setChildReading(final String childReading) {
        this.childReading = childReading;
    }

    public UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }

    public void setUserDefinedFields(final UserDefinedFields userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }
}
