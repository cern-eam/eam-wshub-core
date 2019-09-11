package ch.cern.eam.wshub.core.services.administration.entities;

public class DataspyCopy {

    private String dataspyCode;
    private String userCode;
    private String defaultDataspy;

    public String getDataspyCode() {
        return dataspyCode;
    }

    public void setDataspyCode(String dataspyCode) {
        this.dataspyCode = dataspyCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getDefaultDataspy() {
        return defaultDataspy;
    }

    public void setDefaultDataspy(String defaultDataspy) {
        this.defaultDataspy = defaultDataspy;
    }
}
