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
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0317_001.MP0317_AddLocation_001;
import net.datastream.schemas.mp_functions.mp0318_001.MP0318_GetLocation_001;
import net.datastream.schemas.mp_functions.mp0319_001.MP0319_SyncLocation_001;
import net.datastream.schemas.mp_functions.mp0320_001.MP0320_DeleteLocation_001;
import net.datastream.schemas.mp_functions.mp0361_001.MP0361_GetLocationParentHierarchy_001;
import net.datastream.schemas.mp_results.mp0318_001.MP0318_GetLocation_001_Result;
import net.datastream.schemas.mp_results.mp0361_001.MP0361_GetLocationParentHierarchy_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import java.util.List;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;

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

	public BatchResponse<String> createLocationBatch(InforContext context, List<Location> locations) {
		return tools.batchOperation(context, this::createLocation, locations);
	}

	public BatchResponse<Location> readLocationBatch(InforContext context, List<String> locationCodes) {
		return tools.batchOperation(context, this::readLocation, locationCodes);
	}

	public BatchResponse<String> updateLocationBatch(InforContext context, List<Location> locations) {
		return tools.batchOperation(context, this::updateLocation, locations);
	}

	public BatchResponse<String> deleteLocationBatch(InforContext context, List<String> locationCodes) {
		return tools.batchOperation(context, this::deleteLocation, locationCodes);
	}

	public Location readLocation(InforContext context, String locationCode) throws InforException
	{
		MP0318_GetLocation_001 getLocation = new MP0318_GetLocation_001();
		getLocation.setLOCATIONID(new LOCATIONID_Type());
		getLocation.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
		getLocation.getLOCATIONID().setLOCATIONCODE(locationCode);
		MP0318_GetLocation_001_Result getLocationResult = new MP0318_GetLocation_001_Result();

		getLocationResult = tools.performInforOperation(context, inforws::getLocationOp, getLocation);

		net.datastream.schemas.mp_entities.location_001.Location locationInfor =
			getLocationResult.getResultData().getLocation();

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

		locationInfor.setUSERDEFINEDAREA(tools.getInforCustomFields(
			context,
			toCodeString(locationInfor.getCLASSID()),
			locationInfor.getUSERDEFINEDAREA(),
			locationParam.getClassCode(),
			"LOC"));

		tools.getInforFieldTools().transformWSHubObject(locationInfor, locationParam, context);

		if(locationParam.getHierarchyLocationCode() != null) {
			locationInfor.setLocationParentHierarchy(getLocationParentHierarchy(context, locationParam));
		}

		MP0317_AddLocation_001 addLocation = new MP0317_AddLocation_001();
		addLocation.setLocation(locationInfor);
		tools.performInforOperation(context, inforws::addLocationOp, addLocation);

		return locationInfor.getLOCATIONID().getLOCATIONCODE();
	}

	public String updateLocation(InforContext context, Location locationParam) throws InforException {
		MP0318_GetLocation_001 getLocation = new MP0318_GetLocation_001();
		getLocation.setLOCATIONID(new LOCATIONID_Type());
		getLocation.getLOCATIONID().setLOCATIONCODE(locationParam.getCode());
		getLocation.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));

		MP0318_GetLocation_001_Result result =
			tools.performInforOperation(context, inforws::getLocationOp, getLocation);

		net.datastream.schemas.mp_entities.location_001.Location locationInfor = result.getResultData().getLocation();

		locationInfor.setUSERDEFINEDAREA(tools.getInforCustomFields(
			context,
			toCodeString(locationInfor.getCLASSID()),
			locationInfor.getUSERDEFINEDAREA(),
			locationParam.getClassCode(),
			"LOC"));

		if(locationParam.getHierarchyLocationCode() == null) {
			locationParam.setHierarchyLocationCode(toCodeString(locationInfor.getParentLocationID()));
		}

		if(locationParam.getHierarchyLocationCode() != null && !locationParam.getHierarchyLocationCode().equals("")) {
			locationInfor.setLocationParentHierarchy(getLocationParentHierarchy(context, locationParam));
		}

		locationInfor.setParentLocationID(null);

		tools.getInforFieldTools().transformWSHubObject(locationInfor, locationParam, context);

		// call infor web service
		MP0319_SyncLocation_001 syncLocation = new MP0319_SyncLocation_001();
		syncLocation.setLocation(locationInfor);

		tools.performInforOperation(context, inforws::syncLocationOp, syncLocation);

		return locationInfor.getLOCATIONID().getLOCATIONCODE();
	}

	@Override
	public String deleteLocation(InforContext context, String locationCode) throws InforException {
		MP0320_DeleteLocation_001 deleteLocation = new MP0320_DeleteLocation_001();
		deleteLocation.setLOCATIONID(new LOCATIONID_Type());
		deleteLocation.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
		deleteLocation.getLOCATIONID().setLOCATIONCODE(locationCode);

		tools.performInforOperation(context, inforws::deleteLocationOp, deleteLocation);

		return locationCode;
	}

	private LocationParentHierarchy getLocationParentHierarchy(InforContext context, String locationCode) throws InforException {
		MP0361_GetLocationParentHierarchy_001 getLocationParentHierarchy = new MP0361_GetLocationParentHierarchy_001();
		getLocationParentHierarchy.setLOCATIONID(new LOCATIONID_Type());
		getLocationParentHierarchy.getLOCATIONID().setLOCATIONCODE(locationCode);
		getLocationParentHierarchy.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));

		MP0361_GetLocationParentHierarchy_001_Result getLocationParentHierarchyResult =
			tools.performInforOperation(context, inforws::getLocationParentHierarchyOp, getLocationParentHierarchy);

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