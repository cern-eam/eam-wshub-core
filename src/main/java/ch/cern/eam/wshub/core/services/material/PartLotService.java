package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.Lot;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface PartLotService {

    @Operation(logOperation = EAM_OPERATION.PARTLOT_C)
    String createLot(EAMContext context, Lot lot) throws EAMException;

}

