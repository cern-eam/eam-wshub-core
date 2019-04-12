package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.entities.BuildKitParam;
import ch.cern.eam.wshub.core.services.material.entities.PartKitTemplate;
import ch.cern.eam.wshub.core.tools.InforException;

public interface PartKitService {

    String addPartKitTemplate(InforContext context, PartKitTemplate partKitParam) throws InforException;

    String createKitSession(InforContext context, BuildKitParam buildKitParam) throws InforException;

    String buildKit(InforContext context, String kitSessionId) throws InforException;
}
