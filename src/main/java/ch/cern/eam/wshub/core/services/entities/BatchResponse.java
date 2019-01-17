package ch.cern.eam.wshub.core.services.entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Container of one single response, in the context of a batch request
 * @param <T> Type of the response
 */
public final class BatchResponse<T> implements Serializable {

    private List<BatchSingleResponse<T>> responseList;

    public BatchResponse() {
        responseList = new LinkedList<>();
    }

    public List<BatchSingleResponse<T>> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<BatchSingleResponse<T>> responseList) {
        this.responseList = responseList;
    }
}
