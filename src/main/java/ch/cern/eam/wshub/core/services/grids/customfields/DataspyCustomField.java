package ch.cern.eam.wshub.core.services.grids.customfields;

import javax.persistence.*;

@Entity
@Table(name = "R5DDCUSTOMFIELDS")

/**
 * The join condition matches property name, extracted from the tag name (DCF_TAGNAME)
 * to property code (PRO_CODE) in R5PROPERTIES. The tag name is stored like
 * c_<property name>_<rentity>. In order to extract the property name we strip
 * the parts before and after the first and last underscores. There is an additional hack:
 * Infor escapes '-' in custom field names as '_i' so this is reversed.
 */
@NamedNativeQuery(name = DataspyCustomField.GET_CUSTOM_FIELDS_FOR_DATASPY, query = "SELECT DCF_DDSPYID," +
        "  DCF_TAGNAME," +
        "  DCF_WIDTH," +
        "  DCF_ORDER," +
        "  DCF_VIEWTYPE," +
        "  PRO_TEXT, " +
        "  PRO_TYPE " +
        "FROM R5DDCUSTOMFIELDS " +
        "JOIN R5PROPERTIES " +
        "ON REGEXP_REPLACE(UPPER(SUBSTR(DCF_TAGNAME, 3, INSTR(DCF_TAGNAME, '_', 1, REGEXP_COUNT(DCF_TAGNAME, '\\_')) -3)), '\\_I', '-') = PRO_CODE " +
        "WHERE DCF_DDSPYID                                                   = :dataspyID")
public class DataspyCustomField {

    public static final String GET_CUSTOM_FIELDS_FOR_DATASPY = "DataspyCustomField.GET_CUSTOM_FIELDS_FOR_DATASPY";

    @Column(name = "DCF_DDSPYID")
    private String dataspyID;

    @Column(name = "DCF_TAGNAME")
    private String tagName;

    @Column(name = "DCF_WIDTH")
    private String width;

    @Column(name = "DCF_ORDER")
    private String order;

    @Column(name = "DCF_VIEWTYPE")
    private String viewType;

    @Column(name = "ROWID")
    @Id
    private String rowid;

    @Transient
    private String propertyName;

    @Transient
    private String rentity;

    @Transient
    private String propertyType;

    @Transient
    private String description;

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRentity() {
        return rentity;
    }

    public void setRentity(String rentity) {
        this.rentity = rentity;
    }

    public String getPropertyName() {
        return propertyName;

    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getDataspyID() {
        return dataspyID;
    }

    public void setDataspyID(String dataspyID) {
        this.dataspyID = dataspyID;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }
}
