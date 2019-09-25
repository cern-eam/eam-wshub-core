package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.PartBinStockService;
import ch.cern.eam.wshub.core.services.material.entities.PartStock;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.binstock_001.BinStock;
import net.datastream.schemas.mp_fields.BINSTOCKID;
import net.datastream.schemas.mp_fields.PARTID_Type;
import net.datastream.schemas.mp_fields.STOREID_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0248_001.MP0248_AddBinStock_001;
import net.datastream.schemas.mp_functions.mp0249_001.MP0249_SyncBinStock_001;
import net.datastream.schemas.mp_functions.mp0250_001.MP0250_GetBinStock_001;
import net.datastream.schemas.mp_results.mp0250_001.MP0250_GetBinStock_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;

public class PartBinStockServiceImpl implements PartBinStockService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public PartBinStockServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public String addPartStock(InforContext context, PartStock partStockParam) throws InforException {

		BinStock binStock = new BinStock();

		if (partStockParam.getStoreCode() != null) {
			binStock.setSTOREID(new STOREID_Type());
			binStock.getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
			binStock.getSTOREID().setSTORECODE(partStockParam.getStoreCode().trim().toUpperCase());
		}

		//
		if (partStockParam.getBin() != null) {
			binStock.setBIN(partStockParam.getBin());
		}

		//
		if (partStockParam.getLot() != null) {
			binStock.setLOT(partStockParam.getLot());
		}

		//
		if (partStockParam.getQtyOnHand() != null) {
			binStock.setQTYONHAND(tools.getDataTypeTools().encodeAmount(partStockParam.getQtyOnHand(),"Qty. On Hand"));
		}

		// PART
		if (partStockParam.getPartCode() != null) {
			binStock.setPARTID(new PARTID_Type());
			binStock.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
			binStock.getPARTID().setPARTCODE(partStockParam.getPartCode().trim().toUpperCase());
		}

		MP0248_AddBinStock_001 addbinstock = new MP0248_AddBinStock_001();
		addbinstock.setBinStock(binStock);

		if (context.getCredentials() != null) {
			inforws.addBinStockOp(addbinstock, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addBinStockOp(addbinstock, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return null;
	}

	public String updatePartStock(InforContext context, PartStock partStockParam) throws InforException {
		if (partStockParam == null || partStockParam.getPartCode() == null || partStockParam.getStoreCode() == null || partStockParam.getBin() == null || partStockParam.getLot() == null) {
			throw tools.generateFault("You must supply valid Part, Store, Bin and Lot in order to update the part stock");
		}
		//
		// GET IT FIRST
		//
		MP0250_GetBinStock_001 getBinStock = new MP0250_GetBinStock_001();
		getBinStock.setBINSTOCKID(new BINSTOCKID());
		getBinStock.getBINSTOCKID().setPARTID(new PARTID_Type());
		getBinStock.getBINSTOCKID().getPARTID().setORGANIZATIONID(tools.getOrganization(context));
		getBinStock.getBINSTOCKID().getPARTID().setPARTCODE(partStockParam.getPartCode());

		getBinStock.getBINSTOCKID().setLOT(partStockParam.getLot());
		getBinStock.getBINSTOCKID().setBIN(partStockParam.getBin());

		getBinStock.getBINSTOCKID().setSTOREID(new STOREID_Type());
		getBinStock.getBINSTOCKID().getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
		getBinStock.getBINSTOCKID().getSTOREID().setSTORECODE(partStockParam.getStoreCode());

		MP0250_GetBinStock_001_Result result = new MP0250_GetBinStock_001_Result();

		if (context.getCredentials() != null) {
			result = inforws.getBinStockOp(getBinStock, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.getBinStockOp(getBinStock, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		//
		// UPDATE AFTERWARDS
		//
		MP0249_SyncBinStock_001 syncBinStock = new MP0249_SyncBinStock_001();
		syncBinStock.setBinStock(result.getResultData().getBinStock());

		//
		if (partStockParam.getQtyOnHand() != null) {
			syncBinStock.getBinStock().setQTYONHAND(tools.getDataTypeTools().encodeAmount(partStockParam.getQtyOnHand(),"Qty. On Hand"));
		}

		if (context.getCredentials() != null) {
			inforws.syncBinStockOp(syncBinStock, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.syncBinStockOp(syncBinStock, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return null;
	}


}
