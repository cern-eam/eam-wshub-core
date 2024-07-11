package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.LinearReference;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface LinearReferenceService {

    @Operation(logOperation = EAM_OPERATION.EQP_LINREF_U)
    String updateEquipmentLinearReference(EAMContext context, LinearReference linearReference) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_LINREF_D)
    String deleteEquipmentLinearReference(EAMContext context, String linearReferenceID) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EQP_LINREF_C)
    String createEquipmentLinearReference(EAMContext context, LinearReference linearReference) throws EAMException;
}
