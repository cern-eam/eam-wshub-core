package ch.cern.eam.wshub.core.services.grids.entities;

import net.datastream.schemas.mp_functions.gridrequest.GRID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.*;

public class GridRequest implements Serializable {
	private static final long serialVersionUID = 6690766576660475229L;

	public enum GRIDTYPE {LIST, LOV};

	private String gridID;
	private String dataspyID;
	private Integer rowCount;
	private Integer cursorPosition;
	private String userFunctionName;
	private String gridName;
	private GRIDTYPE gridType = GRIDTYPE.LIST;
	private Boolean useNative = true;
	private String JPAType;
	private Boolean countTotal = false;
	private Boolean queryTimeout = false;
	private Integer queryTimeoutWaitingTime = 15000; //milliseconds
	private String lang = "EN";
	private Boolean returnHeaderData = false;
	private Boolean includeMetadata = false;
	
	/**
	 * Flag to indicate if fetch all the results
	 */
	private Boolean fetchAllResults = false;
	
	/**
	 * Column name of department security for the Grid.
	 * If null then no department security check
	 */
	private String departmentSecurityGridColumn = null;
	
	private Map<String, Object> params = new HashMap<>();
	
	private List<GridRequestFilter> gridRequestFilters;
	private GridRequestSort[] gridRequestSorts;

	public GridRequest() {
		this.cursorPosition = 1;
		this.rowCount = 100;
	}

	public GridRequest(String gridName) {
		this();
		this.gridName = gridName;
	}

	public GridRequest(String gridName, Integer rowCount) {
		this();
		this.rowCount = rowCount;
		this.gridName = gridName;
	}

	public GridRequest(String gridName, GRIDTYPE gridType) {
		this();
		this.gridName = gridName;
		this.gridType = gridType;
	}

	public GridRequest(String gridID, String gridName, String dataspyID) {
		this();
		this.gridID = gridID;
		this.dataspyID = dataspyID;
		this.gridName = gridName;
	}

	public GridRequest(GridRequest o) {
		super();
		this.gridID = o.gridID;
		this.dataspyID = o.dataspyID;
		this.rowCount = o.rowCount;
		this.cursorPosition = o.cursorPosition;
		this.gridName = o.gridName;
		this.gridType = o.gridType;
		this.useNative = o.useNative;
		this.JPAType = o.JPAType;
		this.countTotal = o.countTotal;
		this.queryTimeout = o.queryTimeout;
		this.queryTimeoutWaitingTime = o.queryTimeoutWaitingTime;
		this.params = o.params;
		this.gridRequestFilters = o.gridRequestFilters;
		this.gridRequestSorts = o.gridRequestSorts;
		this.fetchAllResults = o.fetchAllResults;
		this.departmentSecurityGridColumn = o.departmentSecurityGridColumn;
	}
	
	public String getGridID() {
		return gridID;
	}
	public void setGridID(String gridID) {
		this.gridID = gridID;
	}

	public String getDataspyID() {
		return dataspyID;
	}
	public void setDataspyID(String dataspyID) {
		this.dataspyID = dataspyID;
	}

	public Integer getRowCount() {
		return rowCount;
	}
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public Integer getCursorPosition() {
		return cursorPosition;
	}
	public void setCursorPosition(Integer cursorPosition) {
		this.cursorPosition = cursorPosition;
	}

	public String getGridName() {
		return gridName;
	}
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public Boolean getReturnHeaderData() {return returnHeaderData; }
	public void setReturnHeaderData(Boolean returnHeaderData) { this.returnHeaderData = returnHeaderData; }

	public Boolean getFetchAllResults() {
		return fetchAllResults;
	}
	public void setFetchAllResults(Boolean fetchAllResults) {
		this.fetchAllResults = fetchAllResults;
	}

	@XmlElementWrapper(name="gridSorts")
	@XmlElement(name="gridSort")
	public GridRequestSort[] getGridRequestSorts() {
		return gridRequestSorts;
	}
	public void setGridRequestSorts(GridRequestSort[] gridRequestSorts) {
		this.gridRequestSorts = gridRequestSorts;
	}

