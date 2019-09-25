package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.PartManufacturerService;
import ch.cern.eam.wshub.core.services.material.entities.PartManufacturer;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.BooleanType;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.MANUFACTURERID_Type;
import net.datastream.schemas.mp_fields.PARTID_Type;
import net.datastream.schemas.mp_fields.PARTMANUFACTURERID_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0261_001.MP0261_AddPartManufacturer_001;
import net.datastream.schemas.mp_functions.mp0262_001.MP0262_SyncPartManufacturer_001;
import net.datastream.schemas.mp_functions.mp0263_001.MP0263_DeletePartManufacturer_001;
import net.datastream.schemas.mp_functions.mp0264_001.MP0264_GetPartManufacturer_001;
import net.datastream.schemas.mp_results.mp0264_001.MP0264_GetPartManufacturer_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;

public class PartManufacturerServiceImpl implements PartManufacturerService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public PartManufacturerServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public String addPartManufacturer(InforContext context, PartManufacturer partManufacturerParam) throws InforException {
		net.datastream.schemas.mp_entities.partmanufacturer_001.PartManufacturer partManufacturerInfor = new net.datastream.schemas.mp_entities.partmanufacturer_001.PartManufacturer();

		//
		if (partManufacturerParam.getManufacturerPartNumber() != null) {
			partManufacturerInfor.setMANUFACTURERPARTCODE(partManufacturerParam.getManufacturerPartNumber());
		}

		//
		if (partManufacturerParam.getPartCode() != null) {
			partManufacturerInfor.setPARTID(new PARTID_Type());
			partManufacturerInfor.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
			partManufacturerInfor.getPARTID().setPARTCODE(partManufacturerParam.getPartCode().trim().toUpperCase());
		}

		//
		if (partManufacturerParam.getDrawingNumber() != null) {
			partManufacturerInfor.setMANUFACTURERDRAW(partManufacturerParam.getDrawingNumber());
		}
		//
		if (partManufacturerParam.getManufacturerCode() != null) {
			partManufacturerInfor.setMANUFACTURERID(new MANUFACTURERID_Type());
			partManufacturerInfor.getMANUFACTURERID().setORGANIZATIONID(tools.getOrganization(context));
			partManufacturerInfor.getMANUFACTURERID().setMANUFACTURERCODE(partManufacturerParam.getManufacturerCode().trim().toUpperCase());
		}

		//
		if (partManufacturerParam.getOutOfService() != null) {
			partManufacturerInfor.setOUTOFSERVICE(tools.getDataTypeTools().encodeBoolean(partManufacturerParam.getOutOfService(), BooleanType.TRUE_FALSE));
		}

