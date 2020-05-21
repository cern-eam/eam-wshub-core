package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.workorders.entities.MEC;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;

public interface MECService {

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    GridRequestResult getWorkOrderEquipmentsOfWorkorder(InforContext context, String workorderID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addWorkOrderEquipment(InforContext context, String equipmentID, MEC mecDetails) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteWorkOrderEquipment(InforContext context, String parentWorkorderID, String equipmentID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String syncWorkOrderEquipment(InforContext context, String parentWorkorderID, String equipmentID, MEC mecProperties) throws InforException;


    static void validateInput(String workOrderID, MEC mecProperties) throws InforException {
        if (workOrderID == null || mecProperties == null) {
            throw Tools.generateFault("workOrderID or mecProperties cannot be null");
        }
    }

    static void validateInput(String parentWorkorderID, String equipmentID) throws InforException {
        if (parentWorkorderID == null || equipmentID == null) {
            throw Tools.generateFault("parentWorkorderID or equipmentID cannot be null");
        }
    }

    static void validateInput(String parentWorkorderID, String equipmentID, MEC mecProperties) throws InforException {
        if (parentWorkorderID == null || equipmentID == null || mecProperties == null) {
            throw Tools.generateFault("workorderID cannot be null");
        }
    }
}
