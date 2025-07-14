package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.Pair;
import ch.cern.eam.wshub.core.services.entities.Signature;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestFilter;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestRow;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.ChecklistService;
import ch.cern.eam.wshub.core.services.workorders.TaskPlanService;
import ch.cern.eam.wshub.core.services.workorders.entities.*;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderActivityChecklistItem.CheckListType;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderActivityChecklistItem.ReturnType;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.CacheKey;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.taskchecklist_001.TaskChecklist;
import net.datastream.schemas.mp_entities.workorderactivitychecklistdefault_001.WorkOrderActivityCheckListDefault;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp5782_001.MP5782_UpdateWorkOrderActivityCheckList_001;
import net.datastream.schemas.mp_functions.mp5782_001.WorkOrderActivityDetail;
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
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;
import static ch.cern.eam.wshub.core.tools.GridTools.*;

public class ChecklistServiceImpl implements ChecklistService {

	private static final String PERFORMED_BY_1 = "PB01";
	private static final String PERFORMED_BY_2 = "PB02";
	private static final String REVIEWED_BY_1 = "RB01";

	private final Tools tools;
	private final InforWebServicesPT inforws;
	private final GridsService gridsService;
	private final TaskPlanService taskPlanService;

	public ChecklistServiceImpl(
			ApplicationData applicationData,
			Tools tools,
			InforWebServicesPT inforWebServicesToolkitClient
	) {
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
		this.taskPlanService = new TaskPlanServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
	}

	@Override
	public WorkOrderActivityChecklistSignatureResult[] getSignatures(
			InforContext context,
			String workOrderCode,
			String activityCode,
			TaskPlan taskPlan
	) throws InforException {

		MP7999_GetWorkOrderActivityCheckListDefault_001_Result fetchedChecklistValues = getChecklistDefaults(
				context,
				workOrderCode,
				activityCode
		);
		WorkOrderActivityChecklistSignatureResult[] signatures = getFilteredSignatures(
				context,
				fetchedChecklistValues,
				taskPlan
		);
		getResponsibilityDescriptions(context, signatures);

		return signatures;
	}

	private WorkOrderActivityChecklistSignatureResult[] getFilteredSignatures(
			InforContext context,
			MP7999_GetWorkOrderActivityCheckListDefault_001_Result fetchedChecklistValues,
			TaskPlan taskPlan
	) {

		WorkOrderActivityChecklistDefaultResult workOrderActivityCheckListDefaultResult = new WorkOrderActivityChecklistDefaultResult();
		tools.getInforFieldTools().transformInforObject(
				workOrderActivityCheckListDefaultResult,
				fetchedChecklistValues.getResultData().getWorkOrderActivityCheckListDefault(),
				context
		);

		return filterSignatures(workOrderActivityCheckListDefaultResult, taskPlan);
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
		if (filters.isEmpty()) return;
		gridRequest.setGridRequestFilters(filters);
		Map<String, String> responsibilityToDescription =
				convertGridResultToMap("responsibility", "description", gridsService.executeQuery(context, gridRequest));

		Arrays.stream(signatures).forEach(signature -> {
			if(responsibilityToDescription.containsKey(signature.getResponsibilityCode()))
				signature.setResponsibilityDescription(responsibilityToDescription.get(signature.getResponsibilityCode()));
		});
	}

