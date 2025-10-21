package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.NoPoReceipt;
import ch.cern.eam.wshub.core.tools.InforException;

public interface NonPoReceiptService {
    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPT_C)
    String createNoPoReceipt(InforContext context, NoPoReceipt receipt) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPT_R)
    NoPoReceipt readNoPoReceipt(InforContext context, String receiptCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPT_U)
    String updateNoPoReceipt(InforContext context, NoPoReceipt receipt) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPT_D)
    String deleteNoPoReceipt(InforContext context, String receiptCode) throws InforException;

}
