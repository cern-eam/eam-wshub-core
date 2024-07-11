package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.EAM_OPERATION;
import ch.cern.eam.wshub.core.services.administration.entities.ScreenLayout;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.services.administration.entities.GenericLov;

import java.util.List;
import java.util.Map;

public interface ScreenLayoutService {

    @Operation(logOperation = EAM_OPERATION.SCREEN_LAYOUT_READ)
    ScreenLayout readScreenLayout(EAMContext context, String systemFunction, String userFunction, List<String> tabs, String userGroup, String entity) throws EAMException;

    @Operation(logOperation = EAM_OPERATION.SCREEN_LAYOUT_LOV)
    List<Map<String, String>> getGenericLov(EAMContext context, GenericLov genericLov) throws EAMException;

}
