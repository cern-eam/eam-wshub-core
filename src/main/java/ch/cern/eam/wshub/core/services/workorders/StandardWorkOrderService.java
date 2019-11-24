package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.StandardWorkOrder;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.InforException;

public interface StandardWorkOrderService {

    @Operation(logOperation = INFOR_OPERATION.STANDARDWO_READ)
    StandardWorkOrder readStandardWorkOrder(InforContext context, String code) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.STANDARDWO_CREATE)
    String createStandardWorkOrder(InforContext context, StandardWorkOrder standardWorkOrder) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.STANDARDWO_UPDATE)
    String updateStandardWorkOrder(InforContext context, StandardWorkOrder standardWorkOrder) throws InforException;

}
