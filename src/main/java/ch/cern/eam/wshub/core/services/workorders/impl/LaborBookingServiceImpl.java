package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.ChecklistService;
import ch.cern.eam.wshub.core.services.workorders.LaborBookingService;
import ch.cern.eam.wshub.core.services.workorders.TaskPlanService;
import ch.cern.eam.wshub.core.services.workorders.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.GridTools;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0035_001.MP0035_GetActivity_001;
import net.datastream.schemas.mp_functions.mp0037_001.MP0037_AddActivity_001;
import net.datastream.schemas.mp_functions.mp0038_001.MP0038_SyncActivity_001;
import net.datastream.schemas.mp_functions.mp0039_001.MP0039_DeleteActivity_001;
import net.datastream.schemas.mp_functions.mp0042_001.MP0042_AddLaborBooking_001;
import net.datastream.schemas.mp_results.mp0035_001.MP0035_GetActivity_001_Result;
import net.datastream.schemas.mp_results.mp0037_001.MP0037_AddActivity_001_Result;
import net.datastream.schemas.mp_results.mp0038_001.MP0038_SyncActivity_001_Result;
import net.datastream.schemas.mp_results.mp0039_001.MP0039_DeleteActivity_001_Result;
import net.datastream.schemas.mp_results.mp0042_001.MP0042_AddLaborBooking_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;