	@XmlElementWrapper(name="gridFilters")
    @XmlElement(name="gridFilter")
	public List<GridRequestFilter> getGridRequestFilters() {
		if (gridRequestFilters == null) {
			gridRequestFilters = new LinkedList<>();
		}
		return gridRequestFilters;
	}

	public void setGridRequestFilters(List<GridRequestFilter> gridRequestFilters) {
		this.gridRequestFilters = gridRequestFilters;
	}


	public GRIDTYPE getGridType() {
		return gridType;
	}
	public void setGridType(GRIDTYPE gridType) {
		this.gridType = gridType;
	}

	public Boolean getUseNative() {
		return useNative;
	}
	public void setUseNative(Boolean useNative) {
		this.useNative = useNative;
	}

	public Boolean getCountTotal() {
		return countTotal;
	}
	public void setCountTotal(Boolean countTotal) {
		this.countTotal = countTotal;
	}

	public Boolean getIncludeMetadata() {return includeMetadata; }
	public void setIncludeMetadata(Boolean includeMetadata) {this.includeMetadata = includeMetadata; }

	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getJPAType() {
		return JPAType;
	}
	public void setJPAType(String jPAType) {
		JPAType = jPAType;
	}
	public Boolean getQueryTimeout() {
		return queryTimeout;
	}
	public void setQueryTimeout(Boolean queryTimeout) {
		this.queryTimeout = queryTimeout;
	}
	public Integer getQueryTimeoutWaitingTime() {
		return queryTimeoutWaitingTime;
	}

	public void setQueryTimeoutWaitingTime(Integer queryTimeoutWaitingTime) {
		this.queryTimeoutWaitingTime = queryTimeoutWaitingTime;
	}
	public String getDepartmentSecurityGridColumn() {
		return departmentSecurityGridColumn;
	}
	public void setDepartmentSecurityGridColumn(String departmentSecurityGridColumn) {
		this.departmentSecurityGridColumn = departmentSecurityGridColumn;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getUserFunctionName() {
		return userFunctionName;
	}

	public void setUserFunctionName(String userFunctionName) {
		this.userFunctionName = userFunctionName;
	}

	@Override
	public String toString() {
		return "GridRequest [" + (gridID != null ? "gridID=" + gridID + ", " : "")
				+ (dataspyID != null ? "dataspyID=" + dataspyID + ", " : "")
				+ (rowCount != null ? "rowCount=" + rowCount + ", " : "")
				+ (cursorPosition != null ? "cursorPosition=" + cursorPosition + ", " : "")
				+ (gridName != null ? "gridName=" + gridName + ", " : "")
				+ (gridType != null ? "gridType=" + gridType + ", " : "")
				+ (useNative != null ? "useNative=" + useNative + ", " : "")
				+ (JPAType != null ? "JPAType=" + JPAType + ", " : "")
				+ (countTotal != null ? "countTotal=" + countTotal + ", " : "")
				+ (queryTimeout != null ? "queryTimeout=" + queryTimeout + ", " : "")
				+ (queryTimeoutWaitingTime != null ? "queryTimeoutWaitingTime=" + queryTimeoutWaitingTime + ", " : "")
				+ (fetchAllResults != null ? "fetchAllResults=" + fetchAllResults + ", " : "")
				+ (departmentSecurityGridColumn != null
						? "departmentSecurityGridColumn=" + departmentSecurityGridColumn + ", " : "")
				+ (params != null ? "params=" + params + ", " : "")
				+ (gridRequestFilters != null ? "gridRequestFilters=" + gridRequestFilters + ", " : "")
				+ (gridRequestSorts != null ? "gridRequestSorts=" + Arrays.toString(gridRequestSorts) : "") + "]";
	}

	public String getRequestKey() {
		return this.gridName + "#" + this.getDataspyID();
	}
}
