package ch.cern.eam.wshub.core.services.safety.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.safety.SafetyService;
import ch.cern.eam.wshub.core.services.safety.entities.EntitySafetyWSHub;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.entitysafety_001.EntitySafety;
import net.datastream.schemas.mp_fields.HAZARDID_Type;
import net.datastream.schemas.mp_fields.PRECAUTIONID_Type;
import net.datastream.schemas.mp_functions.mp3219_001.MP3219_AddEntitySafety_001;
import net.datastream.schemas.mp_functions.mp3222_001.MP3222_GetEntitySafety_001;
import net.datastream.schemas.mp_results.mp3222_001.MP3222_GetEntitySafety_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import org.openapplications.oagis_segments.QUANTITY;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SafetyServiceImpl implements SafetyService {
    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public SafetyServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

//    public String createEntitySafety(InforContext context, EntitySafetyWSHub entitySafetywshub) throws InforException {
//
//    }

    @Override
    public String addSafety(InforContext context, EntitySafetyWSHub entitySafetywshubX) throws InforException {
        System.out.println("Safety here");

        //TODO comes from object, remove these
        EntitySafetyWSHub entitySafetywshub = new EntitySafetyWSHub();
        entitySafetywshub.setENTITYSAFETYCODE("PR-A-001#*");
//        entitySafetywshub.setENTITYSAFETYCODE("20030821000150#*");
        entitySafetywshub.setHAZARDID("100");
        entitySafetywshub.setPRECAUTIONID("PPE");
        entitySafetywshub.setEntity("OBJ");
        entitySafetywshub.setHazardrevision(BigDecimal.valueOf(0)); //revision
        entitySafetywshub.setPrecautionrevision(BigDecimal.valueOf(0)); //revision


        EntitySafety entitySafetyInfor = new EntitySafety();
        entitySafetyInfor.setSAFETYCODE("0");

        tools.getInforFieldTools().transformWSHubObject(entitySafetyInfor, entitySafetywshub, context);


        MP3219_AddEntitySafety_001 addEntitySafety_001 = new MP3219_AddEntitySafety_001();
        addEntitySafety_001.getEntitySafety().add(entitySafetyInfor);

        tools.performInforOperation(context, inforws::addEntitySafetyOp, addEntitySafety_001);
        System.out.println("End of createEntitySafety");
        return "OK";
    }
}


//
//        EntitySafetyWSHub entitySafetyWSHub = new EntitySafetyWSHub();
//        this.createEntitySafety(context, entitySafetyWSHub);
//


// init this object with something that might be required (maybe the ID?)
//        entitySafetyInfor.setENTITYSAFETYCODE("PR-A-001");
//        MP3222_GetEntitySafety_001 mp3222_getEntitySafety_001 = new MP3222_GetEntitySafety_001();
//        mp3222_getEntitySafety_001.setSAFETYCODE("10010");
//        MP3222_GetEntitySafety_001_Result RES = tools.performInforOperation(context, inforws::getEntitySafetyOp, mp3222_getEntitySafety_001);



//        MP6043_AddExtMenus_001 addExtMenus = this.fillExtMenus(parent, folderName, userGroup, context, UserGroupMenuService.MENU_FUNCTION_CODE, menuType);

//        MP3219_AddEntitySafety_001 addSafety = new MP3219_AddEntitySafety_001();
//
//        EntitySafety es;
//        addSafety.getEntitySafety().get(0).
