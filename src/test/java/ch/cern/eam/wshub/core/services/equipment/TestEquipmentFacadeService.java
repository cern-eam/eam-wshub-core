package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.tools.EAMException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ch.cern.eam.wshub.core.GlobalContext.*;

/*
    TEST ASSUMPTIONS:
        None
 */
public class TestEquipmentFacadeService {
    private LocationService locationService;
    private EquipmentFacadeService equipmentFacadeService;

    @BeforeEach
    void setup() throws Exception {
        locationService = eamClient.getLocationService();
        equipmentFacadeService = eamClient.getEquipmentFacadeService();
    }

    @Test
    public void testDeprecatedLocations() throws EAMException {
        String code = getCode(TypeCode.L);
        Location location = new Location();
        location.setCode(code);
        location.setDescription("location test");
        location.setDepartmentCode(DEPARTMENT);
        locationService.createLocation(context, location);

        assertThrows(EAMException.class, () -> equipmentFacadeService.readEquipment(context, code));
        assertThrows(EAMException.class, () -> equipmentFacadeService.deleteEquipment(context, code));
    }
}
