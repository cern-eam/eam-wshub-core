package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.InforException;

public interface SystemService {
    Equipment readSystem(InforContext context, String systemCode) throws InforException;

    String updateSystem(InforContext context, Equipment systemParam) throws InforException;

    String createSystem(InforContext context, Equipment systemParam) throws InforException;

    String deleteSystem(InforContext context, String systemCode) throws InforException;
}
