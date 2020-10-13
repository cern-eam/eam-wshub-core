package ch.cern.eam.wshub.core.tools;

import ch.cern.eam.wshub.core.annotations.InforField;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import net.datastream.schemas.mp_fields.USERDEFINEDAREA;
import org.openapplications.oagis_segments.AMOUNT;
import org.openapplications.oagis_segments.DATETIME;
import org.openapplications.oagis_segments.QUANTITY;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;

public class InforFieldTools {

    private CustomFieldsTools customFieldsTools;
    private Tools tools;

    public InforFieldTools(CustomFieldsTools customFieldsTools, Tools tools) {
        this.customFieldsTools = customFieldsTools;
        this.tools = tools;
    }

    /**
     * Transform WSHub Entity (wshubObject) to Infor Entity (inforObject). All fields marked as read-only
     * in the WSHub Entity are omitted.
     *
     * @param inforObject
     * @param wshubObject
     * @param context
     * @param <I>
     * @param <W>
     * @return
     */
    public <I,W> I transformWSHubObject(I inforObject, W wshubObject, InforContext context) {
        Arrays.stream(wshubObject.getClass().getDeclaredFields())
                .filter(wshubField -> wshubField.getAnnotation(InforField.class) != null)
                .filter(wshubField -> !wshubField.getAnnotation(InforField.class).readOnly())
                .sorted(Comparator.comparing(field -> field.getAnnotation(InforField.class).nullifyParentLevel()))
                .forEach(wshubField -> setInforValue(wshubObject, wshubField, inforObject, context));
        return inforObject;
    }

    /**
     * Transform  Infor Entity (inforObject) to WSHub Entity (wshubObject)
     *
     * @param wshubObject
     * @param inforObject
     * @param <I>
     * @param <W>
     * @return
     */
    public <I,W> W transformInforObject(W wshubObject, I inforObject) {
        Arrays.stream(wshubObject.getClass().getDeclaredFields())
                .filter(wshubField -> wshubField.getAnnotation(InforField.class) != null)
                .forEach(wshubField -> setWSHubValue(wshubObject, wshubField, inforObject));
        return wshubObject;
    }

    /**
     *
     *
     * @param wshubObject
     * @param wshubField
     * @param inforObject
     * @param context
     * @param <I>
     * @param <W>
     */
    private <I,W> void setInforValue(W wshubObject, Field wshubField, I inforObject, InforContext context ) {
        InforField inforField = wshubField.getAnnotation(InforField.class);
        List<String> fieldNamePath = convertXPathToPropertyChain(inforObject.getClass(), inforField.xpath()[0], inforField.enforceValidXpath());
        try {
            wshubField.setAccessible(true);
            Object wshubFieldValue = wshubField.get(wshubObject);
            setInforFieldByPath(inforObject, fieldNamePath, wshubFieldValue, wshubField, context);
        } catch (Exception exception ) {
            exception.printStackTrace();
            System.out.println("Problem: " + exception.getMessage());
        }
    }

