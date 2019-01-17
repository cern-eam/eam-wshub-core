package ch.cern.eam.wshub.core.services.grids.customfields;

import ch.cern.eam.wshub.core.services.grids.impl.DataField;
import ch.cern.eam.wshub.core.services.grids.impl.DataType;
import ch.cern.eam.wshub.core.services.grids.entities.DataspyField;
import ch.cern.eam.wshub.core.services.grids.entities.GridField;
import ch.cern.eam.wshub.core.tools.Tools;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handles the custom fields when making grid requests.
 *
 * @author pkulig
 */
public class GridCustomFieldHandler implements Serializable {

    private Tools tools;

    public GridCustomFieldHandler(Tools tools) {
        this.tools = tools;
    }

    /**
     * Infor stores different types of custom fields in different
     * fields in R5PROPERTYVALUES
     */
    public static String NUMERICAL_VALUE = "PRV_NVALUE";
    public static String DATE_VALUE = "PRV_DVALUE";
    public static String VARCHAR_VALUE = "PRV_VALUE";

    private String dataspyID;

    private List<DataspyCustomField> customFields;

    /**
     * Initializes the handler, to fetch custom fields for a given dataspy.
     * This dataspy is then in the state of the handler to avoid fetching
     * the custom fields multiple times.
     *
     * @param dataspyID
     */
    public void initializeForDataspy(String dataspyID) {
        this.dataspyID = dataspyID;

        fetchCustomFields(dataspyID);

        // figure out what the proper property name is for each custom field from the tagname
        // the tagname is in form:
        // c_<property name>_<rentity>
        for (DataspyCustomField dataspyCustomField : customFields) {
            dataspyCustomField.setPropertyName(dataspyCustomField.getTagName().split("_")[1].toUpperCase());
            dataspyCustomField.setRentity(dataspyCustomField.getTagName().split("_")[2].toUpperCase());
        }
    }

    /**
     * Fetches all the custom fields for a given dataspy and
     * caches them in this bean.
     */
    private void fetchCustomFields(String dataspyID) {
        EntityManager em = tools.getEntityManager();

        List<Object[]> results = em.createNamedQuery(DataspyCustomField.GET_CUSTOM_FIELDS_FOR_DATASPY)
                .setParameter("dataspyID", dataspyID)
                .getResultList();

        customFields = parseResults(results);
    }

    private List<DataspyCustomField> parseResults(List<Object[]> results) {
        return results.stream().map(result -> {
            DataspyCustomField dataspyCustomField = new DataspyCustomField();

            dataspyCustomField.setDataspyID(String.valueOf(result[0]));
            dataspyCustomField.setTagName(String.valueOf(result[1]));
            dataspyCustomField.setWidth(String.valueOf(result[2]));
            dataspyCustomField.setOrder(String.valueOf(result[3]));
            dataspyCustomField.setViewType(String.valueOf(result[4]));
            dataspyCustomField.setDescription(String.valueOf(result[5]));
            dataspyCustomField.setPropertyType(String.valueOf(result[6]));

            return dataspyCustomField;

        }).collect(Collectors.toList());
    }

    /**
     * Converts custom fields (DataspyCustomField) to
     * normal dataspy fields (DataspyField)
     */
    public List<DataspyField> getCustomFieldsAsDataspyFields() {
        return customFields.stream().map(dataspyCustomField -> {
            DataspyField dataspyField = new DataspyField();

            dataspyField.setId(String.valueOf(-Integer.valueOf(dataspyCustomField.getOrder())));
            dataspyField.setDataspy(getDataspyID());
            dataspyField.setOrder(Integer.valueOf(dataspyCustomField.getOrder()));
            dataspyField.setDataType("VARCHAR");
            dataspyField.setTagName(dataspyCustomField.getTagName());

            return dataspyField;
        }).sorted(Comparator.comparingInt(dataspyCustomField -> dataspyCustomField.getOrder())).collect(Collectors.toList());
    }

