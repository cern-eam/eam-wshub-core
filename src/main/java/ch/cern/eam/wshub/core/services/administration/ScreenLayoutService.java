package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.annotations.Operation;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;
import ch.cern.eam.wshub.core.services.administration.entities.DataspyCopy;
import ch.cern.eam.wshub.core.services.administration.entities.ScreenLayout;
import ch.cern.eam.wshub.core.tools.InforException;

import java.util.List;

public interface ScreenLayoutService {

    @Operation(logOperation = INFOR_OPERATION.SCREEN_LAYOUT_READ)
    ScreenLayout readScreenLayout(InforContext context, String systemFunction, String userFunction, List<String> tabs, String userGroup, String entity) throws InforException;

}
