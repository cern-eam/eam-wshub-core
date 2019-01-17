package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.Aspect;
import ch.cern.eam.wshub.core.tools.InforException;

public interface InspectionService {

    @Operation(logOperation = INFOR_OPERATION.ASPECT_C)
    String addAspect(InforContext context, Aspect aspectParam) throws InforException;

}
