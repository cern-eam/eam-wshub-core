package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.material.PartManufacturerService;
import ch.cern.eam.wshub.core.services.material.entities.PartManufacturer;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.annotations.BooleanType;
import ch.cern.eam.wshub.core.tools.EAMException;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import jakarta.xml.ws.Holder;

public class PartManufacturerServiceImpl implements PartManufacturerService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public PartManufacturerServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}

	public String addPartManufacturer(EAMContext context, PartManufacturer partManufacturerParam) throws EAMException {
		net.datastream.schemas.mp_entities.partmanufacturer_001.PartManufacturer partManufacturerEAM = new net.datastream.schemas.mp_entities.partmanufacturer_001.PartManufacturer();

		//TRANSFORM
		tools.getEAMFieldTools().transformWSHubObject(partManufacturerEAM, partManufacturerParam, context);

		//
		// CALL EAM WS
		//
		MP0261_AddPartManufacturer_001 addPartManufacturer = new  MP0261_AddPartManufacturer_001();
		addPartManufacturer.setPartManufacturer(partManufacturerEAM);
		tools.performEAMOperation(context, eamws::addPartManufacturerOp, addPartManufacturer);

		return partManufacturerParam.getManufacturerCode();
	}

	public String updatePartManufacturer(EAMContext context, PartManufacturer partManufacturerParam) throws EAMException {
		net.datastream.schemas.mp_entities.partmanufacturer_001.PartManufacturer partManufacturerEAM = new net.datastream.schemas.mp_entities.partmanufacturer_001.PartManufacturer();

		MP0264_GetPartManufacturer_001 getPartM = new MP0264_GetPartManufacturer_001();

		getPartM.setPARTMANUFACTURERID(new PARTMANUFACTURERID_Type());
		getPartM.getPARTMANUFACTURERID().setMANUFACTURERCODE(partManufacturerParam.getManufacturerCode());
		if (partManufacturerParam.getManufacturerPartNumber() != null && !partManufacturerParam.getManufacturerPartNumber().trim().equals("")) {
			getPartM.getPARTMANUFACTURERID().setMANUFACTURERPARTCODE(partManufacturerParam.getManufacturerPartNumber());
		}
		getPartM.getPARTMANUFACTURERID().setPARTID(new PARTID_Type() );
		getPartM.getPARTMANUFACTURERID().getPARTID().setORGANIZATIONID(tools.getOrganization(context));
		getPartM.getPARTMANUFACTURERID().getPARTID().setPARTCODE(partManufacturerParam.getPartCode());
		// first get it:
		MP0264_GetPartManufacturer_001_Result result =
			tools.performEAMOperation(context, eamws::getPartManufacturerOp, getPartM);

		partManufacturerEAM = result.getResultData().getPartManufacturer();

		//FIELD ALWAYS HAS TO BE SET
		if (partManufacturerParam.getManufacturerPartNumberNew() == null) {
			partManufacturerParam.setManufacturerPartNumberNew(partManufacturerParam.getManufacturerPartNumber());
		}

		//TRANSFORM
		tools.getEAMFieldTools().transformWSHubObject(partManufacturerEAM, partManufacturerParam, context);

		//CALL EAM WS

		MP0262_SyncPartManufacturer_001 syncPartManufacturer = new  MP0262_SyncPartManufacturer_001();
		syncPartManufacturer.setPartManufacturer(partManufacturerEAM);
		tools.performEAMOperation(context, eamws::syncPartManufacturerOp, syncPartManufacturer);
		return partManufacturerParam.getManufacturerCode();
	}


	public String deletePartManufacturer(EAMContext context, PartManufacturer partManufacturerParam) throws EAMException {

		MP0263_DeletePartManufacturer_001 deletePartM = new MP0263_DeletePartManufacturer_001();

		deletePartM.setPARTMANUFACTURERID(new PARTMANUFACTURERID_Type());
		deletePartM.getPARTMANUFACTURERID().setMANUFACTURERCODE(partManufacturerParam.getManufacturerCode());
		deletePartM.getPARTMANUFACTURERID().setMANUFACTURERPARTCODE(partManufacturerParam.getManufacturerPartNumber());
		deletePartM.getPARTMANUFACTURERID().setPARTID(new PARTID_Type());
		deletePartM.getPARTMANUFACTURERID().getPARTID().setORGANIZATIONID(tools.getOrganization(context));
		deletePartM.getPARTMANUFACTURERID().getPARTID().setPARTCODE(partManufacturerParam.getPartCode());

		// first get it:
		tools.performEAMOperation(context, eamws::deletePartManufacturerOp, deletePartM);

		return partManufacturerParam.getManufacturerCode();
	}

}


