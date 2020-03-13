package ch.cern.eam.wshub.core.services.administration.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.administration.UserGroupMenuService;
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
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.util.*;

public class UserGroupMenuServiceImpl implements UserGroupMenuService {
    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public UserGroupMenuServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    private void addFolderToEntries(List<GenericMenuEntry> entries, FOLDER_Type parent) {
        entries.add(new GenericMenuEntry(parent));
        for(FOLDER_Type childFolder : parent.getFOLDER()) {
            addFolderToEntries(entries, childFolder);
        }
        for (FUNCTION_Type childFunction : parent.getFUNCTION()) {
            addFunctionToEntries(entries, childFunction);
        }
    }

    private void addFunctionToEntries(List<GenericMenuEntry> entries, FUNCTION_Type parent) {
        entries.add(new GenericMenuEntry(parent));
    }

//    private void addFolderToEntriesMap(List<GenericMenuEntry> entries, FOLDER_Type parent) {
//        entries.add(new GenericMenuEntry(parent));
//        for(FOLDER_Type child : parent.getFOLDER()) {
//            addFolderToEntries(entries, child);
//        }
//    }

    private class GenericMenuEntry {
        private String id;
        private String description;
        private String parent;
        private String functionId;

        public GenericMenuEntry(FOLDER_Type folder) {
            id = folder.getEXTMENUCODE();
            description = folder.getFOLDERID().getFOLDERDESCRIPTION();
            parent = folder.getEXTMENUPARENT();
            functionId = folder.getFOLDERID().getFOLDERCODE();
        }

        public GenericMenuEntry(MENU_Type menu) {
            id = menu.getEXTMENUCODE();
            description = menu.getMENUID().getMENUDESCRIPTION();
            parent = menu.getEXTMENUPARENT();
            functionId = menu.getMENUID().getMENUCODE();
        }

        public GenericMenuEntry(FUNCTION_Type function) {
            id = function.getEXTMENUCODE();
            description = function.getFUNCTIONID().getFUNCTIONDESCRIPTION();
            parent = function.getEXTMENUPARENT();
            functionId = function.getFUNCTIONID().getFUNCTIONCODE();
        }

        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String getFunctionId() {
            return functionId;
        }
    }

    private ExtMenusHierarchy getExtMenuHierarchy(InforContext context, MenuSpecification node) throws InforException {
        MP6005_GetExtMenusHierarchy_001 getExtMenusHierarchy = new MP6005_GetExtMenusHierarchy_001();
        getExtMenusHierarchy.setUSERGROUPID(new USERGROUPID_Type());
        getExtMenusHierarchy.getUSERGROUPID().setUSERGROUPCODE(node.userGroup);

        ExtMenusHierarchy result =
                tools.performInforOperation(context, inforws::getExtMenusHierarchyOp, getExtMenusHierarchy)
                        .getResultData().getExtMenusHierarchy();

        return result;
    }

