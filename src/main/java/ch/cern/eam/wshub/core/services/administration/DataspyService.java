package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.administration.entities.DataspyCopy;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.math.BigDecimal;

public interface DataspyService {

    @Operation(logOperation = EAM_OPERATION.DATASPY_CP)
    String copyDataspy(EAMContext context, DataspyCopy dataspyCopy) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.DATASPY_D)
    String deleteDataspy(EAMContext context, BigDecimal dataspyId) throws EAMException;
}
