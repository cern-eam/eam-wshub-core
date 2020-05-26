package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.workorders.entities.MEC;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment;

import java.util.List;

public interface MECService {

    String EQUIPMENT_COLUMN_NAME = "equipment";
    String MEC_ID_COLUMN_NAME = "relatedwo";
    String GRID_ID = "WSJOBS_MEC";
    String GRID_WO_TYPE = "BR";

    @Operation(logOperation = INFOR_OPERATION.MEC_GET, logDataReference1 = LogDataReferenceType.RESULT)
    List<String> getWorkOrderMecIDList(InforContext context, String workorderID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MEC_ADD_BATCH, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> addWorkOrderEquipmentBatch(InforContext context, List<MEC> mecsToAdd) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MEC_GET_INFOR, logDataReference1 = LogDataReferenceType.RESULT)
    WorkOrderEquipment getWorkOrderMecInfor(InforContext context, String workorderID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MEC_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addWorkOrderEquipment(InforContext context, MEC mecToAdd) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MEC_DELETE, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteWorkOrderMEC(InforContext context, String parentWorkorderID, String mecID) throws InforException;

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

    static void validateInput(String parentWorkorderID, String mecID) throws InforException {
        if (parentWorkorderID == null || mecID == null) {
            throw Tools.generateFault("parentWorkorderID or mecID cannot be null");
        }
    }

    static void validateInput(String parentWorkorderID, String mecID, MEC mecProperties) throws InforException {
        if (parentWorkorderID == null || mecID == null || mecProperties == null) {
            throw Tools.generateFault("workorderID, mecID or mecProperties cannot be null");
        }
    }
}
