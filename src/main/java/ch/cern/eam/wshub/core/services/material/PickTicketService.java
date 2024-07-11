package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.PickTicket;
import ch.cern.eam.wshub.core.services.material.entities.PickTicketPart;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface PickTicketService {

    @Operation(logOperation = EAM_OPERATION.PARTSTOCK_R)
    PickTicket readPickTicket(EAMContext context, String pickTicketCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTSTOCK_U)
    String createPickTicket(EAMContext context, PickTicket pickTicketParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTSTOCK_U)
    String updatePickTicket(EAMContext context, PickTicket pickTicketParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTSTOCK_U)
    String addPartToPickTicket(EAMContext context, PickTicketPart pickTicketPartParam) throws EAMException;
}
