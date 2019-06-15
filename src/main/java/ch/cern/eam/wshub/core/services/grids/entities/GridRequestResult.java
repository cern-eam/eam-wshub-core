package ch.cern.eam.wshub.core.services.grids.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.List;

public class GridRequestResult implements Serializable {
	private static final long serialVersionUID = 8397596268554531607L;

	private String gridCode;
	private String gridName;
	private String dataSpyId;
	private String moreRowsPresent;
	private int cursorPosition;
	private String records;

	private List<GridDataspy> gridDataspies;
	private List<GridField> gridFields;
	private GridRequestRow[] rows;

	@XmlElementWrapper(name="rows") 
    @XmlElement(name="row")
	public GridRequestRow[] getRows() {
		return rows;
	}
	public void setRows(GridRequestRow[] rows) {
		this.rows = rows;
	}

	@XmlElementWrapper(name="gridDataspies")
	@XmlElement(name="gridDataspy")
	public List<GridDataspy> getGridDataspies() {
		return gridDataspies;
	}
	public void setGridDataspies(List<GridDataspy> gridDataspies) {
		this.gridDataspies = gridDataspies;
	}

	@XmlElementWrapper(name="gridFields")
	@XmlElement(name="gridField")
	public List<GridField> getGridFields() {
		return gridFields;
	}
	public void setGridFields(List<GridField> gridFields) {
		this.gridFields = gridFields;
	}

	public String getGridCode() {return gridCode; }
	public void setGridCode(String gridCode) { this.gridCode = gridCode; }

	public String getGridName() { return gridName; }
	public void setGridName(String gridName) { this.gridName = gridName; }

	public String getDataSpyId() { return dataSpyId; }
	public void setDataSpyId(String dataSpyId) { this.dataSpyId = dataSpyId; }

	public String getMoreRowsPresent() {
		return moreRowsPresent;
	}
	public void setMoreRowsPresent(String moreRowsPresent) {
		this.moreRowsPresent = moreRowsPresent;
	}

	public String getRecords() {
		return records;
	}
	public void setRecords(String records) {
		this.records = records;
	}

	public int getCursorPosition() {return cursorPosition; }
	public void setCursorPosition(int cursorPosition) { this.cursorPosition = cursorPosition; }
}
