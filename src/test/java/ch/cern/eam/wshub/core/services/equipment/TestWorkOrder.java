package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderService;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.InforException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static ch.cern.eam.wshub.core.GlobalContext.*;
import static ch.cern.eam.wshub.core.GlobalContext.context;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
    TEST ASSUMPTIONS:
        There is an asset "PA-A-001"
        There is a type code "EX"
        There is a department "RPM"
        There is a status code "R"
        There is a workorder class "RP01"
        There is a custom field "P214" in the "RP01" class
 */
public class TestWorkOrder {
    private static WorkOrderService workOrderService = inforClient.getWorkOrderService();

    WorkOrder createWorkOrder() throws Exception {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setDescription("test");
        workOrder.setEquipmentCode("PA-A-001");
        workOrder.setTypeCode("EX");
        workOrder.setDepartmentCode("RPM");
        workOrder.setStatusCode("R");

        String number = workOrderService.createWorkOrder(context, workOrder);
        workOrder.setNumber(number);

        return workOrder;
    }

    @Test
    void testCreateUpdate() throws Exception {
        WorkOrder workOrder = createWorkOrder();
        String number = workOrder.getNumber();

        workOrder = workOrderService.readWorkOrder(context, number);
        workOrder.setClassCode("RP01");

        CustomField[] customFields = new CustomField[1];
        customFields[0] = new CustomField();
        customFields[0].setCode("P214");
        customFields[0].setValue("22");
        workOrder.setCustomFields(customFields);
        workOrderService.updateWorkOrder(context, workOrder);

        workOrder = workOrderService.readWorkOrder(context, number);

        CustomField classCustomField = Arrays.stream(workOrder.getCustomFields())
                .filter(cf -> cf.getCode().equals("P214")).findFirst().get();
        assertEquals("22", classCustomField.getValue());
    }
}
