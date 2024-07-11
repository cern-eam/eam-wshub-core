package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.material.entities.Part;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.util.List;

public interface PartService {

    @Operation(logOperation = EAM_OPERATION.PART_BC)
    BatchResponse<String> createPartBatch(EAMContext context, List<Part> partParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PART_BR)
    BatchResponse<Part> readPartBatch(EAMContext context, List<String> partCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PART_BU)
    BatchResponse<String> updatePartBatch(EAMContext context, List<Part> partParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PART_BD)
    BatchResponse<String> deletePartBatch(EAMContext context, List<String> partCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PART_C)
    String createPart(EAMContext context, Part partParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PART_R)
    Part readPart(EAMContext context, String partCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PART_U)
    String updatePart(EAMContext context, Part partParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PART_D)
    String deletePart(EAMContext context, String partCode) throws EAMException;

    Part readPartDefault(EAMContext context, String organization) throws EAMException;

}
