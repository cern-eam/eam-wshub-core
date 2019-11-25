package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.tools.InforException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserDefinedTableQueries {

    public static <T> void executeInsertQuery(String tableName, Map<String, T> map, EntityManager em) throws InforException {
        //Create list to guarantee ordering
        String query = getInsertQuery(tableName, map);
        //em.joinTransaction();
        try {
            //TODO batch
            Query nativeQuery = em.createNativeQuery(query);
            map.keySet().stream().filter(s -> map.get(s) != null).forEach(
                    column -> nativeQuery.setParameter(column, map.get(column))
            );
            nativeQuery.executeUpdate();
        } catch (PersistenceException e) {
            //String msg, Throwable cause, ExceptionInfo[] details
            throw UserDefinedTableValidator.generateInforException("", e.getMessage());
        }
    }

    private static <T> String getInsertQuery(String tableName, Map<String, T> map) {
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



    public static <T> int executeUpdateQuery(String tableName, Map<String, T> updateColumns,
                                              Map<String, T> whereFilters, EntityManager em)
            throws InforException {
        //Create list to guarantee ordering
        String query = getUpdateQuery(tableName, updateColumns, whereFilters);
        //em.joinTransaction();
        try {
            Query nativeQuery = em.createNativeQuery(query);

            updateColumns.keySet().stream().filter(s -> updateColumns.get(s) != null)
                    .forEach(column -> nativeQuery.setParameter(getSetParameterName(column),
                        updateColumns.get(column)));

            whereFilters.keySet().stream().filter(s -> whereFilters.get(s) != null)
                    .forEach(column -> nativeQuery.setParameter(getFilterParameterName(column), whereFilters.get(column)));
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

        String filters = whereFilters.keySet().stream()
                .map(getParameter(whereFilters, UserDefinedTableQueries::getFilterParameter))
                .collect(Collectors.joining())
                ;

        StringBuilder query = new StringBuilder();
        query.append("UPDATE ");
        query.append(tableName);
        query.append(" SET ");
        query.append(update);
        query.append(" WHERE ( 1=1").append(filters).append(")");
        return query.toString();
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
