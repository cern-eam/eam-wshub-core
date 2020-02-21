package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.tools.InforException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ch.cern.eam.wshub.core.GlobalContext.*;

public class TestLocation {
    @Test
    void testCreateUpdate() throws Exception {
        String code = getCode("L");

        LocationService locationService = inforClient.getLocationService();

        // create the location
        Location location = new Location();
        location.setCode(code);
        location.setDescription("location test");
        location.setDepartmentCode("HXMF");
        locationService.createLocation(context, location);

        // confirm it has been created with the correct fields
        location = locationService.readLocation(context, code);
        assertEquals(code, location.getCode());
        assertEquals("location test", location.getDescription());
        assertEquals("HXMF", location.getDepartmentCode());

        // update the just read location
        location.setDescription("update test");
        location.setDepartmentCode("FC08");
        locationService.updateLocation(context, location);

        // read the location back again and confirm it has been updated
        location = locationService.readLocation(context, code);
        assertEquals("update test", location.getDescription());
        assertEquals("FC08", location.getDepartmentCode());
    }

    @Test
    void testDelete() throws Exception {
        String code = getCode("L");

        LocationService locationService = inforClient.getLocationService();

        // create the location
        Location location = new Location();
        location.setCode(code);
        location.setDescription("location test");
        location.setDepartmentCode("HXMF");
        locationService.createLocation(context, location);

        locationService.deleteLocation(context, code);
        // read the location to confirm it has been deleted
        try {
            locationService.readLocation(context, code);
            assertTrue(false); // successfully reading a location results in failure
        } catch(InforException e) {
            assertEquals("Cannot find the location record.", e.getMessage());
        }
    }
}
