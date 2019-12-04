package ch.cern.eam.wshub.core.services.userdefinedscreens.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class UDTOpBean implements Serializable {
    private String tableName;
    private UDTRow whereFilters;
    private UDTRow row;
    private List<UDTRow> rowList = new ArrayList<>();
    private List<String> columnsList = new ArrayList<>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public UDTRow getWhereFilters() {
        return whereFilters;
    }

    public void setWhereFilters(UDTRow whereFilters) {
        this.whereFilters = whereFilters;
    }

    public UDTRow getRows() {
        return row;
    }

    public void setRows(UDTRow rows) {
        this.row = row;
    }

    @XmlElementWrapper(name = "rows")
    @XmlElement(name = "row")
    public List<UDTRow> getRowList() {
        return rowList;
    }

    public void setRowList(List<UDTRow> rowList) {
        this.rowList = rowList;
    }

    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    public List<String> getColumnsList() {
        return columnsList;
    }

    public void setColumnsList(List<String> columnsList) {
        this.columnsList = columnsList;
    }
}
