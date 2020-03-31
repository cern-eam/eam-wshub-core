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
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.swing.tree.MutableTreeNode;
import java.awt.*;
import java.util.*;
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

//    private void addFolderToEntries(List<GenericMenuEntry> entries, FOLDER_Type parent) {
//        entries.add(new GenericMenuEntry(parent));
//        for(FOLDER_Type childFolder : parent.getFOLDER()) {
//            addFolderToEntries(entries, childFolder);
//        }
//        for (FUNCTION_Type childFunction : parent.getFUNCTION()) {
//            addFunctionToEntries(entries, childFunction);
//        }
//    }
//
//    private void addFunctionToEntries(List<GenericMenuEntry> entries, FUNCTION_Type parent) {
//        entries.add(new GenericMenuEntry(parent));
//    }

    private void addFolderToMenuNode(MenuEntryNode currentNode, FOLDER_Type folder) {
        MenuEntryNode newNode = new MenuEntryNode(folder);
//        newNode.setParent(currentNode);
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
//        newNode.setParent(currentNode);
        currentNode.add(newNode);
    }

    private ExtMenusHierarchy getExtMenuHierarchy(InforContext context, String userGroup) throws InforException {
        System.out.println("3");
        MP6005_GetExtMenusHierarchy_001 getExtMenusHierarchy = new MP6005_GetExtMenusHierarchy_001();
        getExtMenusHierarchy.setUSERGROUPID(new USERGROUPID_Type());
        getExtMenusHierarchy.getUSERGROUPID().setUSERGROUPCODE(userGroup);
        System.out.println("4");

        ExtMenusHierarchy result =
                tools.performInforOperation(context, inforws::getExtMenusHierarchyOp, getExtMenusHierarchy)
                        .getResultData().getExtMenusHierarchy();
        System.out.println("5");

        return result;
    }

//    private ExtMenusHierarchy getExtMenuHierarchy(InforContext context, MenuSpecification node) throws InforException {
//        MP6005_GetExtMenusHierarchy_001 getExtMenusHierarchy = new MP6005_GetExtMenusHierarchy_001();
//        getExtMenusHierarchy.setUSERGROUPID(new USERGROUPID_Type());
//        getExtMenusHierarchy.getUSERGROUPID().setUSERGROUPCODE(node.userGroup);
//
//        ExtMenusHierarchy result =
//                tools.performInforOperation(context, inforws::getExtMenusHierarchyOp, getExtMenusHierarchy)
//                        .getResultData().getExtMenusHierarchy();
//
//        return result;
//    }

//    private List<GenericMenuEntry> getExtMenuHierarchyAsList(InforContext context, MenuSpecification node) throws InforException {
//        ExtMenusHierarchy result = this.getExtMenuHierarchy(context, node);
//
//        List<GenericMenuEntry> menuEntries = new ArrayList<>();
//        for(MENU_Type menu : result.getMENU()) {
//            menuEntries.add(new GenericMenuEntry(menu));
//            menu.getFOLDER().stream().forEach(childFolder -> this.addFolderToEntries(menuEntries, childFolder));
//            menu.getFUNCTION().stream().forEach(childFunction -> this.addFunctionToEntries(menuEntries, childFunction));
//        }
//
//        return menuEntries;
//    }

    private String decideMenuType(String menuCode, String[] path) {
        String menuType;
        if (menuCode == null || menuCode.isEmpty()) {
            if (path.length <= 1) {
                menuType = "M"; // Item is main menu
            } else {
                menuType = "F"; // Item is submenu
            }
        } else {
            menuType = "S"; // Item is function
        }

        //WARN If two paths are identical, item will be added unreliably to one of them, as no item code is given (only path) to this function; we assume correct menu hierarchy already exists
        return menuType;
    }

