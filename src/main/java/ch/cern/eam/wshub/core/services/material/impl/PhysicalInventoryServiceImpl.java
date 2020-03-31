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
import net.datastream.schemas.mp_functions.mp1218_001.MP1218_SyncInventoryTransaction_001;
import net.datastream.schemas.mp_functions.mp1220_001.MP1220_GetInventoryTransaction_001;
import net.datastream.schemas.mp_functions.mp1294_001.MP1294_SyncPhysicalInventoryLine_001;
import net.datastream.schemas.mp_functions.mp2244_001.MP2244_GetPhysicalInventoryLine_001;
import net.datastream.schemas.mp_results.mp1217_001.MP1217_AddInventoryTransaction_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.math.BigInteger;

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

        MP1217_AddInventoryTransaction_001 addInventoryTransaction = new MP1217_AddInventoryTransaction_001();
        addInventoryTransaction.setInventoryTransaction(inventoryTransaction);

        MP1217_AddInventoryTransaction_001_Result result =
            tools.performInforOperation(context, inforws::addInventoryTransactionOp, addInventoryTransaction);

        return tools.getInforFieldTools()
            .transformInforObject(new PhysicalInventory(), result.getResultData().getInventoryTransaction());
    }

    @Override
    public PhysicalInventory readPhysicalInventory(InforContext context, String code) throws InforException {
        return tools.getInforFieldTools().transformInforObject(
            new PhysicalInventory(),
            getInventory(context, code));
    }

    @Override
    public PhysicalInventory updatePhysicalInventory(InforContext context, PhysicalInventory physicalInventory)
            throws InforException {
        InventoryTransaction inventoryTransaction = getInventory(context, physicalInventory.getCode());
        tools.getInforFieldTools().transformWSHubObject(inventoryTransaction, physicalInventory, context);

        MP1218_SyncInventoryTransaction_001 syncInventoryTransaction =
            new MP1218_SyncInventoryTransaction_001();
        syncInventoryTransaction.setInventoryTransaction(inventoryTransaction);

        InventoryTransaction result =
            tools.performInforOperation(context, inforws::syncInventoryTransactionOp, syncInventoryTransaction)
                .getResultData().getInventoryTransaction();

        return tools.getInforFieldTools().transformInforObject(new PhysicalInventory(), result);
    }

    private InventoryTransaction getInventory(InforContext context, String code) throws InforException {
        MP1220_GetInventoryTransaction_001 getInventoryTransaction =
                new MP1220_GetInventoryTransaction_001();
        getInventoryTransaction.setTRANSACTIONID(new TRANSACTIONID_Type());
        getInventoryTransaction.getTRANSACTIONID().setTRANSACTIONCODE(code);
        getInventoryTransaction.getTRANSACTIONID().setORGANIZATIONID(tools.getOrganization(context));

        return tools.performInforOperation(context, inforws::getInventoryTransactionOp, getInventoryTransaction)
            .getResultData().getInventoryTransaction();
    }

    @Override
    public PhysicalInventoryRow readPhysicalInventoryLine(InforContext context, PhysicalInventoryRow row) throws InforException {
        return tools.getInforFieldTools().transformInforObject(
            new PhysicalInventoryRow(),
            getLine(context, row.getPhysicalInventoryCode(), row.getLineNumber()));
    }

    @Override
    public PhysicalInventoryRow updatePhysicalInventoryLine(InforContext context, PhysicalInventoryRow row)
            throws InforException {
        PhysicalInventoryLine physicalInventoryLine = getLine(context, row.getPhysicalInventoryCode(), row.getLineNumber());

        tools.getInforFieldTools().transformWSHubObject(physicalInventoryLine, row, context);

        physicalInventoryLine.setPHYSICALQUANTITY(
                tools.getDataTypeTools().encodeQuantity(row.getPhysicalQuantity(), "Physical Quantity"));

        MP1294_SyncPhysicalInventoryLine_001 syncPhysicalInventoryLine =
            new MP1294_SyncPhysicalInventoryLine_001();

        syncPhysicalInventoryLine.setPhysicalInventoryLine(physicalInventoryLine);

        PhysicalInventoryLine result = tools.performInforOperation(context, inforws::syncPhysicalInventoryLineOp, syncPhysicalInventoryLine)
            .getResultData().getPhysicalInventoryLine();

        return tools.getInforFieldTools().transformInforObject(new PhysicalInventoryRow(), result);
    }

    private PhysicalInventoryLine getLine(InforContext context, String code, Long lineNumber)
            throws InforException {
        TRANSACTIONLINEID transactionLineId = new TRANSACTIONLINEID();
        transactionLineId.setTRANSACTIONID(new TRANSACTIONID_Type());
        transactionLineId.getTRANSACTIONID().setTRANSACTIONCODE(code);
        transactionLineId.getTRANSACTIONID().setORGANIZATIONID(tools.getOrganization(context));

        // assume there are less than 2^64 lines in a physical inventory
        transactionLineId.setTRANSACTIONLINENUM(lineNumber);

        MP2244_GetPhysicalInventoryLine_001 getPhysicalInventoryLine =
                new MP2244_GetPhysicalInventoryLine_001();
        getPhysicalInventoryLine.setTRANSACTIONLINEID(transactionLineId);

        return tools.performInforOperation(context, inforws::getPhysicalInventoryLineOp, getPhysicalInventoryLine)
            .getResultData().getPhysicalInventoryLine();
    }
}
