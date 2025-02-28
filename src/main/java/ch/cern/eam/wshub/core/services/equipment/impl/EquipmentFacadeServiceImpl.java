package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.equipment.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.tools.InforException;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.Tools.*;

public class EquipmentFacadeServiceImpl implements EquipmentFacadeService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;
    private AssetService assetService;
    private PositionService positionService;
    private SystemService systemService;
    private EquipmentTools equipmentTools;

    public EquipmentFacadeServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        this.assetService = new AssetServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
        this.positionService = new PositionServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
        this.systemService = new SystemServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
        this.equipmentTools = new EquipmentTools(applicationData, tools, inforWebServicesToolkitClient);
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

        //New Custom Field API
        final Map<String, String> customFieldMap = equipment.getCustomFieldMap();
        if (customFieldMap != null) {
            final CustomField[] customFields =
                    customFieldMap.entrySet().stream().map(s -> new CustomField(s.getKey(), s.getValue())).toArray(CustomField[]::new);
            equipment.setCustomFields(customFields);
        }

        if (equipment.getSystemTypeCode() == null) {
            equipment.setSystemTypeCode(equipmentTools.getEquipmentSystemTypeForEquipment(inforContext, equipment.getCode(), equipment.getOrganization()));
        }

        switch (equipment.getSystemTypeCode()) {
            case "A":
                return assetService.updateAsset(inforContext, equipment);
            case "P":
                return positionService.updatePosition(inforContext, equipment);
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

        final Map<String, String> customFieldMap = equipment.getCustomFieldMap();
        if (customFieldMap != null) {
            final CustomField[] customFields =
                    customFieldMap.entrySet().stream().map(s -> new CustomField(s.getKey(), s.getValue())).toArray(CustomField[]::new);
            equipment.setCustomFields(customFields);
        }

        if (equipment.getSystemTypeCode() == null) {
            equipment.setSystemTypeCode(equipmentTools.getEquipmentSystemTypeForUserType(inforContext, equipment.getTypeCode()));
        }

        switch (equipment.getSystemTypeCode()) {
            case "A":
                return assetService.createAsset(inforContext, equipment);
            case "P":
                return positionService.createPosition(inforContext, equipment);
            case "S": // System
                return systemService.createSystem(inforContext, equipment);
            case "L": // Location
                throw tools.generateFault("Locations are not available here. Use LocationService.");
            default:
                throw tools.generateFault("Equipment type not recognized.");
        }
    }

    @Override
    public Equipment readEquipment(InforContext inforContext, String equipmentCode) throws InforException {
        String code = extractEntityCode(equipmentCode);
        String organization = extractOrganizationCode(equipmentCode);

        String equipmentTypeCode = equipmentTools.getEquipmentSystemTypeForEquipment(inforContext, code, organization);

        Equipment equipment ;
        switch (equipmentTypeCode) {
            case "A":
                equipment = assetService.readAsset(inforContext, code, organization);
                break;
            case "P":
                equipment = positionService.readPosition(inforContext, code, organization);
                break;
            case "S": // System
                equipment = systemService.readSystem(inforContext, code, organization);
                break;
            case "L": // Location
                throw tools.generateFault("Locations are no longer available here. Use LocationService.");
            default:
                throw tools.generateFault("Equipment type not recognized.");
        }

        //New Custom Field API
        final CustomField[] customFields = equipment.getCustomFields();
        if (customFields != null) {
            final Map<String, String> collect =
                    Arrays.stream(customFields).collect(Collectors.toMap(CustomField::getCode,
                            s -> s.getValue() != null ? s.getValue() : ""));
            equipment.setCustomFieldMap(collect);
        }

        return equipment;
    }

    @Override
    public String deleteEquipment(InforContext inforContext, String equipmentCode) throws InforException {
        String code = extractEntityCode(equipmentCode);
        String organization = extractOrganizationCode(equipmentCode);

        String equipmentTypeCode = equipmentTools.getEquipmentSystemTypeForEquipment(inforContext, code, organization);

        switch (equipmentTypeCode) {
            case "A":
                return assetService.deleteAsset(inforContext, code, organization);
            case "P":
                return positionService.deletePosition(inforContext, code, organization);
            case "S": // System
                return systemService.deleteSystem(inforContext, code, organization);
            case "L": // Location
                throw tools.generateFault("Locations are no longer available here. Use LocationService.");
            default:
                throw tools.generateFault("Equipment type not recognized.");
        }
    }

    @Override
    public String readEquipmentType(InforContext inforContext, String equipmentCode) throws InforException {
        return equipmentTools.getEquipmentSystemTypeForEquipment(inforContext, extractEntityCode(equipmentCode), extractOrganizationCode(equipmentCode));
    }

}