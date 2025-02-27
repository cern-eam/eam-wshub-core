package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.equipment.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.tools.EAMException;
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.Tools.*;

public class EquipmentFacadeServiceImpl implements EquipmentFacadeService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;
    private AssetService assetService;
    private PositionService positionService;
    private SystemService systemService;
    private EquipmentTools equipmentTools;

    public EquipmentFacadeServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
        this.assetService = new AssetServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
        this.positionService = new PositionServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
        this.systemService = new SystemServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
        this.equipmentTools = new EquipmentTools(applicationData, tools, eamWebServicesToolkitClient);
    }

    //
    // BATCH PROCESSING
    //
    @Override
    public BatchResponse<String> createEquipmentBatch(EAMContext eamContext, List<Equipment> equipmentList) {
        return tools.batchOperation(eamContext, this::createEquipment, equipmentList);
    }

    @Override
    public BatchResponse<Equipment> readEquipmentBatch(EAMContext eamContext, List<String> equipmentCodes) {
        return tools.batchOperation(eamContext, this::readEquipment, equipmentCodes);
    }

    @Override
    public BatchResponse<String> updateEquipmentBatch(EAMContext eamContext, List<Equipment> equipmentList) {
        return tools.batchOperation(eamContext, this::updateEquipment, equipmentList);
    }

    @Override
    public BatchResponse<String> deleteEquipmentBatch(EAMContext eamContext, List<String> equipmentCodes) {
        return tools.batchOperation(eamContext, this::deleteEquipment, equipmentCodes);
    }

    //
    // CRUD
    //
    @Override
    public String updateEquipment(EAMContext eamContext, Equipment equipment) throws EAMException {
        //New Custom Field API
        final Map<String, String> customFieldMap = equipment.getCustomFieldMap();
        if (customFieldMap != null) {
            final CustomField[] customFields =
                    customFieldMap.entrySet().stream().map(s -> new CustomField(s.getKey(), s.getValue())).toArray(CustomField[]::new);
            equipment.setCustomFields(customFields);
        }

        if (equipment.getSystemTypeCode() == null) {
            equipment.setSystemTypeCode(equipmentTools.getEquipmentSystemTypeForEquipment(eamContext, equipment.getCode(), equipment.getOrganization()));
        }

        switch (equipment.getSystemTypeCode()) {
            case "A":
                return assetService.updateAsset(eamContext, equipment);
            case "P":
                return positionService.updatePosition(eamContext, equipment);
            case "S": // System
                return systemService.updateSystem(eamContext, equipment);
            case "L": // Location
                throw tools.generateFault("Locations are not available here. Use LocationService.");
            default:
                throw tools.generateFault("Wrong equipment type.");
        }
    }

    @Override
    public String createEquipment(EAMContext eamContext, Equipment equipment) throws EAMException {
        if (equipment.getTypeCode() == null) {
            throw tools.generateFault("Equipment type can not be empty.");
        }

        //New Custom Field API
        final Map<String, String> customFieldMap = equipment.getCustomFieldMap();
        if (customFieldMap != null) {
            final CustomField[] customFields =
                    customFieldMap.entrySet().stream().map(s -> new CustomField(s.getKey(), s.getValue())).toArray(CustomField[]::new);
            equipment.setCustomFields(customFields);
        }

        if (equipment.getSystemTypeCode() == null) {
            equipment.setSystemTypeCode(equipmentTools.getEquipmentSystemTypeForUserType(eamContext, equipment.getTypeCode()));
        }

        switch (equipment.getSystemTypeCode()) {
            case "A":
                return assetService.createAsset(eamContext, equipment);
            case "P":
                return positionService.createPosition(eamContext, equipment);
            case "S": // System
                return systemService.createSystem(eamContext, equipment);
            case "L": // Location
                throw tools.generateFault("Locations are not available here. Use LocationService.");
            default:
                throw tools.generateFault("Equipment type not recognized.");
        }
    }

    @Override
    public Equipment readEquipment(EAMContext eamContext, String equipmentCode) throws EAMException {
        String code = extractEntityCode(equipmentCode);
        String organization = extractOrganizationCode(equipmentCode);

        String equipmentTypeCode = equipmentTools.getEquipmentSystemTypeForEquipment(eamContext, code, organization);

        Equipment equipment = switch (equipmentTypeCode) {
            case "A" -> assetService.readAsset(eamContext, code, organization);
            case "P" -> positionService.readPosition(eamContext, code, organization);
            case "S" -> // System
                    systemService.readSystem(eamContext, code, organization);
            case "L" -> // Location
                    throw tools.generateFault("Locations are no longer available here. Use LocationService.");
            default -> throw tools.generateFault("Equipment type not recognized.");
        };

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
    public String deleteEquipment(EAMContext eamContext, String equipmentCode) throws EAMException {
        String code = extractEntityCode(equipmentCode);
        String organization = extractOrganizationCode(equipmentCode);

        String equipmentTypeCode = equipmentTools.getEquipmentSystemTypeForEquipment(eamContext, code, organization);

        switch (equipmentTypeCode) {
            case "A":
                return assetService.deleteAsset(eamContext, code, organization);
            case "P":
                return positionService.deletePosition(eamContext, code, organization);
            case "S": // System
                return systemService.deleteSystem(eamContext, code, organization);
            case "L": // Location
                throw tools.generateFault("Locations are no longer available here. Use LocationService.");
            default:
                throw tools.generateFault("Equipment type not recognized.");
        }
    }

    @Override
    public String readEquipmentType(EAMContext eamContext, String equipmentCode) throws EAMException {
        return equipmentTools.getEquipmentSystemTypeForEquipment(eamContext, extractEntityCode(equipmentCode), extractOrganizationCode(equipmentCode));
    }

}