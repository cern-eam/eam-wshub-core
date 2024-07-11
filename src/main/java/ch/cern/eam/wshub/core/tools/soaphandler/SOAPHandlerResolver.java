package ch.cern.eam.wshub.core.tools.soaphandler;

import jakarta.xml.ws.handler.Handler;
import jakarta.xml.ws.handler.HandlerResolver;
import jakarta.xml.ws.handler.PortInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SOAPHandlerResolver implements HandlerResolver {
    Logger logger  = Logger.getGlobal();
    @SuppressWarnings("rawtypes")
    @Override
    public List<Handler> getHandlerChain(PortInfo portInfo) {
        List<Handler> handlerChain = new ArrayList<Handler>();
        handlerChain.add(new NSEraserHandler(logger));
        handlerChain.add(new WSLoggingHandler(logger));
        return handlerChain;
    }
}
