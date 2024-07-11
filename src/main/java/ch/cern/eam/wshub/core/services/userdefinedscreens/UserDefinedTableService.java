package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.util.List;
import java.util.Map;

public interface UserDefinedTableService {

    @Operation(logOperation = EAM_OPERATION.USERDEFINEDTABLE_C, logDataReference1 = LogDataReferenceType.INPUT)
    String createUserDefinedTableRows(EAMContext context, String tableName, List<UDTRow> rows) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERDEFINEDTABLE_R, logDataReference1 = LogDataReferenceType.INPUT)
    List<Map<String, Object>> readUserDefinedTableRows(EAMContext context, String tableName, UDTRow filters,
                                                       List<String> fieldsToRead) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERDEFINEDTABLE_U, logDataReference1 = LogDataReferenceType.INPUT)
    int updateUserDefinedTableRows(EAMContext context, String tableName, UDTRow fieldsToUpdate, UDTRow filters)
            throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERDEFINEDTABLE_D, logDataReference1 = LogDataReferenceType.INPUT)
    int deleteUserDefinedTableRows(EAMContext context, String tableName,  UDTRow filters) throws EAMException;

    // HELPERS

    Map<String, Object> getUDTRowAsMap(UDTRow row) throws EAMException;

    List<Map<String, Object>> getUDTRowsAsMaps(List<UDTRow> row) throws EAMException;

    UDTRow getMapAsUDTRow(String tableName, Map<String, Object> mapRow) throws EAMException;

    List<UDTRow> getMapsAsUDTRows(String tableName, List<Map<String, Object>> mapRows) throws EAMException;
}
