package ch.cern.eam.wshub.core.tools;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp9501_001.CUSTOMFIELDREQ;
import net.datastream.schemas.mp_functions.mp9501_001.MP9501_GetCustomFields_001;
import net.datastream.schemas.mp_results.mp9501_001.MP9501_GetCustomFields_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.xml.ws.Holder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.*;

public class CustomFieldsTools {

    private Tools tools;
    private ApplicationData applicationData;
    private InforWebServicesPT inforws;
    private static Map<String, String[][]> map;

    public CustomFieldsTools(Tools tools, ApplicationData applicationData, InforWebServicesPT inforws) {
        this.tools = tools;
        this.applicationData = applicationData;
        this.inforws = inforws;
        map = new HashMap<>();
    }
    //
    // CUSTOM FIELDS
    //

    // INFOR CUSTOM FIELD -> MIDDLE TIER CUSTOM FIELD
    public CustomField decodeInforCustomField(CUSTOMFIELD customFieldInfor) {
        CustomField customField = new CustomField();

        customField.setGroupLabel(customFieldInfor.getGROUPLABEL());
        customField.setClassCode(customFieldInfor.getCLASSID().getCLASSCODE());
        customField.setEntityCode(customFieldInfor.getEntity());
        customField.setCode(customFieldInfor.getPROPERTYCODE());
        customField.setType(customFieldInfor.getType());
        customField.setLabel(customFieldInfor.getPROPERTYLABEL());
        customField.setUOM(customFieldInfor.getUOM());
        customField.setMinValue(customFieldInfor.getMINVALUE());
        customField.setMaxValue(customFieldInfor.getMAXVALUE());
        if (customFieldInfor.getLOVSETTINGS() != null) {
            customField.setLovType(customFieldInfor.getLOVSETTINGS().getLOV_TYPE());
            customField.setLovValidate(customFieldInfor.getLOVSETTINGS().getLOV_VALIDATE());
        }
        //
        //
        //
        if (customFieldInfor.getType().toUpperCase().equals("DATI") && customFieldInfor.getDATETIMEFIELD() != null) {
            customField.setValue(tools.getDataTypeTools().retrieveDate(customFieldInfor.getDATETIMEFIELD(), "dd-MMM-yyyy HH:mm"));
        }
        //
        //
        //
        else if (customFieldInfor.getType().toUpperCase().equals("DATE") && customFieldInfor.getDATEFIELD() != null) {
            customField.setValue(tools.getDataTypeTools().retrieveDate(customFieldInfor.getDATEFIELD(), "dd-MMM-yyyy"));
        }
        //
        //
        //
        else if (customFieldInfor.getType().toUpperCase().equals("RENT") && customFieldInfor.getENTITYCODEFIELD() != null) {
            customField.setValue(customFieldInfor.getENTITYCODEFIELD().getCODEVALUE());
            customField.setRentCodeValue(customFieldInfor.getENTITYCODEFIELD().getEntity());
            customField.setValueDesc(tools.getFieldDescriptionsTools().readCustomFieldDesc(customFieldInfor.getENTITYCODEFIELD().getEntity(), customFieldInfor.getENTITYCODEFIELD().getCODEVALUE()));
        }
        //
        //
        //
        else if (customFieldInfor.getType().toUpperCase().equals("NUM") && customFieldInfor.getNUMBERFIELD() != null) {
            customField.setValue(decodeBigDecimal(tools.getDataTypeTools().decodeQuantity(customFieldInfor.getNUMBERFIELD())));
        }
        //
        //
        //
        else if (customFieldInfor.getType().toUpperCase().equals("CODE") && customFieldInfor.getCODEDESCFIELD() != null) {
            customField.setValue(customFieldInfor.getCODEDESCFIELD().getCODEVALUE());
            customField.setValueDesc(customFieldInfor.getCODEDESCFIELD().getDESCRIPTION());
            try {
                String[][] cf = getCFValues(customFieldInfor.getCLASSID().getCLASSCODE(),
                        customFieldInfor.getPROPERTYCODE(), "EN");
                customField.setCfc(cf[0]);
                customField.setCfd(cf[1]);
            } catch (Exception e) {
            }
        }
        //
        //
        //
        else if (customFieldInfor.getType().toUpperCase().equals("CHAR") && customFieldInfor.getTEXTFIELD() != null){
            customField.setValue(customFieldInfor.getTEXTFIELD());
        }
        //
        //
        //

        return customField;
    }

