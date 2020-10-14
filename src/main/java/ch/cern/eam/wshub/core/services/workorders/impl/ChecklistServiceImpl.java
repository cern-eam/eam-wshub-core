package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.Signature;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.ChecklistService;
import ch.cern.eam.wshub.core.services.workorders.entities.*;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderActivityCheckList.CheckListType;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderActivityCheckList.ReturnType;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.taskchecklist_001.TaskChecklist;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp7913_001.MP7913_SyncWorkOrderActivityCheckList_001;
import net.datastream.schemas.mp_functions.mp7914_001.MP7914_GetWorkOrderActivityCheckList_001;
import net.datastream.schemas.mp_functions.mp7916_001.MP7916_AddTaskChecklist_001;
import net.datastream.schemas.mp_functions.mp7997_001.MP7997_PerformWorkOrderActivityCheckList_001;
import net.datastream.schemas.mp_functions.mp7998_001.MP7998_ReviewWorkOrderActivityCheckList_001;
import net.datastream.schemas.mp_functions.mp7999_001.MP7999_GetWorkOrderActivityCheckListDefault_001;
import net.datastream.schemas.mp_functions.mp8000_001.MP8000_CreateFollowUpWorkOrder_001;
import net.datastream.schemas.mp_results.mp7914_001.MP7914_GetWorkOrderActivityCheckList_001_Result;
import net.datastream.schemas.mp_results.mp7999_001.MP7999_GetWorkOrderActivityCheckListDefault_001_Result;
import net.datastream.schemas.mp_results.mp8000_001.MP8000_CreateFollowUpWorkOrder_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;
import static ch.cern.eam.wshub.core.tools.GridTools.extractSingleResult;
import static ch.cern.eam.wshub.core.tools.GridTools.getCellContent;
import static ch.cern.eam.wshub.core.tools.GridTools.convertGridResultToMap;

