package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.DataTypeTools;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_functions.gridrequest.*;
import net.datastream.schemas.mp_functions.gridrequest.LOV.LOV_PARAMETERS;
import net.datastream.schemas.mp_functions.gridrequest.LOV.LOV_PARAMETERS.LOV_PARAMETER;
import net.datastream.schemas.mp_functions.gridrequest.MULTIADDON_FILTERS.MADDON_FILTER;
import net.datastream.schemas.mp_functions.mp0116_getgriddataonly_001.FUNCTION_REQUEST_INFO;
import net.datastream.schemas.mp_functions.mp0116_getgriddataonly_001.FUNCTION_REQUEST_TYPE;
import net.datastream.schemas.mp_functions.mp0116_getgriddataonly_001.MP0116_GetGridDataOnly_001;
import net.datastream.schemas.mp_functions.mp0116_getgriddataonly_001_result.MP0116_GetGridDataOnly_001_Result;
import net.datastream.schemas.mp_functions.mp0118_getgridheaderdata_001.MP0118_GetGridHeaderData_001;
import net.datastream.schemas.mp_functions.mp0118_getgridheaderdata_001_result.FIELD;
import net.datastream.schemas.mp_functions.mp0118_getgridheaderdata_001_result.MP0118_GetGridHeaderData_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.decodeBoolean;

public class EAMGrids implements Serializable {
	private static final long serialVersionUID = 5957161022766799698L;

	private ApplicationData applicationData;
	private Tools tools;
	private EAMWebServicesPT eamws;
	public static final Map<String, Map<BigInteger, GridField>> gridFieldCache = new ConcurrentHashMap<>();

	public EAMGrids(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}

	public GridRequestResult executeQuery(EAMContext context, GridRequest gridRequest) throws EAMException {
		if (gridRequest.getIncludeMetadata() || !gridFieldCache.containsKey(gridRequest.getRequestKey())) {
			return executeQueryHeaderData(context, gridRequest);
		} else {
			return executeQueryDataOnly(context, gridRequest);
		}
	}

