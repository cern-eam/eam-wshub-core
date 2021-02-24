package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;
import java.util.Map;

public interface UserDefinedScreenService {

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDTABLE_C, logDataReference1 = LogDataReferenceType.INPUT)
    String createUserDefinedScreenRow(InforContext inforContext, String screenName, UDTRow udtRow) throws InforException;

    // TODO
    //@Operation(logOperation = INFOR_OPERATION.USERDEFINEDTABLE_U, logDataReference1 = LogDataReferenceType.INPUT)
    //String updateUserDefinedScreenRow(InforContext inforContext, String screenName, UDTRow udtRow) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.USERDEFINEDTABLE_D, logDataReference1 = LogDataReferenceType.INPUT)
    String deleteUserDefinedScreenRow(InforContext inforContext, String screenName, UDTRow udtRow) throws InforException;

}
