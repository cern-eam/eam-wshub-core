package ch.cern.eam.wshub.core.interceptors.beans;

/**
 * Bean containing details about a response from EAM
 */
public class EAMResponseData {

    // Response time in nanoseconds
    private long responseTime;

    // Response of the EAM service
    private Object response;


    private EAMResponseData(long responseTime, Object response) {
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

        public EAMResponseData build() {
            return new EAMResponseData(responseTime, response);
        }
    }

    public long getResponseTime() {
        return responseTime;
    }

    public Object getResponse() {
        return response;
    }
}
