package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.NonConformityObservationService;
import ch.cern.eam.wshub.core.services.equipment.NonconformityService;
import ch.cern.eam.wshub.core.services.equipment.entities.NonConformity;
import ch.cern.eam.wshub.core.services.equipment.entities.NonConformityObservation;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.DataTypeTools;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.nonconformity_001.Nonconformity;
import net.datastream.schemas.mp_entities.nonconformityobservation_001.NonconformityObservation;
import net.datastream.schemas.mp_fields.NONCONFORMITYOBSERVATIONID_Type;
import net.datastream.schemas.mp_fields.ORGANIZATIONID_Type;
import net.datastream.schemas.mp_fields.STANDARDENTITYID_Type;
import net.datastream.schemas.mp_fields.STATUS_Type;
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

import java.math.BigDecimal;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;

public class NonConformityObservationServiceImpl implements NonConformityObservationService {

    private ApplicationData applicationData;
    private Tools tools;
    private InforWebServicesPT inforws;
    private NonconformityService  nonconformityService;

    public NonConformityObservationServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        this.nonconformityService = new NonconformityServiceImpl(applicationData, tools, inforws);
    }

    @Override
    public String createNonConformityObservation(InforContext context, NonConformityObservation nonConformityObservation) throws InforException {
        if(nonConformityObservation.getJobNum() != null) {
            MP3442_AddNonconformityObservation_001 addNonconformityObservation = new MP3442_AddNonconformityObservation_001();
            NonconformityObservation nonconformityObservation =  tools.getInforFieldTools().transformWSHubObject(
                    createDefaultNonConformityObservation(), nonConformityObservation, context);


            addNonconformityObservation.setNonconformityObservation(nonconformityObservation);
            MP3442_AddNonconformityObservation_001_Result result = tools.performInforOperation(context, inforws::addNonconformityObservationOp, addNonconformityObservation);
            return result.getResultData().getNONCONFORMITYOBSERVATIONID().getOBSERVATIONPK();

        } else {
            MP3402_CreateNonconformityObservation_001 addNonconformityObservation = new MP3402_CreateNonconformityObservation_001();
            NonconformityObservation nonconformityObservation =  tools.getInforFieldTools().transformWSHubObject(
                    new NonconformityObservation(), nonConformityObservation, context);
            STANDARDENTITYID_Type typeId = new STANDARDENTITYID_Type();
            typeId.setDESCRIPTION(nonconformityObservation.getNONCONFORMITYOBSERVATIONID().getDESCRIPTION());
            typeId.setSTANDARDENTITYCODE(nonconformityObservation.getNONCONFORMITYOBSERVATIONID().getNONCONFORMITYCODE());
            typeId.setORGANIZATIONID(nonconformityObservation.getNONCONFORMITYOBSERVATIONID().getORGANIZATIONID());
            addNonconformityObservation.setNONCONFORMITYID(typeId);
            addNonconformityObservation.setJOBNUM(nonConformityObservation.getJobNum());

            MP3402_CreateNonconformityObservation_001_Result result = tools.performInforOperation(context, inforws::createNonconformityObservationOp, addNonconformityObservation);
            return result.getResultData().getNONCONFORMITYOBSERVATIONID().getOBSERVATIONPK();
        }
    }


    @Override
    public NonConformityObservation readNonConformityObservation(InforContext context, String nonconformityObsPk) throws InforException {
        NonconformityObservation nonconformityObservation =
                readNonconformityObservationInfor(context, nonconformityObsPk);

        return tools.getInforFieldTools().transformInforObject(new NonConformityObservation(), nonconformityObservation, context);
    }


    @Override
    public String updateNonConformityObservation(InforContext context, NonConformityObservation nonConformityObservation) throws InforException {
        MP3443_SyncNonconformityObservation_001 syncNonconformityObservation = new MP3443_SyncNonconformityObservation_001();

        NonconformityObservation prev = readNonconformityObservationInfor(context, nonConformityObservation.getObservationPk());
        tools.getInforFieldTools().transformWSHubObject(prev, nonConformityObservation, context);
        syncNonconformityObservation.setNonconformityObservation(prev);

        MP3443_SyncNonconformityObservation_001_Result result = tools.performInforOperation(context, inforws::syncNonconformityObservationOp, syncNonconformityObservation);
        return  result.getResultData().getNONCONFORMITYOBSERVATIONID().getOBSERVATIONPK();

    }

    @Override
    public String deleteNonConformityObservation(InforContext context, String number) throws InforException {
        MP3444_DeleteNonconformityObservation_001 deleteNonconformityObservation = new MP3444_DeleteNonconformityObservation_001();
        NONCONFORMITYOBSERVATIONID_Type idType = new NONCONFORMITYOBSERVATIONID_Type();
        idType.setOBSERVATIONPK(number);
        deleteNonconformityObservation.setNONCONFORMITYOBSERVATIONID(idType);
        MP3444_DeleteNonconformityObservation_001_Result result = tools.performInforOperation(context, inforws::deleteNonconformityObservationOp, deleteNonconformityObservation);
        return result.getResultData().getNONCONFORMITYOBSERVATIONID().getOBSERVATIONPK();

    }
    private NonconformityObservation readNonconformityObservationInfor(
            InforContext context, String nonconformityCode) throws InforException {
        MP3445_GetNonconformityObservation_001 getNonconformityObservation = new MP3445_GetNonconformityObservation_001();
        NONCONFORMITYOBSERVATIONID_Type idType = new NONCONFORMITYOBSERVATIONID_Type();
        idType.setOBSERVATIONPK(nonconformityCode);

        getNonconformityObservation.setNONCONFORMITYOBSERVATIONID(idType);

        MP3445_GetNonconformityObservation_001_Result result =
                tools.performInforOperation(context, inforws::getNonconformityObservationOp, getNonconformityObservation);

        return result.getResultData().getNonconformityObservation();
    }

    private NonconformityObservation createDefaultNonConformityObservation() throws InforException {
        NonconformityObservation defaultObject = new NonconformityObservation();
        NONCONFORMITYOBSERVATIONID_Type idType = new NONCONFORMITYOBSERVATIONID_Type();
        idType.setOBSERVATIONPK("0");

        defaultObject.setNONCONFORMITYOBSERVATIONID(idType);
        return defaultObject;
    }

}

