package ch.cern.eam.wshub.core.services.administration.entities;

import net.datastream.schemas.mp_entities.extmenus_001.ExtMenus;
import net.datastream.schemas.mp_fields.FOLDER_Type;
import net.datastream.schemas.mp_fields.FUNCTION_Type;
import net.datastream.schemas.mp_fields.MENU_Type;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuEntryNode extends DefaultMutableTreeNode {
    private String id;
    private String description;
//        private String parent;
    private String functionId;

    public MenuEntryNode(FOLDER_Type folder) {
        super();
        this.id = folder.getEXTMENUCODE();
        this.description = folder.getFOLDERID().getFOLDERDESCRIPTION();
//            this.parent = folder.getEXTMENUPARENT();
        this.functionId = folder.getFOLDERID().getFOLDERCODE();
    }

    public MenuEntryNode(MENU_Type menu) {
        super();
        this.id = menu.getEXTMENUCODE();
        this.description = menu.getMENUID().getMENUDESCRIPTION();
//            this.parent = menu.getEXTMENUPARENT();
        this.functionId = menu.getMENUID().getMENUCODE();
    }

    public MenuEntryNode(FUNCTION_Type function) {
        super();
        this.id = function.getEXTMENUCODE();
        this.description = function.getFUNCTIONID().getFUNCTIONDESCRIPTION();
//            this.parent = function.getEXTMENUPARENT();
        this.functionId = function.getFUNCTIONID().getFUNCTIONCODE();
    }

    public MenuEntryNode() { // For root only
        super();
        this.description = "ROOT_NODE";
    }

    public MenuEntryNode(ExtMenus entryAdded) {
        super();
        this.id = entryAdded.getEXTMENUID().getEXTMENUCODE();
        this.description = entryAdded.getFUNCTIONID().getFUNCTIONDESCRIPTION();
//            this.parent = function.getEXTMENUPARENT();
        this.functionId = entryAdded.getFUNCTIONID().getFUNCTIONCODE();
    }

    public List<MenuEntryNode> getChildren() {
        List<MenuEntryNode> children = new ArrayList<MenuEntryNode>();
        for (int i = 0 ; i < this.getChildCount() ; i++) {
            children.add((MenuEntryNode) this.getChildAt(i));
        }

        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof MenuEntryNode)) {
            return false;
        }

        MenuEntryNode other = (MenuEntryNode) o;
        return this.getDescription().equals(other.getDescription()) &&
                this.getFunctionId().equals(other.getFunctionId()) &&
                this.getParentMenuEntry().equals(other.getParentMenuEntry()); // Also checks path
    }

    public String getId() {
            return this.id;
        }

        public String getDescription() {
            return this.description;
        }

        public String getFunctionId() {
            return this.functionId;
        }

        public MenuEntryNode getParentMenuEntry() { return (MenuEntryNode) this.getParent(); }

    public void generateTreeFromFromMap(HashMap<MenuEntryNode, List<MenuEntryNode>> lookup) {

    }

    public void generateMapFromMenuListFromPopulatedRoot(MenuEntryNode root) {
        HashMap<MenuEntryNode, List<MenuEntryNode>> map = new HashMap<MenuEntryNode, List<MenuEntryNode>>();

    }


    public void addAllFoldersAndFunctionsThatAreChildrenRecursively(List<FOLDER_Type> folder, List<FUNCTION_Type> function) {
    }
}
