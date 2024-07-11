package ch.cern.eam.wshub.core.services.userdefinedscreens.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedScreenService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.encodeAmount;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.encodeEAMDate;
import net.datastream.schemas.mp_fields.USERDEFINEDSCREENFIELDDATA_Type;
import net.datastream.schemas.mp_fields.USERDEFINEDSCREENFIELDVALUELIST;
import net.datastream.schemas.mp_fields.USERDEFINEDSCREENFIELDVALUEPAIR;
import net.datastream.schemas.mp_functions.mp6441_001.MP6441_ProcessUserDefinedScreenService_001;
import net.datastream.schemas.mp_results.mp6441_001.MP6441_ProcessUserDefinedScreenService_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserDefinedScreenServiceImpl implements UserDefinedScreenService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public UserDefinedScreenServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
    }

    public String createUserDefinedScreenRow(EAMContext eamContext, String screenName, UDTRow udtRow) throws EAMException {
        MP6441_ProcessUserDefinedScreenService_001 userDefinedScreenService = initUserDefinedScreenService(screenName, "ADD");
        addFields(userDefinedScreenService, udtRow);
        tools.performEAMOperation(eamContext, eamws::processUserDefinedScreenServiceOp, userDefinedScreenService);
        return screenName;
    }

    public String updateUserDefinedScreenRow(EAMContext eamContext, String screenName, UDTRow fieldsToUpdate, UDTRow filters) throws EAMException {
        // Fetch the UDS row based on the primary keys
        MP6441_ProcessUserDefinedScreenService_001 userDefinedScreenServiceGet = initUserDefinedScreenService(screenName, "GET");
        addFields(userDefinedScreenServiceGet, filters);
        MP6441_ProcessUserDefinedScreenService_001_Result result = tools.performEAMOperation(eamContext, eamws::processUserDefinedScreenServiceOp, userDefinedScreenServiceGet);

        // Modify the fetched UDS row so it can be used for the update
        MP6441_ProcessUserDefinedScreenService_001 userDefinedScreenServiceUpdate = new MP6441_ProcessUserDefinedScreenService_001();
        userDefinedScreenServiceUpdate.setUserDefinedScreenService(result.getResultData().getUserDefinedScreenService());
        userDefinedScreenServiceUpdate.getUserDefinedScreenService().setUSERDEFINEDSERVICEACTION("UPDATE");
        userDefinedScreenServiceUpdate.getUserDefinedScreenService().getUSERDEFINEDSCREENFIELDVALUELIST().getUSERDEFINEDSCREENFIELDVALUEPAIR().removeIf(pair ->  fieldsToUpdate.getAllKeys().contains(pair.getUSERDEFINEDSCREENFIELDNAME()));
        addFields(userDefinedScreenServiceUpdate, fieldsToUpdate);

        tools.performEAMOperation(eamContext, eamws::processUserDefinedScreenServiceOp, userDefinedScreenServiceUpdate);
        return screenName;
    }

    public String deleteUserDefinedScreenRow(EAMContext eamContext, String screenName, UDTRow udtRow) throws EAMException {
        MP6441_ProcessUserDefinedScreenService_001 userDefinedScreenService= initUserDefinedScreenService(screenName, "DELETE");
        addFields(userDefinedScreenService, udtRow);
        tools.performEAMOperation(eamContext, eamws::processUserDefinedScreenServiceOp, userDefinedScreenService);
        return screenName;
    }

    //
    // HELPER METHODS
    //
    private void addFields(MP6441_ProcessUserDefinedScreenService_001 processUserDefinedScreenService, UDTRow row) throws EAMException {
        List<USERDEFINEDSCREENFIELDVALUEPAIR> udsFieldsFilter = processUserDefinedScreenService.getUserDefinedScreenService().getUSERDEFINEDSCREENFIELDVALUELIST().getUSERDEFINEDSCREENFIELDVALUEPAIR();
        udsFieldsFilter.addAll(getPairList(row.getStrings()));
        udsFieldsFilter.addAll(getPairList(row.getDates()));
        udsFieldsFilter.addAll(getPairList(row.getDecimals()));
    }

    private MP6441_ProcessUserDefinedScreenService_001 initUserDefinedScreenService(String screenName, String screenAction) {
        MP6441_ProcessUserDefinedScreenService_001 userDefinedScreenService = new MP6441_ProcessUserDefinedScreenService_001();
        userDefinedScreenService.setUserDefinedScreenService(new net.datastream.schemas.mp_entities.userdefinedscreenservice_001.UserDefinedScreenService());
        userDefinedScreenService.getUserDefinedScreenService().setUSERDEFINEDSCREENNAME(screenName);
        userDefinedScreenService.getUserDefinedScreenService().setUSERDEFINEDSERVICEACTION(screenAction);
        userDefinedScreenService.getUserDefinedScreenService().setUSERDEFINEDSCREENFIELDVALUELIST(new USERDEFINEDSCREENFIELDVALUELIST());
        return userDefinedScreenService;
    }

    private <T> List<USERDEFINEDSCREENFIELDVALUEPAIR> getPairList(Map<String, T> values) throws EAMException {
        LinkedList<USERDEFINEDSCREENFIELDVALUEPAIR> result = new LinkedList<>();

        if (values == null) {
            return new LinkedList<>();
        }

        // Use imperative approach due to the exception handling
        for(String value : values.keySet()) {
            if (values.get(value) != null) {
                result.add(getPair(value, values.get(value)));
            }
        }

        return result;
    }

    private <T> USERDEFINEDSCREENFIELDVALUEPAIR getPair(String fieldName, T fieldValue) throws EAMException {
        USERDEFINEDSCREENFIELDVALUEPAIR pair = new USERDEFINEDSCREENFIELDVALUEPAIR();
        pair.setUSERDEFINEDSCREENFIELDNAME(fieldName);
        pair.setUSERDEFINEDSCREENFIELDVALUE(new USERDEFINEDSCREENFIELDDATA_Type());

        if (fieldValue instanceof String) {
            pair.getUSERDEFINEDSCREENFIELDVALUE().setTEXTDATA( (String) fieldValue);
        }

        if (fieldValue instanceof BigDecimal) {
            pair.getUSERDEFINEDSCREENFIELDVALUE().setNUMERICDATA(encodeAmount((BigDecimal) fieldValue, fieldName));
        }

        if (fieldValue instanceof Date) {
            pair.getUSERDEFINEDSCREENFIELDVALUE().setDATETIMEDATA(encodeEAMDate((Date) fieldValue, fieldName));
        }

        return pair;
    }

}