	public GridRequestResult executeQueryDataOnly(EAMContext context, GridRequest gridRequest) throws EAMException {
		//
		FUNCTION_REQUEST_INFO funRequest = new FUNCTION_REQUEST_INFO();

		// GRID BASICS
		funRequest.setGRID(createGrid(gridRequest));

		// DATA SPY
		if (gridRequest.getDataspyID() != null) {
			funRequest.setDATASPY(new DATASPY());
			funRequest.getDATASPY().setDATASPY_ID(new BigInteger(gridRequest.getDataspyID()));
		}

		// REQUEST TYPE
		funRequest.setREQUEST_TYPE(FUNCTION_REQUEST_TYPE.LIST___DATA___ONLY___STORED);

		// GRID TYPE
		funRequest.setGRID_TYPE(createGridType(gridRequest));

		// FILTERS
		funRequest.setMULTIADDON_FILTERS(createGridFilters(gridRequest));

		// SORT
		funRequest.setADDON_SORT(createSort(gridRequest));

		// LOV PARAMETERS
		funRequest.setLOV(createLovParams(gridRequest));

		//
		// CALL THE WEB SERVICE
		//
		MP0116_GetGridDataOnly_001 getgridd = new MP0116_GetGridDataOnly_001();
		getgridd.setFUNCTION_REQUEST_INFO(funRequest);
		getgridd.getFUNCTION_REQUEST_INFO().getGRID().setLOCALIZE_RESULT(DataTypeTools.encodeBoolean(context.getLocalizeResults(), BooleanType.TRUE_FALSE));
		MP0116_GetGridDataOnly_001_Result result =
			tools.performEAMOperation(context, eamws::getGridDataOnlyOp, getgridd);

		GridRequestResult grr = new GridRequestResult();
		//
		// META DATA
		//
		grr.setGridCode(result.getGRIDRESULT().getGRID().getMETADATA().getGRIDID().toString());
		grr.setGridName(result.getGRIDRESULT().getGRID().getMETADATA().getGRIDNAME());
		grr.setDataSpyId(result.getGRIDRESULT().getDATASPY().getId());
		grr.setRecords(result.getGRIDRESULT().getGRID().getMETADATA().getRECORDS());
		grr.setCursorPosition(result.getGRIDRESULT().getGRID().getMETADATA().getCURRENTCURSORPOSITION().intValue());


		if (result.getGRIDRESULT().getGRID().getMETADATA().getMORERECORDPRESENT().equals("+")) {
			grr.setMoreRowsPresent("TRUE");
		} else {
			grr.setMoreRowsPresent("FALSE");
		}
		//
		// RESULT DATA
		//

		if (result.getGRIDRESULT().getGRID().getDATA() != null && 
				result.getGRIDRESULT().getGRID().getDATA().getROW() != null &&
				result.getGRIDRESULT().getGRID().getDATA().getROW().size() > 0) {

			LinkedList<GridRequestRow> rows = new LinkedList<GridRequestRow>();
			for (net.datastream.schemas.mp_functions.mp0116_getgriddataonly_001_result.DATA.ROW eamRow : result.getGRIDRESULT().getGRID().getDATA().getROW()) {

				//
				List<GridRequestCell> cells = eamRow.getD().stream().map(eamCell -> {
					// If current column is not in the grid cache (or there is no grid cache :-) ) extract only the ID and content from EAM grid cell
					if (gridFieldCache.get(gridRequest.getRequestKey()) == null || gridFieldCache.get(gridRequest.getRequestKey()).get(eamCell.getN()) == null) {
						return new GridRequestCell(eamCell.getN().toString(), eamCell.getContent(), -99999, "UNKNOWN_TAGNAME");
					}
					// Extract the ID, content, order and the tag name from the grid cell
					GridField gridField = gridFieldCache.get(gridRequest.getRequestKey()).get(eamCell.getN());
					return new GridRequestCell(eamCell.getN().toString(),
							decodeCellContent(gridField, eamCell.getContent()),
							gridField.getOrder(),
							gridField.getName());

				}).collect(Collectors.toList());

				// SET ORDER AND TAG NAME
				
				// Sort using the order 
				cells = cells.stream().sorted(Comparator.comparing(GridRequestCell::getOrder)).collect(Collectors.toList());
				
				GridRequestRow row = new GridRequestRow();
				row.setId(eamRow.getId().toString());
				row.setCells(cells.toArray(new GridRequestCell[0]));
				rows.add(row);
			}
			grr.setRows(rows.toArray(new GridRequestRow[0]));
		} 
		else {
			grr.setRows(new GridRequestRow[0]);
		}

		return grr;
	}

