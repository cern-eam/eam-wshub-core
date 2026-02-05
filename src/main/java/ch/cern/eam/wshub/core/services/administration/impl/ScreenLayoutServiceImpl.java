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

import static ch.cern.eam.wshub.core.services.administration.impl.ScreenLayoutServiceImplTools.*;
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
            String screenLayoutCacheKey = Tools.getCacheKeyWithLang(context, userGroup, userFunction);
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

            GridRequest gridRequestDefaultLayout = new GridRequest( "DULAYT", 5000);
            gridRequestDefaultLayout.addFilter("pld_pagename", getFunctionWithTabs(systemFunction, tabs), "IN");
            Map<String, Map<String, Map<String, String>>> defaultLayout = group(GridTools.convertGridResultToMapList(gridsService.executeQuery(context, gridRequestDefaultLayout)),"pld_pagename", "pld_elementid");

            GridRequest gridRequestLayout = new GridRequest("PULAYT", 5000);
            gridRequestLayout.addFilter("plo_pagename", getFunctionWithTabs(userFunction, tabs), "IN");
            gridRequestLayout.addFilter("plo_usergroup", userGroup, "=");
            Map<String, Map<String, Map<String, String>>> layout = group(GridTools.convertGridResultToMapList(gridsService.executeQuery(context, gridRequestLayout)),"plo_pagename", "plo_elementid");

            // Get layout labels 
            Map<String, String> labels = getTabLayoutLabels(context, userFunction, systemFunction);

            Map<String, Tab> allTabs = getAllTabs(context, userGroup, userFunction);

            screenLayout.setFields(getTabLayout(entity, defaultLayout.get(systemFunction), layout.get(userFunction)));

            Map<String, UserDefinedFieldDescription> udfDetails = getUdfDetails(context, entity);
            screenLayout.getFields()
                    .values()
                    .forEach(e -> bindUdfDescription(udfDetails.get(e.getElementId()), e));

            // Add other tabs
            if (tabs != null && !tabs.isEmpty()) {
                screenLayout.setTabs(getTabs(allTabs, tabs, systemFunction, userFunction, entity, defaultLayout, layout));
            }

            // Custom grid tabs are tabs where the tabcode begins with X
            screenLayout.setCustomGridTabs(allTabs.entrySet().stream().filter(entry -> entry.getKey().startsWith("X")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
            
            screenLayout.setCustomTabs(getCustomTabs(context, allTabs, userGroup, userFunction));

            // Ameliorate with boiler texsts (record view: bot_fld1 matches the upper-cased elementId, tab view: bot_fld1 matches upper-cased tab code + '_' + elementId)
            screenLayout.getFields().values().forEach(elementInfo -> elementInfo.setText(labels.get(elementInfo.getElementId().toUpperCase())));
            screenLayout.getTabs().keySet().forEach(tab -> screenLayout.getTabs().get(tab).getFields().values().forEach(elementInfo -> elementInfo.setText(labels.get(tab + "_" + elementInfo.getElementId().toUpperCase()))));

            return screenLayout;
        } catch (InforException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("e: " + e.getMessage());
            e.printStackTrace();
            return null;
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

    private Map<String, Tab> getAllTabs(InforContext context, String userGroup, String userFunction) throws InforException {
        GridRequest gridRequest = new GridRequest("BSGROU_PRM");
        gridRequest.getParams().put("param.usergroupcode", userGroup);
        gridRequest.getParams().put("param.userfunction", userFunction);
        gridRequest.addFilter("tabcode", "", "BEGINS"); // Artificial filter to improve performance
        return GridTools.convertGridResultToMap(Tab.class, "tabcode", null, gridsService.executeQuery(context, gridRequest));
    }

    private  Map<String, Tab> getTabs(Map<String, Tab> allTabs, List<String> tabCodes, String systemFunction, String userFunction, String entity, Map<String, Map<String, Map<String, String>>> defaultLayout, Map<String, Map<String, Map<String, String>>> layout) throws InforException {
        Map<String, Tab> tabs = new HashMap<>();
        for (String tabCode : tabCodes) {
            if (allTabs.containsKey(tabCode)) {
                Tab tab = allTabs.get(tabCode);
                tab.setFields(getTabLayout(entity, defaultLayout.get( systemFunction + "_" + tabCode), layout.get(userFunction + "_" + tabCode)));
                tabs.put(tabCode, tab);
            }
        }
        return tabs; 
    }

    private Map<String, CustomTab> getCustomTabs(InforContext context, Map<String, Tab> allTabs, String userGroup, String screenCode) throws InforException {
        GridRequest gridRequest = new GridRequest("BSFUNC_TBS");
        gridRequest.getParams().put("parameter.screencode", screenCode);
        gridRequest.addFilter("type", "HTML", Operator.EQUALS.getValue(), GridRequestFilter.JOINER.AND);
        //gridRequest.addFilter("tabcode", String.join(",", alreadyFetchedTabs), Operator.NOT_IN.getValue(), GridRequestFilter.JOINER.OR);
        Map<String, String> customTabsValues = convertGridResultToMap("tabcode", "taburl", gridsService.executeQuery(context, gridRequest));


        LinkedHashMap<String, CustomTab> customTabs = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : customTabsValues.entrySet()) {
            String tabCode = entry.getKey();
            String tabUrl = entry.getValue();

            CustomTab customTab = new CustomTab(allTabs.getOrDefault(tabCode, new Tab()));
            customTab.setValue(tabUrl);
            customTab.setUrlParams(getCustomTabUrlParams(context, screenCode, tabCode));

            customTabs.put(tabCode, customTab);
        }

        return customTabs;
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

    private Map<String, ElementInfo> getTabLayout(String entity, Map<String, Map<String, String>> defaultLayout, Map<String, Map<String, String>> layout) throws InforException {
        List<ElementInfo> elements = new ArrayList<>();
        if (defaultLayout != null) {
            for (Map<String, String> elementDefaultLayout : defaultLayout.values()) {
                elements.add(buildElementInfo(elementDefaultLayout, layout.get(elementDefaultLayout.get("pld_elementid"))));
            }
        }

        elements.stream()
                .filter(element -> element.getXpath() != null && !element.getXpath().isEmpty())
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
    private Map<String, String> getTabLayoutLabels(InforContext context, String userFunction, String systemFunction) throws InforException {
        try {
            String screenLayoutLabelCacheKey = Tools.getCacheKeyWithLang(context, userFunction);
            Function<String, Map<String, String>> loader = key -> loadTabLayoutLabels(context, userFunction, systemFunction);
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

    private Map<String, String> loadTabLayoutLabels(InforContext context, String userFunction, String systemFunction) {
        try {
            // Fetch boiler texts for given screen
            GridRequest gridRequestLabels = new GridRequest("ASOBOT");
            gridRequestLabels.setRowCount(10000);
            gridRequestLabels.setUseNative(false);
            gridRequestLabels.addFilter("bot_function", userFunction, "EQUALS");
            Map<String, String> labels = convertGridResultToMap("bot_fld1", "bot_text", gridsService.executeQuery(context, gridRequestLabels));
            enhanceLabelsResponse(labels, systemFunction);
            return labels;
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

}
