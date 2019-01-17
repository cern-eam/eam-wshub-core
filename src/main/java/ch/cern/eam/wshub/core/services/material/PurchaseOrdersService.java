package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.PurchaseOrder;
import ch.cern.eam.wshub.core.tools.InforException;

public interface PurchaseOrdersService {

    @Operation(logOperation = INFOR_OPERATION.PURCHORDE_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "number")
            String updatePurchaseOrder(InforContext context, PurchaseOrder purchaseOrderParam)
            throws InforException;
}
