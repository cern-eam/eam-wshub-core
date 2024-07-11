package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface SystemService {

    String createSystem(EAMContext context, Equipment systemParam) throws EAMException;

    Equipment readSystem(EAMContext context, String systemCode, String organization) throws EAMException;

    String updateSystem(EAMContext context, Equipment systemParam) throws EAMException;

    String deleteSystem(EAMContext context, String systemCode, String organization) throws EAMException;

    Equipment readSystemDefault(EAMContext context, String organizationCode) throws EAMException;
}
