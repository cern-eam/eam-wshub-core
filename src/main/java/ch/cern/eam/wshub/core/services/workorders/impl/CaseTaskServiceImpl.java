package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.workorders.CaseTaskService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.InforCaseTask;
import net.datastream.schemas.mp_entities.casemanagementtask_001.CaseManagementTask;
import net.datastream.schemas.mp_entities.casemanagementtask_001.TrackingDetails;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp3655_001.MP3655_AddCaseManagementTask_001;
import net.datastream.schemas.mp_functions.mp3656_001.MP3656_SyncCaseManagementTask_001;
import net.datastream.schemas.mp_functions.mp3657_001.MP3657_DeleteCaseManagementTask_001;
import net.datastream.schemas.mp_functions.mp3658_001.MP3658_GetCaseManagementTask_001;
import net.datastream.schemas.mp_results.mp3655_001.MP3655_AddCaseManagementTask_001_Result;
import net.datastream.schemas.mp_results.mp3656_001.MP3656_SyncCaseManagementTask_001_Result;
import net.datastream.schemas.mp_results.mp3658_001.MP3658_GetCaseManagementTask_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;


public class CaseTaskServiceImpl implements CaseTaskService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public CaseTaskServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public InforCaseTask readCaseTask(InforContext context, String caseTaskID) throws InforException {
		//
		// Fetch Case Task
		//
		MP3658_GetCaseManagementTask_001 getCaseTask = new MP3658_GetCaseManagementTask_001();
		getCaseTask.setCASEMANAGEMENTTASKID(new CASEMANAGEMENTTASKID_Type());
		getCaseTask.getCASEMANAGEMENTTASKID().setCASEMANAGEMENTTASKCODE(caseTaskID);

