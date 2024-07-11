package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.workorders.entities.Employee;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.util.List;

public interface EmployeeService {

    @Operation(logOperation = EAM_OPERATION.EMPLOYEE_BC, logDataReference1 = LogDataReferenceType.RESULT)
    BatchResponse<String> createEmployeeBatch(EAMContext context, List<Employee> workOrderParam)
            throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EMPLOYEE_BU, logDataReference1 = LogDataReferenceType.INPUT)
    BatchResponse<String> updateEmployeeBatch(EAMContext context, List<Employee> workOrderParam)
            throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EMPLOYEE_R, logDataReference1 = LogDataReferenceType.INPUT)
    Employee readEmployee(EAMContext context, String employeeCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EMPLOYEE_C, logDataReference1 = LogDataReferenceType.RESULT)
    String createEmployee(EAMContext context, Employee employee) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EMPLOYEE_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "employeeCode")
    String updateEmployee(EAMContext context, Employee employee) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.EMPLOYEE_D)
    String deleteEmployee(EAMContext context, String employeeCode) throws EAMException;
}
