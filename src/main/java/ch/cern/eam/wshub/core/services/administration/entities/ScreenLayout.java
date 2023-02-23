package ch.cern.eam.wshub.core.services.administration.entities;

import java.util.HashMap;
import java.util.Map;

public class ScreenLayout {

	private Map<String, ElementInfo> fields;
	private Map<String, Tab> tabs;

	public Map<String, ElementInfo> getFields() {
		return fields;
	}
	public void setFields(Map<String, ElementInfo> fields) {
		this.fields = fields;
	}

	public Map<String, Tab> getTabs() {
		if (tabs == null) {
			tabs = new HashMap<>();
		}
		return tabs;
	}
	public void setTabs(Map<String, Tab> tabs) {
		this.tabs = tabs;
	}

}
