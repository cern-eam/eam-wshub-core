package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.workorders.entities.EntitySafety;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;

import java.util.List;


public interface SafetyService {
    enum ENTITY_TYPE {Workorder, Equipment};

    String GRID_EQUIPMENT = "OSOBJA_ESF";
    String GRID_WORKORDER = "WSJOBS_KSF";
    Integer GRID_ROW_COUNT = 50;
    String GRID_WO_TYPE = "BR";

    @Operation(logOperation = INFOR_OPERATION.SAFETY_GET_LIST, logDataReference1 = LogDataReferenceType.RESULT)
    List<String> getSafetiesIDList(InforContext context, String entityID, ENTITY_TYPE entityType) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_GET, logDataReference1 = LogDataReferenceType.RESULT)
    net.datastream.schemas.mp_entities.entitysafety_001.EntitySafety getEntitySafety(InforContext context, String safetyCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_GET_BATCH, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<net.datastream.schemas.mp_entities.entitysafety_001.EntitySafety> getEntitySafetiesBatch(InforContext context, List<String> safetyCodes) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addSafety(InforContext context, EntitySafety safety) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_ADD_MULTIPLE, logDataReference1 = LogDataReferenceType.RESULT)
    List<String> addSafeties(InforContext context, List<EntitySafety> listOfSafeties) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addSafetyToEntity(InforContext context, EntitySafety entitySafetywshub, String parentID, String entity) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_ADD_BATCH, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> addSafetyToEntitiesBatch(InforContext context, EntitySafety entitySafetywshub, List<String> parentIDs, String entity) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_DELETE, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteSafety(InforContext context, String safetyCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_DELETE_BATCH, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> deleteSafetiesBatch(InforContext context, List<String> safetyCodes) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_SYNC, logDataReference1 = LogDataReferenceType.RESULT)
    String syncEntitySafety(InforContext context, EntitySafety entitySafetywshub) throws InforException;

    static void validateInput(EntitySafety input) throws InforException {
        if (input == null) {
            throw Tools.generateFault("Input safety object cannot be null");
        }
    }

    static void validateInput(List<EntitySafety> input) throws InforException {
        if (input == null) {
            throw Tools.generateFault("Input list cannot be null");
        }

        if (input.isEmpty()) {
            throw Tools.generateFault("Input list cannot be empty");
        }
    }

    static void validateInput(String input) throws InforException {
        if (input == null) {
            throw Tools.generateFault("Safety code cannot be null");
        }
    }

    static void validateInput(String entityID, ENTITY_TYPE entityType) throws InforException {
        if (entityID == null || entityType == null) {
            throw Tools.generateFault("entityID or entityType cannot be null");
        }
    }
}
