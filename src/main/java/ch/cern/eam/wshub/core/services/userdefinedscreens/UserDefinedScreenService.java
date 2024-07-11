package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface UserDefinedScreenService {

    @Operation(logOperation = EAM_OPERATION.USERDEFINEDTABLE_C, logDataReference1 = LogDataReferenceType.INPUT)
    String createUserDefinedScreenRow(EAMContext eamContext, String screenName, UDTRow udtRow) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERDEFINEDTABLE_U, logDataReference1 = LogDataReferenceType.INPUT)
    String updateUserDefinedScreenRow(EAMContext eamContext, String screenName, UDTRow udtRow, UDTRow filter) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.USERDEFINEDTABLE_D, logDataReference1 = LogDataReferenceType.INPUT)
    String deleteUserDefinedScreenRow(EAMContext eamContext, String screenName, UDTRow udtRow) throws EAMException;

}
