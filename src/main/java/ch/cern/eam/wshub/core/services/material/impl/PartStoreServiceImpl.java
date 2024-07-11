package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.material.PartStoreService;
import ch.cern.eam.wshub.core.services.material.entities.PartStore;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0254_001.MP0254_GetPartStores_001;
import net.datastream.schemas.mp_functions.mp0255_001.MP0255_AddPartStores_001;
import net.datastream.schemas.mp_functions.mp0256_001.MP0256_SyncPartStores_001;
import net.datastream.schemas.mp_results.mp0254_001.MP0254_GetPartStores_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import jakarta.xml.ws.Holder;


public class PartStoreServiceImpl implements PartStoreService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public PartStoreServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}

	public String updatePartStore(EAMContext context, PartStore partStoreParam) throws EAMException {


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

		MP0254_GetPartStores_001_Result result =
			tools.performEAMOperation(context, eamws::getPartStoresOp, getPartStores);
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

		tools.performEAMOperation(context, eamws::syncPartStoresOp, syncPartStores);
		return null;
	}

	public String addPartStore(EAMContext context, PartStore partStoreParam) throws EAMException {

		net.datastream.schemas.mp_entities.partstores_001.PartStores partStoresEAM = new net.datastream.schemas.mp_entities.partstores_001.PartStores();

		// PART ID
		if (partStoreParam.getPartCode() != null) {
			partStoresEAM.setPARTID(new PARTID_Type());
			partStoresEAM.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
			partStoresEAM.getPARTID().setPARTCODE(partStoreParam.getPartCode());
		}

		//
		if (partStoreParam.getDefaultBin() != null) {
			partStoresEAM.setDEFAULTBIN(new BINID_Type());
			partStoresEAM.getDEFAULTBIN().setBIN(partStoreParam.getDefaultBin());
		}

		//
		partStoresEAM.setLABELPRINTINGDEFAULT("");

		//
		if (partStoreParam.getOrderQty() != null) {
			partStoresEAM.setORDERQTY(tools.getDataTypeTools().encodeAmount(partStoreParam.getOrderQty(),"Ordery Qty."));
		}

		//
		if (partStoreParam.getReorderLevel() != null) {
			partStoresEAM.setREORDERLEVEL(tools.getDataTypeTools().encodeAmount(partStoreParam.getReorderLevel(),"Reorder Level"));
		}

		//
		if (partStoreParam.getDefaultReturnBin() != null) {
			partStoresEAM.setDEFAULTRETURNBIN(new BINID_Type());
			partStoresEAM.getDEFAULTRETURNBIN().setBIN(partStoreParam.getDefaultReturnBin());
		}

		//
		if (partStoreParam.getAbcClass() != null) {
			partStoresEAM.setABCCODE(partStoreParam.getAbcClass());
		}

		//
		if (partStoreParam.getPreferredSupplier() != null) {
			partStoresEAM.setPREFERREDSUPPLIER(new SUPPLIERID_Type());
			partStoresEAM.getPREFERREDSUPPLIER().setORGANIZATIONID(tools.getOrganization(context));
			partStoresEAM.getPREFERREDSUPPLIER().setSUPPLIERCODE(partStoreParam.getPreferredSupplier());
		}

		//
		if (partStoreParam.getStoreCode() != null) {
			partStoresEAM.setSTOREID(new STOREID_Type());
			partStoresEAM.getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
			partStoresEAM.getSTOREID().setSTORECODE(partStoreParam.getStoreCode());
		}

		//
		if (partStoreParam.getPreventIssueFromDefaultReturnBin() != null) {
			partStoresEAM.setPREVENTISSUEDEFRTNBIN(partStoreParam.getPreventIssueFromDefaultReturnBin());
		} else {
			partStoresEAM.setPREVENTISSUEDEFRTNBIN("true");
		}

		//
		if (partStoreParam.getPreferredStore() != null) {
			partStoresEAM.setPREFERREDSTORE(new STOREID_Type());
			partStoresEAM.getPREFERREDSTORE().setORGANIZATIONID(tools.getOrganization(context));
			partStoresEAM.getPREFERREDSTORE().setSTORECODE(partStoreParam.getPreferredStore());
		}

		//
		if (partStoreParam.getMinimumQty() != null) {
			partStoresEAM.setMINIMUMQTY(tools.getDataTypeTools().encodeAmount(partStoreParam.getMinimumQty(), "Minimum Qty"));
		}

		//
		if (partStoreParam.getStockMethod() != null) {
			partStoresEAM.setONDEMAND(partStoreParam.getStockMethod());
		}

		//
		MP0255_AddPartStores_001 addPartStores = new  MP0255_AddPartStores_001();
		addPartStores.setPartStores(partStoresEAM);

		tools.performEAMOperation(context, eamws::addPartStoresOp, addPartStores);
		return null;
	}

}


