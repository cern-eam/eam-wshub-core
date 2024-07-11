package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.Store2StoreTransferDTO;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface Store2StoreTransferService {
    @Operation(logOperation = EAM_OPERATION.STORE2STORETRANSFER)
    String store2storeTransfer(EAMContext context, Store2StoreTransferDTO store2StoreTransferDTO) throws EAMException;
}
