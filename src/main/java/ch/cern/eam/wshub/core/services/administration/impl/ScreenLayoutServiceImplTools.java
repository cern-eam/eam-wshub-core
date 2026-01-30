package ch.cern.eam.wshub.core.services.administration.impl;

import ch.cern.eam.wshub.core.services.administration.entities.ElementInfo;
import ch.cern.eam.wshub.core.services.administration.entities.UserDefinedFieldDescription;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;

public class ScreenLayoutServiceImplTools {

    static ElementInfo buildElementInfo(Map<String, String> def, Map<String, String> lay) {
        ElementInfo e = new ElementInfo();

        // --- pld_* always from DEFAULT layout ---
        e.setElementId(def.get("pld_elementid"));
        e.setPageName(def.get("pld_pagename"));
        e.setXpath(def.get("pld_xpath"));
        e.setMaxLength(def.get("pld_maxlength"));
        e.setCharacterCase(def.get("pld_case"));
        e.setFieldType(def.get("pld_fieldtype"));
        e.setOnLookup(def.get("pld_onlookup"));

        // --- plo_* always from LAYOUT override ---
        if (lay != null) {
            e.setAttribute(lay.get("plo_attribute"));
            e.setDefaultValue(lay.get("plo_defaultvalue"));
            e.setPresentInJSP(lay.get("plo_presentinjsp"));
            e.setFieldContainer(lay.get("plo_fieldcontainer"));
            e.setElementType(lay.get("plo_elementtype"));
            e.setFieldContType(lay.get("plo_fieldconttype"));

            e.setFieldGroup(toBigInt(lay.get("plo_fieldgroup")));
            e.setPositionInGroup(toBigInt(lay.get("plo_positioningroup")));
            e.setTabIndex(toBigInt(lay.get("plo_tabindex")));
        }

        return e;
    }

    static BigInteger toBigInt(String v) {
        return v == null || v.isEmpty() ? null : new BigInteger(v);
    }

    static String getFunctionWithTabs(String functionCode, List<String> tabs) {
        List<String> functionWithTabs = new ArrayList<>(Arrays.asList(functionCode));
        tabs.forEach(t -> functionWithTabs.add(functionCode + "_" + t));
        return String.join(",", functionWithTabs);
    }

    static Map<String, Map<String, Map<String, String>>> group(
            List<Map<String, String>> layout, String pagekey, String elementkey) {

        return layout.stream()
                .collect(Collectors.groupingBy(
                        m -> m.get(pagekey),
                        Collectors.toMap(
                                m -> m.get(elementkey),
                                m -> m
                        )
                ));
    }

    static ElementInfo bindUdfDescription(UserDefinedFieldDescription description, ElementInfo elementInfo) {
        if(description != null) {
            elementInfo.setUdfLookupEntity(description.getLookupREntity());
            elementInfo.setUdfLookupType(description.getLookupType());
            elementInfo.setUdfUom(description.getUom());
            if (isNotEmpty(description.getDateType())) {
                if (description.getDateType().equals("DATE")) {
                    elementInfo.setFieldType("date");
                }
                if (description.getDateType().equals("DATI")) {
                    elementInfo.setFieldType("datetime");
                }
            }
        }
        return elementInfo;
    }

    static void enhanceLabelsResponse(Map<String, String> labels, String systemFunction) {
        if (systemFunction.equals("WSJOBS")) {
            labels.put("BLOCK_2", labels.get("WODETAILSSECTION"));
            labels.put("BLOCK_3", labels.get("ACTDETAILSSECTION"));
            labels.put("BLOCK_4", labels.get("SCHEDDETAILSSECTION"));
            labels.put("BLOCK_5", labels.get("CUSTOMFIELDSSECTION"));
            labels.put("BLOCK_6", labels.get("LINEARREFERENCEDETAILS"));
            labels.put("BLOCK_7", labels.get("CALLCENTERDETAILS"));
            labels.put("BLOCK_8", labels.get("USERDEFINEDFIELDSSECTION"));
            labels.put("BLOCK_9", labels.get("PRODUCTIONDETAILS"));
            labels.put("BLOCK_12", labels.get("COMPLIANCEDETAILSSECTION"));
            labels.put("BLOCK_13", labels.get("INCIDENTTRACKINGSECTION"));
            labels.put("BLOCK_14", labels.get("GUESTDETAILSSECTION"));
            labels.put("CLO_BLOCK_2", labels.get("CLOSINGCODESSECTION"));
        }
    }

}
