package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;

import java.io.Serializable;
import java.util.List;

public class Store2StoreTransferDTO implements Serializable {
    @EAMField(xpath = "DESCRIPTION")
    private String description;
    @EAMField(xpath = "FROMSTOREID/STORECODE")
    private String fromStoreCode;
    @EAMField(xpath = "TOSTOREID/STORECODE")
    private String toStoreCode;
    @EAMField(xpath = "ADVICENUMBER")
    private String adviceNumber;
    private List<StoreTransactionPartLine> partLines;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getFromStoreCode() {
        return fromStoreCode;
    }

    public void setFromStoreCode(final String fromStoreCode) {
        this.fromStoreCode = fromStoreCode;
    }

    public String getToStoreCode() {
        return toStoreCode;
    }

    public void setToStoreCode(final String toStoreCode) {
        this.toStoreCode = toStoreCode;
    }

    public String getAdviceNumber() {
        return adviceNumber;
    }

    public void setAdviceNumber(final String adviceNumber) {
        this.adviceNumber = adviceNumber;
    }

    public List<StoreTransactionPartLine> getPartLines() {
        return partLines;
    }

    public void setPartLines(final List<StoreTransactionPartLine> partLines) {
        this.partLines = partLines;
    }

    @Override
    public String toString() {
        return "Store2StoreTransferDTO{" +
                "desccription='" + description + '\'' +
                ", fromStoreCode='" + fromStoreCode + '\'' +
                ", toStoreCode='" + toStoreCode + '\'' +
                ", adviceNumber='" + adviceNumber + '\'' +
                ", partLines=" + partLines +
                '}';
    }
}
