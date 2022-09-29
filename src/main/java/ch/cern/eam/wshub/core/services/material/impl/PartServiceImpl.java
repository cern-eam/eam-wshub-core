package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.material.PartService;
import ch.cern.eam.wshub.core.services.material.entities.Part;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.EntityId;
import ch.cern.eam.wshub.core.services.userdefinedscreens.impl.UserDefinedListServiceImpl;
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
import java.util.HashMap;
import java.util.List;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;
import static ch.cern.eam.wshub.core.tools.Tools.extractEntityCode;
import static ch.cern.eam.wshub.core.tools.Tools.extractOrganizationCode;

public class PartServiceImpl implements PartService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;
	private UserDefinedListService userDefinedListService;

	public PartServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.userDefinedListService = new UserDefinedListServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
	}

	//
	// BATCH WEB SERVICES
	//

	public BatchResponse<String> createPartBatch(InforContext context, List<Part> parts) {
		return tools.batchOperation(context, this::createPart, parts);
	}

	public BatchResponse<Part> readPartBatch(InforContext context, List<String> partCodes)  {
		return tools.batchOperation(context, this::readPart, partCodes);
	}

	public BatchResponse<String> updatePartBatch(InforContext context, List<Part> parts) {
		return tools.batchOperation(context, this::updatePart, parts);
	}

	public BatchResponse<String> deletePartBatch(InforContext context, List<String> partCodes) {
		return tools.batchOperation(context, this::deletePart, partCodes);
	}

	//
	//
	//

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

		Part part = tools.getInforFieldTools().transformInforObject(new Part(), result.getResultData().getPartDefault());
		part.setUserDefinedList(new HashMap<>());
		return part;
	}

	public Part readPart(InforContext context, String partCode) throws InforException {
		Part part = tools.getInforFieldTools().transformInforObject(new Part(), readPartInfor(context, extractEntityCode(partCode), extractOrganizationCode(partCode)));

		// Fetched missing descriptions not returned by Infor web service
		tools.processRunnables(
			() -> part.setClassDesc(tools.getFieldDescriptionsTools().readClassDesc(context, "PART", part.getClassCode())),
			() -> part.setCategoryDesc(tools.getFieldDescriptionsTools().readCategoryDesc(context, part.getCategoryCode())),
			() -> part.setUOMDesc(tools.getFieldDescriptionsTools().readUOMDesc(context, part.getUOM())),
			() -> part.setCommodityDesc(tools.getFieldDescriptionsTools().readCommodityDesc(context,  part.getCommodityCode())),
			() -> userDefinedListService.readUDLToEntity(context, part, new EntityId("PART", extractEntityCode(partCode)))
		);

		return part;
	}

	private net.datastream.schemas.mp_entities.part_001.Part readPartInfor(InforContext context, String partCode, String organization) throws InforException {
		MP0241_GetPart_001 getPart = new MP0241_GetPart_001();
		getPart.setPARTID(new PARTID_Type());
		getPart.getPARTID().setORGANIZATIONID(tools.getOrganization(context, organization));
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

		String partCode = result.getPARTID().getPARTCODE();
		userDefinedListService.writeUDLToEntityCopyFrom(context, partParam, new EntityId("PART", partCode));
		return partCode;
	}

	public String updatePart(InforContext context, Part partParam) throws InforException {

		if (partParam.getNewCode() != null && !partParam.getNewCode().trim().equals("")) {

			MP2072_ChangePartNumber_001 changePartNumber = new MP2072_ChangePartNumber_001();
			changePartNumber.setChangePartNumber(new ChangePartNumber());

			changePartNumber.getChangePartNumber().setOLDPARTID(new PARTID_Type());
			changePartNumber.getChangePartNumber().getOLDPARTID().setORGANIZATIONID(tools.getOrganization(context, partParam.getOrganization()));
			changePartNumber.getChangePartNumber().getOLDPARTID().setPARTCODE(partParam.getCode());

			changePartNumber.getChangePartNumber().setNEWPARTID(new PARTID_Type());
			changePartNumber.getChangePartNumber().getNEWPARTID().setORGANIZATIONID(tools.getOrganization(context, partParam.getOrganization()));
			changePartNumber.getChangePartNumber().getNEWPARTID().setPARTCODE(partParam.getNewCode());

			tools.performInforOperation(context, inforws::changePartNumberOp, changePartNumber);

			partParam.setCode(partParam.getNewCode());

		}

		net.datastream.schemas.mp_entities.part_001.Part inforPart = readPartInfor(context, partParam.getCode(), partParam.getOrganization());

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

		String partCode = result.getResultData().getPart().getPARTID().getPARTCODE();
		userDefinedListService.writeUDLToEntity(context, partParam, new EntityId("PART", partCode));
		return partCode;

	}

	public String deletePart(InforContext context, String partCode) throws InforException {
		MP0243_DeletePart_001 deletePart = new MP0243_DeletePart_001();
		deletePart.setPARTID(new PARTID_Type());
		deletePart.getPARTID().setORGANIZATIONID(tools.getOrganization(context, extractOrganizationCode(partCode)));
		deletePart.getPARTID().setPARTCODE(extractEntityCode(partCode));

		tools.performInforOperation(context, inforws::deletePartOp, deletePart);
		userDefinedListService.deleteUDLFromEntity(context, new EntityId("PART", partCode));
		return partCode;
	}

}
