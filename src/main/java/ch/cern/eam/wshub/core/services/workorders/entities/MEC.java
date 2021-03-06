package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.InforField;
import net.datastream.schemas.mp_entities.workorderequipment_001.AdditionalDetails;
import net.datastream.schemas.mp_fields.EQUIPMENTID_Type;
import net.datastream.schemas.mp_fields.USERDEFINEDCODEID_Type;
import net.datastream.schemas.mp_fields.WOID_Type;

import java.math.BigDecimal;

public class MEC {
    @InforField(xpath = "WORKORDERID/JOBNUM")
    private String workorderID;

    @InforField(xpath = "EQUIPMENTID/EQUIPMENTCODE")
    private String equipmentCode;

    @InforField(xpath = "AdditionalDetails/RELATEDWORKORDERID/JOBNUM")
    private String relatedWorkorderID;

    @InforField(xpath = "AdditionalDetails/LOCATIONID/LOCATIONCODE")
    private String locationID;

    @InforField(xpath = "AdditionalDetails/COSTCODEID/COSTCODE")
    private String costCode;

    @InforField(xpath = "AdditionalDetails/SAFETY")
    private String safety;


    public String getWorkorderID() {
        return workorderID;
    }

    public void setWorkorderID(String workorderID) {
        this.workorderID = workorderID;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getRelatedWorkorderID() {
        return relatedWorkorderID;
    }

    public void setRelatedWorkorderID(String relatedWorkorderID) {
        this.relatedWorkorderID = relatedWorkorderID;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
    }
}