    private List<GenericMenuEntry> getExtMenuHierarchyAsList(InforContext context, MenuSpecification node) throws InforException {
        ExtMenusHierarchy result = this.getExtMenuHierarchy(context, node);

        List<GenericMenuEntry> menuEntries = new ArrayList<>();
        for(MENU_Type menu : result.getMENU()) {
            menuEntries.add(new GenericMenuEntry(menu));
            for (FOLDER_Type folder : menu.getFOLDER()) {
                this.addFolderToEntries(menuEntries, folder);
            }
            for (FUNCTION_Type childFunction : menu.getFUNCTION()) {
               this.addFunctionToEntries(menuEntries, childFunction);
            }
        }

        return menuEntries;
    }

//    private List<GenericMenuEntry> getExtMenuHierarchyAsHashMap(InforContext context, MenuSpecification node) throws InforException {
//        ExtMenusHierarchy result = this.getExtMenuHierarchy(context, node);
//
//        Map<String, GenericMenuEntry> menuEntries = new HashMap<String, GenericMenuEntry>();
//
//        for (MENU_Type menu : result.getMENU()) {
//            menuEntries.put(menu.getEXTMENUPARENT(), new GenericMenuEntry(menu));
//            for(FOLDER_Type folder : menu.getFOLDER()) {
//                addFolderToEntries(menuEntries, folder);
//            }
//        }
//
////        List<GenericMenuEntry> menuEntries = new ArrayList<>();
////        for(MENU_Type menu : result.getMENU()) {
////            menuEntries.add(new GenericMenuEntry(menu));
////            for(FOLDER_Type folder : menu.getFOLDER()) {
////                addFolderToEntries(menuEntries, folder);
////            }
////        }
//
//        return menuEntries;
//    }

//    private MenuNode getExtMenuHierarchyAsTree(InforContext context, MenuSpecification node) throws InforException {
//        ExtMenusHierarchy result = this.getExtMenuHierarchy(context, node);
//
//        MenuNode tree = new MenuNode();
//
//        List<GenericMenuEntry> menuEntries = new ArrayList<>();
//        for(MENU_Type menu : result.getMENU()) {
//            menuEntries.add(new GenericMenuEntry(menu));
//            for(FOLDER_Type folder : menu.getFOLDER()) {
//                addFolderToEntries(menuEntries, folder);
//            }
//        }
//
//        return menuEntries;
//    }


    private GenericMenuEntry getParentIdOfNewEntryFromList(List<GenericMenuEntry> menuEntries, String[] words) {
        GenericMenuEntry current = null;
        GenericMenuEntry previous = null;

        Boolean gmeFound = false;
        if (words.length > 0) {System.out.println("WORDS1: " + words[0]);}
        for(String next : words) {
            gmeFound = false;
            System.out.println("Current word:" + next);
            for(GenericMenuEntry gme : menuEntries) {
                if(!gmeFound && gme.description.equals(next) && (current == null || gme.parent.equals(current.getId()))) {
                    System.out.println("!! IN");
                    previous = current;
                    current = gme;
                    gmeFound = true;
                    System.out.println("!!! Parent Found (id, parent, description): " + gme.id + "\t" + gme.parent + "\t" + gme.description);
//                    break;
                }
            }
        }
        //TODO if adding a menu item on Root hierarchy, then the maximum amount is 10 items; it should be checked here

        return previous; // Can be null if it's a main menu item
    }

    private String decideMenuType(String menuCode, String[] path) {
        String menuType;
        if (menuCode == null || menuCode.isEmpty()) {
            if (path.length < 1) {
                System.out.println("Main menu item");
                menuType = "M"; // Item is main menu
            } else {
                menuType = "F"; // Item is submenu
            }
        } else {
            menuType = "S"; // Item is function
        }
        //TODO issue, if two paths are identical, item will be added unreliably to one of them, as no item code is given (only path) to this function

        return menuType;
    }

//    private void addMissingPathToMenuHierarchy(String[] missingPath) {
//        for (String currentPathStep: missingPath) {
//            this.addToMenuHierarchy()
//        }
//    }

//    private String[] calculateMissingPath(String[] words, List<GenericMenuEntry> menuEntries) {
//        for (String currentPathStep: words) {
//            this.getEntryByPathFromList(menuEntries, words);
//        }
//    }


//    private String[] calculateExistingPath(String[] words, List<GenericMenuEntry> menuEntries) {
//
//
//        GenericMenuEntry current = null;
//        GenericMenuEntry previous = null;
//
//        Boolean gmeFound = false;
//        for(String next : words) {
//            gmeFound = false;
//            for(GenericMenuEntry gme : menuEntries) {
//                if(!gmeFound && gme.description.equals(next) && (current == null || gme.parent.equals(current.getId()))) {
//                    System.out.println("!! IN");
//                    previous = current;
//                    current = gme;
//                    gmeFound = true;
//                    System.out.println("!!! Parent Found (id, parent, description): " + gme.id + "\t" + gme.parent + "\t" + gme.description);
////                    break;
//                }
//            }
//        }
//        //TODO if adding a menu item on Root hierarchy, then the maximum amount is 10 items; it should be checked here
//
//        return previous; // Can be null if it's a main menu item
//    }


