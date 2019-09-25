package ch.cern.eam.wshub.core.services.workorders.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.workorders.InspectionService;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.services.workorders.entities.Aspect;
import ch.cern.eam.wshub.core.services.workorders.entities.AspectPoint;
import ch.cern.eam.wshub.core.services.workorders.entities.Point;
import net.datastream.schemas.mp_entities.inspectionaspect_001.InspectionAspect;
import net.datastream.schemas.mp_entities.inspectionaspectpoint_001.InspectionAspectPoint;
import net.datastream.schemas.mp_entities.inspectionpoint_001.InspectionPoint;
import net.datastream.schemas.mp_entities.inspectionsforworkorder_001.InspectionsForWorkOrder;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp1017_001.MP1017_AddAspect_001;
import net.datastream.schemas.mp_functions.mp1022_001.MP1022_AddInspectionAspect_001;
import net.datastream.schemas.mp_functions.mp1027_001.MP1027_AddInspectionPoint_001;
import net.datastream.schemas.mp_functions.mp1031_001.MP1031_AddInspectionAspectPoint_001;
import net.datastream.schemas.mp_functions.mp7177_001.MP7177_AddInspectionsForWorkOrder_001;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;

public class InspectionServiceImpl implements InspectionService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public InspectionServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	//
	//
	//
	private void addPoint(InforContext context, Point point, String pobject) throws InforException {
		InspectionPoint inspectionPoint = new InspectionPoint();

		inspectionPoint.setINSPECTIONPOINTID(new INSPECTIONPOINTID_Type());
		inspectionPoint.getINSPECTIONPOINTID().setDESCRIPTION(point.getDesc());
		inspectionPoint.getINSPECTIONPOINTID().setPOINTCODE(point.getCode());
		inspectionPoint.getINSPECTIONPOINTID().setOBJTYPE("L");

		inspectionPoint.getINSPECTIONPOINTID().setPOINTTYPEID(new POINTTYPEID_Type());
		inspectionPoint.getINSPECTIONPOINTID().getPOINTTYPEID().setPOINTTYPECODE(point.getPointType());

		inspectionPoint.getINSPECTIONPOINTID().setINSPECTIONOBJECTID(new OBJECT_Type());
		inspectionPoint.getINSPECTIONPOINTID().getINSPECTIONOBJECTID().setOBJECTCODE(pobject);
		inspectionPoint.getINSPECTIONPOINTID().getINSPECTIONOBJECTID().setORGANIZATIONID(tools.getOrganization(context));

		MP1027_AddInspectionPoint_001 addInspPoint = new MP1027_AddInspectionPoint_001();
		addInspPoint.setInspectionPoint(inspectionPoint);

		if (context.getCredentials() != null) {
			inforws.addInspectionPointOp(addInspPoint, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addInspectionPointOp(addInspPoint, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

	}

	private void addInspectionAspect(InforContext context, Aspect aspect, String aobject) throws InforException {
		InspectionAspect inspectionAspect = new InspectionAspect();
		inspectionAspect.setINSPECTIONASPECTID(new INSPECTIONASPECTID_Type());
		inspectionAspect.getINSPECTIONASPECTID().setOBJTYPE("L");

		inspectionAspect.getINSPECTIONASPECTID().setASPECTID(new ASPECTID_Type());
		inspectionAspect.getINSPECTIONASPECTID().getASPECTID().setASPECTCODE(aspect.getCode());

		inspectionAspect.getINSPECTIONASPECTID().setINSPECTIONOBJECTID(new OBJECT_Type());
		inspectionAspect.getINSPECTIONASPECTID().getINSPECTIONOBJECTID().setORGANIZATIONID(tools.getOrganization(context));
		inspectionAspect.getINSPECTIONASPECTID().getINSPECTIONOBJECTID().setOBJECTCODE(aobject);

		MP1022_AddInspectionAspect_001 addInspAspect = new MP1022_AddInspectionAspect_001();
		addInspAspect.setInspectionAspect(inspectionAspect);
		if (context.getCredentials() != null) {
			inforws.addInspectionAspectOp(addInspAspect, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addInspectionAspectOp(addInspAspect, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
	}

	private void addAspectPoint(InforContext context, AspectPoint aspectPoint, String aobject)
			throws InforException {
		InspectionAspectPoint inspectionAspectPoint = new InspectionAspectPoint();

		inspectionAspectPoint.setINSPECTIONASPECTPOINTID(new INSPECTIONASPECTPOINTID_Type());
		inspectionAspectPoint.getINSPECTIONASPECTPOINTID().setASPECTID(new ASPECTID_Type());
		inspectionAspectPoint.getINSPECTIONASPECTPOINTID().getASPECTID().setASPECTCODE(aspectPoint.getAspectCode());

		inspectionAspectPoint.getINSPECTIONASPECTPOINTID().setINSPECTIONPOINTID(new INSPECTIONPOINTID_Type());
		inspectionAspectPoint.getINSPECTIONASPECTPOINTID().getINSPECTIONPOINTID()
				.setPOINTCODE(aspectPoint.getPointCode());
		inspectionAspectPoint.getINSPECTIONASPECTPOINTID().getINSPECTIONPOINTID().setOBJTYPE("L");

		inspectionAspectPoint.getINSPECTIONASPECTPOINTID().getINSPECTIONPOINTID().setPOINTTYPEID(new POINTTYPEID_Type());
		inspectionAspectPoint.getINSPECTIONASPECTPOINTID().getINSPECTIONPOINTID().getPOINTTYPEID()
				.setPOINTTYPECODE(aspectPoint.getPointType());

		inspectionAspectPoint.getINSPECTIONASPECTPOINTID().getINSPECTIONPOINTID()
				.setINSPECTIONOBJECTID(new OBJECT_Type());
		inspectionAspectPoint.getINSPECTIONASPECTPOINTID().getINSPECTIONPOINTID().getINSPECTIONOBJECTID()
				.setORGANIZATIONID(tools.getOrganization(context));
		inspectionAspectPoint.getINSPECTIONASPECTPOINTID().getINSPECTIONPOINTID().getINSPECTIONOBJECTID()
				.setOBJECTCODE(aobject);

		MP1031_AddInspectionAspectPoint_001 addInspAspectPoint = new MP1031_AddInspectionAspectPoint_001();
		addInspAspectPoint.setInspectionAspectPoint(inspectionAspectPoint);

		if (context.getCredentials() != null) {
			inforws.addInspectionAspectPointOp(addInspAspectPoint, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addInspectionAspectPointOp(addInspAspectPoint, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
	}

	private void addWOInspections(InforContext context, AspectPoint aspectPoint, String inspobject, String woNumber, String seqNumber) throws InforException {
		InspectionsForWorkOrder inspectionsForWO = new InspectionsForWorkOrder();
		//
		inspectionsForWO.setEVENTPOINTID(new EVENTPOINTID_Type());
		inspectionsForWO.getEVENTPOINTID().setEVENTPOINTCODE("");

		inspectionsForWO.setWORKORDERID(new WOID_Type());
		inspectionsForWO.getWORKORDERID().setJOBNUM(woNumber);
		inspectionsForWO.getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));

		inspectionsForWO.setINSPECTIONSEQUENCENUMBER(tools.getDataTypeTools().encodeQuantity(seqNumber, "Inspection Sequence Number"));

		inspectionsForWO.setASPECTPOINTID(new ASPECTPOINTID_Type());
		inspectionsForWO.getASPECTPOINTID().setEQUIPMENTID(new EQUIPMENTID_Type());
		inspectionsForWO.getASPECTPOINTID().getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
		inspectionsForWO.getASPECTPOINTID().getEQUIPMENTID().setEQUIPMENTCODE(inspobject);

		inspectionsForWO.getASPECTPOINTID().setASPECTID(new ASPECTID_Type());
		inspectionsForWO.getASPECTPOINTID().getASPECTID().setASPECTCODE(aspectPoint.getAspectCode());

		inspectionsForWO.getASPECTPOINTID().setPOINTTYPEID(new POINTTYPEID_Type());
		inspectionsForWO.getASPECTPOINTID().getPOINTTYPEID().setPOINTTYPECODE(aspectPoint.getPointType());

		inspectionsForWO.getASPECTPOINTID().setASPECTPOINTCODE(aspectPoint.getPointCode());
		//
		//
		//
		MP7177_AddInspectionsForWorkOrder_001 addInspForWO = new MP7177_AddInspectionsForWorkOrder_001();
		addInspForWO.setInspectionsForWorkOrder(inspectionsForWO);

		if (context.getCredentials() != null) {
			inforws.addInspectionsForWorkOrderOp(addInspForWO, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addInspectionsForWorkOrderOp(addInspForWO, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

	}


	public String addAspect(InforContext context, Aspect aspect) throws InforException {
		net.datastream.schemas.mp_entities.aspect_001.Aspect inforAspect = new net.datastream.schemas.mp_entities.aspect_001.Aspect();

		inforAspect.setASPECTID(new ASPECTID_Type());
		inforAspect.getASPECTID().setASPECTCODE(aspect.getCode());
		inforAspect.getASPECTID().setDESCRIPTION(aspect.getDesc());

		MP1017_AddAspect_001 addAspect = new MP1017_AddAspect_001();
		addAspect.setAspect(inforAspect);
		if (context.getCredentials() != null) {
			inforws.addAspectOp(addAspect, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					tools.createMessageConfig(), tools.getTenant(context));
		} else {
			inforws.addAspectOp(addAspect, tools.getOrganizationCode(context), null, "",
					new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}
		return aspect.getCode();
	}


}
