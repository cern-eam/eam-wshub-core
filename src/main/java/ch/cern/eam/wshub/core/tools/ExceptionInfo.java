package ch.cern.eam.wshub.core.tools;

import java.io.Serializable;

public class ExceptionInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6752519760065086203L;

	public ExceptionInfo() {
		super();
	}
	
	public ExceptionInfo(String location, String message) {
		super();
		this.location = location;
		this.message = message;
	}
	private String location;
	private String message;
	private String name;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return (location != null ? "Field=" + location + ", " : "")
				+ (message != null ? "Error=" + message : "");
	}
	
}
