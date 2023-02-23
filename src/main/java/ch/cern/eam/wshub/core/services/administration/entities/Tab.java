package ch.cern.eam.wshub.core.services.administration.entities;

import java.util.HashMap;
import java.util.Map;

public class Tab {
    Map<String, ElementInfo> fields;
    Boolean alwaysDisplayed;
    Boolean tabAvailable;
    String tabDescription;

    Boolean deleteAllowed;

    Boolean updateAllowed;

    Boolean queryAllowed;

    Boolean insertAllowed;


    public Tab() {
        fields = new HashMap<>();
        alwaysDisplayed = false;
        tabAvailable = false;
    }

    public Map<String, ElementInfo> getFields() {
        return fields;
    }

    public void setFields(Map<String, ElementInfo> fields) {
        this.fields = fields;
    }

    public Boolean getAlwaysDisplayed() {
        return alwaysDisplayed;
    }

    public void setAlwaysDisplayed(Boolean alwaysDisplayed) {
        this.alwaysDisplayed = alwaysDisplayed;
    }

    public Boolean getTabAvailable() {
        return tabAvailable;
    }

    public void setTabAvailable(Boolean tabAvailable) {
        this.tabAvailable = tabAvailable;
    }

    public String getTabDescription() {
        return tabDescription;
    }

    public void setTabDescription(String tabDescription) {
        this.tabDescription = tabDescription;
    }

    public Boolean getDeleteAllowed() {
        return deleteAllowed;
    }

    public void setDeleteAllowed(Boolean deleteAllowed) {
        this.deleteAllowed = deleteAllowed;
    }

    public Boolean getUpdateAllowed() {
        return updateAllowed;
    }

    public void setUpdateAllowed(Boolean updateAllowed) {
        this.updateAllowed = updateAllowed;
    }

    public Boolean getQueryAllowed() {
        return queryAllowed;
    }

    public void setQueryAllowed(Boolean queryAllowed) {
        this.queryAllowed = queryAllowed;
    }

    public Boolean getInsertAllowed() {
        return insertAllowed;
    }

    public void setInsertAllowed(Boolean insertAllowed) {
        this.insertAllowed = insertAllowed;
    }
}