public class LaborBookingServiceImpl implements LaborBookingService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;
	private ChecklistService checklistService;
	private GridsService gridsService;
	private TaskPlanService taskPlanService;

	public LaborBookingServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.checklistService = new ChecklistServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
		this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
		this.taskPlanService = new TaskPlanServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
	}

	public List<LaborBooking> readLaborBookings(InforContext context, String workOrderNumber) throws InforException {
		GridRequest gridRequest = new GridRequest("WSJOBS_BOO");
		gridRequest.setUserFunctionName("WSJOBS");
		gridRequest.getParams().put("param.jobnum", workOrderNumber);
		gridRequest.getParams().put("param.headeractivity", "0");
		gridRequest.getParams().put("param.headerjob", "0");
		gridRequest.setRowCount(500);

		return GridTools.convertGridResultToObject(LaborBooking.class, null, gridsService.executeQuery(context, gridRequest));
	}

	public String createLaborBooking(InforContext context, LaborBooking laborBookingParam) throws InforException {
		net.datastream.schemas.mp_entities.laborbooking_001.LaborBooking laborBookingInfor = new net.datastream.schemas.mp_entities.laborbooking_001.LaborBooking();

		//
		if (laborBookingParam.getEmployeeCode() != null) {
			laborBookingInfor.setEMPLOYEE(new PERSONID_Type());
			laborBookingInfor.getEMPLOYEE().setPERSONCODE(laborBookingParam.getEmployeeCode());
		}

		//
		if (laborBookingParam.getDepartmentCode() != null) {
			laborBookingInfor.setDEPARTMENTID(new DEPARTMENTID_Type());
			laborBookingInfor.getDEPARTMENTID().setORGANIZATIONID(tools.getOrganization(context));
			laborBookingInfor.getDEPARTMENTID().setDEPARTMENTCODE(laborBookingParam.getDepartmentCode());
		}

		//
		if (laborBookingParam.getDateWorked() != null) {
			laborBookingInfor.setDATEWORKED(tools.getDataTypeTools().encodeInforDate(laborBookingParam.getDateWorked(), "Date Worked"));
		}

		//
		if (laborBookingParam.getStartTime() != null) {
			laborBookingInfor.setSTARTTIME(tools.getDataTypeTools().encodeAmount(laborBookingParam.getStartTime(), "Start Time"));
		}

		//
		if (laborBookingParam.getEndTime() != null) {
			laborBookingInfor.setENDTIME(tools.getDataTypeTools().encodeAmount(laborBookingParam.getEndTime(), "End Time"));
		}

		//
		if (laborBookingParam.getHoursWorked() != null) {
			laborBookingInfor.setHOURSWORKED(tools.getDataTypeTools().encodeAmount(laborBookingParam.getHoursWorked(),"Hours Worked"));
		}

		//
		laborBookingInfor.setTRADERATE(tools.getDataTypeTools().encodeAmount(BigDecimal.ZERO,"Trade Rate"));

		//
		if (laborBookingParam.getTypeOfHours() != null) {
			laborBookingInfor.setOCCUPATIONTYPE(new OCCUPATIONTYPEID_Type());
			laborBookingInfor.getOCCUPATIONTYPE().setOCCUPATIONTYPECODE(laborBookingParam.getTypeOfHours());
		}

		//
		if (laborBookingParam.getTradeCode() != null) {
			laborBookingInfor.setTRADEID(new TRADEID_Type());
			laborBookingInfor.getTRADEID().setORGANIZATIONID(tools.getOrganization(context));
			laborBookingInfor.getTRADEID().setTRADECODE(laborBookingParam.getTradeCode());
		}

		//
		laborBookingInfor.setACTIVITYID(new ACTIVITYID());
		if (laborBookingParam.getActivityCode() != null) {
			laborBookingInfor.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
			laborBookingInfor.getACTIVITYID().getACTIVITYCODE().setValue(Long.parseLong(laborBookingParam.getActivityCode()));
		}
		if (laborBookingParam.getWorkOrderNumber() != null) {
			laborBookingInfor.getACTIVITYID().setWORKORDERID(new WOID_Type());
			laborBookingInfor.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
			laborBookingInfor.getACTIVITYID().getWORKORDERID().setJOBNUM(laborBookingParam.getWorkOrderNumber());
		}
		//
		// CALL THE WS
		//
		MP0042_AddLaborBooking_001 addLaborBoking = new MP0042_AddLaborBooking_001();
		addLaborBoking.setLaborBooking(laborBookingInfor);

		MP0042_AddLaborBooking_001_Result result =
			tools.performInforOperation(context, inforws::addLaborBookingOp, addLaborBoking);
		return laborBookingParam.getActivityCode();
	}


	public Activity[] readActivities(InforContext context, String workOrderNumber, Boolean includeChecklists) throws InforException {
		try {
			GridRequest gridRequest = new GridRequest("WSJOBS_ACT");
			gridRequest.setRowCount(1000);
			gridRequest.setUserFunctionName("WSJOBS");
			gridRequest.getParams().put("param.jobnum", workOrderNumber);

			List<Activity> activities = tools.getGridTools().convertGridResultToObject(Activity.class, null, gridsService.executeQuery(context, gridRequest));

			if (includeChecklists) {
				// Read checklists for all activities in parallel
				List<Runnable> runnables = activities.stream()
						.<Runnable>map(activity -> () -> {
							try {
								TaskPlan taskPlan = new TaskPlan();
								taskPlan.setCode(activity.getTaskCode());
								taskPlan = taskPlanService.getTaskPlan(context, taskPlan);
								WorkOrderActivityChecklistSignatureResult[] signatures = checklistService.getSignatures(context, workOrderNumber, activity.getActivityCode().toString(), taskPlan);
								if (signatures.length > 0) {
									activity.setChecklists(checklistService.readWorkOrderChecklistItems(context, activity));
									if (taskPlan.getReviewedByRequired()) {
										activity.setSignatures(Arrays.stream(signatures)
												.collect(Collectors.toMap(WorkOrderActivityChecklistSignatureResult::getType, Function.identity())));
									} else if (taskPlan.getPerformedByRequired()) {
										activity.setSignatures(
												Arrays.stream(signatures).filter(signature -> !signature.getType().equals("RB01"))
														.collect(Collectors.toMap(WorkOrderActivityChecklistSignatureResult::getType, Function.identity())));
									}
									activity.setForceActivityExpansion(taskPlan.getUserDefinedFields().getUdfchkbox03());
								}
								else {
									activity.setChecklists(new WorkOrderActivityChecklistItem[0]);
								}
							} catch (Exception e) {
								activity.setChecklists(new WorkOrderActivityChecklistItem[0]);
							}
						})
						.collect(Collectors.toList());
				tools.processRunnables(runnables);
			}
			//
			return activities.stream().toArray(Activity[]::new);
		} catch (Exception e) {
			throw tools.generateFault("Couldn't fetch activities for this work order: " + e.getMessage());
		}
	}

	public String createActivity (InforContext context, Activity activityParam) throws InforException {
		net.datastream.schemas.mp_entities.activity_001.Activity activityInfor = new net.datastream.schemas.mp_entities.activity_001.Activity();

		tools.getInforFieldTools().transformWSHubObject(activityInfor, activityParam, context);

		if (isNotEmpty(activityParam.getTaskCode())) {
			activityInfor.getTASKSID().setTASKREVISION(0L);
		}

		// CALL THE WS
		//
		MP0037_AddActivity_001 addActivity = new MP0037_AddActivity_001();
		addActivity.setActivity(activityInfor);

		MP0037_AddActivity_001_Result result =
			tools.performInforOperation(context, inforws::addActivityOp, addActivity);
		return result.getResultData().getACTIVITYID().getACTIVITYCODE().getValue() + "";
	}

	public String updateActivity(InforContext context, Activity activityParam) throws InforException {
		return updateActivity(context, activityParam, null);
	}

	public String updateActivity(InforContext context, Activity activityParam, String confirmDeleteChecklist) throws InforException {
		//
		// READ THE ACTIVITY FIRST
		//
		MP0035_GetActivity_001 getActivity = new MP0035_GetActivity_001();
		getActivity.setACTIVITYID(new ACTIVITYID());
		try {
			getActivity.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
			getActivity.getACTIVITYID().getACTIVITYCODE().setValue(Long.parseLong(activityParam.getActivityCode().toString()));
		} catch (Exception e) {
		}

		getActivity.getACTIVITYID().setWORKORDERID(new WOID_Type());
		getActivity.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		getActivity.getACTIVITYID().getWORKORDERID().setJOBNUM(activityParam.getWorkOrderNumber());

		MP0035_GetActivity_001_Result getresult =
				tools.performInforOperation(context, inforws::getActivityOp, getActivity);

		net.datastream.schemas.mp_entities.activity_001.Activity activityInfor = getresult.getResultData().getActivity();

		tools.getInforFieldTools().transformWSHubObject(activityInfor, activityParam, context);

		if (isNotEmpty(activityParam.getTaskCode())) {
			activityInfor.getTASKSID().setTASKREVISION(0L);
		}

		//
		// CALL THE WS
		//
		MP0038_SyncActivity_001 syncActivity = new MP0038_SyncActivity_001();
		syncActivity.setActivity(activityInfor);
		syncActivity.setConfirmadddeletechecklist(confirmDeleteChecklist);

		MP0038_SyncActivity_001_Result syncresult =
				tools.performInforOperation(context, inforws::syncActivityOp, syncActivity);
		return syncresult.getResultData().getACTIVITYID().getACTIVITYCODE().getValue() + "";
	}

	public String deleteActivity(InforContext context, Activity activityParam) throws InforException {
		//
		// CALL THE WS
		//
		MP0039_DeleteActivity_001 deleteActivity = new MP0039_DeleteActivity_001();
		deleteActivity.setACTIVITYID(new ACTIVITYID());
		deleteActivity.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
		deleteActivity.getACTIVITYID().getACTIVITYCODE().setValue(activityParam.getActivityCode().longValue());
		deleteActivity.getACTIVITYID().setWORKORDERID(new WOID_Type());
		deleteActivity.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		deleteActivity.getACTIVITYID().getWORKORDERID().setJOBNUM(activityParam.getWorkOrderNumber());
		deleteActivity.setConfirmdeletechecklist("true");

		MP0039_DeleteActivity_001_Result result =
			tools.performInforOperation(context, inforws::deleteActivityOp, deleteActivity);
		return result.getResultData().getACTIVITYID().getACTIVITYCODE().getValue() + "";
	}


}
