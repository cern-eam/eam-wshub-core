package ch.cern.eam.wshub.core.tools.soaphandler;


import jakarta.xml.soap.Node;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class NSEraserHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound) {
            try {
                // Clean SOAP Header
                @SuppressWarnings("unchecked")
                Iterator<Node> headerIter = context.getMessage().getSOAPPart().getEnvelope().getHeader().getChildElements();
                while (headerIter.hasNext()) {
                    cleanSOAPElement((SOAPElement) headerIter.next());
                }
                // Clean SOAP Body (only the first element)
                cleanSOAPElement((SOAPElement) context.getMessage().getSOAPPart().getEnvelope().getBody().getChildElements().next());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Problem: " + e.getMessage() + e.getClass().getName());
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void cleanSOAPElement(SOAPElement soapElement) {
        String headerNSPrefix = soapElement.getElementName().getPrefix();
        Iterator<String> namespaceIT = soapElement.getNamespacePrefixes();
        List<String> result = new ArrayList<String>();
        while (namespaceIT.hasNext()) {
            result.add(namespaceIT.next());
        }
        // Remove
        result.stream().filter(ns -> !ns.equals(headerNSPrefix)).forEach(ns -> soapElement.removeNamespaceDeclaration(ns));
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
        // TODO Auto-generated method stub
    }

    @Override
    public Set<QName> getHeaders() {
        // TODO Auto-generated method stub
        return null;
    }
}



