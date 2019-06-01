package ch.cern.eam.wshub.core.tools;

import ch.cern.eam.wshub.core.services.grids.entities.GridRequestCell;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestRow;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class GridTools {

    private Tools tools;

    public GridTools(Tools tools) {
        this.tools = tools;
    }

    public List<Map<String,String>> convertGridResultToMapList(List<String> columns, GridRequestResult gridRequestResult) {
        return Arrays.stream(gridRequestResult.getRows())
                .map(gridRequestRow ->
                    Arrays.stream(gridRequestRow.getCell()).filter(cell -> columns.contains(cell.getCol()) || columns.contains(cell.getTag()))
                                                           .collect(Collectors.toMap(GridRequestCell::getCol, GridRequestCell::getContent))
                ).collect(Collectors.toList());
    }

    public <T> List<T> converGridResultToObject(Class<T> clazz, Map<String, String> columns, GridRequestResult gridRequestResult) {
            return Arrays.stream(gridRequestResult.getRows())
                    .map(gridRequestRow -> convertCellListToObject(clazz, columns, Arrays.asList(gridRequestRow.getCell())))
                    .collect(Collectors.toList());
    }

    private <T> T convertCellListToObject(Class<T> clazz, Map<String, String> columns, List<GridRequestCell> gridRequestCellList) {
        try {
            T object = clazz.newInstance();
            for (String column : columns.keySet()) {
                String value = gridRequestCellList.stream().filter(cell -> cell.getCol().equals(column)).map(GridRequestCell::getContent).findFirst().orElse(null);
                // Populate field with the value extracted above
                Field field = object.getClass().getDeclaredField(columns.get(column));
                field.setAccessible(true);
                if (field.getType() == Date.class) {
                    field.set(object, tools.getDataTypeTools().convertStringToDate(value));
                } else {
                    field.set(object, value);
                }
            }
            return object;
        } catch (Exception exception) {
            tools.log(Level.SEVERE, exception.getMessage());
            return null;
        }
    }

    public static String getCellContent(String cellid, GridRequestRow gridRequestRow) {
        if (gridRequestRow == null || gridRequestRow.getCell() == null) {
            return null;
        }
        return Arrays.stream(gridRequestRow.getCell()).filter(cell -> cell.getTag().equals(cellid) || cell.getCol().equals(cellid)).map(GridRequestCell::getContent).findFirst().orElse(null);
    }

}