    // MIDDLE TIER CUSTOM FIELD -> INFOR CUSTOM FIELD
    public CUSTOMFIELD encodeInforCustomField(CUSTOMFIELD customFieldInfor, CustomField customField)
            throws InforException {
        //
        // DATE TIME
        //
        if (customFieldInfor.getType().toUpperCase().equals("DATI")) {
            if (customField.getValue() != null) {
                customFieldInfor.setDATETIMEFIELD(tools.getDataTypeTools().formatDate(customField.getValue(),
                        "Custom field '" + customFieldInfor.getPROPERTYLABEL() + "'"));
            }
        }
        //
        // DATE
        //
        if (customFieldInfor.getType().toUpperCase().equals("DATE")) {
            if (customField.getValue() != null) {
                customFieldInfor.setDATEFIELD(tools.getDataTypeTools().formatDate(customField.getValue(),
                        "Custom field '" + customFieldInfor.getPROPERTYLABEL() + "'"));
            }
        }
        //
        // ENTITY
        //
        if (customFieldInfor.getType().toUpperCase().equals("RENT")) {
            customFieldInfor.setENTITYCODEFIELD(new ENTITYCODEFIELD());
            if (customField.getValue() != null) {
                customFieldInfor.getENTITYCODEFIELD().setCODEVALUE(customField.getValue());
            } else {
                customFieldInfor.getENTITYCODEFIELD().setCODEVALUE("");
            }
        }
        //
        //
        //
        if (customFieldInfor.getType().toUpperCase().equals("NUM")) {
            if (customField.getValue() != null) {
                customFieldInfor.setNUMBERFIELD(tools.getDataTypeTools().encodeQuantity(encodeBigDecimal(customField.getValue(), "Custom Field"),
                        "Custom field '" + customFieldInfor.getPROPERTYLABEL() + "'"));
            }
        }
        //
        //
        //
        if (customFieldInfor.getType().toUpperCase().equals("CODE") && customFieldInfor.getCODEDESCFIELD() != null) {
            customFieldInfor.setCODEDESCFIELD(new CODEDESCFIELD());
            if (customField.getValue() != null) {
                customFieldInfor.getCODEDESCFIELD().setCODEVALUE(customField.getValue());
            } else {
                customFieldInfor.getCODEDESCFIELD().setCODEVALUE("");
            }
        }
        //
        //
        //
        if (customFieldInfor.getType().toUpperCase().equals("CHAR")) {
            customFieldInfor.setTEXTFIELD(customField.getValue());
        }
        return customFieldInfor;
    }

    public void updateInforCustomFields(USERDEFINEDAREA userdefinedarea, CustomField[] customFields)
            throws InforException {
        if (userdefinedarea == null
                || userdefinedarea.getCUSTOMFIELD() == null
                || userdefinedarea.getCUSTOMFIELD().size() == 0) {
            return;
        }

        userdefinedarea.getCUSTOMFIELD().forEach(cf -> cf.setChanged("false"));

        if (customFields == null || customFields.length == 0) {
            return;
        }

        Map<String, CustomField> wshubCustomFieldMap = Arrays.stream(customFields)
            .filter(cf -> cf != null && cf.getCode() != null)
            .collect(Collectors.toMap(CustomField::getCode, cf -> cf));

        userdefinedarea.getCUSTOMFIELD().removeIf(inforCustomField -> {
            CustomField wshubCustomField = wshubCustomFieldMap.get(inforCustomField.getPROPERTYCODE());
            return wshubCustomField != null
                && (wshubCustomField.getValue() == null || "".equals(wshubCustomField.getValue()));
        });

        for (CUSTOMFIELD inforCustomField : userdefinedarea.getCUSTOMFIELD()) {
            CustomField wshubCustomField = wshubCustomFieldMap.get(inforCustomField.getPROPERTYCODE());

            if (wshubCustomField != null && hasChangedCustomField(inforCustomField, wshubCustomField)) {
                encodeInforCustomField(inforCustomField, wshubCustomField);
                inforCustomField.setChanged("true");
            }
        }
    }

