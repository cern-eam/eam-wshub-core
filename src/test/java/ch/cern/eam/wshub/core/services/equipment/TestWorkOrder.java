package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderService;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.InforException;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static ch.cern.eam.wshub.core.GlobalContext.*;
import static ch.cern.eam.wshub.core.GlobalContext.context;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        for(CustomField customField : workOrder.getCustomFields()) {
            if(customField.getCode().equals("P214")) {
                assertEquals("22", customField.getValue());
            }
        }
    }
}
