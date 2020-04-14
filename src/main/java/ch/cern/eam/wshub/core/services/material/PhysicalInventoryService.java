package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.PhysicalInventory;
import ch.cern.eam.wshub.core.services.material.entities.PhysicalInventoryRow;
import ch.cern.eam.wshub.core.tools.InforException;

public interface PhysicalInventoryService {
    @Operation(logOperation = INFOR_OPERATION.INVENTORY_C)
    PhysicalInventory createPhysicalInventory(InforContext context, PhysicalInventory physicalInventory)
        throws InforException;

    @Operation(logOperation = INFOR_OPERATION.INVENTORY_R)
    PhysicalInventory readPhysicalInventory(InforContext context, String code)
        throws InforException;

    @Operation(logOperation = INFOR_OPERATION.INVENTORY_U)
    PhysicalInventory updatePhysicalInventory(InforContext context, PhysicalInventory physicalInventory)
        throws InforException;

    @Operation(logOperation = INFOR_OPERATION.INVENTORY_RL)
    PhysicalInventoryRow readPhysicalInventoryLine(InforContext context, PhysicalInventoryRow row)
        throws InforException;

    @Operation(logOperation = INFOR_OPERATION.INVENTORY_UL)
    PhysicalInventoryRow updatePhysicalInventoryLine(InforContext context, PhysicalInventoryRow row)
        throws InforException;

    @Operation(logOperation = INFOR_OPERATION.INVENTORY_RD)
    PhysicalInventory readDefaultPhysicalInventory(InforContext context, String storeCode)
        throws InforException;
}
