package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.InforField;
import net.datastream.schemas.mp_entities.depreciation_001.AdditionalDetails;
import net.datastream.schemas.mp_fields.EQUIPMENTID_Type;
import net.datastream.schemas.mp_fields.USERDEFINEDCODEID_Type;
import net.datastream.schemas.mp_fields.WOID_Type;

import java.math.BigDecimal;

public class Equipment {
    @InforField(xpath = "WORKORDERID")
    private WOID_Type workorderid;

    @InforField(xpath = "EQUIPMENTID")
    private EQUIPMENTID_Type equipmentid;

    @InforField(xpath = "AdditionalDetails")
    private AdditionalDetails additionalDetails;

    public WOID_Type getWorkorderid() {
        return workorderid;
    }

    public void setWorkorderid(WOID_Type workorderid) {
        this.workorderid = workorderid;
    }

    public EQUIPMENTID_Type getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(EQUIPMENTID_Type equipmentid) {
        this.equipmentid = equipmentid;
    }

    public AdditionalDetails getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(AdditionalDetails additionalDetails) {
        this.additionalDetails = additionalDetails;
    }
}
