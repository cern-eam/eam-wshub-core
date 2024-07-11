package ch.cern.eam.wshub.core.interceptors.beans;

/**
 * Bean containing extracted information from eam calls
 */
public class EAMExtractedData {

    private String dataReference1;

    private String dataReference2;


    private EAMExtractedData(String dataReference1, String dataReference2) {
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

        public EAMExtractedData build() {
            return new EAMExtractedData(dataReference1, dataReference2);
        }
    }

    public String getDataReference1() {
        return dataReference1;
    }

    public String getDataReference2() {
        return dataReference2;
    }
}
