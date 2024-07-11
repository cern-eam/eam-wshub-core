package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.workorders.entities.SalesPrice;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface SalesPriceService {

    String createSalesPrice(EAMContext context, SalesPrice salesPrice) throws EAMException;

    String updateSalesPrice(EAMContext context, SalesPrice salesPrice) throws EAMException;


}
