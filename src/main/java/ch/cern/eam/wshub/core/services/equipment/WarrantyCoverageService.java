package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentWarranty;
import ch.cern.eam.wshub.core.tools.InforException;

public interface WarrantyCoverageService {

    @Operation(logOperation = INFOR_OPERATION.EQP_WARR_C)
    String createEquipmentWarrantyCoverage(InforContext context, EquipmentWarranty equipmentWarrantyParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_WARR_U)
    String updateEquipmentWarrantyCoverage(InforContext context, EquipmentWarranty equipmentWarrantyParam) throws InforException;
}
