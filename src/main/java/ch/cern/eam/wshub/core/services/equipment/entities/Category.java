package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.services.entities.CustomField;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Arrays;

@Entity
@Table(name = "R5OBJECTS")
public class Category implements Serializable {

    @Id
    @Column(name = "OBJ_CODE")
    @EAMField(xpath = "CATEGORYID/CATEGORYCODE")
    private String code;

    @Column(name = "OBJ_DESC")
    @EAMField(xpath = "CATEGORYID/DESCRIPTION")
    private String description;

    @Transient
    @EAMField(xpath = "CLASSID/CLASSCODE")
    private String classCode;

    @Transient
    @EAMField(xpath = "CLASSID/DESCRIPTION", readOnly = true)
    private String classDesc;

    @Transient
    @EAMField(xpath = "MANUFACTURERID/MANUFACTURERCODE")
    private String manufacturerCode;

    @Transient
    @EAMField(xpath = "MANUFACTURERID/DESCRIPTION")
    private String manufacturerDesc;

    @Transient
    @Column(name = "OBJ_SCHEMATIC")
    @EAMField(xpath= "SCHEMATICID/USERDEFINEDCODE")
    private String schematic;

    @Transient
    @EAMField(xpath = "USERDEFINEDAREA")
    private CustomField[] customFields;


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

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassDesc() {
        return classDesc;
    }

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public String getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(String manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public String getManufacturerDesc() {
        return manufacturerDesc;
    }

    public void setManufacturerDesc(String manufacturerDesc) {
        this.manufacturerDesc = manufacturerDesc;
    }

    public String getSchematic() {
        return schematic;
    }

    public void setSchematic(String schematic) {
        this.schematic = schematic;
    }

    public CustomField[] getCustomFields() {
        return customFields;
    }

    public void setCustomFields(CustomField[] customFields) {
        this.customFields = customFields;
    }

    @Override
    public String toString() {
        return "Category{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", classCode='" + classCode + '\'' +
                ", classDesc='" + classDesc + '\'' +
                ", manufacturerCode='" + manufacturerCode + '\'' +
                ", manufacturerDesc='" + manufacturerDesc + '\'' +
                ", schematic='" + schematic + '\'' +
                ", customFields=" + Arrays.toString(customFields) +
                '}';
    }
}