	public GridRequestResult executeQueryHeaderData(EAMContext context, GridRequest gridRequest) throws EAMException {
		//
		net.datastream.schemas.mp_functions.mp0118_getgridheaderdata_001.FUNCTION_REQUEST_INFO funRequest = new net.datastream.schemas.mp_functions.mp0118_getgridheaderdata_001.FUNCTION_REQUEST_INFO();

		// GRID BASICS
		funRequest.setGRID(createGrid(gridRequest));

		// DATA SPY
		if (gridRequest.getDataspyID() != null) {
			funRequest.setDATASPY(new DATASPY());
			funRequest.getDATASPY().setDATASPY_ID(new BigInteger(gridRequest.getDataspyID()));
		}

		// REQUEST TYPE
		if (gridRequest.getRowCount() != null && gridRequest.getRowCount() == 0) {
			funRequest.setREQUEST_TYPE(net.datastream.schemas.mp_functions.mp0118_getgridheaderdata_001.FUNCTION_REQUEST_TYPE.LIST___HEAD___ONLY___STORED);
		} else {
			funRequest.setREQUEST_TYPE(net.datastream.schemas.mp_functions.mp0118_getgridheaderdata_001.FUNCTION_REQUEST_TYPE.LIST___HEAD___DATA___STORED);
		}
		// GRID TYPE
		funRequest.setGRID_TYPE(createGridType(gridRequest));

		// FILTERS
		funRequest.setMULTIADDON_FILTERS(createGridFilters(gridRequest));

		// SORT
		funRequest.setADDON_SORT(createSort(gridRequest));

		// LOV PARAMETERS
		funRequest.setLOV(createLovParams(gridRequest));

		//
		// CALL THE WEB SERVICE
		//
		MP0118_GetGridHeaderData_001 getgridd = new MP0118_GetGridHeaderData_001();
		getgridd.setFUNCTION_REQUEST_INFO(funRequest);
		getgridd.getFUNCTION_REQUEST_INFO().getGRID().setLOCALIZE_RESULT(DataTypeTools.encodeBoolean(context.getLocalizeResults(), BooleanType.TRUE_FALSE));
		MP0118_GetGridHeaderData_001_Result result =
			tools.performEAMOperation(context, eamws::getGridHeaderDataOp, getgridd);

		GridRequestResult grr = new GridRequestResult();
		//
		// POPULATE GRID FIELD CACHE
		//
		gridFieldCache.put(gridRequest.getRequestKey(), new ConcurrentHashMap<>());
		for (FIELD field : result.getGRIDRESULT().getGRID().getFIELDS().getFIELD()) {
			gridFieldCache.get(gridRequest.getRequestKey()).put(field.getAliasnum(), decodeEAMGridField(field));
		}

		//
		// META DATA
		//
		grr.setGridCode(result.getGRIDRESULT().getGRID().getMETADATA().getGRIDID().toString());
		grr.setGridName(result.getGRIDRESULT().getGRID().getMETADATA().getGRIDNAME());
		grr.setDataSpyId(result.getGRIDRESULT().getDATASPY().getId());
		grr.setRecords(result.getGRIDRESULT().getGRID().getMETADATA().getRECORDS());
		grr.setCursorPosition(result.getGRIDRESULT().getGRID().getMETADATA().getCURRENTCURSORPOSITION().intValue());

		if (result.getGRIDRESULT().getGRID().getMETADATA().getMORERECORDPRESENT().equals("+")) {
			grr.setMoreRowsPresent("TRUE");
		} else {
			grr.setMoreRowsPresent("FALSE");
		}

		grr.setGridDataspies(result.getGRIDRESULT().getTOOLBAR().getFIELDVALUES().getROW().getDataspylist_Options().getOption().stream()
				.map(option -> new GridDataspy(
						option.getValue().get(0),
						option.getValue().get(0).equals(result.getGRIDRESULT().getDATASPY().getId()) ?
							result.getGRIDRESULT().getDATASPY().getLabel()
							: option.getDisplay().get(0))
				).collect(Collectors.toList()));

		grr.setGridFields(result.getGRIDRESULT().getGRID().getFIELDS().getFIELD().stream()
				.filter(field -> Integer.parseInt(field.getOrder()) >= 0)
				.map(field -> decodeEAMGridField(field))
				.collect(Collectors.toList()));

		//
		// RESULT DATA
		//
		if (result.getGRIDRESULT().getGRID().getDATA() != null &&
				result.getGRIDRESULT().getGRID().getDATA().getROW() != null &&
				result.getGRIDRESULT().getGRID().getDATA().getROW().size() > 0) {

			LinkedList<GridRequestRow> rows = new LinkedList<GridRequestRow>();
			for (net.datastream.schemas.mp_functions.mp0118_getgridheaderdata_001_result.DATA.ROW eamRow : result.getGRIDRESULT().getGRID().getDATA().getROW()) {

				//
				List<GridRequestCell> cells = eamRow.getD().stream().map(eamCell ->
					 new GridRequestCell(eamCell.getN().toString(),
											   decodeCellContent(gridFieldCache.get(gridRequest.getRequestKey()).get(eamCell.getN()), eamCell.getContent()),
											   gridFieldCache.get(gridRequest.getRequestKey()).get(eamCell.getN()).getOrder(),
											   gridFieldCache.get(gridRequest.getRequestKey()).get(eamCell.getN()).getName())
				).collect(Collectors.toList());

				// Sort using the order
				cells = cells.stream().sorted(Comparator.comparing(GridRequestCell::getOrder)).collect(Collectors.toList());

				GridRequestRow row = new GridRequestRow();
				row.setId(eamRow.getId().toString());
				row.setCells(cells.toArray(new GridRequestCell[0]));
				rows.add(row);
			}
			grr.setRows(rows.toArray(new GridRequestRow[0]));
		}
		else {
			grr.setRows(new GridRequestRow[0]);
		}

		return grr;
	}

