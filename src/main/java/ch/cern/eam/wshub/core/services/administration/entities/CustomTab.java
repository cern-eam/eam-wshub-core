package ch.cern.eam.wshub.core.services.administration.entities;

import java.util.List;

public class CustomTab extends Tab {
    private String value;
    private List<URLParam> urlParams;
    public CustomTab() {
        super();
    }
    public CustomTab(Tab tab) {
        super();
        this.setFields(tab.getFields());
        this.setAlwaysDisplayed(tab.getAlwaysDisplayed());
        this.setTabAvailable(tab.getTabAvailable());
        this.setTabDescription(tab.getTabDescription());
        this.setDeleteAllowed(tab.getDeleteAllowed());
        this.setUpdateAllowed(tab.getUpdateAllowed());
        this.setQueryAllowed(tab.getQueryAllowed());
        this.setInsertAllowed(tab.getInsertAllowed());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<URLParam> getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(List<URLParam> urlParams) {
        this.urlParams = urlParams;
    }
}
