package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.InforException;

public interface SystemService {

    String createSystem(InforContext context, Equipment systemParam) throws InforException;

    Equipment readSystem(InforContext context, String systemCode) throws InforException;

    String updateSystem(InforContext context, Equipment systemParam) throws InforException;

    String deleteSystem(InforContext context, String systemCode) throws InforException;

    Equipment readSystemDefault(InforContext context, String organizationCode) throws InforException;
}
