package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.LocationService;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.LOCATIONID_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0318_001.MP0318_GetLocation_001;
import net.datastream.schemas.mp_results.mp0318_001.MP0318_GetLocation_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;

public class LocationServiceImpl implements LocationService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public LocationServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}


	public Equipment readLocation(InforContext context, String locationCode) throws InforException
	{
		MP0318_GetLocation_001 getLocation = new MP0318_GetLocation_001();
		getLocation.setLOCATIONID(new LOCATIONID_Type());
		getLocation.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
		getLocation.getLOCATIONID().setLOCATIONCODE(locationCode);
		MP0318_GetLocation_001_Result getLocationResult = new MP0318_GetLocation_001_Result();

		if (context.getCredentials() != null)
			getLocationResult = inforws.getLocationOp(getLocation, "*", tools.createSecurityHeader(context), "TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		else {
			getLocationResult = inforws.getLocationOp(getLocation, "*", null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		net.datastream.schemas.mp_entities.location_001.Location locationInfor = getLocationResult.getResultData().getLocation();

		Equipment location = new Equipment();

		if (locationInfor.getLOCATIONID() != null) {
			location.setCode(locationInfor.getLOCATIONID().getLOCATIONCODE());
			location.setDescription(locationInfor.getLOCATIONID().getDESCRIPTION());
		}

		if (locationInfor.getCLASSID() != null) {
			location.setClassCode(locationInfor.getCLASSID().getCLASSCODE());
			location.setClassDesc(locationInfor.getCLASSID().getDESCRIPTION());
		}

		if (locationInfor.getDEPARTMENTID() != null) {
			location.setDepartmentCode(locationInfor.getDEPARTMENTID().getDEPARTMENTCODE());
			location.setDepartmentDesc(locationInfor.getDEPARTMENTID().getDESCRIPTION());
		}

		if (locationInfor.getParentLocationID() != null) {
			location.setHierarchyLocationCode(locationInfor.getParentLocationID().getLOCATIONCODE());
			location.setHierarchyLocationDesc(locationInfor.getParentLocationID().getDESCRIPTION());
		}

		location.setTypeCode("L");
		location.setTypeDesc("Localisation");

		return location;
	}


	//TODO
	/*
	public String updateLocation(ch.cern.cmms.wshub.equipment.entities.Location locationParam, Credentials credentials, String sessionID) throws InforException  {
		net.datastream.schemas.mp_entities.location_001.Location locationInfor;
		MP0318_GetLocation_001 getLocation = new MP0318_GetLocation_001();
		getLocation.setLOCATIONID(new LOCATIONID_Type());
		getLocation.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
		getLocation.getLOCATIONID().setLOCATIONCODE(locationParam.getCode());
		MP0318_GetLocation_001_Result getLocationResult = new MP0318_GetLocation_001_Result();

		if (credentials != null)
			getLocationResult = inforws.getLocationOp(getLocation, "*", tools.createSecurityHeader(credentials.getUsername(), credentials.getPassword()), "TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		else {
			getLocationResult = inforws.getLocationOp(getLocation, "*", null, null, new Holder<SessionType>(tools.createInforSession(sessionID)), null, tools.getTenant(context));
		}
		locationInfor = getLocationResult.getResultData().getLocation();

		MP0319SyncLocation001 syncLocation = new MP0319SyncLocation001();
		syncLocation.setLocation(locationInfor);

		if (credentials != null)
			inforws.syncLocationOp(syncLocation, "*", tools.createSecurityHeader(credentials.getUsername(), credentials.getPassword()), "TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		else {
			inforws.syncLocationOp(syncLocation, "*", null, null, new Holder<SessionType>(tools.createInforSession(sessionID)), null, tools.getTenant(context));
		}
		return null;
	}
	*/
}