package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.administration.entities.MenuSpecification;
import ch.cern.eam.wshub.core.services.workorders.entities.MEC;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;

import java.util.List;

public interface MECService {

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String getWorkOrderEquipment(InforContext context, String equipmentID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String getWorkOrderEquipmentOfWorkorder(InforContext context, String equipmentID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addWorkOrderEquipment(InforContext context, String equipmentID, MEC mecDetails) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteWorkOrderEquipment(InforContext context, String parentWorkorderID, String equipmentID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String syncWorkOrderEquipment(InforContext context, String equipmentID) throws InforException;

    static void validateInputNode(MenuSpecification ms) throws InforException {
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
    }
}
