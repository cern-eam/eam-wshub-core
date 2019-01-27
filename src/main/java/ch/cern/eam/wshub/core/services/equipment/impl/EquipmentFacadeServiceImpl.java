package ch.cern.eam.wshub.core.services.equipment.impl;

import javax.persistence.EntityManager;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.tools.InforException;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class EquipmentFacadeServiceImpl implements EquipmentFacadeService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;
	private AssetService assetService;
	private PositionService positionService;
	private SystemService systemService;
	private LocationService locationService;

	public EquipmentFacadeServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.assetService = new AssetServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
		this.positionService = new PositionServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
		this.systemService = new SystemServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
		this.locationService = new LocationServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
	}

	//
	// BATCH PROCESSING
	//
	@Override
	public BatchResponse<String> createEquipmentBatch(InforContext inforContext, List<Equipment> equipmentList)
			throws InforException {
		List<Callable<String>> callableList = equipmentList.stream()
				.<Callable<String>>map(equipment -> () -> createEquipment(inforContext, equipment))
				.collect(Collectors.toList());
		return tools.processCallables(callableList);
	}

	@Override
	public BatchResponse<Equipment> readEquipmentBatch(InforContext inforContext, List<Equipment> equipmentList) {
		List<Callable<Equipment>> callableList = equipmentList.stream()
				.<Callable<Equipment>>map(number -> () -> readEquipment(inforContext, number))
				.collect(Collectors.toList());
		return tools.processCallables(callableList);
	}

	@Override
	public BatchResponse<String> updateEquipmentBatch(InforContext inforContext, List<Equipment> equipmentList)
			throws InforException {
		List<Callable<String>> callableList = equipmentList.stream()
				.<Callable<String>>map(equipment -> () -> updateEquipment(inforContext, equipment))
				.collect(Collectors.toList());

		return tools.processCallables(callableList);
	}

	@Override
	public BatchResponse<String> deleteEquipmentBatch(InforContext inforContext, List<Equipment> equipmentList)
			throws InforException {
		List<Callable<String>> callableList = equipmentList.stream()
				.<Callable<String>>map(equipment -> () -> deleteEquipment(inforContext, equipment))
				.collect(Collectors.toList());

		return tools.processCallables(callableList);
	}

	//
	// CRUD
	//
	@Override
	public String updateEquipment(InforContext inforContext, Equipment equipment) throws InforException {

		fetchAndPopulateEquipmentTypeCode(equipment);

		switch (equipment.getTypeCode()) {
		case "A":
			return assetService.updateAsset(inforContext, equipment);
		case "P":
			return positionService.updatePosition(inforContext, equipment);
		case "B": // Lot
		case "M": // Material
		case "R": // Route
		case "S": // System
			return systemService.updateSystem(inforContext, equipment);
		default:
			throw tools.generateFault("Wrong equipment type.");
		}
	}

	@Override
	public String createEquipment(InforContext inforContext, Equipment equipment) throws InforException {
		if (equipment.getTypeCode() == null) {
			throw tools.generateFault("Equipment type can not be empty.");
		}
		switch (equipment.getTypeCode()) {
		case "A":
			return assetService.createAsset(inforContext, equipment);
		case "P":
			return positionService.createPosition(inforContext, equipment);
		case "B": // Lot
		case "M": // Material
		case "R": // Route
		case "S": // System
			return systemService.createSystem(inforContext, equipment);
		default:
			throw tools.generateFault("Equipment type not recognized.");
		}
	}

	@Override
	public Equipment readEquipment(InforContext inforContext, Equipment equipment)
			throws InforException {

		fetchAndPopulateEquipmentTypeCode(equipment);

		switch (equipment.getTypeCode()) {
		case "A":
			return assetService.readAsset(inforContext, equipment.getCode());
		case "P":
			return positionService.readPosition(inforContext, equipment.getCode());
		case "B": // Lot
		case "M": // Material
		case "R": // Route
		case "S": // System
			return systemService.readSystem(inforContext, equipment.getCode());
		case "L":
			return locationService.readLocation(inforContext, equipment.getCode());
		default:
			throw tools.generateFault("Equipment type not recognized.");
		}
	}

	@Override
	public String deleteEquipment(InforContext inforContext, Equipment equipment) throws InforException {

		fetchAndPopulateEquipmentTypeCode(equipment);

		switch (equipment.getTypeCode()) {
		case "A":
			return assetService.deleteAsset(inforContext, equipment.getCode());
		case "P":
			return positionService.deletePosition(inforContext, equipment.getCode());
		case "B": // Lot
		case "M": // Material
		case "R": // Route
		case "S": // System
			return systemService.deleteSystem(inforContext, equipment.getCode());
		case "L":
			throw tools.generateFault("Deletion of locations is not supported.");
		default:
			throw tools.generateFault("Equipment type not recognized.");
		}
	}

	private void fetchAndPopulateEquipmentTypeCode(Equipment equipment) throws InforException {
		if (equipment.getTypeCode() == null || equipment.getTypeCode().trim().equals("")) {
			tools.demandDatabaseConnection();
			EntityManager em = tools.getEntityManager();
			try {
				equipment.setCode(em.find(Equipment.class, equipment.getCode()).getTypeCode());
			} catch (IllegalArgumentException | NullPointerException exception) {
				throw tools.generateFault("The equipment record couldn't be found.");
			} finally {
				em.close();
			}
		}
	}

}