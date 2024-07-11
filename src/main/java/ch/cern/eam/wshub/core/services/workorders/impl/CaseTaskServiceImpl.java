package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.workorders.CaseTaskService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.EAMCaseTask;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import jakarta.xml.ws.Holder;


public class CaseTaskServiceImpl implements CaseTaskService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public CaseTaskServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}

	public EAMCaseTask readCaseTask(EAMContext context, String caseTaskID) throws EAMException {
		//
		// Fetch Case Task
		//
		MP3658_GetCaseManagementTask_001 getCaseTask = new MP3658_GetCaseManagementTask_001();
		getCaseTask.setCASEMANAGEMENTTASKID(new CASEMANAGEMENTTASKID_Type());
		getCaseTask.getCASEMANAGEMENTTASKID().setCASEMANAGEMENTTASKCODE(caseTaskID);

		MP3658_GetCaseManagementTask_001_Result result =
			tools.performEAMOperation(context, eamws::getCaseManagementTaskOp, getCaseTask);

		CaseManagementTask caseTaskEAM = result.getResultData().getCaseManagementTask();
		EAMCaseTask caseTaskMT = new EAMCaseTask();
		//
		// IDs and DESCRIPTION
		//
		if (caseTaskEAM.getCASEMANAGEMENTTASKID() != null) {
			caseTaskMT.setCaseCode(caseTaskEAM.getCASEID().getCASECODE());
			caseTaskMT.setTaskCode(caseTaskEAM.getCASEMANAGEMENTTASKID().getCASEMANAGEMENTTASKCODE());
			caseTaskMT.setDescription(caseTaskEAM.getCASEMANAGEMENTTASKID().getDESCRIPTION());
		}
		//
		// SEQUENCE
		//
		caseTaskMT.setSequence(caseTaskEAM.getSEQUENCE());
		//
		// STEP
		//
		if (caseTaskEAM.getSTEP() != null) {
			caseTaskMT.setStep(caseTaskEAM.getSTEP());
		}
		//
		// ESTIMATED COSTS
		//
		if (caseTaskEAM.getESTIMATEDCOSTS() != null) {
			caseTaskMT.setEstimatedCosts(tools.getDataTypeTools().decodeAmount(caseTaskEAM.getESTIMATEDCOSTS()));
		}
		//
		// PRIORITY
		//
		if (caseTaskEAM.getPRIORITY() != null) {
			caseTaskMT.setPriority(caseTaskEAM.getPRIORITY().getPRIORITYCODE());
		}
		//
		// CREATED BY
		//
		if (caseTaskEAM.getCREATEDBY() != null) {
			caseTaskMT.setCreatedBy(caseTaskEAM.getCREATEDBY().getUSERCODE());
		}
		//
		// CREATE DATE
		//
		if (caseTaskEAM.getCREATEDDATE() != null) {
			caseTaskMT.setDateCreated(tools.getDataTypeTools().decodeEAMDate(caseTaskEAM.getCREATEDDATE()));
		}
		//
		// UPDATED BY
		//
		if (caseTaskEAM.getUPDATEDBY() != null) {
			caseTaskMT.setUpdatedBy(caseTaskEAM.getUPDATEDBY().getUSERCODE());
		}
		//
		// UPDATE DATE
		//
		if (caseTaskEAM.getDATEUPDATED() != null) {
			caseTaskMT.setDateUpdated(tools.getDataTypeTools().decodeEAMDate(caseTaskEAM.getDATEUPDATED()));
		}
		//
		// TRACKING DETAILS
		//
		if (caseTaskEAM.getTrackingDetails() != null) {
			TrackingDetails trackingDetails = caseTaskEAM.getTrackingDetails();
			caseTaskMT.setStartDate(tools.getDataTypeTools().decodeEAMDate(caseTaskEAM.getTrackingDetails().getSTARTDATE()));
			if (trackingDetails.getCOMPLETEDDATE() != null) {
				caseTaskMT
						.setCompletedDate(tools.getDataTypeTools().decodeEAMDate(caseTaskEAM.getTrackingDetails().getCOMPLETEDDATE()));
			}
			if (trackingDetails.getSCHEDULEDSTARTDATE() != null) {
				caseTaskMT.setScheduledStartDate(
						tools.getDataTypeTools().decodeEAMDate(caseTaskEAM.getTrackingDetails().getSCHEDULEDSTARTDATE()));
			}
			if (trackingDetails.getSCHEDULEDENDDATE() != null) {
				caseTaskMT.setScheduledEndDate(
						tools.getDataTypeTools().decodeEAMDate(caseTaskEAM.getTrackingDetails().getSCHEDULEDENDDATE()));
			}
			if (caseTaskEAM.getTrackingDetails().getPLANNEDDURATION() != null) {
				PLANNEDDURATION_Type planneddurationType = caseTaskEAM.getTrackingDetails().getPLANNEDDURATION();
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
		caseTaskMT.setCustomFields(tools.getCustomFieldsTools().readEAMCustomFields(caseTaskEAM.getUSERDEFINEDAREA(), context));
		//
		// USER DEFINED FIELDS
		//
		//TODO caseTaskMT.setUserDefinedFields(tools.getUDFTools().readEAMUserDefinedFields(caseTaskEAM.getStandardUserDefinedFields()));
		//
		//
		//
		return caseTaskMT;
	}

	public String createCaseTask(EAMContext context, EAMCaseTask caseTaskMT) throws EAMException {
		CaseManagementTask caseManagement = new CaseManagementTask();
		caseManagement.setStandardUserDefinedFields(new StandardUserDefinedFields());

		initCaseTaskObject(caseManagement, caseTaskMT, context);
		MP3655_AddCaseManagementTask_001 initCaseTask = new MP3655_AddCaseManagementTask_001();
		initCaseTask.setCaseManagementTask(caseManagement);
		MP3655_AddCaseManagementTask_001_Result initCaseTaskResult =
			tools.performEAMOperation(context, eamws::addCaseManagementTaskOp, initCaseTask);
		return initCaseTaskResult.getResultData().getCASEMANAGEMENTTASKID().getCASEMANAGEMENTTASKCODE();
	}

	public String updateCaseTask(EAMContext context, EAMCaseTask caseTaskMT) throws EAMException {
		//
		// Fetch Case Task
		//
		MP3658_GetCaseManagementTask_001 getCaseTask = new MP3658_GetCaseManagementTask_001();
		getCaseTask.setCASEMANAGEMENTTASKID(new CASEMANAGEMENTTASKID_Type());
		getCaseTask.getCASEMANAGEMENTTASKID().setCASEMANAGEMENTTASKCODE(caseTaskMT.getTaskCode());

		MP3658_GetCaseManagementTask_001_Result result =
			tools.performEAMOperation(context, eamws::getCaseManagementTaskOp, getCaseTask);

		CaseManagementTask caseManagementTask = result.getResultData().getCaseManagementTask();
		initCaseTaskObject(caseManagementTask, caseTaskMT, context);

		//
		// UPDATE
		//
		MP3656_SyncCaseManagementTask_001 syncCase = new MP3656_SyncCaseManagementTask_001();
		syncCase.setCaseManagementTask(caseManagementTask);
		MP3656_SyncCaseManagementTask_001_Result syncCaseResult =
			tools.performEAMOperation(context, eamws::syncCaseManagementTaskOp, syncCase);
		return syncCaseResult.getResultData().getCASEMANAGEMENTTASKID().getCASEMANAGEMENTTASKCODE();
	}

	public String deleteCaseTask(EAMContext context, String caseTaskID) throws EAMException {
		MP3657_DeleteCaseManagementTask_001 deleteCaseTask = new MP3657_DeleteCaseManagementTask_001();

		// Set task id
		CASEMANAGEMENTTASKID_Type caseManagementTaskIdType = new CASEMANAGEMENTTASKID_Type();
		caseManagementTaskIdType.setCASEMANAGEMENTTASKCODE(caseTaskID);
		deleteCaseTask.setCASEMANAGEMENTTASKID(caseManagementTaskIdType);

		tools.performEAMOperation(context, eamws::deleteCaseManagementTaskOp, deleteCaseTask);
		return deleteCaseTask.getCASEMANAGEMENTTASKID().getCASEMANAGEMENTTASKCODE();
	}

	private void initCaseTaskObject(CaseManagementTask caseTaskEAM, EAMCaseTask caseTaskMT, EAMContext context) throws EAMException {
		if (caseTaskEAM.getCASEID() == null) {
			CASEID_Type caseIdType = new CASEID_Type();
			caseIdType.setCASECODE(caseTaskMT.getCaseCode());
			caseTaskEAM.setCASEID(caseIdType);
		}

		//
		// CODE AND DESCRIPTION
		//
		if (caseTaskEAM.getCASEID().getORGANIZATIONID() == null) {
			caseTaskEAM.getCASEID().setORGANIZATIONID(tools.getOrganization(context));
		}
		if (caseTaskEAM.getCASEMANAGEMENTTASKID() == null) {
			caseTaskEAM.setCASEMANAGEMENTTASKID(new CASEMANAGEMENTTASKID_Type());
			caseTaskEAM.getCASEMANAGEMENTTASKID().setCASEMANAGEMENTTASKCODE("0");
		}

		if (caseTaskMT.getDescription() != null) {
			caseTaskEAM.getCASEMANAGEMENTTASKID().setDESCRIPTION(caseTaskMT.getDescription());
		}
		//
		// SEQUENCE
		//
		if (caseTaskMT.getSequence() != null) {
			caseTaskEAM.setSEQUENCE(caseTaskMT.getSequence());
		}
		//
		// ESTIMATED COSTS
		//
		if (caseTaskMT.getEstimatedCosts() != null) {
			caseTaskEAM.setESTIMATEDCOSTS(tools.getDataTypeTools().encodeAmount(caseTaskMT.getEstimatedCosts(), null));
		}
		//
		// SEQUENCE
		//
		if (caseTaskMT.getSequence() != null) {
			caseTaskEAM.setSEQUENCE(caseTaskMT.getSequence());
		}
		//
		// STEP
		//
		if (caseTaskMT.getStep() != null) {
			caseTaskEAM.setSTEP(caseTaskMT.getStep());
		}
		//
		// PRIORITY
		//
		if (caseTaskMT.getPriority() != null) {
			caseTaskEAM.setPRIORITY(new PRIORITY());
			caseTaskEAM.getPRIORITY().setPRIORITYCODE(caseTaskMT.getPriority());
		}
		//
		// USER DEFINED FIELDS
		//
		//TODO tools.getUDFTools().updateEAMUserDefinedFields(caseTaskEAM.getStandardUserDefinedFields(), caseTaskMT.getUserDefinedFields());
		//
		// TRACKING DETAILS
		//
		if (caseTaskMT.getAssignedTo() != null) {
			if (caseTaskEAM.getTrackingDetails() == null) {
				caseTaskEAM.setTrackingDetails(new TrackingDetails());
			}
			caseTaskEAM.getTrackingDetails().setASSIGNEDTO(new PERSONID_Type());
			caseTaskEAM.getTrackingDetails().getASSIGNEDTO().setPERSONCODE(caseTaskMT.getAssignedTo());
		}

		if (caseTaskMT.getStartDate() != null) {
			if (caseTaskEAM.getTrackingDetails() == null) {
				caseTaskEAM.setTrackingDetails(new TrackingDetails());
			}
			caseTaskEAM.getTrackingDetails()
					.setSTARTDATE(tools.getDataTypeTools().encodeEAMDate(caseTaskMT.getStartDate(), "Start Date"));
		}

		if (caseTaskMT.getCompletedDate() != null) {
			if (caseTaskEAM.getTrackingDetails() == null) {
				caseTaskEAM.setTrackingDetails(new TrackingDetails());
			}
			caseTaskEAM.getTrackingDetails()
					.setCOMPLETEDDATE(tools.getDataTypeTools().encodeEAMDate(caseTaskMT.getCompletedDate(), "Completed Date"));
		}

		if (caseTaskMT.getScheduledStartDate() != null) {
			if (caseTaskEAM.getTrackingDetails() == null) {
				caseTaskEAM.setTrackingDetails(new TrackingDetails());
			}
			caseTaskEAM.getTrackingDetails().setSCHEDULEDSTARTDATE(
					tools.getDataTypeTools().encodeEAMDate(caseTaskMT.getScheduledStartDate(), "Scheduling Start Date"));
		}

		if (caseTaskMT.getPlannedDuration() != null && caseTaskMT.getPlannedDurationUnit() != null) {
			if (caseTaskEAM.getTrackingDetails() == null) {
				caseTaskEAM.setTrackingDetails(new TrackingDetails());
			}
			if (caseTaskEAM.getTrackingDetails().getPLANNEDDURATION() == null) {
				caseTaskEAM.getTrackingDetails().setPLANNEDDURATION(new PLANNEDDURATION_Type());
				caseTaskEAM.getTrackingDetails().getPLANNEDDURATION().setPLANNEDDURATIONUOM(new PLANNEDDURATIONUOM());
				caseTaskEAM.getTrackingDetails().getPLANNEDDURATION().getPLANNEDDURATIONUOM()
						.setPLANNEDDURATIONUOMCODE(caseTaskMT.getPlannedDurationUnit());
				caseTaskEAM.getTrackingDetails().getPLANNEDDURATION().setPLANNEDDURATIONVALUE(
						tools.getDataTypeTools().encodeQuantity(caseTaskMT.getPlannedDuration(), "Planned Duration"));
			}
		}

		if (caseTaskMT.getRequestedStartDate() != null) {
			if (caseTaskEAM.getTrackingDetails() == null) {
				caseTaskEAM.setTrackingDetails(new TrackingDetails());
			}
			caseTaskEAM.getTrackingDetails().setREQUESTEDSTART(
					tools.getDataTypeTools().encodeEAMDate(caseTaskMT.getRequestedStartDate(), "Requested Start Date"));
		}

		if (caseTaskMT.getRequestedEndDate() != null) {
			if (caseTaskEAM.getTrackingDetails() == null) {
				caseTaskEAM.setTrackingDetails(new TrackingDetails());
			}
			caseTaskEAM.getTrackingDetails()
					.setREQUESTEDEND(tools.getDataTypeTools().encodeEAMDate(caseTaskMT.getRequestedEndDate(), "Requested End Date"));
		}
	}


}