    /**
     * //TODO write jdoc
     * @param context
     * @param node
     * @return
     * @throws InforException
     */
    @Override
    public String addToMenuHierarchy(InforContext context, MenuSpecification node) throws InforException {
        //TODO validate node object (and check if path is correct (regex, throw))
        //TODO for now, we assume that the path provided exists (all except last entry, in case of menu item). Next step is to implement mutiple directories creation
//        try {

        // Get menu entries as list
        List<GenericMenuEntry> menuEntries = this.getExtMenuHierarchyAsList(context, node);

        // Check if path already exists; if so, exception or continue
        String[] words = node.path.split("\\/");
//        String[] existingPath = this.calculateExistingPath(words, menuEntries);
//        if (words.length - existingPath.length == 0) { // Path already exists
//            return "OK";
//        }
//
//        // Check if path is incomplete; if so, complete it (if it doesn't, add all submenus (or menu)) starting from second to last item
//        if ((words.length - existingPath.length) > 1) { // Path is incomplete (or doesn't exist)
////            System.out.println("---1");
////            System.out.println(node.path);
//            String[] oldPath = Arrays.copyOf(words, words.length);
//            node.path = String.join("\\/", Arrays.copyOf(words, words.length-1));
////            this.addMissingPathToMenuHierarchy(missingPath);
//            this.addToMenuHierarchy(context, node);
//            node.path = String.join("\\/", oldPath);
////            System.out.println(node.path);
////            System.out.println("---2");
//        }

        // Add leaf item
        // Decide menu type: if function type is not set, then assume it's a menu and not a function
        String menuType = this.decideMenuType(node.menuCode, words);

        // Find parent id of new entry/item from previous list
        GenericMenuEntry parent;
        if (menuType.equals("M") || menuType.equals("F")) { //TODO better with an enum, even if it's not our design (but might be too cluttered)
            parent = this.getEntryByPathFromList(menuEntries, Arrays.copyOf(words, words.length - 1));
        } else { // If to be added is a function, get end of path
            parent = this.getEntryByPathFromList(menuEntries, words);
        }

        // With the previous ID found and the menu type determined, fill the request object for both menu item or function item
        String id = parent.getId(); // Which would be the extMenuCode of the parent..
        System.out.println("Parent: " + parent.getId() + " " + parent.getDescription() + " " + parent.getClass());
        MP6043_AddExtMenus_001 addExtMenus = new MP6043_AddExtMenus_001();
        ExtMenus extMenus = new ExtMenus();
        addExtMenus.setExtMenus(extMenus);
        extMenus.setUSERGROUPID(new USERGROUPID_Type());
        extMenus.getUSERGROUPID().setUSERGROUPCODE(node.userGroup);
        extMenus.setFUNCTIONID(new FUNCTIONID_Type());
        if (menuType.equals("M") || menuType.equals("F")) { //TODO better with an enum, even if it's not our design (but might be too cluttered)
            node.menuCode = "BSFOLD"; // And set internal BSFOLD code for menu item
            extMenus.getFUNCTIONID().setFUNCTIONDESCRIPTION(words[words.length - 1]); // The last item on the path provided (the name of the menu item to add)
        }
        extMenus.getFUNCTIONID().setFUNCTIONCODE(node.menuCode); // Menu code is function code..
        extMenus.setEXTMENUPARENT(id);
        extMenus.setEXTMENUTYPE(menuType);
        extMenus.setSEQUENCENUMBER(100); //TODO set to a different number depending on position
        extMenus.setMOBILE("false");

        // With the request object created, perform the add operation
       tools.performInforOperation(context, inforws::addExtMenusOp, addExtMenus);

       return "OK";
    }


