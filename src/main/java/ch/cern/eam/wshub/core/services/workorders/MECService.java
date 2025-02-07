package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.workorders.entities.MEC;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.workorderequipment_001.WorkOrderEquipment;

import java.util.List;

public interface MECService {

    String EQUIPMENT_COLUMN_NAME = "equipment";
    String MEC_ID_COLUMN_NAME = "relatedwo";
    String GRID_ID = "WSJOBS_MEC";
    String GRID_WO_TYPE = "BR";

    @Operation(logOperation = EAM_OPERATION.MEC_GET, logDataReference1 = LogDataReferenceType.RESULT)
    List<String> getWorkOrderMecIDList(EAMContext context, String workorderID) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.MEC_ADD_BATCH, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> addWorkOrderEquipmentBatch(EAMContext context, List<MEC> mecsToAdd) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.MEC_GET_EAM, logDataReference1 = LogDataReferenceType.RESULT)
    WorkOrderEquipment getWorkOrderMecEAM(EAMContext context, String workorderID) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.MEC_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addWorkOrderEquipment(EAMContext context, MEC mecToAdd) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.MEC_DELETE, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteWorkOrderMEC(EAMContext context, String parentWorkorderID, String mecID) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.MEC_SYNC, logDataReference1 = LogDataReferenceType.RESULT)
    String syncWorkOrderEquipment(EAMContext context, MEC updatedMEC) throws EAMException;

    static void validateInput(String workOrderID) throws EAMException {
        if (workOrderID == null) {
            throw Tools.generateFault("workOrderID or mecProperties cannot be null");
        }
    }

    static void validateInput(MEC mecToAdd) throws EAMException {
        if (mecToAdd == null) {
            throw Tools.generateFault("mecToAdd cannot be null");
        }
    }

    static void validateInput(String parentWorkorderID, String mecID) throws EAMException {
        if (parentWorkorderID == null || mecID == null) {
            throw Tools.generateFault("parentWorkorderID or mecID cannot be null");
        }
    }

    static void validateInput(String parentWorkorderID, String mecID, MEC mecProperties) throws EAMException {
        if (parentWorkorderID == null || mecID == null || mecProperties == null) {
            throw Tools.generateFault("workorderID, mecID or mecProperties cannot be null");
        }
    }
}
