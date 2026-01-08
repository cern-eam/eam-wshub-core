package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.ChargeDefinition;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface ChargeDefinitionService {

    @Operation(logOperation = EAM_OPERATION.CHARGEDEFINITION_CREATE)
    String createChargeDefinition(EAMContext context, ChargeDefinition chargeDefinition) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.CHARGEDEFINITION_READ)
    ChargeDefinition getChargeDefinition(EAMContext context, String chargeDefinitionId) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.CHARGEDEFINITION_UPDATE)
    String updateChargeDefinition(EAMContext context, ChargeDefinition chargeDefinition) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.CHARGEDEFINITION_DELETE)
    String deleteChargeDefinition(EAMContext context, String chargeDefinitionId) throws EAMException;
}
