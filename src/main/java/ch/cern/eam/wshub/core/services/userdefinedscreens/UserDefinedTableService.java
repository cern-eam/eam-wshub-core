package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.services.workorders.entities.InforCase;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface UserDefinedTableService {

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDTABLE_C, logDataReference1 = LogDataReferenceType.INPUT)
    String createUserDefinedTableRows(InforContext context, String tableName, List<UDTRow> rows) throws InforException;
}
