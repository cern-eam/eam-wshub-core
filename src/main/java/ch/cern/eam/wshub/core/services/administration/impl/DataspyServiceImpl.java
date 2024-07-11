package ch.cern.eam.wshub.core.services.administration.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.administration.DataspyService;
import ch.cern.eam.wshub.core.services.administration.entities.DataspyCopy;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.SCREENDATASPYID_Type;
import net.datastream.schemas.mp_fields.USERID_Type;
import net.datastream.schemas.mp_functions.mp6516_001.MP6516_CopyScreenDataspy_001;
import net.datastream.schemas.mp_functions.mp6518_001.MP6518_DeleteScreenDataspy_001;
import net.datastream.schemas.mp_results.mp6516_001.MP6516_CopyScreenDataspy_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.math.BigDecimal;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.encodeQuantity;
import static ch.cern.eam.wshub.core.tools.GridTools.extractSingleResult;

public class DataspyServiceImpl implements DataspyService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;
    private GridsService gridsService;

    public DataspyServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
        gridsService = new GridsServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
    }

    public String copyDataspy(EAMContext context, DataspyCopy dataspyCopy) throws EAMException {

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
        result = tools.performEAMOperation(context, eamws::copyScreenDataspyOp, copyScreenDataspy);

        // Uncomment once EAM WS returns the ID of the created dataspy and not the one used as a source
        //return tools.getDataTypeTools().decodeQuantity(result.getResultData().getSCREENDATASPYID().getDDSPYID());

        // Temporarily fetch the ID of the created dataspy (most recent dataspy created for the passed user)
        GridRequest gridRequest = new GridRequest("BEWSDP", 1);
        gridRequest.addFilter("dds_owner", dataspyCopy.getUserCode(), "=");
        gridRequest.sortBy("dds_ddspyid", "DESC");
        return extractSingleResult(gridsService.executeQuery(context, gridRequest), "dds_ddspyid").replace(",", "");
    }

    public String deleteDataspy(EAMContext context, BigDecimal dataspyId) throws EAMException {
        MP6518_DeleteScreenDataspy_001 deleteScreenDataspy = new MP6518_DeleteScreenDataspy_001();
        deleteScreenDataspy.setSCREENDATASPYID(new SCREENDATASPYID_Type());
        deleteScreenDataspy.getSCREENDATASPYID().setDDSPYID(encodeQuantity(dataspyId, "Dataspy ID"));
        tools.performEAMOperation(context, eamws::deleteScreenDataspyOp, deleteScreenDataspy);
        return dataspyId.toPlainString();
    }

}
