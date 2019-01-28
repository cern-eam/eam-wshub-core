package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.InforException;


public interface AssetService {

	Equipment readAsset(InforContext context, String assetCode) throws InforException;

	String updateAsset(InforContext context, Equipment assetParam) throws InforException;

	String createAsset(InforContext context, Equipment assetParam) throws InforException;

	String deleteAsset(InforContext context, String assetCode) throws InforException;

}