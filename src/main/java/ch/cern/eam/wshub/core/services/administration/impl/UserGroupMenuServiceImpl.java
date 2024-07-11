package ch.cern.eam.wshub.core.services.administration.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.administration.UserGroupMenuService;
import ch.cern.eam.wshub.core.services.administration.entities.MenuEntryNode;
import ch.cern.eam.wshub.core.services.administration.entities.MenuRequestType;
import ch.cern.eam.wshub.core.services.administration.entities.MenuSpecification;
import ch.cern.eam.wshub.core.services.administration.entities.MenuType;
import ch.cern.eam.wshub.core.services.entities.BatchResponse;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.extmenus_001.ExtMenus;
import net.datastream.schemas.mp_entities.extmenushierarchy_001.ExtMenusHierarchy;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp6005_001.MP6005_GetExtMenusHierarchy_001;
import net.datastream.schemas.mp_functions.mp6043_001.MP6043_AddExtMenus_001;
import net.datastream.schemas.mp_functions.mp6045_001.MP6045_DeleteExtMenus_001;
import net.datastream.schemas.mp_results.mp6043_001.MP6043_AddExtMenus_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class UserGroupMenuServiceImpl implements UserGroupMenuService {
    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public UserGroupMenuServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
    }

    /**
     * Adds a full menu/submenu/function path to the menu hierarchy. If the path specified is not complete, it will be completed.
     *
     * @param context           the user credentials
     * @param menuSpecification the specified full path and function to add, for a specific user group
     * @return
     */
    @Override
    public String addToMenuHierarchy(EAMContext context, MenuSpecification menuSpecification) throws EAMException {
        UserGroupMenuService.validateInputNode(menuSpecification);

        // Get menu entries as tree
        MenuEntryNode menuRoot = this.getExtMenuHierarchyAsTree(context, menuSpecification.getForUserGroup(), MenuRequestType.EXCLUDE_PERMISSIONSAND_TABS);

        // Check if path already exists; if so, continue
        List<String> pathList = menuSpecification.getMenuPath();
        MenuEntryNode latestMenuEntryNodeFound = this.getLatestMenuEntryByPath(pathList, menuRoot);

        // If level of the folder to be added is last
        String func = menuSpecification.getFunctionCode();
        if (latestMenuEntryNodeFound.getLevel() == pathList.size()) {
            // If function is not set, do nothing
            if (func == null && func.isEmpty()) {
                return "OK";
            }
            // If function is set and child function exists, do nothing
            for (MenuEntryNode child : latestMenuEntryNodeFound.getChildren()) {
                if (child.getFunctionId().equals(func)) {
                    return "OK";
                }
            }
        }

        // Else, complete path
        for (int i = latestMenuEntryNodeFound.getLevel(); i < pathList.size(); i++) {
            MenuEntryNode addedMenuEntryNode = this.performAddFolderOperation(latestMenuEntryNodeFound, pathList.get(i), menuSpecification.getForUserGroup(), context);
            latestMenuEntryNodeFound.add(addedMenuEntryNode);
            latestMenuEntryNodeFound = addedMenuEntryNode;
        }

        // And add function if it is set
        if (func != null && !func.isEmpty()) {
            MenuEntryNode addedMenuEntryNode = this.performAddFunctionOperation(latestMenuEntryNodeFound, func, menuSpecification.getForUserGroup(), context);
        }

        return "OK";
    }

    /**
     * Adds many full menu/submenu/function path to the menu hierarchy. If the paths specified are not complete, they will be completed.
     *
     * @param context               the user credentials
     * @param menuSpecificationList the list of menu specifications to add
     * @return                      the batch of eam responses
     */
    @Override
    public BatchResponse<String> addToMenuHierarchyBatch(EAMContext context, List<MenuSpecification> menuSpecificationList) {
        return tools.batchOperation(context, this::addToMenuHierarchy, menuSpecificationList);
    }

    /**
     * Adds a full menu/submenu/function path to the menu hierarchy of different user groups. If the path specified is not complete, it will be completed.
     *
     * @param context           the user credentials
     * @param userGroups        the list of specific user groups to add the menu specification
     * @param menuSpecification the specified full path and function to add; the user group is not used
     * @return                  the batch of eam responses
     */
    @Override
    public BatchResponse<String> addToMenuHierarchyManyUsergroups(EAMContext context, List<String> userGroups, MenuSpecification menuSpecification) {
        List<MenuSpecification> menuSpecificationList = new ArrayList<MenuSpecification>();
        userGroups.stream().forEach(u -> menuSpecificationList.add(new MenuSpecification(menuSpecification.getMenuPath(), menuSpecification.getFunctionCode(), u)));

        return addToMenuHierarchyBatch(context, menuSpecificationList);
    }

    /**
     * For each menu specification in the list: if a function code is specified in menuSpecification,
     * deletes all children functions with that function code from the specified path. If the function code
     * provided is an empty string, deletes the last menu item with all its children from the path specified.
     *
     * @param context               the user credentials
     * @param menuSpecificationList the list of menu specifications to add
     * @return                      the batch of eam responses
     */
    @Override
    public BatchResponse<String> deleteFromMenuHierarchyBatch(EAMContext context, List<MenuSpecification> menuSpecificationList) {
        return tools.batchOperation(context, this::deleteFromMenuHierarchy, menuSpecificationList);
    }

    /**
     * For a each user group of a specific list of usergroups: if a function code is specified in menuSpecification,
     * deletes all children functions with that function code from the specified path. If the function code
     * provided is an empty string, deletes the last menu item with all its children from the path specified.
     *
     * @param context           the user credentials
     * @param userGroups        the list of specific user groups to delete the menu specification
     * @param menuSpecification the specified full path and function to delete; the user group is not used
     * @return                  the batch of eam responses
     */
    @Override
    public BatchResponse<String> deleteFromMenuHierarchyManyUsergroups(EAMContext context, List<String> userGroups, MenuSpecification menuSpecification) {
        List<MenuSpecification> menuSpecificationList = new ArrayList<MenuSpecification>();
        userGroups.stream().forEach(u -> menuSpecificationList.add(new MenuSpecification(menuSpecification.getMenuPath(), menuSpecification.getFunctionCode(), u)));

        return deleteFromMenuHierarchyBatch(context, menuSpecificationList);
    }

    private ExtMenusHierarchy getExtMenuHierarchy(EAMContext context, String userGroup, MenuRequestType requestType) throws EAMException {
        MP6005_GetExtMenusHierarchy_001 getExtMenusHierarchy = new MP6005_GetExtMenusHierarchy_001();
        getExtMenusHierarchy.setUSERGROUPID(new USERGROUPID_Type());
        getExtMenusHierarchy.getUSERGROUPID().setUSERGROUPCODE(userGroup);
        getExtMenusHierarchy.setRequest(EXTMENUSHIERARCHYREQUEST_Type.fromValue(requestType.value()));

        ExtMenusHierarchy result =
                tools.performEAMOperation(context, eamws::getExtMenusHierarchyOp, getExtMenusHierarchy)
                        .getResultData().getExtMenusHierarchy();

        return result;
    }

    public MenuEntryNode getExtMenuHierarchyAsTree(EAMContext context, String userGroup, MenuRequestType requestType) throws EAMException {
        MenuEntryNode root = new MenuEntryNode();
        ExtMenusHierarchy result = this.getExtMenuHierarchy(context, userGroup, requestType);

        List<MENU_Type> menus = result.getMENU();
        for (MENU_Type menu : menus) {
            MenuEntryNode mainMenuEntry = new MenuEntryNode(menu);
            root.add(mainMenuEntry);
            menu.getFOLDER().stream().forEach(childFolder -> this.addFolderToMenuNode(mainMenuEntry, childFolder));
            menu.getFUNCTION().stream().forEach(childFunction -> this.addFunctionToMenuNode(mainMenuEntry, childFunction));
        }

        return root;
    }

    private void addFolderToMenuNode(MenuEntryNode currentNode, FOLDER_Type folder) {
        MenuEntryNode newNode = new MenuEntryNode(folder);
        currentNode.add(newNode);
        for (FOLDER_Type childFolder : folder.getFOLDER()) {
            addFolderToMenuNode(newNode, childFolder);
        }
        for (FUNCTION_Type childFunction : folder.getFUNCTION()) {
            addFunctionToMenuNode(newNode, childFunction);
        }
    }

    private void addFunctionToMenuNode(MenuEntryNode currentNode, FUNCTION_Type function) {
        MenuEntryNode newNode = new MenuEntryNode(function);
        currentNode.add(newNode);
    }

    private MP6043_AddExtMenus_001 fillExtMenus(MenuEntryNode parent, String folderName, String userGroup, EAMContext context, String code, String menuType) {
        // With the previous ID found and the menu type determined, fill the request object for both menu item or function item
        String id = parent != null ? parent.getId() : ""; // Which would be the extMenuCode of the parent..
        MP6043_AddExtMenus_001 addExtMenus = new MP6043_AddExtMenus_001();
        ExtMenus extMenus = new ExtMenus();
        addExtMenus.setExtMenus(extMenus);
        extMenus.setUSERGROUPID(new USERGROUPID_Type());
        extMenus.getUSERGROUPID().setUSERGROUPCODE(userGroup);
        extMenus.setFUNCTIONID(new FUNCTIONID_Type());
        if (!menuType.equals(MenuType.FUNCTION.getType())) { // If not function, don't set name
            extMenus.getFUNCTIONID().setFUNCTIONDESCRIPTION(folderName);
        }
        extMenus.getFUNCTIONID().setFUNCTIONCODE(code); // Submenu code
        extMenus.setEXTMENUPARENT(id);
        extMenus.setEXTMENUTYPE(menuType);
        Long maxSequenceNumber = parent.getChildren().stream().mapToLong(MenuEntryNode::getSequenceNumber).max().orElse(0);
        extMenus.setSEQUENCENUMBER(maxSequenceNumber + 1); // Set by default at the end of newly added menu item, to be moved manually by local admins if required
        extMenus.setMOBILE("false");

        return addExtMenus;
    }

    private MenuEntryNode performAddFunctionOperation(MenuEntryNode parent, String functionCode, String userGroup, EAMContext context) throws EAMException {
        MP6043_AddExtMenus_001 addExtMenus = this.fillExtMenus(parent, "", userGroup, context, functionCode, MenuType.FUNCTION.getType());

        // With the request object created, perform the add operation
        MP6043_AddExtMenus_001_Result result = tools.performEAMOperation(context, eamws::addExtMenusOp, addExtMenus);

        return new MenuEntryNode(result.getResultData().getExtMenus());
    }

    private MenuEntryNode performAddFolderOperation(MenuEntryNode parent, String folderName, String userGroup, EAMContext context) throws EAMException {
        String menuType = MenuType.SUBMENU.getType(); // Assume entry is submenu
        if (parent.getDescription().equals("ROOT_NODE")) {
            menuType = MenuType.MAIN_MENU.getType(); // Entry is main menu
        }

        MP6043_AddExtMenus_001 addExtMenus = this.fillExtMenus(parent, folderName, userGroup, context, UserGroupMenuService.MENU_FUNCTION_CODE, menuType);

        // With the request object created, perform the add operation
        MP6043_AddExtMenus_001_Result result = tools.performEAMOperation(context, eamws::addExtMenusOp, addExtMenus);

        return new MenuEntryNode(result.getResultData().getExtMenus());
    }

    private MenuEntryNode getLatestMenuEntryByPath(List<String> pathList, MenuEntryNode menuRoot) {
        boolean found = false;
        boolean endReached = false;
        MenuEntryNode currentNode = menuRoot;

        for (String currentPathEntry : pathList) {
            for (MenuEntryNode childNode : currentNode.getChildren()) {
                if (childNode.getDescription().equals(currentPathEntry)) { // Note: assumes identity by description, and paths unique
                    currentNode = childNode;
                    found = true;
                    break;
                }
            }
            endReached = !found;
            if (endReached) {
                break;
            }
            found = false;
        }

        return currentNode;
    }

    /**
     * If a function code is specified in menuSpecification, deletes all children functions with that function code from
     * the specified path. If the function code provided is an empty string, deletes the last menu item with all its
     * children from the path specified.
     *
     * @param context           the user credentials
     * @param menuSpecification the specified path and function to delete, for a specific user group
     * @return
     */
    @Override
    public String deleteFromMenuHierarchy(EAMContext context, MenuSpecification menuSpecification) throws EAMException {
        UserGroupMenuService.validateInputNode(menuSpecification);

        // Get menu entries as tree
        MenuEntryNode menuRoot = this.getExtMenuHierarchyAsTree(context, menuSpecification.getForUserGroup(), MenuRequestType.EXCLUDE_PERMISSIONSAND_TABS);

        List<String> pathList = menuSpecification.getMenuPath();
        MenuEntryNode latestMenuEntryNodeFound = this.getLatestMenuEntryByPath(pathList, menuRoot);

        // If the specified path doesn't exist, we'll assume it's a problem
        if (latestMenuEntryNodeFound.getLevel() != pathList.size()) {
            throw Tools.generateFault("Path doesn't exist");
        }

        // Else, the path specified exists, so delete function if there is one specified
        String func = menuSpecification.getFunctionCode();
        if (func != null && !func.isEmpty()) { // Function is specified
            for (MenuEntryNode child : latestMenuEntryNodeFound.getChildren()) {
                if (child.getFunctionId().equals(func)) { // Find function (allows not finding), also delete all target function children
                    this.performDeleteFunctionOperation(child, menuSpecification.getFunctionCode(), menuSpecification.getForUserGroup(), context);
                }
            }
        } else { // Or delete last element in path if no function specified
            this.performDeleteFolderOperation(latestMenuEntryNodeFound, menuSpecification.getFunctionCode(), menuSpecification.getForUserGroup(), context);
        }

        return "OK";
    }

    private void performDeleteFunctionOperation(MenuEntryNode entryToDelete, String functionCode, String userGroup, EAMContext context) throws EAMException {
        // Find id of the menu item to be removed
        entryToDelete.getFunctionId();
        this.performDeleteOperation(entryToDelete, functionCode, userGroup, context, MenuType.FUNCTION.getType());
    }

    private void performDeleteFolderOperation(MenuEntryNode entryToDelete, String functionCode, String userGroup, EAMContext context) throws EAMException {
        String menuType = MenuType.SUBMENU.getType(); // Assume entry is submenu
        if (entryToDelete.getParentMenuEntry().getDescription().equals("ROOT_NODE")) {
            menuType = MenuType.MAIN_MENU.getType(); // Entry is main menu
        }
        this.performDeleteOperation(entryToDelete, UserGroupMenuService.MENU_FUNCTION_CODE, userGroup, context, menuType);
    }

    private void performDeleteOperation(MenuEntryNode entryToDelete, String functionCode, String userGroup, EAMContext context, String menuType) throws EAMException {
        // With the id of the item, fill the request object
        MP6045_DeleteExtMenus_001 deleteExtMenus = new MP6045_DeleteExtMenus_001();
        ExtMenus extMenus = new ExtMenus();
        deleteExtMenus.setExtMenus(extMenus);
        extMenus.setUSERGROUPID(new USERGROUPID_Type());
        extMenus.getUSERGROUPID().setUSERGROUPCODE(userGroup);
        extMenus.setFUNCTIONID(new FUNCTIONID_Type());
        extMenus.getFUNCTIONID().setFUNCTIONCODE(functionCode);
        extMenus.setEXTMENUID(new EXTMENUID_Type());
        extMenus.getEXTMENUID().setEXTMENUCODE(entryToDelete.getId());
        extMenus.setEXTMENUTYPE(menuType);

        // With the request object created, perform the add operation
        tools.performEAMOperation(context, eamws::deleteExtMenusOp, deleteExtMenus);
    }

}
