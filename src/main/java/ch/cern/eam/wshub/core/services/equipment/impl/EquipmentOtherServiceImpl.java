package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.EquipmentOtherService;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentCampaign;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentDepreciation;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.campaignequipment_001.CampaignEquipment;
import net.datastream.schemas.mp_entities.depreciation_001.Depreciation;
import net.datastream.schemas.mp_entities.depreciation_001.RemainingUsefulLife;
import net.datastream.schemas.mp_entities.depreciationdefault_001.DepreciationDefault;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp3015_001.MP3015_GetDepreciationDefault_001;
import net.datastream.schemas.mp_functions.mp3016_001.MP3016_GetDepreciation_001;
import net.datastream.schemas.mp_functions.mp3017_001.MP3017_AddDepreciation_001;
import net.datastream.schemas.mp_functions.mp3018_001.MP3018_SyncDepreciation_001;
import net.datastream.schemas.mp_functions.mp3291_001.ChangeEquipmentNumber;
import net.datastream.schemas.mp_functions.mp3291_001.MP3291_ChangeEquipmentNumber_001;
import net.datastream.schemas.mp_functions.mp5039_001.MP5039_AddCampaignEquipment_001;
import net.datastream.schemas.mp_results.mp3016_001.MP3016_GetDepreciation_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.persistence.EntityManager;
import javax.xml.ws.Holder;

public class EquipmentOtherServiceImpl implements EquipmentOtherService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public EquipmentOtherServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public String createEquipmentDepreciation(InforContext context, EquipmentDepreciation equipmentDepreciation) throws InforException {
		//
		// Fetch default values
		//
		MP3015_GetDepreciationDefault_001 getdepdef = new MP3015_GetDepreciationDefault_001();

		getdepdef.setEQUIPMENTID(new EQUIPMENTID_Type());
		getdepdef.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
		getdepdef.getEQUIPMENTID().setEQUIPMENTCODE(equipmentDepreciation.getEquipmentCode());

		getdepdef.setDEPRECIATIONCATEGORYID(new DEPRECIATIONCATEGORYID_Type());
		if (equipmentDepreciation.getDepreciationCategory() != null) {
			getdepdef.getDEPRECIATIONCATEGORYID()
					.setDEPRECIATIONCATEGORYCODE(equipmentDepreciation.getDepreciationCategory().trim());
		} else {
			getdepdef.getDEPRECIATIONCATEGORYID().setDEPRECIATIONCATEGORYCODE("C");
		}

		if (equipmentDepreciation.getDepreciationType() != null) {
			getdepdef.setEQUIPMENTDEPTYPE(equipmentDepreciation.getDepreciationType().toUpperCase().trim());
		} else {
			getdepdef.setEQUIPMENTDEPTYPE("*");
		}

		getdepdef.setFROMDATE(tools.getDataTypeTools().formatDate(equipmentDepreciation.getFromDate(), "From Date"));

