package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.material.entities.MaterialList;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderMiscService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.RouteEquipment;
import ch.cern.eam.wshub.core.services.workorders.entities.TaskPlan;
import net.datastream.schemas.mp_entities.materiallistpart_001.MaterialListPart;
import net.datastream.schemas.mp_entities.meterreading_001.MeterReading;
import net.datastream.schemas.mp_entities.task_001.Task;
import net.datastream.schemas.mp_entities.workorderadditionalcosts_001.WorkOrderAdditionalCosts;
import net.datastream.schemas.mp_entities.workorderpart_001.WorkOrderPart;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0044_001.MP0044_AddMeterReading_001;
import net.datastream.schemas.mp_functions.mp0067_001.MP0067_AddMaterialListPart_001;
import net.datastream.schemas.mp_functions.mp0071_001.MP0071_AddWorkOrderPart_001;
import net.datastream.schemas.mp_functions.mp0080_001.MP0080_AddTask_001;
import net.datastream.schemas.mp_functions.mp7153_001.MP7153_AddRouteEquipment_001;
import net.datastream.schemas.mp_functions.mp7156_001.MP7156_DeleteRouteEquipment_001;
import net.datastream.schemas.mp_functions.mp7336_001.MP7336_GetWOEquipLinearDetails_001;
import net.datastream.schemas.mp_functions.mp7593_001.MP7593_AddWorkOrderAdditionalCosts_001;
import net.datastream.schemas.mp_results.mp0044_001.MP0044_AddMeterReading_001_Result;
import net.datastream.schemas.mp_results.mp7336_001.AdditionalWOEquipDetails;
import net.datastream.schemas.mp_results.mp7336_001.MP7336_GetWOEquipLinearDetails_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import jakarta.xml.ws.Holder;
import java.math.BigDecimal;
import java.math.BigInteger;

