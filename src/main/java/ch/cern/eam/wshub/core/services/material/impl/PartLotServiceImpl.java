package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.material.PartLotService;
import ch.cern.eam.wshub.core.services.material.entities.Lot;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.EAMException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.CLASSID_Type;
import net.datastream.schemas.mp_fields.LOTID_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp1201_001.MP1201_AddLot_001;
import net.datastream.wsdls.eamws.EAMWebServicesPT;
import jakarta.xml.ws.Holder;

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

        // CLASS, DESCRIPTION
        lotEAM.setLOTID(new LOTID_Type());
        lotEAM.getLOTID().setLOTCODE(lot.getCode());
        lotEAM.getLOTID().setDESCRIPTION(lot.getDesc());
        lotEAM.getLOTID().setORGANIZATIONID(tools.getOrganization(context));

        // CLASS
        if (tools.getDataTypeTools().isNotEmpty(lot.getClassCode())) {
            lotEAM.setCLASSID(new CLASSID_Type());
            lotEAM.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
            lotEAM.getCLASSID().setCLASSCODE(lot.getClassCode());

        }

        // EXPIRATION DATE
        if (lot.getExpirationDate() != null) {
            lotEAM.setEXPIRATIONDATE(tools.getDataTypeTools().encodeEAMDate(lot.getExpirationDate(), "Lot Expiration Date"));
        }

        // MANUFACTURER LOT
        if (lot.getManufacturerLot() != null) {
            lotEAM.setMANUFACTLOT(lot.getManufacturerLot());
        }

        MP1201_AddLot_001 addlot = new MP1201_AddLot_001();
        addlot.setLot(lotEAM);

        tools.performEAMOperation(context, eamws::addLotOp, addlot);

        return lot.getCode();
    }

}

