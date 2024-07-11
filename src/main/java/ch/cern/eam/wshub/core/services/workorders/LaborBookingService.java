package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.Activity;
import ch.cern.eam.wshub.core.services.workorders.entities.LaborBooking;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.util.List;

public interface LaborBookingService {

    @Operation(logOperation = EAM_OPERATION.LABOR_BOK_R)
    List<LaborBooking> readLaborBookings(EAMContext context, String workOrderNumber) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.LABOR_BOK_C)
    String createLaborBooking(EAMContext context, LaborBooking laborBookingParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.ACTIVITY_R)
    Activity[] readActivities(EAMContext context, String workOrderNumbers, Boolean includeChecklists) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.ACTIVITY_C)
    String createActivity(EAMContext context, Activity activityParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.ACTIVITY_U)
    String updateActivity(EAMContext context, Activity activityParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.ACTIVITY_U)
    String updateActivity(EAMContext context, Activity activityParam, String confirmDeleteChecklist) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.ACTIVITY_D)
    String deleteActivity(EAMContext context, Activity activityParam) throws EAMException;
}