//    private String[] calculateExistingPath(String[] words, List<GenericMenuEntry> menuEntries) {
//        GenericMenuEntry current = null;
//
//        int amountFound = 0;
//        for (String next : words) {
//            Boolean gmeFound = false;
//            for (GenericMenuEntry gme : menuEntries) {
//                if (!gmeFound && gme.description.equals(next) && (current == null || gme.getParent().equals(current.getId()))) {
//                    current = gme;
//                    gmeFound = true;
//                    amountFound++;
//                }
//            }
//            if (!gmeFound) {
//                return Arrays.copyOf(words, amountFound);
//            }
//        }
//
//        return words; // If reached the end, then all path could be found
//    }


    /**
     * Adds a full menu/submenu/function path to the menu hierarchy.
     * @param context the user credentials
     * @param menuSpecification the specified full path and function to add, for a specific user group
     * @return
     */
    @Override
    public String addToMenuHierarchy(InforContext context, MenuSpecification menuSpecification) throws InforException {
        System.out.println("1");
        this.validateInputNode(menuSpecification);
        System.out.println("2");



        // Get menu entries as tree
        MenuEntryNode menuRoot = this.getExtMenuHierarchyAsTree(context, menuSpecification);
        System.out.println("XXXXX 1 XXXXX");
        // Check if path already exists; if so, continue
        List<String> pathList = Arrays.asList(menuSpecification.desiredFinalPath.split("\\/"));
        MenuEntryNode latestMenuEntryNodeFound = this.getLatestMenuEntryByPath(pathList, menuRoot);
        System.out.println("XXXXX 2 XXXXX");

        // If folder level is equal to path length
        String func = menuSpecification.desiredFinalFunctionCode;
        if (latestMenuEntryNodeFound.getLevel() == pathList.size()) {
            // If function is not set, do nothing
            if (func != null && !func.isEmpty()) {
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
            latestMenuEntryNodeFound.add(this.performAddFolderOperation(context, pathList.get(i)));
        }

        // Add function if it is set
        if (func != null && !func.isEmpty()) {
            latestMenuEntryNodeFound.add(this.performAddFunctionOperation(context, func));
        }

        System.out.println("END HERE");
        return "OK";

//        // Else, menu is incomplete; add all missing menu nodes
//        List<String> missingPathList = pathList.subList(latestMenuEntryNodeFound.getLevel(), pathList.size());
//        for (String menuEntryMissing : missingPathList) {
//            latestMenuEntryNodeFound = this.addMenuEntry(menuEntryMissing, latestMenuEntryNodeFound);
//        }
//        // And add also the function, if exists
//        if ( menuSpecification.desiredFinalFunctionCode != null && !menuSpecification.desiredFinalFunctionCode.isEmpty()) {
//            this.addFunctionEntry(menuSpecification.desiredFinalFunctionCode, latestMenuEntryNodeFound);
//        }


        // Now add leaf item
        // Decide menu type: if function type is not set, then assume it's a menu and not a function
        // Find parent id of new entry/item from previous list
        // With the previous ID found and the menu type determined, fill the request object for both menu item or function item
        // With the request object created, perform the add operation
//       tools.performInforOperation(context, inforws::addExtMenusOp, addExtMenus);

//        System.out.println("END HERE");
////        if(true) return "OK";
//
//
//        return "OK";

//        // Get menu entries as list
//        List<GenericMenuEntry> menuEntries = this.getExtMenuHierarchyAsList(context, node);
//
//        // Check if path already exists; if so, continue
//        String[] words = node.path.split("\\/");
//        String[] existingPath = this.calculateExistingPath(words, menuEntries);
//
//        if (words.length - existingPath.length == 0 && node.menuCode.isEmpty()) { // Path already exists and no function to add
//            return "OK";
//        }
//
//        // Check if path is incomplete; if so, complete it (if it doesn't, add all submenus (or menu)) starting from second to last item
//        if ((words.length - existingPath.length) > 0) { // Path is incomplete (or doesn't exist), since differs more than 0
//            UserGroupMenuService.MenuSpecification recursiveMenuSpecification = new UserGroupMenuService.MenuSpecification(node.path, "", node.userGroup); // If function is there, then we remove only that
//            if (node.menuCode.isEmpty()) { // But if no function, then we remove only one path item at the end
//                recursiveMenuSpecification = new UserGroupMenuService.MenuSpecification(String.join("/", Arrays.copyOf(words, words.length - 1)), node.menuCode, node.userGroup);
//            }
//            this.addToMenuHierarchy(context, recursiveMenuSpecification);
//            menuEntries = this.getExtMenuHierarchyAsList(context, recursiveMenuSpecification); //TODO Calling a get two times is not good; fixed by using a HashMap or a tree in a future iteration
//        }
//
//        // Now add leaf item
//        // Decide menu type: if function type is not set, then assume it's a menu and not a function
//        String menuType = this.decideMenuType(node.menuCode, words);
//
//        // Find parent id of new entry/item from previous list
//        GenericMenuEntry parent;
//        if (menuType.equals("M") || menuType.equals("F")) { //TODO better with an enum, even if it's not our design (but might be too cluttered)
//            parent = this.getEntryByPathFromList(menuEntries, Arrays.copyOf(words, words.length - 1));
//        } else { // If to be added is a function, get end of path
//            parent = this.getEntryByPathFromList(menuEntries, words);
//        }
//
//        // With the previous ID found and the menu type determined, fill the request object for both menu item or function item
//        String id = parent != null ? parent.getId() : ""; // Which would be the extMenuCode of the parent..
//        MP6043_AddExtMenus_001 addExtMenus = new MP6043_AddExtMenus_001();
//        ExtMenus extMenus = new ExtMenus();
//        addExtMenus.setExtMenus(extMenus);
//        extMenus.setUSERGROUPID(new USERGROUPID_Type());
//        extMenus.getUSERGROUPID().setUSERGROUPCODE(node.userGroup);
//        extMenus.setFUNCTIONID(new FUNCTIONID_Type());
//        String menuCodeToSend = node.menuCode;
//        if (menuType.equals("M") || menuType.equals("F")) { //TODO better with an enum, even if it's not our design (but might be too cluttered)
//            menuCodeToSend = "BSFOLD"; // And set internal BSFOLD code for menu item
//            extMenus.getFUNCTIONID().setFUNCTIONDESCRIPTION(words[words.length - 1]); // The last item on the path provided (the name of the menu item to add)
//        }
//        extMenus.getFUNCTIONID().setFUNCTIONCODE(menuCodeToSend); // Menu code is function code..
//        extMenus.setEXTMENUPARENT(id);
//        extMenus.setEXTMENUTYPE(menuType);
//        extMenus.setSEQUENCENUMBER(100); //TODO set to a different number depending on position
//        extMenus.setMOBILE("false");
//
//        // With the request object created, perform the add operation
////       tools.performInforOperation(context, inforws::addExtMenusOp, addExtMenus);

//       return "OK";
    }

    private MutableTreeNode performAddFunctionOperation(InforContext context, String func) {
        return new MenuEntryNode();
    }

    private MutableTreeNode performAddFolderOperation(InforContext context, String s) {
        return new MenuEntryNode();
    }

    private void addFunctionEntry(String desiredFinalFunctionCode, MenuEntryNode latestMenuEntryNodeFound) {
    }

    private MenuEntryNode addMenuEntry(String menuEntryMissing, MenuEntryNode latestMenuEntryNodeFound) {
        return latestMenuEntryNodeFound;
    }

    private MenuEntryNode getLatestMenuEntryByPath(List<String> pathList, MenuEntryNode menuRoot) {
        boolean found = false;
        boolean endReached = false;
        int depthReached = 0;
        MenuEntryNode currentNode = menuRoot;

        for (String currentPathEntry : pathList) {
            for (MenuEntryNode childNode : currentNode.getChildren()) {
                System.out.println(childNode.getDescription() + " equals? " + currentPathEntry);
                if (childNode.getDescription().equals(currentPathEntry)) { // Note: assumes identity by description, and paths unique
                    System.out.println("YES!");
                    currentNode = childNode;
                    found = true;
                    break;
                }
            }
            endReached = !found;
            if (endReached) {
                break;
            }
            depthReached++;
            found = false;
        }

        System.out.println("END REACHED: " + endReached);
        System.out.println("DEPTH REACHED: " + depthReached);

        return currentNode;
    }

//    private List<MenuEntryNode> generateMenuListFromInforLists(List<MENU_Type> menus, List<FOLDER_Type> folders, List<FUNCTION_Type> functions){
//        List<MenuEntryNode> entryNodeList = new ArrayList<MenuEntryNode>();
//        for (MENU_Type menu : menus) {
//            entryNodeList.add(new MenuEntryNode(menu));
//        }
//        for (FOLDER_Type folder : folders) {
//            entryNodeList.add(new MenuEntryNode(folder));
//        }
//        for (FUNCTION_Type function : functions) {
//            entryNodeList.add(new MenuEntryNode(function));
//        }
//
//        return entryNodeList;
//    }

    private MenuEntryNode getExtMenuHierarchyAsTree(InforContext context, MenuSpecification menuSpecification) throws InforException {
        ExtMenusHierarchy result = this.getExtMenuHierarchy(context, menuSpecification.forUserGroup);

        System.out.println("6");
        MenuEntryNode root = new MenuEntryNode();
        System.out.println("7");
        List<FOLDER_Type> folders;
        List<FOLDER_Type> functions;
        List<MENU_Type> menus = result.getMENU();
        for (MENU_Type menu : menus) {
            MenuEntryNode mainMenuEntry = new MenuEntryNode(menu);
            root.add(mainMenuEntry);
            menu.getFOLDER().stream().forEach(childFolder -> this.addFolderToMenuNode(mainMenuEntry, childFolder));
            menu.getFUNCTION().stream().forEach(childFunction -> this.addFunctionToMenuNode(mainMenuEntry, childFunction));
        }
        System.out.println("8");

        for (MenuEntryNode m : root.getChildren()) {
            System.out.println(m.getDescription());
            for (MenuEntryNode m2 : m.getChildren()) {
                System.out.println(m2.getFunctionId() +"\t\t" + m2.getDescription());
            }
        }

        System.out.println("xxxxx");
        System.out.println(root.getDescription() +
                root.getChildren().get(0).getDescription() + " - " +
                root.getChildren().get(0).getChildren().get(0).getDescription() + " - " +
                root.getChildren().get(0).getChildren().get(0).getChildren().get(0).getDescription() + " - " +
                root.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0).getDescription());
        System.out.println("xxxxx");
//        System.out.println(root.getDescription() +
//                root.getChildren().get(7).getDescription() +
//                root.getChildren().get(7).getChildren().get(3).getDescription() +
//                root.getChildren().get(7).getChildren().get(3).getChildren().get(0).getDescription() +
//                root.getChildren().get(7).getChildren().get(3).getChildren().get(0).getChildren().get(0).getDescription());
        System.out.println("xxxxx");

        return root;





//            List<GenericMenuEntry> menuEntries = new ArrayList<>();
//        for(MENU_Type menu : result.getMENU()) {
//            menuEntries.add(new GenericMenuEntry(menu));
//            menu.getFOLDER().stream().forEach(childFolder -> this.addFolderToEntries(menuEntries, childFolder));
//            menu.getFUNCTION().stream().forEach(childFunction -> this.addFunctionToEntries(menuEntries, childFunction));
//        }
//
//        return menuEntries;
    }


    private void validateInputNode(MenuSpecification ms) {
        String path = ms.desiredFinalPath;
        String func = ms.desiredFinalFunctionCode;
        String userGroup = ms.forUserGroup;
        if (path == null || func == null || userGroup == null) {
            tools.generateFault("Menu specifications cannot be null");
        }
        if (path.isEmpty()) {
            tools.generateFault("Path cannot be empty");
        }
        if (path.startsWith("/")) {
            tools.generateFault("Path cannot start with '/'");
        }
        if (path.endsWith("/")) {
            tools.generateFault("Path cannot end with '/'");
        }
        if (path.contains("//")) {
            tools.generateFault("Path cannot have empty path items");
        }
        if (path.contains(" ")) {
            tools.generateFault("Linear Reference ID must be present.");
        }

        return;
    }

