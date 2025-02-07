package ch.cern.eam.wshub.core.services.material.impl;


import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import ch.cern.eam.wshub.core.services.material.PurchaseOrdersService;
import ch.cern.eam.wshub.core.services.material.entities.PurchaseOrder;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.tools.EAMException;
import net.datastream.schemas.mp_fields.PURCHASEORDERID_Type;
import net.datastream.schemas.mp_fields.STATUS_Type;
import net.datastream.schemas.mp_functions.mp0413_001.MP0413_GetPurchaseOrder_001;
import net.datastream.schemas.mp_functions.mp0415_001.MP0415_SyncPurchaseOrder_001;
import net.datastream.schemas.mp_results.mp0413_001.MP0413_GetPurchaseOrder_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

public class PurchaseOrdersImpl implements PurchaseOrdersService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public PurchaseOrdersImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}
	
	@Override
	public String updatePurchaseOrder(EAMContext context, PurchaseOrder purchaseOrderParam)
			throws EAMException {
		//
		
		MP0413_GetPurchaseOrder_001 req = new MP0413_GetPurchaseOrder_001();
		req.setPURCHASEORDERID(new PURCHASEORDERID_Type());
		req.getPURCHASEORDERID().setPURCHASEORDERCODE(purchaseOrderParam.getPurchaseOrderId());
		req.getPURCHASEORDERID().setORGANIZATIONID(tools.getOrganization(context));
		
		MP0413_GetPurchaseOrder_001_Result getPOResult =
			tools.performEAMOperation(context, eamws::getPurchaseOrderOp, req);
		net.datastream.schemas.mp_entities.purchaseorder_001.PurchaseOrder eamPurchaseOrder = getPOResult.getResultData()
				.getPurchaseOrder();
		
		//
		// SET ALL PROPERTIES
		//
		this.initializeEAMPOObject(eamPurchaseOrder, purchaseOrderParam);
		
		//
		// CALL EAM WEB SERVICE
		//
		MP0415_SyncPurchaseOrder_001 syncPO = new MP0415_SyncPurchaseOrder_001();
		syncPO.setPurchaseOrder(eamPurchaseOrder);
		tools.performEAMOperation(context, eamws::syncPurchaseOrderOp, syncPO);

		return eamPurchaseOrder.getPURCHASEORDERID().getPURCHASEORDERCODE();
	}
	
	private void initializeEAMPOObject(net.datastream.schemas.mp_entities.purchaseorder_001.PurchaseOrder eamPO,
			PurchaseOrder po) throws EAMException {

		if (eamPO.getUserDefinedFields() == null) {
			eamPO.setUserDefinedFields(new net.datastream.schemas.mp_entities.purchaseorder_001.UserDefinedFields());
		}
		
		initializeEAMPOObject(eamPO.getUserDefinedFields(), po.getUserDefinedFields());
		
		// STATUS
		if (po.getStatusCode() != null) {
			eamPO.setSTATUS(new STATUS_Type());
			eamPO.getSTATUS().setSTATUSCODE(po.getStatusCode().trim());
		} 
		
	}
	
	private void initializeEAMPOObject(net.datastream.schemas.mp_entities.purchaseorder_001.UserDefinedFields eamUserDefinedFields, UserDefinedFields userDefinedFields) throws EAMException {
		
		if (userDefinedFields.getUdfchar01() != null) {
			eamUserDefinedFields.setUDFCHAR01(userDefinedFields.getUdfchar01());
		}

		if (userDefinedFields.getUdfchar02() != null) {
			eamUserDefinedFields.setUDFCHAR02(userDefinedFields.getUdfchar02());
		}

		if (userDefinedFields.getUdfchar03() != null) {
			eamUserDefinedFields.setUDFCHAR03(userDefinedFields.getUdfchar03());
		}

		if (userDefinedFields.getUdfchar04() != null) {
			eamUserDefinedFields.setUDFCHAR04(userDefinedFields.getUdfchar04());
		}

		if (userDefinedFields.getUdfchar05() != null) {
			eamUserDefinedFields.setUDFCHAR05(userDefinedFields.getUdfchar05());
		}

		if (userDefinedFields.getUdfchar06() != null) {
			eamUserDefinedFields.setUDFCHAR06(userDefinedFields.getUdfchar06());
		}

		if (userDefinedFields.getUdfchar07() != null) {
			eamUserDefinedFields.setUDFCHAR07(userDefinedFields.getUdfchar07());
		}

		if (userDefinedFields.getUdfchar08() != null) {
			eamUserDefinedFields.setUDFCHAR08(userDefinedFields.getUdfchar08());
		}

		if (userDefinedFields.getUdfchar09() != null) {
			eamUserDefinedFields.setUDFCHAR09(userDefinedFields.getUdfchar09());
		}

		if (userDefinedFields.getUdfchar10() != null) {
			eamUserDefinedFields.setUDFCHAR10(userDefinedFields.getUdfchar10());
		}

		if (userDefinedFields.getUdfchar11() != null) {
			eamUserDefinedFields.setUDFCHAR11(userDefinedFields.getUdfchar11());
		}

		if (userDefinedFields.getUdfchar12() != null) {
			eamUserDefinedFields.setUDFCHAR12(userDefinedFields.getUdfchar12());
		}

		if (userDefinedFields.getUdfchar13() != null) {
			eamUserDefinedFields.setUDFCHAR13(userDefinedFields.getUdfchar13());
		}

		if (userDefinedFields.getUdfchar14() != null) {
			eamUserDefinedFields.setUDFCHAR14(userDefinedFields.getUdfchar14());
		}

		if (userDefinedFields.getUdfchar15() != null) {
			eamUserDefinedFields.setUDFCHAR15(userDefinedFields.getUdfchar15());
		}

		if (userDefinedFields.getUdfchar16() != null) {
			eamUserDefinedFields.setUDFCHAR16(userDefinedFields.getUdfchar16());
		}

		if (userDefinedFields.getUdfchar17() != null) {
			eamUserDefinedFields.setUDFCHAR17(userDefinedFields.getUdfchar17());
		}

		if (userDefinedFields.getUdfchar18() != null) {
			eamUserDefinedFields.setUDFCHAR18(userDefinedFields.getUdfchar18());
		}

		if (userDefinedFields.getUdfchar19() != null) {
			eamUserDefinedFields.setUDFCHAR19(userDefinedFields.getUdfchar19());
		}

		if (userDefinedFields.getUdfchar20() != null) {
			eamUserDefinedFields.setUDFCHAR20(userDefinedFields.getUdfchar20());
		}

		if (userDefinedFields.getUdfchar21() != null) {
			eamUserDefinedFields.setUDFCHAR21(userDefinedFields.getUdfchar21());
		}

		if (userDefinedFields.getUdfchar22() != null) {
			eamUserDefinedFields.setUDFCHAR22(userDefinedFields.getUdfchar22());
		}

		if (userDefinedFields.getUdfchar23() != null) {
			eamUserDefinedFields.setUDFCHAR23(userDefinedFields.getUdfchar23());
		}

		if (userDefinedFields.getUdfchar24() != null) {
			eamUserDefinedFields.setUDFCHAR24(userDefinedFields.getUdfchar24());
		}

		if (userDefinedFields.getUdfchar25() != null) {
			eamUserDefinedFields.setUDFCHAR25(userDefinedFields.getUdfchar25());
		}

		if (userDefinedFields.getUdfchar26() != null) {
			eamUserDefinedFields.setUDFCHAR26(userDefinedFields.getUdfchar26());
		}

		if (userDefinedFields.getUdfchar27() != null) {
			eamUserDefinedFields.setUDFCHAR27(userDefinedFields.getUdfchar27());
		}

		if (userDefinedFields.getUdfchar28() != null) {
			eamUserDefinedFields.setUDFCHAR28(userDefinedFields.getUdfchar28());
		}

		if (userDefinedFields.getUdfchar29() != null) {
			eamUserDefinedFields.setUDFCHAR29(userDefinedFields.getUdfchar29());
		}

		if (userDefinedFields.getUdfchar30() != null) {
			eamUserDefinedFields.setUDFCHAR30(userDefinedFields.getUdfchar30());
		}
	}

}
