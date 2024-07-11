package ch.cern.eam.wshub.core.tools;

import ch.cern.eam.wshub.core.adapters.DateAdapter;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.entities.CustomField;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp9501_001.CUSTOMFIELDREQ;
import net.datastream.schemas.mp_functions.mp9501_001.MP9501_GetCustomFields_001;
import net.datastream.schemas.mp_results.mp9501_001.MP9501_GetCustomFields_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import jakarta.xml.ws.Holder;
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
    private EAMWebServicesPT eamws;
    private static Map<String, String[][]> map;

    public CustomFieldsTools(Tools tools, ApplicationData applicationData, EAMWebServicesPT eamws) {
        this.tools = tools;
        this.applicationData = applicationData;
        this.eamws = eamws;
        map = new HashMap<>();
    }
    //
    // CUSTOM FIELDS
    //

    // INFOR CUSTOM FIELD -> MIDDLE TIER CUSTOM FIELD
    public CustomField decodeEAMCustomField(CUSTOMFIELD customFieldEAM, EAMContext eamContext) {
        CustomField customField = new CustomField();

        customField.setGroupLabel(customFieldEAM.getGROUPLABEL());
        customField.setClassCode(customFieldEAM.getCLASSID().getCLASSCODE());
        customField.setEntityCode(customFieldEAM.getEntity());
        customField.setCode(customFieldEAM.getPROPERTYCODE());
        customField.setType(customFieldEAM.getType());
        customField.setLabel(customFieldEAM.getPROPERTYLABEL());
        customField.setUOM(customFieldEAM.getUOM());
        customField.setMinValue(customFieldEAM.getMINVALUE());
        customField.setMaxValue(customFieldEAM.getMAXVALUE());
        if (customFieldEAM.getLOVSETTINGS() != null) {
            customField.setLovType(customFieldEAM.getLOVSETTINGS().getLOV_TYPE());
            customField.setLovValidate(customFieldEAM.getLOVSETTINGS().getLOV_VALIDATE());
        }
        //
        //
        //
        if (customFieldEAM.getType().toUpperCase().equals("DATI") && customFieldEAM.getDATETIMEFIELD() != null) {
            customField.setValue(tools.getDataTypeTools().retrieveDate(customFieldEAM.getDATETIMEFIELD(),
                    eamContext.getLocalizeResults() ? "dd-MMM-yyyy HH:mm" : DateAdapter.DATE_ISO_FORMAT));
        }
        //
        //
        //
        else if (customFieldEAM.getType().toUpperCase().equals("DATE") && customFieldEAM.getDATEFIELD() != null) {
            customField.setValue(tools.getDataTypeTools().retrieveDate(customFieldEAM.getDATEFIELD(),
                    eamContext.getLocalizeResults() ? "dd-MMM-yyyy" : DateAdapter.DATE_ISO_FORMAT));
        }
        //
        //
        //
        else if (customFieldEAM.getType().toUpperCase().equals("RENT") && customFieldEAM.getENTITYCODEFIELD() != null) {
            customField.setValue(customFieldEAM.getENTITYCODEFIELD().getCODEVALUE());
            customField.setRentCodeValue(customFieldEAM.getENTITYCODEFIELD().getEntity());
            customField.setValueDesc(tools.getFieldDescriptionsTools().readCustomFieldDesc(customFieldEAM.getENTITYCODEFIELD().getEntity(), customFieldEAM.getENTITYCODEFIELD().getCODEVALUE()));
        }
        //
        //
        //
        else if (customFieldEAM.getType().toUpperCase().equals("NUM") && customFieldEAM.getNUMBERFIELD() != null) {
            customField.setValue(decodeBigDecimal(tools.getDataTypeTools().decodeQuantity(customFieldEAM.getNUMBERFIELD())));
        }
        //
        //
        //
        else if (customFieldEAM.getType().toUpperCase().equals("CODE") && customFieldEAM.getCODEDESCFIELD() != null) {
            customField.setValue(customFieldEAM.getCODEDESCFIELD().getCODEVALUE());
            customField.setValueDesc(customFieldEAM.getCODEDESCFIELD().getDESCRIPTION());
        }
        //
        //
        //
        else if (customFieldEAM.getType().toUpperCase().equals("CHAR") && customFieldEAM.getTEXTFIELD() != null){
            customField.setValue(customFieldEAM.getTEXTFIELD());
        }
        //
        //
        //

        return customField;
    }

    // MIDDLE TIER CUSTOM FIELD -> INFOR CUSTOM FIELD
    public CUSTOMFIELD encodeEAMCustomField(CUSTOMFIELD customFieldEAM, CustomField customField)
            throws EAMException {
        //
        // DATE TIME
        //
        if (customFieldEAM.getType().toUpperCase().equals("DATI")) {
            if (customField.getValue() != null) {
                customFieldEAM.setDATETIMEFIELD(tools.getDataTypeTools().formatDate(customField.getValue(),
                        "Custom field '" + customFieldEAM.getPROPERTYLABEL() + "'"));
            }
        }
        //
        // DATE
        //
        if (customFieldEAM.getType().toUpperCase().equals("DATE")) {
            if (customField.getValue() != null) {
                customFieldEAM.setDATEFIELD(tools.getDataTypeTools().formatDate(customField.getValue(),
                        "Custom field '" + customFieldEAM.getPROPERTYLABEL() + "'"));
            }
        }
        //
        // ENTITY
        //
        if (customFieldEAM.getType().toUpperCase().equals("RENT")) {
            customFieldEAM.setENTITYCODEFIELD(new ENTITYCODEFIELD());
            if (customField.getValue() != null) {
                customFieldEAM.getENTITYCODEFIELD().setCODEVALUE(customField.getValue());
            } else {
                customFieldEAM.getENTITYCODEFIELD().setCODEVALUE("");
            }
        }
        //
        //
        //
        if (customFieldEAM.getType().toUpperCase().equals("NUM")) {
            if (customField.getValue() != null) {
                customFieldEAM.setNUMBERFIELD(tools.getDataTypeTools().encodeQuantity(encodeBigDecimal(customField.getValue(), "Custom field '" + customFieldEAM.getPROPERTYLABEL() + "'"),
                        "Custom field '" + customFieldEAM.getPROPERTYLABEL() + "'"));
            }
        }
        //
        //
        //
        if (customFieldEAM.getType().toUpperCase().equals("CODE") && customFieldEAM.getCODEDESCFIELD() != null) {
            customFieldEAM.setCODEDESCFIELD(new CODEDESCFIELD());
            if (customField.getValue() != null) {
                customFieldEAM.getCODEDESCFIELD().setCODEVALUE(customField.getValue());
            } else {
                customFieldEAM.getCODEDESCFIELD().setCODEVALUE("");
            }
        }
        //
        //
        //
        if (customFieldEAM.getType().toUpperCase().equals("CHAR")) {
            customFieldEAM.setTEXTFIELD(customField.getValue());
        }
        return customFieldEAM;
    }

    public void updateEAMCustomFields(USERDEFINEDAREA userdefinedarea, CustomField[] customFields)
            throws EAMException {
        if (userdefinedarea == null
                || userdefinedarea.getCUSTOMFIELD() == null
                || userdefinedarea.getCUSTOMFIELD().size() == 0) {
            return;
        }

        userdefinedarea.getCUSTOMFIELD().forEach(cf -> cf.setChanged("false"));

        if (customFields == null || customFields.length == 0) {
            return;
        }

        Map<String, List<CustomField>> wshubCustomFieldMap = Arrays.stream(customFields)
            .filter(cf -> cf != null && cf.getCode() != null)
            .collect(Collectors.groupingBy(CustomField::getCode, Collectors.toList()));

        userdefinedarea.getCUSTOMFIELD().removeIf(eamCustomField -> {
            List<CustomField> wshubCustomField = wshubCustomFieldMap.get(eamCustomField.getPROPERTYCODE());
            if (wshubCustomField == null || wshubCustomField.size() == 0) {
                return false;
            }
            final Optional<CustomField> cf =
                    wshubCustomField.stream().filter(customField -> customField.getEntityCode() == null ||
                            eamCustomField.getEntity().equals(customField.getEntityCode())).findFirst();
            return cf.isPresent()
                && (cf.get().getValue() == null || "".equals(cf.get().getValue()));
        });

        for (CUSTOMFIELD eamCustomField : userdefinedarea.getCUSTOMFIELD()) {
            final Optional<CustomField> wshubCustomField = wshubCustomFieldMap.getOrDefault(eamCustomField.getPROPERTYCODE(),
                    new ArrayList<>()).stream().filter(cf -> cf.getEntityCode() == null || cf.getEntityCode().equals(eamCustomField.getEntity())).findFirst();
            if (wshubCustomField.isPresent() && hasChangedCustomField(eamCustomField, wshubCustomField.get())) {
                encodeEAMCustomField(eamCustomField, wshubCustomField.get());
                eamCustomField.setChanged("true");
            }
        }
    }

    /**
     * To identify if the custom field really changed
     *
     * @param customFieldEAM
     *            The custom field from EAM (The one that was read)
     * @param customField
     *            The custom field comming to be updated
     * @return true if it was changed, false otherwise
     */
    private boolean hasChangedCustomField(CUSTOMFIELD customFieldEAM, CustomField customField) {
        // Check accorging with the type of custom field
        switch (customFieldEAM.getType().toUpperCase()) {

            case "RENT":
                // Compare different
                if (customFieldEAM.getENTITYCODEFIELD() != null)
                    return isDifferentValue(customFieldEAM.getENTITYCODEFIELD().getCODEVALUE(), customField.getValue());
                return isDifferentValue(null, customField.getValue());
            case "NUM":
                // Decode the quantity
                String quantity = decodeBigDecimal(decodeQuantity(customFieldEAM.getNUMBERFIELD()));
                // Compare
                return isDifferentValue(quantity, customField.getValue());

            case "CODE":
                // Compare different
                if (customFieldEAM.getCODEDESCFIELD() != null)
                    return isDifferentValue(customFieldEAM.getCODEDESCFIELD().getCODEVALUE(), customField.getValue());
                return isDifferentValue(null, customField.getValue());
            case "CHAR":
                // Compare different
                return isDifferentValue(customFieldEAM.getTEXTFIELD(), customField.getValue());

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

    public CustomField[] readEAMCustomFields(USERDEFINEDAREA userdefinedarea, EAMContext eamContext) {
        if (userdefinedarea == null || userdefinedarea.getCUSTOMFIELD() == null) {
            return new CustomField[0];
        }
        return userdefinedarea.getCUSTOMFIELD().stream().sorted(comparing(CUSTOMFIELD::getIndex)).map(cf -> decodeEAMCustomField(cf, eamContext)).toArray(CustomField[]::new);
    }

    public USERDEFINEDAREA getEAMCustomFields(EAMContext context, String entity, String eamClass)
            throws EAMException {
        CUSTOMFIELDREQ cfreq = new CUSTOMFIELDREQ();
        cfreq.setORGANIZATIONID(tools.getOrganization(context));

        cfreq.setCLASSID(new CLASSID_Type());
        cfreq.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
        cfreq.getCLASSID().setCLASSCODE(eamClass);

        cfreq.setENTITYNAME(entity);

        MP9501_GetCustomFields_001 getcustomfields = new MP9501_GetCustomFields_001();
        getcustomfields.setCUSTOMFIELDREQ(cfreq);

        MP9501_GetCustomFields_001_Result result =
            tools.performEAMOperation(context, eamws::getCustomFieldsOp, getcustomfields);
        return result.getUSERDEFINEDAREA();

    }

    public CustomField[] getWSHubCustomFields(EAMContext context, String entity, String eamClass)
            throws EAMException {
        return readEAMCustomFields(getEAMCustomFields(context, entity, eamClass), context);
    }

    public USERDEFINEDAREA getEAMCustomFields(
            EAMContext context,
            String previousClass,
            USERDEFINEDAREA previousCustomFields,
            String targetClass,
            String entityType)
            throws EAMException {

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

            USERDEFINEDAREA classCustomFields = getEAMCustomFields(context, entityType, newClass);

            // [2] handle case 11 and 12
            if(previousCustomFields == null) return classCustomFields;

            // [3] handle cases 2, 3, 5, 6, 8 and 9
            return merge(classCustomFields, previousCustomFields);
        }

        // we can now assume that targetClass is null

        // [4] handle case 1 and 7
        if(previousCustomFields != null) return previousCustomFields;

        // [5] handle case 4 and 10
        return getEAMCustomFields(context, entityType, "*");
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
