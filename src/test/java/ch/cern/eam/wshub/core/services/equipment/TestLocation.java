package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.tools.InforException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static ch.cern.eam.wshub.core.GlobalContext.*;

public class TestLocation {
    private LocationService locationService = inforClient.getLocationService();

    private Location parentLocation;

    @BeforeEach
    void setup() throws Exception {
        parentLocation = createLocation(false);
    }

    Location createLocation(boolean filled) throws Exception {
        String code = getCode("L");

        // create the location
        Location location = new Location();
        // set mandatory fields
        location.setCode(code);
        location.setDescription("location test");
        location.setDepartmentCode("HXMF");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();

        if(filled) {
            location.setClassCode("DES");
            location.setSafety(true);
            location.setOutOfService(true);
            location.setCostCode("H#96231");
            location.setHierarchyLocationCode(parentLocation.getCode());

            UserDefinedFields udf = new UserDefinedFields();
            udf.setUdfchar01("test");
            udf.setUdfchkbox01(true);

            udf.setUdfdate01(date);

            location.setUserDefinedFields(udf);

            CustomField[] customFields = new CustomField[1];
            customFields[0] = new CustomField();
            customFields[0].setCode("HMLPR019");
            customFields[0].setValue("5");

            location.setCustomFields(customFields);
        }

        locationService.createLocation(context, location);

        // confirm it has been created with the correct mandatory fields
        location = locationService.readLocation(context, code);
        assertEquals(code, location.getCode());
        assertEquals("location test", location.getDescription());
        assertEquals("HXMF", location.getDepartmentCode());

        if(filled) {
            assertEquals("location test", location.getDescription());
            assertEquals("HXMF", location.getDepartmentCode());

            assertEquals("DES", location.getClassCode());
            assertEquals(true, location.getSafety());
            assertEquals(true, location.getOutOfService());
            assertEquals("H#96231", location.getCostCode());
            assertEquals(parentLocation.getCode(), location.getHierarchyLocationCode());
            assertEquals("test", location.getUserDefinedFields().getUdfchar01());
            assertEquals(true, location.getUserDefinedFields().getUdfchkbox01());
            assertEquals(date, location.getUserDefinedFields().getUdfdate01());
            assertEquals(1, location.getCustomFields().length);
            assertEquals("HMLPR019", location.getCustomFields()[0].getCode());
            assertEquals("5", location.getCustomFields()[0].getValue());
        }

        return location;
    }

    @Test
    void testCreateNullUpdateFill() throws Exception {
        Location location = createLocation(false);
        String code = location.getCode();

        // confirm it has been created with the correct fields
        location = locationService.readLocation(context, code);
        assertEquals(code, location.getCode());
        assertEquals("location test", location.getDescription());
        assertEquals("HXMF", location.getDepartmentCode());

        // check that all the other fields are null
        assertEquals(null, location.getClassCode());
        assertEquals(false, location.getSafety());
        assertEquals(false, location.getOutOfService());
        assertEquals(null, location.getCostCode());
        assertEquals(null, location.getHierarchyLocationCode());
        assertEquals(null, location.getUserDefinedFields().getUdfchar01());
        assertEquals(false, location.getUserDefinedFields().getUdfchkbox01());
        assertEquals(null, location.getUserDefinedFields().getUdfdate01());
        assertEquals(0, location.getCustomFields().length);

        // update the just read location
        location.setDescription("update test");
        location.setDepartmentCode("FC08");

        // set other fields to non-null values
        location.setClassCode("DES");
        location.setSafety(true);
        location.setOutOfService(true);
        location.setCostCode("H#96231");
        location.setHierarchyLocationCode(parentLocation.getCode());

        UserDefinedFields udf = location.getUserDefinedFields();
        udf.setUdfchar01("test");
        udf.setUdfchkbox01(true);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();

        udf.setUdfdate01(date);

        CustomField[] customFields = new CustomField[1];
        customFields[0] = new CustomField();
        customFields[0].setCode("HMLPR019");
        customFields[0].setValue("5");

        location.setCustomFields(customFields);

        locationService.updateLocation(context, location);

        // try to update the location with the same values again,
        // ensuring we can update a filled location to a filled location
        locationService.updateLocation(context, location);

        // read the location back again and confirm it has been updated
        location = locationService.readLocation(context, code);
        assertEquals("update test", location.getDescription());
        assertEquals("FC08", location.getDepartmentCode());

        assertEquals("DES", location.getClassCode());
        assertEquals(true, location.getSafety());
        assertEquals(true, location.getOutOfService());
        assertEquals("H#96231", location.getCostCode());
        assertEquals(parentLocation.getCode(), location.getHierarchyLocationCode());
        assertEquals("test", location.getUserDefinedFields().getUdfchar01());
        assertEquals(true, location.getUserDefinedFields().getUdfchkbox01());
        assertEquals(date, location.getUserDefinedFields().getUdfdate01());
        assertEquals(1, location.getCustomFields().length);
        assertEquals("HMLPR019", location.getCustomFields()[0].getCode());
        assertEquals("5", location.getCustomFields()[0].getValue());
    }

    @Test
    void testCreateFillUpdateNull() throws Exception {
        Location location = createLocation(true);
        String code = location.getCode();

        // null all values
        location.setClassCode("");
        location.setSafety(false);
        location.setOutOfService(false);
        location.setCostCode("");
        location.setHierarchyLocationCode(null);

        UserDefinedFields udf = location.getUserDefinedFields();
        udf.setUdfchar01("");
        udf.setUdfchkbox01(false);
        udf.setUdfdate01(new Date(0));

        location.setCustomFields(new CustomField[0]);

        locationService.updateLocation(context, location);

        // try to update the location with the same values again,
        // ensuring we can update a nullified location to a nullified location
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
        assertEquals(0, location.getCustomFields().length);
    }

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
}
