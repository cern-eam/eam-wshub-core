package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.comments.CommentService;
import ch.cern.eam.wshub.core.services.comments.impl.CommentServiceImpl;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.comments.entities.Comment;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import net.datastream.schemas.mp_entities.standardworkorder_001.StandardWorkOrder;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
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
import javax.xml.ws.Holder;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class WorkOrderServiceImpl implements WorkOrderService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;
	private CommentService comments;
	private StandardWorkOrderServiceImpl standardWorkOrderServiceImpl;

	public WorkOrderServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.comments = new CommentServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
		this.standardWorkOrderServiceImpl = new StandardWorkOrderServiceImpl(applicationData, tools, inforWebServicesToolkitClient);

	}

	//
	// BATCH WEB SERVICES
	//

	public BatchResponse<String> createWorkOrderBatch(InforContext context, List<WorkOrder> workOrderParam)
			throws InforException {
		List<Callable<String>> callableList = workOrderParam.stream()
				.<Callable<String>>map(wo -> () -> createWorkOrder(context, wo))
				.collect(Collectors.toList());

		return tools.processCallables(callableList);
	}

	public BatchResponse<WorkOrder> readWorkOrderBatch(InforContext context, List<String> workOrderNumbers)  {
		List<Callable<WorkOrder>> callableList = workOrderNumbers.stream()
				.<Callable<WorkOrder>>map(workOrderNumber -> () -> readWorkOrder(context, workOrderNumber))
				.collect(Collectors.toList());
		return tools.processCallables(callableList);
	}

	public BatchResponse<String> updateWorkOrderBatch(InforContext context, List<WorkOrder> workOrders)
			throws InforException {
		List<Callable<String>> callableList = workOrders.stream()
				.<Callable<String>>map(workOrder -> () -> updateWorkOrder(context, workOrder))
				.collect(Collectors.toList());
		return tools.processCallables(callableList);
	}

	public BatchResponse<String> deleteWorkOrderBatch(InforContext context, List<String> workOrderNumbers)
			throws InforException {
		List<Callable<String>> callableList = workOrderNumbers.stream()
				.<Callable<String>>map(workOrderNumber -> () -> deleteWorkOrder(context, workOrderNumber))
				.collect(Collectors.toList());
		return tools.processCallables(callableList);
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

		MP0024_GetWorkOrder_001_Result result = null;
		if (context.getCredentials() != null) {
			result = inforws.getWorkOrderOp(getWorkOrder, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.getWorkOrderOp(getWorkOrder, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		return result.getResultData().getWorkOrder();
	}

	public WorkOrder readWorkOrderDefault(InforContext context, String number) throws InforException {
		//
		// Fetch WO
		//
		MP0026_GetWorkOrderDefault_001 getWorkOrderDefault = new MP0026_GetWorkOrderDefault_001();

		getWorkOrderDefault.setORGANIZATIONID(new ORGANIZATIONID_Type());
		getWorkOrderDefault.getORGANIZATIONID().setORGANIZATIONCODE(context.getOrganizationCode());
		MP0026_GetWorkOrderDefault_001_Result getWODefaultResult = null;

		if (context.getCredentials() != null) {
			getWODefaultResult = inforws.getWorkOrderDefaultOp(getWorkOrderDefault, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			getWODefaultResult = inforws.getWorkOrderDefaultOp(getWorkOrderDefault, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
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

		String woClass = "*";

		// REQUIRED
		inforWorkOrder.setWORKORDERID(new WOID_Type());
		inforWorkOrder.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		inforWorkOrder.getWORKORDERID().setJOBNUM("0");
		inforWorkOrder.setFIXED("V");

		// STANDARD WORK ORDER
		if (workorderParam.getStandardWO() != null && !workorderParam.getStandardWO().trim().equals("")) {
			StandardWorkOrder standardWO = standardWorkOrderServiceImpl.readStandardWorkOrderInfor(context, workorderParam.getStandardWO());
			inforWorkOrder.setPERMITREVIEWEDBY(standardWO.getPERMITREVIEWEDBY());
			inforWorkOrder.setPRIORITY(standardWO.getPRIORITY());
			inforWorkOrder.setPROBLEMCODEID(standardWO.getPROBLEMCODEID());
			inforWorkOrder.setSAFETYREVIEWEDBY(standardWO.getSAFETYREVIEWEDBY());
			inforWorkOrder.setSTANDARDWO(standardWO.getSTANDARDWO());
			inforWorkOrder.setCLASSID(standardWO.getWORKORDERCLASSID());

			CustomField[] swoCFs = tools.getCustomFieldsTools().readInforCustomFields(standardWO.getUSERDEFINEDAREA());
			CustomField[] paramCFs = workorderParam.getCustomFields() == null ?
					new CustomField[0]
					: workorderParam.getCustomFields()
					;

			//inforWorkOrder.setUSERDEFINEDAREA(standardWO.getUSERDEFINEDAREA());
			if (standardWO.getWORKORDERCLASSID() != null
					&& standardWO.getWORKORDERCLASSID().getCLASSCODE() != null){
				woClass = standardWO.getWORKORDERCLASSID().getCLASSCODE();
			}

			CustomField[] customFields = Stream.concat(
					Arrays.stream(swoCFs)
					.filter(
							swo -> Arrays.stream(paramCFs).noneMatch(
									cf -> swo.getCode().equals(cf.getCode())
							)
					),
					Arrays.stream(paramCFs)
				)
				.toArray(CustomField[]::new);
			workorderParam.setCustomFields(customFields);

			inforWorkOrder.setTYPE(standardWO.getWORKORDERTYPE());
			inforWorkOrder.getWORKORDERID().setORGANIZATIONID(standardWO.getSTANDARDWO().getORGANIZATIONID());
			inforWorkOrder.getWORKORDERID().setDESCRIPTION(standardWO.getSTANDARDWO().getDESCRIPTION());

			// Create temporary workorder to make use of the transformWSHubObject method to populate udfs
			WorkOrder wo = new WorkOrder();
			wo.setUserDefinedFields(tools.getUDFTools().readInforUserDefinedFields(standardWO.getUserDefinedFields()));
			tools.getInforFieldTools().transformWSHubObject(inforWorkOrder, wo, context);
		}

		// CUSTOM FIELDS
		if (workorderParam.getClassCode() != null && !workorderParam.getClassCode().trim().equals("")) {
			woClass = workorderParam.getClassCode();
		}
		USERDEFINEDAREA cfs = tools.getCustomFieldsTools().getInforCustomFields(context, "EVNT", woClass);
		inforWorkOrder.setUSERDEFINEDAREA(cfs);

		// POPULATE ALL OTHER FIELDS
		tools.getInforFieldTools().transformWSHubObject(inforWorkOrder, workorderParam, context);

		MP0023_AddWorkOrder_001 addWO = new MP0023_AddWorkOrder_001();
		addWO.setWorkOrder(inforWorkOrder);
		MP0023_AddWorkOrder_001_Result result;

		if (context.getCredentials() != null) {
			result = inforws.addWorkOrderOp(addWO, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.addWorkOrderOp(addWO, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

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
		if (workorderParam.getClassCode() != null && (inforWorkOrder.getCLASSID() == null
				|| !workorderParam.getClassCode().toUpperCase().equals(inforWorkOrder.getCLASSID().getCLASSCODE()))) {
			inforWorkOrder.setUSERDEFINEDAREA(
					tools.getCustomFieldsTools().getInforCustomFields(context, "EVNT", workorderParam.getClassCode().toUpperCase()));
		}

		// SET ALL PROPERTIES
		tools.getInforFieldTools().transformWSHubObject(inforWorkOrder, workorderParam, context);

		// CALL INFOR WEB SERVICE
		MP0025_SyncWorkOrder_001 syncWO = new MP0025_SyncWorkOrder_001();
		syncWO.setWorkOrder(inforWorkOrder);

		if (context.getCredentials() != null) {
			inforws.syncWorkOrderOp(syncWO, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.syncWorkOrderOp(syncWO, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		return inforWorkOrder.getWORKORDERID().getJOBNUM();
	}

	public String deleteWorkOrder(InforContext context, String workOrderNumber) throws InforException {

		MP0055_DeleteWorkOrder_001 deleteWO = new MP0055_DeleteWorkOrder_001();
		deleteWO.setWORKORDERID(new WOID_Type());
		deleteWO.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		deleteWO.getWORKORDERID().setJOBNUM(workOrderNumber);

		if (context.getCredentials() != null) {
			inforws.deleteWorkOrderOp(deleteWO, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.deleteWorkOrderOp(deleteWO, "*", null, null, new Holder<>(tools.createInforSession(context)),
					tools.createMessageConfig(), tools.getTenant(context));
		}
		return workOrderNumber;
	}

	public String updateWorkOrderStatus(InforContext context, String workOrderNumber, String statusCode) throws InforException {

		MP7161_ChangeWorkOrderStatus_001 changeWOStatus = new MP7161_ChangeWorkOrderStatus_001();
		changeWOStatus.setWORKORDERID(new WOID_Type());
		changeWOStatus.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		changeWOStatus.getWORKORDERID().setJOBNUM(workOrderNumber);
		changeWOStatus.setNEWSTATUS(new STATUS_Type());
		changeWOStatus.getNEWSTATUS().setSTATUSCODE(statusCode);

		if (context.getCredentials() != null) {
			inforws.changeWorkOrderStatusOp(changeWOStatus, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.changeWorkOrderStatusOp(changeWOStatus, "*", null, null,
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return workOrderNumber;
	}

}
