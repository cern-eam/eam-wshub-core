package ch.cern.eam.wshub.core.services.material.impl;


import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.PartService;
import ch.cern.eam.wshub.core.services.material.entities.Part;
import ch.cern.eam.wshub.core.tools.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.BooleanType;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.part_001.UserDefinedFields;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0240_001.MP0240_AddPart_001;
import net.datastream.schemas.mp_functions.mp0241_001.MP0241_GetPart_001;
import net.datastream.schemas.mp_functions.mp0242_001.MP0242_SyncPart_001;
import net.datastream.schemas.mp_functions.mp0243_001.MP0243_DeletePart_001;
import net.datastream.schemas.mp_functions.mp2072_001.ChangePartNumber;
import net.datastream.schemas.mp_functions.mp2072_001.MP2072_ChangePartNumber_001;
import net.datastream.schemas.mp_results.mp0240_001.MP0240_AddPart_001_Result;
import net.datastream.schemas.mp_results.mp0241_001.MP0241_GetPart_001_Result;
import net.datastream.schemas.mp_results.mp0242_001.MP0242_SyncPart_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;

public class PartServiceImpl implements PartService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public PartServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public Part readPart(InforContext context, String partCode) throws InforException {
		//
		//
		//
		MP0241_GetPart_001_Result getPartResult = null;

		MP0241_GetPart_001 getPart = new MP0241_GetPart_001();
		getPart.setPARTID(new PARTID_Type());
		getPart.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
		getPart.getPARTID().setPARTCODE(partCode);