	private GRID createGrid(GridRequest gridRequest) throws EAMException {
		//
		// GRID BASICS
		//
		GRID grid = new GRID();
		//
		if (gridRequest.getCursorPosition() != null) {
			grid.setCURSOR_POSITION(BigInteger.valueOf(gridRequest.getCursorPosition()));
		} else {
			grid.setCURSOR_POSITION(BigInteger.valueOf(1));
		}
		// GRID NAME
		if (gridRequest.getGridName() != null) {
			grid.setGRID_NAME(gridRequest.getGridName());
		} else {
			throw tools.generateFault("Please supply grid name.");
		}

		// GRID ID
		if (gridRequest.getGridID() != null) {
			grid.setGRID_ID(new BigInteger(gridRequest.getGridID()));
		}

		// USER FUNCTION NAME
		if (tools.getDataTypeTools().isNotEmpty(gridRequest.getUserFunctionName())) {
			grid.setUSER_FUNCTION_NAME(gridRequest.getUserFunctionName());
		}
		// SET NUMBERS OF ROWS TO RETURN
		if (gridRequest.getRowCount() != null) {
			grid.setNUMBER_OF_ROWS_FIRST_RETURNED(BigInteger.valueOf(gridRequest.getRowCount()));
		} else {
			grid.setNUMBER_OF_ROWS_FIRST_RETURNED(BigInteger.valueOf(100));
		}
		// DON'T SHORTEN THE RESPONSE
		grid.setTERSERESPONSE("false");

		return  grid;
	}

	private MULTIADDON_FILTERS createGridFilters(GridRequest gridRequest) {
		MULTIADDON_FILTERS multiaddon_filters = new MULTIADDON_FILTERS();
		//
		// FILTERS
		//
		if (gridRequest.getGridRequestFilters() != null && gridRequest.getGridRequestFilters().size() > 0) {
			int counter = 0;
			for (GridRequestFilter filter: gridRequest.getGridRequestFilters()) {
				MADDON_FILTER eamFilter = new MADDON_FILTER();
				eamFilter.setSEQNUM(BigInteger.valueOf(counter++));

				// JOINER
				if (filter.getJoiner() == GridRequestFilter.JOINER.AND) {
					eamFilter.setJOINER(AND_OR.AND);
				}
				if (filter.getJoiner() == GridRequestFilter.JOINER.OR) {
					eamFilter.setJOINER(AND_OR.OR);
				}
				//
				eamFilter.setOPERATOR(filter.getOperator());
				eamFilter.setALIAS_NAME(filter.getFieldName());
				try {
					String fieldValue = applicationData.isEncodeGridFilter() && filter.getFieldValue() != null ?
							java.net.URLEncoder.encode(filter.getFieldValue(), "UTF-8")
							: filter.getFieldValue();
					eamFilter.setVALUE(fieldValue);
				} catch (UnsupportedEncodingException e) {
					tools.log(Level.WARNING, e.getMessage());
				}
				eamFilter.setLPAREN(tools.getDataTypeTools().decodeBoolean(filter.getLeftParenthesis()));
				eamFilter.setRPAREN(tools.getDataTypeTools().decodeBoolean(filter.getRightParenthesis()));
				switch(eamFilter.getOPERATOR()) {
					case "EQUALS":
						eamFilter.setOPERATOR("=");
						break;
					case "LESS_THAN":
						eamFilter.setOPERATOR("<");
						break;
					case "GREATER_THAN":
						eamFilter.setOPERATOR(">");
						break;
					case "LESS_THAN_EQUALS":
						eamFilter.setOPERATOR("<=");
						break;
					case "SELECTED":
						eamFilter.setOPERATOR("=");
						eamFilter.setVALUE("-1");
						break;
					case "NOT_SELECTED":
						eamFilter.setOPERATOR("=");
						eamFilter.setVALUE("0");
						break;
					case "":
						continue;
				}

				multiaddon_filters.getMADDON_FILTER().add(eamFilter);
			}
		}
		return multiaddon_filters;
	}

