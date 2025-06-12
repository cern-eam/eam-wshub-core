package ch.cern.eam.wshub.core.services.administration.impl;

import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.administration.ScreenLayoutService;
import ch.cern.eam.wshub.core.services.administration.entities.*;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequest;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestFilter;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestResult;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestRow;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.grids.impl.Operator;
import ch.cern.eam.wshub.core.tools.*;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.decodeBoolean;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.isNotEmpty;
import static ch.cern.eam.wshub.core.tools.GridTools.convertGridResultToMap;

public class ScreenLayoutServiceImpl implements ScreenLayoutService {

    private static final List<String> ALLOW_SEARCH_DESC = Collections.singletonList("LVPERS");
    private static final List<String> ALLOW_SEARCH_ALIAS = Collections.singletonList("LVOBJL");

    private final GridsService gridsService;

    public ScreenLayoutServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.gridsService = new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient);
    }

    public ScreenLayout readScreenLayout(InforContext context, String systemFunction, String userFunction, List<String> tabs, String userGroup, String entity) throws InforException {
        try {
            String screenLayoutCacheKey = String.join("_",
                    context.getTenant(), context.getCredentials().getLanguage(), userGroup, userFunction);
            Function<String, ScreenLayout> loader = key ->
                    loadScreenLayout(context, systemFunction, userFunction, tabs, userGroup, entity);
            return Optional.ofNullable(InforClient.cacheMap.get(CacheKey.SCREEN_LAYOUT))
                    .map(cache -> (ScreenLayout) cache.get(screenLayoutCacheKey, loader))
                    .orElseGet(() -> loader.apply(screenLayoutCacheKey));
        } catch (RuntimeException e) {
            if (e.getCause() instanceof InforException) {
                throw (InforException) e.getCause();
            }
            throw new InforException("Failed to read screens", null, null);
        }
    }

    public ScreenLayout loadScreenLayout(InforContext context, String systemFunction, String userFunction, List<String> tabs, String userGroup, String entity) {
        try {
            ScreenLayout screenLayout = new ScreenLayout();
            // Add the record view
            screenLayout.setFields(getTabLayout(context, userGroup, systemFunction, userFunction, entity));
            // Add other tabs
            if (tabs != null && !tabs.isEmpty()) {
                screenLayout.setTabs(getTabs(context, tabs, userGroup, systemFunction, userFunction, entity));
            }

            screenLayout.setCustomGridTabs(getCustomGridTabs(context, userGroup, userFunction));
            screenLayout.setCustomTabs(getCustomTabs(context, tabs, userGroup, userFunction));
            // Get layout labels first
            Map<String, String> labels = getTabLayoutLabels(context, userFunction);
            // For all fields for the record view, the bot_fld1 matches the upper-cased elementId
            screenLayout.getFields().values().forEach(elementInfo -> elementInfo.setText(labels.get(elementInfo.getElementId().toUpperCase())));
            // For all tab fields, bot_fld1 matches upper-cased tab code + '_' + elementId
            screenLayout.getTabs().keySet().forEach(tab -> screenLayout.getTabs().get(tab).getFields().values().forEach(elementInfo -> elementInfo.setText(labels.get(tab + "_" + elementInfo.getElementId().toUpperCase()))));

            return screenLayout;
        } catch (InforException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Map<String, String>> getGenericLov(InforContext context, GenericLov genericLov
                ) throws InforException {
        final Map<String, String> inputParams = new HashMap<>();
        inputParams.put("objectrtype", null);
        inputParams.put("filterutilitybill", null);
        inputParams.putAll(genericLov.getInputParams());

        GridRequest gridRequest = new GridRequest(genericLov.getLovName());
        gridRequest.getParams().putAll(inputParams);
        gridRequest.setGridType(GridRequest.GRIDTYPE.LOV);
        gridRequest.setUserFunctionName(genericLov.getRentity());
        gridRequest.setRowCount(genericLov.getRowCount() != null ? genericLov.getRowCount().intValue() : 10);
        gridRequest.setQueryTimeout(false);
        gridRequest.setCountTotal(false);
        List<String> listReturnFields = new ArrayList<>(genericLov.getReturnFields().values());
        final Map<String, String> returnFields = genericLov.getReturnFields();

        boolean searchOnDesc = ALLOW_SEARCH_DESC.contains(genericLov.getLovName());
        boolean searchOnAlias = ALLOW_SEARCH_ALIAS.contains(genericLov.getLovName());

        GridRequestFilter filter = new GridRequestFilter(
                listReturnFields.get(0),
                genericLov.getHint().toUpperCase(),
                genericLov.isExact() ? "EQUALS" : "BEGINS",
                searchOnDesc || searchOnAlias ? GridRequestFilter.JOINER.OR : GridRequestFilter.JOINER.AND
        );
        gridRequest.getGridRequestFilters().add(filter);

        if (searchOnDesc) {
            Arrays.stream(genericLov.getHint().split(" ")).forEach(name -> {
                gridRequest.addFilter("description", " " + name, "CONTAINS",
                        GridRequestFilter.JOINER.OR, true, false);

                gridRequest.addFilter("description", name, "BEGINS",
                        GridRequestFilter.JOINER.AND, false, true);
            });
        }

        if (searchOnAlias && !genericLov.isExact()) {
            gridRequest.addFilter("alias", genericLov.getHint().toUpperCase(), "BEGINS",
                    GridRequestFilter.JOINER.AND);
        }

        final GridRequestResult gridRequestResult =
                gridsService.executeQuery(context, gridRequest);
        return Arrays.stream(gridRequestResult.getRows()).map(row -> {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("code", GridTools.getCellContent(listReturnFields.get(0), row));

            final Optional<String> desc = returnFields.keySet().stream().filter(s -> s.endsWith("desc")).findFirst();
            if (desc.isPresent()) {
                map.put("desc", GridTools.getCellContent(returnFields.get(desc.get()), row));
            } else if ( GridTools.getCellContent("des_text", row) != null) {
                map.put("desc", GridTools.getCellContent("des_text", row));
            } else if (returnFields.size() > 1) {
                map.put("desc", GridTools.getCellContent(listReturnFields.get(1), row));
            }
            listReturnFields.forEach(str -> map.put(str, GridTools.getCellContent(str, row)));
            return map;
        }).collect(Collectors.toList());
    }

    private  Map<String, Tab> getTabs(InforContext context, List<String> tabCodes, String userGroup, String systemFunction, String userFunction, String entity) throws InforException {
        Map<String, Tab> result = new HashMap<>();
        tabCodes.forEach(tabCode -> {
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

    private Map<String, Tab> getCustomGridTabs(InforContext context, String userGroup, String userFunction) throws InforException {
        GridRequest gridRequest = new GridRequest("BSGROU_PRM");
        gridRequest.getParams().put("param.usergroupcode", userGroup);
        gridRequest.getParams().put("param.userfunction", userFunction);
        gridRequest.addFilter("tabcode", "X", "BEGINS", GridRequestFilter.JOINER.OR);
        GridRequestResult result = gridsService.executeQuery(context, gridRequest);
        Map<String, Tab> tabs = new HashMap<>();
        for (GridRequestRow row : result.getRows()) {
            tabs.put(GridTools.getCellContent("tabcode", row), gridRowToTab(row));
        }
        return tabs;
    }

    private Map<String, CustomTab> getCustomTabs(InforContext context, List<String> alreadyFetchedTabs, String userGroup, String screenCode) throws InforException {
        GridRequest gridRequest = new GridRequest("BSFUNC_TBS");
        gridRequest.getParams().put("parameter.screencode", screenCode);
        gridRequest.addFilter("type", "HTML", Operator.EQUALS.getValue(), GridRequestFilter.JOINER.AND);
        gridRequest.addFilter("tabcode", String.join(",", alreadyFetchedTabs), Operator.NOT_IN.getValue(), GridRequestFilter.JOINER.OR);
        GridRequestResult result = gridsService.executeQuery(context, gridRequest);
        LinkedHashMap<String, String> customTabsValues = new LinkedHashMap<>();
        for (GridRequestRow row : result.getRows()) {
            String tabCode = GridTools.getCellContent("tabcode", row);
            String tabUrl = GridTools.getCellContent("taburl", row);
            customTabsValues.put(tabCode, tabUrl);
        }

        Map<String, Tab> tabs = getTabsInCodes(context, userGroup, screenCode, customTabsValues.keySet());
        LinkedHashMap<String, CustomTab> customTabs = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : customTabsValues.entrySet()) {
            String tabCode = entry.getKey();
            String tabUrl = entry.getValue();

            CustomTab customTab = new CustomTab(tabs.getOrDefault(tabCode, new Tab()));
            customTab.setValue(tabUrl);
            customTab.setUrlParams(getCustomTabUrlParams(context, screenCode, tabCode));

            customTabs.put(tabCode, customTab);
        }

        return customTabs;
    }

    private Map<String, Tab> getTabsInCodes(InforContext context, String userGroup, String userFunction, Collection<String> codes) throws InforException {
        GridRequest gridRequest = new GridRequest("BSGROU_PRM");
        gridRequest.getParams().put("param.usergroupcode", userGroup);
        gridRequest.getParams().put("param.userfunction", userFunction);
        gridRequest.addFilter("tabcode", String.join(",", codes), Operator.IN.getValue(), GridRequestFilter.JOINER.OR);
        GridRequestResult result = gridsService.executeQuery(context, gridRequest);
        Map<String, Tab> tabs = new HashMap<>();
        for (GridRequestRow row : result.getRows()) {
            tabs.put(GridTools.getCellContent("tabcode", row), gridRowToTab(row));
        }
        return tabs;
    }

    private List<URLParam> getCustomTabUrlParams(InforContext context, String screenCode, String tabCode) throws InforException {
        GridRequest gridRequest = new GridRequest("BSURLP");
        gridRequest.getParams().put("param.screencode", screenCode);
        gridRequest.getParams().put("param.tabcode", tabCode);
        gridRequest.addFilter("active", null, "SELECTED", GridRequestFilter.JOINER.OR);
        GridRequestResult result = gridsService.executeQuery(context, gridRequest);
        return Arrays.stream(result.getRows()).map(this::rowToURLParam).collect(Collectors.toList());
    }

    private URLParam rowToURLParam(GridRequestRow row) {
        return new URLParam(
                GridTools.getCellContent("parametername", row),
                GridTools.getCellContent("parametervalue", row),
                Objects.equals(GridTools.getCellContent("system", row), "true"),
                Objects.equals(GridTools.getCellContent("usefieldvalue", row), "true")
        );
    }

    private Map<String, ElementInfo> getTabLayout(InforContext context, String userGroup, String systemFunction, String userFunction, String entity) throws InforException {
        GridRequest gridRequestLayout = new GridRequest( "EULLAY");
        gridRequestLayout.setRowCount(2000);
        gridRequestLayout.setUseNative(false);
        gridRequestLayout.addFilter("plo_usergroup", userGroup, "=", GridRequestFilter.JOINER.AND);
        gridRequestLayout.addFilter("plo_pagename", userFunction, "=", GridRequestFilter.JOINER.AND);
        gridRequestLayout.addFilter("pld_pagename", systemFunction, "=", GridRequestFilter.JOINER.AND);
        final GridRequestResult gridRequestResult = gridsService.executeQuery(context, gridRequestLayout);
        List<ElementInfo> elements = GridTools.convertGridResultToObject(ElementInfo.class, null, gridRequestResult);
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
        try {
            String screenLayoutLabelCacheKey = String.join("_",
                    context.getTenant(), context.getCredentials().getLanguage(), userFunction);
            Function<String, Map<String, String>> loader = key -> loadTabLayoutLabels(context, userFunction);
            return Optional.ofNullable(InforClient.cacheMap.get(CacheKey.SCREEN_LAYOUT_LABEL))
                    .map(cache -> (Map<String, String>) cache.get(screenLayoutLabelCacheKey, loader))
                    .orElseGet(() -> loader.apply(screenLayoutLabelCacheKey));
        } catch (RuntimeException e) {
            if (e.getCause() instanceof InforException) {
                throw (InforException) e.getCause();
            }
            throw new InforException("Failed to read screens", null, null);
        }
    }

    private Map<String, String> loadTabLayoutLabels(InforContext context, String userFunction) {
        try {
            // Fetch boiler texts for given screen
            GridRequest gridRequestLabels = new GridRequest("ASOBOT");
            gridRequestLabels.setRowCount(10000);
            gridRequestLabels.setUseNative(false);
            gridRequestLabels.addFilter("bot_function", userFunction, "EQUALS");
            return convertGridResultToMap("bot_fld1", "bot_text", gridsService.executeQuery(context, gridRequestLabels));
        } catch (InforException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, UserDefinedFieldDescription> getUdfDetails(InforContext context, String entity) throws InforException {
        GridRequest gridRequest = new GridRequest("BCUDFS");
        gridRequest.getParams().put("parameter.lastupdated", "01-Jan-1970");
        List<GridRequestFilter> filters = new ArrayList<>();
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
        gridRequest.addFilter("tabcode", String.join(",", tabCodes), "ALT_IN", GridRequestFilter.JOINER.OR);
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

    private Tab gridRowToTab(GridRequestRow row) {
        Tab tab = new Tab();
        tab.setTabAvailable(decodeBoolean(GridTools.getCellContent("tabavailable", row)));
        tab.setAlwaysDisplayed(decodeBoolean(GridTools.getCellContent("tabalwaysdisp", row)));
        tab.setTabDescription(GridTools.getCellContent("tabcodetext", row));
        tab.setInsertAllowed(decodeBoolean(GridTools.getCellContent("insertval", row)));
        tab.setQueryAllowed(decodeBoolean(GridTools.getCellContent("queryval", row)));
        tab.setUpdateAllowed(decodeBoolean(GridTools.getCellContent("updateval", row)));
        tab.setDeleteAllowed(decodeBoolean(GridTools.getCellContent("deleteval", row)));
        return tab;
    }

}
