package ch.cern.eam.wshub.core.services.material;

import java.util.List;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.material.entities.IssueReturnPartTransaction;
import ch.cern.eam.wshub.core.services.material.entities.*;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface PartMiscService {

    @Operation(logOperation = EAM_OPERATION.PARTSUPP_C)
    String addPartSupplier(EAMContext context, PartSupplier partSupplierParam) throws EAMException;

    List<String> createIssueReturnTransaction(EAMContext context, List<IssueReturnPartTransaction> issueReturnPartTransactionList) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.ISSUE_RET, logDataReference1 = LogDataReferenceType.RESULT, logDataReference2 = LogDataReferenceType.INPUTFIELD, logDataReference2FieldName = "storeCode")
    String createIssueReturnTransaction(EAMContext context, IssueReturnPartTransaction issueReturnPartTransaction) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTASSOC_C)
    String createPartAssociation(EAMContext context, PartAssociation partAssociation) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTASSOC_D)
    String deletePartAssociation(EAMContext context, PartAssociation partAssociation) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTSUBS_C)
    String createPartSubstitute(EAMContext context, PartSubstitute partSubstitute) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.STOREBIN_C)
    String addStoreBin(EAMContext context, Bin binParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.STOREBIN_R)
    Bin readStoreBin(EAMContext context, Bin binParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.STOREBIN_U)
    String updateStoreBin(EAMContext context, Bin binParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.STOREBIN_D)
    String deleteStoreBin(EAMContext context, Bin binParam) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTMAN_R)
    public PartManufacturer[] getPartManufacturers(EAMContext context, String partCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTBIN2BIN_C)
    public String createBin2binTransfer(EAMContext context, Bin2BinTransfer bin2BinTransfer) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.PARTBIN2BIN_BC)
    public BatchResponse<String> createBin2binTransferBatch(EAMContext context, List<Bin2BinTransfer> bin2BinTransferList) throws EAMException;
}
