package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.Route;
import ch.cern.eam.wshub.core.tools.InforException;

public interface RouteService {

    @Operation(logOperation = INFOR_OPERATION.ROUTE_R)
    Route readRoute(InforContext inforContext, String routeCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.ROUTE_C)
    String createRoute(InforContext inforContext, Route route) throws InforException;

}
