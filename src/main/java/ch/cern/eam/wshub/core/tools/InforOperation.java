package ch.cern.eam.wshub.core.tools;

import net.datastream.schemas.mp_functions.MessageConfigType;
import org.xmlsoap.schemas.ws._2002._04.secext.Security;

import jakarta.xml.ws.Holder;

@FunctionalInterface
public interface InforOperation<A, R> {
    R apply(
            A operation,
            String unknown,
            Security securityHeader,
            String unknown2,
            Holder holder,
            MessageConfigType messageConfigType,
            String tenant);
}