package ch.cern.eam.wshub.core.services.userdefinedscreens.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedTableService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.services.workorders.entities.InforCase;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.ExceptionInfo;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserDefinedTableServiceImpl implements UserDefinedTableService {

    enum DATA_TYPE {
        DATE, STRING, INTEGER, DECIMAL
    }

    private static final List<String> RESERVED_FIELD_NAMES = Arrays.asList(
            "CREATED", "CREATEDBY", "UPDATED", "UPDATEDBY", "UPDATECOUNT"
    );

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public UserDefinedTableServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    @Override
    public String createUserDefinedTableRows(InforContext context, String tableName, List<UDTRow> rows)
            throws InforException {
        validateOperation(tableName, rows);
        EntityManager entityManager = tools.getEntityManager();
        for (UDTRow row: rows) {
            Map<String, Object> parameters = getParameters(row, context.getCredentials().getUsername());
            executeQuery(tableName.toUpperCase(), parameters, entityManager);
        }
        return null;
    }

    private static Map<String, Object> getParameters(UDTRow row, String username) {
        //Use Map implementation that guarantees deterministic order on keySet
        Map<String, Object> mapa = new LinkedHashMap<>();
        mapa.putAll(upperMap(row.getDates()));
        mapa.putAll(upperMap(row.getStrings()));
        mapa.putAll(upperMap(row.getIntegers()));
        mapa.putAll(upperMap(row.getDecimals()));
        mapa.putAll(getDefaultColumns(username));
        return mapa;
    }

    private static Map<String, Object> getDefaultColumns(String username) {
        Map<String, Object> mapa = new TreeMap<>();
        mapa.put("CREATEDBY", username);
        mapa.put("CREATED", new Date());
        mapa.put("UPDATEDBY", null);
        mapa.put("UPDATED", null);
        mapa.put("UPDATECOUNT", new BigInteger("0"));
        return mapa;
    }

    private static <T extends Object> Map<String, T> upperMap(Map<String, T> mapa) {
        if (mapa == null) {
            return new HashMap<>();
        }

        Map<String, T> map = new TreeMap<>();
        for(Map.Entry<String, T> entry: mapa.entrySet()) {
            T value = null;
            try {
                value = entry.getValue();
            } catch (Throwable t) {}
            map.put(entry.getKey().toUpperCase(), value);
        }
//		// Keep keySet ordered
//		AbstractMap<String, T> collect = mapa.entrySet().stream().collect(Collectors.toMap(
//				e -> e.getKey().toUpperCase(),
//				Map.Entry::getValue, // Value cannot be null...
//				(a, b) -> a, //TODO throw exception?
//				TreeMap::new
//			)
//		);
        return map;
    }

    private <T> void executeQuery(String tableName, Map<String, T> map, EntityManager em) {
        //Create list to guarantee ordering
        String query = getQuery(tableName, map);
        //em.joinTransaction();
        Query nativeQuery = em.createNativeQuery(query);
        map.keySet().stream().filter(s -> map.get(s) != null).forEach(column -> nativeQuery.setParameter(column, map.get(column)));
        nativeQuery.executeUpdate();


    }

    private <T> String getQuery(String tableName, Map<String, T> map) {
        String columnNames = map.keySet().stream()
                .collect(
                        StringBuilder::new,
                        (builder, n) -> builder.append(", \"").append(n).append("\""),
                        StringBuilder::append
                )
                .substring(2);

        String sbArguments = map.keySet().stream()
                .collect(
                        StringBuilder::new,
                        (builder, n) -> builder.append(", ").append(map.get(n) == null ? "NULL" : ":" + n),
                        StringBuilder::append
                )
                .substring(2);

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(tableName);
        query.append(" (").append(columnNames).append(")");
        query.append(" VALUES (");
        query.append(sbArguments);
        query.append(")");
        return query.toString();
    }

    //Validators

    private static void validateOperation(String tableName, List<UDTRow> rows) throws InforException {
        validateTableName(tableName);
        for(UDTRow row: rows) {
            validateKeyList(row.getAllKeys());
        }
    }

    private static final String INSERT_QUERY = "INSERT INTO {tableName} ({columnNames}) VALUES ({columnValues})";

    private static void validateKeyList(List<String> keyList) throws InforException {
        for (String key: keyList) {
            validateColumnName(key);
            Set<String> hashSet = keyList.stream().map(String::toUpperCase).collect(Collectors.toSet());
            // The same column name can appear twice in different maps, or different casings
            if (keyList.size() != hashSet.size()){
                String repeaters = keyList.stream()
                        .map(String::toUpperCase)
                        .filter(hashSet::contains)
                        .collect(Collectors.joining(","))
                        ;
                throw generateInforException( "columnNames", "Repeated column names: " + repeaters);
            }
            //Reserved column names that shall not be manipulated by the user
            if (hashSet.stream().anyMatch(RESERVED_FIELD_NAMES::contains)) {
                throw generateInforException( "columnNames", "Reserved field names cannot be used: "
                        + hashSet.stream().filter(RESERVED_FIELD_NAMES::contains)
                        .collect(Collectors.joining(",")));
            }
            //TODO maybe check if the field name is part of the table?
        }
    }


    private static InforException generateInforException(String field, String errorMessage) {
        return new InforException(
                errorMessage,
                null,
                Collections.singleton(new ExceptionInfo(field, errorMessage))
                        .toArray(new ExceptionInfo[0])
        );
    }

    private static void validateTableName(String name) throws InforException {
        if (name == null) {
            throw generateInforException("key", "Parameter name cannot be null");
        }
        // Valid u5 table names
        if (!Pattern.matches("^U5[_A-Z0-9]+$", name)) {
            String errorMessage = "Invalid Parameter name: \"" + name + '"';
            throw generateInforException(name, errorMessage);
        }
    }

    private static void validateColumnName(String name) throws InforException {
        if (name == null) {
            throw generateInforException("key", "Parameter name cannot be null");
        }
        // Valid u5 table names
        if (!Pattern.matches("^[_A-Za-z0-9]+$", name)) {
            String errorMessage = "Invalid Parameter name: \"" + name + '"';
            throw generateInforException(name, errorMessage);
        }
    }

    private static String replace(String var, String value) {
        //TODO
        return var != null ?
               var.replace("{" + var + "}", value)
                : null
                ;
    }
}
