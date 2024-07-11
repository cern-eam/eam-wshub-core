package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.StandardWorkOrder;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface StandardWorkOrderService {

    @Operation(logOperation = EAM_OPERATION.STANDARDWO_READ)
    StandardWorkOrder readStandardWorkOrder(EAMContext context, String code) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.STANDARDWO_CREATE)
    String createStandardWorkOrder(EAMContext context, StandardWorkOrder standardWorkOrder) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.STANDARDWO_UPDATE)
    String updateStandardWorkOrder(EAMContext context, StandardWorkOrder standardWorkOrder) throws EAMException;

}
