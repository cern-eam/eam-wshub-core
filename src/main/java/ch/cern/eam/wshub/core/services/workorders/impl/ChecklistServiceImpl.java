package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestRow;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.ChecklistService;
import ch.cern.eam.wshub.core.services.workorders.entities.Activity;
import ch.cern.eam.wshub.core.services.workorders.entities.Finding;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.BooleanType;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.TaskplanCheckList;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderActivityCheckList;
import net.datastream.schemas.mp_entities.taskchecklist_001.TaskChecklist;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp7913_001.MP7913_SyncWorkOrderActivityCheckList_001;
import net.datastream.schemas.mp_functions.mp7914_001.MP7914_GetWorkOrderActivityCheckList_001;
import net.datastream.schemas.mp_functions.mp7916_001.MP7916_AddTaskChecklist_001;
import net.datastream.schemas.mp_functions.mp8000_001.MP8000_CreateFollowUpWorkOrder_001;
import net.datastream.schemas.mp_results.mp7914_001.MP7914_GetWorkOrderActivityCheckList_001_Result;
import net.datastream.schemas.mp_results.mp8000_001.MP8000_CreateFollowUpWorkOrder_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import static ch.cern.eam.wshub.core.tools.GridTools.getCellContent;

import javax.persistence.EntityManager;
import javax.xml.ws.Holder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class ChecklistServiceImpl implements ChecklistService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;
	private GridsService gridsService;

	public ChecklistServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
	}

	public String updateWorkOrderChecklist(InforContext context, WorkOrderActivityCheckList workOrderActivityCheckList) throws InforException {
		//
		// Fetch it first
		//
		MP7914_GetWorkOrderActivityCheckList_001 getwoactchl = new MP7914_GetWorkOrderActivityCheckList_001();
		getwoactchl.setCHECKLISTCODE(workOrderActivityCheckList.getCheckListCode());
		MP7914_GetWorkOrderActivityCheckList_001_Result getresult;

		if (context.getCredentials() != null) {
			getresult = inforws.getWorkOrderActivityCheckListOp(getwoactchl, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			getresult = inforws.getWorkOrderActivityCheckListOp(getwoactchl, "*", null, null,
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		//
		// Sync afterwards
		//
		net.datastream.schemas.mp_entities.workorderactivitychecklist_001.WorkOrderActivityCheckList workOrderActivityCheckListInfor = getresult
				.getResultData().getWorkOrderActivityCheckList();

		// Follow Up
		if (workOrderActivityCheckList.getFollowUp() != null) {
			workOrderActivityCheckListInfor.setFOLLOWUP(tools.getDataTypeTools().encodeBoolean(workOrderActivityCheckList.getFollowUp(), BooleanType.PLUS_MINUS));
		}

		switch (workOrderActivityCheckList.getType()) {
			case "01":
				// CHECK
				if ("COMPLETED".equalsIgnoreCase(workOrderActivityCheckList.getResult())) {
					workOrderActivityCheckListInfor.setCOMPLETED("true");
				} else {
					workOrderActivityCheckListInfor.setCOMPLETED("false");
				}
				break;
			case "02":
				// YES, NO
				workOrderActivityCheckListInfor
						.setYES(String.valueOf("YES".equals(workOrderActivityCheckList.getResult())));
				workOrderActivityCheckListInfor.setNO(String.valueOf("NO".equals(workOrderActivityCheckList.getResult())));
				break;
			case "03":
				// FINDING
				if (workOrderActivityCheckList.getFinding() != null) {
					workOrderActivityCheckListInfor.setFINDINGID(new FINDINGID_Type());
					workOrderActivityCheckListInfor.getFINDINGID().setFINDINGCODE(workOrderActivityCheckList.getFinding());
				} else {
					workOrderActivityCheckListInfor.setFINDINGID(null);
				}
				break;
			case "04":
				// NUMERIC VALUE
				workOrderActivityCheckListInfor
						.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(workOrderActivityCheckList.getResult(), "Checklists Value"));
				break;
			case "05":
				// METER READING
				workOrderActivityCheckListInfor
						.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(workOrderActivityCheckList.getResult(), "Checklists Value"));
				break;
			case "06":
				// INSPECTION
				if (workOrderActivityCheckList.getFinding() != null) {
					workOrderActivityCheckListInfor.setFINDINGID(new FINDINGID_Type());
					workOrderActivityCheckListInfor.getFINDINGID().setFINDINGCODE(workOrderActivityCheckList.getFinding());
				} else {
					workOrderActivityCheckListInfor.setFINDINGID(null);
				}
				workOrderActivityCheckListInfor
						.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(workOrderActivityCheckList.getResult(), "Checklists Value"));
				break;
			default:
		}

		if (workOrderActivityCheckList.getNotes() != null) {
			workOrderActivityCheckListInfor.setNOTES(workOrderActivityCheckList.getNotes());
		}

		MP7913_SyncWorkOrderActivityCheckList_001 syncwoactchl = new MP7913_SyncWorkOrderActivityCheckList_001();
		syncwoactchl.setWorkOrderActivityCheckList(workOrderActivityCheckListInfor);

		if (context.getCredentials() != null) {
			inforws.syncWorkOrderActivityCheckListOp(syncwoactchl, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.syncWorkOrderActivityCheckListOp(syncwoactchl, "*", null, null,
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		return null;
	}

	public String createTaskplanChecklist(InforContext context, TaskplanCheckList taskChecklist) throws InforException {
		TaskChecklist taskChecklistInfor = new TaskChecklist();
		//
		// TASK LIST ID
		//
		taskChecklistInfor.setTASKLISTID(new TASKLISTID_Type());
		taskChecklistInfor.getTASKLISTID().setORGANIZATIONID(tools.getOrganization(context));
		taskChecklistInfor.getTASKLISTID().setTASKCODE(taskChecklist.getTaskPlanCode());
		if (taskChecklist.getTaskPlanRevision() == null) {
			taskChecklistInfor.getTASKLISTID().setTASKREVISION(0L);
		} else {
			taskChecklistInfor.getTASKLISTID()
					.setTASKREVISION(tools.getDataTypeTools().encodeLong(taskChecklist.getTaskPlanRevision(), "Task Revision"));
		}
		//
		// DESCRIPTION
		//
		taskChecklistInfor.setCHECKLISTID(new CHECKLISTID_Type());
		taskChecklistInfor.getCHECKLISTID().setCHECKLISTCODE("0");
		taskChecklistInfor.getCHECKLISTID().setDESCRIPTION(taskChecklist.getChecklistDesc());
		//
		// SEQUENCE
		//
		taskChecklistInfor.setSEQUENCE(tools.getDataTypeTools().encodeLong(taskChecklist.getSequence(), "Sequence number"));
		//
		// TYPE
		//
		taskChecklistInfor.setTYPE(new TYPE_Type());
		taskChecklistInfor.getTYPE().setTYPECODE(taskChecklist.getType());
		//
		// REQUIRED ENTRY
		//
		taskChecklistInfor.setREQUIREDTOCLOSEDOC(new USERDEFINEDCODEID_Type());
		taskChecklistInfor.getREQUIREDTOCLOSEDOC().setUSERDEFINEDCODE(taskChecklist.getRequiredEntry());
		//
		// EQUIPMENT LEVEL
		//
		taskChecklistInfor.setEQUIPMENTLEVEL(new USERDEFINEDCODEID_Type());
		taskChecklistInfor.getEQUIPMENTLEVEL().setUSERDEFINEDCODE(taskChecklist.getEquipmentLevel());
		//
		// POSSIBLE FINDINGS
		//
		if (taskChecklist.getFindings() != null) {
			taskChecklistInfor.setPOSSIBLEFINDINGS(taskChecklist.getFindings());
		}
		//
		// UOM
		//
		if (taskChecklist.getUOM() != null) {
			taskChecklistInfor.setUOMID(new UOMID_Type());
			taskChecklistInfor.getUOMID().setUOMCODE(taskChecklist.getUOM());
		}
		//
		// ASPECT ID
		//
		if (taskChecklist.getAspectCode() != null) {
			taskChecklistInfor.setASPECTID(new ASPECTID_Type());
			taskChecklistInfor.getASPECTID().setASPECTCODE(taskChecklist.getAspectCode());
		}
		//
		// POINT TYPE ID
		//
		if (taskChecklist.getPointType() != null) {
			taskChecklistInfor.setPOINTTYPEID(new POINTTYPEID_Type());
			taskChecklistInfor.getPOINTTYPEID().setPOINTTYPECODE(taskChecklist.getPointType());
		}
		//
		// REPEATING OCCURRENCES
		//
		taskChecklistInfor.setREPEATINGOCCURRENCES(taskChecklist.getRepeatingOccurrences());
		//
		// FOLLOW-UP TASK PLAN
		//
		if (taskChecklist.getFollowUpTaskPlan() != null) {
			taskChecklistInfor.setFOLLOWUPTASKID(new TASKLISTID_Type());
			taskChecklistInfor.getFOLLOWUPTASKID().setORGANIZATIONID(tools.getOrganization(context));
			taskChecklistInfor.getFOLLOWUPTASKID().setTASKCODE(taskChecklist.getFollowUpTaskPlan());
			if (taskChecklist.getFollowUpTaskPlanRevision() != null) {
				taskChecklistInfor.getFOLLOWUPTASKID().setTASKREVISION(
						tools.getDataTypeTools().encodeLong(taskChecklist.getFollowUpTaskPlanRevision(), "Follow Up Task Plan Revision"));
			} else {
				taskChecklistInfor.getFOLLOWUPTASKID().setTASKREVISION(0L);
			}
		}
		//
		// CLASS
		//
		if (taskChecklist.getClassCode() != null && !taskChecklist.getClassCode().trim().equals("")) {
			taskChecklistInfor.setCLASSID(new CLASSID_Type());
			taskChecklistInfor.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
			taskChecklistInfor.getCLASSID().setCLASSCODE(taskChecklist.getClassCode());
		}
		//
		// CATEGORY
		//
		if (taskChecklist.getCategoryCode() != null && !taskChecklist.getCategoryCode().trim().equals("")) {
			taskChecklistInfor.setCATEGORYID(new CATEGORYID());
			taskChecklistInfor.getCATEGORYID().setCATEGORYCODE(taskChecklist.getCategoryCode());
		}

		MP7916_AddTaskChecklist_001 addTaskChecklist = new MP7916_AddTaskChecklist_001();
		addTaskChecklist.setTaskChecklist(taskChecklistInfor);

		if (context.getCredentials() != null) {
			inforws.addTaskChecklistOp(addTaskChecklist, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addTaskChecklistOp(addTaskChecklist, "*", null, null,
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		return "OK";
	}

	public WorkOrderActivityCheckList[] readWorkOrderChecklists(InforContext context, Activity activity) throws InforException {
		LinkedList<WorkOrderActivityCheckList> checklists = new LinkedList<WorkOrderActivityCheckList>();
		// Fetch the data
		GridRequest gridRequest = new GridRequest("3315", "WSJOBS_ACK", "3369");
		gridRequest.setRowCount(1000);
		gridRequest.setUseNative(false);
		gridRequest.getParams().put("param.workordernum", activity.getWorkOrderNumber());
		gridRequest.getParams().put("param.activity", activity.getActivityCode());
		gridRequest.getParams().put("param.jobseq", "0");
		GridRequestResult gridRequestResult = gridsService.executeQuery(context, gridRequest);

		for (GridRequestRow gridRequestRow : gridRequestResult.getRows()) {

			WorkOrderActivityCheckList checklistTemp = new WorkOrderActivityCheckList();
			checklistTemp.setWorkOrderCode(activity.getWorkOrderNumber());
			checklistTemp.setActivityCode(activity.getActivityCode());
			checklistTemp.setCheckListCode(getCellContent("checklistcode", gridRequestRow));
			//checklistTemp.setOccurrence(v_result.getString("ack_occurrence"));
			checklistTemp.setSequence(getCellContent("checklistsequence", gridRequestRow));
			checklistTemp.setEquipmentCode(getCellContent("equipment", gridRequestRow));
			checklistTemp.setEquipmentDesc(getCellContent("equipmentdesc", gridRequestRow));
			checklistTemp.setType(getCellContent("checklisttype", gridRequestRow));

			// FOLLOW-UP
			String followUp = getCellContent("followup", gridRequestRow);
			if ("true".equals(followUp)) {
				checklistTemp.setFollowUp("+");
			} else {
				checklistTemp.setFollowUp("-");
			}

			// FOLLOW-UP WORK ORDER
			String followUpWorkOrderActivity = getCellContent("followupwoactivity", gridRequestRow);
			if (tools.getDataTypeTools().isNotEmpty(followUpWorkOrderActivity)) {
				// Remove the activity after the Work Order Number
				checklistTemp.setFollowUpWorkOrder(followUpWorkOrderActivity.split("-")[0]);
			}

			// REQUIRED
			String required = getCellContent("requiredtoclosedocument", gridRequestRow);
			if ("Yes".equals(required)) {
				checklistTemp.setRequiredToClose(true);
			} else {
				checklistTemp.setRequiredToClose(false);
			}

			// NOTES
			checklistTemp.setNotes(getCellContent("notes", gridRequestRow));

			//checklistTemp.setFinalOccurrence(v_result.getString("ack_finaloccurrence"));
			checklistTemp.setDesc(getCellContent("checklistdescription", gridRequestRow));

			//
			// VALUES FOR DIFFERENT CHECKLIST TYPES
			//
			if (checklistTemp.getType().equals("01")) {
				// CHECKLIST ITEM
				if ("true".equals(getCellContent("completed", gridRequestRow))) {
					checklistTemp.setResult("COMPLETED");
				} else {
					checklistTemp.setResult(null);
				}
			}
			if (checklistTemp.getType().equals("02")) {
				// QUESTION
				if (getCellContent("yes", gridRequestRow) != null && getCellContent("yes", gridRequestRow).equals("true")) {
					checklistTemp.setResult("YES");
				}
				if (getCellContent("no", gridRequestRow) != null && getCellContent("no", gridRequestRow).equals("true")) {
					checklistTemp.setResult("NO");
				}
			}
			if (checklistTemp.getType().equals("03")) {
				// QUALITATIVE
				checklistTemp.setFinding(getCellContent("finding", gridRequestRow));
				String[] possibleFindings = getCellContent("possiblefindings", gridRequestRow).split(",");
				List<Finding> findings = new LinkedList<Finding>();

				for (String findingCode : possibleFindings) {
					EntityManager em = tools.getEntityManager();
					try {
						Finding findingTemp = em.find(Finding.class, findingCode);
						findings.add(findingTemp);
					} catch (Exception e) {
						tools.log(Level.SEVERE,"Error in readWOActivityChecklists");
					} finally {
						em.close();
					}
				}
				checklistTemp.setPossibleFindings(findings.toArray(new Finding[findings.size()]));

			}
			if (checklistTemp.getType().equals("04")) {
				// QUANTITATIVE
				checklistTemp.setResult(getCellContent("value", gridRequestRow));
				checklistTemp.setUOM(getCellContent("uom", gridRequestRow));
			}
			if (checklistTemp.getType().equals("05")) {
				// METER READING
				checklistTemp.setResult(getCellContent("value", gridRequestRow));
				checklistTemp.setUOM(getCellContent("uom", gridRequestRow));
			}
			if (checklistTemp.getType().equals("06")) {
				// INSPECTION
				checklistTemp.setResult(getCellContent("value", gridRequestRow));
				checklistTemp.setUOM(getCellContent("uom", gridRequestRow));
				checklistTemp.setFinding(getCellContent("finding", gridRequestRow));
				String[] possibleFindings = getCellContent("possiblefindings", gridRequestRow).split(",");
				List<Finding> findings = new LinkedList<Finding>();

				for (String findingCode : possibleFindings) {
					EntityManager em = tools.getEntityManager();
					try {
						Finding findingTemp = em.find(Finding.class, findingCode);
						findings.add(findingTemp);
					} catch (Exception e) {
						tools.log(Level.SEVERE,"Error in readWOActivityChecklists");
					} finally {
						em.close();
					}
				}
				checklistTemp.setPossibleFindings(findings.toArray(new Finding[findings.size()]));
			}
			checklists.add(checklistTemp);
		}


		return checklists.toArray(new WorkOrderActivityCheckList[] {});
	}

	private String getValue(ResultSet v_result) throws SQLException {
		Double value = v_result.getDouble("ack_value");
		if (v_result.wasNull()) {
			return null;
		} else {
			return Double.toString(value);
		}
	}

	/**
	 * Webservice to create Follow Up workorders for checklist activities
	 * @param context
	 * @param activity
	 * @return Number of work orders that were created
	 * @throws InforException
	 */

	public Long createFollowUpWorkOrders(InforContext context, Activity activity) throws InforException {
		MP8000_CreateFollowUpWorkOrder_001 createFUWO = new MP8000_CreateFollowUpWorkOrder_001();
		MP8000_CreateFollowUpWorkOrder_001_Result createFUWOResult;

		Long activityNumber;
		try {
			activityNumber = Long.valueOf( activity.getActivityCode());
		} catch(Exception e) {
			throw new InforException("Activity code '" + activity.getActivityCode() + "' is not a valid number.", e.getCause(), null);
		}

		createFUWO.setACTIVITYID(new ACTIVITYID());
		createFUWO.getACTIVITYID().setWORKORDERID(new WOID_Type());
		createFUWO.getACTIVITYID().getWORKORDERID().setJOBNUM(activity.getWorkOrderNumber());
		createFUWO.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
		createFUWO.getACTIVITYID().getACTIVITYCODE().setValue(activityNumber);
		createFUWO.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));

		if (context.getCredentials() != null) {
			createFUWOResult = inforws.createFollowUpWorkOrderOp(createFUWO, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			createFUWOResult = inforws.createFollowUpWorkOrderOp(createFUWO, "*", null, null,
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return createFUWOResult.getResultData().getWORKORDERCOUNT();
	}
}
