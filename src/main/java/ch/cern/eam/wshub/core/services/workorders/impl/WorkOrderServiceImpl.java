package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.comments.CommentService;
import ch.cern.eam.wshub.core.services.comments.impl.CommentServiceImpl;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.comments.entities.Comment;
import ch.cern.eam.wshub.core.services.equipment.impl.EquipmentTools;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.EntityId;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLEntry;
import ch.cern.eam.wshub.core.services.userdefinedscreens.impl.UserDefinedListServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.StandardWorkOrderService;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderService;
import ch.cern.eam.wshub.core.services.workorders.entities.StandardWorkOrder;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.GridTools;
import ch.cern.eam.wshub.core.tools.EAMException;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.decodeBoolean;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;
import static ch.cern.eam.wshub.core.tools.Tools.extractEntityCode;
import static ch.cern.eam.wshub.core.tools.Tools.extractOrganizationCode;


public class WorkOrderServiceImpl implements WorkOrderService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;
	private CommentService comments;
	private StandardWorkOrderService standardWorkOrderService;
	private GridsService gridsService;
	private UserDefinedListService userDefinedListService;

	public WorkOrderServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
		this.comments = new CommentServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
		this.standardWorkOrderService = new StandardWorkOrderServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
		this.gridsService = new GridsServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
		this.userDefinedListService = new UserDefinedListServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
	}

	//
	// BATCH WEB SERVICES
	//

	public BatchResponse<String> createWorkOrderBatch(EAMContext context, List<WorkOrder> workOrderParam) {
		return tools.batchOperation(context, this::createWorkOrder, workOrderParam);
	}

	public BatchResponse<WorkOrder> readWorkOrderBatch(EAMContext context, List<String> workOrderNumbers)  {
		return tools.batchOperation(context, this::readWorkOrder, workOrderNumbers);
	}

	public BatchResponse<String> updateWorkOrderBatch(EAMContext context, List<WorkOrder> workOrders) {
		return tools.batchOperation(context, this::updateWorkOrder, workOrders);
	}

	public BatchResponse<String> deleteWorkOrderBatch(EAMContext context, List<String> workOrderNumbers) {
		return tools.batchOperation(context, this::deleteWorkOrder, workOrderNumbers);
	}

	//
	// WORK ORDER CRUD
	//
	public WorkOrder readWorkOrder(EAMContext context, String number) throws EAMException {
		// Get EAM Work Order
		MP0024_GetWorkOrder_001_Result result = readWorkOrderEAM(context, extractEntityCode(number), extractOrganizationCode(number));
		//
		WorkOrder workOrder = tools.getEAMFieldTools().transformEAMObject(new WorkOrder(), result.getResultData().getWorkOrder(), context);
		workOrder.setJtAuthCanUpdate(decodeBoolean(result.getJtauth_Can_Update()));

		// Fetching missing descriptions and UDL not returned by EAM web service
		tools.processRunnables(
			() -> workOrder.setAssignedToDesc(tools.getFieldDescriptionsTools().readPersonDesc(context, workOrder.getAssignedTo())),
			() -> workOrder.setDepartmentDesc(tools.getFieldDescriptionsTools().readDepartmentDesc(context, workOrder.getDepartmentCode())),
			() -> workOrder.setClassDesc(tools.getFieldDescriptionsTools().readClassDesc(context, "EVNT", workOrder.getClassCode())),
			() -> workOrder.setCostCodeDesc(tools.getFieldDescriptionsTools().readCostCodeDesc(context, workOrder.getCostCode())),
			() -> workOrder.setSystemStatusCode(tools.getFieldDescriptionsTools().readSystemCodeForUserCode(context, "EVST", workOrder.getStatusCode())),
			() -> userDefinedListService.readUDLToEntity(context, workOrder, new EntityId("EVNT", extractEntityCode(number)))
		);

		return workOrder;
	}

	public MP0024_GetWorkOrder_001_Result readWorkOrderEAM(EAMContext context, String number, String organization) throws EAMException {
		MP0024_GetWorkOrder_001 getWorkOrder = new MP0024_GetWorkOrder_001();
		getWorkOrder.setWORKORDERID(new WOID_Type());
		getWorkOrder.getWORKORDERID().setJOBNUM(number);
		getWorkOrder.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context, organization));

		return tools.performEAMOperation(context, eamws::getWorkOrderOp, getWorkOrder);
	}

	public WorkOrder readWorkOrderDefault(EAMContext context, String number) throws EAMException {
		//
		// Fetch WO
		//
		MP0026_GetWorkOrderDefault_001 getWorkOrderDefault = new MP0026_GetWorkOrderDefault_001();

		getWorkOrderDefault.setORGANIZATIONID(new ORGANIZATIONID_Type());
		getWorkOrderDefault.getORGANIZATIONID().setORGANIZATIONCODE(context.getOrganizationCode());
		MP0026_GetWorkOrderDefault_001_Result getWODefaultResult =
			tools.performEAMOperation(context, eamws::getWorkOrderDefaultOp, getWorkOrderDefault);
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
			workOrder.setScheduledStartDate(tools.getDataTypeTools().decodeEAMDate(resultData.getTARGETDATE()));
		}

		// SCHEDULED END DATE
		if (resultData.getSCHEDEND() != null) {
			workOrder.setScheduledEndDate(tools.getDataTypeTools().decodeEAMDate(resultData.getSCHEDEND()));
		}

		// REPORTED DATE
		if (resultData.getREPORTED() != null) {
			workOrder.setReportedDate(tools.getDataTypeTools().decodeEAMDate(resultData.getREPORTED()));
		}

		workOrder.setUserDefinedList(new HashMap<>());

		return workOrder;
	}

	/*
		This method creates a work order based on workorderParam
		There is an additional field, workOrderParam.copyFrom, which signals this method to copy from this work order
		Currently, with the exception of the default values, the behavior of EAM EAM Extended is matched.

		DEFAULT VALUE IMPLEMENTATION NOTES
		Take note when implementing the default values that there is an install parameter that determines whether
		defaults should be applied before or after the fields from workorderParam are applied. The behavior at CERN is
		to apply the defaults after the fields from workorderParam are applied.

		For work orders, when the default values are applied after the fields from workorderParam the values set in
		EAM ("final values") work as follows, depending on the default value of the corresponding field:
			- default value cleared: final value is the workorderParam value
			- default value is a concrete value: final value is the default value
			- default value is "NULL": final value is empty

		EAM Light implements this default value logic in its frontend.
		END OF DEFAULT VALUE IMPLEMENTATION NOTES
	 */
	public String createWorkOrder(EAMContext context, WorkOrder workorderParam) throws EAMException {
		net.datastream.schemas.mp_entities.workorder_001.WorkOrder eamWorkOrder;

		if(workorderParam.getCopyFrom() == null) {
			eamWorkOrder = new net.datastream.schemas.mp_entities.workorder_001.WorkOrder();
		} else {
			eamWorkOrder = duplicateWorkOrder(context, workorderParam.getCopyFrom());
		}

		// REQUIRED
		if(eamWorkOrder.getWORKORDERID() == null) {
			eamWorkOrder.setWORKORDERID(new WOID_Type());
		}

		eamWorkOrder.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context, workorderParam.getOrganization()));
		eamWorkOrder.getWORKORDERID().setJOBNUM("0");
		eamWorkOrder.setFIXED("V");

		// STANDARD WORK ORDER
		if (workorderParam.getStandardWO() != null && !workorderParam.getStandardWO().trim().equals("")) {
			StandardWorkOrder standardWorkOrder = standardWorkOrderService.readStandardWorkOrder(context, workorderParam.getStandardWO());

			if (workorderParam.getDescription() == null) {
				workorderParam.setDescription(standardWorkOrder.getDesc());
			}

			if (workorderParam.getClassCode() == null) {
				workorderParam.setClassCode(standardWorkOrder.getWoClassCode());
			}

			if (workorderParam.getPriorityCode() == null) {
				workorderParam.setPriorityCode(standardWorkOrder.getPriorityCode());
			}

			if (workorderParam.getTypeCode() == null) {
				workorderParam.setTypeCode(standardWorkOrder.getWorkOrderTypeCode());
			}

			if (workorderParam.getProblemCode() == null) {
				workorderParam.setProblemCode(standardWorkOrder.getProblemCode());
			}

			//TODO more intelligent merge required
			if (workorderParam.getCustomFields() == null) {
				workorderParam.setCustomFields(standardWorkOrder.getCustomFields());
			}

			//TODO more intelligent merge required
			if (workorderParam.getUserDefinedFields() == null) {
				workorderParam.setUserDefinedFields(standardWorkOrder.getUserDefinedFields());
			}
		}

		eamWorkOrder.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
				context,
				toCodeString(eamWorkOrder.getCLASSID()),
				eamWorkOrder.getUSERDEFINEDAREA(),
				workorderParam.getClassCode(),
				"EVNT"));

		// POPULATE ALL OTHER FIELDS
		tools.getEAMFieldTools().transformWSHubObject(eamWorkOrder, workorderParam, context);

		if(workorderParam.getCopyFrom() != null
				&& eamWorkOrder.getUSERDEFINEDAREA() != null
				&& eamWorkOrder.getUSERDEFINEDAREA().getCUSTOMFIELD() != null) {
			eamWorkOrder.getUSERDEFINEDAREA().getCUSTOMFIELD().stream()
					.forEach(customField -> customField.setChanged("true"));
		}

		MP0023_AddWorkOrder_001 addWO = new MP0023_AddWorkOrder_001();
		addWO.setWorkOrder(eamWorkOrder);
		MP0023_AddWorkOrder_001_Result result = tools.performEAMOperation(context, eamws::addWorkOrderOp, addWO);

		// Work Order has been created, check if comment should be added
		if (workorderParam.getComment() != null && !workorderParam.getComment().trim().equals("")) {
			Comment comment = new Comment();
			comment.setEntityCode("EVNT");
			comment.setEntityKeyCode(result.getResultData().getJOBNUM());
			comment.setText(workorderParam.getComment());
			comment.setTypeCode("*");
			comments.createComment(context, comment);
		}

		String workOrderNumber = result.getResultData().getJOBNUM();
		userDefinedListService.writeUDLToEntityCopyFrom(context,
			workorderParam, new EntityId("EVNT", workOrderNumber));
		return workOrderNumber;
	}

	private net.datastream.schemas.mp_entities.workorder_001.WorkOrder duplicateWorkOrder(
			EAMContext context,
			String workOrderCode)
				throws EAMException {
		net.datastream.schemas.mp_entities.workorder_001.WorkOrder workOrder = readWorkOrderEAM(context, extractEntityCode(workOrderCode), extractOrganizationCode(workOrderCode)).getResultData().getWorkOrder();

		// As this work order is read directly from EAM, we can assume that workOrder.getWORKORDERID is not null,
		// and thus there is no need for a null check
		workOrder.getWORKORDERID().setJOBNUM("0");

		if (isClearingActivitiesRequired(context, workOrder)) {
			workOrder.setActivities(null);
		}

		return workOrder;
	}

	private boolean isClearingActivitiesRequired(EAMContext context, net.datastream.schemas.mp_entities.workorder_001.WorkOrder workOrder) throws EAMException {
		// we only need to clear the activities if there is a standard work order present
		if (workOrder.getSTANDARDWO() == null || workOrder.getSTANDARDWO().getSTDWOCODE() == null) {
			return false;
		}

		// if the activities are already null, there is no need to clear them, as they are already cleared
		if (workOrder.getActivities() == null ||
				workOrder.getActivities().getActivity() == null ||
				workOrder.getActivities().getActivity().size() == 0) {
			return false;
		}

		// otherwise, we want to clear the activities if the standard work order has more than 0 activities
		GridRequest gridRequest = new GridRequest("WSSTWO_ACT", GridRequest.GRIDTYPE.LIST, 1);
		gridRequest.addParam("param.stwocode", workOrder.getSTANDARDWO().getSTDWOCODE());
		return GridTools.isNotEmpty(gridsService.executeQuery(context, gridRequest));
	}

	public String updateWorkOrder(EAMContext context, WorkOrder workorderParam) throws EAMException {
		net.datastream.schemas.mp_entities.workorder_001.WorkOrder eamWorkOrder = readWorkOrderEAM(context, workorderParam.getNumber(), workorderParam.getOrganization()).getResultData().getWorkOrder();

		// Check Custom fields. If they change, or now we have them
		eamWorkOrder.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
			context,
			toCodeString(eamWorkOrder.getCLASSID()),
			eamWorkOrder.getUSERDEFINEDAREA(),
			workorderParam.getClassCode(),
			"EVNT"));

		// SET ALL PROPERTIES
		tools.getEAMFieldTools().transformWSHubObject(eamWorkOrder, workorderParam, context);

		// Has to be nullified, otherwise the activity dates are not updated correctly (EAM Incident 16467875)
		eamWorkOrder.setActivities(null);

		// CALL INFOR WEB SERVICE
		MP0025_SyncWorkOrder_001 syncWO = new MP0025_SyncWorkOrder_001();
		syncWO.setWorkOrder(eamWorkOrder);

		if (workorderParam.isConfirmedIncompleteChecklist() != null && workorderParam.isConfirmedIncompleteChecklist()) {
			syncWO.setConfirmincompletechecklist("confirmed");
		}

		tools.performEAMOperation(context, eamws::syncWorkOrderOp, syncWO);

		String workOrderNumber = eamWorkOrder.getWORKORDERID().getJOBNUM();
		userDefinedListService.writeUDLToEntity(context,
			workorderParam, new EntityId("EVNT", workOrderNumber));
		return workOrderNumber;
	}

	public String deleteWorkOrder(EAMContext context, String workOrderNumber) throws EAMException {
		MP0055_DeleteWorkOrder_001 deleteWO = new MP0055_DeleteWorkOrder_001();
		deleteWO.setWORKORDERID(new WOID_Type());
		deleteWO.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context, extractOrganizationCode(workOrderNumber)));
		deleteWO.getWORKORDERID().setJOBNUM(extractEntityCode(workOrderNumber));

		tools.performEAMOperation(context, eamws::deleteWorkOrderOp, deleteWO);
		userDefinedListService.deleteUDLFromEntity(context, new EntityId("EVNT", workOrderNumber));
		return workOrderNumber;
	}

	public String updateWorkOrderStatus(EAMContext context, String workOrderNumber, String statusCode) throws EAMException {
		MP7161_ChangeWorkOrderStatus_001 changeWOStatus = new MP7161_ChangeWorkOrderStatus_001();
		changeWOStatus.setWORKORDERID(new WOID_Type());
		changeWOStatus.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context, extractOrganizationCode(workOrderNumber)));
		changeWOStatus.getWORKORDERID().setJOBNUM(extractEntityCode(workOrderNumber));
		changeWOStatus.setNEWSTATUS(new STATUS_Type());
		changeWOStatus.getNEWSTATUS().setSTATUSCODE(statusCode);

		tools.performEAMOperation(context, eamws::changeWorkOrderStatusOp, changeWOStatus);
		return workOrderNumber;
	}
}
