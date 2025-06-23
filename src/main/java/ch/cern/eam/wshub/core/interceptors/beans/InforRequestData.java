package ch.cern.eam.wshub.core.interceptors.beans;

import ch.cern.eam.wshub.core.client.InforContext;
import lombok.Getter;

/**
 * Bean containing details about a request to an Infor service
 */
@Getter
public class InforRequestData {

    // Context of infor request
    private final InforContext inforContext;

    // Object sent in the request to Infor
    private final Object input;


    private InforRequestData(InforContext inforContext, Object input) {
        this.inforContext = inforContext;
        this.input = input;
    }

    public static class Builder {
        private InforContext inforContext;
        private Object input;

        public Builder withInforContext(InforContext inforContext) {
            this.inforContext = inforContext;
            return this;
        }

        public Builder withInput(Object input) {
            this.input = input;
            return this;
        }

        public InforRequestData build() {
            return new InforRequestData(this.inforContext, this.input);
        }
    }

}
