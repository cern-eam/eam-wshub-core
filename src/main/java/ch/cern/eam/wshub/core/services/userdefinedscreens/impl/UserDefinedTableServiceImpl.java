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
import java.math.BigInteger;
import java.util.*;

public class UserDefinedTableServiceImpl implements UserDefinedTableService {

    enum DATA_TYPE {
        DATE, STRING, INTEGER, DECIMAL
    }


    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public UserDefinedTableServiceImpl(ApplicationData applicationData, Tools tools,
                                       InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    @Override
    public String createUserDefinedTableRows(InforContext context, String tableName, List<UDTRow> rows)
            throws InforException {
        UserDefinedTableValidator.validateOperation(tableName, rows);
        EntityManager entityManager = tools.getEntityManager();
        for (UDTRow row: rows) {
            Map<String, Object> parameters = getParameters(row);
            parameters.putAll(getDefaultInsertColumns(context.getCredentials().getUsername()));
            UserDefinedTableQueries.executeInsertQuery(tableName.toUpperCase(), parameters, entityManager);
        }
        return null;
    }

    @Override
    public String readUserDefinedTableRows(InforContext context, String tableName, UDTRow filters, List<String> fieldsToRead) throws InforException {
        return null;
    }

    @Override
    public int updateUserDefinedTableRows(InforContext context, String tableName, UDTRow fieldsToUpdate,
                                             UDTRow filters) throws InforException {
        try {
            UserDefinedTableValidator.validateOperation(tableName, fieldsToUpdate, filters);
            Map<String, Object> updateMapMap = getParameters(fieldsToUpdate);
            Map<String, Object> whereMap = getParameters(filters);
            updateMapMap.putAll(getDefaultUpdateColumns(context.getCredentials().getUsername()));
            return UserDefinedTableQueries.executeUpdateQuery(tableName, updateMapMap, whereMap, tools.getEntityManager());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public int deleteUserDefinedTableRows(InforContext context, String tableName, UDTRow filters) throws InforException {
        UserDefinedTableValidator.validateOperation(tableName, null, filters);
        Map<String, Object> filterMap = getParameters(filters);
        return UserDefinedTableQueries.executeDeleteQuery(tableName, filterMap, tools.getEntityManager());
    }

    // HELPERS

    private static Map<String, Object> getParameters(UDTRow row) {
        //Use Map implementation that guarantees deterministic order on keySet
        Map<String, Object> mapa = new LinkedHashMap<>();
        mapa.putAll(upperMap(row.getDates()));
        mapa.putAll(upperMap(row.getStrings()));
        mapa.putAll(upperMap(row.getIntegers()));
        mapa.putAll(upperMap(row.getDecimals()));
        return mapa;
    }

    private static Map<String, Object> getDefaultInsertColumns(String username) {
        Map<String, Object> mapa = new TreeMap<>();
        mapa.put("CREATEDBY", username);
        mapa.put("CREATED", new Date());
        mapa.put("UPDATEDBY", null);
        mapa.put("UPDATED", null);
        mapa.put("UPDATECOUNT", new BigInteger("0"));
        return mapa;
    }

    private static Map<String, Object> getDefaultUpdateColumns(String username) {
        Map<String, Object> mapa = new TreeMap<>();
        mapa.put("UPDATEDBY", username);
        mapa.put("UPDATED", new Date());
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
}
