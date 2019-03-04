package ch.cern.eam.wshub.core.services;

/**
 * List of infor operations supported by this library
 */
public enum INFOR_OPERATION {

    // WORK ORDERS
    WORKORDER_CREATE("WORKORDER_C"),
    WORKORDER_READ("WORKORDER_R"),
    WORKORDER_U("WORKORDER_U"),
    WORKORDER_D("WORKORDER_D"),
    WO_STATUS_U("WO_STATUS_U"),
    STANDARDWO_READ("STANDARDWO_R"),
    WORKORDERS_BC("WORKORDERS_BC"),
    WORKORDER_BR("WORKORDER_BR"),
    WORKORDER_BU("WORKORDER_BU"),
    WORKORDER_BD("WORKORDER_BD"),
    // ACTIVITIES
    ACTIVITY_C("ACTIVITY_C"),
    ACTIVITY_U("ACTIVITY_U"),
    ACTIVITY_R("ACTIVITY_R"),
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
    WO_CHECKL_R("WO_CHECKL_R"),
    WO_CHECKL_U("WO_CHECKL_U"),
    WO_ROUTEEQ_C("WO_ROUTEEQ_C"),
    WO_ROUTEEQ_D("WO_ROUTEEQ_D"),
    // INSPECTIONS
    ASPECT_C("ASPECT_C"),
    // COMMENTS
    COMMENT_C("COMMENT_C"),
    COMMENT_U("COMMENT_U"),
    COMMENT_R("COMMENT_R"),
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
    // EQUIPMENT OTHER
    EQP_REPL("EQP_REPL"),
    EQP_GRAPH_R("EQP_GRAPH_R"),
    EQP_CAMP_C("EQP_CAMP_C"),
    // MATERIAL
    PART_C("PART_C"),
    PART_U("PART_U"),
    PART_R("PART_R"),
    PART_D("PART_D"),
    ISSUE_RET("ISSUE_RET"),
    PARTSTORE_C("PARTSTORE_C"),
    PARTSTORE_U("PARTSTORE_U"),
    PARTSUPP_C("PARTSUPP_C"),
    PARTSTOCK_C("PARTSTOCK_C"),
    PARTSTOCK_U("PARTSTOCK_U"),
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
    // MISC
    LOGIN("LOGIN"),
    CUSTFIELDS_R("CUSTFIELDS_R"),
    EAMUSER_R("EAMUSER_R"),
    // GRIDS
    GRID_R("GRID_R"),
    GRIDMETAD_R("GRIDMETAD_R"),
    GRIDDDSPY_R("GRIDDDSPY_R"),
    DDSPYFIELD_R("DDSPYFIELD_R"),
    // USER SETUP
    USERSETUP_R("USERSETUP_R"),
    USERSETUP_C("USERSETUP_C"),
    USERSETUP_U("USERSETUP_U"),
    USERSETUP_D("USERSETUP_D"),
    // EMPLOYEE
    EMPLOYEE_R("EMPLOYEE_R"),
    EMPLOYEE_C("EMPLOYEE_C"),
    EMPLOYEE_U("EMPLOYEE_U"),
    EMPLOYEE_D("EMPLOYEE_D")
    ;

    private String code;

    INFOR_OPERATION(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
