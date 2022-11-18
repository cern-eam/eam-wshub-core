package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.workorders.entities.Route;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.workroute_001.WorkRoute;
import net.datastream.schemas.mp_fields.ROUTE_Type;
import net.datastream.schemas.mp_functions.mp7063_001.MP7063_AddWorkRoute_001;
import net.datastream.schemas.mp_functions.mp7064_001.MP7064_GetWorkRoute_001;
import net.datastream.schemas.mp_results.mp7063_001.MP7063_AddWorkRoute_001_Result;
import net.datastream.schemas.mp_results.mp7064_001.MP7064_GetWorkRoute_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import ch.cern.eam.wshub.core.services.workorders.RouteService;

public class RouteServiceImpl implements RouteService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public RouteServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    public Route readRoute(InforContext inforContext, String routeCode) throws InforException {
        MP7064_GetWorkRoute_001 getWorkRoute = new MP7064_GetWorkRoute_001();
        getWorkRoute.setROUTEID(new ROUTE_Type());

        MP7064_GetWorkRoute_001_Result result = tools.performInforOperation(inforContext, inforws::getWorkRouteOp, getWorkRoute);
        return tools.getInforFieldTools().transformInforObject(new Route(), result.getResultData().getWorkRoute(), inforContext);
    }

    public String createRoute(InforContext inforContext, Route route) throws InforException {
        WorkRoute workRoute = new WorkRoute();
        tools.getInforFieldTools().transformWSHubObject(workRoute, route, inforContext);

        MP7063_AddWorkRoute_001 addWorkRoute = new MP7063_AddWorkRoute_001();
        addWorkRoute.setWorkRoute(workRoute);

        MP7063_AddWorkRoute_001_Result result = tools.performInforOperation(inforContext, inforws::addWorkRouteOp, addWorkRoute);
        return result.getResultData().getROUTEID().getROUTECODE();
    }

}
