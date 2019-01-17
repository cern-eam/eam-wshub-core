package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.entities.BuildKitParam;
import ch.cern.eam.wshub.core.services.material.entities.PartKitTemplate;
import ch.cern.eam.wshub.core.tools.InforException;

public interface PartKitService {
    String addPartKitTemplate(PartKitTemplate partKitParam, InforContext context) throws InforException;

    String createKitSession(BuildKitParam buildKitParam, InforContext context) throws InforException;

    String buildKit(String kitSessionId, InforContext context) throws InforException;
}
