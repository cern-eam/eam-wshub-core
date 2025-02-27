package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.tools.EAMException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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
    void setup() {
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

    @Test
    public void testUpdateEquipmentWithoutCustomFields() throws EAMException {
        // Given
        String code = getCode(TypeCode.A);
        Equipment equipment = new Equipment();
        equipment.setCode(code);
        equipment.setTypeCode(TypeCode.A.name());
        equipment.setDescription("equipment test");
        equipment.setStatusCode("I");
        equipment.setDepartmentCode(DEPARTMENT);
        equipmentFacadeService.createEquipment(context, equipment);
        Equipment createdEquipment = equipmentFacadeService.readEquipment(context, code);

        // When
        createdEquipment.setCustomFields(null);
        createdEquipment.setCustomFieldMap(null);
        equipmentFacadeService.updateEquipment(context, createdEquipment);
        Equipment updatedEquipment = equipmentFacadeService.readEquipment(context, code);

        // Then
        assertEquals(3, updatedEquipment.getCustomFields().length); // By default, there is 3 CustomFields
        assertEquals(3, updatedEquipment.getCustomFieldMap().size());
        equipmentFacadeService.deleteEquipment(context, code);
    }

    @Test
    public void testUpdateEquipmentWithPartialCustomFields() throws EAMException {
        // Given
        String code = getCode(TypeCode.A);
        Equipment equipment = new Equipment();
        equipment.setCode(code);
        equipment.setTypeCode(TypeCode.A.name());
        equipment.setDescription("equipment test");
        equipment.setStatusCode("I");
        equipment.setDepartmentCode(DEPARTMENT);
        equipment.setCustomFieldMap(Map.of(
                "0002", "525252",
                "0003", "TEST",
                "0010", "2025"
        ));
        equipmentFacadeService.createEquipment(context, equipment);
        Equipment createdEquipment = equipmentFacadeService.readEquipment(context, code);

        // When
        CustomField[] customFields = createdEquipment.getCustomFields();
        createdEquipment.setCustomFields(new CustomField[]{customFields[0], customFields[1]}); // update 2 instead of 3
        createdEquipment.setCustomFieldMap(null);

        equipmentFacadeService.updateEquipment(context, createdEquipment);
        Equipment updatedEquipment = equipmentFacadeService.readEquipment(context, code);

        // Then
        assertEquals(3, updatedEquipment.getCustomFields().length);
        assertEquals(3, updatedEquipment.getCustomFieldMap().size());
        assertEquals("525252", updatedEquipment.getCustomFieldMap().get("0002"));
        assertEquals("TEST", updatedEquipment.getCustomFieldMap().get("0003"));
        assertEquals("2025", updatedEquipment.getCustomFieldMap().get("0010"));
        equipmentFacadeService.deleteEquipment(context, code);
    }

    @Test
    public void testUpdateEquipmentWithPartialCustomFieldMap() throws EAMException {
        // Given
        String code = getCode(TypeCode.A);
        Equipment equipment = new Equipment();
        equipment.setCode(code);
        equipment.setTypeCode(TypeCode.A.name());
        equipment.setDescription("equipment test");
        equipment.setStatusCode("I");
        equipment.setDepartmentCode(DEPARTMENT);
        equipment.setCustomFieldMap(Map.of(
                "0002", "525252",
                "0003", "TEST",
                "0010", "2025"
        ));
        equipmentFacadeService.createEquipment(context, equipment);
        Equipment createdEquipment = equipmentFacadeService.readEquipment(context, code);

        // When
        createdEquipment.setCustomFields(null);
        Map<String, String> customFieldMap = createdEquipment.getCustomFieldMap();
        createdEquipment.setCustomFieldMap(Map.of("0010", customFieldMap.get("0010"))); // update 1 instead of 3

        equipmentFacadeService.updateEquipment(context, createdEquipment);
        Equipment updatedEquipment = equipmentFacadeService.readEquipment(context, code);

        // Then
        assertEquals(3, updatedEquipment.getCustomFields().length);
        assertEquals(3, updatedEquipment.getCustomFieldMap().size());
        assertEquals("525252", updatedEquipment.getCustomFieldMap().get("0002"));
        assertEquals("TEST", updatedEquipment.getCustomFieldMap().get("0003"));
        assertEquals("2025", updatedEquipment.getCustomFieldMap().get("0010"));
        equipmentFacadeService.deleteEquipment(context, code);
    }

    @Test
    public void testUpdateEquipmentWithCustomFields() throws EAMException {
        // Given
        String code = getCode(TypeCode.A);
        Equipment equipment = new Equipment();
        equipment.setCode(code);
        equipment.setTypeCode(TypeCode.A.name());
        equipment.setDescription("equipment test");
        equipment.setStatusCode("I");
        equipment.setDepartmentCode(DEPARTMENT);
        equipmentFacadeService.createEquipment(context, equipment);
        Equipment createdEquipment = equipmentFacadeService.readEquipment(context, code);

        // When
        CustomField[] customFields = createdEquipment.getCustomFields();
        CustomField yearBuilt = new CustomField("0010","2025" );
        createdEquipment.setCustomFields(new CustomField[]{customFields[0], customFields[1], yearBuilt});
        createdEquipment.setCustomFieldMap(null);


        equipmentFacadeService.updateEquipment(context, createdEquipment);
        Equipment updatedEquipment = equipmentFacadeService.readEquipment(context, code);

        // Then
        assertEquals(3, updatedEquipment.getCustomFields().length);
        assertEquals(3, updatedEquipment.getCustomFieldMap().size());
        assertEquals("2025", updatedEquipment.getCustomFieldMap().get("0010"));
        equipmentFacadeService.deleteEquipment(context, code);
    }

    @Test
    public void testUpdateEquipmentWithCustomFieldMap() throws EAMException {
        // Given
        String code = getCode(TypeCode.A);
        Equipment equipment = new Equipment();
        equipment.setCode(code);
        equipment.setTypeCode(TypeCode.A.name());
        equipment.setDescription("equipment test");
        equipment.setStatusCode("I");
        equipment.setDepartmentCode(DEPARTMENT);
        equipmentFacadeService.createEquipment(context, equipment);
        Equipment createdEquipment = equipmentFacadeService.readEquipment(context, code);

        // When
        createdEquipment.setCustomFields(null);
        Map<String, String> customFieldMap = new HashMap<>(createdEquipment.getCustomFieldMap());
        customFieldMap.put("0010", "2025");
        createdEquipment.setCustomFieldMap(customFieldMap); // update 1 instead of 3

        equipmentFacadeService.updateEquipment(context, createdEquipment);
        Equipment updatedEquipment = equipmentFacadeService.readEquipment(context, code);

        // Then
        assertEquals(3, updatedEquipment.getCustomFields().length);
        assertEquals(3, updatedEquipment.getCustomFieldMap().size());
        assertEquals("2025", updatedEquipment.getCustomFieldMap().get("0010"));
        equipmentFacadeService.deleteEquipment(context, code);
    }

    @Test
    public void testUpdateEquipmentWithCustomFieldsAndCustomFieldMap() throws EAMException {
        // Given
        String code = getCode(TypeCode.A);
        Equipment equipment = new Equipment();
        equipment.setCode(code);
        equipment.setTypeCode(TypeCode.A.name());
        equipment.setDescription("equipment test");
        equipment.setStatusCode("I");
        equipment.setDepartmentCode(DEPARTMENT);
        equipmentFacadeService.createEquipment(context, equipment);
        Equipment createdEquipment = equipmentFacadeService.readEquipment(context, code);

        // When
        CustomField[] customFields = createdEquipment.getCustomFields();
        CustomField yearBuilt = new CustomField("0010","1998" );
        createdEquipment.setCustomFields(new CustomField[]{customFields[0], customFields[1], yearBuilt});

        Map<String, String> customFieldMap = new HashMap<>(createdEquipment.getCustomFieldMap());
        customFieldMap.put("0010", "2025");
        createdEquipment.setCustomFieldMap(customFieldMap); // update 1 instead of 3

        equipmentFacadeService.updateEquipment(context, createdEquipment);
        Equipment updatedEquipment = equipmentFacadeService.readEquipment(context, code);

        // Then
        assertEquals(3, updatedEquipment.getCustomFields().length);
        assertEquals(3, updatedEquipment.getCustomFieldMap().size());
        assertEquals("2025", updatedEquipment.getCustomFieldMap().get("0010"));
        equipmentFacadeService.deleteEquipment(context, code);
    }
}
