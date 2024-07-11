package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.entities.Pair;
import ch.cern.eam.wshub.core.services.entities.Signature;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestFilter;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestRow;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.ChecklistService;
import ch.cern.eam.wshub.core.services.workorders.entities.*;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderActivityCheckList.CheckListType;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderActivityCheckList.ReturnType;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;
import static ch.cern.eam.wshub.core.tools.GridTools.*;

public class ChecklistServiceImpl implements ChecklistService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;
	private GridsService gridsService;
	public static final Map<String, String> findingsCache = new ConcurrentHashMap<>();

	public ChecklistServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
		this.gridsService = new GridsServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
	}

	public WorkOrderActivityChecklistSignatureResult[] getSignatures(EAMContext context, String workOrderCode,
		String activityCode, TaskPlan taskPlan) throws EAMException {

		MP7999_GetWorkOrderActivityCheckListDefault_001_Result getResult = getSignatureWS(context, workOrderCode, activityCode);
		WorkOrderActivityCheckListDefaultResult workOrderActivityCheckListDefaultResult = new WorkOrderActivityCheckListDefaultResult();
		tools.getEAMFieldTools().transformEAMObject(workOrderActivityCheckListDefaultResult,
				getResult.getResultData().getWorkOrderActivityCheckListDefault(), context);

		WorkOrderActivityChecklistSignatureResult[] res = filterSignatures(workOrderActivityCheckListDefaultResult, taskPlan);
		getResponsibilityDescriptions(context, res);
		return res;
	}

	private void getResponsibilityDescriptions(EAMContext context, WorkOrderActivityChecklistSignatureResult[] signatures)
			throws EAMException {

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
		Map<String, String> responsibilityToDescription =
				convertGridResultToMap("responsibility", "description", gridsService.executeQuery(context, gridRequest));

		Arrays.stream(signatures).forEach(signature -> {
			if(responsibilityToDescription.containsKey(signature.getResponsibilityCode()))
				signature.setResponsibilityDescription(responsibilityToDescription.get(signature.getResponsibilityCode()));
		});
	}

	private WorkOrderActivityChecklistSignatureResult[] filterSignatures(WorkOrderActivityCheckListDefaultResult workOrderActivityCheckList, TaskPlan taskPlan){
		String reviewerQualification = workOrderActivityCheckList.getReviewerQualification();
		String performer1Qualification = workOrderActivityCheckList.getPerformer1Qualification();
		String performer2Qualification = workOrderActivityCheckList.getPerformer2Qualification();
		String viewerQualification =  taskPlan.getViewOnlyResponsibility();
		List<String> qualifications = new ArrayList<>();

		if(workOrderActivityCheckList.getUserQualifications() != null){
			qualifications = workOrderActivityCheckList.getUserQualifications().stream()
					.map(UserQualification::getUserDefinedCode)
					.collect(Collectors.toList());
		}

		boolean noRequiredQualifications = reviewerQualification == null &&
				                           performer1Qualification == null &&
				                           performer2Qualification == null &&
										   viewerQualification == null;

		boolean isViewer = noRequiredQualifications
			|| viewerQualification != null && qualifications.contains(viewerQualification);

		boolean isPerformer1 = noRequiredQualifications || performer1Qualification == null && isViewer
							   || qualifications.contains(performer1Qualification);

		boolean isPerformer2 = noRequiredQualifications || performer2Qualification == null && isPerformer1
				               || qualifications.contains(performer2Qualification);

		boolean isReviewer = noRequiredQualifications || reviewerQualification == null && (isPerformer1 || isPerformer2)
							 || qualifications.contains(reviewerQualification);


		List<WorkOrderActivityChecklistSignatureResult> signatures = new LinkedList<>();

		if (isPerformer1 || isReviewer || isViewer) {
			WorkOrderActivityChecklistSignatureResult perf1 = new WorkOrderActivityChecklistSignatureResult();
			perf1.setType("PB01");
			perf1.setSigner(workOrderActivityCheckList.getPerformer1Name());
			perf1.setViewAsViewer(isViewer);
			perf1.setViewAsPerformer(isPerformer1);
			perf1.setViewAsReviewer(isReviewer);
			perf1.setTime(workOrderActivityCheckList.getTimePerf1() != null ? workOrderActivityCheckList.getTimePerf1() : null);
			perf1.setResponsibilityCode(performer1Qualification);
			signatures.add(perf1);
		}

		// TODO: refactor the check below to move this CERN-specific logic to EAM Light
		boolean performedBy2Hidden = taskPlan.getUserDefinedFields().getUdfchkbox02();
		if (!performedBy2Hidden && (isPerformer2 || isReviewer || isViewer)) {
			WorkOrderActivityChecklistSignatureResult perf2 = new WorkOrderActivityChecklistSignatureResult();
			perf2.setType("PB02");
			perf2.setSigner(workOrderActivityCheckList.getPerformer2Name());
			perf2.setViewAsViewer(isViewer);
			perf2.setViewAsPerformer(isPerformer2);
			perf2.setViewAsReviewer(isReviewer);
			perf2.setTime(workOrderActivityCheckList.getTimePerf2() != null ?
					workOrderActivityCheckList.getTimePerf2() : null);
			perf2.setResponsibilityCode(performer2Qualification);
			signatures.add(perf2);
		}

		if (isPerformer1 || isPerformer2 || isReviewer || isViewer) {
			WorkOrderActivityChecklistSignatureResult reviewer = new WorkOrderActivityChecklistSignatureResult();
			reviewer.setType("RB01");
			reviewer.setSigner(workOrderActivityCheckList.getReviewerName());
			reviewer.setViewAsViewer(isViewer);
			reviewer.setViewAsPerformer(isReviewer);
			reviewer.setViewAsReviewer(isReviewer);
			reviewer.setTime(workOrderActivityCheckList.getTimeRev1() != null ? workOrderActivityCheckList.getTimeRev1() : null);
			if(reviewerQualification == null && performer1Qualification != null && performer2Qualification == null)
				reviewerQualification = performer1Qualification;


			reviewer.setResponsibilityCode(reviewerQualification);
			signatures.add(reviewer);
		}

		return signatures.toArray(new WorkOrderActivityChecklistSignatureResult[0]);
	}

	private MP7999_GetWorkOrderActivityCheckListDefault_001_Result getSignatureWS(EAMContext context, String workOrderCode, String activityCode) throws EAMException {
		MP7999_GetWorkOrderActivityCheckListDefault_001 getWorkOrderActivityCheckListDefault = new MP7999_GetWorkOrderActivityCheckListDefault_001();
		transformGetWorkOrderActivityCheckListDefaultRequest(getWorkOrderActivityCheckListDefault, workOrderCode, activityCode);
		return tools.performEAMOperation(context, eamws::getWorkOrderActivityCheckListDefaultOp, getWorkOrderActivityCheckListDefault);
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

	public WorkOrderActivityChecklistSignatureResponse eSignWorkOrderActivityChecklist(EAMContext context, WorkOrderActivityCheckListSignature workOrderActivityCheckListSignature)
			throws EAMException{
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

	private WorkOrderActivityChecklistSignatureResponse performWorkOrderActivityChecklist(EAMContext context, WorkOrderActivityCheckListSignature workOrderActivityCheckListSignature)
			throws EAMException {

		MP7997_PerformWorkOrderActivityCheckList_001 performWorkOrderActivityCheckList = new MP7997_PerformWorkOrderActivityCheckList_001();

		tools.getEAMFieldTools().transformWSHubObject(performWorkOrderActivityCheckList, workOrderActivityCheckListSignature, context);
		tools.performEAMOperation(context, eamws::performWorkOrderActivityCheckListOp, performWorkOrderActivityCheckList);

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

	private WorkOrderActivityChecklistSignatureResponse reviewWorkOrderActivityCheckList(EAMContext context, WorkOrderActivityCheckListSignature workOrderActivityCheckListSignature)
			throws EAMException {

		MP7998_ReviewWorkOrderActivityCheckList_001 reviewWorkOrderActivityCheckList = new MP7998_ReviewWorkOrderActivityCheckList_001();

		tools.getEAMFieldTools().transformWSHubObject(reviewWorkOrderActivityCheckList, workOrderActivityCheckListSignature, context);
		tools.performEAMOperation(context, eamws::reviewWorkOrderActivityCheckListOp, reviewWorkOrderActivityCheckList);

		MP7999_GetWorkOrderActivityCheckListDefault_001_Result getResult =
				getSignatureWS(context, workOrderActivityCheckListSignature.getWorkOrderCode(),
						       workOrderActivityCheckListSignature.getActivityCodeValue().toString());

		return transformESIGNATUREtoResponse(getResult.getResultData().getWorkOrderActivityCheckListDefault().getREVIEWEDBYESIGN().getESIGNATURE());
	}

	private WorkOrderActivityChecklistSignatureResponse transformESIGNATUREtoResponse(ESIGNATURE eSignature) {
		WorkOrderActivityChecklistSignatureResponse response = new WorkOrderActivityChecklistSignatureResponse();
		response.setSigner(eSignature.getUSERID().getDESCRIPTION());
		response.setTimeStamp(decodeEAMDate(eSignature.getEXTERNALDATETIME()));
		return response;
	}

	public String updateWorkOrderChecklist(EAMContext context, WorkOrderActivityCheckList workOrderActivityCheckList) throws EAMException {
		//
		// Fetch it first
		//
		MP7914_GetWorkOrderActivityCheckList_001 getwoactchl = new MP7914_GetWorkOrderActivityCheckList_001();
		getwoactchl.setCHECKLISTCODE(workOrderActivityCheckList.getCheckListCode());
		MP7914_GetWorkOrderActivityCheckList_001_Result getresult =
			tools.performEAMOperation(context, eamws::getWorkOrderActivityCheckListOp, getwoactchl);

		//
		// Sync afterwards
		//
		net.datastream.schemas.mp_entities.workorderactivitychecklist_001.WorkOrderActivityCheckList workOrderActivityCheckListEAM = getresult
				.getResultData().getWorkOrderActivityCheckList();

		// Follow Up
		if (workOrderActivityCheckList.getFollowUp() != null) {
			workOrderActivityCheckListEAM.setFOLLOWUP(tools.getDataTypeTools().encodeBoolean(workOrderActivityCheckList.getFollowUp(), BooleanType.PLUS_MINUS));
		}

		if (workOrderActivityCheckList.getNotApplicableOption() != null) {
			USERDEFINEDCODEID_Type option = new USERDEFINEDCODEID_Type();
			option.setUSERDEFINEDCODE(workOrderActivityCheckList.getNotApplicableOption());
			workOrderActivityCheckListEAM.setNOTAPPLICABLEOPTION(option);
		}

		Function<String, String> getStringBool =
			key -> String.valueOf(key.equals(workOrderActivityCheckList.getResult()));

		switch (workOrderActivityCheckList.getType()) {
			case CheckListType.CHECKLIST_ITEM:
				if (ReturnType.COMPLETED.equalsIgnoreCase(workOrderActivityCheckList.getResult())) {
					workOrderActivityCheckListEAM.setCOMPLETED("true");
				} else {
					workOrderActivityCheckListEAM.setCOMPLETED("false");
				}
				break;
			case CheckListType.QUESTION_YES_NO:
				workOrderActivityCheckListEAM.setYES(getStringBool.apply(ReturnType.YES));
				workOrderActivityCheckListEAM.setNO(getStringBool.apply(ReturnType.NO));
				break;
			case CheckListType.QUALITATIVE:
				if (workOrderActivityCheckList.getFinding() != null) {
					workOrderActivityCheckListEAM.setFINDINGID(new FINDINGID_Type());
					workOrderActivityCheckListEAM.getFINDINGID().setFINDINGCODE(workOrderActivityCheckList.getFinding());
				} else {
					workOrderActivityCheckListEAM.setFINDINGID(null);
				}
				break;
			case CheckListType.INSPECTION:
				if (workOrderActivityCheckList.getFinding() != null) {
					workOrderActivityCheckListEAM.setFINDINGID(new FINDINGID_Type());
					workOrderActivityCheckListEAM.getFINDINGID().setFINDINGCODE(workOrderActivityCheckList.getFinding());
				} else {
					workOrderActivityCheckListEAM.setFINDINGID(null);
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

				workOrderActivityCheckListEAM
						.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(numericValue, "Checklists Value"));
				break;
			case CheckListType.OK_REPAIR_NEEDED:
				workOrderActivityCheckListEAM.setOKFLAG(getStringBool.apply(ReturnType.OK));
				workOrderActivityCheckListEAM.setREPAIRSNEEDED(getStringBool.apply(ReturnType.REPAIRSNEEDED));

				if(isEmpty(workOrderActivityCheckList.getFinding())) {
					workOrderActivityCheckListEAM.setRESOLUTIONID(null);
				} else {
					workOrderActivityCheckListEAM.setRESOLUTIONID(new USERDEFINEDCODEID_Type());
					workOrderActivityCheckListEAM.getRESOLUTIONID().setUSERDEFINEDCODE(workOrderActivityCheckList.getFinding());
				}
				break;
			case CheckListType.GOOD_POOR:
				workOrderActivityCheckListEAM.setGOOD(getStringBool.apply(ReturnType.GOOD));
				workOrderActivityCheckListEAM.setPOOR(getStringBool.apply(ReturnType.POOR));
				break;
			case CheckListType.OK_ADJUSTED_MEASUREMENT:
				workOrderActivityCheckListEAM
					.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(workOrderActivityCheckList.getNumericValue(), "Checklists Value"));
				// no break here, OK_ADJUSTED_MEASUREMENT is the same as OK_ADJUSTED,
				// but with a numeric value, so we will set the result to OK/ADJUSTED below
			case CheckListType.OK_ADJUSTED:
				workOrderActivityCheckListEAM.setOKFLAG(getStringBool.apply(ReturnType.OK));
				workOrderActivityCheckListEAM.setADJUSTED(getStringBool.apply(ReturnType.ADJUSTED));
				break;
			case CheckListType.NONCONFORMITY_MEASUREMENT:
				workOrderActivityCheckListEAM
					.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(workOrderActivityCheckList.getNumericValue(), "Checklists Value"));
				// no break here, NONCONFORMITY_MEASUREMENT is the same as NONCONFORMITY_CHECK,
				// but with a numberic value, so we will set the result to OK/NONCONFORMITY below
			case CheckListType.NONCONFORMITY_CHECK:
				workOrderActivityCheckListEAM.setOKFLAG(getStringBool.apply(ReturnType.OK));
				workOrderActivityCheckListEAM.setNONCONFORMITYFLAG(getStringBool.apply(ReturnType.NONCONFORMITY));
				break;
			case CheckListType.DATE:
				workOrderActivityCheckListEAM.setCHECKLISTDATE(tools.getDataTypeTools().encodeEAMDate(workOrderActivityCheckList.getDate(), ""));
				break;
			case CheckListType.DATETIME:
				workOrderActivityCheckListEAM.setCHECKLISTDATETIME(tools.getDataTypeTools().encodeEAMDate(workOrderActivityCheckList.getDateTime(), ""));
				break;
			case CheckListType.FREE_TEXT:
				workOrderActivityCheckListEAM.setCHECKLISTFREETEXT(workOrderActivityCheckList.getFreeText());
				break;
			case CheckListType.ENTITY:
				if (isEmpty(workOrderActivityCheckList.getEntityCode())) {
					workOrderActivityCheckListEAM.setENTITYCODEID(null);
					break;
				}
				workOrderActivityCheckListEAM.setENTITYCODEID(new ENTITYCODEID_Type());
				workOrderActivityCheckListEAM.getENTITYCODEID().setCODE(workOrderActivityCheckList.getEntityCode());
				ORGANIZATIONID_Type organizationidType = new ORGANIZATIONID_Type();
				organizationidType.setORGANIZATIONCODE(isEmpty(workOrderActivityCheckList.getEntityCodeOrg()) ? tools.getOrganizationCode(context) : workOrderActivityCheckList.getEntityCodeOrg());
				workOrderActivityCheckListEAM.getENTITYCODEID().setORGANIZATIONID(organizationidType);
				break;
			case CheckListType.DUAL_QUANTITATIVE:
				workOrderActivityCheckListEAM.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(workOrderActivityCheckList.getNumericValue(), "Checklists Value"));
				workOrderActivityCheckListEAM.setRESULTVALUE2(tools.getDataTypeTools().encodeQuantity(workOrderActivityCheckList.getNumericValue2(), "Checklists Value"));
				break;
		}

		if (workOrderActivityCheckList.getNotes() != null) {
			workOrderActivityCheckListEAM.setNOTES(workOrderActivityCheckList.getNotes());
		}

		MP7913_SyncWorkOrderActivityCheckList_001 syncwoactchl = new MP7913_SyncWorkOrderActivityCheckList_001();
		syncwoactchl.setWorkOrderActivityCheckList(workOrderActivityCheckListEAM);

		tools.performEAMOperation(context, eamws::syncWorkOrderActivityCheckListOp, syncwoactchl);
		return null;
	}

	public String createTaskplanChecklist(EAMContext context, TaskplanCheckList taskChecklist) throws EAMException {
		TaskChecklist taskChecklistEAM = new TaskChecklist();
		//
		// TASK LIST ID
		//
		taskChecklistEAM.setTASKLISTID(new TASKLISTID_Type());
		taskChecklistEAM.getTASKLISTID().setORGANIZATIONID(tools.getOrganization(context));
		taskChecklistEAM.getTASKLISTID().setTASKCODE(taskChecklist.getTaskPlanCode());
		if (taskChecklist.getTaskPlanRevision() == null) {
			taskChecklistEAM.getTASKLISTID().setTASKREVISION(0L);
		} else {
			taskChecklistEAM.getTASKLISTID()
					.setTASKREVISION(tools.getDataTypeTools().encodeLong(taskChecklist.getTaskPlanRevision(), "Task Revision"));
		}
		//
		// DESCRIPTION
		//
		taskChecklistEAM.setCHECKLISTID(new CHECKLISTID_Type());
		taskChecklistEAM.getCHECKLISTID().setCHECKLISTCODE("0");
		taskChecklistEAM.getCHECKLISTID().setDESCRIPTION(taskChecklist.getChecklistDesc());
		//
		// SEQUENCE
		//
		taskChecklistEAM.setSEQUENCE(tools.getDataTypeTools().encodeLong(taskChecklist.getSequence(), "Sequence number"));
		//
		// TYPE
		//
		taskChecklistEAM.setTYPE(new TYPE_Type());
		taskChecklistEAM.getTYPE().setTYPECODE(taskChecklist.getType());
		//
		// REQUIRED ENTRY
		//
		taskChecklistEAM.setREQUIREDTOCLOSEDOC(new USERDEFINEDCODEID_Type());
		taskChecklistEAM.getREQUIREDTOCLOSEDOC().setUSERDEFINEDCODE(taskChecklist.getRequiredEntry());
		//
		// EQUIPMENT LEVEL
		//
		taskChecklistEAM.setEQUIPMENTLEVEL(new USERDEFINEDCODEID_Type());
		taskChecklistEAM.getEQUIPMENTLEVEL().setUSERDEFINEDCODE(taskChecklist.getEquipmentLevel());
		//
		// POSSIBLE FINDINGS
		//
		if (taskChecklist.getFindings() != null) {
			taskChecklistEAM.setPOSSIBLEFINDINGS(taskChecklist.getFindings());
		}
		//
		// UOM
		//
		if (taskChecklist.getUOM() != null) {
			taskChecklistEAM.setUOMID(new UOMID_Type());
			taskChecklistEAM.getUOMID().setUOMCODE(taskChecklist.getUOM());
		}
		//
		// ASPECT ID
		//
		if (taskChecklist.getAspectCode() != null) {
			taskChecklistEAM.setASPECTID(new ASPECTID_Type());
			taskChecklistEAM.getASPECTID().setASPECTCODE(taskChecklist.getAspectCode());
		}
		//
		// POINT TYPE ID
		//
		if (taskChecklist.getPointType() != null) {
			taskChecklistEAM.setPOINTTYPEID(new POINTTYPEID_Type());
			taskChecklistEAM.getPOINTTYPEID().setPOINTTYPECODE(taskChecklist.getPointType());
		}
		//
		// REPEATING OCCURRENCES
		//
		taskChecklistEAM.setREPEATINGOCCURRENCES(taskChecklist.getRepeatingOccurrences());
		//
		// FOLLOW-UP TASK PLAN
		//
		if (taskChecklist.getFollowUpTaskPlan() != null) {
			taskChecklistEAM.setFOLLOWUPTASKID(new TASKLISTID_Type());
			taskChecklistEAM.getFOLLOWUPTASKID().setORGANIZATIONID(tools.getOrganization(context));
			taskChecklistEAM.getFOLLOWUPTASKID().setTASKCODE(taskChecklist.getFollowUpTaskPlan());
			if (taskChecklist.getFollowUpTaskPlanRevision() != null) {
				taskChecklistEAM.getFOLLOWUPTASKID().setTASKREVISION(
						tools.getDataTypeTools().encodeLong(taskChecklist.getFollowUpTaskPlanRevision(), "Follow Up Task Plan Revision"));
			} else {
				taskChecklistEAM.getFOLLOWUPTASKID().setTASKREVISION(0L);
			}
		}
		//
		// CLASS
		//
		if (taskChecklist.getClassCode() != null && !taskChecklist.getClassCode().trim().equals("")) {
			taskChecklistEAM.setCLASSID(new CLASSID_Type());
			taskChecklistEAM.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
			taskChecklistEAM.getCLASSID().setCLASSCODE(taskChecklist.getClassCode());
		}
		//
		// CATEGORY
		//
		if (taskChecklist.getCategoryCode() != null && !taskChecklist.getCategoryCode().trim().equals("")) {
			taskChecklistEAM.setCATEGORYID(new CATEGORYID());
			taskChecklistEAM.getCATEGORYID().setCATEGORYCODE(taskChecklist.getCategoryCode());
		}

		//
		// EQUIPMENT FILTER
		//
		if (isNotEmpty(taskChecklist.getEquipmentFilter())) {
			taskChecklistEAM.setEQUIPMENTFILTER(taskChecklist.getEquipmentFilter());
		}

		MP7916_AddTaskChecklist_001 addTaskChecklist = new MP7916_AddTaskChecklist_001();
		addTaskChecklist.setTaskChecklist(taskChecklistEAM);

		tools.performEAMOperation(context, eamws::addTaskChecklistOp, addTaskChecklist);
		return "OK";
	}

	public WorkOrderActivityCheckList[] readWorkOrderChecklists(EAMContext context, Activity activity) throws EAMException {
		// Fetch the data
		GridRequest gridRequest = new GridRequest("WSJOBS_ACK");
		gridRequest.setRowCount(2000);
		gridRequest.setUseNative(true);
		gridRequest.setUserFunctionName("WSJOBS");
		gridRequest.getParams().put("param.workordernum", activity.getWorkOrderNumber());
		gridRequest.getParams().put("param.activity", activity.getActivityCode().toString());
		gridRequest.getParams().put("param.jobseq", "0");
		GridRequestResult gridRequestResult = gridsService.executeQuery(context, gridRequest);

		LinkedList<WorkOrderActivityCheckList> checklists = new LinkedList<>();
		for (GridRequestRow row : gridRequestResult.getRows()) {
			checklists.add(getCheckList(context, row, activity));
		}

		return checklists.toArray(new WorkOrderActivityCheckList[]{});
	}


	private WorkOrderActivityCheckList getCheckList(EAMContext context, GridRequestRow row, Activity activity) throws EAMException {
		WorkOrderActivityCheckList checklist = new WorkOrderActivityCheckList();
		checklist.setWorkOrderCode(activity.getWorkOrderNumber());
		checklist.setActivityCode(activity.getActivityCode().toString());
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
		if ("Yes".equalsIgnoreCase(required)) {
			checklist.setRequiredToClose(true);
		} else {
			checklist.setRequiredToClose(false);
		}

		checklist.setConditional("X".equalsIgnoreCase(getCellContent("conditionpk", row)));

		// NOTES
		checklist.setNotes(getCellContent("notes", row));

		//checklistTemp.setFinalOccurrence(v_result.getString("ack_finaloccurrence"));
		checklist.setDesc(getCellContent("checklistdescription", row));
		
		checklist.setHideFollowUp(cellEquals(row, "hidefollowup", "true"));

		checklist.setMinimumValue(encodeBigInteger(
				getCellContent("minimumslidervalue", row), "minimumslidervalue"));

		checklist.setMaximumValue(encodeBigInteger(
				getCellContent("maximumslidervalue", row), "maximumslidervalue"));
		checklist.setMinimumValue2(encodeBigInteger(
				getCellContent("minimumslidervalue2", row), "minimumslidervalue2"));

		checklist.setMaximumValue2(encodeBigInteger(
				getCellContent("maximumslidervalue2", row), "maximumslidervalue2"));

		checklist.setNotApplicableOption(getCellContent("notapplicable", row));

		checklist.setChecklistDefinitionCode(getCellContent("taskchecklistcode", row));

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
			case CheckListType.DATE:
				checklist.setDate(tools.getDataTypeTools().convertStringToDate(getCellContent("checklistdate", row)));
				break;
			case CheckListType.DATETIME:
				checklist.setDateTime(tools.getDataTypeTools().convertStringToDate(getCellContent("checklistdatetime", row)));
				break;
			case CheckListType.FREE_TEXT:
				checklist.setFreeText(getCellContent("checklistfreetext", row));
				break;
			case CheckListType.ENTITY:
				checklist.setEntityCode(getCellContent("entitycode", row));
				checklist.setEntityCodeOrg(getCellContent("entitycodeorg", row));
				checklist.setEntityType(getCellContent("rentitycode", row));
				checklist.setEntityClass(getCellContent("entityclassoptions", row));
				break;
			case CheckListType.DUAL_QUANTITATIVE:
				checklist.setNumericValue(encodeBigDecimal(getCellContent("value", row), ""));
				checklist.setNumericValue2(encodeBigDecimal(getCellContent("value2", row), ""));
				checklist.setUOM(getCellContent("uom", row));
				checklist.setUOM2(getCellContent("uom2", row));
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
	private List<Finding> getPossibleFindings(EAMContext context, GridRequestRow row) {
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
	 * @throws EAMException
	 */

	public Long createFollowUpWorkOrders(EAMContext context, Activity activity) throws EAMException {
		MP8000_CreateFollowUpWorkOrder_001 createFUWO = new MP8000_CreateFollowUpWorkOrder_001();

		createFUWO.setACTIVITYID(new ACTIVITYID());
		createFUWO.getACTIVITYID().setWORKORDERID(new WOID_Type());
		createFUWO.getACTIVITYID().getWORKORDERID().setJOBNUM(activity.getWorkOrderNumber());
		createFUWO.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
		createFUWO.getACTIVITYID().getACTIVITYCODE().setValue(activity.getActivityCode().longValue());
		createFUWO.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));

		MP8000_CreateFollowUpWorkOrder_001_Result createFUWOResult =
			tools.performEAMOperation(context, eamws::createFollowUpWorkOrderOp, createFUWO);
		return createFUWOResult.getResultData().getWORKORDERCOUNT();
	}

	public WorkOrderActivityCheckListDefinition getChecklistDefinition(EAMContext context, TaskPlan taskPlan, String code) throws EAMException {
		GridRequest gridRequest = new GridRequest("WSTASK_TCH", 1);
		gridRequest.addParam("param.task", taskPlan.getCode());
		gridRequest.addParam("param.revision", taskPlan.getTaskRevision() == null ? null : taskPlan.getTaskRevision().toString());

		gridRequest.addFilter("checklistitem", code, "EQUALS");

		GridRequestResult result = gridsService.executeQuery(context, gridRequest);

		WorkOrderActivityCheckListDefinition definition = convertGridResultToObject(
				WorkOrderActivityCheckListDefinition.class, null, result).stream().findFirst().orElse(null);

		String notApplicableOptionsString = extractSingleResult(result, "naoptions");

		// optimization to not call the grid request below
		if (notApplicableOptionsString == null || "".equals(notApplicableOptionsString)) {
			return definition;
		}

		GridRequest notApplicableOptionsRequest = new GridRequest("LVNAOPTIONS", 2000);
		Map<String, String> notApplicableOptionsMap =
			convertGridResultToMap("code", "description", gridsService.executeQuery(context, notApplicableOptionsRequest));

		List<Pair> notApplicableOptions = Arrays.stream(notApplicableOptionsString.split(","))
				.map(optionCode -> new Pair(optionCode, notApplicableOptionsMap.get(optionCode)))
				.collect(Collectors.toList());

		definition.setNotApplicableOptions(notApplicableOptions);

		return definition;
	}
}
