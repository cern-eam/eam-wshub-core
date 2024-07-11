package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.comments.CommentService;
import ch.cern.eam.wshub.core.services.comments.impl.CommentServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.StandardWorkOrderService;
import ch.cern.eam.wshub.core.services.workorders.entities.StandardWorkOrder;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.workorder_001.UserDefinedFields;
import net.datastream.schemas.mp_fields.STDWOID_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0023_001.MP0023_AddWorkOrder_001;
import net.datastream.schemas.mp_functions.mp7079_001.MP7079_AddStandardWorkOrder_001;
import net.datastream.schemas.mp_functions.mp7080_001.MP7080_SyncStandardWorkOrder_001;
import net.datastream.schemas.mp_functions.mp7082_001.MP7082_GetStandardWorkOrder_001;
import net.datastream.schemas.mp_results.mp0023_001.MP0023_AddWorkOrder_001_Result;
import net.datastream.schemas.mp_results.mp7079_001.MP7079_AddStandardWorkOrder_001_Result;
import net.datastream.schemas.mp_results.mp7082_001.MP7082_GetStandardWorkOrder_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import jakarta.xml.ws.Holder;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;

public class StandardWorkOrderServiceImpl implements StandardWorkOrderService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public StandardWorkOrderServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
    }

    public StandardWorkOrder readStandardWorkOrder(EAMContext context, String number) throws EAMException {
        return tools.getEAMFieldTools().transformEAMObject(new StandardWorkOrder(), readStandardWorkOrderEAM(context, number), context);
    }

    public net.datastream.schemas.mp_entities.standardworkorder_001.StandardWorkOrder readStandardWorkOrderEAM(EAMContext context, String number) throws EAMException {
        //
        // Fetch WO
        //
        MP7082_GetStandardWorkOrder_001 getStandardWorkOrder = new MP7082_GetStandardWorkOrder_001();

        getStandardWorkOrder.setSTANDARDWO(new STDWOID_Type());
        getStandardWorkOrder.getSTANDARDWO().setORGANIZATIONID(tools.getOrganization(context));
        getStandardWorkOrder.getSTANDARDWO().setSTDWOCODE(number);

        MP7082_GetStandardWorkOrder_001_Result result =
            tools.performEAMOperation(context, eamws::getStandardWorkOrderOp, getStandardWorkOrder);

        return result.getResultData().getStandardWorkOrder();
    }


    public String createStandardWorkOrder(EAMContext context, StandardWorkOrder standardWorkOrder) throws EAMException {

        MP7079_AddStandardWorkOrder_001 addStandardWorkOrder = new MP7079_AddStandardWorkOrder_001();
        addStandardWorkOrder.setStandardWorkOrder(new net.datastream.schemas.mp_entities.standardworkorder_001.StandardWorkOrder());

        tools.getEAMFieldTools().transformWSHubObject(addStandardWorkOrder.getStandardWorkOrder(), standardWorkOrder, context);

        MP7079_AddStandardWorkOrder_001_Result result =
            tools.performEAMOperation(context, eamws::addStandardWorkOrderOp, addStandardWorkOrder);

        return result.getResultData().getSTANDARDWO().getSTDWOCODE();
    }

    public String updateStandardWorkOrder(EAMContext context, StandardWorkOrder standardWorkOrder) throws EAMException {
        net.datastream.schemas.mp_entities.standardworkorder_001.StandardWorkOrder eamStandardWorkOrder = readStandardWorkOrderEAM(context, standardWorkOrder.getCode());

        // Check Custom fields. If they change, or now we have them
//        eamStandardWorkOrder.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
//            context,
//            toCodeString(eamStandardWorkOrder.getCLASSID()),
//            eamStandardWorkOrder.getUSERDEFINEDAREA(),
//            standardWorkOrder.getClassCode(),
//            "STWO"));

        tools.getEAMFieldTools().transformWSHubObject(eamStandardWorkOrder, standardWorkOrder, context);

        //
        // CALL INFOR WEB SERVICE
        //
        MP7080_SyncStandardWorkOrder_001 syncStandardWorkOrder = new MP7080_SyncStandardWorkOrder_001();
        syncStandardWorkOrder.setStandardWorkOrder(eamStandardWorkOrder);

        tools.performEAMOperation(context, eamws::syncStandardWorkOrderOp, syncStandardWorkOrder);

        return eamStandardWorkOrder.getSTANDARDWO().getSTDWOCODE();
    }


}
