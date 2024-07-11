package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.util.List;

public interface EquipmentFacadeService {
    //
    // BATCH
    //
    @Operation(logOperation = EAM_OPERATION.EQUIPMENT_BC)
    BatchResponse<String> createEquipmentBatch(EAMContext eamContext, List<Equipment> equipmentList)
            throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQUIPMENT_BR)
    BatchResponse<Equipment> readEquipmentBatch(EAMContext eamContext, List<String> equipmentCodes);

    @Operation(logOperation = EAM_OPERATION.EQUIPMENT_BU)
    BatchResponse<String> updateEquipmentBatch(EAMContext eamContext, List<Equipment> equipmentList)
                    throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQUIPMENT_BD)
    BatchResponse<String> deleteEquipmentBatch(EAMContext eamContext, List<String> equipmentCodes)
            throws EAMException;

    //
    // CRUD
    //
    @Operation(logOperation = EAM_OPERATION.EQUIPMENT_C, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String createEquipment(EAMContext eamContext, Equipment equipment) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQUIPMENT_R, logDataReference1 = LogDataReferenceType.INPUT)
    Equipment readEquipment(EAMContext eamContext, String equipmentCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQUIPMENT_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "code")
    String updateEquipment(EAMContext eamContext, Equipment equipment) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQUIPMENT_D, logDataReference1 = LogDataReferenceType.INPUT)
    String deleteEquipment(EAMContext eamContext, String equipmentCode) throws EAMException;


    String readEquipmentType(EAMContext eamContext, String equipmentCode) throws EAMException;
}
