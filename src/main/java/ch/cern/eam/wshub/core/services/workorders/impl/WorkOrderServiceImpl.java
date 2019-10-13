package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.comments.CommentService;
import ch.cern.eam.wshub.core.services.comments.impl.CommentServiceImpl;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.comments.entities.Comment;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import net.datastream.schemas.mp_entities.standardworkorder_001.StandardWorkOrder;
import net.datastream.schemas.mp_entities.workorder_001.UserDefinedFields;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0023_001.MP0023_AddWorkOrder_001;
import net.datastream.schemas.mp_functions.mp0024_001.MP0024_GetWorkOrder_001;
import net.datastream.schemas.mp_functions.mp0025_001.MP0025_SyncWorkOrder_001;
import net.datastream.schemas.mp_functions.mp0026_001.MP0026_GetWorkOrderDefault_001;
import net.datastream.schemas.mp_functions.mp0055_001.MP0055_DeleteWorkOrder_001;
import net.datastream.schemas.mp_functions.mp7082_001.MP7082_GetStandardWorkOrder_001;
import net.datastream.schemas.mp_functions.mp7161_001.MP7161_ChangeWorkOrderStatus_001;
import net.datastream.schemas.mp_results.mp0023_001.MP0023_AddWorkOrder_001_Result;
import net.datastream.schemas.mp_results.mp0024_001.MP0024_GetWorkOrder_001_Result;
import net.datastream.schemas.mp_results.mp0026_001.MP0026_GetWorkOrderDefault_001_Result;
import net.datastream.schemas.mp_results.mp0026_001.ResultData;
import net.datastream.schemas.mp_results.mp7082_001.MP7082_GetStandardWorkOrder_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class WorkOrderServiceImpl implements WorkOrderService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;
	private CommentService comments;

	public WorkOrderServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.comments = new CommentServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
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
		//
		// Fetch WO
		//
		MP0024_GetWorkOrder_001 getWorkOrder = new MP0024_GetWorkOrder_001();

		WOID_Type woid = new WOID_Type();
		woid.setJOBNUM(number);
		woid.setORGANIZATIONID(tools.getOrganization(context));
		getWorkOrder.setWORKORDERID(woid);
		MP0024_GetWorkOrder_001_Result getWOResult = null;
		if (context.getCredentials() != null) {
			getWOResult = inforws.getWorkOrderOp(getWorkOrder, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			getWOResult = inforws.getWorkOrderOp(getWorkOrder, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		net.datastream.schemas.mp_entities.workorder_001.WorkOrder inforWorkOrder = getWOResult.getResultData().getWorkOrder();
		//
		// Populate the 'workOrder' object
		//
		WorkOrder workOrder = new WorkOrder();
		// EVENT TYPE
		if (getWOResult.getResultData().getEVENTTYPE() != null) {
			workOrder.setEventType(getWOResult.getResultData().getEVENTTYPE());
		}
		// DESCRIPTION
		if (inforWorkOrder.getWORKORDERID() != null) {
			workOrder.setDescription(inforWorkOrder.getWORKORDERID().getDESCRIPTION());
			workOrder.setNumber(inforWorkOrder.getWORKORDERID().getJOBNUM());
		}
		// CLASS
		if (inforWorkOrder.getCLASSID() != null) {
			workOrder.setClassCode(inforWorkOrder.getCLASSID().getCLASSCODE());
			workOrder.setClassDesc(tools.getFieldDescriptionsTools().readClassDesc("EVNT", workOrder.getClassCode()));
		}
		// STATUS
		if (inforWorkOrder.getSTATUS() != null) {
			workOrder.setStatusCode(inforWorkOrder.getSTATUS().getSTATUSCODE());
			workOrder.setStatusDesc(inforWorkOrder.getSTATUS().getDESCRIPTION());
		}
		// TYPE
		if (inforWorkOrder.getTYPE() != null) {
			workOrder.setTypeCode(inforWorkOrder.getTYPE().getTYPECODE());
			workOrder.setTypeDesc(inforWorkOrder.getTYPE().getDESCRIPTION());
		}
		// DEPARTMENT
		if (inforWorkOrder.getDEPARTMENTID() != null) {
			workOrder.setDepartmentCode(inforWorkOrder.getDEPARTMENTID().getDEPARTMENTCODE());
			workOrder.setDepartmentDesc(tools.getFieldDescriptionsTools().readDepartmentDesc(workOrder.getDepartmentCode()));
		}

		// EQUIPMENT
		if (inforWorkOrder.getEQUIPMENTID() != null) {
			workOrder.setEquipmentCode(inforWorkOrder.getEQUIPMENTID().getEQUIPMENTCODE());
			workOrder.setEquipmentDesc(inforWorkOrder.getEQUIPMENTID().getDESCRIPTION());
		}
		// PROJECT
		if (inforWorkOrder.getPROJECTID() != null) {
			workOrder.setProjectCode(inforWorkOrder.getPROJECTID().getPROJECTCODE());
			workOrder.setProjectDesc(inforWorkOrder.getPROJECTID().getDESCRIPTION());
		}

		// PRIORITY
		if (inforWorkOrder.getPRIORITY() != null) {
			workOrder.setPriorityCode(inforWorkOrder.getPRIORITY().getPRIORITYCODE());
			workOrder.setPriorityDesc(inforWorkOrder.getPRIORITY().getDESCRIPTION());
		}
		// CUSTOM FIELDS
		workOrder.setCustomFields(tools.getCustomFieldsTools().readInforCustomFields(inforWorkOrder.getUSERDEFINEDAREA()));

		// LOCATION
		if (inforWorkOrder.getLOCATIONID() != null) {
			workOrder.setLocationCode(inforWorkOrder.getLOCATIONID().getLOCATIONCODE());
			workOrder.setLocationDesc(inforWorkOrder.getLOCATIONID().getDESCRIPTION());
		}

		// PROBLEM
		if (inforWorkOrder.getPROBLEMCODEID() != null) {
			workOrder.setProblemCode(inforWorkOrder.getPROBLEMCODEID().getPROBLEMCODE());
			// PROBLEM CODE - INCLUDE DESCRIPTION
		}

		// REPORTED DATE
		if (inforWorkOrder.getREPORTED() != null) {
			workOrder.setReportedDate(tools.getDataTypeTools().decodeInforDate(inforWorkOrder.getREPORTED()));
		}

		// REQUESTED START
		if (inforWorkOrder.getREQUESTEDSTART() != null) {
			workOrder.setRequestedStartDate(tools.getDataTypeTools().decodeInforDate(inforWorkOrder.getREQUESTEDSTART()));
		}

		// START DATE
		if (inforWorkOrder.getSTARTDATE() != null) {
			workOrder.setStartDate(tools.getDataTypeTools().decodeInforDate(inforWorkOrder.getSTARTDATE()));
		}

		// REQUESTED END
		if (inforWorkOrder.getREQUESTEDEND() != null) {
			workOrder.setRequestedEndDate(tools.getDataTypeTools().decodeInforDate(inforWorkOrder.getREQUESTEDEND()));
		}

		// SCHEDULING START DATE
		if (inforWorkOrder.getTARGETDATE() != null) {
			workOrder.setScheduledStartDate(tools.getDataTypeTools().decodeInforDate(inforWorkOrder.getTARGETDATE()));
		}

		// SCHEDULING END DATE
		if (inforWorkOrder.getSCHEDEND() != null) {
			workOrder.setScheduledEndDate(tools.getDataTypeTools().decodeInforDate(inforWorkOrder.getSCHEDEND()));
		}

		// COMPLETED DATE
		if (inforWorkOrder.getCOMPLETEDDATE() != null) {
			workOrder.setCompletedDate(tools.getDataTypeTools().decodeInforDate(inforWorkOrder.getCOMPLETEDDATE()));
		}

		// DUE DATE
		if (inforWorkOrder.getDUEDATE() != null) {
			workOrder.setDueDate(tools.getDataTypeTools().decodeInforDate(inforWorkOrder.getDUEDATE()));
		}

		// PROBLEM CODE
		if (inforWorkOrder.getPROBLEMCODEID() != null) {
			workOrder.setProblemCode(inforWorkOrder.getPROBLEMCODEID().getPROBLEMCODE());
		}

		// FAILURE CODE
		if (inforWorkOrder.getFAILURECODEID() != null) {
			workOrder.setFailureCode(inforWorkOrder.getFAILURECODEID().getFAILURECODE());
		}

		// CAUSE CODE
		if (inforWorkOrder.getCAUSECODEID() != null) {
			workOrder.setCauseCode(inforWorkOrder.getCAUSECODEID().getCAUSECODE());
		}

		// COST CODE
		if (inforWorkOrder.getCOSTCODEID() != null) {
			workOrder.setCostCode(inforWorkOrder.getCOSTCODEID().getCOSTCODE());
			workOrder.setCostCodeDesc(inforWorkOrder.getCOSTCODEID().getDESCRIPTION());
		}

		// ACTION
		if (inforWorkOrder.getACTIONCODEID() != null) {
			workOrder.setActionCode(inforWorkOrder.getACTIONCODEID().getACTIONCODE());
		}

		// STANDARD WORK ORDER
		if (inforWorkOrder.getSTANDARDWO() != null) {
			workOrder.setStandardWO(inforWorkOrder.getSTANDARDWO().getSTDWOCODE());
		}

		// REPORTED BY
		if (inforWorkOrder.getREQUESTEDBY() != null) {
			workOrder.setReportedBy(inforWorkOrder.getREQUESTEDBY().getPERSONCODE());
			workOrder.setReportedByDesc(tools.getFieldDescriptionsTools().readPersonDesc(workOrder.getReportedBy()));
		}

		// ASSIGNED BY
		if (inforWorkOrder.getSCHEDGROUP() != null) {
			workOrder.setAssignedBy(inforWorkOrder.getSCHEDGROUP());
		}

		// ASSIGNED TO
		if (inforWorkOrder.getASSIGNEDTO() != null) {
			workOrder.setAssignedTo(inforWorkOrder.getASSIGNEDTO().getPERSONCODE());
			workOrder.setAssignedToDesc(tools.getFieldDescriptionsTools().readPersonDesc(workOrder.getAssignedTo()));
		}

		// PARENT WO
		if (inforWorkOrder.getPARENTWO() != null) {
			workOrder.setParentWO(inforWorkOrder.getPARENTWO().getJOBNUM());
			workOrder.setParentWODesc(inforWorkOrder.getPARENTWO().getDESCRIPTION());
		}

		// CREATED DATE
		if (inforWorkOrder.getCREATEDDATE() != null) {
			workOrder.setCreatedDate(tools.getDataTypeTools().decodeInforDate(inforWorkOrder.getCREATEDDATE()));
		}

		// TARGET VALUE
		if (inforWorkOrder.getTARGETVALUE() != null) {
			workOrder.setTargetValue(tools.getDataTypeTools().decodeAmount(inforWorkOrder.getTARGETVALUE()));
		}

		// DOWNTIME HOURS
		if (inforWorkOrder.getDOWNTIMEHOURS() != null) {
			workOrder.setDowntimeHours(tools.getDataTypeTools().decodeQuantity(inforWorkOrder.getDOWNTIMEHOURS()));
		}

		// UPDATE COUNT
		workOrder.setUpdateCount(inforWorkOrder.getRecordid().toString());

		// USER DEFINED FIELDS
		workOrder.setUserDefinedFields(tools.getUDFTools().readInforUserDefinedFields(inforWorkOrder.getUserDefinedFields()));

		return workOrder;
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


	public WorkOrder readStandardWorkOrder(InforContext context, WorkOrder workorderParam) throws InforException {
		// Just do it if the StandardWO parameter is setted
		if (workorderParam.getStandardWO() != null && !workorderParam.getStandardWO().trim().equals("")) {
			// Get standard WO
			MP7082_GetStandardWorkOrder_001 getStandardWorkOrder = new MP7082_GetStandardWorkOrder_001();
			getStandardWorkOrder.setSTANDARDWO(new STDWOID_Type());
			getStandardWorkOrder.getSTANDARDWO().setORGANIZATIONID(tools.getOrganization(context));
			getStandardWorkOrder.getSTANDARDWO().setSTDWOCODE(workorderParam.getStandardWO());
			MP7082_GetStandardWorkOrder_001_Result getSWOResult;
			if (context.getCredentials() != null) {
				getSWOResult = inforws.getStandardWorkOrderOp(getStandardWorkOrder, tools.getOrganizationCode(context),
						tools.createSecurityHeader(context), "TERMINATE",
						null, tools.createMessageConfig(), tools.getTenant(context));
			} else {
				getSWOResult = inforws.getStandardWorkOrderOp(getStandardWorkOrder, tools.getOrganizationCode(context),
						null, "", new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(),
						tools.getTenant(context));
			}
			StandardWorkOrder standardWO = getSWOResult.getResultData().getStandardWorkOrder();
			// assign properties
			workorderParam
					.setDescription(standardWO.getSTANDARDWO() != null ? standardWO.getSTANDARDWO().getDESCRIPTION()
							: workorderParam.getDescription());
			workorderParam.setPriorityCode(standardWO.getPRIORITY() != null ? standardWO.getPRIORITY().getPRIORITYCODE()
					: workorderParam.getPriorityCode());
			workorderParam.setPriorityDesc(standardWO.getPRIORITY() != null ? standardWO.getPRIORITY().getDESCRIPTION()
					: workorderParam.getPriorityDesc());
			workorderParam.setProblemCode(
					standardWO.getPROBLEMCODEID() != null ? standardWO.getPROBLEMCODEID().getPROBLEMCODE()
							: workorderParam.getProblemCode());
			workorderParam.setClassCode(
					standardWO.getWORKORDERCLASSID() != null ? standardWO.getWORKORDERCLASSID().getCLASSCODE()
							: workorderParam.getClassCode());
			workorderParam.setClassDesc(
					standardWO.getWORKORDERCLASSID() != null ? standardWO.getWORKORDERCLASSID().getDESCRIPTION()
							: workorderParam.getClassDesc());
			workorderParam
					.setTypeCode(standardWO.getWORKORDERTYPE() != null ? standardWO.getWORKORDERTYPE().getTYPECODE()
							: workorderParam.getTypeCode());
			workorderParam
					.setTypeDesc(standardWO.getWORKORDERTYPE() != null ? standardWO.getWORKORDERTYPE().getDESCRIPTION()
							: workorderParam.getTypeDesc());
			workorderParam.setUserDefinedFields(tools.getUDFTools().readInforUserDefinedFields(standardWO.getUserDefinedFields()));
		}
		// Return the same object
		return workorderParam;
	}

	public String createWorkOrder(InforContext context, WorkOrder workorderParam) throws InforException {
		net.datastream.schemas.mp_entities.workorder_001.WorkOrder inforWorkOrder = new net.datastream.schemas.mp_entities.workorder_001.WorkOrder();
		//
		//
		//
		if (workorderParam.getCustomFields() != null && workorderParam.getCustomFields().length > 0) {
			if (workorderParam.getClassCode() != null && !workorderParam.getClassCode().trim().equals("")) {
				inforWorkOrder.setUSERDEFINEDAREA(
						tools.getCustomFieldsTools().getInforCustomFields(context, "EVNT", workorderParam.getClassCode()));
			} else {
				inforWorkOrder.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(context, "EVNT", "*"));
			}
		}
		//
		inforWorkOrder.setUserDefinedFields(new UserDefinedFields());
		//
		//
		//
		this.initializeInforWOObject(inforWorkOrder, workorderParam, context);
		//
		//
		//
		if (workorderParam.getStandardWO() != null && !workorderParam.getStandardWO().trim().equals("")) {
			// Get standard WO
			MP7082_GetStandardWorkOrder_001 getStandardWorkOrder = new MP7082_GetStandardWorkOrder_001();
			getStandardWorkOrder.setSTANDARDWO(new STDWOID_Type());
			getStandardWorkOrder.getSTANDARDWO().setORGANIZATIONID(tools.getOrganization(context));
			getStandardWorkOrder.getSTANDARDWO().setSTDWOCODE(workorderParam.getStandardWO());
			MP7082_GetStandardWorkOrder_001_Result getSWOResult;
			if (context.getCredentials() != null) {
				getSWOResult = inforws.getStandardWorkOrderOp(getStandardWorkOrder, tools.getOrganizationCode(context),
						tools.createSecurityHeader(context), "TERMINATE",
						null, tools.createMessageConfig(), tools.getTenant(context));
			} else {
				getSWOResult = inforws.getStandardWorkOrderOp(getStandardWorkOrder, tools.getOrganizationCode(context),
						null, "", new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(),
						tools.getTenant(context));
			}
			StandardWorkOrder standardWO = getSWOResult.getResultData().getStandardWorkOrder();
			// assign properties
			inforWorkOrder.setPERMITREVIEWEDBY(standardWO.getPERMITREVIEWEDBY());
			if (workorderParam.getPriorityCode() == null) {
				inforWorkOrder.setPRIORITY(standardWO.getPRIORITY());
			}
			inforWorkOrder.setPROBLEMCODEID(standardWO.getPROBLEMCODEID());
			inforWorkOrder.setSAFETYREVIEWEDBY(standardWO.getSAFETYREVIEWEDBY());
			inforWorkOrder.setSTANDARDWO(standardWO.getSTANDARDWO());
			inforWorkOrder.setUSERDEFINEDAREA(standardWO.getUSERDEFINEDAREA());
			inforWorkOrder.setCLASSID(standardWO.getWORKORDERCLASSID());
			inforWorkOrder.setTYPE(standardWO.getWORKORDERTYPE());
			workorderParam.setUserDefinedFields(tools.getUDFTools().readInforUserDefinedFields(standardWO.getUserDefinedFields()));
		}

		//
		//
		//
		MP0023_AddWorkOrder_001 addWO = new MP0023_AddWorkOrder_001();
		MP0023_AddWorkOrder_001_Result result;

		addWO.setWorkOrder(inforWorkOrder);
		// FIXED - REQUIRED AND NEVER USED
		inforWorkOrder.setFIXED("V");
		//
		//
		//
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
		//
		MP0024_GetWorkOrder_001 req = new MP0024_GetWorkOrder_001();
		WOID_Type woid = new WOID_Type();
		woid.setJOBNUM(workorderParam.getNumber());
		woid.setORGANIZATIONID(tools.getOrganization(context));
		//
		req.setWORKORDERID(woid);
		MP0024_GetWorkOrder_001_Result getWOResult;
		if (context.getCredentials() != null) {
			getWOResult = inforws.getWorkOrderOp(req, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			getWOResult = inforws.getWorkOrderOp(req, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		net.datastream.schemas.mp_entities.workorder_001.WorkOrder inforWorkOrder = getWOResult.getResultData().getWorkOrder();

		// Check Custom fields. If they change, or now we have them
		if (workorderParam.getClassCode() != null && (inforWorkOrder.getCLASSID() == null
				|| !workorderParam.getClassCode().toUpperCase().equals(inforWorkOrder.getCLASSID().getCLASSCODE()))) {
			inforWorkOrder.setUSERDEFINEDAREA(
					tools.getCustomFieldsTools().getInforCustomFields(context, "EVNT", workorderParam.getClassCode().toUpperCase()));
		}
		//
		// SET ALL PROPERTIES
		//
		this.initializeInforWOObject(inforWorkOrder, workorderParam, context);
		//
		// CALL INFOR WEB SERVICE
		//
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

	private void initializeInforWOObject(net.datastream.schemas.mp_entities.workorder_001.WorkOrder inforWorkOrder,
										 WorkOrder workorderParam,
										 InforContext context) throws InforException {

		// == null means Work Order creation
		if (inforWorkOrder.getWORKORDERID() == null) {
			inforWorkOrder.setWORKORDERID(new WOID_Type());
			inforWorkOrder.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
			inforWorkOrder.getWORKORDERID().setJOBNUM("0");
		}

		// DESCRIPTION
		if (workorderParam.getDescription() != null) {
			inforWorkOrder.getWORKORDERID().setDESCRIPTION(workorderParam.getDescription());
		}

		// CLASS
		if (workorderParam.getClassCode() != null) {
			if (workorderParam.getClassCode().trim().equals("")) {
				inforWorkOrder.setCLASSID(null);
			} else {
				inforWorkOrder.setCLASSID(new CLASSID_Type());
				inforWorkOrder.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
				inforWorkOrder.getCLASSID().setCLASSCODE(workorderParam.getClassCode().toUpperCase().trim());
			}
		}
		// STATUS
		if (workorderParam.getStatusCode() != null) {
			inforWorkOrder.setSTATUS(new STATUS_Type());
			inforWorkOrder.getSTATUS().setSTATUSCODE(workorderParam.getStatusCode().trim());
		}

		// TYPE
		if (workorderParam.getTypeCode() != null) {
			inforWorkOrder.setTYPE(new TYPE_Type());
			inforWorkOrder.getTYPE().setTYPECODE(workorderParam.getTypeCode().toUpperCase().trim());
		}

		// DEPARTMENT
		if (workorderParam.getDepartmentCode() != null) {
			inforWorkOrder.setDEPARTMENTID(new DEPARTMENTID_Type());
			inforWorkOrder.getDEPARTMENTID().setORGANIZATIONID(tools.getOrganization(context));
			inforWorkOrder.getDEPARTMENTID().setDEPARTMENTCODE(workorderParam.getDepartmentCode().toUpperCase().trim());
		}

		// EQUIPMENT
		if (workorderParam.getEquipmentCode() != null) {
			if (workorderParam.getEquipmentCode().trim().equals("")) {
				inforWorkOrder.setEQUIPMENTID(null);
			} else {
				inforWorkOrder.setEQUIPMENTID(new EQUIPMENTID_Type());
				inforWorkOrder.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
				inforWorkOrder.getEQUIPMENTID()
						.setEQUIPMENTCODE(workorderParam.getEquipmentCode().toUpperCase().trim());
			}
		}

		// PROJECT
		if (workorderParam.getProjectCode() != null) {
			if (workorderParam.getProjectCode().trim().equals("")) {
				inforWorkOrder.setPROJECTID(null);
			} else {
				inforWorkOrder.setPROJECTID(new PROJECTID_Type());
				inforWorkOrder.getPROJECTID().setORGANIZATIONID(tools.getOrganization(context));
				inforWorkOrder.getPROJECTID().setPROJECTCODE(workorderParam.getProjectCode().trim());
			}
		}

		// PRIORITY
		if (workorderParam.getPriorityCode() != null) {
			inforWorkOrder.setPRIORITY(new PRIORITY());
			inforWorkOrder.getPRIORITY().setPRIORITYCODE(workorderParam.getPriorityCode().toUpperCase().trim());
		}

		// REPORTED DATE
		if (workorderParam.getReportedDate() != null) {
			if (workorderParam.getReportedDate().getTime() == 0) {
				inforWorkOrder.setREPORTED(null);
			} else {
				inforWorkOrder.setREPORTED(tools.getDataTypeTools().encodeInforDate(workorderParam.getReportedDate(), "Reported Date"));
			}
		}

		// REQUESTED START DATE
		if (workorderParam.getRequestedStartDate() != null) {
			if (workorderParam.getRequestedStartDate().getTime() == 0) {
				inforWorkOrder.setREQUESTEDSTART(null);
			} else {
				inforWorkOrder.setREQUESTEDSTART(
						tools.getDataTypeTools().encodeInforDate(workorderParam.getRequestedStartDate(), "Requested Start Date"));
			}
		}

		// REQUESTED END DATE
		if (workorderParam.getRequestedEndDate() != null) {
			if (workorderParam.getRequestedEndDate().getTime() == 0) {
				inforWorkOrder.setREQUESTEDEND(null);
			} else {
				inforWorkOrder.setREQUESTEDEND(
						tools.getDataTypeTools().encodeInforDate(workorderParam.getRequestedEndDate(), "Requested End Date"));
			}
		}

		// SCHEDULING START DATE
		if (workorderParam.getScheduledStartDate() != null) {
			if (workorderParam.getScheduledStartDate().getTime() == 0) {
				inforWorkOrder.setTARGETDATE(null);
			} else {
				inforWorkOrder.setTARGETDATE(
						tools.getDataTypeTools().encodeInforDate(workorderParam.getScheduledStartDate(), "Scheduling Start Date"));
			}
		}

		// SCHEDULING END DATE
		if (workorderParam.getScheduledEndDate() != null) {
			if (workorderParam.getScheduledEndDate().getTime() == 0) {
				inforWorkOrder.setSCHEDEND(null);
			} else {
				inforWorkOrder.setSCHEDEND(
						tools.getDataTypeTools().encodeInforDate(workorderParam.getScheduledEndDate(), "Scheduling End Date"));
			}
		}

		// START DATE
		if (workorderParam.getStartDate() != null) {
			if (workorderParam.getStartDate().getTime() == 0) {
				inforWorkOrder.setSTARTDATE(null);
			} else {
				inforWorkOrder.setSTARTDATE(tools.getDataTypeTools().encodeInforDate(workorderParam.getStartDate(), "Start Date"));
			}
		}

		// COMPLETED DATE
		if (workorderParam.getCompletedDate() != null) {
			inforWorkOrder.setCOMPLETEDDATE(tools.getDataTypeTools().encodeInforDate(workorderParam.getCompletedDate(), "Completed Date"));
		}

		// DUE DATE
		if (workorderParam.getDueDate() != null) {
			inforWorkOrder.setDUEDATE(tools.getDataTypeTools().encodeInforDate(workorderParam.getDueDate(), "Due Date"));
		}

		// CUSTOM FIELDS
		tools.getCustomFieldsTools().updateInforCustomFields(inforWorkOrder.getUSERDEFINEDAREA(), workorderParam.getCustomFields());

		// UDFS
		tools.getUDFTools().updateInforUserDefinedFields(inforWorkOrder.getUserDefinedFields(),
				workorderParam.getUserDefinedFields());
		// PROBLEM CODE
		if (workorderParam.getProblemCode() != null) {
			inforWorkOrder.setPROBLEMCODEID(new PROBLEMCODEID());
			inforWorkOrder.getPROBLEMCODEID().setPROBLEMCODE(workorderParam.getProblemCode().toUpperCase());
		}

		// FAILURE CODE
		if (workorderParam.getFailureCode() != null) {
			if (workorderParam.getFailureCode().trim().equals("")) {
				inforWorkOrder.setFAILURECODEID(null);
			} else {
				inforWorkOrder.setFAILURECODEID(new FAILURECODEID());
				inforWorkOrder.getFAILURECODEID().setFAILURECODE(workorderParam.getFailureCode().toUpperCase().trim());
			}
		}

		// CAUSE CODE
		if (workorderParam.getCauseCode() != null) {
			inforWorkOrder.setCAUSECODEID(new CAUSECODEID());
			inforWorkOrder.getCAUSECODEID().setCAUSECODE(workorderParam.getCauseCode().toUpperCase().trim());
		}

		// ACTION CODE
		if (workorderParam.getActionCode() != null) {
			inforWorkOrder.setACTIONCODEID(new ACTIONCODEID());
			inforWorkOrder.getACTIONCODEID().setACTIONCODE(workorderParam.getActionCode().toUpperCase().trim());
		}

		// COST CODE
		if (workorderParam.getCostCode() != null) {
			inforWorkOrder.setCOSTCODEID(new COSTCODEID_Type());
			inforWorkOrder.getCOSTCODEID().setCOSTCODE(workorderParam.getCostCode().toUpperCase().trim());
		}

		// LOCATION
		if (workorderParam.getLocationCode() != null) {
			inforWorkOrder.setLOCATIONID(new LOCATIONID_Type());
			inforWorkOrder.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
			inforWorkOrder.getLOCATIONID().setLOCATIONCODE(workorderParam.getLocationCode().toUpperCase().trim());
		}

		// ASSIGNED BY (SCHEDGROUP)
		if (workorderParam.getAssignedBy() != null) {
			inforWorkOrder.setSCHEDGROUP(workorderParam.getAssignedBy());
		}

		// REPORTED BY (REQUESTED BY)
		if (workorderParam.getReportedBy() != null) {
			inforWorkOrder.setREQUESTEDBY(new PERSONID_Type());
			inforWorkOrder.getREQUESTEDBY().setPERSONCODE(workorderParam.getReportedBy());
		}

		// ASSIGNED TO
		if (workorderParam.getAssignedTo() != null) {
			inforWorkOrder.setASSIGNEDTO(new PERSONID_Type());
			inforWorkOrder.getASSIGNEDTO().setPERSONCODE(workorderParam.getAssignedTo());
		}

		// STANDARD WO
		if (workorderParam.getStandardWO() != null) {
			inforWorkOrder.setSTANDARDWO(new STDWOID_Type());
			inforWorkOrder.getSTANDARDWO().setORGANIZATIONID(tools.getOrganization(context));
			inforWorkOrder.getSTANDARDWO().setSTDWOCODE(workorderParam.getStandardWO().toUpperCase().trim());
		}

		// ROUTE
		if (workorderParam.getRoute() != null) {
			if (workorderParam.getRoute().equals("")) {
				inforWorkOrder.setROUTE(null);
			} else {
				inforWorkOrder.setROUTE(new ROUTE_Type());
				inforWorkOrder.getROUTE().setORGANIZATIONID(tools.getOrganization(context));
				inforWorkOrder.getROUTE().setROUTECODE(workorderParam.getRoute());
				inforWorkOrder.getROUTE().setROUTEREVISION((long) 0);
			}
		}

		// CREATED DATE
		if (workorderParam.getCreatedDate() != null) {
			inforWorkOrder.setCREATEDDATE(tools.getDataTypeTools().encodeInforDate(workorderParam.getCreatedDate(), "Created Date"));
		}

		// PARENT WO
		if (workorderParam.getParentWO() != null) {
			inforWorkOrder.setPARENTWO(new WOID_Type());
			inforWorkOrder.getPARENTWO().setORGANIZATIONID(tools.getOrganization(context));
			inforWorkOrder.getPARENTWO().setJOBNUM(workorderParam.getParentWO().trim());
		}

		// PROJECT BUDGET
		if (workorderParam.getProjectBudget() != null) {
			inforWorkOrder.setPROJBUD(workorderParam.getProjectBudget());
		}

		// TARGET VALUE
		if (workorderParam.getTargetValue() != null) {
			inforWorkOrder.setTARGETVALUE(tools.getDataTypeTools().encodeAmount(workorderParam.getTargetValue(), "Target Value"));
		}

		// DOWNTIME HOURS
		if (workorderParam.getDowntimeHours() != null) {
			inforWorkOrder.setDOWNTIMEHOURS(tools.getDataTypeTools().encodeQuantity(workorderParam.getDowntimeHours(), "Downtime Hours"));
		}
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
