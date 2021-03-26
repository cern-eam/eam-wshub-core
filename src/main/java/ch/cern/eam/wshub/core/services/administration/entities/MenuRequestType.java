package ch.cern.eam.wshub.core.services.administration.entities;

public enum MenuRequestType {

    ALL("All"),
    EXCLUDE_PERMISSIONS("ExcludePermissions"),
    EXCLUDE_TABS("ExcludeTabs"),
    EXCLUDE_PERMISSIONSAND_TABS("ExcludePermissionsandTabs"),
    EXCLUDE_HIDDEN_PERMISSIONSAND_TABS("ExcludeHiddenPermissionsandTabs");
    private final String value;

    MenuRequestType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MenuRequestType fromValue(String v) {
        for (MenuRequestType c : MenuRequestType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
