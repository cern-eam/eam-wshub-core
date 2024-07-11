package ch.cern.eam.wshub.core.services.material.impl;

import java.math.BigDecimal;
import jakarta.xml.ws.Holder;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.material.PartKitService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import org.openapplications.oagis_segments.QUANTITY;
import ch.cern.eam.wshub.core.services.material.entities.BuildKitParam;
import ch.cern.eam.wshub.core.services.material.entities.PartKitTemplate;
import net.datastream.schemas.mp_entities.kitsession_001.KitSession;
import net.datastream.schemas.mp_entities.kittemplate_001.KitTemplate;
import net.datastream.schemas.mp_fields.BINID_Type;
import net.datastream.schemas.mp_fields.KITID_Type;
import net.datastream.schemas.mp_fields.PARTID_Type;
import net.datastream.schemas.mp_fields.STOREID_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp2227_001.MP2227_AddKitTemplate_001;
import net.datastream.schemas.mp_functions.mp2231_001.MP2231_CreateKitSession_001;
import net.datastream.schemas.mp_functions.mp2235_001.MP2235_CreateKit_001;
import net.datastream.schemas.mp_results.mp2231_001.MP2231_CreateKitSession_001_Result;
import net.datastream.schemas.mp_results.mp2235_001.MP2235_CreateKit_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

public class PartKitServiceImpl implements PartKitService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public PartKitServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}

	@Override
	public String addPartKitTemplate(EAMContext context, PartKitTemplate partKitParam) throws EAMException {

		KitTemplate kitTemplate = new KitTemplate();
		
		kitTemplate.setKITID(new KITID_Type());
		
		// PART CODE
		if (partKitParam.getPartCode() != null) {
			kitTemplate.getKITID().setKITTEMPLATEPARTID(new PARTID_Type());
			kitTemplate.getKITID().getKITTEMPLATEPARTID().setORGANIZATIONID(tools.getOrganization(context));
			kitTemplate.getKITID().getKITTEMPLATEPARTID().setPARTCODE(partKitParam.getPartCode().trim().toUpperCase());
		}

		// KIT CODE
		if (partKitParam.getKitTemplatePartCode() != null) {
			kitTemplate.getKITID().setPARTID(new PARTID_Type());
			kitTemplate.getKITID().getPARTID().setORGANIZATIONID(tools.getOrganization(context));
			kitTemplate.getKITID().getPARTID().setPARTCODE(partKitParam.getKitTemplatePartCode().trim().toUpperCase());
		}
		
		// UOM
		if (partKitParam.getUomCode() != null) {
			kitTemplate.setUOMCODE(partKitParam.getUomCode());
		}

		// QUANTITY
		kitTemplate.setKITQUANTITY(new QUANTITY());
		kitTemplate.getKITQUANTITY().setNUMOFDEC(new BigDecimal(0).toBigInteger());
		kitTemplate.getKITQUANTITY().setSIGN("+");
		kitTemplate.getKITQUANTITY().setUOM("default");
		kitTemplate.getKITQUANTITY().setQualifier("OTHER");
		
		if (partKitParam.getQty() != null) {
			kitTemplate.getKITQUANTITY().setVALUE(new BigDecimal(partKitParam.getQty()));
		}

		MP2227_AddKitTemplate_001 addKitTemplate = new MP2227_AddKitTemplate_001();
		addKitTemplate.setKitTemplate(kitTemplate);

		tools.performEAMOperation(context, eamws::addKitTemplateOp, addKitTemplate);

		return null;
	}	


	public String createKitSession(EAMContext context, BuildKitParam buildKitParam) throws EAMException{

		// CREATE KIT SESSION
		
		KitSession kitSession = new KitSession();
		
		kitSession.setBINID(new BINID_Type());
		kitSession.getBINID().setBIN(buildKitParam.getKitBin().toUpperCase().trim());
		
		kitSession.setDBSESSIONID(new QUANTITY());
		kitSession.getDBSESSIONID().setVALUE(new BigDecimal(0));
		kitSession.getDBSESSIONID().setNUMOFDEC(new BigDecimal(0).toBigInteger());
		kitSession.getDBSESSIONID().setSIGN("+");
		kitSession.getDBSESSIONID().setUOM("default");
		kitSession.getDBSESSIONID().setQualifier("OTHER");
		
		kitSession.setKITCOUNT(new QUANTITY());
		kitSession.getKITCOUNT().setVALUE(new BigDecimal(1));
		kitSession.getKITCOUNT().setNUMOFDEC(new BigDecimal(0).toBigInteger());
		kitSession.getKITCOUNT().setSIGN("+");
		kitSession.getKITCOUNT().setUOM("default");
		kitSession.getKITCOUNT().setQualifier("OTHER");
		
		kitSession.setLOTDESCRIPTION("*");
		
		kitSession.setPARTID(new PARTID_Type());
		kitSession.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
		kitSession.getPARTID().setPARTCODE(buildKitParam.getPartKitCode().trim().toUpperCase());
		
		kitSession.setSTOREID(new STOREID_Type());
		kitSession.getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
		kitSession.getSTOREID().setSTORECODE(buildKitParam.getStoreCode().trim().toUpperCase());
		
		MP2231_CreateKitSession_001 kitSessionMsg = new MP2231_CreateKitSession_001();
		kitSessionMsg.setKitSession(kitSession);

		MP2231_CreateKitSession_001_Result r =
			tools.performEAMOperation(context, eamws::createKitSessionOp, kitSessionMsg);

		return r.getResultData().getDBSESSIONID().getVALUE().toString();
	}


	public String buildKit(EAMContext context, String kitSessionId) throws EAMException{
		MP2235_CreateKit_001 createKitMsg = new MP2235_CreateKit_001();
		MP2235_CreateKit_001_Result createKitResult = new MP2235_CreateKit_001_Result();
		createKitMsg.setDBSESSIONID(new QUANTITY());
		createKitMsg.getDBSESSIONID().setVALUE(new BigDecimal(kitSessionId));
		createKitMsg.getDBSESSIONID().setNUMOFDEC(new BigDecimal(0).toBigInteger());
		createKitMsg.getDBSESSIONID().setSIGN("+");
		createKitMsg.getDBSESSIONID().setUOM("default");
		createKitMsg.getDBSESSIONID().setQualifier("OTHER");
		tools.performEAMOperation(context, eamws::createKitOp, createKitMsg);

		return createKitResult.getResultData().getDBSESSIONID().getVALUE().toString();
	}
	
}
