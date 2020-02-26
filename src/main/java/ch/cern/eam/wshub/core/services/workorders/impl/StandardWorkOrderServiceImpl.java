package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.comments.CommentService;
import ch.cern.eam.wshub.core.services.comments.impl.CommentServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.StandardWorkOrderService;
import ch.cern.eam.wshub.core.services.workorders.entities.StandardWorkOrder;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
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
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.xml.ws.Holder;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;

public class StandardWorkOrderServiceImpl implements StandardWorkOrderService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public StandardWorkOrderServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    public StandardWorkOrder readStandardWorkOrder(InforContext context, String number) throws InforException {
        return tools.getInforFieldTools().transformInforObject(new StandardWorkOrder(), readStandardWorkOrderInfor(context, number));
    }

    public net.datastream.schemas.mp_entities.standardworkorder_001.StandardWorkOrder readStandardWorkOrderInfor(InforContext context, String number) throws InforException {
        //
        // Fetch WO
        //
        MP7082_GetStandardWorkOrder_001 getStandardWorkOrder = new MP7082_GetStandardWorkOrder_001();

        getStandardWorkOrder.setSTANDARDWO(new STDWOID_Type());
        getStandardWorkOrder.getSTANDARDWO().setORGANIZATIONID(tools.getOrganization(context));
        getStandardWorkOrder.getSTANDARDWO().setSTDWOCODE(number);

        MP7082_GetStandardWorkOrder_001_Result result = null;
        if (context.getCredentials() != null) {
            result = inforws.getStandardWorkOrderOp(getStandardWorkOrder, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), tools.getTenant(context));
        } else {
            result = inforws.getStandardWorkOrderOp(getStandardWorkOrder, tools.getOrganizationCode(context), null, "",
                    new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
        }

        return result.getResultData().getStandardWorkOrder();
    }


    public String createStandardWorkOrder(InforContext context, StandardWorkOrder standardWorkOrder) throws InforException {

        MP7079_AddStandardWorkOrder_001 addStandardWorkOrder = new MP7079_AddStandardWorkOrder_001();
        addStandardWorkOrder.setStandardWorkOrder(new net.datastream.schemas.mp_entities.standardworkorder_001.StandardWorkOrder());

        tools.getInforFieldTools().transformWSHubObject(addStandardWorkOrder.getStandardWorkOrder(), standardWorkOrder, context);

        MP7079_AddStandardWorkOrder_001_Result result = new MP7079_AddStandardWorkOrder_001_Result();

        if (context.getCredentials() != null) {
             result = inforws.addStandardWorkOrderOp(addStandardWorkOrder, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), tools.getTenant(context));
        } else {
             result = inforws.addStandardWorkOrderOp(addStandardWorkOrder, tools.getOrganizationCode(context), null, "",
                    new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
        }

        return result.getResultData().getSTANDARDWO().getSTDWOCODE();
    }

    public String updateStandardWorkOrder(InforContext context, StandardWorkOrder standardWorkOrder) throws InforException {
        net.datastream.schemas.mp_entities.standardworkorder_001.StandardWorkOrder inforStandardWorkOrder = readStandardWorkOrderInfor(context, standardWorkOrder.getCode());

        // Check Custom fields. If they change, or now we have them
        inforStandardWorkOrder.setUSERDEFINEDAREA(tools.getInforCustomFields(
            context,
            toCodeString(inforStandardWorkOrder.getCLASSID()),
            inforStandardWorkOrder.getUSERDEFINEDAREA(),
            standardWorkOrder.getClassCode(),
            "EVNT"));

        tools.getInforFieldTools().transformWSHubObject(inforStandardWorkOrder, standardWorkOrder, context);

        //
        // CALL INFOR WEB SERVICE
        //
        MP7080_SyncStandardWorkOrder_001 syncStandardWorkOrder = new MP7080_SyncStandardWorkOrder_001();
        syncStandardWorkOrder.setStandardWorkOrder(inforStandardWorkOrder);

        if (context.getCredentials() != null) {
            inforws.syncStandardWorkOrderOp(syncStandardWorkOrder, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    tools.createMessageConfig(), tools.getTenant(context));
        } else {
            inforws.syncStandardWorkOrderOp(syncStandardWorkOrder, tools.getOrganizationCode(context), null, null,
                    new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
        }

        return inforStandardWorkOrder.getSTANDARDWO().getSTDWOCODE();
    }


}
