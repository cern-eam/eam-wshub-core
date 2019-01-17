package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.equipment.entities.LinearReference;
import ch.cern.eam.wshub.core.tools.InforException;

public interface LinearReferenceService {

    @Operation(logOperation = INFOR_OPERATION.EQP_LINREF_U)
    String updateEquipmentLinearReference(InforContext context, LinearReference linearReference) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_LINREF_D)
    String deleteEquipmentLinearReference(InforContext context, String linearReferenceID) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EQP_LINREF_C)
    String createEquipmentLinearReference(InforContext context, LinearReference linearReference) throws InforException;
}
