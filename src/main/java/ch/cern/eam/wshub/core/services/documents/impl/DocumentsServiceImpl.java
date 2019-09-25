package ch.cern.eam.wshub.core.services.documents.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.documents.DocumentsService;
import ch.cern.eam.wshub.core.services.documents.entities.InforDocument;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.tools.InforException;
import net.datastream.schemas.mp_entities.document_001.Document;
import net.datastream.schemas.mp_fields.CLASSID_Type;
import net.datastream.schemas.mp_fields.DOCUMENTENTITYID_Type;
import net.datastream.schemas.mp_fields.DOCUMENTENTITY_Type;
import net.datastream.schemas.mp_fields.DOCUMENTID_Type;
import net.datastream.schemas.mp_fields.TYPE_Type;
import net.datastream.schemas.mp_functions.mp0112_001.MP0112_AddDocumentAssociation_001;
import net.datastream.schemas.mp_functions.mp6001_001.MP6001_AddDocument_001;
import net.datastream.schemas.mp_results.mp0112_001.MP0112_AddDocumentAssociation_001_Result;
import net.datastream.schemas.mp_results.mp6001_001.MP6001_AddDocument_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.List;

public class DocumentsServiceImpl implements DocumentsService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public DocumentsServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}
	
	@Override
	public List<InforDocument> readInforDocuments(InforContext context, String entity, String objectCode
												  ) throws InforException {
		if ((entity == null || entity.isEmpty())
				|| (objectCode == null || objectCode.isEmpty())) {
			throw tools.generateFault("Parameters not supplied correctly.");
		}
		return tools.getEntityManager().createNamedQuery(InforDocument.GET_DOCUMENTS, InforDocument.class)
				.setParameter("code", objectCode).setParameter("entity", entity)
				.getResultList();
	}

	@Override
	public String createInforDocumentAndAssociation(InforContext context, InforDocument doc, String entity, String objectCode)
			throws InforException {
		createInforDocument(doc, context);
		String result = createInforDocumentAssociation(doc.getCode(), entity, objectCode, context);
		return result;
	}
	
	@Override
	public String createInforDocument(InforDocument doc, InforContext context)
			throws InforException {

		MP6001_AddDocument_001 addDoc = new MP6001_AddDocument_001();
		Document document = new Document();
		
		// Set main info
		document.setDOCUMENTID(new DOCUMENTID_Type());
		document.getDOCUMENTID().setORGANIZATIONID(tools.getOrganization(context));
		document.getDOCUMENTID().setDOCUMENTCODE(doc.getCode());
		document.getDOCUMENTID().setDESCRIPTION(doc.getDescription());
		
		// Set filename
		if(doc.getFilename() != null) {
			document.setFILE(doc.getFilename());
		}
		
		// Set class
		if(doc.getDocClass() != null) {
			document.setCLASSID(new CLASSID_Type());
			document.getCLASSID().setCLASSCODE(doc.getDocClass());
		}
		
		// Set filetype
		if(doc.getFiletype() != null) {
			document.setFILETYPE(doc.getFiletype()); 
		}

		// Set type
		// possible types: select *  from r5ucodes where uco_entity = 'DOTP';
		// D = Dynamic document
		// F = File system document
		// S = Static document
		// T = Template document
		// U = Uploaded document
		document.setDOCUMENTTYPE(new TYPE_Type());
		if(doc.getType() != null) {
			document.getDOCUMENTTYPE().setTYPECODE(doc.getType());
		} else {
			document.getDOCUMENTTYPE().setTYPECODE("F");
		}

		document.setOUTOFSERVICE("false");
		
		addDoc.setDocument(document);
		MP6001_AddDocument_001_Result result = inforws.addDocumentOp(addDoc, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));

		return result.getResultData().getDOCUMENTID().getDOCUMENTCODE();
	}
	
	@Override
	public String createInforDocumentAssociation(String document, String entity, String objectCode, InforContext context)
			throws InforException {
		
		// Handle postfix to the entity code
		String inforObjectCode = objectCode;
		switch (entity) {
			// if it is an asset or part we add postfix #*
			case "OBJ":
			case "PART":
			case "LOC":
				inforObjectCode+="#*";//#*
				break;
			case "PPM":
				inforObjectCode+="#0";//#0
				break; 
			default:
			// otherwise we do nothing
			case "ACT": 
			case "CASE":
			case "STWO":
			case "EVNT":
			case "PROJ":
				break;
		}
		
		MP0112_AddDocumentAssociation_001 addDocAssoc = new MP0112_AddDocumentAssociation_001();
		addDocAssoc.setDOCUMENTENTITY(new DOCUMENTENTITY_Type());
		addDocAssoc.getDOCUMENTENTITY().setType("*");
		addDocAssoc.getDOCUMENTENTITY().setDOCUMENTENTITYID(new DOCUMENTENTITYID_Type());
		addDocAssoc.getDOCUMENTENTITY().getDOCUMENTENTITYID().setCode(inforObjectCode);   
		addDocAssoc.getDOCUMENTENTITY().getDOCUMENTENTITYID().setDocument(document);
		addDocAssoc.getDOCUMENTENTITY().getDOCUMENTENTITYID().setRentity(entity);
		MP0112_AddDocumentAssociation_001_Result result = inforws.addDocumentAssociationOp(addDocAssoc, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));

		return result.getDOCUMENTENTITYID().getDocument(); 
	}

}
