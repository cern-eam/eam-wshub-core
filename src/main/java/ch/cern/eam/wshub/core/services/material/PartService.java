package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.material.entities.Part;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface PartService {

    @Operation(logOperation = INFOR_OPERATION.PART_BC)
    BatchResponse<String> createPartBatch(InforContext context, List<Part> partParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PART_BR)
    BatchResponse<Part> readPartBatch(InforContext context, List<String> partCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PART_BU)
    BatchResponse<String> updatePartBatch(InforContext context, List<Part> partParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PART_BD)
    BatchResponse<String> deletePartBatch(InforContext context, List<String> partCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PART_C)
    String createPart(InforContext context, Part partParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PART_R)
    Part readPart(InforContext context, String partCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PART_U)
    String updatePart(InforContext context, Part partParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.PART_D)
    String deletePart(InforContext context, String partCode) throws InforException;

    Part readPartDefault(InforContext context, String organization) throws InforException;

}
