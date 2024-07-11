package ch.cern.eam.wshub.core.services.userdefinedscreens.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListHelpable;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedTableService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.Tools.generateFault;

public class UserDefinedListServiceImpl implements UserDefinedListService {
    final static private String TABLE_NAME = "U5PROPVALUESLISTS";
    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;
    private UserDefinedTableService userDefinedTableService;
    public UserDefinedListServiceImpl(ApplicationData applicationData, Tools tools,
                                      EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
        userDefinedTableService =
                new UserDefinedTableServiceImpl(applicationData, tools, eamWebServicesToolkitClient);
    }

    private UDTRow initUDLRow(UDLEntryId entryId) {
        UDTRow row = new UDTRow();

        if(entryId.getEntityId().getEntityType() == null || entryId.getEntityId().getEntityCode() == null) {
            throw new IllegalArgumentException("Must at least have entity type and code");
        }

        row.addString("PVL_RENTITY", entryId.getEntityId().getEntityType());
        row.addString("PVL_CODE", entryId.getEntityId().getEntityCode());

        if(entryId.getProperty() != null) {
            row.addString("PVL_PROPERTY", entryId.getProperty());
        }

        if(entryId.getSequenceNumber() != null) {
            row.addInteger("PVL_SEQNO", entryId.getSequenceNumber());
        }

        return row;
    }

    private UDTRow initUDLRow(UDLEntry entry) {
        UDTRow row = new UDTRow();
        UDLEntryId entryId = entry.getEntryId();
        EntityId entityId = entryId.getEntityId();

        row.addString("PVL_RENTITY", entityId.getEntityType());
        row.addString("PVL_CODE", entityId.getEntityCode());
        row.addString("PVL_PROPERTY", entryId.getProperty());
        row.addInteger("PVL_SEQNO", entryId.getSequenceNumber());

        UDLValue value = entry.getValue();
        row.addString("PVL_VALUE", value.getString());
        row.addDate("PVL_DVALUE", value.getDate());
        row.addDecimal("PVL_NVALUE", value.getNumeric());

        return row;
    }

    private UDLEntry getUDLEntry(Map<String, Object> map) {
        String entityType = (String) map.get("PVL_RENTITY");
        String entityCode = (String) map.get("PVL_CODE");
        EntityId entityId = new EntityId(entityType, entityCode);

        String property = (String) map.get("PVL_PROPERTY");
        BigInteger sequenceNumber = (BigInteger) map.get("PVL_SEQNO");
        UDLEntryId entryId = new UDLEntryId(entityId, property, sequenceNumber);

        String stringValue = (String) map.get("PVL_VALUE");
        Date dateValue = (Date) map.get("PVL_DVALUE");
        BigDecimal numericValue = (BigDecimal) map.get("PVL_NVALUE");

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
    public HashMap<String, ArrayList<UDLValue>> readUserDefinedLists(EAMContext context, UDLEntryId entryId) throws EAMException {
        List<UDLEntry> entries = readUserDefinedListEntries(context, entryId);

        HashMap<String, ArrayList<UDLValue>> map = new HashMap<>();

        // readUserDefinedListEntries returned an ordered list of entries, so we can simply add it to the map
        for(UDLEntry entry : entries) {
            String property = entry.getEntryId().getProperty();
            UDLValue value = entry.getValue();

            List<UDLValue> list = map.get(property);

            if(list == null) {
                ArrayList<UDLValue> newList = new ArrayList<>();
                newList.add(value);
                map.put(property, newList);
            } else {
                list.add(value);
            }
        }

        return map;
    }

    @Override
    public String setUserDefinedLists(EAMContext context, EntityId entityId, Map<String, ArrayList<UDLValue>> values) throws EAMException {

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

        if (!rows.isEmpty()) {
            userDefinedTableService.createUserDefinedTableRows(context, TABLE_NAME, rows);
        }

        return "OK";
    }

    @Override
    public List<UDLEntry> readUserDefinedListEntries(EAMContext context, UDLEntryId property)
            throws EAMException {
        UDTRow filters = initUDLRow(property);

        List<Map<String, Object> > rows = userDefinedTableService.readUserDefinedTableRows(context, TABLE_NAME, filters,
                Arrays.asList("PVL_RENTITY", "PVL_CODE", "PVL_PROPERTY", "PVL_SEQNO", "PVL_VALUE", "PVL_DVALUE", "PVL_NVALUE"));

        try {
            return rows.stream()
                    // sorted based on property first, then sequence number
                    .sorted((a, b) -> {
                        String propertyA = (String) a.get("PVL_PROPERTY");
                        String propertyB = (String) b.get("PVL_PROPERTY");
                        int propertyCompare = propertyA.compareTo(propertyB);

                        if (propertyCompare == 0) {
                            BigInteger sequenceNumberA = (BigInteger) a.get("PVL_SEQNO");
                            BigInteger sequenceNumberB = (BigInteger) b.get("PVL_SEQNO");

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
    public String createUserDefinedListEntry(EAMContext context, UDLEntry entry) throws EAMException {
        userDefinedTableService.createUserDefinedTableRows(context, TABLE_NAME, Arrays.asList(initUDLRow(entry)));
        return "OK";
    }

    @Override
    public String updateUserDefinedListEntry(EAMContext context, UDLEntry entry) throws EAMException {
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
    public String deleteUserDefinedListEntries(EAMContext context, UDLEntryId filters) throws EAMException {
        UDTRow tableFilters = initUDLRow(filters);
        userDefinedTableService.deleteUserDefinedTableRows(context, TABLE_NAME, tableFilters);
        return "OK";
    }

    @Override
    public void readUDLToEntity(EAMContext context, UserDefinedListHelpable entity, EntityId entityId) {
        try {
            HashMap<String, ArrayList<UDLValue>> entries = readUserDefinedLists(context, new UDLEntryId(entityId));
            entity.setUserDefinedList(entries);
        } catch(Exception e) {
            tools.log(Level.SEVERE, "Failed reading UDL from " + entityId);
        }
    }

    @Override
    public void writeUDLToEntityCopyFrom(EAMContext context, UserDefinedListHelpable entity, EntityId entityId) {
        try {
            HashMap<String, ArrayList<UDLValue>> entries = entity.getUserDefinedList();
            if (entries != null) {
                setUserDefinedLists(context, entityId, entries);
            } else if (entity.getCopyFrom() != null) {
                // only copy the UDL from the copyFrom entity if we didn't set it
                Map<String, ArrayList<UDLValue>> copyFromEntries = readUserDefinedLists(context,
                    new UDLEntryId(new EntityId(entityId.getEntityType(), entity.getCopyFrom())));
                setUserDefinedLists(context, entityId, copyFromEntries);
            }
        } catch(Exception e) {
            tools.log(Level.SEVERE, "Failed writing UDL with copyFrom in " + entityId);
        }
    }

    @Override
    public void writeUDLToEntity(EAMContext context, UserDefinedListHelpable entity, EntityId entityId) {
        try {
            if(entity.getUserDefinedList() != null) {
                setUserDefinedLists(
                    context,
                    entityId,
                    entity.getUserDefinedList());
            }
        } catch(Exception e) {
            tools.log(Level.SEVERE, "Failed writing UDL in " + entityId);
        }
    }

    @Override
    public void deleteUDLFromEntity(EAMContext context, EntityId entityId) {
        try {
            deleteUserDefinedListEntries(context, new UDLEntryId(entityId));
        } catch(Exception e) {
            tools.log(Level.SEVERE, "Failed deleting UDL of " + entityId);
        }
    }
}
