package ch.cern.eam.wshub.core.services.documents;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.documents.entities.InforDocument;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface DocumentsService {
    List<InforDocument> readInforDocuments(InforContext context, String entity, String objectCode
                                           ) throws InforException;

    String createInforDocumentAndAssociation(InforContext context, InforDocument doc, String entity, String objectCode)
                                                         throws InforException;

    String createInforDocument(InforDocument doc, InforContext context)
                                                                 throws InforException;

    String createInforDocumentAssociation(String document, String entity, String objectCode, InforContext context)
                                                                         throws InforException;
}
