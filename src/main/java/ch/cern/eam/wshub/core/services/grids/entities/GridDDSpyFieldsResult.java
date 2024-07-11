package ch.cern.eam.wshub.core.services.grids.entities;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;

public class GridDDSpyFieldsResult implements Serializable {
	private static final long serialVersionUID = 7166460915418241252L;
	
	private GridField[] gridFields;
	private String dataSpyId;
	
	public String getDataSpyId() {
		return dataSpyId;
	}
	public void setDataSpyId(String dataSpyId) {
		this.dataSpyId = dataSpyId;
	}
	
	@XmlElementWrapper(name="gridFields") 
    @XmlElement(name="gridField")
	public GridField[] getGridFields() {
		return gridFields;
	}
	public void setGridFields(GridField[] gridFields) {
		this.gridFields = gridFields;
	}
	
}
