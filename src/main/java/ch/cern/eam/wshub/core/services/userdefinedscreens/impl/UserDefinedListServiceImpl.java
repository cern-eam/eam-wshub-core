package ch.cern.eam.wshub.core.services.userdefinedscreens.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListHelpable;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedTableService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.Tools.generateFault;

public class UserDefinedListServiceImpl implements UserDefinedListService {
    final static private String TABLE_NAME = "U5DEVPROPERTIES22"; // TODO: change to the final table name
    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;
    private UserDefinedTableService userDefinedTableService;
    private EntityManager entityManager;

    public UserDefinedListServiceImpl(ApplicationData applicationData, Tools tools,
                                      InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        userDefinedTableService =
                new UserDefinedTableServiceImpl(applicationData, tools, inforWebServicesToolkitClient);

        if (tools.isDatabaseConnectionConfigured()) {
            this.entityManager = tools.getEntityManager();
        }
    }

    private UDTRow initUDLRow(UDLEntryId entryId) {
        UDTRow row = new UDTRow();

        if(entryId.getEntityId().getEntityType() == null || entryId.getEntityId().getEntityCode() == null) {
            throw new IllegalArgumentException("Must at least have entity type and code");
        }

        row.addString("APV_RENTITY", entryId.getEntityId().getEntityType());
        row.addString("APV_CODE", entryId.getEntityId().getEntityCode());

        if(entryId.getProperty() != null) {
            row.addString("APV_PROPERTY", entryId.getProperty());
        }

        if(entryId.getSequenceNumber() != null) {
            row.addInteger("APV_SEQNO", entryId.getSequenceNumber());
        }

        return row;
    }

    private UDTRow initUDLRow(UDLEntry entry) {
        UDTRow row = new UDTRow();
        UDLEntryId entryId = entry.getEntryId();
        EntityId entityId = entryId.getEntityId();

        row.addString("APV_RENTITY", entityId.getEntityType());
        row.addString("APV_CODE", entityId.getEntityCode());
        row.addString("APV_PROPERTY", entryId.getProperty());
        row.addInteger("APV_SEQNO", entryId.getSequenceNumber());

        UDLValue value = entry.getValue();
        row.addString("APV_VALUE", value.getString());
        row.addDate("APV_DVALUE", value.getDate());
        row.addDecimal("APV_NVALUE", value.getNumeric());

        return row;
    }

    private UDLEntry getUDLEntry(Map<String, Object> map) {
        String entityType = (String) map.get("APV_RENTITY");
        String entityCode = (String) map.get("APV_CODE");
        EntityId entityId = new EntityId(entityType, entityCode);

        String property = (String) map.get("APV_PROPERTY");
        BigInteger sequenceNumber = (BigInteger) map.get("APV_SEQNO");
        UDLEntryId entryId = new UDLEntryId(entityId, property, sequenceNumber);

        String stringValue = (String) map.get("APV_VALUE");
        Date dateValue = (Date) map.get("APV_DVALUE");
        BigDecimal numericValue = (BigDecimal) map.get("APV_NVALUE");

        long nonNulls = Arrays.asList(stringValue, dateValue, numericValue).stream().filter(a -> a != null).count();
        if(nonNulls > 1) {
            throw new RuntimeException("Multiple value types for UDLEntry");
        }

        if(stringValue != null) {
            return new UDLEntry(entryId, new UDLValue(stringValue));
        } else if(dateValue != null) {
            return new UDLEntry(entryId, new UDLValue(dateValue));
        } else if(numericValue != null) {
            return new UDLEntry(entryId, new UDLValue(numericValue));
        }

        return new UDLEntry(entryId);
    }

    @Override
    public Map<String, List<UDLValue>> readUserDefinedLists(InforContext context, UDLEntryId entryId) throws InforException {
        List<UDLEntry> entries = readUserDefinedListEntries(context, entryId);

        Map<String, List<UDLValue>> map = new HashMap<>();

        // readUserDefinedListEntries returned an ordered list of entries, so we can simply add it to the map
        for(UDLEntry entry : entries) {
            String property = entry.getEntryId().getProperty();
            UDLValue value = entry.getValue();

            List<UDLValue> list = map.get(property);

            if(list == null) {
                List<UDLValue> newList = new ArrayList<>();
                newList.add(value);
                map.put(property, newList);
            } else {
                list.add(value);
            }
        }

        return map;
    }

