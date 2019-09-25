package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.EquipmentWarrantyCoverageService;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentWarranty;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.BooleanType;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.warrantycoverage_001.CoverageByDate;
import net.datastream.schemas.mp_fields.EQUIPMENTID_Type;
import net.datastream.schemas.mp_fields.WARRANTYID_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0344_001.MP0344_AddWarrantyCoverage_001;
import net.datastream.schemas.mp_functions.mp0345_001.MP0345_SyncWarrantyCoverage_001;
import net.datastream.schemas.mp_functions.mp3238_001.MP3238_GetWarrantyCoverage_001;
import net.datastream.schemas.mp_results.mp0344_001.MP0344_AddWarrantyCoverage_001_Result;
import net.datastream.schemas.mp_results.mp0345_001.MP0345_SyncWarrantyCoverage_001_Result;
import net.datastream.schemas.mp_results.mp3238_001.MP3238_GetWarrantyCoverage_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.xml.ws.Holder;

public class EquipmentWarrantyCoverageServiceImpl implements EquipmentWarrantyCoverageService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public EquipmentWarrantyCoverageServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}


	public String createEquipmentWarrantyCoverage(InforContext context, EquipmentWarranty equipmentWarrantyParam) throws InforException {
		//
		//
		//
		net.datastream.schemas.mp_entities.warrantycoverage_001.EquipmentWarranty equipmentWarranty = new net.datastream.schemas.mp_entities.warrantycoverage_001.EquipmentWarranty();

		// EQUIPMENT ID
		if (equipmentWarrantyParam.getEquipmentCode() != null && !equipmentWarrantyParam.getEquipmentCode().trim().equals("")) {
			equipmentWarranty.setASSETID(new EQUIPMENTID_Type());
			equipmentWarranty.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
			equipmentWarranty.getASSETID().setEQUIPMENTCODE(equipmentWarrantyParam.getEquipmentCode());
		}

		// WARRANTY CODE
		if (equipmentWarrantyParam.getWarrantyCode() != null && !equipmentWarrantyParam.getWarrantyCode().trim().equals("")) {
			equipmentWarranty.setWARRANTYID(new WARRANTYID_Type());
			equipmentWarranty.getWARRANTYID().setORGANIZATIONID(tools.getOrganization(context));
			equipmentWarranty.getWARRANTYID().setWARRANTYCODE(equipmentWarrantyParam.getWarrantyCode());

		}

		if (equipmentWarrantyParam.getCoverageType() != null && (equipmentWarrantyParam.getCoverageType().toUpperCase().equals("CALENDAR") || equipmentWarrantyParam.getCoverageType().toUpperCase().equals("D"))) {
			equipmentWarranty.setCoverageByDate(new CoverageByDate());
			equipmentWarranty.getCoverageByDate().setSTARTDATE(tools.getDataTypeTools().formatDate(equipmentWarrantyParam.getStartDate(), "Start Date"));
			equipmentWarranty.getCoverageByDate().setEXPIRATIONDATE(tools.getDataTypeTools().formatDate(equipmentWarrantyParam.getExpirationDate(), "Expiration Date"));
			equipmentWarranty.getCoverageByDate().setWARRANTYDURATIONDAYS(tools.getDataTypeTools().encodeDouble(equipmentWarrantyParam.getDuration(), "Duration"));
			equipmentWarranty.getCoverageByDate().setTHRESHHOLDDAYS(tools.getDataTypeTools().encodeQuantity(equipmentWarrantyParam.getThreshold(), "Threshold"));
		} else {
			throw tools.generateFault("Coverage type other than 'Calendar' is not supported. Please contact CMMS Support.");
		}

		if (equipmentWarrantyParam.getActive() != null) {
			equipmentWarranty.setISWARRANTYACTIVE(tools.getDataTypeTools().encodeBoolean(equipmentWarrantyParam.getActive(), BooleanType.TRUE_FALSE));
		} else {
			equipmentWarranty.setISWARRANTYACTIVE("false");
		}

		MP0344_AddWarrantyCoverage_001 addwarrantycoverage = new MP0344_AddWarrantyCoverage_001();
		addwarrantycoverage.setEquipmentWarranty(equipmentWarranty);

		MP0344_AddWarrantyCoverage_001_Result result = null;

		if (context.getCredentials() != null) {
			result = inforws.addWarrantyCoverageOp(addwarrantycoverage, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.addWarrantyCoverageOp(addwarrantycoverage, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		return result.getResultData().getWARRANTYCOVERAGESEQNUM() + "";
	}

	public String updateEquipmentWarrantyCoverage(InforContext context, EquipmentWarranty equipmentWarrantyParam) throws InforException {
		//
		// Get it first
		//
		MP3238_GetWarrantyCoverage_001 getwarrantycoverege = new MP3238_GetWarrantyCoverage_001();
		MP3238_GetWarrantyCoverage_001_Result getwarrantycoveregeResult = new MP3238_GetWarrantyCoverage_001_Result();

		if (equipmentWarrantyParam.getSequenceNumber() == null) {
			tools.demandDatabaseConnection();
			EntityManager em = tools.getEntityManager();
			try {
				TypedQuery<EquipmentWarranty> eqwarr = em.createNamedQuery(EquipmentWarranty.GETEQPWARRANTY, EquipmentWarranty.class);
				eqwarr.setParameter("equipmentCode", equipmentWarrantyParam.getEquipmentCode().trim().toUpperCase());
				eqwarr.setParameter("warrantyCode", equipmentWarrantyParam.getWarrantyCode());
				equipmentWarrantyParam.setSequenceNumber(eqwarr.getSingleResult().getSequenceNumber());

			} catch (Exception e) {
				throw tools.generateFault("Couldn't fetch warranty record for this equipment (" + e.getMessage() + ")");
			} finally {
				em.close();
			}
		}

		getwarrantycoverege.setWARRANTYCOVERAGESEQNUM(Long.parseLong(equipmentWarrantyParam.getSequenceNumber()));

		if (context.getCredentials() != null) {
			getwarrantycoveregeResult = inforws.getWarrantyCoverageOp(getwarrantycoverege,
					tools.getOrganizationCode(context),
					tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(),
					tools.getTenant(context));
		} else {
			getwarrantycoveregeResult = inforws.getWarrantyCoverageOp(getwarrantycoverege,
                    tools.getOrganizationCode(context), null, null,
                    new Holder<SessionType>(tools.createInforSession(context)), null,
                    tools.getTenant(context));
		}
		//
		//
		//
		net.datastream.schemas.mp_entities.warrantycoverage_001.WarrantyCoverage warrantyCoverege = getwarrantycoveregeResult.getResultData().getWarrantyCoverage();

		// CALENDER COVERAGE TYPE
		if (warrantyCoverege.getEquipmentWarranty().getCoverageByDate() != null) {
			if (equipmentWarrantyParam.getStartDate() != null) {
				warrantyCoverege.getEquipmentWarranty().getCoverageByDate().setSTARTDATE(tools.getDataTypeTools().formatDate(equipmentWarrantyParam.getStartDate(), "Start Date"));
			}

			if (equipmentWarrantyParam.getExpirationDate() != null) {
				warrantyCoverege.getEquipmentWarranty().getCoverageByDate().setEXPIRATIONDATE(tools.getDataTypeTools().formatDate(equipmentWarrantyParam.getExpirationDate(), "Expiration Date"));
			}

			if (equipmentWarrantyParam.getDuration() != null) {
				warrantyCoverege.getEquipmentWarranty().getCoverageByDate().setWARRANTYDURATIONDAYS(tools.getDataTypeTools().encodeDouble(equipmentWarrantyParam.getDuration(), "Duration"));
			}

			if (equipmentWarrantyParam.getThreshold() != null) {
				warrantyCoverege.getEquipmentWarranty().getCoverageByDate().setTHRESHHOLDDAYS(tools.getDataTypeTools().encodeQuantity(equipmentWarrantyParam.getThreshold(), "Threshold"));
			}
		}

		// USAGE COVERAGE TYPE
		if (warrantyCoverege.getEquipmentWarranty().getCoverageByUsage() != null) {
			// TO BE IMPLEMENTED
		}

		if (equipmentWarrantyParam.getActive() != null) {
			warrantyCoverege.getEquipmentWarranty().setISWARRANTYACTIVE(tools.getDataTypeTools().encodeBoolean(equipmentWarrantyParam.getActive(), BooleanType.TRUE_FALSE));
		}

		MP0345_SyncWarrantyCoverage_001 syncwarrantycoverege = new MP0345_SyncWarrantyCoverage_001();
		syncwarrantycoverege.setWarrantyCoverage(warrantyCoverege);

		MP0345_SyncWarrantyCoverage_001_Result result = null;

		if (context.getCredentials() != null) {
			result = inforws.syncWarrantyCoverageOp(syncwarrantycoverege, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.syncWarrantyCoverageOp(syncwarrantycoverege, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		return result.getResultData().getWARRANTYCOVERAGESEQNUM() + "";
	}


}
