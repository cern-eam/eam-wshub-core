package ch.cern.eam.wshub.core.services.administration.entities;
import net.datastream.schemas.mp_entities.extmenus_001.ExtMenus;
import net.datastream.schemas.mp_fields.FOLDER_Type;
import net.datastream.schemas.mp_fields.FUNCTION_Type;
import net.datastream.schemas.mp_fields.MENU_Type;
import static ch.cern.eam.wshub.core.tools.DataTypeTools.decodeBoolean;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuEntryNode extends DefaultMutableTreeNode {
    private static final String ROOT_NODE = "ROOT_NODE";
    private String id;
    private String description;
    private String functionId;
    private String systemFunctionId;
    private boolean isHidden;
    private long sequenceNumber;
    private boolean readAllowed;
    private boolean creationAllowed;
    private boolean deleteAllowed;
    private boolean updateAllowed;


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
        this.systemFunctionId = function.getSYSTEMFUNCTION();
        this.isHidden = decodeBoolean(function.getHIDEMENU());
        this.sequenceNumber = function.getSEQUENCENUMBER();
        if (function.getUSERGROUPPERMISSIONS() != null) {
            this.readAllowed = "?".equals(function.getUSERGROUPPERMISSIONS().getSELECTPERMISSION());
            this.creationAllowed = "+".equals(function.getUSERGROUPPERMISSIONS().getINSERTPERMISSION());
            this.updateAllowed = "*".equals(function.getUSERGROUPPERMISSIONS().getUPDATEPERMISSION());
            this.deleteAllowed = "X".equals(function.getUSERGROUPPERMISSIONS().getDELETEPERMISSION());
        }
    }

    public MenuEntryNode(ExtMenus entryAdded) {
        super();
        Objects.requireNonNull(entryAdded);
        this.id = entryAdded.getEXTMENUID().getEXTMENUCODE();
        this.description = entryAdded.getFUNCTIONID().getFUNCTIONDESCRIPTION();
        this.functionId = entryAdded.getFUNCTIONID().getFUNCTIONCODE();
        this.sequenceNumber = entryAdded.getSEQUENCENUMBER();
    }

    public MenuEntryNode() { // For root only
        super();
        this.description = this.ROOT_NODE;
    }

    public List<MenuEntryNode> getChildren() {
        List<MenuEntryNode> children = new ArrayList<MenuEntryNode>();
        for (int i = 0; i < this.getChildCount(); i++) {
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

    public long getSequenceNumber() {
        return this.sequenceNumber;
    }

    public MenuEntryNode getParentMenuEntry() {
        return (MenuEntryNode) this.getParent();
    }

    public boolean isReadAllowed() {
        return readAllowed;
    }

    public void setReadAllowed(boolean readAllowed) {
        this.readAllowed = readAllowed;
    }

    public boolean isCreationAllowed() {
        return creationAllowed;
    }

    public void setCreationAllowed(boolean creationAllowed) {
        this.creationAllowed = creationAllowed;
    }

    public boolean isDeleteAllowed() {
        return deleteAllowed;
    }

    public void setDeleteAllowed(boolean deleteAllowed) {
        this.deleteAllowed = deleteAllowed;
    }

    public boolean isUpdateAllowed() {
        return updateAllowed;
    }

    public void setUpdateAllowed(boolean updateAllowed) {
        this.updateAllowed = updateAllowed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getSystemFunctionId() {
        return systemFunctionId;
    }

    public void setSystemFunctionId(String systemFunctionId) {
        this.systemFunctionId = systemFunctionId;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }
}
