package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.workorders.EmployeeService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.Employee;
import net.datastream.schemas.mp_fields.CLASSID_Type;
import net.datastream.schemas.mp_fields.Employee_Type;
import net.datastream.schemas.mp_fields.StandardUserDefinedFields;
import net.datastream.schemas.mp_functions.mp7037_001.MP7037_GetEmployee_001;
import net.datastream.schemas.mp_functions.mp7038_001.MP7038_AddEmployee_001;
import net.datastream.schemas.mp_functions.mp7039_001.MP7039_SyncEmployee_001;
import net.datastream.schemas.mp_functions.mp7040_001.MP7040_DeleteEmployee_001;
import net.datastream.schemas.mp_results.mp7037_001.MP7037_GetEmployee_001_Result;
import net.datastream.schemas.mp_results.mp7038_001.MP7038_AddEmployee_001_Result;
import net.datastream.schemas.mp_results.mp7039_001.MP7039_SyncEmployee_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public EmployeeServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public BatchResponse<String> createEmployeeBatch(InforContext context, List<Employee> workOrderParam) {
		return tools.batchOperation(context, this::createEmployee, workOrderParam);
	}

	public BatchResponse<String> updateEmployeeBatch(InforContext context, List<Employee> workOrders) {
		return tools.batchOperation(context, this::updateEmployee, workOrders);
	}

	public Employee readEmployee(InforContext context, String employeeCode) throws InforException {

		MP7037_GetEmployee_001 request = new MP7037_GetEmployee_001();
		request.setEMPLOYEEID(new Employee_Type());
		request.getEMPLOYEEID().setEMPLOYEECODE(employeeCode);

		MP7037_GetEmployee_001_Result result = tools.performInforOperation(context, inforws::getEmployeeOp, request);

		Employee employee = tools.getInforFieldTools().transformInforObject(new Employee(), result.getResultData().getEmployee(), context);
		employee.setSupervisor(employee.getUserDefinedFields().getUdfnum01());
		employee.setPersonID(employee.getUserDefinedFields().getUdfnum01());

		return employee;
	}

	public String createEmployee(InforContext context, Employee employee) throws InforException {
		net.datastream.schemas.mp_entities.employee_001.Employee inforEmployee = new net.datastream.schemas.mp_entities.employee_001.Employee();
		populateInforObject(inforEmployee, employee, context);

		MP7038_AddEmployee_001 request = new MP7038_AddEmployee_001();
		request.setEmployee(inforEmployee);

		MP7038_AddEmployee_001_Result result =
			tools.performInforOperation(context, inforws::addEmployeeOp, request);
		return result.getResultData().getEMPLOYEEID().getEMPLOYEECODE();
	}

	public String updateEmployee(InforContext context, Employee employee) throws InforException {
		MP7037_GetEmployee_001 readRequest = new MP7037_GetEmployee_001();

		readRequest.setEMPLOYEEID(new Employee_Type());
		readRequest.getEMPLOYEEID().setEMPLOYEECODE(employee.getCode());

		MP7037_GetEmployee_001_Result readResult =
			tools.performInforOperation(context, inforws::getEmployeeOp, readRequest);

		net.datastream.schemas.mp_entities.employee_001.Employee inforEmployee = readResult.getResultData()
				.getEmployee();

		populateInforObject(inforEmployee, employee, context);

		MP7039_SyncEmployee_001 syncRequest = new MP7039_SyncEmployee_001();
		syncRequest.setEmployee(inforEmployee);

		MP7039_SyncEmployee_001_Result syncResult =
			tools.performInforOperation(context, inforws::syncEmployeeOp, syncRequest);
		return syncResult.getResultData().getEmployee().getEMPLOYEEID().getEMPLOYEECODE();
	}

	public String deleteEmployee(InforContext context, String employeeCode) throws InforException {
		MP7040_DeleteEmployee_001 request = new MP7040_DeleteEmployee_001();

		request.setEMPLOYEEID(new Employee_Type());
		request.getEMPLOYEEID().setEMPLOYEECODE(employeeCode);

		tools.performInforOperation(context, inforws::deleteEmployeeOp, request);
		return employeeCode;
	}

	private void populateInforObject(net.datastream.schemas.mp_entities.employee_001.Employee inforEmployee,
									 Employee employee, InforContext context) throws InforException {

		if (employee.getCode() != null) {
			if (inforEmployee.getEMPLOYEEID() == null) {
				inforEmployee.setEMPLOYEEID(new Employee_Type());
				inforEmployee.getEMPLOYEEID().setEMPLOYEECODE(employee.getCode());
				inforEmployee.getEMPLOYEEID().setORGANIZATIONID(tools.getOrganization(context));
			}
		}

		if (employee.getDescription() != null) {
			if (inforEmployee.getEMPLOYEEID() == null) {
				inforEmployee.setEMPLOYEEID(new Employee_Type());
			}
			inforEmployee.getEMPLOYEEID().setDESCRIPTION(employee.getDescription());
		}

		if (employee.getPhone() != null) {
			inforEmployee.setPHONE(employee.getPhone());
		}

		if (employee.getMobilePhone() != null) {
			inforEmployee.setMOBILEPHONENUMBER(employee.getMobilePhone());
		}

		if (employee.getAddress() != null) {
			inforEmployee.setADDRESS(employee.getAddress());
		}

		if (employee.getClazz() != null) {
			inforEmployee.setCLASSID(new CLASSID_Type());
			inforEmployee.getCLASSID().setCLASSCODE(employee.getClazz());
			inforEmployee.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
		}

		if (employee.getMRC() != null) {
			inforEmployee.setDEPARTMENTCODE(employee.getMRC());
		}

		// email has to be a proper email
		if (employee.getEmail() != null) {
			inforEmployee.setEMAIL(employee.getEmail());
		}

		if (employee.getUserCode() != null) {
			inforEmployee.setUSERCODE(employee.getUserCode());
		}

		if (employee.getTrade() != null) {
			inforEmployee.setTRADECODE(employee.getTrade());
		}

		if (inforEmployee.getStandardUserDefinedFields() == null) {
			inforEmployee.setStandardUserDefinedFields(new StandardUserDefinedFields());
		}

		if (employee.getSupervisor() != null) {
			inforEmployee.getStandardUserDefinedFields()
					.setUDFNUM01(tools.getDataTypeTools().encodeQuantity(employee.getSupervisor(), "Supervisor"));
		}

		if (employee.getPersonID() != null) {
			inforEmployee.getStandardUserDefinedFields()
					.setUDFNUM02(tools.getDataTypeTools().encodeQuantity(employee.getPersonID(), "PersonID"));
		}

		if (employee.getDepartment() != null) {
			inforEmployee.getStandardUserDefinedFields().setUDFCHAR02(employee.getDepartment());
		}

		if (employee.getGroup() != null) {
			inforEmployee.getStandardUserDefinedFields().setUDFCHAR03(employee.getGroup());
		}

		if (employee.getSection() != null) {
			inforEmployee.getStandardUserDefinedFields().setUDFCHAR04(employee.getSection());
		}

		if (employee.getPreferredLanguage() != null) {
			inforEmployee.getStandardUserDefinedFields().setUDFCHAR05(employee.getPreferredLanguage());
		}

		if (employee.getAccountBlocked() != null) {
			inforEmployee.getStandardUserDefinedFields().setUDFCHKBOX01(employee.getAccountBlocked());
		}

		if(employee.getOutOfService() != null) {
			inforEmployee.setOUTOFSERVICE(employee.getOutOfService());
		}

	}

}
