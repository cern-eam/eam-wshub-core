package ch.cern.eam.wshub.core.tools;

import ch.cern.eam.wshub.core.annotations.EAMField;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import net.datastream.schemas.mp_fields.USERDEFINEDAREA;
import org.openapplications.oagis_segments.AMOUNT;
import org.openapplications.oagis_segments.DATETIME;
import org.openapplications.oagis_segments.QUANTITY;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;

public class EAMFieldTools {

    private CustomFieldsTools customFieldsTools;
    private Tools tools;

    public EAMFieldTools(CustomFieldsTools customFieldsTools, Tools tools) {
        this.customFieldsTools = customFieldsTools;
        this.tools = tools;
    }

    /**
     * Transform WSHub Entity (wshubObject) to EAM Entity (eamObject). All fields marked as read-only
     * in the WSHub Entity are omitted.
     *
     * @param eamObject
     * @param wshubObject
     * @param context
     * @param <I>
     * @param <W>
     * @return
     */
    public <I,W> I transformWSHubObject(I eamObject, W wshubObject, EAMContext context) throws EAMException {
    	final List<EAMException> exceptionList = new ArrayList<>();
    	Arrays.stream(wshubObject.getClass().getDeclaredFields())
                .filter(wshubField -> wshubField.getAnnotation(EAMField.class) != null)
                .filter(wshubField -> !wshubField.getAnnotation(EAMField.class).readOnly())
                .sorted(Comparator.comparing(field -> field.getAnnotation(EAMField.class).nullifyParentLevel()))
                .forEach(wshubField -> {
                    try {
                        setEAMValue(wshubObject, wshubField, eamObject, context);
                    } catch (EAMException e) {
                        exceptionList.add(e);
                    }
                });
    	
    	if (!exceptionList.isEmpty()) {
            final String message = exceptionList.stream()
                    .map(EAMException::getMessage)
                    .reduce("", (t, acc) -> acc + "; " + t);
            throw Tools.generateFault(message);
        }
    	
        return eamObject;
    }

    /**
     * Transform  EAM Entity (eamObject) to WSHub Entity (wshubObject)
     *
     * @param wshubObject
     * @param eamObject
     * @param <I>
     * @param <W>
     * @return
     */
    public <I,W> W transformEAMObject(W wshubObject, I eamObject, EAMContext eamContext) {
        Arrays.stream(wshubObject.getClass().getDeclaredFields())
                .filter(wshubField -> wshubField.getAnnotation(EAMField.class) != null)
                .forEach(wshubField -> setWSHubValue(wshubObject, wshubField, eamObject, eamContext));
        return wshubObject;
    }

    /**
     *
     *
     * @param wshubObject
     * @param wshubField
     * @param eamObject
     * @param context
     * @param <I>
     * @param <W>
     */
    private <I,W> void setEAMValue(W wshubObject, Field wshubField, I eamObject, EAMContext context ) throws EAMException {
        EAMField eamField = wshubField.getAnnotation(EAMField.class);
        for (String xpath: eamField.xpath()) {
            List<String> fieldNamePath = convertXPathToPropertyChain(eamObject.getClass(), xpath, eamField.enforceValidXpath());
            try {
                wshubField.setAccessible(true);
                Object wshubFieldValue = wshubField.get(wshubObject);
                setEAMFieldByPath(eamObject, fieldNamePath, wshubFieldValue, wshubField, context);
                if (!fieldNamePath.isEmpty()) {
                    // This will allow multiple xpaths
                    break;
                }
            } catch (EAMException exception) {
                throw exception;
            } catch (Exception exception ) {
                // Silence constant errors
//                exception.printStackTrace();
//                System.out.println("Problem: " + exception.getMessage());
            }
        }
    }

