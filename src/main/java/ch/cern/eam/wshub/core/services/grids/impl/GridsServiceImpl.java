package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.administration.impl.UserSetupServiceImpl;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.CacheKey;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;
import static ch.cern.eam.wshub.core.tools.GridTools.getCellContent;

public class GridsServiceImpl implements GridsService {

	private final ApplicationData applicationData;
	private final Tools tools;
	private final InforWebServicesPT inforws;
	private final InforGrids inforGrids;
	private final JPAGrids jpaGrids;

	public GridsServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.applicationData = applicationData;
		this.inforGrids = new InforGrids(applicationData, tools, inforws);
		// Init JPA Grids only when DB connection is present
		if (tools.isDatabaseConnectionConfigured()) {
			jpaGrids = new JPAGrids(applicationData, tools, inforws);
		} else {
			jpaGrids = null;
		}
	}

	@Override
	public BatchResponse<GridRequestResult> executeQueryBatch(InforContext context, List<GridRequest> gridRequests) throws InforException {
		return tools.batchOperation(context, this::executeQuery, gridRequests);
	}

	public GridRequestResult executeQuery(InforContext context, GridRequest gridRequest) throws InforException {
		if (gridRequest.getUseNative() || !tools.isDatabaseConnectionConfigured()) {
			return inforGrids.executeQuery(context, gridRequest);
		}
		tools.demandDatabaseConnection();
		if (isEmpty(gridRequest.getDataspyID()) || isEmpty(gridRequest.getGridID()) || isEmpty(gridRequest.getGridName())) {
			GridMetadataRequestResult gridMetadataInfor = getGridMetadataInfor(context, gridRequest.getGridName(), gridRequest.getGridID());
			gridRequest.setGridID(gridMetadataInfor.getGridCode());
			gridRequest.setGridName(gridMetadataInfor.getGridName());
			if (gridRequest.getDataspyID() == null) gridRequest.setDataspyID(gridMetadataInfor.getDataSpyId());
			if (gridRequest.getUserFunctionName() == null) gridRequest.setUserFunctionName(gridMetadataInfor.getGridName());
		}

		if (applicationData.getWithJPAGridsAuthentication()) {
			// Invoke the EAM login web service before JPA Grid invocation
			UserSetupServiceImpl.login(context, null, tools, inforws);
		}
		return jpaGrids.executeQuery(context, gridRequest);
	}

	public GridMetadataRequestResult getGridMetadataInfor(InforContext context, String gridName) {
		return getGridMetadataInfor(context, gridName, null);
	}

	public GridMetadataRequestResult getGridMetadataInfor(InforContext context, String gridName, String gridId) {
		String gridIdCacheKey = Tools.getCacheKey(context, gridName + "#" + gridId);
		Function<String, GridMetadataRequestResult> loader = key -> loadGridMetadataInfor(context, gridName, gridId);
		return Optional.ofNullable(InforClient.cacheMap.get(CacheKey.GRID_ID))
				.map(cache -> (GridMetadataRequestResult) cache.get(gridIdCacheKey, loader))
				.orElseGet(() -> loader.apply(gridIdCacheKey));
	}

	public GridMetadataRequestResult loadGridMetadataInfor(InforContext context, String gridName, String gridId) {
		try {
			GridRequest gridRequest = new GridRequest("BEWSGR");
			gridRequest.setIncludeMetadata(true);
			gridRequest.setRowCount(1);
			if (gridName != null) {
				gridRequest.getGridRequestFilters().add(new GridRequestFilter("grd_gridname", gridName, "=", GridRequestFilter.JOINER.OR));
			}
			if (gridId != null) {
				gridRequest.getGridRequestFilters().add(new GridRequestFilter("grd_gridid", gridId, "="));
			}
			GridRequestResult result = inforGrids.executeQuery(context, gridRequest);

			GridMetadataRequestResult gridData = new GridMetadataRequestResult();
			gridData.setGridName(getCellContent("grd_gridname", result.getRows()[0]));
			gridData.setGridCode(getCellContent("grd_gridid", result.getRows()[0]));
			gridData.setDataSpyId(getCellContent("dds_ddspyid", result.getRows()[0]).replaceAll(",", ""));
			return gridData;
		} catch (Exception e) {
			return null;
		}
	}

	public GridMetadataRequestResult getGridMetadata(InforContext context, String gridCode, String viewType) throws InforException {
		tools.demandDatabaseConnection();
		return getGridMetadata(context, gridCode, viewType, "EN");
	}

	public GridMetadataRequestResult getGridMetadata(InforContext context, String gridCode, String viewType, String language) throws InforException {
		tools.demandDatabaseConnection();
		return jpaGrids.getGridMetadata(context, gridCode, viewType, language);
	}

	public GridDDSpyFieldsResult getDDspyFields(InforContext context, String gridCode, String viewType, String ddSpyId, String language) throws InforException {
		tools.demandDatabaseConnection();
		return jpaGrids.getDDspyFields(context, gridCode, viewType, ddSpyId, language);
	}

	public GridDataspy getDefaultDataspy(InforContext context, String gridCode, String viewType) throws InforException {
		tools.demandDatabaseConnection();
		return jpaGrids.getDefaultDataspy(context, gridCode, viewType);
	}

    public String getGridCsvData(InforContext context, GridRequest gridRequest) throws InforException {
		// Always fetch the metadata to ensure that the grid fields will be included
		gridRequest.setIncludeMetadata(true);
        GridRequestResult gridRequestResult = inforGrids.executeQuery(context, gridRequest);
        return CSVUtils.convertGridRequestResultToCsv(gridRequestResult);
    }

}

