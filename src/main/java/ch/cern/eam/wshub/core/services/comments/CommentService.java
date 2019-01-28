package ch.cern.eam.wshub.core.services.comments;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.comments.entities.Comment;
import ch.cern.eam.wshub.core.tools.InforException;

public interface CommentService {

    @Operation(logOperation = INFOR_OPERATION.COMMENT_C, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "entityKeyCode", logDataReference2 = LogDataReferenceType.RESULT)
    String createComment(InforContext context, Comment commentParam) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.COMMENT_R)
    Comment[] readComments(InforContext context, String entityCode, String entityKeyCode, String typeCode) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.COMMENT_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "entityKeyCode", logDataReference2 = LogDataReferenceType.RESULT)
    String updateComment(InforContext context, Comment commentParam) throws InforException;
}
