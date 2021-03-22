package ch.cern.eam.wshub.core.services.administration.impl;

import ch.cern.eam.wshub.core.services.administration.entities.RemoveUserSetupResponsibility;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import net.datastream.schemas.mp_functions.mp6520_001.MP6520_AddUserSetupResponsibility_001;
import net.datastream.schemas.mp_functions.mp6521_001.MP6521_DeleteUserSetupResponsibility_001;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.administration.ResponsibilityService;
import ch.cern.eam.wshub.core.services.administration.entities.AddUserSetupResponsibility;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.wsdls.inforws.InforWebServicesPT;


public class ResponsibilityServiceImpl implements ResponsibilityService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private  ApplicationData applicationData;

    public ResponsibilityServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforws) {
        this.tools = tools;
        this.inforws = inforws;
        this.applicationData = applicationData;
    }

    @Override
    public void addResponsibility(InforContext context, String userCode, String responsibilityCode) throws InforException {
        AddUserSetupResponsibility addUserSetupResponsibility = new AddUserSetupResponsibility();
        addUserSetupResponsibility.setUserCode(userCode);
        addUserSetupResponsibility.setResponsibilityCode(responsibilityCode);
        MP6520_AddUserSetupResponsibility_001 addUserSetupResponsibilityInfor = new MP6520_AddUserSetupResponsibility_001();
        tools.getInforFieldTools().transformWSHubObject(addUserSetupResponsibilityInfor, addUserSetupResponsibility, context);
        tools.performInforOperation(context, inforws::addUserSetupResponsibilityOp, addUserSetupResponsibilityInfor);
    }

    @Override
    public void deleteResponsibility(InforContext context, String userCode, String responsibilityCode) throws InforException {
        RemoveUserSetupResponsibility removeUserSetupResponsibility = new RemoveUserSetupResponsibility();
        removeUserSetupResponsibility.setUserCode(userCode);
        removeUserSetupResponsibility.setResponsibilityCode(responsibilityCode);
        MP6521_DeleteUserSetupResponsibility_001 removeUserSetupResponsibilityInfor = new MP6521_DeleteUserSetupResponsibility_001();
        tools.getInforFieldTools().transformWSHubObject(removeUserSetupResponsibilityInfor, removeUserSetupResponsibility, context);
        tools.performInforOperation(context, inforws::deleteUserSetupResponsibilityOp, removeUserSetupResponsibilityInfor);
    }
}
