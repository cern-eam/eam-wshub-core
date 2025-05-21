package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.material.entities.IssueReturnPartTransaction;
import ch.cern.eam.wshub.core.services.material.entities.IssueReturnPartTransactionLine;
import ch.cern.eam.wshub.core.services.material.PartMiscService;
import ch.cern.eam.wshub.core.services.material.entities.*;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderService;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.services.workorders.impl.WorkOrderServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.DataTypeTools;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.binstock_001.BinStock;
import net.datastream.schemas.mp_entities.catalogue_001.Catalogue;
import net.datastream.schemas.mp_entities.issuereturntransaction_001.IssueReturnTransaction;
import net.datastream.schemas.mp_entities.issuereturntransactionline_001.IssueReturnTransactionLine;
import net.datastream.schemas.mp_entities.issuereturntransactionline_001.IssueReturnTransactionLines;
import net.datastream.schemas.mp_entities.partsassociated_001.PartsAssociated;
import net.datastream.schemas.mp_entities.storebin_001.StoreBin;
import net.datastream.schemas.mp_entities.substitutepart_001.SubstitutePart;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp0220_001.MP0220_AddIssueReturnTransaction_001;
import net.datastream.schemas.mp_functions.mp0271_001.MP0271_AddCatalogue_001;
import net.datastream.schemas.mp_functions.mp0281_001.MP0281_AddStoreBin_001;
import net.datastream.schemas.mp_functions.mp0282_001.MP0282_GetStoreBin_001;
import net.datastream.schemas.mp_functions.mp0283_001.MP0283_SyncStoreBin_001;
import net.datastream.schemas.mp_functions.mp0284_001.MP0284_DeleteStoreBin_001;
import net.datastream.schemas.mp_functions.mp0286_001.MP0286_Bin2BinTransfer_001;
import net.datastream.schemas.mp_functions.mp0612_001.MP0612_AddPartsAssociated_001;
import net.datastream.schemas.mp_functions.mp0614_001.MP0614_DeletePartsAssociated_001;
import net.datastream.schemas.mp_functions.mp2051_001.MP2051_AddSubstitutePart_001;
import net.datastream.schemas.mp_results.mp0220_001.MP0220_AddIssueReturnTransaction_001_Result;
import net.datastream.schemas.mp_results.mp0282_001.MP0282_GetStoreBin_001_Result;
import net.datastream.schemas.mp_results.mp0283_001.MP0283_SyncStoreBin_001_Result;
import net.datastream.schemas.mp_results.mp0284_001.MP0284_DeleteStoreBin_001_Result;
import net.datastream.schemas.mp_results.mp0612_001.MP0612_AddPartsAssociated_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.decodeBoolean;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PartMiscServiceImpl implements PartMiscService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	private WorkOrderService workOrderService;

	public PartMiscServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		this.workOrderService = new WorkOrderServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
	}

	public String addPartSupplier(InforContext context, PartSupplier partSupplierParam) throws InforException {

		Catalogue catalogue = new Catalogue();

		if (partSupplierParam.getPartCode() != null) {
			catalogue.setPARTID(new PARTID_Type());
			catalogue.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
			catalogue.getPARTID().setPARTCODE(partSupplierParam.getPartCode());
		}

		//
		if (partSupplierParam.getSupplierPartDescription() != null) {
			catalogue.setSUPPLIERPARTDESCRIPTION(partSupplierParam.getSupplierPartDescription());
		}

		//
		if (partSupplierParam.getCatalogReference() != null) {
			// catalogue.get
		}

		//
		if (partSupplierParam.getGrossPrice() != null) {
			catalogue.setGROSSPRICE(tools.getDataTypeTools().encodeAmount(partSupplierParam.getGrossPrice(), "Gross Price"));
		}

		//
		if (partSupplierParam.getMinimumOrderQty() != null) {
			catalogue.setMINIMUMQTY(tools.getDataTypeTools().encodeAmount(partSupplierParam.getMinimumOrderQty(), "Minimum Order Qty."));
		}

		//
		if (partSupplierParam.getSupplierCode() != null) {
			catalogue.setSUPPLIERID(new SUPPLIERID_Type());
			catalogue.getSUPPLIERID().setORGANIZATIONID(tools.getOrganization(context));
			catalogue.getSUPPLIERID().setSUPPLIERCODE(partSupplierParam.getSupplierCode());
		}

		MP0271_AddCatalogue_001 addCatalogue = new MP0271_AddCatalogue_001();
		addCatalogue.setCatalogue(catalogue);

		tools.performInforOperation(context, inforws::addCatalogueOp, addCatalogue);

		return null;
	}

	public List<String> createIssueReturnTransaction(InforContext context, List<IssueReturnPartTransaction> issueReturnPartTransactionList) throws InforException {
		List<String> results = new ArrayList<String>();
		for (IssueReturnPartTransaction tr : issueReturnPartTransactionList) {
			results.add(createIssueReturnTransaction(context, tr));
		}
		return results;
	}

	public String createIssueReturnTransaction(InforContext context, IssueReturnPartTransaction issueReturnPartTransaction) throws InforException {
		IssueReturnTransaction issueReturnTransactionInfor = new IssueReturnTransaction();

		//
		// TRANSACTION TYPE
		//
		if (issueReturnPartTransaction.getTransactionType() != null) {
			if (issueReturnPartTransaction.getTransactionType().toUpperCase().startsWith("I")) {
				issueReturnTransactionInfor.setISSUERETURNTYPE("ISSUE");
			} else {
				issueReturnTransactionInfor.setISSUERETURNTYPE("RETURN");
			}
		}

		switch (issueReturnPartTransaction.getTransactionOn()) {
			case WORKORDER:
				// WORK ORDER - ACTIVITY
				if (issueReturnPartTransaction.getWorkOrderNumber() != null
						&& issueReturnPartTransaction.getActivityCode() != null) {
					issueReturnTransactionInfor.setACTIVITYID(new ACTIVITYID());
					issueReturnTransactionInfor.getACTIVITYID().setWORKORDERID(new WOID_Type());
					issueReturnTransactionInfor.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
					issueReturnTransactionInfor.getACTIVITYID().getWORKORDERID()
							.setJOBNUM(issueReturnPartTransaction.getWorkOrderNumber());

					issueReturnTransactionInfor.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
					issueReturnTransactionInfor.getACTIVITYID().getACTIVITYCODE()
							.setValue(tools.getDataTypeTools().encodeLong(issueReturnPartTransaction.getActivityCode(), "Activity Code"));
				}
				break;

			case PROJECT:
				// PROJECT - BUDGET
				if (issueReturnPartTransaction.getProjectCode() != null && issueReturnPartTransaction.getBudgetCode() != null) {
					issueReturnTransactionInfor.setPROJECTBUDGET(new PROJECTBUDGETID_Type());
					issueReturnTransactionInfor.getPROJECTBUDGET().setPROJECTID(new PROJECTID_Type());
					issueReturnTransactionInfor.getPROJECTBUDGET().getPROJECTID()
							.setPROJECTCODE(issueReturnPartTransaction.getProjectCode());
					issueReturnTransactionInfor.getPROJECTBUDGET().getPROJECTID().setORGANIZATIONID(tools.getOrganization(context));
					issueReturnTransactionInfor.getPROJECTBUDGET().getPROJECTID()
							.setDESCRIPTION(issueReturnPartTransaction.getProjectDesc());
					issueReturnTransactionInfor.getPROJECTBUDGET().setBUDGETID(new BUDGET_Type());
					issueReturnTransactionInfor.getPROJECTBUDGET().getBUDGETID()
							.setBUDGETCODE(issueReturnPartTransaction.getBudgetCode());
					issueReturnTransactionInfor.getPROJECTBUDGET().getBUDGETID()
							.setDESCRIPTION(issueReturnPartTransaction.getBudgetDesc());
				}
				break;

			case EQUIPMENT:
				// EQUIPMENT
				if (issueReturnPartTransaction.getEquipmentCode() != null
						&& !issueReturnPartTransaction.getEquipmentCode().trim().equals("")) {
					issueReturnTransactionInfor.setEQUIPMENTID(new EQUIPMENTID_Type());
					issueReturnTransactionInfor.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
					issueReturnTransactionInfor.getEQUIPMENTID()
							.setEQUIPMENTCODE(issueReturnPartTransaction.getEquipmentCode().toUpperCase());
				}
				break;

			case EMPLOYEE:
				// EMPLOYEE
				if (issueReturnPartTransaction.getEmployeeCode() != null
						&& !issueReturnPartTransaction.getEmployeeCode().trim().equals("")) {
					issueReturnTransactionInfor.setISSUETO(new PERSONID_Type());
					issueReturnTransactionInfor.getISSUETO()
							.setPERSONCODE(issueReturnPartTransaction.getEmployeeCode().toUpperCase());
					issueReturnTransactionInfor.getISSUETO().setDESCRIPTION(issueReturnPartTransaction.getEmployeeDesc());
				}
				break;

			default:
				break;

		}

		// PICK TICKET
		if (issueReturnPartTransaction.getPickTicketCode() != null
				&& !issueReturnPartTransaction.getPickTicketCode().trim().equals("")) {
			issueReturnTransactionInfor.setPICKLISTID(new PICKLIST_Type());
			issueReturnTransactionInfor.getPICKLISTID().setPICKLIST(issueReturnPartTransaction.getPickTicketCode());
		}

		// DEPARTMENT
		if (issueReturnPartTransaction.getDepartmentCode() != null) {
			issueReturnTransactionInfor.setDEPARTMENTID(new DEPARTMENTID_Type());
			issueReturnTransactionInfor.getDEPARTMENTID().setORGANIZATIONID(tools.getOrganization(context));
			issueReturnTransactionInfor.getDEPARTMENTID()
					.setDEPARTMENTCODE(issueReturnPartTransaction.getDepartmentCode().toUpperCase());
		}

		// STORE
		if (issueReturnPartTransaction.getStoreCode() != null) {
			issueReturnTransactionInfor.setSTOREID(new STOREID_Type());
			issueReturnTransactionInfor.getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
			issueReturnTransactionInfor.getSTOREID()
					.setSTORECODE(issueReturnPartTransaction.getStoreCode().toUpperCase());
		}
		// Date

		// TRANSACTION ID
		issueReturnTransactionInfor.setTRANSACTIONID(new TRANSACTIONID_Type());
		issueReturnTransactionInfor.getTRANSACTIONID().setORGANIZATIONID(tools.getOrganization(context));
		issueReturnTransactionInfor.getTRANSACTIONID().setTRANSACTIONCODE("0");

		//
		// TRANSACTION LINEs
		//
		issueReturnTransactionInfor.setIssueReturnTransactionLines(new IssueReturnTransactionLines());
		for (IssueReturnPartTransactionLine line : issueReturnPartTransaction.getTransactionlines()) {

			IssueReturnTransactionLine issueReturnTransactionLine = tools.getInforFieldTools().transformWSHubObject(new IssueReturnTransactionLine(), line, context);

			if (line.getPartOrg() != null) {
				issueReturnTransactionLine.getPARTID().getORGANIZATIONID().setORGANIZATIONCODE(line.getPartOrg());
			}

			if (issueReturnTransactionLine.getLOT() == null) {
				issueReturnTransactionLine.setLOT("*");
			}

			issueReturnTransactionLine.setTRANSACTIONLINEID(new TRANSACTIONLINEID());
			issueReturnTransactionLine.getTRANSACTIONLINEID().setTRANSACTIONID(new TRANSACTIONID_Type());
			issueReturnTransactionLine.getTRANSACTIONLINEID().getTRANSACTIONID().setTRANSACTIONCODE("0");

			issueReturnTransactionInfor.getIssueReturnTransactionLines().getIssueReturnTransactionLine()
					.add(issueReturnTransactionLine);

			if (isNotEmpty(line.getAssetIDCode())
					&& "ISSUE".equalsIgnoreCase(issueReturnPartTransaction.getTransactionType())
					&& IssueReturnPartTransactionType.WORKORDER.equals(issueReturnPartTransaction.getTransactionOn())) {
				issueReturnTransactionLine.setATTACHEQUIPMENT("true");
				issueReturnTransactionLine.setATTACHTOEQUIPMENT(new EQUIPMENTID_Type());
				WorkOrder workOrder = workOrderService.readWorkOrder(context, issueReturnPartTransaction.getWorkOrderNumber());
				issueReturnTransactionLine.getATTACHTOEQUIPMENT().setEQUIPMENTCODE(workOrder.getEquipmentCode());
				issueReturnTransactionLine.getATTACHTOEQUIPMENT().setORGANIZATIONID(tools.getOrganization(context));
			}
		}

		if (issueReturnPartTransaction.getRelatedWorkOrder() != null) {
			issueReturnTransactionInfor.setMULTIEQUIPSPLITINFOBATCH(new MULTIEQUIPSPLITINFOBATCH());
			WOID_Type woid_type = new WOID_Type();
			woid_type.setJOBNUM(issueReturnPartTransaction.getRelatedWorkOrder());
			issueReturnTransactionInfor.getMULTIEQUIPSPLITINFOBATCH().setRELATEDWORKORDERID(woid_type);
		}
		//
		//
		//
		MP0220_AddIssueReturnTransaction_001 addIssueReturnTransaction = new MP0220_AddIssueReturnTransaction_001();
		addIssueReturnTransaction.setIssueReturnTransaction(issueReturnTransactionInfor);

		MP0220_AddIssueReturnTransaction_001_Result result =
			tools.performInforOperation(context, inforws::addIssueReturnTransactionOp, addIssueReturnTransaction);

		String transactId = result.getResultData().getIssueReturnTransaction().getTRANSACTIONID().getTRANSACTIONCODE();

		// Manually update the Transaction UserDefined fields here through the db connection since Infor WS does not
		//support it - this was removed because it is not possible to do it on the interface

		return transactId;
	}

	public String createPartAssociation(InforContext context, PartAssociation partAssociation) throws InforException {

		MP0612_AddPartsAssociated_001 addpass = new MP0612_AddPartsAssociated_001();

		addpass.setPartsAssociated(new PartsAssociated());

		if (partAssociation.getPartCode() != null) {
			addpass.getPartsAssociated().setPARTID(new PARTID_Type());
			addpass.getPartsAssociated().getPARTID().setORGANIZATIONID(tools.getOrganization(context));
			addpass.getPartsAssociated().getPARTID().setPARTCODE(partAssociation.getPartCode().toUpperCase());
		}

		if (partAssociation.getQuantity() != null) {
			addpass.getPartsAssociated()
					.setPARTQUANTITY(tools.getDataTypeTools().encodeQuantity(partAssociation.getQuantity(), "Part Quantity"));
		}

		if (partAssociation.getUOM() != null) {
			addpass.getPartsAssociated().setUOMCODE(partAssociation.getUOM());
		}

		if (partAssociation.getAssociationEntity() != null) {
			addpass.getPartsAssociated().setENTITY(partAssociation.getAssociationEntity().toUpperCase());
		} else {
			addpass.getPartsAssociated().setENTITY("OBJ");
		}

		if (partAssociation.getEquipmentCode() != null) {
			addpass.getPartsAssociated().setPARTASSOCIATEDCODE(partAssociation.getEquipmentCode());
		}

		addpass.getPartsAssociated().setPARTASSOCIATEDID(new PARTASSOCIATEDID_Type());
		addpass.getPartsAssociated().getPARTASSOCIATEDID().setPARTASSOCIATEDPK("0");
		addpass.getPartsAssociated().setPARTASSOCIATEDTYPE("*");

		MP0612_AddPartsAssociated_001_Result result =
			tools.performInforOperation(context, inforws::addPartsAssociatedOp, addpass);

		return result.getResultData().getPartsAssociated().getPARTASSOCIATEDID().getPARTASSOCIATEDPK();
	}

	public String deletePartAssociation(InforContext context, PartAssociation partAssociation) throws InforException {
		MP0614_DeletePartsAssociated_001 deletepass = new MP0614_DeletePartsAssociated_001();
		deletepass.setPARTASSOCIATEDID(new PARTASSOCIATEDID_Type());
		deletepass.getPARTASSOCIATEDID().setParentcode(partAssociation.getEquipmentCode() + "#*");


		EntityManager em = tools.getEntityManager();
		try {
			deletepass.getPARTASSOCIATEDID().setPARTASSOCIATEDPK(
					em.createNamedQuery(PartAssociation.GET_PART_ASSOCIATION, PartAssociation.class)
							.setParameter("partCode", partAssociation.getPartCode())
							.setParameter("equipmentCode", partAssociation.getEquipmentCode() + "#*").getSingleResult()
							.getPk());
		} catch (Exception e) {
			throw tools.generateFault(
					"Couldn't fetch part association record for this equipment (" + e.getMessage() + ")");
		} finally {
			em.close();
		}

		tools.performInforOperation(context, inforws::deletePartsAssociatedOp, deletepass);
		return "OK";
	}

	public String createPartSubstitute(InforContext context, PartSubstitute partSubstitute) throws InforException {

		MP2051_AddSubstitutePart_001 addPartSub = new MP2051_AddSubstitutePart_001();
		addPartSub.setSubstitutePart(new SubstitutePart());

		addPartSub.getSubstitutePart().setSUBSTITUTEPARTID(new SUBSTITUTEPART_Type());

		if (partSubstitute.getPartA() != null) {
			addPartSub.getSubstitutePart().getSUBSTITUTEPARTID().setPARTAID(new PARTID_Type());
			addPartSub.getSubstitutePart().getSUBSTITUTEPARTID().getPARTAID()
					.setORGANIZATIONID(tools.getOrganization(context));
			addPartSub.getSubstitutePart().getSUBSTITUTEPARTID().getPARTAID()
					.setPARTCODE(partSubstitute.getPartA().toUpperCase().trim());
		}

		if (partSubstitute.getPartB() != null) {
			addPartSub.getSubstitutePart().getSUBSTITUTEPARTID().setPARTBID(new PARTID_Type());
			addPartSub.getSubstitutePart().getSUBSTITUTEPARTID().getPARTBID()
					.setORGANIZATIONID(tools.getOrganization(context));
			addPartSub.getSubstitutePart().getSUBSTITUTEPARTID().getPARTBID()
					.setPARTCODE(partSubstitute.getPartB().toUpperCase().trim());
		}

		if (partSubstitute.getCondition() != null) {
			addPartSub.getSubstitutePart().setPARTCONDITIONTEMPLATECONDITIONCODE(partSubstitute.getCondition());
		}

		if (partSubstitute.getFullyCompatible() != null) {
			addPartSub.getSubstitutePart().setFULLYCOMPATIBLE(partSubstitute.getFullyCompatible());
		}

		tools.performInforOperation(context, inforws::addSubstitutePartOp, addPartSub);

		return "OK";
	}

	public String addStoreBin(InforContext context, Bin binParam) throws InforException {
		StoreBin storeBin = tools.getInforFieldTools().transformWSHubObject(new StoreBin(), binParam, context);

		MP0281_AddStoreBin_001 addStoreBin = new MP0281_AddStoreBin_001();
		addStoreBin.setStoreBin(storeBin);

		tools.performInforOperation(context, inforws::addStoreBinOp, addStoreBin);
		return null;
	}

	@Override
	public Bin readStoreBin(InforContext context, Bin binParam) throws InforException {
		StoreBin storeBin = tools.getInforFieldTools().transformWSHubObject(new StoreBin(), binParam, context);

		MP0282_GetStoreBin_001 getStoreBin = new MP0282_GetStoreBin_001();
			getStoreBin.setSTOREBINID(storeBin.getSTOREBINID());

		MP0282_GetStoreBin_001_Result mp0282_getStoreBin_001_result
				= tools.performInforOperation(context, inforws::getStoreBinOp, getStoreBin);

		StoreBin result = mp0282_getStoreBin_001_result.getResultData().getStoreBin();

		return tools.getInforFieldTools().transformInforObject(new Bin(), result, context);
	}

	@Override
	public String updateStoreBin(InforContext context, Bin binParam) throws InforException {
		//Read bin
		StoreBin storeBin = tools.getInforFieldTools().transformWSHubObject(new StoreBin(), binParam, context);
		MP0282_GetStoreBin_001 getStoreBin = new MP0282_GetStoreBin_001();
		getStoreBin.setSTOREBINID(storeBin.getSTOREBINID());
		MP0282_GetStoreBin_001_Result mp0282_getStoreBin_001_result
				= tools.performInforOperation(context, inforws::getStoreBinOp, getStoreBin);
		StoreBin result = mp0282_getStoreBin_001_result.getResultData().getStoreBin();

		//Update storeBin
		StoreBin storeBin2
				= tools.getInforFieldTools().transformWSHubObject(result, binParam, context);

		MP0283_SyncStoreBin_001 syncStoreBin = new MP0283_SyncStoreBin_001();
		syncStoreBin.setStoreBin(storeBin2);

		MP0283_SyncStoreBin_001_Result syncResult
				= tools.performInforOperation(context, inforws::syncStoreBinOp, syncStoreBin);

		return null;
	}

	@Override
	public String deleteStoreBin(InforContext context, Bin binParam) throws InforException {
		StoreBin storeBin = tools.getInforFieldTools().transformWSHubObject(new StoreBin(), binParam, context);
		MP0284_DeleteStoreBin_001 deleteBinOp = new MP0284_DeleteStoreBin_001();
		deleteBinOp.setSTOREBINID(storeBin.getSTOREBINID());

		MP0284_DeleteStoreBin_001_Result deleteStoreBin_001_Result
				= tools.performInforOperation(context, inforws::deleteStoreBinOp, deleteBinOp);

		return null;
	}


	public PartManufacturer[] getPartManufacturers(InforContext context, String partCode) throws InforException {

		LinkedList<PartManufacturer> partManufacturers = new LinkedList<PartManufacturer>();
		String sqlQuery = "select r5partmfgs.mfp_part, r5partmfgs.mfp_manufacturer, r5manufacturers.mfg_desc, r5partmfgs.mfp_manufactpart, r5partmfgs.mfp_manufactdraw, r5partmfgs.mfp_primary, r5partmfgs.mfp_notused from r5partmfgs,r5manufacturers where mfp_manufacturer = mfg_code and mfp_part= '" + partCode + "'";
		Connection v_connection = null;
		Statement stmt = null;
		ResultSet v_result = null;
		try {
			v_connection = tools.getDataSource().getConnection();
			stmt = v_connection.createStatement();
			v_result = stmt.executeQuery(sqlQuery);

			while (v_result.next())
			{
				PartManufacturer partManufacturer = new PartManufacturer();
				partManufacturer.setPartCode(v_result.getString("mfp_part"));
				partManufacturer.setManufacturerCode(v_result.getString("mfp_manufacturer"));
				partManufacturer.setManufacturerDesc(v_result.getString("mfg_desc"));
				partManufacturer.setManufacturerPartNumber(v_result.getString("mfp_manufactpart"));
				partManufacturer.setDrawingNumber(v_result.getString("mfp_manufactdraw"));
				partManufacturer.setPrimary(decodeBoolean(v_result.getString("mfp_primary")));
				partManufacturer.setOutOfService(decodeBoolean(v_result.getString("mfp_notused")));
				partManufacturers.addLast(partManufacturer);
			}
		} catch (Exception e) {
			throw tools.generateFault("Couldn't read the manufacturers: " + e.getMessage());
		}
		finally {
			try {
				if(v_result != null) v_result.close();
				if(stmt != null) stmt.close();
				if(v_connection != null) v_connection.close();
			} catch (Exception e) {
				//tools.log(Level.FATAL, "Couldn't close the connection in readPartManufacturers");
				throw tools.generateFault("Couldn't read the manufacturers");
			}
		}

		return partManufacturers.toArray(new PartManufacturer[0]);


	}

	@Override
	public String createBin2binTransfer(InforContext context, Bin2BinTransfer bin2BinTransfer) throws InforException {
		net.datastream.schemas.mp_entities.bin2bintransfer_001.Bin2BinTransfer bin2BinTransferInfor =
				new net.datastream.schemas.mp_entities.bin2bintransfer_001.Bin2BinTransfer();

		BinStock binStockInfor = new BinStock();
		bin2BinTransferInfor.setBinStock(binStockInfor);
		bin2BinTransferInfor.setBIN(bin2BinTransfer.getDestinationBin());

		binStockInfor.setSTOREID(new STOREID_Type());
		binStockInfor.getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
		binStockInfor.getSTOREID().setSTORECODE(bin2BinTransfer.getStoreCode());

		IssueReturnPartTransactionLine transactionLine = bin2BinTransfer.getTransactionLine();
		if (transactionLine != null) {
			binStockInfor.setPARTID(new PARTID_Type());
			binStockInfor.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
			binStockInfor.getPARTID().setPARTCODE(treatCodeSafe(transactionLine.getPartCode()));

			bin2BinTransferInfor.setSTOCKQTY(
					DataTypeTools.encodeQuantity(transactionLine.getTransactionQty(), "Stock Quantity")
			);

			binStockInfor.setBIN(transactionLine.getBin());
			binStockInfor.setLOT(transactionLine.getLot());

			if (isNotEmpty(transactionLine.getAssetIDCode())) {
				bin2BinTransferInfor.setASSETID(new EQUIPMENTID_Type());
				bin2BinTransferInfor.getASSETID().setEQUIPMENTCODE(transactionLine.getAssetIDCode());
				bin2BinTransferInfor.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
			}

			IssueReturnTransactionLine lineInfor = tools.getInforFieldTools().transformWSHubObject(new IssueReturnTransactionLine(), transactionLine, context);
			bin2BinTransferInfor.getBinStock().setStandardUserDefinedFields(lineInfor.getStandardUserDefinedFields());
		}

		MP0286_Bin2BinTransfer_001 opBean = new MP0286_Bin2BinTransfer_001();
		opBean.setBin2BinTransfer(bin2BinTransferInfor);
		tools.performInforOperation(context, inforws::bin2BinTransferOp, opBean);

		return null;
	}

	public BatchResponse<String> createBin2binTransferBatch(InforContext context, List<Bin2BinTransfer> bin2BinTransferList) {
		return tools.batchOperation(context, this::createBin2binTransfer, bin2BinTransferList);
	}

	private String treatCodeSafe(String code) {
		if(code == null) return null;
		return code.trim().toUpperCase();
	}
}
