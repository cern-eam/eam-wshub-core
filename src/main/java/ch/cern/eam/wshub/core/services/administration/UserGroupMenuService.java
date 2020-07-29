package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.administration.entities.MenuSpecification;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;

import java.util.List;


public interface UserGroupMenuService {
    String MENU_FUNCTION_CODE = "BSFOLD";

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addToMenuHierarchy(InforContext context, MenuSpecification menuSpecification) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_DELETE, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteFromMenuHierarchy(InforContext context, MenuSpecification menuSpecification) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD_BATCH, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> addToMenuHierarchyBatch(InforContext context, List<MenuSpecification> menuSpecificationList) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_DELETE_MANY_USERGROUPS, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> deleteFromMenuHierarchyManyUsergroups(InforContext context, List<String> userGroups, MenuSpecification menuSpecification) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_DELETE_BATCH, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> deleteFromMenuHierarchyBatch(InforContext context, List<MenuSpecification> menuSpecificationList) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD_MANY_USERGROUPS, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> addToMenuHierarchyManyUsergroups(InforContext context, List<String> userGroups, MenuSpecification menuSpecification) throws InforException;

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
}