	private LOV createLovParams(GridRequest gridRequest) {
		LOV lov = new LOV();
		lov.setLOV_PARAMETERS(new LOV_PARAMETERS());
		gridRequest.getParams().forEach( (paramName, paramValue) -> {
			LOV_PARAMETER lovParameter = new LOV_PARAMETER();
			lovParameter.setTYPE("VARCHAR");
			lovParameter.setALIAS_NAME(paramName);
			if (paramValue != null) {
				lovParameter.setVALUE(paramValue);
			}
			lov.getLOV_PARAMETERS().getLOV_PARAMETER().add(lovParameter);
		});
		return lov;
	}

	private ADDON_SORT createSort(GridRequest gridRequest) {
		if(gridRequest.getGridRequestSorts() != null && gridRequest.getGridRequestSorts().length > 0){
			ADDON_SORT addon_sort = new ADDON_SORT();
				GridRequestSort sort = gridRequest.getGridRequestSorts()[0];
				SORT_TYPE sortType = "DESC".equalsIgnoreCase(sort.getSortType()) ? SORT_TYPE.DESC : SORT_TYPE.ASC;
				addon_sort.setTYPE(sortType);
			addon_sort.setALIAS_NAME(sort.getSortBy());
			return addon_sort;
		} else {
			return null;
		}
	}

	private GRID_TYPE createGridType(GridRequest gridRequest) {
		GRID_TYPE grid_type = new GRID_TYPE();
		if (gridRequest.getGridType() == null || gridRequest.getGridType() == GridRequest.GRIDTYPE.LIST) {
			grid_type.setTYPE(GRID_TYPE_Type.LIST); // LIST: business data query
		} else {
			grid_type.setTYPE(GRID_TYPE_Type.LOV); // LOV: List of values to validate field entry
		}
		return grid_type;
	}

	private String decodeCellContent(GridField gridField, String content) {
		switch (gridField.getDataType()) {
			case "CHKBOOLEAN":
				if ("true".equalsIgnoreCase(content) || "+".equalsIgnoreCase(content) || "Yes".equalsIgnoreCase(content) || "-1".equalsIgnoreCase(content)) {
					return "true";
				} else {
					return "false";
				}
			default:
				return content;
		}
	}

	private GridField decodeEAMGridField(FIELD field) {
		GridField gridField = new GridField();
		if (field.getType() != null) {
			gridField.setDataType(field.getType().value());
		}
		gridField.setId(field.getAliasnum().toString());
		gridField.setName(field.getName());
		gridField.setOrder(Integer.parseInt(field.getOrder()));
		gridField.setWidth(field.getWidth());
		gridField.setLabel(field.getLabel());
		gridField.setVisible(decodeBoolean(field.getVisible()));
		return gridField;
	}

}
