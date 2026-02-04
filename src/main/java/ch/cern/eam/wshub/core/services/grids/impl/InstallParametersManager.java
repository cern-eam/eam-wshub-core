package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.services.entities.InstallParameters;
import ch.cern.eam.wshub.core.tools.Tools;
import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;

public class InstallParametersManager {

	private static volatile HashMap<String, String> params;
	private static final Object lock = new Object();
	private Tools tools;

	public InstallParametersManager(Tools tools) {
		this.tools = tools;
		if (params == null) {
			synchronized (lock) {
				if (params == null) {
					params = new HashMap<>();
					update();
				}
			}
		}
	}

	/**
	 * Update map with install parameters.
	 * @return updated map with parameters
	 */
	public void update(){
		// Read the installation parameters directly from the DB
		final EntityManager entityManager = tools.getEntityManager();
		try {
			List<Object[]> list = entityManager.createNamedQuery(InstallParameters.GETINSTALLPARAMS).getResultList();
			for (Object[] result : list) {
				if (result[0] != null && result[1] != null) {
					params.put(result[0].toString(), result[1].toString());
				}
			}
		} finally {
			entityManager.close();
		}
	}
	
	
	/**
	 * @return the params
	 */
	public HashMap<String, String> getParams() {
		return params;
	}

}
