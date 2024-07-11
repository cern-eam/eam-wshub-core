package ch.cern.eam.wshub.core.interceptors;

import ch.cern.eam.wshub.core.interceptors.beans.EAMErrorData;
import ch.cern.eam.wshub.core.interceptors.beans.EAMExtractedData;
import ch.cern.eam.wshub.core.interceptors.beans.EAMRequestData;
import ch.cern.eam.wshub.core.interceptors.beans.EAMResponseData;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;

/**
 * Interception of calls to EAM services
 */
public interface EAMInterceptor {

    /**
     * Callback executed before the actual call to every EAM service
     * @param operation Operation to execute
     * @param requestData Request parameters
     */
    void before(EAM_OPERATION operation, EAMRequestData requestData);

    /**
     * Callback executed after the call to every EAM service, in case of success
     * @param operation Operation to execute
     * @param requestData Request parameters
     * @param responseData Response parameters
     * @param extractedData Data extracted from the response
     */
    void afterSuccess(EAM_OPERATION operation, EAMRequestData requestData, EAMResponseData responseData, EAMExtractedData extractedData);

    /**
     * Callback executed after the actual call to every EAM service, in case of error
     * @param operation Operation to execute
     * @param requestData Request parameters
     * @param errorData Error details
     * @param extractedData Data extracted from the response
     */
    void afterError(EAM_OPERATION operation, EAMRequestData requestData, EAMErrorData errorData, EAMExtractedData extractedData);

}
