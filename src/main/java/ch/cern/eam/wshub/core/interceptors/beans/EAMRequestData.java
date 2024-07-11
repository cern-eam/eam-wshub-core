package ch.cern.eam.wshub.core.interceptors.beans;

import ch.cern.eam.wshub.core.client.EAMContext;

/**
 * Bean containing details about a request to an EAM service
 */
public class EAMRequestData {

    // Context of eam request
    private EAMContext eamContext;

    // Object sent in the request to EAM
    private Object input;


    private EAMRequestData(EAMContext eamContext, Object input) {
        this.eamContext = eamContext;
        this.input = input;
    }

    public static class Builder {
        private EAMContext eamContext;
        private Object input;

        public Builder withEAMContext(EAMContext eamContext) {
            this.eamContext = eamContext;
            return this;
        }

        public Builder withInput(Object input) {
            this.input = input;
            return this;
        }

        public EAMRequestData build() {
            return new EAMRequestData(this.eamContext, this.input);
        }
    }

    public EAMContext getEAMContext() {
        return eamContext;
    }

    public Object getInput() {
        return input;
    }
}
