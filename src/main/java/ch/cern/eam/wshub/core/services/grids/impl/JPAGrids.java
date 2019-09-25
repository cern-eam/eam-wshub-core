package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.grids.customfields.GridCustomFieldHandler;
import ch.cern.eam.wshub.core.services.grids.exceptions.IncorrectParenthesesGridFilterException;
import ch.cern.eam.wshub.core.services.grids.exceptions.IncorrectSortTypeException;
import ch.cern.eam.wshub.core.services.grids.exceptions.MissingJoinerGridFilterException;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JPAGrids implements Serializable {

	private static final long serialVersionUID = -7463612088169746465L;

	private static String QUERY_TIMEOUT_DEFAULT_VALUE = "15000";

	public static Boolean USE_CUSTOM_FIELDS = false; //TODO Fix handling of Custom Fields 

	private InstallParametersManager paramManager;

	private GridCustomFieldHandler gridCustomFieldHandler;

	private ApplicationData applicationData;
	private Tools tools;
	private InforWebServicesPT inforws;

	public JPAGrids(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.paramManager = new InstallParametersManager(tools);
		this.gridCustomFieldHandler = new GridCustomFieldHandler(tools);
	}

	@SuppressWarnings("unchecked")
	public GridRequestResult executeQuery(InforContext context, GridRequest gridRequest)
			throws InforException {
		
		// get information of query timeout
		String queryTimeout = QUERY_TIMEOUT_DEFAULT_VALUE;
		if (gridRequest.getQueryTimeout()) {
			// if timeout is specified by requester
			if (gridRequest.getQueryTimeoutWaitingTime() != null) 
				queryTimeout = gridRequest.getQueryTimeoutWaitingTime().toString();
			else { // else if timeout is specified in config.properties
				Object queryTimeoutO = applicationData.getQueryTimeout();
				if (queryTimeoutO != null)
					queryTimeout = queryTimeout.toString();
			}
		}

		// GRID ID, GRID NAME
		BigInteger gridID;
		String gridName;
		BigInteger dataSpyID;
		if (gridRequest.getGridID() != null && gridRequest.getGridName() != null
				&& gridRequest.getDataspyID() != null) {
			gridID = new BigInteger(gridRequest.getGridID().trim());
			gridName = gridRequest.getGridName();
			dataSpyID = new BigInteger(gridRequest.getDataspyID());
		} else {
			throw tools.generateFault("Please supply grid name and grid id and dataSpy id.");
		}

		Integer rowCount;
		Integer cursorPosition;
		if (gridRequest.getRowCount() != null && gridRequest.getCursorPosition() != null) {
			rowCount = new Integer(gridRequest.getRowCount());
			if (rowCount <= 0)
				throw tools.generateFault("Please supply row count > 0");
			cursorPosition = new Integer(gridRequest.getCursorPosition()) - 1;
			if (cursorPosition < 0)
				throw tools.generateFault("Please supply cursor position >= 1");
		} else {
			throw tools.generateFault("Please supply row count and cursor position.");
		}

		// Fetch grid data
		EntityManager em = tools.getEntityManager();
		try {

			//
			// Obtaining information on tagNames for FILTER and SORTING
			//
			Map<String, DataField> tagNames = new HashMap<>();
			Object userfunctionValue = gridRequest.getParams().get("userfunction");
			List<Object[]> selectFields = em.createNamedQuery(GridDataspy.GETGRIDFIELDSANDSTATEMENT)
					.setParameter("gridID", gridID)
					.setParameter("userfunction", userfunctionValue == null ? gridName
							: userfunctionValue.toString())
					.getResultList();
			if (selectFields == null)
				throw tools.generateFault(
						"Couldn't fetch data for this grid. No select fields found. Verify Grid Type or other parameters.");
			for (Object[] o : selectFields) {
				String tagName = ((String) o[0]).trim();
				String selectColumn = ((String) o[1]).trim();
				String tagDataType =  ((String) o[2]).trim();
				String uppercase = ((String) o[3]) != null ? ((String) o[3]).trim() : "";
				selectColumn = replaceParamPrefix(selectColumn);

				Boolean isUppercase = ("uppercase".equals(uppercase)) ? true : false;  
				DataField dataField = new DataField(tagName, selectColumn, DataType.valueOf(tagDataType), isUppercase);
				tagNames.put(tagName, dataField);
			}

			if (USE_CUSTOM_FIELDS) {
				tagNames.putAll(gridCustomFieldHandler.getCustomFieldsForGrid(gridRequest.getGridID()));
			}

			//
			// Get Query SQL STATEMENT
			//
			List<String> sqlStatements = em.createNamedQuery(GridDataspy.GETGRIDDATASPYSELECTSTATEMENT)
					.setParameter("requestType", gridRequest.getGridType().toString()).setParameter("gridID", gridID)
					.setParameter("dataSpyID", dataSpyID).getResultList();

			if (USE_CUSTOM_FIELDS) {
				gridCustomFieldHandler.initializeForDataspy(gridRequest.getDataspyID());
				sqlStatements = gridCustomFieldHandler.attachCustomFields(sqlStatements);
			}

			String sqlStatementSelect = "select " + sqlStatements.stream().collect(Collectors.joining(","));

			sqlStatementSelect = replaceParamPrefix(sqlStatementSelect);

			String sqlStatementFW = (String) em.createNamedQuery(GridDataspy.GETGRIDDATASPYQUERY)
					.setParameter("gridID", gridID).setParameter("dataSpyID", dataSpyID).getSingleResult();

			sqlStatementFW = replaceParamPrefix(sqlStatementFW);

			int i = sqlStatementFW.toLowerCase().indexOf("where");
			if (i == -1)
				i = sqlStatementFW.length();
			String[] sqlStatementO = { sqlStatementFW.substring(0, i), sqlStatementFW.substring(i) };

			String sqlStatementFrom = sqlStatementO[0];
			if (USE_CUSTOM_FIELDS) {
				sqlStatementFrom = gridCustomFieldHandler.addCustomFieldsJoinClause(sqlStatementFrom);
			}
			String sqlStatementWhere = sqlStatementO[1];

			i = sqlStatementWhere.toLowerCase().lastIndexOf("order by");
			if (i == -1)
				i = sqlStatementWhere.length();
			String[] sqlStatementOR = { sqlStatementWhere.substring(0, i), sqlStatementWhere.substring(i) };

			sqlStatementWhere = sqlStatementOR[0];
			String sqlStatementOrderBy = "";
			if (sqlStatementOR.length > 1 && !sqlStatementOR[1].isEmpty()) {
				sqlStatementOrderBy = sqlStatementOR[1];
			}
	
			//
			// FILTERS and SORT from Dataspy
			// Obtain list of filters and sorting layers
			//

			// Remove "param." prefix from the parameter names
			Map<String, Object> params = new HashMap<>();
			gridRequest.getParams().forEach((key, value) -> {
				if (key.startsWith("param.")) {
					params.put(key.substring(6), value);
				} else {
					params.put(key, value);
				}
			});

			List<Object[]> gridListOfSortAndFilters = em.createNamedQuery(GridDataspy.GETGRIDDDSFILTER_AND_SORT_STRXML)
					.setParameter("dataspyid", dataSpyID)
					.setParameter("userid", context.getCredentials().getUsername())
					.setParameter("userfunction", 
							userfunctionValue == null ? gridName : userfunctionValue.toString())
					.getResultList();

			if (gridListOfSortAndFilters == null)
				throw tools.generateFault("Couldn't fetch data for this grid. No filter or sorting parameters found.");

			ArrayList<GridRequestFilter> dataspyFiltersArr = new ArrayList<GridRequestFilter>();
			ArrayList<GridRequestSort> dataspySortArr = new ArrayList<GridRequestSort>();

			for (Object[] o : gridListOfSortAndFilters) {
				//Load filter
				if(o[0] != null) {
					String filterResult = ((String) o[0]).trim();
	
					Document doc = loadXMLFromString(filterResult);
					doc.getDocumentElement().normalize();
	
					NodeList nl = doc.getElementsByTagName("FILTER_ELEMENT");
					for (int n = 0; n < nl.getLength(); ++n) {
						Node nNode = nl.item(n);
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element el = (Element) nNode;

							String joiner = el.getAttribute("JOINER");
							GridRequestFilter.JOINER gridJoiner = GridRequestFilter.JOINER.OR;
							if (joiner != null && joiner.equals("AND")) {
								gridJoiner = GridRequestFilter.JOINER.AND;
							}

							GridRequestFilter filter = new GridRequestFilter(el.getAttribute("ALIAS_NAME"),
									el.getAttribute("VALUE"), el.getAttribute("OPERATOR"),
									gridJoiner, "true".equalsIgnoreCase(el.getAttribute("LPAREN")),
                                    "true".equalsIgnoreCase(el.getAttribute("RPAREN")));
							//skip custom fields
							if(tagNames.get(filter.getFieldName()) != null){
								dataspyFiltersArr.add(filter);
							}
						}
					}
				}
				
				//
				// FILTERS from Dataspy
				//
				if (!dataspyFiltersArr.isEmpty()) {
					String filterString = "";
					filterString = createFilterSQLStatement(
							dataspyFiltersArr, tagNames, params,
							"D");
					if(!filterString.isEmpty()){
						sqlStatementWhere = sqlStatementWhere + " AND (" + filterString + ")";
					}
					dataspyFiltersArr = new ArrayList<GridRequestFilter>();
				}

				//Load sort from user
				if(gridRequest.getGridRequestSorts() != null){
					dataspySortArr.addAll(new ArrayList<GridRequestSort>(Arrays.asList(gridRequest.getGridRequestSorts())));
				} else { 
					// if user did not provide any sort info then check if DataSpy has defined sort info
					if(o[1] != null) {
						String sortResult = ((String) o[1]).trim();
						
						Document doc = loadXMLFromString(sortResult);
						doc.getDocumentElement().normalize();
						
						NodeList nl = doc.getElementsByTagName("SORT_ELEMENT");
						for(int n = 0; n < nl.getLength(); ++n) {
							Node nNode = nl.item(n);
							if(nNode.getNodeType() == Node.ELEMENT_NODE){
								Element el = (Element) nNode;
								GridRequestSort sort = new GridRequestSort(el.getAttribute("ALIAS_NAME"),
										el.getAttribute("TYPE"));
								dataspySortArr.add(sort);			
							}
						}
					}
				}
				//remove duplicates
				removeDuplicates(dataspySortArr);
			}

			// add optional custom field join clauses
			if (USE_CUSTOM_FIELDS) {
				sqlStatementWhere += gridCustomFieldHandler.getCustomFieldJoinConditions();
			}
			
			//
			// FILTERS from the User
			//
			if (gridRequest.getGridRequestFilters() != null && gridRequest.getGridRequestFilters().size() > 0) {
				String filterString = "";
				filterString = createFilterSQLStatement(gridRequest.getGridRequestFilters(), tagNames, params, "U");
				if(!filterString.isEmpty()){
					sqlStatementWhere = sqlStatementWhere + " AND (" + filterString + ")";
				}
			}
			
			//
			// Manual Department Security to be added for GRIDS
			//
			if (gridRequest.getDepartmentSecurityGridColumn() != null) {
				String deptSecCol = gridRequest.getDepartmentSecurityGridColumn();

				if (!sqlStatementWhere.isEmpty()) {
					sqlStatementWhere += " AND ";
				}

				sqlStatementWhere += "(:deptsec = 'OFF' OR EXISTS (SELECT 1 FROM r5departmentsecurity WHERE dse_user =:r5user AND dse_mrc = "
						+ deptSecCol + "))";
			}
			
			sqlStatementWhere = replaceParamPrefix(sqlStatementWhere);
			
			//TODO EAMWSHUB-29 - Check uppercasing?
			//Apply uppercase to the values for all parameters
			// TODO Here the code is making the content of List uppercase, not sure if we have to leave
			// the code for single fields
			for(Map.Entry<String, Object> entry : params.entrySet()){
				String keyWithoutSuffix = entry.getKey().replaceAll("(U|D)\\d*$", "");
				if(entry.getValue() instanceof List){ // when list
					List<String> nList = ((List)entry.getValue());
					nList.replaceAll(String::toUpperCase);
				}
//				else if(tagNames.get(keyWithoutSuffix) != null && (tagNames.get(keyWithoutSuffix).isUppercase())){
//					if(entry.getValue() != null){
//						entry.setValue(entry.getValue().toString().toUpperCase());
//					} 
//				}
				
			}

			//
			// SORT from the User and Dataspy
			//

			for(GridRequestSort sort : dataspySortArr){
				String so = Arrays.asList("DESC", "ASC").stream()
						.filter(str -> sort.getSortType() != null && str.equals(sort.getSortType().toUpperCase()))
						.findFirst()
						.orElseThrow(IncorrectSortTypeException::new);


				//skip custom fields
				if(tagNames.get(sort.getSortBy()) != null){
					if(sqlStatementOrderBy.length() > 0){
						sqlStatementOrderBy += "," + tagNames.get(sort.getSortBy()).getSourcename() + " " + so;
					}
					else{
						sqlStatementOrderBy = " order by " + tagNames.get(sort.getSortBy()).getSourcename() + " " + so;
					}
				}
			}

			GridRequestResult grr = new GridRequestResult();
			Number records = 0;
			
			// To check wether fetching all possible results
			// if true we skip counting
			if(!gridRequest.getFetchAllResults()){
				Integer wsgridsz = Integer.parseInt(paramManager.getParams().get("WSGRIDSZ")); 
				if(rowCount > wsgridsz) {
					//TODO DEBUG
					rowCount = wsgridsz;
				}
				
				//
				// TOTAL COUNT INFO
				//
				Query q;
				Date d1, d2;
				if (gridRequest.getCountTotal()) {
					Integer maxRecord = (((cursorPosition + rowCount + 1) / wsgridsz + 1) * wsgridsz);
					String sqlStatement = "select count(1) " + sqlStatementFrom + sqlStatementWhere + " and rownum <= "
							+ (maxRecord + 1);
					sqlStatement = filterOutNULLValues(sqlStatement, params);
					q = em.createNativeQuery(sqlStatement);
					defineParameters(gridName, sqlStatement, q, context.getCredentials(), params, gridRequest.getLang());
					d1 = new Date();
					records = (Number) q.getSingleResult();
					d2 = new Date();
					tools.log(Level.FINE,
							"TOTAL COUNT INFO in " + (d2.getTime() - d1.getTime()) + ":\n" + sqlStatement + "\n" + params); 
					if (records.longValue() > maxRecord) {
						records = maxRecord;
						grr.setRecords(records.toString() + "+");
					} else {
						grr.setRecords(records.toString());
					}
				}
			}

			//
			// RESULT DATA
			//
			String rownumString = "";
			if (gridRequest.getCountTotal()) {
				rownumString = ", rownum rn";
			}
		
			if (sqlStatementOrderBy.length() > 0)
				rownumString = ", row_number() over (" + sqlStatementOrderBy + ") rn";
			String sqlStatement = sqlStatementSelect + rownumString + " " + sqlStatementFrom + sqlStatementWhere
					+ sqlStatementOrderBy;

			List<Object[]> qResult;
			
			Date d1, d2;
			Query q;
			
			JPATypes jpaType = JPATypes.JPA_FINAL;
			if (gridRequest.getJPAType() != null)
				jpaType = JPATypes.valueOf(gridRequest.getJPAType());
			switch (jpaType) {
			case JPA_FINAL:
			default:
				String paginationQueryBeg = "";
				String paginationQueryEnd = "";
				// To check wether fetching all possible results
				//if true skip limiting results
				if(!gridRequest.getFetchAllResults()){
					paginationQueryBeg = "SELECT * FROM ( SELECT a.*, ROWNUM rnum FROM ( ";
					paginationQueryEnd = " ) a WHERE ROWNUM <= :MAX_ROW ) WHERE rnum > :MIN_ROW";
					params.put("MIN_ROW", cursorPosition);
					int maxRow = gridRequest.getCountTotal() ? (cursorPosition + rowCount) : (cursorPosition + rowCount + 1);
					params.put("MAX_ROW", maxRow);
				}
				sqlStatement = paginationQueryBeg + sqlStatement + paginationQueryEnd;
				sqlStatement = filterOutNULLValues(sqlStatement, params);
				q = em.createNativeQuery(sqlStatement);
				if(gridRequest.getQueryTimeout()){
					q.setHint("javax.persistence.query.timeout", queryTimeout.toString()); // only works for higher values than 500
				}
				defineParameters(gridName, sqlStatement, q, context.getCredentials(), params, gridRequest.getLang());
				d1 = new Date();
				tools.log(Level.FINE, "EXECUTING STATEMENT:\n" + sqlStatement + "\n" + params + "\n");
				qResult = q.getResultList();
				d2 = new Date();
				tools.log(Level.FINE, "RESULT DATA in " + (d2.getTime() - d1.getTime()));
				break;
			}
			
			if (!gridRequest.getCountTotal()) { // No counting all results
				records = qResult.size();
				if (!gridRequest.getFetchAllResults() && records.longValue() > rowCount) { // When more results than rowCount
					qResult = qResult.subList(0, qResult.size() - 1);
					grr.setMoreRowsPresent("TRUE");
					Number totalCount = records.longValue() + cursorPosition - 1;
					grr.setRecords(totalCount.toString() + "+");
				} else {
					grr.setMoreRowsPresent("FALSE");
					Number totalCount = (records.longValue() == 0) ? 0 : (records.longValue() + cursorPosition);
					grr.setRecords(totalCount.toString());
				}
			}
			else{
				Number totalNumberOfRecords = (Number) records.longValue();
				Number indexOfLastRecord = 0;
				if(qResult.size()>0)
					indexOfLastRecord = (Number) qResult.get(qResult.size()-1)[qResult.get(qResult.size()-1).length-1];
				if (totalNumberOfRecords.longValue() == indexOfLastRecord.longValue()) {
					grr.setMoreRowsPresent("FALSE");
				} else {
					grr.setMoreRowsPresent("TRUE");
				}
			}

			List<DataspyField> fields = em.createNamedQuery(DataspyField.GETDATASPYFIELDS, DataspyField.class)
					.setParameter("requestType", gridRequest.getGridType().toString())
					.setParameter("dataspyid", gridRequest.getDataspyID())
					.getResultList();

			Comparator<DataspyField> comparator = Comparator.comparingInt(df -> df.getOrder());
			comparator = comparator.thenComparing(Comparator.comparing(df -> Integer.valueOf(df.getId())));

			fields = fields.stream().sorted(comparator).collect(Collectors.toList());
			if (USE_CUSTOM_FIELDS) {
				fields.addAll(gridCustomFieldHandler.getCustomFieldsAsDataspyFields());
			}

			if (qResult != null && qResult.size() > 0) {

				LinkedList<GridRequestRow> rows = new LinkedList<GridRequestRow>();
				Integer id = 1;
				for (Object[] a : qResult) {

					List<GridRequestCell> cells = new ArrayList<GridRequestCell>();
					int count = 0;
					for (DataspyField dsf : fields) {
						String content = JPAFieldDataConverter.getAsString(dsf, a[count]);
						cells.add(new GridRequestCell(dsf.getId(), content, dsf.getOrder(), dsf.getTagName()));
						count++;
					}

					// Sort using the order
					cells = cells.stream().sorted((e1, e2) -> Integer.compare(e1.getOrder(), e2.getOrder()))
							.collect(Collectors.toList());

					GridRequestRow row = new GridRequestRow();

					Object indexO = a[a.length - 1];
					String index = indexO == null ? "NULL" : indexO.toString();
					try {
						Integer.parseInt(index);
						row.setId(index);
					} catch (NumberFormatException ex) {
						row.setId((id).toString());
					}
					row.setCells(cells.toArray(new GridRequestCell[0]));
					rows.add(row);

					id++;
				}
				grr.setRows(rows.toArray(new GridRequestRow[0]));
			} else {
				grr.setRows(new GridRequestRow[0]);
			}
			return grr;

		} catch (QueryTimeoutException e) {
			tools.log(Level.SEVERE, e.getMessage());
			throw tools.generateFault("Current database operation timeout. Please try again.");
		} catch (MissingJoinerGridFilterException e) {
			throw tools.generateFault("Couldn't fetch data for this grid. Missing joiner in grid filter.");
		} catch (IncorrectParenthesesGridFilterException e){
			throw tools.generateFault("Couldn't fetch data for this grid. Incorrect parentheses in grid filter.");
		} catch (IncorrectSortTypeException e){
			throw tools.generateFault("Couldn't fetch data for this grid. Incorrect sort type defined in gridSort.");
		}
		catch (Exception e) {
			e.printStackTrace();
			tools.log(Level.SEVERE,"Error whlie executing Grid Query");
			throw tools.generateFault("Couldn't fetch data for this grid.");
		} finally {
			em.clear();
			em.close();
		}

	}

	/**
	 * @param inputString
	 * @return
	 */
	private String replaceParamPrefix(String inputString) {
		inputString = inputString.replaceAll(":parameter.", ":"); // TODO
		inputString = inputString.replaceAll(":param.", ":"); // TODO
		return inputString;
	}
	
	private String filterOutNULLValues(String sqlStatement, Map<String, Object> params) {
		// ad parameters set while adding user filters
		for(String p : params.keySet()) {
			if(sqlStatement.contains(":"+p)) {
				Object value = params.get(p);
				if(value == null) {
					sqlStatement = sqlStatement.replaceAll(":"+p, "NULL");
				}  
			}
		}
		return sqlStatement;
	}

	private void defineParameters(String gridName, String sqlStatement, Query q, Credentials credentials,
			Map<String, Object> params, String lang) {
		HashMap<String, String> installParams = paramManager.getParams();
		
		if (sqlStatement.contains(":r5user"))
			q.setParameter("r5user", credentials.getUsername().toUpperCase());
		if (sqlStatement.contains(":MP5USER"))
			q.setParameter("MP5USER", credentials.getUsername().toUpperCase());
		if (sqlStatement.contains(":deptsec"))
			q.setParameter("deptsec", installParams.get("DEPTSEC"));
		if (sqlStatement.contains(":storesec"))
			q.setParameter("storesec", installParams.get("STORESEC"));
		if (sqlStatement.contains(":language"))
			q.setParameter("language", lang);
		if (sqlStatement.contains(":userfunction") && params.get("userfunction") == null)
			q.setParameter("userfunction", gridName);
		if (sqlStatement.contains(":syskit2"))
			q.setParameter("syskit2", installParams.get("SYSKIT2"));
		if (sqlStatement.contains(":syskit1"))
			q.setParameter("syskit1", installParams.get("SYSKIT1"));
		if (sqlStatement.contains(":filternonconformity"))
			q.setParameter("filternonconformity", "false");
		
		// TODO - to test if we need the params
		if (sqlStatement.contains(":bypassstatuscheck"))
			q.setParameter("bypassstatuscheck", null);
		if (sqlStatement.contains(":bypassdeptsecurity"))
			q.setParameter("bypassdeptsecurity", null);
		if (sqlStatement.contains(":excludeparentpart"))
			q.setParameter("excludeparentpart", "true");

		// add parameters set while adding user filters
		for (String p : params.keySet()) {
			if(q.getParameters().stream().filter(par -> par.getName().equals(p)).collect(Collectors.toList()).size()>0)
				if(params.get(p)!=null && params.get(p) instanceof List && ((List)params.get(p)).size()==0)
					q.setParameter(p, "");
				else
					q.setParameter(p, params.get(p));
		}
	}

	private static Document loadXMLFromString(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);
	}

	/**
	 * Creates sql statement for filtering data in grid dataspy
	 * @param filters Array with all the filters defined either in dataspy editor or by user
	 * @param tagNames Map with all the data fields for a given grid
	 * @param params Global parameters use in SQL query as inputs
	 * @param filterType String that defines whether we retrieve filters from dataspy or ones created by the user
	 * @return SQL statement for the filters
	 * @throws MissingJoinerGridFilterException When the joiner is missing for not last grid filter.
	 */
	private String createFilterSQLStatement(List<GridRequestFilter> filters, Map<String, DataField> tagNames,
			Map<String, Object> params, String filterType) throws Exception  {
		String filterString = "";
		int count = 0;
		int filtersLengthMinusOne = filters.size() - 1;
		for (GridRequestFilter filter : filters) {
			//skip custom fields
			if(tagNames.get(filter.getFieldName()) != null){
				
				if ("true".equals(filter.getLeftParenthesis()))
					filterString += "(";

				String p = "";
				// Replace parameter name with global or local
				if(filter.getFieldValue()!=null && filter.getFieldValue().startsWith(":")){
					p = filter.getFieldValue();
					p = p.substring(1,p.length()); //remove colon
				}
				else{
					p = filter.getFieldName() + filterType + count;
				}
				
				Operator op = Operator.fromString(filter.getOperator().toUpperCase());
				
				DataType fieldDataType = tagNames.get(filter.getFieldName()).getDatatype();
				
				switch(fieldDataType){
				case DATE:
				case DATETIME:
					filterString += buildSQLStatementForDateTypeOfValue(op, filter, tagNames, p, params);
					break;
				case DECIMAL:
				case NUMBER:
					filterString += buildSQLStatementForDecimalTypeOfValue(op, filter, tagNames, p, params);
					break;
				case CHKBOOLEAN:
					String sourcename = tagNames.get(filter.getFieldName()).getSourcename();
					filterString += sourcename + " = :" + p;					
					switch(op) {
						case SELECTED:
							params.put(p, "+");
							break;
						case NOT_SELECTED:
							params.put(p, "-");
							break;
						default:
							params.put(p, "false".equals(filter.getFieldValue())?"-":"+");
					}					
					break;
				default:
					String sourcename1 = ( isCaseInsensitive(tagNames, filter) ) 
										 ? "UPPER(" + tagNames.get(filter.getFieldName()).getSourcename() + ")"
										   : tagNames.get(filter.getFieldName()).getSourcename();
					filterString += buildSQLStatementForOtherTypeOfValue(op, filter, tagNames, p, params, sourcename1);
					break;
				}
	
				if ("true".equals(filter.getRightParenthesis()))
					filterString += ")";
	
				// JOINER
				if (filter.getJoiner() ==  GridRequestFilter.JOINER.AND) {
					filterString += " AND ";
				}
				if (filter.getJoiner() == GridRequestFilter.JOINER.OR) {
					filterString += " OR ";
				}
				
				//Check missing joiner
				if(filter.getJoiner() == null && count < filtersLengthMinusOne ){
					throw new MissingJoinerGridFilterException();
				}
				
				++count;
			}
		}
		
		//check parenthesis correctness for filters defined by the user
		if(filterType == "U" && !isParenthesisSyntacticallyCorrect(filters)){
			throw new IncorrectParenthesesGridFilterException();
		}
		// Remove last AND or OR operator that is useless
		if(filterString.length() >= 5 && filterString.subSequence((filterString.length() - 5), (filterString.length() )).equals(" AND ")){
			filterString = filterString.subSequence(0, (filterString.length() - 5)).toString();
		}
		else if(filterString.length() >= 4 && filterString.subSequence((filterString.length() - 4), (filterString.length() )).equals(" OR ")){
			filterString = filterString.subSequence(0, (filterString.length() - 4)).toString();
		}
		
		return filterString;
	}
	
	/**
	 * Check whether parentheses are syntactically correct for filters defined by the user.
	 * @param filters
	 * @return
	 */
	private boolean isParenthesisSyntacticallyCorrect(List<GridRequestFilter> filters){
		int parenthesisCounter = 0;
		for (GridRequestFilter filter : filters){
			if(filter.getLeftParenthesis() != null && filter.getLeftParenthesis().equals("true"))
				++parenthesisCounter;
			if(filter.getRightParenthesis() != null && filter.getRightParenthesis().equals("true"))
				--parenthesisCounter;
			
			if(parenthesisCounter < 0) return false;
		}
		return (parenthesisCounter == 0) ? true : false;
	}
	
	private String buildSQLStatementForDateTypeOfValue(Operator op, GridRequestFilter filter,
			Map<String, DataField> tagNames, String p, Map<String, Object> params){
		String sourcename = tagNames.get(filter.getFieldName()).getSourcename();

		String dateFormat;
		String dateDBFormat;
		String parameter;
		String dateWithOneMoreDay;
		switch(tagNames.get(filter.getFieldName()).getDatatype()){
			case DATETIME:
				dateFormat = applicationData.getDateTimeFormat();
				dateDBFormat = applicationData.getDateTimeDBFormat();
				parameter = formatDateToProperPattern(filter, dateFormat, dateDBFormat);
				dateWithOneMoreDay = dateIncrementOfOneDay(filter, dateFormat, dateDBFormat);
				dateDBFormat = dateDBFormat.replaceAll("HH:mm", "HH24:mi");
				break;
			default: 
				dateFormat = applicationData.getDateFormat();
				dateDBFormat = applicationData.getDateDBFormat();
				parameter = formatDateToProperPattern(filter, dateFormat, dateDBFormat);
				dateWithOneMoreDay = dateIncrementOfOneDay(filter, dateFormat, dateDBFormat);
				break;
		}
		
		dateFormat = ",'" + dateDBFormat + "')";
		String filterString = "";
		
		switch (op) {
		case LESS_THAN:
			filterString += sourcename + " < to_date(:" + p + dateFormat; 
			break;
		case GREATER_THAN:
			filterString += sourcename + " > to_date(:" + p + dateFormat;
			break;
		case LESS_THAN_EQUALS:
			filterString += sourcename + " <= to_date(:" + p + dateFormat;
			break;
		case GREATER_THAN_EQUALS:
			filterString += sourcename + " >= to_date(:" + p + dateFormat;
			break;
		case EQUALS:
			filterString += sourcename + " >= " + "to_date(:" + p + dateFormat + " AND "  
						 + sourcename + " < to_date('" + dateWithOneMoreDay + "'" + dateFormat;
			break;
		case NOT_EQUAL:
			filterString += "nvl(trunc(" + sourcename + ",'DD'),to_date('" + dateWithOneMoreDay
						 + "'" + dateFormat + ") != to_date(:" + p + dateFormat; 
			break;
		case IS_EMPTY:
			filterString += sourcename + " IS NULL";
			break;
		case NOT_EMPTY:
			filterString += sourcename + " IS NOT NULL";
			break;
		default:
			filterString += sourcename + " " + filter.getOperator() + " :" + p;
			break;
		}
		params.put(p, parameter);
		
		return filterString;
	}

	/**
	 * @param filter
	 * @param dateInputFormat
	 * @param dateOutputFormat
	 * @return Date in proper format
	 */
	private String formatDateToProperPattern(GridRequestFilter filter, String dateInputFormat, String dateOutputFormat) {
		//format date
		Date date = null;
		try {
			date = new SimpleDateFormat(dateInputFormat, Locale.ENGLISH).parse(filter.getFieldValue().toString());
			return new SimpleDateFormat(dateOutputFormat, Locale.ENGLISH).format(date);
		} catch (ParseException e) {
			tools.log(Level.SEVERE, "Couldn't retrieve given date type");
			tools.log(Level.SEVERE,"Error in formatDateToProperPattern dateInputFormat=" + dateInputFormat + " dateOutputFormat=" + dateOutputFormat);
		}
		return filter.getFieldValue().toString();
	}
	
	private String buildSQLStatementForDecimalTypeOfValue(Operator op, GridRequestFilter filter,
			Map<String, DataField> tagNames, String p, Map<String, Object> params){
		Object parameter = filter.getFieldValue();
		String sourcename = tagNames.get(filter.getFieldName()).getSourcename();
		String filterString = "";
		
		switch (op) {
		case EQUALS:
			filterString += sourcename + " = :" + p; 
			break;
		case LESS_THAN:
			filterString += sourcename + " < :" + p;
			break;
		case GREATER_THAN:
			filterString += sourcename + " > :" + p;
			break;
		case LESS_THAN_EQUALS:
			filterString += sourcename + " <= :" + p;
			break;
		case GREATER_THAN_EQUALS:
			filterString += sourcename + " >= :" + p;
			break;
		case IS_EMPTY:
			filterString += sourcename + " IS NULL"; 
			break;
		case NOT_EMPTY:
			filterString += sourcename + " IS NOT NULL";
			break;
		case NOT_EQUAL:
			filterString += "nvl(" + sourcename + ","
					     + Float.toString((Float.parseFloat(filter.getFieldValue()) + 1))
					     + ") != :" + p;
			break;
		case IN:
			StringTokenizer inVars = new StringTokenizer(filter.getFieldValue().trim(), ",");
			List<String> vars = new ArrayList<>();
			while (inVars.hasMoreElements()) {
				String sv = (String) inVars.nextElement();
				vars.add(sv.substring(1, sv.length() - 1));
			}
			parameter = vars;
			filterString += sourcename + " " + filter.getOperator() + " (:" + p + ")";
			break;
		default:
			filterString += sourcename + " " + filter.getOperator() + " :" + p;
			break;
		}
		params.put(p, parameter);
		
		return filterString;
	}

	private String dateIncrementOfOneDay(GridRequestFilter filter, String dateInputFormat, String dateOutputFormat) {
		Date dt = null;
		String dateWithOneMoreDay = "";
		String dateValue = filter.getFieldValue();
		try {
			dt = new SimpleDateFormat(dateInputFormat, Locale.ENGLISH).parse(dateValue);
			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			c.add(Calendar.DATE, 1);
			dt = c.getTime();
			dateWithOneMoreDay = new SimpleDateFormat(dateOutputFormat, Locale.ENGLISH).format(dt);
			
		} catch (ParseException e) {
			tools.log(Level.SEVERE, "Couldn't retrieve given date type");
			tools.log(Level.SEVERE,"Error in dateIncrementOfOneDay dateInputFormat=" + dateInputFormat + " dateOutputFormat=" + dateOutputFormat);
		}
		return dateWithOneMoreDay;
	}
	
	private String buildSQLStatementForOtherTypeOfValue(Operator op, GridRequestFilter filter,
			Map<String, DataField> tagNames, String p, Map<String, Object> params, String sourcename){
		Object parameter = filter.getFieldValue();
		
		if(parameter != null && parameter instanceof String)
			parameter = ((String) parameter).trim();

		String filterString = "";
		String p1 = (isUppercase(tagNames, filter) || isCaseInsensitive(tagNames, filter)) ? " UPPER(:" + p + ")" : " :" + p;  
		
		switch (op) {
		case IN:
			StringTokenizer inVars = new StringTokenizer(filter.getFieldValue().trim(), ",");
			List<String> vars = new ArrayList<>();
			while (inVars.hasMoreElements()) {
				String sv = (String) inVars.nextElement();
				if(sv.length()>1 && sv.charAt(0)=='\'')
					vars.add(sv.substring(1, sv.length() - 1));
				else
					vars.add(sv);
			}
			parameter = vars;
			filterString += sourcename + " " + filter.getOperator() + " (:" + p + ")";
			break;
		case CONTAINS:
			parameter = "%" + escapeCharacters(parameter) + "%";
			filterString += sourcename + " LIKE" + p1 + buildLIKEEscapeStatement();
			break;
		case NOT_CONTAINS:
			parameter = "%" + escapeCharacters(parameter) + "%";
			filterString += "( " + sourcename + " NOT LIKE" + p1 + buildLIKEEscapeStatement() + " OR "
					+ sourcename + " IS NULL" + " )";
			break;
		case BEGINS:
			parameter = escapeCharacters(parameter) + "%";
			filterString += sourcename + " LIKE" + p1 + buildLIKEEscapeStatement();
			break;
		case ENDS:
			parameter = "%" + escapeCharacters(parameter);
			filterString += sourcename + " LIKE" + p1 + buildLIKEEscapeStatement();
			break;
		case EQUALS:
			filterString += sourcename + " = " + p1;
			break;
		case NOT_EQUAL:
			filterString += "nvl(" + sourcename + "," + p1 + ") !=" + p1;
			break;
		case IS_EMPTY:
			filterString += sourcename + " IS NULL";
			break;
		case NOT_EMPTY:
			filterString += sourcename + " IS NOT NULL";
			break;
		default:
			if("LIKE".equals(filter.getOperator().toUpperCase()))				
				filterString += sourcename + " " + filter.getOperator() + p1 + buildLIKEEscapeStatement();
			else
				filterString += sourcename + " " + filter.getOperator() + p1;
			break;
		}
		params.put(p, parameter);
		
		return filterString;
	}
	
	private String escapeCharacters(Object i) {
		if(i == null)
			return null;
		// We do not escape % as it is useful to have it as a wildcard
		return i.toString().replaceAll("_", "|_"); //.replaceAll("%", "|%");
	}
	
	private String buildLIKEEscapeStatement() {
		return " ESCAPE '|' ";
	}
	
	/**
	 * Removes duplicates from the list of sorts.
	 * @param list
	 * @return Number of removed duplicates.
	 */
	public int removeDuplicates(ArrayList<GridRequestSort> list){
		int size = list.size();
		int dups = 0;
		
		for(int i=0; i<size-1; ++i){
			for(int j=i+1; j<size; ++j){
				if(list.get(i).getSortBy().equals(list.get(j).getSortBy())){
					++dups;
					list.remove(j);
					--j; //array got re-indexed
					--size; //decrease size of the array
				}
			}
		}
		return dups;
	}

	/*
	 * utils method
	 */

	private boolean isUppercase(Map<String, DataField> tagNames, GridRequestFilter filter) {
		return filter.getUpperCase() || tagNames.get(filter.getFieldName()).isUppercase();
	}
	
	/**
	 * 
	 * @param tagNames
	 * @param filter
	 * @return true is it has to be considered as case insensitive UPPER() = UPPER (:param)
	 */
	private boolean isCaseInsensitive(Map<String, DataField> tagNames, GridRequestFilter filter) {
		if(filter.getForceCaseInsensitive())
			return true;
		
		// If the field is marked as UPPERCASE and the sourcename is using a function
		// then we apply a case insensitive filter
		if(isUppercase(tagNames, filter) && tagNames.get(filter.getFieldName()).getSourcename().startsWith("r5o7"))
			return true;	
		
		// If the field is marked as UPPERCASE, then we assume all content is uppercase
		// and we do NOT apply a case insensitive filter
		if(isUppercase(tagNames, filter))
			return false;	
		
		// We consider MIXVARCHAR field without other information whether case sensitive or insensitive
		// as being case insensitive
		return tagNames.get(filter.getFieldName()).getDatatype().toString().equals("MIXVARCHAR");
	}


    public GridMetadataRequestResult getGridMetadata(InforContext context, String gridCode, String viewType, String language) throws InforException {
        tools.demandDatabaseConnection();
        if (gridCode == null || gridCode.trim().equals("")) {
            throw tools.generateFault("Grid code is a mandatory field.");
        }
        if (viewType == null || viewType.trim().equals("")) {
            throw tools.generateFault("View Type is a mandatory field.");
        }
        // Fetch grid data
        EntityManager em = tools.getEntityManager();
        try {
            // Grid name
            GridMetadataRequestResult result = em.find(GridMetadataRequestResult.class, gridCode);
            if (result == null) {
                // Just 'forward' the exception to the catch block.
                throw new Exception();
            }
            // Grid fields and data spies
            GridDataspy[] gridDataspies = em.createNamedQuery(GridDataspy.GETGRIDDATASPIES, GridDataspy.class)
                    .setParameter("gridid", gridCode)
                    .setParameter("userid", context.getCredentials().getUsername()).getResultList().toArray(new GridDataspy[0]);

            // select default dataspy
            List<GridDataspy> selectedDataSpyList = Arrays.stream(gridDataspies).filter(ds -> ds.isDefaultDataspy()).collect(Collectors.toList());
            GridDataspy selectedDataSpy = gridDataspies[0];
            if(!selectedDataSpyList.isEmpty())
                selectedDataSpy = selectedDataSpyList.get(0);

            result.setGridCode(gridCode);
            result.setGridDataspies(gridDataspies);
            result.setDataSpyId(selectedDataSpy.getCode());

            // This code is fetching all fields for the dataspy
            GridField[] gridFields = em.createNamedQuery(GridField.GETDDSPYFIELDS, GridField.class)
                    .setParameter("gridid", gridCode)
                    .setParameter("ddspyid", selectedDataSpy.getCode())
                    .setParameter("viewtype", viewType)
                    .setParameter("language", language != null ? language.toUpperCase() : "EN")
                    .getResultList().toArray(new GridField[0]);

            // Concat with custom fields
            if (JPAGrids.USE_CUSTOM_FIELDS) {
                gridFields = Stream.of(gridFields, gridCustomFieldHandler.getCustomFieldsAsGridFields(selectedDataSpy.getCode())).flatMap(Stream::of).toArray(GridField[]::new);
            }

            result.setGridFields(gridFields);

            return result;
        } catch (Exception e) {
            tools.log(Level.SEVERE,"Error while fetching grid metadata for gridCode " + gridCode);
            throw tools.generateFault("Couldn't fetch the metadata for this grid.");
        } finally {
            em.clear();
            em.close();
        }
    }

    public GridDDSpyFieldsResult getDDspyFields(InforContext context, String gridCode, String viewType, String ddSpyId, String language) throws InforException {
        tools.demandDatabaseConnection();
        if (gridCode == null || gridCode.trim().equals("")) {
            throw tools.generateFault("Grid code is a mandatory field.");
        }
        if (ddSpyId == null || ddSpyId.trim().equals("")) {
            throw tools.generateFault("DataSpy id is a mandatory field.");
        }
        if (viewType == null || viewType.trim().equals("")) {
            throw tools.generateFault("View Type is a mandatory field.");
        }
        // Fetch fields data
        EntityManager em = tools.getEntityManager();
        try {
            // Grid name
            GridDDSpyFieldsResult result = new GridDDSpyFieldsResult();
            // Grid fields and data spies
            GridField[] gridFields = em.createNamedQuery(GridField.GETDDSPYFIELDS, GridField.class)
                    .setParameter("gridid", gridCode)
                    .setParameter("ddspyid", ddSpyId)
                    .setParameter("viewtype", viewType)
                    .setParameter("language", language != null ? language.toUpperCase() : "EN")
                    .getResultList().toArray(new GridField[0]);

            // Concat with custom fields
            if (JPAGrids.USE_CUSTOM_FIELDS) {
                gridFields = Stream.of(gridFields, gridCustomFieldHandler.getCustomFieldsAsGridFields(ddSpyId)).flatMap(Stream::of).toArray(GridField[]::new);
            }

            result.setGridFields(gridFields);
            result.setDataSpyId(ddSpyId);
            return result;
        } catch (Exception e) {
            tools.log(Level.SEVERE,"Error");
            throw tools.generateFault("Couldn't fetch the metadata for this grid.");
        } finally {
            em.clear();
            em.close();
        }
    }

    public GridDataspy getDefaultDataspy(InforContext context, String gridCode, String viewType) throws InforException {
        tools.demandDatabaseConnection();
        if (gridCode == null || gridCode.trim().equals("")) {
            throw tools.generateFault("Grid code is a mandatory field.");
        }
        if (viewType == null || viewType.trim().equals("")) {
            throw tools.generateFault("View Type is a mandatory field.");
        }
        // Fetch grid data
        EntityManager em = tools.getEntityManager();
        try {
            // Check Grid Code
            GridMetadataRequestResult result = em.find(GridMetadataRequestResult.class, gridCode);
            if (result == null) {
                // Just 'forward' the exception to the catch block.
                throw new Exception();
            }
            // Grid DataSpy
            GridDataspy gridDataspy = em.createNamedQuery(GridDataspy.GETDEFAULTDATASPY, GridDataspy.class)
                    .setParameter("gridid", gridCode)
                    .setParameter("userid", context.getCredentials().getUsername())
                    .getSingleResult();

            return gridDataspy;
        } catch (Exception e) {
            tools.log(Level.SEVERE,"Error while fetching default dataspy for grid code " + gridCode + " and view type " + viewType);
            throw tools.generateFault("Couldn't fetch the metadata for this grid.");
        } finally {
            em.clear();
            em.close();
        }
    }


}
