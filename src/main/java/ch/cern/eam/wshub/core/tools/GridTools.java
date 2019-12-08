package ch.cern.eam.wshub.core.tools;

import ch.cern.eam.wshub.core.annotations.GridField;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toList;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.convertStringToDate;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.encodeBigDecimal;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.encodeBigInteger;

public class GridTools {

    private Tools tools;

    public GridTools(Tools tools) {
        this.tools = tools;
    }

    /**
     *
     * @param columns
     * @param gridRequestResult
     * @return
     */
    public List<Map<String,String>> convertGridResultToMapList(List<String> columns, GridRequestResult gridRequestResult) {
        return Arrays.stream(gridRequestResult.getRows())
                .map(gridRequestRow ->
                    Arrays.stream(gridRequestRow.getCell()).filter(cell -> columns.contains(cell.getCol()) || columns.contains(cell.getTag()))
                                                           .filter(cell -> cell.getContent() != null)
                                                           .collect(toMap(GridRequestCell::getTag, GridRequestCell::getContent))
                ).collect(toList());
    }

    /**
     *
     * @param clazz
     * @param key
     * @param columns
     * @param gridRequestResult
     * @param <T>
     * @return
     */
    public <T> Map<String,T> convertGridResultToMap(Class<T> clazz, String key, Map<String, String> columns, GridRequestResult gridRequestResult)
    {
        HashMap<String,T> result = new HashMap<>();
        if (gridRequestResult == null || gridRequestResult.getRows() == null) {
            return result;
        }
        // Use imperative approach to avoid java.lang.IllegalStateException Duplicate Key
        for (GridRequestRow gridRequestRow : gridRequestResult.getRows()) {
            result.put(getCellContent(key, gridRequestRow), convertCellListToObject(clazz, columns, Arrays.asList(gridRequestRow.getCell())));
        }
        return result;
    }

    /**
     *
     * @param key
     * @param value
     * @param gridRequestResult
     * @return
     */
    public static Map<String,String> convertGridResultToMap(String key, String value, GridRequestResult gridRequestResult) {
        HashMap<String,String> result = new HashMap<>();
        if (gridRequestResult == null || gridRequestResult.getRows() == null) {
            return result;
        }
        // Use imperative approach to avoid java.lang.IllegalStateException Duplicate Key
        for (GridRequestRow gridRequestRow : gridRequestResult.getRows()) {
            result.put(getCellContent(key, gridRequestRow), getCellContent(value, gridRequestRow));
        }
        return result;
    }

    /**
     *
     * @param clazz
     * @param columns
     * @param gridRequestResult
     * @param <T>
     * @return
     */
    public static <T> List<T> converGridResultToObject(Class<T> clazz, Map<String, String> columns, GridRequestResult gridRequestResult) {
        List<T> result = new LinkedList<>();
        if (gridRequestResult == null || gridRequestResult.getRows() == null) {
            return result;
        }
        // Use imperative approach to avoid java.lang.IllegalStateException Duplicate Key
        for (GridRequestRow gridRequestRow : gridRequestResult.getRows()) {
            result.add(convertCellListToObject(clazz, columns, Arrays.asList(gridRequestRow.getCell())));
        }
        return result;
    }


    private static <T> T convertCellListToObject(Class<T> clazz, Map<String, String> columns, List<GridRequestCell> gridRequestCellList) {
        if (columns == null) {
            return convertCellListToObjectAnnotation(clazz, gridRequestCellList);
        } else {
            return convertCellListToObjectMap(clazz, columns, gridRequestCellList);
        }
    }


    /**
     * Converts gridRequestCellList to object using columns map
     *
     * @param clazz
     * @param columns
     * @param gridRequestCellList
     * @param <T>
     * @return
     */
    private static <T> T convertCellListToObjectMap(Class<T> clazz, Map<String, String> columns, List<GridRequestCell> gridRequestCellList) {
        try {
            T object = clazz.newInstance();
            for (String column : columns.keySet()) {
                Field field = object.getClass().getDeclaredField(columns.get(column));
                setValue(object, field, column, new String[]{}, gridRequestCellList);
            }
            return object;
        } catch (Exception exception) {
            //tools.log(Level.SEVERE, exception.getMessage());
            return null;
        }
    }

    /**
     * Converts gridRequestCellList to object using objects annotations
     *
     * @param clazz
     * @param gridRequestCellList
     * @param <T>
     * @return
     */
    private static <T> T convertCellListToObjectAnnotation(Class<T> clazz, List<GridRequestCell> gridRequestCellList) {
        try {

            T object = clazz.newInstance();
            for (Field field : object.getClass().getDeclaredFields()) {
                GridField gridFieldAnnotation = field.getAnnotation(GridField.class);
                if (gridFieldAnnotation != null) {
                    setValue(object, field, gridFieldAnnotation.name(), gridFieldAnnotation.alternativeNames(), gridRequestCellList);
                }
            }
            return object;
        } catch (Exception exception) {
            //tools.log(Level.SEVERE, exception.getMessage());
            exception.printStackTrace();
            System.out.println("Error: " + exception.getMessage());
            return null;
        }
    }

    /**
     * object.field = gridRequestCellList[column]
     *
     * @param object
     * @param field
     * @param column
     * @param gridRequestCellList
     * @param <T>
     * @throws Exception
     */
    private static <T> void setValue(T object, Field field, String column, String[] alternativeColumns, List<GridRequestCell> gridRequestCellList) throws Exception {
        // Extract the value from gridRequestCellList
        String value = gridRequestCellList.stream()
                .filter(cell -> cell.getCol().equals(column) ||
                        cell.getTag().equals(column) ||
                        Arrays.stream(alternativeColumns).anyMatch(altColumn -> cell.getCol().equals(altColumn)) ||
                        Arrays.stream(alternativeColumns).anyMatch(altColumn -> cell.getTag().equals(altColumn))
                )
                .filter(cell -> cell.getContent() != null)
                .map(GridRequestCell::getContent)
                .findFirst().orElse(null);
        // Don't continue when null
        if (value == null) {
            return;
        }
        // Populate field with the value extracted above
        field.setAccessible(true);
        if (field.getType() == Date.class) {
            field.set(object, convertStringToDate(value));
        }
        // Consider 'Boolean' and primitive 'boolean'
        else if (field.getType().equals(Boolean.class) || field.getType().equals(Boolean.TYPE)) {
            field.set(object, "true".equals(value));
        }
        // Big Integer
        else if (field.getType().equals(BigInteger.class)) {
            field.set(object, encodeBigInteger(value, column));
        }
        // Big Decimal
        else if (field.getType().equals(BigDecimal.class)) {
            field.set(object, encodeBigDecimal(value, column));
        }
        else {
            field.set(object, value);
        }
    }


    /**
     * Gets cell content from GridRequestRow
     *
     * @param cellid
     * @param gridRequestRow
     * @return
     */
    public static String getCellContent(String cellid, GridRequestRow gridRequestRow) {
        if (gridRequestRow == null || gridRequestRow.getCell() == null) {
            return null;
        }
        return Arrays.stream(gridRequestRow.getCell())
                .filter(cell -> cell.getTag().equalsIgnoreCase(cellid) || cell.getCol().equalsIgnoreCase(cellid))
                .filter(cell -> cell.getContent() != null)
                .map(GridRequestCell::getContent).findFirst().orElse(null);
    }

}
