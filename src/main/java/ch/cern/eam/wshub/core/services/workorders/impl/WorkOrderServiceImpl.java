package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.comments.CommentService;
import ch.cern.eam.wshub.core.services.comments.impl.CommentServiceImpl;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.comments.entities.Comment;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.workorders.StandardWorkOrderService;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderService;
import ch.cern.eam.wshub.core.services.workorders.entities.StandardWorkOrder;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0023_001.MP0023_AddWorkOrder_001;
import net.datastream.schemas.mp_functions.mp0024_001.MP0024_GetWorkOrder_001;
import net.datastream.schemas.mp_functions.mp0025_001.MP0025_SyncWorkOrder_001;
import net.datastream.schemas.mp_functions.mp0026_001.MP0026_GetWorkOrderDefault_001;
import net.datastream.schemas.mp_functions.mp0055_001.MP0055_DeleteWorkOrder_001;
import net.datastream.schemas.mp_functions.mp7161_001.MP7161_ChangeWorkOrderStatus_001;
import net.datastream.schemas.mp_results.mp0023_001.MP0023_AddWorkOrder_001_Result;
import net.datastream.schemas.mp_results.mp0024_001.MP0024_GetWorkOrder_001_Result;
import net.datastream.schemas.mp_results.mp0026_001.MP0026_GetWorkOrderDefault_001_Result;
import net.datastream.schemas.mp_results.mp0026_001.ResultData;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;


