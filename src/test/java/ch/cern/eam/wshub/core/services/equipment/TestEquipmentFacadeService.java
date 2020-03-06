package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.tools.InforException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ch.cern.eam.wshub.core.GlobalContext.*;

/*
    TEST ASSUMPTIONS:
        There is a department "HXMF"
 */
public class TestEquipmentFacadeService {
    private LocationService locationService;
    private EquipmentFacadeService equipmentFacadeService;

    @BeforeEach
    void setup() throws Exception {
        locationService = inforClient.getLocationService();
        equipmentFacadeService = inforClient.getEquipmentFacadeService();
    }

    @Test
    public void testDeprecatedLocations() throws InforException {
        String code = getCode(TypeCode.L);
        Location location = new Location();
        location.setCode(code);
        location.setDescription("location test");
        location.setDepartmentCode("HXMF");
        locationService.createLocation(context, location);

        assertThrows(InforException.class, () -> equipmentFacadeService.readEquipment(context, code));
        assertThrows(InforException.class, () -> equipmentFacadeService.deleteEquipment(context, code));
    }
}
