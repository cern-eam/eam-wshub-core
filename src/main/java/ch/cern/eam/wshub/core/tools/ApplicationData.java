package ch.cern.eam.wshub.core.tools;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplicationData {
	public static Boolean localizeResults = true;

	private String url;
	private String tenant;
	private String organization;
	private Boolean withJPAGridsAuthentication = false;

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

	public Long getQueryMaxNumberOfRows() {
		return 1000L;
	}

	public boolean isEncodeGridFilter() {
		return "true".equals(System.getProperty("ENCODE_GRID_FILTERS"));
	}
}