		if (context.getCredentials() != null) {
			getPartResult = inforws.getPartOp(getPart, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));

		} else {
			getPartResult = inforws.getPartOp(getPart, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		net.datastream.schemas.mp_entities.part_001.Part partInfor = getPartResult.getResultData().getPart();
		//
		// Populate 'Part' Object
		//
		Part part = new Part();
		//
		//
		//
		if (partInfor.getPARTID() != null) {
			part.setCode(partInfor.getPARTID().getPARTCODE());
			part.setDescription(partInfor.getPARTID().getDESCRIPTION());
		}

		//
		if (partInfor.getCLASSID() != null) {
			part.setClassCode(partInfor.getCLASSID().getCLASSCODE());
			part.setClassDesc(tools.getFieldDescriptionsTools().readClassDesc("PART", part.getClassCode()));
		}

		//
		if (partInfor.getUOMID() != null) {
			part.setUOM(partInfor.getUOMID().getUOMCODE());
			part.setUOMDesc(tools.getFieldDescriptionsTools().readUOMDesc(part.getUOM()));
		}

		//
		if (partInfor.getCATEGORYID() != null) {
			part.setCategoryCode(partInfor.getCATEGORYID().getCATEGORYCODE());
			part.setCategoryDesc(tools.getFieldDescriptionsTools().readCategoryDesc(part.getCategoryCode()));
		}

		//
		if (partInfor.getPRIMARYCOMMODITY() != null) {
			part.setCommodityCode(partInfor.getPRIMARYCOMMODITY().getCOMMODITYCODE());
			part.setCommodityDesc(tools.getFieldDescriptionsTools().readCommodityDesc(part.getCommodityCode()));
		}

		//
		if (partInfor.getTRACKMETHOD() != null) {
			part.setTrackingMethod(partInfor.getTRACKMETHOD().getTYPECODE());
		}

		// TRACK BY ASSET
		if (partInfor.getBYASSET() != null) {
			part.setTrackByAsset(tools.getDataTypeTools().decodeBoolean(partInfor.getBYASSET()));
		}

		// TRACK CORES
		if (partInfor.getREPAIRABLE() != null) {
			part.setTrackCores(tools.getDataTypeTools().decodeBoolean(partInfor.getREPAIRABLE()));
		}

		// TRACK BY LOT
		if (partInfor.getBYLOT() != null) {
			part.setTrackByLot(tools.getDataTypeTools().decodeBoolean(partInfor.getBYLOT()));
		}

		// Long description
		if (partInfor.getLONGDESCRIPTION() != null) {
			part.setLongDescription(partInfor.getLONGDESCRIPTION());
		}

		// CUSTOM FIELDS
		part.setCustomFields(tools.getCustomFieldsTools().readInforCustomFields(partInfor.getUSERDEFINEDAREA()));

		// USER DEFINED FIELDS
		part.setUserDefinedFields(tools.getUDFTools().readInforUserDefinedFields(partInfor.getUserDefinedFields()));

		return part;
	}

	public String createPart(InforContext context, Part partParam) throws InforException {
		net.datastream.schemas.mp_entities.part_001.Part partInfor = new net.datastream.schemas.mp_entities.part_001.Part();
		//
		//
		//
		if (partParam.getCustomFields() != null && partParam.getCustomFields().length > 0) {
			if (partParam.getClassCode() != null && !partParam.getClassCode().trim().equals("")) {
				partInfor.setUSERDEFINEDAREA(
						tools.getCustomFieldsTools().getInforCustomFields(context, "PART", partParam.getClassCode()));
			} else {
				partInfor.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(context, "PART", "*"));
			}
		}
		//
		partInfor.setUserDefinedFields(new UserDefinedFields());
		//
		initializeInforPartObject(partInfor, partParam, context);

		MP0240_AddPart_001 addPart = new MP0240_AddPart_001();
		addPart.setPart(partInfor);

		MP0240_AddPart_001_Result result = null;

		if (context.getCredentials() != null) {
			result = inforws.addPartOp(addPart, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			result = inforws.addPartOp(addPart, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

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

			if (context.getCredentials() != null) {
				inforws.changePartNumberOp(changePartNumber, tools.getOrganizationCode(context),
						tools.createSecurityHeader(context), "TERMINATE",
						null, null, tools.getTenant(context));
			} else {
				inforws.changePartNumberOp(changePartNumber, tools.getOrganizationCode(context), null, null,
						new Holder<SessionType>(tools.createInforSession(context)), null,
						tools.getTenant(context));
			}

			return partParam.getNewCode();
		} else {

			net.datastream.schemas.mp_entities.part_001.Part partInfor = new net.datastream.schemas.mp_entities.part_001.Part();
			//
			//
			//
			MP0241_GetPart_001_Result getPartResult = null;

			MP0241_GetPart_001 getPart = new MP0241_GetPart_001();
			getPart.setPARTID(new PARTID_Type());
			getPart.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
			getPart.getPARTID().setPARTCODE(partParam.getCode());
			if (context.getCredentials() != null) {
				getPartResult = inforws.getPartOp(getPart, tools.getOrganizationCode(context),
						tools.createSecurityHeader(context), "TERMINATE",
						null, null, tools.getTenant(context));
			} else {
				getPartResult = inforws.getPartOp(getPart, tools.getOrganizationCode(context), null, null,
						new Holder<SessionType>(tools.createInforSession(context)), null,
						tools.getTenant(context));
			}

			partInfor = getPartResult.getResultData().getPart();

			if (partParam.getClassCode() != null && (partInfor.getCLASSID() == null
					|| !partParam.getClassCode().toUpperCase().equals(partInfor.getCLASSID().getCLASSCODE()))) {
				partInfor.setUSERDEFINEDAREA(
						tools.getCustomFieldsTools().getInforCustomFields(context, "PART", partParam.getClassCode().toUpperCase()));
			}

			//
			//
			//
			initializeInforPartObject(partInfor, partParam, context);
			//
			// UPDATE PART
			//
			MP0242_SyncPart_001 syncPart = new MP0242_SyncPart_001();

			syncPart.setPart(partInfor);

			MP0242_SyncPart_001_Result result = null;

			if (context.getCredentials() != null) {
				result = inforws.syncPartOp(syncPart, tools.getOrganizationCode(context),
						tools.createSecurityHeader(context), "TERMINATE",
						null, null, tools.getTenant(context));
			} else {
				result = inforws.syncPartOp(syncPart, tools.getOrganizationCode(context), null, null,
						new Holder<SessionType>(tools.createInforSession(context)), null,
						tools.getTenant(context));
			}

			return result.getResultData().getPart().getPARTID().getPARTCODE();
		}
	}

	private void initializeInforPartObject(net.datastream.schemas.mp_entities.part_001.Part partInfor, Part partParam, InforContext context)
			throws InforException {
		// == null means Part creation
		if (partInfor.getPARTID() == null) {
			partInfor.setPARTID(new PARTID_Type());
			partInfor.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
			partInfor.getPARTID().setPARTCODE(partParam.getCode().toUpperCase().trim());
		}

		// DESCRIPTION
		if (partParam.getDescription() != null) {
			partInfor.getPARTID().setDESCRIPTION(partParam.getDescription());
		}

		// LONG DESCRIPTION
		if (partParam.getLongDescription() != null) {
			partInfor.setLONGDESCRIPTION(partParam.getLongDescription());
		}

		// UOM
		if (partParam.getUOM() != null) {
			partInfor.setUOMID(new UOMID_Type());
			partInfor.getUOMID().setUOMCODE(partParam.getUOM().trim());
		}

		// CLASS
		if (partParam.getClassCode() != null) {
			if (partParam.getClassCode().trim().equals("")) {
				partInfor.setCLASSID(null);
			} else {
				partInfor.setCLASSID(new CLASSID_Type());
				partInfor.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
				partInfor.getCLASSID().setCLASSCODE(partParam.getClassCode().toUpperCase().trim());
			}
		}

		// CATEGORY
		if (partParam.getCategoryCode() != null) {
			if (partParam.getCategoryCode().trim().equals("")) {
				partInfor.setCATEGORYID(null);
			} else {
				partInfor.setCATEGORYID(new CATEGORYID());
				partInfor.getCATEGORYID().setCATEGORYCODE(partParam.getCategoryCode().toUpperCase().trim());
			}
		}

		// CUSTOM FIELDS
		tools.getCustomFieldsTools().updateInforCustomFields(partInfor.getUSERDEFINEDAREA(), partParam.getCustomFields());

		// USER DEFINED AREA
		tools.getUDFTools().updateInforUserDefinedFields(partInfor.getUserDefinedFields(), partParam.getUserDefinedFields());

		// TRACK BY ASSET
		if (partParam.getTrackByAsset() != null) {
			partInfor.setBYASSET(tools.getDataTypeTools().encodeBoolean(partParam.getTrackByAsset(), BooleanType.PLUS_MINUS));
		}

		// OUT OF SERVICE
		if (partParam.getOutOfService() != null) {
			partInfor.setOUTOFSERVICE(tools.getDataTypeTools().encodeBoolean(partParam.getOutOfService(), BooleanType.TRUE_FALSE));
		}

		// TRACK AS KIT
		if (partParam.getTrackAsKit() != null) {
			partInfor.setKIT(tools.getDataTypeTools().encodeBoolean(partParam.getTrackAsKit(), BooleanType.TRUE_FALSE));
		}

		// TRACK BY LOT
		if (partParam.getTrackByLot() != null) {
			partInfor.setBYLOT(tools.getDataTypeTools().encodeBoolean(partParam.getTrackByLot(), BooleanType.PLUS_MINUS));
		}

		// PREVENT REORDERS
		if (partParam.getPreventReorders() != null) {
			partInfor.setPREVENTREORDERS(tools.getDataTypeTools().encodeBoolean(partParam.getPreventReorders(), BooleanType.TRUE_FALSE));
		}

		// TRACK CORES
		if (partParam.getTrackCores() != null) {
			partInfor.setREPAIRABLE(tools.getDataTypeTools().encodeBoolean(partParam.getTrackCores(), BooleanType.TRUE_FALSE));
		}

		// COMMODITY
		if (partParam.getCommodityCode() != null) {
			partInfor.setPRIMARYCOMMODITY(new COMMODITY_Type());
			partInfor.getPRIMARYCOMMODITY().setCOMMODITYCODE(partParam.getCommodityCode());
			partInfor.getPRIMARYCOMMODITY().setORGANIZATIONID(tools.getOrganization(context));
		}

		// TRACKING METHOD
		if (partParam.getTrackingMethod() != null) {
			partInfor.setTRACKMETHOD(new TYPE_Type());
			partInfor.getTRACKMETHOD().setTYPECODE(partParam.getTrackingMethod());
		} else {
			partInfor.setTRACKMETHOD(new TYPE_Type());
			partInfor.getTRACKMETHOD().setTYPECODE("TRPQ");
		}

		// PRICE TYPE
		if (partParam.getPriceType() != null) {
			partInfor.setPRICETYPE(new TYPE_Type());
			partInfor.getPRICETYPE().setTYPECODE(partParam.getPriceType());
		}

		// AVERAGE PRICE
		if (partParam.getAveragePrice() != null) {
			partInfor.setAVERAGEPRICE(tools.getDataTypeTools().encodeAmount(partParam.getAveragePrice(), "Average Price"));
		}

		// STANDARD PRICE
		if (partParam.getStandardPrice() != null) {
			partInfor.setSTANDARDPRICE(tools.getDataTypeTools().encodeAmount(partParam.getStandardPrice(), "Standard Price"));
		}

		// LAST PRICE
		if (partParam.getLastPrice() != null) {
			partInfor.setLASTPRICE(tools.getDataTypeTools().encodeAmount(partParam.getLastPrice(), "Last Price"));
		}

		// BUYER
		if (partParam.getBuyerCode() != null) {
			partInfor.setBUYER(new USERID_Type());
			partInfor.getBUYER().setUSERCODE(partParam.getBuyerCode());
		}

		// PREFERRED SUPPLIER
		if (partParam.getPreferredSupplier() != null) {
			partInfor.setPREFERREDSUPPLIER(new SUPPLIERID_Type());
			partInfor.getPREFERREDSUPPLIER().setORGANIZATIONID(tools.getOrganization(context));
			partInfor.getPREFERREDSUPPLIER().setSUPPLIERCODE(partParam.getPreferredSupplier());
		}
	}

	public String deletePart(InforContext context, String partCode) throws InforException {
		MP0243_DeletePart_001 deletePart = new MP0243_DeletePart_001();
		deletePart.setPARTID(new PARTID_Type());
		deletePart.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
		deletePart.getPARTID().setPARTCODE(partCode);

		if (context.getCredentials() != null) {
			inforws.deletePartOp(deletePart, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.deletePartOp(deletePart, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		return partCode;
	}

}
