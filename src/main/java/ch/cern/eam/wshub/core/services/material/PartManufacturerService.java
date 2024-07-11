package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.PartManufacturer;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface PartManufacturerService {

    @Operation(logOperation = EAM_OPERATION.PARTMAN_C)
    String addPartManufacturer(EAMContext context, PartManufacturer partManufacturerParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTMAN_U)
    String updatePartManufacturer(EAMContext context, PartManufacturer partManufacturerParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTMAN_D)
    String deletePartManufacturer(EAMContext context, PartManufacturer partManufacturerParam) throws EAMException;
}
