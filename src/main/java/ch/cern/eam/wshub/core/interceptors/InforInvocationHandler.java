package ch.cern.eam.wshub.core.interceptors;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.interceptors.beans.InforErrorData;
import ch.cern.eam.wshub.core.interceptors.beans.InforExtractedData;
import ch.cern.eam.wshub.core.interceptors.beans.InforRequestData;
import ch.cern.eam.wshub.core.interceptors.beans.InforResponseData;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.tools.ExceptionInfo;
import ch.cern.eam.wshub.core.tools.InforException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.ws.soap.SOAPFaultException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * Handler allowing the user to decorate Infor services
 */
public class InforInvocationHandler<T> implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(InforInvocationHandler.class);

    private final T target;

    private final InforInterceptor inforInterceptor;

    public InforInvocationHandler(T target, InforInterceptor inforInterceptor) {
        this.target = target;
        this.inforInterceptor = inforInterceptor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Operation operation = method.getAnnotation(Operation.class);

        // Should we intercept this call ?
        boolean intercept = inforInterceptor != null  && operation != null && operation.logOperation() != null;

        if (!intercept) {
            try {
                return method.invoke(target, args);
            } catch(Exception e) {
                throw convertException(e.getCause());
            }
        }

        // Interception of the method
        else {
            INFOR_OPERATION inforOperation = operation.logOperation();
            long startNanoTime = System.nanoTime();

            // InforContext is always the first argument
            InforContext inforContext = args.length >= 0 ? (InforContext) (args[0]) : null;
            Object input = args.length >= 1 ? args[1] : null;

            // Request data
            InforRequestData request = new InforRequestData.Builder()
                    .withInforContext(inforContext)
                    .withInput(input)
                    .build();

            inforInterceptor.before(inforOperation, request);
            try {
                // Invoke actual method
                Object result = method.invoke(target, args);

                // Response data
                InforResponseData response = new InforResponseData.Builder()
                        .withResponseTime(System.nanoTime() - startNanoTime)
                        .withResponse(result)
                        .build();
                InforExtractedData extractedData = extractDataReference(operation, input, result);

                inforInterceptor.afterSuccess(inforOperation, request, response, extractedData);
                return result;
            } catch (Exception e) {
                // Error data
                InforException ie = convertException(e.getCause());
                InforErrorData error = new InforErrorData.Builder()
                        .withResponseTime(System.nanoTime() - startNanoTime)
                        .withException(ie)
                        .build();
                InforExtractedData extractedData = extractDataReference(operation, input, null);
                inforInterceptor.afterError(inforOperation, request, error, extractedData);

                logger.error("Error while calling Infor service " + inforOperation, e);
                throw ie;
            }
        }
    }

    private InforExtractedData extractDataReference(Operation operation, Object input, Object result) {
        String logFieldName1 = operation.logDataReference1FieldName();
        LogDataReferenceType logDataReference1 = operation.logDataReference1();
        String extractDataReference1 = readDataReferenceValue(input, result, logFieldName1, logDataReference1);

        String logFieldName2 = operation.logDataReference2FieldName();
        LogDataReferenceType logDataReference2 = operation.logDataReference2();
        String extractDataReference2 = readDataReferenceValue(input, result, logFieldName2, logDataReference2);

        return new InforExtractedData.Builder()
                .withDataReference1(extractDataReference1)
                .withDataReference2(extractDataReference2)
                .build();
    }


    private String readDataReferenceValue(Object input, Object result, String logFieldName, LogDataReferenceType logDataReference) {
        //
        // INPUT
        //
        if (logDataReference == LogDataReferenceType.INPUT && input != null) {
            return input.toString().replaceAll("'", "''");
        }
        //
        // INPUTFIELD
        //
        if (logDataReference == LogDataReferenceType.INPUTFIELD && input != null) {
            try {
                Field field = input.getClass().getDeclaredField(logFieldName);
                field.setAccessible(true);
                return ((String) field.get(input)).replaceAll("'", "''");
            } catch (Exception e) {
                return null;
            }
        }
        //
        // RESULT
        //
        if (logDataReference == LogDataReferenceType.RESULT && result != null) {
            return result.toString();
        }
        return null;
    }

    // Conversion to InforException
    private InforException convertException(Throwable e) {
        if (e instanceof SOAPFaultException) {
            SOAPFaultException se = (SOAPFaultException) e;
            return new InforException(e.getMessage(), se, extractSOAPFaultException(se));
        } else {
            return new InforException(e.getMessage(), e, null);
        }
    }

    private ExceptionInfo[] extractSOAPFaultException(SOAPFaultException exception) {
        LinkedList<ExceptionInfo> exs = new LinkedList<ExceptionInfo>();
        try {
            // nodeList = list of <ExceptionInfo> elements (see example below)
            NodeList nodeList =  exception.getFault().getDetail().getFirstChild().getChildNodes();
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
