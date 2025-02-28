package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.tools.InforException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
        locationService = inforClient.getLocationService();
        equipmentFacadeService = inforClient.getEquipmentFacadeService();
    }

    @Test
    public void testDeprecatedLocations() throws InforException {
        String code = getCode(TypeCode.L);
        Location location = new Location();
        location.setCode(code);
        location.setDescription("location test");
        location.setDepartmentCode(DEPARTMENT);
        locationService.createLocation(context, location);

        assertThrows(InforException.class, () -> equipmentFacadeService.readEquipment(context, code));
        assertThrows(InforException.class, () -> equipmentFacadeService.deleteEquipment(context, code));
    }

    @Test
    public void testUpdateEquipmentWithoutCustomFields() throws InforException {
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
    public void testUpdateEquipmentWithPartialCustomFields() throws InforException {
        // Given
        String code = getCode(TypeCode.A);
        Equipment equipment = new Equipment();
        equipment.setCode(code);
        equipment.setTypeCode(TypeCode.A.name());
        equipment.setDescription("equipment test");
        equipment.setStatusCode("I");
        equipment.setDepartmentCode(DEPARTMENT);

        Map<String, String> customFieldMap = new HashMap<>();
        customFieldMap.put("0002", "525252");
        customFieldMap.put("0003", "TEST");
        customFieldMap.put("0010", "2025");

        equipment.setCustomFieldMap(customFieldMap);
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
    public void testUpdateEquipmentWithPartialCustomFieldMap() throws InforException {
        // Given
        String code = getCode(TypeCode.A);
        Equipment equipment = new Equipment();
        equipment.setCode(code);
        equipment.setTypeCode(TypeCode.A.name());
        equipment.setDescription("equipment test");
        equipment.setStatusCode("I");
        equipment.setDepartmentCode(DEPARTMENT);

        Map<String, String> customFieldMap = new HashMap<>();
        customFieldMap.put("0002", "525252");
        customFieldMap.put("0003", "TEST");
        customFieldMap.put("0010", "2025");

        equipment.setCustomFieldMap(customFieldMap);
        equipmentFacadeService.createEquipment(context, equipment);
        Equipment createdEquipment = equipmentFacadeService.readEquipment(context, code);

        // When
        createdEquipment.setCustomFields(null);
        Map<String, String> createdCustomFieldMap = createdEquipment.getCustomFieldMap();

        Map<String, String> updateCustomFieldMap = new HashMap<>();
        updateCustomFieldMap.put("0010",  createdCustomFieldMap.get("0010"));
        createdEquipment.setCustomFieldMap(updateCustomFieldMap); // update 1 instead of 3

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
    public void testUpdateEquipmentWithCustomFields() throws InforException {
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
    public void testUpdateEquipmentWithCustomFieldMap() throws InforException {
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
    public void testUpdateEquipmentWithCustomFieldsAndCustomFieldMap() throws InforException {
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
