package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.services.workorders.entities.InforCase;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;
import java.util.Map;

public interface UserDefinedTableService {

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDTABLE_C, logDataReference1 = LogDataReferenceType.INPUT)
    String createUserDefinedTableRows(InforContext context, String tableName, List<UDTRow> rows) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDTABLE_R, logDataReference1 = LogDataReferenceType.INPUT)
    List<Map<String, Object>> readUserDefinedTableRows(InforContext context, String tableName, UDTRow filters,
                                                       List<String> fieldsToRead) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDTABLE_U, logDataReference1 = LogDataReferenceType.INPUT)
    int updateUserDefinedTableRows(InforContext context, String tableName, UDTRow fieldsToUpdate, UDTRow filters)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDTABLE_D, logDataReference1 = LogDataReferenceType.INPUT)
    int deleteUserDefinedTableRows(InforContext context, String tableName,  UDTRow filters) throws InforException;

    // HELPERS

    Map<String, Object> getUDTRowAsMap(UDTRow row) throws InforException;

    List<Map<String, Object>> getUDTRowsAsMaps(List<UDTRow> row) throws InforException;

    UDTRow getMapAsUDTRow(String tableName, Map<String, Object> mapRow) throws InforException;

    List<UDTRow> getMapsAsUDTRows(String tableName, List<Map<String, Object>> mapRows) throws InforException;
}
