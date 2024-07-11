package ch.cern.eam.wshub.core.tools;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import jakarta.persistence.EntityManager;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestFilter;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import static ch.cern.eam.wshub.core.tools.GridTools.extractSingleResult;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isEmpty;

/**
 * Fetch the description of some fields that are missing when reading from
 * web services
 *
 */
public class FieldDescriptionTools {

	private Tools tools;
	private ApplicationData applicationData;
	private EAMWebServicesPT eamws;
	private GridsService gridsService;

	public FieldDescriptionTools(Tools tools, ApplicationData applicationData, EAMWebServicesPT eamws) {
		this.tools = tools;
		this.applicationData = applicationData;
		this.eamws = eamws;
		gridsService = new GridsServiceImpl(applicationData, tools, eamws);

	}

	private String getDescription(EAMContext context, GridRequest gridRequest, String descriptionKey) {
		try {
			return extractSingleResult(gridsService.executeQuery(context, gridRequest), descriptionKey);
		} catch (EAMException eamException ) {
			tools.log(Level.WARNING, "Couldn't fetch description for " + descriptionKey);
			return null;
		}
	}

	public String readPersonDesc(EAMContext context, String personCode)  {
		if (isEmpty(personCode)) {
			return null;
		}
		GridRequest gridRequest = new GridRequest("LVPERS", GridRequest.GRIDTYPE.LOV, 1);
		gridRequest.addFilter("personcode", personCode, "=");
		gridRequest.addParam("parameter.per_type", "");
		gridRequest.addParam("param.bypassdeptsecurity", "false");
		gridRequest.addParam("param.sessionid", "");
		gridRequest.addParam("parameter.noemployees", "");
		gridRequest.addParam("param.shift", "");
		return getDescription(context, gridRequest, "description");
	}

	public String readDepartmentDesc(EAMContext context, String departmentCode)  {
		if (isEmpty(departmentCode)) {
			return null;
		}
		GridRequest gridRequest = new GridRequest("LVMRCS", GridRequest.GRIDTYPE.LOV, 1);
		gridRequest.addFilter("department", departmentCode, "=");
		gridRequest.addParam("param.showstardepartment", "true");
		gridRequest.addParam("param.bypassdeptsecurity", "false");
		return getDescription(context, gridRequest, "des_text");
	}

	public String readClassDesc(EAMContext context, String entity, String classCode) {
		if (isEmpty(classCode)) {
			return null;
		}
		GridRequest gridRequest = new GridRequest("LVCLAS", GridRequest.GRIDTYPE.LOV, 1);
		gridRequest.addFilter("class", classCode, "=");
		gridRequest.addParam("parameter.rentity", entity);
		gridRequest.addParam("parameter.r5role", "");
		gridRequest.addParam("parameter.bypassorg", "");
		return getDescription(context, gridRequest, "des_text");
	}

	public String readUOMDesc(EAMContext eamContext, String uomCode) {
		if (isEmpty(uomCode)) {
			return null;
		}
		GridRequest gridRequest = new GridRequest("LVUOMS", GridRequest.GRIDTYPE.LOV, 1);
		gridRequest.addFilter("uomcode", uomCode, "=");
		gridRequest.addParam("param.aspect", "");
		return getDescription(eamContext, gridRequest, "description");
	}

	public String readCategoryDesc(EAMContext eamContext, String categoryCode) {
		if (isEmpty(categoryCode)) {
			return null;
		}
		GridRequest gridRequest = new GridRequest("LVPARTCAT", GridRequest.GRIDTYPE.LOV, 1);
		gridRequest.addFilter("category", categoryCode, "=");
		return getDescription(eamContext, gridRequest, "description");
	}

	public String readCommodityDesc(EAMContext eamContext, String commodityCode) {
		if (isEmpty(commodityCode)) {
			return null;
		}
		GridRequest gridRequest = new GridRequest("LVCOMM", GridRequest.GRIDTYPE.LOV, 1);
		gridRequest.addFilter("commoditycode", commodityCode, "=");
		return getDescription(eamContext, gridRequest, "des_text");
	}

	public String readManufacturerDesc(EAMContext eamContext, String manufacturerCode) {
		if (isEmpty(manufacturerCode)) {
			return null;
		}
		GridRequest gridRequest = new GridRequest("LVMANU", GridRequest.GRIDTYPE.LOV, 1);
		gridRequest.addFilter("manufacturercode", manufacturerCode, "=");
		return getDescription(eamContext, gridRequest, "des_text");
	}

	public String readBinDesc(EAMContext eamContext, String storeCode, String binCode) {
		if (isEmpty(binCode) || isEmpty(storeCode)) {
			return null;
		}
		GridRequest gridRequest = new GridRequest("LVBINALL", GridRequest.GRIDTYPE.LOV, 1);
		gridRequest.addFilter("code", binCode, "=", GridRequestFilter.JOINER.AND);
		gridRequest.addFilter("bis_store", storeCode, "=");
		return getDescription(eamContext, gridRequest, "description");
	}

