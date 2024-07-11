package ch.cern.eam.wshub.core.services.comments.impl;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.comments.CommentService;
import ch.cern.eam.wshub.core.services.comments.entities.Comment;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.util.LinkedList;
import java.util.List;


public class CommentServiceImpl implements CommentService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public CommentServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}

	public String createComment(EAMContext context, Comment comment) throws EAMException {
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
		COMMENT_Type commentEAM = new COMMENT_Type();

		if (comment.getEntityCode() != null) {
			commentEAM.setENTITYCOMMENTID(new ENTITYCOMMENTID_Type());
			commentEAM.getENTITYCOMMENTID().setENTITY(comment.getEntityCode());
			commentEAM.getENTITYCOMMENTID().setENTITYKEYCODE(complementEntityKeyCode(comment.getEntityCode(), comment.getEntityKeyCode(), tools.getOrganizationCode(context, comment.getOrganization())));
			commentEAM.getENTITYCOMMENTID().setLANGUAGEID(new LANGUAGEID_Type());
			commentEAM.getENTITYCOMMENTID().getLANGUAGEID().setLANGUAGECODE("EN");
			commentEAM.getENTITYCOMMENTID().setCOMMENTTYPE(new TYPE_Type());
			if (comment.getTypeCode() != null) {
				commentEAM.getENTITYCOMMENTID().getCOMMENTTYPE().setTYPECODE(comment.getTypeCode());
			} else {
				commentEAM.getENTITYCOMMENTID().getCOMMENTTYPE().setTYPECODE("*");
			}
			//
			if (comment.getLineNumber() != null) {
				commentEAM.getENTITYCOMMENTID().setLINENUM(tools.getDataTypeTools().encodeLong(comment.getLineNumber(), "Line Number"));
			}
		}

		//
		if (comment.getText()!= null && !comment.getText().trim().equals("")) {
			commentEAM.setCOMMENTTEXT(comment.getText());
		}

		//
		if (comment.getCreationUserCode() != null) {
			commentEAM.setCREATEDBY(new USERID_Type());
			commentEAM.getCREATEDBY().setUSERCODE(comment.getCreationUserCode());
		}

		//
		commentEAM.setORGANIZATIONID(tools.getOrganization(context, comment.getOrganization()));

		if (comment.getPrint() == null) {
			commentEAM.setPRINT("true");
		} else {
			commentEAM.setPRINT(encodeBoolean(comment.getPrint(), BooleanType.TRUE_FALSE));
		}

		MP0109_AddComments_001 addComments = new MP0109_AddComments_001();
		addComments.setCOMMENT(commentEAM);

		MP0109_AddComments_001_Result result = tools.performEAMOperation(context, eamws::addCommentsOp, addComments);
		long lineNumber = result.getResultData().getENTITYCOMMENTID().getLINENUM();

		comment.setLineNumber(String.valueOf(lineNumber));

		return comment.getPk();
	}

	public Comment[] readComments(EAMContext context, String entityCode, String entityKeyCode, String typeCode) throws EAMException {
		return readCommentsEAM(context, entityCode, extractEntityCode(entityKeyCode), typeCode, tools.getOrganizationCode(context, extractOrganizationCode(entityCode)))
				.stream().map(this::convertToComment)
				.toArray(Comment[]::new);
	}

	public String updateComment(EAMContext context, Comment comment) throws EAMException {

		if (comment.getEntityCode() == null || comment.getEntityCode().trim().equals("")) {
			throw tools.generateFault("Entity Code is required.");
		}

		if (comment.getEntityKeyCode() == null || comment.getEntityKeyCode().trim().equals("")) {
			throw tools.generateFault("Entity Key Code is required.");
		}

		COMMENT_Type commentEAM = readCommentsEAM(context, comment.getEntityCode(), comment.getEntityKeyCode(), comment.getTypeCode(), comment.getOrganization())
				.stream().filter(commentTemp -> commentTemp.getENTITYCOMMENTID().getLINENUM().toString().equals(comment.getLineNumber())).findFirst().orElse(null);

		if (commentEAM == null) {
			throw tools.generateFault("Comment not found");
		}

		if (comment.getText()!= null && !comment.getText().trim().equals("")) {
			commentEAM.setCOMMENTTEXT(comment.getText());
		}

		if (comment.getPrint() == null) {
			commentEAM.setPRINT("true");
		} else {
			commentEAM.setPRINT(encodeBoolean(comment.getPrint(), BooleanType.TRUE_FALSE));
		}

		MP0110_SyncComments_001 syncComments = new MP0110_SyncComments_001();
		syncComments.setCOMMENT(commentEAM);

		tools.performEAMOperation(context, eamws::syncCommentsOp, syncComments);
		return comment.getPk();
	}

	public String deleteComment(EAMContext context, Comment comment) throws EAMException {
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

		tools.performEAMOperation(context, eamws::deleteCommentsOp, deleteComments);

		return comment.getPk();
	}

	private List<COMMENT_Type> readCommentsEAM(EAMContext context, String entityCode, String entityKeyCode, String typeCode, String organization) throws EAMException {

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

		result.addAll(tools.performEAMOperation(context, eamws::getCommentsOp, getComments).getResultData().getComments().getCOMMENT());

		// Fetch closing comments for Work Orders
		if (isEmpty(typeCode) && entityCode.equals("EVNT")) {
			commentsReq.getCOMMENTTYPE().setTYPECODE("+");
			result.addAll(tools.performEAMOperation(context, eamws::getCommentsOp, getComments).getResultData().getComments().getCOMMENT());
		}

		return result;
	}

	private Comment convertToComment(COMMENT_Type commentEAM) {
		Comment comment = new Comment();

		if (commentEAM.getCREATEDDATE() != null) {
			comment.setCreationDate(tools.getDataTypeTools().retrieveDate(commentEAM.getCREATEDDATE()));
		}

		if (commentEAM.getUPDATEDDATE() != null) {
			comment.setUpdateDate(tools.getDataTypeTools().retrieveDate(commentEAM.getUPDATEDDATE()));
		}

		comment.setText(commentEAM.getCOMMENTTEXT());

		if (commentEAM.getCREATEDBY() != null) {
			comment.setCreationUserCode(commentEAM.getCREATEDBY().getUSERCODE());
			comment.setCreationUserDesc(commentEAM.getCREATEDBY().getDESCRIPTION());
		}

		if (commentEAM.getUPDATEDBY() != null) {
			comment.setUpdateUserCode(commentEAM.getUPDATEDBY().getUSERCODE());
			comment.setUpdateUserDesc(commentEAM.getUPDATEDBY().getDESCRIPTION());
		}

		if (commentEAM.getENTITYCOMMENTID() != null) {
			comment.setEntityCode(commentEAM.getENTITYCOMMENTID().getENTITY());
			comment.setEntityKeyCode(extractEntityCode(commentEAM.getENTITYCOMMENTID().getENTITYKEYCODE()));
			comment.setLineNumber(commentEAM.getENTITYCOMMENTID().getLINENUM().toString());
			if (commentEAM.getENTITYCOMMENTID().getCOMMENTTYPE() != null) {
				comment.setTypeCode(commentEAM.getENTITYCOMMENTID().getCOMMENTTYPE().getTYPECODE());
			}
		}


		comment.setUpdateCount(commentEAM.getRecordid().toString());

		return comment;
	}

	private static String complementEntityKeyCode(String entityCode, String entityKeyCode, String organization) {
		if ("OBJ".equals(entityCode) || "PART".equals(entityCode)) {
			return entityKeyCode + "#" + organization;
		}
		return entityKeyCode;
	}

}

