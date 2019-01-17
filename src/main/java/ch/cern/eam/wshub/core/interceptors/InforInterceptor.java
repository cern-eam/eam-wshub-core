package ch.cern.eam.wshub.core.interceptors;

import ch.cern.eam.wshub.core.interceptors.beans.InforErrorData;
import ch.cern.eam.wshub.core.interceptors.beans.InforExtractedData;
import ch.cern.eam.wshub.core.interceptors.beans.InforRequestData;
import ch.cern.eam.wshub.core.interceptors.beans.InforResponseData;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;

/**
 * Interception of calls to Infor services
 */
public interface InforInterceptor {

    /**
     * Callback executed before the actual call to every Infor service
     * @param operation Operation to execute
     * @param requestData Request parameters
     */
    void before(INFOR_OPERATION operation, InforRequestData requestData);

    /**
     * Callback executed after the call to every Infor service, in case of success
     * @param operation Operation to execute
     * @param requestData Request parameters
     * @param responseData Response parameters
     * @param extractedData Data extracted from the response
     */
    void afterSuccess(INFOR_OPERATION operation, InforRequestData requestData, InforResponseData responseData, InforExtractedData extractedData);

    /**
     * Callback executed after the actual call to every Infor service, in case of error
     * @param operation Operation to execute
     * @param requestData Request parameters
     * @param errorData Error details
     * @param extractedData Data extracted from the response
     */
    void afterError(INFOR_OPERATION operation, InforRequestData requestData, InforErrorData errorData, InforExtractedData extractedData);

}
