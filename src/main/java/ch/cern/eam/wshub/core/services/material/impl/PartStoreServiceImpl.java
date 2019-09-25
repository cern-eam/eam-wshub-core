package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.PartStoreService;
import ch.cern.eam.wshub.core.services.material.entities.PartStore;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0254_001.MP0254_GetPartStores_001;
import net.datastream.schemas.mp_functions.mp0255_001.MP0255_AddPartStores_001;
import net.datastream.schemas.mp_functions.mp0256_001.MP0256_SyncPartStores_001;
import net.datastream.schemas.mp_results.mp0254_001.MP0254_GetPartStores_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;


public class PartStoreServiceImpl implements PartStoreService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public PartStoreServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public String updatePartStore(InforContext context, PartStore partStoreParam) throws InforException {


		// fetch the part first
		MP0254_GetPartStores_001 getPartStores = new MP0254_GetPartStores_001();
		getPartStores.setSTOREPARTID(new STOREPARTID_Type());

		// PART ID
		getPartStores.getSTOREPARTID().setPARTID(new PARTID_Type());
		getPartStores.getSTOREPARTID().getPARTID().setORGANIZATIONID(tools.getOrganization(context));
		getPartStores.getSTOREPARTID().getPARTID().setPARTCODE(partStoreParam.getPartCode());

		// STORE ID
		getPartStores.getSTOREPARTID().setSTOREID(new STOREID_Type());
		getPartStores.getSTOREPARTID().getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
		getPartStores.getSTOREPARTID().getSTOREID().setSTORECODE(partStoreParam.getStoreCode());

