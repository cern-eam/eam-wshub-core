package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.PartService;
import ch.cern.eam.wshub.core.services.material.entities.Part;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0240_001.MP0240_AddPart_001;
import net.datastream.schemas.mp_functions.mp0241_001.MP0241_GetPart_001;
import net.datastream.schemas.mp_functions.mp0242_001.MP0242_SyncPart_001;
import net.datastream.schemas.mp_functions.mp0243_001.MP0243_DeletePart_001;
import net.datastream.schemas.mp_functions.mp0244_001.MP0244_GetPartDefault_001;
import net.datastream.schemas.mp_functions.mp2072_001.ChangePartNumber;
import net.datastream.schemas.mp_functions.mp2072_001.MP2072_ChangePartNumber_001;
import net.datastream.schemas.mp_results.mp0240_001.MP0240_AddPart_001_Result;
import net.datastream.schemas.mp_results.mp0241_001.MP0241_GetPart_001_Result;
import net.datastream.schemas.mp_results.mp0242_001.MP0242_SyncPart_001_Result;
import net.datastream.schemas.mp_results.mp0244_001.MP0244_GetPartDefault_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;

public class PartServiceImpl implements PartService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public PartServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public Part readPartDefault(InforContext context, String organization) throws InforException {
		MP0244_GetPartDefault_001 getPartDefault_001 = new MP0244_GetPartDefault_001();
		if (isEmpty(organization)) {
			getPartDefault_001.setORGANIZATIONID(tools.getOrganization(context));
		} else {
			getPartDefault_001.setORGANIZATIONID(new ORGANIZATIONID_Type());
			getPartDefault_001.getORGANIZATIONID().setORGANIZATIONCODE(organization);
		}

		MP0244_GetPartDefault_001_Result result =
				tools.performInforOperation(context, inforws::getPartDefaultOp, getPartDefault_001);

		return tools.getInforFieldTools().transformInforObject(new Part(), result.getResultData().getPartDefault());
	}

	public Part readPart(InforContext context, String partCode) throws InforException {
		Part part = tools.getInforFieldTools().transformInforObject(new Part(), readPartInfor(context, partCode));

		// Fetched missing descriptions not returned by Infor web service
		tools.processRunnables(
			() -> part.setClassDesc(tools.getFieldDescriptionsTools().readClassDesc(context, "PART", part.getClassCode())),
			() -> part.setCategoryDesc(tools.getFieldDescriptionsTools().readCategoryDesc(context, part.getCategoryCode())),
			() -> part.setUOMDesc(tools.getFieldDescriptionsTools().readUOMDesc(context, part.getUOM())),
			() -> part.setCommodityDesc(tools.getFieldDescriptionsTools().readCommodityDesc(context,  part.getCommodityCode()))
		);

		return part;
	}

	private net.datastream.schemas.mp_entities.part_001.Part readPartInfor(InforContext context, String partCode) throws InforException {
		MP0241_GetPart_001 getPart = new MP0241_GetPart_001();
		getPart.setPARTID(new PARTID_Type());
		getPart.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
		getPart.getPARTID().setPARTCODE(partCode);

		MP0241_GetPart_001_Result getPartResult =
			tools.performInforOperation(context, inforws::getPartOp, getPart);

		return getPartResult.getResultData().getPart();
	}

	public String createPart(InforContext context, Part partParam) throws InforException {
		net.datastream.schemas.mp_entities.part_001.Part inforPart = new net.datastream.schemas.mp_entities.part_001.Part();
		//
		//
		//
		inforPart.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
			context,
			toCodeString(inforPart.getCLASSID()),
			inforPart.getUSERDEFINEDAREA(),
			partParam.getClassCode(),
			"PART"));

		// POPULATE ALL OTHER FIELDS
		tools.getInforFieldTools().transformWSHubObject(inforPart, partParam, context);

		MP0240_AddPart_001 addPart = new MP0240_AddPart_001();
		addPart.setPart(inforPart);

		MP0240_AddPart_001_Result result =
			tools.performInforOperation(context, inforws::addPartOp, addPart);

		return result.getPARTID().getPARTCODE();
	}

	public String updatePart(InforContext context, Part partParam) throws InforException {

		if (partParam.getNewCode() != null && !partParam.getNewCode().trim().equals("")) {

			MP2072_ChangePartNumber_001 changePartNumber = new MP2072_ChangePartNumber_001();
			changePartNumber.setChangePartNumber(new ChangePartNumber());

			changePartNumber.getChangePartNumber().setOLDPARTID(new PARTID_Type());
			changePartNumber.getChangePartNumber().getOLDPARTID().setORGANIZATIONID(tools.getOrganization(context));
			changePartNumber.getChangePartNumber().getOLDPARTID().setPARTCODE(partParam.getCode());

			changePartNumber.getChangePartNumber().setNEWPARTID(new PARTID_Type());
			changePartNumber.getChangePartNumber().getNEWPARTID().setORGANIZATIONID(tools.getOrganization(context));
			changePartNumber.getChangePartNumber().getNEWPARTID().setPARTCODE(partParam.getNewCode());

			tools.performInforOperation(context, inforws::changePartNumberOp, changePartNumber);

			partParam.setCode(partParam.getNewCode());

		}

		net.datastream.schemas.mp_entities.part_001.Part inforPart = readPartInfor(context, partParam.getCode());

		inforPart.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
			context,
			toCodeString(inforPart.getCLASSID()),
			inforPart.getUSERDEFINEDAREA(),
			partParam.getClassCode(),
			"PART"));

		// SET ALL PROPERTIES
		tools.getInforFieldTools().transformWSHubObject(inforPart, partParam, context);
		//
		// UPDATE PART
		//
		MP0242_SyncPart_001 syncPart = new MP0242_SyncPart_001();
		syncPart.setPart(inforPart);

		MP0242_SyncPart_001_Result result =
			tools.performInforOperation(context, inforws::syncPartOp, syncPart);

		return result.getResultData().getPart().getPARTID().getPARTCODE();

	}

	public String deletePart(InforContext context, String partCode) throws InforException {
		MP0243_DeletePart_001 deletePart = new MP0243_DeletePart_001();
		deletePart.setPARTID(new PARTID_Type());
		deletePart.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
		deletePart.getPARTID().setPARTCODE(partCode);

		tools.performInforOperation(context, inforws::deletePartOp, deletePart);
		return partCode;
	}

}
