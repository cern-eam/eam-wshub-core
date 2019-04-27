package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.gridrequest.*;
import net.datastream.schemas.mp_functions.gridrequest.LOV.LOV_PARAMETERS;
import net.datastream.schemas.mp_functions.gridrequest.LOV.LOV_PARAMETERS.LOV_PARAMETER;
import net.datastream.schemas.mp_functions.gridrequest.MULTIADDON_FILTERS.MADDON_FILTER;
import net.datastream.schemas.mp_functions.mp0116_getgriddataonly_001.FUNCTION_REQUEST_INFO;
import net.datastream.schemas.mp_functions.mp0116_getgriddataonly_001.FUNCTION_REQUEST_TYPE;
import net.datastream.schemas.mp_functions.mp0116_getgriddataonly_001.MP0116_GetGridDataOnly_001;
import net.datastream.schemas.mp_functions.mp0116_getgriddataonly_001_result.DATA.ROW;
import net.datastream.schemas.mp_functions.mp0116_getgriddataonly_001_result.MP0116_GetGridDataOnly_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.ws.Holder;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class InforGrids implements Serializable {
	private static final long serialVersionUID = 5957161022766799698L;


	private ApplicationData applicationData;
	private Tools tools;
	private InforWebServicesPT inforws;

	public InforGrids(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public GridRequestResult executeQuery(InforContext context, GridRequest gridRequest) throws InforException {
		//
		//
		//
		FUNCTION_REQUEST_INFO funRequest = new FUNCTION_REQUEST_INFO();
		//
		// GRID BASICS
		//
		funRequest.setGRID(new GRID());
		// 
		if (gridRequest.getCursorPosition() != null && !gridRequest.getCursorPosition().trim().equals("")) {
			funRequest.getGRID().setCURSOR_POSITION(new BigInteger(gridRequest.getCursorPosition()));
		} else {
			funRequest.getGRID().setCURSOR_POSITION(BigInteger.valueOf(100));
		}
		// GRID ID, GRID NAME
		if (gridRequest.getGridID() != null && gridRequest.getGridName() != null) {
			funRequest.getGRID().setGRID_ID(new BigInteger(gridRequest.getGridID()));
			funRequest.getGRID().setGRID_NAME(gridRequest.getGridName());
		} else {
			throw tools.generateFault("Please supply grid name and grid id.");
		}

		// USER FUNCTION NAME
		if (tools.getDataTypeTools().isNotEmpty(gridRequest.getUserFunctionName())) {
			funRequest.getGRID().setUSER_FUNCTION_NAME(gridRequest.getUserFunctionName());
		}
		// SET NUMBERS OF ROWS TO RETURN
		if (gridRequest.getRowCount() != null && !gridRequest.getRowCount().trim().equals("")) {
			funRequest.getGRID().setNUMBER_OF_ROWS_FIRST_RETURNED(new BigInteger(gridRequest.getRowCount()));
		} else {
			funRequest.getGRID().setNUMBER_OF_ROWS_FIRST_RETURNED(BigInteger.ZERO);
		}
		// DON'T SHORTEN THE RESPONSE 
		funRequest.getGRID().setTERSERESPONSE("false");
		//
		//
		//
		funRequest.setDATASPY(new DATASPY());
		funRequest.getDATASPY().setDATASPY_ID(new BigInteger(gridRequest.getDataspyID()));

		//
		// GRID TYPE
		// LIST: business data query
		// LOV: List of values to validate field entry 
		//
		funRequest.setGRID_TYPE(new GRID_TYPE());
		if (gridRequest.getGridType() == null || "LIST".equals(gridRequest.getGridType())) {
			funRequest.getGRID_TYPE().setTYPE(GRID_TYPE_Type.LIST);
		} else {
			funRequest.getGRID_TYPE().setTYPE(GRID_TYPE_Type.LOV);
		}
		funRequest.setREQUEST_TYPE(FUNCTION_REQUEST_TYPE.LIST___DATA___ONLY___STORED);

		//
		// FILTERS
		//
		if (gridRequest.getGridRequestFilters() != null && gridRequest.getGridRequestFilters().length > 0) {
			funRequest.setMULTIADDON_FILTERS(new MULTIADDON_FILTERS());
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
				inforFilter.setLPAREN(filter.getLeftParenthesis());
				inforFilter.setRPAREN(filter.getRightParenthesis());
				switch(inforFilter.getOPERATOR()) {
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

				funRequest.getMULTIADDON_FILTERS().getMADDON_FILTER().add(inforFilter);
			}
		} else {
			funRequest.setMULTIADDON_FILTERS(new MULTIADDON_FILTERS());
		}

		//
		// SORT
		//
		if(gridRequest.getGridRequestSorts() != null){
			for(GridRequestSort sort : gridRequest.getGridRequestSorts()){
				funRequest.setADDON_SORT(new ADDON_SORT());
				if (sort.getSortType() != null && sort.getSortType().toUpperCase().equals("DESC")) {
					funRequest.getADDON_SORT().setTYPE(SORT_TYPE.DESC);
				} else {
					funRequest.getADDON_SORT().setTYPE(SORT_TYPE.ASC);
				}
				funRequest.getADDON_SORT().setALIAS_NAME(sort.getSortBy());
			}
		}

		//
		// LOV PARAMETERS 
		//
		if (gridRequest.getGridRequestParameterNames() != null 
				&& gridRequest.getGridRequestParameterNames().length > 0
				&& gridRequest.getGridRequestParameterValues() != null 
				&& gridRequest.getGridRequestParameterValues().length > 0
				&& gridRequest.getGridRequestParameterNames().length == gridRequest.getGridRequestParameterValues().length) {
			funRequest.setLOV(new LOV());
			funRequest.getLOV().setLOV_PARAMETERS(new LOV_PARAMETERS());
			for (int i = 0; i < gridRequest.getGridRequestParameterNames().length; i++) {
				LOV_PARAMETER lovParameter = new LOV_PARAMETER();
				lovParameter.setTYPE("VARCHAR");
				lovParameter.setALIAS_NAME(gridRequest.getGridRequestParameterNames()[i]);
				lovParameter.setVALUE(gridRequest.getGridRequestParameterValues()[i]);
				funRequest.getLOV().getLOV_PARAMETERS().getLOV_PARAMETER().add(lovParameter);
			}
		}

		MP0116_GetGridDataOnly_001 getgridd = new MP0116_GetGridDataOnly_001();
		getgridd.setFUNCTION_REQUEST_INFO(funRequest);
		MP0116_GetGridDataOnly_001_Result result = new MP0116_GetGridDataOnly_001_Result();
		//
		// CALL THE WEB SERVICE
		//
		if (context.getCredentials() != null) {
			result = inforws.getGridDataOnlyOp(getgridd, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), applicationData.getTenant());
		} else 
		{
			SessionType session = new SessionType();
			session.setSessionId(context.getSessionID());
			result = inforws.getGridDataOnlyOp(getgridd, tools.getOrganizationCode(context), null,"", new Holder<SessionType>(session), null, applicationData.getTenant());
		}

		//
		// META DATA
		//
		GridRequestResult grr = new GridRequestResult();
		if (result.getGRIDRESULT().getGRID().getMETADATA().getMORERECORDPRESENT().equals("+")) {
			grr.setMoreRowsPresent("TRUE");
		} else {
			grr.setMoreRowsPresent("FALSE");
		}
		grr.setRecords(result.getGRIDRESULT().getGRID().getMETADATA().getRECORDS());
		//
		// RESULT DATA
		//
		List<DataspyField> fields = new LinkedList<DataspyField>();
		EntityManager em = tools.getEntityManager();
		try {
			fields =  em.createNamedQuery(DataspyField.GETDATASPYFIELDS, DataspyField.class)  
						  .setParameter("requestType", gridRequest.getGridType())
						  .setParameter("dataspyid", gridRequest.getDataspyID())
						  .getResultList();
		} catch (Exception e) {
			throw tools.generateFault("Couldn't fetch data spy fields" + e.getMessage());
		} finally {
			em.close();
		}
		
		if (result.getGRIDRESULT().getGRID().getDATA() != null && 
				result.getGRIDRESULT().getGRID().getDATA().getROW() != null &&
				result.getGRIDRESULT().getGRID().getDATA().getROW().size() > 0) {
			
			// Load Customs fields info			
			final List<Object[]> customFields = loadCustomFieldsInfo(gridRequest);

			LinkedList<GridRequestRow> rows = new LinkedList<GridRequestRow>();
			for (ROW inforRow : result.getGRIDRESULT().getGRID().getDATA().getROW()) {
				
				// 
				List<GridRequestCell> cells = inforRow.getD().stream().map(inforCell -> {
					return new GridRequestCell(inforCell.getN().toString(), 
							                   inforCell.getContent());
				}).collect(Collectors.toList());

				// Extend each cell with the order and tagname
				for (DataspyField dataspyField : fields) {
					// Normal fields
					cells.stream().filter(cell -> cell.getCol().equals(dataspyField.getId())).forEach(cell -> {
						cell.setOrder(dataspyField.getOrder()); //order 
						cell.setTag(dataspyField.getTagName()); //tagname 
					});
				
					// Custom fields
					AtomicInteger atomicInteger = new AtomicInteger(0);
					cells.stream().filter(cell -> (Integer.parseInt(cell.getCol())<-1)).forEach(cell -> {
						cell.setOrder(Integer.parseInt(customFields.get(atomicInteger.get())[0].toString())); //order 
						cell.setTag(customFields.get(atomicInteger.getAndIncrement())[1].toString()); //tagname 
					});
				}
				
				// Sort using the order 
				cells = cells.stream().sorted( (e1, e2) -> Integer.compare(e1.getOrder(), e2.getOrder()))
						.collect(Collectors.toList());
				
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
	
	private static String SELECT_CUSTOM_FIELDS = "select dcf_order, dcf_tagname from R5DDCUSTOMFIELDS where DCF_DDSPYID = :dataspyid";
	
	@SuppressWarnings("unchecked")
	private List<Object[]> loadCustomFieldsInfo(GridRequest gridRequest) throws InforException {
		EntityManager em = tools.getEntityManager();
		List<Object[]> customFieldsL = null; 
		try {
			em = tools.getEntityManager();
			Query q = em.createNativeQuery(SELECT_CUSTOM_FIELDS);
			q.setParameter("dataspyid", gridRequest.getDataspyID());  
			customFieldsL = (List<Object[]>) q.getResultList();
		} catch (IllegalArgumentException | NullPointerException exception) {
			tools.log(Level.FINE, exception.getMessage());
			throw tools.generateFault("Error loading Custom Fields information");
		} catch(javax.persistence.NoResultException exception) {
			customFieldsL = null;
		} finally {
			em.close();
		}
		
		return customFieldsL;		
	}
	
}