    /**
     * In the JPAGrids class we fetch all the fields that need to be selected, as a list.
     * This method attaches the names of the of the custom fields to this list.
     */
    public List<String> attachCustomFields(List<String> sqlStatements) {
        List<String> newSqlStatements = new ArrayList<>(sqlStatements);

        final int[] index = {0};
        customFields = customFields.stream().sorted(Comparator.comparingInt(c -> Integer.valueOf(c.getOrder()))).collect(Collectors.toList());

        /**
         * Per each custom field there is a join called PROP_<property name>_JOIN
         * and aliased like C<index> to avoid JPA complaining about
         * duplicate aliases
         */
        newSqlStatements.addAll(customFields.stream()
                .map(customField -> getSourceName(customField.getRentity(), getJoinName(customField.getPropertyName()), getField(customField.getPropertyType())) + " AS C" + (index[0]++))
                .collect(Collectors.toList()));

        return newSqlStatements;
    }

    /**
     * Adds the join clause to the SQL used to fetch the grid.
     */
    public String addCustomFieldsJoinClause(String sqlStatementFrom) {
        String joinClause = customFields.stream().map(customField -> {
            String primaryKey = null;
            String organizationSuffix = "";

            // to make the join on the r5propertyvalues we need to know the primary key
            // on which the join is made (PRV_CODE)
            // so far only those entities are supported
            if (customField.getRentity().equals("OBJ")) {
                primaryKey = "OBJ_CODE";
                organizationSuffix = "|| '#*'";
            }

            if (customField.getRentity().equals("EVNT")) {
                primaryKey = "EVT_CODE";
            }

            if (customField.getRentity().equals("PART")) {
                primaryKey = "PAR_CODE";
                organizationSuffix = "|| '#*'";
            }

            String joinClausePart = null;

            /**
             * Per each custom field there are two joins: one on primary key and one on the category
             * Don't know why but this is the way Infor does this
             */
            if (!customField.getRentity().equals("PART")) {
                joinClausePart = "LEFT OUTER JOIN R5PROPERTYVALUES " + getJoinName(customField.getPropertyName()) +
                        " ON " + primaryKey + organizationSuffix + "=" + getJoinName(customField.getPropertyName()) + ".PRV_CODE " +
                        "AND " + getJoinName(customField.getPropertyName()) + ".PRV_RENTITY='" + customField.getRentity() + "' " +
                        "AND " + getJoinName(customField.getPropertyName()) + ".PRV_PROPERTY='" + customField.getPropertyName() + "' ";

                if (customField.getRentity().equals("OBJ")) {
                    joinClausePart += "LEFT OUTER JOIN R5PROPERTYVALUES " + getJoinName(customField.getPropertyName()) + "2" +
                            " ON " + "OBJ_CATEGORY=" + getJoinName(customField.getPropertyName()) + "2.PRV_CODE " +
                            "AND " + getJoinName(customField.getPropertyName()) + "2.PRV_RENTITY='" + customField.getRentity() + "' " +
                            "AND " + getJoinName(customField.getPropertyName()) + "2.PRV_PROPERTY='" + customField.getPropertyName() + "' ";
                }
            }

            /**
             * In case of parts, we cannot make the normal LEFT OUTER JOIN
             * because Infor uses the Oracle deprecated join syntax with the plus (+)
             * and the two cannot coexist.
             * In this case we do this kind of join.
             */
            if (customField.getRentity().equals("PART")) {
                joinClausePart = ", R5PROPERTYVALUES " + getJoinName(customField.getPropertyName()) + " ";
            }

            return joinClausePart;
        }).collect(Collectors.joining(" "));

        return sqlStatementFrom + joinClause;
    }

    public GridField[] getCustomFieldsAsGridFields(String dataspyID) {
        if (this.dataspyID == null || !getDataspyID().equals(dataspyID)) {
            fetchCustomFields(dataspyID);
        }

        return mapCustomFieldsToGridFields(customFields);
    }

    /**
     * Maps custom fields (DataspyCustomField) to
     * grid fields (Grid Field)
     */
    private GridField[] mapCustomFieldsToGridFields(List<DataspyCustomField> customFields) {
        return customFields.stream().map(customField -> {
            GridField gridField = new GridField();

            gridField.setId(String.valueOf(-Integer.valueOf(customField.getOrder())));
            gridField.setName(customField.getTagName());
            gridField.setLabel(customField.getDescription());
            gridField.setWidth(customField.getWidth());
            gridField.setDataType("VARCHAR");
            gridField.setDdSpyId(customField.getDataspyID());
            gridField.setOrder(customField.getOrder());

            return gridField;
        }).collect(Collectors.toList()).toArray(new GridField[customFields.size()]);
    }

