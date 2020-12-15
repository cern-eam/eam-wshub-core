package ch.cern.eam.wshub.core.services.userdefinedscreens;

import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.UDLValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface UserDefinedListHelpable {
    void setUserDefinedList(HashMap<String, ArrayList<UDLValue>> userDefinedList);
    HashMap<String, ArrayList<UDLValue>> getUserDefinedList();
    String getCopyFrom();
}