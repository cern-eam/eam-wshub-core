package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.workorders.CaseService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.InforCase;
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
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.xml.ws.Holder;

public class CaseServiceImpl implements CaseService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public CaseServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public InforCase readCase(InforContext context, String caseID) throws InforException {
		//
		// Fetch WO
		//
		MP3643_GetCaseManagement_001 getCase = new MP3643_GetCaseManagement_001();
		getCase.setCASEID(new CASEID_Type());
		getCase.getCASEID().setCASECODE(caseID);
		getCase.getCASEID().setORGANIZATIONID(tools.getOrganization(context));

		MP3643_GetCaseManagement_001_Result result = null;
		if (context.getCredentials() != null) {
			result = inforws.getCaseManagementOp(getCase, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.getCaseManagementOp(getCase, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		CaseManagement caseManagement = result.getResultData().getCaseManagement();
		InforCase caseMT = new InforCase();
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
			caseMT.setPriority(caseManagement.getCaseDetails().getCASEPRIORITY().getENTITY());
		}
		//
		// CUSTOM FIELDS
		//
		caseMT.setCustomFields(tools.getCustomFieldsTools().readInforCustomFields(caseManagement.getUSERDEFINEDAREA()));
		//
		//
		//
		// UDFS
		caseMT.setUserDefinedFields(tools.getUDFTools().readInforUserDefinedFields(caseManagement.getStandardUserDefinedFields()));
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
					tools.getDataTypeTools().decodeInforDate(caseManagement.getTrackingDetails().getSCHEDULEDSTARTDATE()));
			caseMT.setScheduledEndDate(
					tools.getDataTypeTools().decodeInforDate(caseManagement.getTrackingDetails().getSCHEDULEDENDDATE()));
			caseMT.setRequestedStartDate(
					tools.getDataTypeTools().decodeInforDate(caseManagement.getTrackingDetails().getREQUESTEDSTART()));
			caseMT.setRequestedEndDate(tools.getDataTypeTools().decodeInforDate(caseManagement.getTrackingDetails().getREQUESTEDEND()));
			caseMT.setStartDate(tools.getDataTypeTools().decodeInforDate(caseManagement.getTrackingDetails().getSTARTDATE()));
			caseMT.setCompletedDate(tools.getDataTypeTools().decodeInforDate(caseManagement.getTrackingDetails().getCOMPLETEDDATE()));
			caseMT.setDaterequested(tools.getDataTypeTools().decodeInforDate(caseManagement.getTrackingDetails().getDATEREQUESTED()));
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
			caseMT.setCreateDate(tools.getDataTypeTools().decodeInforDate(caseManagement.getCREATEDDATE()));
		}
		//
		// UPDATED DATE
		//
		if (caseManagement.getDATEUPDATED() != null) {
			caseMT.setUpdatedDate(tools.getDataTypeTools().decodeInforDate(caseManagement.getDATEUPDATED()));
		}
		//
		// CASE DETAILS
		//
		if (caseManagement.getCaseDetails() != null) {
			caseMT.setEventstartdate(tools.getDataTypeTools().decodeInforDate(caseManagement.getCaseDetails().getEVENTSTARTDATE()));
			caseMT.setEventenddate(tools.getDataTypeTools().decodeInforDate(caseManagement.getCaseDetails().getEVENTENDDATE()));
		}

		//
		// UPDATE COUNT
		//
		if (caseManagement.getRecordid() != null) {
			caseMT.setUpdateCount(caseManagement.getRecordid());
		}

