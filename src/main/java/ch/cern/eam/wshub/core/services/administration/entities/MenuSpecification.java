package ch.cern.eam.wshub.core.services.administration.entities;

import java.util.List;

public class MenuSpecification {
    private List<String> menuPath;
    private String functionCode;
    private String forUserGroup;

    public MenuSpecification() { }

    public MenuSpecification(List<String> menuPath, String functionCode, String forUserGroup) {
        this.menuPath = menuPath;
        this.functionCode = functionCode;
        this.forUserGroup = forUserGroup;
    }

    public List<String> getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(List<String> menuPath) {
        this.menuPath = menuPath;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getForUserGroup() {
        return forUserGroup;
    }

    public void setForUserGroup(String forUserGroup) {
        this.forUserGroup = forUserGroup;
    }
}
