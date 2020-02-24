package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.LocationService;
import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.location_001.LocationParentHierarchy;
import net.datastream.schemas.mp_entities.location_001.ParentLocation;
import net.datastream.schemas.mp_fields.DEPARTMENTID_Type;
import net.datastream.schemas.mp_fields.LOCATIONID_Type;
import net.datastream.schemas.mp_fields.TYPE_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0317_001.MP0317_AddLocation_001;
import net.datastream.schemas.mp_functions.mp0318_001.MP0318_GetLocation_001;
import net.datastream.schemas.mp_functions.mp0319_001.MP0319_SyncLocation_001;
import net.datastream.schemas.mp_functions.mp0320_001.MP0320_DeleteLocation_001;
import net.datastream.schemas.mp_functions.mp0361_001.MP0361_GetLocationParentHierarchy_001;
import net.datastream.schemas.mp_results.mp0317_001.MP0317_AddLocation_001_Result;
import net.datastream.schemas.mp_results.mp0318_001.MP0318_GetLocation_001_Result;
import net.datastream.schemas.mp_results.mp0361_001.MP0361_GetLocationParentHierarchy_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class LocationServiceImpl implements LocationService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public LocationServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	//
	// BATCH WEB SERVICES
	//

	public BatchResponse<String> createLocationBatch(InforContext context, List<Location> locations)
			throws InforException {
		List<Callable<String>> callableList = locations.stream()
				.<Callable<String>>map(location -> () -> createLocation(context, location))
				.collect(Collectors.toList());

		return tools.processCallables(callableList);
	}

	public BatchResponse<Location> readLocationBatch(InforContext context, List<String> locationCodes)
			throws InforException {
		List<Callable<Location>> callableList = locationCodes.stream()
				.<Callable<Location>>map(locationCode -> () -> readLocation(context, locationCode))
				.collect(Collectors.toList());
		return tools.processCallables(callableList);
	}

	public BatchResponse<String> updateLocationBatch(InforContext context, List<Location> locations)
			throws InforException {
		List<Callable<String>> callableList = locations.stream()
				.<Callable<String>>map(location -> () -> updateLocation(context, location))
				.collect(Collectors.toList());
		return tools.processCallables(callableList);
	}

	public BatchResponse<String> deleteLocationBatch(InforContext context, List<String> locationCodes)
			throws InforException {
		List<Callable<String>> callableList = locationCodes.stream()
				.<Callable<String>>map(locationCode -> () -> deleteLocation(context, locationCode))
				.collect(Collectors.toList());
		return tools.processCallables(callableList);
	}

	public Location readLocation(InforContext context, String locationCode) throws InforException
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

		Location location = tools.getInforFieldTools().transformInforObject(new Location(), locationInfor);

		if(locationInfor.getParentLocationID() != null) {
			location.setHierarchyLocationCode(locationInfor.getParentLocationID().getLOCATIONCODE());
		}

		return location;
	}

	public String createLocation(InforContext context, Location locationParam) throws InforException {
		net.datastream.schemas.mp_entities.location_001.Location locationInfor =
			new net.datastream.schemas.mp_entities.location_001.Location();

		locationInfor.setLOCATIONID(new LOCATIONID_Type());
		locationInfor.getLOCATIONID().setLOCATIONCODE(locationParam.getCode());
		locationInfor.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));

		// Check Custom fields. If they change, or now we have them
		if (locationParam.getClassCode() != null && (locationInfor.getCLASSID() == null
				|| !locationParam.getClassCode().toUpperCase().equals(locationInfor.getCLASSID().getCLASSCODE()))) {
			locationInfor.setUSERDEFINEDAREA(
					tools.getCustomFieldsTools().getInforCustomFields(context, "LOC", locationParam.getClassCode().toUpperCase()));
		}

		tools.getInforFieldTools().transformWSHubObject(locationInfor, locationParam, context);

		locationInfor.setLocationParentHierarchy(getLocationParentHierarchy(context, locationParam));

		MP0317_AddLocation_001 addLocation = new MP0317_AddLocation_001();
		addLocation.setLocation(locationInfor);
		MP0317_AddLocation_001_Result result;

		if (context.getCredentials() != null) {
			result = inforws.addLocationOp(addLocation, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.addLocationOp(addLocation, tools.getOrganizationCode(context), null, "",
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		return locationInfor.getLOCATIONID().getLOCATIONCODE();
	}

	public String updateLocation(InforContext context, Location locationParam) throws InforException {
		MP0318_GetLocation_001 getLocation = new MP0318_GetLocation_001();
		getLocation.setLOCATIONID(new LOCATIONID_Type());
		getLocation.getLOCATIONID().setLOCATIONCODE(locationParam.getCode());
		getLocation.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));

		MP0318_GetLocation_001_Result result = null;
		if (context.getCredentials() != null) {
			result = inforws.getLocationOp(getLocation, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.getLocationOp(getLocation, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		net.datastream.schemas.mp_entities.location_001.Location locationInfor = result.getResultData().getLocation();

		// Check Custom fields. If they change, or now we have them
		if (locationParam.getClassCode() != null && (locationInfor.getCLASSID() == null
				|| !locationParam.getClassCode().toUpperCase().equals(locationInfor.getCLASSID().getCLASSCODE()))) {
			locationInfor.setUSERDEFINEDAREA(
					tools.getCustomFieldsTools().getInforCustomFields(context, "LOC", locationParam.getClassCode().toUpperCase()));
		}

		locationInfor.setLocationParentHierarchy(getLocationParentHierarchy(context, locationParam));

		tools.getInforFieldTools().transformWSHubObject(locationInfor, locationParam, context);


		// call infor web service
		MP0319_SyncLocation_001 syncLocation = new MP0319_SyncLocation_001();
		syncLocation.setLocation(locationInfor);

		if (context.getCredentials() != null) {
			inforws.syncLocationOp(syncLocation, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.syncLocationOp(syncLocation, tools.getOrganizationCode(context), null, null,
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		return locationInfor.getLOCATIONID().getLOCATIONCODE();
	}

	@Override
	public String deleteLocation(InforContext context, String locationCode) throws InforException {
		MP0320_DeleteLocation_001 deleteLocation = new MP0320_DeleteLocation_001();
		deleteLocation.setLOCATIONID(new LOCATIONID_Type());
		deleteLocation.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
		deleteLocation.getLOCATIONID().setLOCATIONCODE(locationCode);

		if (context.getCredentials() != null) {
			inforws.deleteLocationOp(deleteLocation, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.deleteLocationOp(deleteLocation, "*", null, null, new Holder<>(tools.createInforSession(context)),
					tools.createMessageConfig(), tools.getTenant(context));
		}

		return locationCode;
	}

	private LocationParentHierarchy getLocationParentHierarchy(InforContext context, String locationCode) throws InforException {
		MP0361_GetLocationParentHierarchy_001 getLocationParentHierarchy = new MP0361_GetLocationParentHierarchy_001();
		getLocationParentHierarchy.setLOCATIONID(new LOCATIONID_Type());
		getLocationParentHierarchy.getLOCATIONID().setLOCATIONCODE(locationCode);
		getLocationParentHierarchy.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
		MP0361_GetLocationParentHierarchy_001_Result getLocationParentHierarchyResult = new MP0361_GetLocationParentHierarchy_001_Result();

		if (context.getCredentials() != null) {
			getLocationParentHierarchyResult = inforws.getLocationParentHierarchyOp(getLocationParentHierarchy, "*", tools.createSecurityHeader(context), "TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			getLocationParentHierarchyResult = inforws.getLocationParentHierarchyOp(getLocationParentHierarchy, "*", null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return getLocationParentHierarchyResult.getResultData().getLocationParentHierarchy();
	}

	private LocationParentHierarchy getLocationParentHierarchy(InforContext context, Location location) {
		if(location.getHierarchyLocationCode() == null)
			return null;

		LocationParentHierarchy locationParentHierarchy = new LocationParentHierarchy();

		TYPE_Type lType = new TYPE_Type();
		lType.setTYPECODE("L");
		lType.setDESCRIPTION("Localisation");

		locationParentHierarchy.setLOCATIONID(new LOCATIONID_Type());
		locationParentHierarchy.getLOCATIONID().setLOCATIONCODE(location.getCode());
		locationParentHierarchy.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));

		locationParentHierarchy.setTYPE(lType);

		ParentLocation parentLocation = new ParentLocation();
		parentLocation.setTYPE(lType);

		parentLocation.setLOCATIONID(new LOCATIONID_Type());
		parentLocation.getLOCATIONID().setLOCATIONCODE(location.getHierarchyLocationCode());
		parentLocation.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));

		parentLocation.setDEPARTMENTID(new DEPARTMENTID_Type());
		parentLocation.getDEPARTMENTID().setDEPARTMENTCODE("*");
		parentLocation.getDEPARTMENTID().setORGANIZATIONID(tools.getOrganization(context));

		locationParentHierarchy.setParentLocation(parentLocation);

		return locationParentHierarchy;
	}
}