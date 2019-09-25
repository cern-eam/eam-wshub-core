package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.PartLotService;
import ch.cern.eam.wshub.core.services.material.entities.Lot;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_fields.CLASSID_Type;
import net.datastream.schemas.mp_fields.LOTID_Type;
import net.datastream.schemas.mp_functions.SessionType;
import net.datastream.schemas.mp_functions.mp1201_001.MP1201_AddLot_001;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import javax.xml.ws.Holder;

public class PartLotServiceImpl implements PartLotService {

    private Tools tools;
    private InforWebServicesPT inforws;
    private ApplicationData applicationData;

    public PartLotServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
        this.applicationData = applicationData;
        this.tools = tools;
        this.inforws = inforWebServicesToolkitClient;
    }

    public String createLot(InforContext context, Lot lot) throws InforException {

        net.datastream.schemas.mp_entities.lot_001.Lot lotInfor = new net.datastream.schemas.mp_entities.lot_001.Lot();

        // CLASS, DESCRIPTION
        lotInfor.setLOTID(new LOTID_Type());
        lotInfor.getLOTID().setLOTCODE(lot.getCode());
        lotInfor.getLOTID().setDESCRIPTION(lot.getDesc());
        lotInfor.getLOTID().setORGANIZATIONID(tools.getOrganization(context));

        // CLASS
        if (tools.getDataTypeTools().isNotEmpty(lot.getClassCode())) {
            lotInfor.setCLASSID(new CLASSID_Type());
            lotInfor.getCLASSID().setORGANIZATIONID(tools.getOrganization(context));
            lotInfor.getCLASSID().setCLASSCODE(lot.getClassCode());

        }

        // EXPIRATION DATE
        if (lot.getExpirationDate() != null) {
            lotInfor.setEXPIRATIONDATE(tools.getDataTypeTools().encodeInforDate(lot.getExpirationDate(), "Lot Expiration Date"));
        }

        // MANUFACTURER LOT
        if (lot.getManufacturerLot() != null) {
            lotInfor.setMANUFACTLOT(lot.getManufacturerLot());
        }

        MP1201_AddLot_001 addlot = new MP1201_AddLot_001();
        addlot.setLot(lotInfor);

        if (context.getCredentials() != null) {
            inforws.addLotOp(addlot, tools.getOrganizationCode(context), tools.createSecurityHeader(context),"TERMINATE", null, tools.createMessageConfig(), tools.getTenant(context));
        } else {
            inforws.addLotOp(addlot, tools.getOrganizationCode(context), null, null, new Holder<SessionType>(tools.createInforSession(context)), null, tools.getTenant(context));
        }

        return lot.getCode();
    }

}