		return caseMT;
	}

	public String createCase(InforContext context, InforCase caseMT) throws InforException {

		CaseManagement caseManagement = new CaseManagement();
		caseManagement.setStandardUserDefinedFields(new StandardUserDefinedFields());
		initCaseObject(caseManagement, caseMT, context);

		MP3640_AddCaseManagement_001 addCase = new MP3640_AddCaseManagement_001();
		addCase.setCaseManagement(caseManagement);
		MP3640_AddCaseManagement_001_Result addCaseResult = null;

		if (context.getCredentials() != null) {
			addCaseResult = inforws.addCaseManagementOp(addCase, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			addCaseResult = inforws.addCaseManagementOp(addCase, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return addCaseResult.getResultData().getCASEID().getCASECODE();
	}

	public String deleteCase(InforContext context, String caseID) throws InforException {

		MP3642_DeleteCaseManagement_001 deleteCase = new MP3642_DeleteCaseManagement_001();
		deleteCase.setCASEID(new CASEID_Type());
		deleteCase.getCASEID().setCASECODE(caseID);
		deleteCase.getCASEID().setORGANIZATIONID(tools.getOrganization(context));

		if (context.getCredentials() != null) {
			inforws.deleteCaseManagementOp(deleteCase, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.deleteCaseManagementOp(deleteCase, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return caseID;
	}

	public synchronized String updateCase(InforContext context, InforCase caseMT)
			throws InforException {
		//
		// FETCH IT FIRST
		//
		MP3643_GetCaseManagement_001 getCase = new MP3643_GetCaseManagement_001();
		getCase.setCASEID(new CASEID_Type());
		getCase.getCASEID().setCASECODE(caseMT.getCode());
		getCase.getCASEID().setORGANIZATIONID(tools.getOrganization(context));

		MP3643_GetCaseManagement_001_Result result = null;
		if (context.getCredentials() != null) {
			result = inforws.getCaseManagementOp(getCase, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.getCaseManagementOp(getCase, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		CaseManagement caseManagement = result.getResultData().getCaseManagement();

		// Validate update count to prevent parallel edition of a case
		if (caseManagement.getRecordid() != caseMT.getUpdateCount()) {
			throw tools.generateFault("The record has been updated by another user.");
		}

		//
		// INIT
		//
		initCaseObject(caseManagement, caseMT, context);
		//
		// UPDATE
		//
		MP3641_SyncCaseManagement_001 syncCase = new MP3641_SyncCaseManagement_001();
		syncCase.setCaseManagement(caseManagement);
		MP3641_SyncCaseManagement_001_Result syncCaseResult = null;

		if (context.getCredentials() != null) {
			syncCaseResult = inforws.syncCaseManagementOp(syncCase, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			syncCaseResult = inforws.syncCaseManagementOp(syncCase, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return syncCaseResult.getResultData().getCASEID().getCASECODE();
	}

	private void initCaseObject(CaseManagement caseInfor, InforCase caseMT, InforContext context) throws InforException {
		//
		// CODE AND DESCRIPTION
		//
		if (caseInfor.getCASEID() == null) {
			caseInfor.setCASEID(new CASEID_Type());
			caseInfor.getCASEID().setORGANIZATIONID(tools.getOrganization(context));
			caseInfor.getCASEID().setCASECODE("0");
		}

		if (caseMT.getDescription() != null) {
			caseInfor.getCASEID().setDESCRIPTION(caseMT.getDescription());
		}
		//
		// EQUIPMENT
		//
		if (caseMT.getEquipmentCode() != null) {
			if (caseMT.getEquipmentCode().trim().equals("")) {
				caseInfor.setEQUIPMENTID(null);
			} else {
				caseInfor.setEQUIPMENTID(new EQUIPMENTID_Type());
				caseInfor.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
				caseInfor.getEQUIPMENTID().setEQUIPMENTCODE(caseMT.getEquipmentCode().toUpperCase().trim());
			}
		}
		//
		// STATUS
		//
		if (caseMT.getStatusCode() != null) {
			caseInfor.setSTATUS(new STATUS_Type());
			caseInfor.getSTATUS().setSTATUSCODE(caseMT.getStatusCode().toUpperCase().trim());
			caseInfor.setRSTATUS(new STATUS_Type());
			caseInfor.getRSTATUS().setSTATUSCODE(caseMT.getStatusCode().toUpperCase().trim());
		}
		//
		// TYPE
		//
		if (caseMT.getTypeCode() != null) {
			caseInfor.setCASETYPE(new TYPE_Type());
			caseInfor.getCASETYPE().setTYPECODE(caseMT.getTypeCode().toUpperCase().trim());
		}
		//
		// DEPARTMENT
		//
		if (caseMT.getDepartmentCode() != null) {
			caseInfor.setDEPARTMENTID(new DEPARTMENTID_Type());
			caseInfor.getDEPARTMENTID().setORGANIZATIONID(tools.getOrganization(context));
			caseInfor.getDEPARTMENTID().setDEPARTMENTCODE(caseMT.getDepartmentCode().toUpperCase().trim());
		}
		//
		// CLASS CODE AND DESCRIPTION
		//
		if (caseMT.getClassCode() != null) {
			if (caseInfor.getCaseDetails() == null)
				caseInfor.setCaseDetails(new CaseDetails());
			if (caseInfor.getCaseDetails().getCASECLASSID() == null) {
				caseInfor.getCaseDetails().setCASECLASSID(new CLASSID_Type());
			}
			caseInfor.getCaseDetails().getCASECLASSID().setCLASSCODE(caseMT.getClassCode());
			caseInfor.getCaseDetails().getCASECLASSID().setDESCRIPTION(caseMT.getClassDesc());
			caseInfor.getCaseDetails().getCASECLASSID().setORGANIZATIONID(tools.getOrganization(context));

			if (caseMT.getPriority() != null) {
				USERDEFINEDCODEID_Type t = new USERDEFINEDCODEID_Type();
				caseInfor.getCaseDetails().setCASEPRIORITY(new USERDEFINEDCODEID_Type());
				caseInfor.getCaseDetails().getCASEPRIORITY().setENTITY(caseMT.getPriority());
			}
		}

		//
		// WORK ADDRESS
		//
		if (caseMT.getWorkaddress() != null) {
			if (caseInfor.getCaseDetails() == null)
				caseInfor.setCaseDetails(new CaseDetails());
			caseInfor.getCaseDetails().setWORKADDRESS(caseMT.getWorkaddress());
		}
		//
		// RESPONSIBLE
		//
		if (caseMT.getResponsibleCode() != null) {
			if (caseInfor.getTrackingDetails() == null)
				caseInfor.setTrackingDetails(new TrackingDetails());
			if (caseInfor.getTrackingDetails().getPERSONRESPONSIBLE() == null)
				caseInfor.getTrackingDetails().setPERSONRESPONSIBLE(new Employee_Type());
			caseInfor.getTrackingDetails().getPERSONRESPONSIBLE().setEMPLOYEECODE(caseMT.getResponsibleCode());
			caseInfor.getTrackingDetails().getPERSONRESPONSIBLE().setDESCRIPTION(caseMT.getResponsibleDesc());
			caseInfor.getTrackingDetails().setEMAIL(caseMT.getResponsibleEMail());
		}
		//
		// ASSIGNED TO
		//
		if (caseMT.getAssignedToCode() != null) {
			if (caseInfor.getTrackingDetails() == null)
				caseInfor.setTrackingDetails(new TrackingDetails());
			if (caseInfor.getTrackingDetails().getASSIGNEDTO() == null)
				caseInfor.getTrackingDetails().setASSIGNEDTO(new PERSONID_Type());
			caseInfor.getTrackingDetails().getASSIGNEDTO().setPERSONCODE(caseMT.getAssignedToCode());
			caseInfor.getTrackingDetails().getASSIGNEDTO().setDESCRIPTION(caseMT.getAssignedToDesc());
			caseInfor.getTrackingDetails().setASSIGNEDTOEMAIL(caseMT.getAssignedToEMail());
		}
		//
		// LOCATION
		//
		if (caseMT.getLocationCode() != null) {
			if (caseInfor.getCaseDetails() == null) {
				caseInfor.setCaseDetails(new CaseDetails());
			}
			if (caseMT.getLocationCode().equals("")) {
				caseInfor.getCaseDetails().setLOCATIONID(null);
			} else {
				caseInfor.getCaseDetails().setLOCATIONID(new LOCATIONID_Type());
				caseInfor.getCaseDetails().getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
				caseInfor.getCaseDetails().getLOCATIONID().setLOCATIONCODE(caseMT.getLocationCode().trim());
			}
		}
		//
		// User Defined Fields
		//
		tools.getUDFTools().updateInforUserDefinedFields(caseInfor.getStandardUserDefinedFields(), caseMT.getUserDefinedFields());
		//
		// SCHEDULING START DATE
		//
		if (caseMT.getScheduledStartDate() != null) {
			if (caseInfor.getTrackingDetails() == null) {
				caseInfor.setTrackingDetails(new TrackingDetails());
			}
			caseInfor.getTrackingDetails().setSCHEDULEDSTARTDATE(
					tools.getDataTypeTools().encodeInforDate(caseMT.getScheduledStartDate(), "Scheduling Start Date"));
		}
		//
		// SCHEDULING END DATE
		//
		if (caseMT.getScheduledEndDate() != null) {
			if (caseInfor.getTrackingDetails() == null) {
				caseInfor.setTrackingDetails(new TrackingDetails());
			}
			caseInfor.getTrackingDetails().setSCHEDULEDENDDATE(
					tools.getDataTypeTools().encodeInforDate(caseMT.getScheduledEndDate(), "Scheduling Completed Date"));
		}
		//
		// REQUESTED START DATE
		//
		if (caseMT.getRequestedStartDate() != null) {
			if (caseInfor.getTrackingDetails() == null) {
				caseInfor.setTrackingDetails(new TrackingDetails());
			}
			caseInfor.getTrackingDetails()
					.setREQUESTEDSTART(tools.getDataTypeTools().encodeInforDate(caseMT.getRequestedStartDate(), "Requested Start Date"));
		}
		//
		// REQUESTED END DATE
		//
		if (caseMT.getRequestedEndDate() != null) {
			if (caseInfor.getTrackingDetails() == null) {
				caseInfor.setTrackingDetails(new TrackingDetails());
			}
			caseInfor.getTrackingDetails()
					.setREQUESTEDEND(tools.getDataTypeTools().encodeInforDate(caseMT.getRequestedEndDate(), "Requested End Date"));
		}
		//
		// START DATE
		//
		if (caseMT.getStartDate() != null) {
			if (caseInfor.getTrackingDetails() == null) {
				caseInfor.setTrackingDetails(new TrackingDetails());
			}
			caseInfor.getTrackingDetails().setSTARTDATE(tools.getDataTypeTools().encodeInforDate(caseMT.getStartDate(), "Start Date"));
		}
		//
		// COMPLETED DATE
		//
		if (caseMT.getCompletedDate() != null) {
			if (caseInfor.getTrackingDetails() == null) {
				caseInfor.setTrackingDetails(new TrackingDetails());
			}
			caseInfor.getTrackingDetails()
					.setCOMPLETEDDATE(tools.getDataTypeTools().encodeInforDate(caseMT.getCompletedDate(), "Completed Date"));
		}
		//
		// DATEREQUESTED
		//
		if (caseMT.getDaterequested() != null) {
			if (caseInfor.getTrackingDetails() == null)
				caseInfor.setTrackingDetails(new TrackingDetails());
			caseInfor.getTrackingDetails()
					.setDATEREQUESTED(tools.getDataTypeTools().encodeInforDate(caseMT.getDaterequested(), "Requested Date"));
		}
		//
		// CASE DETAILS EVENT START DATE
		//
		if (caseMT.getEventstartdate() != null) {
			if (caseInfor.getCaseDetails() == null)
				caseInfor.setCaseDetails(new CaseDetails());
			caseInfor.getCaseDetails()
					.setEVENTSTARTDATE(tools.getDataTypeTools().encodeInforDate(caseMT.getEventstartdate(), "Event Start Date"));
		}
		//
		// CASE DETAILS EVENT END DATE
		//
		if (caseMT.getEventenddate() != null) {
			if (caseInfor.getCaseDetails() == null)
				caseInfor.setCaseDetails(new CaseDetails());
			caseInfor.getCaseDetails()
					.setEVENTENDDATE(tools.getDataTypeTools().encodeInforDate(caseMT.getEventenddate(), "Event End Date"));
		}

	}

}
