package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.MaterialList;
import ch.cern.eam.wshub.core.services.workorders.entities.MeterReading;
import ch.cern.eam.wshub.core.services.workorders.entities.RouteEquipment;
import ch.cern.eam.wshub.core.services.workorders.entities.TaskPlan;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.services.entities.WorkOrderPart;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderAdditionalCosts;
import net.datastream.schemas.mp_results.mp7336_001.AdditionalWOEquipDetails;

public interface WorkOrderMiscService {
    @Operation(logOperation = EAM_OPERATION.METERRREAD_C)
    String createMeterReading(EAMContext context, MeterReading meterReadingParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WO_ADDCOST_C)
    String createWorkOrderAdditionalCost(EAMContext context, WorkOrderAdditionalCosts workOrderAddCostsParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WO_MATLIST_C)
    String createMaterialList(EAMContext context, MaterialList materialList) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WOPART_C)
    String addWorkOrderPart(EAMContext context, WorkOrderPart workOrderPart) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WO_ROUTEEQ_C)
    String createRouteEquipment(EAMContext context, RouteEquipment routeEquipment) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WO_ROUTEEQ_D)
    String deleteRouteEquipment(EAMContext context, RouteEquipment routeEquipment) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.TASKPLAN_C)
    String createTaskPlan(EAMContext context, TaskPlan taskPlan) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.WO_EQP_LINEAR_DETAILS_R)
    AdditionalWOEquipDetails getEquipLinearDetails(EAMContext context, String eqCode) throws EAMException;
}
