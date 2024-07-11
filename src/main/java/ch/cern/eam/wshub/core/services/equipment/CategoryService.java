package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.equipment.entities.Category;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface CategoryService {

    Category readCategory(EAMContext context, String categoryCode) throws EAMException;

    String updateCategory(EAMContext context, Category category) throws EAMException;

    String createCategory(EAMContext context, Category category) throws EAMException;

    String deleteCategory(EAMContext context, String categoryCode) throws EAMException;

//    Category readCategoryDefault(EAMContext context, String organizationCode) throws EAMException;
}
