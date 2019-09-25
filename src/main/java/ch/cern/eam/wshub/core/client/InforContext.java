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
    private String tenant;


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
}
