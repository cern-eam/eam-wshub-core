package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface LocationService {

    @Operation(logOperation = INFOR_OPERATION.LOCATION_BC)
    BatchResponse<String> createLocationBatch(InforContext context, List<Location> locations) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.LOCATION_BR)
    BatchResponse<Location> readLocationBatch(InforContext context, List<String> locationCodes) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.LOCATION_BU)
    BatchResponse<String> updateLocationBatch(InforContext context, List<Location> locations) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.LOCATION_BD)
    BatchResponse<String> deleteLocationBatch(InforContext context, List<String> locationCodes) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.LOCATION_R)
    Location readLocation(InforContext context, String locationCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.LOCATION_C)
    String createLocation(InforContext context, Location locationParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.LOCATION_U)
    String updateLocation(InforContext context, Location locationParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.LOCATION_D)
    String deleteLocation(InforContext context, String locationCode) throws InforException;
}