    /**
     * To identify if the custom field really changed
     *
     * @param customFieldInfor
     *            The custom field from Infor (The one that was read)
     * @param customField
     *            The custom field comming to be updated
     * @return true if it was changed, false otherwise
     */
    private boolean hasChangedCustomField(CUSTOMFIELD customFieldInfor, CustomField customField) {
        // Check accorging with the type of custom field
        switch (customFieldInfor.getType().toUpperCase()) {

            case "RENT":
                // Compare different
                if (customFieldInfor.getENTITYCODEFIELD() != null)
                    return isDifferentValue(customFieldInfor.getENTITYCODEFIELD().getCODEVALUE(), customField.getValue());
                return isDifferentValue(null, customField.getValue());
            case "NUM":
                // Decode the quantity
                String quantity = decodeBigDecimal(decodeQuantity(customFieldInfor.getNUMBERFIELD()));
                // Compare
                return isDifferentValue(quantity, customField.getValue());

            case "CODE":
                // Compare different
                if (customFieldInfor.getCODEDESCFIELD() != null)
                    return isDifferentValue(customFieldInfor.getCODEDESCFIELD().getCODEVALUE(), customField.getValue());
                return isDifferentValue(null, customField.getValue());
            case "CHAR":
                // Compare different
                return isDifferentValue(customFieldInfor.getTEXTFIELD(), customField.getValue());

            case "DATI":/* Date time */
            case "DATE":/* Date */
            default:// Cases not being checked
                return true;
        }
    }

    /**
     * Compare if the values are different
     *
     * @param value1
     * @param value2
     * @return True if the values are different, false otherwise
     */
    private boolean isDifferentValue(String value1, String value2) {
        // If one of the two is null or the value is different
        if (value1 == null && value2 != null)
            return true;
        else if (value1 != null && value2 == null)
            return true;
        else if (value1 == null && value2 == null)
            return false;
        // Compare not null values
        return !value1.equals(value2);
    }

    public CustomField[] readInforCustomFields(USERDEFINEDAREA userdefinedarea) {
        if (userdefinedarea == null || userdefinedarea.getCUSTOMFIELD() == null) {
            return new CustomField[0];
        }
        return userdefinedarea.getCUSTOMFIELD().stream().sorted(comparing(CUSTOMFIELD::getIndex)).map(cf -> decodeInforCustomField(cf)).toArray(CustomField[]::new);
    }

    public USERDEFINEDAREA getInforCustomFields(InforContext context, String entity, String inforClass)
            throws InforException {
        CUSTOMFIELDREQ cfreq = new CUSTOMFIELDREQ();
        cfreq.setORGANIZATIONID(tools.getOrganization(context));

        cfreq.setCLASSID(new CLASSID_Type());
        cfreq.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
        cfreq.getCLASSID().setCLASSCODE(inforClass);

        cfreq.setENTITYNAME(entity);

        MP9501_GetCustomFields_001 getcustomfields = new MP9501_GetCustomFields_001();
        getcustomfields.setCUSTOMFIELDREQ(cfreq);

        MP9501_GetCustomFields_001_Result result =
            tools.performInforOperation(context, inforws::getCustomFieldsOp, getcustomfields);
        return result.getUSERDEFINEDAREA();

    }

