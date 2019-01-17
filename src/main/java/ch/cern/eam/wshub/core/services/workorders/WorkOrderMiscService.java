package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.MaterialList;
import ch.cern.eam.wshub.core.services.workorders.entities.MeterReading;
import ch.cern.eam.wshub.core.services.workorders.entities.RouteEquipment;
import ch.cern.eam.wshub.core.services.workorders.entities.TaskPlan;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.services.entities.WorkOrderPart;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderAdditionalCosts;

public interface WorkOrderMiscService {
    @Operation(logOperation = INFOR_OPERATION.METERRREAD_C)
    String createMeterReading(InforContext context, MeterReading meterReadingParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_ADDCOST_C)
    String createWorkOrderAdditionalCost(InforContext context, WorkOrderAdditionalCosts workOrderAddCostsParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_MATLIST_C)
    String createMaterialList(InforContext context, MaterialList materialList) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WOPART_C)
    String addWorkOrderPart(InforContext context, WorkOrderPart workOrderPart) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_ROUTEEQ_C)
    String createRouteEquipment(InforContext context, RouteEquipment routeEquipment) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.WO_ROUTEEQ_D)
    String deleteRouteEquipment(InforContext context, RouteEquipment routeEquipment) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.TASKPLAN_C)
    String createTaskPlan(InforContext context, TaskPlan taskPlan) throws InforException;
}
