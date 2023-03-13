package ch.cern.eam.wshub.core.services.administration.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class GenericLov implements Serializable {
    private String hint;
    private boolean exact;
    private Map<String, String> inputParams;
    private LinkedHashMap<String, String> returnFields;
    private String lovName;
    private String rentity;
    private BigInteger rowCount;
}
