package ch.cern.eam.wshub.core.services.grids;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.services.grids.entities.*;

public interface GridsService {

	@Operation(logOperation = INFOR_OPERATION.GRID_R)
	GridRequestResult executeQuery(InforContext context, GridRequest gridRequest) throws InforException;

	@Operation(logOperation = INFOR_OPERATION.GRIDMETAD_R)
	GridMetadataRequestResult getGridMetadata(InforContext context, String gridCode, String viewType) throws InforException;

	@Operation(logOperation = INFOR_OPERATION.GRIDMETAD_R)
	GridMetadataRequestResult getGridMetadata(InforContext context, String gridCode, String viewType, String language) throws InforException;

	@Operation(logOperation = INFOR_OPERATION.GRIDMETAD_R)
	GridMetadataRequestResult getGridMetadataInfor(InforContext context, String gridName) throws InforException;

	@Operation(logOperation = INFOR_OPERATION.GRIDDDSPY_R)
	GridDDSpyFieldsResult getDDspyFields(InforContext context, String gridCode, String viewType, String ddSpyId, String language) throws InforException;

	@Operation(logOperation = INFOR_OPERATION.DDSPYFIELD_R)
	GridDataspy getDefaultDataspy(InforContext context, String gridCode, String viewType) throws InforException;
	
}

