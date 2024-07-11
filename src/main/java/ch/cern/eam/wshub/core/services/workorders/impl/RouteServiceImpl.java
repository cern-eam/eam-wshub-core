package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.workorders.entities.Route;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.workroute_001.WorkRoute;
import net.datastream.schemas.mp_fields.ROUTE_Type;
import net.datastream.schemas.mp_functions.mp7063_001.MP7063_AddWorkRoute_001;
import net.datastream.schemas.mp_functions.mp7064_001.MP7064_GetWorkRoute_001;
import net.datastream.schemas.mp_results.mp7063_001.MP7063_AddWorkRoute_001_Result;
import net.datastream.schemas.mp_results.mp7064_001.MP7064_GetWorkRoute_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import ch.cern.eam.wshub.core.services.workorders.RouteService;

public class RouteServiceImpl implements RouteService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public RouteServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
    }

    public Route readRoute(EAMContext eamContext, String routeCode) throws EAMException {
        MP7064_GetWorkRoute_001 getWorkRoute = new MP7064_GetWorkRoute_001();
        getWorkRoute.setROUTEID(new ROUTE_Type());

        MP7064_GetWorkRoute_001_Result result = tools.performEAMOperation(eamContext, eamws::getWorkRouteOp, getWorkRoute);
        return tools.getEAMFieldTools().transformEAMObject(new Route(), result.getResultData().getWorkRoute(), eamContext);
    }

    public String createRoute(EAMContext eamContext, Route route) throws EAMException {
        WorkRoute workRoute = new WorkRoute();
        tools.getEAMFieldTools().transformWSHubObject(workRoute, route, eamContext);

        MP7063_AddWorkRoute_001 addWorkRoute = new MP7063_AddWorkRoute_001();
        addWorkRoute.setWorkRoute(workRoute);

        MP7063_AddWorkRoute_001_Result result = tools.performEAMOperation(eamContext, eamws::addWorkRouteOp, addWorkRoute);
        return result.getResultData().getROUTEID().getROUTECODE();
    }

}
