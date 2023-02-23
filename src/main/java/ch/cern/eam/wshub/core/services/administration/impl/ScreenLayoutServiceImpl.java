package ch.cern.eam.wshub.core.services.administration.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.administration.ScreenLayoutService;
import ch.cern.eam.wshub.core.services.administration.entities.ElementInfo;
import ch.cern.eam.wshub.core.services.administration.entities.ScreenLayout;
import ch.cern.eam.wshub.core.services.administration.entities.Tab;
import ch.cern.eam.wshub.core.services.administration.entities.UserDefinedFieldDescription;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestFilter;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestRow;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.GridTools;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.decodeBoolean;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;
import static ch.cern.eam.wshub.core.tools.GridTools.convertGridResultToMap;

public class ScreenLayoutServiceImpl implements ScreenLayoutService {

    public static final Map<String, ScreenLayout> screenLayoutCache = new ConcurrentHashMap<>();
    public static final Map<String, Map<String, String>> screenLayoutLabelCache = new ConcurrentHashMap<>();

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;
    private GridsService gridsService;

    public ScreenLayoutServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
        gridsService = new GridsServiceImpl(applicationData, tools, inforws);
    }


    public ScreenLayout readScreenLayout(InforContext context, String systemFunction, String userFunction, List<String> tabs, String userGroup, String entity) throws InforException {
        // Check if value not already in the cache
        String layoutCacheKey = userGroup + "_" + userFunction;
        if (screenLayoutCache.containsKey(layoutCacheKey)) {
            return screenLayoutCache.get(layoutCacheKey);
        }

        ScreenLayout screenLayout = new ScreenLayout();
        // Add the record view
        screenLayout.setFields(getTabLayout(context, userGroup, systemFunction, userFunction, entity));
        // Add other tabs
        if (tabs != null && tabs.size() > 0) {
            screenLayout.setTabs(getTabs(context, tabs, userGroup, systemFunction, userFunction, entity));
        }
        // Get layout labels first
        Map<String, String> labels = getTabLayoutLabels(context, userFunction);
        // For all fields for the record view the bot_fld1 matches the upper-cased elementId
        screenLayout.getFields().values().forEach(elementInfo -> elementInfo.setText(labels.get(elementInfo.getElementId().toUpperCase())));
        // For all tab fields bot_fld1 matches upper-cased tab code + '_' + elementId
        screenLayout.getTabs().keySet().forEach(tab -> {
            screenLayout.getTabs().get(tab).getFields().values().forEach(elementInfo -> elementInfo.setText(labels.get(tab + "_" + elementInfo.getElementId().toUpperCase())));
        });

        // Cache it before returning
        screenLayoutCache.put(layoutCacheKey, screenLayout);

        return  screenLayout;
    }

    private  Map<String, Tab> getTabs(InforContext context, List<String> tabCodes, String userGroup, String systemFunction, String userFunction, String entity) throws InforException {
        Map<String, Tab> result = new HashMap<>();
        tabCodes.stream().forEach(tabCode -> {
            Tab tab = new Tab();
            try {
                tab.setFields(getTabLayout(context, userGroup, systemFunction + "_" + tabCode, userFunction + "_" + tabCode, entity));
                result.put(tabCode, tab);
            } catch (InforException e) {
                e.printStackTrace();
            }
        });
        getTabScreenPermissions(context, result, tabCodes, userGroup, userFunction);
        return result;
    }

    private Map<String, ElementInfo> getTabLayout(InforContext context, String userGroup, String systemFunction, String userFunction, String entity) throws InforException {
        GridRequest gridRequestLayout = new GridRequest( "EULLAY");
        gridRequestLayout.setRowCount(2000);
        gridRequestLayout.setUseNative(false);
        gridRequestLayout.addFilter("plo_usergroup", userGroup, "=", GridRequestFilter.JOINER.AND);
        gridRequestLayout.addFilter("plo_pagename", userFunction, "=", GridRequestFilter.JOINER.AND);
        gridRequestLayout.addFilter("pld_pagename", systemFunction, "=", GridRequestFilter.JOINER.AND);
        List<ElementInfo> elements = tools.getGridTools().convertGridResultToObject(ElementInfo.class, null, gridsService.executeQuery(context, gridRequestLayout));
        Map<String, UserDefinedFieldDescription> udfDetails = getUdfDetails(context, entity);
        elements.stream()
                .map(element -> bindUdfDescription(udfDetails.getOrDefault(element.getElementId(), null), element))
                .filter(element -> element.getXpath() != null)
                .forEach(element -> element.setXpath("EAMID_" + element.getXpath().replace("\\", "_")));
        return elements.stream().collect(Collectors.toMap(ElementInfo::getElementId, element -> element));
    }

    /**
     * Reads all boiler texts (labels) for given function
     *
     * @param context
     * @param userFunction
     * @throws InforException
     */
    private Map<String, String> getTabLayoutLabels(InforContext context, String userFunction) throws InforException {
        // Check if value already not present in the cache
        if (screenLayoutLabelCache.containsKey(userFunction)) {
            return screenLayoutLabelCache.get(userFunction);
        }

        // Fetch boiler texts for given screen
        GridRequest gridRequestLabels = new GridRequest( "ASOBOT");
        gridRequestLabels.setRowCount(10000);
        gridRequestLabels.setUseNative(false);
        gridRequestLabels.addFilter("bot_function", userFunction, "EQUALS");
        Map<String, String> labels = convertGridResultToMap("bot_fld1", "bot_text", gridsService.executeQuery(context, gridRequestLabels));

        // Save to cache and return
        screenLayoutLabelCache.put(userFunction, labels);
        return labels;
    }

    private Map<String, UserDefinedFieldDescription> getUdfDetails(InforContext context, String entity) throws InforException {
        GridRequest gridRequest = new GridRequest("BCUDFS");
        gridRequest.getParams().put("parameter.lastupdated", "01-Jan-1970");
        List<GridRequestFilter> filters = new ArrayList<GridRequestFilter>();
        GridRequestFilter rentity = new GridRequestFilter("UDF_RENTITY", entity, "=");
        filters.add(rentity);
        gridRequest.setGridRequestFilters(filters);
        Map<String, UserDefinedFieldDescription> result = convertGridResultToMap(UserDefinedFieldDescription.class, "udf_field", null,
                gridsService.executeQuery(context, gridRequest));
        for(String key : result.keySet()) {
            UserDefinedFieldDescription udf = result.get(key);
            if(udf.getLookupType().equals("CODEDESC") || udf.getLookupType().equals("CODE")) {
                udf.setLookupREntity(entity);
            }
        }
        return result;
    }

    private void getTabScreenPermissions(InforContext context, Map<String, Tab> tabs, List<String> tabCodes, String userGroup, String userFunction) throws InforException {
        GridRequest gridRequest = new GridRequest("BSGROU_PRM");
        gridRequest.getParams().put("param.usergroupcode", userGroup);
        gridRequest.getParams().put("param.userfunction", userFunction);
        gridRequest.addFilter("tabcode", String.join(",", tabCodes), "IN", GridRequestFilter.JOINER.OR);
        GridRequestResult result = gridsService.executeQuery(context, gridRequest);
        for (GridRequestRow row : result.getRows()) {
            String tabCode = GridTools.getCellContent("tabcode", row);
            tabs.get(tabCode).setTabAvailable(decodeBoolean(GridTools.getCellContent("tabavailable", row)));
            tabs.get(tabCode).setAlwaysDisplayed(decodeBoolean(GridTools.getCellContent("tabalwaysdisp", row)));
            tabs.get(tabCode).setTabDescription(GridTools.getCellContent("tabcodetext", row));
            tabs.get(tabCode).setInsertAllowed(decodeBoolean(GridTools.getCellContent("insertval", row)));
            tabs.get(tabCode).setQueryAllowed(decodeBoolean(GridTools.getCellContent("queryval", row)));
            tabs.get(tabCode).setUpdateAllowed(decodeBoolean(GridTools.getCellContent("updateval", row)));
            tabs.get(tabCode).setDeleteAllowed(decodeBoolean(GridTools.getCellContent("deleteval", row)));
        }
    }

    private ElementInfo bindUdfDescription(UserDefinedFieldDescription description, ElementInfo elementInfo) {
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

}
