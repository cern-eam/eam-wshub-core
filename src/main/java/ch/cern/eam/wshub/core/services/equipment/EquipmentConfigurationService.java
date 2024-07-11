package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfigurationEntity;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfigurationId;
import ch.cern.eam.wshub.core.tools.EAMException;
import net.datastream.schemas.mp_entities.equipmentconfigurationdefault_001.EquipmentConfigurationDefault;

import java.util.List;

public interface EquipmentConfigurationService {

    @Operation(logOperation = EAM_OPERATION.EQP_CONF_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createEquipmentConfiguration(EAMContext context, EquipmentConfigurationEntity equipmentConfiguration) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_CONF_R, logDataReference1 = LogDataReferenceType.INPUT)
    EquipmentConfigurationEntity readEquipmentConfiguration(EAMContext context, EquipmentConfigurationId equipmentConfigurationId) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_CONF_U, logDataReference1 = LogDataReferenceType.INPUT)
    String updateEquipmentConfiguration(EAMContext context, EquipmentConfigurationEntity equipmentConfiguration) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_CONF_D, logDataReference1 = LogDataReferenceType.INPUT)
    String deleteEquipmentConfiguration(EAMContext context, EquipmentConfigurationId equipmentConfigurationId) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_CONF_RD, logDataReference1 = LogDataReferenceType.RESULT)
    EquipmentConfigurationDefault readEquipmentDefaultConfiguration(EAMContext context) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_CONF_BC, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> createEquipmentConfigurationBatch(EAMContext context, List<EquipmentConfigurationEntity> equipmentConfigurationList) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_CONF_BR, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<EquipmentConfigurationEntity> readEquipmentConfigurationBatch(EAMContext context, List<EquipmentConfigurationId> equipmentConfigurationIdList) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_CONF_BU, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<String> updateEquipmentConfigurationBatch(EAMContext context, List<EquipmentConfigurationEntity> equipmentConfigurationList) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_CONF_BD, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<String> deleteEquipmentConfigurationBatch(EAMContext context, List<EquipmentConfigurationId> equipmentConfigurationIdList) throws EAMException;

}
