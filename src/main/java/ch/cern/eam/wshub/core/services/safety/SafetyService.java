package ch.cern.eam.wshub.core.services.safety;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.safety.entities.EntitySafetyWSHub;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.entitysafety_001.EntitySafety;

import java.util.List;


public interface SafetyService {
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

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addSafety(InforContext context, EntitySafetyWSHub entitySafetywshub, String parentID, String entity) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.MENU_HIERARCHY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> addSafetiesBatch(InforContext context, EntitySafetyWSHub entitySafetywshub, List<String> parentIDs, String entity) throws InforException;


        static void validateInput(EntitySafetyWSHub input) throws InforException {
        if (input == null) {
            throw Tools.generateFault("Input safety object cannot be null");
        }
    }

    static void validateInput(List<EntitySafetyWSHub> input) throws InforException {
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
}
