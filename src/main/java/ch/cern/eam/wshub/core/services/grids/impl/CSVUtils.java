package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.services.grids.entities.GridField;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestCell;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class CSVUtils {

    private static final String SEPARATOR = ",";

    public static String convertGridRequestResultToCsv(GridRequestResult gridRequestResult) throws EAMException {

        if (gridRequestResult.getGridFields() == null || gridRequestResult.getGridFields().size() == 0) {
            throw Tools.generateFault("The grid request result must contain the list of fields (GridRequestResult.gridFields)");
        }

        String header = gridRequestResult.getGridFields().stream()
                .filter(gridField -> gridField.getOrder() >= 0)
                .sorted(Comparator.comparing(GridField::getOrder))
                .map(GridField::getLabel)
                .map(CSVUtils::escapeCsv)
                .collect(Collectors.joining(SEPARATOR)) + "\n";

        String result = Arrays.stream(gridRequestResult.getRows())
                .map(row -> Arrays.stream(row.getCell())
                        .filter(cell -> cell.getOrder() >= 0)
                        .sorted(Comparator.comparing(GridRequestCell::getOrder))
                        .map(GridRequestCell::getContent)
                        .map(CSVUtils::escapeCsv)
                        .collect(Collectors.joining(SEPARATOR)))
                .collect(Collectors.joining("\n"));

        return header + result;
    }

    private static String escapeCsv(String text) {
        text = text == null ? "" : text;
        return text.length() > 0 ? "\"" + text.replaceAll("\"", "\"\"") + "\"" : "";
    }

}
