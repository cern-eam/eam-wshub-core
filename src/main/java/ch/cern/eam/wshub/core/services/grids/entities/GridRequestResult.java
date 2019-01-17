package ch.cern.eam.wshub.core.services.grids.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;

public class GridRequestResult implements Serializable {
	private static final long serialVersionUID = 8397596268554531607L;
	
	private String moreRowsPresent;
	private String records;
	private GridRequestRow[] rows;

	@XmlElementWrapper(name="rows") 
    @XmlElement(name="row")
	public GridRequestRow[] getRows() {
		return rows;
	}

	public void setRows(GridRequestRow[] rows) {
		this.rows = rows;
	}

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
	
	
}
