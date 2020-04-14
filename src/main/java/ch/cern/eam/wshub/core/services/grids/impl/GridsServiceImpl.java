package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.GridsService;

import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;

import java.util.stream.Collectors;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ch.cern.eam.wshub.core.tools.GridTools.getCellContent;
import static org.apache.commons.text.StringEscapeUtils.escapeCsv;

public class GridsServiceImpl implements GridsService {

	private Tools tools;
	private InforWebServicesPT inforws;

	private InforGrids inforGrids;
	private JPAGrids jpaGrids;
	public static final Map<String, GridMetadataRequestResult> gridIdCache = new ConcurrentHashMap<>();

	public GridsServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		inforGrids = new InforGrids(applicationData, tools, inforws);
		// Init JPA Grids only when DB connection is present
		if (tools.isDatabaseConnectionConfigured()) {
			jpaGrids = new JPAGrids(applicationData, tools, inforws);
		}
	}

	public GridRequestResult executeQuery(InforContext context, GridRequest gridRequest) throws InforException {
		if (gridRequest.getUseNative() || !tools.isDatabaseConnectionConfigured()) {
			return inforGrids.executeQuery(context, gridRequest);
		} else {
			tools.demandDatabaseConnection();
			if (isEmpty(gridRequest.getDataspyID()) || isEmpty(gridRequest.getGridID()) || isEmpty(gridRequest.getGridName())) {
				GridMetadataRequestResult gridMetadataInfor = getGridMetadataInfor(context, gridRequest.getGridName(), gridRequest.getGridID());
				gridRequest.setGridID(gridMetadataInfor.getGridCode());
				gridRequest.setGridName(gridMetadataInfor.getGridName());
				if (gridRequest.getDataspyID() == null) gridRequest.setDataspyID(gridMetadataInfor.getDataSpyId());
				if (gridRequest.getUserFunctionName() == null) gridRequest.setUserFunctionName(gridMetadataInfor.getGridName());
			}
			GridRequestResult gridRequestResult = jpaGrids.executeQuery(context, gridRequest);
			return gridRequestResult;
		}
	}

	public GridMetadataRequestResult getGridMetadataInfor(InforContext context, String gridName) {
		return getGridMetadataInfor(context, gridName, null);
	}

	public GridMetadataRequestResult getGridMetadataInfor(InforContext context, String gridName, String gridId) {
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
			GridRequestResult result = inforGrids.executeQuery(context, gridRequest);

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

    public static String gridToCSV(GridRequestResult gridRequestResult, String separator) {
        String title = gridRequestResult.getGridName() + " \n ";

        String header = gridRequestResult.getGridFields().stream()
            .map(GridField::getLabel)
            .map(text -> escapeCsv(text))
            .collect(Collectors.joining(separator)) + " \n ";

        String result = Arrays.stream(gridRequestResult.getRows())
            .map(row -> Arrays.stream(row.getCell())
                .filter(cell -> cell.getOrder() >= 0)
                .map(GridRequestCell::getContent)
                .map(text ->  escapeCsv(text) )
                .collect(Collectors.joining(separator)))
            .collect(Collectors.joining(" \n "));

        return title + header + result;
    }


}

