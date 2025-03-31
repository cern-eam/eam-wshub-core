package ch.cern.eam.wshub.core.services;

/**
 * List of infor operations supported by this library
 */
public enum INFOR_OPERATION {

    // WORK ORDERS
    WORKORDER_CREATE("WORKORDER_C"),
    WORKORDER_READ("WORKORDER_R"),
    WORKORDER_READ_DEFAULT("WORKORDER_RD"),
    WORKORDER_UPDATE("WORKORDER_U"),
    WORKORDER_DELETE("WORKORDER_D"),
    WO_STATUS_UPDATE("WO_STATUS_U"),
    // STANDARD WORK ORDERS
    STANDARDWO_READ("STDWO_R"),
    STANDARDWO_CREATE("STDWO_C"),
    STANDARDWO_UPDATE("STDWO_U"),
    STANDARDWO_DELETE("STDWO_D"),
    // STANDARD WORK ORDER CHILD
    STANDARDWOCHILD_READ_DEFAULT("STDWOC_RD"),
    STANDARDWOCHILD_CREATE("STDWOC_C"),
    STANDARDWOCHILD_READ("STDWOC_R"),
    STANDARDWOCHILD_UPDATE("STDWOC_U"),
    STANDARDWOCHILD_DELETE("STDWOC_D"),

    WORKORDER_BC("WORKORDER_BC"),
    WORKORDER_BR("WORKORDER_BR"),
    WORKORDER_BU("WORKORDER_BU"),
    WORKORDER_BD("WORKORDER_BD"),
    // ACTIVITIES
    ACTIVITY_C("ACTIVITY_C"),
    ACTIVITY_U("ACTIVITY_U"),
    ACTIVITY_R("ACTIVITY_R"),
    ACTIVITY_D("ACTIVITY_D"),
    // LABOR BOOKING
    LABOR_BOK_R("LABOR_BOK_R"),
    LABOR_BOK_C("LABOR_BOK_C"),
    // PURCHASE ORDERS
    PURCHORDE_U("PURCHORDE_U"),
    // CASE MANAGEMENT
    CASE_R("CASE_R"),
    CASE_C("CASE_C"),
    CASE_U("CASE_U"),
    CASE_D("CASE_D"),
    // CASE MANAGEMENT 2
    CASEMGMT_R("CASEMGMT_R"),
    CASEMGMT_C("CASEMGMT_C"),
    CASEMGMT_U("CASEMGMT_U"),
    CASEMGMT_D("CASEMGMT_D"),