    /**
     * Fetches the custom fields possible for a grid.
     * To do this it checks all custom fields belonging
     * to dataspies belonging to a grid. Also fetches the type
     * of the custom fields.
     */
    public Map<String, DataField> getCustomFieldsForGrid(String gridID) {
        EntityManager em = tools.getEntityManager();

        String queryString = "SELECT DISTINCT " +
                "  DCF_TAGNAME, " +
                "  PRO_TYPE " +
                "FROM R5DDCUSTOMFIELDS " +
                "JOIN R5DDDATASPY ON DCF_DDSPYID = DDS_DDSPYID " +
                "JOIN R5PROPERTIES ON UPPER(SUBSTR(DCF_TAGNAME, 3, INSTR(DCF_TAGNAME, '_', 1, 2) - 3)) = PRO_CODE " +
                "WHERE DDS_GRIDID = :gridID";

        List<Object[]> results = em.createNativeQuery(queryString).setParameter("gridID", gridID).getResultList();

        return results.stream().map(result -> {
            String tagName = String.valueOf(result[0]);
            String propertyName = tagName.split("_")[1].toUpperCase();
            // get the rentity from last portion of the tagname
            String rentity = String.valueOf(tagName.split("_")[tagName.split("_").length - 1]).toUpperCase();

            String dataTypeString = String.valueOf(result[1]);
            DataType dataType = inferDataTypeFromPropertyType(dataTypeString);

            return new DataField(String.valueOf(result[0]), getSourceName(rentity, getJoinName(propertyName), getField(dataTypeString)), dataType, false);
        }).collect(Collectors.toMap(dataField -> dataField.getTagName(), dataField -> dataField));
    }

    private DataType inferDataTypeFromPropertyType(String dataTypeString) {
        switch (dataTypeString) {
            case "CHAR": {
                return DataType.VARCHAR;
            }

            case "NUM": {
                return DataType.NUMBER;
            }

            case "CODE": {
                return DataType.VARCHAR;
            }

            case "DATE": {
                return DataType.DATE;
            }

            case "DATI": {
                return DataType.DATETIME;
            }

            default: {
                return DataType.VARCHAR;
            }
        }
    }

    /**
     * For parts we have to use the deprecated join syntax with the plus (+)
     * To make the join we add ANDs to the where clause.
     */
    public String getCustomFieldJoinConditions() {
        return customFields.stream().filter(customField -> customField.getRentity().equals("PART")).map(customField ->
             "AND PAR_CODE || '#*' = " + getJoinName(customField.getPropertyName()) + ".PRV_CODE(+)" +
                    " AND (" + getJoinName(customField.getPropertyName()) + ".PRV_RENTITY = 'PART' OR " + getJoinName(customField.getPropertyName()) + ".PRV_RENTITY IS NULL) "+
                    " AND (" + getJoinName(customField.getPropertyName()) + ".PRV_PROPERTY = '" + customField.getPropertyName() + "' OR " + getJoinName(customField.getPropertyName()) + ".PRV_PROPERTY IS NULL) "
        ).collect(Collectors.joining(" "));
    }

    /**
     * Gets the expression that is in the SELECT clause for a custom field.
     * For equipment, per each custom fields there are two joins: the first one is preferred,
     * if the first join returns null, the second join is utilised.
     */
    private String getSourceName(String rentity, String joinName, String field) {
        if (rentity.equals("OBJ")) {
            return "NVL(" + joinName + "." + field + ", " + joinName + "2." + field + ")";
        }
        else {
            return joinName + "." + field;
        }
    }

    private String getJoinName(String property) {
        return "PROP_" + property + "_JOIN";
    }

    private String getField(String propertyType) {
        if (propertyType.equals("NUM")) {
            return NUMERICAL_VALUE;
        }
        if (propertyType.equals("DATE") || propertyType.equals("DATI")) {
            return DATE_VALUE;
        }
        else {
            return VARCHAR_VALUE;
        }
    }

    public String getDataspyID() {
        return this.dataspyID;
    }
}
