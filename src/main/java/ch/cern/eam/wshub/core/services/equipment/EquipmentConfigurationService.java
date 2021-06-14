package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfigurationEntity;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentConfigurationId;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.InforException;
import net.datastream.schemas.mp_entities.equipmentconfigurationdefault_001.EquipmentConfigurationDefault;

import java.math.BigDecimal;
import java.util.List;

public interface EquipmentConfigurationService {

    @Operation(logOperation = INFOR_OPERATION.EQP_CONF_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createEquipmentConfiguration(InforContext context, EquipmentConfigurationEntity equipmentConfiguration) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_CONF_R, logDataReference1 = LogDataReferenceType.INPUT)
    EquipmentConfigurationEntity readEquipmentConfiguration(InforContext context, EquipmentConfigurationId equipmentConfigurationId) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_CONF_U, logDataReference1 = LogDataReferenceType.INPUT)
    String updateEquipmentConfiguration(InforContext context, EquipmentConfigurationEntity equipmentConfiguration) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_CONF_D, logDataReference1 = LogDataReferenceType.INPUT)
    String deleteEquipmentConfiguration(InforContext context, EquipmentConfigurationId equipmentConfigurationId) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_CONF_RD, logDataReference1 = LogDataReferenceType.RESULT)
    EquipmentConfigurationDefault readEquipmentDefaultConfiguration(InforContext context) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_CONF_BC, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> createEquipmentConfigurationBatch(InforContext context, List<EquipmentConfigurationEntity> equipmentConfigurationList) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_CONF_BR, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<EquipmentConfigurationEntity> readEquipmentConfigurationBatch(InforContext context, List<EquipmentConfigurationId> equipmentConfigurationIdList) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_CONF_BU, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<String> updateEquipmentConfigurationBatch(InforContext context, List<EquipmentConfigurationEntity> equipmentConfigurationList) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_CONF_BD, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<String> deleteEquipmentConfigurationBatch(InforContext context, List<EquipmentConfigurationId> equipmentConfigurationIdList) throws InforException;

}
