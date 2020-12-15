package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.EntityId;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLEntry;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLEntryId;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLValue;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserDefinedListService {
    // High level services
    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDLIST_HLR)
    Map<String, ArrayList<UDLValue>> readUserDefinedLists(InforContext context, UDLEntryId entryId) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDLIST_HLU)
    String setUserDefinedLists(InforContext context, EntityId entityId, Map<String, ArrayList<UDLValue>> values)
            throws InforException;

    // Low level services
    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDLIST_R)
    List<UDLEntry> readUserDefinedListEntries(InforContext context, UDLEntryId filters) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDLIST_C)
    String createUserDefinedListEntry(InforContext context, UDLEntry entry) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDLIST_U)
    String updateUserDefinedListEntry(InforContext context, UDLEntry entry) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDLIST_D)
    String deleteUserDefinedListEntries(InforContext context, UDLEntryId filters) throws InforException;

    // Helper methods to use when performing CRUD entity functions

    /**
     * This method should be used after you read an entity from Infor. It will set its UDL to the correct one.
     * If we fail to retrieve the UDLs it will silently fail, with a SEVERE log level message,
     * so that if there is no UDL UDS configured, such as in the case of using vanilla Infor,
     * the read will still suceed with all other fields.
     * @param context infor context
     * @param entity the entity that you are reading
     * @param entityId the id of the entity that you are reading
     */
    void readUDLToEntity(InforContext context, UserDefinedListHelpable entity, EntityId entityId);

    /**
     * This method should be used after you create an entity in Infor. It will insert the UDL in the database.
     * As it is impossible/difficult to rollback the Infor entity creation, this method is always assumed to succeed.
     * If this does not happen, a SEVERE log level message will be logged and no exception will be thrown.
     * @param context infor context
     * @param entity the entity that you are creating
     * @param entityId the id of the entity that you are reading
     */
    void writeUDLToEntityCopyFrom(InforContext context, UserDefinedListHelpable entity, EntityId entityId);

    /**
     * This method should be used after you update an entity in Infor. It will update the UDL in the database.
     * As it is impossible/difficult to rollback the Infor entity update, this method is always assumed to succeed.
     * If this does not happen, a SEVERE log level message will be logged and no exception will be thrown.
     * @param context infor context
     * @param entity the entity that you are updating
     * @param entityId the id of the entity that you are reading
     */
    void writeUDLToEntity(InforContext context, UserDefinedListHelpable entity, EntityId entityId);

    /**
     * This method should be used after you delete an entity in Infor. It will clear its UDL in the database.
     * As it is impossible/difficult to rollback the Infor entity deletion, this method is always assumed to succeed.
     * If this does not happen, a SEVERE log level message will be logged and no exception will be thrown.
     * @param context infor context
     * @param entityId the id of the entity that you are reading
     */
    void deleteUDLFromEntity(InforContext context, EntityId entityId);
}
