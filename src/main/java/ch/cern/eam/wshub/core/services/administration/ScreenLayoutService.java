package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.administration.entities.ScreenLayout;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.services.administration.entities.GenericLov;

import java.util.List;
import java.util.Map;

public interface ScreenLayoutService {

    @Operation(logOperation = INFOR_OPERATION.SCREEN_LAYOUT_READ)
    ScreenLayout readScreenLayout(InforContext context, String systemFunction, String userFunction, List<String> tabs, String userGroup, String entity) throws InforException;

    @Operation(logOperation = INFOR_OPERATION.SCREEN_LAYOUT_LOV)
    List<Map<String, String>> getGenericLov(InforContext context, GenericLov genericLov) throws InforException;

}
