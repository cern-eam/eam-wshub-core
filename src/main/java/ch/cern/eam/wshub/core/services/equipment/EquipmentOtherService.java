package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentCampaign;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentDepreciation;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface EquipmentOtherService {


    @Operation(logOperation = EAM_OPERATION.EQP_DEPR_C)
    String createEquipmentDepreciation(EAMContext context, EquipmentDepreciation equipmentDepreciation) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_DEPR_U)
    String updateEquipmentDepreciation(EAMContext context, EquipmentDepreciation equipmentDepreciation) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_CODE_U)
    String updateEquipmentCode(EAMContext context, String equipmentCode, String equipmentNewCode, String equipmentType) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_CAMP_C)
    String createEquipmentCampaign(EAMContext context, EquipmentCampaign equipmentCampaign) throws EAMException;
}
