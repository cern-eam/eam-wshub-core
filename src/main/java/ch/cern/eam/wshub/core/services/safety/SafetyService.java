package ch.cern.eam.wshub.core.services.safety;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.safety.entities.EntitySafetyWSHub;
import ch.cern.eam.wshub.core.tools.InforException;
import net.datastream.schemas.mp_entities.entitysafety_001.EntitySafety;

import java.util.List;


public interface SafetyService {
    String MENU_FUNCTION_CODE = "BSFOLD";

//    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
//    String addToMenuHierarchy(InforContext context, MenuSpecification node) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addSafety(InforContext context, EntitySafetyWSHub safety) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    List<String> addSafeties(InforContext context, List<EntitySafetyWSHub> listOfSafeties) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteSafety(InforContext context, String safetyCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    EntitySafety getEntitySafety(InforContext context, String safetyCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String syncEntitySafety(InforContext context, EntitySafetyWSHub entitySafetywshub) throws InforException;

//    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_DELETE, logDataReference1 = LogDataReferenceType.RESULT)
//    String deleteFromMenuHierarchy(InforContext context, MenuSpecification node) throws InforException;

//    static void validateInputNode(MenuSpecification ms) throws InforException {
//        List<String> path = ms.getMenuPath();
//        String func = ms.getFunctionCode();
//        String userGroup = ms.getForUserGroup();
//
//        if (path == null || func == null || userGroup == null) {
//            throw Tools.generateFault("Menu specifications cannot be null");
//        }
//        if (path.isEmpty()) {
//            throw Tools.generateFault("Path cannot be empty");
//        }
//        if (userGroup.isEmpty()) {
//            throw Tools.generateFault("User group cannot be empty");
//        }
//    }
}
