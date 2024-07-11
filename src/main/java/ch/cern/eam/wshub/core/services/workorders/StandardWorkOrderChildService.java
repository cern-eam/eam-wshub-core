package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.StandardWorkOrder;
import ch.cern.eam.wshub.core.services.workorders.entities.StandardWorkOrderChild;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface StandardWorkOrderChildService {

    @Operation(logOperation = EAM_OPERATION.STANDARDWOCHILD_READ_DEFAULT)
    StandardWorkOrder readStandardWorkOrderChildDefault(EAMContext context, String standardWOCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.STANDARDWOCHILD_CREATE)
    StandardWorkOrderChild createStandardWorkOrderChild(EAMContext context, StandardWorkOrderChild standardWorkOrderChild) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.STANDARDWOCHILD_READ)
    StandardWorkOrderChild readStandardWorkOrderChild(EAMContext context, StandardWorkOrderChild standardWorkOrderChild) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.STANDARDWOCHILD_UPDATE)
    StandardWorkOrderChild updateStandardWorkOrderChild(EAMContext context, StandardWorkOrderChild standardWorkOrderChild) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.STANDARDWOCHILD_DELETE)
    StandardWorkOrderChild deleteStandardWorkOrderChild(EAMContext context, StandardWorkOrderChild standardWorkOrderChild) throws EAMException;
}
