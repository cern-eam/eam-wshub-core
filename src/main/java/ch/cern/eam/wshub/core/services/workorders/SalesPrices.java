package ch.cern.eam.wshub.core.services.workorders;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.workorders.entities.SalesPrice;
import ch.cern.eam.wshub.core.tools.InforException;

public interface SalesPrices {

    String createSalesPrice(InforContext context, SalesPrice salesPrice) throws InforException;

    String updateSalesPrice(InforContext context, SalesPrice salesPrice) throws InforException;


}
