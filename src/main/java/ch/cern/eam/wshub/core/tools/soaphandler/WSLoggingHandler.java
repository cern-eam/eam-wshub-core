package ch.cern.eam.wshub.core.tools.soaphandler;


import jakarta.xml.soap.Node;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

import javax.xml.namespace.QName;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class WSLoggingHandler implements SOAPHandler<SOAPMessageContext> {

    Pattern regex = null;
    Logger logger = Logger.getGlobal();

    public WSLoggingHandler() {
        regex = Pattern.compile("(<([a-zA-Z0-9]+:)?password>)([\\s\\S]*?)(</([a-zA-Z0-9]+:)?password>)", Pattern.CASE_INSENSITIVE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        String soapMessage = soapMessageToString(context.getMessage());
        System.out.println(soapMessage);
        try {
            Iterator<Node> it = context.getMessage().getSOAPBody().getChildElements();

            while (it.hasNext()) {
                SOAPElement se = (SOAPElement) it.next();
                if (se.getElementName().getLocalName().equalsIgnoreCase("InformationAlert")) {
                    se.detachNode();
                }
            }
        } catch (Exception e) {

        }
        return true;
    }

    private String soapMessageToString(SOAPMessage message) {
        String result = null;

        if (message != null) {
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                message.writeTo(baos);
                result = baos.toString();
            } catch (Exception e) {
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException ioe) {
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        String soapMessage = soapMessageToString(context.getMessage());
        System.out.println(soapMessage);
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