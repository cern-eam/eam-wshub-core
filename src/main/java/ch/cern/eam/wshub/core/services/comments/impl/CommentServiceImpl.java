package ch.cern.eam.wshub.core.services.comments.impl;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.comments.CommentService;
import ch.cern.eam.wshub.core.services.comments.entities.Comment;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.encodeBoolean;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;
import static ch.cern.eam.wshub.core.tools.Tools.extractEntityCode;
import static ch.cern.eam.wshub.core.tools.Tools.extractOrganizationCode;

import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0108_001.CommentsReq;
import net.datastream.schemas.mp_functions.mp0108_001.MP0108_GetComments_001;
import net.datastream.schemas.mp_functions.mp0109_001.MP0109_AddComments_001;
import net.datastream.schemas.mp_functions.mp0110_001.MP0110_SyncComments_001;
import net.datastream.schemas.mp_functions.mp0111_001.MP0111_DeleteComments_001;
import net.datastream.schemas.mp_results.mp0109_001.MP0109_AddComments_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.LinkedList;
import java.util.List;


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
			commentInfor.getENTITYCOMMENTID().setENTITYKEYCODE(complementEntityKeyCode(comment.getEntityCode(), comment.getEntityKeyCode(), tools.getOrganizationCode(context, comment.getOrganization())));
			commentInfor.getENTITYCOMMENTID().setLANGUAGEID(new LANGUAGEID_Type());
			commentInfor.getENTITYCOMMENTID().getLANGUAGEID().setLANGUAGECODE("EN");
			commentInfor.getENTITYCOMMENTID().setCOMMENTTYPE(new TYPE_Type());
			if (comment.getTypeCode() != null) {
				commentInfor.getENTITYCOMMENTID().getCOMMENTTYPE().setTYPECODE(comment.getTypeCode());
			} else {
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
		commentInfor.setORGANIZATIONID(tools.getOrganization(context, comment.getOrganization()));

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
		return readCommentsInfor(context, entityCode, extractEntityCode(entityKeyCode), typeCode, tools.getOrganizationCode(context, extractOrganizationCode(entityCode)))
				.stream().map(this::convertToComment)
				.toArray(Comment[]::new);
	}

	public String updateComment(InforContext context, Comment comment) throws InforException {

		if (comment.getEntityCode() == null || comment.getEntityCode().trim().equals("")) {
			throw tools.generateFault("Entity Code is required.");
		}

		if (comment.getEntityKeyCode() == null || comment.getEntityKeyCode().trim().equals("")) {
			throw tools.generateFault("Entity Key Code is required.");
		}

		COMMENT_Type commentInfor = readCommentsInfor(context, comment.getEntityCode(), comment.getEntityKeyCode(), comment.getTypeCode(), comment.getOrganization())
				.stream().filter(commentTemp -> commentTemp.getENTITYCOMMENTID().getLINENUM().toString().equals(comment.getLineNumber())).findFirst().orElse(null);

		if (commentInfor == null) {
			throw tools.generateFault("Comment not found");
		}

		if (comment.getText()!= null && !comment.getText().trim().equals("")) {
			commentInfor.setCOMMENTTEXT(comment.getText());
		}

		if (comment.getPrint() == null) {
			commentInfor.setPRINT("true");
		} else {
			commentInfor.setPRINT(encodeBoolean(comment.getPrint(), BooleanType.TRUE_FALSE));
		}

		MP0110_SyncComments_001 syncComments = new MP0110_SyncComments_001();
		syncComments.setCOMMENT(commentInfor);

		tools.performInforOperation(context, inforws::syncCommentsOp, syncComments);
		return comment.getPk();
	}

	public String deleteComment(InforContext context, Comment comment) throws InforException {
		MP0111_DeleteComments_001 deleteComments = new MP0111_DeleteComments_001();
		deleteComments.setENTITYCOMMENTID(new ENTITYCOMMENTID_Type());

		deleteComments.getENTITYCOMMENTID().setENTITY(comment.getEntityCode());
		deleteComments.getENTITYCOMMENTID().setENTITYKEYCODE(complementEntityKeyCode(comment.getEntityCode(), comment.getEntityKeyCode(), tools.getOrganizationCode(context, comment.getOrganization())));
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

	private List<COMMENT_Type> readCommentsInfor(InforContext context, String entityCode, String entityKeyCode, String typeCode, String organization) throws InforException {

		List<COMMENT_Type> result = new LinkedList<>();

		CommentsReq commentsReq = new CommentsReq();
		commentsReq.setENTITY(entityCode);
		commentsReq.setENTITYKEYCODE(complementEntityKeyCode(entityCode, entityKeyCode, tools.getOrganizationCode(context, organization)));
		commentsReq.setCOMMENTTYPE(new TYPE_Type());

		if (isEmpty(typeCode)) {
			commentsReq.getCOMMENTTYPE().setTYPECODE("*");
		} else {
			commentsReq.getCOMMENTTYPE().setTYPECODE(typeCode);
		}
		//
		MP0108_GetComments_001 getComments = new MP0108_GetComments_001();
		getComments.setCommentsReq(commentsReq);

		result.addAll(tools.performInforOperation(context, inforws::getCommentsOp, getComments).getResultData().getComments().getCOMMENT());

		// Fetch closing comments for Work Orders
		if (isEmpty(typeCode) && entityCode.equals("EVNT")) {
			commentsReq.getCOMMENTTYPE().setTYPECODE("+");
			result.addAll(tools.performInforOperation(context, inforws::getCommentsOp, getComments).getResultData().getComments().getCOMMENT());
		}

		return result;
	}

	private Comment convertToComment(COMMENT_Type commentInfor) {
		Comment comment = new Comment();

		if (commentInfor.getCREATEDDATE() != null) {
			comment.setCreationDate(tools.getDataTypeTools().retrieveDate(commentInfor.getCREATEDDATE()));
		}

		if (commentInfor.getUPDATEDDATE() != null) {
			comment.setUpdateDate(tools.getDataTypeTools().retrieveDate(commentInfor.getUPDATEDDATE()));
		}

		comment.setText(commentInfor.getCOMMENTTEXT());

		if (commentInfor.getCREATEDBY() != null) {
			comment.setCreationUserCode(commentInfor.getCREATEDBY().getUSERCODE());
			comment.setCreationUserDesc(commentInfor.getCREATEDBY().getDESCRIPTION());
		}

		if (commentInfor.getUPDATEDBY() != null) {
			comment.setUpdateUserCode(commentInfor.getUPDATEDBY().getUSERCODE());
			comment.setUpdateUserDesc(commentInfor.getUPDATEDBY().getDESCRIPTION());
		}

		if (commentInfor.getENTITYCOMMENTID() != null) {
			comment.setEntityCode(commentInfor.getENTITYCOMMENTID().getENTITY());
			comment.setEntityKeyCode(extractEntityCode(commentInfor.getENTITYCOMMENTID().getENTITYKEYCODE()));
			comment.setLineNumber(commentInfor.getENTITYCOMMENTID().getLINENUM().toString());
			if (commentInfor.getENTITYCOMMENTID().getCOMMENTTYPE() != null) {
				comment.setTypeCode(commentInfor.getENTITYCOMMENTID().getCOMMENTTYPE().getTYPECODE());
			}
		}


		comment.setUpdateCount(commentInfor.getRecordid().toString());

		return comment;
	}

	private static String complementEntityKeyCode(String entityCode, String entityKeyCode, String organization) {
		if ("OBJ".equals(entityCode) || "PART".equals(entityCode)) {
			return entityKeyCode + "#" + organization;
		}
		return entityKeyCode;
	}

}