    // CASE TASKS
    CASE_TASK_R("CASE_TASK_R"),
    CASE_TASK_C("CASE_TASK_C"),
    CASE_TASK_U("CASE_TASK_U"),
    CASE_TASK_D("CASE_TASK_D"),
    CASE_TASK_RM("CASE_TASK_RM"),
    // WORK ORDERS MISC
    METERRREAD_C("METERRREAD_C"),
    WO_ADDCOST_C("WO_ADDCOST_C"),
    WOPART_C("WOPART_C"),
    WO_MATLIST_C("WO_MATLIST_C"),
    TP_CHECKLI_C("TP_CHECKLI_C"),
    WO_CHECKL_FOLLOWUPWO_C("WO_CHECKL_FOLLOWUPWO_C"),
    TASKPLAN_C("TASKPLAN_C"),
    TASKPLAN_R("TASKPLAN_R"),
    WO_CHECKL_R("WO_CHECKL_R"),
    WO_CHECKL_U("WO_CHECKL_U"),
    WO_CHECKL_ESGN_R("WO_CHECKL_ESGN_R"),
    WO_CHECKL_ESGN_U("WO_CHECKL_ESGN_U"),
    WO_CHECKLDEF_R("WO_CHECKLDEF_R"),
    WO_ROUTEEQ_C("WO_ROUTEEQ_C"),
    WO_ROUTEEQ_D("WO_ROUTEEQ_D"),
    ROUTE_C("ROUTE_C"),
    ROUTE_R("ROUTE_R"),
    // INSPECTIONS
    ASPECT_C("ASPECT_C"),
    // COMMENTS
    COMMENT_C("COMMENT_C"),
    COMMENT_U("COMMENT_U"),
    COMMENT_R("COMMENT_R"),
    COMMENT_D("COMMENT_D"),
    // EQUIPMENT
    EQUIPMENT_U("EQUIPMENT_U"),
    EQUIPMENT_C("EQUIPMENT_C"),
    EQUIPMENT_R("EQUIPMENT_R"),
    EQUIPMENT_D("EQUIPMENT_D"),
    EQUIPMENT_BC("EQUIPMENT_BC"),
    EQUIPMENT_BR("EQUIPMENT_BR"),
    EQUIPMENT_BU("EQUIPMENT_BU"),
    EQUIPMENT_BD("EQUIPMENT_BD"),
    EQP_CODE_U("EQP_CODE_U"),
    // EQUIPMENT LINEAR REFERENCES
    EQP_LINREF_C("EQP_LINREF_C"),
    EQP_LINREF_U("EQP_LINREF_U"),
    EQP_LINREF_D("EQP_LINREF_D"),
    // EQUIPMENT STRUCTURE
    EQP_STR_C("EQP_STR_C"),
    EQP_STR_U("EQP_STR_U"),
    EQP_STR_D("EQP_STR_D"),
    // EQUIPMENT WARRANTY COVERAGE
    EQP_WARR_C("EQP_WARR_C"),
    EQP_WARR_U("EQP_WARR_U"),
    // EQUIPMENT PM SCHEDULES
    EQP_PMSCH_C("EQP_PMSCH_C"),
    EQP_PMSCH_D("EQP_PMSCH_D"),
    EQP_PMSCH_U("EQP_PMSCH_U"),
    // EQUIPMENT DEPRECIATION
    EQP_DEPR_C("EQP_DEPR_C"),
    EQP_DEPR_U("EQP_DEPR_U"),
    // EQUIPMENT CONFIGURATION
    EQP_CONF_C("EQP_CONF_C"),
    EQP_CONF_R("EQP_CONF_R"),
    EQP_CONF_U("EQP_CONF_U"),
    EQP_CONF_D("EQP_CONF_D"),
    EQP_CONF_RD("EQP_CONF_RD"),
    EQP_CONF_BC("EQP_CONF_BC"),
    EQP_CONF_BR("EQP_CONF_BR"),
    EQP_CONF_BU("EQP_CONF_BU"),
    EQP_CONF_BD("EQP_CONF_BD"),
    // EQUIPMENT GENERATION
    EQP_GEN_C("EQP_GEN_C"),
    EQP_GEN_U("EQP_GEN_U"),
    EQP_GEN_D("EQP_GEN_D"),
    EQP_GEN_G("EQP_GEN_G"),

    /// EQUIPMENT METER READINGS
    EQP_METR_C("EQP_METR_C"),
    EQP_METR_R("EQP_METR_R"),
    EQP_METR_RD("EQP_METR_RD"),
    EQP_METR_U("EQP_METR_U"),
    EQP_METR_D("EQP_METR_D"),

    // EQUIPMENT RESERVATION
    EQP_RES_C("EQP_RES_C"),
    EQP_RES_R("EQP_RES_R"),
    EQP_RES_U("EQP_RES_U"),
    EQP_RES_D("EQP_RES_D"),
    EQP_RES_RA("EQP_RES_RA"),

    EQP_RES_BC("EQP_RES_BC"),
    EQP_RES_BR("EQP_RES_BR"),
    EQP_RES_BU("EQP_RES_BU"),
    EQP_RES_BD("EQP_RES_BD"),

