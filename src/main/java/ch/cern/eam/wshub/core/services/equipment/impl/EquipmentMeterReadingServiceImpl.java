package ch.cern.eam.wshub.core.services.equipment.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.EquipmentMeterReadingService;
import ch.cern.eam.wshub.core.services.equipment.entities.EqpMeterReading;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.equipmentmeterreadingdefault_001.EquipmentMeterReadingDefault;
import net.datastream.schemas.mp_fields.EQUIPMENTID_Type;
import net.datastream.schemas.mp_fields.STANDARDENTITYID_Type;
import net.datastream.schemas.mp_fields.UOMID_Type;
import net.datastream.schemas.mp_functions.mp9132_001.MP9132_GetEquipmentMeterReading_001;
import net.datastream.schemas.mp_functions.mp9133_001.MP9133_GetEquipmentMeterReadingDefault_001;
import net.datastream.schemas.mp_functions.mp9134_001.MP9134_AddEquipmentMeterReading_001;
import net.datastream.schemas.mp_entities.equipmentmeterreading_001.EquipmentMeterReading;
import net.datastream.schemas.mp_functions.mp9135_001.MP9135_SyncEquipmentMeterReading_001;
import net.datastream.schemas.mp_functions.mp9136_001.MP9136_DeleteEquipmentMeterReading_001;
import net.datastream.schemas.mp_results.mp9132_001.MP9132_GetEquipmentMeterReading_001_Result;
import net.datastream.schemas.mp_results.mp9133_001.MP9133_GetEquipmentMeterReadingDefault_001_Result;
import net.datastream.schemas.mp_results.mp9134_001.MP9134_AddEquipmentMeterReading_001_Result;
import net.datastream.schemas.mp_results.mp9135_001.MP9135_SyncEquipmentMeterReading_001_Result;
import net.datastream.schemas.mp_results.mp9136_001.MP9136_DeleteEquipmentMeterReading_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

public class EquipmentMeterReadingServiceImpl implements EquipmentMeterReadingService {

    private ApplicationData applicationData;
    private Tools tools;
    private InforWebServicesPT inforws;

    public EquipmentMeterReadingServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    @Override
    public String createEquipmentMeterReading(final InforContext context, final EqpMeterReading eqpMeterReading,
                                              boolean rolloverAllowed) throws InforException {
        MP9134_AddEquipmentMeterReading_001 addEquipmentMeterReading_001 = new MP9134_AddEquipmentMeterReading_001();
        final EquipmentMeterReading equipmentMeterReading =
                tools.getInforFieldTools().transformWSHubObject(new EquipmentMeterReading(), eqpMeterReading, context);
        addEquipmentMeterReading_001.setRollOverAllowed(rolloverAllowed ? "confirm" : "prompt");
        addEquipmentMeterReading_001.setEquipmentMeterReading(equipmentMeterReading);
        final MP9134_AddEquipmentMeterReading_001_Result mp9134_addEquipmentMeterReading_001_result = tools.performInforOperation(context, inforws::addEquipmentMeterReadingOp, addEquipmentMeterReading_001);

        return mp9134_addEquipmentMeterReading_001_result.getResultData().getEQUIPMETERID().getSTANDARDENTITYCODE();
    }

    @Override
    public EqpMeterReading readEquipmentMeterReading(final InforContext context, String readingCode) throws InforException {
        final EquipmentMeterReading equipmentMeterReading = readEquipmentMeterReadingInternal(context, readingCode);
        final EqpMeterReading eqpMeterReading = tools.getInforFieldTools().transformInforObject(new EqpMeterReading(), equipmentMeterReading);
        return eqpMeterReading;
    }

    private EquipmentMeterReading readEquipmentMeterReadingInternal(final InforContext context, String readingCode) throws InforException {
        MP9132_GetEquipmentMeterReading_001 getEquipmentMeterReading_001 = new MP9132_GetEquipmentMeterReading_001();
        final STANDARDENTITYID_Type standardentityid_type = new STANDARDENTITYID_Type();
        standardentityid_type.setSTANDARDENTITYCODE(readingCode);
        standardentityid_type.setORGANIZATIONID(tools.getOrganization(context));
        getEquipmentMeterReading_001.setEQUIPMETERID(standardentityid_type);

        final MP9132_GetEquipmentMeterReading_001_Result mp9132_getEquipmentMeterReading_001_result = tools.performInforOperation(context, inforws::getEquipmentMeterReadingOp, getEquipmentMeterReading_001);
        final EquipmentMeterReading equipmentMeterReading = mp9132_getEquipmentMeterReading_001_result.getResultData().getEquipmentMeterReading();
        return equipmentMeterReading;
    }

