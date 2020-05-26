package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.workorders.entities.EntitySafetyWSHub;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.entitysafety_001.EntitySafety;

import java.util.List;


public interface SafetyService {
    enum ENTITY_TYPE {Workorder, Equipment};

    enum GRID_EQUIPMENT {
        ID("OSOBJA_ESF"),
        PARAM1("equipmentno"),
        PARAM2("statusrcode"),
        ARG2("I"),

        OTHER("OTHER")
        ;

        private String code;
        GRID_EQUIPMENT(String code) {
            this.code = code;
        }
        public String getCode() { return code; }
    };

    enum GRID_WORKORDER {
        ID("WSJOBS_KSF"),
        PARAM1("workordernum"),
        PARAM2("workorderrtype"),
        ARG2("BR"),

        OTHER("OTHER")
        ;

        private String code;
        GRID_WORKORDER(String code) {
            this.code = code;
        }
        public String getCode() { return code; }
    };

//    equipmentno: PR-A-001
//    statusrcode: I
//
//    workordernum: 28021934
//    workorderrtype: BR

    Integer GRID_ROW_COUNT = 50;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_GET_LIST, logDataReference1 = LogDataReferenceType.RESULT)
    List<String> getSafetiesIDList(InforContext context, String entityID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_GET, logDataReference1 = LogDataReferenceType.RESULT)
    EntitySafety getEntitySafety(InforContext context, String safetyCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_GET_BATCH, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<EntitySafety> getEntitySafetiesBatch(InforContext context, List<String> safetyCodes) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addSafety(InforContext context, EntitySafetyWSHub safety) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_ADD_MULTIPLE, logDataReference1 = LogDataReferenceType.RESULT)
    List<String> addSafeties(InforContext context, List<EntitySafetyWSHub> listOfSafeties) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_ADD, logDataReference1 = LogDataReferenceType.RESULT)
    String addSafetyToEntity(InforContext context, EntitySafetyWSHub entitySafetywshub, String parentID, String entity) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_ADD_BATCH, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> addSafetyToEntitiesBatch(InforContext context, EntitySafetyWSHub entitySafetywshub, List<String> parentIDs, String entity) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_DELETE, logDataReference1 = LogDataReferenceType.RESULT)
    String deleteSafety(InforContext context, String safetyCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_DELETE_BATCH, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> deleteSafetiesBatch(InforContext context, List<String> safetyCodes) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SAFETY_SYNC, logDataReference1 = LogDataReferenceType.RESULT)
    String syncEntitySafety(InforContext context, EntitySafetyWSHub entitySafetywshub) throws InforException;

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

    static void validateInput(String entityID, ENTITY_TYPE entityType) throws InforException {
        if (entityID == null || entityType == null) {
            throw Tools.generateFault("entityID or entityType cannot be null");
        }
    }
}
