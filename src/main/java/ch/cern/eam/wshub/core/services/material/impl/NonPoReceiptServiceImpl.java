package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.material.NonPoReceiptPartService;
import ch.cern.eam.wshub.core.services.material.NonPoReceiptService;
import ch.cern.eam.wshub.core.services.material.entities.NoPoReceipt;
import ch.cern.eam.wshub.core.services.material.entities.NoPoReceiptPart;
import ch.cern.eam.wshub.core.services.material.entities.TransactionLineId;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.nonconformityobservation_001.NonconformityObservation;
import net.datastream.schemas.mp_entities.nonporeceipt_001.NonPOReceipt;
import net.datastream.schemas.mp_entities.nonporeceiptpart_001.NonPOReceiptPart;
import net.datastream.schemas.mp_fields.NONCONFORMITYOBSERVATIONID_Type;
import net.datastream.schemas.mp_fields.TRANSACTIONID_Type;
import net.datastream.schemas.mp_fields.TRANSACTIONLINEID;
import net.datastream.schemas.mp_functions.mp1243_001.MP1243_AddNonPOReceipt_001;
import net.datastream.schemas.mp_functions.mp1244_001.MP1244_SyncNonPOReceipt_001;
import net.datastream.schemas.mp_functions.mp1245_001.MP1245_DeleteNonPOReceipt_001;
import net.datastream.schemas.mp_functions.mp1247_001.MP1247_GetNonPOReceipt_001;
import net.datastream.schemas.mp_functions.mp2014_001.MP2014_AddNonPOReceiptPart_001;
import net.datastream.schemas.mp_functions.mp2015_001.MP2015_SyncNonPOReceiptPart_001;
import net.datastream.schemas.mp_functions.mp2016_001.MP2016_DeleteNonPOReceiptPart_001;
import net.datastream.schemas.mp_functions.mp2017_001.MP2017_GetNonPOReceiptPart_001;
import net.datastream.schemas.mp_results.mp1243_001.MP1243_AddNonPOReceipt_001_Result;
import net.datastream.schemas.mp_results.mp1244_001.MP1244_SyncNonPOReceipt_001_Result;
import net.datastream.schemas.mp_results.mp1245_001.MP1245_DeleteNonPOReceipt_001_Result;
import net.datastream.schemas.mp_results.mp1247_001.MP1247_GetNonPOReceipt_001_Result;
import net.datastream.schemas.mp_results.mp2015_001.MP2015_SyncNonPOReceiptPart_001_Result;
import net.datastream.schemas.mp_results.mp2016_001.MP2016_DeleteNonPOReceiptPart_001_Result;
import net.datastream.schemas.mp_results.mp2017_001.MP2017_GetNonPOReceiptPart_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import org.openapplications.oagis_segments.DATETIME;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NonPoReceiptServiceImpl implements NonPoReceiptService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;
    private NonPoReceiptPartService nonPoReceiptPartService;

    public NonPoReceiptServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        this.nonPoReceiptPartService = new NonPoReceiptPartServiceImpl(applicationData, tools, inforWebServicesToolkitClient);

    }

    @Override
    public String createNoPoReceipt(InforContext context, NoPoReceipt receipt) throws InforException {

        NonPOReceipt transactionInfor = new NonPOReceipt();
        tools.getInforFieldTools().transformWSHubObject(transactionInfor, receipt, context);
        MP1243_AddNonPOReceipt_001 addtransaction = new MP1243_AddNonPOReceipt_001();
        transactionInfor.setTRANSACTIONID(createDefaultTransactionType(context, receipt));
        addtransaction.setNonPOReceipt(transactionInfor);

        MP1243_AddNonPOReceipt_001_Result addNonPoReceipt = tools.performInforOperation(context, inforws::addNonPOReceiptOp, addtransaction);
        String transactionCode = addNonPoReceipt.getResultData().getTRANSACTIONID().getTRANSACTIONCODE();
        if(receipt.getParts() != null && !receipt.getParts().isEmpty()) {
            receipt.getParts().stream()
                    .peek(part -> part.setTransactionCode(transactionCode))
                    .collect(Collectors.toList());
            nonPoReceiptPartService.createNoPoReceiptPartBatch(context, receipt.getParts());
        }

        return transactionCode;
    }

    @Override
    public NoPoReceipt readNoPoReceipt(InforContext context, String receiptCode) throws InforException {
        NonPOReceipt transaction = readNonPoReceiptInfor(context, receiptCode);
        NoPoReceipt result = tools.getInforFieldTools().transformInforObject(new NoPoReceipt(), transaction, context);
        List<NoPoReceiptPart> parts = nonPoReceiptPartService.getNoPoReceiptParts(context, receiptCode);
        result.setParts(parts != null ? parts : Collections.emptyList());
        return result;
    }

    @Override
    public String updateNoPoReceipt(InforContext context, NoPoReceipt receipt) throws InforException {

        MP1244_SyncNonPOReceipt_001 syncTransaction = new MP1244_SyncNonPOReceipt_001();

        NonPOReceipt prev = readNonPoReceiptInfor(context, receipt.getCode());
        List<NoPoReceiptPart> oldParts = nonPoReceiptPartService.getNoPoReceiptParts(context, receipt.getCode());
        tools.getInforFieldTools().transformWSHubObject(prev, receipt, context);
        syncTransaction.setNonPOReceipt(prev);

        MP1244_SyncNonPOReceipt_001_Result result = tools.performInforOperation(context, inforws::syncNonPOReceiptOp, syncTransaction);

        List<NoPoReceiptPart> toCreate = new ArrayList<>();
        List<NoPoReceiptPart> toUpdate = new ArrayList<>();
        List<TransactionLineId> toDeleteIds;
        if (receipt.getParts() != null && !receipt.getParts().isEmpty()) {
            for (NoPoReceiptPart newPart : receipt.getParts()) {
                if(newPart.getTransactionLineId() == null) {
                    toCreate.add(newPart);
                }
                else {
                    toUpdate.add(newPart);
                }
            }
        }
        
        toDeleteIds = oldParts.stream()
                .filter(oldPart -> receipt.getParts().stream()
                        .noneMatch(newPart ->
                                Objects.equals(newPart.getTransactionLineId(), oldPart.getTransactionLineId())))
                .map(oldPart -> new TransactionLineId(
                        oldPart.getTransactionCode(),
                        oldPart.getTransactionLineId()))
                .collect(Collectors.toList());


        if (!toDeleteIds.isEmpty()) {
            nonPoReceiptPartService.deleteNoPoReceiptPartBatch(context,toDeleteIds);
        }

        if (!toUpdate.isEmpty()) {
            nonPoReceiptPartService.updateNoPoReceiptPartBatch(context, toUpdate);
        }

        if (!toCreate.isEmpty()) {
            nonPoReceiptPartService.createNoPoReceiptPartBatch(context, toCreate);
        }
        nonPoReceiptPartService.updateNoPoReceiptPartBatch(context, receipt.getParts());
        return result.getResultData().getTRANSACTIONID().getTRANSACTIONCODE();
    }

    @Override
    public String deleteNoPoReceipt(InforContext context, String receiptCode) throws InforException {
        MP1245_DeleteNonPOReceipt_001 deleteTransaction = new MP1245_DeleteNonPOReceipt_001();
        TRANSACTIONID_Type transactionIdType = new TRANSACTIONID_Type();
        transactionIdType.setTRANSACTIONCODE(receiptCode);
        transactionIdType.setORGANIZATIONID(tools.getOrganization(context));

        deleteTransaction.setTRANSACTIONID(transactionIdType);
        NoPoReceipt prev = readNoPoReceipt(context, receiptCode);
        List<TransactionLineId> ids = new ArrayList<>();
        if (prev.getParts() != null) {
            for (NoPoReceiptPart part : prev.getParts()) {
                ids.add(new TransactionLineId(part.getTransactionCode(), part.getTransactionLineId()));
            }
            if (!ids.isEmpty()) {
                nonPoReceiptPartService.deleteNoPoReceiptPartBatch(context, ids);
            }
        }
        nonPoReceiptPartService.deleteNoPoReceiptPartBatch(context, ids);
        MP1245_DeleteNonPOReceipt_001_Result result = tools.performInforOperation(context, inforws::deleteNonPOReceiptOp, deleteTransaction);
        return result.getResultData().getTRANSACTIONID().getTRANSACTIONCODE();
    }

    private NonPOReceipt readNonPoReceiptInfor(
            InforContext context, String transactionCode) throws InforException {
        MP1247_GetNonPOReceipt_001 getTransaction = new MP1247_GetNonPOReceipt_001();
        TRANSACTIONID_Type transactionIdType = new TRANSACTIONID_Type();
        transactionIdType.setTRANSACTIONCODE(transactionCode);
        transactionIdType.setORGANIZATIONID(tools.getOrganization(context));

        getTransaction.setTRANSACTIONID(transactionIdType);

        MP1247_GetNonPOReceipt_001_Result result =
                tools.performInforOperation(context, inforws::getNonPOReceiptOp, getTransaction);

        return result.getResultData().getNonPOReceipt();
    }

    private TRANSACTIONID_Type createDefaultTransactionType(InforContext context, NoPoReceipt receipt) throws InforException {
        TRANSACTIONID_Type idType = new TRANSACTIONID_Type();
        idType.setTRANSACTIONCODE("@[EMPTY]#*");
        idType.setDESCRIPTION(receipt.getDescription());
        idType.setORGANIZATIONID(tools.getOrganization(context));
        return idType;
    }
}
