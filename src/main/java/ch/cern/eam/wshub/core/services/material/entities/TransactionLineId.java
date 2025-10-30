package ch.cern.eam.wshub.core.services.material.entities;

import ch.cern.eam.wshub.core.annotations.InforField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@Embeddable
public class TransactionLineId implements Serializable {

    @InforField(xpath = "TRANSACTIONLINENUM")
    private BigInteger transactionLineId;

    @InforField(xpath = "TRANSACTIONID/TRANSACTIONCODE")
    private String transactionCode;

    public TransactionLineId(String transactionCode, BigInteger transactionLineId) {
        this.transactionCode = transactionCode;
        this.transactionLineId = transactionLineId;
    }

    public TransactionLineId() {

    }
}
