package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger logger = LoggerFactory.getLogger(ChecklistServiceImpl.class);

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public ChecklistServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
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
					tools.createMessageConfig(), applicationData.getTenant());
		} else {
			getresult = inforws.getWorkOrderActivityCheckListOp(getwoactchl, "*", null, null,
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), applicationData.getTenant());
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
					tools.createMessageConfig(), applicationData.getTenant());
		} else {
			inforws.syncWorkOrderActivityCheckListOp(syncwoactchl, "*", null, null,
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), applicationData.getTenant());
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
					tools.createMessageConfig(), applicationData.getTenant());
		} else {
			inforws.addTaskChecklistOp(addTaskChecklist, "*", null, null,
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), applicationData.getTenant());
		}

		return "OK";
	}

	public WorkOrderActivityCheckList[] readWorkOrderChecklists(InforContext context, Activity activity) throws InforException {
		// Check if the client was configured with the DB connection
		tools.demandDatabaseConnection();

		LinkedList<WorkOrderActivityCheckList> checklists = new LinkedList<WorkOrderActivityCheckList>();
		Connection v_connection = null;
		Statement stmt = null;
		ResultSet v_result = null;
		try {
			String sqlQuery = "with checklist_data as(select ack_event,ack_act,ack_code,ack_occurrence,ack_sequence,ack_object, "
					+ " ack_type,ack_completed,ack_yes,ack_no,ack_finding,ack_possiblefindings,ack_value,ack_uom,ack_notes, ack_finaloccurrence, o.obj_desc, ack_followup, ack_requiredtoclose, ack_followupevent, "
					+ " NVL((SELECT TRA_TEXT FROM U5TRANSLATIONS WHERE TRA_PAGENAME = 'EAM_CHECKLIST' AND TRA_ELEMENTID = ACK_TASKCHECKLISTCODE AND TRA_LANGUAGE = '"
					+ context.getCredentials().getLanguage() + "'), ack_desc) ack_desc, "
					+ " NVL((SELECT ROB_LINE FROM R5ROUTOBJECTS WHERE ROB_ROUTE = (SELECT EVT_ROUTE FROM R5EVENTS WHERE EVT_CODE = '"
					+ activity.getWorkOrderNumber() + "') AND ROB_OBJECT = ack_object), 1) eqp_order "
					+ " from R5ACTCHECKLISTS c, R5OBJECTS o where obj_code = ack_object and ack_event = '"
					+ activity.getWorkOrderNumber() + "' and ack_act = '" + activity.getActivityCode()
					+ "' order by ack_object, ack_sequence, ack_occurrence)  "
					+ " select * from checklist_data order by eqp_order,ack_sequence, ack_occurrence";
			v_connection = tools.getDataSource().getConnection();
			stmt = v_connection.createStatement();
			v_result = stmt.executeQuery(sqlQuery);
			while (v_result.next()) {
				WorkOrderActivityCheckList checklistTemp = new WorkOrderActivityCheckList();
				checklistTemp.setWorkOrderCode(v_result.getString("ack_event"));
				checklistTemp.setActivityCode(v_result.getString("ack_act"));
				checklistTemp.setCheckListCode(v_result.getString("ack_code"));
				checklistTemp.setOccurrence(v_result.getString("ack_occurrence"));
				checklistTemp.setSequence(v_result.getString("ack_sequence"));
				checklistTemp.setEquipmentCode(v_result.getString("ack_object"));
				checklistTemp.setEquipmentDesc(v_result.getString("obj_desc"));
				checklistTemp.setType(v_result.getString("ack_type"));
				checklistTemp.setFollowUp(v_result.getString("ack_followup"));
				checklistTemp.setFollowUpWorkOrder(v_result.getString("ack_followupevent"));
				checklistTemp.setRequiredToClose(v_result.getString("ack_requiredtoclose"));
				if (checklistTemp.getType().equals("01")) {
					// CHECKLIST ITEM
					if ("+".equals(v_result.getString("ack_completed"))) {
						checklistTemp.setResult("COMPLETED");
					} else {
						checklistTemp.setResult(null);
					}
				}
				if (checklistTemp.getType().equals("02")) {
					// QUESTION
					if (v_result.getString("ack_yes") != null && v_result.getString("ack_yes").equals("+")) {
						checklistTemp.setResult("YES");
					}
					if (v_result.getString("ack_no") != null && v_result.getString("ack_no").equals("+")) {
						checklistTemp.setResult("NO");
					}
				}
				if (checklistTemp.getType().equals("03")) {
					// QUALITATIVE
					checklistTemp.setFinding(v_result.getString("ack_finding"));
					String[] possibleFindings = v_result.getString("ack_possiblefindings").split(",");
					List<Finding> findings = new LinkedList<Finding>();

					for (String findingCode : possibleFindings) {
						EntityManager em = tools.getEntityManager();
						try {
							Finding findingTemp = em.find(Finding.class, findingCode);
							findings.add(findingTemp);
						} catch (Exception e) {
							logger.error("Error in readWOActivityChecklists", e);
						} finally {
							em.close();
						}
					}
					checklistTemp.setPossibleFindings(findings.toArray(new Finding[findings.size()]));

				}
				if (checklistTemp.getType().equals("04")) {
					// QUANTITATIVE
					checklistTemp.setResult(getValue(v_result));
					checklistTemp.setUOM(v_result.getString("ack_uom"));
				}
				if (checklistTemp.getType().equals("05")) {
					// METER READING
					checklistTemp.setResult(getValue(v_result));
					checklistTemp.setUOM(v_result.getString("ack_uom"));
				}
				if (checklistTemp.getType().equals("06")) {
					// INSPECTION
					checklistTemp.setResult(getValue(v_result));
					checklistTemp.setUOM(v_result.getString("ack_uom"));
					checklistTemp.setFinding(v_result.getString("ack_finding"));
					String[] possibleFindings = v_result.getString("ack_possiblefindings").split(",");
					List<Finding> findings = new LinkedList<Finding>();

					for (String findingCode : possibleFindings) {
						EntityManager em = tools.getEntityManager();
						try {
							Finding findingTemp = em.find(Finding.class, findingCode);
							findings.add(findingTemp);
						} catch (Exception e) {
							logger.error("Error in readWOActivityChecklists", e);
						} finally {
							em.close();
						}
					}
					checklistTemp.setPossibleFindings(findings.toArray(new Finding[findings.size()]));
				}
				checklistTemp.setNotes(v_result.getString("ack_notes"));
				checklistTemp.setFinalOccurrence(v_result.getString("ack_finaloccurrence"));
				checklistTemp.setDesc(v_result.getString("ack_desc"));
				checklists.add(checklistTemp);
			}

		} catch (SQLException e) {
			tools.log(Level.WARNING, "Couldn't read checklist line: " + e.getMessage());
		} finally {
			try {
				if (v_result != null)
					v_result.close();
				if (stmt != null)
					stmt.close();
				if (v_connection != null)
					v_connection.close();
			} catch (Exception e) {
				tools.log(Level.WARNING, "Couldn't close the connection!" + e.getMessage());
			}
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
					tools.createMessageConfig(), applicationData.getTenant());
		} else {
			createFUWOResult = inforws.createFollowUpWorkOrderOp(createFUWO, "*", null, null,
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), applicationData.getTenant());
		}
		return createFUWOResult.getResultData().getWORKORDERCOUNT();
	}
}
