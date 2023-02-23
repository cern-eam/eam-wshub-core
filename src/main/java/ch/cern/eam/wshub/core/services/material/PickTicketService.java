package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.services.material.entities.PickTicket;
import ch.cern.eam.wshub.core.services.material.entities.PickTicketPart;
import ch.cern.eam.wshub.core.tools.InforException;

public interface PickTicketService {

    @Operation(logOperation = INFOR_OPERATION.PARTSTOCK_R)
    PickTicket readPickTicket(InforContext context, String pickTicketCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PARTSTOCK_U)
    String createPickTicket(InforContext context, PickTicket pickTicketParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PARTSTOCK_U)
    String updatePickTicket(InforContext context, PickTicket pickTicketParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PARTSTOCK_U)
    String addPartToPickTicket(InforContext context, PickTicketPart pickTicketPartParam) throws InforException;
}
