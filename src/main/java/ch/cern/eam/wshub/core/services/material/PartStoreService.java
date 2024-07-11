package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.PartStore;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface PartStoreService {

    @Operation(logOperation = EAM_OPERATION.PARTSTORE_U)
    String updatePartStore(EAMContext context, PartStore partStoreParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTSTORE_C)
    String addPartStore(EAMContext context, PartStore partStoreParam) throws EAMException;
}
