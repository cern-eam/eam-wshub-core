package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.tools.*;
import com.github.benmanes.caffeine.cache.Cache;
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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.decodeBoolean;

public class InforGrids implements Serializable {
	private static final long serialVersionUID = 5957161022766799698L;

	private final ApplicationData applicationData;
	private final Tools tools;
	private final InforWebServicesPT inforws;

	public InforGrids(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public GridRequestResult executeQuery(InforContext context, GridRequest gridRequest) throws InforException {
		String gridFieldCacheKey = Tools.getCacheKey(context, gridRequest.getRequestKey());
		Cache<String, Object> gridFieldCache = InforClient.cacheMap.get(CacheKey.GRID_FIELD);
		if (gridFieldCache == null) {
			return executeQueryHeaderData(context, gridRequest);
		}
		
		Map<BigInteger, GridField> gridFieldMap = (Map<BigInteger, GridField>) gridFieldCache.getIfPresent(gridFieldCacheKey);
		if (gridRequest.getIncludeMetadata() || gridFieldMap == null) {
			return executeQueryHeaderData(context, gridRequest);
		} else {
			return executeQueryDataOnly(context, gridRequest, gridFieldMap);
		}
	}

	private GridRequestResult executeQueryDataOnly(InforContext context, GridRequest gridRequest, Map<BigInteger, GridField> gridFieldMap) throws InforException {
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
		funRequest.setLOV(createLovParams(context, gridRequest));

		//
		// CALL THE WEB SERVICE
		//
		MP0116_GetGridDataOnly_001 getgridd = new MP0116_GetGridDataOnly_001();
		getgridd.setFUNCTION_REQUEST_INFO(funRequest);
		getgridd.getFUNCTION_REQUEST_INFO().getGRID().setLOCALIZE_RESULT(DataTypeTools.encodeBoolean(context.getLocalizeResults(), BooleanType.TRUE_FALSE));
		MP0116_GetGridDataOnly_001_Result result =
			tools.performInforOperation(context, inforws::getGridDataOnlyOp, getgridd);

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
				!result.getGRIDRESULT().getGRID().getDATA().getROW().isEmpty()) {

			LinkedList<GridRequestRow> rows = new LinkedList<>();
			for (net.datastream.schemas.mp_functions.mp0116_getgriddataonly_001_result.DATA.ROW inforRow : result.getGRIDRESULT().getGRID().getDATA().getROW()) {

				//
				List<GridRequestCell> cells = inforRow.getD().stream().map(inforCell -> {
					// If the current column is not in the grid cache (or there is no grid cache :-) ) extract only the ID and content from Infor grid cell
					if (gridFieldMap == null || gridFieldMap.get(inforCell.getN()) == null) {
						return new GridRequestCell(inforCell.getN().toString(), inforCell.getContent(), -99999, "UNKNOWN_TAGNAME");
					}
					// Extract the ID, content, order and the tag name from the grid cell
					GridField gridField = gridFieldMap.get(inforCell.getN());
					return new GridRequestCell(inforCell.getN().toString(),
							decodeCellContent(gridField, inforCell.getContent()),
							gridField.getOrder(),
							gridField.getName());

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

	private GridRequestResult executeQueryHeaderData(InforContext context, GridRequest gridRequest) throws InforException {
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
		funRequest.setLOV(createLovParams(context, gridRequest));

		//
		// CALL THE WEB SERVICE
		//
		MP0118_GetGridHeaderData_001 getgridd = new MP0118_GetGridHeaderData_001();
		getgridd.setFUNCTION_REQUEST_INFO(funRequest);
		getgridd.getFUNCTION_REQUEST_INFO().getGRID().setLOCALIZE_RESULT(DataTypeTools.encodeBoolean(context.getLocalizeResults(), BooleanType.TRUE_FALSE));
		MP0118_GetGridHeaderData_001_Result result =
			tools.performInforOperation(context, inforws::getGridHeaderDataOp, getgridd);

		GridRequestResult grr = new GridRequestResult();
		//
		// POPULATE GRID FIELD CACHE
		//
		Map<BigInteger, GridField> gridFieldMap = new ConcurrentHashMap<>();
		for (FIELD field : result.getGRIDRESULT().getGRID().getFIELDS().getFIELD()) {
			gridFieldMap.put(field.getAliasnum(), decodeInforGridField(field));
		}
		String gridFieldCacheKey = Tools.getCacheKeyWithLang(context, gridRequest.getRequestKey());
		Cache<String, Object> gridFieldCache = InforClient.cacheMap.get(CacheKey.GRID_FIELD);
		if (gridFieldCache != null) {
			gridFieldCache.put(gridFieldCacheKey, gridFieldMap);
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
				.map(this::decodeInforGridField)
				.collect(Collectors.toList()));

		//
		// RESULT DATA
		//
		if (result.getGRIDRESULT().getGRID().getDATA() != null &&
				result.getGRIDRESULT().getGRID().getDATA().getROW() != null &&
				!result.getGRIDRESULT().getGRID().getDATA().getROW().isEmpty()) {

			LinkedList<GridRequestRow> rows = new LinkedList<>();
			for (net.datastream.schemas.mp_functions.mp0118_getgridheaderdata_001_result.DATA.ROW inforRow : result.getGRIDRESULT().getGRID().getDATA().getROW()) {

				//
				List<GridRequestCell> cells = inforRow.getD().stream().map(inforCell ->
					 new GridRequestCell(inforCell.getN().toString(),
							 decodeCellContent(gridFieldMap.get(inforCell.getN()), inforCell.getContent()),
							 gridFieldMap.get(inforCell.getN()).getOrder(),
							 gridFieldMap.get(inforCell.getN()).getName())
				).collect(Collectors.toList());

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
			throw Tools.generateFault("Please supply grid name.");
		}

		// GRID ID
		if (gridRequest.getGridID() != null) {
			grid.setGRID_ID(new BigInteger(gridRequest.getGridID()));
		}

		// USER FUNCTION NAME
		if (DataTypeTools.isNotEmpty(gridRequest.getUserFunctionName())) {
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
		if (gridRequest.getGridRequestFilters() != null && !gridRequest.getGridRequestFilters().isEmpty()) {
			int counter = 0;
			for (GridRequestFilter filter: gridRequest.getGridRequestFilters()) {
				if ("ALT_IN".equals(filter.getOperator())) {
					counter = createInGridFilter(multiaddon_filters, filter, counter);
					continue;
				}
				if ("NOT_IN".equals(filter.getOperator())) {
					counter = createNotInGridFilter(multiaddon_filters, filter, counter);
					continue;
				}
				MADDON_FILTER inforFilter = new MADDON_FILTER();
				inforFilter.setSEQNUM(BigInteger.valueOf(counter++));
				if (filter.getJoiner() != null) {
					inforFilter.setJOINER(filter.getJoiner().getEamValue());
				}
				inforFilter.setOPERATOR(filter.getOperator());
				inforFilter.setALIAS_NAME(filter.getFieldName());
				getFilterValue(filter).ifPresent(inforFilter::setVALUE);
				inforFilter.setLPAREN(tools.getDataTypeTools().decodeBoolean(filter.getLeftParenthesis()));
				inforFilter.setRPAREN(tools.getDataTypeTools().decodeBoolean(filter.getRightParenthesis()));
				// Translate operators
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
					case "SELECTED":
						inforFilter.setOPERATOR("=");
						inforFilter.setVALUE("-1");
						break;
					case "NOT_SELECTED":
						inforFilter.setOPERATOR("=");
						inforFilter.setVALUE("0");
						break;
					case "":
						continue;
				}

				multiaddon_filters.getMADDON_FILTER().add(inforFilter);
			}
		}
		return multiaddon_filters;
	}

	private LOV createLovParams(InforContext inforContext, GridRequest gridRequest) {
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
		lov.getLOV_PARAMETERS().getLOV_PARAMETER().addAll(getDefaultLovParams(inforContext, gridRequest));
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

	private int createNotInGridFilter(MULTIADDON_FILTERS multiaddon_filters, GridRequestFilter gridRequestFilter, int counter) {
		if (Boolean.TRUE.equals(gridRequestFilter.getLeftParenthesis())) {
			counter = addDumbParenthesisFilter(multiaddon_filters, counter, gridRequestFilter, Operator.IS_EMPTY, GridRequestFilter.JOINER.OR);
		}
		counter = applyListOperator(multiaddon_filters, counter, gridRequestFilter, Operator.NOT_EQUAL, GridRequestFilter.JOINER.AND);
		if (Boolean.TRUE.equals(gridRequestFilter.getRightParenthesis())) {
			counter = addDumbParenthesisFilter(multiaddon_filters, counter, gridRequestFilter, Operator.IS_EMPTY);
		}
		return counter;
	}

	private int createInGridFilter(MULTIADDON_FILTERS multiaddon_filters, GridRequestFilter gridRequestFilter, int counter) {
		if (Boolean.TRUE.equals(gridRequestFilter.getLeftParenthesis())) {
			counter = addDumbParenthesisFilter(multiaddon_filters, counter, gridRequestFilter, Operator.NOT_EMPTY, GridRequestFilter.JOINER.AND);
		}
		counter = applyListOperator(multiaddon_filters, counter, gridRequestFilter, Operator.EQUALS, GridRequestFilter.JOINER.OR);
		if (Boolean.TRUE.equals(gridRequestFilter.getRightParenthesis())) {
			counter = addDumbParenthesisFilter(multiaddon_filters, counter, gridRequestFilter, Operator.NOT_EMPTY);
		}
		return counter;
	}

	private int addDumbParenthesisFilter(MULTIADDON_FILTERS multiaddon_filters, int counter, GridRequestFilter gridRequestFilter, Operator operator) {
		return addDumbParenthesisFilter(multiaddon_filters, counter, gridRequestFilter, operator, null);
	}

	private int addDumbParenthesisFilter(MULTIADDON_FILTERS multiaddon_filters, int counter, GridRequestFilter gridRequestFilter, Operator operator, GridRequestFilter.JOINER joiner) {
		MADDON_FILTER parenthesisFilter = new MADDON_FILTER();
		parenthesisFilter.setSEQNUM(BigInteger.valueOf(counter++));
		boolean isLeft = joiner != null;
		Optional.ofNullable(isLeft ? joiner : gridRequestFilter.getJoiner())
				.ifPresent(presentJoiner -> parenthesisFilter.setJOINER(presentJoiner.getEamValue()));
		parenthesisFilter.setOPERATOR(operator.getValue());
		parenthesisFilter.setALIAS_NAME(gridRequestFilter.getFieldName());
		if (isLeft) {
			parenthesisFilter.setLPAREN(tools.getDataTypeTools().decodeBoolean(Boolean.TRUE));
		} else {
			parenthesisFilter.setRPAREN(tools.getDataTypeTools().decodeBoolean(Boolean.TRUE));
		}
		multiaddon_filters.getMADDON_FILTER().add(parenthesisFilter);
		return counter;
	}

	private int applyListOperator(MULTIADDON_FILTERS multiaddon_filters, int counter, GridRequestFilter gridRequestFilter, Operator operator, GridRequestFilter.JOINER joiner) {
		AtomicInteger counterWrapper = new AtomicInteger(counter);
		getFilterValue(gridRequestFilter).ifPresent(values -> {
			String[] splitValues = values.split(",");
			for (int i = 0; i < splitValues.length; i++) {
				boolean isFirst = i == 0;
				boolean isLast = i == splitValues.length - 1;
				MADDON_FILTER filter = new MADDON_FILTER();
				filter.setSEQNUM(BigInteger.valueOf(counterWrapper.getAndIncrement()));
				filter.setOPERATOR(operator.getValue());
				filter.setALIAS_NAME(gridRequestFilter.getFieldName());
				filter.setVALUE(splitValues[i]);
				Optional.ofNullable(isLast ? gridRequestFilter.getJoiner() : joiner)
						.ifPresent(presentJoiner -> filter.setJOINER(presentJoiner.getEamValue()));
				if (isFirst) {
					filter.setLPAREN(tools.getDataTypeTools().decodeBoolean(Boolean.TRUE));
				}
				if (isLast) {
					filter.setRPAREN(tools.getDataTypeTools().decodeBoolean(Boolean.TRUE));
				}
				multiaddon_filters.getMADDON_FILTER().add(filter);
			}
		});
		return counterWrapper.get();
	}

	private Optional<String> getFilterValue(GridRequestFilter filter) {
		Optional<String> filterValue = Optional.empty();
		try {
			filterValue = Optional.ofNullable(applicationData.isEncodeGridFilter() && filter.getFieldValue() != null ?
                    java.net.URLEncoder.encode(filter.getFieldValue(), "UTF-8")
                    : filter.getFieldValue());
		} catch (UnsupportedEncodingException e) {
			tools.log(Level.WARNING, e.getMessage());
		}
		return filterValue;
	}

	private GridField decodeInforGridField(FIELD field) {
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

	private List<LOV_PARAMETER> getDefaultLovParams(InforContext inforContext, GridRequest gridRequest) {
		ArrayList<LOV_PARAMETER> lovParams = new ArrayList<>();
		if (gridRequest.getGridType() == GridRequest.GRIDTYPE.LOV) {
			LOV_PARAMETER controlOrgParam = new LOV_PARAMETER();
			controlOrgParam.setTYPE("VARCHAR");
			controlOrgParam.setALIAS_NAME("control.org");
			controlOrgParam.setVALUE(tools.getOrganizationCode(inforContext));
			lovParams.add(controlOrgParam);
		}
		return lovParams;
	}

}
