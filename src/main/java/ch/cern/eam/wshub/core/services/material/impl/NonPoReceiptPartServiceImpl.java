package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.contractmanagement.entities.EquipmentReservationAdjustment;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestFilter;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.material.NonPoReceiptPartService;
import ch.cern.eam.wshub.core.services.material.entities.NoPoReceiptPart;
import ch.cern.eam.wshub.core.services.material.entities.TransactionLineId;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.GridTools;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.nonporeceiptpart_001.NonPOReceiptPart;
import net.datastream.schemas.mp_fields.TRANSACTIONID_Type;
import net.datastream.schemas.mp_fields.TRANSACTIONLINEID;
import net.datastream.schemas.mp_functions.mp2014_001.MP2014_AddNonPOReceiptPart_001;
import net.datastream.schemas.mp_functions.mp2015_001.MP2015_SyncNonPOReceiptPart_001;
import net.datastream.schemas.mp_functions.mp2016_001.MP2016_DeleteNonPOReceiptPart_001;
import net.datastream.schemas.mp_functions.mp2017_001.MP2017_GetNonPOReceiptPart_001;
import net.datastream.schemas.mp_results.mp2015_001.MP2015_SyncNonPOReceiptPart_001_Result;
import net.datastream.schemas.mp_results.mp2016_001.MP2016_DeleteNonPOReceiptPart_001_Result;
import net.datastream.schemas.mp_results.mp2017_001.MP2017_GetNonPOReceiptPart_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NonPoReceiptPartServiceImpl implements NonPoReceiptPartService {
    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;
    private GridsService gridsService;

    public NonPoReceiptPartServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);

    }

    @Override
    public Long createNoPoReceiptPart(InforContext context, NoPoReceiptPart transactionLine) throws InforException {
        NonPOReceiptPart transactionLineInfor = new NonPOReceiptPart();
        tools.getInforFieldTools().transformWSHubObject(transactionLineInfor, transactionLine, context);
        MP2014_AddNonPOReceiptPart_001 addtransactionLine = new MP2014_AddNonPOReceiptPart_001();
        addtransactionLine.setNonPOReceiptPart(transactionLineInfor);
        tools.performInforOperation(context, inforws::addNonPOReceiptPartOp, addtransactionLine);
        return addtransactionLine.getNonPOReceiptPart().getTRANSACTIONLINEID().getTRANSACTIONLINENUM();

    }

    @Override
    public NoPoReceiptPart readNoPoReceiptPart(InforContext context, BigInteger transactionLineId, String transactionCode) throws InforException {
        return readNoPoReceiptPart(context, new TransactionLineId(transactionCode, transactionLineId)) ;
    }

    private NoPoReceiptPart readNoPoReceiptPart(InforContext context, TransactionLineId transactionLineId) throws InforException {
        NonPOReceiptPart transactionLine = readNonPoReceiptPartInfor(context, transactionLineId);

        return tools.getInforFieldTools().transformInforObject(new NoPoReceiptPart(), transactionLine, context);
    }

    @Override
    public Long updateNoPoReceiptPart(InforContext context, NoPoReceiptPart transactionLine) throws InforException {
        MP2015_SyncNonPOReceiptPart_001 syncTransactionLine = new MP2015_SyncNonPOReceiptPart_001();

        NonPOReceiptPart prev = readNonPoReceiptPartInfor(context, new TransactionLineId(transactionLine.getTransactionCode(), transactionLine.getTransactionLineId()));
        tools.getInforFieldTools().transformWSHubObject(prev, transactionLine, context);
        syncTransactionLine.setNonPOReceiptPart(prev);

        MP2015_SyncNonPOReceiptPart_001_Result result = tools.performInforOperation(context, inforws::syncNonPOReceiptPartOp, syncTransactionLine);
        return result.getResultData().getTRANSACTIONLINEID().getTRANSACTIONLINENUM();

    }

    @Override
    public Long deleteNoPoReceiptPart(InforContext context, BigInteger transactionLineId, String transactionCode) throws InforException {
        return deleteNoPoReceiptPart(context, new TransactionLineId(transactionCode, transactionLineId));
    }

    @Override
    public List<NoPoReceiptPart> getNoPoReceiptParts(InforContext context, String transactionCode) throws InforException {
        GridRequest gridRequest = new GridRequest("SSCOMP_PAR", GridRequest.GRIDTYPE.LIST);
        gridRequest.setUserFunctionName("SSCOMP");

        gridRequest.addParam("param.nonporeceiptcode", transactionCode);

        GridRequestResult gridRequestResult = gridsService.executeQuery(context, gridRequest);
        return GridTools.convertGridResultToObject(NoPoReceiptPart.class, null, gridRequestResult);

    }

    private Long deleteNoPoReceiptPart(InforContext context, TransactionLineId transactionLineId) throws InforException {
        MP2016_DeleteNonPOReceiptPart_001 deleteTransactionLine = new MP2016_DeleteNonPOReceiptPart_001();
        TRANSACTIONLINEID idType = createType(context, transactionLineId);

        deleteTransactionLine.setTRANSACTIONLINEID(idType);
        MP2016_DeleteNonPOReceiptPart_001_Result result = tools.performInforOperation(context, inforws::deleteNonPOReceiptPartOp, deleteTransactionLine);
        return result.getResultData().getTRANSACTIONLINEID().getTRANSACTIONLINENUM();
    }

    @Override
    public BatchResponse<Long> createNoPoReceiptPartBatch(InforContext context, List<NoPoReceiptPart> transactionLines) throws InforException {
        return tools.batchOperation(context, this::createNoPoReceiptPart, transactionLines);

    }

    @Override
    public BatchResponse<NoPoReceiptPart> readNoPoReceiptPartBatch(InforContext context, List<TransactionLineId> transactionLineIds) {
       return tools.batchOperation(context, this::readNoPoReceiptPart, transactionLineIds);
    }

    @Override
    public BatchResponse<Long> updateNoPoReceiptPartBatch(InforContext context, List<NoPoReceiptPart> transactionLines) throws InforException {
        return tools.batchOperation(context, this::updateNoPoReceiptPart, transactionLines);
    }

    @Override
    public BatchResponse<Long> deleteNoPoReceiptPartBatch(InforContext context, List<TransactionLineId> transactionLineIds) throws InforException {
        return tools.batchOperation(context, this::deleteNoPoReceiptPart, transactionLineIds);
    }

    private NonPOReceiptPart readNonPoReceiptPartInfor(
            InforContext context, TransactionLineId transactionLineId) throws InforException {
        MP2017_GetNonPOReceiptPart_001 getTransactionLine = new MP2017_GetNonPOReceiptPart_001();
        TRANSACTIONLINEID idType = createType(context, transactionLineId);

        getTransactionLine.setTRANSACTIONLINEID(idType);

        MP2017_GetNonPOReceiptPart_001_Result result =
                tools.performInforOperation(context, inforws::getNonPOReceiptPartOp, getTransactionLine);

        return result.getResultData().getNonPOReceiptPart();
    }


    private TRANSACTIONLINEID createType(InforContext context, TransactionLineId transactionLineId) {
        TRANSACTIONLINEID idType = new TRANSACTIONLINEID();
        TRANSACTIONID_Type transactionIdType = new TRANSACTIONID_Type();
        transactionIdType.setTRANSACTIONCODE(transactionLineId.getTransactionCode());
        transactionIdType.setORGANIZATIONID(tools.getOrganization(context));

        idType.setTRANSACTIONID(transactionIdType);
        idType.setTRANSACTIONLINENUM(transactionLineId.getTransactionLineId().longValue());

        return idType;

    }


}



