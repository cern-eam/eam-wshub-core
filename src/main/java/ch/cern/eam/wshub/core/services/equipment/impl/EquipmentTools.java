package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestFilter;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;

import static ch.cern.eam.wshub.core.tools.GridTools.convertGridResultToMap;
import static ch.cern.eam.wshub.core.tools.GridTools.extractSingleResult;

import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EquipmentTools {

    public static final Map<String, String> equimentSystemTypesCache = new ConcurrentHashMap<>();
    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;
    private GridsService gridsService;

    public EquipmentTools(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
        this.gridsService = new GridsServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
    }

    public String getEquipmentSystemTypeForUserType(EAMContext eamContext, String equipmentUserType) throws EAMException {
        if (equimentSystemTypesCache.containsKey(equipmentUserType)) {
            return equimentSystemTypesCache.get(equipmentUserType);
        }

        GridRequest gridRequest = new GridRequest("BSUCOD_HDR", GridRequest.GRIDTYPE.LIST);
        gridRequest.addParam("param.entitycode", "OBTP");
        GridRequestResult gridRequestResult = gridsService.executeQuery(eamContext, gridRequest);
        equimentSystemTypesCache.putAll(convertGridResultToMap("usercode", "systemcode", gridRequestResult));
        // Cache the descriptions temporarily till we find a better grid for getEquipmentSystemTypeForEquipment
        equimentSystemTypesCache.putAll(convertGridResultToMap("usercodedescription", "systemcode", gridRequestResult));

        return equimentSystemTypesCache.get(equipmentUserType);
    }

    public String getEquipmentSystemTypeForEquipment(EAMContext eamContext, String equipmentCode, String organization) throws EAMException {
        GridRequest gridRequest = new GridRequest("OCOBJC", GridRequest.GRIDTYPE.LIST);
        gridRequest.addFilter("obj_code", equipmentCode, "=", GridRequestFilter.JOINER.AND);
        gridRequest.addFilter("obj_org", tools.getOrganizationCode(eamContext, organization), "=");
        gridRequest.addParam("parameter.lastupdated", "31-JAN-1970");
        String systemType = extractSingleResult(gridsService.executeQuery(eamContext, gridRequest), "obj_obrtype");

        if(systemType == null) {
            throw tools.generateFault("The equipment record couldn't be found.");
        }

        return systemType;
    }

}
