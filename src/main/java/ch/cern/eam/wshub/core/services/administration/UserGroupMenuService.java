package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;

import java.util.List;


public interface UserGroupMenuService {
    String MENU_FUNCTION_CODE = "BSFOLD";

    @Operation(logOperation = INFOR_OPERATION.WORKORDER_CREATE, logDataReference1 = LogDataReferenceType.RESULT)
    String addToMenuHierarchy(InforContext context, MenuSpecification node) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WORKORDER_CREATE, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteFromMenuHierarchy(InforContext context, MenuSpecification node) throws InforException;

    static void validateInputNode(MenuSpecification ms) throws InforException {
        List<String> path = ms.getMenuPath();
        String func = ms.getFunctionCode();
        String userGroup = ms.getForUserGroup();

        if (path == null || func == null || userGroup == null) {
            throw Tools.generateFault("Menu specifications cannot be null");
        }
        if (path.isEmpty()) {
            throw Tools.generateFault("Path cannot be empty");
        }
        if (userGroup.isEmpty()) {
            throw Tools.generateFault("User group cannot be empty");
        }
    }

    class MenuSpecification {
        private List<String> menuPath;
        private String functionCode;
        private String forUserGroup;

        public MenuSpecification() {
        }

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

}
