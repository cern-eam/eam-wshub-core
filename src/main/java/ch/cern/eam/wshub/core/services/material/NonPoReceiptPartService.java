package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.material.entities.NoPoReceiptPart;
import ch.cern.eam.wshub.core.services.material.entities.TransactionLineId;
import ch.cern.eam.wshub.core.tools.InforException;

import java.math.BigInteger;
import java.util.List;

public interface NonPoReceiptPartService {
    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPTPART_C)
    Long createNoPoReceiptPart(InforContext context, NoPoReceiptPart transactionLine) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPTPART_R)
    NoPoReceiptPart readNoPoReceiptPart(InforContext context, BigInteger transactionLineId, String transactionCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPTPART_U)
    Long updateNoPoReceiptPart(InforContext context, NoPoReceiptPart transactionLine) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPTPART_D)
    Long deleteNoPoReceiptPart(InforContext context, BigInteger transactionLineId, String transactionCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPTPART_LR)
    List<NoPoReceiptPart> getNoPoReceiptParts(InforContext context, String transactionCode) throws InforException;
    //
    // BATCH
    //
    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPTPART_BC)
    BatchResponse<Long> createNoPoReceiptPartBatch(InforContext context, List<NoPoReceiptPart> transactionLines)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPTPART_BR)
    BatchResponse<NoPoReceiptPart> readNoPoReceiptPartBatch(InforContext context, List<TransactionLineId> transactionLineCodes);

    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPTPART_BU)
    BatchResponse<Long> updateNoPoReceiptPartBatch(InforContext context, List<NoPoReceiptPart> transactionLines)
            throws InforException;

    @Operation(logOperation = INFOR_OPERATION.NOPORECEIPTPART_BD)
    BatchResponse<Long> deleteNoPoReceiptPartBatch(InforContext context, List<TransactionLineId> transactionLineCodes)
            throws InforException;


}
