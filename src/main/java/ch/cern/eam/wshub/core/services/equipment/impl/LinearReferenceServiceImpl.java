package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.LinearReferenceService;
import ch.cern.eam.wshub.core.services.equipment.entities.LinearReference;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
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
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;

public class LinearReferenceServiceImpl implements LinearReferenceService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public LinearReferenceServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}


	public String updateEquipmentLinearReference(InforContext context, LinearReference linearReference) throws InforException {
		//
		// GET THE LINEAR REFERENCE ID
		//
		if (linearReference.getID() == null) {
			throw tools.generateFault("Linear Reference ID must be present.");
		}
		MP3023_GetEquipLinearRef_001 getLinRef = new MP3023_GetEquipLinearRef_001();
		getLinRef.setLRFID(tools.getDataTypeTools().encodeLong(linearReference.getID(), "Linear Reference ID"));

		MP3023_GetEquipLinearRef_001_Result result;
		if (context.getCredentials() != null) {
			result = inforws.getEquipLinearRefOp(getLinRef, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.getEquipLinearRefOp(getLinRef, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		//
		// UPDATE THE LINEAR REFERENCE
		//
		EquipLinearRef linearReferenceInfor = result.getResultData().getEquipLinearRef();

		if (linearReference.getRelatedEquipmentCode() != null && !linearReference.getRelatedEquipmentCode().trim().equals(""))
		{
			linearReferenceInfor.setLRFRELATEDEQUIPMENTID(new EQUIPMENTID_Type());
			linearReferenceInfor.getLRFRELATEDEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
			linearReferenceInfor.getLRFRELATEDEQUIPMENTID().setEQUIPMENTCODE(linearReference.getRelatedEquipmentCode());
		}

		if (linearReference.getUpdateCount() != null && !linearReference.getUpdateCount().trim().equals("")) {
			linearReferenceInfor.setRecordid(tools.getDataTypeTools().encodeLong(linearReference.getUpdateCount(), "Update Count"));
		}

		if (linearReference.getTypeCode() != null && !linearReference.getTypeCode().trim().equals("")) {
			linearReferenceInfor.setLRFTYPE(new TYPE_Type());
			linearReferenceInfor.getLRFTYPE().setTYPECODE(linearReference.getTypeCode());
		}

		if (linearReference.getEquipmentCode() != null && !linearReference.getEquipmentCode().trim().equals("")) {
			linearReferenceInfor.setEQUIPMENTID(new EQUIPMENTID_Type());
			linearReferenceInfor.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
			linearReferenceInfor.getEQUIPMENTID().setEQUIPMENTCODE(linearReference.getEquipmentCode().toUpperCase());
		}

		if (linearReference.getDescription() != null && !linearReference.getDescription().trim().equals("")) {
			linearReferenceInfor.setLRFDESC(linearReference.getDescription());
		}

		if (linearReference.getFromPoint() != null && !linearReference.getFromPoint().trim().equals("")) {
			linearReferenceInfor.setLRFFROMPOINT(tools.getDataTypeTools().encodeQuantity(linearReference.getFromPoint(), "From Point"));
		}

		if (linearReference.getToPoint() != null && !linearReference.getToPoint().trim().equals("")) {
			linearReferenceInfor.setLRFTOPOINT(tools.getDataTypeTools().encodeQuantity(linearReference.getToPoint(), "To Point"));
		}

		if (linearReference.getGeographicalReference() != null && !linearReference.getGeographicalReference().trim().equals("")) {
			linearReferenceInfor.setLRFGEOREF(linearReference.getGeographicalReference());
		}

		if (linearReference.getDisplayOnOverview() != null ||
				linearReference.getColorCode() != null ||
				linearReference.getIconCode() != null ||
				linearReference.getIconPath() != null) {
			linearReferenceInfor.setOverviewDetails(new OverviewDetails());
			linearReferenceInfor.getOverviewDetails().setCOLOR(linearReference.getColorCode());
			linearReferenceInfor.getOverviewDetails().setDISPLAYONOVERVIEW(linearReference.getDisplayOnOverview());
			linearReferenceInfor.getOverviewDetails().setICONCODE(linearReference.getIconCode());
			linearReferenceInfor.getOverviewDetails().setICONPATH(linearReference.getIconPath());
		}

		if (linearReference.getClassCode() != null) {
			linearReferenceInfor.setCLASSID(new CLASSID_Type());
			linearReferenceInfor.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
			linearReferenceInfor.getCLASSID().setCLASSCODE(linearReference.getClassCode().toUpperCase());
		}

		MP3026_SyncEquipLinearRef_001 syncEquipLienarRef = new MP3026_SyncEquipLinearRef_001();
		syncEquipLienarRef.setEquipLinearRef(linearReferenceInfor);

		try {
			if (context.getCredentials() != null) {
				inforws.syncEquipLinearRefOp(syncEquipLienarRef, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
			} else {
				inforws.syncEquipLinearRefOp(syncEquipLienarRef, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
			}
		} catch (Exception e) {
			if (!e.getMessage().contains("EquipLinearRef has been Synchronized.")) {
				throw e;
			}
		}

		//return String.valueOf(syncResult.getResultData().getEquipLinearRef().getLRFID());
		return linearReference.getID();
	}

	public String deleteEquipmentLinearReference(InforContext context, String linearReferenceID) throws InforException {
		//
		//
		//
		MP3025_DeleteEquipLinearRef_001 deleteEquipLinearRef = new MP3025_DeleteEquipLinearRef_001();
		deleteEquipLinearRef.setLRFID(tools.getDataTypeTools().encodeLong(linearReferenceID, "Linear Ref. ID"));
		if (context.getCredentials() != null) {
			inforws.deleteEquipLinearRefOp(deleteEquipLinearRef, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.deleteEquipLinearRefOp(deleteEquipLinearRef, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		return linearReferenceID;
	}

	public String createEquipmentLinearReference(InforContext context, LinearReference linearReference) throws InforException {

		EquipLinearRef linearReferenceInfor = new EquipLinearRef();

		if (linearReference.getRelatedEquipmentCode() != null && !linearReference.getRelatedEquipmentCode().trim().equals(""))
		{
			linearReferenceInfor.setLRFRELATEDEQUIPMENTID(new EQUIPMENTID_Type());
			linearReferenceInfor.getLRFRELATEDEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
			linearReferenceInfor.getLRFRELATEDEQUIPMENTID().setEQUIPMENTCODE(linearReference.getRelatedEquipmentCode());
		}

		if (linearReference.getTypeCode() != null && !linearReference.getTypeCode().trim().equals("")) {
			linearReferenceInfor.setLRFTYPE(new TYPE_Type());
			linearReferenceInfor.getLRFTYPE().setTYPECODE(linearReference.getTypeCode());
		}

		if (linearReference.getEquipmentCode() != null && !linearReference.getEquipmentCode().trim().equals("")) {
			linearReferenceInfor.setEQUIPMENTID(new EQUIPMENTID_Type());
			linearReferenceInfor.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
			linearReferenceInfor.getEQUIPMENTID().setEQUIPMENTCODE(linearReference.getEquipmentCode().toUpperCase());
		}

		if (linearReference.getDescription() != null && !linearReference.getDescription().trim().equals("")) {
			linearReferenceInfor.setLRFDESC(linearReference.getDescription());
		}

		if (linearReference.getFromPoint() != null && !linearReference.getFromPoint().trim().equals("")) {
			linearReferenceInfor.setLRFFROMPOINT(tools.getDataTypeTools().encodeQuantity(linearReference.getFromPoint(), "From Point"));
		}

		if (linearReference.getToPoint() != null && !linearReference.getToPoint().trim().equals("")) {
			linearReferenceInfor.setLRFTOPOINT(tools.getDataTypeTools().encodeQuantity(linearReference.getToPoint(), "To Point"));
		}

		if (linearReference.getGeographicalReference() != null && !linearReference.getGeographicalReference().trim().equals("")) {
			linearReferenceInfor.setLRFGEOREF(linearReference.getGeographicalReference());
		}

		if (linearReference.getDisplayOnOverview() != null ||
				linearReference.getColorCode() != null ||
				linearReference.getIconCode() != null ||
				linearReference.getIconPath() != null) {
			linearReferenceInfor.setOverviewDetails(new OverviewDetails());
			linearReferenceInfor.getOverviewDetails().setCOLOR(linearReference.getColorCode());
			linearReferenceInfor.getOverviewDetails().setDISPLAYONOVERVIEW(linearReference.getDisplayOnOverview());
			linearReferenceInfor.getOverviewDetails().setICONCODE(linearReference.getIconCode());
			linearReferenceInfor.getOverviewDetails().setICONPATH(linearReference.getIconPath());
		}

		if (linearReference.getClassCode() != null) {
			linearReferenceInfor.setCLASSID(new CLASSID_Type());
			linearReferenceInfor.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
			linearReferenceInfor.getCLASSID().setCLASSCODE(linearReference.getClassCode());
		}
		//
		//
		//
		MP3024_AddEquipLinearRef_001 addEquipLinearRef = new MP3024_AddEquipLinearRef_001();
		addEquipLinearRef.setEquipLinearRef(linearReferenceInfor);
		MP3024_AddEquipLinearRef_001_Result result;
		if (context.getCredentials() != null) {
			result = inforws.addEquipLinearRefOp(addEquipLinearRef, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.addEquipLinearRefOp(addEquipLinearRef, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}


		return String.valueOf(result.getResultData().getLRFID());
	}




}
