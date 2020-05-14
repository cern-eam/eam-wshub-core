package ch.cern.eam.wshub.core.services.safety.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.safety.SafetyService;
import ch.cern.eam.wshub.core.services.safety.entities.EntitySafetyWSHub;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.entitysafety_001.EntitySafety;
import net.datastream.schemas.mp_functions.mp3219_001.MP3219_AddEntitySafety_001;
import net.datastream.schemas.mp_functions.mp3220_001.MP3220_SyncEntitySafety_001;
import net.datastream.schemas.mp_functions.mp3221_001.MP3221_DeleteEntitySafety_001;
import net.datastream.schemas.mp_functions.mp3222_001.MP3222_GetEntitySafety_001;
import net.datastream.schemas.mp_results.mp3219_001.MP3219_AddEntitySafety_001_Result;
import net.datastream.schemas.mp_results.mp3220_001.MP3220_SyncEntitySafety_001_Result;
import net.datastream.schemas.mp_results.mp3222_001.MP3222_GetEntitySafety_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SafetyServiceImpl implements SafetyService {
    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public SafetyServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    @Override
    public String addSafety(InforContext context, EntitySafetyWSHub entitySafetywshub) throws InforException {
        SafetyService.validateInput(entitySafetywshub);

        EntitySafety entitySafetyInfor = new EntitySafety();
        entitySafetyInfor.setSAFETYCODE("0"); // Safety code must be set to any value (infor will assign an internal one later)

        tools.getInforFieldTools().transformWSHubObject(entitySafetyInfor, entitySafetywshub, context);

        MP3219_AddEntitySafety_001 addEntitySafety = new MP3219_AddEntitySafety_001();
        addEntitySafety.getEntitySafety().add(entitySafetyInfor);

        MP3219_AddEntitySafety_001_Result res = tools.performInforOperation(context, inforws::addEntitySafetyOp, addEntitySafety);

        return res.getResultData().getSAFETYCODE().get(0);
    }

    @Override
    public List<String> addSafeties(InforContext context, List<EntitySafetyWSHub> listOfSafeties) throws InforException {
        SafetyService.validateInput(listOfSafeties);

        MP3219_AddEntitySafety_001 addEntitySafety = new MP3219_AddEntitySafety_001();
        for (EntitySafetyWSHub entity : listOfSafeties) {
            EntitySafety entitySafetyInfor = new EntitySafety();
            entitySafetyInfor.setSAFETYCODE("0"); // Safety code must be set to any value (infor will assign an internal one later)
            tools.getInforFieldTools().transformWSHubObject(entitySafetyInfor, entity, context);
            addEntitySafety.getEntitySafety().add(entitySafetyInfor);
        }

        MP3219_AddEntitySafety_001_Result res = tools.performInforOperation(context, inforws::addEntitySafetyOp, addEntitySafety);

        return res.getResultData().getSAFETYCODE();
    }

    @Override
    public String deleteSafety(InforContext context, String safetyCode) throws InforException {
        SafetyService.validateInput(safetyCode);

        MP3221_DeleteEntitySafety_001 deleteEntitySafety = new MP3221_DeleteEntitySafety_001();
        deleteEntitySafety.setSAFETYCODE(safetyCode);

        tools.performInforOperation(context, inforws::deleteEntitySafetyOp, deleteEntitySafety);
        return "OK";
    }

    @Override
    public EntitySafety getEntitySafety(InforContext context, String safetyCode) throws InforException {
        SafetyService.validateInput(safetyCode);

        MP3222_GetEntitySafety_001 getEntitySafety = new MP3222_GetEntitySafety_001();
        getEntitySafety.setSAFETYCODE(safetyCode);

        MP3222_GetEntitySafety_001_Result res = tools.performInforOperation(context, inforws::getEntitySafetyOp, getEntitySafety);

        return res.getResultData().getEntitySafety();
    }

    @Override
    public String syncEntitySafety(InforContext context, EntitySafetyWSHub entitySafetywshub) throws InforException {
        SafetyService.validateInput(entitySafetywshub);

        MP3222_GetEntitySafety_001 getEntitySafety = new MP3222_GetEntitySafety_001();
        getEntitySafety.setSAFETYCODE(entitySafetywshub.getSafetyCode());
        MP3222_GetEntitySafety_001_Result resGet = tools.performInforOperation(context, inforws::getEntitySafetyOp, getEntitySafety);
        EntitySafety entitySafetyInfor = resGet.getResultData().getEntitySafety();

        tools.getInforFieldTools().transformWSHubObject(entitySafetyInfor, entitySafetywshub, context);

        MP3220_SyncEntitySafety_001 syncEntitySafety = new MP3220_SyncEntitySafety_001();

        syncEntitySafety.setEntitySafety(entitySafetyInfor);
        MP3220_SyncEntitySafety_001_Result resSync = tools.performInforOperation(context, inforws::syncEntitySafetyOp, syncEntitySafety);

        return resSync.getResultData().getSAFETYCODE();
    }


    /**
     * If a function code is specified in menuSpecification, deletes all children functions with that function code from
     * the specified path. If the function code provided is an empty string, deletes the last menu item with all its
     * children from the path specified.
     *
     * @param context           the user credentials
     * @param menuSpecification the specified path and function to delete, for a specific user group
     * @return
     */
    @Override
    public String addSafety(InforContext context, EntitySafetyWSHub entitySafetywshub, String parentID, String entity) throws InforException {
        // Validate not needed now, since performed at .addSafety

        EntitySafetyWSHub cloned = new EntitySafetyWSHub(entitySafetywshub);

        cloned.setEntity(entity);
        cloned.setEntitySafetyCode(parentID);

        return this.addSafety(context,  cloned);
    }

    @Override
    public BatchResponse<String> addSafetiesBatch(InforContext context, EntitySafetyWSHub entitySafetywshub, List<String> parentIDs, String entity) throws InforException {
        // Validate not needed now, since performed at .addSafety

        List<EntitySafetyWSHub> safetiesToAdd = new ArrayList<EntitySafetyWSHub>();
        parentIDs.stream().forEach(parentID -> {
            EntitySafetyWSHub cloned = new EntitySafetyWSHub(entitySafetywshub);
            cloned.setEntity(entity);
            cloned.setEntitySafetyCode(parentID);
            safetiesToAdd.add(cloned);
            System.out.println("ADD ONE");
        });

//        for (String parentID : parentIDs){
//            EntitySafetyWSHub cloned = new EntitySafetyWSHub(entitySafetywshub);
//            cloned.setEntity(entity);
//            cloned.setEntitySafetyCode(parentID);
//        }

//        return this.addSafety(context,  entitySafetywshub);

        return tools.batchOperation(context, this::addSafety, safetiesToAdd);
    }

}
