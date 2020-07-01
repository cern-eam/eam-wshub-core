package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLEntry;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLValue;

import java.util.List;
import java.util.Map;

public interface UserDefinedListHelpable {
    void setUserDefinedList(Map<String, List<UDLValue>> userDefinedList);
    Map<String, List<UDLValue>> getUserDefinedList();
    String getCopyFrom();
}