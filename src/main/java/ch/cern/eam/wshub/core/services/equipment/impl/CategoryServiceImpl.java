package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.equipment.CategoryService;
import ch.cern.eam.wshub.core.services.equipment.entities.Category;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.equipmentcategory_001.EquipmentCategory;
import net.datastream.schemas.mp_fields.CATEGORYID;
import net.datastream.schemas.mp_functions.mp0323_001.MP0323_AddEquipmentCategory_001;
import net.datastream.schemas.mp_functions.mp0324_001.MP0324_GetEquipmentCategory_001;
import net.datastream.schemas.mp_functions.mp0325_001.MP0325_SyncEquipmentCategory_001;
import net.datastream.schemas.mp_functions.mp0326_001.MP0326_DeleteEquipmentCategory_001;
import net.datastream.schemas.mp_results.mp0323_001.MP0323_AddEquipmentCategory_001_Result;
import net.datastream.schemas.mp_results.mp0324_001.MP0324_GetEquipmentCategory_001_Result;
import net.datastream.schemas.mp_results.mp0325_001.MP0325_SyncEquipmentCategory_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;

public class CategoryServiceImpl implements CategoryService {

    private Tools tools;
    private EAMWebServicesPT eamws;

    public CategoryServiceImpl(Tools tools, EAMWebServicesPT eamws) {
        this.tools = tools;
        this.eamws = eamws;
    }

    @Override
    public Category readCategory(EAMContext context, String categoryCode) throws EAMException {
        Category category = tools.getEAMFieldTools().transformEAMObject(new Category(), readEAMCategory(context, categoryCode), context);
        return category;
    }

    private EquipmentCategory readEAMCategory(EAMContext context, String categoryCode) throws EAMException {
        MP0324_GetEquipmentCategory_001 getEquipmentCategory001 = new MP0324_GetEquipmentCategory_001();
        getEquipmentCategory001.setCATEGORYID(new CATEGORYID());
        getEquipmentCategory001.getCATEGORYID().setCATEGORYCODE(categoryCode);

        MP0324_GetEquipmentCategory_001_Result result = tools.performEAMOperation(context, eamws::getEquipmentCategoryOp, getEquipmentCategory001);
        return result.getResultData().getEquipmentCategory();
    }

    @Override
    public String updateCategory(EAMContext context, Category category) throws EAMException {
        EquipmentCategory equipmentCategory = readEAMCategory(context, category.getCode());
        equipmentCategory.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
                context,
                toCodeString(equipmentCategory.getCLASSID()),
                equipmentCategory.getUSERDEFINEDAREA(),
                category.getClassCode(),
                "OBJ"
        ));
        tools.getEAMFieldTools().transformWSHubObject(equipmentCategory, category, context);
        MP0325_SyncEquipmentCategory_001 syncEquipmentCategory = new MP0325_SyncEquipmentCategory_001();
        syncEquipmentCategory.setEquipmentCategory(equipmentCategory);
        MP0325_SyncEquipmentCategory_001_Result result = tools.performEAMOperation(context, eamws::syncEquipmentCategoryOp, syncEquipmentCategory);
        return result.getResultData().getCATEGORYID().getCATEGORYCODE();
    }

    @Override
    public String createCategory(EAMContext context, Category category) throws EAMException {
        EquipmentCategory equipmentCategory = new EquipmentCategory();
        equipmentCategory.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getEAMCustomFields(
                context,
                toCodeString(equipmentCategory.getCLASSID()),
                equipmentCategory.getUSERDEFINEDAREA(),
                category.getClassCode(),
                "OBJ"
        ));
        tools.getEAMFieldTools().transformWSHubObject(equipmentCategory, category, context);

        MP0323_AddEquipmentCategory_001 addCategory = new MP0323_AddEquipmentCategory_001();
        addCategory.setEquipmentCategory(equipmentCategory);
        MP0323_AddEquipmentCategory_001_Result result = tools.performEAMOperation(context, eamws::addEquipmentCategoryOp, addCategory);
        String categoryCode = result.getResultData().getCATEGORYID().getCATEGORYCODE();
        return categoryCode;
    }

    @Override
    public String deleteCategory(EAMContext context, String categoryCode) throws EAMException {
        MP0326_DeleteEquipmentCategory_001 deleteEquipmentCategory = new MP0326_DeleteEquipmentCategory_001();
        deleteEquipmentCategory.setCATEGORYID(new CATEGORYID());
        deleteEquipmentCategory.getCATEGORYID().setCATEGORYCODE(categoryCode);
        tools.performEAMOperation(context, eamws::deleteEquipmentCategoryOp, deleteEquipmentCategory);
        return categoryCode;
    }


}
