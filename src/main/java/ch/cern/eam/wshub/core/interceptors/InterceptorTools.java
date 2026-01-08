package ch.cern.eam.wshub.core.interceptors;

import ch.cern.eam.wshub.core.tools.EAMException;
import jakarta.xml.ws.soap.SOAPFaultException;
import ch.cern.eam.wshub.core.tools.ExceptionInfo;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.LinkedList;

public class InterceptorTools {
    // Conversion to EAMException
    public static EAMException convertException(Throwable e) {
        if (e instanceof SOAPFaultException) {
            SOAPFaultException se = (SOAPFaultException) e;
            return new EAMException(e.getMessage(), se, extractSOAPFaultException(se));
        } else {
            return new EAMException(e.getMessage(), e, null);
        }
    }

    public static ExceptionInfo[] extractSOAPFaultException(SOAPFaultException exception) {
        LinkedList<ExceptionInfo> exs = new LinkedList<ExceptionInfo>();
        try {
            // nodeList = list of <ExceptionInfo> elements (see example below)
            NodeList nodeList = exception.getFault().getDetail().getFirstChild().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                ExceptionInfo exceptionInfo = new ExceptionInfo();

                Node locationNode = nodeList.item(i).getAttributes().getNamedItem("location_reference");
                if (locationNode != null) {
                    String locationString = locationNode.getTextContent().replace("/", "_");
                    if (locationString.startsWith("_")) {
                        locationString = "EAMID" + locationString;
                    } else {
                        locationString = "EAMID_" + locationString;
                    }
                    exceptionInfo.setLocation(locationString);
                }

                exceptionInfo.setMessage(nodeList.item(i).getFirstChild().getLastChild().getTextContent());
                exs.add(exceptionInfo);
            }
        } catch (Exception e) {
            return null;
        }
        return exs.toArray(new ExceptionInfo[0]);
    }
}