public class WorkOrderServiceImpl implements WorkOrderService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;
	private CommentService comments;
	private StandardWorkOrderService standardWorkOrderService;

	public WorkOrderServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.comments = new CommentServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
		this.standardWorkOrderService = new StandardWorkOrderServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
	}

	//
	// BATCH WEB SERVICES
	//

	public BatchResponse<String> createWorkOrderBatch(InforContext context, List<WorkOrder> workOrderParam) {
		return tools.batchOperation(context, this::createWorkOrder, workOrderParam);
	}

	public BatchResponse<WorkOrder> readWorkOrderBatch(InforContext context, List<String> workOrderNumbers)  {
		return tools.batchOperation(context, this::readWorkOrder, workOrderNumbers);
	}

	public BatchResponse<String> updateWorkOrderBatch(InforContext context, List<WorkOrder> workOrders) {
		return tools.batchOperation(context, this::updateWorkOrder, workOrders);
	}

	public BatchResponse<String> deleteWorkOrderBatch(InforContext context, List<String> workOrderNumbers) {
		return tools.batchOperation(context, this::deleteWorkOrder, workOrderNumbers);
	}

	//
	// WORK ORDER CRUD
	//
	public WorkOrder readWorkOrder(InforContext context, String number) throws InforException {
		// Get Infor Work Order
		net.datastream.schemas.mp_entities.workorder_001.WorkOrder inforWorkOrder = readWorkOrderInfor(context, number);
		//
		return tools.getInforFieldTools().transformInforObject(new WorkOrder(), inforWorkOrder);
	}

	public net.datastream.schemas.mp_entities.workorder_001.WorkOrder readWorkOrderInfor(InforContext context, String number) throws InforException {
		MP0024_GetWorkOrder_001 getWorkOrder = new MP0024_GetWorkOrder_001();
		getWorkOrder.setWORKORDERID(new WOID_Type());
		getWorkOrder.getWORKORDERID().setJOBNUM(number);
		getWorkOrder.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));

		MP0024_GetWorkOrder_001_Result result =
			tools.performInforOperation(context, inforws::getWorkOrderOp, getWorkOrder);
		return result.getResultData().getWorkOrder();
	}

	public WorkOrder readWorkOrderDefault(InforContext context, String number) throws InforException {
		//
		// Fetch WO
		//
		MP0026_GetWorkOrderDefault_001 getWorkOrderDefault = new MP0026_GetWorkOrderDefault_001();

		getWorkOrderDefault.setORGANIZATIONID(new ORGANIZATIONID_Type());
		getWorkOrderDefault.getORGANIZATIONID().setORGANIZATIONCODE(context.getOrganizationCode());
		MP0026_GetWorkOrderDefault_001_Result getWODefaultResult =
			tools.performInforOperation(context, inforws::getWorkOrderDefaultOp, getWorkOrderDefault);
		ResultData resultData = getWODefaultResult.getResultData();
		//
		// Populate the 'workOrder' object
		//
		WorkOrder workOrder = new WorkOrder();

		// STATUS
		if (resultData.getSTATUS() != null) {
			workOrder.setStatusCode(resultData.getSTATUS().getSTATUSCODE());
		}

		// TYPE
		if (resultData.getTYPE() != null) {
			workOrder.setTypeCode(resultData.getTYPE().getTYPECODE());
		}

		// TARGET (SCHEDULED START) DATE
		if (resultData.getTARGETDATE() != null) {
			workOrder.setScheduledStartDate(tools.getDataTypeTools().decodeInforDate(resultData.getTARGETDATE()));
		}

		// SCHEDULED END DATE
		if (resultData.getSCHEDEND() != null) {
			workOrder.setScheduledEndDate(tools.getDataTypeTools().decodeInforDate(resultData.getSCHEDEND()));
		}

		// REPORTED DATE
		if (resultData.getREPORTED() != null) {
			workOrder.setReportedDate(tools.getDataTypeTools().decodeInforDate(resultData.getREPORTED()));
		}

		return workOrder;
	}

	public String createWorkOrder(InforContext context, WorkOrder workorderParam) throws InforException {
		net.datastream.schemas.mp_entities.workorder_001.WorkOrder inforWorkOrder = new net.datastream.schemas.mp_entities.workorder_001.WorkOrder();

		// REQUIRED
		inforWorkOrder.setWORKORDERID(new WOID_Type());
		inforWorkOrder.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		inforWorkOrder.getWORKORDERID().setJOBNUM("0");
		inforWorkOrder.setFIXED("V");

		// STANDARD WORK ORDER
		if (workorderParam.getStandardWO() != null && !workorderParam.getStandardWO().trim().equals("")) {
			StandardWorkOrder standardWorkOrder = standardWorkOrderService.readStandardWorkOrder(context, workorderParam.getStandardWO());
			workorderParam.setDescription(standardWorkOrder.getDesc());
			workorderParam.setClassCode(standardWorkOrder.getWoClassCode());
			workorderParam.setPriorityCode(standardWorkOrder.getPriorityCode());
			workorderParam.setTypeCode(standardWorkOrder.getWorkOrderTypeCode());
			workorderParam.setProblemCode(standardWorkOrder.getProblemCode());
			workorderParam.setCustomFields(standardWorkOrder.getCustomFields());
			workorderParam.setUserDefinedFields(standardWorkOrder.getUserDefinedFields());
		}

		inforWorkOrder.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
				context,
				toCodeString(inforWorkOrder.getCLASSID()),
				inforWorkOrder.getUSERDEFINEDAREA(),
				workorderParam.getClassCode(),
				"EVNT"));

		// POPULATE ALL OTHER FIELDS
		tools.getInforFieldTools().transformWSHubObject(inforWorkOrder, workorderParam, context);

		MP0023_AddWorkOrder_001 addWO = new MP0023_AddWorkOrder_001();
		addWO.setWorkOrder(inforWorkOrder);
		MP0023_AddWorkOrder_001_Result result = tools.performInforOperation(context, inforws::addWorkOrderOp, addWO);

		// Work Order has been created, check if comment should be added
		if (workorderParam.getComment() != null && !workorderParam.getComment().trim().equals("")) {
			Comment comment = new Comment();
			comment.setEntityCode("EVNT");
			comment.setEntityKeyCode(result.getResultData().getJOBNUM());
			comment.setText(workorderParam.getComment());
			comment.setTypeCode("*");
			comments.createComment(context, comment);
		}
		return result.getResultData().getJOBNUM();
	}

	public String updateWorkOrder(InforContext context, WorkOrder workorderParam) throws InforException {
		net.datastream.schemas.mp_entities.workorder_001.WorkOrder inforWorkOrder = readWorkOrderInfor(context, workorderParam.getNumber());

		// Check Custom fields. If they change, or now we have them
		inforWorkOrder.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
			context,
			toCodeString(inforWorkOrder.getCLASSID()),
			inforWorkOrder.getUSERDEFINEDAREA(),
			workorderParam.getClassCode(),
			"EVNT"));

		// SET ALL PROPERTIES
		tools.getInforFieldTools().transformWSHubObject(inforWorkOrder, workorderParam, context);

		// CALL INFOR WEB SERVICE
		MP0025_SyncWorkOrder_001 syncWO = new MP0025_SyncWorkOrder_001();
		syncWO.setWorkOrder(inforWorkOrder);

		if (workorderParam.isConfirmedIncompleteChecklist()) {
			syncWO.setConfirmincompletechecklist("confirmed");
		}

		tools.performInforOperation(context, inforws::syncWorkOrderOp, syncWO);
		return inforWorkOrder.getWORKORDERID().getJOBNUM();
	}

	public String deleteWorkOrder(InforContext context, String workOrderNumber) throws InforException {
		MP0055_DeleteWorkOrder_001 deleteWO = new MP0055_DeleteWorkOrder_001();
		deleteWO.setWORKORDERID(new WOID_Type());
		deleteWO.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		deleteWO.getWORKORDERID().setJOBNUM(workOrderNumber);

		tools.performInforOperation(context, inforws::deleteWorkOrderOp, deleteWO);
		return workOrderNumber;
	}

	public String updateWorkOrderStatus(InforContext context, String workOrderNumber, String statusCode) throws InforException {
		MP7161_ChangeWorkOrderStatus_001 changeWOStatus = new MP7161_ChangeWorkOrderStatus_001();
		changeWOStatus.setWORKORDERID(new WOID_Type());
		changeWOStatus.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		changeWOStatus.getWORKORDERID().setJOBNUM(workOrderNumber);
		changeWOStatus.setNEWSTATUS(new STATUS_Type());
		changeWOStatus.getNEWSTATUS().setSTATUSCODE(statusCode);

		tools.performInforOperation(context, inforws::changeWorkOrderStatusOp, changeWOStatus);
		return workOrderNumber;
	}

}
