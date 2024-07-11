package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.util.List;

public interface LocationService {

    @Operation(logOperation = EAM_OPERATION.LOCATION_BC)
    BatchResponse<String> createLocationBatch(EAMContext context, List<Location> locations) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.LOCATION_BR)
    BatchResponse<Location> readLocationBatch(EAMContext context, List<String> locationCodes) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.LOCATION_BU)
    BatchResponse<String> updateLocationBatch(EAMContext context, List<Location> locations) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.LOCATION_BD)
    BatchResponse<String> deleteLocationBatch(EAMContext context, List<String> locationCodes) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.LOCATION_R)
    Location readLocation(EAMContext context, String locationCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.LOCATION_C)
    String createLocation(EAMContext context, Location locationParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.LOCATION_U)
    String updateLocation(EAMContext context, Location locationParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.LOCATION_D)
    String deleteLocation(EAMContext context, String locationCode) throws EAMException;
}
