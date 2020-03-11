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

import java.util.ArrayList;
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

    private void addFolderToEntries(List<GenericMenuEntry> entries, FOLDER_Type parent) {
        entries.add(new GenericMenuEntry(parent));
        for(FOLDER_Type child : parent.getFOLDER()) {
            addFolderToEntries(entries, child);
        }
    }

    private class GenericMenuEntry {
        private String id;
        private String description;
        private String parent;

        public GenericMenuEntry(FOLDER_Type folder) {
            id = folder.getEXTMENUCODE();
            description = folder.getFOLDERID().getFOLDERDESCRIPTION();
            parent = folder.getEXTMENUPARENT();
        }

        public GenericMenuEntry(MENU_Type menu) {
            id = menu.getEXTMENUCODE();
            description = menu.getMENUID().getMENUDESCRIPTION();
            parent = menu.getEXTMENUPARENT();
        }

        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
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
            for(FOLDER_Type folder : menu.getFOLDER()) {
                addFolderToEntries(menuEntries, folder);
            }
        }

        return menuEntries;
    }

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
        GenericMenuEntry previous = null;

        if (words.length > 0) {System.out.println("WORDS1: " + words[0]);}
        for(String next : words) {
            System.out.println("Current word:" + next);
            for(GenericMenuEntry gme : menuEntries) {
                if(gme.description.equals(next) && (previous == null || gme.parent.equals(previous.getId()))) {
                    System.out.println("!! IN");
                    previous = gme;
                    System.out.println("!!! Parent Found (id, parent, description): " + gme.id + "\t" + gme.parent + "\t" + gme.description);
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

    @Override
    public String addToMenuHierarchy(InforContext context, MenuSpecification node) throws InforException {
        //TODO validate node object (and check if path is correct (regex, throw))
        //TODO for now, we assume that the path provided exists (all except last entry, in case of menu item). Next step is to implement mutiple directories creation
//        try {

        // Get menu entries as list
        List<GenericMenuEntry> menuEntries = this.getExtMenuHierarchyAsList(context, node);

        // Find parent id of new entry/item from previous list
        String[] words = node.path.split("\\/");
        GenericMenuEntry previous = this.getParentIdOfNewEntryFromList(menuEntries, words);

        // Decide menu type: if function type is not set, then assume it's a menu and not a function
//        String menuType = this.decideMenuType(node.menuCode, previous);
        String menuType = this.decideMenuType(node.menuCode, words);

        // With the previous ID found and the menu type determined, fill the request object for both menu item or function item
        String id = previous.getId(); // Which would be the extMenuCode of the parent..
        System.out.println("Parent: " + previous.getId() + " " + previous.getDescription() + " " + previous.getClass());
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
        extMenus.setSEQUENCENUMBER(100);
        extMenus.setMOBILE("false");

        // With the request object created, perform the add operation
       tools.performInforOperation(context, inforws::addExtMenusOp, addExtMenus);

        return "OK";

//        }catch(Exception e) {
//            System.out.println("ERR " + e.getMessage());
//            e.printStackTrace();
//        }
//        return null;
    }



    private GenericMenuEntry getEntryByPathFromList(List<GenericMenuEntry> menuEntries, String[] words) {
        GenericMenuEntry toDelete = null;

        if (words.length > 0) {System.out.println("WORDS1: " + words[0]);}
        for(String next : words) {
            System.out.println(next);
            for(GenericMenuEntry gme : menuEntries) {
                System.out.println(gme.getId());
                if (gme.getId().equals("54102")) {
                    break;
                }
                // If the next word in the path is equals to the description of the current item, and the parent item ID
                // is equals to the current item ID (or is in root hierarchy)
                System.out.println("GME: description " + gme.description + ", parent " + gme.parent + ", id: " + gme.getId());
                if(gme.description.equals(next) && (toDelete == null || gme.parent.equals(toDelete.getId()))) {
//                if(gme.description.equals(next) && (toDelete == null || gme.parent.equals(toDelete))) {
                    toDelete = gme;
                    System.out.println("!!! Parent Found (id, parent, description): " + gme.id + "\t" + gme.parent + "\t" + gme.description);
                }
            }
        }

        return toDelete; //TODO Should not be null (check after method call)
    }

    @Override
    public String deleteFromMenuHierarchy(InforContext context, MenuSpecification node) throws InforException {
        //TODO validate node object (and check if path is correct (regex, throw))
        //TODO for now, we assume that the path provided exists (all except last entry, in case of menu item). Next step is to implement mutiple directories creation

//        try {

        // Get menu entries as list
        List<GenericMenuEntry> menuEntries = this.getExtMenuHierarchyAsList(context, node);

        // Find id of the item to be removed
        String[] words = node.path.split("\\/");
        GenericMenuEntry entryToDelete = this.getEntryByPathFromList(menuEntries, words);

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
        extMenus.setEXTMENUTYPE(this.decideMenuType(node.menuCode, words));

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
