package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface LocationService {
    BatchResponse<String> createLocationBatch(InforContext context, List<Location> locations) throws InforException;
    BatchResponse<Location> readLocationBatch(InforContext context, List<String> locationCodes) throws InforException;
    BatchResponse<String> updateLocationBatch(InforContext context, List<Location> locations) throws InforException;
    BatchResponse<String> deleteLocationBatch(InforContext context, List<String> locationCodes) throws InforException;

    Location readLocation(InforContext context, String locationCode) throws InforException;
    String createLocation(InforContext context, Location locationParam) throws InforException;
    String updateLocation(InforContext context, Location locationParam) throws InforException;
    String deleteLocation(InforContext context, String locationCode) throws InforException;
}
