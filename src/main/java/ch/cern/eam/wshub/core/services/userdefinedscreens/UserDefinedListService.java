package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLEntry;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLProperty;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface UserDefinedListService {
    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDLIST_C)
    String createUserDefinedListEntry(InforContext context, UDLProperty property, UDLEntry entry)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDLIST_R)
    List<UDLEntry> readUserDefinedListEntries(InforContext context, UDLProperty property)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDLIST_U)
    String updateUserDefinedListEntry(InforContext context, UDLProperty oldProperty, UDLEntry newEntry)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDLIST_D)
    String deleteUserDefinedListEntry(InforContext context, UDLProperty property)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDLIST)
    String setUserDefinedList(InforContext context, UDLProperty property, List<UDLEntry> entries)
            throws InforException;

    // Helper methods to use when performing CRUD entity functions
    void readUDLToEntity(InforContext context, UserDefinedListHelpable entity, String entityType, String code);
    void writeUDLToEntityCopyFrom(InforContext context, UserDefinedListHelpable entity, String entityType, String code);
    void writeUDLToEntity(InforContext context, UserDefinedListHelpable entity, String entityType, String code);
    void deleteUDLFromEntity(InforContext context, String entityType, String code);
}
