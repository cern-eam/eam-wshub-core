package ch.cern.eam.wshub.core.services.administration.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.administration.UserGroupMenuService;
import ch.cern.eam.wshub.core.services.administration.entities.MenuEntryNode;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.extmenus_001.ExtMenus;
import net.datastream.schemas.mp_entities.extmenushierarchy_001.ExtMenusHierarchy;
import net.datastream.schemas.mp_fields.*;
import net.datastream.schemas.mp_functions.mp6005_001.MP6005_GetExtMenusHierarchy_001;
import net.datastream.schemas.mp_functions.mp6043_001.MP6043_AddExtMenus_001;
import net.datastream.schemas.mp_functions.mp6045_001.MP6045_DeleteExtMenus_001;
import net.datastream.schemas.mp_results.mp6043_001.MP6043_AddExtMenus_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.Arrays;
import java.util.List;

public class UserGroupMenuServiceImpl implements UserGroupMenuService {
    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public UserGroupMenuServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    private void addFolderToMenuNode(MenuEntryNode currentNode, FOLDER_Type folder) {
        MenuEntryNode newNode = new MenuEntryNode(folder);
        currentNode.add(newNode);
        for(FOLDER_Type childFolder : folder.getFOLDER()) {
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

    private ExtMenusHierarchy getExtMenuHierarchy(InforContext context, String userGroup) throws InforException {
        MP6005_GetExtMenusHierarchy_001 getExtMenusHierarchy = new MP6005_GetExtMenusHierarchy_001();
        getExtMenusHierarchy.setUSERGROUPID(new USERGROUPID_Type());
        getExtMenusHierarchy.getUSERGROUPID().setUSERGROUPCODE(userGroup);

        ExtMenusHierarchy result =
                tools.performInforOperation(context, inforws::getExtMenusHierarchyOp, getExtMenusHierarchy)
                        .getResultData().getExtMenusHierarchy();

        return result;
    }

    /**
     * Adds a full menu/submenu/function path to the menu hierarchy.
     * @param context the user credentials
     * @param menuSpecification the specified full path and function to add, for a specific user group
     * @return
     */
    @Override
    public String addToMenuHierarchy(InforContext context, MenuSpecification menuSpecification) throws InforException {
        this.validateInputNode(menuSpecification);

        // Get menu entries as tree
        MenuEntryNode menuRoot = this.getExtMenuHierarchyAsTree(context, menuSpecification);

        // Check if path already exists; if so, continue
        List<String> pathList = Arrays.asList(menuSpecification.menuPath.split("\\/"));
        MenuEntryNode latestMenuEntryNodeFound = this.getLatestMenuEntryByPath(pathList, menuRoot);

        // If level of the folder to be added is last
        String func = menuSpecification.functionCode;
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
        for (int i = latestMenuEntryNodeFound.getLevel() ; i < pathList.size() ; i++) {
            MenuEntryNode addedMenuEntryNode = this.performAddFolderOperation(latestMenuEntryNodeFound, pathList.get(i), menuSpecification.forUserGroup, context);
            latestMenuEntryNodeFound.add(addedMenuEntryNode);
            latestMenuEntryNodeFound = addedMenuEntryNode;
        }

        // And add function if it is set
        if (func != null && !func.isEmpty()) {
            MenuEntryNode addedMenuEntryNode = this.performAddFunctionOperation(latestMenuEntryNodeFound, func, menuSpecification.forUserGroup, context);
        }

        return "OK";
    }

    private MP6043_AddExtMenus_001 fillExtMenus(MenuEntryNode parent, String folderName, String userGroup, InforContext context, String code, String menuType) {
        // With the previous ID found and the menu type determined, fill the request object for both menu item or function item
        String id = parent != null ? parent.getId() : ""; // Which would be the extMenuCode of the parent..
        MP6043_AddExtMenus_001 addExtMenus = new MP6043_AddExtMenus_001();
        ExtMenus extMenus = new ExtMenus();
        addExtMenus.setExtMenus(extMenus);
        extMenus.setUSERGROUPID(new USERGROUPID_Type());
        extMenus.getUSERGROUPID().setUSERGROUPCODE(userGroup);
        extMenus.setFUNCTIONID(new FUNCTIONID_Type());
        if (!menuType.equals("S")) { // If not function, don't set name
            extMenus.getFUNCTIONID().setFUNCTIONDESCRIPTION(folderName);
        }
        extMenus.getFUNCTIONID().setFUNCTIONCODE(code); // Submenu code
        extMenus.setEXTMENUPARENT(id);
        extMenus.setEXTMENUTYPE(menuType);
        extMenus.setSEQUENCENUMBER(100); // Set by default at the end of newly added menu item, to be moved manually if required
        extMenus.setMOBILE("false");

        return addExtMenus;
    }

    private MenuEntryNode performAddFunctionOperation(MenuEntryNode parent, String functionCode, String userGroup, InforContext context) throws InforException {
        MP6043_AddExtMenus_001 addExtMenus = this.fillExtMenus(parent, "", userGroup, context, functionCode, "S");

        // With the request object created, perform the add operation
        MP6043_AddExtMenus_001_Result result = tools.performInforOperation(context, inforws::addExtMenusOp, addExtMenus);

        return new MenuEntryNode(result.getResultData().getExtMenus());
    }

    private MenuEntryNode performAddFolderOperation(MenuEntryNode parent, String folderName, String userGroup, InforContext context) throws InforException {
        String menuType = "F"; // Assume entry is submenu
        if (parent.getDescription().equals("ROOT_NODE")) {
            menuType = "M"; // Entry is main menu
        }

        MP6043_AddExtMenus_001 addExtMenus = this.fillExtMenus(parent, folderName, userGroup, context, "BSFOLD", menuType);

        // With the request object created, perform the add operation
        MP6043_AddExtMenus_001_Result result = tools.performInforOperation(context, inforws::addExtMenusOp, addExtMenus);

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

    private MenuEntryNode getExtMenuHierarchyAsTree(InforContext context, MenuSpecification menuSpecification) throws InforException {
        MenuEntryNode root = new MenuEntryNode();
        ExtMenusHierarchy result = this.getExtMenuHierarchy(context, menuSpecification.forUserGroup);

        List<MENU_Type> menus = result.getMENU();
        for (MENU_Type menu : menus) {
            MenuEntryNode mainMenuEntry = new MenuEntryNode(menu);
            root.add(mainMenuEntry);
            menu.getFOLDER().stream().forEach(childFolder -> this.addFolderToMenuNode(mainMenuEntry, childFolder));
            menu.getFUNCTION().stream().forEach(childFunction -> this.addFunctionToMenuNode(mainMenuEntry, childFunction));
        }

        return root;
    }


    private void validateInputNode(MenuSpecification ms) throws InforException {
        String path = ms.menuPath;
        String func = ms.functionCode;
        String userGroup = ms.forUserGroup;
        if (path == null || func == null || userGroup == null) {
            throw tools.generateFault("Menu specifications cannot be null");
        }
        if (path.isEmpty()) {
            throw tools.generateFault("Path cannot be empty");
        }
        if (path.startsWith("/")) {
            throw tools.generateFault("Path cannot start with '/'");
        }
        if (path.endsWith("/")) {
            throw tools.generateFault("Path cannot end with '/'");
        }
        if (path.contains("//")) {
            throw tools.generateFault("Path cannot have empty path items");
        }
        if (path.contains(" ")) {
            throw tools.generateFault("Linear Reference ID must be present.");
        }

        return;
    }

    /**
     * Deletes a function item, or a menu item with all its children.
     * @param context the user credentials
     * @param menuSpecification the specified path and function to delete, for a specific user group
     * @return
     */
    @Override
    public String deleteFromMenuHierarchy(InforContext context, MenuSpecification menuSpecification) throws InforException {
        this.validateInputNode(menuSpecification);

        // Get menu entries as tree
        MenuEntryNode menuRoot = this.getExtMenuHierarchyAsTree(context, menuSpecification);

        List<String> pathList = Arrays.asList(menuSpecification.menuPath.split("\\/"));
        MenuEntryNode latestMenuEntryNodeFound = this.getLatestMenuEntryByPath(pathList, menuRoot);

        // If the specified path doesn't exist, we'll assume it's a problem
        if (latestMenuEntryNodeFound.getLevel() != pathList.size()) {
            throw tools.generateFault("Path doesn't exist");
        }

        // Else, the path specified exists, so delete function if there is one specified
        String func = menuSpecification.functionCode;
        if (func != null && !func.isEmpty()) { // Function is specified
            for (MenuEntryNode child : latestMenuEntryNodeFound.getChildren()) {
                if (child.getFunctionId().equals(func)) { // Find function (allows not finding), also delete all target function children
                    this.performDeleteFunctionOperation(child, menuSpecification.functionCode, menuSpecification.forUserGroup, context);
                }
            }
        } else { // Or delete last element in path if no function specified
            this.performDeleteFolderOperation(latestMenuEntryNodeFound, menuSpecification.functionCode, menuSpecification.forUserGroup, context);
        }

        return "OK";
    }

    private void performDeleteFunctionOperation(MenuEntryNode entryToDelete, String functionCode, String userGroup, InforContext context) throws InforException {
        // Find id of the menu item to be removed
        entryToDelete.getFunctionId();
        this.performDeleteOperation(entryToDelete, functionCode, userGroup, context, "S");
    }

    private void performDeleteFolderOperation(MenuEntryNode entryToDelete, String functionCode, String userGroup, InforContext context) throws InforException {
        String menuType = "F"; // Assume entry is submenu
        if (entryToDelete.getParentMenuEntry().getDescription().equals("ROOT_NODE")) {
            menuType = "M"; // Entry is main menu
        }
        this.performDeleteOperation(entryToDelete, "BSFOLD", userGroup, context, menuType);
    }

    private void performDeleteOperation(MenuEntryNode entryToDelete, String functionCode, String userGroup, InforContext context, String menuType) throws InforException {
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
        tools.performInforOperation(context, inforws::deleteExtMenusOp, deleteExtMenus);
    }
}
