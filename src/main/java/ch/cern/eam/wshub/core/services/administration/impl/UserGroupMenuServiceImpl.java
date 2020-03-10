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

    @Override
    public String addToMenuHierarchy(InforContext context, MenuSpecification node) throws InforException {
        // Get menu entries as list
        List<GenericMenuEntry> menuEntries = this.getExtMenuHierarchyAsList(context, node);

        // Find parent id of new entry
        GenericMenuEntry previous = null;
        String[] words = node.path.split("\\/");
        for(String next : words) {
            for(GenericMenuEntry gme : menuEntries) {
                if(gme.description.equals(next) && (previous == null || gme.parent.equals(previous))) {
                    previous = gme;
                    System.out.println("!!!" + gme.id + "\t" + gme.parent + "\t" + gme.description);
                }
            }
        }

        // With the previous ID found, fill the request object
        String id = previous.getId();

        System.out.println("Parent: " + previous.getId() + " " + previous.getDescription() + " " + previous.getClass());

        MP6043_AddExtMenus_001 addExtMenus = new MP6043_AddExtMenus_001();

        ExtMenus extMenus = new ExtMenus();
        addExtMenus.setExtMenus(extMenus);

        extMenus.setUSERGROUPID(new USERGROUPID_Type());
        extMenus.getUSERGROUPID().setUSERGROUPCODE(node.userGroup);

        extMenus.setFUNCTIONID(new FUNCTIONID_Type());
        extMenus.getFUNCTIONID().setFUNCTIONCODE(node.menuCode);

        extMenus.setEXTMENUPARENT(id);
        extMenus.setEXTMENUTYPE("S");
        extMenus.setSEQUENCENUMBER(100);
        extMenus.setMOBILE("false");

        // With the request object created, perform the add operation
        //tools.performInforOperation(context, inforws::addExtMenusOp, addExtMenus);

        return "OK";

//        }catch(Exception e) {
//            System.out.println("ERR " + e.getMessage());
//            e.printStackTrace();
//        }
//        return null;
    }
}
