package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.ChecklistService;
import ch.cern.eam.wshub.core.services.workorders.LaborBookingService;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderActivityCheckList;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.Activity;
import ch.cern.eam.wshub.core.services.workorders.entities.LaborBooking;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
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

import javax.persistence.EntityManager;
import javax.xml.ws.Holder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class LaborBookingServiceImpl implements LaborBookingService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;
	private ChecklistService checklistService;
	private GridsService gridsService;

	public LaborBookingServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.checklistService = new ChecklistServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
		this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
	}

	public List<LaborBooking> readLaborBookings(InforContext context, String workOrderNumber) throws InforException {
		GridRequest gridRequest = new GridRequest("WSJOBS_BOO");
		gridRequest.setUserFunctionName("WSJOBS");
		gridRequest.getParams().put("param.jobnum", workOrderNumber);
		gridRequest.getParams().put("param.headeractivity", "0");
		gridRequest.getParams().put("param.headerjob", "0");

		Map map = new HashMap();
		map.put("octype", "typeOfHours");
		map.put("hours", "hoursWorked");
		map.put("boodate", "dateWorked");
		map.put("department", "departmentCode");
		map.put("employee", "employeeCode");
		map.put("employeedesc", "employeeDesc");
		map.put("booactivity", "activityCode");
		map.put("emptrade", "tradeCode");

		return tools.getGridTools().converGridResultToObject(LaborBooking.class, map, gridsService.executeQuery(context, gridRequest));
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
		if (laborBookingParam.getHoursWorked() != null) {
			laborBookingInfor.setHOURSWORKED(tools.getDataTypeTools().encodeAmount(laborBookingParam.getHoursWorked(),"Hours Worked"));
		}

		//
		laborBookingInfor.setTRADERATE(tools.getDataTypeTools().encodeAmount("0","Trade Rate"));

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

		MP0042_AddLaborBooking_001_Result result = null;

		if (context.getCredentials() != null) {
			result = inforws.addLaborBookingOp(addLaborBoking, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.addLaborBookingOp(addLaborBoking, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return laborBookingParam.getActivityCode();
	}


	public Activity[] readActivities(InforContext context, String workOrderNumber, Boolean includeChecklists) throws InforException {
		try {
			GridRequest gridRequest = new GridRequest("WSJOBS_ACT");
			gridRequest.setRowCount(1000);
			gridRequest.setUserFunctionName("WSJOBS");
			gridRequest.getParams().put("param.jobnum", workOrderNumber);

			Map map = new HashMap<>();
			map.put("activity", "activityCode");
			map.put("activitynote", "activityNote");
			map.put("workordernum", "workOrderNumber");
			map.put("personsreq", "peopleRequired");
			map.put("esthrs", "estimatedHours");
			map.put("hrsremain", "hoursRemaining");
			map.put("actstartdate", "startDate");
			map.put("actenddate", "endDate");
			map.put("matlcode", "materialList");
			map.put("task", "taskCode");
			map.put("trade", "tradeCode");
			map.put("taskqty", "taskQty");

			List<Activity> activities = tools.getGridTools().converGridResultToObject(Activity.class, map, gridsService.executeQuery(context, gridRequest));

			if (includeChecklists) {
				// Read checklists for all activities in parallel
				List<Runnable> runnables = activities.stream()
						.<Runnable>map(activity -> () -> {
							try {
								activity.setChecklists(checklistService.readWorkOrderChecklists(context, activity));
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

	public String createActivity(InforContext context, Activity activityParam) throws InforException {
		net.datastream.schemas.mp_entities.activity_001.Activity activityInfor = new net.datastream.schemas.mp_entities.activity_001.Activity();
		//
		activityInfor.setACTIVITYID(new ACTIVITYID());

		if (activityParam.getWorkOrderNumber() != null) {
			activityInfor.getACTIVITYID().setWORKORDERID(new WOID_Type());
			activityInfor.getACTIVITYID().getWORKORDERID().setJOBNUM(activityParam.getWorkOrderNumber());
			activityInfor.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		}

		if (activityParam.getActivityCode() != null) {
			activityInfor.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
			activityInfor.getACTIVITYID().getACTIVITYCODE()
					.setValue(tools.getDataTypeTools().encodeLong(activityParam.getActivityCode(), "Activity ID"));
		}
		//
		if (activityParam.getTradeCode() != null) {
			activityInfor.setTRADEID(new TRADEID_Type());
			activityInfor.getTRADEID().setORGANIZATIONID(tools.getOrganization(context));
			activityInfor.getTRADEID().setTRADECODE(activityParam.getTradeCode());
		}
		//
		if (activityParam.getPeopleRequired() != null) {
			activityInfor.setPERSONS(tools.getDataTypeTools().encodeLong(activityParam.getPeopleRequired(), "People Required"));
		}
		//
		if (activityParam.getEstimatedHours() != null) {
			activityInfor.setESTIMATEDHOURS(Double.parseDouble(activityParam.getEstimatedHours()));
		}
		//
		if (activityParam.getStartDate() != null) {
			activityInfor
					.setACTIVITYSTARTDATE(tools.getDataTypeTools().encodeInforDate(activityParam.getStartDate(), "Activity Start Date"));
		}
		//
		if (activityParam.getEndDate() != null) {
			activityInfor.setACTIVITYENDDATE(tools.getDataTypeTools().encodeInforDate(activityParam.getEndDate(), "Activity End Date"));
		}

		//
		if (activityParam.getHoursRemaining() != null) {
			activityInfor.setHOURSREMAINING(Double.parseDouble(activityParam.getHoursRemaining()));
		}
		//
		if (activityParam.getTaskCode() != null && !activityParam.getTaskCode().trim().equals("")) {
			activityInfor.setTASKSID(new TASKS_Type());
			activityInfor.getTASKSID().setORGANIZATIONID(tools.getOrganization(context));
			activityInfor.getTASKSID().setTASKCODE(activityParam.getTaskCode());
			activityInfor.getTASKSID().setTASKREVISION((long) 0);
			activityInfor.getTASKSID().setTASKQUANTITY(tools.getDataTypeTools().encodeQuantity(activityParam.getTaskQty(), "Task Quantity"));
		}

		//
		if (activityParam.getMaterialList() != null && !activityParam.getMaterialList().trim().equals("")) {
			activityInfor.setMATLIST(new MATLIST_Type());
			activityInfor.getMATLIST().setMTLCODE(activityParam.getMaterialList());
		}



		// NOTE
		if (tools.getDataTypeTools().isNotEmpty(activityParam.getActivityNote())) {
			activityInfor.getACTIVITYID().setACTIVITYNOTE(activityParam.getActivityNote());
		}


		// CALL THE WS
		//
		MP0037_AddActivity_001 addActivity = new MP0037_AddActivity_001();
		addActivity.setActivity(activityInfor);

		MP0037_AddActivity_001_Result result;

		if (context.getCredentials() != null) {
			result = inforws.addActivityOp(addActivity, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.addActivityOp(addActivity, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return result.getResultData().getACTIVITYID().getACTIVITYCODE().getValue() + "";
	}

	public String updateActivity(InforContext context, Activity activityParam) throws InforException {
		//
		// READ THE ACTIVITY FIRST
		//
		MP0035_GetActivity_001 getActivity = new MP0035_GetActivity_001();
		getActivity.setACTIVITYID(new ACTIVITYID());
		try {
			getActivity.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
			getActivity.getACTIVITYID().getACTIVITYCODE().setValue(Long.parseLong(activityParam.getActivityCode()));
		} catch (Exception e) {

		}

		getActivity.getACTIVITYID().setWORKORDERID(new WOID_Type());
		getActivity.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		getActivity.getACTIVITYID().getWORKORDERID().setJOBNUM(activityParam.getWorkOrderNumber());

		MP0035_GetActivity_001_Result getresult = null;

		if (context.getCredentials() != null) {
			getresult = inforws.getActivityOp(getActivity, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			getresult = inforws.getActivityOp(getActivity, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		net.datastream.schemas.mp_entities.activity_001.Activity activityInfor = getresult.getResultData().getActivity();

		//
		if (activityParam.getTradeCode() != null) {
			if (activityInfor.getTRADEID() == null) {
				activityInfor.setTRADEID(new TRADEID_Type());
				activityInfor.getTRADEID().setORGANIZATIONID(tools.getOrganization(context));
			}

			activityInfor.getTRADEID().setTRADECODE(activityParam.getTradeCode());
		}

		//
		if (activityParam.getPeopleRequired() != null) {
			activityInfor.setPERSONS(tools.getDataTypeTools().encodeLong(activityParam.getPeopleRequired(), "People Required"));
		}
		//
		if (activityParam.getEstimatedHours() != null) {
			activityInfor.setESTIMATEDHOURS(Double.parseDouble(activityParam.getEstimatedHours()));
		}
		//
		if (activityParam.getStartDate() != null) {
			activityInfor
					.setACTIVITYSTARTDATE(tools.getDataTypeTools().encodeInforDate(activityParam.getStartDate(), "Activity Start Date"));
		}
		//
		if (activityParam.getEndDate() != null) {
			activityInfor.setACTIVITYENDDATE(tools.getDataTypeTools().encodeInforDate(activityParam.getEndDate(), "Activity End Date"));
		}

		//
		if (activityParam.getHoursRemaining() != null) {
			activityInfor.setHOURSREMAINING(Double.parseDouble(activityParam.getHoursRemaining()));
		}

		//
		if (activityParam.getTaskCode() != null) {
			if (activityInfor.getTASKSID() == null) {
				activityInfor.setTASKSID(new TASKS_Type());
				activityInfor.getTASKSID().setORGANIZATIONID(tools.getOrganization(context));
				activityInfor.getTASKSID().setTASKREVISION((long) 0);
			}
			activityInfor.getTASKSID().setTASKCODE(activityParam.getTaskCode());
			activityInfor.getTASKSID().setTASKQUANTITY(tools.getDataTypeTools().encodeQuantity(activityParam.getTaskQty(), "Task Quantity"));
		}

		//
		if (activityParam.getMaterialList() != null) {
			if (activityParam.getMaterialList().trim().equals("")) {
				activityInfor.setMATLIST(null);
			} else {
				activityInfor.setMATLIST(new MATLIST_Type());
				activityInfor.getMATLIST().setMTLCODE(activityParam.getMaterialList());
			}
		}

		// NOTE
		if (activityParam.getActivityNote() != null) {
			activityInfor.getACTIVITYID().setACTIVITYNOTE(activityParam.getActivityNote());
		}

		//
		// CALL THE WS
		//
		MP0038_SyncActivity_001 syncActivity = new MP0038_SyncActivity_001();
		syncActivity.setActivity(activityInfor);

		MP0038_SyncActivity_001_Result syncresult = null;

		if (context.getCredentials() != null) {
			syncresult = inforws.syncActivityOp(syncActivity, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			syncresult = inforws.syncActivityOp(syncActivity, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return syncresult.getResultData().getACTIVITYID().getACTIVITYCODE().getValue() + "";
	}

	public String deleteActivity(InforContext context, Activity activityParam) throws InforException {
		//
		// CALL THE WS
		//
		MP0039_DeleteActivity_001 deleteActivity = new MP0039_DeleteActivity_001();
		deleteActivity.setACTIVITYID(new ACTIVITYID());
		deleteActivity.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
		deleteActivity.getACTIVITYID().getACTIVITYCODE().setValue(tools.getDataTypeTools().encodeLong(activityParam.getActivityCode(), "Activity Code"));
		deleteActivity.getACTIVITYID().setWORKORDERID(new WOID_Type());
		deleteActivity.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		deleteActivity.getACTIVITYID().getWORKORDERID().setJOBNUM(activityParam.getWorkOrderNumber());
		deleteActivity.setConfirmdeletechecklist("true");

		MP0039_DeleteActivity_001_Result result = null;

		if (context.getCredentials() != null) {
			result = inforws.deleteActivityOp(deleteActivity, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.deleteActivityOp(deleteActivity, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return result.getResultData().getACTIVITYID().getACTIVITYCODE().getValue() + "";
	}


}
