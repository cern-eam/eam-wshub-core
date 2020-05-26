package ch.cern.eam.wshub.core.services.userdefinedscreens.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedTableQueries;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedTableService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedTableValidator;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class UserDefinedTableServiceImpl implements UserDefinedTableService {

    enum DATA_TYPE {
        DATE, STRING, INTEGER, DECIMAL
    }


    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;
    private EntityManager entityManager;

    public UserDefinedTableServiceImpl(ApplicationData applicationData, Tools tools,
                                       InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        if (tools.isDatabaseConnectionConfigured()) {
            this.entityManager = tools.getEntityManager();
        }
    }

    @Override
    public String createUserDefinedTableRows(InforContext context, String tableName, List<UDTRow> rows)
            throws InforException {
        tools.demandDatabaseConnection();
        UserDefinedTableValidator.validateOperation(tableName, rows);
        entityManager.joinTransaction();
        for (UDTRow row: rows) {
            Map<String, Object> parameters = getUDTRowAsMap(row);
            parameters.putAll(getDefaultInsertColumns(context.getCredentials().getUsername()));
            UserDefinedTableQueries.executeInsertQuery(tableName.toUpperCase(), parameters, entityManager);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> readUserDefinedTableRows(InforContext context,
                        String tableName, UDTRow filters, List<String> fieldsToRead) throws InforException {
        tools.demandDatabaseConnection();
        UserDefinedTableValidator.validateOperation(tableName, null, filters);
        UserDefinedTableValidator.validateKeyList(fieldsToRead, false);
        Map<String, Object> parameters = getUDTRowAsMap(filters);
        Map<String, ?> columnTypes =
                UserDefinedTableQueries.getColumnTypes(tableName.toUpperCase(), entityManager);
        if (fieldsToRead.size() == 0) {
            fieldsToRead = new ArrayList<>(columnTypes.keySet());
        }
        Long maxRows = applicationData.getQueryMaxNumberOfRows();
        List<Map<String, Object>> maps = UserDefinedTableQueries.executeReadQuery(tableName.toUpperCase(),
                parameters, fieldsToRead, maxRows, entityManager);
        return maps;
    }

    @Override
    public int updateUserDefinedTableRows(InforContext context, String tableName, UDTRow fieldsToUpdate,
                                             UDTRow filters) throws InforException {
        tools.demandDatabaseConnection();
        entityManager.joinTransaction();
        UserDefinedTableValidator.validateOperation(tableName, fieldsToUpdate, filters);
        Map<String, Object> updateMapMap = getUDTRowAsMap(fieldsToUpdate);
        Map<String, Object> whereMap = getUDTRowAsMap(filters);
        updateMapMap.putAll(getDefaultUpdateColumns(context.getCredentials().getUsername()));
        return UserDefinedTableQueries.executeUpdateQuery(tableName, updateMapMap, whereMap,
                entityManager);

    }

    @Override
    public int deleteUserDefinedTableRows(InforContext context, String tableName, UDTRow filters) throws InforException {
        tools.demandDatabaseConnection();
        entityManager.joinTransaction();
        UserDefinedTableValidator.validateOperation(tableName, null, filters);
        Map<String, Object> filterMap = getUDTRowAsMap(filters);
        return UserDefinedTableQueries.executeDeleteQuery(tableName, filterMap, entityManager);
    }

    public List<UDTRow> getMapsAsUDTRows(String tableName, List<Map<String, Object>> mapRows) throws InforException {
        List<UDTRow> udtRowList = new ArrayList<>();
        for(Map<String, Object> row: mapRows) {
            UDTRow udtRow = getMapAsUDTRow(tableName, row);
            udtRowList.add(udtRow);
        }
        return udtRowList;
    }

    public UDTRow getMapAsUDTRow(String tableName, Map<String, Object> mapRow) throws InforException {
        Map<String, Class<?>> columnTypes = UserDefinedTableQueries.getColumnTypes(tableName, entityManager);
        UDTRow newRow = new UDTRow();
        for (Map.Entry<String, Object> entry: mapRow.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            Optional<Map.Entry<String, Class<?>>> first = columnTypes.entrySet().stream()
                    .filter(type -> type.getKey().equals(entry.getKey()))
                    .findFirst();

            Class<?> clazz = first.isPresent() ?
                    first.get().getValue()
                    : String.class
                    ;

            if (Date.class.equals(clazz)) {
                newRow.addDate(key, value != null ? (Date) value : null);
            }
            if (BigInteger.class.equals(clazz)) {
                newRow.addInteger(key, value != null ? (BigInteger) value : null);
            }
            if (BigDecimal.class.equals(clazz)) {
                newRow.addDecimal(key, value != null ? (BigDecimal) value : null);
            }
            if (String.class.equals(clazz)) {
                newRow.addString(key, value != null ? (String) value : null);
            }
        }
        return newRow;
    }

    public List<Map<String, Object>> getUDTRowsAsMaps(List<UDTRow> rows) {
        List<Map<String, Object>> rowList = new ArrayList<>();
        for(UDTRow row: rows) {
            Map<String, Object> udtRowAsMap = getUDTRowAsMap(row);
            rowList.add(udtRowAsMap);
        }
        return rowList;
    }

    public Map<String, Object> getUDTRowAsMap(UDTRow row) {
        //Use Map implementation that guarantees deterministic order on keySet
        Map<String, Object> mapa = new LinkedHashMap<>();
        mapa.putAll(upperMap(row.getDates()));
        mapa.putAll(upperMap(row.getStrings()));
        mapa.putAll(upperMap(row.getIntegers()));
        mapa.putAll(upperMap(row.getDecimals()));
        return mapa;
    }

    // HELPERS

    private static Map<String, Object> getDefaultInsertColumns(String username) {
        Date currentDate = new Date();

        Map<String, Object> mapa = new TreeMap<>();
        mapa.put("CREATEDBY", username);
        mapa.put("CREATED", currentDate);
        mapa.put("UPDATEDBY", null);
        mapa.put("UPDATED", null);
        mapa.put("UPDATECOUNT", new BigInteger("0"));
        mapa.put("LASTSAVED", currentDate.clone()); // cloned so that editing last saved date does not alter created one
        return mapa;
    }

    private static Map<String, Object> getDefaultUpdateColumns(String username) {
        Map<String, Object> mapa = new TreeMap<>();
        mapa.put("UPDATEDBY", username);
        mapa.put("UPDATED", new Date());
        //UPDATEDCOUNT is incremented through a trigger
        return mapa;
    }

    private static <T extends Object> Map<String, T> upperMap(Map<String, T> mapa) {
        if (mapa == null) {
            return new HashMap<>();
        }

        // TreeMap to keep natural order (alphabetical)
        Map<String, T> map = new TreeMap<>();
        for(Map.Entry<String, T> entry: mapa.entrySet()) {
            T value = null;
            try {
                value = entry.getValue();
            } catch (Throwable t) {}
            map.put(entry.getKey().toUpperCase(), value);
        }
        return map;
    }
}
