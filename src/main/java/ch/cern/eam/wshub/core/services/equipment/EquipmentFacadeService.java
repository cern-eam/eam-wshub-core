package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
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
    @Operation(logOperation = INFOR_OPERATION.EQUIPMENT_C)
    String createEquipment(InforContext inforContext, Equipment equipment) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQUIPMENT_R)
    Equipment readEquipment(InforContext inforContext, String equipmentCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQUIPMENT_U)
    String updateEquipment(InforContext inforContext, Equipment equipment) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQUIPMENT_D)
    String deleteEquipment(InforContext inforContext, String equipmentCode) throws InforException;


    String readEquipmentType(InforContext inforContext, String equipmentCode) throws InforException;
}
