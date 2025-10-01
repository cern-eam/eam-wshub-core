package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.workorders.CaseService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.EAMCase;
import net.datastream.schemas.mp_entities.casemanagement_001.CaseDetails;
import net.datastream.schemas.mp_entities.casemanagement_001.CaseManagement;
import net.datastream.schemas.mp_entities.casemanagement_001.TrackingDetails;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp3640_001.MP3640_AddCaseManagement_001;
import net.datastream.schemas.mp_functions.mp3641_001.MP3641_SyncCaseManagement_001;
import net.datastream.schemas.mp_functions.mp3642_001.MP3642_DeleteCaseManagement_001;
import net.datastream.schemas.mp_functions.mp3643_001.MP3643_GetCaseManagement_001;
import net.datastream.schemas.mp_results.mp3640_001.MP3640_AddCaseManagement_001_Result;
import net.datastream.schemas.mp_results.mp3641_001.MP3641_SyncCaseManagement_001_Result;
import net.datastream.schemas.mp_results.mp3643_001.MP3643_GetCaseManagement_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import jakarta.xml.ws.Holder;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;

public class CaseServiceImpl implements CaseService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public CaseServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}

	public EAMCase readCase(EAMContext context, String caseID) throws EAMException {
		//
		// Fetch WO
		//
		MP3643_GetCaseManagement_001 getCase = new MP3643_GetCaseManagement_001();
		getCase.setCASEID(new CASEID_Type());
		getCase.getCASEID().setCASECODE(caseID);
		getCase.getCASEID().setORGANIZATIONID(tools.getOrganization(context));

		MP3643_GetCaseManagement_001_Result result =
			tools.performEAMOperation(context, eamws::getCaseManagementOp, getCase);

		CaseManagement caseManagement = result.getResultData().getCaseManagement();
		EAMCase caseMT = tools.getEAMFieldTools().transformEAMObject(new EAMCase(), caseManagement, context);
		//
		// DESCRIPTION
		//
		if (caseManagement.getCASEID() != null) {
			caseMT.setDescription(caseManagement.getCASEID().getDESCRIPTION());
			caseMT.setCode(caseManagement.getCASEID().getCASECODE());
		}

		//
		// EQUIPMENT
		//
		if (caseManagement.getEQUIPMENTID() != null) {
			caseMT.setEquipmentCode(caseManagement.getEQUIPMENTID().getEQUIPMENTCODE());
			caseMT.setEquipmentDesc(caseManagement.getEQUIPMENTID().getDESCRIPTION());
		}
		//
		// TYPE
		//
		if (caseManagement.getCASETYPE() != null) {
			caseMT.setTypeCode(caseManagement.getCASETYPE().getTYPECODE());
			caseMT.setTypeDesc(caseManagement.getCASETYPE().getDESCRIPTION());
		}
		//
		// STATUS
		//
		if (caseManagement.getSTATUS() != null) {
			caseMT.setStatusCode(caseManagement.getSTATUS().getSTATUSCODE());
			caseMT.setStatusDesc(caseManagement.getSTATUS().getDESCRIPTION());
		}
		//
		// DEPARTMENT
		//
		if (caseManagement.getDEPARTMENTID() != null) {
			caseMT.setDepartmentCode(caseManagement.getDEPARTMENTID().getDEPARTMENTCODE());
			caseMT.setDepartmentDesc(caseManagement.getDEPARTMENTID().getDESCRIPTION());
		}
		//
		// RESPONSIBLE
		//
		if (caseManagement.getTrackingDetails() != null
				&& caseManagement.getTrackingDetails().getPERSONRESPONSIBLE() != null) {
			caseMT.setResponsibleCode(caseManagement.getTrackingDetails().getPERSONRESPONSIBLE().getEMPLOYEECODE());
			caseMT.setResponsibleDesc(caseManagement.getTrackingDetails().getPERSONRESPONSIBLE().getDESCRIPTION());
			caseMT.setResponsibleEMail(caseManagement.getTrackingDetails().getEMAIL());
		}
		//
		// ASSIGNED TO
		//
		if (caseManagement.getTrackingDetails() != null
				&& caseManagement.getTrackingDetails().getASSIGNEDTO() != null) {
			caseMT.setAssignedToCode(caseManagement.getTrackingDetails().getASSIGNEDTO().getPERSONCODE());
			caseMT.setAssignedToDesc(caseManagement.getTrackingDetails().getASSIGNEDTO().getDESCRIPTION());
			caseMT.setAssignedToEMail(caseManagement.getTrackingDetails().getASSIGNEDTOEMAIL());
		}
		//
		// CLASS
		//
		if (caseManagement.getCaseDetails() != null && caseManagement.getCaseDetails().getCASECLASSID() != null) {
			caseMT.setClassCode(caseManagement.getCaseDetails().getCASECLASSID().getCLASSCODE());
			caseMT.setClassDesc(caseManagement.getCaseDetails().getCASECLASSID().getDESCRIPTION());
		}
		//
		// WORK ADDRESS
		//
		if (caseManagement.getCaseDetails() != null && caseManagement.getCaseDetails().getWORKADDRESS() != null) {
			caseMT.setWorkaddress(caseManagement.getCaseDetails().getWORKADDRESS());
		}
		//
		// PRIORITY
		//
		if (caseManagement.getCaseDetails() != null && caseManagement.getCaseDetails().getCASEPRIORITY() != null) {
			caseMT.setPriority(caseManagement.getCaseDetails().getCASEPRIORITY().getUSERDEFINEDCODE());
		}
		//
		// CUSTOM FIELDS
		//
		caseMT.setCustomFields(tools.getCustomFieldsTools().readEAMCustomFields(caseManagement.getUSERDEFINEDAREA(), context));

		//
		//
		//
		if (caseManagement.getCaseDetails() != null && caseManagement.getCaseDetails().getLOCATIONID() != null) {
			caseMT.setLocationCode(caseManagement.getCaseDetails().getLOCATIONID().getLOCATIONCODE());
			caseMT.setLocationDesc(caseManagement.getCaseDetails().getLOCATIONID().getDESCRIPTION());
		}
		//
		// TRACKING DETAILS
		//
		if (caseManagement.getTrackingDetails() != null) {
			caseMT.setScheduledStartDate(
					tools.getDataTypeTools().decodeEAMDate(caseManagement.getTrackingDetails().getSCHEDULEDSTARTDATE()));
			caseMT.setScheduledEndDate(
					tools.getDataTypeTools().decodeEAMDate(caseManagement.getTrackingDetails().getSCHEDULEDENDDATE()));
			caseMT.setRequestedStartDate(
					tools.getDataTypeTools().decodeEAMDate(caseManagement.getTrackingDetails().getREQUESTEDSTART()));
			caseMT.setRequestedEndDate(tools.getDataTypeTools().decodeEAMDate(caseManagement.getTrackingDetails().getREQUESTEDEND()));
			caseMT.setStartDate(tools.getDataTypeTools().decodeEAMDate(caseManagement.getTrackingDetails().getSTARTDATE()));
			caseMT.setCompletedDate(tools.getDataTypeTools().decodeEAMDate(caseManagement.getTrackingDetails().getCOMPLETEDDATE()));
			caseMT.setDaterequested(tools.getDataTypeTools().decodeEAMDate(caseManagement.getTrackingDetails().getDATEREQUESTED()));
		}
		//
		// CREATED BY
		//
		if (caseManagement.getCREATEDBY() != null) {
			caseMT.setCreatedBy(caseManagement.getCREATEDBY().getUSERCODE());
		}
		//
		// CREATED DATE
		//
		if (caseManagement.getCREATEDDATE() != null) {
			caseMT.setCreateDate(tools.getDataTypeTools().decodeEAMDate(caseManagement.getCREATEDDATE()));
		}
		//
		// UPDATED DATE
		//
		if (caseManagement.getDATEUPDATED() != null) {
			caseMT.setUpdatedDate(tools.getDataTypeTools().decodeEAMDate(caseManagement.getDATEUPDATED()));
		}
		//
		// CASE DETAILS
		//
		if (caseManagement.getCaseDetails() != null) {
			caseMT.setEventstartdate(tools.getDataTypeTools().decodeEAMDate(caseManagement.getCaseDetails().getEVENTSTARTDATE()));
			caseMT.setEventenddate(tools.getDataTypeTools().decodeEAMDate(caseManagement.getCaseDetails().getEVENTENDDATE()));
		}

		//
		// UPDATE COUNT
		//
		if (caseManagement.getRecordid() != null) {
			caseMT.setUpdateCount(caseManagement.getRecordid());
		}

		return caseMT;
	}

	public String createCase(EAMContext context, EAMCase caseMT) throws EAMException {

		CaseManagement caseManagement = new CaseManagement();
		caseManagement.setStandardUserDefinedFields(new StandardUserDefinedFields());

		caseManagement.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
				context,
				null,
				caseManagement.getUSERDEFINEDAREA(),
				caseMT.getClassCode(),
				"CASE"));

		initCaseObject(caseManagement, caseMT, context);

		MP3640_AddCaseManagement_001 addCase = new MP3640_AddCaseManagement_001();
		addCase.setCaseManagement(caseManagement);
		MP3640_AddCaseManagement_001_Result addCaseResult =
			tools.performEAMOperation(context, eamws::addCaseManagementOp, addCase);
		return addCaseResult.getResultData().getCASEID().getCASECODE();
	}

	public String deleteCase(EAMContext context, String caseID) throws EAMException {

		MP3642_DeleteCaseManagement_001 deleteCase = new MP3642_DeleteCaseManagement_001();
		deleteCase.setCASEID(new CASEID_Type());
		deleteCase.getCASEID().setCASECODE(caseID);
		deleteCase.getCASEID().setORGANIZATIONID(tools.getOrganization(context));

		tools.performEAMOperation(context, eamws::deleteCaseManagementOp, deleteCase);
		return caseID;
	}

	public synchronized String updateCase(EAMContext context, EAMCase caseMT)
			throws EAMException {
		//
		// FETCH IT FIRST
		//
		MP3643_GetCaseManagement_001 getCase = new MP3643_GetCaseManagement_001();
		getCase.setCASEID(new CASEID_Type());
		getCase.getCASEID().setCASECODE(caseMT.getCode());
		getCase.getCASEID().setORGANIZATIONID(tools.getOrganization(context));

		MP3643_GetCaseManagement_001_Result result =
			tools.performEAMOperation(context, eamws::getCaseManagementOp, getCase);

		CaseManagement caseManagement = result.getResultData().getCaseManagement();

		// Validate update count to prevent parallel edition of a case
		if (caseManagement.getRecordid() != caseMT.getUpdateCount()) {
			throw tools.generateFault("The record has been updated by another user.");
		}

		caseManagement.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
				context,
				caseManagement.getCaseDetails() != null ? toCodeString(caseManagement.getCaseDetails().getCASECLASSID()) : null,
				caseManagement.getUSERDEFINEDAREA(),
				caseMT.getClassCode(),
				"CASE"));
		//
		// INIT
		//
		initCaseObject(caseManagement, caseMT, context);

		//
		// UPDATE
		//

		MP3641_SyncCaseManagement_001 syncCase = new MP3641_SyncCaseManagement_001();
		syncCase.setCaseManagement(caseManagement);
		MP3641_SyncCaseManagement_001_Result syncCaseResult =
			tools.performEAMOperation(context, eamws::syncCaseManagementOp, syncCase);
		return syncCaseResult.getResultData().getCASEID().getCASECODE();
	}

	private void initCaseObject(CaseManagement caseEAM, EAMCase caseMT, EAMContext context) throws EAMException {

		tools.getEAMFieldTools().transformWSHubObject(caseEAM, caseMT, context);
		//
		// CODE AND DESCRIPTION
		//
		if (caseEAM.getCASEID() == null) {
			caseEAM.setCASEID(new CASEID_Type());
			caseEAM.getCASEID().setORGANIZATIONID(tools.getOrganization(context));
			caseEAM.getCASEID().setCASECODE("0");
		}

		if (caseMT.getDescription() != null) {
			caseEAM.getCASEID().setDESCRIPTION(caseMT.getDescription());
		}
		//
		// EQUIPMENT
		//
		if (caseMT.getEquipmentCode() != null) {
			if (caseMT.getEquipmentCode().trim().equals("")) {
				caseEAM.setEQUIPMENTID(null);
			} else {
				caseEAM.setEQUIPMENTID(new EQUIPMENTID_Type());
				caseEAM.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
				caseEAM.getEQUIPMENTID().setEQUIPMENTCODE(caseMT.getEquipmentCode().toUpperCase().trim());
			}
		}
		//
		// STATUS
		//
		if (caseMT.getStatusCode() != null) {
			caseEAM.setSTATUS(new STATUS_Type());
			caseEAM.getSTATUS().setSTATUSCODE(caseMT.getStatusCode().toUpperCase().trim());
			caseEAM.setRSTATUS(new STATUS_Type());
			caseEAM.getRSTATUS().setSTATUSCODE(caseMT.getStatusCode().toUpperCase().trim());
		}
		//
		// TYPE
		//
		if (caseMT.getTypeCode() != null) {
			caseEAM.setCASETYPE(new TYPE_Type());
			caseEAM.getCASETYPE().setTYPECODE(caseMT.getTypeCode().toUpperCase().trim());
		}
		//
		// DEPARTMENT
		//
		if (caseMT.getDepartmentCode() != null) {
			caseEAM.setDEPARTMENTID(new DEPARTMENTID_Type());
			caseEAM.getDEPARTMENTID().setORGANIZATIONID(tools.getOrganization(context));
			caseEAM.getDEPARTMENTID().setDEPARTMENTCODE(caseMT.getDepartmentCode().toUpperCase().trim());
		}
		//
		// CLASS CODE AND DESCRIPTION
		//
		if (caseMT.getClassCode() != null) {
			if (caseEAM.getCaseDetails() == null)
				caseEAM.setCaseDetails(new CaseDetails());
			if (caseEAM.getCaseDetails().getCASECLASSID() == null) {
				caseEAM.getCaseDetails().setCASECLASSID(new CLASSID_Type());
			}
			caseEAM.getCaseDetails().getCASECLASSID().setCLASSCODE(caseMT.getClassCode());
			caseEAM.getCaseDetails().getCASECLASSID().setDESCRIPTION(caseMT.getClassDesc());
			caseEAM.getCaseDetails().getCASECLASSID().setORGANIZATIONID(tools.getOrganization(context));

			if (caseMT.getPriority() != null) {
				USERDEFINEDCODEID_Type t = new USERDEFINEDCODEID_Type();
				caseEAM.getCaseDetails().setCASEPRIORITY(new USERDEFINEDCODEID_Type());
				caseEAM.getCaseDetails().getCASEPRIORITY().setUSERDEFINEDCODE(caseMT.getPriority());
			}
		}

		//
		// WORK ADDRESS
		//
		if (caseMT.getWorkaddress() != null) {
			if (caseEAM.getCaseDetails() == null)
				caseEAM.setCaseDetails(new CaseDetails());
			caseEAM.getCaseDetails().setWORKADDRESS(caseMT.getWorkaddress());
		}
		//
		// RESPONSIBLE
		//
		if (caseMT.getResponsibleCode() != null) {
			if (caseEAM.getTrackingDetails() == null)
				caseEAM.setTrackingDetails(new TrackingDetails());
			if (caseEAM.getTrackingDetails().getPERSONRESPONSIBLE() == null)
				caseEAM.getTrackingDetails().setPERSONRESPONSIBLE(new Employee_Type());
			caseEAM.getTrackingDetails().getPERSONRESPONSIBLE().setEMPLOYEECODE(caseMT.getResponsibleCode());
			caseEAM.getTrackingDetails().getPERSONRESPONSIBLE().setDESCRIPTION(caseMT.getResponsibleDesc());
			caseEAM.getTrackingDetails().setEMAIL(caseMT.getResponsibleEMail());
		}
		//
		// ASSIGNED TO
		//
		if (caseMT.getAssignedToCode() != null) {
			if (caseEAM.getTrackingDetails() == null)
				caseEAM.setTrackingDetails(new TrackingDetails());
			if (caseEAM.getTrackingDetails().getASSIGNEDTO() == null)
				caseEAM.getTrackingDetails().setASSIGNEDTO(new PERSONID_Type());
			caseEAM.getTrackingDetails().getASSIGNEDTO().setPERSONCODE(caseMT.getAssignedToCode());
			caseEAM.getTrackingDetails().getASSIGNEDTO().setDESCRIPTION(caseMT.getAssignedToDesc());
			caseEAM.getTrackingDetails().setASSIGNEDTOEMAIL(caseMT.getAssignedToEMail());
		}
		//
		// LOCATION
		//
		if (caseMT.getLocationCode() != null) {
			if (caseEAM.getCaseDetails() == null) {
				caseEAM.setCaseDetails(new CaseDetails());
			}
			if (caseMT.getLocationCode().equals("")) {
				caseEAM.getCaseDetails().setLOCATIONID(null);
			} else {
				caseEAM.getCaseDetails().setLOCATIONID(new LOCATIONID_Type());
				caseEAM.getCaseDetails().getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
				caseEAM.getCaseDetails().getLOCATIONID().setLOCATIONCODE(caseMT.getLocationCode().trim());
			}
		}
		//
		// SCHEDULING START DATE
		//
		if (caseMT.getScheduledStartDate() != null) {
			if (caseEAM.getTrackingDetails() == null) {
				caseEAM.setTrackingDetails(new TrackingDetails());
			}
			caseEAM.getTrackingDetails().setSCHEDULEDSTARTDATE(
					tools.getDataTypeTools().encodeEAMDate(caseMT.getScheduledStartDate(), "Scheduling Start Date"));
		}
		//
		// SCHEDULING END DATE
		//
		if (caseMT.getScheduledEndDate() != null) {
			if (caseEAM.getTrackingDetails() == null) {
				caseEAM.setTrackingDetails(new TrackingDetails());
			}
			caseEAM.getTrackingDetails().setSCHEDULEDENDDATE(
					tools.getDataTypeTools().encodeEAMDate(caseMT.getScheduledEndDate(), "Scheduling Completed Date"));
		}
		//
		// REQUESTED START DATE
		//
		if (caseMT.getRequestedStartDate() != null) {
			if (caseEAM.getTrackingDetails() == null) {
				caseEAM.setTrackingDetails(new TrackingDetails());
			}
			caseEAM.getTrackingDetails()
					.setREQUESTEDSTART(tools.getDataTypeTools().encodeEAMDate(caseMT.getRequestedStartDate(), "Requested Start Date"));
		}
		//
		// REQUESTED END DATE
		//
		if (caseMT.getRequestedEndDate() != null) {
			if (caseEAM.getTrackingDetails() == null) {
				caseEAM.setTrackingDetails(new TrackingDetails());
			}
			caseEAM.getTrackingDetails()
					.setREQUESTEDEND(tools.getDataTypeTools().encodeEAMDate(caseMT.getRequestedEndDate(), "Requested End Date"));
		}
		//
		// START DATE
		//
		if (caseMT.getStartDate() != null) {
			if (caseEAM.getTrackingDetails() == null) {
				caseEAM.setTrackingDetails(new TrackingDetails());
			}
			caseEAM.getTrackingDetails().setSTARTDATE(tools.getDataTypeTools().encodeEAMDate(caseMT.getStartDate(), "Start Date"));
		}
		//
		// COMPLETED DATE
		//
		if (caseMT.getCompletedDate() != null) {
			if (caseEAM.getTrackingDetails() == null) {
				caseEAM.setTrackingDetails(new TrackingDetails());
			}
			caseEAM.getTrackingDetails()
					.setCOMPLETEDDATE(tools.getDataTypeTools().encodeEAMDate(caseMT.getCompletedDate(), "Completed Date"));
		}
		//
		// DATEREQUESTED
		//
		if (caseMT.getDaterequested() != null) {
			if (caseEAM.getTrackingDetails() == null)
				caseEAM.setTrackingDetails(new TrackingDetails());
			caseEAM.getTrackingDetails()
					.setDATEREQUESTED(tools.getDataTypeTools().encodeEAMDate(caseMT.getDaterequested(), "Requested Date"));
		}
		//
		// CASE DETAILS EVENT START DATE
		//
		if (caseMT.getEventstartdate() != null) {
			if (caseEAM.getCaseDetails() == null)
				caseEAM.setCaseDetails(new CaseDetails());
			caseEAM.getCaseDetails()
					.setEVENTSTARTDATE(tools.getDataTypeTools().encodeEAMDate(caseMT.getEventstartdate(), "Event Start Date"));
		}
		//
		// CASE DETAILS EVENT END DATE
		//
		if (caseMT.getEventenddate() != null) {
			if (caseEAM.getCaseDetails() == null)
				caseEAM.setCaseDetails(new CaseDetails());
			caseEAM.getCaseDetails()
					.setEVENTENDDATE(tools.getDataTypeTools().encodeEAMDate(caseMT.getEventenddate(), "Event End Date"));
		}

	}

}
