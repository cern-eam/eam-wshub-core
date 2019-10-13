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
import static java.util.Comparator.comparing;

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
        else if (customFieldInfor.getType().toUpperCase().equals("RENT")
                && customFieldInfor.getENTITYCODEFIELD() != null) {
            customField.setValue(customFieldInfor.getENTITYCODEFIELD().getCODEVALUE());
            customField.setRentCodeValue(customFieldInfor.getENTITYCODEFIELD().getEntity());
            customField.setValueDesc(tools.getFieldDescriptionsTools().readCustomFieldDesc(customFieldInfor.getENTITYCODEFIELD().getEntity(), customFieldInfor.getENTITYCODEFIELD().getCODEVALUE()));
        }
        //
        //
        //
        else if (customFieldInfor.getType().toUpperCase().equals("NUM") && customFieldInfor.getNUMBERFIELD() != null) {
            customField.setValue(tools.getDataTypeTools().decodeQuantity(customFieldInfor.getNUMBERFIELD()));
        }
        //
        //
        //
        else if (customFieldInfor.getType().toUpperCase().equals("CODE")
                && customFieldInfor.getCODEDESCFIELD() != null) {
            customField.setValue(customFieldInfor.getCODEDESCFIELD().getCODEVALUE());
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
        else {
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
                customFieldInfor.setNUMBERFIELD(tools.getDataTypeTools().encodeQuantity(customField.getValue(),
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
        if (userdefinedarea != null && userdefinedarea.getCUSTOMFIELD() != null
                && userdefinedarea.getCUSTOMFIELD().size() > 0) {
            for (CUSTOMFIELD customFieldInfor : userdefinedarea.getCUSTOMFIELD()) {
                customFieldInfor.setChanged("false");
            }
        }

        if (customFields != null && customFields.length > 0 && userdefinedarea != null
                && userdefinedarea.getCUSTOMFIELD() != null && userdefinedarea.getCUSTOMFIELD().size() > 0) {
            for (CustomField customField : customFields) {
                for (CUSTOMFIELD customFieldInfor : userdefinedarea.getCUSTOMFIELD()) {
                    if (customFieldInfor.getPROPERTYCODE().equals(customField.getCode())
                            && hasChangedCustomField(customFieldInfor, customField)) {
                        encodeInforCustomField(customFieldInfor, customField);
                        customFieldInfor.setChanged("true");
                        break;
                    }
                }
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
                String quantity = tools.getDataTypeTools().decodeQuantity(customFieldInfor.getNUMBERFIELD());
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

        MP9501_GetCustomFields_001_Result result;

        if (context.getCredentials() != null) {
            result = inforws.getCustomFieldsOp(getcustomfields, tools.getOrganizationCode(context),
                    tools.createSecurityHeader(context), "TERMINATE", null, null,
                    tools.getTenant(context));
        } else {
            result = inforws.getCustomFieldsOp(getcustomfields, tools.getOrganizationCode(context), null, null,
                    new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
        }

        return result.getUSERDEFINEDAREA();

    }

    public CustomField[] getWSHubCustomFields(InforContext context, String entity, String inforClass)
            throws InforException {
        CUSTOMFIELDREQ cfreq = new CUSTOMFIELDREQ();
        cfreq.setORGANIZATIONID(tools.getOrganization(context));

        if (inforClass != null) {
            cfreq.setCLASSID(new CLASSID_Type());
            cfreq.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
            cfreq.getCLASSID().setCLASSCODE(inforClass.toUpperCase());
        }

        if (entity != null) {
            cfreq.setENTITYNAME(entity.toUpperCase());
        }

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
}
