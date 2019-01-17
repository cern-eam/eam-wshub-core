package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.PartStore;
import ch.cern.eam.wshub.core.tools.InforException;

public interface PartStoreService {

    @Operation(logOperation = INFOR_OPERATION.PARTSTORE_U)
    String updatePartStore(InforContext context, PartStore partStoreParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PARTSTORE_C)
    String addPartStore(InforContext context, PartStore partStoreParam) throws InforException;
}
