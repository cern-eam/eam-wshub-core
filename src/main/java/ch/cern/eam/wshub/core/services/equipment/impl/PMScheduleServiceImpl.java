package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.PMScheduleService;
import ch.cern.eam.wshub.core.services.equipment.entities.EquipmentPMSchedule;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.BooleanType;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.pmschedule_001.PMScheduleData;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0364_001.MP0364_AddEquipmentPMSchedule_001;
import net.datastream.schemas.mp_functions.mp0365_001.MP0365_SyncEquipmentPMSchedule_001;
import net.datastream.schemas.mp_functions.mp3014_001.MP3014_GetEquipmentPMSchedule_001;
import net.datastream.schemas.mp_functions.mp7006_001.MP7006_DeletePMScheduleEquipment_001;
import net.datastream.schemas.mp_results.mp0364_001.MP0364_AddEquipmentPMSchedule_001_Result;
import net.datastream.schemas.mp_results.mp0365_001.MP0365_SyncEquipmentPMSchedule_001_Result;
import net.datastream.schemas.mp_results.mp3014_001.MP3014_GetEquipmentPMSchedule_001_Result;
import net.datastream.schemas.mp_results.mp7006_001.MP7006_DeletePMScheduleEquipment_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.persistence.EntityManager;
import javax.xml.ws.Holder;

