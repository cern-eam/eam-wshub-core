package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.adapters.BigIntegerAdapter;
import ch.cern.eam.wshub.core.adapters.BooleanAdapter;
import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigInteger;

public class StandardWorkOrder {

    @InforField(xpath = "STANDARDWO/STDWOCODE")
    private String code;
    @InforField(xpath = "STANDARDWO/DESCRIPTION")
    private String desc;

    @InforField(xpath = "TYPE/TYPECODE")
    private String typeCode;
    @InforField(xpath = "TYPE/DESCRIPTION", readOnly = true)
    private String typeDesc;

    @InforField(xpath = "WORKORDERTYPE/TYPECODE")
    private String workOrderTypeCode;
    @InforField(xpath = "WORKORDERTYPE/DESCRIPTION", readOnly = true)
    private String workOrderTypeDesc;

    @InforField(xpath = "DURATION")
    private BigInteger duration;

    @InforField(xpath = "CLASSID/CLASSCODE")
    private String classCode;
    @InforField(xpath = "CLASSID/DESCRIPTION", readOnly = true)
    private String classDesc;

    @InforField(xpath = "WORKORDERCLASSID/CLASSCODE")
    private String woClassCode;
    @InforField(xpath = "WORKORDERCLASSID/DESCRIPTION", readOnly = true)
    private String woClassDesc;

    @InforField(xpath = "EQUIPMENTCLASSID/CLASSCODE")
    private String equipmentClassCode;
    @InforField(xpath = "EQUIPMENTCLASSID/DESCRIPTION", readOnly = true)
    private String equipmentCassDesc;

    @InforField(xpath = "CATEGORYID/CATEGORYCODE")
    private String categoryCode;
    @InforField(xpath = "CATEGORYID/DESCRIPTION", readOnly = true)
    private String categoryDesc;

    @InforField(xpath = "PRIORITY/PRIORITYCODE")
    private String priorityCode;
    @InforField(xpath = "PRIORITY/DESCRIPTION", readOnly = true)
    private String priorityDesc;

    @InforField(xpath = "OUTOFSERVICE", booleanType = BooleanType.TRUE_FALSE)
    private Boolean outOfService = false;

    @InforField(xpath = "USERDEFINEDAREA")
    private CustomField[] customFields;

    @InforField(xpath = "UserDefinedFields")
    private UserDefinedFields userDefinedFields;

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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getWorkOrderTypeCode() {
        return workOrderTypeCode;
    }

    public void setWorkOrderTypeCode(String workOrderTypeCode) {
        this.workOrderTypeCode = workOrderTypeCode;
    }

    public String getWorkOrderTypeDesc() {
        return workOrderTypeDesc;
    }

    public void setWorkOrderTypeDesc(String workOrderTypeDesc) {
        this.workOrderTypeDesc = workOrderTypeDesc;
    }

    @XmlJavaTypeAdapter(BigIntegerAdapter.class)
    public BigInteger getDuration() {
        return duration;
    }

    public void setDuration(BigInteger duration) {
        this.duration = duration;
    }

    public String getWoClassCode() {
        return woClassCode;
    }

    public void setWoClassCode(String woClassCode) {
        this.woClassCode = woClassCode;
    }

    public String getWoClassDesc() {
        return woClassDesc;
    }

    public void setWoClassDesc(String woClassDesc) {
        this.woClassDesc = woClassDesc;
    }

    public String getEquipmentClassCode() {
        return equipmentClassCode;
    }

    public void setEquipmentClassCode(String equipmentClassCode) {
        this.equipmentClassCode = equipmentClassCode;
    }

    public String getEquipmentCassDesc() {
        return equipmentCassDesc;
    }

    public void setEquipmentCassDesc(String equipmentCassDesc) {
        this.equipmentCassDesc = equipmentCassDesc;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public String getPriorityDesc() {
        return priorityDesc;
    }

    public void setPriorityDesc(String priorityDesc) {
        this.priorityDesc = priorityDesc;
    }

    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean getOutOfService() {
        return outOfService;
    }

    public void setOutOfService(Boolean outOfService) {
        this.outOfService = outOfService;
    }

    @XmlElementWrapper(name = "customFields")
    @XmlElement(name = "customField")
    public CustomField[] getCustomFields() {
        return customFields;
    }

    public void setCustomFields(CustomField[] customFields) {
        this.customFields = customFields;
    }

    public UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }

    public void setUserDefinedFields(UserDefinedFields userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }

    @Override
    public String toString() {
        return "StandardWorkOrder{" +
                "categoryCode='" + categoryCode + '\'' +
                ", categoryDesc='" + categoryDesc + '\'' +
                ", classCode='" + classCode + '\'' +
                ", classDesc='" + classDesc + '\'' +
                ", code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                ", duration=" + duration +
                ", equipmentCassDesc='" + equipmentCassDesc + '\'' +
                ", equipmentClassCode='" + equipmentClassCode + '\'' +
                ", outOfService=" + outOfService +
                ", priorityCode='" + priorityCode + '\'' +
                ", priorityDesc='" + priorityDesc + '\'' +
                ", typeCode='" + typeCode + '\'' +
                ", typeDesc='" + typeDesc + '\'' +
                ", woClassCode='" + woClassCode + '\'' +
                ", woClassDesc='" + woClassDesc + '\'' +
                ", workOrderTypeCode='" + workOrderTypeCode + '\'' +
                ", workOrderTypeDesc='" + workOrderTypeDesc + '\'' +
                '}';
    }
}
