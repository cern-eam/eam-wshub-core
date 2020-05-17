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

    /**
     * Returns a safety object given its safety code.
     *
     * @param context the user credentials
     * @param safetyCode the safety code to get
     * @return the safety object
     * @throws InforException
     */
    @Override
    public EntitySafety getEntitySafety(InforContext context, String safetyCode) throws InforException {
        SafetyService.validateInput(safetyCode);

        MP3222_GetEntitySafety_001 getEntitySafety = new MP3222_GetEntitySafety_001();
        getEntitySafety.setSAFETYCODE(safetyCode);

        MP3222_GetEntitySafety_001_Result res = tools.performInforOperation(context, inforws::getEntitySafetyOp, getEntitySafety);

        return res.getResultData().getEntitySafety();
    }

    /**
     * Returns a list of safety object given their safety code.
     *
     * @param context the user credentials
     * @param safetyCodes the list of safety codes
     * @return the list of safety objects
     * @throws InforException
     */
    @Override
    public BatchResponse<EntitySafety> getEntitySafetiesBatch(InforContext context, List<String> safetyCodes) throws InforException {
        // Validate not needed now, since performed at .deleteSafety

        return tools.batchOperation(context, this::getEntitySafety, safetyCodes);
    }

    /**
     * Adds a safety (given a hazard and a precaution measure) to a specified entity.
     *
     * @param context the user credentials
     * @param entitySafetywshub the safety to add
     * @return the ID of the added safety
     * @throws InforException
     */
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

    /**
     * Adds a list safeties (given their hazard and a precaution measures).
     *
     * @param context the user credentials
     * @param listOfSafeties a list of the safeties to be added
     * @return a list of the safety ids added
     * @throws InforException
     */
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

    /**
     * Adds a safety (given a hazard and a precaution measure) to a specified entity. The parentID and entity values
     * must be passed as arguments, and not inside the entitySafetywshub object. The rest of the safety values should
     * be set in the entitySafetywshub object.
     *
     * @param context the user credentials
     * @param entitySafetywshub the safety to add
     * @param parentID the parent id of the safety
     * @param entity the entity of the safety
     * @return the added safety id
     * @throws InforException
     */
    @Override
    public String addSafetyToEntity(InforContext context, EntitySafetyWSHub entitySafetywshub, String parentID, String entity) throws InforException {
        // Validate not needed now, since performed at .addSafety

        EntitySafetyWSHub cloned = new EntitySafetyWSHub(entitySafetywshub);

        cloned.setEntity(entity);
        cloned.setEntitySafetyCode(parentID);

        return this.addSafety(context,  cloned);
    }

    /**
     * Adds a safety (given a hazard and a precaution measure) to list of entities. The parentID list and entity value
     * must be passed as arguments, and not inside the entitySafetywshub object. The rest of the safety values should
     * be set in the entitySafetywshub object.
     *
     * @param context the user credentials
     * @param entitySafetywshub the safety to add
     * @param parentIDs the list of parent ids of the safety
     * @param entity the entity of the safety
     * @return a list of the added safety ids
     * @throws InforException
     */
    @Override
    public BatchResponse<String> addSafetyToEntitiesBatch(InforContext context, EntitySafetyWSHub entitySafetywshub, List<String> parentIDs, String entity) throws InforException {
        // Validate not needed now, since performed at .addSafety

        List<EntitySafetyWSHub> safetiesToAdd = new ArrayList<EntitySafetyWSHub>();
        parentIDs.stream().forEach(parentID -> {
            EntitySafetyWSHub cloned = new EntitySafetyWSHub(entitySafetywshub);
            cloned.setEntity(entity);
            cloned.setEntitySafetyCode(parentID);
            safetiesToAdd.add(cloned);
        });

        return tools.batchOperation(context, this::addSafety, safetiesToAdd);
    }

    /**
     * Deletes a safety given its id (safety code).
     *
     * @param context the user credentials
     * @param safetyCode the safety code of the safety to delete
     * @return
     * @throws InforException
     */
    @Override
    public String deleteSafety(InforContext context, String safetyCode) throws InforException {
        SafetyService.validateInput(safetyCode);

        MP3221_DeleteEntitySafety_001 deleteEntitySafety = new MP3221_DeleteEntitySafety_001();
        deleteEntitySafety.setSAFETYCODE(safetyCode);

        tools.performInforOperation(context, inforws::deleteEntitySafetyOp, deleteEntitySafety);
        return "OK";
    }

    /**
     * Deletes safeties given their ids (safety codes).
     *
     * @param context the user credentials
     * @param safetyCodes a list of the safety codes of the safeties to delete
     * @return
     * @throws InforException
     */
    @Override
    public BatchResponse<String> deleteSafetiesBatch(InforContext context, List<String> safetyCodes) throws InforException {
        // Validate not needed now, since performed at .deleteSafety

        return tools.batchOperation(context, this::deleteSafety, safetyCodes);
    }

    /**
     * Updates the specified safety with the given values of the safety object.
     *
     * @param context the user credentials
     * @param entitySafetywshub the safety to be updated, with its updated values
     * @return the updated safety object
     * @throws InforException
     */
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



}
