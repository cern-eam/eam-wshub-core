package ch.cern.eam.wshub.core.client;

import javax.xml.namespace.QName;
import javax.xml.soap.Node;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.*;

class AuthenticationHandler implements SOAPHandler<SOAPMessageContext> {

    public Set<QName> getHeaders() {
        return new TreeSet<>();
    }

    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
            try {
                Iterator<Node> iterator = context.getMessage().getSOAPHeader().getChildElements();
                while (iterator.hasNext()) {
                    Node node = iterator.next();
                    if (node.getLocalName().equals("Session")) {
                        setToken(context, node);
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception in handler: " + e);
            }
        }
        return true;
    }

    private void setToken(SOAPMessageContext context, Node node) {
        String sessionValue = node.getFirstChild().getFirstChild().getTextContent();
        if (sessionValue.startsWith("Bearer")) {
            node.detachNode();
            Map<String, List<String>> headers = (Map<String, java.util.List<String>>) context.get(MessageContext.HTTP_REQUEST_HEADERS);
            headers.put("Authorization", Collections.singletonList(sessionValue));
            context.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        }
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    public void close(MessageContext context) {
        //
    }
}
