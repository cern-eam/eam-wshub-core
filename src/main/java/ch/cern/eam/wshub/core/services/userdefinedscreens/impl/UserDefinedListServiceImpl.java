package ch.cern.eam.wshub.core.services.userdefinedscreens.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListHelpable;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedTableService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLEntry;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLProperty;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDTRow;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.Callable;
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

    private UDTRow initUDLRow(UDLProperty property) {
        UDTRow row = new UDTRow();

        if(property.getEntityType() == null || property.getCode() == null) {
            throw new IllegalArgumentException("Must at least have entity type and code");
        }

        row.addString("APV_RENTITY", property.getEntityType());
        row.addString("APV_CODE", property.getCode());

        if(property.getProperty() != null) {
            row.addString("APV_PROPERTY", property.getProperty());
        }

        if(property.getSequenceNumber() != null) {
            row.addInteger("APV_SEQNO", property.getSequenceNumber());
        }

        return row;
    }

    private void addUDLEntryToUDTRow(UDLEntry entry, UDTRow row) {
        row.addString("APV_PROPERTY", entry.getProperty());
        row.addInteger("APV_SEQNO", entry.getSequenceNumber());
        row.addString("APV_VALUE", entry.getString());
        row.addDate("APV_DVALUE", entry.getDate());
        row.addDecimal("APV_NVALUE", entry.getNumeric());
    }

    @Override
    public String createUserDefinedListEntry(InforContext context, UDLProperty property, UDLEntry entry) throws InforException {
        UDTRow row = initUDLRow(property);
        addUDLEntryToUDTRow(entry, row);

        return userDefinedTableService.createUserDefinedTableRows(context, TABLE_NAME, Arrays.asList(row));
    }

    private UDLEntry getUDLEntryFromMap(Map<String, Object> map) {
        String property = (String) map.get("APV_PROPERTY");
        BigInteger sequenceNumber = (BigInteger) map.get("APV_SEQNO");

        String stringValue = (String) map.get("APV_VALUE");
        Date dateValue = (Date) map.get("APV_DVALUE");
        BigDecimal numericValue = (BigDecimal) map.get("APV_NVALUE");

        long nonNulls = Arrays.asList(stringValue, dateValue, numericValue).stream().filter(a -> a != null).count();
        if(nonNulls > 1) {
            throw new RuntimeException("Multiple value types for UDLEntry");
        }

        if(stringValue != null) {
            return new UDLEntry(property, sequenceNumber, stringValue);
        } else if(dateValue != null) {
            return new UDLEntry(property, sequenceNumber, dateValue);
        } else if(numericValue != null) {
            return new UDLEntry(property, sequenceNumber, numericValue);
        }

        return new UDLEntry(property, sequenceNumber);
    }

    @Override
    public List<UDLEntry> readUserDefinedListEntries(InforContext context, UDLProperty property)
            throws InforException {
        UDTRow filters = initUDLRow(property);

        List<Map<String, Object> > rows = userDefinedTableService.readUserDefinedTableRows(context, TABLE_NAME, filters,
                Arrays.asList("APV_PROPERTY", "APV_SEQNO", "APV_VALUE", "APV_DVALUE", "APV_NVALUE"));

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
                    .map(this::getUDLEntryFromMap)
                    .collect(Collectors.toList());
        } catch(RuntimeException e) {
            throw generateFault(e.getMessage()); // in case this::getUDLEntryFromMap fails
        }
    }

    @Override
    public String updateUserDefinedListEntry(InforContext context, UDLProperty oldProperty, UDLEntry newEntry)
            throws InforException {
        if(oldProperty.getProperty() == null || oldProperty.getSequenceNumber() == null) {
            throw new IllegalArgumentException(
                "Can only update a single entry at a time: specify property and sequence number.");
        }

        UDTRow row = initUDLRow(oldProperty);
        addUDLEntryToUDTRow(newEntry, row);

        UDTRow filters = initUDLRow(oldProperty);

        int updates = userDefinedTableService.updateUserDefinedTableRows(context, TABLE_NAME, row, filters);

        if (updates == 1) return "OK";

        if(updates == 0) {
            throw generateFault("Specified row not found");
        }

        tools.log(Level.SEVERE,
            "UserDefinedListServiceImpl::updateUserDefinedListEntry updated more than 1 row");
        throw generateFault("Updated more than one row");
    }

    @Override
    public String deleteUserDefinedListEntry(InforContext context, UDLProperty property) throws InforException {
        UDTRow filters = initUDLRow(property);
        userDefinedTableService.deleteUserDefinedTableRows(context, TABLE_NAME, filters);
        return "OK";
    }

    @Override
    public String setUserDefinedList(InforContext context, UDLProperty property, List<UDLEntry> entries) throws InforException {
        if(property.getSequenceNumber() != null) {
            throw new IllegalArgumentException("Cannot specify sequence number for property when setting UDL");
        }

        if(property.getProperty() != null) {
            boolean valid = entries.stream().map(entry -> entry.getProperty()).allMatch(entryProperty ->
                entryProperty.equalsIgnoreCase(property.getProperty()));

            if(!valid) {
                throw new IllegalArgumentException("UDL entry has non-conforming property");
            }
        }

        List<UDTRow> rows = new ArrayList<>(entries.size());
        for(UDLEntry entry : entries) {
            UDTRow row = initUDLRow(property);
            addUDLEntryToUDTRow(entry, row);
            rows.add(row);
        }

        UDTRow filters = initUDLRow(property);

        entityManager.joinTransaction();
        userDefinedTableService.deleteUserDefinedTableRows(context, TABLE_NAME, filters);
        userDefinedTableService.createUserDefinedTableRows(context, TABLE_NAME, rows);
        return "OK";
    }

    @Override
    public void readUDLToEntity(InforContext context, UserDefinedListHelpable entity, String entityType, String code) {
        try {
            List<UDLEntry> entries = readUserDefinedListEntries(context, new UDLProperty(entityType, code));
            entity.setUserDefinedList(entries);
        } catch(Exception e) {
            tools.log(Level.SEVERE, "Failed reading UDL from " + entityType + " " + code);
        }
    }

    @Override
    public void writeUDLToEntityCopyFrom(InforContext context, UserDefinedListHelpable entity, String entityType, String code) {
        try {
            List<UDLEntry> entityEntries = entity.getUserDefinedList();
            if (entityEntries != null) {
                setUserDefinedList(context, new UDLProperty(entityType, code), entityEntries);
            } else if (entity.getCopyFrom() != null) {
                // only copy the UDL from the copyFrom entity if we didn't set it
                List<UDLEntry> entries = readUserDefinedListEntries(context,
                    new UDLProperty(entityType, entity.getCopyFrom()));
                setUserDefinedList(context, new UDLProperty(entityType, code), entries);
            }
        } catch(Exception e) {
            tools.log(Level.SEVERE, "Failed writing UDL with copyFrom in " + entityType + " " + code);
        }
    }

    @Override
    public void writeUDLToEntity(InforContext context, UserDefinedListHelpable entity, String entityType, String code) {
        try {
            if(entity.getUserDefinedList() != null) {
                setUserDefinedList(
                    context,
                    new UDLProperty(entityType, code),
                    entity.getUserDefinedList());
            }
        } catch(Exception e) {
            tools.log(Level.SEVERE, "Failed writing UDL in " + entityType + " " + code);
        }
    }

    @Override
    public void deleteUDLFromEntity(InforContext context, String entityType, String code) {
        try {
            deleteUserDefinedListEntry(context, new UDLProperty(entityType, code));
        } catch(Exception e) {
            tools.log(Level.SEVERE, "Failed deleting UDL of " + entityType + " " + code);
        }
    }
}
