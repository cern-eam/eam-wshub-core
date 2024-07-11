package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.equipment.NonconformityService;
import ch.cern.eam.wshub.core.services.equipment.entities.NonConformity;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.STANDARDENTITYID_Type;
import net.datastream.schemas.mp_functions.mp3396_001.MP3396_GetNonconformityDefault_001;
import net.datastream.schemas.mp_functions.mp3397_001.MP3397_AddNonconformity_001;
import net.datastream.schemas.mp_functions.mp3398_001.MP3398_SyncNonconformity_001;
import net.datastream.schemas.mp_functions.mp3399_001.MP3399_DeleteNonconformity_001;
import net.datastream.schemas.mp_functions.mp3400_001.MP3400_GetNonconformity_001;
import net.datastream.schemas.mp_results.mp3396_001.MP3396_GetNonconformityDefault_001_Result;
import net.datastream.schemas.mp_results.mp3397_001.MP3397_AddNonconformity_001_Result;
import net.datastream.schemas.mp_results.mp3398_001.MP3398_SyncNonconformity_001_Result;
import net.datastream.schemas.mp_results.mp3400_001.MP3400_GetNonconformity_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

public class NonconformityServiceImpl implements NonconformityService {

    private ApplicationData applicationData;
    private Tools tools;
    private EAMWebServicesPT eamws;

    public NonconformityServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
    }

    @Override
    public NonConformity readNonconformityDefault(EAMContext context) throws EAMException {
        MP3396_GetNonconformityDefault_001 getNonconformityDefault = new MP3396_GetNonconformityDefault_001();

        getNonconformityDefault.setORGANIZATIONID(tools.getOrganization(context));

        MP3396_GetNonconformityDefault_001_Result result =
                tools.performEAMOperation(context, eamws::getNonconformityDefaultOp, getNonconformityDefault);

        return tools.getEAMFieldTools().transformEAMObject(
                new NonConformity(), result.getResultData().getNonconformityDefault(), context
        );
    }

    @Override
    public String createNonconformity(EAMContext context, NonConformity nonconformityParam) throws EAMException {
        net.datastream.schemas.mp_entities.nonconformity_001.Nonconformity nonconformity =
                new net.datastream.schemas.mp_entities.nonconformity_001.Nonconformity();

        tools.getEAMFieldTools().transformWSHubObject(nonconformity, nonconformityParam, context);

        MP3397_AddNonconformity_001 addNonconformity = new MP3397_AddNonconformity_001();
        addNonconformity.setNonconformity(nonconformity);

        MP3397_AddNonconformity_001_Result result =
                tools.performEAMOperation(context, eamws::addNonconformityOp, addNonconformity);

        return result.getResultData().getNONCONFORMITYID().getSTANDARDENTITYCODE();
    }

    @Override
    public NonConformity readNonconformity(EAMContext context, String nonconformityCode) throws EAMException {
        net.datastream.schemas.mp_entities.nonconformity_001.Nonconformity nonconformity =
                readNonconformityEAM(context, nonconformityCode);

        return tools.getEAMFieldTools().transformEAMObject(new NonConformity(), nonconformity, context);
    }

    private net.datastream.schemas.mp_entities.nonconformity_001.Nonconformity readNonconformityEAM(
            EAMContext context, String nonconformityCode) throws EAMException {
        MP3400_GetNonconformity_001 getNonconformity = new MP3400_GetNonconformity_001();

        getNonconformity.setNONCONFORMITYID(new STANDARDENTITYID_Type());
        getNonconformity.getNONCONFORMITYID().setSTANDARDENTITYCODE(nonconformityCode);
        getNonconformity.getNONCONFORMITYID().setORGANIZATIONID(tools.getOrganization(context));

        MP3400_GetNonconformity_001_Result result =
                tools.performEAMOperation(context, eamws::getNonconformityOp, getNonconformity);

        return result.getResultData().getNonconformity();
    }

    @Override
    public String updateNonconformity(EAMContext context, NonConformity nonconformityParam) throws EAMException {
        net.datastream.schemas.mp_entities.nonconformity_001.Nonconformity nonconformity =
                readNonconformityEAM(context, nonconformityParam.getCode());

        tools.getEAMFieldTools().transformWSHubObject(nonconformity, nonconformityParam, context);

        MP3398_SyncNonconformity_001 syncNonconformity = new MP3398_SyncNonconformity_001();
        syncNonconformity.setNonconformity(nonconformity);

        MP3398_SyncNonconformity_001_Result syncResult =
                tools.performEAMOperation(context, eamws::syncNonconformityOp, syncNonconformity);

        return syncResult.getResultData().getNONCONFORMITYID().getSTANDARDENTITYCODE();
    }

    @Override
    public String deleteNonconformity(EAMContext context, String nonconformityCode) throws EAMException {
        MP3399_DeleteNonconformity_001 deleteNonconformity = new MP3399_DeleteNonconformity_001();

        deleteNonconformity.setNONCONFORMITYID(new STANDARDENTITYID_Type());
        deleteNonconformity.getNONCONFORMITYID().setSTANDARDENTITYCODE(nonconformityCode);
        deleteNonconformity.getNONCONFORMITYID().setORGANIZATIONID(tools.getOrganization(context));

        tools.performEAMOperation(context, eamws::deleteNonconformityOp, deleteNonconformity);

        return nonconformityCode;
    }
}
