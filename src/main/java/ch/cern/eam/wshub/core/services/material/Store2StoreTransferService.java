package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.services.material.entities.PickTicket;
import ch.cern.eam.wshub.core.services.material.entities.PickTicketPart;
import ch.cern.eam.wshub.core.services.material.entities.Store2StoreTransferDTO;
import ch.cern.eam.wshub.core.tools.InforException;

public interface Store2StoreTransferService {
    @Operation(logOperation = INFOR_OPERATION.STORE2STORETRANSFER)
    String store2storeTransfer(InforContext context, Store2StoreTransferDTO store2StoreTransferDTO) throws InforException;
}
