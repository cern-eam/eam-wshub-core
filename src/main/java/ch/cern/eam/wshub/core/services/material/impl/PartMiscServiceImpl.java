package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.entities.IssueReturnPartTransaction;
import ch.cern.eam.wshub.core.services.material.entities.IssueReturnPartTransactionLine;
import ch.cern.eam.wshub.core.services.material.PartMiscService;
import ch.cern.eam.wshub.core.services.material.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.catalogue_001.Catalogue;
import net.datastream.schemas.mp_entities.issuereturntransaction_001.IssueReturnTransaction;
import net.datastream.schemas.mp_entities.issuereturntransactionline_001.IssueReturnTransactionLine;
import net.datastream.schemas.mp_entities.issuereturntransactionline_001.IssueReturnTransactionLines;
import net.datastream.schemas.mp_entities.partsassociated_001.PartsAssociated;
import net.datastream.schemas.mp_entities.storebin_001.StoreBin;
import net.datastream.schemas.mp_entities.substitutepart_001.SubstitutePart;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp0220_001.MP0220_AddIssueReturnTransaction_001;
import net.datastream.schemas.mp_functions.mp0271_001.MP0271_AddCatalogue_001;
import net.datastream.schemas.mp_functions.mp0281_001.MP0281_AddStoreBin_001;
import net.datastream.schemas.mp_functions.mp0612_001.MP0612_AddPartsAssociated_001;
import net.datastream.schemas.mp_functions.mp0614_001.MP0614_DeletePartsAssociated_001;
import net.datastream.schemas.mp_functions.mp2051_001.MP2051_AddSubstitutePart_001;
import net.datastream.schemas.mp_results.mp0220_001.MP0220_AddIssueReturnTransaction_001_Result;
import net.datastream.schemas.mp_results.mp0612_001.MP0612_AddPartsAssociated_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.persistence.EntityManager;
import javax.xml.ws.Holder;
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

	public PartMiscServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
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

		if (context.getCredentials() != null) {
			inforws.addCatalogueOp(addCatalogue, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.addCatalogueOp(addCatalogue, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

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
			IssueReturnTransactionLine issueReturnTransactionLine = new IssueReturnTransactionLine();
			// PART
			if (line.getPartCode() != null) {
				issueReturnTransactionLine.setPARTID(new PARTID_Type());
				issueReturnTransactionLine.getPARTID().setORGANIZATIONID(tools.getOrganization(context));
				issueReturnTransactionLine.getPARTID().setPARTCODE(line.getPartCode().toUpperCase());
			}

			// BIN
			if (line.getBin() != null) {
				issueReturnTransactionLine.setBIN(line.getBin().toUpperCase());
			}

			// LOT
			if (line.getLot() != null) {
				issueReturnTransactionLine.setLOT(line.getLot().toUpperCase());
			} else {
				issueReturnTransactionLine.setLOT("*");
			}

			// TRANSACTION QTY
			if (line.getTransactionQty() != null) {
				issueReturnTransactionLine
						.setTRANSACTIONQUANTITY(tools.getDataTypeTools().encodeAmount(line.getTransactionQty(), "Transaction Qty."));
			}

			// ASSET ID
			if (line.getAssetIDCode() != null && !"".equals(line.getAssetIDCode().trim())) {
				issueReturnTransactionLine.setEQUIPMENTID(new EQUIPMENTID_Type());
				issueReturnTransactionLine.getEQUIPMENTID().setORGANIZATIONID(tools.getOrganization(context));
				issueReturnTransactionLine.getEQUIPMENTID().setEQUIPMENTCODE(line.getAssetIDCode());
			}

			//
			issueReturnTransactionLine.setTRANSACTIONLINEID(new TRANSACTIONLINEID());
			issueReturnTransactionLine.getTRANSACTIONLINEID().setTRANSACTIONID(new TRANSACTIONID_Type());
			issueReturnTransactionLine.getTRANSACTIONLINEID().getTRANSACTIONID().setTRANSACTIONCODE("0");

			issueReturnTransactionInfor.getIssueReturnTransactionLines().getIssueReturnTransactionLine()
					.add(issueReturnTransactionLine);
		}
		//
		//
		//
		MP0220_AddIssueReturnTransaction_001_Result result = null;
		MP0220_AddIssueReturnTransaction_001 addIssueReturnTransaction = new MP0220_AddIssueReturnTransaction_001();
		addIssueReturnTransaction.setIssueReturnTransaction(issueReturnTransactionInfor);

		if (context.getCredentials() != null) {
			result = inforws.addIssueReturnTransactionOp(addIssueReturnTransaction, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
		} else {
			result = inforws.addIssueReturnTransactionOp(addIssueReturnTransaction, tools.getOrganizationCode(context),  null, null, new Holder<SessionType>(tools.createInforSession(context)), tools.createMessageConfig(), tools.getTenant(context));
		}

		return result.getResultData().getIssueReturnTransaction().getTRANSACTIONID().getTRANSACTIONCODE();
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
			addpass.getPartsAssociated().setPARTASSOCIATEDCODE(partAssociation.getEquipmentCode() + "#*");
		}

		addpass.getPartsAssociated().setPARTASSOCIATEDID(new PARTASSOCIATEDID_Type());
		addpass.getPartsAssociated().getPARTASSOCIATEDID().setPARTASSOCIATEDPK("0");
		addpass.getPartsAssociated().setPARTASSOCIATEDTYPE("*");

		MP0612_AddPartsAssociated_001_Result result;

		// first get it:
		if (context.getCredentials() != null) {
			result = inforws.addPartsAssociatedOp(addpass, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			result = inforws.addPartsAssociatedOp(addpass, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

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

		if (context.getCredentials() != null) {
			inforws.deletePartsAssociatedOp(deletepass, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.deletePartsAssociatedOp(deletepass, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
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

		if (context.getCredentials() != null) {
			inforws.addSubstitutePartOp(addPartSub, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.addSubstitutePartOp(addPartSub, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}

		return "OK";
	}

	public String addStoreBin(InforContext context, Bin binParam) throws InforException {

		StoreBin bin = new StoreBin();
		bin.setSTOREBINID(new STOREBINID());

		if (binParam.getBinCode() != null && !binParam.getBinCode().trim().equals("") && binParam.getBinDesc() != null
				&& !binParam.getBinDesc().trim().equals("")) {
			bin.getSTOREBINID().setBINID(new BINID_Type());
			bin.getSTOREBINID().getBINID().setBIN(binParam.getBinCode().toUpperCase().trim());
			bin.getSTOREBINID().getBINID().setDESCRIPTION(binParam.getBinDesc().toUpperCase().trim());
		}

		if (binParam.getStoreCode() != null && !binParam.getStoreCode().trim().equals("")) {
			bin.getSTOREBINID().setSTOREID(new STOREID_Type());
			bin.getSTOREBINID().getSTOREID().setORGANIZATIONID(tools.getOrganization(context));
			bin.getSTOREBINID().getSTOREID().setSTORECODE(binParam.getStoreCode().toUpperCase().trim());
		}

		bin.setOUTOFSERVICE(binParam.getOutOfService());

		MP0281_AddStoreBin_001 storeBin = new MP0281_AddStoreBin_001();
		storeBin.setStoreBin(bin);

		if (context.getCredentials() != null) {
			inforws.addStoreBinOp(storeBin, tools.getOrganizationCode(context),
					tools.createSecurityHeader(context), "TERMINATE", null,
					null, tools.getTenant(context));
		} else {
			inforws.addStoreBinOp(storeBin, tools.getOrganizationCode(context), null, null,
					new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
		}
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
				partManufacturer.setPrimary(v_result.getString("mfp_primary"));
				if (partManufacturer.getPrimary().equals("+")) {
					partManufacturer.setPrimary("TRUE");
				} else {
					partManufacturer.setPrimary("FALSE");
				}
				partManufacturer.setOutOfService(v_result.getString("mfp_notused"));
				if (partManufacturer.getOutOfService().equals("+")) {
					partManufacturer.setOutOfService("TRUE");
				} else {
					partManufacturer.setOutOfService("FALSE");
				}
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

}
