package ch.cern.eam.wshub.core.client;

import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.services.entities.Signature;

import java.io.Serializable;

/**
 * Context necessary to make a request to Infor
 */
public class InforContext implements Serializable {

    private Credentials credentials;
    private String sessionID;
    private String organizationCode;
    private String tenant;
    private Signature signature;
    private Boolean keepSession;
    private String authToken;

    public InforContext () { }

    public InforContext(Credentials credentials) {
        this.credentials = credentials;
    }

    public InforContext(String sessionID) {
        this.sessionID = sessionID;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getOrganizationCode() {return organizationCode; }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getTenant() { return tenant; }

    public void setTenant(String tenant) { this.tenant = tenant; }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public Boolean getKeepSession() {
        return keepSession;
    }

    public void setKeepSession(Boolean keepSession) {
        this.keepSession = keepSession;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