		//
		if (partManufacturerParam.getPrimary() != null) {
			partManufacturerInfor.setISPRIMARY(tools.getDataTypeTools().encodeBoolean(partManufacturerParam.getPrimary(), BooleanType.TRUE_FALSE));
		}
		//
		// CALL INFOR WS
		//
		MP0261_AddPartManufacturer_001 addPartManufacturer = new  MP0261_AddPartManufacturer_001();
		addPartManufacturer.setPartManufacturer(partManufacturerInfor);
		if (context.getCredentials() != null) {
			inforws.addPartManufacturerOp(addPartManufacturer, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addPartManufacturerOp(addPartManufacturer,tools.getOrganizationCode(context),  null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		return partManufacturerParam.getManufacturerCode();
	}

	public String updatePartManufacturer(InforContext context, PartManufacturer partManufacturerParam) throws InforException {
		net.datastream.schemas.mp_entities.partmanufacturer_001.PartManufacturer partManufacturerInfor = new net.datastream.schemas.mp_entities.partmanufacturer_001.PartManufacturer();

		MP0264_GetPartManufacturer_001 getPartM = new MP0264_GetPartManufacturer_001();

		MP0264_GetPartManufacturer_001_Result result = new MP0264_GetPartManufacturer_001_Result();
		getPartM.setPARTMANUFACTURERID(new PARTMANUFACTURERID_Type());
		getPartM.getPARTMANUFACTURERID().setMANUFACTURERCODE(partManufacturerParam.getManufacturerCode());
		if (partManufacturerParam.getManufacturerPartNumber() != null && !partManufacturerParam.getManufacturerPartNumber().trim().equals("")) {
			getPartM.getPARTMANUFACTURERID().setMANUFACTURERPARTCODE(partManufacturerParam.getManufacturerPartNumber());
		}
		getPartM.getPARTMANUFACTURERID().setPARTID(new PARTID_Type() );
		getPartM.getPARTMANUFACTURERID().getPARTID().setORGANIZATIONID(tools.getOrganization(context));
		getPartM.getPARTMANUFACTURERID().getPARTID().setPARTCODE(partManufacturerParam.getPartCode());
		// first get it:
		if (context.getCredentials() != null) {
			result = inforws.getPartManufacturerOp(getPartM, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.getPartManufacturerOp(getPartM,tools.getOrganizationCode(context),  null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		partManufacturerInfor = result.getResultData().getPartManufacturer();
		//
		if (partManufacturerParam.getManufacturerPartNumberNew() != null) {
			partManufacturerInfor.setManufacturerpartcode_New(partManufacturerParam.getManufacturerPartNumberNew().trim());
		} else {
			partManufacturerInfor.setManufacturerpartcode_New("");
		}

		//
		if (partManufacturerParam.getDrawingNumber() != null) {
			partManufacturerInfor.setMANUFACTURERDRAW(partManufacturerParam.getDrawingNumber());
		}

		//
		if (partManufacturerParam.getOutOfService() != null) {
			partManufacturerInfor.setOUTOFSERVICE(tools.getDataTypeTools().encodeBoolean(partManufacturerParam.getOutOfService(), BooleanType.TRUE_FALSE));
		}

		//
		if (partManufacturerParam.getPrimary() != null) {
			partManufacturerInfor.setISPRIMARY(tools.getDataTypeTools().encodeBoolean(partManufacturerParam.getPrimary(), BooleanType.TRUE_FALSE));
		}
		//
		// CALL INFOR WS
		//
		MP0262_SyncPartManufacturer_001 syncPartManufacturer = new  MP0262_SyncPartManufacturer_001();
		syncPartManufacturer.setPartManufacturer(partManufacturerInfor);
		if (context.getCredentials() != null) {
			inforws.syncPartManufacturerOp(syncPartManufacturer, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.syncPartManufacturerOp(syncPartManufacturer,tools.getOrganizationCode(context),  null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		return partManufacturerParam.getManufacturerCode();
	}


	public String deletePartManufacturer(InforContext context, PartManufacturer partManufacturerParam) throws InforException {

		MP0263_DeletePartManufacturer_001 deletePartM = new MP0263_DeletePartManufacturer_001();

		deletePartM.setPARTMANUFACTURERID(new PARTMANUFACTURERID_Type());
		deletePartM.getPARTMANUFACTURERID().setMANUFACTURERCODE(partManufacturerParam.getManufacturerCode());
		deletePartM.getPARTMANUFACTURERID().setMANUFACTURERPARTCODE(partManufacturerParam.getManufacturerPartNumber());
		deletePartM.getPARTMANUFACTURERID().setPARTID(new PARTID_Type());
		deletePartM.getPARTMANUFACTURERID().getPARTID().setORGANIZATIONID(tools.getOrganization(context));
		deletePartM.getPARTMANUFACTURERID().getPARTID().setPARTCODE(partManufacturerParam.getPartCode());

		// first get it:
		if (context.getCredentials() != null) {
			inforws.deletePartManufacturerOp(deletePartM, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.deletePartManufacturerOp(deletePartM,tools.getOrganizationCode(context),  null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return partManufacturerParam.getManufacturerCode();
	}

}


