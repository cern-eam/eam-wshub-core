package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentWarranty;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface EquipmentWarrantyCoverageService {

    @Operation(logOperation = EAM_OPERATION.EQP_WARR_C)
    String createEquipmentWarrantyCoverage(EAMContext context, EquipmentWarranty equipmentWarrantyParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_WARR_U)
    String updateEquipmentWarrantyCoverage(EAMContext context, EquipmentWarranty equipmentWarrantyParam) throws EAMException;
}
