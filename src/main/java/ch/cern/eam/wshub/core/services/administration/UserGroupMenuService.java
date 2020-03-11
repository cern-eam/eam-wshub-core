package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.tools.InforException;


public interface UserGroupMenuService {
    class MenuSpecification {
        public String userGroup;
        public String path;
        public String menuCode;
    }

    @Operation(logOperation = INFOR_OPERATION.WORKORDER_CREATE, logDataReference1 = LogDataReferenceType.RESULT)
    String addToMenuHierarchy(InforContext context, MenuSpecification node) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WORKORDER_CREATE, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteFromMenuHierarchy(InforContext context, MenuSpecification node) throws InforException;


}