	public String readCostCodeDesc(EAMContext eamContext, String costCode) {
		if (isEmpty(costCode)) {
			return null;
		}
		GridRequest gridRequest = new GridRequest("LVCSTC", GridRequest.GRIDTYPE.LOV, 1);
		gridRequest.addFilter("costcode", costCode, "=");
		return getDescription(eamContext, gridRequest, "des_text");
	}

	public String readUserCodeDesc(EAMContext eamContext, String entity, String userCode) {
		if (isEmpty(userCode)) return null;
		GridRequest gridRequest = new GridRequest("BSUCOD_HDR", GridRequest.GRIDTYPE.LOV, 1);
		gridRequest.addParam("param.entitycode", entity);
		gridRequest.setUserFunctionName("BSUCOD");
		gridRequest.addFilter("usercode", userCode, "EQUALS");
		return getDescription(eamContext, gridRequest, "usercodedescription");
	}

	public String readSystemCodeForUserCode(EAMContext eamContext, String entity, String userCode) {
		if (isEmpty(userCode)) return null;
		GridRequest gridRequest = new GridRequest("BSUCOD_HDR", GridRequest.GRIDTYPE.LOV, 1);
		gridRequest.addParam("param.entitycode", entity);
		gridRequest.setUserFunctionName("BSUCOD");
		gridRequest.addFilter("usercode", userCode, "EQUALS");
		return getDescription(eamContext, gridRequest, "systemcode");
	}

	/**
	 * Reads the description of a custom field value
	 * 
	 * @param entityCode
	 *            The entity
	 * @param codeValue
	 *            The code value
	 * @return The desc value for the custom field
	 * 
	 */
	public String readCustomFieldDesc(String entityCode, String codeValue) {
		if (!tools.isDatabaseConnectionConfigured()) {
			return null;
		}
		if (tools.getDataTypeTools().isEmpty(codeValue)) {
			return null;
		}
		EntityManager em = tools.getEntityManager();
		try {
			if (entityCode != null && codeValue != null)
				return em.createNativeQuery(
						"SELECT DES_TEXT FROM R5DESCRIPTIONS WHERE DES_RENTITY = :entityCode AND DES_CODE = :codeValue")
						.setParameter("entityCode", entityCode).setParameter("codeValue", codeValue).getSingleResult()
						.toString();
		} catch (Exception e) {
			tools.log(Level.SEVERE,"Error in readCustomFieldDesc for entityCode " + entityCode + " and codeValue " + codeValue );
		} finally {
			em.close();
		}
		return null;
	}

	/**
	 * Method to complete the descriptions of the user defined fields of type RENT
	 * 
	 * @param udfs
	 *            user defined fields that are going to be populated
	 * @param entity
	 *            entity that is being populated
	 */
	@SuppressWarnings("unchecked")
	public void readUDFRENTDescriptions(UserDefinedFields udfs, String entity) {
		if (!tools.isDatabaseConnectionConfigured()) {
			return;
		}
		EntityManager em = tools.getEntityManager();
		try {
			// First read all the possible RENT fields for the entity
			List<Object[]> rentFields = (List<Object[]>) em
					.createNativeQuery("SELECT UDF_FIELD, UDF_LOOKUPRENTITY FROM R5USERDEFINEDFIELDSETUP WHERE"
							+ " UDF_RENTITY = :entity AND UDF_LOOKUPTYPE = 'RENT'  and UDF_FIELD LIKE 'udfchar%'")
					.setParameter("entity", entity).getResultList();

			// Iterate over the fields to get the description
			for (Object[] fieldInfo : rentFields) {
				// Information about the field
				String field = fieldInfo[0].toString();
				String rentity = fieldInfo[1].toString();

				// Now, try to access the value with reflection
				Field reflexField = udfs.getClass().getDeclaredField(field);
				// The field should be accessible
				reflexField.setAccessible(true);
				// Get the real value
				Object realValue = reflexField.get(udfs);
				// If there is a value, then we have to get the description
				if (realValue != null && !realValue.toString().equals("")) {
					// Get the description
					String valueDesc = null;
					try {
						valueDesc = em.createNativeQuery(
								"SELECT DES_TEXT FROM R5DESCRIPTIONS WHERE DES_RENTITY = :entityCode AND DES_CODE = :codeValue")
								.setParameter("entityCode", rentity).setParameter("codeValue", realValue.toString())
								.getSingleResult().toString();
					} catch (Exception e) {

					}
					// Check if the description is there to set it
					if (valueDesc != null) {
						Field reflexFieldDesc = udfs.getClass().getDeclaredField(field + "Desc");
						// Accessible
						reflexFieldDesc.setAccessible(true);
						// Set the value
						reflexFieldDesc.set(udfs, valueDesc);
					}
				}
			}
		} catch (Exception e) {
			tools.log(Level.SEVERE,"Error in readUDFRENTDescriptions");
		} finally {
			em.close();
		}
	}

}