		MP3658_GetCaseManagementTask_001_Result result = null;
		if (context.getCredentials() != null) {
			result = inforws.getCaseManagementTaskOp(getCaseTask, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.getCaseManagementTaskOp(getCaseTask, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		CaseManagementTask caseTaskInfor = result.getResultData().getCaseManagementTask();
		InforCaseTask caseTaskMT = new InforCaseTask();
		//
		// IDs and DESCRIPTION
		//
		if (caseTaskInfor.getCASEMANAGEMENTTASKID() != null) {
			caseTaskMT.setCaseCode(caseTaskInfor.getCASEID().getCASECODE());
			caseTaskMT.setTaskCode(caseTaskInfor.getCASEMANAGEMENTTASKID().getCASEMANAGEMENTTASKCODE());
			caseTaskMT.setDescription(caseTaskInfor.getCASEMANAGEMENTTASKID().getDESCRIPTION());
		}
		//
		// SEQUENCE
		//
		caseTaskMT.setSequence(caseTaskInfor.getSEQUENCE());
		//
		// STEP
		//
		if (caseTaskInfor.getSTEP() != null) {
			caseTaskMT.setStep(caseTaskInfor.getSTEP());
		}
		//
		// ESTIMATED COSTS
		//
		if (caseTaskInfor.getESTIMATEDCOSTS() != null) {
			caseTaskMT.setEstimatedCosts(tools.getDataTypeTools().decodeAmount(caseTaskInfor.getESTIMATEDCOSTS()));
		}
		//
		// PRIORITY
		//
		if (caseTaskInfor.getPRIORITY() != null) {
			caseTaskMT.setPriority(caseTaskInfor.getPRIORITY().getPRIORITYCODE());
		}
		//
		// CREATED BY
		//
		if (caseTaskInfor.getCREATEDBY() != null) {
			caseTaskMT.setCreatedBy(caseTaskInfor.getCREATEDBY().getUSERCODE());
		}
		//
		// CREATE DATE
		//
		if (caseTaskInfor.getCREATEDDATE() != null) {
			caseTaskMT.setDateCreated(tools.getDataTypeTools().decodeInforDate(caseTaskInfor.getCREATEDDATE()));
		}
		//
		// UPDATED BY
		//
		if (caseTaskInfor.getUPDATEDBY() != null) {
			caseTaskMT.setUpdatedBy(caseTaskInfor.getUPDATEDBY().getUSERCODE());
		}
		//
		// UPDATE DATE
		//
		if (caseTaskInfor.getDATEUPDATED() != null) {
			caseTaskMT.setDateUpdated(tools.getDataTypeTools().decodeInforDate(caseTaskInfor.getDATEUPDATED()));
		}
		//
		// TRACKING DETAILS
		//
		if (caseTaskInfor.getTrackingDetails() != null) {
			TrackingDetails trackingDetails = caseTaskInfor.getTrackingDetails();
			caseTaskMT.setStartDate(tools.getDataTypeTools().decodeInforDate(caseTaskInfor.getTrackingDetails().getSTARTDATE()));
			if (trackingDetails.getCOMPLETEDDATE() != null) {
				caseTaskMT
						.setCompletedDate(tools.getDataTypeTools().decodeInforDate(caseTaskInfor.getTrackingDetails().getCOMPLETEDDATE()));
			}
			if (trackingDetails.getSCHEDULEDSTARTDATE() != null) {
				caseTaskMT.setScheduledStartDate(
						tools.getDataTypeTools().decodeInforDate(caseTaskInfor.getTrackingDetails().getSCHEDULEDSTARTDATE()));
			}
			if (trackingDetails.getSCHEDULEDENDDATE() != null) {
				caseTaskMT.setScheduledEndDate(
						tools.getDataTypeTools().decodeInforDate(caseTaskInfor.getTrackingDetails().getSCHEDULEDENDDATE()));
			}
			if (caseTaskInfor.getTrackingDetails().getPLANNEDDURATION() != null) {
				PLANNEDDURATION_Type planneddurationType = caseTaskInfor.getTrackingDetails().getPLANNEDDURATION();
				if (planneddurationType.getPLANNEDDURATIONVALUE() != null) {
					caseTaskMT.setPlannedDuration(tools.getDataTypeTools().decodeQuantity(planneddurationType.getPLANNEDDURATIONVALUE()));
				}
				if (planneddurationType.getPLANNEDDURATIONUOM() != null
						&& planneddurationType.getPLANNEDDURATIONUOM().getPLANNEDDURATIONUOMCODE() != null) {
					caseTaskMT.setPlannedDurationUnit(
							planneddurationType.getPLANNEDDURATIONUOM().getPLANNEDDURATIONUOMCODE());
				}
			}
		}
		//
		// CUSTOM FIELDS
		//
		caseTaskMT.setCustomFields(tools.getCustomFieldsTools().readInforCustomFields(caseTaskInfor.getUSERDEFINEDAREA()));
		//
		// USER DEFINED FIELDS
		//
		caseTaskMT.setUserDefinedFields(
				tools.getUDFTools().readInforUserDefinedFields(caseTaskInfor.getStandardUserDefinedFields()));
		//
		//
		//
		return caseTaskMT;
	}

	public String createCaseTask(InforContext context, InforCaseTask caseTaskMT) throws InforException {
		CaseManagementTask caseManagement = new CaseManagementTask();
		caseManagement.setStandardUserDefinedFields(new StandardUserDefinedFields());

		initCaseTaskObject(caseManagement, caseTaskMT, context);
		MP3655_AddCaseManagementTask_001 initCaseTask = new MP3655_AddCaseManagementTask_001();
		initCaseTask.setCaseManagementTask(caseManagement);
		MP3655_AddCaseManagementTask_001_Result initCaseTaskResult = null;

		if (context.getCredentials() != null) {
			initCaseTaskResult = inforws.addCaseManagementTaskOp(initCaseTask, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			initCaseTaskResult = inforws.addCaseManagementTaskOp(initCaseTask, tools.getOrganizationCode(context), null,
					"", new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(),
					tools.getTenant(context));
		}
		return initCaseTaskResult.getResultData().getCASEMANAGEMENTTASKID().getCASEMANAGEMENTTASKCODE();
	}

	public String updateCaseTask(InforContext context, InforCaseTask caseTaskMT) throws InforException {
		//
		// Fetch Case Task
		//
		MP3658_GetCaseManagementTask_001 getCaseTask = new MP3658_GetCaseManagementTask_001();
		getCaseTask.setCASEMANAGEMENTTASKID(new CASEMANAGEMENTTASKID_Type());
		getCaseTask.getCASEMANAGEMENTTASKID().setCASEMANAGEMENTTASKCODE(caseTaskMT.getTaskCode());

		MP3658_GetCaseManagementTask_001_Result result = null;
		if (context.getCredentials() != null) {
			result = inforws.getCaseManagementTaskOp(getCaseTask, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.getCaseManagementTaskOp(getCaseTask, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		CaseManagementTask caseManagementTask = result.getResultData().getCaseManagementTask();
		initCaseTaskObject(caseManagementTask, caseTaskMT, context);

		//
		// UPDATE
		//
		MP3656_SyncCaseManagementTask_001 syncCase = new MP3656_SyncCaseManagementTask_001();
		syncCase.setCaseManagementTask(caseManagementTask);
		MP3656_SyncCaseManagementTask_001_Result syncCaseResult = null;

		if (context.getCredentials() != null) {
			syncCaseResult = inforws.syncCaseManagementTaskOp(syncCase, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			syncCaseResult = inforws.syncCaseManagementTaskOp(syncCase, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return syncCaseResult.getResultData().getCASEMANAGEMENTTASKID().getCASEMANAGEMENTTASKCODE();
	}

	public String deleteCaseTask(InforContext context, String caseTaskID) throws InforException {
		MP3657_DeleteCaseManagementTask_001 deleteCaseTask = new MP3657_DeleteCaseManagementTask_001();

		// Set task id
		CASEMANAGEMENTTASKID_Type caseManagementTaskIdType = new CASEMANAGEMENTTASKID_Type();
		caseManagementTaskIdType.setCASEMANAGEMENTTASKCODE(caseTaskID);
		deleteCaseTask.setCASEMANAGEMENTTASKID(caseManagementTaskIdType);

		if (context.getCredentials() != null) {
			inforws.deleteCaseManagementTaskOp(deleteCaseTask, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.deleteCaseManagementTaskOp(deleteCaseTask, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return deleteCaseTask.getCASEMANAGEMENTTASKID().getCASEMANAGEMENTTASKCODE();
	}

	private void initCaseTaskObject(CaseManagementTask caseTaskInfor, InforCaseTask caseTaskMT, InforContext context) throws InforException {
		if (caseTaskInfor.getCASEID() == null) {
			CASEID_Type caseIdType = new CASEID_Type();
			caseIdType.setCASECODE(caseTaskMT.getCaseCode());
			caseTaskInfor.setCASEID(caseIdType);
		}

		//
		// CODE AND DESCRIPTION
		//
		if (caseTaskInfor.getCASEID().getORGANIZATIONID() == null) {
			caseTaskInfor.getCASEID().setORGANIZATIONID(tools.getOrganization(context));
		}
		if (caseTaskInfor.getCASEMANAGEMENTTASKID() == null) {
			caseTaskInfor.setCASEMANAGEMENTTASKID(new CASEMANAGEMENTTASKID_Type());
			caseTaskInfor.getCASEMANAGEMENTTASKID().setCASEMANAGEMENTTASKCODE("0");
		}

		if (caseTaskMT.getDescription() != null) {
			caseTaskInfor.getCASEMANAGEMENTTASKID().setDESCRIPTION(caseTaskMT.getDescription());
		}
		//
		// SEQUENCE
		//
		if (caseTaskMT.getSequence() != null) {
			caseTaskInfor.setSEQUENCE(caseTaskMT.getSequence());
		}
		//
		// ESTIMATED COSTS
		//
		if (caseTaskMT.getEstimatedCosts() != null) {
			caseTaskInfor.setESTIMATEDCOSTS(tools.getDataTypeTools().encodeAmount(caseTaskMT.getEstimatedCosts(), null));
		}
		//
		// SEQUENCE
		//
		if (caseTaskMT.getSequence() != null) {
			caseTaskInfor.setSEQUENCE(caseTaskMT.getSequence());
		}
		//
		// STEP
		//
		if (caseTaskMT.getStep() != null) {
			caseTaskInfor.setSTEP(caseTaskMT.getStep());
		}
		//
		// PRIORITY
		//
		if (caseTaskMT.getPriority() != null) {
			caseTaskInfor.setPRIORITY(new PRIORITY());
			caseTaskInfor.getPRIORITY().setPRIORITYCODE(caseTaskMT.getPriority());
		}
		//
		// USER DEFINED FIELDS
		//
		tools.getUDFTools().updateInforUserDefinedFields(caseTaskInfor.getStandardUserDefinedFields(),
				caseTaskMT.getUserDefinedFields());
		//
		// TRACKING DETAILS
		//
		if (caseTaskMT.getAssignedTo() != null) {
			if (caseTaskInfor.getTrackingDetails() == null) {
				caseTaskInfor.setTrackingDetails(new TrackingDetails());
			}
			caseTaskInfor.getTrackingDetails().setASSIGNEDTO(new PERSONID_Type());
			caseTaskInfor.getTrackingDetails().getASSIGNEDTO().setPERSONCODE(caseTaskMT.getAssignedTo());
		}

		if (caseTaskMT.getStartDate() != null) {
			if (caseTaskInfor.getTrackingDetails() == null) {
				caseTaskInfor.setTrackingDetails(new TrackingDetails());
			}
			caseTaskInfor.getTrackingDetails()
					.setSTARTDATE(tools.getDataTypeTools().encodeInforDate(caseTaskMT.getStartDate(), "Start Date"));
		}

		if (caseTaskMT.getCompletedDate() != null) {
			if (caseTaskInfor.getTrackingDetails() == null) {
				caseTaskInfor.setTrackingDetails(new TrackingDetails());
			}
			caseTaskInfor.getTrackingDetails()
					.setCOMPLETEDDATE(tools.getDataTypeTools().encodeInforDate(caseTaskMT.getCompletedDate(), "Completed Date"));
		}

		if (caseTaskMT.getScheduledStartDate() != null) {
			if (caseTaskInfor.getTrackingDetails() == null) {
				caseTaskInfor.setTrackingDetails(new TrackingDetails());
			}
			caseTaskInfor.getTrackingDetails().setSCHEDULEDSTARTDATE(
					tools.getDataTypeTools().encodeInforDate(caseTaskMT.getScheduledStartDate(), "Scheduling Start Date"));
		}

		if (caseTaskMT.getPlannedDuration() != null && caseTaskMT.getPlannedDurationUnit() != null) {
			if (caseTaskInfor.getTrackingDetails() == null) {
				caseTaskInfor.setTrackingDetails(new TrackingDetails());
			}
			if (caseTaskInfor.getTrackingDetails().getPLANNEDDURATION() == null) {
				caseTaskInfor.getTrackingDetails().setPLANNEDDURATION(new PLANNEDDURATION_Type());
				caseTaskInfor.getTrackingDetails().getPLANNEDDURATION().setPLANNEDDURATIONUOM(new PLANNEDDURATIONUOM());
				caseTaskInfor.getTrackingDetails().getPLANNEDDURATION().getPLANNEDDURATIONUOM()
						.setPLANNEDDURATIONUOMCODE(caseTaskMT.getPlannedDurationUnit());
				caseTaskInfor.getTrackingDetails().getPLANNEDDURATION().setPLANNEDDURATIONVALUE(
						tools.getDataTypeTools().encodeQuantity(caseTaskMT.getPlannedDuration(), "Planned Duration"));
			}
		}

		if (caseTaskMT.getRequestedStartDate() != null) {
			if (caseTaskInfor.getTrackingDetails() == null) {
				caseTaskInfor.setTrackingDetails(new TrackingDetails());
			}
			caseTaskInfor.getTrackingDetails().setREQUESTEDSTART(
					tools.getDataTypeTools().encodeInforDate(caseTaskMT.getRequestedStartDate(), "Requested Start Date"));
		}

		if (caseTaskMT.getRequestedEndDate() != null) {
			if (caseTaskInfor.getTrackingDetails() == null) {
				caseTaskInfor.setTrackingDetails(new TrackingDetails());
			}
			caseTaskInfor.getTrackingDetails()
					.setREQUESTEDEND(tools.getDataTypeTools().encodeInforDate(caseTaskMT.getRequestedEndDate(), "Requested End Date"));
		}
	}


}