//    private GenericMenuEntry getEntryByPathFromList(List<GenericMenuEntry> menuEntries, String[] words) {
//        GenericMenuEntry current = null;
//
//        Boolean gmeFound = false;
//        for(String next : words) {
//            gmeFound = false;
//            for(GenericMenuEntry gme : menuEntries) {
//                // If the next word in the path is equals to the description of the current item, and the parent item ID
//                // is equals to the current item ID (or is in root hierarchy)
//                if(!gmeFound && gme.description.equals(next) && (current == null || gme.getParent().equals(current.getId()))) {
//                    current = gme;
//                    gmeFound = true;
//                }
//            }
//        }
//
//        return current; // Could be null, if it's root folder (check after method call)
//    }

//    private GenericMenuEntry findGMEFunctionId(GenericMenuEntry entryToDelete, List<GenericMenuEntry> menuEntries, String functionId) {
//        for (GenericMenuEntry gme : menuEntries) {
//            if (entryToDelete.getId().equals(gme.getParent()) && // If the parent of the current gme is entry to delete
//                gme.getFunctionId().equals(functionId)) {  // If the function is the one we want to delete
//                return gme;
//            }
//        }
//
//        return null;
//    }

    /**
     * Deletes a function item, or a menu item with all its children.
     * @param context the user credentials
     * @param node the specified path and function to delete, for a specific user group
     * @return
     */
    @Override
    public String deleteFromMenuHierarchy(InforContext context, MenuSpecification node) throws InforException {
//        this.validateInputNode(node);
//
//        // Get menu entries as list
//        List<GenericMenuEntry> menuEntries = this.getExtMenuHierarchyAsList(context, node);
//
//
//        // Check if path already exists; if so, continue
//        String[] words = node.path.split("\\/");
//        String[] existingPath = this.calculateExistingPath(words, menuEntries);
//        if (words.length > existingPath.length) { // Path doesn't exist
//            tools.generateFault("Path doesn't exist");
//        }
//
//        // Find id of the menu item to be removed
//        GenericMenuEntry entryToDelete = this.getEntryByPathFromList(menuEntries, words);
//
//        // If function is set, delete function and not the menu item
//        // If function is set, delete function and not the menu item
//        String menuType = this.decideMenuType(node.menuCode, words);
//        if (menuType.equals("S")) {
//            entryToDelete = this.findGMEFunctionId(entryToDelete, menuEntries, node.menuCode);
//            if (entryToDelete == null) {
//                tools.generateFault("No entry to delete found'");
//            }
//        }
//
//        // With the id of the item, fill the request object
//        String id = entryToDelete.getId(); // Which would be the extMenuCode..
//        MP6045_DeleteExtMenus_001 deleteExtMenus = new MP6045_DeleteExtMenus_001();
//        ExtMenus extMenus = new ExtMenus();
//        deleteExtMenus.setExtMenus(extMenus);
//        extMenus.setUSERGROUPID(new USERGROUPID_Type());
//        extMenus.getUSERGROUPID().setUSERGROUPCODE(node.userGroup);
//        extMenus.setFUNCTIONID(new FUNCTIONID_Type());
//        extMenus.getFUNCTIONID().setFUNCTIONCODE(node.menuCode); // Menu code is function code..
//        extMenus.setEXTMENUID(new EXTMENUID_Type());
//        extMenus.getEXTMENUID().setEXTMENUCODE(id);
//        extMenus.setEXTMENUTYPE(menuType);
//
//        // With the request object created, perform the add operation
//       tools.performInforOperation(context, inforws::deleteExtMenusOp, deleteExtMenus);

        return "OK";
    }
}
