package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface PositionService {

    String createPosition(EAMContext context, Equipment positionParam) throws EAMException;

    Equipment readPosition(EAMContext context, String positionCode, String organization) throws EAMException;

    String deletePosition(EAMContext context, String positionCode, String organization) throws EAMException;

    String updatePosition(EAMContext context, Equipment positionParam) throws EAMException;

    Equipment readPositionDefault(EAMContext context, String organizationCode) throws EAMException;
}
