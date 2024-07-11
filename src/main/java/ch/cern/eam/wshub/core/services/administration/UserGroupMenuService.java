package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.administration.entities.MenuEntryNode;
import ch.cern.eam.wshub.core.services.administration.entities.MenuRequestType;
import ch.cern.eam.wshub.core.services.administration.entities.MenuSpecification;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;

import java.util.List;


public interface UserGroupMenuService {
    String MENU_FUNCTION_CODE = "BSFOLD";

    @Operation(logOperation = EAM_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addToMenuHierarchy(EAMContext context, MenuSpecification menuSpecification) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.MENU_HIERARCHY_DELETE, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteFromMenuHierarchy(EAMContext context, MenuSpecification menuSpecification) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.MENU_HIERARCHY_ADD_BATCH, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> addToMenuHierarchyBatch(EAMContext context, List<MenuSpecification> menuSpecificationList) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.MENU_HIERARCHY_DELETE_MANY_USERGROUPS, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> deleteFromMenuHierarchyManyUsergroups(EAMContext context, List<String> userGroups, MenuSpecification menuSpecification) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.MENU_HIERARCHY_DELETE_BATCH, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> deleteFromMenuHierarchyBatch(EAMContext context, List<MenuSpecification> menuSpecificationList) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.MENU_HIERARCHY_ADD_MANY_USERGROUPS, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> addToMenuHierarchyManyUsergroups(EAMContext context, List<String> userGroups, MenuSpecification menuSpecification) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.MENU_HIERARCHY_READ)
    MenuEntryNode getExtMenuHierarchyAsTree(EAMContext context, String userGroup, MenuRequestType requestType) throws EAMException;

    static void validateInputNode(MenuSpecification ms) throws EAMException {
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
