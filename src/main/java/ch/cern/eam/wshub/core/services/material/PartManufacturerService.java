package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.PartManufacturer;
import ch.cern.eam.wshub.core.tools.InforException;

public interface PartManufacturerService {

    @Operation(logOperation = INFOR_OPERATION.PARTMAN_C)
    String addPartManufacturer(InforContext context, PartManufacturer partManufacturerParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PARTMAN_U)
    String updatePartManufacturer(InforContext context, PartManufacturer partManufacturerParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PARTMAN_D)
    String deletePartManufacturer(InforContext context, PartManufacturer partManufacturerParam) throws InforException;
}