    @Override
    public String updateEquipmentMeterReading(final InforContext context, final EqpMeterReading eqpMeterReading) throws InforException {
        final EquipmentMeterReading equipmentMeterReadingInternal = readEquipmentMeterReadingInternal(context,
                eqpMeterReading.getReadingCode());

        final EquipmentMeterReading equipmentMeterReading = tools.getInforFieldTools().transformWSHubObject(equipmentMeterReadingInternal,
                eqpMeterReading, context);

        MP9135_SyncEquipmentMeterReading_001 mp9135_syncEquipmentMeterReading_001 =
                new MP9135_SyncEquipmentMeterReading_001();
        mp9135_syncEquipmentMeterReading_001.setEquipmentMeterReading(equipmentMeterReading);
        final MP9135_SyncEquipmentMeterReading_001_Result mp9135_syncEquipmentMeterReading_001_result = tools.performInforOperation(context, inforws::syncEquipmentMeterReadingOp, mp9135_syncEquipmentMeterReading_001);
        return mp9135_syncEquipmentMeterReading_001_result.getResultData().getEQUIPMETERID().getSTANDARDENTITYCODE();
    }

    @Override
    public String deleteEquipmentMeterReading(final InforContext context, final String readingCode) throws InforException {
        MP9136_DeleteEquipmentMeterReading_001 mp9136_deleteEquipmentMeterReading_001 =
                new MP9136_DeleteEquipmentMeterReading_001();
        final STANDARDENTITYID_Type standardentityid_type = new STANDARDENTITYID_Type();
        standardentityid_type.setSTANDARDENTITYCODE(readingCode);
        mp9136_deleteEquipmentMeterReading_001.setEQUIPMETERID(standardentityid_type);

        final MP9136_DeleteEquipmentMeterReading_001_Result mp9136_deleteEquipmentMeterReading_001_result =
                tools.performInforOperation(context, inforws::deleteEquipmentMeterReadingOp, mp9136_deleteEquipmentMeterReading_001);
        return mp9136_deleteEquipmentMeterReading_001_result.getResultData().getEQUIPMETERID().getSTANDARDENTITYCODE();
    }

    @Override
    public EqpMeterReading readEquipmentMeterReadingDefault(final InforContext context, final EqpMeterReading eqpMeterReading) throws InforException {

        MP9133_GetEquipmentMeterReadingDefault_001 mp9133_getEquipmentMeterReadingDefault_001 =
                new MP9133_GetEquipmentMeterReadingDefault_001();

        final EQUIPMENTID_Type equipmentid_type = new EQUIPMENTID_Type();
        equipmentid_type.setEQUIPMENTCODE(eqpMeterReading.getEquipmentCode());
        mp9133_getEquipmentMeterReadingDefault_001.setEQUIPMENTID(equipmentid_type);

        final UOMID_Type uomid_type = new UOMID_Type();
        uomid_type.setUOMCODE(eqpMeterReading.getUOM());
        mp9133_getEquipmentMeterReadingDefault_001.setUOMID(uomid_type);

        final MP9133_GetEquipmentMeterReadingDefault_001_Result mp9133_getEquipmentMeterReadingDefault_001_result = tools.performInforOperation(context, inforws::getEquipmentMeterReadingDefaultOp, mp9133_getEquipmentMeterReadingDefault_001);
        final EquipmentMeterReadingDefault equipmentMeterReadingDefault
                = mp9133_getEquipmentMeterReadingDefault_001_result.getResultData().getEquipmentMeterReadingDefault();

        final EqpMeterReading equipmentMeterReading =
                tools.getInforFieldTools().transformInforObject(new EqpMeterReading(), equipmentMeterReadingDefault);
        return equipmentMeterReading;
    }
}
