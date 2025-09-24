package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.material.PartLotService;
import ch.cern.eam.wshub.core.services.material.entities.Lot;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.LOTID_Type;
import net.datastream.schemas.mp_functions.mp1201_001.MP1201_AddLot_001;
import net.datastream.schemas.mp_functions.mp1202_001.MP1202_SyncLot_001;
import net.datastream.schemas.mp_functions.mp1203_001.MP1203_DeleteLot_001;
import net.datastream.schemas.mp_functions.mp1205_001.MP1205_GetLot_001;
import net.datastream.schemas.mp_results.mp1202_001.MP1202_SyncLot_001_Result;
import net.datastream.schemas.mp_results.mp1203_001.MP1203_DeleteLot_001_Result;
import net.datastream.schemas.mp_results.mp1205_001.MP1205_GetLot_001_Result;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

public class PartLotServiceImpl implements PartLotService {

    private Tools tools;
    private EAMWebServicesPT eamws;
    private ApplicationData applicationData;

    public PartLotServiceImpl(ApplicationData applicationData, Tools tools, EAMWebServicesPT eamWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.eamws = eamWebServicesToolkitClient;
    }

    public String createLot(EAMContext context, Lot lot) throws EAMException {

        net.datastream.schemas.mp_entities.lot_001.Lot lotEAM = new net.datastream.schemas.mp_entities.lot_001.Lot();
        tools.getEAMFieldTools().transformWSHubObject(lotEAM, lot, context);
        MP1201_AddLot_001 addLot = new MP1201_AddLot_001();
        addLot.setLot(lotEAM);
        tools.performEAMOperation(context, eamws::addLotOp, addLot);
        return lot.getCode();
    }

    @Override
    public Lot readLot(EAMContext context, String lotPk) throws EAMException {
        net.datastream.schemas.mp_entities.lot_001.Lot lot = readLotInfor(context, lotPk);

        return tools.getEAMFieldTools().transformEAMObject(new Lot(), lot, context);

    }

    @Override
    public String updateLot(EAMContext context, Lot lot) throws EAMException {
        MP1202_SyncLot_001 syncLot = new MP1202_SyncLot_001();

        net.datastream.schemas.mp_entities.lot_001.Lot prev = readLotInfor(context, lot.getCode());
        tools.getEAMFieldTools().transformWSHubObject(prev, lot, context);
        syncLot.setLot(prev);

        MP1202_SyncLot_001_Result result = tools.performEAMOperation(context, eamws::syncLotOp, syncLot);
        return  result.getResultData().getLot().getLOTID().getLOTCODE();
    }

    @Override
    public String deleteLot(EAMContext context, String lotCode) throws EAMException {
        MP1203_DeleteLot_001 deleteLot = new MP1203_DeleteLot_001();
        LOTID_Type idType = new LOTID_Type();
        idType.setLOTCODE(lotCode);
        idType.setORGANIZATIONID(tools.getOrganization(context));

        deleteLot.setLOTID(idType);
        MP1203_DeleteLot_001_Result result = tools.performEAMOperation(context, eamws::deleteLotOp, deleteLot);
        return result.getResultData().getLOTID().getLOTCODE();
    }


    private net.datastream.schemas.mp_entities.lot_001.Lot readLotInfor(
            EAMContext context, String lotCode) throws EAMException {
        MP1205_GetLot_001 getLot = new MP1205_GetLot_001();
        LOTID_Type idType = new LOTID_Type();
        idType.setLOTCODE(lotCode);
        idType.setORGANIZATIONID(tools.getOrganization(context));
        getLot.setLOTID(idType);

        MP1205_GetLot_001_Result result =
                tools.performEAMOperation(context, eamws::getLotOp, getLot);

        return result.getResultData().getLot();
    }

}

