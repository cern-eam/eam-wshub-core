package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.NonconformityService;
import ch.cern.eam.wshub.core.services.equipment.entities.Nonconformity;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.STANDARDENTITYID_Type;
import net.datastream.schemas.mp_functions.mp3397_001.MP3397_AddNonconformity_001;
import net.datastream.schemas.mp_functions.mp3398_001.MP3398_SyncNonconformity_001;
import net.datastream.schemas.mp_functions.mp3399_001.MP3399_DeleteNonconformity_001;
import net.datastream.schemas.mp_functions.mp3400_001.MP3400_GetNonconformity_001;
import net.datastream.schemas.mp_results.mp3397_001.MP3397_AddNonconformity_001_Result;
import net.datastream.schemas.mp_results.mp3398_001.MP3398_SyncNonconformity_001_Result;
import net.datastream.schemas.mp_results.mp3400_001.MP3400_GetNonconformity_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

public class NonconformityServiceImpl implements NonconformityService {

    private ApplicationData applicationData;
    private Tools tools;
    private InforWebServicesPT inforws;

    public NonconformityServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    @Override
    public String createNonconformity(InforContext context, Nonconformity nonconformityParam) throws InforException {
        net.datastream.schemas.mp_entities.nonconformity_001.Nonconformity nonconformity =
                new net.datastream.schemas.mp_entities.nonconformity_001.Nonconformity();

        tools.getInforFieldTools().transformWSHubObject(nonconformity, nonconformityParam, context);

        MP3397_AddNonconformity_001 addNonconformity = new MP3397_AddNonconformity_001();
        addNonconformity.setNonconformity(nonconformity);

        MP3397_AddNonconformity_001_Result result =
                tools.performInforOperation(context, inforws::addNonconformityOp, addNonconformity);

        return result.getResultData().getNONCONFORMITYID().getSTANDARDENTITYCODE();
    }

    @Override
    public Nonconformity readNonconformity(InforContext context, String nonconformityCode) throws InforException {
        net.datastream.schemas.mp_entities.nonconformity_001.Nonconformity nonconformity =
                readNonconformityInfor(context, nonconformityCode);

        return tools.getInforFieldTools().transformInforObject(new Nonconformity(), nonconformity, context);
    }

    private net.datastream.schemas.mp_entities.nonconformity_001.Nonconformity readNonconformityInfor(
            InforContext context, String nonconformityCode) throws InforException {
        MP3400_GetNonconformity_001 getNonconformity = new MP3400_GetNonconformity_001();

        getNonconformity.setNONCONFORMITYID(new STANDARDENTITYID_Type());
        getNonconformity.getNONCONFORMITYID().setSTANDARDENTITYCODE(nonconformityCode);
        getNonconformity.getNONCONFORMITYID().setORGANIZATIONID(tools.getOrganization(context));

        MP3400_GetNonconformity_001_Result result =
                tools.performInforOperation(context, inforws::getNonconformityOp, getNonconformity);

        return result.getResultData().getNonconformity();
    }

    @Override
    public String updateNonconformity(InforContext context, Nonconformity nonconformityParam) throws InforException {
        net.datastream.schemas.mp_entities.nonconformity_001.Nonconformity nonconformity =
                readNonconformityInfor(context, nonconformityParam.getCode());

        tools.getInforFieldTools().transformWSHubObject(nonconformity, nonconformityParam, context);

        MP3398_SyncNonconformity_001 syncNonconformity = new MP3398_SyncNonconformity_001();
        syncNonconformity.setNonconformity(nonconformity);

        MP3398_SyncNonconformity_001_Result syncResult =
                tools.performInforOperation(context, inforws::syncNonconformityOp, syncNonconformity);

        return syncResult.getResultData().getNONCONFORMITYID().getSTANDARDENTITYCODE();
    }

    @Override
    public String deleteNonconformity(InforContext context, String nonconformityCode) throws InforException {
        MP3399_DeleteNonconformity_001 deleteNonconformity = new MP3399_DeleteNonconformity_001();

        deleteNonconformity.setNONCONFORMITYID(new STANDARDENTITYID_Type());
        deleteNonconformity.getNONCONFORMITYID().setSTANDARDENTITYCODE(nonconformityCode);
        deleteNonconformity.getNONCONFORMITYID().setORGANIZATIONID(tools.getOrganization(context));

        tools.performInforOperation(context, inforws::deleteNonconformityOp, deleteNonconformity);

        return nonconformityCode;
    }
}
