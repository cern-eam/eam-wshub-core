package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.Activity;
import ch.cern.eam.wshub.core.services.workorders.entities.LaborBooking;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface LaborBookingService {

    @Operation(logOperation = INFOR_OPERATION.LABOR_BOK_R)
    List<LaborBooking> readLaborBookings(InforContext context, String workOrderNumber) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.LABOR_BOK_C)
    String createLaborBooking(InforContext context, LaborBooking laborBookingParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.ACTIVITY_R)
    Activity[] readActivities(InforContext context, String workOrderNumbers, Boolean includeChecklists) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.ACTIVITY_C)
    String createActivity(InforContext context, Activity activityParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.ACTIVITY_U)
    String updateActivity(InforContext context, Activity activityParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.ACTIVITY_D)
    String deleteActivity(InforContext context, Activity activityParam) throws InforException;
}
