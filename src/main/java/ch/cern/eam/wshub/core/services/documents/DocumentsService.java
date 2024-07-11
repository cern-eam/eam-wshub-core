package ch.cern.eam.wshub.core.services.documents;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.documents.entities.EAMDocument;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.util.List;

public interface DocumentsService {
    List<EAMDocument> readEAMDocuments(EAMContext context, String entity, String objectCode
                                           ) throws EAMException;

    String createEAMDocumentAndAssociation(EAMContext context, EAMDocument doc, String entity, String objectCode)
                                                         throws EAMException;

    String createEAMDocument(EAMDocument doc, EAMContext context)
                                                                 throws EAMException;

    String createEAMDocumentAssociation(String document, String entity, String objectCode, EAMContext context)
                                                                         throws EAMException;
}
