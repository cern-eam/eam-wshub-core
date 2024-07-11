package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.PurchaseOrder;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface PurchaseOrdersService {

    @Operation(logOperation = EAM_OPERATION.PURCHORDE_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "number")
            String updatePurchaseOrder(EAMContext context, PurchaseOrder purchaseOrderParam)
            throws EAMException;
}
