package ch.cern.eam.wshub.core.services.material.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.material.Store2StoreTransferService;
import ch.cern.eam.wshub.core.services.material.entities.Store2StoreTransferDTO;
import ch.cern.eam.wshub.core.services.material.entities.StoreTransactionPartLine;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.schemas.mp_entities.store2storetransfer_001.PartLine;
import net.datastream.schemas.mp_entities.store2storetransfer_001.Store2StoreTransfer;
import net.datastream.schemas.mp_functions.mp1287_001.MP1287_Store2StoreTransfer_001;
import net.datastream.schemas.mp_results.mp1287_001.MP1287_Store2StoreTransfer_001_Result;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import java.util.ArrayList;
import java.util.List;

public class Store2StoreTransferServiceImpl implements Store2StoreTransferService {

	private Tools tools;
	private InforWebServicesPT inforws;
	private ApplicationData applicationData;

	public Store2StoreTransferServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.applicationData = applicationData;
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
	}

	@Override
	public String store2storeTransfer(final InforContext context,
									  final Store2StoreTransferDTO store2StoreTransferDTO) throws InforException {

		final Store2StoreTransfer store2StoreTransfer = tools.getInforFieldTools().transformWSHubObject(new Store2StoreTransfer(), store2StoreTransferDTO, context);
		store2StoreTransfer.setPartLines(new Store2StoreTransfer.PartLines());

		List<StoreTransactionPartLine> partLines = store2StoreTransferDTO.getPartLines();
		if (partLines == null) {
			partLines = new ArrayList<>();
		}
		for (StoreTransactionPartLine line: partLines) {
			final PartLine partLine = tools.getInforFieldTools().transformWSHubObject(new PartLine(), line, context);
			store2StoreTransfer.getPartLines().getPartLine().add(partLine);
		}

		MP1287_Store2StoreTransfer_001 store2StoreTransfer001 = new MP1287_Store2StoreTransfer_001();
		store2StoreTransfer001.setStore2StoreTransfer(store2StoreTransfer);

		final MP1287_Store2StoreTransfer_001_Result store2StoreTransfer001Result = tools.performInforOperation(context, inforws::store2StoreTransferOp, store2StoreTransfer001);
		String transactId =
				store2StoreTransfer001Result.getResultData().getISSUETRANSACTIONID().getTRANSACTIONCODE() +
						"," + store2StoreTransfer001Result.getResultData().getRECEIVETRANSACTIONID().getTRANSACTIONCODE() ;
		return transactId;
	}
}
