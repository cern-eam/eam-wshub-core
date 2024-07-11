package ch.cern.eam.wshub.core.services.casemanagement.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.casemanagement.CaseManagementService;
import ch.cern.eam.wshub.core.services.casemanagement.entities.EAMCaseManagement;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.casemanagement_001.CaseManagement;
import net.datastream.schemas.mp_fields.CASEID_Type;
import net.datastream.schemas.mp_functions.mp3640_001.MP3640_AddCaseManagement_001;
import net.datastream.schemas.mp_functions.mp3641_001.MP3641_SyncCaseManagement_001;
import net.datastream.schemas.mp_functions.mp3642_001.MP3642_DeleteCaseManagement_001;
import net.datastream.schemas.mp_functions.mp3643_001.MP3643_GetCaseManagement_001;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CaseManagementServiceImpl implements CaseManagementService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public CaseManagementServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}

	public EAMCaseManagement readCase(EAMContext context, String caseCode) throws EAMException {
		final CaseManagement caseManagement = readCaseEAM(context, caseCode);
		EAMCaseManagement eamCaseManagement = tools.getEAMFieldTools().transformEAMObject(new EAMCaseManagement(),
				caseManagement, context);

		//New Custom Field API
		final CustomField[] customFields = eamCaseManagement.getCustomFields();
		if (customFields != null) {
			final Map<String, String> collect =
					Arrays.stream(customFields).collect(Collectors.toMap(CustomField::getCode,
							s -> s.getValue() != null ? s.getValue() : ""));
			eamCaseManagement.setCustomFieldMap(collect);
		}

		return eamCaseManagement;
	}

	private CaseManagement readCaseEAM(EAMContext context, String caseCode) throws EAMException {
		MP3643_GetCaseManagement_001 getCase = new MP3643_GetCaseManagement_001();
		getCase.setCASEID(new CASEID_Type());
		getCase.getCASEID().setCASECODE(caseCode);
		getCase.getCASEID().setORGANIZATIONID(tools.getOrganization(context));

		final CaseManagement caseManagement = tools.performEAMOperation(context, eamws::getCaseManagementOp, getCase)
				.getResultData().getCaseManagement();
		return caseManagement;
	}

	private CaseManagement syncCase(EAMContext context, EAMCaseManagement eamCaseManagement, CaseManagement caseManagement) throws EAMException {
		//New Custom Field API
		final Map<String, String> customFieldMap = eamCaseManagement.getCustomFieldMap();
		if (customFieldMap != null) {
			final CustomField[] customFields =
					customFieldMap.entrySet().stream().map(s -> new CustomField(s.getKey(), s.getValue())).toArray(CustomField[]::new);
			eamCaseManagement.setCustomFields(customFields);
		}

		caseManagement.setUSERDEFINEDAREA(
			tools.getCustomFieldsTools().getEAMCustomFields(
				context,
				null,
				caseManagement.getUSERDEFINEDAREA(),
				eamCaseManagement.getClassCode(),
				"CASE"
			)
		);

		// The system code is set by EAM, but it is still required. User status shall be used always instead
		eamCaseManagement.setSystemStatusCode("O");

		final CaseManagement caseManagement1 = tools.getEAMFieldTools().transformWSHubObject(
				caseManagement,
				eamCaseManagement,
				context
		);
		return caseManagement1;
	}

	public String createCase(EAMContext context, EAMCaseManagement eamCaseManagement) throws EAMException {
		eamCaseManagement.setCaseCode("0");
		final CaseManagement caseManagement = syncCase(context, eamCaseManagement, new CaseManagement());

		MP3640_AddCaseManagement_001 mp3640_AddCaseManagement_001 = new MP3640_AddCaseManagement_001();
		mp3640_AddCaseManagement_001.setCaseManagement(caseManagement);

		final String caseCode = tools.performEAMOperation(context, eamws::addCaseManagementOp,
						mp3640_AddCaseManagement_001)
				.getResultData().getCASEID().getCASECODE();
		return caseCode;
	}

	public String deleteCase(EAMContext context, String caseCode) throws EAMException {
		MP3642_DeleteCaseManagement_001 deleteCase = new MP3642_DeleteCaseManagement_001();
		deleteCase.setCASEID(new CASEID_Type());
		deleteCase.getCASEID().setCASECODE(caseCode);
		deleteCase.getCASEID().setORGANIZATIONID(tools.getOrganization(context));
		tools.performEAMOperation(context, eamws::deleteCaseManagementOp, deleteCase);
		return caseCode;
	}

	public synchronized String updateCase(EAMContext context, EAMCaseManagement eamCaseManagement)
			throws EAMException {
		final CaseManagement caseManagement = readCaseEAM(context, eamCaseManagement.getCaseCode());
		final CaseManagement caseManagement1 = syncCase(context, eamCaseManagement, caseManagement);

		MP3641_SyncCaseManagement_001 mp3641_SyncCaseManagement_001 = new MP3641_SyncCaseManagement_001();
		mp3641_SyncCaseManagement_001.setCaseManagement(caseManagement1);

		final String caseCode = tools.performEAMOperation(context, eamws::syncCaseManagementOp,
						mp3641_SyncCaseManagement_001)
				.getResultData().getCASEID().getCASECODE();
		return caseCode;
	}
}
