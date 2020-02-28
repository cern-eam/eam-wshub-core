package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.tools.InforException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserDefinedTableQueries {

    private static final Map<String, Class<?>> DATA_TYPE_CLASS_MAP = new HashMap<>();

    static {
        DATA_TYPE_CLASS_MAP.put("VARCHAR2", String.class);
        DATA_TYPE_CLASS_MAP.put("NUMBER", BigInteger.class);
        DATA_TYPE_CLASS_MAP.put("DECIMAL", BigDecimal.class);
        DATA_TYPE_CLASS_MAP.put("DATE", Date.class);
        DATA_TYPE_CLASS_MAP.put("CLOB", String.class);
    }

    public static <T> void executeInsertQuery(String tableName, Map<String, T> map, EntityManager em) throws InforException {
        //Create list to guarantee ordering
        String query = getInsertQuery(tableName, map);
        try {
            Query nativeQuery = em.createNativeQuery(query);
            map.keySet().stream().filter(s -> map.get(s) != null).forEach(
                    column -> nativeQuery.setParameter(column, map.get(column))
            );
            nativeQuery.executeUpdate();
        } catch (PersistenceException e) {
            throw UserDefinedTableValidator.generateInforException("", e.getMessage());
        }
    }

    private static <T> String getInsertQuery(String tableName, Map<String, T> map) {
        List<String> orderedColumnNames = new ArrayList<>(map.keySet());

        String columnNames = getColumnNames(orderedColumnNames);
        String sbArguments = orderedColumnNames.stream()
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

    public static <T> List<Map<String, Object>> executeReadQuery(String tableName, Map<String, T> whereFiltersMap,
                                             List<String> fieldsToRead, Long maxRows, EntityManager em)
            throws InforException {
        //Create list to guarantee ordering
        String query = getReadQuery(tableName, whereFiltersMap, fieldsToRead, maxRows);
        try {
            Query nativeQuery = createQuery(query, new HashMap<>(), whereFiltersMap, em);
            List<Object[]> resultList = nativeQuery.getResultList();
            List<Map<String, Object>> collect = resultList.stream().map(s -> {
                        Map<String, Object> map = new LinkedHashMap<>();
                        for(int i = 0; i < fieldsToRead.size(); i++) {
                            map.put(fieldsToRead.get(i).toUpperCase(), s[i]);
                        }
                        return map;
                    }
                )
                .collect(Collectors.toList());
            Map<String, Class<?>> columnTypes = getColumnTypes(tableName, em);
            List<Map<String, Object>> lista = new ArrayList<>();
            for (Map<String, Object> s: collect) {
                Map<String, Object> stringObjectMap = castObjects(s, columnTypes);
                lista.add(stringObjectMap);
            }
            return lista;
        } catch (PersistenceException e) {
            //String msg, Throwable cause, ExceptionInfo[] details
            throw UserDefinedTableValidator.generateInforException("", e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }

    private static <T, U> U castType(T entity, Class<U> clazz) throws InforException {
        try {
            if (entity instanceof Clob) {
                Reader r = ((Clob) entity).getCharacterStream();
                StringBuffer buffer = new StringBuffer();
                int ch;
                while ((ch = r.read())!=-1) {
                    buffer.append(""+(char)ch);
                }
                return (U) buffer.toString();
            }
            if (entity instanceof Timestamp) {
                return (U) new Date(((Timestamp) entity).getTime());
            }
            if (entity instanceof BigDecimal && clazz.equals(BigInteger.class)) {
                //&& clazz.getClass().equals(BigInteger.class)
                return (U) new BigInteger(String.valueOf((entity)));
            }
            return (U) entity;
        } catch (Exception e) {
            throw UserDefinedTableValidator.generateInforException("", "Cannot cast "+ entity + " to " + clazz.getName());
        }
    }


    private static final String GET_TABLE_TYPES = "SELECT column_name, DECODE(data_type, 'NUMBER', CASE WHEN DATA_SCALE = 0 THEN 'NUMBER' ELSE 'DECIMAL' END, data_type) AS datatype " +
            "FROM user_tab_columns " +
            "WHERE table_name = UPPER(:tableName)"
            ;

    public static Map<String, Class<?>> getColumnTypes(String tableName, EntityManager em)
            throws InforException {
        try {
            Query nativeQuery = em.createNativeQuery(GET_TABLE_TYPES);
            nativeQuery.setParameter("tableName", tableName);
            List<Object[]> resultList = nativeQuery.getResultList();
            Map<String, Class<?>> classMap = resultList.stream().collect(
                    Collectors.toMap(
                            s -> "" + s[0],
                            s -> DATA_TYPE_CLASS_MAP.computeIfAbsent("" + s[1], (key) -> String.class)
                    )
            );
            return classMap;
        } catch (PersistenceException e) {
            //String msg, Throwable cause, ExceptionInfo[] details
            throw UserDefinedTableValidator.generateInforException("", e.getMessage());
        }
    }


    private static Map<String, Object> castObjects(Map<String, Object> map, Map<String, Class<?>> classMap) throws InforException {
        HashMap<String, Object> collect = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry: map.entrySet()) {
            Object o = castType(entry.getValue(), classMap.get(entry.getKey()));
            collect.put(entry.getKey(), o);
        }
        return collect;
    }

    private static <T> String getReadQuery(String tableName, Map<String, T> whereFilters, List<String> fieldsToRead,
                                           Long maxRows) {
        String filters = getWhereFilters(whereFilters);

        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        query.append(getColumnNames(fieldsToRead.stream().map(String::toUpperCase).collect(Collectors.toList())));
        query.append(" FROM ");
        query.append(tableName);
        query.append(" WHERE ( 1=1").append(filters).append(" )");
        if (maxRows != null) {
            query.append(" AND ROWNUM < " + maxRows);
        }
        return query.toString();
    }

    public static <T> int executeUpdateQuery(String tableName, Map<String, T> updateColumns,
                                              Map<String, T> whereFilters, EntityManager em)
            throws InforException {
        //Create list to guarantee ordering
        String query = getUpdateQuery(tableName, updateColumns, whereFilters);
        try {
            Query nativeQuery = createQuery(query, updateColumns, whereFilters, em);
            return nativeQuery.executeUpdate();
        } catch (PersistenceException e) {
            //String msg, Throwable cause, ExceptionInfo[] details
            throw UserDefinedTableValidator.generateInforException("", e.getMessage());
        }
    }

    private static <T> String getUpdateQuery(String tableName, Map<String, T> updateColumns,
                                             Map<String, T> whereFilters) {

        Function<String, String> updateArgumentsSupplier = getParameter(updateColumns,
                UserDefinedTableQueries::getFilterParameter);

        String update = updateColumns.keySet().stream()
                .map(getParameter(updateColumns, UserDefinedTableQueries::getSetParameter))
                .collect(Collectors.joining())
                .substring(2)
                ;

        String filters = getWhereFilters(whereFilters);

        StringBuilder query = new StringBuilder();
        query.append("UPDATE ");
        query.append(tableName);
        query.append(" SET ");
        query.append(update);
        query.append(" WHERE ( 1=1").append(filters).append(")");
        return query.toString();
    }

    public static <T> int executeDeleteQuery(String tableName, Map<String, T> whereFilters, EntityManager em)
            throws InforException {
        //Create list to guarantee ordering
        String query = getDeleteQuery(tableName, whereFilters);
        try {
            Query nativeQuery = createQuery(query, new HashMap<>(), whereFilters, em);
            return nativeQuery.executeUpdate();
        } catch (PersistenceException e) {
            //String msg, Throwable cause, ExceptionInfo[] details
            throw UserDefinedTableValidator.generateInforException("", e.getMessage());
        }
    }

    private static String getColumnNames(List<String> columnNameList) {
        String columnNames = columnNameList.stream()
                .collect(
                        StringBuilder::new,
                        (builder, n) -> builder.append(", \"").append(n).append("\""),
                        StringBuilder::append
                )
                .substring(2);
        return columnNames;
    }

    private static <T> Query createQuery(String query, Map<String, T> updateParameters, Map<String, T> filterParameters,
                                        EntityManager em) {
        Query nativeQuery = em.createNativeQuery(query);
        updateParameters.keySet().stream().filter(s -> updateParameters.get(s) != null)
                .forEach(column -> nativeQuery.setParameter(getSetParameterName(column),
                        updateParameters.get(column)));
        filterParameters.keySet().stream().filter(s -> filterParameters.get(s) != null)
                .forEach(column -> nativeQuery.setParameter(getFilterParameterName(column),
                        filterParameters.get(column)));
        return nativeQuery;

    }

    private static <T> String getDeleteQuery(String tableName, Map<String, T> whereFilters) {
        String whereString = getWhereFilters(whereFilters);
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ");
        query.append(tableName);
        query.append(" WHERE ( 1=1").append(whereString).append(")");
        return query.toString();
    }

    private static <T> String getWhereFilters(Map<String, T> whereFilters) {
        return whereFilters.keySet().stream()
                .map(getParameter(whereFilters,
                        UserDefinedTableQueries::getFilterParameter))
                .collect(Collectors.joining())
        ;
    }

    private static <T, R extends String> String getFilterParameter(R columnName, T value) {
        return " AND \"" + columnName + "\""
                + (
                    value == null ?
                        " IS NULL"
                        : " = :" + getFilterParameterName(columnName)
                );
    }

    private static <R> String getFilterParameterName(R columnName) {
        return "F_" + columnName;
    }

    private static <T, R> String getSetParameter(R columnName, T value) {
        return ", \"" + columnName + "\"" +
                (value == null ?
                        " IS NULL"
                        : " = :" + getSetParameterName(columnName)
                );
    }

    private static <R> String getSetParameterName(R columnName) {
        return "S_" + columnName;
    }

    private static <T, R> Function<R, R> getParameter(Map<R, T> map, BiFunction<R, T, R> fun) {
        return (R parameterName) -> fun.apply(parameterName, map.get(parameterName));
    }
}