    private GenericMenuEntry getEntryByPathFromList(List<GenericMenuEntry> menuEntries, String[] words) {
        GenericMenuEntry current = null;

        Boolean gmeFound = false;
        if (words.length > 0) {System.out.println("WORDS1: " + words[0]);}
        for(String next : words) {
            gmeFound = false;
            System.out.println(next);
            for(GenericMenuEntry gme : menuEntries) {
                System.out.println(gme.getId());
//                if (gme.getId().equals("56400")) { //TODO Fix!
//                    break;
//                }
                if (gme.getDescription().equals("TEST1")) {
                    System.out.println("!!!!!");
                }
                // If the next word in the path is equals to the description of the current item, and the parent item ID
                // is equals to the current item ID (or is in root hierarchy)
                System.out.println("GME: description " + gme.description + ", parent " + gme.parent + ", id: " + gme.getId());
                if(!gmeFound && gme.description.equals(next) && (current == null || gme.parent.equals(current.getId()))) {
                    current = gme;
                    gmeFound = true;
                    System.out.println("!!! Parent Found (id, parent, description): " + gme.id + "\t" + gme.parent + "\t" + gme.description);
                }
            }
        }

        return current; //TODO Should not be null (check after method call)
    }

    private GenericMenuEntry findFunctionId(GenericMenuEntry entryToDelete, List<GenericMenuEntry> menuEntries, String functionId) {
        for (GenericMenuEntry gme : menuEntries) {
            if (entryToDelete.getId().equals(gme.parent)) { // If the parent of the current gme is entry to delete
                if (gme.getFunctionId().equals(functionId)) { // If the function is the one we want to delete
                    return gme;
                }
            }
        }

        return null; // TODO throw
    }

    @Override
    public String deleteFromMenuHierarchy(InforContext context, MenuSpecification node) throws InforException {
        //TODO validate node object (and check if path is correct (regex, throw))
        //TODO for now, we assume that the path provided exists (all except last entry, in case of menu item). Next step is to implement mutiple directories creation

//        try {

        // Get menu entries as list
        List<GenericMenuEntry> menuEntries = this.getExtMenuHierarchyAsList(context, node);

        // Check if full path already exists; if not, exception or continue
        String[] words = node.path.split("\\/");
//        String[] existingPath = this.calculateExistingPath(words, menuEntries);
//        if (words.length - existingPath.length > 0) { // Path is incomplete
//            return "OK";
//        }

        // Find id of the menu item to be removed
        GenericMenuEntry entryToDelete = this.getEntryByPathFromList(menuEntries, words);

        // If function is set, delete function and not the menu item
        String menuType = this.decideMenuType(node.menuCode, words);
        if (menuType.equals("S")) {
            entryToDelete = this.findFunctionId(entryToDelete, menuEntries, node.menuCode);
            System.out.println("A");
        }

        // With the id of the item, fill the request object
        String id = entryToDelete.getId(); // Which would be the extMenuCode..
        MP6045_DeleteExtMenus_001 deleteExtMenus = new MP6045_DeleteExtMenus_001();
        ExtMenus extMenus = new ExtMenus();
        deleteExtMenus.setExtMenus(extMenus);
        extMenus.setUSERGROUPID(new USERGROUPID_Type());
        extMenus.getUSERGROUPID().setUSERGROUPCODE(node.userGroup);
        extMenus.setFUNCTIONID(new FUNCTIONID_Type());
        extMenus.getFUNCTIONID().setFUNCTIONCODE(node.menuCode); // Menu code is function code..
        extMenus.setEXTMENUID(new EXTMENUID_Type());
        extMenus.getEXTMENUID().setEXTMENUCODE(id);
        extMenus.setEXTMENUTYPE(menuType);

        System.out.println("MENU ID: " + extMenus.getEXTMENUID().getEXTMENUCODE());
        // With the request object created, perform the add operation
       tools.performInforOperation(context, inforws::deleteExtMenusOp, deleteExtMenus);

        return "OK";

//        }catch(Exception e) {
//            System.out.println("ERR " + e.getMessage());
//            e.printStackTrace();
//        }
//        return null;
    }




}
