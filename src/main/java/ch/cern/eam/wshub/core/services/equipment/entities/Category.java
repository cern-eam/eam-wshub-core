package ch.cern.eam.wshub.core.services.equipment.entities;

import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.CustomField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;

@Entity
@Table(name = "R5OBJECTS")
public class Category implements Serializable {

    @Id
    @Column(name = "OBJ_CODE")
    @InforField(xpath = "CATEGORYID/CATEGORYCODE")
    private String code;

    @Column(name = "OBJ_DESC")
    @InforField(xpath = "CATEGORYID/DESCRIPTION")
    private String description;

    @Transient
    @InforField(xpath = "CLASSID/CLASSCODE")
    private String classCode;

    @Transient
    @InforField(xpath = "CLASSID/DESCRIPTION", readOnly = true)
    private String classDesc;

    @Transient
    @InforField(xpath = "MANUFACTURERID/MANUFACTURERCODE")
    private String manufacturerCode;

    @Transient
    @InforField(xpath = "MANUFACTURERID/DESCRIPTION")
    private String manufacturerDesc;

    @Transient
    @Column(name = "OBJ_SCHEMATIC")
    @InforField(xpath= "SCHEMATICID/USERDEFINEDCODE")
    private String schematic;

    @Transient
    @InforField(xpath = "USERDEFINEDAREA")
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

