package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.administration.entities.DataspyCopy;
import ch.cern.eam.wshub.core.tools.InforException;

public interface DataspyService {

    @Operation(logOperation = INFOR_OPERATION.DATASPY_CP)
    String copyDataspy(InforContext context, DataspyCopy dataspyCopy) throws InforException;
}