    public CustomField[] getWSHubCustomFields(InforContext context, String entity, String inforClass)
            throws InforException {
        return readInforCustomFields(getInforCustomFields(context, entity, inforClass));
    }

    public String[][] getCFValues(String classCode, String propertyCode, String language) throws SQLException {
        String[][] values = new String[2][];
        if (map.containsKey(propertyCode)) {
            return map.get(classCode + "_" + propertyCode + "_" + language);
        }
        String sqlq = "select distinct prv_value val,"
                + " (NVL((SELECT TRA_TEXT FROM U5TRANSLATIONS WHERE TRA_LANGUAGE = '" + language
                + "' and TRA_PAGENAME = '" + propertyCode
                + "' and UPPER(TRA_ELEMENTID) = UPPER(PVD_VALUE)),NVL(PVD_DESC, PVD_VALUE))) des, PRV_SEQNO"
                + " from r5propertyvalues, r5pvdescriptions where prv_property = '" + propertyCode
                + "' and prv_code is null and pvd_property(+)=prv_property and pvd_value(+)=prv_value AND COALESCE(prv_notused, '-') <> '+' order by PRV_SEQNO ASC";
        Connection v_connection = null;
        Statement stmt = null;
        ResultSet v_result = null;
        try {
            v_connection = tools.getDataSource().getConnection();
            stmt = v_connection.createStatement();
            v_result = stmt.executeQuery(sqlq);

            LinkedList<String> listCode = new LinkedList<String>();
            listCode.add("");
            LinkedList<String> listDesc = new LinkedList<String>();
            listDesc.add("");
            while (v_result.next()) {
                listCode.add(v_result.getString(1));
                listDesc.add(v_result.getString(2));
            }

            values[0] = listCode.toArray(new String[0]);
            values[1] = listDesc.toArray(new String[0]);
            map.put(classCode + "_" + propertyCode + "_" + language, values);
        } catch (Exception e) {
            //TODO log(Level.FATAL, "Failure in getCFValues: " + e.getMessage());
        } finally {
            if (v_result != null)
                v_result.close();
            if (stmt != null)
                stmt.close();
            if (v_connection != null)
                v_connection.close();
        }
        return values;
    }

