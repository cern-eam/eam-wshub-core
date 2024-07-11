package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.workorders.EmployeeService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public EmployeeServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}

	public BatchResponse<String> createEmployeeBatch(EAMContext context, List<Employee> workOrderParam) {
		return tools.batchOperation(context, this::createEmployee, workOrderParam);
	}

	public BatchResponse<String> updateEmployeeBatch(EAMContext context, List<Employee> workOrders) {
		return tools.batchOperation(context, this::updateEmployee, workOrders);
	}

	public Employee readEmployee(EAMContext context, String employeeCode) throws EAMException {

		MP7037_GetEmployee_001 request = new MP7037_GetEmployee_001();
		request.setEMPLOYEEID(new Employee_Type());
		request.getEMPLOYEEID().setEMPLOYEECODE(employeeCode);

		MP7037_GetEmployee_001_Result result = tools.performEAMOperation(context, eamws::getEmployeeOp, request);

		Employee employee = tools.getEAMFieldTools().transformEAMObject(new Employee(), result.getResultData().getEmployee(), context);
		employee.setSupervisor(employee.getUserDefinedFields().getUdfnum01());
		employee.setPersonID(employee.getUserDefinedFields().getUdfnum01());

		return employee;
	}

	public String createEmployee(EAMContext context, Employee employee) throws EAMException {
		net.datastream.schemas.mp_entities.employee_001.Employee eamEmployee = new net.datastream.schemas.mp_entities.employee_001.Employee();
		populateEAMObject(eamEmployee, employee, context);

		MP7038_AddEmployee_001 request = new MP7038_AddEmployee_001();
		request.setEmployee(eamEmployee);

		MP7038_AddEmployee_001_Result result =
			tools.performEAMOperation(context, eamws::addEmployeeOp, request);
		return result.getResultData().getEMPLOYEEID().getEMPLOYEECODE();
	}

	public String updateEmployee(EAMContext context, Employee employee) throws EAMException {
		MP7037_GetEmployee_001 readRequest = new MP7037_GetEmployee_001();

		readRequest.setEMPLOYEEID(new Employee_Type());
		readRequest.getEMPLOYEEID().setEMPLOYEECODE(employee.getCode());

		MP7037_GetEmployee_001_Result readResult =
			tools.performEAMOperation(context, eamws::getEmployeeOp, readRequest);

		net.datastream.schemas.mp_entities.employee_001.Employee eamEmployee = readResult.getResultData()
				.getEmployee();

		populateEAMObject(eamEmployee, employee, context);

		MP7039_SyncEmployee_001 syncRequest = new MP7039_SyncEmployee_001();
		syncRequest.setEmployee(eamEmployee);

		MP7039_SyncEmployee_001_Result syncResult =
			tools.performEAMOperation(context, eamws::syncEmployeeOp, syncRequest);
		return syncResult.getResultData().getEmployee().getEMPLOYEEID().getEMPLOYEECODE();
	}

	public String deleteEmployee(EAMContext context, String employeeCode) throws EAMException {
		MP7040_DeleteEmployee_001 request = new MP7040_DeleteEmployee_001();

		request.setEMPLOYEEID(new Employee_Type());
		request.getEMPLOYEEID().setEMPLOYEECODE(employeeCode);

		tools.performEAMOperation(context, eamws::deleteEmployeeOp, request);
		return employeeCode;
	}

	private void populateEAMObject(net.datastream.schemas.mp_entities.employee_001.Employee eamEmployee,
									 Employee employee, EAMContext context) throws EAMException {

		if (employee.getCode() != null) {
			if (eamEmployee.getEMPLOYEEID() == null) {
				eamEmployee.setEMPLOYEEID(new Employee_Type());
				eamEmployee.getEMPLOYEEID().setEMPLOYEECODE(employee.getCode());
				eamEmployee.getEMPLOYEEID().setORGANIZATIONID(tools.getOrganization(context));
			}
		}

		if (employee.getDescription() != null) {
			if (eamEmployee.getEMPLOYEEID() == null) {
				eamEmployee.setEMPLOYEEID(new Employee_Type());
			}
			eamEmployee.getEMPLOYEEID().setDESCRIPTION(employee.getDescription());
		}

		if (employee.getPhone() != null) {
			eamEmployee.setPHONE(employee.getPhone());
		}

		if (employee.getMobilePhone() != null) {
			eamEmployee.setMOBILEPHONENUMBER(employee.getMobilePhone());
		}

		if (employee.getAddress() != null) {
			eamEmployee.setADDRESS(employee.getAddress());
		}

		if (employee.getClazz() != null) {
			eamEmployee.setCLASSID(new CLASSID_Type());
			eamEmployee.getCLASSID().setCLASSCODE(employee.getClazz());
			eamEmployee.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
		}

		if (employee.getMRC() != null) {
			eamEmployee.setDEPARTMENTCODE(employee.getMRC());
		}

		// email has to be a proper email
		if (employee.getEmail() != null) {
			eamEmployee.setEMAIL(employee.getEmail());
		}

		if (employee.getUserCode() != null) {
			eamEmployee.setUSERCODE(employee.getUserCode());
		}

		if (employee.getTrade() != null) {
			eamEmployee.setTRADECODE(employee.getTrade());
		}

		if (eamEmployee.getStandardUserDefinedFields() == null) {
			eamEmployee.setStandardUserDefinedFields(new StandardUserDefinedFields());
		}

		if (employee.getSupervisor() != null) {
			eamEmployee.getStandardUserDefinedFields()
					.setUDFNUM01(tools.getDataTypeTools().encodeQuantity(employee.getSupervisor(), "Supervisor"));
		}

		if (employee.getPersonID() != null) {
			eamEmployee.getStandardUserDefinedFields()
					.setUDFNUM02(tools.getDataTypeTools().encodeQuantity(employee.getPersonID(), "PersonID"));
		}

		if (employee.getDepartment() != null) {
			eamEmployee.getStandardUserDefinedFields().setUDFCHAR02(employee.getDepartment());
		}

		if (employee.getGroup() != null) {
			eamEmployee.getStandardUserDefinedFields().setUDFCHAR03(employee.getGroup());
		}

		if (employee.getSection() != null) {
			eamEmployee.getStandardUserDefinedFields().setUDFCHAR04(employee.getSection());
		}

		if (employee.getPreferredLanguage() != null) {
			eamEmployee.getStandardUserDefinedFields().setUDFCHAR05(employee.getPreferredLanguage());
		}

		if (employee.getAccountBlocked() != null) {
			eamEmployee.getStandardUserDefinedFields().setUDFCHKBOX01(employee.getAccountBlocked());
		}

		if(employee.getOutOfService() != null) {
			eamEmployee.setOUTOFSERVICE(employee.getOutOfService());
		}

	}

}
