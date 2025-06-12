package ch.cern.eam.wshub.core.interceptors.beans;

import lombok.Getter;

/**
 * Bean containing details about a response from Infor
 */
@Getter
public class InforResponseData {

    // Response time in nanoseconds
    private final long responseTime;

    // Response of the Infor service
    private final Object response;


    private InforResponseData(long responseTime, Object response) {
        this.responseTime = responseTime;
        this.response = response;
    }

    public static class Builder {
        private long responseTime;
        private Object response;

        public Builder withResponseTime(long responseTime) {
            this.responseTime = responseTime;
            return this;
        }

        public Builder withResponse(Object response) {
            this.response = response;
            return this;
        }

        public InforResponseData build() {
            return new InforResponseData(responseTime, response);
        }
    }

}
