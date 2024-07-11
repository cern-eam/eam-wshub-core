package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.workorders.entities.Aspect;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface InspectionService {

    @Operation(logOperation = EAM_OPERATION.ASPECT_C)
    String addAspect(EAMContext context, Aspect aspectParam) throws EAMException;

}
