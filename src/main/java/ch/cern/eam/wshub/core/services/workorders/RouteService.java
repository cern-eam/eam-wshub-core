package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.Route;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface RouteService {

    @Operation(logOperation = EAM_OPERATION.ROUTE_R)
    Route readRoute(EAMContext eamContext, String routeCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.ROUTE_C)
    String createRoute(EAMContext eamContext, Route route) throws EAMException;

}
