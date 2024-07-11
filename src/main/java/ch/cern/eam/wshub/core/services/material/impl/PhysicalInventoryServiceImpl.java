package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.material.PhysicalInventoryService;
import ch.cern.eam.wshub.core.services.material.entities.PhysicalInventory;
import ch.cern.eam.wshub.core.services.material.entities.PhysicalInventoryRow;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.math.BigInteger;

public class PhysicalInventoryServiceImpl implements PhysicalInventoryService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public PhysicalInventoryServiceImpl(
            ApplicationData applicationData,
            Tools tools,
            EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
    }

    @Override
    public PhysicalInventory createPhysicalInventory(EAMContext context, PhysicalInventory physicalInventory)
            throws EAMException {
        InventoryTransaction inventoryTransaction = new InventoryTransaction();

        inventoryTransaction.setTRANSACTIONID(new TRANSACTIONID_Type());
        inventoryTransaction.getTRANSACTIONID().setTRANSACTIONCODE("0");

        tools.getEAMFieldTools().transformWSHubObject(inventoryTransaction, physicalInventory, context);

        MP1217_AddInventoryTransaction_001 addInventoryTransaction = new MP1217_AddInventoryTransaction_001();
        addInventoryTransaction.setInventoryTransaction(inventoryTransaction);

        MP1217_AddInventoryTransaction_001_Result result =
            tools.performEAMOperation(context, eamws::addInventoryTransactionOp, addInventoryTransaction);

        return tools.getEAMFieldTools()
            .transformEAMObject(new PhysicalInventory(), result.getResultData().getInventoryTransaction(), context);
    }

    @Override
    public PhysicalInventory readPhysicalInventory(EAMContext context, String code) throws EAMException {
        ResultData resultData = getInventoryResultData(context, code);
        PhysicalInventory physicalInventory = tools.getEAMFieldTools().transformEAMObject(
            new PhysicalInventory(),
            resultData.getInventoryTransaction(), context);

        if (resultData.getCREATEDBY() != null) {
            physicalInventory.setCreatedBy(resultData.getCREATEDBY().getUSERCODE());
        }

        if (resultData.getCREATEDDATE() != null) {
            physicalInventory.setCreatedDate(tools.getDataTypeTools().decodeEAMDate(resultData.getCREATEDDATE()));
        }

        return physicalInventory;
    }

    @Override
    public PhysicalInventory updatePhysicalInventory(EAMContext context, PhysicalInventory physicalInventory)
            throws EAMException {
        InventoryTransaction inventoryTransaction = getInventoryResultData(context, physicalInventory.getCode())
            .getInventoryTransaction();
        tools.getEAMFieldTools().transformWSHubObject(inventoryTransaction, physicalInventory, context);

        MP1218_SyncInventoryTransaction_001 syncInventoryTransaction =
            new MP1218_SyncInventoryTransaction_001();
        syncInventoryTransaction.setInventoryTransaction(inventoryTransaction);

        InventoryTransaction result =
            tools.performEAMOperation(context, eamws::syncInventoryTransactionOp, syncInventoryTransaction)
                .getResultData().getInventoryTransaction();

        return tools.getEAMFieldTools().transformEAMObject(new PhysicalInventory(), result, context);
    }

    private ResultData getInventoryResultData(EAMContext context, String code) throws EAMException {
        MP1220_GetInventoryTransaction_001 getInventoryTransaction =
            new MP1220_GetInventoryTransaction_001();
        getInventoryTransaction.setTRANSACTIONID(new TRANSACTIONID_Type());
        getInventoryTransaction.getTRANSACTIONID().setTRANSACTIONCODE(code);
        getInventoryTransaction.getTRANSACTIONID().setORGANIZATIONID(tools.getOrganization(context));

        return tools.performEAMOperation(context, eamws::getInventoryTransactionOp, getInventoryTransaction)
            .getResultData();
    }

    @Override
    public PhysicalInventoryRow readPhysicalInventoryLine(EAMContext context, PhysicalInventoryRow row) throws EAMException {
        return tools.getEAMFieldTools().transformEAMObject(
            new PhysicalInventoryRow(),
            getLine(context, row.getPhysicalInventoryCode(), row.getLineNumber()), context);
    }

    @Override
    public PhysicalInventoryRow updatePhysicalInventoryLine(EAMContext context, PhysicalInventoryRow row)
            throws EAMException {
        PhysicalInventoryLine physicalInventoryLine = getLine(context, row.getPhysicalInventoryCode(), row.getLineNumber());

        tools.getEAMFieldTools().transformWSHubObject(physicalInventoryLine, row, context);

        physicalInventoryLine.setPHYSICALQUANTITY(
                tools.getDataTypeTools().encodeQuantity(row.getPhysicalQuantity(), "Physical Quantity"));

        MP1294_SyncPhysicalInventoryLine_001 syncPhysicalInventoryLine =
            new MP1294_SyncPhysicalInventoryLine_001();

        syncPhysicalInventoryLine.setPhysicalInventoryLine(physicalInventoryLine);

        PhysicalInventoryLine result = tools.performEAMOperation(context, eamws::syncPhysicalInventoryLineOp, syncPhysicalInventoryLine)
            .getResultData().getPhysicalInventoryLine();

        return tools.getEAMFieldTools().transformEAMObject(new PhysicalInventoryRow(), result, context);
    }

    private PhysicalInventoryLine getLine(EAMContext context, String code, BigInteger lineNumber)
            throws EAMException {
        TRANSACTIONLINEID transactionLineId = new TRANSACTIONLINEID();
        transactionLineId.setTRANSACTIONID(new TRANSACTIONID_Type());
        transactionLineId.getTRANSACTIONID().setTRANSACTIONCODE(code);
        transactionLineId.getTRANSACTIONID().setORGANIZATIONID(tools.getOrganization(context));
        transactionLineId.setTRANSACTIONLINENUM(tools.getDataTypeTools().convertBigIntegerToLong(lineNumber));

        MP2244_GetPhysicalInventoryLine_001 getPhysicalInventoryLine =
                new MP2244_GetPhysicalInventoryLine_001();
        getPhysicalInventoryLine.setTRANSACTIONLINEID(transactionLineId);

        return tools.performEAMOperation(context, eamws::getPhysicalInventoryLineOp, getPhysicalInventoryLine)
            .getResultData().getPhysicalInventoryLine();
    }

    @Override
    public PhysicalInventory readDefaultPhysicalInventory(EAMContext context, String storeCode) throws EAMException {
        MP1219_GetInventoryTransactionDefault_001 getInventoryTransactionDefault =
                new MP1219_GetInventoryTransactionDefault_001();

        getInventoryTransactionDefault.setSTOREID(new STOREID_Type());
        getInventoryTransactionDefault.getSTOREID().setSTORECODE(storeCode);
        getInventoryTransactionDefault.getSTOREID().setORGANIZATIONID(tools.getOrganization(context));


        InventoryTransactionDefault inventoryTransactionDefault =
                tools.performEAMOperation(context, eamws::getInventoryTransactionDefaultOp, getInventoryTransactionDefault)
                        .getResultData().getInventoryTransactionDefault();

        return tools.getEAMFieldTools().transformEAMObject(new PhysicalInventory(), inventoryTransactionDefault, context);
    }
}
