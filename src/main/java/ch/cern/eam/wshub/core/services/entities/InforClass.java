package ch.cern.eam.wshub.core.services.entities;

public class InforClass {

	private String code;
	private String description;
	private String entity;
	private String outOfService;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getOutOfService() {
		return outOfService;
	}
	public void setOutOfService(String outOfService) {
		this.outOfService = outOfService;
	}
	@Override
	public String toString() {
		return "InforClass ["
				+ (code != null ? "code=" + code + ", " : "")
				+ (description != null ? "description=" + description + ", "
						: "")
				+ (entity != null ? "entity=" + entity + ", " : "")
				+ (outOfService != null ? "outOfService=" + outOfService : "")
				+ "]";
	}
}
