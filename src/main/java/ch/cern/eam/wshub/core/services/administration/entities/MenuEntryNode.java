package ch.cern.eam.wshub.core.services.administration.entities;

import net.datastream.schemas.mp_entities.extmenus_001.ExtMenus;
import net.datastream.schemas.mp_fields.FOLDER_Type;
import net.datastream.schemas.mp_fields.FUNCTION_Type;
import net.datastream.schemas.mp_fields.MENU_Type;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuEntryNode extends DefaultMutableTreeNode {
    private String id;
    private String description;
    private String functionId;
    private long sequenceNumber;


    public MenuEntryNode(FOLDER_Type folder) {
        super();
        Objects.requireNonNull(folder);
        this.id = folder.getEXTMENUCODE();
        this.description = folder.getFOLDERID().getFOLDERDESCRIPTION();
        this.functionId = folder.getFOLDERID().getFOLDERCODE();
        this.sequenceNumber = folder.getSEQUENCENUMBER();
    }

    public MenuEntryNode(MENU_Type menu) {
        super();
        Objects.requireNonNull(menu);
        this.id = menu.getEXTMENUCODE();
        this.description = menu.getMENUID().getMENUDESCRIPTION();
        this.functionId = menu.getMENUID().getMENUCODE();
        this.sequenceNumber = menu.getSEQUENCENUMBER();
    }

    public MenuEntryNode(FUNCTION_Type function) {
        super();
        Objects.requireNonNull(function);
        this.id = function.getEXTMENUCODE();
        this.description = function.getFUNCTIONID().getFUNCTIONDESCRIPTION();
        this.functionId = function.getFUNCTIONID().getFUNCTIONCODE();
        this.sequenceNumber = function.getSEQUENCENUMBER();
    }

    public MenuEntryNode(ExtMenus entryAdded) {
        super();
        Objects.requireNonNull(entryAdded);
        this.id = entryAdded.getEXTMENUID().getEXTMENUCODE();
        this.description = entryAdded.getFUNCTIONID().getFUNCTIONDESCRIPTION();
        this.functionId = entryAdded.getFUNCTIONID().getFUNCTIONCODE();
        this.sequenceNumber = entryAdded.getSEQUENCENUMBER();
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

    public MenuEntryNode() { // For root only
        super();
        this.description = "ROOT_NODE";
    }

    public String getId() { return this.id; }

    public String getDescription() { return this.description; }

    public String getFunctionId() { return this.functionId; }

    public long getSequenceNumber() { return this.sequenceNumber; }

    public MenuEntryNode getParentMenuEntry() { return (MenuEntryNode) this.getParent(); }

}
