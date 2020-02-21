package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.workorders.entities.Employee;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface EmployeeService {

    @Operation(logOperation = INFOR_OPERATION.EMPLOYEE_BC, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> createEmployeeBatch(InforContext context, List<Employee> workOrderParam)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EMPLOYEE_BU, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<String> updateEmployeeBatch(InforContext context, List<Employee> workOrderParam)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EMPLOYEE_R, logDataReference1 = LogDataReferenceType.INPUT)
    Employee readEmployee(InforContext context, String employeeCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EMPLOYEE_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createEmployee(InforContext context, Employee employee) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EMPLOYEE_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "employeeCode")
    String updateEmployee(InforContext context, Employee employee) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.EMPLOYEE_D)
    String deleteEmployee(InforContext context, String employeeCode) throws InforException;
}