    @Override
    public String setUserDefinedLists(InforContext context, EntityId entityId, Map<String, List<UDLValue>> values) throws InforException {
        entityManager.joinTransaction();

        List<UDTRow> rows = new ArrayList<>();
        for(String property : values.keySet()) {
            UDTRow filters = initUDLRow(new UDLEntryId(entityId, property));
            userDefinedTableService.deleteUserDefinedTableRows(context, TABLE_NAME, filters);

            List<UDLValue> list = values.get(property); // guaranteed to succeed
            BigInteger sequenceNumber = BigInteger.ZERO;
            for(UDLValue value : list) {
                UDLEntryId entryId = new UDLEntryId(entityId, property, sequenceNumber);
                rows.add(initUDLRow(new UDLEntry(entryId, value)));
                sequenceNumber = sequenceNumber.add(BigInteger.ONE);
            }
        }

        userDefinedTableService.createUserDefinedTableRows(context, TABLE_NAME, rows);
        return "OK";
    }

    @Override
    public List<UDLEntry> readUserDefinedListEntries(InforContext context, UDLEntryId property)
            throws InforException {
        UDTRow filters = initUDLRow(property);

        List<Map<String, Object> > rows = userDefinedTableService.readUserDefinedTableRows(context, TABLE_NAME, filters,
                Arrays.asList("APV_RENTITY", "APV_CODE", "APV_PROPERTY", "APV_SEQNO", "APV_VALUE", "APV_DVALUE", "APV_NVALUE"));

        try {
            return rows.stream()
                    // sorted based on property first, then sequence number
                    .sorted((a, b) -> {
                        String propertyA = (String) a.get("APV_PROPERTY");
                        String propertyB = (String) b.get("APV_PROPERTY");
                        int propertyCompare = propertyA.compareTo(propertyB);

                        if (propertyCompare == 0) {
                            BigInteger sequenceNumberA = (BigInteger) a.get("APV_SEQNO");
                            BigInteger sequenceNumberB = (BigInteger) b.get("APV_SEQNO");

                            return sequenceNumberA.compareTo(sequenceNumberB);
                        }

                        return propertyCompare;
                    })
                    // convert Map<String, Object> to UDLEntry
                    .map(this::getUDLEntry)
                    .collect(Collectors.toList());
        } catch(RuntimeException e) {
            throw generateFault(e.getMessage()); // in case this::getUDLEntryFromMap fails
        }
    }

    @Override
    public String createUserDefinedListEntry(InforContext context, UDLEntry entry) throws InforException {
        userDefinedTableService.createUserDefinedTableRows(context, TABLE_NAME, Arrays.asList(initUDLRow(entry)));
        return "OK";
    }

    @Override
    public String updateUserDefinedListEntry(InforContext context, UDLEntry entry) throws InforException {
        UDTRow row = initUDLRow(entry);
        UDTRow filters = initUDLRow(entry.getEntryId());
        int updates = userDefinedTableService.updateUserDefinedTableRows(context, TABLE_NAME, row, filters);

        if (updates == 1) return "OK";
        else if (updates == 0) throw generateFault("Specified row not found");

        tools.log(Level.SEVERE,
                "UserDefinedListServiceImpl::updateUserDefinedListEntry updated more than 1 row");
        throw generateFault("Updated more than one row");
    }

    @Override
    public String deleteUserDefinedListEntries(InforContext context, UDLEntryId filters) throws InforException {
        UDTRow tableFilters = initUDLRow(filters);
        userDefinedTableService.deleteUserDefinedTableRows(context, TABLE_NAME, tableFilters);
        return "OK";
    }

    @Override
    public void readUDLToEntity(InforContext context, UserDefinedListHelpable entity, String entityType, String code) {
        // TODO
    }

    @Override
    public void writeUDLToEntityCopyFrom(InforContext context, UserDefinedListHelpable entity, String entityType, String code) {
        // TODO
    }

    @Override
    public void writeUDLToEntity(InforContext context, UserDefinedListHelpable entity, String entityType, String code) {
        // TODO
    }

    @Override
    public void deleteUDLFromEntity(InforContext context, String entityType, String code) {
        // TODO
    }
}