		MP0254_GetPartStores_001_Result result;
		if (context.getCredentials() != null) {
			result = inforws.getPartStoresOp(getPartStores, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.getPartStoresOp(getPartStores, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		//
		// DO THE UPDATE NOW
		//
		net.datastream.schemas.mp_entities.partstores_001.PartStores partStoresResult = result.getResultData().getPartStores();

		//
		if (partStoreParam.getDefaultBin() != null) {
			partStoresResult.setDEFAULTBIN(new BINID_Type());
			partStoresResult.getDEFAULTBIN().setBIN(partStoreParam.getDefaultBin());
		}

		//
		if (partStoreParam.getOrderQty() != null) {
			partStoresResult.setORDERQTY(tools.getDataTypeTools().encodeAmount(partStoreParam.getOrderQty(),"Ordery Qty."));
		}

		//
		if (partStoreParam.getReorderLevel() != null) {
			partStoresResult.setREORDERLEVEL(tools.getDataTypeTools().encodeAmount(partStoreParam.getReorderLevel(),"Reorder Level"));
		}

		//
		if (partStoreParam.getDefaultReturnBin() != null) {
			partStoresResult.setDEFAULTRETURNBIN(new BINID_Type());
			partStoresResult.getDEFAULTRETURNBIN().setBIN(partStoreParam.getDefaultReturnBin());
		}

		//
		if (partStoreParam.getAbcClass() != null) {
			partStoresResult.setABCCODE(partStoreParam.getAbcClass());
		}

		//
		if (partStoreParam.getPreferredSupplier() != null) {
			partStoresResult.setPREFERREDSUPPLIER(new SUPPLIERID_Type());
			partStoresResult.getPREFERREDSUPPLIER().setORGANIZATIONID(tools.getOrganization(context));
			partStoresResult.getPREFERREDSUPPLIER().setSUPPLIERCODE(partStoreParam.getPreferredSupplier());
		}

		//
		if (partStoreParam.getPreventIssueFromDefaultReturnBin() != null) {
			partStoresResult.setPREVENTISSUEDEFRTNBIN(partStoreParam.getPreventIssueFromDefaultReturnBin());
		} else {
			partStoresResult.setPREVENTISSUEDEFRTNBIN("true");
		}

		//
		if (partStoreParam.getPreferredStore() != null) {
			partStoresResult.setPREFERREDSTORE(new STOREID_Type());
			partStoresResult.getPREFERREDSTORE().setORGANIZATIONID(tools.getOrganization(context));
			partStoresResult.getPREFERREDSTORE().setSTORECODE(partStoreParam.getPreferredStore());
		}

		//
		if (partStoreParam.getMinimumQty() != null) {
			partStoresResult.setMINIMUMQTY(tools.getDataTypeTools().encodeAmount(partStoreParam.getMinimumQty(), "Minimum Qty"));
		}

		//
		if (partStoreParam.getStockMethod() != null) {
			partStoresResult.setONDEMAND(partStoreParam.getStockMethod());
		}

		MP0256_SyncPartStores_001 syncPartStores = new MP0256_SyncPartStores_001();
		syncPartStores.setPartStores(partStoresResult);

		if (context.getCredentials() != null) {
			inforws.syncPartStoresOp(syncPartStores, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.syncPartStoresOp(syncPartStores, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}


		return null;
	}

	public String addPartStore(InforContext context, PartStore partStoreParam) throws InforException {

		net.datastream.schemas.mp_entities.partstores_001.PartStores partStoresInfor = new net.datastream.schemas.mp_entities.partstores_001.PartStores();

		// PART ID
		if (partStoreParam.getPartCode() != null) {
			partStoresInfor.setPARTID(new PARTID_Type());
			partStoresInfor.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
			partStoresInfor.getPARTID().setPARTCODE(partStoreParam.getPartCode());
		}

		//
		if (partStoreParam.getDefaultBin() != null) {
			partStoresInfor.setDEFAULTBIN(new BINID_Type());
			partStoresInfor.getDEFAULTBIN().setBIN(partStoreParam.getDefaultBin());
		}

		//
		partStoresInfor.setLABELPRINTINGDEFAULT("");

		//
		if (partStoreParam.getOrderQty() != null) {
			partStoresInfor.setORDERQTY(tools.getDataTypeTools().encodeAmount(partStoreParam.getOrderQty(),"Ordery Qty."));
		}

		//
		if (partStoreParam.getReorderLevel() != null) {
			partStoresInfor.setREORDERLEVEL(tools.getDataTypeTools().encodeAmount(partStoreParam.getReorderLevel(),"Reorder Level"));
		}

		//
		if (partStoreParam.getDefaultReturnBin() != null) {
			partStoresInfor.setDEFAULTRETURNBIN(new BINID_Type());
			partStoresInfor.getDEFAULTRETURNBIN().setBIN(partStoreParam.getDefaultReturnBin());
		}

		//
		if (partStoreParam.getAbcClass() != null) {
			partStoresInfor.setABCCODE(partStoreParam.getAbcClass());
		}

		//
		if (partStoreParam.getPreferredSupplier() != null) {
			partStoresInfor.setPREFERREDSUPPLIER(new SUPPLIERID_Type());
			partStoresInfor.getPREFERREDSUPPLIER().setORGANIZATIONID(tools.getOrganization(context));
			partStoresInfor.getPREFERREDSUPPLIER().setSUPPLIERCODE(partStoreParam.getPreferredSupplier());
		}

		//
		if (partStoreParam.getStoreCode() != null) {
			partStoresInfor.setSTOREID(new STOREID_Type());
			partStoresInfor.getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
			partStoresInfor.getSTOREID().setSTORECODE(partStoreParam.getStoreCode());
		}

		//
		if (partStoreParam.getPreventIssueFromDefaultReturnBin() != null) {
			partStoresInfor.setPREVENTISSUEDEFRTNBIN(partStoreParam.getPreventIssueFromDefaultReturnBin());
		} else {
			partStoresInfor.setPREVENTISSUEDEFRTNBIN("true");
		}

		//
		if (partStoreParam.getPreferredStore() != null) {
			partStoresInfor.setPREFERREDSTORE(new STOREID_Type());
			partStoresInfor.getPREFERREDSTORE().setORGANIZATIONID(tools.getOrganization(context));
			partStoresInfor.getPREFERREDSTORE().setSTORECODE(partStoreParam.getPreferredStore());
		}

		//
		if (partStoreParam.getMinimumQty() != null) {
			partStoresInfor.setMINIMUMQTY(tools.getDataTypeTools().encodeAmount(partStoreParam.getMinimumQty(), "Minimum Qty"));
		}

		//
		if (partStoreParam.getStockMethod() != null) {
			partStoresInfor.setONDEMAND(partStoreParam.getStockMethod());
		}

		//
		MP0255_AddPartStores_001 addPartStores = new  MP0255_AddPartStores_001();
		addPartStores.setPartStores(partStoresInfor);

		if (context.getCredentials() != null) {
			inforws.addPartStoresOp(addPartStores, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addPartStoresOp(addPartStores, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return null;
	}

}


