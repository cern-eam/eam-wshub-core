package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.EAMField;

import java.math.BigInteger;

public class StandardWorkOrderChild {

    @EAMField(xpath = "PARENTSTANDARDWO/STDWOCODE")
    private String parentStandardWOCode;
    @EAMField(xpath = "CHILDSTANDARDWO/STDWOCODE")
    private String childStandardWOCode;
    @EAMField(xpath = "SEQUENCE")
    private BigInteger sequence;
    @EAMField(xpath = "STEP")
    private BigInteger step;

    @EAMField(xpath = "OLDSEQUENCE")
    private BigInteger oldSequence;
    @EAMField(xpath = "OLDSTEP")
    private BigInteger oldStep;

    public String getParentStandardWOCode() {
        return parentStandardWOCode;
    }

    public void setParentStandardWOCode(String parentStandardWOCode) {
        this.parentStandardWOCode = parentStandardWOCode;
    }

    public String getChildStandardWOCode() {
        return childStandardWOCode;
    }

    public void setChildStandardWOCode(String childStandardWOCode) {
        this.childStandardWOCode = childStandardWOCode;
    }

    public BigInteger getSequence() {
        return sequence;
    }

    public void setSequence(BigInteger sequence) {
        this.sequence = sequence;
    }

    public BigInteger getStep() {
        return step;
    }

    public void setStep(BigInteger step) {
        this.step = step;
    }

    public BigInteger getOldSequence() {
        return oldSequence;
    }

    public void setOldSequence(BigInteger oldSequence) {
        this.oldSequence = oldSequence;
    }

    public BigInteger getOldStep() {
        return oldStep;
    }

    public void setOldStep(BigInteger oldStep) {
        this.oldStep = oldStep;
    }

    @Override
    public String toString() {
        return "StandardWorkOrderChild{" +
                "parentStandardWOCode='" + parentStandardWOCode + '\'' +
                ", childStandardWOCode='" + childStandardWOCode + '\'' +
                ", sequence=" + sequence +
                ", step=" + step +
                ", oldSequence=" + oldSequence +
                ", oldStep=" + oldStep +
                '}';
    }
}
