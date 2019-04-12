package ch.cern.eam.wshub.core.services.material.entities;

import java.io.Serializable;

public class PickTicketPart implements Serializable {
    String partCode;
    String pickTicket;
    Long quantity;

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getPickTicket() {
        return pickTicket;
    }

    public void setPickTicket(String pickTicket) {
        this.pickTicket = pickTicket;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
