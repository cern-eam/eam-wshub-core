package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.GridsService;

import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ch.cern.eam.wshub.core.tools.GridTools.getCellContent;

public class GridsServiceImpl implements GridsService {

	private Tools tools;
	private InforWebServicesPT inforws;

	private InforGrids inforGrids;
	private JPAGrids jpaGrids;
	private static Map<String, GridMetadataRequestResult> gridIdCache = new ConcurrentHashMap<>();

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
			if (isEmpty(gridRequest.getDataspyID()) || isEmpty(gridRequest.getGridID())) {
				gridRequest.setDataspyID(getGridMetadataInfor(context, gridRequest.getGridName()).getDataSpyId());
				gridRequest.setGridID(getGridMetadataInfor(context, gridRequest.getGridName()).getGridCode());
			}
			return jpaGrids.executeQuery(context, gridRequest);
		}
	}

	public GridMetadataRequestResult getGridMetadataInfor(InforContext context, String gridName)  {
		try {
			if (gridIdCache.containsKey(gridName)) {
				return gridIdCache.get(gridName);
			}
			GridRequest gridRequest = new GridRequest("BEWSGR");
			gridRequest.setIncludeMetadata(true);
			gridRequest.setRowCount(1);
			gridRequest.getGridRequestFilters().add(new GridRequestFilter("grd_gridname", gridName, "="));
			GridRequestResult result = inforGrids.executeQuery(context, gridRequest);

			GridMetadataRequestResult gridData = new GridMetadataRequestResult();
			gridData.setGridName(gridName);
			gridData.setGridCode(getCellContent("grd_gridid", result.getRows()[0]));
			gridData.setDataSpyId(getCellContent("dds_ddspyid", result.getRows()[0]).replaceAll(",", ""));
			// Store the gridId in the cache
			gridIdCache.put(gridName, gridData);
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
	
}

