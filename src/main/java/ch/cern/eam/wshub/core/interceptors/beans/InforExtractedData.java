package ch.cern.eam.wshub.core.interceptors.beans;

import lombok.Getter;

/**
 * Bean containing extracted information from infor calls
 */
@Getter
public class InforExtractedData {

    private final String dataReference1;

    private final String dataReference2;


    private InforExtractedData(String dataReference1, String dataReference2) {
        this.dataReference1 = dataReference1;
        this.dataReference2 = dataReference2;
    }

    public static class Builder {
        private String dataReference1;
        private String dataReference2;

        public Builder withDataReference1(String dataReference1) {
            this.dataReference1 = dataReference1;
            return this;
        }

        public Builder withDataReference2(String dataReference2) {
            this.dataReference2 = dataReference2;
            return this;
        }

        public InforExtractedData build() {
            return new InforExtractedData(dataReference1, dataReference2);
        }
    }

}
