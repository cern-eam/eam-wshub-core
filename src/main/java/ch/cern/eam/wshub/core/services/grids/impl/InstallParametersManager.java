package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.services.entities.InstallParameters;
import ch.cern.eam.wshub.core.tools.Tools;

import java.util.HashMap;
import java.util.List;

public class InstallParametersManager {

	private HashMap<String, String> params;
	private Tools tools;

	public InstallParametersManager(Tools tools) {
		this.tools = tools;
		params = new HashMap<String, String>();
		update();
	}

	/**
	 * Update map with install parameters.
	 * @return updated map with parameters
	 */
	public void update(){
		// Read the installation parameters directly from the DB
		List<Object[]> list = tools.getEntityManager().createNamedQuery(InstallParameters.GETINSTALLPARAMS).getResultList();
		for (Object[] result : list) {
			if (result[0] != null && result[1] != null) {
				params.put(result[0].toString(), result[1].toString());
			}
		}
	}
	
	
	/**
	 * @return the params
	 */
	public HashMap<String, String> getParams() {
		return params;
	}

}