public class PMScheduleServiceImpl implements PMScheduleService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public PMScheduleServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}


	public String createEquipmentPMSchedule(InforContext context, EquipmentPMSchedule pmSchedule) throws InforException {
		//
		//
		//
		MP0364_AddEquipmentPMSchedule_001 pmschedule = new MP0364_AddEquipmentPMSchedule_001();
		pmschedule.setPMScheduleData(new PMScheduleData());

		// EQUIPMENT ID
		pmschedule.getPMScheduleData().setEQUIPMENTID(new EQUIPMENTID_Type());
		pmschedule.getPMScheduleData().getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
		pmschedule.getPMScheduleData().getEQUIPMENTID().setEQUIPMENTCODE(pmSchedule.getEquipmentCode());

		// INTERVAL
		pmschedule.getPMScheduleData().setPERIODINTERVAL(new PERIODINTERVAL());

		if (pmSchedule.getPeriodLength() != null && pmSchedule.getPeriodUOM() != null) {
			pmschedule.getPMScheduleData().getPERIODINTERVAL().setINTERVAL(tools.getDataTypeTools().encodeLong(pmSchedule.getPeriodLength(), "Period Length"));
			pmschedule.getPMScheduleData().getPERIODINTERVAL().setUOM(pmSchedule.getPeriodUOM());
		}

		if (pmSchedule.getDueDate() != null) {
			pmschedule.getPMScheduleData().getPERIODINTERVAL().setDUEDATE(tools.getDataTypeTools().formatDate(pmSchedule.getDueDate(), "PM Schedule Due Date"));
		}

		// PM SCHEDULE
		pmschedule.getPMScheduleData().setPMSCHEDULEEQUIPMENTID(new PMSCHEDULEEQUIPMENTID());
		pmschedule.getPMScheduleData().getPMSCHEDULEEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
		pmschedule.getPMScheduleData().getPMSCHEDULEEQUIPMENTID().setPPMID(new PPM_Type());
		pmschedule.getPMScheduleData().getPMSCHEDULEEQUIPMENTID().getPPMID().setORGANIZATIONID(tools.getOrganization(context));
		pmschedule.getPMScheduleData().getPMSCHEDULEEQUIPMENTID().getPPMID().setPPMCODE(pmSchedule.getPmCode());

		// DEPARTMENT
		pmschedule.getPMScheduleData().setDEPARTMENTID(new DEPARTMENTID_Type());
		pmschedule.getPMScheduleData().getDEPARTMENTID().setORGANIZATIONID(tools.getOrganization(context));
		pmschedule.getPMScheduleData().getDEPARTMENTID().setDEPARTMENTCODE(pmSchedule.getDepartmentCode());

		// ROUTE
		if (pmSchedule.getRoute() != null && !pmSchedule.getRoute().trim().equals("")) {
			pmschedule.getPMScheduleData().setROUTE(new ROUTE_Type());
			pmschedule.getPMScheduleData().getROUTE().setORGANIZATIONID(tools.getOrganization(context));
			pmschedule.getPMScheduleData().getROUTE().setROUTECODE(pmSchedule.getRoute());
		}

		// PM SCHEDULE TYPE
		if (pmSchedule.getScheduleType() == null || pmSchedule.getScheduleType().trim().equals("")) {
			pmschedule.getPMScheduleData().setPMSCHEDULETYPE("F");
		} else {
			pmschedule.getPMScheduleData().setPMSCHEDULETYPE(pmSchedule.getScheduleType());
		}

		// CHANGED
		if (pmSchedule.getChanged() != null) {
			pmschedule.getPMScheduleData().setCHANGED(tools.getDataTypeTools().encodeBoolean(pmSchedule.getChanged(), BooleanType.TRUE_FALSE));
		} else {
			pmschedule.getPMScheduleData().setCHANGED("false");
		}

		// PPO_SCHEDGRP - SUPERVISOR

		if (pmSchedule.getSupervisor() != null) {
			pmschedule.getPMScheduleData().setSUPERVISORID(new SUPERVISORID());
			pmschedule.getPMScheduleData().getSUPERVISORID().setORGANIZATIONID(tools.getOrganization(context));
			pmschedule.getPMScheduleData().getSUPERVISORID().setSUPERVISORCODE(pmSchedule.getSupervisor().toUpperCase());
		}

		// PPO_COSTCODE
		if (pmSchedule.getCostCode() != null) {
			pmschedule.getPMScheduleData().setCOSTCODEID(new COSTCODEID_Type());
			pmschedule.getPMScheduleData().getCOSTCODEID().setORGANIZATIONID(tools.getOrganization(context));
			pmschedule.getPMScheduleData().getCOSTCODEID().setCOSTCODE(pmSchedule.getCostCode().toUpperCase());
		}

		// PPO_METER, PPO_METUOM, PPO_METERDUE (1)
		if (pmSchedule.getMeter1Due() != null ||
				pmSchedule.getMeter1Interval() != null ||
				pmSchedule.getMeter1UOM() != null) {
			pmschedule.getPMScheduleData().setMETER1INTERVAL(new METERINTERVAL_Type());
			pmschedule.getPMScheduleData().getMETER1INTERVAL().setINTERVAL(tools.getDataTypeTools().encodeQuantity(pmSchedule.getMeter1Interval(), "Meter 1 Interval"));
			pmschedule.getPMScheduleData().getMETER1INTERVAL().setREADINGDUE(tools.getDataTypeTools().encodeQuantity(pmSchedule.getMeter1Due(), "Meter 1 Due"));
			pmschedule.getPMScheduleData().getMETER1INTERVAL().setUOMID(new UOMID_Type());
			pmschedule.getPMScheduleData().getMETER1INTERVAL().getUOMID().setUOMCODE(pmSchedule.getMeter1UOM());
		}

		// PPO_METER, PPO_METUOM, PPO_METERDUE (2)
		if (pmSchedule.getMeter2Due() != null ||
				pmSchedule.getMeter2Interval() != null ||
				pmSchedule.getMeter2UOM() != null) {
			pmschedule.getPMScheduleData().setMETER2INTERVAL(new METERINTERVAL_Type());
			pmschedule.getPMScheduleData().getMETER2INTERVAL().setINTERVAL(tools.getDataTypeTools().encodeQuantity(pmSchedule.getMeter2Interval(), "Meter 2 Interval"));
			pmschedule.getPMScheduleData().getMETER2INTERVAL().setREADINGDUE(tools.getDataTypeTools().encodeQuantity(pmSchedule.getMeter2Due(), "Meter 2 Due"));
			pmschedule.getPMScheduleData().getMETER2INTERVAL().setUOMID(new UOMID_Type());
			pmschedule.getPMScheduleData().getMETER2INTERVAL().getUOMID().setUOMCODE(pmSchedule.getMeter2UOM());
		}

		// PPO_DEACTIVE
		if (pmSchedule.getDateDeactivated() != null) {
			pmschedule.getPMScheduleData().setDEACTIVATEDDATE(tools.getDataTypeTools().formatDate(pmSchedule.getDateDeactivated(), "Date Deactivated"));
		}

		// PPO_LOCATION
		if (pmSchedule.getLocation() != null) {
			pmschedule.getPMScheduleData().setLOCATIONID(new LOCATIONID_Type());
			pmschedule.getPMScheduleData().getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
			pmschedule.getPMScheduleData().getLOCATIONID().setLOCATIONCODE(pmSchedule.getLocation());
		}

		// ASSIGNED TO
		if (pmSchedule.getAssignedTo() != null) {
			pmschedule.getPMScheduleData().setASSIGNEDTO(new PERSONID_Type());
			pmschedule.getPMScheduleData().getASSIGNEDTO().setPERSONCODE(pmSchedule.getAssignedTo());
		}

		// WO CLASS
		if (pmSchedule.getWorkOrderClass() != null) {
			pmschedule.getPMScheduleData().setWORKORDERCLASSID(new CLASSID_Type());
			pmschedule.getPMScheduleData().getWORKORDERCLASSID().setORGANIZATIONID(tools.getOrganization(context));
			pmschedule.getPMScheduleData().getWORKORDERCLASSID().setCLASSCODE(pmSchedule.getWorkOrderClass());
		}

		MP0364_AddEquipmentPMSchedule_001_Result result = null;

		if (context.getCredentials() != null) {
			result = inforws.addEquipmentPMScheduleOp(pmschedule, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.addEquipmentPMScheduleOp(pmschedule, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		return result.getResultData().getPMSCHEDULEEQUIPMENTID().getSEQUENCENUMBER() + "";
	}

	public String deleteEquipmentPMSchedule(InforContext context, EquipmentPMSchedule pmSchedule) throws InforException {
		//
		// Fetch PM Schedule Sequence Number and Revision
		//
		if (pmSchedule.getSequenceNumber() == null || pmSchedule.getRevision() == null) {
			tools.demandDatabaseConnection();
			EntityManager em = tools.getEntityManager();
			try {
				EquipmentPMSchedule pmScheduleTemp = em.createNamedQuery(EquipmentPMSchedule.FIND_PM_SCHEDULE, EquipmentPMSchedule.class).setParameter("equipmentCode", pmSchedule.getEquipmentCode()).setParameter("pmCode", pmSchedule.getPmCode()).getSingleResult();
				pmSchedule.setSequenceNumber(pmScheduleTemp.getSequenceNumber());
				pmSchedule.setRevision(pmScheduleTemp.getRevision());
				// Just in case pmCode was not supplied
				pmSchedule.setPmCode(pmScheduleTemp.getPmCode());
			} catch (Exception e) {
				throw tools.generateFault("Couldn't fetch PM Schedule record for this equipment (" + e.getMessage() + ")");
			} finally {
				em.close();
			}
		}
		//
		// Delete PM Schedule
		//
		MP7006_DeletePMScheduleEquipment_001 pmschedule = new MP7006_DeletePMScheduleEquipment_001();
		pmschedule.setPMSCHEDULEEQUIPMENTID(new PMSCHEDULEEQUIPMENTID());
		pmschedule.getPMSCHEDULEEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));

		pmschedule.getPMSCHEDULEEQUIPMENTID().setPPMID(new PPM_Type());
		pmschedule.getPMSCHEDULEEQUIPMENTID().getPPMID().setORGANIZATIONID(tools.getOrganization(context));
		pmschedule.getPMSCHEDULEEQUIPMENTID().getPPMID().setPPMCODE(pmSchedule.getPmCode());
		pmschedule.getPMSCHEDULEEQUIPMENTID().getPPMID().setPPMREVISION(Integer.parseInt(pmSchedule.getRevision()));

		pmschedule.getPMSCHEDULEEQUIPMENTID().setSEQUENCENUMBER(tools.getDataTypeTools().encodeLong(pmSchedule.getSequenceNumber(), "Sequence Number"));

		MP7006_DeletePMScheduleEquipment_001_Result result = null;

		if (context.getCredentials() != null) {
			result = inforws.deletePMScheduleEquipmentOp(pmschedule, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.deletePMScheduleEquipmentOp(pmschedule, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		return result.getResultData().getPMSCHEDULEEQUIPMENTID().getSEQUENCENUMBER() + "";
	}

	public String updateEquipmentPMSchedule(InforContext context, EquipmentPMSchedule pmSchedule) throws InforException {
		//
		// Fetch PM Schedule Sequence Number and Revision
		//
		if (pmSchedule.getSequenceNumber() == null || pmSchedule.getRevision() == null) {
			tools.demandDatabaseConnection();
			EntityManager em = tools.getEntityManager();
			try {
				EquipmentPMSchedule pmScheduleTemp = em.createNamedQuery(EquipmentPMSchedule.FIND_PM_SCHEDULE, EquipmentPMSchedule.class).setParameter("equipmentCode", pmSchedule.getEquipmentCode()).setParameter("pmCode", pmSchedule.getPmCode()).getSingleResult();
				pmSchedule.setSequenceNumber(pmScheduleTemp.getSequenceNumber());
				pmSchedule.setRevision(pmScheduleTemp.getRevision());
				// Just in case pmCode was not supplied
				pmSchedule.setPmCode(pmScheduleTemp.getPmCode());
			} catch (Exception e) {
				throw tools.generateFault("Couldn't fetch PM Schedule record for this equipment (" + e.getMessage() + ")");
			} finally {
				em.close();
			}
		}
		//
		// Fetch Equipment PM Schedule first
		//
		MP3014_GetEquipmentPMSchedule_001 getpm = new MP3014_GetEquipmentPMSchedule_001();
		getpm.setPMSCHEDULEEQUIPMENTID(new PMSCHEDULEEQUIPMENTID());
		getpm.getPMSCHEDULEEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
		// PPM ID, do we need it?
		getpm.getPMSCHEDULEEQUIPMENTID().setPPMID(new PPM_Type());
		getpm.getPMSCHEDULEEQUIPMENTID().getPPMID().setORGANIZATIONID(tools.getOrganization(context));
		getpm.getPMSCHEDULEEQUIPMENTID().getPPMID().setPPMCODE(pmSchedule.getPmCode());
		getpm.getPMSCHEDULEEQUIPMENTID().getPPMID().setPPMREVISION((int)tools.getDataTypeTools().encodeLong(pmSchedule.getRevision(), "PM Schedule Revision"));
		getpm.getPMSCHEDULEEQUIPMENTID().setSEQUENCENUMBER(tools.getDataTypeTools().encodeLong(pmSchedule.getSequenceNumber(), "PM Schedule Sequence Number"));

		MP3014_GetEquipmentPMSchedule_001_Result getresult = null;
		if (context.getCredentials() != null) {
			getresult = inforws.getEquipmentPMScheduleOp(getpm, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			getresult = inforws.getEquipmentPMScheduleOp(getpm, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
		//
		// Update it
		//
		PMScheduleData pmScheduleData = getresult.getResultData().getPMSchedule().getPMScheduleData();

		// PERIOD INTERVAL
		if (pmSchedule.getPeriodLength() != null) {
			if (pmScheduleData.getPERIODINTERVAL() == null) {
				pmScheduleData.setPERIODINTERVAL(new PERIODINTERVAL());
			}
			pmScheduleData.getPERIODINTERVAL().setINTERVAL(tools.getDataTypeTools().encodeLong(pmSchedule.getPeriodLength(), "Period Length"));
		}

		// PERIOD UOM
		if (pmSchedule.getPeriodUOM() != null) {
			if (pmScheduleData.getPERIODINTERVAL() == null) {
				pmScheduleData.setPERIODINTERVAL(new PERIODINTERVAL());
			}
			pmScheduleData.getPERIODINTERVAL().setUOM(pmSchedule.getPeriodUOM());
		}

		// DUE DATE
		if (pmSchedule.getDueDate() != null) {
			if (pmScheduleData.getPERIODINTERVAL() == null) {
				pmScheduleData.setPERIODINTERVAL(new PERIODINTERVAL());
			}
			pmScheduleData.getPERIODINTERVAL().setDUEDATE(tools.getDataTypeTools().formatDate(pmSchedule.getDueDate(), "PM Schedule Due Date"));
		}

		// DEPARTMENT
		if (pmSchedule.getDepartmentCode() != null) {
			pmScheduleData.setDEPARTMENTID(new DEPARTMENTID_Type());
			pmScheduleData.getDEPARTMENTID().setORGANIZATIONID(tools.getOrganization(context));
			pmScheduleData.getDEPARTMENTID().setDEPARTMENTCODE(pmSchedule.getDepartmentCode());
		}

		// ROUTE
		if (pmSchedule.getRoute() != null && !pmSchedule.getRoute().trim().equals("")) {
			pmScheduleData.setROUTE(new ROUTE_Type());
			pmScheduleData.getROUTE().setORGANIZATIONID(tools.getOrganization(context));
			pmScheduleData.getROUTE().setROUTECODE(pmSchedule.getRoute());
		}

		// PM SCHEDULE TYPE
		if (pmSchedule.getScheduleType() != null ) {
			pmScheduleData.setPMSCHEDULETYPE(pmSchedule.getScheduleType());
		}

		// CHANGED
		if (pmSchedule.getChanged() != null ) {
			pmScheduleData.setCHANGED(tools.getDataTypeTools().encodeBoolean(pmSchedule.getChanged(), BooleanType.TRUE_FALSE));
		}

		// PPO_SCHEDGRP - SUPERVISOR
		if (pmSchedule.getSupervisor() != null) {
			pmScheduleData.setSUPERVISORID(new SUPERVISORID());
			pmScheduleData.getSUPERVISORID().setORGANIZATIONID(tools.getOrganization(context));
			pmScheduleData.getSUPERVISORID().setSUPERVISORCODE(pmSchedule.getSupervisor().toUpperCase());
		}

		// PPO_COSTCODE

		if (pmSchedule.getCostCode() != null) {
			pmScheduleData.setCOSTCODEID(new COSTCODEID_Type());
			pmScheduleData.getCOSTCODEID().setORGANIZATIONID(tools.getOrganization(context));
			pmScheduleData.getCOSTCODEID().setCOSTCODE(pmSchedule.getCostCode().toUpperCase());
		}

		// PPO_METER, PPO_METUOM, PPO_METERDUE (1)
		if (pmSchedule.getMeter1Due() != null ||
				pmSchedule.getMeter1Interval() != null ||
				pmSchedule.getMeter1UOM() != null) {
			pmScheduleData.setMETER1INTERVAL(new METERINTERVAL_Type());
			pmScheduleData.getMETER1INTERVAL().setINTERVAL(tools.getDataTypeTools().encodeQuantity(pmSchedule.getMeter1Interval(), "Meter 1 Interval"));
			pmScheduleData.getMETER1INTERVAL().setREADINGDUE(tools.getDataTypeTools().encodeQuantity(pmSchedule.getMeter1Due(), "Meter 1 Due"));
			pmScheduleData.getMETER1INTERVAL().setUOMID(new UOMID_Type());
			pmScheduleData.getMETER1INTERVAL().getUOMID().setUOMCODE(pmSchedule.getMeter1UOM());
		}

		// PPO_METER, PPO_METUOM, PPO_METERDUE (2)
		if (pmSchedule.getMeter2Due() != null ||
				pmSchedule.getMeter2Interval() != null ||
				pmSchedule.getMeter2UOM() != null) {
			pmScheduleData.setMETER2INTERVAL(new METERINTERVAL_Type());
			pmScheduleData.getMETER2INTERVAL().setINTERVAL(tools.getDataTypeTools().encodeQuantity(pmSchedule.getMeter2Interval(), "Meter 2 Interval"));
			pmScheduleData.getMETER2INTERVAL().setREADINGDUE(tools.getDataTypeTools().encodeQuantity(pmSchedule.getMeter2Due(), "Meter 2 Due"));
			pmScheduleData.getMETER2INTERVAL().setUOMID(new UOMID_Type());
			pmScheduleData.getMETER2INTERVAL().getUOMID().setUOMCODE(pmSchedule.getMeter2UOM());
		}

		// PPO_DEACTIVE
		if (pmSchedule.getDateDeactivated() != null) {
			pmScheduleData.setDEACTIVATEDDATE(tools.getDataTypeTools().formatDate(pmSchedule.getDateDeactivated(), "Date Deactivated"));
		}

		// PPO_LOCATION
		if (pmSchedule.getLocation() != null) {
			pmScheduleData.setLOCATIONID(new LOCATIONID_Type());
			pmScheduleData.getLOCATIONID().setORGANIZATIONID(tools.getOrganization(context));
			pmScheduleData.getLOCATIONID().setLOCATIONCODE(pmSchedule.getLocation());
		}

		// ASSIGNED TO
		if (pmSchedule.getAssignedTo() != null) {
			pmScheduleData.setASSIGNEDTO(new PERSONID_Type());
			pmScheduleData.getASSIGNEDTO().setPERSONCODE(pmSchedule.getAssignedTo());
		}

		// WO CLASS
		if (pmSchedule.getWorkOrderClass() != null) {
			pmScheduleData.setWORKORDERCLASSID(new CLASSID_Type());
			pmScheduleData.getWORKORDERCLASSID().setORGANIZATIONID(tools.getOrganization(context));
			pmScheduleData.getWORKORDERCLASSID().setCLASSCODE(pmSchedule.getWorkOrderClass());
		}

		// Sync Equipment PM Schedule
		MP0365_SyncEquipmentPMSchedule_001 syncpm = new MP0365_SyncEquipmentPMSchedule_001();
		syncpm.setPMScheduleData(pmScheduleData);

		MP0365_SyncEquipmentPMSchedule_001_Result syncresult = null;

		if (context.getCredentials() != null) {
			syncresult = inforws.syncEquipmentPMScheduleOp(syncpm, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			syncresult = inforws.syncEquipmentPMScheduleOp(syncpm, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return syncresult.getResultData().getPMSCHEDULEEQUIPMENTID().getSEQUENCENUMBER() + "";
	}
}
