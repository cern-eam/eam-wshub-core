package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.entities.Category;
import ch.cern.eam.wshub.core.tools.InforException;

public interface CategoryService {

    Category readCategory(InforContext context, String categoryCode) throws InforException;

    String updateCategory(InforContext context, Category category) throws InforException;

    String createCategory(InforContext context, Category category) throws InforException;

    String deleteCategory(InforContext context, String categoryCode) throws InforException;

//    Category readCategoryDefault(InforContext context, String organizationCode) throws InforException;
}
