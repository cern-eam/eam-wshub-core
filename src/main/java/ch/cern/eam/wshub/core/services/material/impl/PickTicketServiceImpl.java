package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.material.PickTicketService;
import ch.cern.eam.wshub.core.services.material.entities.PickTicket;
import ch.cern.eam.wshub.core.services.material.entities.PickTicketPart;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.assetequipment_001.AssetEquipment;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import org.openapplications.oagis_segments.QUANTITY;

import jakarta.xml.ws.Holder;
import java.math.BigDecimal;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;

public class PickTicketServiceImpl implements PickTicketService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public PickTicketServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
    }

    public String createPickTicket(EAMContext context, PickTicket pickTicketParam) throws EAMException {
        PickList pickList = new PickList();
        pickList.setUSERDEFINEDAREA(
            tools.getCustomFieldsTools().getEAMCustomFields(
                context,
                toCodeString(pickList.getCLASSID()),
                pickList.getUSERDEFINEDAREA(),
                pickTicketParam.getClassCode(),
                "PICK"
            )
        );
        tools.getEAMFieldTools().transformWSHubObject(pickList, pickTicketParam, context);
        if (pickList.getPICKLISTID() != null) {
            pickList.getPICKLISTID().setPICKLIST("");
        }
        MP0296_AddPickList_001 createPickTicket = new MP0296_AddPickList_001();
        createPickTicket.setPickList(pickList);
        MP0296_AddPickList_001_Result result =
            tools.performEAMOperation(context, eamws::addPickListOp, createPickTicket);
        return result.getPICKLISTID().getPICKLIST();
    }

    public String updatePickTicket(EAMContext context, PickTicket pickTicketParam) throws EAMException {
        PickList pickList = readPickList(context, pickTicketParam.getCode());
        pickList.setUSERDEFINEDAREA(
            tools.getCustomFieldsTools().getEAMCustomFields(
                context,
                toCodeString(pickList.getCLASSID()),
                pickList.getUSERDEFINEDAREA(),
                pickTicketParam.getClassCode(),
                "PICK"
            )
        );
        tools.getEAMFieldTools().transformWSHubObject(pickList, pickTicketParam, context);

        MP0297_SyncPickList_001 syncPickTicket = new MP0297_SyncPickList_001();
        syncPickTicket.setPickList(pickList);

        MP0297_SyncPickList_001_Result result =
            tools.performEAMOperation(context, eamws::syncPickListOp, syncPickTicket);
        return result.getResultData().getPICKLISTID().getPICKLIST();
    }

    public PickList readPickList(EAMContext context, String code) throws EAMException {
        MP0211_GetPickList_001 getPickList = new MP0211_GetPickList_001();
        getPickList.setPICKLISTID(new PICKLIST_Type());
        getPickList.getPICKLISTID().setPICKLIST(code);
        MP0211_GetPickList_001_Result pickListResult =
            tools.performEAMOperation(context, eamws::getPickListOp, getPickList);
        return pickListResult.getResultData().getPickList();
    }

    public PickTicket readPickTicket(EAMContext context, String code) throws EAMException {
        PickList pickList = readPickList(context, code);
        final PickTicket pickTicket = tools.getEAMFieldTools().transformEAMObject(new PickTicket(), pickList, context);
        return pickTicket;
    }


    public String addPartToPickTicket(EAMContext context, PickTicketPart pickTicketPartParam) throws EAMException {
        MP1223_AddPickListPart_001 addPickListPart = new MP1223_AddPickListPart_001();

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

        MP1223_AddPickListPart_001_Result result =
            tools.performEAMOperation(context, eamws::addPickListPartOp, addPickListPart);
        return result.toString();
    }
}
