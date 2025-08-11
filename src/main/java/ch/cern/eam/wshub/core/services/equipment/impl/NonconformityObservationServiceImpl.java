package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.NonconformityObservationService;
import ch.cern.eam.wshub.core.services.equipment.NonconformityService;
import ch.cern.eam.wshub.core.services.equipment.entities.NonconformityObservation;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.GridTools;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.NONCONFORMITYOBSERVATIONID_Type;
import net.datastream.schemas.mp_functions.mp3402_001.MP3402_CreateNonconformityObservation_001;
import net.datastream.schemas.mp_functions.mp3442_001.MP3442_AddNonconformityObservation_001;
import net.datastream.schemas.mp_functions.mp3443_001.MP3443_SyncNonconformityObservation_001;
import net.datastream.schemas.mp_functions.mp3444_001.MP3444_DeleteNonconformityObservation_001;
import net.datastream.schemas.mp_functions.mp3445_001.MP3445_GetNonconformityObservation_001;
import net.datastream.schemas.mp_results.mp3402_001.MP3402_CreateNonconformityObservation_001_Result;
import net.datastream.schemas.mp_results.mp3442_001.MP3442_AddNonconformityObservation_001_Result;
import net.datastream.schemas.mp_results.mp3443_001.MP3443_SyncNonconformityObservation_001_Result;
import net.datastream.schemas.mp_results.mp3444_001.MP3444_DeleteNonconformityObservation_001_Result;
import net.datastream.schemas.mp_results.mp3445_001.MP3445_GetNonconformityObservation_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.List;

public class NonconformityObservationServiceImpl implements NonconformityObservationService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private GridsService gridsService;

    public NonconformityObservationServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
    }

    @Override
    public String createNonconformityObservation(InforContext context, NonconformityObservation nonConformityObservation) throws InforException {
        if (nonConformityObservation.getJobNum() == null) {
            MP3442_AddNonconformityObservation_001 addNonconformityObservation = new MP3442_AddNonconformityObservation_001();
            net.datastream.schemas.mp_entities.nonconformityobservation_001.NonconformityObservation nonconformityObservation =  tools.getInforFieldTools().transformWSHubObject(
                    createDefaultNonConformityObservation(), nonConformityObservation, context);


            addNonconformityObservation.setNonconformityObservation(nonconformityObservation);
            MP3442_AddNonconformityObservation_001_Result result = tools.performInforOperation(context, inforws::addNonconformityObservationOp, addNonconformityObservation);
            return result.getResultData().getNONCONFORMITYOBSERVATIONID().getOBSERVATIONPK();

        }
        MP3402_CreateNonconformityObservation_001 addNonconformityObservation = new MP3402_CreateNonconformityObservation_001();
        addNonconformityObservation =  tools.getInforFieldTools().transformWSHubObject(
                addNonconformityObservation, nonConformityObservation, context);
        MP3402_CreateNonconformityObservation_001_Result result = tools.performInforOperation(context, inforws::createNonconformityObservationOp, addNonconformityObservation);
        return result.getResultData().getNONCONFORMITYOBSERVATIONID().getOBSERVATIONPK();
    }

    @Override
    public NonconformityObservation readNonconformityObservation(InforContext context, String nonconformityObsPk) throws InforException {
        net.datastream.schemas.mp_entities.nonconformityobservation_001.NonconformityObservation nonconformityObservation =
                readNonconformityObservationInfor(context, nonconformityObsPk);

        return tools.getInforFieldTools().transformInforObject(new NonconformityObservation(), nonconformityObservation, context);
    }

    @Override
    public List<NonconformityObservation> readNonconformityObservationsByCode(
            InforContext context,
            String nonconformityCode,
            String organization
    ) throws InforException {
        GridRequest observationsRequest = new GridRequest("OSNCHD_OBS");

        observationsRequest.setRowCount(100);
        observationsRequest.setUserFunctionName("OSNCHD");
        observationsRequest.addParam("param.nonconformity", nonconformityCode);
        observationsRequest.addParam("param.organization", organization);

        GridRequestResult observationsResult = gridsService.executeQuery(context, observationsRequest);

        return GridTools.convertGridResultToObject(NonconformityObservation.class, null, observationsResult);
    }

    @Override
    public String updateNonconformityObservation(InforContext context, NonconformityObservation nonConformityObservation) throws InforException {
        MP3443_SyncNonconformityObservation_001 syncNonconformityObservation = new MP3443_SyncNonconformityObservation_001();

        net.datastream.schemas.mp_entities.nonconformityobservation_001.NonconformityObservation prev = readNonconformityObservationInfor(context, nonConformityObservation.getObservationPk());
        tools.getInforFieldTools().transformWSHubObject(prev, nonConformityObservation, context);
        syncNonconformityObservation.setNonconformityObservation(prev);

        MP3443_SyncNonconformityObservation_001_Result result = tools.performInforOperation(context, inforws::syncNonconformityObservationOp, syncNonconformityObservation);
        return  result.getResultData().getNONCONFORMITYOBSERVATIONID().getOBSERVATIONPK();

    }

    @Override
    public String deleteNonconformityObservation(InforContext context, String number) throws InforException {
        MP3444_DeleteNonconformityObservation_001 deleteNonconformityObservation = new MP3444_DeleteNonconformityObservation_001();
        NONCONFORMITYOBSERVATIONID_Type idType = new NONCONFORMITYOBSERVATIONID_Type();
        idType.setOBSERVATIONPK(number);
        deleteNonconformityObservation.setNONCONFORMITYOBSERVATIONID(idType);
        MP3444_DeleteNonconformityObservation_001_Result result = tools.performInforOperation(context, inforws::deleteNonconformityObservationOp, deleteNonconformityObservation);
        return result.getResultData().getNONCONFORMITYOBSERVATIONID().getOBSERVATIONPK();

    }
    private net.datastream.schemas.mp_entities.nonconformityobservation_001.NonconformityObservation readNonconformityObservationInfor(
            InforContext context, String nonconformityCode) throws InforException {
        MP3445_GetNonconformityObservation_001 getNonconformityObservation = new MP3445_GetNonconformityObservation_001();
        NONCONFORMITYOBSERVATIONID_Type idType = new NONCONFORMITYOBSERVATIONID_Type();
        idType.setOBSERVATIONPK(nonconformityCode);

        getNonconformityObservation.setNONCONFORMITYOBSERVATIONID(idType);

        MP3445_GetNonconformityObservation_001_Result result =
                tools.performInforOperation(context, inforws::getNonconformityObservationOp, getNonconformityObservation);

        return result.getResultData().getNonconformityObservation();
    }

    private net.datastream.schemas.mp_entities.nonconformityobservation_001.NonconformityObservation createDefaultNonConformityObservation() throws InforException {
        net.datastream.schemas.mp_entities.nonconformityobservation_001.NonconformityObservation defaultObject = new net.datastream.schemas.mp_entities.nonconformityobservation_001.NonconformityObservation();
        NONCONFORMITYOBSERVATIONID_Type idType = new NONCONFORMITYOBSERVATIONID_Type();
        idType.setOBSERVATIONPK("0");

        defaultObject.setNONCONFORMITYOBSERVATIONID(idType);
        return defaultObject;
    }

}
