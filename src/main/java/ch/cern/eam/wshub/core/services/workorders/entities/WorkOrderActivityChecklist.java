package ch.cern.eam.wshub.core.services.workorders.entities;

import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class WorkOrderActivityChecklist implements Serializable {

    @NonNull
    @InforField(xpath = "ACTIVITYID/WORKORDERID/JOBNUM")
    private String workOrderCode;

    @InforField(xpath = "ACTIVITYID/WORKORDERID/ORGANIZATIONID/ORGANIZATIONCODE")
    private String organizationCode = "*";

    @NonNull
    @InforField(xpath = "ACTIVITYID/ACTIVITYCODE/value")
    private long activityCode;

    @InforField(xpath = "JOBSEQUENCE")
    private Long jobSequence;

    @InforField(xpath = "REJECTPERFORMEDBY")
    private Boolean rejectPerformedBy;

    @InforField(xpath = "REJECTPERFORMEDBY2")
    private Boolean rejectPerformedBy2;

    @InforField(xpath = "REJECTIONREASON")
    private String rejectionReason;

    @InforField(xpath = "CONDITIONOPTION/USERDEFINEDCODE")
    private String conditionOptionUserDefinedCode;

    @InforField(xpath = "StandardUserDefinedFields")
    private UserDefinedFields userDefinedFields;
}
