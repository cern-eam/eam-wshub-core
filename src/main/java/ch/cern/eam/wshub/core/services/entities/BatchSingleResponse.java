package ch.cern.eam.wshub.core.services.entities;

import java.io.Serializable;

/**
 * Container of one single response, in the context of a batch request
 * @param <T> Type of the entry parameter
 */
public final class BatchSingleResponse<T> implements Serializable {

    private T response;
    private String errorMessage;

    private final BATCH_SINGLE_RESPONSE_STATUS status;

    public enum BATCH_SINGLE_RESPONSE_STATUS {
        OK,
        FAILED,
    }

    public BatchSingleResponse(T response, String errorMessage) {
        this.response = response;
        this.errorMessage = errorMessage;
        if (response != null) {
            status = BATCH_SINGLE_RESPONSE_STATUS.OK;
        } else {
            status = BATCH_SINGLE_RESPONSE_STATUS.FAILED;
        }
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String exception) {
        this.errorMessage = errorMessage;
    }

    public BATCH_SINGLE_RESPONSE_STATUS getStatus() {
        return status;
    }
}