    /**
     *
     * @param wshubObject
     * @param wshubField
     * @param inforObject
     * @param <I>
     * @param <W>
     */
    private <I,W> void setWSHubValue(W wshubObject, Field wshubField, I inforObject) {

        try {
            Object inforValue = getValue(inforObject, wshubField.getAnnotation(InforField.class));
            if (inforValue == null) {
                return;
            }
            wshubField.setAccessible(true);
            if (inforValue.getClass().equals(String.class)) {
                String stringValue = (String) inforValue;
                if (wshubField.getType().equals(Boolean.class) || wshubField.getType().equals(Boolean.TYPE)) {
                    wshubField.set(wshubObject, decodeBoolean(stringValue));
                } else {
                    wshubField.set(wshubObject, stringValue);
                }
            } else if (inforValue.getClass().equals(DATETIME.class)) {
                DATETIME dateTimeValue = (DATETIME) inforValue;
                wshubField.set(wshubObject, decodeInforDate(dateTimeValue));
            } else if (inforValue.getClass().equals(AMOUNT.class)) {
                AMOUNT amountValue = (AMOUNT) inforValue;
                wshubField.set(wshubObject, decodeAmount(amountValue));
            } else if (inforValue.getClass().equals(QUANTITY.class)) {
                QUANTITY quantityValue = (QUANTITY) inforValue;
                wshubField.set(wshubObject, decodeQuantity(quantityValue));
            } else if (inforValue.getClass().equals(USERDEFINEDAREA.class)) {
                USERDEFINEDAREA userDefinedAreaValue = (USERDEFINEDAREA) inforValue;
                wshubField.set(wshubObject, customFieldsTools.readInforCustomFields(userDefinedAreaValue));
            } else if (inforValue.getClass().equals(Long.class) || inforValue.getClass().equals(Long.TYPE)) {
                Long longValue = (Long) inforValue;
                wshubField.set(wshubObject, BigInteger.valueOf(longValue));
            } else if ("UserDefinedFields".equals(wshubField.getAnnotation(InforField.class).xpath()[0]) ||
                       "StandardUserDefinedFields".equals(wshubField.getAnnotation(InforField.class).xpath()[0])) {
                wshubField.set(wshubObject, transformInforObject(new UserDefinedFields(), inforValue));
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
     * @param inforClass
     * @param xpath
     * @param enforceValidXpath
     * @return
     */
    private List<String> convertXPathToPropertyChain(Class inforClass, String xpath, Boolean enforceValidXpath) {
            LinkedList<String> result = new LinkedList<>();
            try {

                for (String xp : Arrays.asList(xpath.split("/"))) {
                    Field field = Arrays.stream(inforClass.getDeclaredFields())
                            .filter(decField ->
                                    decField.getAnnotation(XmlElement.class) != null &&
                                            xp.equalsIgnoreCase(decField.getAnnotation(XmlElement.class).name()) ||
                                            decField.getAnnotation(XmlAttribute.class) != null &&
                                                    xp.equalsIgnoreCase(decField.getAnnotation(XmlAttribute.class).name()))
                            .findFirst().orElse(null);

                    result.add(field.getName());
                    inforClass = field.getType();
                }
                return result;
            } catch (Exception e) {
                if (enforceValidXpath) {
                    System.out.println("Couldn't extract path for: " + xpath);
                }
            }
            return result;
    }

    /**
     *
     * @param inforObject
     * @param fieldNames
     * @return
     */
    private <I> Object getValue(I inforObject, List<String> fieldNames) {
        if (fieldNames == null || fieldNames.size() == 0) {
            return null;
        }

        try {
            Object result = inforObject;
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

    private <I> Object getValue(I inforObject, InforField inforField) {
        return Arrays.stream(inforField.xpath()).map(xpath -> convertXPathToPropertyChain(inforObject.getClass(), xpath, false))
                .map(propList -> getValue(inforObject, propList))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private void setOrganizationField(Object inforObject, InforContext context) {
        try {
            Field orgField = inforObject.getClass().getDeclaredField("organizationid");
            orgField.setAccessible(true);
            orgField.set(inforObject, tools.getOrganization(context));
        } catch (Exception exception) {
            // Nothing wrong about an exception here
        }
    }

    private Object setPreviousFields(Object inforObject, List<String> fields) {
        Object inforTempObject = inforObject;
        try {
            for (String field : fields) {
                Field inforField = inforTempObject.getClass().getDeclaredField(field);
                inforField.setAccessible(true);
                Object inforFieldValue = inforField.get(inforTempObject);
                if (inforFieldValue == null) {
                    inforField.set(inforTempObject, inforField.getType().newInstance());
                }
                inforTempObject = inforField.get(inforTempObject);
            }

        } catch (Exception exception) {
            // Error not allowed here
        }
        return inforTempObject;
    }

    /**
     *
     * @param inforObject
     * @param inforFieldName
     * @param wshubFieldValue
     * @param wshubField
     * @param context
     */
    private void setSingleField(Object inforObject, String inforFieldName, Object wshubFieldValue, Field wshubField, InforContext context) {
        try {
            Field inforField = inforObject.getClass().getDeclaredField(inforFieldName);
            inforField.setAccessible(true);

            if (wshubFieldValue instanceof Date) {
                // DATE -> DATETIME
                Date dateValue = (Date) wshubFieldValue;
                if (dateValue.getTime() == 0) {
                    inforField.set(inforObject, null);
                } else {
                    inforField.set(inforObject, tools.getDataTypeTools().encodeInforDate(dateValue, inforFieldName));
                }
            } else if (wshubFieldValue.getClass().equals(BigDecimal.class)) {
                // BIG DECIMAL -> AMOUNT / QUANTITY
                BigDecimal bigDecimalValue = (BigDecimal) wshubFieldValue;
                if (bigDecimalValue.equals(BigDecimal.valueOf(DataTypeTools.NULLIFY_VALUE))) {
                    inforField.set(inforObject, null);
                } else if (inforField.getType().equals(AMOUNT.class)) {
                    inforField.set(inforObject, encodeAmount(bigDecimalValue, inforFieldName));
                } else if (inforField.getType().equals(QUANTITY.class)) {
                    inforField.set(inforObject, encodeQuantity(bigDecimalValue, inforFieldName));
                }
            } else if (wshubFieldValue.getClass().equals(BigInteger.class)) {
                // BIG INTEGER -> LONG
                BigInteger bigIntegerValue = (BigInteger) wshubFieldValue;
                if (bigIntegerValue.equals(BigInteger.valueOf(DataTypeTools.NULLIFY_VALUE))) {
                    inforField.set(inforObject, null);
                } else {
                    inforField.set(inforObject, bigIntegerValue.longValue());
                }
            } else if (wshubFieldValue.getClass().equals(Boolean.class) || wshubFieldValue.getClass().equals(Boolean.TYPE)) {
                // BOOLEAN -> STRING
                Boolean booleanValue = (Boolean) wshubFieldValue;
                inforField.set(inforObject, encodeBoolean(booleanValue, wshubField.getAnnotation(InforField.class).booleanType()));
            } else if (wshubFieldValue.getClass().equals(CustomField[].class)) {
                // CUSTOM FIELDS
                CustomField[] customFields = (CustomField[]) wshubFieldValue;
                USERDEFINEDAREA userdefinedarea = (USERDEFINEDAREA) inforField.get(inforObject);
                tools.getCustomFieldsTools().updateInforCustomFields(userdefinedarea, customFields);
            } else if ("UserDefinedFields".equals(wshubField.getAnnotation(InforField.class).xpath()[0])
                || "StandardUserDefinedFields".equals(wshubField.getAnnotation(InforField.class).xpath()[0])) {
                // USER DEFINED FIELDS
                UserDefinedFields userDefinedFieldsValue = (UserDefinedFields) wshubFieldValue;
                if (inforField.get(inforObject) == null) {
                    inforField.set(inforObject, inforField.getType().newInstance());
                }
                inforField.set(inforObject, transformWSHubObject(inforField.get(inforObject), userDefinedFieldsValue, context));
            } else {
                inforField.set(inforObject, wshubFieldValue);
            }

            setOrganizationField(inforObject, context);
        } catch (Exception exception) {
            System.out.println("Error in setSingleField: " + exception.getMessage());
        }
    }

    /**
     *
     * @param inforObject
     * @param fieldNameWrapper
     * @param fieldNameValue
     * @param wshubFieldValue
     * @param wshubField
     * @param context
     */
    private void setComplexField(Object inforObject, String fieldNameWrapper, String fieldNameValue, Object wshubFieldValue, Field wshubField, InforContext context) {
        try {
            Field inforField = inforObject.getClass().getDeclaredField(fieldNameWrapper);
            inforField.setAccessible(true);

            if (wshubFieldValue.equals("")) {
                inforField.set(inforObject, null);
            } else {
                if (inforField.get(inforObject) == null) {
                    inforField.set(inforObject, inforField.getType().newInstance());
                }
                setOrganizationField(inforObject, context);
                setSingleField(inforField.get(inforObject), fieldNameValue, wshubFieldValue, wshubField, context);
            }
        } catch (Exception exception) {
            // Error not allowed here
        }
    }

    private void setInforFieldByPath(Object inforObject, List<String> path,
                                     Object wshubFieldValue, Field wshubField, InforContext context) {
        try {
            String fieldName = path.get(0);
            Field inforField = inforObject.getClass().getDeclaredField(fieldName);
            inforField.setAccessible(true);

            if (wshubFieldValue == null) {
                return;
            }

            if (path.size() == 1) {
                setSingleField(inforObject, fieldName, wshubFieldValue, wshubField, context);
                return;
            } else if (wshubFieldValue.equals("")) {
                if (path.size() == wshubField.getAnnotation(InforField.class).nullifyParentLevel() + 1) {
                    inforField.set(inforObject, null);
                }
                if (inforField.get(inforObject) == null) return;
            }

            if (inforField.get(inforObject) == null) {
                inforField.set(inforObject, inforField.getType().newInstance());
            }
            setOrganizationField(inforObject, context);
            setInforFieldByPath(inforField.get(inforObject), path.subList(1, path.size()), wshubFieldValue, wshubField, context);
        } catch (Exception exception) {
            // Error not allowed here
        }
    }

}
