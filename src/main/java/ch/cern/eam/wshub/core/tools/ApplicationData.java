package ch.cern.eam.wshub.core.tools;

public class ApplicationData {

	private String url;
	private String tenant;
	private String organization;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getQueryTimeout() {
		return "QUERY_TIMEOUT";
	}

	public String getDateFormat() {
		return "DATE_FORMAT";
	}

	public String getDateTimeFormat() {
		return "DATETIME_FORMAT";
	}

	public String getDateTimeDBFormat() {
		return "DATETIME_DB_FORMAT";
	}

}
