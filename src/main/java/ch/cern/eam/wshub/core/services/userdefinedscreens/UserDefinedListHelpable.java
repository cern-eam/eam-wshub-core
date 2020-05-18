package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLEntry;

import java.util.List;

public interface UserDefinedListHelpable {
    void setUserDefinedList(List<UDLEntry> userDefinedList);
    List<UDLEntry> getUserDefinedList();
    String getCopyFrom();
}