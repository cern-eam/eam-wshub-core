package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.PartStock;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface PartBinStockService {

    @Operation(logOperation = EAM_OPERATION.PARTSTOCK_C)
    String addPartStock(EAMContext context, PartStock partStockParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTSTOCK_U)
    String updatePartStock(EAMContext context, PartStock partStockParam) throws EAMException;
}
