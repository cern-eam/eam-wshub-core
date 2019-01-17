package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.PartStock;
import ch.cern.eam.wshub.core.tools.InforException;

public interface PartBinStockService {

    @Operation(logOperation = INFOR_OPERATION.PARTSTOCK_C)
    String addPartStock(InforContext context, PartStock partStockParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PARTSTOCK_U)
    String updatePartStock(InforContext context, PartStock partStockParam) throws InforException;
}
