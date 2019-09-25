package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.PickTicketService;
import ch.cern.eam.wshub.core.services.material.entities.PickTicket;
import ch.cern.eam.wshub.core.services.material.entities.PickTicketPart;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.picklist_001.PickList;
import net.datastream.schemas.mp_entities.picklistpart_001.PickListPart;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0211_001.MP0211_GetPickList_001;
import net.datastream.schemas.mp_functions.mp0296_001.MP0296_AddPickList_001;
import net.datastream.schemas.mp_functions.mp0297_001.MP0297_SyncPickList_001;
import net.datastream.schemas.mp_functions.mp1223_001.MP1223_AddPickListPart_001;
import net.datastream.schemas.mp_results.mp0211_001.MP0211_GetPickList_001_Result;
import net.datastream.schemas.mp_results.mp0296_001.MP0296_AddPickList_001_Result;
import net.datastream.schemas.mp_results.mp0297_001.MP0297_SyncPickList_001_Result;
import net.datastream.schemas.mp_results.mp1223_001.MP1223_AddPickListPart_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import org.openapplications.oagis_segments.QUANTITY;

import javax.xml.ws.Holder;
import java.math.BigDecimal;

public class PickTicketServiceImpl implements PickTicketService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public PickTicketServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    public String createPickTicket(InforContext context, PickTicket pickTicketParam) throws InforException {
        MP0296_AddPickList_001 createPickTicket = new MP0296_AddPickList_001();
        MP0296_AddPickList_001_Result result = new MP0296_AddPickList_001_Result();

        pickTicketParam.setCode("");
        createPickTicket.setPickList(pickTicket2pickList(context, pickTicketParam));

        // Invoke Infor web service
        if (context.getCredentials() != null) {
            result = inforws.addPickListOp(createPickTicket,
                    applicationData.getOrganization(),
                    tools.createSecurityHeader(context),
                    "TERMINATE", null, null, tools.getTenant(context));
        } else {
            result = inforws.addPickListOp(createPickTicket, applicationData.getOrganization(),
                    null, null, new Holder<>(tools.createInforSession(context)), null,
                    tools.getTenant(context));
        }

