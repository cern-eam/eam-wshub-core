package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.InforException;

public interface PositionService {
    String createPosition(InforContext context, Equipment positionParam) throws InforException;

    String deletePosition(InforContext context, String systemCode) throws InforException;

    Equipment readPosition(InforContext context, String positionCode) throws InforException;

    String updatePosition(InforContext context, Equipment positionParam) throws InforException;
}
