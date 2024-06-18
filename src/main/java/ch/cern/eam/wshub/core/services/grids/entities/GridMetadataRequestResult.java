package ch.cern.eam.wshub.core.services.grids.entities;

import javax.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;

@Entity
@Table(name="R5GRID")
public class GridMetadataRequestResult implements Serializable {

	private static final long serialVersionUID = 7166460915418241252L;
	@Id
	@Column(name="GRD_GRIDID")
	private String gridCode;
	@Column(name="GRD_GRIDNAME")
	private String gridName;
	@Transient private GridDataspy[] gridDataspies;
	@Transient private GridField[] gridFields;
	@Transient private String dataSpyId;
	
	public String getGridCode() {
		return gridCode;
	}
	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}
	public String getDataSpyId() {
		return dataSpyId;
	}
	public void setDataSpyId(String dataSpyId) {
		this.dataSpyId = dataSpyId;
	}
	
	@XmlElementWrapper(name="gridDataspies") 
    @XmlElement(name="gridDataspy")
	public GridDataspy[] getGridDataspies() {
		return gridDataspies;
	}
	public void setGridDataspies(GridDataspy[] gridDataspies) {
		this.gridDataspies = gridDataspies;
	}
	
	@XmlElementWrapper(name="gridFields") 
    @XmlElement(name="gridField")
	public GridField[] getGridFields() {
		return gridFields;
	}
	public void setGridFields(GridField[] gridFields) {
		this.gridFields = gridFields;
	}
	public String getGridName() {
		return gridName;
	}
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}
	
	
}
