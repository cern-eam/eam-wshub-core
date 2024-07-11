package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.EntityId;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLEntry;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLEntryId;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLValue;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserDefinedListService {
    // High level services
    @Operation(logOperation = EAM_OPERATION.USERDEFINEDLIST_HLR)
    Map<String, ArrayList<UDLValue>> readUserDefinedLists(EAMContext context, UDLEntryId entryId) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERDEFINEDLIST_HLU)
    String setUserDefinedLists(EAMContext context, EntityId entityId, Map<String, ArrayList<UDLValue>> values)
            throws EAMException;

    // Low level services
    @Operation(logOperation = EAM_OPERATION.USERDEFINEDLIST_R)
    List<UDLEntry> readUserDefinedListEntries(EAMContext context, UDLEntryId filters) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERDEFINEDLIST_C)
    String createUserDefinedListEntry(EAMContext context, UDLEntry entry) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERDEFINEDLIST_U)
    String updateUserDefinedListEntry(EAMContext context, UDLEntry entry) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERDEFINEDLIST_D)
    String deleteUserDefinedListEntries(EAMContext context, UDLEntryId filters) throws EAMException;

    // Helper methods to use when performing CRUD entity functions

    /**
     * This method should be used after you read an entity from EAM. It will set its UDL to the correct one.
     * If we fail to retrieve the UDLs it will silently fail, with a SEVERE log level message,
     * so that if there is no UDL UDS configured, such as in the case of using vanilla EAM,
     * the read will still suceed with all other fields.
     * @param context eam context
     * @param entity the entity that you are reading
     * @param entityId the id of the entity that you are reading
     */
    void readUDLToEntity(EAMContext context, UserDefinedListHelpable entity, EntityId entityId);

    /**
     * This method should be used after you create an entity in EAM. It will insert the UDL in the database.
     * As it is impossible/difficult to rollback the EAM entity creation, this method is always assumed to succeed.
     * If this does not happen, a SEVERE log level message will be logged and no exception will be thrown.
     * @param context eam context
     * @param entity the entity that you are creating
     * @param entityId the id of the entity that you are reading
     */
    void writeUDLToEntityCopyFrom(EAMContext context, UserDefinedListHelpable entity, EntityId entityId);

    /**
     * This method should be used after you update an entity in EAM. It will update the UDL in the database.
     * As it is impossible/difficult to rollback the EAM entity update, this method is always assumed to succeed.
     * If this does not happen, a SEVERE log level message will be logged and no exception will be thrown.
     * @param context eam context
     * @param entity the entity that you are updating
     * @param entityId the id of the entity that you are reading
     */
    void writeUDLToEntity(EAMContext context, UserDefinedListHelpable entity, EntityId entityId);

    /**
     * This method should be used after you delete an entity in EAM. It will clear its UDL in the database.
     * As it is impossible/difficult to rollback the EAM entity deletion, this method is always assumed to succeed.
     * If this does not happen, a SEVERE log level message will be logged and no exception will be thrown.
     * @param context eam context
     * @param entityId the id of the entity that you are reading
     */
    void deleteUDLFromEntity(EAMContext context, EntityId entityId);
}
