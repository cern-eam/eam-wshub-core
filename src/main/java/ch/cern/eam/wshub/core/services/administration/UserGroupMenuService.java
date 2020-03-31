package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.tools.InforException;


public interface UserGroupMenuService {
//    class MenuSpecification {
//        public String userGroup;
//        public String path;
//        public String menuCode;
//
//        public MenuSpecification() { }
//
//        public MenuSpecification(String path, String menuCode, String userGroup) {
//            this.path = path;
//            this.menuCode = menuCode;
//            this.userGroup = userGroup;
//        }
//    }
    class MenuSpecification {
        public String desiredFinalPath;
        public String desiredFinalFunctionCode;
        public String forUserGroup;

        public MenuSpecification() { }

        public MenuSpecification(String desiredFinalPath, String desiredFinalFunctionCode, String forUserGroup) {
            this.desiredFinalPath = desiredFinalPath;
            this.desiredFinalFunctionCode = desiredFinalFunctionCode;
            this.forUserGroup = forUserGroup;
        }
    }
    @Operation(logOperation = INFOR_OPERATION.WORKORDER_CREATE, logDataReference1 = LogDataReferenceType.RESULT)
    String addToMenuHierarchy(InforContext context, MenuSpecification node) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WORKORDER_CREATE, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteFromMenuHierarchy(InforContext context, MenuSpecification node) throws InforException;


}
