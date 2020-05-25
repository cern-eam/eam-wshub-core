package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.workorders.entities.MEC;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;

import java.util.List;

public interface MECService {

    String EQUIPMENT_COLUMN_NAME = "equipment";
    String MEC_ID_COLUMN_NAME = "relatedwo";
    String GRID_ID = "WSJOBS_MEC";
    String GRID_WO_TYPE = "BR";

    @Operation(logOperation = INFOR_OPERATION.MEC_GET, logDataReference1 = LogDataReferenceType.RESULT)
    List<String> getWorkOrderMecIDList(InforContext context, String workorderID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MEC_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addWorkOrderEquipment(InforContext context, MEC mecToAdd) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MEC_DELETE, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteWorkOrderMEC(InforContext context, String parentWorkorderID, String equipmentID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MEC_SYNC, logDataReference1 = LogDataReferenceType.RESULT)
    String syncWorkOrderEquipment(InforContext context, MEC updatedMEC) throws InforException;

    static void validateInput(String workOrderID) throws InforException {
        if (workOrderID == null) {
            throw Tools.generateFault("workOrderID or mecProperties cannot be null");
        }
    }

    static void validateInput(MEC mecToAdd) throws InforException {
        if (mecToAdd == null) {
            throw Tools.generateFault("mecToAdd cannot be null");
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
