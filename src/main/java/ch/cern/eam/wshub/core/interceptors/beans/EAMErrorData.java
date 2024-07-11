package ch.cern.eam.wshub.core.interceptors.beans;

/**
 * Bean containing details about an exception occuring during a call to an EAM service
 */
public class EAMErrorData {

    // Response time in nanoseconds
    private long responseTime;

    private Exception exception;


    private EAMErrorData(long responseTime, Exception exception) {
        this.responseTime = responseTime;
        this.exception = exception;
    }

    public static class Builder {
        private long responseTime;
        private Exception exception;

        public Builder withResponseTime(long responseTime) {
            this.responseTime = responseTime;
            return this;
        }

        public Builder withException(Exception exception) {
            this.exception = exception;
            return this;
        }

        public EAMErrorData build() {
            return new EAMErrorData(responseTime, exception);
        }
    }

    public long getResponseTime() {
        return responseTime;
    }

    public Exception getException() {
        return exception;
    }
}
