package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.LocationService;
import ch.cern.eam.wshub.core.services.equipment.entities.Location;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.location_001.LocationParentHierarchy;
import net.datastream.schemas.mp_entities.location_001.ParentLocation;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0317_001.MP0317_AddLocation_001;
import net.datastream.schemas.mp_functions.mp0318_001.MP0318_GetLocation_001;
import net.datastream.schemas.mp_functions.mp0319_001.MP0319_SyncLocation_001;
import net.datastream.schemas.mp_functions.mp0320_001.MP0320_DeleteLocation_001;
import net.datastream.schemas.mp_functions.mp0361_001.MP0361_GetLocationParentHierarchy_001;
import net.datastream.schemas.mp_results.mp0318_001.MP0318_GetLocation_001_Result;
import net.datastream.schemas.mp_results.mp0361_001.MP0361_GetLocationParentHierarchy_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import java.util.List;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;

public class LocationServiceImpl implements LocationService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public LocationServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}

	//
	// BATCH WEB SERVICES
	//

	public BatchResponse<String> createLocationBatch(EAMContext context, List<Location> locations) {
		return tools.batchOperation(context, this::createLocation, locations);
	}

	public BatchResponse<Location> readLocationBatch(EAMContext context, List<String> locationCodes) {
		return tools.batchOperation(context, this::readLocation, locationCodes);
	}

	public BatchResponse<String> updateLocationBatch(EAMContext context, List<Location> locations) {
		return tools.batchOperation(context, this::updateLocation, locations);
	}

	public BatchResponse<String> deleteLocationBatch(EAMContext context, List<String> locationCodes) {
		return tools.batchOperation(context, this::deleteLocation, locationCodes);
	}

	public Location readLocation(EAMContext context, String locationCode) throws EAMException
	{
		MP0318_GetLocation_001 getLocation = new MP0318_GetLocation_001();
		getLocation.setLOCATIONID(new LOCATIONID_Type());
		getLocation.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
		getLocation.getLOCATIONID().setLOCATIONCODE(locationCode);
		MP0318_GetLocation_001_Result getLocationResult = new MP0318_GetLocation_001_Result();

		getLocationResult = tools.performEAMOperation(context, eamws::getLocationOp, getLocation);

		net.datastream.schemas.mp_entities.location_001.Location locationEAM =
			getLocationResult.getResultData().getLocation();

		Location location = tools.getEAMFieldTools().transformEAMObject(new Location(), locationEAM, context);

		if(locationEAM.getParentLocationID() != null) {
			location.setHierarchyLocationCode(locationEAM.getParentLocationID().getLOCATIONCODE());
		}

		return location;
	}

	public String createLocation(EAMContext context, Location locationParam) throws EAMException {
		net.datastream.schemas.mp_entities.location_001.Location locationEAM =
			new net.datastream.schemas.mp_entities.location_001.Location();

		locationEAM.setLOCATIONID(new LOCATIONID_Type());
		locationEAM.getLOCATIONID().setLOCATIONCODE(locationParam.getCode());
		locationEAM.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));

		locationEAM.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
			context,
			toCodeString(locationEAM.getCLASSID()),
			locationEAM.getUSERDEFINEDAREA(),
			locationParam.getClassCode(),
			"LOC"));

		tools.getEAMFieldTools().transformWSHubObject(locationEAM, locationParam, context);

		if(locationParam.getHierarchyLocationCode() != null) {
			locationEAM.setLocationParentHierarchy(getLocationParentHierarchy(context, locationParam));
		}

		MP0317_AddLocation_001 addLocation = new MP0317_AddLocation_001();
		addLocation.setLocation(locationEAM);
		tools.performEAMOperation(context, eamws::addLocationOp, addLocation);

		return locationEAM.getLOCATIONID().getLOCATIONCODE();
	}

	public String updateLocation(EAMContext context, Location locationParam) throws EAMException {
		MP0318_GetLocation_001 getLocation = new MP0318_GetLocation_001();
		getLocation.setLOCATIONID(new LOCATIONID_Type());
		getLocation.getLOCATIONID().setLOCATIONCODE(locationParam.getCode());
		getLocation.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));

		MP0318_GetLocation_001_Result result =
			tools.performEAMOperation(context, eamws::getLocationOp, getLocation);

		net.datastream.schemas.mp_entities.location_001.Location locationEAM = result.getResultData().getLocation();

		locationEAM.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
			context,
			toCodeString(locationEAM.getCLASSID()),
			locationEAM.getUSERDEFINEDAREA(),
			locationParam.getClassCode(),
			"LOC"));

		if(locationParam.getHierarchyLocationCode() == null) {
			locationParam.setHierarchyLocationCode(toCodeString(locationEAM.getParentLocationID()));
		}

		if(locationParam.getHierarchyLocationCode() != null && !locationParam.getHierarchyLocationCode().equals("")) {
			locationEAM.setLocationParentHierarchy(getLocationParentHierarchy(context, locationParam));
		}

		locationEAM.setParentLocationID(null);

		tools.getEAMFieldTools().transformWSHubObject(locationEAM, locationParam, context);

		// call eam web service
		MP0319_SyncLocation_001 syncLocation = new MP0319_SyncLocation_001();
		syncLocation.setLocation(locationEAM);

		tools.performEAMOperation(context, eamws::syncLocationOp, syncLocation);

		return locationEAM.getLOCATIONID().getLOCATIONCODE();
	}

	@Override
	public String deleteLocation(EAMContext context, String locationCode) throws EAMException {
		MP0320_DeleteLocation_001 deleteLocation = new MP0320_DeleteLocation_001();
		deleteLocation.setLOCATIONID(new LOCATIONID_Type());
		deleteLocation.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
		deleteLocation.getLOCATIONID().setLOCATIONCODE(locationCode);

		tools.performEAMOperation(context, eamws::deleteLocationOp, deleteLocation);

		return locationCode;
	}

	private LocationParentHierarchy getLocationParentHierarchy(EAMContext context, String locationCode) throws EAMException {
		MP0361_GetLocationParentHierarchy_001 getLocationParentHierarchy = new MP0361_GetLocationParentHierarchy_001();
		getLocationParentHierarchy.setLOCATIONID(new LOCATIONID_Type());
		getLocationParentHierarchy.getLOCATIONID().setLOCATIONCODE(locationCode);
		getLocationParentHierarchy.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));

		MP0361_GetLocationParentHierarchy_001_Result getLocationParentHierarchyResult =
			tools.performEAMOperation(context, eamws::getLocationParentHierarchyOp, getLocationParentHierarchy);

		return getLocationParentHierarchyResult.getResultData().getLocationParentHierarchy();
	}

	private LocationParentHierarchy getLocationParentHierarchy(EAMContext context, Location location) {
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