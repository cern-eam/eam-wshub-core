package ch.cern.eam.wshub.core.client;

import ch.cern.eam.wshub.core.services.entities.Credentials;

import java.io.Serializable;

/**
 * Context necessary to make a request to Infor
 */
public class InforContext implements Serializable {

    private Credentials credentials;
    private String sessionID;
    private String organizationCode;

    private InforContext(Credentials credentials, String sessionID, String organizationCode) {
        this.credentials = credentials;
        this.sessionID = sessionID;
        this.organizationCode = organizationCode;
    };

    public static class Builder implements Serializable {
        private Credentials credentials;
        private String sessionID;
        private String organizationCode;

        public Builder withCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public Builder withSessionID(String sessionID) {
            this.sessionID = sessionID;
            return this;
        }

        public Builder withOrganizationCode(String organizationCode) {
            this.organizationCode = organizationCode;
            return this;
        }

        public InforContext build() {
            return new InforContext(credentials, sessionID, organizationCode);
        }
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getOrganizationCode() {return organizationCode; }
}
