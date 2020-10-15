package ch.cern.eam.wshub.core.services.comments.impl;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.comments.CommentService;
import ch.cern.eam.wshub.core.services.comments.entities.Comment;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.encodeBoolean;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0108_001.CommentsReq;
import net.datastream.schemas.mp_functions.mp0108_001.MP0108_GetComments_001;
import net.datastream.schemas.mp_functions.mp0109_001.MP0109_AddComments_001;
import net.datastream.schemas.mp_functions.mp0110_001.MP0110_SyncComments_001;
import net.datastream.schemas.mp_functions.mp0111_001.MP0111_DeleteComments_001;
import net.datastream.schemas.mp_results.mp0108_001.MP0108_GetComments_001_Result;
import net.datastream.schemas.mp_results.mp0109_001.MP0109_AddComments_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;


public class CommentServiceImpl implements CommentService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public CommentServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public String createComment(InforContext context, Comment comment) throws InforException {
		//
		// VALIDATION
		//
		if (comment == null) {
			throw tools.generateFault("Comment can not be empty");
		}

		if (comment.getText() == null || comment.getEntityCode() == null || comment.getEntityKeyCode() == null) {
			throw tools.generateFault("Please supply entity code, entity key code and comment text.");
		}

		if (comment.getEntityKeyCode().endsWith("#*")) {
			throw tools.generateFault("Entity key code can't end with '#*'");
		}

		//
		// CREATION
		//
		COMMENT_Type commentInfor = new COMMENT_Type();

		if (comment.getEntityCode() != null) {
			commentInfor.setENTITYCOMMENTID(new ENTITYCOMMENTID_Type());
			commentInfor.getENTITYCOMMENTID().setENTITY(comment.getEntityCode());
			commentInfor.getENTITYCOMMENTID().setENTITYKEYCODE(complementEntityKeyCode(comment.getEntityCode(), comment.getEntityKeyCode()));
			commentInfor.getENTITYCOMMENTID().setLANGUAGEID(new LANGUAGEID_Type());
			commentInfor.getENTITYCOMMENTID().getLANGUAGEID().setLANGUAGECODE("EN");
			if (comment.getTypeCode() != null) {
				commentInfor.getENTITYCOMMENTID().setCOMMENTTYPE(new TYPE_Type());
				commentInfor.getENTITYCOMMENTID().getCOMMENTTYPE().setTYPECODE(comment.getTypeCode());
			} else {
				commentInfor.getENTITYCOMMENTID().setCOMMENTTYPE(new TYPE_Type());
				commentInfor.getENTITYCOMMENTID().getCOMMENTTYPE().setTYPECODE("*");
			}
			//
			if (comment.getLineNumber() != null) {
				commentInfor.getENTITYCOMMENTID().setLINENUM(tools.getDataTypeTools().encodeLong(comment.getLineNumber(), "Line Number"));
			}
		}

		//
		if (comment.getText()!= null && !comment.getText().trim().equals("")) {
			commentInfor.setCOMMENTTEXT(comment.getText());
		}

		//
		if (comment.getCreationUserCode() != null) {
			commentInfor.setCREATEDBY(new USERID_Type());
			commentInfor.getCREATEDBY().setUSERCODE(comment.getCreationUserCode());
		}

		//
		commentInfor.setORGANIZATIONID(tools.getOrganization(context));

		if (comment.getPrint() == null) {
			commentInfor.setPRINT("true");
		} else {
			commentInfor.setPRINT(encodeBoolean(comment.getPrint(), BooleanType.TRUE_FALSE));
		}

		MP0109_AddComments_001 addComments = new MP0109_AddComments_001();
		addComments.setCOMMENT(commentInfor);

		MP0109_AddComments_001_Result result = tools.performInforOperation(context, inforws::addCommentsOp, addComments);
		long lineNumber = result.getResultData().getENTITYCOMMENTID().getLINENUM();

		comment.setLineNumber(String.valueOf(lineNumber));

