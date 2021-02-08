package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.GridField;
import ch.cern.eam.wshub.core.services.entities.Pair;

import java.util.List;

public class WorkOrderActivityCheckListDefinition {
    @GridField(name="checklistitem")
    private String code;

    private List<Pair> notApplicableOptions;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Pair> getNotApplicableOptions() {
        return notApplicableOptions;
    }

    public void setNotApplicableOptions(List<Pair> notApplicableOptions) {
        this.notApplicableOptions = notApplicableOptions;
    }

    @Override
    public String toString() {
        return "WorkOrderActivityCheckList{" +
                "code='" + code + '\'' +
                "notApplicableOptions='" + notApplicableOptions + '\'' +
                '}';
    }
}