	private WorkOrderActivityChecklistSignatureResult[] filterSignatures(WorkOrderActivityChecklistDefaultResult workOrderActivityCheckList, TaskPlan taskPlan){
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
			perf1.setType(ChecklistServiceImpl.PERFORMED_BY_1);
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
			perf2.setType(PERFORMED_BY_2);
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
			reviewer.setType(REVIEWED_BY_1);
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

	private MP7999_GetWorkOrderActivityCheckListDefault_001_Result getChecklistDefaults(
			InforContext context,
			String workOrderCode,
			String activityCode
	) throws InforException {

		MP7999_GetWorkOrderActivityCheckListDefault_001 getWorkOrderActivityCheckListDefault = new MP7999_GetWorkOrderActivityCheckListDefault_001();
		transformGetWorkOrderActivityCheckListDefaultRequest(
				getWorkOrderActivityCheckListDefault,
				workOrderCode,
				activityCode
		);
		return tools.performInforOperation(
				context,
				inforws::getWorkOrderActivityCheckListDefaultOp,
				getWorkOrderActivityCheckListDefault
		);
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

	@Override
	public WorkOrderActivityChecklistSignatureResponse eSignWorkOrderActivityChecklist(InforContext context, WorkOrderActivityChecklistSignature workOrderActivityCheckListSignature)
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

	private WorkOrderActivityChecklistSignatureResponse performWorkOrderActivityChecklist(InforContext context, WorkOrderActivityChecklistSignature workOrderActivityCheckListSignature)
			throws InforException {

		MP7997_PerformWorkOrderActivityCheckList_001 performWorkOrderActivityCheckList = new MP7997_PerformWorkOrderActivityCheckList_001();

		tools.getInforFieldTools().transformWSHubObject(performWorkOrderActivityCheckList, workOrderActivityCheckListSignature, context);
		tools.performInforOperation(context, inforws::performWorkOrderActivityCheckListOp, performWorkOrderActivityCheckList);

		MP7999_GetWorkOrderActivityCheckListDefault_001_Result getResult =
				getChecklistDefaults(context, workOrderActivityCheckListSignature.getWorkOrderCode(),
						       workOrderActivityCheckListSignature.getActivityCodeValue().toString());
		if(workOrderActivityCheckListSignature.getSequenceNumber() != null &&
				workOrderActivityCheckListSignature.getSequenceNumber().intValue() == 2) {
			return transformESIGNATUREtoResponse(getResult.getResultData().getWorkOrderActivityCheckListDefault().getPERFORMEDBYESIGN2().getESIGNATURE());
		} else {
			return transformESIGNATUREtoResponse(getResult.getResultData().getWorkOrderActivityCheckListDefault().getPERFORMEDBYESIGN().getESIGNATURE());
		}
	}

	private WorkOrderActivityChecklistSignatureResponse reviewWorkOrderActivityCheckList(InforContext context, WorkOrderActivityChecklistSignature workOrderActivityCheckListSignature)
			throws InforException {

		MP7998_ReviewWorkOrderActivityCheckList_001 reviewWorkOrderActivityCheckList = new MP7998_ReviewWorkOrderActivityCheckList_001();

		tools.getInforFieldTools().transformWSHubObject(reviewWorkOrderActivityCheckList, workOrderActivityCheckListSignature, context);
		tools.performInforOperation(context, inforws::reviewWorkOrderActivityCheckListOp, reviewWorkOrderActivityCheckList);

		MP7999_GetWorkOrderActivityCheckListDefault_001_Result getResult =
				getChecklistDefaults(context, workOrderActivityCheckListSignature.getWorkOrderCode(),
						       workOrderActivityCheckListSignature.getActivityCodeValue().toString());

		return transformESIGNATUREtoResponse(getResult.getResultData().getWorkOrderActivityCheckListDefault().getREVIEWEDBYESIGN().getESIGNATURE());
	}

	private WorkOrderActivityChecklistSignatureResponse transformESIGNATUREtoResponse(ESIGNATURE eSignature) {
		WorkOrderActivityChecklistSignatureResponse response = new WorkOrderActivityChecklistSignatureResponse();
		response.setSigner(eSignature.getUSERID().getDESCRIPTION());
		response.setTimeStamp(decodeInforDate(eSignature.getEXTERNALDATETIME()));
		return response;
	}

	@Override
	public String updateWorkOrderActivityCheckList(
			InforContext context,
			WorkOrderActivityChecklist workOrderActivityChecklist,
			boolean shouldMergeExistingValues
	) throws InforException {

		if (shouldMergeExistingValues) {
			MP7999_GetWorkOrderActivityCheckListDefault_001_Result fetchedExistingValues = getChecklistDefaults(
					context,
					workOrderActivityChecklist.getWorkOrderCode(),
					String.valueOf(workOrderActivityChecklist.getActivityCode())
			);
			mergeChecklistValues(workOrderActivityChecklist, fetchedExistingValues);
		}

		MP5782_UpdateWorkOrderActivityCheckList_001 updateCheckList = new MP5782_UpdateWorkOrderActivityCheckList_001();
		updateCheckList.setWorkOrderActivityDetail(tools.getInforFieldTools().transformWSHubObject(
				new WorkOrderActivityDetail(),
				workOrderActivityChecklist,
				context
		));
		tools.performInforOperation(context, inforws::updateWorkOrderActivityCheckListOp, updateCheckList);

		return "OK";
	}

	private void mergeChecklistValues(
			WorkOrderActivityChecklist workOrderActivityChecklist,
			MP7999_GetWorkOrderActivityCheckListDefault_001_Result fetchedValues
	) {

		WorkOrderActivityCheckListDefault existingValues = fetchedValues
				.getResultData()
				.getWorkOrderActivityCheckListDefault();

		if (workOrderActivityChecklist.getJobSequence() == null) {
			workOrderActivityChecklist.setJobSequence(existingValues.getJOBSEQUENCE());
		}
		if (workOrderActivityChecklist.getRejectPerformedBy() == null) {
			workOrderActivityChecklist.setRejectPerformedBy(
					Boolean.parseBoolean(existingValues.getREJECTPERFORMEDBY())
			);
		}
		if (workOrderActivityChecklist.getRejectPerformedBy2() == null) {
			workOrderActivityChecklist.setRejectPerformedBy2(
					Boolean.parseBoolean(existingValues.getREJECTPERFORMEDBY2())
			);
		}
		if (workOrderActivityChecklist.getRejectionReason() == null) {
			workOrderActivityChecklist.setRejectionReason(existingValues.getREJECTIONREASON());
		}
		if (
				workOrderActivityChecklist.getConditionOptionUserDefinedCode() == null &&
						existingValues.getCONDITIONOPTION() != null
		) {
			workOrderActivityChecklist.setConditionOptionUserDefinedCode(
					existingValues.getCONDITIONOPTION().getUSERDEFINEDCODE()
			);
		}

		// A merge of user-defined fields can happen here if needed.
	}

	@Override
	public String updateWorkOrderChecklistItem(
			InforContext context,
			WorkOrderActivityChecklistItem workOrderActivityChecklistItem,
			TaskPlan taskPlan
	) throws InforException {
		//
		// Fetch it first
		//
		MP7914_GetWorkOrderActivityCheckList_001 getwoactchl = new MP7914_GetWorkOrderActivityCheckList_001();
		getwoactchl.setCHECKLISTCODE(workOrderActivityChecklistItem.getCheckListCode());
		MP7914_GetWorkOrderActivityCheckList_001_Result getresult =
			tools.performInforOperation(context, inforws::getWorkOrderActivityCheckListOp, getwoactchl);

		//
		// Sync afterwards
		//
		net.datastream.schemas.mp_entities.workorderactivitychecklist_001.WorkOrderActivityCheckList workOrderActivityCheckListInfor = getresult
				.getResultData().getWorkOrderActivityCheckList();

		// Follow Up
		if (workOrderActivityChecklistItem.getFollowUp() != null) {
			workOrderActivityCheckListInfor.setFOLLOWUP(tools.getDataTypeTools().encodeBoolean(workOrderActivityChecklistItem.getFollowUp(), BooleanType.PLUS_MINUS));
		}

		if (workOrderActivityChecklistItem.getNotApplicableOption() != null) {
			USERDEFINEDCODEID_Type option = new USERDEFINEDCODEID_Type();
			option.setUSERDEFINEDCODE(workOrderActivityChecklistItem.getNotApplicableOption());
			workOrderActivityCheckListInfor.setNOTAPPLICABLEOPTION(option);
		}

		Function<String, String> getStringBool =
			key -> String.valueOf(key.equals(workOrderActivityChecklistItem.getResult()));

		switch (workOrderActivityChecklistItem.getType()) {
			case CheckListType.CHECKLIST_ITEM:
				if (ReturnType.COMPLETED.equalsIgnoreCase(workOrderActivityChecklistItem.getResult())) {
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
				if (workOrderActivityChecklistItem.getFinding() != null) {
					workOrderActivityCheckListInfor.setFINDINGID(new FINDINGID_Type());
					workOrderActivityCheckListInfor.getFINDINGID().setFINDINGCODE(workOrderActivityChecklistItem.getFinding());
				} else {
					workOrderActivityCheckListInfor.setFINDINGID(null);
				}
				break;
			case CheckListType.INSPECTION:
				if (workOrderActivityChecklistItem.getFinding() != null) {
					workOrderActivityCheckListInfor.setFINDINGID(new FINDINGID_Type());
					workOrderActivityCheckListInfor.getFINDINGID().setFINDINGCODE(workOrderActivityChecklistItem.getFinding());
				} else {
					workOrderActivityCheckListInfor.setFINDINGID(null);
				}
				// no break here, INSPECTION is the same as QUANTITATIVE/METER_READING,
				// but with findings and possible findings, so we will set the numeric value below
			case CheckListType.QUANTITATIVE:
			case CheckListType.METER_READING:
				BigDecimal numericValue = workOrderActivityChecklistItem.getNumericValue();

				// this logic is used while applications are not yet using the numeric value field
				// using the result field in the way below is deprecated
				if(numericValue == null) {
					BigDecimal possibleNumericValue =
						encodeBigDecimal(workOrderActivityChecklistItem.getResult(), "");

					if(possibleNumericValue != null) numericValue = possibleNumericValue;
				}

				workOrderActivityCheckListInfor
						.setRESULTVALUE(encodeQuantity(numericValue, "Checklists Value"));
				break;
			case CheckListType.OK_REPAIR_NEEDED:
				workOrderActivityCheckListInfor.setOKFLAG(getStringBool.apply(ReturnType.OK));
				workOrderActivityCheckListInfor.setREPAIRSNEEDED(getStringBool.apply(ReturnType.REPAIRSNEEDED));

				if(isEmpty(workOrderActivityChecklistItem.getFinding())) {
					workOrderActivityCheckListInfor.setRESOLUTIONID(null);
				} else {
					workOrderActivityCheckListInfor.setRESOLUTIONID(new USERDEFINEDCODEID_Type());
					workOrderActivityCheckListInfor.getRESOLUTIONID().setUSERDEFINEDCODE(workOrderActivityChecklistItem.getFinding());
				}
				break;
			case CheckListType.GOOD_POOR:
				workOrderActivityCheckListInfor.setGOOD(getStringBool.apply(ReturnType.GOOD));
				workOrderActivityCheckListInfor.setPOOR(getStringBool.apply(ReturnType.POOR));
				break;
			case CheckListType.OK_ADJUSTED_MEASUREMENT:
				workOrderActivityCheckListInfor
					.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(workOrderActivityChecklistItem.getNumericValue(), "Checklists Value"));
				// no break here, OK_ADJUSTED_MEASUREMENT is the same as OK_ADJUSTED,
				// but with a numeric value, so we will set the result to OK/ADJUSTED below
			case CheckListType.OK_ADJUSTED:
				workOrderActivityCheckListInfor.setOKFLAG(getStringBool.apply(ReturnType.OK));
				workOrderActivityCheckListInfor.setADJUSTED(getStringBool.apply(ReturnType.ADJUSTED));
				break;
			case CheckListType.NONCONFORMITY_MEASUREMENT:
				workOrderActivityCheckListInfor
					.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(workOrderActivityChecklistItem.getNumericValue(), "Checklists Value"));
				// no break here, NONCONFORMITY_MEASUREMENT is the same as NONCONFORMITY_CHECK,
				// but with a numberic value, so we will set the result to OK/NONCONFORMITY below
			case CheckListType.NONCONFORMITY_CHECK:
				workOrderActivityCheckListInfor.setOKFLAG(getStringBool.apply(ReturnType.OK));
				workOrderActivityCheckListInfor.setNONCONFORMITYFLAG(getStringBool.apply(ReturnType.NONCONFORMITY));
				break;
			case CheckListType.DATE:
				workOrderActivityCheckListInfor.setCHECKLISTDATE(tools.getDataTypeTools().encodeInforDate(workOrderActivityChecklistItem.getDate(), ""));
				break;
			case CheckListType.DATETIME:
				workOrderActivityCheckListInfor.setCHECKLISTDATETIME(tools.getDataTypeTools().encodeInforDate(workOrderActivityChecklistItem.getDateTime(), ""));
				break;
			case CheckListType.FREE_TEXT:
				workOrderActivityCheckListInfor.setCHECKLISTFREETEXT(workOrderActivityChecklistItem.getFreeText());
				break;
			case CheckListType.ENTITY:
				if (isEmpty(workOrderActivityChecklistItem.getEntityCode())) {
					workOrderActivityCheckListInfor.setENTITYCODEID(null);
					break;
				}
				workOrderActivityCheckListInfor.setENTITYCODEID(new ENTITYCODEID_Type());
				workOrderActivityCheckListInfor.getENTITYCODEID().setCODE(workOrderActivityChecklistItem.getEntityCode());
				ORGANIZATIONID_Type organizationidType = new ORGANIZATIONID_Type();
				organizationidType.setORGANIZATIONCODE(isEmpty(workOrderActivityChecklistItem.getEntityCodeOrg()) ? tools.getOrganizationCode(context) : workOrderActivityChecklistItem.getEntityCodeOrg());
				workOrderActivityCheckListInfor.getENTITYCODEID().setORGANIZATIONID(organizationidType);
				break;
			case CheckListType.DUAL_QUANTITATIVE:
				workOrderActivityCheckListInfor.setRESULTVALUE(tools.getDataTypeTools().encodeQuantity(workOrderActivityChecklistItem.getNumericValue(), "Checklists Value"));
				workOrderActivityCheckListInfor.setRESULTVALUE2(tools.getDataTypeTools().encodeQuantity(workOrderActivityChecklistItem.getNumericValue2(), "Checklists Value"));
				break;
		}

		if (workOrderActivityChecklistItem.getNotes() != null) {
			workOrderActivityCheckListInfor.setNOTES(workOrderActivityChecklistItem.getNotes());
		}

		MP7913_SyncWorkOrderActivityCheckList_001 syncwoactchl = new MP7913_SyncWorkOrderActivityCheckList_001();
		syncwoactchl.setWorkOrderActivityCheckList(workOrderActivityCheckListInfor);

		tools.performInforOperation(context, inforws::syncWorkOrderActivityCheckListOp, syncwoactchl);
		invalidateSignatures(
				context,
				workOrderActivityChecklistItem.getWorkOrderCode(),
				workOrderActivityChecklistItem.getActivityCode(),
				taskPlan
		);
		return null;
	}

	private void invalidateSignatures(
			InforContext context,
			String workOrderCode,
			String activityCode,
			TaskPlan taskPlan
	) throws InforException {

		TaskPlan fetchedTaskPlan = taskPlanService.getTaskPlan(context, taskPlan);
		MP7999_GetWorkOrderActivityCheckListDefault_001_Result fetchedChecklistValues = getChecklistDefaults(
				context,
				workOrderCode,
				activityCode
		);
		WorkOrderActivityChecklistSignatureResult[] signatures = getFilteredSignatures(
				context,
				fetchedChecklistValues,
				fetchedTaskPlan
		);

		if (signatures.length == 0) {
			return;
		}

		WorkOrderActivityChecklist checklist = new WorkOrderActivityChecklist(
				workOrderCode,
				Long.parseLong(activityCode)
		);

		for (WorkOrderActivityChecklistSignatureResult signature : signatures) {
			if (signature.getSigner() == null) {
				continue;
			}

			if (PERFORMED_BY_1.equals(signature.getType())) {
				checklist.setRejectPerformedBy(true);
			} else if (PERFORMED_BY_2.equals(signature.getType())) {
				checklist.setRejectPerformedBy2(true);
			}
		}
		if (
				Boolean.TRUE.equals(checklist.getRejectPerformedBy()) ||
				Boolean.TRUE.equals(checklist.getRejectPerformedBy2())
		) {
			checklist.setRejectionReason("The checklist was updated.");
		}

		mergeChecklistValues(checklist, fetchedChecklistValues);
		updateWorkOrderActivityCheckList(context, checklist, false);
	}

	@Override
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
		if (taskChecklist.getClassCode() != null && !taskChecklist.getClassCode().trim().isEmpty()) {
			taskChecklistInfor.setCLASSID(new CLASSID_Type());
			taskChecklistInfor.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
			taskChecklistInfor.getCLASSID().setCLASSCODE(taskChecklist.getClassCode());
		}
		//
		// CATEGORY
		//
		if (taskChecklist.getCategoryCode() != null && !taskChecklist.getCategoryCode().trim().isEmpty()) {
			taskChecklistInfor.setCATEGORYID(new CATEGORYID());
			taskChecklistInfor.getCATEGORYID().setCATEGORYCODE(taskChecklist.getCategoryCode());
		}

		//
		// EQUIPMENT FILTER
		//
		if (isNotEmpty(taskChecklist.getEquipmentFilter())) {
			taskChecklistInfor.setEQUIPMENTFILTER(taskChecklist.getEquipmentFilter());
		}

		MP7916_AddTaskChecklist_001 addTaskChecklist = new MP7916_AddTaskChecklist_001();
		addTaskChecklist.setTaskChecklist(taskChecklistInfor);

		tools.performInforOperation(context, inforws::addTaskChecklistOp, addTaskChecklist);
		return "OK";
	}