public class ChecklistServiceImpl implements ChecklistService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;
	private GridsService gridsService;
	public static final Map<String, String> findingsCache = new ConcurrentHashMap<>();

	public ChecklistServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
	}

	public WorkOrderActivityChecklistSignatureResult[] getSignatures(InforContext context, String workOrderCode,
																	 String activityCode) throws InforException {
		MP7999_GetWorkOrderActivityCheckListDefault_001_Result getResult = getSignatureWS(context, workOrderCode, activityCode);
		WorkOrderActivityCheckListDefaultResult workOrderActivityCheckListDefaultResult = new WorkOrderActivityCheckListDefaultResult();
		tools.getInforFieldTools().transformInforObject(workOrderActivityCheckListDefaultResult,
				getResult.getResultData().getWorkOrderActivityCheckListDefault());

		WorkOrderActivityChecklistSignatureResult[] res = filterSignatures(workOrderActivityCheckListDefaultResult);
		getResponsibilityDescriptions(context, res);
		return res;
	}

	private void getResponsibilityDescriptions(InforContext context, WorkOrderActivityChecklistSignatureResult[] signatures)
			throws InforException {

		GridRequest gridRequest = new GridRequest("LVUSERRESPONSIBILITIES");
		gridRequest.setDataspyID("4297");
		gridRequest.getParams().put("param.rentity", "RESP");
		List<GridRequestFilter> filters = new LinkedList<>();
		for(WorkOrderActivityChecklistSignatureResult signatureResult : signatures){
			String responsibilityCode = signatureResult.getResponsibilityCode();
			if(responsibilityCode != null) {
				filters.add(new GridRequestFilter(
						"responsibility", responsibilityCode, "=", GridRequestFilter.JOINER.OR, false, false));
			}
		}
		if(filters.size() == 0) return;
		gridRequest.setGridRequestFilters(filters);
		Map<String, String>	 responsibilityToDescription =
				convertGridResultToMap("responsibility", "description", gridsService.executeQuery(context, gridRequest));

		Arrays.stream(signatures).forEach(signature -> {
			if(responsibilityToDescription.containsKey(signature.getResponsibilityCode()))
				signature.setResponsibilityDescription(responsibilityToDescription.get(signature.getResponsibilityCode()));
		});
	}

	private WorkOrderActivityChecklistSignatureResult[] filterSignatures(WorkOrderActivityCheckListDefaultResult workOrderActivityCheckList){
		String reviewerQualification = workOrderActivityCheckList.getReviewerQualification();
		String performer1Qualification = workOrderActivityCheckList.getPerformer1Qualification();
		String performer2Qualification = workOrderActivityCheckList.getPerformer2Qualification();
		List<String> qualifications = new ArrayList<>();

		if(workOrderActivityCheckList.getUserQualifications() != null){
			qualifications = workOrderActivityCheckList.getUserQualifications().stream()
					.map(UserQualification::getUserDefinedCode)
					.collect(Collectors.toList());
		}

		boolean noRequiredQualifications = reviewerQualification == null &&
				                           performer1Qualification == null &&
				                           performer2Qualification == null;

		List<WorkOrderActivityChecklistSignatureResult> signatures = new LinkedList<>();

		boolean isPerformer1 = noRequiredQualifications || performer1Qualification == null
							   || qualifications.contains(performer1Qualification);

		boolean isPerformer2 = noRequiredQualifications || performer2Qualification == null && isPerformer1
				               || qualifications.contains(performer2Qualification);

		boolean isReviewer = noRequiredQualifications || reviewerQualification == null && (isPerformer1 || isPerformer2)
							 || qualifications.contains(reviewerQualification);


		if(isPerformer1 || isReviewer){
			WorkOrderActivityChecklistSignatureResult perf1 = new WorkOrderActivityChecklistSignatureResult();
			perf1.setType("PB01");
			perf1.setSigner(workOrderActivityCheckList.getPerformer1Name());
			perf1.setViewAsPerformer(isPerformer1);
			perf1.setViewAsReviewer(isReviewer);
			perf1.setTime(workOrderActivityCheckList.getTimePerf1() != null ? workOrderActivityCheckList.getTimePerf1() : null);
			perf1.setResponsibilityCode(performer1Qualification);
			signatures.add(perf1);
		}

		if(isPerformer2 || isReviewer){
			WorkOrderActivityChecklistSignatureResult perf2 = new WorkOrderActivityChecklistSignatureResult();
			perf2.setType("PB02");
			perf2.setSigner(workOrderActivityCheckList.getPerformer2Name());
			perf2.setViewAsPerformer(isPerformer2);
			perf2.setViewAsReviewer(isReviewer);
			perf2.setTime(workOrderActivityCheckList.getTimePerf2() != null ?
					workOrderActivityCheckList.getTimePerf2() : null);
			perf2.setResponsibilityCode(performer2Qualification != null ? performer2Qualification : performer1Qualification);
			signatures.add(perf2);
		}

		if(isReviewer){
			WorkOrderActivityChecklistSignatureResult reviewer = new WorkOrderActivityChecklistSignatureResult();
			reviewer.setType("RB01");
			reviewer.setSigner(workOrderActivityCheckList.getReviewerName());
			reviewer.setViewAsPerformer(workOrderActivityCheckList.getPerformer1Name() != null
					|| workOrderActivityCheckList.getPerformer2Name() != null);
			reviewer.setTime(workOrderActivityCheckList.getTimeRev1() != null ? workOrderActivityCheckList.getTimeRev1() : null);
			if(reviewerQualification == null && performer1Qualification != null && performer2Qualification == null)
				reviewerQualification = performer1Qualification;


			reviewer.setResponsibilityCode(reviewerQualification);
			signatures.add(reviewer);
		}

		return signatures.toArray(new WorkOrderActivityChecklistSignatureResult[0]);
	}

	private MP7999_GetWorkOrderActivityCheckListDefault_001_Result getSignatureWS(InforContext context, String workOrderCode, String activityCode) throws InforException {
		MP7999_GetWorkOrderActivityCheckListDefault_001 getWorkOrderActivityCheckListDefault = new MP7999_GetWorkOrderActivityCheckListDefault_001();
		transformGetWorkOrderActivityCheckListDefaultRequest(getWorkOrderActivityCheckListDefault, workOrderCode, activityCode);
		return tools.performInforOperation(context, inforws::getWorkOrderActivityCheckListDefaultOp, getWorkOrderActivityCheckListDefault);
	}

	private void transformGetWorkOrderActivityCheckListDefaultRequest(MP7999_GetWorkOrderActivityCheckListDefault_001 getWorkOrderActivityCheckListDefault,
																	  String workOrderCode, String activityCode) {
		ACTIVITYCODE activityCodeField = new ACTIVITYCODE();
		activityCodeField.setValue(Long.parseLong(activityCode));
		getWorkOrderActivityCheckListDefault.setACTIVITYCODE(activityCodeField);
		WOID_Type workOrderID = new WOID_Type();
		workOrderID.setJOBNUM(workOrderCode);
		workOrderID.setAuto_Generated(true);
		getWorkOrderActivityCheckListDefault.setWORKORDERID(workOrderID);
	}

	public WorkOrderActivityChecklistSignatureResponse eSignWorkOrderActivityChecklist(InforContext context, WorkOrderActivityCheckListSignature workOrderActivityCheckListSignature)
			throws InforException{
		Signature signature = new Signature();
		signature.setUserCode(workOrderActivityCheckListSignature.getUserCode());
		signature.setPassword(workOrderActivityCheckListSignature.getPassword());
		signature.setSignatureType(workOrderActivityCheckListSignature.getSignatureType());
		context.setSignature(signature);
		if(signature.getSignatureType().endsWith("02"))
			workOrderActivityCheckListSignature.setSequenceNumber(new BigInteger("2"));
		if(workOrderActivityCheckListSignature.getSignatureType().startsWith("PB")) {
			return performWorkOrderActivityChecklist(context, workOrderActivityCheckListSignature);
		} else {
			return reviewWorkOrderActivityCheckList(context, workOrderActivityCheckListSignature);
		}
	}

	private WorkOrderActivityChecklistSignatureResponse performWorkOrderActivityChecklist(InforContext context, WorkOrderActivityCheckListSignature workOrderActivityCheckListSignature)
			throws InforException {

		MP7997_PerformWorkOrderActivityCheckList_001 performWorkOrderActivityCheckList = new MP7997_PerformWorkOrderActivityCheckList_001();

		tools.getInforFieldTools().transformWSHubObject(performWorkOrderActivityCheckList, workOrderActivityCheckListSignature, context);
		tools.performInforOperation(context, inforws::performWorkOrderActivityCheckListOp, performWorkOrderActivityCheckList);

		MP7999_GetWorkOrderActivityCheckListDefault_001_Result getResult =
				getSignatureWS(context, workOrderActivityCheckListSignature.getWorkOrderCode(),
						       workOrderActivityCheckListSignature.getActivityCodeValue().toString());
		if(workOrderActivityCheckListSignature.getSequenceNumber() != null &&
				workOrderActivityCheckListSignature.getSequenceNumber().intValue() == 2) {
			return transformESIGNATUREtoResponse(getResult.getResultData().getWorkOrderActivityCheckListDefault().getPERFORMEDBYESIGN2().getESIGNATURE());
		} else {
			return transformESIGNATUREtoResponse(getResult.getResultData().getWorkOrderActivityCheckListDefault().getPERFORMEDBYESIGN().getESIGNATURE());
		}
	}

	private WorkOrderActivityChecklistSignatureResponse reviewWorkOrderActivityCheckList(InforContext context, WorkOrderActivityCheckListSignature workOrderActivityCheckListSignature)
			throws InforException {

		MP7998_ReviewWorkOrderActivityCheckList_001 reviewWorkOrderActivityCheckList = new MP7998_ReviewWorkOrderActivityCheckList_001();

		tools.getInforFieldTools().transformWSHubObject(reviewWorkOrderActivityCheckList, workOrderActivityCheckListSignature, context);
		tools.performInforOperation(context, inforws::reviewWorkOrderActivityCheckListOp, reviewWorkOrderActivityCheckList);

		MP7999_GetWorkOrderActivityCheckListDefault_001_Result getResult =
				getSignatureWS(context, workOrderActivityCheckListSignature.getWorkOrderCode(),
						       workOrderActivityCheckListSignature.getActivityCodeValue().toString());

		return transformESIGNATUREtoResponse(getResult.getResultData().getWorkOrderActivityCheckListDefault().getREVIEWEDBYESIGN().getESIGNATURE());
	}

	private WorkOrderActivityChecklistSignatureResponse transformESIGNATUREtoResponse(ESIGNATURE eSignature) {
		WorkOrderActivityChecklistSignatureResponse response = new WorkOrderActivityChecklistSignatureResponse();
		response.setSigner(eSignature.getUSERID().getDESCRIPTION());
		response.setTimeStamp(decodeInforDate(eSignature.getEXTERNALDATETIME()));
		return response;
	}

	public String updateWorkOrderChecklist(InforContext context, WorkOrderActivityCheckList workOrderActivityCheckList) throws InforException {
		//
		// Fetch it first
		//
		MP7914_GetWorkOrderActivityCheckList_001 getwoactchl = new MP7914_GetWorkOrderActivityCheckList_001();
		getwoactchl.setCHECKLISTCODE(workOrderActivityCheckList.getCheckListCode());
		MP7914_GetWorkOrderActivityCheckList_001_Result getresult =
			tools.performInforOperation(context, inforws::getWorkOrderActivityCheckListOp, getwoactchl);

		//
		// Sync afterwards
		//
		net.datastream.schemas.mp_entities.workorderactivitychecklist_001.WorkOrderActivityCheckList workOrderActivityCheckListInfor = getresult
				.getResultData().getWorkOrderActivityCheckList();

		// Follow Up
		if (workOrderActivityCheckList.getFollowUp() != null) {
			workOrderActivityCheckListInfor.setFOLLOWUP(tools.getDataTypeTools().encodeBoolean(workOrderActivityCheckList.getFollowUp(), BooleanType.PLUS_MINUS));
		}

		Function<String, String> getStringBool =
			key -> String.valueOf(key.equals(workOrderActivityCheckList.getResult()));

		switch (workOrderActivityCheckList.getType()) {
			case CheckListType.CHECKLIST_ITEM:
				if (ReturnType.COMPLETED.equalsIgnoreCase(workOrderActivityCheckList.getResult())) {
					workOrderActivityCheckListInfor.setCOMPLETED("true");
				} else {
					workOrderActivityCheckListInfor.setCOMPLETED("false");
				}
				break;
			case CheckListType.QUESTION_YES_NO:
				workOrderActivityCheckListInfor.setYES(getStringBool.apply(ReturnType.YES));
				workOrderActivityCheckListInfor.setNO(getStringBool.apply(ReturnType.NO));
				break;
			case CheckListType.QUALITATIVE:
				if (workOrderActivityCheckList.getFinding() != null) {
					workOrderActivityCheckListInfor.setFINDINGID(new FINDINGID_Type());
					workOrderActivityCheckListInfor.getFINDINGID().setFINDINGCODE(workOrderActivityCheckList.getFinding());
				} else {
					workOrderActivityCheckListInfor.setFINDINGID(null);
				}
				break;
			case CheckListType.INSPECTION:
				if (workOrderActivityCheckList.getFinding() != null) {
					workOrderActivityCheckListInfor.setFINDINGID(new FINDINGID_Type());
					workOrderActivityCheckListInfor.getFINDINGID().setFINDINGCODE(workOrderActivityCheckList.getFinding());
				} else {
					workOrderActivityCheckListInfor.setFINDINGID(null);
				}
				// no break here, INSPECTION is the same as QUANTITATIVE/METER_READING,
				// but with findings and possible findings, so we will set the numeric value below
			case CheckListType.QUANTITATIVE:
			case CheckListType.METER_READING:
				BigDecimal numericValue = workOrderActivityCheckList.getNumericValue();

				// this logic is used while applications are not yet using the numeric value field
				// using the result field in the way below is deprecated
				if(numericValue == null) {
					BigDecimal possibleNumericValue =
						encodeBigDecimal(workOrderActivityCheckList.getResult(), "");

					if(possibleNumericValue != null) numericValue = possibleNumericValue;
				}

				workOrderActivityCheckListInfor
						.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(numericValue, "Checklists Value"));
				break;
			case CheckListType.OK_REPAIR_NEEDED:
				workOrderActivityCheckListInfor.setOKFLAG(getStringBool.apply(ReturnType.OK));
				workOrderActivityCheckListInfor.setREPAIRSNEEDED(getStringBool.apply(ReturnType.REPAIRSNEEDED));

				if(isEmpty(workOrderActivityCheckList.getFinding())) {
					workOrderActivityCheckListInfor.setRESOLUTIONID(null);
				} else {
					workOrderActivityCheckListInfor.setRESOLUTIONID(new USERDEFINEDCODEID_Type());
					workOrderActivityCheckListInfor.getRESOLUTIONID().setUSERDEFINEDCODE(workOrderActivityCheckList.getFinding());
				}
				break;
			case CheckListType.GOOD_POOR:
				workOrderActivityCheckListInfor.setGOOD(getStringBool.apply(ReturnType.GOOD));
				workOrderActivityCheckListInfor.setPOOR(getStringBool.apply(ReturnType.POOR));
				break;
			case CheckListType.OK_ADJUSTED_MEASUREMENT:
				workOrderActivityCheckListInfor
					.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(workOrderActivityCheckList.getNumericValue(), "Checklists Value"));
				// no break here, OK_ADJUSTED_MEASUREMENT is the same as OK_ADJUSTED,
				// but with a numeric value, so we will set the result to OK/ADJUSTED below
			case CheckListType.OK_ADJUSTED:
				workOrderActivityCheckListInfor.setOKFLAG(getStringBool.apply(ReturnType.OK));
				workOrderActivityCheckListInfor.setADJUSTED(getStringBool.apply(ReturnType.ADJUSTED));
				break;
			case CheckListType.NONCONFORMITY_MEASUREMENT:
				workOrderActivityCheckListInfor
					.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(workOrderActivityCheckList.getNumericValue(), "Checklists Value"));
				// no break here, NONCONFORMITY_MEASUREMENT is the same as NONCONFORMITY_CHECK,
				// but with a numberic value, so we will set the result to OK/NONCONFORMITY below
			case CheckListType.NONCONFORMITY_CHECK:
				workOrderActivityCheckListInfor.setOKFLAG(getStringBool.apply(ReturnType.OK));
				workOrderActivityCheckListInfor.setNONCONFORMITYFLAG(getStringBool.apply(ReturnType.NONCONFORMITY));
				break;
		}

		if (workOrderActivityCheckList.getNotes() != null) {
			workOrderActivityCheckListInfor.setNOTES(workOrderActivityCheckList.getNotes());
		}

		MP7913_SyncWorkOrderActivityCheckList_001 syncwoactchl = new MP7913_SyncWorkOrderActivityCheckList_001();
		syncwoactchl.setWorkOrderActivityCheckList(workOrderActivityCheckListInfor);

		tools.performInforOperation(context, inforws::syncWorkOrderActivityCheckListOp, syncwoactchl);
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

		tools.performInforOperation(context, inforws::addTaskChecklistOp, addTaskChecklist);
		return "OK";
	}

	public WorkOrderActivityCheckList[] readWorkOrderChecklists(InforContext context, Activity activity) throws InforException {
		// Fetch the data
		GridRequest gridRequest = new GridRequest("3315", "WSJOBS_ACK", "3369");
		gridRequest.setRowCount(1000);
		gridRequest.setUseNative(false);
		gridRequest.getParams().put("param.workordernum", activity.getWorkOrderNumber());
		gridRequest.getParams().put("param.activity", activity.getActivityCode());
		gridRequest.getParams().put("param.jobseq", "0");
		GridRequestResult gridRequestResult = gridsService.executeQuery(context, gridRequest);


		LinkedList<WorkOrderActivityCheckList> checklists = new LinkedList<>();
		for (GridRequestRow row : gridRequestResult.getRows()) {
			checklists.add(getCheckList(context, row, activity));
		}

		return checklists.toArray(new WorkOrderActivityCheckList[]{});
	}


	private WorkOrderActivityCheckList getCheckList(InforContext context, GridRequestRow row, Activity activity) throws InforException {
		WorkOrderActivityCheckList checklist = new WorkOrderActivityCheckList();
		checklist.setWorkOrderCode(activity.getWorkOrderNumber());
		checklist.setActivityCode(activity.getActivityCode());
		checklist.setCheckListCode(getCellContent("checklistcode", row));
		//checklistTemp.setOccurrence(v_result.getString("ack_occurrence"));
		checklist.setSequence(getCellContent("checklistsequence", row));
		checklist.setEquipmentCode(getCellContent("equipment", row));
		checklist.setEquipmentDesc(getCellContent("equipmentdesc", row));
		checklist.setType(getCellContent("checklisttype", row));
		checklist.setColor(getCellContent("color", row));

		// FOLLOW-UP
		checklist.setFollowUp(decodeBoolean(getCellContent("followup", row)));


		// FOLLOW-UP WORK ORDER
		String followUpWorkOrderActivity = getCellContent("followupwoactivity", row);
		if (tools.getDataTypeTools().isNotEmpty(followUpWorkOrderActivity)) {
			// Remove the activity after the Work Order Number
			checklist.setFollowUpWorkOrder(followUpWorkOrderActivity.split("-")[0]);
		}

		// REQUIRED
		String required = getCellContent("requiredtoclosedocument", row);
		if ("Yes".equals(required)) {
			checklist.setRequiredToClose(true);
		} else {
			checklist.setRequiredToClose(false);
		}

		// NOTES
		checklist.setNotes(getCellContent("notes", row));

		//checklistTemp.setFinalOccurrence(v_result.getString("ack_finaloccurrence"));
		checklist.setDesc(getCellContent("checklistdescription", row));
		
		checklist.setHideFollowUp(cellEquals(row, "hidefollowup", "true"));
		//
		// VALUES FOR DIFFERENT CHECKLIST TYPES
		//

		switch(checklist.getType()) {
			case CheckListType.CHECKLIST_ITEM:
				if (cellEquals(row, "completed", "true")) {
					checklist.setResult(ReturnType.COMPLETED);
				} else {
					checklist.setResult(ReturnType.NULL);
				}
				break;
			case CheckListType.QUESTION_YES_NO:
				if (cellEquals(row, "yes", "true")) {
					checklist.setResult(ReturnType.YES);
				} else if (cellEquals(row, "no", "true")) {
					checklist.setResult(ReturnType.NO);
				} else {
					checklist.setResult(null);
				}
				break;
			case CheckListType.QUALITATIVE:
				checklist.setFinding(getCellContent("finding", row));
				checklist.setPossibleFindings(getPossibleFindings(context, row));
				break;
			case CheckListType.INSPECTION:
				checklist.setFinding(getCellContent("finding", row));
				checklist.setPossibleFindings(getPossibleFindings(context, row));
				// no break here, INSPECTION is the same as QUANTITATIVE/METER_READING,
				// but with findings and possible findings, so we will set the numeric value and UOM below
			case CheckListType.QUANTITATIVE:
			case CheckListType.METER_READING:
				checklist.setNumericValue(encodeBigDecimal(getCellContent("value", row), ""));
				checklist.setUOM(getCellContent("uom", row));

				// this is set for backward compatibility reasons, deprecated, do not use in new applications
				// TODO: update all applications to use the numeric value and remove this
				checklist.setResult(getCellContent("value", row));
				break;
			case CheckListType.GOOD_POOR:
				if (cellEquals(row, "good", "true")) {
					checklist.setResult(ReturnType.GOOD);
				} else if (cellEquals(row, "poor", "true")) {
					checklist.setResult(ReturnType.POOR);
				} else {
					checklist.setResult(ReturnType.NULL);
				}
				break;
			case CheckListType.NONCONFORMITY_MEASUREMENT:
				checklist.setNumericValue(encodeBigDecimal(getCellContent("value", row), ""));
				checklist.setUOM(getCellContent("uom", row));
				// no break here, NONCONFORMITY_MEASUREMENT is the same as NONCONFORMITY_CHECK,
				// but with a numberic value and UOM, so we will set the result to OK/NONCONFORMITY below
			case CheckListType.NONCONFORMITY_CHECK:
				if (cellEquals(row, "ok", "true")) {
					checklist.setResult(ReturnType.OK);
				} else if (cellEquals(row, "nonconformityfound", "true")) {
					checklist.setResult(ReturnType.NONCONFORMITY);
				} else {
					checklist.setResult(ReturnType.NULL);
				}
				break;
			case CheckListType.OK_ADJUSTED_MEASUREMENT:
				checklist.setNumericValue(encodeBigDecimal(getCellContent("value", row), ""));
				checklist.setUOM(getCellContent("uom", row));
				// no break here, OK_ADJUSTED_MEASUREMENT is the same as OK_ADJUSTED,
				// but with a numeric value and UOM, so we will set the result to OK/ADJUSTED below
			case CheckListType.OK_ADJUSTED:
				if (cellEquals(row, "ok", "true")) {
					checklist.setResult(ReturnType.OK);
				} else if(cellEquals(row,"adjusted", "true")) {
					checklist.setResult(ReturnType.ADJUSTED);
				} else {
					checklist.setResult(ReturnType.NULL);
				}
				break;
			case CheckListType.OK_REPAIR_NEEDED:
				checklist.setFinding(getCellContent("resolution", row));
				if (cellEquals(row, "ok", "true")) {
					checklist.setResult(ReturnType.OK);
				} else if(cellEquals(row,"repairsneeded", "true")) {
					checklist.setResult(ReturnType.REPAIRSNEEDED);
				} else {
					checklist.setResult(ReturnType.NULL);
				}
				break;
		}

		return checklist;
	}

	/**
	 * Fetches the list of Findings (code, desc) for GridRequestRow containing comma-delimited string of finding codes.
	 *
	 * @param context
	 * @param row
	 * @return
	 */
	private List<Finding> getPossibleFindings(InforContext context, GridRequestRow row) {
		List<String> possibleFindings = Arrays.asList(getCellContent("possiblefindings", row).split(","));

		for (String findingCode : possibleFindings) {
			if (!findingsCache.containsKey(findingCode)) {
				try {
					GridRequest gridRequest = new GridRequest("ISFIND", GridRequest.GRIDTYPE.LIST);
					gridRequest.addFilter("findingcode", findingCode, "=");
					findingsCache.put(findingCode, extractSingleResult(gridsService.executeQuery(context, gridRequest), "findingdesc"));
				}
				catch (Exception e) {
					tools.log(Level.WARNING, "Finding could not be fetched: " + e.getMessage());
				}
			}
		}

		return possibleFindings.stream()
				.map(findingCode -> new Finding(findingCode, findingsCache.containsKey(findingCode) ? findingsCache.get(findingCode) : findingCode))
				.collect(Collectors.toList());
	}

	private boolean cellEquals(GridRequestRow row, String key, String value) {
		return getCellContent(key, row) != null && getCellContent(key, row).equals(value);
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

		MP8000_CreateFollowUpWorkOrder_001_Result createFUWOResult =
			tools.performInforOperation(context, inforws::createFollowUpWorkOrderOp, createFUWO);
		return createFUWOResult.getResultData().getWORKORDERCOUNT();
	}
}
