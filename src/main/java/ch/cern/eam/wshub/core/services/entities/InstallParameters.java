package ch.cern.eam.wshub.core.services.entities;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@NamedNativeQueries({
	@NamedNativeQuery(name=InstallParameters.GETINSTALLPARAMS,
		query="select * from r5install"
	)
})
@Table(name="r5install")
public class InstallParameters implements Serializable {
	
	private static final long serialVersionUID = 5661071586437074385L;
	public static final String GETINSTALLPARAMS = "InstallParameters.GETINSTALLPARAMS";

	@Id
	@Column(name="ins_code")
	private String code;
	
	@Column(name="ins_desc")
	private String value;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