    // EQUIPMENT RESERVATION ADJUSTMENT
    EQP_RES_ADJ_C("EQP_RES_ADJ_C"),
    EQP_RES_ADJ_R("EQP_RES_ADJ_R"),
    EQP_RES_ADJ_U("EQP_RES_ADJ_U"),
    EQP_RES_ADJ_D("EQP_RES_ADJ_D"),

    // EQUIPMENT OTHER
    EQP_REPL("EQP_REPL"),
    EQP_GRAPH_R("EQP_GRAPH_R"),
    EQP_CAMP_C("EQP_CAMP_C"),

    // NONCONFORMITIES
    NONCONF_OBS_C("NONCONF_OBS_C"),
    NONCONF_OBS_R("NONCONF_OBS_R"),
    NONCONF_OBS_U("NONCONF_OBS_U"),
    NONCONF_OBS_D("NONCONF_OBS_D"),

    NONCONF_C("NONCONF_C"),
    NONCONF_R("NONCONF_R"),
    NONCONF_RD("NONCONF_RD"),
    NONCONF_U("NONCONF_U"),
    NONCONF_D("NONCONF_D"),

    // LOCATION
    LOCATION_U("LOCATION_U"),
    LOCATION_C("LOCATION_C"),
    LOCATION_R("LOCATION_R"),
    LOCATION_D("LOCATION_D"),
    LOCATION_BU("LOCATION_BU"),
    LOCATION_BC("LOCATION_BC"),
    LOCATION_BR("LOCATION_BR"),
    LOCATION_BD("LOCATION_BD"),
    // MATERIAL
    PART_C("PART_C"),
    PART_U("PART_U"),
    PART_R("PART_R"),
    PART_D("PART_D"),
    PART_BC("PART_BC"),
    PART_BU("PART_BU"),
    PART_BR("PART_BR"),
    PART_BD("PART_BD"),
    ISSUE_RET("ISSUE_RET"),
    PARTSTORE_C("PARTSTORE_C"),
    PARTSTORE_U("PARTSTORE_U"),
    PARTSUPP_C("PARTSUPP_C"),
    PARTSTOCK_C("PARTSTOCK_C"),
    PARTSTOCK_R("PARTSTOCK_R"),
    PARTSTOCK_U("PARTSTOCK_U"),
    // LOT
    PARTLOT_C("PARTLOT_C"),
    // PART MANUFACTURER
    PARTMAN_R("PARTMAN_R"),
    PARTMAN_C("PARTMAN_C"),
    PARTMAN_U("PARTMAN_U"),
    PARTMAN_D("PARTMAN_D"),
    // PART ASSOCIATION
    PARTASSOC_C("PARTASSOC_C"),
    PARTASSOC_D("PARTASSOC_D"),
    PARTSUBS_C("PARTSUBS_C"),
    STOREBIN_C("STOREBIN_C"),
    STOREBIN_R("STOREBIN_R"),
    STOREBIN_U("STOREBIN_U"),
    STOREBIN_D("STOREBIN_D"),

