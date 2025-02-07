package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.equipment.NonConformityObservationService;
import ch.cern.eam.wshub.core.services.equipment.NonconformityService;
import ch.cern.eam.wshub.core.services.equipment.entities.NonConformityObservation;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.nonconformityobservation_001.NonconformityObservation;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;

public class NonConformityObservationServiceImpl implements NonConformityObservationService {

    private ApplicationData applicationData;
    private Tools tools;
    private EAMWebServicesPT eamws;
    private NonconformityService  nonconformityService;

    public NonConformityObservationServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
        this.nonconformityService = new NonconformityServiceImpl(applicationData, tools, eamws);
    }

    @Override
    public String createNonConformityObservation(EAMContext context, NonConformityObservation nonConformityObservation) throws EAMException {
        if (nonConformityObservation.getJobNum() == null) {
            MP3442_AddNonconformityObservation_001 addNonconformityObservation = new MP3442_AddNonconformityObservation_001();
            NonconformityObservation nonconformityObservation =  tools.getEAMFieldTools().transformWSHubObject(
                    createDefaultNonConformityObservation(), nonConformityObservation, context);


            addNonconformityObservation.setNonconformityObservation(nonconformityObservation);
            MP3442_AddNonconformityObservation_001_Result result = tools.performEAMOperation(context, eamws::addNonconformityObservationOp, addNonconformityObservation);
            return result.getResultData().getNONCONFORMITYOBSERVATIONID().getOBSERVATIONPK();

        }
        MP3402_CreateNonconformityObservation_001 addNonconformityObservation = new MP3402_CreateNonconformityObservation_001();
        addNonconformityObservation =  tools.getEAMFieldTools().transformWSHubObject(
                addNonconformityObservation, nonConformityObservation, context);
        MP3402_CreateNonconformityObservation_001_Result result = tools.performEAMOperation(context, eamws::createNonconformityObservationOp, addNonconformityObservation);
        return result.getResultData().getNONCONFORMITYOBSERVATIONID().getOBSERVATIONPK();
    }


    @Override
    public NonConformityObservation readNonConformityObservation(EAMContext context, String nonconformityObsPk) throws EAMException {
        NonconformityObservation nonconformityObservation =
                readNonconformityObservationEAM(context, nonconformityObsPk);

        return tools.getEAMFieldTools().transformEAMObject(new NonConformityObservation(), nonconformityObservation, context);
    }


    @Override
    public String updateNonConformityObservation(EAMContext context, NonConformityObservation nonConformityObservation) throws EAMException {
        MP3443_SyncNonconformityObservation_001 syncNonconformityObservation = new MP3443_SyncNonconformityObservation_001();

        NonconformityObservation prev = readNonconformityObservationEAM(context, nonConformityObservation.getObservationPk());
        tools.getEAMFieldTools().transformWSHubObject(prev, nonConformityObservation, context);
        syncNonconformityObservation.setNonconformityObservation(prev);

        MP3443_SyncNonconformityObservation_001_Result result = tools.performEAMOperation(context, eamws::syncNonconformityObservationOp, syncNonconformityObservation);
        return  result.getResultData().getNONCONFORMITYOBSERVATIONID().getOBSERVATIONPK();

    }

    @Override
    public String deleteNonConformityObservation(EAMContext context, String number) throws EAMException {
        MP3444_DeleteNonconformityObservation_001 deleteNonconformityObservation = new MP3444_DeleteNonconformityObservation_001();
        NONCONFORMITYOBSERVATIONID_Type idType = new NONCONFORMITYOBSERVATIONID_Type();
        idType.setOBSERVATIONPK(number);
        deleteNonconformityObservation.setNONCONFORMITYOBSERVATIONID(idType);
        MP3444_DeleteNonconformityObservation_001_Result result = tools.performEAMOperation(context, eamws::deleteNonconformityObservationOp, deleteNonconformityObservation);
        return result.getResultData().getNONCONFORMITYOBSERVATIONID().getOBSERVATIONPK();

    }
    private NonconformityObservation readNonconformityObservationEAM(
            EAMContext context, String nonconformityCode) throws EAMException {
        MP3445_GetNonconformityObservation_001 getNonconformityObservation = new MP3445_GetNonconformityObservation_001();
        NONCONFORMITYOBSERVATIONID_Type idType = new NONCONFORMITYOBSERVATIONID_Type();
        idType.setOBSERVATIONPK(nonconformityCode);

        getNonconformityObservation.setNONCONFORMITYOBSERVATIONID(idType);

        MP3445_GetNonconformityObservation_001_Result result =
                tools.performEAMOperation(context, eamws::getNonconformityObservationOp, getNonconformityObservation);

        return result.getResultData().getNonconformityObservation();
    }

    private NonconformityObservation createDefaultNonConformityObservation() throws EAMException {
        NonconformityObservation defaultObject = new NonconformityObservation();
        NONCONFORMITYOBSERVATIONID_Type idType = new NONCONFORMITYOBSERVATIONID_Type();
        idType.setOBSERVATIONPK("0");

        defaultObject.setNONCONFORMITYOBSERVATIONID(idType);
        return defaultObject;
    }

}

