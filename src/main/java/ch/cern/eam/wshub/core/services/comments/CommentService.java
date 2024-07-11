package ch.cern.eam.wshub.core.services.comments;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.comments.entities.Comment;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface CommentService {

    @Operation(logOperation = EAM_OPERATION.COMMENT_C, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "entityKeyCode", logDataReference2 = LogDataReferenceType.RESULT)
    String createComment(EAMContext context, Comment comment) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.COMMENT_R)
    Comment[] readComments(EAMContext context, String entityCode, String entityKeyCode, String typeCode) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.COMMENT_U, logDataReference1 = LogDataReferenceType.INPUTFIELD, logDataReference1FieldName = "entityKeyCode", logDataReference2 = LogDataReferenceType.RESULT)
    String updateComment(EAMContext context, Comment comment) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.COMMENT_D)
    String deleteComment(EAMContext context, Comment comment) throws EAMException;
}
