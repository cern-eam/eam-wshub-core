package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestFilter;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;

import static ch.cern.eam.wshub.core.tools.GridTools.convertGridResultToMap;
import static ch.cern.eam.wshub.core.tools.GridTools.extractSingleResult;

import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EquipmentTools {

    public static final Map<String, String> equimentSystemTypesCache = new ConcurrentHashMap<>();
    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;
    private GridsService gridsService;

    public EquipmentTools(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
    }

    public String getEquipmentSystemTypeForUserType(InforContext inforContext, String equipmentUserType) throws InforException {
        if (equimentSystemTypesCache.containsKey(equipmentUserType)) {
            return equimentSystemTypesCache.get(equipmentUserType);
        }

        GridRequest gridRequest = new GridRequest("BSUCOD_HDR", GridRequest.GRIDTYPE.LIST);
        gridRequest.addParam("param.entitycode", "OBTP");
        GridRequestResult gridRequestResult = gridsService.executeQuery(inforContext, gridRequest);
        equimentSystemTypesCache.putAll(convertGridResultToMap("usercode", "systemcode", gridRequestResult));
        // Cache the descriptions temporarily till we find a better grid for getEquipmentSystemTypeForEquipment
        equimentSystemTypesCache.putAll(convertGridResultToMap("usercodedescription", "systemcode", gridRequestResult));

        return equimentSystemTypesCache.get(equipmentUserType);
    }

    public String getEquipmentSystemTypeForEquipment(InforContext inforContext, String equipmentCode, String organization) throws InforException {
        GridRequest gridRequest = new GridRequest("OCOBJC", GridRequest.GRIDTYPE.LIST);
        gridRequest.addFilter("obj_code", equipmentCode, "=", GridRequestFilter.JOINER.AND);
        gridRequest.addFilter("obj_org", tools.getOrganizationCode(inforContext, organization), "=");
        gridRequest.addParam("parameter.lastupdated", "31-JAN-1970");
        final GridRequestResult gridRequestResult1 = gridsService.executeQuery(inforContext, gridRequest);
        String systemType = extractSingleResult(gridRequestResult1, "obj_obrtype");

        if (systemType == null) {
            GridRequest gridRequest2 = new GridRequest("OSOBJL", GridRequest.GRIDTYPE.LIST);
            gridRequest2.addFilter("equipmentno", equipmentCode, "=", GridRequestFilter.JOINER.AND);
            gridRequest2.addFilter("organization", tools.getOrganizationCode(inforContext, organization), "=");
            final GridRequestResult gridRequestResult = gridsService.executeQuery(inforContext, gridRequest2);
            if (gridRequestResult.getRows().length > 0) {
                return "L";
            }
            throw Tools.generateFault("The equipment record couldn't be found.");
        }

        return systemType;
    }

}