    public USERDEFINEDAREA getInforCustomFields(
            InforContext context,
            String previousClass,
            USERDEFINEDAREA previousCustomFields,
            String targetClass,
            String entityType)
            throws InforException {

        // TODO: check if uppercasing these classes is actually necessary, left here for safety
        previousClass = previousClass == null ? null : previousClass.toUpperCase();
        targetClass = targetClass == null ? null : targetClass.toUpperCase();

		/*	Table with all possible cases of inputs
			IAE = IllegalArgumentException
			prevC = previousClass
			prevCFs = previousCustomFields
			targetC = targetClass

			+----+-------+---------+---------+----------------+---------------------------------+
			| ID | prevC | prevCFs | targetC | description    | usage                           |
			+----+-------+---------+---------+----------------+---------------------------------+
			| 1  | null  | CF      | null    | prevCFs        | no previous class nor target    | [4]
			| 2  | null  | CF      | ""      | merge with "*" | nullifying null class           | [3]
			| 3  | null  | CF      | "D"     | merge with "D" | merge "*" into "D"              | [3]
			| 4  | ""    | CF      | null    | prevCFs        | no previous class nor target    | [5]
			| 5  | ""    | CF      | ""      | merge with "*" | nullifying null class           | [3]
			| 6  | ""    | CF      | "D"     | merge with "D" | merge "*" into "D"              | [3]
			| 7  | "C"   | CF      | null    | prevCFs        | null [non-]update               | [4]
			| 8  | "C"   | CF      | ""      | merge with "*" | merge "C" into "*"              | [3]
			| 9  | "C"   | CF      | "D"     | merge with "D" | merge "C" into "D"              | [3]
			| 10 | null  | null    | null    | "*" CFs        | null constructor                | [5]
			| 11 | null  | null    | ""      | "*" CFs        | "" constructor                  | [2]
			| 12 | null  | null    | "D"     | "D" CFs        | constructor with "D" CFs        | [2]
			| 13 | ""    | null    | null    | IAE            | illegal argument                | [1]
			| 14 | ""    | null    | ""      | IAE            | illegal argument                | [1]
			| 15 | ""    | null    | "D"     | IAE            | illegal argument                | [1]
			| 16 | "C"   | null    | null    | IAE            | illegal argument                | [1]
			| 17 | "C"   | null    | ""      | IAE            | illegal argument                | [1]
			| 18 | "C"   | null    | "D"     | IAE            | illegal argument                | [1]
			+----+-------+---------+---------+----------------+---------------------------------+
		*/

        // [1] handle cases 13 to 18
        if(previousCustomFields == null && previousClass != null) {
            throw new IllegalArgumentException("Unable to create an object that already has a previous class.");
        }

        if(targetClass != null) {
            // this determines whether the class we should use is "*"or newClass
            // this separates cases 2 and 3, 5 and 6, 8 and 9
            String newClass = targetClass.length() == 0 ? "*" : targetClass;

            USERDEFINEDAREA classCustomFields = getInforCustomFields(context, entityType, newClass);

            // [2] handle case 11 and 12
            if(previousCustomFields == null) return classCustomFields;

            // [3] handle cases 2, 3, 5, 6, 8 and 9
            return merge(classCustomFields, previousCustomFields);
        }

        // we can now assume that targetClass is null

        // [4] handle case 1 and 7
        if(previousCustomFields != null) return previousCustomFields;

        // [5] handle case 4 and 10
        return getInforCustomFields(context, entityType, "*");
    }

    // IMPORTANT: this method mutates the argument called "base"
    private USERDEFINEDAREA merge(USERDEFINEDAREA base, USERDEFINEDAREA toppings) {
        // this hashmap turns the merge into a linear time operation
        HashMap<String, CUSTOMFIELD> codeToTopping = new HashMap<>();
        toppings.getCUSTOMFIELD().stream().forEach(topping -> codeToTopping.put(topping.getPROPERTYCODE(), topping));

        base.getCUSTOMFIELD().stream().forEach(baseCustomField -> {
            CUSTOMFIELD toppingCustomField = codeToTopping.get(baseCustomField.getPROPERTYCODE());

            CUSTOMFIELD sourceCustomField = toppingCustomField;
            if(toppingCustomField == null) sourceCustomField = baseCustomField;

            baseCustomField.setPROPERTYCODE(sourceCustomField.getPROPERTYCODE());

            baseCustomField.setDATEFIELD(
                    nonNullOrDefault(sourceCustomField.getDATEFIELD(), baseCustomField.getDATEFIELD()));
            baseCustomField.setDATETIMEFIELD(
                    nonNullOrDefault(sourceCustomField.getDATETIMEFIELD(), baseCustomField.getDATETIMEFIELD()));
            baseCustomField.setNUMBERFIELD(
                    nonNullOrDefault(sourceCustomField.getNUMBERFIELD(), baseCustomField.getNUMBERFIELD()));
            baseCustomField.setTEXTFIELD(
                    nonNullOrDefault(sourceCustomField.getTEXTFIELD(), baseCustomField.getTEXTFIELD()));
            baseCustomField.setCODEDESCFIELD(
                    nonNullOrDefault(sourceCustomField.getCODEDESCFIELD(), baseCustomField.getCODEDESCFIELD()));
            baseCustomField.setENTITYCODEFIELD(
                    nonNullOrDefault(sourceCustomField.getENTITYCODEFIELD(), baseCustomField.getENTITYCODEFIELD()));
        });

        return base;
    }

}
