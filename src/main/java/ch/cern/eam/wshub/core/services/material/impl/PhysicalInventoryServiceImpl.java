package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.PhysicalInventoryService;
import ch.cern.eam.wshub.core.services.material.entities.PhysicalInventory;
import ch.cern.eam.wshub.core.services.material.entities.PhysicalInventoryRow;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.inventorytransaction_001.InventoryTransaction;
import net.datastream.schemas.mp_entities.inventorytransactiondefault_001.InventoryTransactionDefault;
import net.datastream.schemas.mp_entities.physicalinventoryline_001.PhysicalInventoryLine;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp1217_001.MP1217_AddInventoryTransaction_001;
import net.datastream.schemas.mp_functions.mp1218_001.MP1218_SyncInventoryTransaction_001;
import net.datastream.schemas.mp_functions.mp1219_001.MP1219_GetInventoryTransactionDefault_001;
import net.datastream.schemas.mp_functions.mp1220_001.MP1220_GetInventoryTransaction_001;
import net.datastream.schemas.mp_functions.mp1294_001.MP1294_SyncPhysicalInventoryLine_001;
import net.datastream.schemas.mp_functions.mp2244_001.MP2244_GetPhysicalInventoryLine_001;
import net.datastream.schemas.mp_results.mp1217_001.MP1217_AddInventoryTransaction_001_Result;
import net.datastream.schemas.mp_results.mp1220_001.ResultData;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.math.BigInteger;
import java.util.Date;

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
        ResultData resultData = getInventoryResultData(context, code);
        PhysicalInventory physicalInventory = tools.getInforFieldTools().transformInforObject(
            new PhysicalInventory(),
            resultData.getInventoryTransaction());

        physicalInventory.setCreatedBy(resultData.getCREATEDBY().getUSERCODE());
        physicalInventory.setCreatedDate(tools.getDataTypeTools().decodeInforDate(resultData.getCREATEDDATE()));

        return physicalInventory;
    }

    @Override
    public PhysicalInventory updatePhysicalInventory(InforContext context, PhysicalInventory physicalInventory)
            throws InforException {
        InventoryTransaction inventoryTransaction = getInventoryResultData(context, physicalInventory.getCode())
            .getInventoryTransaction();
        tools.getInforFieldTools().transformWSHubObject(inventoryTransaction, physicalInventory, context);

        MP1218_SyncInventoryTransaction_001 syncInventoryTransaction =
            new MP1218_SyncInventoryTransaction_001();
        syncInventoryTransaction.setInventoryTransaction(inventoryTransaction);

        InventoryTransaction result =
            tools.performInforOperation(context, inforws::syncInventoryTransactionOp, syncInventoryTransaction)
                .getResultData().getInventoryTransaction();

        return tools.getInforFieldTools().transformInforObject(new PhysicalInventory(), result);
    }

    private ResultData getInventoryResultData(InforContext context, String code) throws InforException {
        MP1220_GetInventoryTransaction_001 getInventoryTransaction =
            new MP1220_GetInventoryTransaction_001();
        getInventoryTransaction.setTRANSACTIONID(new TRANSACTIONID_Type());
        getInventoryTransaction.getTRANSACTIONID().setTRANSACTIONCODE(code);
        getInventoryTransaction.getTRANSACTIONID().setORGANIZATIONID(tools.getOrganization(context));

        return tools.performInforOperation(context, inforws::getInventoryTransactionOp, getInventoryTransaction)
            .getResultData();
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

    @Override
    public PhysicalInventory readDefaultPhysicalInventory(InforContext context, String storeCode) throws InforException {
        MP1219_GetInventoryTransactionDefault_001 getInventoryTransactionDefault =
            new MP1219_GetInventoryTransactionDefault_001();

        getInventoryTransactionDefault.setSTOREID(new STOREID_Type());
        getInventoryTransactionDefault.getSTOREID().setSTORECODE(storeCode);
        getInventoryTransactionDefault.getSTOREID().setORGANIZATIONID(tools.getOrganization(context));


        InventoryTransactionDefault inventoryTransactionDefault =
            tools.performInforOperation(context, inforws::getInventoryTransactionDefaultOp, getInventoryTransactionDefault)
                .getResultData().getInventoryTransactionDefault();

        return tools.getInforFieldTools().transformInforObject(new PhysicalInventory(), inventoryTransactionDefault);
    }

    private PhysicalInventoryLine getLine(InforContext context, String code, BigInteger lineNumber)
            throws InforException {
        TRANSACTIONLINEID transactionLineId = new TRANSACTIONLINEID();
        transactionLineId.setTRANSACTIONID(new TRANSACTIONID_Type());
        transactionLineId.getTRANSACTIONID().setTRANSACTIONCODE(code);
        transactionLineId.getTRANSACTIONID().setORGANIZATIONID(tools.getOrganization(context));

        transactionLineId.setTRANSACTIONLINENUM(tools.getDataTypeTools().convertBigIntegerToLong(lineNumber));

        MP2244_GetPhysicalInventoryLine_001 getPhysicalInventoryLine =
                new MP2244_GetPhysicalInventoryLine_001();
        getPhysicalInventoryLine.setTRANSACTIONLINEID(transactionLineId);

        return tools.performInforOperation(context, inforws::getPhysicalInventoryLineOp, getPhysicalInventoryLine)
            .getResultData().getPhysicalInventoryLine();
    }
}
