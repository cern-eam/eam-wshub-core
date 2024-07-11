package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.equipment.LinearReferenceService;
import ch.cern.eam.wshub.core.services.equipment.entities.LinearReference;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.equiplinearref_001.EquipLinearRef;
import net.datastream.schemas.mp_entities.equiplinearref_001.OverviewDetails;
import net.datastream.schemas.mp_fields.CLASSID_Type;
import net.datastream.schemas.mp_fields.EQUIPMENTID_Type;
import net.datastream.schemas.mp_fields.TYPE_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp3023_001.MP3023_GetEquipLinearRef_001;
import net.datastream.schemas.mp_functions.mp3024_001.MP3024_AddEquipLinearRef_001;
import net.datastream.schemas.mp_functions.mp3025_001.MP3025_DeleteEquipLinearRef_001;
import net.datastream.schemas.mp_functions.mp3026_001.MP3026_SyncEquipLinearRef_001;
import net.datastream.schemas.mp_results.mp3023_001.MP3023_GetEquipLinearRef_001_Result;
import net.datastream.schemas.mp_results.mp3024_001.MP3024_AddEquipLinearRef_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import jakarta.xml.ws.Holder;

public class LinearReferenceServiceImpl implements LinearReferenceService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public LinearReferenceServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}


	public String updateEquipmentLinearReference(EAMContext context, LinearReference linearReference) throws EAMException {
		//
		// GET THE LINEAR REFERENCE ID
		//
		if (linearReference.getID() == null) {
			throw tools.generateFault("Linear Reference ID must be present.");
		}
		MP3023_GetEquipLinearRef_001 getLinRef = new MP3023_GetEquipLinearRef_001();
		getLinRef.setLRFID(tools.getDataTypeTools().encodeLong(linearReference.getID(), "Linear Reference ID"));

		MP3023_GetEquipLinearRef_001_Result result =
			tools.performEAMOperation(context, eamws::getEquipLinearRefOp, getLinRef);
		//
		// UPDATE THE LINEAR REFERENCE
		//
		EquipLinearRef linearReferenceEAM = result.getResultData().getEquipLinearRef();

		if (linearReference.getRelatedEquipmentCode() != null && !linearReference.getRelatedEquipmentCode().trim().equals(""))
		{
			linearReferenceEAM.setLRFRELATEDEQUIPMENTID(new EQUIPMENTID_Type());
			linearReferenceEAM.getLRFRELATEDEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
			linearReferenceEAM.getLRFRELATEDEQUIPMENTID().setEQUIPMENTCODE(linearReference.getRelatedEquipmentCode());
		}

		if (linearReference.getUpdateCount() != null && !linearReference.getUpdateCount().trim().equals("")) {
			linearReferenceEAM.setRecordid(tools.getDataTypeTools().encodeLong(linearReference.getUpdateCount(), "Update Count"));
		}

		if (linearReference.getTypeCode() != null && !linearReference.getTypeCode().trim().equals("")) {
			linearReferenceEAM.setLRFTYPE(new TYPE_Type());
			linearReferenceEAM.getLRFTYPE().setTYPECODE(linearReference.getTypeCode());
		}

		if (linearReference.getEquipmentCode() != null && !linearReference.getEquipmentCode().trim().equals("")) {
			linearReferenceEAM.setEQUIPMENTID(new EQUIPMENTID_Type());
			linearReferenceEAM.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
			linearReferenceEAM.getEQUIPMENTID().setEQUIPMENTCODE(linearReference.getEquipmentCode().toUpperCase());
		}

		if (linearReference.getDescription() != null && !linearReference.getDescription().trim().equals("")) {
			linearReferenceEAM.setLRFDESC(linearReference.getDescription());
		}

		if (linearReference.getFromPoint() != null) {
			linearReferenceEAM.setLRFFROMPOINT(tools.getDataTypeTools().encodeQuantity(linearReference.getFromPoint(), "From Point"));
		}

		if (linearReference.getToPoint() != null) {
			linearReferenceEAM.setLRFTOPOINT(tools.getDataTypeTools().encodeQuantity(linearReference.getToPoint(), "To Point"));
		}

		if (linearReference.getGeographicalReference() != null && !linearReference.getGeographicalReference().trim().equals("")) {
			linearReferenceEAM.setLRFGEOREF(linearReference.getGeographicalReference());
		}

		if (linearReference.getDisplayOnOverview() != null ||
				linearReference.getColorCode() != null ||
				linearReference.getIconCode() != null ||
				linearReference.getIconPath() != null) {
			linearReferenceEAM.setOverviewDetails(new OverviewDetails());
			linearReferenceEAM.getOverviewDetails().setCOLOR(linearReference.getColorCode());
			linearReferenceEAM.getOverviewDetails().setDISPLAYONOVERVIEW(linearReference.getDisplayOnOverview());
			linearReferenceEAM.getOverviewDetails().setICONCODE(linearReference.getIconCode());
			linearReferenceEAM.getOverviewDetails().setICONPATH(linearReference.getIconPath());
		}

		if (linearReference.getClassCode() != null) {
			linearReferenceEAM.setCLASSID(new CLASSID_Type());
			linearReferenceEAM.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
			linearReferenceEAM.getCLASSID().setCLASSCODE(linearReference.getClassCode().toUpperCase());
		}

		MP3026_SyncEquipLinearRef_001 syncEquipLienarRef = new MP3026_SyncEquipLinearRef_001();
		syncEquipLienarRef.setEquipLinearRef(linearReferenceEAM);

		try {
			tools.performEAMOperation(context, eamws::syncEquipLinearRefOp, syncEquipLienarRef);
		} catch (Exception e) {
			if (!e.getMessage().contains("EquipLinearRef has been Synchronized.")) {
				throw e;
			}
		}

		//return String.valueOf(syncResult.getResultData().getEquipLinearRef().getLRFID());
		return linearReference.getID();
	}

	public String deleteEquipmentLinearReference(EAMContext context, String linearReferenceID) throws EAMException {
		//
		//
		//
		MP3025_DeleteEquipLinearRef_001 deleteEquipLinearRef = new MP3025_DeleteEquipLinearRef_001();
		deleteEquipLinearRef.setLRFID(tools.getDataTypeTools().encodeLong(linearReferenceID, "Linear Ref. ID"));
		tools.performEAMOperation(context, eamws::deleteEquipLinearRefOp, deleteEquipLinearRef);
		return linearReferenceID;
	}

	public String createEquipmentLinearReference(EAMContext context, LinearReference linearReference) throws EAMException {

		EquipLinearRef linearReferenceEAM = new EquipLinearRef();

		if (linearReference.getRelatedEquipmentCode() != null && !linearReference.getRelatedEquipmentCode().trim().equals(""))
		{
			linearReferenceEAM.setLRFRELATEDEQUIPMENTID(new EQUIPMENTID_Type());
			linearReferenceEAM.getLRFRELATEDEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
			linearReferenceEAM.getLRFRELATEDEQUIPMENTID().setEQUIPMENTCODE(linearReference.getRelatedEquipmentCode());
		}

		if (linearReference.getTypeCode() != null && !linearReference.getTypeCode().trim().equals("")) {
			linearReferenceEAM.setLRFTYPE(new TYPE_Type());
			linearReferenceEAM.getLRFTYPE().setTYPECODE(linearReference.getTypeCode());
		}

		if (linearReference.getEquipmentCode() != null && !linearReference.getEquipmentCode().trim().equals("")) {
			linearReferenceEAM.setEQUIPMENTID(new EQUIPMENTID_Type());
			linearReferenceEAM.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
			linearReferenceEAM.getEQUIPMENTID().setEQUIPMENTCODE(linearReference.getEquipmentCode().toUpperCase());
		}

		if (linearReference.getDescription() != null && !linearReference.getDescription().trim().equals("")) {
			linearReferenceEAM.setLRFDESC(linearReference.getDescription());
		}

		if (linearReference.getFromPoint() != null) {
			linearReferenceEAM.setLRFFROMPOINT(tools.getDataTypeTools().encodeQuantity(linearReference.getFromPoint(), "From Point"));
		}

		if (linearReference.getToPoint() != null) {
			linearReferenceEAM.setLRFTOPOINT(tools.getDataTypeTools().encodeQuantity(linearReference.getToPoint(), "To Point"));
		}

		if (linearReference.getGeographicalReference() != null && !linearReference.getGeographicalReference().trim().equals("")) {
			linearReferenceEAM.setLRFGEOREF(linearReference.getGeographicalReference());
		}

		if (linearReference.getDisplayOnOverview() != null ||
				linearReference.getColorCode() != null ||
				linearReference.getIconCode() != null ||
				linearReference.getIconPath() != null) {
			linearReferenceEAM.setOverviewDetails(new OverviewDetails());
			linearReferenceEAM.getOverviewDetails().setCOLOR(linearReference.getColorCode());
			linearReferenceEAM.getOverviewDetails().setDISPLAYONOVERVIEW(linearReference.getDisplayOnOverview());
			linearReferenceEAM.getOverviewDetails().setICONCODE(linearReference.getIconCode());
			linearReferenceEAM.getOverviewDetails().setICONPATH(linearReference.getIconPath());
		}

		if (linearReference.getClassCode() != null) {
			linearReferenceEAM.setCLASSID(new CLASSID_Type());
			linearReferenceEAM.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
			linearReferenceEAM.getCLASSID().setCLASSCODE(linearReference.getClassCode());
		}
		//
		//
		//
		MP3024_AddEquipLinearRef_001 addEquipLinearRef = new MP3024_AddEquipLinearRef_001();
		addEquipLinearRef.setEquipLinearRef(linearReferenceEAM);
		MP3024_AddEquipLinearRef_001_Result result =
			tools.performEAMOperation(context, eamws::addEquipLinearRefOp, addEquipLinearRef);

		return String.valueOf(result.getResultData().getLRFID());
	}




}
