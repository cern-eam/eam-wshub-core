package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;


public interface UserGroupMenuService {
    String MENU_FUNCTION_CODE = "BSFOLD";

    static void validateInputNode(MenuSpecification ms) throws InforException {
        String path = ms.getMenuPath();
        String func = ms.getFunctionCode();
        String userGroup = ms.getForUserGroup();
        if (path == null || func == null || userGroup == null) {
            throw Tools.generateFault("Menu specifications cannot be null");
        }
        if (path.isEmpty()) {
            throw Tools.generateFault("Path cannot be empty");
        }
        if (path.startsWith("/")) {
            throw Tools.generateFault("Path cannot start with '/'");
        }
        if (path.endsWith("/")) {
            throw Tools.generateFault("Path cannot end with '/'");
        }
        if (path.contains("//")) {
            throw Tools.generateFault("Path cannot have empty path items");
        }
        if (path.contains(" ")) {
            throw Tools.generateFault("Linear Reference ID must be present.");
        }

        return;
    }

    class MenuSpecification {
        private String menuPath;
        private String functionCode;
        private String forUserGroup;

        public MenuSpecification() { }

        public MenuSpecification(String menuPath, String functionCode, String forUserGroup) {
            this.menuPath = menuPath;
            this.functionCode = functionCode;
            this.forUserGroup = forUserGroup;
        }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
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
    @Operation(logOperation = INFOR_OPERATION.WORKORDER_CREATE, logDataReference1 = LogDataReferenceType.RESULT)
    String addToMenuHierarchy(InforContext context, MenuSpecification node) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WORKORDER_CREATE, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteFromMenuHierarchy(InforContext context, MenuSpecification node) throws InforException;

}
