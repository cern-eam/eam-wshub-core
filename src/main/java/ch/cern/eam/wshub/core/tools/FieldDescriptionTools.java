package ch.cern.eam.wshub.core.tools;

import java.lang.reflect.Field;
import java.util.List;
import javax.persistence.EntityManager;

import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fetch the description of some fields that are missing when reading from
 * web services
 *
 */
public class FieldDescriptionTools {

	private static final Logger logger = LoggerFactory.getLogger(FieldDescriptionTools.class);

	private Tools tools;

	public FieldDescriptionTools(Tools tools) {
		this.tools = tools;
	}

	public String readPersonDesc(String personCode) {
		if (!tools.isDatabaseConnectionConfigured()) {
			return null;
		}
		EntityManager em = tools.getEntityManager();
		try {
			Object[] result = (Object[]) em
					.createNativeQuery("select per_desc, per_code from r5personnel where per_code = :personCode")
					.setParameter("personCode", personCode).getSingleResult();
			return result[0].toString();
		} catch (Exception e) {
			logger.error("Error in readPersonDesc for personCode " + personCode, e);
			return null;
		} finally {
			em.close();
		}
	}

	public String readDepartmentDesc(String departmentCode) {
		if (!tools.isDatabaseConnectionConfigured()) {
			return null;
		}
		EntityManager em = tools.getEntityManager();
		try {
			if (departmentCode != null)
				return em.createNativeQuery("SELECT MRC_DESC FROM R5MRCS WHERE MRC_CODE = :mrc_code")
						.setParameter("mrc_code", departmentCode).getSingleResult().toString();
		} catch (Exception e) {
			logger.error("Error in readDepartmentDesc for departmentCode " + departmentCode, e);
		} finally {
			em.close();
		}
		return null;
	}

	public String readClassDesc(String entityType, String classCode) {
		if (!tools.isDatabaseConnectionConfigured()) {
			return null;
		}
		EntityManager em = tools.getEntityManager();
		try {
			if (entityType != null && classCode != null)
				return em.createNativeQuery(
						"SELECT CLS_DESC FROM R5CLASSES WHERE CLS_ENTITY= :entityType and CLS_CODE = :classCode")
						.setParameter("entityType", entityType).setParameter("classCode", classCode).getSingleResult()
						.toString();
		} catch (Exception e) {
			logger.error("Error in readClassDesc for entityType " + entityType + " and classCode " + classCode, e);
		} finally {
			em.close();
		}
		return null;
	}

	public String readUOMDesc(String uomCode) {
		if (!tools.isDatabaseConnectionConfigured()) {
			return null;
		}
		EntityManager em = tools.getEntityManager();
		try {
			if (uomCode != null)
				return em.createNativeQuery("SELECT UOM_DESC FROM R5UOMS WHERE UOM_CODE = :uomCode")
						.setParameter("uomCode", uomCode).getSingleResult().toString();
		} catch (Exception e) {
			logger.error("Error in readUOMDesc for uomCode " + uomCode, e);
		} finally {
			em.close();
		}
		return null;
	}

	public String readCategoryDesc(String categoryCode) {
		if (!tools.isDatabaseConnectionConfigured()) {
			return null;
		}
		EntityManager em = tools.getEntityManager();
		try {
			if (categoryCode != null)
				return em.createNativeQuery("SELECT CAT_DESC FROM R5CATEGORIES where CAT_CODE = :categoryCode")
						.setParameter("categoryCode", categoryCode).getSingleResult().toString();
		} catch (Exception e) {
			logger.error("Error in readCategoryDesc for categoryCode " + categoryCode, e);
		} finally {
			em.close();
		}
		return null;
	}

	public String readCommodityDesc(String commodityCode) {
		if (!tools.isDatabaseConnectionConfigured()) {
			return null;
		}
		EntityManager em = tools.getEntityManager();
		try {
			if (commodityCode != null)
				return em.createNativeQuery("SELECT CMD_DESC FROM R5COMMODITIES WHERE CMD_CODE = :commodityCode")
						.setParameter("commodityCode", commodityCode).getSingleResult().toString();
		} catch (Exception e) {
			logger.error("Error in readCommodityDesc for commodityCode " + commodityCode, e);
		} finally {
			em.close();
		}
		return null;
	}

	public String readManufacturerDesc(String manufacturerCode) {
		if (!tools.isDatabaseConnectionConfigured()) {
			return null;
		}
		EntityManager em = tools.getEntityManager();
		try {
			if (manufacturerCode != null)
				return em.createNativeQuery("SELECT MFG_DESC FROM R5MANUFACTURERS where MFG_CODE = :manufacturerCode")
						.setParameter("manufacturerCode", manufacturerCode).getSingleResult().toString();
		} catch (Exception e) {
			logger.error("Error in readManufacturerDesc for manufacturerCode " + manufacturerCode, e);
		} finally {
			em.close();
		}
		return null;
	}

	public String readBinDesc(String storeCode, String binCode) {
		if (!tools.isDatabaseConnectionConfigured()) {
			return null;
		}
		EntityManager em = tools.getEntityManager();
		try {
			if (storeCode != null && binCode != null)
				return em
						.createNativeQuery(
								"SELECT BIN_DESC FROM R5BINS where BIN_STORE = :storeCode and BIN_CODE = :binCode")
						.setParameter("storeCode", storeCode).setParameter("binCode", binCode).getSingleResult()
						.toString();
		} catch (Exception e) {
			logger.error("Error in readBinDesc for storeCode " + storeCode + " and binCode " + binCode , e);
		} finally {
			em.close();
		}
		return null;
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
			logger.error("Error in readCustomFieldDesc for entityCode " + entityCode + " and codeValue " + codeValue , e);
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
			logger.error("Error in readUDFRENTDescriptions", e);
		} finally {
			em.close();
		}
	}

}
