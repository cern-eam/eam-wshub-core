package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
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
import ch.cern.eam.wshub.core.tools.EAMException;
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
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.decodeBoolean;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;

import jakarta.persistence.EntityManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PartMiscServiceImpl implements PartMiscService {

	private Tools tools;
	private EAMWebServicesPT eamws;
	private ApplicationData applicationData;

	private WorkOrderService workOrderService;

	public PartMiscServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.eamws = eamWebServicesToolkitClient;
		this.workOrderService = new WorkOrderServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
	}

	public String addPartSupplier(EAMContext context, PartSupplier partSupplierParam) throws EAMException {

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

		tools.performEAMOperation(context, eamws::addCatalogueOp, addCatalogue);

		return null;
	}

	public List<String> createIssueReturnTransaction(EAMContext context, List<IssueReturnPartTransaction> issueReturnPartTransactionList) throws EAMException {
		List<String> results = new ArrayList<String>();
		for (IssueReturnPartTransaction tr : issueReturnPartTransactionList) {
			results.add(createIssueReturnTransaction(context, tr));
		}
		return results;
	}

	public String createIssueReturnTransaction(EAMContext context, IssueReturnPartTransaction issueReturnPartTransaction) throws EAMException {
		IssueReturnTransaction issueReturnTransactionEAM = new IssueReturnTransaction();

		//
		// TRANSACTION TYPE
		//
		if (issueReturnPartTransaction.getTransactionType() != null) {
			if (issueReturnPartTransaction.getTransactionType().toUpperCase().startsWith("I")) {
				issueReturnTransactionEAM.setISSUERETURNTYPE("ISSUE");
			} else {
				issueReturnTransactionEAM.setISSUERETURNTYPE("RETURN");
			}
		}

		switch (issueReturnPartTransaction.getTransactionOn()) {
			case WORKORDER:
				// WORK ORDER - ACTIVITY
				if (issueReturnPartTransaction.getWorkOrderNumber() != null
						&& issueReturnPartTransaction.getActivityCode() != null) {
					issueReturnTransactionEAM.setACTIVITYID(new ACTIVITYID());
					issueReturnTransactionEAM.getACTIVITYID().setWORKORDERID(new WOID_Type());
					issueReturnTransactionEAM.getACTIVITYID().getWORKORDERID().setORGANIZATIONID(tools.getOrganization(context));
					issueReturnTransactionEAM.getACTIVITYID().getWORKORDERID()
							.setJOBNUM(issueReturnPartTransaction.getWorkOrderNumber());

					issueReturnTransactionEAM.getACTIVITYID().setACTIVITYCODE(new ACTIVITYCODE());
					issueReturnTransactionEAM.getACTIVITYID().getACTIVITYCODE()
							.setValue(tools.getDataTypeTools().encodeLong(issueReturnPartTransaction.getActivityCode(), "Activity Code"));
				}
				break;

			case PROJECT:
				// PROJECT - BUDGET
				if (issueReturnPartTransaction.getProjectCode() != null && issueReturnPartTransaction.getBudgetCode() != null) {
					issueReturnTransactionEAM.setPROJECTBUDGET(new PROJECTBUDGETID_Type());
					issueReturnTransactionEAM.getPROJECTBUDGET().setPROJECTID(new PROJECTID_Type());
					issueReturnTransactionEAM.getPROJECTBUDGET().getPROJECTID()
							.setPROJECTCODE(issueReturnPartTransaction.getProjectCode());
					issueReturnTransactionEAM.getPROJECTBUDGET().getPROJECTID().setORGANIZATIONID(tools.getOrganization(context));
					issueReturnTransactionEAM.getPROJECTBUDGET().getPROJECTID()
							.setDESCRIPTION(issueReturnPartTransaction.getProjectDesc());
					issueReturnTransactionEAM.getPROJECTBUDGET().setBUDGETID(new BUDGET_Type());
					issueReturnTransactionEAM.getPROJECTBUDGET().getBUDGETID()
							.setBUDGETCODE(issueReturnPartTransaction.getBudgetCode());
					issueReturnTransactionEAM.getPROJECTBUDGET().getBUDGETID()
							.setDESCRIPTION(issueReturnPartTransaction.getBudgetDesc());
				}
				break;

			case EQUIPMENT:
				// EQUIPMENT
				if (issueReturnPartTransaction.getEquipmentCode() != null
						&& !issueReturnPartTransaction.getEquipmentCode().trim().equals("")) {
					issueReturnTransactionEAM.setEQUIPMENTID(new EQUIPMENTID_Type());
					issueReturnTransactionEAM.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
					issueReturnTransactionEAM.getEQUIPMENTID()
							.setEQUIPMENTCODE(issueReturnPartTransaction.getEquipmentCode().toUpperCase());
				}
				break;

			case EMPLOYEE:
				// EMPLOYEE
				if (issueReturnPartTransaction.getEmployeeCode() != null
						&& !issueReturnPartTransaction.getEmployeeCode().trim().equals("")) {
					issueReturnTransactionEAM.setISSUETO(new PERSONID_Type());
					issueReturnTransactionEAM.getISSUETO()
							.setPERSONCODE(issueReturnPartTransaction.getEmployeeCode().toUpperCase());
					issueReturnTransactionEAM.getISSUETO().setDESCRIPTION(issueReturnPartTransaction.getEmployeeDesc());
				}
				break;

			default:
				break;

		}

		// PICK TICKET
		if (issueReturnPartTransaction.getPickTicketCode() != null
				&& !issueReturnPartTransaction.getPickTicketCode().trim().equals("")) {
			issueReturnTransactionEAM.setPICKLISTID(new PICKLIST_Type());
			issueReturnTransactionEAM.getPICKLISTID().setPICKLIST(issueReturnPartTransaction.getPickTicketCode());
		}

		// DEPARTMENT
		if (issueReturnPartTransaction.getDepartmentCode() != null) {
			issueReturnTransactionEAM.setDEPARTMENTID(new DEPARTMENTID_Type());
			issueReturnTransactionEAM.getDEPARTMENTID().setORGANIZATIONID(tools.getOrganization(context));
			issueReturnTransactionEAM.getDEPARTMENTID()
					.setDEPARTMENTCODE(issueReturnPartTransaction.getDepartmentCode().toUpperCase());
		}

		// STORE
		if (issueReturnPartTransaction.getStoreCode() != null) {
			issueReturnTransactionEAM.setSTOREID(new STOREID_Type());
			issueReturnTransactionEAM.getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
			issueReturnTransactionEAM.getSTOREID()
					.setSTORECODE(issueReturnPartTransaction.getStoreCode().toUpperCase());
		}
		// Date

		// TRANSACTION ID
		issueReturnTransactionEAM.setTRANSACTIONID(new TRANSACTIONID_Type());
		issueReturnTransactionEAM.getTRANSACTIONID().setORGANIZATIONID(tools.getOrganization(context));
		issueReturnTransactionEAM.getTRANSACTIONID().setTRANSACTIONCODE("0");

		//
		// TRANSACTION LINEs
		//
		issueReturnTransactionEAM.setIssueReturnTransactionLines(new IssueReturnTransactionLines());
		for (IssueReturnPartTransactionLine line : issueReturnPartTransaction.getTransactionlines()) {

			IssueReturnTransactionLine issueReturnTransactionLine = tools.getEAMFieldTools().transformWSHubObject(new IssueReturnTransactionLine(), line, context);

			if (issueReturnTransactionLine.getLOT() == null) {
				issueReturnTransactionLine.setLOT("*");
			}

			issueReturnTransactionLine.setTRANSACTIONLINEID(new TRANSACTIONLINEID());
			issueReturnTransactionLine.getTRANSACTIONLINEID().setTRANSACTIONID(new TRANSACTIONID_Type());
			issueReturnTransactionLine.getTRANSACTIONLINEID().getTRANSACTIONID().setTRANSACTIONCODE("0");

			issueReturnTransactionEAM.getIssueReturnTransactionLines().getIssueReturnTransactionLine()
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
			issueReturnTransactionEAM.setMULTIEQUIPSPLITINFOBATCH(new MULTIEQUIPSPLITINFOBATCH());
			WOID_Type woid_type = new WOID_Type();
			woid_type.setJOBNUM(issueReturnPartTransaction.getRelatedWorkOrder());
			issueReturnTransactionEAM.getMULTIEQUIPSPLITINFOBATCH().setRELATEDWORKORDERID(woid_type);
		}
		//
		//
		//
		MP0220_AddIssueReturnTransaction_001 addIssueReturnTransaction = new MP0220_AddIssueReturnTransaction_001();
		addIssueReturnTransaction.setIssueReturnTransaction(issueReturnTransactionEAM);

		MP0220_AddIssueReturnTransaction_001_Result result =
			tools.performEAMOperation(context, eamws::addIssueReturnTransactionOp, addIssueReturnTransaction);

		String transactId = result.getResultData().getIssueReturnTransaction().getTRANSACTIONID().getTRANSACTIONCODE();

		// Manually update the Transaction UserDefined fields here through the db connection since EAM WS does not
		//support it - this was removed because it is not possible to do it on the interface

		return transactId;
	}

	public String createPartAssociation(EAMContext context, PartAssociation partAssociation) throws EAMException {

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
			addpass.getPartsAssociated().setPARTASSOCIATEDCODE(partAssociation.getEquipmentCode() + "#*");
		}

		addpass.getPartsAssociated().setPARTASSOCIATEDID(new PARTASSOCIATEDID_Type());
		addpass.getPartsAssociated().getPARTASSOCIATEDID().setPARTASSOCIATEDPK("0");
		addpass.getPartsAssociated().setPARTASSOCIATEDTYPE("*");

		MP0612_AddPartsAssociated_001_Result result =
			tools.performEAMOperation(context, eamws::addPartsAssociatedOp, addpass);

		return result.getResultData().getPartsAssociated().getPARTASSOCIATEDID().getPARTASSOCIATEDPK();
	}

	public String deletePartAssociation(EAMContext context, PartAssociation partAssociation) throws EAMException {
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

		tools.performEAMOperation(context, eamws::deletePartsAssociatedOp, deletepass);
		return "OK";
	}

	public String createPartSubstitute(EAMContext context, PartSubstitute partSubstitute) throws EAMException {

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

		tools.performEAMOperation(context, eamws::addSubstitutePartOp, addPartSub);

		return "OK";
	}

	public String addStoreBin(EAMContext context, Bin binParam) throws EAMException {
		StoreBin storeBin = tools.getEAMFieldTools().transformWSHubObject(new StoreBin(), binParam, context);

		MP0281_AddStoreBin_001 addStoreBin = new MP0281_AddStoreBin_001();
		addStoreBin.setStoreBin(storeBin);

		tools.performEAMOperation(context, eamws::addStoreBinOp, addStoreBin);
		return null;
	}

	@Override
	public Bin readStoreBin(EAMContext context, Bin binParam) throws EAMException {
		StoreBin storeBin = tools.getEAMFieldTools().transformWSHubObject(new StoreBin(), binParam, context);

		MP0282_GetStoreBin_001 getStoreBin = new MP0282_GetStoreBin_001();
			getStoreBin.setSTOREBINID(storeBin.getSTOREBINID());

		MP0282_GetStoreBin_001_Result mp0282_getStoreBin_001_result
				= tools.performEAMOperation(context, eamws::getStoreBinOp, getStoreBin);

		StoreBin result = mp0282_getStoreBin_001_result.getResultData().getStoreBin();

		return tools.getEAMFieldTools().transformEAMObject(new Bin(), result, context);
	}

	@Override
	public String updateStoreBin(EAMContext context, Bin binParam) throws EAMException {
		//Read bin
		StoreBin storeBin = tools.getEAMFieldTools().transformWSHubObject(new StoreBin(), binParam, context);
		MP0282_GetStoreBin_001 getStoreBin = new MP0282_GetStoreBin_001();
		getStoreBin.setSTOREBINID(storeBin.getSTOREBINID());
		MP0282_GetStoreBin_001_Result mp0282_getStoreBin_001_result
				= tools.performEAMOperation(context, eamws::getStoreBinOp, getStoreBin);
		StoreBin result = mp0282_getStoreBin_001_result.getResultData().getStoreBin();

		//Update storeBin
		StoreBin storeBin2
				= tools.getEAMFieldTools().transformWSHubObject(result, binParam, context);

		MP0283_SyncStoreBin_001 syncStoreBin = new MP0283_SyncStoreBin_001();
		syncStoreBin.setStoreBin(storeBin2);

		MP0283_SyncStoreBin_001_Result syncResult
				= tools.performEAMOperation(context, eamws::syncStoreBinOp, syncStoreBin);

		return null;
	}

	@Override
	public String deleteStoreBin(EAMContext context, Bin binParam) throws EAMException {
		StoreBin storeBin = tools.getEAMFieldTools().transformWSHubObject(new StoreBin(), binParam, context);
		MP0284_DeleteStoreBin_001 deleteBinOp = new MP0284_DeleteStoreBin_001();
		deleteBinOp.setSTOREBINID(storeBin.getSTOREBINID());

		MP0284_DeleteStoreBin_001_Result deleteStoreBin_001_Result
				= tools.performEAMOperation(context, eamws::deleteStoreBinOp, deleteBinOp);

		return null;
	}


	public PartManufacturer[] getPartManufacturers(EAMContext context, String partCode) throws EAMException {

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
	public String createBin2binTransfer(EAMContext context, Bin2BinTransfer bin2BinTransfer) throws EAMException {
		net.datastream.schemas.mp_entities.bin2bintransfer_001.Bin2BinTransfer bin2BinTransferEAM =
				new net.datastream.schemas.mp_entities.bin2bintransfer_001.Bin2BinTransfer();

		BinStock binStockEAM = new BinStock();
		bin2BinTransferEAM.setBinStock(binStockEAM);
		bin2BinTransferEAM.setBIN(bin2BinTransfer.getDestinationBin());

		binStockEAM.setSTOREID(new STOREID_Type());
		binStockEAM.getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
		binStockEAM.getSTOREID().setSTORECODE(bin2BinTransfer.getStoreCode());

		IssueReturnPartTransactionLine transactionLine = bin2BinTransfer.getTransactionLine();
		if (transactionLine != null) {
			binStockEAM.setPARTID(new PARTID_Type());
			binStockEAM.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
			binStockEAM.getPARTID().setPARTCODE(treatCodeSafe(transactionLine.getPartCode()));

			bin2BinTransferEAM.setSTOCKQTY(
					DataTypeTools.encodeQuantity(transactionLine.getTransactionQty(), "Stock Quantity")
			);

			binStockEAM.setBIN(transactionLine.getBin());
			binStockEAM.setLOT(transactionLine.getLot());

			if (isNotEmpty(transactionLine.getAssetIDCode())) {
				bin2BinTransferEAM.setASSETID(new EQUIPMENTID_Type());
				bin2BinTransferEAM.getASSETID().setEQUIPMENTCODE(transactionLine.getAssetIDCode());
				bin2BinTransferEAM.getASSETID().setORGANIZATIONID(tools.getOrganization(context));
			}

			IssueReturnTransactionLine lineEAM = tools.getEAMFieldTools().transformWSHubObject(new IssueReturnTransactionLine(), transactionLine, context);
			bin2BinTransferEAM.getBinStock().setStandardUserDefinedFields(lineEAM.getStandardUserDefinedFields());
		}

		MP0286_Bin2BinTransfer_001 opBean = new MP0286_Bin2BinTransfer_001();
		opBean.setBin2BinTransfer(bin2BinTransferEAM);
		tools.performEAMOperation(context, eamws::bin2BinTransferOp, opBean);

		return null;
	}

	public BatchResponse<String> createBin2binTransferBatch(EAMContext context, List<Bin2BinTransfer> bin2BinTransferList) {
		return tools.batchOperation(context, this::createBin2binTransfer, bin2BinTransferList);
	}

	private String treatCodeSafe(String code) {
		if(code == null) return null;
		return code.trim().toUpperCase();
	}
}
