package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;

import jakarta.persistence.Transient;
import java.math.BigDecimal;

public class EquipmentConfigurationId {
    @EAMField(xpath = "EQUIPMENTCONFIGURATIONID/EQUIPMENTCONFIGURATIONCODE")
    private String equipmentConfigCode;

    @EAMField(xpath = "EQUIPMENTCONFIGURATIONID/REVISIONNUM")
    private BigDecimal revisionNum;

    public String getEquipmentConfigCode() {
        return equipmentConfigCode;
    }

    public void setEquipmentConfigCode(String equipmentConfigCode) {
        this.equipmentConfigCode = equipmentConfigCode;
    }

    public BigDecimal getRevisionNum() {
        return revisionNum;
    }

    public void setRevisionNum(BigDecimal revisionNum) {
        this.revisionNum = revisionNum;
    }

    public EquipmentConfigurationId() {
    }

    public EquipmentConfigurationId(String equipmentConfigCode, BigDecimal revisionNum) {
        this.equipmentConfigCode = equipmentConfigCode;
        this.revisionNum = revisionNum;
    }
}
