package ch.cern.eam.wshub.core.services.grids;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.services.grids.entities.*;

import java.util.List;

public interface GridsService {
	@Operation(logOperation = EAM_OPERATION.GRID_BR)
	BatchResponse<GridRequestResult> executeQueryBatch(EAMContext context, List<GridRequest> gridRequests) throws EAMException;

	@Operation(logOperation = EAM_OPERATION.GRID_R)
	GridRequestResult executeQuery(EAMContext context, GridRequest gridRequest) throws EAMException;

	@Operation(logOperation = EAM_OPERATION.GRIDMETAD_R)
	GridMetadataRequestResult getGridMetadata(EAMContext context, String gridCode, String viewType) throws EAMException;

	@Operation(logOperation = EAM_OPERATION.GRIDMETAD_R)
	GridMetadataRequestResult getGridMetadata(EAMContext context, String gridCode, String viewType, String language) throws EAMException;

	@Operation(logOperation = EAM_OPERATION.GRIDMETAD_R)
	GridMetadataRequestResult getGridMetadataEAM(EAMContext context, String gridName) throws EAMException;

	@Operation(logOperation = EAM_OPERATION.GRIDDDSPY_R)
	GridDDSpyFieldsResult getDDspyFields(EAMContext context, String gridCode, String viewType, String ddSpyId, String language) throws EAMException;

	@Operation(logOperation = EAM_OPERATION.DDSPYFIELD_R)
	GridDataspy getDefaultDataspy(EAMContext context, String gridCode, String viewType) throws EAMException;

	String getGridCsvData(EAMContext context, GridRequest gridRequest) throws EAMException;
}