	@Override
	public WorkOrderActivityChecklistItem[] readWorkOrderChecklistItems(InforContext context, Activity activity) throws InforException {
		// Fetch the data
		GridRequest gridRequest = new GridRequest("WSJOBS_ACK");
		gridRequest.setRowCount(2000);
		gridRequest.setUseNative(true);
		gridRequest.setUserFunctionName("WSJOBS");
		gridRequest.getParams().put("param.workordernum", activity.getWorkOrderNumber());
		gridRequest.getParams().put("param.activity", activity.getActivityCode().toString());
		gridRequest.getParams().put("param.jobseq", "0");
		GridRequestResult gridRequestResult = gridsService.executeQuery(context, gridRequest);

		LinkedList<WorkOrderActivityChecklistItem> checklists = new LinkedList<>();
		for (GridRequestRow row : gridRequestResult.getRows()) {
			checklists.add(getCheckList(context, row, activity));
		}

		return checklists.toArray(new WorkOrderActivityChecklistItem[]{});
	}


	private WorkOrderActivityChecklistItem getCheckList(InforContext context, GridRequestRow row, Activity activity) throws InforException {
		WorkOrderActivityChecklistItem checklist = new WorkOrderActivityChecklistItem();
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
		if (isNotEmpty(followUpWorkOrderActivity)) {
			// Remove the activity after the Work Order Number
			checklist.setFollowUpWorkOrder(followUpWorkOrderActivity.split("-")[0]);
		}

		// REQUIRED
		String required = getCellContent("requiredtoclosedocument", row);
		checklist.setRequiredToClose("Yes".equalsIgnoreCase(required));

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
				// but with a numeric value and UOM, so we will set the result to OK/NONCONFORMITY below
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
				checklist.setDate(convertStringToDate(getCellContent("checklistdate", row)));
				break;
			case CheckListType.DATETIME:
				checklist.setDateTime(convertStringToDate(getCellContent("checklistdatetime", row)));
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
	private List<Finding> getPossibleFindings(InforContext context, GridRequestRow row) {
		List<String> possibleFindings = Arrays.asList(getCellContent("possiblefindings", row).split(","));
		return possibleFindings.stream()
				.map(findingCode -> {
					String findingsCacheKey = Tools.getCacheKey(context, findingCode);
					Function<String, String> loader = key -> loadFinding(context, findingCode);
					String finding = Optional.ofNullable(InforClient.cacheMap.get(CacheKey.FINDINGS))
							.map(cache -> (String) cache.get(findingsCacheKey, loader))
							.orElseGet(() -> loader.apply(findingsCacheKey));
					return new Finding(findingCode, finding != null ? finding : findingCode);
				})
				.collect(Collectors.toList());
	}

	private String loadFinding(InforContext context, String findingCode) {
		try {
			GridRequest gridRequest = new GridRequest("ISFIND", GridRequest.GRIDTYPE.LIST);
			gridRequest.addFilter("findingcode", findingCode, "=");
			return extractSingleResult(gridsService.executeQuery(context, gridRequest), "findingdesc");
		} catch (Exception e) {
			tools.log(Level.WARNING, "Finding could not be fetched: " + e.getMessage());
			return null;
		}
	}

	private boolean cellEquals(GridRequestRow row, String key, String value) {
		return getCellContent(key, row) != null && getCellContent(key, row).equals(value);
	}

	private String getValue(ResultSet v_result) throws SQLException {
		double value = v_result.getDouble("ack_value");
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

	@Override
	public Long createFollowUpWorkOrders(InforContext context, Activity activity) throws InforException {
		MP8000_CreateFollowUpWorkOrder_001 createFUWO = new MP8000_CreateFollowUpWorkOrder_001();

		createFUWO.setACTIVITYID(new ACTIVITYID());
		createFUWO.getACTIVITYID().setWORKORDERID(new WOID_Type());
		createFUWO.getACTIVITYID().getWORKORDERID().setJOBNUM(activity.getWorkOrderNumber());
		createFUWO.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
		createFUWO.getACTIVITYID().getACTIVITYCODE().setValue(activity.getActivityCode().longValue());
		createFUWO.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));

		MP8000_CreateFollowUpWorkOrder_001_Result createFUWOResult =
			tools.performInforOperation(context, inforws::createFollowUpWorkOrderOp, createFUWO);
		return createFUWOResult.getResultData().getWORKORDERCOUNT();
	}

	@Override
	public WorkOrderActivityChecklistDefinition getChecklistDefinition(InforContext context, TaskPlan taskPlan, String code) throws InforException {
		GridRequest gridRequest = new GridRequest("WSTASK_TCH", 1);
		gridRequest.addParam("param.task", taskPlan.getCode());
		gridRequest.addParam("param.revision", taskPlan.getTaskRevision() == null ? null : taskPlan.getTaskRevision().toString());

		gridRequest.addFilter("checklistitem", code, "EQUALS");

		GridRequestResult result = gridsService.executeQuery(context, gridRequest);

		WorkOrderActivityChecklistDefinition definition = convertGridResultToObject(
				WorkOrderActivityChecklistDefinition.class, null, result).stream().findFirst().orElse(null);

		String notApplicableOptionsString = extractSingleResult(result, "naoptions");

		// optimization to not call the grid request below
		if (notApplicableOptionsString == null || notApplicableOptionsString.isEmpty()) {
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
