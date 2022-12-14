package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface EquipmentFacadeService {
    //
    // BATCH
    //
    @Operation(logOperation = INFOR_OPERATION.EQUIPMENT_BC)
    BatchResponse<String> createEquipmentBatch(InforContext inforContext, List<Equipment> equipmentList)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQUIPMENT_BR)
    BatchResponse<Equipment> readEquipmentBatch(InforContext inforContext, List<String> equipmentCodes);

    @Operation(logOperation = INFOR_OPERATION.EQUIPMENT_BU)
    BatchResponse<String> updateEquipmentBatch(InforContext inforContext, List<Equipment> equipmentList)
                    throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQUIPMENT_BD)
    BatchResponse<String> deleteEquipmentBatch(InforContext inforContext, List<String> equipmentCodes)
            throws InforException;

    //
    // CRUD
    //
    @Operation(logOperation = INFOR_OPERATION.EQUIPMENT_C, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String createEquipment(InforContext inforContext, Equipment equipment) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQUIPMENT_R, logDataReference1 = LogDataReferenceType.INPUT)
    Equipment readEquipment(InforContext inforContext, String equipmentCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQUIPMENT_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String updateEquipment(InforContext inforContext, Equipment equipment) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQUIPMENT_D, logDataReference1 = LogDataReferenceType.INPUT)
    String deleteEquipment(InforContext inforContext, String equipmentCode) throws InforException;


    String readEquipmentType(InforContext inforContext, String equipmentCode) throws InforException;
}
