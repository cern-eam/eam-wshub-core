package ch.cern.eam.wshub.core.tools;

public class ApplicationData {

	private String url;
	private String tenant;
	private String organization;
	private Boolean withJPAGridsAuthentication = false;

	public static Boolean localizeResults = true;

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
		return "15000";
	}

	public String getDateFormat() {
		return "dd-MMM-yyyy";
	}

	public String getDateDBFormat() {
		return "MM/dd/yyyy";
	}

	public String getDateTimeFormat() {
		return "dd-MMM-yyyy HH:mm";
	}

	public String getDateTimeDBFormat() {
		return "MM/dd/yyyy HH:mm";
	}

	public Boolean getWithJPAGridsAuthentication() {
		return withJPAGridsAuthentication;
	}

	public void setWithJPAGridsAuthentication(Boolean withJPAGridsAuthentication) {
		this.withJPAGridsAuthentication = withJPAGridsAuthentication;
	}

	public Long getQueryMaxNumberOfRows() {
		return 1000L;
	}

	public boolean isEncodeGridFilter() {
		return "true".equals(System.getProperty("ENCODE_GRID_FILTERS"));
	}

}
