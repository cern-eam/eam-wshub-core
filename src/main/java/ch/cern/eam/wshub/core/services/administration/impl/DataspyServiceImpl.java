package ch.cern.eam.wshub.core.services.administration.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.administration.DataspyService;
import ch.cern.eam.wshub.core.services.administration.entities.DataspyCopy;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.BooleanType;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.USERID_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp6516_001.MP6516_CopyScreenDataspy_001;
import net.datastream.schemas.mp_functions.mp6517_001.MP6517_SyncScreenDataspy_001;
import net.datastream.schemas.mp_results.mp6516_001.MP6516_CopyScreenDataspy_001_Result;
import net.datastream.schemas.mp_results.mp6519_001.MP6519_GetScreenDataspy_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.xml.ws.Holder;

public class DataspyServiceImpl implements DataspyService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public DataspyServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    public String copyDataspy(InforContext context, DataspyCopy dataspyCopy) throws InforException {

        MP6516_CopyScreenDataspy_001 copyScreenDataspy = new MP6516_CopyScreenDataspy_001();

        // DATASPY CODE
        copyScreenDataspy.setDDSPYID(tools.getDataTypeTools().encodeQuantity(dataspyCopy.getDataspyCode(), "Dataspy Code"));

        // USER
        copyScreenDataspy.setUSERID(new USERID_Type());
        copyScreenDataspy.getUSERID().setUSERCODE(dataspyCopy.getUserCode());

        // IS DEFAULT DATASPY
        copyScreenDataspy.setDEFAULT(tools.getDataTypeTools().encodeBoolean(dataspyCopy.getDefaultDataspy(), BooleanType.PLUS_MINUS));

        MP6516_CopyScreenDataspy_001_Result result = null;

        // Execute operation of reading
        if (context.getCredentials() != null) {
            result = inforws.copyScreenDataspyOp(copyScreenDataspy, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null,
                    null, tools.getTenant(context));
        } else {
            result = inforws.copyScreenDataspyOp(copyScreenDataspy, tools.getOrganizationCode(context), null, null,
                    new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
        }

        return tools.getDataTypeTools().decodeQuantity(result.getResultData().getSCREENDATASPYID().getDDSPYID());
    }

}
