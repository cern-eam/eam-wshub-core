package ch.cern.eam.wshub.core.services.material.entities;

public class Bin2BinTransfer {

    private String storeCode;
    private String destinationBin;
    private IssueReturnPartTransactionLine transactionLine;

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getDestinationBin() {
        return destinationBin;
    }

    public void setDestinationBin(String destinationBin) {
        this.destinationBin = destinationBin;
    }

    public IssueReturnPartTransactionLine getTransactionLine() {
        return transactionLine;
    }

    public void setTransactionLine(IssueReturnPartTransactionLine transactionLine) {
        this.transactionLine = transactionLine;
    }
}
