package ch.cern.eam.wshub.core.services.administration.entities;

import ch.cern.eam.wshub.core.annotations.InforField;

public class AddUserSetupResponsibility {
    @InforField(xpath = "UserSetupResponsibility/USERSETUPRESPONSIBILITYID/USERID/USERCODE")
    private String userCode;

    @InforField(xpath = "UserSetupResponsibility/USERSETUPRESPONSIBILITYID/RESPONSIBILITYID/UCODE")
    private String responsibilityCode;

    @InforField(xpath = "UserSetupResponsibility/USERSETUPRESPONSIBILITYID/RESPONSIBILITYID/DESCRIPTION")
    private String responsibilityDescription;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getResponsibilityCode() {
        return responsibilityCode;
    }

    public void setResponsibilityCode(String responsibilityCode) {
        this.responsibilityCode = responsibilityCode;
    }

    public String getResponsibilityDescription() {
        return responsibilityDescription;
    }

    public void setResponsibilityDescription(String responsibilityDescription) {
        this.responsibilityDescription = responsibilityDescription;
    }
}
