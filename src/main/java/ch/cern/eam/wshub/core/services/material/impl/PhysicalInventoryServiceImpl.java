package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.PhysicalInventoryService;
import ch.cern.eam.wshub.core.services.material.entities.PhysicalInventory;
import ch.cern.eam.wshub.core.services.material.entities.PhysicalInventoryRow;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.inventorytransaction_001.InventoryTransaction;
import net.datastream.schemas.mp_entities.physicalinventoryline_001.PhysicalInventoryLine;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp1217_001.MP1217_AddInventoryTransaction_001;
import net.datastream.schemas.mp_functions.mp1294_001.MP1294_SyncPhysicalInventoryLine_001;
import net.datastream.schemas.mp_functions.mp2244_001.MP2244_GetPhysicalInventoryLine_001;
import net.datastream.schemas.mp_results.mp1217_001.MP1217_AddInventoryTransaction_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

public class PhysicalInventoryServiceImpl implements PhysicalInventoryService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public PhysicalInventoryServiceImpl(
            ApplicationData applicationData,
            Tools tools,
            InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    @Override
    public PhysicalInventory createPhysicalInventory(InforContext context, PhysicalInventory physicalInventory)
            throws InforException {
        InventoryTransaction inventoryTransaction = new InventoryTransaction();
        inventoryTransaction.setTRANSACTIONID(new TRANSACTIONID_Type());
        inventoryTransaction.getTRANSACTIONID().setTRANSACTIONCODE("0");
        tools.getInforFieldTools().transformWSHubObject(inventoryTransaction, physicalInventory, context);

        // TODO: do not overwrite status
        inventoryTransaction.setTRANSACTIONSTATUS(new STATUS_Type());
        inventoryTransaction.getTRANSACTIONSTATUS().setSTATUSCODE("U");

        MP1217_AddInventoryTransaction_001 addInventoryTransaction = new MP1217_AddInventoryTransaction_001();
        addInventoryTransaction.setInventoryTransaction(inventoryTransaction);

        MP1217_AddInventoryTransaction_001_Result result =
            tools.performInforOperation(context, inforws::addInventoryTransactionOp, addInventoryTransaction);

        return tools.getInforFieldTools()
            .transformInforObject(new PhysicalInventory(), result.getResultData().getInventoryTransaction());
    }

    @Override
    public PhysicalInventoryRow updatePhysicalInventoryLine(InforContext context, PhysicalInventoryRow row)
            throws InforException {
        ORGANIZATIONID_Type organizationIdType = new ORGANIZATIONID_Type();
        organizationIdType.setORGANIZATIONCODE("*");

        TRANSACTIONLINEID transactionLineId = new TRANSACTIONLINEID();
        transactionLineId.setTRANSACTIONID(new TRANSACTIONID_Type());
        transactionLineId.getTRANSACTIONID().setTRANSACTIONCODE(row.getPhysicalInventoryCode());
        transactionLineId.getTRANSACTIONID().setORGANIZATIONID(organizationIdType); // TODO: is this ok?
        transactionLineId.setTRANSACTIONLINENUM(row.getLine().longValue()); // TODO: unsafe?

        MP2244_GetPhysicalInventoryLine_001 getPhysicalInventoryLine =
            new MP2244_GetPhysicalInventoryLine_001();
        getPhysicalInventoryLine.setTRANSACTIONLINEID(transactionLineId);

        PhysicalInventoryLine physicalInventoryLine =
            tools.performInforOperation(context, inforws::getPhysicalInventoryLineOp, getPhysicalInventoryLine)
                .getResultData().getPhysicalInventoryLine();

        tools.getInforFieldTools().transformWSHubObject(physicalInventoryLine, row, context);

        MP1294_SyncPhysicalInventoryLine_001 syncPhysicalInventoryLine =
            new MP1294_SyncPhysicalInventoryLine_001();

        syncPhysicalInventoryLine.setPhysicalInventoryLine(physicalInventoryLine);

        PhysicalInventoryLine result = tools.performInforOperation(context, inforws::syncPhysicalInventoryLineOp, syncPhysicalInventoryLine)
            .getResultData().getPhysicalInventoryLine();

        return tools.getInforFieldTools().transformInforObject(new PhysicalInventoryRow(), result);
    }
}
