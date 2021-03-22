package ch.cern.eam.wshub.core.services.administration;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.tools.InforException;

public interface ResponsibilityService {
    void addResponsibility(InforContext context, String userCode, String responsibilityCode) throws InforException;

    void deleteResponsibility(InforContext context, String userCode, String responsibilityCode) throws InforException;
}
