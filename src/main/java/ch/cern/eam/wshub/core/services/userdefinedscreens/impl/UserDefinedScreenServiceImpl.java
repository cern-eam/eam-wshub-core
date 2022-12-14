package ch.cern.eam.wshub.core.services.userdefinedscreens.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedScreenService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.encodeAmount;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.encodeInforDate;
import net.datastream.schemas.mp_fields.USERDEFINEDSCREENFIELDDATA_Type;
import net.datastream.schemas.mp_fields.USERDEFINEDSCREENFIELDVALUELIST;
import net.datastream.schemas.mp_fields.USERDEFINEDSCREENFIELDVALUEPAIR;
import net.datastream.schemas.mp_functions.mp6441_001.MP6441_ProcessUserDefinedScreenService_001;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserDefinedScreenServiceImpl implements UserDefinedScreenService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public UserDefinedScreenServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    public String createUserDefinedScreenRow(InforContext inforContext, String screenName, UDTRow udtRow) throws InforException {
        tools.performInforOperation(inforContext, inforws::processUserDefinedScreenServiceOp, getUserDefinedScreenService(screenName, "GET", null));
        return screenName;
    }

    // TODO
    //public String updateUserDefinedScreenRow(InforContext inforContext, String screenName, UDTRow udtRow) throws InforException {
    //    tools.performInforOperation(inforContext, inforws::processUserDefinedScreenServiceOp, getUserDefinedScreenService(screenName, "UPDATE", udtRow));
    //    return screenName;
    //}

    public String deleteUserDefinedScreenRow(InforContext inforContext, String screenName, UDTRow udtRow) throws InforException {
        tools.performInforOperation(inforContext, inforws::processUserDefinedScreenServiceOp, getUserDefinedScreenService(screenName, "DELETE", udtRow));
        return screenName;
    }

    //
    // HELPER METHODS
    //
    private MP6441_ProcessUserDefinedScreenService_001 getUserDefinedScreenService(String screenName, String screenAction, UDTRow udtRow) throws InforException {
        MP6441_ProcessUserDefinedScreenService_001 userDefinedScreenService = new MP6441_ProcessUserDefinedScreenService_001();
        userDefinedScreenService.setUserDefinedScreenService(new net.datastream.schemas.mp_entities.userdefinedscreenservice_001.UserDefinedScreenService());
        userDefinedScreenService.getUserDefinedScreenService().setUSERDEFINEDSCREENNAME(screenName);
        userDefinedScreenService.getUserDefinedScreenService().setUSERDEFINEDSERVICEACTION(screenAction);

        userDefinedScreenService.getUserDefinedScreenService().setUSERDEFINEDSCREENFIELDVALUELIST(new USERDEFINEDSCREENFIELDVALUELIST());
        List<USERDEFINEDSCREENFIELDVALUEPAIR> udsFields = userDefinedScreenService.getUserDefinedScreenService().getUSERDEFINEDSCREENFIELDVALUELIST().getUSERDEFINEDSCREENFIELDVALUEPAIR();
        udsFields.addAll(getPairList(udtRow.getStrings()));
        udsFields.addAll(getPairList(udtRow.getDates()));
        udsFields.addAll(getPairList(udtRow.getDates()));

        return userDefinedScreenService;
    }

    private <T> List<USERDEFINEDSCREENFIELDVALUEPAIR> getPairList(Map<String, T> values) throws InforException {
        LinkedList<USERDEFINEDSCREENFIELDVALUEPAIR> result = new LinkedList<>();

        if (values == null) {
            return new LinkedList<>();
        }

        // Use imperative approach due to the exception handling
        for(String value : values.keySet()) {
            result.add(getPair(value, values.get(value)));
        }

        return result;
    }

    private <T> USERDEFINEDSCREENFIELDVALUEPAIR getPair(String fieldName, T fieldValue) throws InforException {
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
            pair.getUSERDEFINEDSCREENFIELDVALUE().setDATETIMEDATA(encodeInforDate((Date) fieldValue, fieldName));
        }

        return pair;
    }

}
