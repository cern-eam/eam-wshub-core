package ch.cern.eam.wshub.core.services.administration.entities;

import ch.cern.eam.wshub.core.adapters.BigDecimalAdapter;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;

public class DataspyCopy {

    private BigDecimal dataspyCode;
    private String userCode;
    private Boolean defaultDataspy = false;

    @XmlJavaTypeAdapter(BigDecimalAdapter.class)
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
