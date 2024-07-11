package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.workorders.StandardWorkOrderChildService;
import ch.cern.eam.wshub.core.services.workorders.entities.StandardWorkOrder;
import ch.cern.eam.wshub.core.services.workorders.entities.StandardWorkOrderChild;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.standardwochild_001.StandardWOChild;
import net.datastream.schemas.mp_fields.STANDARDWOCHILDID_Type;
import net.datastream.schemas.mp_fields.STDWOID_Type;
import net.datastream.schemas.mp_functions.mp7271_001.MP7271_GetStandardWOChild_001;
import net.datastream.schemas.mp_functions.mp7273_001.MP7273_AddStandardWOChild_001;
import net.datastream.schemas.mp_functions.mp7274_001.MP7274_SyncStandardWOChild_001;
import net.datastream.schemas.mp_functions.mp7275_001.MP7275_DeleteStandardWOChild_001;
import net.datastream.schemas.mp_functions.mp7746_001.MP7746_GetStandardWOChildDefault_001;
import net.datastream.schemas.mp_results.mp7271_001.MP7271_GetStandardWOChild_001_Result;
import net.datastream.schemas.mp_results.mp7273_001.MP7273_AddStandardWOChild_001_Result;
import net.datastream.schemas.mp_results.mp7274_001.MP7274_SyncStandardWOChild_001_Result;
import net.datastream.schemas.mp_results.mp7275_001.MP7275_DeleteStandardWOChild_001_Result;
import net.datastream.schemas.mp_results.mp7746_001.MP7746_GetStandardWOChildDefault_001_Result;
import net.datastream.schemas.mp_results.mp7746_001.ResultData;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

