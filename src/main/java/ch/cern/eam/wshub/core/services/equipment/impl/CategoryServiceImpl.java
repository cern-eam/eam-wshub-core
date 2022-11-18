package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.CategoryService;
import ch.cern.eam.wshub.core.services.equipment.entities.Category;
import ch.cern.eam.wshub.core.tools.InforException;
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
import net.datastream.wsdls.inforws.InforWebServicesPT;

import static ch.cern.eam.wshub.core.tools.DataTypeTools.toCodeString;

public class CategoryServiceImpl implements CategoryService {

    private Tools tools;
    private InforWebServicesPT inforws;

    public CategoryServiceImpl(Tools tools, InforWebServicesPT inforws) {
        this.tools = tools;
        this.inforws = inforws;
    }

    @Override
    public Category readCategory(InforContext context, String categoryCode) throws InforException {
        Category category = tools.getInforFieldTools().transformInforObject(new Category(), readInforCategory(context, categoryCode), context);
        return category;
    }

    private EquipmentCategory readInforCategory(InforContext context, String categoryCode) throws InforException {
        MP0324_GetEquipmentCategory_001 getEquipmentCategory001 = new MP0324_GetEquipmentCategory_001();
        getEquipmentCategory001.setCATEGORYID(new CATEGORYID());
        getEquipmentCategory001.getCATEGORYID().setCATEGORYCODE(categoryCode);

        MP0324_GetEquipmentCategory_001_Result result = tools.performInforOperation(context, inforws::getEquipmentCategoryOp, getEquipmentCategory001);
        return result.getResultData().getEquipmentCategory();
    }

    @Override
    public String updateCategory(InforContext context, Category category) throws InforException {
        EquipmentCategory equipmentCategory = readInforCategory(context, category.getCode());
        equipmentCategory.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
                context,
                toCodeString(equipmentCategory.getCLASSID()),
                equipmentCategory.getUSERDEFINEDAREA(),
                category.getClassCode(),
                "OBJ"
        ));
        tools.getInforFieldTools().transformWSHubObject(equipmentCategory, category, context);
        MP0325_SyncEquipmentCategory_001 syncEquipmentCategory = new MP0325_SyncEquipmentCategory_001();
        syncEquipmentCategory.setEquipmentCategory(equipmentCategory);
        MP0325_SyncEquipmentCategory_001_Result result = tools.performInforOperation(context, inforws::syncEquipmentCategoryOp, syncEquipmentCategory);
        return result.getResultData().getCATEGORYID().getCATEGORYCODE();
    }

    @Override
    public String createCategory(InforContext context, Category category) throws InforException {
        EquipmentCategory equipmentCategory = new EquipmentCategory();
        equipmentCategory.setUSERDEFINEDAREA(tools.getCustomFieldsTools().getInforCustomFields(
                context,
                toCodeString(equipmentCategory.getCLASSID()),
                equipmentCategory.getUSERDEFINEDAREA(),
                category.getClassCode(),
                "OBJ"
        ));
        tools.getInforFieldTools().transformWSHubObject(equipmentCategory, category, context);

        MP0323_AddEquipmentCategory_001 addCategory = new MP0323_AddEquipmentCategory_001();
        addCategory.setEquipmentCategory(equipmentCategory);
        MP0323_AddEquipmentCategory_001_Result result = tools.performInforOperation(context, inforws::addEquipmentCategoryOp, addCategory);
        String categoryCode = result.getResultData().getCATEGORYID().getCATEGORYCODE();
        return categoryCode;
    }

    @Override
    public String deleteCategory(InforContext context, String categoryCode) throws InforException {
        MP0326_DeleteEquipmentCategory_001 deleteEquipmentCategory = new MP0326_DeleteEquipmentCategory_001();
        deleteEquipmentCategory.setCATEGORYID(new CATEGORYID());
        deleteEquipmentCategory.getCATEGORYID().setCATEGORYCODE(categoryCode);
        tools.performInforOperation(context, inforws::deleteEquipmentCategoryOp, deleteEquipmentCategory);
        return categoryCode;
    }


}