        return result.getPICKLISTID().getPICKLIST();
    }

    private PickList pickTicket2pickList(InforContext context, PickTicket pickTicket) throws InforException {
        PickList pickList = new PickList();
        pickList.setDATEREQUIRED(tools.getDataTypeTools().encodeInforDate(pickTicket.getRequestedEndDate(), "Date Required"));

        // Store
        pickList.setSTOREID(new STOREID_Type());
        pickList.getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
        pickList.getSTOREID().setSTORECODE(pickTicket.getStoreCode());

        // Status
        pickList.setSTATUS(new STATUS_Type());
        pickList.getSTATUS().setSTATUSCODE(pickTicket.getStatus());

        // Description
        pickList.setPICKLISTID(new PICKLIST_Type());
        pickList.getPICKLISTID().setPICKLISTDESC(pickTicket.getDescription());
        pickList.getPICKLISTID().setPICKLIST(pickTicket.getCode());

        //Next step is either an equipment (location, asset or employee's asset) or an acitvity from a work order
        // Equipment
        if (pickTicket.getAssetCode() != null) {
            pickList.setASSETID(new EQUIPMENTID_Type());
            pickList.getASSETID().setEQUIPMENTCODE(pickTicket.getAssetCode());
            pickList.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
        }

        // Workorder activity
        if (pickTicket.getWorkorderCode() != null) {
            pickList.setWORKORDERACTIVITY(new WORKORDERACTIVITY());
            pickList.getWORKORDERACTIVITY().setACTIVITYID(new ACTIVITYID());

            pickList.getWORKORDERACTIVITY().getACTIVITYID().setACTIVITYCODE(
                    new ACTIVITYCODE());
            pickList.getWORKORDERACTIVITY().getACTIVITYID().getACTIVITYCODE()
                    .setValue(pickTicket.getActivityNumber());
            pickList.getWORKORDERACTIVITY().getACTIVITYID().setWORKORDERID(new WOID_Type());
            pickList.getWORKORDERACTIVITY().getACTIVITYID().getWORKORDERID()
                    .setJOBNUM(pickTicket.getWorkorderCode());
        }

        if (pickTicket.getClassCode() != null) {
            pickList.setCLASSID(new CLASSID_Type());
            pickList.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
            pickList.getCLASSID().setCLASSCODE(pickTicket.getClassCode());
        }

        // Origin
        pickList.setORIGINID(new USERID_Type());
        pickList.getORIGINID().setUSERCODE(pickTicket.getOriginCode());
        return pickList;
    }

    private PickTicket pickList2pickTicket(PickList pickList) throws InforException {
        PickTicket pickTicket = new PickTicket();
        pickTicket.setRequestedEndDate(pickTicket.getRequestedEndDate());

        pickTicket.setStoreCode(pickList.getSTOREID().getSTORECODE());
        pickTicket.setStatus(pickList.getSTATUS().getSTATUSCODE());
        pickTicket.setCode(pickList.getPICKLISTID().getPICKLIST());
        pickTicket.setDescription(pickList.getPICKLISTID().getPICKLISTDESC());
        pickTicket.setAssetCode(pickList.getASSETID().getEQUIPMENTCODE());
        pickTicket.setWorkorderCode(pickList.getWORKORDERACTIVITY().getACTIVITYID().getWORKORDERID().getJOBNUM());
        pickTicket.setActivityNumber(pickList.getWORKORDERACTIVITY().getACTIVITYID().getACTIVITYCODE().getValue());
        pickTicket.setClassCode(pickList.getCLASSID().getCLASSCODE());
        pickTicket.setOriginCode(pickList.getORIGINID().getUSERCODE());

        return pickTicket;
    }

    //ONLY WORKS FOR UPDATING STATUS
    public String updatePickTicket(InforContext context, PickTicket pickTicketParam) throws InforException {
        MP0297_SyncPickList_001 syncPickTicket = new MP0297_SyncPickList_001();
        MP0297_SyncPickList_001_Result result;

        PickList pickList = readPickTicket(context, pickTicketParam.getCode());

        if (pickTicketParam.getStatus() != null) {
            pickList.getSTATUS().setSTATUSCODE(pickTicketParam.getStatus());
        }
        syncPickTicket.setPickList(pickList);

        // Invoke Infor web service
        if (context.getCredentials() != null) {
            result = inforws.syncPickListOp(syncPickTicket,
                    applicationData.getOrganization(),
                    tools.createSecurityHeader(context),
                    "TERMINATE", null, null, tools.getTenant(context));
        } else {
            result = inforws.syncPickListOp(syncPickTicket, applicationData.getOrganization(),
                    null, null, new Holder<>(tools.createInforSession(context)), null,
                    tools.getTenant(context));
        }

        return result.getResultData().getPICKLISTID().getPICKLIST();
    }

    public PickList readPickTicket(InforContext context, String code) throws InforException {
        MP0211_GetPickList_001 getPickList = new MP0211_GetPickList_001();
        getPickList.setPICKLISTID(new PICKLIST_Type());
        getPickList.getPICKLISTID().setPICKLIST(code);
        MP0211_GetPickList_001_Result pickListResult;
        if (context.getCredentials() != null) {
            pickListResult = inforws.getPickListOp(getPickList, applicationData.getOrganization(),
                    tools.createSecurityHeader(context),
                    "TERMINATE", null, null,
                    tools.getTenant(context));
        } else {
            pickListResult = inforws.getPickListOp(getPickList, applicationData.getOrganization(), null,
                    null, new Holder<>(tools.createInforSession(context)), null,
                    tools.getTenant(context));
        }
        return pickListResult.getResultData().getPickList();
    }

    public String addPartToPickTicket(InforContext context, PickTicketPart pickTicketPartParam) throws InforException {
        MP1223_AddPickListPart_001 addPickListPart = new MP1223_AddPickListPart_001();
        MP1223_AddPickListPart_001_Result result;

        addPickListPart.setPickListPart(new PickListPart());

        QUANTITY quantity1 = new QUANTITY();
        quantity1.setVALUE(new BigDecimal(pickTicketPartParam.getQuantity()));
        quantity1.setUOM("default");
        quantity1.setSIGN("+");
        quantity1.setQualifier("OTHER");
        quantity1.setNUMOFDEC(new BigDecimal(0).toBigInteger());
        addPickListPart.getPickListPart().setQUANTITYREQUIRED(quantity1);

        PICKLISTPARTID_Type picklist_type = new PICKLISTPARTID_Type();
        picklist_type.setPARTID(new PARTID_Type());
        picklist_type.getPARTID().setPARTCODE(pickTicketPartParam.getPartCode());
        picklist_type.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
        picklist_type.setPICKLISTID(new PICKLIST_Type());
        picklist_type.getPICKLISTID().setPICKLIST(pickTicketPartParam.getPickTicket());

        addPickListPart.getPickListPart().setPICKLISTPARTID(picklist_type);


        if (context.getCredentials() != null) {
            result = inforws.addPickListPartOp(addPickListPart, applicationData.getOrganization(),
                    tools.createSecurityHeader(context),
                    "TERMINATE", null, null,
                    tools.getTenant(context));
        } else {
            result = inforws.addPickListPartOp(addPickListPart, applicationData.getOrganization(), null,
                    null, new Holder<>(tools.createInforSession(context)), null,
                    tools.getTenant(context));
        }
        return result.toString();
    }
}
