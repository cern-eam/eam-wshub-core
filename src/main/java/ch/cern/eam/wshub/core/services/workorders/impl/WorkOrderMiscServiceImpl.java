package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.entities.MaterialList;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderMiscService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
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
import net.datastream.schemas.mp_functions.mp7593_001.MP7593_AddWorkOrderAdditionalCosts_001;
import net.datastream.schemas.mp_results.mp0044_001.MP0044_AddMeterReading_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.xml.ws.Holder;

public class WorkOrderMiscServiceImpl implements WorkOrderMiscService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public WorkOrderMiscServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	public String createMeterReading(InforContext context, ch.cern.eam.wshub.core.services.workorders.entities.MeterReading meterReadingParam) throws InforException {

		// Handling the normal case for meter reading
		MeterReading meterreadinginfor = new MeterReading();

		meterreadinginfor.setUSAGEUOMID(new UOMID_Type());
		meterreadinginfor.getUSAGEUOMID().setUOMCODE(meterReadingParam.getUOM());

		if (meterReadingParam.getActualValue() != null && !meterReadingParam.getActualValue().trim().equals("")) {
			meterreadinginfor
					.setACTUALREADING(tools.getDataTypeTools().encodeQuantity(meterReadingParam.getActualValue(), "Meter Reading Value"));
		} else if (meterReadingParam.getDifferenceValue() != null
				&& !meterReadingParam.getDifferenceValue().trim().equals("")) {
			meterreadinginfor.setDIFFERENCEREADING(
					tools.getDataTypeTools().encodeQuantity(meterReadingParam.getDifferenceValue(), "Meter Reading Value"));
		} else {
			throw tools.generateFault("Supply actual reading or difference reading value.");
		}

		if (meterReadingParam.getReadingDate() == null) {
			meterreadinginfor.setREADINGDATE(tools.getDataTypeTools().formatDate("SYSDATE", "Meter Reading Date"));
		} else {
			meterreadinginfor
					.setREADINGDATE(tools.getDataTypeTools().encodeInforDate(meterReadingParam.getReadingDate(), "Meter Reading Date"));
		}

		if (meterReadingParam.getEquipmentCode() != null) {
			meterreadinginfor.setTARGETEQUIPMENTID(new EQUIPMENTID_Type());
			meterreadinginfor.getTARGETEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
			meterreadinginfor.getTARGETEQUIPMENTID()
					.setEQUIPMENTCODE(meterReadingParam.getEquipmentCode().trim().toUpperCase());
		}

		if (meterReadingParam.getWoNumber() != null) {
			meterreadinginfor.setWORKORDERID(new WOID_Type());
			meterreadinginfor.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
			meterreadinginfor.getWORKORDERID().setJOBNUM(meterReadingParam.getWoNumber());
		}

