package ch.cern.eam.wshub.core.services.material;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.material.entities.BuildKitParam;
import ch.cern.eam.wshub.core.services.material.entities.PartKitTemplate;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface PartKitService {

    String addPartKitTemplate(EAMContext context, PartKitTemplate partKitParam) throws EAMException;

    String createKitSession(EAMContext context, BuildKitParam buildKitParam) throws EAMException;

    String buildKit(EAMContext context, String kitSessionId) throws EAMException;
}
