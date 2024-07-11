package ch.cern.eam.wshub.core.services.equipment;

import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.equipment.entities.Equipment;
import ch.cern.eam.wshub.core.tools.EAMException;

public interface AssetService {

	Equipment readAsset(EAMContext context, String assetCode, String organization) throws EAMException;

	String updateAsset(EAMContext context, Equipment assetParam) throws EAMException;

	String createAsset(EAMContext context, Equipment assetParam) throws EAMException;

	String deleteAsset(EAMContext context, String assetCode, String organization) throws EAMException;

	Equipment readAssetDefault(EAMContext context, String organizationCode) throws EAMException;
}