		return comment.getPk();
	}

	public Comment[] readComments(InforContext context, String entityCode, String entityKeyCode, String typeCode) throws InforException {

		if (entityCode == null || entityCode.trim().equals("")) {
			throw tools.generateFault("Entity Code is required.");
		}

		if (entityKeyCode == null || entityKeyCode.trim().equals("")) {
			throw tools.generateFault("Entity Key Code is required.");
		}

		if (entityKeyCode.endsWith("#*")) {
			throw tools.generateFault("Entity key code can't end with '#*'");
		}

		if (entityCode.trim().toUpperCase().equals("EVNT") || (typeCode == null) || (typeCode.trim().equals("")))
		{
			Comment[] normal = readCommentsForType(context, entityCode, entityKeyCode, "*");
			Comment[] closing = readCommentsForType(context, entityCode, entityKeyCode, "+");
			Comment[] concat = new Comment[normal.length + closing.length];
			System.arraycopy(normal, 0, concat, 0, normal.length);
			System.arraycopy(closing, 0, concat, normal.length, closing.length);
			return concat;
		}
		return readCommentsForType(context, entityCode, entityKeyCode, typeCode);
	}

	private Comment[] readCommentsForType(InforContext context, String entityCode, String entityKeyCode, String typeCode) throws InforException {

		CommentsReq commentsReq = new CommentsReq();
		//
		commentsReq.setENTITY(entityCode);
		commentsReq.setENTITYKEYCODE(complementEntityKeyCode(entityCode, entityKeyCode));
		commentsReq.setCOMMENTTYPE(new TYPE_Type());
		commentsReq.getCOMMENTTYPE().setTYPECODE(typeCode);
		//
		MP0108_GetComments_001 getComments = new MP0108_GetComments_001();
		getComments.setCommentsReq(commentsReq);

		MP0108_GetComments_001_Result result =
			tools.performInforOperation(context, inforws::getCommentsOp, getComments);

		Comment[] commentsArray = new Comment[result.getResultData().getComments().getCOMMENT().size()];
		int counter = 0;

		for (COMMENT_Type commentInfor : result.getResultData().getComments().getCOMMENT()) {
			commentsArray[counter] = new Comment();
			//
			if (commentInfor.getCREATEDDATE() != null) {
				commentsArray[counter].setCreationDate(tools.getDataTypeTools().retrieveDate(commentInfor.getCREATEDDATE()));
			}

			//
			if (commentInfor.getUPDATEDDATE() != null) {
				commentsArray[counter].setUpdateDate(tools.getDataTypeTools().retrieveDate(commentInfor.getUPDATEDDATE()));
			}

			//
			commentsArray[counter].setText(commentInfor.getCOMMENTTEXT());

			//
			if (commentInfor.getCREATEDBY() != null) {
				commentsArray[counter].setCreationUserCode(commentInfor.getCREATEDBY().getUSERCODE());
				commentsArray[counter].setCreationUserDesc(commentInfor.getCREATEDBY().getDESCRIPTION());
			}

			//
			if (commentInfor.getUPDATEDBY() != null) {
				commentsArray[counter].setUpdateUserCode(commentInfor.getUPDATEDBY().getUSERCODE());
				commentsArray[counter].setUpdateUserDesc(commentInfor.getUPDATEDBY().getDESCRIPTION());
			}

			//
			if (commentInfor.getENTITYCOMMENTID() != null) {
				commentsArray[counter].setEntityCode(commentInfor.getENTITYCOMMENTID().getENTITY());
				commentsArray[counter].setEntityKeyCode(entityKeyCode);
				commentsArray[counter].setLineNumber(commentInfor.getENTITYCOMMENTID().getLINENUM().toString());
				if (commentInfor.getENTITYCOMMENTID().getCOMMENTTYPE() != null) {
					commentsArray[counter].setTypeCode(commentInfor.getENTITYCOMMENTID().getCOMMENTTYPE().getTYPECODE());
				}
			}

			//
			commentsArray[counter].setUpdateCount(commentInfor.getRecordid().toString());

			counter++;
		}
		return commentsArray;
	}

	public String updateComment(InforContext context, Comment commentParam) throws InforException {

		if (commentParam.getEntityCode() == null || commentParam.getEntityCode().trim().equals("")) {
			throw tools.generateFault("Entity Code is required.");
		}

		if (commentParam.getEntityKeyCode() == null || commentParam.getEntityKeyCode().trim().equals("")) {
			throw tools.generateFault("Entity Key Code is required.");
		}

		if (commentParam.getEntityKeyCode().endsWith("#*")) {
			throw tools.generateFault("Entity key code can't end with '#*'");
		}

		// FETCH UPDATE COUNT IF NOT PROVIDED
		if (commentParam.getUpdateCount() == null ||
				commentParam.getUpdateCount().trim().equals("") ||
				commentParam.getTypeCode() == null ||
				commentParam.getTypeCode().trim().equals("")) {
			Comment[] existingComments = readComments(context, commentParam.getEntityCode(), commentParam.getEntityKeyCode(), commentParam.getTypeCode());
			for (Comment comment : existingComments) {
				if (comment.getLineNumber().equals(commentParam.getLineNumber())) {
					commentParam.setUpdateCount(comment.getUpdateCount());
					commentParam.setTypeCode(comment.getTypeCode());
				}
			}
		}

		COMMENT_Type commentInfor = new COMMENT_Type();
		//
		//
		//
		if (commentParam.getEntityCode() != null) {
			commentInfor.setENTITYCOMMENTID(new ENTITYCOMMENTID_Type());
			commentInfor.getENTITYCOMMENTID().setENTITY(commentParam.getEntityCode());
			commentInfor.getENTITYCOMMENTID().setENTITYKEYCODE(complementEntityKeyCode(commentParam.getEntityCode(), commentParam.getEntityKeyCode()));
			commentInfor.getENTITYCOMMENTID().setLANGUAGEID(new LANGUAGEID_Type());
			commentInfor.getENTITYCOMMENTID().getLANGUAGEID().setLANGUAGECODE("EN");
			commentInfor.getENTITYCOMMENTID().setLINENUM(tools.getDataTypeTools().encodeLong(commentParam.getLineNumber(), "Line Number"));
			if (commentParam.getTypeCode() != null) {
				commentInfor.getENTITYCOMMENTID().setCOMMENTTYPE(new TYPE_Type());
				commentInfor.getENTITYCOMMENTID().getCOMMENTTYPE().setTYPECODE(commentParam.getTypeCode());
			}
		}

		//
		if (commentParam.getText()!= null && !commentParam.getText().trim().equals("")) {
			commentInfor.setCOMMENTTEXT(commentParam.getText());
		}

		//
		if (commentParam.getUpdateCount() != null) {
			commentInfor.setRecordid(tools.getDataTypeTools().encodeLong(commentParam.getUpdateCount(), "Update Count"));
		}
		//
		commentInfor.setORGANIZATIONID(tools.getOrganization(context));

		if (commentParam.getPrint() == null) {
			commentInfor.setPRINT("true");
		} else {
			commentInfor.setPRINT(encodeBoolean(commentParam.getPrint(), BooleanType.TRUE_FALSE));
		}

		MP0110_SyncComments_001 syncComments = new MP0110_SyncComments_001();
		syncComments.setCOMMENT(commentInfor);

		tools.performInforOperation(context, inforws::syncCommentsOp, syncComments);
		return commentParam.getPk();
	}

	public String deleteComment(InforContext context, Comment comment) throws InforException {
		MP0111_DeleteComments_001 deleteComments = new MP0111_DeleteComments_001();
		deleteComments.setENTITYCOMMENTID(new ENTITYCOMMENTID_Type());

		deleteComments.getENTITYCOMMENTID().setENTITY(comment.getEntityCode());
		deleteComments.getENTITYCOMMENTID().setENTITYKEYCODE(complementEntityKeyCode(comment.getEntityCode(), comment.getEntityKeyCode()));
		deleteComments.getENTITYCOMMENTID().setLANGUAGEID(new LANGUAGEID_Type());
		deleteComments.getENTITYCOMMENTID().getLANGUAGEID().setLANGUAGECODE("EN");
		deleteComments.getENTITYCOMMENTID().setCOMMENTTYPE(new TYPE_Type());
		deleteComments.getENTITYCOMMENTID().setLINENUM(tools.getDataTypeTools().encodeLong(comment.getLineNumber(), "Line Number"));

		if (comment.getTypeCode() != null) {
			deleteComments.getENTITYCOMMENTID().getCOMMENTTYPE().setTYPECODE(comment.getTypeCode());
		} else {
			deleteComments.getENTITYCOMMENTID().getCOMMENTTYPE().setTYPECODE("*");
		}

		tools.performInforOperation(context, inforws::deleteCommentsOp, deleteComments);

		return comment.getPk();
	}

	private static String complementEntityKeyCode(String entityCode, String entityKeyCode) {
		if ("OBJ".equals(entityCode) || "PART".equals(entityCode)) {
			return entityKeyCode + "#*";
		}
		return entityKeyCode;
	}

}

