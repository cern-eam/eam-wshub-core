package ch.cern.eam.wshub.core.interceptors.beans;

/**
 * Bean containing details about an exception occuring during a call to an Infor service
 */
public class InforErrorData {

    // Response time in nanoseconds
    private long responseTime;

    private Exception exception;


    private InforErrorData(long responseTime, Exception exception) {
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

        public InforErrorData build() {
            return new InforErrorData(responseTime, exception);
        }
    }

    public long getResponseTime() {
        return responseTime;
    }

    public Exception getException() {
        return exception;
    }
}