public class StandardWorkOrderChildServiceImpl implements StandardWorkOrderChildService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public StandardWorkOrderChildServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
    }

    @Override
    public StandardWorkOrder readStandardWorkOrderChildDefault(EAMContext context, String standardWOCode) throws EAMException {
        MP7746_GetStandardWOChildDefault_001 op = new MP7746_GetStandardWOChildDefault_001();
        op.setSTANDARDWO(new STDWOID_Type());
        op.getSTANDARDWO().setORGANIZATIONID(tools.getOrganization(context));
        op.getSTANDARDWO().setSTDWOCODE(standardWOCode);
        final MP7746_GetStandardWOChildDefault_001_Result mp7746_getStandardWOChildDefault_001_result = tools.performEAMOperation(context, eamws::getStandardWOChildDefaultOp, op);
        final ResultData resultData = mp7746_getStandardWOChildDefault_001_result.getResultData();
        final StandardWorkOrder standardWorkOrder = tools.getEAMFieldTools().transformEAMObject(new StandardWorkOrder(), resultData, context);
        return standardWorkOrder;
    }

    @Override
    public StandardWorkOrderChild createStandardWorkOrderChild(EAMContext context, StandardWorkOrderChild standardWorkOrderChild) throws EAMException {
        final MP7273_AddStandardWOChild_001 mp7273_addStandardWOChild_001 = new MP7273_AddStandardWOChild_001();
        mp7273_addStandardWOChild_001.setStandardWOChild(new StandardWOChild());

        final STANDARDWOCHILDID_Type standardwochildid_type = tools.getEAMFieldTools().transformWSHubObject(new STANDARDWOCHILDID_Type(), standardWorkOrderChild, context);
        mp7273_addStandardWOChild_001.getStandardWOChild().setSTANDARDWOCHILDID(standardwochildid_type);

        final MP7273_AddStandardWOChild_001_Result mp7273_addStandardWOChild_001_result = tools.performEAMOperation(context, eamws::addStandardWOChildOp, mp7273_addStandardWOChild_001);
        return tools.getEAMFieldTools().transformEAMObject(new StandardWorkOrderChild(), mp7273_addStandardWOChild_001_result.getResultData().getSTANDARDWOCHILDID(), context);
    }

    @Override
    public StandardWorkOrderChild readStandardWorkOrderChild(EAMContext context, StandardWorkOrderChild standardWorkOrderChild) throws EAMException {
        final MP7271_GetStandardWOChild_001 mp7271_getStandardWOChild_001 = new MP7271_GetStandardWOChild_001();
        final STANDARDWOCHILDID_Type standardwochildid_type = tools.getEAMFieldTools().transformWSHubObject(new STANDARDWOCHILDID_Type(), standardWorkOrderChild, context);
        mp7271_getStandardWOChild_001.setSTANDARDWOCHILDID(standardwochildid_type);

        final MP7271_GetStandardWOChild_001_Result mp7271_getStandardWOChild_001_result = tools.performEAMOperation(context, eamws::getStandardWOChildOp, mp7271_getStandardWOChild_001);
        return tools.getEAMFieldTools().transformEAMObject(new StandardWorkOrderChild(), mp7271_getStandardWOChild_001_result.getResultData().getStandardWOChild().getSTANDARDWOCHILDID(), context);
    }

    @Override
    public StandardWorkOrderChild updateStandardWorkOrderChild(EAMContext context, StandardWorkOrderChild standardWorkOrderChild) throws EAMException {
        assert standardWorkOrderChild.getOldSequence() != null;
        assert standardWorkOrderChild.getOldStep() != null;

        // Start by assigning old Step and old Status
        final MP7274_SyncStandardWOChild_001 mp7274_syncStandardWOChild_001 = tools.getEAMFieldTools().transformWSHubObject(new MP7274_SyncStandardWOChild_001(), standardWorkOrderChild, context);

        //Read previous STWO
        final MP7271_GetStandardWOChild_001 mp7271_getStandardWOChild_001_2 = new MP7271_GetStandardWOChild_001();
        final STANDARDWOCHILDID_Type standardwochildid_type2 = tools.getEAMFieldTools().transformWSHubObject(new STANDARDWOCHILDID_Type(), standardWorkOrderChild, context);
        standardwochildid_type2.setSTEP(standardWorkOrderChild.getOldStep().longValue());
        standardwochildid_type2.setSEQUENCE(standardWorkOrderChild.getOldSequence().longValue());
        mp7271_getStandardWOChild_001_2.setSTANDARDWOCHILDID(standardwochildid_type2);
        final MP7271_GetStandardWOChild_001_Result mp7271_getStandardWOChild_001_result = tools.performEAMOperation(context, eamws::getStandardWOChildOp, mp7271_getStandardWOChild_001_2);

        //Assign the previous values, such as record id, to object, simulating a PATCH operation
        mp7274_syncStandardWOChild_001.setStandardWOChild(mp7271_getStandardWOChild_001_result.getResultData().getStandardWOChild());

        //Patch the object
        final STANDARDWOCHILDID_Type standardwochildid_type = tools.getEAMFieldTools().transformWSHubObject(new STANDARDWOCHILDID_Type(), standardWorkOrderChild, context);
        mp7274_syncStandardWOChild_001.getStandardWOChild().setSTANDARDWOCHILDID(standardwochildid_type);

        final MP7274_SyncStandardWOChild_001_Result mp7274_syncStandardWOChild_001_result1 = tools.performEAMOperation(context, eamws::syncStandardWOChildOp, mp7274_syncStandardWOChild_001);
        return tools.getEAMFieldTools().transformEAMObject(new StandardWorkOrderChild(), mp7274_syncStandardWOChild_001_result1.getResultData().getSTANDARDWOCHILDID(), context);
    }

    @Override
    public StandardWorkOrderChild deleteStandardWorkOrderChild(EAMContext context, StandardWorkOrderChild standardWorkOrderChild) throws EAMException {
        final STANDARDWOCHILDID_Type standardwochildid_type = tools.getEAMFieldTools().transformWSHubObject(new STANDARDWOCHILDID_Type(), standardWorkOrderChild, context);
        final MP7275_DeleteStandardWOChild_001 mp7275_deleteStandardWOChild_001 = new MP7275_DeleteStandardWOChild_001();
        mp7275_deleteStandardWOChild_001.setSTANDARDWOCHILDID(standardwochildid_type);
        final MP7275_DeleteStandardWOChild_001_Result mp7275_deleteStandardWOChild_001_result = tools.performEAMOperation(context, eamws::deleteStandardWOChildOp, mp7275_deleteStandardWOChild_001);
        return  tools.getEAMFieldTools().transformEAMObject(new StandardWorkOrderChild(), mp7275_deleteStandardWOChild_001_result.getResultData().getSTANDARDWOCHILDID(), context);
    }
}
