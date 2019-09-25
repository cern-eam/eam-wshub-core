package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_functions.SessionType;
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
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.xml.ws.Holder;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InforGrids implements Serializable {
	private static final long serialVersionUID = 5957161022766799698L;

	private ApplicationData applicationData;
	private Tools tools;
	private InforWebServicesPT inforws;
	private static Map<String, Map<BigInteger, GridField>> gridFieldCache = new ConcurrentHashMap<>();

	public InforGrids(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public GridRequestResult executeQuery(InforContext context, GridRequest gridRequest) throws InforException {
		if (gridRequest.getIncludeMetadata() || !gridFieldCache.containsKey(gridRequest.getRequestKey())) {
			return executeQueryHeaderData(context, gridRequest);
		} else {
			return executeQueryDataOnly(context, gridRequest);
		}
	}

	public GridRequestResult executeQueryDataOnly(InforContext context, GridRequest gridRequest) throws InforException {
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
		MP0116_GetGridDataOnly_001_Result result = new MP0116_GetGridDataOnly_001_Result();

		if (context.getCredentials() != null) {
			result = inforws.getGridDataOnlyOp(getgridd, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else 
		{
			SessionType session = new SessionType();
			session.setSessionId(context.getSessionID());
			result = inforws.getGridDataOnlyOp(getgridd, tools.getOrganizationCode(context), null,"", new Holder<SessionType>(session), null, tools.getTenant(context));
		}

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
			for (net.datastream.schemas.mp_functions.mp0116_getgriddataonly_001_result.DATA.ROW inforRow : result.getGRIDRESULT().getGRID().getDATA().getROW()) {

				//
				List<GridRequestCell> cells = inforRow.getD().stream().map(inforCell -> {
					return new GridRequestCell(inforCell.getN().toString(),
							decodeCellContent(gridFieldCache.get(gridRequest.getRequestKey()).get(inforCell.getN()), inforCell.getContent()),
							Integer.parseInt(gridFieldCache.get(gridRequest.getRequestKey()).get(inforCell.getN()).getOrder()),
							gridFieldCache.get(gridRequest.getRequestKey()).get(inforCell.getN()).getName());
				}).collect(Collectors.toList());

				// SET ORDER AND TAG NAME
				
				// Sort using the order 
				cells = cells.stream().sorted(Comparator.comparing(GridRequestCell::getOrder)).collect(Collectors.toList());
				
				GridRequestRow row = new GridRequestRow();
				row.setId(inforRow.getId().toString());
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

	public GridRequestResult executeQueryHeaderData(InforContext context, GridRequest gridRequest) throws InforException {
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
		funRequest.setREQUEST_TYPE(net.datastream.schemas.mp_functions.mp0118_getgridheaderdata_001.FUNCTION_REQUEST_TYPE.LIST___HEAD___DATA___STORED);

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
		MP0118_GetGridHeaderData_001_Result result = new MP0118_GetGridHeaderData_001_Result();

		if (context.getCredentials() != null) {
			result = inforws.getGridHeaderDataOp(getgridd, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else
		{
			SessionType session = new SessionType();
			session.setSessionId(context.getSessionID());
			result = inforws.getGridHeaderDataOp(getgridd, tools.getOrganizationCode(context), null,"", new Holder<SessionType>(session), null, tools.getTenant(context));
		}

		GridRequestResult grr = new GridRequestResult();
		//
		// POPULATE GRID FIELD CACHE
		//
		gridFieldCache.put(gridRequest.getRequestKey(), new ConcurrentHashMap<>());
		for (FIELD field : result.getGRIDRESULT().getGRID().getFIELDS().getFIELD()) {
			gridFieldCache.get(gridRequest.getRequestKey()).put(field.getAliasnum(), decodeInforGridField(field));
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
				.map(option -> new GridDataspy(option.getValue().get(0), option.getDisplay().get(0))).collect(Collectors.toList()));

		grr.setGridFields(result.getGRIDRESULT().getGRID().getFIELDS().getFIELD().stream()
				.filter(field -> Integer.parseInt(field.getOrder()) >= 0)
				.map(field -> decodeInforGridField(field))
				.collect(Collectors.toList()));

		//
		// RESULT DATA
		//
		if (result.getGRIDRESULT().getGRID().getDATA() != null &&
				result.getGRIDRESULT().getGRID().getDATA().getROW() != null &&
				result.getGRIDRESULT().getGRID().getDATA().getROW().size() > 0) {

			LinkedList<GridRequestRow> rows = new LinkedList<GridRequestRow>();
			for (net.datastream.schemas.mp_functions.mp0118_getgridheaderdata_001_result.DATA.ROW inforRow : result.getGRIDRESULT().getGRID().getDATA().getROW()) {

				//
				List<GridRequestCell> cells = inforRow.getD().stream().map(inforCell -> {
					return new GridRequestCell(inforCell.getN().toString(),
											   decodeCellContent(gridFieldCache.get(gridRequest.getRequestKey()).get(inforCell.getN()), inforCell.getContent()),
											   Integer.parseInt(gridFieldCache.get(gridRequest.getRequestKey()).get(inforCell.getN()).getOrder()),
											   gridFieldCache.get(gridRequest.getRequestKey()).get(inforCell.getN()).getName());
				}).collect(Collectors.toList());

				// Sort using the order
				cells = cells.stream().sorted(Comparator.comparing(GridRequestCell::getOrder)).collect(Collectors.toList());

				GridRequestRow row = new GridRequestRow();
				row.setId(inforRow.getId().toString());
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

	private GRID createGrid(GridRequest gridRequest) throws InforException {
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
				MADDON_FILTER inforFilter = new MADDON_FILTER();
				inforFilter.setSEQNUM(BigInteger.valueOf(counter++));

				// JOINER
				if (filter.getJoiner() == GridRequestFilter.JOINER.AND) {
					inforFilter.setJOINER(AND_OR.AND);
				}
				if (filter.getJoiner() == GridRequestFilter.JOINER.OR) {
					inforFilter.setJOINER(AND_OR.OR);
				}
				//
				inforFilter.setOPERATOR(filter.getOperator());
				inforFilter.setALIAS_NAME(filter.getFieldName());
				inforFilter.setVALUE(filter.getFieldValue());
				inforFilter.setLPAREN(tools.getDataTypeTools().decodeBoolean(filter.getLeftParenthesis()));
				inforFilter.setRPAREN(tools.getDataTypeTools().decodeBoolean(filter.getRightParenthesis()));
				switch(inforFilter.getOPERATOR()) {
					case "EQUALS":
						inforFilter.setOPERATOR("=");
						break;
					case "LESS_THAN":
						inforFilter.setOPERATOR("<");
						break;
					case "GREATER_THAN":
						inforFilter.setOPERATOR(">");
						break;
					case "LESS_THAN_EQUALS":
						inforFilter.setOPERATOR("<=");
						break;
					case "GREATER_THAN_EQUALS":
						inforFilter.setOPERATOR(">=");
						break;
				}

				multiaddon_filters.getMADDON_FILTER().add(inforFilter);
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
				lovParameter.setVALUE(paramValue.toString());
			}
			lov.getLOV_PARAMETERS().getLOV_PARAMETER().add(lovParameter);
		});
		return lov;
	}

	private ADDON_SORT createSort(GridRequest gridRequest) {
		if(gridRequest.getGridRequestSorts() != null && gridRequest.getGridRequestSorts().length > 0){
			ADDON_SORT addon_sort = new ADDON_SORT();
				GridRequestSort sort = gridRequest.getGridRequestSorts()[0];
				if (sort.getSortType() != null && sort.getSortType().toUpperCase().equals("DESC")) {
					addon_sort.setTYPE(SORT_TYPE.DESC);
				} else {
					addon_sort.setTYPE(SORT_TYPE.ASC);
				}
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

	private GridField decodeInforGridField(FIELD field) {
		GridField gridField = new GridField();
		if (field.getType() != null) {
			gridField.setDataType(field.getType().value());
		}
		gridField.setId(field.getAliasnum().toString());
		gridField.setName(field.getName());
		gridField.setOrder(field.getOrder());
		gridField.setWidth(field.getWidth());
		gridField.setLabel(field.getLabel());
		return gridField;
	}

}