		MP0044_AddMeterReading_001 addmeterreading = new MP0044_AddMeterReading_001();
		addmeterreading.setMeterReading(meterreadinginfor);
		MP0044_AddMeterReading_001_Result result = new MP0044_AddMeterReading_001_Result();
		if (context.getCredentials() != null) {
			result = this.inforws.addMeterReadingOp(addmeterreading, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = this.inforws.addMeterReadingOp(addmeterreading, "*", null, null,
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return result.getResultData().getMETERREADINGCODE();

	}

	public String createWorkOrderAdditionalCost(InforContext context,
												ch.cern.eam.wshub.core.services.workorders.entities.WorkOrderAdditionalCosts workOrderAddCostsParam) throws InforException {
		WorkOrderAdditionalCosts workOrderAddCosts = new WorkOrderAdditionalCosts();

		// ACTIVITY AND WORK ORDER NUMBER
		workOrderAddCosts.setACTIVITYID(new ACTIVITYID());
		workOrderAddCosts.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
		workOrderAddCosts.getACTIVITYID().getACTIVITYCODE()
				.setValue(tools.getDataTypeTools().encodeLong(workOrderAddCostsParam.getActivityCode(), "Activity Code"));
		workOrderAddCosts.getACTIVITYID().setWORKORDERID(new WOID_Type());
		workOrderAddCosts.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
		workOrderAddCosts.getACTIVITYID().getWORKORDERID().setJOBNUM(workOrderAddCostsParam.getWorkOrderNumber());

		// COST TYPE
		workOrderAddCosts.setCOSTTYPEID(new COSTTYPEID_Type());
		workOrderAddCosts.getCOSTTYPEID().setCOSTTYPECODE(workOrderAddCostsParam.getCostType());

		// CREATED DATE
		workOrderAddCosts.setCREATEDDATE(tools.getDataTypeTools().formatDate(workOrderAddCostsParam.getDate(), "Created Date"));

		// DESCRIPTION
		workOrderAddCosts.setDESCRIPTION(workOrderAddCostsParam.getCostDescription());

		// UNIT PRICE
		workOrderAddCosts.setUNITPRICE(tools.getDataTypeTools().encodeAmount(workOrderAddCostsParam.getCost(), "Cost Value"));

		workOrderAddCosts.setWOADDITIONALCOSTQTY(tools.getDataTypeTools().encodeQuantity("1", "Additional Quantity"));

		MP7593_AddWorkOrderAdditionalCosts_001 addCost = new MP7593_AddWorkOrderAdditionalCosts_001();
		addCost.setWorkOrderAdditionalCosts(workOrderAddCosts);

		if (context.getCredentials() != null) {
			inforws.addWorkOrderAdditionalCostsOp(addCost, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addWorkOrderAdditionalCostsOp(addCost, "*", null, null,
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return "done";

	}

	public String createMaterialList(InforContext context, MaterialList materialList) throws InforException {

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

		if (context.getCredentials() != null) {
			inforws.addMaterialListPartOp(matList, "*",
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addMaterialListPartOp(matList, "*", null, null, new Holder<>(tools.createInforSession(context)),
					tools.createMessageConfig(), tools.getTenant(context));
		}
		return "done";

	}

	public String addWorkOrderPart(InforContext context, ch.cern.eam.wshub.core.services.entities.WorkOrderPart workOrderPart) throws InforException {

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
		if (workOrderPart.getPlannedQty() != null && !workOrderPart.getPlannedQty().trim().equals("")) {
			wop.setPLANNEDPARTQUANTITY(tools.getDataTypeTools().encodeQuantity(workOrderPart.getPlannedQty(), "Planned Qty."));
		}

		// RESERVED QTY
		if (workOrderPart.getReservedQty() != null && !workOrderPart.getReservedQty().trim().equals("")) {
			wop.setRESERVEDPARTQUANTITY(tools.getDataTypeTools().encodeQuantity(workOrderPart.getReservedQty(), "Reserved Qty."));
		}
		// ALLOCATED QTY
		if (workOrderPart.getAllocatedQty() != null && !workOrderPart.getAllocatedQty().trim().equals("")) {
			wop.setALLOCATEDPARTQUANTITY(tools.getDataTypeTools().encodeQuantity(workOrderPart.getAllocatedQty(), "Allocated Qty."));
		}
		//
		if (workOrderPart.getPlannedSource() != null && !workOrderPart.getPlannedSource().trim().equals("")) {
			wop.setDIRECT(workOrderPart.getPlannedSource());
		}
		//
		MP0071_AddWorkOrderPart_001 addwop = new MP0071_AddWorkOrderPart_001();
		addwop.setWorkOrderPart(wop);

		if (context.getCredentials() != null) {
			inforws.addWorkOrderPartOp(addwop, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addWorkOrderPartOp(addwop, tools.getOrganizationCode(context), null, "",
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return "done";
	}

	public String createRouteEquipment(InforContext context, RouteEquipment routeEquipment) throws InforException {

		net.datastream.schemas.mp_entities.routeequipment_001.RouteEquipment routeEquipmentInfor = new net.datastream.schemas.mp_entities.routeequipment_001.RouteEquipment();
		//
		// ROUTE ID
		//
		routeEquipmentInfor.setROUTEEQUIPMENTID(new ROUTEEQUIPMENTID_Type());
		routeEquipmentInfor.getROUTEEQUIPMENTID().setROUTEEQUIPMENTSEQUENCE(
				tools.getDataTypeTools().encodeLong(routeEquipment.getRouteEquipmentSequence(), "Route Equipment Sequence"));
		routeEquipmentInfor.getROUTEEQUIPMENTID().setROUTEID(new ROUTE_Type());
		routeEquipmentInfor.getROUTEEQUIPMENTID().getROUTEID().setROUTECODE(routeEquipment.getRouteCode());
		if (routeEquipment.getRouteRevision() != null) {
			routeEquipmentInfor.getROUTEEQUIPMENTID().getROUTEID()
					.setROUTEREVISION(tools.getDataTypeTools().encodeLong(routeEquipment.getRouteRevision(), "Route Revision"));
		} else {
			routeEquipmentInfor.getROUTEEQUIPMENTID().getROUTEID().setROUTEREVISION(0l);
		}
		//
		// EQUIPMENT ID
		//
		routeEquipmentInfor.setROUTEEQUIPMENTTYPE(new ROUTEEQUIPMENTTYPE_Type());
		routeEquipmentInfor.getROUTEEQUIPMENTTYPE().setEQUIPMENTID(new EQUIPMENTID_Type());
		routeEquipmentInfor.getROUTEEQUIPMENTTYPE().getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
		routeEquipmentInfor.getROUTEEQUIPMENTTYPE().getEQUIPMENTID()
				.setEQUIPMENTCODE(routeEquipment.getEquipmentCode());
		routeEquipmentInfor.getROUTEEQUIPMENTTYPE().setOBJRTYPE(routeEquipment.getObjRType());
		routeEquipmentInfor.getROUTEEQUIPMENTTYPE().setOBJTYPE(routeEquipment.getObjType());
		//
		MP7153_AddRouteEquipment_001 addRouteEquipment = new MP7153_AddRouteEquipment_001();
		addRouteEquipment.setRouteEquipment(routeEquipmentInfor);

		if (context.getCredentials() != null) {
			inforws.addRouteEquipmentOp(addRouteEquipment, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addRouteEquipmentOp(addRouteEquipment, tools.getOrganizationCode(context), null, "",
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return "done";
	}

	public String deleteRouteEquipment(InforContext context, RouteEquipment routeEquipment) throws InforException {
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


		if (context.getCredentials() != null) {
			inforws.deleteRouteEquipmentOp(deleteRouteEquipment, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.deleteRouteEquipmentOp(deleteRouteEquipment, tools.getOrganizationCode(context), null, "",
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return "done";
	}

	//TODO
	/*
	public String syncRoutes(String nothing, Credentials credentials, String sessionID) throws InforException {
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

	public String createTaskPlan(InforContext context, TaskPlan taskPlan) throws InforException {

		Task inforTask = new Task();

		// CODE, DESCRIPTION
		inforTask.setTASKLISTID(new TASKLISTID_Type());
		inforTask.getTASKLISTID().setDESCRIPTION(taskPlan.getDescription());
		inforTask.getTASKLISTID().setTASKCODE(taskPlan.getCode());
		inforTask.getTASKLISTID().setORGANIZATIONID(tools.getOrganization(context));
		if (taskPlan.getRevision() == null) {
			inforTask.getTASKLISTID().setTASKREVISION(0L);

		} else {
			inforTask.getTASKLISTID().setTASKREVISION(tools.getDataTypeTools().encodeLong(taskPlan.getRevision(), "Task Revision"));
		}

		// TRADE
		if (taskPlan.getTradeCode() != null) {
			inforTask.setTRADEID(new TRADEID_Type());
			inforTask.getTRADEID().setTRADECODE(taskPlan.getTradeCode());
			inforTask.getTRADEID().setORGANIZATIONID(tools.getOrganization(context));
			// PEOPLE REQUIRED
			if (taskPlan.getPeopleRequired() == null) {
				inforTask.setPERSONS(1l);
			} else {
				inforTask.setPERSONS(tools.getDataTypeTools().encodeLong(taskPlan.getPeopleRequired(), "People Required"));
			}
			// PEOPLE REQUIRED
			if (taskPlan.getEstimatedHours() == null) {
				inforTask.setHOURSREQUESTED(tools.getDataTypeTools().encodeQuantity("1", "Estimated Hours"));
			} else {
				inforTask.setHOURSREQUESTED(tools.getDataTypeTools().encodeQuantity(taskPlan.getEstimatedHours(), "Estimated Hours"));
			}
		}

		// STATUS
		if (taskPlan.getRevisionStatus() == null) {
			inforTask.setSTATUS(new STATUS_Type());
			inforTask.getSTATUS().setSTATUSCODE("A");
		} else {
			inforTask.setSTATUS(new STATUS_Type());
			inforTask.getSTATUS().setSTATUSCODE(taskPlan.getRevisionStatus());
		}

		// PLANNING LEVEL
		inforTask.setPLANNINGLEVEL("TP");

		// TYPE
		if (taskPlan.getTypeCode() != null) {
			inforTask.setTASKPLANTYPE(taskPlan.getTypeCode());
		}

		// CLASS
		if (taskPlan.getClassCode() != null) {
			inforTask.setCLASSID(new CLASSID_Type());
			inforTask.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
			inforTask.getCLASSID().setCLASSCODE(taskPlan.getClassCode());
		}

		//
		if (taskPlan.getOutOfService() != null) {
			inforTask.setOUTOFSERVICE(taskPlan.getOutOfService());
		}

		if (taskPlan.getEquipmentType() != null) {
			inforTask.setEQUIPMENTTYPE(taskPlan.getEquipmentType());
		}

		if (taskPlan.getEquipmentClass() != null) {
			inforTask.setEQUIPMENTCLASSID(new CLASSID_Type());
			inforTask.getEQUIPMENTCLASSID().setORGANIZATIONID(tools.getOrganization(context));
			inforTask.getEQUIPMENTCLASSID().setCLASSCODE(taskPlan.getEquipmentClass());
		}

		if (taskPlan.getMaterialList() != null) {
			inforTask.setMATERIALLISTID(new MATERIALLISTID_Type());
			inforTask.getMATERIALLISTID().setORGANIZATIONID(tools.getOrganization(context));
			inforTask.getMATERIALLISTID().setMTLCODE(taskPlan.getMaterialList());
		}
		//
		MP0080_AddTask_001 addTask = new MP0080_AddTask_001();
		addTask.setTask(inforTask);

		if (context.getCredentials() != null) {
			inforws.addTaskOp(addTask, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addTaskOp(addTask, tools.getOrganizationCode(context), null, "",
					new Holder<>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return "done";
	}

}