    /**
     *
     * @param wshubObject
     * @param wshubField
     * @param eamObject
     * @param <I>
     * @param <W>
     */
    private <I,W> void setWSHubValue(W wshubObject, Field wshubField, I eamObject, EAMContext eamContext) {

        try {
            Object eamValue = getValue(eamObject, wshubField.getAnnotation(EAMField.class));
            if (eamValue == null) {
                return;
            }
            wshubField.setAccessible(true);
            if (eamValue.getClass().equals(String.class)) {
                String stringValue = (String) eamValue;
                if (wshubField.getType().equals(Boolean.class) || wshubField.getType().equals(Boolean.TYPE)) {
                    wshubField.set(wshubObject, decodeBoolean(stringValue));
                } else {
                    wshubField.set(wshubObject, stringValue);
                }
            } else if (eamValue.getClass().equals(DATETIME.class)) {
                DATETIME dateTimeValue = (DATETIME) eamValue;
                wshubField.set(wshubObject, decodeEAMDate(dateTimeValue));
            } else if (eamValue.getClass().equals(AMOUNT.class)) {
                AMOUNT amountValue = (AMOUNT) eamValue;
                wshubField.set(wshubObject, decodeAmount(amountValue));
            } else if (eamValue.getClass().equals(QUANTITY.class)) {
                QUANTITY quantityValue = (QUANTITY) eamValue;
                wshubField.set(wshubObject, decodeQuantity(quantityValue));
            } else if (eamValue.getClass().equals(USERDEFINEDAREA.class)) {
                USERDEFINEDAREA userDefinedAreaValue = (USERDEFINEDAREA) eamValue;
                wshubField.set(wshubObject, customFieldsTools.readEAMCustomFields(userDefinedAreaValue, eamContext));
            } else if (eamValue.getClass().equals(Long.class) || eamValue.getClass().equals(Long.TYPE)) {
                Long longValue = (Long) eamValue;
                wshubField.set(wshubObject, BigInteger.valueOf(longValue));
            } else if ("UserDefinedFields".equals(wshubField.getAnnotation(EAMField.class).xpath()[0]) ||
                       "StandardUserDefinedFields".equals(wshubField.getAnnotation(EAMField.class).xpath()[0])) {
                wshubField.set(wshubObject, transformEAMObject(new UserDefinedFields(), eamValue, eamContext));
            } else if(List.class.isAssignableFrom(eamValue.getClass())){
                List eamValueAsList = (List) eamValue;
                if(wshubField.getGenericType() instanceof ParameterizedType) {
                    ParameterizedType listGenericType = (ParameterizedType) wshubField.getGenericType();
                    Class itemsType = (Class) listGenericType.getActualTypeArguments()[0];
                    List rawWSHubList = new ArrayList();
                    for (int i = 0; i < eamValueAsList.size(); i++) {
                        rawWSHubList.add(itemsType.newInstance());
                        transformEAMObject(rawWSHubList.get(i), eamValueAsList.get(i), eamContext);
                    }
                    wshubField.set(wshubObject, rawWSHubList);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Property: " + e.getMessage());
        }

    }

    /**
     * Traverses XmlElement or XmlAttribute annotations matching the xpath elements to produce the list
     * of corresponding nested properties
     *
     * @param eamClass
     * @param xpath
     * @param enforceValidXpath
     * @return
     */
    private List<String> convertXPathToPropertyChain(Class eamClass, String xpath, Boolean enforceValidXpath) {
            LinkedList<String> result = new LinkedList<>();
            try {

                for (String xp : Arrays.asList(xpath.split("/"))) {
                    Field field = Arrays.stream(eamClass.getDeclaredFields())
                            .filter(decField ->
                                    decField.getAnnotation(XmlElement.class) != null
                                            && xp.equalsIgnoreCase(decField.getAnnotation(XmlElement.class).name())
                                    || decField.getAnnotation(XmlAttribute.class) != null
                                            && xp.equalsIgnoreCase(decField.getAnnotation(XmlAttribute.class).name())
                                    || xp.equals(decField.getName()))
                            .findFirst().orElse(null);

                    result.add(field.getName());
                    eamClass = field.getType();
                }
                return result;
            } catch (Exception e) {
//                if (enforceValidXpath) {
//                    System.out.println(" Couldn't extract path for: " + xpath);
//                }
            }
            return result;
    }

    /**
     *
     * @param eamObject
     * @param fieldNames
     * @return
     */
    private <I> Object getValue(I eamObject, List<String> fieldNames) {
        if (fieldNames == null || fieldNames.size() == 0) {
            return null;
        }

        try {
            Object result = eamObject;
            for (String fieldName : fieldNames) {
                Field field = result.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                result =  field.get(result);
            }
            return result;
        } catch (Exception e) {
            // Nothing wrong about an exception here
            return null;
        }
    }

    private <I> Object getValue(I eamObject, EAMField eamField) {
        return Arrays.stream(eamField.xpath()).map(xpath -> convertXPathToPropertyChain(eamObject.getClass(), xpath, false))
                .map(propList -> getValue(eamObject, propList))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private void setOrganizationField(Object eamObject, EAMContext context) {
        try {
            Field orgField = eamObject.getClass().getDeclaredField("organizationid");
            orgField.setAccessible(true);

            String currentOrganization = (String) orgField.get(eamObject);

            if (currentOrganization == null) {
                orgField.set(eamObject, tools.getOrganization(context));
            }
        } catch (Exception exception) {
            // Nothing wrong about an exception here
        }
    }

    private Object setPreviousFields(Object eamObject, List<String> fields) {
        Object eamTempObject = eamObject;
        try {
            for (String field : fields) {
                Field eamField = eamTempObject.getClass().getDeclaredField(field);
                eamField.setAccessible(true);
                Object eamFieldValue = eamField.get(eamTempObject);
                if (eamFieldValue == null) {
                    eamField.set(eamTempObject, eamField.getType().newInstance());
                }
                eamTempObject = eamField.get(eamTempObject);
            }

        } catch (Exception exception) {
            // Error not allowed here
        }
        return eamTempObject;
    }

    /**
     *
     * @param eamObject
     * @param eamFieldName
     * @param wshubFieldValue
     * @param wshubField
     * @param context
     */
    private void setSingleField(Object eamObject, String eamFieldName, Object wshubFieldValue, Field wshubField, EAMContext context) throws EAMException {
        try {
            Field eamField = eamObject.getClass().getDeclaredField(eamFieldName);
            eamField.setAccessible(true);

            if (wshubFieldValue instanceof Date) {
                // DATE -> DATETIME
                Date dateValue = (Date) wshubFieldValue;
                if (dateValue.getTime() == 0) {
                    eamField.set(eamObject, null);
                } else {
                    eamField.set(eamObject, tools.getDataTypeTools().encodeEAMDate(dateValue, eamFieldName));
                }
            } else if (wshubFieldValue.getClass().equals(BigDecimal.class)) {
                // BIG DECIMAL -> AMOUNT / QUANTITY / Double
                BigDecimal bigDecimalValue = (BigDecimal) wshubFieldValue;
                if (bigDecimalValue.equals(BigDecimal.valueOf(DataTypeTools.NULLIFY_VALUE))) {
                    eamField.set(eamObject, null);
                } else if (eamField.getType().equals(AMOUNT.class)) {
                    eamField.set(eamObject, encodeAmount(bigDecimalValue, eamFieldName));
                } else if (eamField.getType().equals(QUANTITY.class)) {
                    eamField.set(eamObject, encodeQuantity(bigDecimalValue, eamFieldName));
                } else if (eamField.getType().equals(Double.class)) {
                    eamField.set(eamObject, bigDecimalValue.doubleValue());
                }
            } else if (wshubFieldValue.getClass().equals(BigInteger.class)) {
                // BIG INTEGER -> LONG
                BigInteger bigIntegerValue = (BigInteger) wshubFieldValue;
                if (bigIntegerValue.equals(BigInteger.valueOf(DataTypeTools.NULLIFY_VALUE))) {
                    eamField.set(eamObject, null);
                } else {
                    eamField.set(eamObject, bigIntegerValue.longValue());
                }
            } else if (wshubFieldValue.getClass().equals(Boolean.class) || wshubFieldValue.getClass().equals(Boolean.TYPE)) {
                // BOOLEAN -> STRING
                Boolean booleanValue = (Boolean) wshubFieldValue;
                eamField.set(eamObject, encodeBoolean(booleanValue, wshubField.getAnnotation(EAMField.class).booleanType()));
            } else if (wshubFieldValue.getClass().equals(CustomField[].class)) {
                // CUSTOM FIELDS
                CustomField[] customFields = (CustomField[]) wshubFieldValue;
                USERDEFINEDAREA userdefinedarea = (USERDEFINEDAREA) eamField.get(eamObject);
                tools.getCustomFieldsTools().updateEAMCustomFields(userdefinedarea, customFields);
            } else if ("UserDefinedFields".equals(wshubField.getAnnotation(EAMField.class).xpath()[0])
                || "StandardUserDefinedFields".equals(wshubField.getAnnotation(EAMField.class).xpath()[0])) {
                // USER DEFINED FIELDS
                UserDefinedFields userDefinedFieldsValue = (UserDefinedFields) wshubFieldValue;
                if (eamField.get(eamObject) == null) {
                    eamField.set(eamObject, eamField.getType().newInstance());
                }
                eamField.set(eamObject, transformWSHubObject(eamField.get(eamObject), userDefinedFieldsValue, context));
            } else {
                eamField.set(eamObject, wshubFieldValue);
            }

            setOrganizationField(eamObject, context);
        } 
        catch (EAMException exception) {
        	throw exception;
        }
        catch (Exception exception) {
            System.out.println("Error in setSingleField: " + exception.getMessage());
        }
    }

    private void setEAMFieldByPath(Object eamObject, List<String> path,
                                     Object wshubFieldValue, Field wshubField, EAMContext context) throws EAMException {
        try {
            String fieldName = path.get(0);
            Field eamField = eamObject.getClass().getDeclaredField(fieldName);
            eamField.setAccessible(true);

            if (wshubFieldValue == null) {
                return;
            }

            if (path.size() == 1) {
                setSingleField(eamObject, fieldName, wshubFieldValue, wshubField, context);
                return;
            } else if (wshubFieldValue.equals("")) {
                if (path.size() == wshubField.getAnnotation(EAMField.class).nullifyParentLevel() + 1) {
                    eamField.set(eamObject, null);
                }
                if (eamField.get(eamObject) == null) return;
            }

            if (eamField.get(eamObject) == null) {
                eamField.set(eamObject, eamField.getType().newInstance());
            }
            setOrganizationField(eamObject, context);
            setEAMFieldByPath(eamField.get(eamObject), path.subList(1, path.size()), wshubFieldValue, wshubField, context);
        } 
        catch (EAMException exception) {
        	throw exception;
        }
        catch (Exception exception) {
            // Error not allowed here
        }
    }

}