    STORE2STORETRANSFER("STO2STO_TRANSFER"),
    PARTBIN2BIN_C("PARTBIN2BIN_C"),
    PARTBIN2BIN_BC("PARTBIN2BIN_C"),
    // MISC
    LOGIN("LOGIN"),
    CUSTFIELDS_R("CUSTFIELDS_R"),
    EAMUSER_R("EAMUSER_R"),
    // GRIDS
    GRID_BR("GRID_BR"),
    GRID_R("GRID_R"),
    GRIDMETAD_R("GRIDMETAD_R"),
    GRIDDDSPY_R("GRIDDDSPY_R"),
    DDSPYFIELD_R("DDSPYFIELD_R"),
    // USER SETUP
    USERSETUP_R("USERSETUP_R"),
    USERSETUP_C("USERSETUP_C"),
    USERSETUP_U("USERSETUP_U"),
    USERSETUP_UB("USERSETUP_UB"),
    USERSETUP_D("USERSETUP_D"),
    // EMPLOYEE
    EMPLOYEE_R("EMPLOYEE_R"),
    EMPLOYEE_C("EMPLOYEE_C"),
    EMPLOYEE_U("EMPLOYEE_U"),
    EMPLOYEE_D("EMPLOYEE_D"),
    EMPLOYEE_BC("EMPLOYEE_BC"),
    EMPLOYEE_BU("EMPLOYEE_BU"),
    //
    DATASPY_CP("DATASPY_CP"),
    DATASPY_D("DATASPY_D"),
    // PICKTICKET
    PICKTICKET_C("PICKTICKET_C"),
    PICKTICKET_U("PICKTICKET_U"),
    PICKTICKEL_C("PICKTICKEL_C"),
    // USER DEFINED TABLES
    USERDEFINEDTABLE_C("USERDEFINEDTABLE_C"),
    USERDEFINEDTABLE_R("USERDEFINEDTABLE_R"),
    USERDEFINEDTABLE_U("USERDEFINEDTABLE_U"),
    USERDEFINEDTABLE_D("USERDEFINEDTABLE_D"),

    USERDEFINEDLIST_HLR("USERDEFINEDLIST_HLR"),
    USERDEFINEDLIST_HLU("USERDEFINEDLIST_HLU"),

    USERDEFINEDLIST_C("USERDEFINEDLIST_C"),
    USERDEFINEDLIST_R("USERDEFINEDLIST_R"),
    USERDEFINEDLIST_U("USERDEFINEDLIST_U"),
    USERDEFINEDLIST_D("USERDEFINEDLIST_D"),

    // MENU HIERARCHY
    MENU_HIERARCHY_ADD("MENU_HIERARCHY_ADD"),
    MENU_HIERARCHY_READ("MENU_HIERARCHY_READ"),
    MENU_HIERARCHY_DELETE("MENU_HIERARCHY_DELETE"),
    MENU_HIERARCHY_ADD_BATCH("MENU_HIERARCHY_ADD_BATCH"),
    MENU_HIERARCHY_ADD_MANY_USERGROUPS("MENU_HIERARCHY_ADD_MANY_USERGROUPS"),
    MENU_HIERARCHY_DELETE_BATCH("MENU_HIERARCHY_DELETE_BATCH"),
    MENU_HIERARCHY_DELETE_MANY_USERGROUPS("MENU_HIERARCHY_DELETE_MANY_USERGROUPS"),

    // INVENTORY
    INVENTORY_C("INVENTORY_C"),
    INVENTORY_R("INVENTORY_R"),
    INVENTORY_U("INVENTORY_U"),
    INVENTORY_RD("INVENTORY_RD"),
    INVENTORY_RL("INVENTORY_RL"),
    INVENTORY_UL("INVENTORY_UL"),

    // SAFETIES
    SAFETY_BHLR("SAFETY_BHLR"),
    SAFETY_BHLU("SAFETY_BHLU"),
    SAFETY_HLR("SAFETY_HLR"),
    SAFETY_HLU("SAFETY_HLU"),
    SAFETY_R("SAFETY_R"),

    // MEC
    MEC_GET("MEC_GET"),
    MEC_ADD("MEC_ADD"),
    MEC_ADD_BATCH("MEC_ADD_BATCH"),
    MEC_DELETE("MEC_DELETE"),
    MEC_SYNC("MEC_SYNC"),
    MEC_GET_INFOR("MEC_GET_INFOR"),

    WO_EQP_LINEAR_DETAILS_R("WO_EQP_LINEAR_DETAILS_R"),

    SCREEN_LAYOUT_READ("SCREEN_LAYOUT_R"),

    SCREEN_LAYOUT_LOV("SCREEN_LAYOUT_LOV"),

    OTHER("OTHER")
    ;

    private String code;

    INFOR_OPERATION(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
