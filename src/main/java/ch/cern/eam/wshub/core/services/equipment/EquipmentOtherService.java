package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentCampaign;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentDepreciation;
import ch.cern.eam.wshub.core.tools.InforException;

public interface EquipmentOtherService {


    @Operation(logOperation = INFOR_OPERATION.EQP_DEPR_C)
    String createEquipmentDepreciation(InforContext context, EquipmentDepreciation equipmentDepreciation) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_DEPR_U)
    String updateEquipmentDepreciation(InforContext context, EquipmentDepreciation equipmentDepreciation) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_CODE_U)
    String updateEquipmentCode(InforContext context, String equipmentCode, String equipmentNewCode, String equipmentType) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_CAMP_C)
    String createEquipmentCampaign(InforContext context, EquipmentCampaign equipmentCampaign) throws InforException;
}
