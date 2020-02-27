package ch.cern.eam.wshub.core.services.equipment.impl;

import javax.persistence.EntityManager;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.equipment.*;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestFilter;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.GridTools;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.tools.InforException;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private GridsService gridsService;

    public EquipmentFacadeServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        this.assetService = new AssetServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
        this.positionService = new PositionServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
        this.systemService = new SystemServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
        this.locationService = new LocationServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
        this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
    }

    //
    // BATCH PROCESSING
    //
    @Override
    public BatchResponse<String> createEquipmentBatch(InforContext inforContext, List<Equipment> equipmentList) {
        return tools.batchOperation(inforContext, this::createEquipment, equipmentList);
    }

    @Override
    public BatchResponse<Equipment> readEquipmentBatch(InforContext inforContext, List<String> equipmentCodes) {
        return tools.batchOperation(inforContext, this::readEquipment, equipmentCodes);
    }

    @Override
    public BatchResponse<String> updateEquipmentBatch(InforContext inforContext, List<Equipment> equipmentList) {
        return tools.batchOperation(inforContext, this::updateEquipment, equipmentList);
    }

    @Override
    public BatchResponse<String> deleteEquipmentBatch(InforContext inforContext, List<String> equipmentCodes) {
        return tools.batchOperation(inforContext, this::deleteEquipment, equipmentCodes);
    }

    //
    // CRUD
    //
    @Override
    public String updateEquipment(InforContext inforContext, Equipment equipment) throws InforException {

        if (equipment.getTypeCode() == null) {
            equipment.setTypeCode(getEquipmentTypeCode(inforContext, equipment.getCode()));
        }

        switch (equipment.getTypeCode()) {
            case "A":
            case "PB":
            case "PM":
            case "T":
                return assetService.updateAsset(inforContext, equipment);
            case "P":
                return positionService.updatePosition(inforContext, equipment);
            case "B": // Lot
            case "M": // Material
            case "R": // Route
            case "S": // System
                return systemService.updateSystem(inforContext, equipment);
            case "L": // Location
                throw tools.generateFault("Locations are not available here. Use LocationService.");
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
            case "PB":
            case "PM":
            case "T":
                return assetService.createAsset(inforContext, equipment);
            case "P":
                return positionService.createPosition(inforContext, equipment);
            case "B": // Lot
            case "M": // Material
            case "R": // Route
            case "S": // System
                return systemService.createSystem(inforContext, equipment);
            case "L": // Location
                throw tools.generateFault("Locations are not available here. Use LocationService.");
            default:
                throw tools.generateFault("Equipment type not recognized.");
        }
    }

    @Override
    public Equipment readEquipment(InforContext inforContext, String equipmentCode)
            throws InforException {

        String equipmentTypeCode = getEquipmentTypeCode(inforContext, equipmentCode);

        switch (equipmentTypeCode) {
            case "A":
            case "PB":
            case "PM":
            case "T":
                return assetService.readAsset(inforContext, equipmentCode);
            case "P":
                return positionService.readPosition(inforContext, equipmentCode);
            case "B": // Lot
            case "M": // Material
            case "R": // Route
            case "S": // System
                return systemService.readSystem(inforContext, equipmentCode);
            case "L": // Location
                throw tools.generateFault("Locations are no longer available here. Use LocationService.");
            default:
                throw tools.generateFault("Equipment type not recognized.");
        }
    }

    @Override
    public String deleteEquipment(InforContext inforContext, String equipmentCode) throws InforException {

        String equipmentTypeCode = getEquipmentTypeCode(inforContext, equipmentCode);

        switch (equipmentTypeCode) {
            case "A":
            case "PB":
            case "PM":
            case "T":
                return assetService.deleteAsset(inforContext, equipmentCode);
            case "P":
                return positionService.deletePosition(inforContext, equipmentCode);
            case "B": // Lot
            case "M": // Material
            case "R": // Route
            case "S": // System
                return systemService.deleteSystem(inforContext, equipmentCode);
            case "L": // Location
                throw tools.generateFault("Locations are no longer available here. Use LocationService.");
            default:
                throw tools.generateFault("Equipment type not recognized.");
        }
    }

    private String getEquipmentTypeCode(InforContext inforContext, String equipmentCode) throws InforException {
        //TODO: Find more suitable grid to fetch equipment type
        Map<String, String> types = new HashMap<>();
        types.put("Equipement", "A");
        types.put("Asset", "A");
        types.put("Position", "P");
        types.put("Position fonctionelle", "P");
        types.put("System", "S");
        types.put("Systeme", "S");
        types.put("Lot", "B");
        types.put("Route", "R");
        types.put("Material", "M");

        GridRequest gridRequest = new GridRequest("LVREPCOGALLEQUIPMENT", GridRequest.GRIDTYPE.LOV);
        gridRequest.getGridRequestFilters().add(new GridRequestFilter("code", equipmentCode, "="));
        GridRequestResult requestResult = gridsService.executeQuery(inforContext, gridRequest);

        if(requestResult.getRows().length != 1) {
            throw tools.generateFault("The equipment record couldn't be found.");
        }

        String type = types.get(GridTools.getCellContent("type", requestResult.getRows()[0]));

        if(type == null) {
            throw tools.generateFault("This code does not correspond to an equipment.");
        }

        return type;
    }

}