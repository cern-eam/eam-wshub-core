package ch.cern.eam.wshub.core.tools;

import ch.cern.eam.wshub.core.annotations.GridField;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toList;

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
                                                           .collect(toMap(GridRequestCell::getCol, GridRequestCell::getContent))
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
    public <T> List<T> converGridResultToObject(Class<T> clazz, Map<String, String> columns, GridRequestResult gridRequestResult) {
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


    private <T> T convertCellListToObject(Class<T> clazz, Map<String, String> columns, List<GridRequestCell> gridRequestCellList) {
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
    private <T> T convertCellListToObjectMap(Class<T> clazz, Map<String, String> columns, List<GridRequestCell> gridRequestCellList) {
        try {
            T object = clazz.newInstance();
            for (String column : columns.keySet()) {
                Field field = object.getClass().getDeclaredField(columns.get(column));
                setValue(object, field, column, gridRequestCellList);
            }
            return object;
        } catch (Exception exception) {
            tools.log(Level.SEVERE, exception.getMessage());
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
    private <T> T convertCellListToObjectAnnotation(Class<T> clazz, List<GridRequestCell> gridRequestCellList) {
        try {

            T object = clazz.newInstance();
            for (Field field : object.getClass().getDeclaredFields()) {
                GridField gridFieldAnnotation = field.getAnnotation(GridField.class);
                if (gridFieldAnnotation != null) {
                    setValue(object, field, gridFieldAnnotation.name(), gridRequestCellList);
                }
            }
            return object;
        } catch (Exception exception) {
            tools.log(Level.SEVERE, exception.getMessage());
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
    private <T> void setValue(T object, Field field, String column, List<GridRequestCell> gridRequestCellList) throws Exception {

        String value = gridRequestCellList.stream()
                .filter(cell -> cell.getCol().equals(column) || cell.getTag().equals(column))
                .filter(cell -> cell.getContent() != null)
                .map(GridRequestCell::getContent).findFirst().orElse(null);
        // Populate field with the value extracted above
        field.setAccessible(true);
        if (field.getType() == Date.class) {
            field.set(object, tools.getDataTypeTools().convertStringToDate(value));
        }
        // Consider 'Boolean' and primitive 'boolean'
        else if (field.getType().equals(Boolean.class) || field.getType().equals(Boolean.TYPE)) {
            field.set(object, "true".equals(value));
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
