package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.tools.InforException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static ch.cern.eam.wshub.core.GlobalContext.*;

/*
    TEST ASSUMPTIONS:
        There are location classes "DES" and "FIC"
        There is a custom field "HMLPR019" on classes "DES" and "FIC"
        There is a custom field "HMLPR147" on class "*"
        The intervals for the dates must be compatible with Infor EAM
 */

// test the CRUD operations on locations
public class TestLocation {
    private Location parentLocation;
    private LocationService locationService;

    @BeforeEach
    void setup() throws Exception {
        locationService = inforClient.getLocationService();
        parentLocation = createLocation(false);
    }

    // this helper function creates a test location
    // it also confirms that the created location is read as created
    // filled: whether the optional fields should be filled
    Location createLocation(boolean filled) throws Exception {
        String code = getCode(TypeCode.L);

        // create the location
        Location location = new Location();
        // set mandatory fields
        location.setCode(code);
        location.setDescription("location test");
        location.setDepartmentCode(DEPARTMENT);

        Date date = getCurrentDate();

        if(filled) {
            location.setClassCode("DES");
            location.setSafety(true);
            location.setOutOfService(true);
            location.setCostCode(COST_CODE);
            location.setHierarchyLocationCode(parentLocation.getCode());

            UserDefinedFields udf = new UserDefinedFields();
            udf.setUdfchar01("test");
            udf.setUdfchkbox01(true);

            udf.setUdfdate01(date);

            location.setUserDefinedFields(udf);

            CustomField[] customFields = new CustomField[2];
            customFields[0] = new CustomField();
            customFields[0].setCode("HMLPR019");
            customFields[0].setValue("5");
            customFields[1] = new CustomField();
            customFields[1].setCode("HMLPR147");
            customFields[1].setValue("10");

            location.setCustomFields(customFields);
        }

        locationService.createLocation(context, location);

        // confirm it has been created with the correct mandatory fields
        location = locationService.readLocation(context, code);
        assertEquals(code, location.getCode());
        assertEquals("location test", location.getDescription());
        assertEquals(DEPARTMENT, location.getDepartmentCode());

        if(filled) {
            assertEquals("location test", location.getDescription());
            assertEquals(DEPARTMENT, location.getDepartmentCode());

            assertEquals("DES", location.getClassCode());
            assertEquals(true, location.getSafety());
            assertEquals(true, location.getOutOfService());
            assertEquals(COST_CODE, location.getCostCode());
            assertEquals(parentLocation.getCode(), location.getHierarchyLocationCode());
            assertEquals("test", location.getUserDefinedFields().getUdfchar01());
            assertEquals(true, location.getUserDefinedFields().getUdfchkbox01());
            assertEquals(date, location.getUserDefinedFields().getUdfdate01());

            CustomField[] customFields = location.getCustomFields();

            CustomField classCustomField = Arrays.stream(customFields)
                .filter(cf -> cf.getCode().equals("HMLPR019")).findFirst().get();
            assertEquals("5", classCustomField.getValue());

            CustomField generalCustomField = Arrays.stream(customFields)
                    .filter(cf -> cf.getCode().equals("HMLPR147")).findFirst().get();
            assertEquals("10", generalCustomField.getValue());
        }

        return location;
    }

    // tests creation of a unfilled location, and fills it using an update
    @Test
    void testCreateNullUpdateFill() throws Exception {
        Location location = createLocation(false);
        String code = location.getCode();

        // confirm it has been created with the correct fields
        location = locationService.readLocation(context, code);
        assertEquals(code, location.getCode());
        assertEquals("location test", location.getDescription());
        assertEquals(DEPARTMENT, location.getDepartmentCode());

        // check that all the other fields are null
        assertEquals(null, location.getClassCode());
        assertEquals(false, location.getSafety());
        assertEquals(false, location.getOutOfService());
        assertEquals(null, location.getCostCode());
        assertEquals(null, location.getHierarchyLocationCode());
        assertEquals(null, location.getUserDefinedFields().getUdfchar01());
        assertEquals(false, location.getUserDefinedFields().getUdfchkbox01());
        assertEquals(null, location.getUserDefinedFields().getUdfdate01());

        CustomField[] customFields = location.getCustomFields();

        assertFalse(Arrays.stream(customFields)
                .filter(cf -> cf.getCode().equals("HMLPR019")).findFirst().isPresent());

        CustomField generalCustomField = Arrays.stream(customFields)
                .filter(cf -> cf.getCode().equals("HMLPR147")).findFirst().get();
        assertEquals("", generalCustomField.getValue());

        // update the just read location
        location.setDescription("update test");
        location.setDepartmentCode("FC08");

        // set other fields to non-null values
        location.setClassCode("DES");
        location.setSafety(true);
        location.setOutOfService(true);
        location.setCostCode(COST_CODE);
        location.setHierarchyLocationCode(parentLocation.getCode());

        UserDefinedFields udf = location.getUserDefinedFields();
        udf.setUdfchar01("test");
        udf.setUdfchkbox01(true);

        Date date = getCurrentDate();

        udf.setUdfdate01(date);

        customFields = new CustomField[2];
        customFields[0] = new CustomField();
        customFields[0].setCode("HMLPR019");
        customFields[0].setValue("5");
        customFields[1] = new CustomField();
        customFields[1].setCode("HMLPR147");
        customFields[1].setValue("10");

        location.setCustomFields(customFields);

        locationService.updateLocation(context, location);

        // try to update the location with the same values again,
        // ensuring we can update a filled location to a filled location
        locationService.updateLocation(context, location);

        // try updating the location with an empty Location (no changes)
        location = new Location();
        location.setCode(code);
        locationService.updateLocation(context, location);

        // read the location back again and confirm it has been updated
        location = locationService.readLocation(context, code);
        assertEquals("update test", location.getDescription());
        assertEquals("FC08", location.getDepartmentCode());

        assertEquals("DES", location.getClassCode());
        assertEquals(true, location.getSafety());
        assertEquals(true, location.getOutOfService());
        assertEquals(COST_CODE, location.getCostCode());
        assertEquals(parentLocation.getCode(), location.getHierarchyLocationCode());
        assertEquals("test", location.getUserDefinedFields().getUdfchar01());
        assertEquals(true, location.getUserDefinedFields().getUdfchkbox01());
        assertEquals(date, location.getUserDefinedFields().getUdfdate01());

        customFields = location.getCustomFields();

        CustomField classCustomField = Arrays.stream(customFields)
                .filter(cf -> cf.getCode().equals("HMLPR019")).findFirst().get();
        assertEquals("5", classCustomField.getValue());

        generalCustomField = Arrays.stream(customFields)
                .filter(cf -> cf.getCode().equals("HMLPR147")).findFirst().get();
        assertEquals("10", generalCustomField.getValue());
    }