		DepreciationDefault depreciationDefault;
		if (context.getCredentials() != null) {
			depreciationDefault = inforws
					.getDepreciationDefaultOp(getdepdef, tools.getOrganizationCode(context),
							tools.createSecurityHeader(context),
							"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context))
					.getResultData().getDepreciationDefault();
		} else {
			depreciationDefault = inforws.getDepreciationDefaultOp(getdepdef, tools.getOrganizationCode(context), null,
					null, new Holder<SessionType>(tools.createInforSession(context)), null,
					tools.getTenant(context)).getResultData().getDepreciationDefault();
		}

		Depreciation depreciation = new Depreciation();

		// DEPRECIATION PK
		depreciation.setDEPRECIATIONPK(tools.getDataTypeTools().encodeQuantity("0", "Depreciation PK"));

		// ORIGINAL VALUE
		if (equipmentDepreciation.getOriginalValue() != null) {
			depreciation.setORIGINALVALUE(
					tools.getDataTypeTools().encodeAmount(equipmentDepreciation.getOriginalValue().trim(), "Original Value"));
		} else {
			depreciation.setORIGINALVALUE(depreciationDefault.getORIGINALVALUE());
		}

		// RESIDUAL VALUE
		if (equipmentDepreciation.getResidualValue() != null) {
			depreciation.setRESIDUALVALUE(
					tools.getDataTypeTools().encodeAmount(equipmentDepreciation.getResidualValue().trim(), "Residual Value"));
		} else {
			depreciation.setRESIDUALVALUE(depreciationDefault.getRESIDUALVALUE());
		}

		// ESTIMATED USEFUL LIFE
		if (equipmentDepreciation.getEstimatedUsefulLife() != null) {
			depreciation.setRemainingUsefulLife(new RemainingUsefulLife());
			if (equipmentDepreciation.getEstimatedUsefulLifeUOM() != null
					&& !equipmentDepreciation.getEstimatedUsefulLifeUOM().trim().equals("")) {
				depreciation.getRemainingUsefulLife().setUOMID(new UOMID_Type());
				depreciation.getRemainingUsefulLife().getUOMID()
						.setUOMCODE(equipmentDepreciation.getEstimatedUsefulLifeUOM().trim());
			} else {
				depreciation.getRemainingUsefulLife().setUOMID(new UOMID_Type());
				depreciation.getRemainingUsefulLife().getUOMID().setUOMCODE("Y");
			}
			depreciation.getRemainingUsefulLife().setESTIMATEDLIFE(
					tools.getDataTypeTools().encodeAmount(equipmentDepreciation.getEstimatedUsefulLife().trim(), "Estimated Useful Life"));
		} else {
			depreciation.setRemainingUsefulLife(new RemainingUsefulLife());
			depreciation.getRemainingUsefulLife()
					.setESTIMATEDLIFE(depreciationDefault.getRemainingUsefulLife().getESTIMATEDLIFE());
			depreciation.getRemainingUsefulLife().setUOMID(depreciationDefault.getRemainingUsefulLife().getUOMID());
		}

		// DEPRECIATION METHOD
		if (equipmentDepreciation.getDepreciationMethod() != null) {
			depreciation.setDEPRECIATIONMETHOD(equipmentDepreciation.getDepreciationMethod().trim());
		}

		// DEPRECIATION ID
		if (equipmentDepreciation.getEquipmentCode() != null) {
			// depreciation.seteq
			depreciation.setEQUIPMENTID(new EQUIPMENTID_Type());
			depreciation.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
			depreciation.getEQUIPMENTID().setEQUIPMENTCODE(equipmentDepreciation.getEquipmentCode().trim());
		}

		// DEPRECIATION TYPE
		// Possible types: select * from r5descriptions where des_entity =
		// 'UCOD' AND DES_TYPE = 'DETP';
		if (equipmentDepreciation.getDepreciationType() != null) {
			depreciation.setEQUIPMENTDEPTYPE(equipmentDepreciation.getDepreciationType().toUpperCase().trim());
		} else {
			depreciation.setEQUIPMENTDEPTYPE("*");
		}

		// DEPRECIATION CATEGORY
		if (equipmentDepreciation.getDepreciationCategory() != null) {
			depreciation.setDEPRECIATIONCATEGORYID(new DEPRECIATIONCATEGORYID_Type());
			depreciation.getDEPRECIATIONCATEGORYID()
					.setDEPRECIATIONCATEGORYCODE(equipmentDepreciation.getDepreciationCategory().trim());
		} else {
			depreciation.setDEPRECIATIONCATEGORYID(new DEPRECIATIONCATEGORYID_Type());
			depreciation.getDEPRECIATIONCATEGORYID().setDEPRECIATIONCATEGORYCODE("C");
		}

		// FROM DATE
		if (equipmentDepreciation.getFromDate() != null) {
			depreciation.setFROMDATE(tools.getDataTypeTools().formatDate(equipmentDepreciation.getFromDate().trim(), "From Date"));
		} else {
			depreciation.setFROMDATE(depreciationDefault.getFROMDATE());
		}

		// CHANGE VALUE
		if (equipmentDepreciation.getChangeValue() != null) {
			depreciation.setCHANGEVALUE(tools.getDataTypeTools().encodeAmount(equipmentDepreciation.getChangeValue(), "Change Value"));
		}

		// CHANGE LIFE
		if (equipmentDepreciation.getChangeLife() != null) {
			depreciation.setCHANGELIFE(tools.getDataTypeTools().encodeAmount(equipmentDepreciation.getChangeLife(), "Change Life"));
		}

		// CHANGE ESTIMATED LIFETIME OUTPUT
		if (equipmentDepreciation.getChangeEstimatedLifetimeOutput() != null) {
			depreciation.setCHANGEESTLIFETIMEOUTPUT(tools.getDataTypeTools().encodeAmount(
					equipmentDepreciation.getChangeEstimatedLifetimeOutput(), "Change Estimated Lifetime Output"));
		}

		// USER DEFINED FIELDS
		depreciation.setStandardUserDefinedFields(new StandardUserDefinedFields());
		tools.getUDFTools().updateInforUserDefinedFields(depreciation.getStandardUserDefinedFields(),
				equipmentDepreciation.getUserDefinedFields());

		// ADD DEPRECIATION
		MP3017_AddDepreciation_001 adddep = new MP3017_AddDepreciation_001();
		adddep.setDepreciation(depreciation);
		if (context.getCredentials() != null) {
			inforws.addDepreciationOp(adddep, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.addDepreciationOp(adddep, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return "OK";
	}

	public String updateEquipmentDepreciation(InforContext context, EquipmentDepreciation equipmentDepreciation) throws InforException {
		//
		// GET THE DEPRECIATION VALUE FIRST
		//
		if (equipmentDepreciation.getDepreciationPK() == null
				|| equipmentDepreciation.getDepreciationPK().trim().equals("")) {

			if (equipmentDepreciation.getEquipmentCode() == null) {
				throw tools.generateFault("Equipment Code is mandatory field");
			}

			EntityManager em = tools.getEntityManager();
			try {
				equipmentDepreciation.setDepreciationPK(em
						.createNamedQuery(EquipmentDepreciation.GETDEPRECIATION, EquipmentDepreciation.class)
						.setParameter("equipmentCode", equipmentDepreciation.getEquipmentCode().trim().toUpperCase())
						.getSingleResult().getDepreciationPK());
			} catch (Exception e) {
				throw tools.generateFault("Couldn't fetch depreciation record for this equipment.");
			} finally {
				em.close();
			}
		}

		MP3016_GetDepreciation_001 getdep = new MP3016_GetDepreciation_001();
		getdep.setDEPRECIATIONPK(tools.getDataTypeTools().encodeQuantity(equipmentDepreciation.getDepreciationPK(), "Depreciation PK"));

		MP3016_GetDepreciation_001_Result result;
		if (context.getCredentials() != null) {
			result = inforws.getDepreciationOp(getdep, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			result = inforws.getDepreciationOp(getdep, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		//
		// UPDATE DEPRECIATION
		//
		Depreciation depreciation = result.getResultData().getDepreciation();

		// ORIGINAL VALUE
		if (equipmentDepreciation.getOriginalValue() != null) {
			depreciation
					.setORIGINALVALUE(tools.getDataTypeTools().encodeAmount(equipmentDepreciation.getOriginalValue(), "Original Value"));
		}

		// RESIDUAL VALUE
		if (equipmentDepreciation.getResidualValue() != null) {
			depreciation
					.setRESIDUALVALUE(tools.getDataTypeTools().encodeAmount(equipmentDepreciation.getResidualValue(), "Residual Value"));
		}

		// ESTIMATED USEFUL LIFE
		if (equipmentDepreciation.getEstimatedUsefulLifeUOM() != null) {
			depreciation.getRemainingUsefulLife().setUOMID(new UOMID_Type());
			depreciation.getRemainingUsefulLife().getUOMID()
					.setUOMCODE(equipmentDepreciation.getEstimatedUsefulLifeUOM().toUpperCase());
			String amount = tools.getDataTypeTools().decodeAmount(depreciation.getRemainingUsefulLife().getESTIMATEDLIFE());
			depreciation.getRemainingUsefulLife().setESTIMATEDLIFE(tools.getDataTypeTools().encodeAmount(amount, "Estiamted Life Time"));
		}

		if (equipmentDepreciation.getEstimatedUsefulLife() != null) {
			depreciation.getRemainingUsefulLife().setESTIMATEDLIFE(
					tools.getDataTypeTools().encodeAmount(equipmentDepreciation.getEstimatedUsefulLife(), "Estiamted Life Time"));
		}

		// DEPRECIATION METHOD
		if (equipmentDepreciation.getDepreciationMethod() != null) {
			depreciation.setDEPRECIATIONMETHOD(equipmentDepreciation.getDepreciationMethod());
		}

		// DEPRECIATION TYPE
		if (equipmentDepreciation.getDepreciationType() != null) {
			depreciation.setEQUIPMENTDEPTYPE(equipmentDepreciation.getDepreciationType().toUpperCase());
		}

		// DEPRECIATION CATEGORY
		if (equipmentDepreciation.getDepreciationCategory() != null) {
			depreciation.setDEPRECIATIONCATEGORYID(new DEPRECIATIONCATEGORYID_Type());
			depreciation.getDEPRECIATIONCATEGORYID()
					.setDEPRECIATIONCATEGORYCODE(equipmentDepreciation.getDepreciationCategory());
		}

		// FROM DATE
		if (equipmentDepreciation.getFromDate() != null) {
			depreciation.setFROMDATE(tools.getDataTypeTools().formatDate(equipmentDepreciation.getFromDate(), "From Date"));
		}

		// USER DEFINED FIELDS
		if (depreciation.getStandardUserDefinedFields() == null) {
			depreciation.setStandardUserDefinedFields(new StandardUserDefinedFields());
		}
		tools.getUDFTools().updateInforUserDefinedFields(depreciation.getStandardUserDefinedFields(),
				equipmentDepreciation.getUserDefinedFields());

		// CHANGE VALUE
		if (equipmentDepreciation.getChangeValue() != null) {
			depreciation.setCHANGEVALUE(tools.getDataTypeTools().encodeAmount(equipmentDepreciation.getChangeValue(), "Change Value"));
		}

		// CHANGE LIFE
		if (equipmentDepreciation.getChangeLife() != null) {
			depreciation.setCHANGELIFE(tools.getDataTypeTools().encodeAmount(equipmentDepreciation.getChangeLife(), "Change Life"));
		}

		// CHANGE ESTIMATED LIFETIME OUTPUT
		if (equipmentDepreciation.getChangeEstimatedLifetimeOutput() != null) {
			depreciation.setCHANGEESTLIFETIMEOUTPUT(tools.getDataTypeTools().encodeAmount(
					equipmentDepreciation.getChangeEstimatedLifetimeOutput(), "Change Estimated Lifetime Output"));
		}

		MP3018_SyncDepreciation_001 syncdep = new MP3018_SyncDepreciation_001();
		syncdep.setDepreciation(depreciation);
		if (context.getCredentials() != null) {
			inforws.syncDepreciationOp(syncdep, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.syncDepreciationOp(syncdep, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return "OK";
	}

	public String updateEquipmentCode(InforContext context, String equipmentCode, String equipmentNewCode, String equipmentType) throws InforException {

		MP3291_ChangeEquipmentNumber_001 changeeqpnum = new MP3291_ChangeEquipmentNumber_001();
		changeeqpnum.setChangeEquipmentNumber(new ChangeEquipmentNumber());
		//
		changeeqpnum.getChangeEquipmentNumber().setCURRENTEQUIPMENTID(new EQUIPMENTID_Type());
		changeeqpnum.getChangeEquipmentNumber().getCURRENTEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
		changeeqpnum.getChangeEquipmentNumber().getCURRENTEQUIPMENTID().setEQUIPMENTCODE(equipmentCode);

		//
		changeeqpnum.getChangeEquipmentNumber().setNEWEQUIPMENTID(new EQUIPMENTID_Type());
		changeeqpnum.getChangeEquipmentNumber().getNEWEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
		changeeqpnum.getChangeEquipmentNumber().getNEWEQUIPMENTID().setEQUIPMENTCODE(equipmentNewCode);

		if (context.getCredentials() != null) {
			inforws.changeEquipmentNumberOp(changeeqpnum, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.changeEquipmentNumberOp(changeeqpnum, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		return "OK";
	}

	public String createEquipmentCampaign(InforContext context, EquipmentCampaign equipmentCampaign) throws InforException {
		CampaignEquipment campaignEquipment = new CampaignEquipment();
		campaignEquipment.setCAMPAIGNEQUIPMENTID(new CAMPAIGNEQUIPMENTID_Type());
		//
		// CAMPAIGN ID
		//
		campaignEquipment.getCAMPAIGNEQUIPMENTID().setCAMPAIGNID(new CAMPAIGNID_Type());
		campaignEquipment.getCAMPAIGNEQUIPMENTID().getCAMPAIGNID().setCAMPAIGNCODE(equipmentCampaign.getCampaign());
		campaignEquipment.getCAMPAIGNEQUIPMENTID().getCAMPAIGNID().setORGANIZATIONID(tools.getOrganization(context));
		//
		//
		//
		campaignEquipment.getCAMPAIGNEQUIPMENTID().setEQUIPMENTID(new EQUIPMENTID_Type());
		campaignEquipment.getCAMPAIGNEQUIPMENTID().getEQUIPMENTID().setEQUIPMENTCODE(equipmentCampaign.getEquipment());
		campaignEquipment.getCAMPAIGNEQUIPMENTID().getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));

		MP5039_AddCampaignEquipment_001 addCampaignEquipment = new MP5039_AddCampaignEquipment_001();

		addCampaignEquipment.setCampaignEquipment(campaignEquipment);

		if (context.getCredentials() != null) {
			inforws.addCampaignEquipmentOp(addCampaignEquipment, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.addCampaignEquipmentOp(addCampaignEquipment, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return null;
	}



}
