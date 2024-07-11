package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.tools.ExceptionInfo;
import ch.cern.eam.wshub.core.tools.EAMException;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserDefinedTableValidator {

    private static final List<String> RESERVED_FIELD_NAMES = Arrays.asList(
            "CREATED", "CREATEDBY", "UPDATED", "UPDATEDBY", "UPDATECOUNT"
    );

    public static void validateOperation(String tableName, UDTRow rowsToInsert,
                                         UDTRow rowsToFilter) throws EAMException {
        validateOperation(tableName, rowsToInsert == null ? new ArrayList<>()
                : Collections.singletonList(rowsToInsert), rowsToFilter);
    }

    public static void validateOperation(String tableName, List<UDTRow> rowsToInsert) throws EAMException {
        validateOperation(tableName, rowsToInsert, null);
    }

    private static void validateOperation(String tableName, List<UDTRow> rowsToInsert
            , UDTRow rowsToFilter) throws EAMException {
        validateTableName(tableName);
        if (rowsToInsert != null) {
            for(UDTRow row: rowsToInsert) {
                validateKeyList(row.getAllKeys(), true);
            }
        }
        if (rowsToFilter != null) {
            validateKeyList(rowsToFilter.getAllKeys(), false);
        }
    }

    public static void validateKeyList(List<String> keyList, boolean insert) throws EAMException {
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
                throw generateEAMException( "columnNames", "Repeated column names: " + repeaters);
            }
            //Reserved column names that shall not be manipulated by the user
            if (insert && hashSet.stream().anyMatch(RESERVED_FIELD_NAMES::contains)) {
                throw generateEAMException( "columnNames", "Reserved field names cannot be used: "
                        + hashSet.stream().filter(RESERVED_FIELD_NAMES::contains)
                        .collect(Collectors.joining(",")));
            }
            //TODO Check if the field name is described on the table?
        }
    }

    public static EAMException generateEAMException(String field, String errorMessage) {
        return new EAMException(
                errorMessage,
                null,
                Collections.singleton(new ExceptionInfo(field, errorMessage))
                        .toArray(new ExceptionInfo[0])
        );
    }

    private static void validateTableName(String name) throws EAMException {
        if (name == null) {
            throw generateEAMException("key", "Table name cannot be null");
        }
        // Valid U5 (User Defined Screen) table names
        if (!Pattern.matches("^[Uu]5[_A-Za-z0-9]+$", name)) {
            String errorMessage = "Invalid Table name: \"" + name + '"';
            throw generateEAMException(name, errorMessage);
        }
    }

    private static void validateColumnName(String name) throws EAMException {
        if (name == null) {
            throw generateEAMException("key", "Column name cannot be null");
        }
        // Valid column names
        if (!Pattern.matches("^[_A-Za-z0-9]+$", name)) {
            String errorMessage = "Invalid Column name: \"" + name + '"';
            throw generateEAMException(name, errorMessage);
        }
    }
}
