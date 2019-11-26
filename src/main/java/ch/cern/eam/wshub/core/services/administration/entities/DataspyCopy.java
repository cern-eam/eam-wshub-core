package ch.cern.eam.wshub.core.services.administration.entities;

import java.math.BigDecimal;

public class DataspyCopy {

    private BigDecimal dataspyCode;
    private String userCode;
    private Boolean defaultDataspy = false;

    public BigDecimal getDataspyCode() {
        return dataspyCode;
    }

    public void setDataspyCode(BigDecimal dataspyCode) {
        this.dataspyCode = dataspyCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Boolean getDefaultDataspy() {
        return defaultDataspy;
    }

    public void setDefaultDataspy(Boolean defaultDataspy) {
        this.defaultDataspy = defaultDataspy;
    }
}
