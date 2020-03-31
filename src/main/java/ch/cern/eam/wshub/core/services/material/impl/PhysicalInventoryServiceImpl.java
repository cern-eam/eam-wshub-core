package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.PhysicalInventoryService;
import ch.cern.eam.wshub.core.services.material.entities.Part;
import ch.cern.eam.wshub.core.services.material.entities.PhysicalInventory;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.inventorytransaction_001.InventoryTransaction;
import net.datastream.schemas.mp_entities.issuereturntransactionline_001.IssueReturnTransactionLine;
import net.datastream.schemas.mp_fields.PERSONID_Type;
import net.datastream.schemas.mp_fields.STATUS_Type;
import net.datastream.schemas.mp_fields.STOREID_Type;
import net.datastream.schemas.mp_fields.TRANSACTIONID_Type;
import net.datastream.schemas.mp_functions.mp1217_001.MP1217_AddInventoryTransaction_001;
import net.datastream.schemas.mp_results.mp1217_001.MP1217_AddInventoryTransaction_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.Random;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;

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
        InventoryTransaction inventoryTransaction =
            tools.getInforFieldTools().transformWSHubObject(new InventoryTransaction(), physicalInventory, context);

        inventoryTransaction.getTRANSACTIONID().setTRANSACTIONCODE("0");
        inventoryTransaction.setTRANSACTIONSTATUS(new STATUS_Type());
        inventoryTransaction.getTRANSACTIONSTATUS().setSTATUSCODE("U");

        MP1217_AddInventoryTransaction_001 addInventoryTransaction = new MP1217_AddInventoryTransaction_001();
        addInventoryTransaction.setInventoryTransaction(inventoryTransaction);

        MP1217_AddInventoryTransaction_001_Result result =
            tools.performInforOperation(context, inforws::addInventoryTransactionOp, addInventoryTransaction);

        return tools.getInforFieldTools()
            .transformInforObject(new PhysicalInventory(), result.getResultData().getInventoryTransaction());
    }
}