    // tests creation of a filled location, and nulls it using an update
    @Test
    void testCreateFillUpdateNull() throws Exception {
        Location location = createLocation(true);
        String code = location.getCode();

        // null all values
        location.setClassCode("");
        location.setSafety(false);
        location.setOutOfService(false);
        location.setCostCode("");
        location.setHierarchyLocationCode("");

        UserDefinedFields udf = location.getUserDefinedFields();
        udf.setUdfchar01("");
        udf.setUdfchkbox01(false);
        udf.setUdfdate01(new Date(0));

        location.setCustomFields(new CustomField[0]);

        locationService.updateLocation(context, location);

        // try to update the location with the same values again,
        // ensuring we can update a nullified location to a nullified location
        locationService.updateLocation(context, location);

        // try updating the location with an empty Location (no changes)
        location = new Location();
        location.setCode(code);
        locationService.updateLocation(context, location);

        location = locationService.readLocation(context, code);
        assertEquals(null, location.getClassCode());
        assertEquals(false, location.getSafety());
        assertEquals(false, location.getOutOfService());
        assertEquals(null, location.getCostCode());
        assertEquals(null, location.getHierarchyLocationCode());
        assertEquals(null, location.getUserDefinedFields().getUdfchar01());
        assertEquals(false, location.getUserDefinedFields().getUdfchkbox01());
        assertEquals(null, location.getUserDefinedFields().getUdfdate01());

        CustomField[] customFields = location.getCustomFields();

        assertFalse(Arrays.stream(customFields)
                .filter(cf -> cf.getCode().equals("HMLPR019")).findFirst().isPresent());

        CustomField generalCustomField = Arrays.stream(customFields)
                .filter(cf -> cf.getCode().equals("HMLPR147")).findFirst().get();
        assertEquals("10", generalCustomField.getValue());
    }

    // tests the deletion of locations
    @Test
    void testDelete() throws Exception {
        Location location = createLocation(false);
        String code = location.getCode();

        locationService.deleteLocation(context, code);
        // read the location to confirm it has been deleted
        try {
            locationService.readLocation(context, code);
            assertTrue(false); // successfully reading a location results in failure
        } catch(InforException e) {
            assertEquals("Cannot find the location record.", e.getMessage());
        }
    }

    // tests whether default values for the location fields have been set
    // (which would be a big problem when updating just part of a location)
    @Test
    void testDefaultValues() throws Exception {
        Location location = createLocation(true);

        Location brandNew = new Location();
        brandNew.setCode(location.getCode());
        brandNew.setDescription("ricardoricardo");
        locationService.updateLocation(context, brandNew);

        location = locationService.readLocation(context, location.getCode());
        assertEquals("ricardoricardo", location.getDescription());
        assertEquals(true, location.getOutOfService());
    }

    // tests changing a filled location from class DES to class FIC
    // ensuring the custom fields are kept
    @Test
    void testMergeFromFilled() throws Exception {
        String code = createLocation(true).getCode();
        Location location = new Location();
        location.setCode(code);
        location.setClassCode("FIC");
        locationService.updateLocation(context, location);

        location = locationService.readLocation(context, location.getCode());
        CustomField[] customFields = location.getCustomFields();
        CustomField classCustomField = Arrays.stream(customFields)
            .filter(cf -> cf.getCode().equals("HMLPR019")).findFirst().get();
        assertEquals("5", classCustomField.getValue());

        CustomField generalCustomField = Arrays.stream(customFields)
                .filter(cf -> cf.getCode().equals("HMLPR147")).findFirst().get();
        assertEquals("10", generalCustomField.getValue());
    }

    // tests changing a unfilled location from no class to FIC
    // ensuring the nullified values are kept
    @Test
    void testMergeFromEmpty() throws Exception {
        String code = createLocation(false).getCode();
        Location location = new Location();
        location.setCode(code);
        location.setClassCode("FIC");
        locationService.updateLocation(context, location);

        location = locationService.readLocation(context, location.getCode());
        CustomField[] customFields = location.getCustomFields();
        CustomField classCustomField = Arrays.stream(customFields)
                .filter(cf -> cf.getCode().equals("HMLPR019")).findFirst().get();
        assertEquals(null, classCustomField.getValue());

        CustomField generalCustomField = Arrays.stream(customFields)
                .filter(cf -> cf.getCode().equals("HMLPR147")).findFirst().get();
        assertEquals("", generalCustomField.getValue());
    }
}
