package ch.cern.eam.wshub.core.services.documents.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.documents.DocumentsService;
import ch.cern.eam.wshub.core.services.documents.entities.EAMDocument;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.tools.EAMException;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import jakarta.persistence.EntityManager;
import java.util.List;

public class DocumentsServiceImpl implements DocumentsService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public DocumentsServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}
	
	@Override
	public List<EAMDocument> readEAMDocuments(EAMContext context, String entity, String objectCode
												  ) throws EAMException {
		if ((entity == null || entity.isEmpty())
				|| (objectCode == null || objectCode.isEmpty())) {
			throw tools.generateFault("Parameters not supplied correctly.");
		}
		EntityManager em = tools.getEntityManager();
		try {
			return em.createNamedQuery(EAMDocument.GET_DOCUMENTS, EAMDocument.class)
					.setParameter("code", objectCode).setParameter("entity", entity)
					.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public String createEAMDocumentAndAssociation(EAMContext context, EAMDocument doc, String entity, String objectCode)
			throws EAMException {
		createEAMDocument(doc, context);
		String result = createEAMDocumentAssociation(doc.getCode(), entity, objectCode, context);
		return result;
	}
	
	@Override
	public String createEAMDocument(EAMDocument doc, EAMContext context)
			throws EAMException {

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
		MP6001_AddDocument_001_Result result =
			tools.performEAMOperation(context, eamws::addDocumentOp, addDoc);

		return result.getResultData().getDOCUMENTID().getDOCUMENTCODE();
	}
	
	@Override
	public String createEAMDocumentAssociation(String document, String entity, String objectCode, EAMContext context)
			throws EAMException {
		
		// Handle postfix to the entity code
		String eamObjectCode = objectCode;
		switch (entity) {
			// if it is an asset or part we add postfix #*
			case "OBJ":
			case "PART":
			case "LOC":
				eamObjectCode+="#*";//#*
				break;
			case "PPM":
				eamObjectCode+="#0";//#0
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
		addDocAssoc.getDOCUMENTENTITY().getDOCUMENTENTITYID().setCode(eamObjectCode);   
		addDocAssoc.getDOCUMENTENTITY().getDOCUMENTENTITYID().setDocument(document);
		addDocAssoc.getDOCUMENTENTITY().getDOCUMENTENTITYID().setRentity(entity);

		MP0112_AddDocumentAssociation_001_Result result =
			tools.performEAMOperation(context, eamws::addDocumentAssociationOp, addDocAssoc);

		return result.getDOCUMENTENTITYID().getDocument(); 
	}

}
