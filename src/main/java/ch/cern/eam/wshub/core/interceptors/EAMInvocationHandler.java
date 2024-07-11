package ch.cern.eam.wshub.core.interceptors;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.interceptors.beans.EAMErrorData;
import ch.cern.eam.wshub.core.interceptors.beans.EAMExtractedData;
import ch.cern.eam.wshub.core.interceptors.beans.EAMRequestData;
import ch.cern.eam.wshub.core.interceptors.beans.EAMResponseData;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.tools.ExceptionInfo;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jakarta.xml.ws.soap.SOAPFaultException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 * Handler allowing the user to decorate EAM services
 */
public class EAMInvocationHandler<T> implements InvocationHandler {

    private final T target;
    private final EAMInterceptor eamInterceptor;
    private final Tools tools;

    public EAMInvocationHandler(T target, EAMInterceptor eamInterceptor, Tools tools) {
        this.target = target;
        this.eamInterceptor = eamInterceptor;
        this.tools = tools;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Operation operation = method.getAnnotation(Operation.class);

        // Should we intercept this call ?
        boolean intercept = eamInterceptor != null  && operation != null && operation.logOperation() != null;

        if (!intercept) {
            try {
                return method.invoke(target, args);
            } catch(Exception e) {
                throw convertException(e.getCause());
            }
        }

        // Interception of the method
        else {
            EAM_OPERATION eamOperation = operation.logOperation();
            long startNanoTime = System.nanoTime();

            // EAMContext is always the first argument
            EAMContext eamContext = args.length >= 0 ? (EAMContext) (args[0]) : null;
            Object input = args.length >= 1 ? args[1] : null;

            // Request data
            EAMRequestData request = new EAMRequestData.Builder()
                    .withEAMContext(eamContext)
                    .withInput(input)
                    .build();

            eamInterceptor.before(eamOperation, request);
            try {
                // Invoke actual method
                Object result = method.invoke(target, args);

                // Response data
                EAMResponseData response = new EAMResponseData.Builder()
                        .withResponseTime(System.nanoTime() - startNanoTime)
                        .withResponse(result)
                        .build();
                EAMExtractedData extractedData = extractDataReference(operation, input, result);

                eamInterceptor.afterSuccess(eamOperation, request, response, extractedData);
                return result;
            } catch (Exception e) {
                // Error data
                EAMException ie = convertException(e.getCause());
                EAMErrorData error = new EAMErrorData.Builder()
                        .withResponseTime(System.nanoTime() - startNanoTime)
                        .withException(ie)
                        .build();
                EAMExtractedData extractedData = extractDataReference(operation, input, null);
                eamInterceptor.afterError(eamOperation, request, error, extractedData);

                tools.log(Level.SEVERE,"Error while calling EAM service " + eamOperation);
                throw ie;
            }
        }
    }

    private EAMExtractedData extractDataReference(Operation operation, Object input, Object result) {
        String logFieldName1 = operation.logDataReference1FieldName();
        LogDataReferenceType logDataReference1 = operation.logDataReference1();
        String extractDataReference1 = readDataReferenceValue(input, result, logFieldName1, logDataReference1);

        String logFieldName2 = operation.logDataReference2FieldName();
        LogDataReferenceType logDataReference2 = operation.logDataReference2();
        String extractDataReference2 = readDataReferenceValue(input, result, logFieldName2, logDataReference2);

        return new EAMExtractedData.Builder()
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

    // Conversion to EAMException
    private EAMException convertException(Throwable e) {
        if (e instanceof SOAPFaultException) {
            SOAPFaultException se = (SOAPFaultException) e;
            return new EAMException(e.getMessage(), se, extractSOAPFaultException(se));
        } else {
            return new EAMException(e.getMessage(), e, null);
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
