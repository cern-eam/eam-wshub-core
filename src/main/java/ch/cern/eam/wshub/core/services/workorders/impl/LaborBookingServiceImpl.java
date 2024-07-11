package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.ChecklistService;
import ch.cern.eam.wshub.core.services.workorders.LaborBookingService;
import ch.cern.eam.wshub.core.services.workorders.TaskPlanService;
import ch.cern.eam.wshub.core.services.workorders.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.GridTools;
import ch.cern.eam.wshub.core.tools.EAMException;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;

public class LaborBookingServiceImpl implements LaborBookingService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;
	private ChecklistService checklistService;
	private GridsService gridsService;
	private TaskPlanService taskPlanService;

	public LaborBookingServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
		this.checklistService = new ChecklistServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
		this.gridsService = new GridsServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
		this.taskPlanService = new TaskPlanServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
	}

	public List<LaborBooking> readLaborBookings(EAMContext context, String workOrderNumber) throws EAMException {
		GridRequest gridRequest = new GridRequest("WSJOBS_BOO");
		gridRequest.setUserFunctionName("WSJOBS");
		gridRequest.getParams().put("param.jobnum", workOrderNumber);
		gridRequest.getParams().put("param.headeractivity", "0");
		gridRequest.getParams().put("param.headerjob", "0");
		gridRequest.setRowCount(500);

		return GridTools.convertGridResultToObject(LaborBooking.class, null, gridsService.executeQuery(context, gridRequest));
	}

	public String createLaborBooking(EAMContext context, LaborBooking laborBookingParam) throws EAMException {
		net.datastream.schemas.mp_entities.laborbooking_001.LaborBooking laborBookingEAM = new net.datastream.schemas.mp_entities.laborbooking_001.LaborBooking();

		//
		if (laborBookingParam.getEmployeeCode() != null) {
			laborBookingEAM.setEMPLOYEE(new PERSONID_Type());
			laborBookingEAM.getEMPLOYEE().setPERSONCODE(laborBookingParam.getEmployeeCode());
		}

		//
		if (laborBookingParam.getDepartmentCode() != null) {
			laborBookingEAM.setDEPARTMENTID(new DEPARTMENTID_Type());
			laborBookingEAM.getDEPARTMENTID().setORGANIZATIONID(tools.getOrganization(context));
			laborBookingEAM.getDEPARTMENTID().setDEPARTMENTCODE(laborBookingParam.getDepartmentCode());
		}

		//
		if (laborBookingParam.getDateWorked() != null) {
			laborBookingEAM.setDATEWORKED(tools.getDataTypeTools().encodeEAMDate(laborBookingParam.getDateWorked(), "Date Worked"));
		}

		//
		if (laborBookingParam.getStartTime() != null) {
			laborBookingEAM.setSTARTTIME(tools.getDataTypeTools().encodeAmount(laborBookingParam.getStartTime(), "Start Time"));
		}

		//
		if (laborBookingParam.getEndTime() != null) {
			laborBookingEAM.setENDTIME(tools.getDataTypeTools().encodeAmount(laborBookingParam.getEndTime(), "End Time"));
		}

		//
		if (laborBookingParam.getHoursWorked() != null) {
			laborBookingEAM.setHOURSWORKED(tools.getDataTypeTools().encodeAmount(laborBookingParam.getHoursWorked(),"Hours Worked"));
		}

		//
		laborBookingEAM.setTRADERATE(tools.getDataTypeTools().encodeAmount(BigDecimal.ZERO,"Trade Rate"));

		//
		if (laborBookingParam.getTypeOfHours() != null) {
			laborBookingEAM.setOCCUPATIONTYPE(new OCCUPATIONTYPEID_Type());
			laborBookingEAM.getOCCUPATIONTYPE().setOCCUPATIONTYPECODE(laborBookingParam.getTypeOfHours());
		}

		//
		if (laborBookingParam.getTradeCode() != null) {
			laborBookingEAM.setTRADEID(new TRADEID_Type());
			laborBookingEAM.getTRADEID().setORGANIZATIONID(tools.getOrganization(context));
			laborBookingEAM.getTRADEID().setTRADECODE(laborBookingParam.getTradeCode());
		}

		//
		laborBookingEAM.setACTIVITYID(new ACTIVITYID());
		if (laborBookingParam.getActivityCode() != null) {
			laborBookingEAM.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
			laborBookingEAM.getACTIVITYID().getACTIVITYCODE().setValue(Long.parseLong(laborBookingParam.getActivityCode()));
		}
		if (laborBookingParam.getWorkOrderNumber() != null) {
			laborBookingEAM.getACTIVITYID().setWORKORDERID(new WOID_Type());
			laborBookingEAM.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
			laborBookingEAM.getACTIVITYID().getWORKORDERID().setJOBNUM(laborBookingParam.getWorkOrderNumber());
		}
		//
		// CALL THE WS
		//
		MP0042_AddLaborBooking_001 addLaborBoking = new MP0042_AddLaborBooking_001();
		addLaborBoking.setLaborBooking(laborBookingEAM);

		MP0042_AddLaborBooking_001_Result result =
			tools.performEAMOperation(context, eamws::addLaborBookingOp, addLaborBoking);
		return laborBookingParam.getActivityCode();
	}


	public Activity[] readActivities(EAMContext context, String workOrderNumber, Boolean includeChecklists) throws EAMException {
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
									activity.setChecklists(checklistService.readWorkOrderChecklists(context, activity));
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
									activity.setChecklists(new WorkOrderActivityCheckList[0]);
								}
							} catch (Exception e) {
								activity.setChecklists(new WorkOrderActivityCheckList[0]);
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

	public String createActivity (EAMContext context, Activity activityParam) throws EAMException {
		net.datastream.schemas.mp_entities.activity_001.Activity activityEAM = new net.datastream.schemas.mp_entities.activity_001.Activity();

		tools.getEAMFieldTools().transformWSHubObject(activityEAM, activityParam, context);

		if (isNotEmpty(activityParam.getTaskCode())) {
			activityEAM.getTASKSID().setTASKREVISION(0L);
		}

		// CALL THE WS
		//
		MP0037_AddActivity_001 addActivity = new MP0037_AddActivity_001();
		addActivity.setActivity(activityEAM);

		MP0037_AddActivity_001_Result result =
			tools.performEAMOperation(context, eamws::addActivityOp, addActivity);
		return result.getResultData().getACTIVITYID().getACTIVITYCODE().getValue() + "";
	}

	public String updateActivity(EAMContext context, Activity activityParam) throws EAMException {
		return updateActivity(context, activityParam, null);
	}

	public String updateActivity(EAMContext context, Activity activityParam, String confirmDeleteChecklist) throws EAMException {
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
				tools.performEAMOperation(context, eamws::getActivityOp, getActivity);

		net.datastream.schemas.mp_entities.activity_001.Activity activityEAM = getresult.getResultData().getActivity();

		tools.getEAMFieldTools().transformWSHubObject(activityEAM, activityParam, context);

		if (isNotEmpty(activityParam.getTaskCode())) {
			activityEAM.getTASKSID().setTASKREVISION(0L);
		}

		//
		// CALL THE WS
		//
		MP0038_SyncActivity_001 syncActivity = new MP0038_SyncActivity_001();
		syncActivity.setActivity(activityEAM);
		syncActivity.setConfirmadddeletechecklist(confirmDeleteChecklist);

		MP0038_SyncActivity_001_Result syncresult =
				tools.performEAMOperation(context, eamws::syncActivityOp, syncActivity);
		return syncresult.getResultData().getACTIVITYID().getACTIVITYCODE().getValue() + "";
	}

	public String deleteActivity(EAMContext context, Activity activityParam) throws EAMException {
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
			tools.performEAMOperation(context, eamws::deleteActivityOp, deleteActivity);
		return result.getResultData().getACTIVITYID().getACTIVITYCODE().getValue() + "";
	}


}
