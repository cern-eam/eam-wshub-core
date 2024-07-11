package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.material.entities.PhysicalInventory;
import ch.cern.eam.wshub.core.services.material.entities.PhysicalInventoryRow;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface PhysicalInventoryService {
    @Operation(logOperation = EAM_OPERATION.INVENTORY_C)
    PhysicalInventory createPhysicalInventory(EAMContext context, PhysicalInventory physicalInventory)
        throws EAMException;

    @Operation(logOperation = EAM_OPERATION.INVENTORY_R)
    PhysicalInventory readPhysicalInventory(EAMContext context, String code)
        throws EAMException;

    @Operation(logOperation = EAM_OPERATION.INVENTORY_U)
    PhysicalInventory updatePhysicalInventory(EAMContext context, PhysicalInventory physicalInventory)
        throws EAMException;

    @Operation(logOperation = EAM_OPERATION.INVENTORY_RL)
    PhysicalInventoryRow readPhysicalInventoryLine(EAMContext context, PhysicalInventoryRow row)
        throws EAMException;

    @Operation(logOperation = EAM_OPERATION.INVENTORY_UL)
    PhysicalInventoryRow updatePhysicalInventoryLine(EAMContext context, PhysicalInventoryRow row)
        throws EAMException;

    @Operation(logOperation = EAM_OPERATION.INVENTORY_RD)
    PhysicalInventory readDefaultPhysicalInventory(EAMContext context, String storeCode)
        throws EAMException;
}
