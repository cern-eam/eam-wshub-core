package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.workorders.SalesPrices;
import ch.cern.eam.wshub.core.services.workorders.entities.SalesPrice;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.customercontractsalesprice_001.CustomerContractSalesPrice;
import net.datastream.schemas.mp_fields.CUSTOMERCONTRACTSALESPRICEID_Type;
import net.datastream.schemas.mp_functions.mp7873_001.MP7873_GetCustomerContractSalesPrice_001;
import net.datastream.schemas.mp_functions.mp7874_001.MP7874_AddCustomerContractSalesPrice_001;
import net.datastream.schemas.mp_functions.mp7875_001.MP7875_SyncCustomerContractSalesPrice_001;
import net.datastream.schemas.mp_results.mp7873_001.MP7873_GetCustomerContractSalesPrice_001_Result;
import net.datastream.schemas.mp_results.mp7874_001.MP7874_AddCustomerContractSalesPrice_001_Result;
import net.datastream.schemas.mp_results.mp7875_001.MP7875_SyncCustomerContractSalesPrice_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;

public class SalesPricesImpl implements SalesPrices {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public SalesPricesImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    public String createSalesPrice(InforContext context, SalesPrice salesPrice) throws InforException {
        //
        if (isEmpty(salesPrice.getSalesPriceCode())) {
            salesPrice.setSalesPriceCode("");
        }

        MP7874_AddCustomerContractSalesPrice_001 addSalesPrice = new MP7874_AddCustomerContractSalesPrice_001();
        addSalesPrice.setCustomerContractSalesPrice(new CustomerContractSalesPrice());
        tools.getInforFieldTools().transformWSHubObject(addSalesPrice.getCustomerContractSalesPrice(), salesPrice, context);

        MP7874_AddCustomerContractSalesPrice_001_Result result = tools.performInforOperation(context, inforws::addCustomerContractSalesPriceOp, addSalesPrice);
        return result.getResultData().getCUSTOMERCONTRACTSALESPRICEID().getCUSTOMERCONTRACTSALESPRICECODE();
    }

    public String updateSalesPrice(InforContext context, SalesPrice salesPrice) throws  InforException {
        MP7873_GetCustomerContractSalesPrice_001 getSalesPrice = new MP7873_GetCustomerContractSalesPrice_001();
        getSalesPrice.setCUSTOMERCONTRACTSALESPRICEID(new CUSTOMERCONTRACTSALESPRICEID_Type());
        getSalesPrice.getCUSTOMERCONTRACTSALESPRICEID().setCUSTOMERCONTRACTSALESPRICECODE(salesPrice.getSalesPriceCode());
        MP7873_GetCustomerContractSalesPrice_001_Result getSalesPriceResult = tools.performInforOperation(context, inforws::getCustomerContractSalesPriceOp, getSalesPrice);

        CustomerContractSalesPrice salesPriceInfor  = tools.getInforFieldTools().transformWSHubObject(getSalesPriceResult.getResultData().getCustomerContractSalesPrice(), salesPrice, context);

        MP7875_SyncCustomerContractSalesPrice_001 syncSalesPrice = new MP7875_SyncCustomerContractSalesPrice_001();
        syncSalesPrice.setCustomerContractSalesPrice(salesPriceInfor);

        MP7875_SyncCustomerContractSalesPrice_001_Result result = tools.performInforOperation(context, inforws::syncCustomerContractSalesPriceOp, syncSalesPrice);
        return result.getResultData().getCUSTOMERCONTRACTSALESPRICEID().getCUSTOMERCONTRACTSALESPRICECODE();

    }

}