public class WorkOrderMiscServiceImpl implements WorkOrderMiscService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	public WorkOrderMiscServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
	}

	public String createMeterReading(EAMContext context, ch.cern.eam.wshub.core.services.workorders.entities.MeterReading meterReadingParam) throws EAMException {
		// Handling the normal case for meter reading
		MeterReading meterreadingeam = new MeterReading();
		tools.getEAMFieldTools().transformWSHubObject(meterreadingeam, meterReadingParam, context);

		if (meterReadingParam.getActualValue() != null) {
			meterreadingeam
					.setACTUALREADING(tools.getDataTypeTools().encodeQuantity(meterReadingParam.getActualValue(), "Meter Reading Value"));
		} else if (meterReadingParam.getDifferenceValue() != null) {
			meterreadingeam.setDIFFERENCEREADING(
					tools.getDataTypeTools().encodeQuantity(meterReadingParam.getDifferenceValue(), "Meter Reading Value"));
		} else {
			throw tools.generateFault("Supply actual reading or difference reading value.");
		}

		if (meterReadingParam.getReadingDate() == null) {
			meterreadingeam.setREADINGDATE(tools.getDataTypeTools().formatDate("SYSDATE", "Meter Reading Date"));
		} else {
			meterreadingeam
					.setREADINGDATE(tools.getDataTypeTools().encodeEAMDate(meterReadingParam.getReadingDate(), "Meter Reading Date"));
		}

		MP0044_AddMeterReading_001 addmeterreading = new MP0044_AddMeterReading_001();
		addmeterreading.setMeterReading(meterreadingeam);
		MP0044_AddMeterReading_001_Result result =
			tools.performEAMOperation(context, eamws::addMeterReadingOp, addmeterreading);
		return result.getResultData().getMETERREADINGCODE();

	}

	public String createWorkOrderAdditionalCost(EAMContext context,
												ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderAdditionalCosts workOrderAddCostsParam) throws EAMException {
		WorkOrderAdditionalCosts workOrderAddCosts = new WorkOrderAdditionalCosts();

		tools.getEAMFieldTools().transformWSHubObject(workOrderAddCosts, workOrderAddCostsParam, context);

		workOrderAddCosts.setWOADDITIONALCOSTQTY(tools.getDataTypeTools().encodeQuantity(BigDecimal.ONE, "Additional Quantity"));

		// This is required so that the equipment is set as 'WO Header Equipment'
		MULTIEQUIPSPLITINFO multiEquipSplitInfo = new MULTIEQUIPSPLITINFO();
		WOID_Type woIdType = new WOID_Type();
		woIdType.setJOBNUM(workOrderAddCostsParam.getWorkOrderNumber());
		multiEquipSplitInfo.setRELATEDWORKORDERID(woIdType);

		workOrderAddCosts.setMULTIEQUIPSPLITINFO(multiEquipSplitInfo);

		MP7593_AddWorkOrderAdditionalCosts_001 addCost = new MP7593_AddWorkOrderAdditionalCosts_001();
		addCost.setWorkOrderAdditionalCosts(workOrderAddCosts);

		tools.performEAMOperation(context, eamws::addWorkOrderAdditionalCostsOp, addCost);

		return "done";
	}

	public String createMaterialList(EAMContext context, MaterialList materialList) throws EAMException {

		MP0067_AddMaterialListPart_001 matList = new MP0067_AddMaterialListPart_001();
		matList.setMaterialListPart(new MaterialListPart());
		// MAT LIST
		matList.getMaterialListPart().setMATERIALLISTPARTID(new MATERIALLISTPARTID_Type());
		matList.getMaterialListPart().getMATERIALLISTPARTID().setMATERIALLISTID(new MATERIALLISTID_Type());
		if (materialList.getMaterialListCode() != null && !materialList.getMaterialListCode().trim().equals("")) {
			matList.getMaterialListPart().getMATERIALLISTPARTID().getMATERIALLISTID().setMTLREVISION(0L);
			matList.getMaterialListPart().getMATERIALLISTPARTID().getMATERIALLISTID()
					.setMTLCODE(materialList.getMaterialListCode().toUpperCase().trim());
			matList.getMaterialListPart().getMATERIALLISTPARTID().getMATERIALLISTID()
					.setORGANIZATIONID(tools.getOrganization(context));
		}
		matList.getMaterialListPart().getMATERIALLISTPARTID()
				.setMATERIALLISTPARTLINENUM(tools.getDataTypeTools().encodeLong(materialList.getLineNumber(), "Line Number"));

		// PART
		if (materialList.getPartCode() != null && !materialList.getPartCode().trim().equals("")) {
			matList.getMaterialListPart().setPARTID(new PARTID_Type());
			matList.getMaterialListPart().getPARTID().setORGANIZATIONID(tools.getOrganization(context));
			matList.getMaterialListPart().getPARTID().setPARTCODE(materialList.getPartCode());
		}
		// QUANTITY
		matList.getMaterialListPart().setPARTQUANTITY(tools.getDataTypeTools().encodeQuantity(materialList.getQuantity(), "Quantity"));

		// RESERVE
		if (materialList.getReserve() != null) {
			matList.getMaterialListPart().setRESERVEPART(materialList.getReserve().trim());
		}

		// EQUIPMENT
		if (materialList.getEquipmentCode() != null && !materialList.getEquipmentCode().trim().equals("")) {
			matList.getMaterialListPart().setEQUIPMENTID(new EQUIPMENTID_Type());
			matList.getMaterialListPart().getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
			matList.getMaterialListPart().getEQUIPMENTID().setEQUIPMENTCODE(materialList.getEquipmentCode());
		}

		tools.performEAMOperation(context, eamws::addMaterialListPartOp, matList);
		return "done";

	}

	public String addWorkOrderPart(EAMContext context, ch.cern.eam.wshub.core.services.entities.WorkOrderPart workOrderPart) throws EAMException {

		WorkOrderPart wop = new WorkOrderPart();
		// PART ID
		wop.setPARTID(new PARTID_Type());
		wop.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
		wop.getPARTID().setPARTCODE(workOrderPart.getPartCode());

		// STORE ID
		if (workOrderPart.getStoreCode() != null) {
			wop.setSTOREID(new STOREID_Type());
			wop.getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
			wop.getSTOREID().setSTORECODE(workOrderPart.getStoreCode());
		}

		// WORK ORDER PART ID
		wop.setWORKORDERPARTID(new WORKORDERPARTID());

		wop.getWORKORDERPARTID().setACTIVITYID(new ACTIVITYID());
		wop.getWORKORDERPARTID().getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
		wop.getWORKORDERPARTID().getACTIVITYID().getACTIVITYCODE()
				.setValue(tools.getDataTypeTools().encodeLong(workOrderPart.getActivityCode(), "Activity Code"));

		wop.getWORKORDERPARTID().getACTIVITYID().setWORKORDERID(new WOID_Type());
		wop.getWORKORDERPARTID().getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		wop.getWORKORDERPARTID().getACTIVITYID().getWORKORDERID().setJOBNUM(workOrderPart.getWorkOrderNumber());

		// PLANNED QTY
		if (workOrderPart.getPlannedQty() != null) {
			wop.setPLANNEDPARTQUANTITY(tools.getDataTypeTools().encodeQuantity(workOrderPart.getPlannedQty(), "Planned Qty."));
		}

		// RESERVED QTY
		if (workOrderPart.getReservedQty() != null) {
			wop.setRESERVEDPARTQUANTITY(tools.getDataTypeTools().encodeQuantity(workOrderPart.getReservedQty(), "Reserved Qty."));
		}
		// ALLOCATED QTY
		if (workOrderPart.getAllocatedQty() != null) {
			wop.setALLOCATEDPARTQUANTITY(tools.getDataTypeTools().encodeQuantity(workOrderPart.getAllocatedQty(), "Allocated Qty."));
		}
		//
		if (workOrderPart.getPlannedSource() != null && !workOrderPart.getPlannedSource().trim().equals("")) {
			wop.setDIRECT(workOrderPart.getPlannedSource());
		}
		//
		MP0071_AddWorkOrderPart_001 addwop = new MP0071_AddWorkOrderPart_001();
		addwop.setWorkOrderPart(wop);

		tools.performEAMOperation(context, eamws::addWorkOrderPartOp, addwop);
		return "done";
	}

	public String createRouteEquipment(EAMContext context, RouteEquipment routeEquipment) throws EAMException {

		net.datastream.schemas.mp_entities.routeequipment_001.RouteEquipment routeEquipmentEAM = new net.datastream.schemas.mp_entities.routeequipment_001.RouteEquipment();
		//
		// ROUTE ID
		//
		routeEquipmentEAM.setROUTEEQUIPMENTID(new ROUTEEQUIPMENTID_Type());
		routeEquipmentEAM.getROUTEEQUIPMENTID().setROUTEEQUIPMENTSEQUENCE(
				tools.getDataTypeTools().encodeLong(routeEquipment.getRouteEquipmentSequence(), "Route Equipment Sequence"));
		routeEquipmentEAM.getROUTEEQUIPMENTID().setROUTEID(new ROUTE_Type());
		routeEquipmentEAM.getROUTEEQUIPMENTID().getROUTEID().setROUTECODE(routeEquipment.getRouteCode());
		if (routeEquipment.getRouteRevision() != null) {
			routeEquipmentEAM.getROUTEEQUIPMENTID().getROUTEID()
					.setROUTEREVISION(tools.getDataTypeTools().encodeLong(routeEquipment.getRouteRevision(), "Route Revision"));
		} else {
			routeEquipmentEAM.getROUTEEQUIPMENTID().getROUTEID().setROUTEREVISION(0l);
		}
		//
		// EQUIPMENT ID
		//
		routeEquipmentEAM.setROUTEEQUIPMENTTYPE(new ROUTEEQUIPMENTTYPE_Type());
		routeEquipmentEAM.getROUTEEQUIPMENTTYPE().setEQUIPMENTID(new EQUIPMENTID_Type());
		routeEquipmentEAM.getROUTEEQUIPMENTTYPE().getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
		routeEquipmentEAM.getROUTEEQUIPMENTTYPE().getEQUIPMENTID()
				.setEQUIPMENTCODE(routeEquipment.getEquipmentCode());
		routeEquipmentEAM.getROUTEEQUIPMENTTYPE().setOBJRTYPE(routeEquipment.getObjRType());
		routeEquipmentEAM.getROUTEEQUIPMENTTYPE().setOBJTYPE(routeEquipment.getObjType());
		//
		MP7153_AddRouteEquipment_001 addRouteEquipment = new MP7153_AddRouteEquipment_001();
		addRouteEquipment.setRouteEquipment(routeEquipmentEAM);

		tools.performEAMOperation(context, eamws::addRouteEquipmentOp, addRouteEquipment);
		return "done";
	}

	public String deleteRouteEquipment(EAMContext context, RouteEquipment routeEquipment) throws EAMException {
		//
		MP7156_DeleteRouteEquipment_001 deleteRouteEquipment = new MP7156_DeleteRouteEquipment_001();

		deleteRouteEquipment.setROUTEEQUIPMENTID(new ROUTEEQUIPMENTID_Type());
		deleteRouteEquipment.getROUTEEQUIPMENTID().setROUTEEQUIPMENTSEQUENCE(
				tools.getDataTypeTools().encodeLong(routeEquipment.getRouteEquipmentSequence(), "Route Equipment Sequence"));
		deleteRouteEquipment.getROUTEEQUIPMENTID().setROUTEID(new ROUTE_Type());
		deleteRouteEquipment.getROUTEEQUIPMENTID().getROUTEID().setROUTECODE(routeEquipment.getRouteCode());
		if (routeEquipment.getRouteRevision() != null) {
			deleteRouteEquipment.getROUTEEQUIPMENTID().getROUTEID()
					.setROUTEREVISION(tools.getDataTypeTools().encodeLong(routeEquipment.getRouteRevision(), "Route Revision"));
		} else {
			deleteRouteEquipment.getROUTEEQUIPMENTID().getROUTEID().setROUTEREVISION(0l);
		}

		tools.performEAMOperation(context, eamws::deleteRouteEquipmentOp, deleteRouteEquipment);
		return "done";
	}

	//TODO
	/*
	public String syncRoutes(String nothing, Credentials credentials, String sessionID) throws EAMException {
		EntityManager em = tools.getEntityManager();
		try {
			List<RouteEquipment> equipmentUdfRoutes = em.createNamedQuery("FINDUDFROUTES", RouteEquipment.class)
					.getResultList();
			Map<String, List<RouteEquipment>> equipmentUdfRoutesMap = equipmentUdfRoutes.stream()
					.collect(Collectors.groupingBy(RouteEquipment::getRouteCode));

			for (String route : equipmentUdfRoutesMap.keySet()) {
				List<RouteEquipment> equipmentForRoute = em.createNamedQuery("FINDEQPROUTES", RouteEquipment.class)
						.setParameter("route", route).getResultList();
				int sequenceOfLastRoute = 1;
				if (equipmentForRoute.size() > 0) {
					sequenceOfLastRoute = Integer.parseInt(
							equipmentForRoute.get(equipmentForRoute.size() - 1).getRouteEquipmentSequence()) + 5;
				}
				for (RouteEquipment re : equipmentUdfRoutesMap.get(route)) {
					re.setRouteEquipmentSequence(Integer.toString(sequenceOfLastRoute));
					System.out.println("CREATING: " + re);
					createRouteEquipment(re, credentials, sessionID);
					sequenceOfLastRoute += 5;
				}
			}

			return "OK";
		} catch (Exception e) {
			throw tools.generateFault("Couldn't fetch activities for this work order. " + e.getMessage());
		} finally {
			em.clear();
			em.close();
		}


	}
	*/

	public String createTaskPlan(EAMContext context, TaskPlan taskPlan) throws EAMException {
		// Provide defaults for the revision control if not present
		if (taskPlan.getTaskRevision() == null) {
			taskPlan.setTaskRevision(BigInteger.ZERO);
		}

		if (taskPlan.getRevisionStatus() == null) {
			taskPlan.setRevisionStatus("A");
		}

		// Create the Task Plan
		MP0080_AddTask_001 addTask = new MP0080_AddTask_001();
		addTask.setTask(tools.getEAMFieldTools().transformWSHubObject(new Task(), taskPlan, context));

		tools.performEAMOperation(context, eamws::addTaskOp, addTask);
		return "done";
	}

	@Override
	public AdditionalWOEquipDetails getEquipLinearDetails(final EAMContext context, final String eqCode) throws EAMException {
		MP7336_GetWOEquipLinearDetails_001 op = new MP7336_GetWOEquipLinearDetails_001();
		op.setEQUIPMENTID(new EQUIPMENTID_Type());
		op.getEQUIPMENTID().setEQUIPMENTCODE(eqCode);
		op.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
		op.setORGANIZATIONID(tools.getOrganization(context));
		final MP7336_GetWOEquipLinearDetails_001_Result additionalWOEquipDetails =
				tools.performEAMOperation(context, eamws::getWOEquipLinearDetailsOp, op);
		return additionalWOEquipDetails.getResultData().getAdditionalWOEquipDetails();
	}

}
