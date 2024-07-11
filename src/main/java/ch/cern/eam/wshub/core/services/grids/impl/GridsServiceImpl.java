package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.administration.impl.UserSetupServiceImpl;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.grids.GridsService;

import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;

import java.util.List;
import java.util.stream.Collectors;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ch.cern.eam.wshub.core.tools.GridTools.getCellContent;

public class GridsServiceImpl implements GridsService {

	private Tools tools;
	private EAMWebServicesPT eamws;

	private EAMGrids eamGrids;
	private JPAGrids jpaGrids;
	private ApplicationData applicationData;
	public static final Map<String, GridMetadataRequestResult> gridIdCache = new ConcurrentHashMap<>();

	public GridsServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
		this.applicationData = applicationData;
		eamGrids = new EAMGrids(applicationData, tools, eamws);
		// Init JPA Grids only when DB connection is present
		if (tools.isDatabaseConnectionConfigured()) {
			jpaGrids = new JPAGrids(applicationData, tools, eamws);
		}
	}

	@Override
	public BatchResponse<GridRequestResult> executeQueryBatch(EAMContext context, List<GridRequest> gridRequests) throws EAMException {
		return tools.batchOperation(context, this::executeQuery, gridRequests);
	}

	public GridRequestResult executeQuery(EAMContext context, GridRequest gridRequest) throws EAMException {
		if (gridRequest.getUseNative() || !tools.isDatabaseConnectionConfigured()) {
			return eamGrids.executeQuery(context, gridRequest);
		}
		tools.demandDatabaseConnection();
		if (isEmpty(gridRequest.getDataspyID()) || isEmpty(gridRequest.getGridID()) || isEmpty(gridRequest.getGridName())) {
			GridMetadataRequestResult gridMetadataEAM = getGridMetadataEAM(context, gridRequest.getGridName(), gridRequest.getGridID());
			gridRequest.setGridID(gridMetadataEAM.getGridCode());
			gridRequest.setGridName(gridMetadataEAM.getGridName());
			if (gridRequest.getDataspyID() == null) gridRequest.setDataspyID(gridMetadataEAM.getDataSpyId());
			if (gridRequest.getUserFunctionName() == null) gridRequest.setUserFunctionName(gridMetadataEAM.getGridName());
		}

		if (applicationData.getWithJPAGridsAuthentication()) {
			// Invoke the EAM login web service before JPA Grid invocation
			UserSetupServiceImpl.login(context, null, tools, eamws);
		}
		GridRequestResult gridRequestResult = jpaGrids.executeQuery(context, gridRequest);
		return gridRequestResult;
	}

	public GridMetadataRequestResult getGridMetadataEAM(EAMContext context, String gridName) {
		return getGridMetadataEAM(context, gridName, null);
	}

	public GridMetadataRequestResult getGridMetadataEAM(EAMContext context, String gridName, String gridId) {
		try {
			String cacheName = gridName + "#" + gridId;
			if (gridIdCache.containsKey(cacheName)) {
				return gridIdCache.get(cacheName);
			}
			GridRequest gridRequest = new GridRequest("BEWSGR");
			gridRequest.setIncludeMetadata(true);
			gridRequest.setRowCount(1);
			if (gridName != null) {
				gridRequest.getGridRequestFilters().add(new GridRequestFilter("grd_gridname", gridName, "=", GridRequestFilter.JOINER.OR));
			}
			if (gridId != null) {
				gridRequest.getGridRequestFilters().add(new GridRequestFilter("grd_gridid", gridId, "="));
			}
			GridRequestResult result = eamGrids.executeQuery(context, gridRequest);

			GridMetadataRequestResult gridData = new GridMetadataRequestResult();
			gridData.setGridName(getCellContent("grd_gridname", result.getRows()[0]));
			gridData.setGridCode(getCellContent("grd_gridid", result.getRows()[0]));
			gridData.setDataSpyId(getCellContent("dds_ddspyid", result.getRows()[0]).replaceAll(",", ""));
			// Store the gridId in the cache
			gridIdCache.put(cacheName, gridData);
			//
			return gridData;
		} catch (Exception e) {
			return null;
		}
	}

	public GridMetadataRequestResult getGridMetadata(EAMContext context, String gridCode, String viewType) throws EAMException {
		tools.demandDatabaseConnection();
		return getGridMetadata(context, gridCode, viewType, "EN");
	}

	public GridMetadataRequestResult getGridMetadata(EAMContext context, String gridCode, String viewType, String language) throws EAMException {
		tools.demandDatabaseConnection();
		return jpaGrids.getGridMetadata(context, gridCode, viewType, language);
	}

	public GridDDSpyFieldsResult getDDspyFields(EAMContext context, String gridCode, String viewType, String ddSpyId, String language) throws EAMException {
		tools.demandDatabaseConnection();
		return jpaGrids.getDDspyFields(context, gridCode, viewType, ddSpyId, language);
	}

	public GridDataspy getDefaultDataspy(EAMContext context, String gridCode, String viewType) throws EAMException {
		tools.demandDatabaseConnection();
		return jpaGrids.getDefaultDataspy(context, gridCode, viewType);
	}

    public String getGridCsvData(EAMContext context, GridRequest gridRequest) throws EAMException {
		// Always fetch the meta data to ensure that the grid fields will be included
		gridRequest.setIncludeMetadata(true);
        GridRequestResult gridRequestResult = eamGrids.executeQuery(context, gridRequest);
        return CSVUtils.convertGridRequestResultToCsv(gridRequestResult);
    }

}

