package ch.cern.eam.wshub.core.interceptors.beans;

import lombok.Getter;

/**
 * Bean containing details about an exception occurring during a call to an Infor service
 */
@Getter
public class InforErrorData {

    // Response time in nanoseconds
    private final long responseTime;

    private final Exception exception;


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

}
