package ch.cern.eam.wshub.core.services.administration.entities;

public enum MenuType {
    // WORK ORDERS
    MAIN_MENU("M"),
    SUBMENU("F"),
    FUNCTION("S");

    private String type;

    MenuType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
