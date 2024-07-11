package ch.cern.eam.wshub.core.services.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;

public class Signature {

    @EAMField(xpath = "USERID/USERCODE")
    private String userCode;
    @EAMField(xpath = "PASSWORD")
    private String password;
    @EAMField(xpath = "SIGNATURETYPE")
    private String signatureType;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }
}
