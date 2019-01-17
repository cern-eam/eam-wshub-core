package ch.cern.eam.wshub.core.services.equipment.impl;

import javax.persistence.EntityManager;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.*;
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
	// BATCH
	//
	@Override
	public BatchResponse<String> createEquipmentBatch(InforContext inforContext, List<Equipment> equipmentList)
			throws InforException {
		List<Callable<String>> wos = equipmentList.stream()
				.<Callable<String>>map(equipment -> () -> createEquipment(inforContext, equipment))
				.collect(Collectors.toList());
		return tools.processCallables(wos);
	}

	@Override
	public BatchResponse<Equipment> readEquipmentBatch(InforContext inforContext, List<String> numbers) {
		List<Callable<Equipment>> wos = numbers.stream()
				.<Callable<Equipment>>map(number -> () -> readEquipment(inforContext, number))
				.collect(Collectors.toList());
		return tools.processCallables(wos);
	}

	@Override
	public BatchResponse<String> updateEquipmentBatch(InforContext inforContext, List<Equipment> equipmentList)
			throws InforException {
		List<Callable<String>> wos = equipmentList.stream()
				.<Callable<String>>map(
						equipment -> () -> updateEquipment(inforContext, equipment))
				.collect(Collectors.toList());

		return tools.processCallables(wos);
	}

	//
	// CRUD
	//
	@Override
	public String updateEquipment(InforContext inforContext, Equipment equipmentParam) throws InforException {
		if (equipmentParam.getTypeCode() == null || equipmentParam.getTypeCode().trim().equals("")) {
			EntityManager em = tools.getEntityManager();
			try {
				equipmentParam.setTypeCode(em.find(Equipment.class, equipmentParam.getCode()).getTypeCode());
			} catch (IllegalArgumentException | NullPointerException exception) {
				throw tools.generateFault("The equipment record couldn't be found.");
			} finally {
				em.close();
			}
		}

		switch (equipmentParam.getTypeCode()) {
		case "A":
			return assetService.updateAsset(inforContext, equipmentParam);
		case "P":
			return positionService.updatePosition(inforContext, equipmentParam);
		case "B": // Lot
		case "M": // Material
		case "R": // Route
		case "S": // System
			return systemService.updateSystem(inforContext, equipmentParam);
		default:
			throw tools.generateFault("Wrong equipment type.");
		}
	}

	@Override
	public String createEquipment(InforContext inforContext, Equipment equipmentParam) throws InforException {
		switch (equipmentParam.getTypeCode()) {
		case "A":
			return assetService.createAsset(inforContext, equipmentParam);
		case "P":
			return positionService.createPosition(inforContext, equipmentParam);
		case "B": // Lot
		case "M": // Material
		case "R": // Route
		case "S": // System
			return systemService.createSystem(inforContext, equipmentParam);
		default:
			throw tools.generateFault("Equipment type must be supplied with 'A', 'P', 'S' value.");
		}
	}

	@Override
	public Equipment readEquipment(InforContext inforContext, String equipmentCode)
			throws InforException {
		String equipmentType = null;
		EntityManager em = tools.getEntityManager();
		try {
			equipmentType = (em.find(Equipment.class, equipmentCode).getTypeCode());
		} catch (IllegalArgumentException | NullPointerException exception) {
			InforException inforException = tools.generateFault("The equipment record couldn't be found.");
			throw inforException;
		} finally {
			em.close();
		}

		switch (equipmentType) {
		case "A":
			return assetService.readAsset(inforContext, equipmentCode);
		case "P":
			return positionService.readPosition(inforContext, equipmentCode);
		case "B": // Lot
		case "M": // Material
		case "R": // Route
		case "S": // System
			return systemService.readSystem(inforContext, equipmentCode);
		case "L":
			return locationService.readLocation(inforContext, equipmentCode);
		default:
			return null;
		}
	}

	@Override
	public String deleteEquipment(InforContext inforContext, String equipmentCode)
			throws InforException {
		String equipmentType = null;
		EntityManager em = tools.getEntityManager();
		try {
			equipmentType = (em.find(Equipment.class, equipmentCode).getTypeCode());
		} catch (IllegalArgumentException | NullPointerException exception) {
			throw tools.generateFault("The equipment record couldn't be found.");
		} finally {
			em.close();
		}

		switch (equipmentType) {
		case "A":
			return assetService.deleteAsset(inforContext, equipmentCode);
		case "P":
			return positionService.deletePosition(inforContext, equipmentCode);
		case "S":
			return systemService.deleteSystem(inforContext, equipmentCode);
		default:
			throw tools.generateFault("Deletion of locations is not supported.");
		}
	}

}