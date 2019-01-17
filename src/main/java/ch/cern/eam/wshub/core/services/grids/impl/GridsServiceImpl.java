package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.customfields.GridCustomFieldHandler;
import ch.cern.eam.wshub.core.services.grids.entities.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.InforException;
import ch.cern.eam.wshub.core.tools.Tools;
import net.datastream.wsdls.inforws.InforWebServicesPT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GridsServiceImpl implements GridsService {

	private static final Logger logger = LoggerFactory.getLogger(GridsServiceImpl.class);

	private Tools tools;
	private InforWebServicesPT inforws;

	private InforGrids inforGrids;
	private JPAGrids jpaGrids;
	private GridCustomFieldHandler gridCustomFieldHandler;

	public GridsServiceImpl(ApplicationData applicationData, Tools tools, InforWebServicesPT inforWebServicesToolkitClient) {
		this.tools = tools;
		this.inforws = inforWebServicesToolkitClient;
		inforGrids = new InforGrids(applicationData, tools, inforws);
		// Init JPA Grids only when DB connection is present
		if (tools.isDatabaseConnectionConfigured()) {
			jpaGrids = new JPAGrids(applicationData, tools, inforws);
		}
	}

	public GridRequestResult executeQuery(InforContext context, GridRequest gridRequest) throws InforException {
		if (gridRequest.getUseNative()) {
			return jpaGrids.executeQuery(context, gridRequest);
		} else {
			tools.demandDatabaseConnection();
			return inforGrids.executeQuery(context, gridRequest);
		}
	}

	public GridMetadataRequestResult getGridMetadata(InforContext context, String gridCode, String viewType) throws InforException {
		tools.demandDatabaseConnection();
		return getGridMetadata(context, gridCode, viewType, "EN");
	}
	
	public GridMetadataRequestResult getGridMetadata(InforContext context, String gridCode, String viewType, String language) throws InforException {
		tools.demandDatabaseConnection();
		if (gridCode == null || gridCode.trim().equals("")) {
			throw tools.generateFault("Grid code is a mandatory field.");
		}
		if (viewType == null || viewType.trim().equals("")) {
			throw tools.generateFault("View Type is a mandatory field.");
		}
		// Fetch grid data
		EntityManager em = tools.getEntityManager();
		try {
			// Grid name
			GridMetadataRequestResult result = em.find(GridMetadataRequestResult.class, gridCode);
			if (result == null) {
				// Just 'forward' the exception to the catch block. 
				throw new Exception();
			}
			// Grid fields and data spies
			GridDataspy[] gridDataspies = em.createNamedQuery(GridDataspy.GETGRIDDATASPIES, GridDataspy.class)
											.setParameter("gridid", gridCode)
											.setParameter("userid", context.getCredentials().getUsername()).getResultList().toArray(new GridDataspy[0]);
			
			// select default dataspy
			List<GridDataspy> selectedDataSpyList = Arrays.stream(gridDataspies).filter(ds -> ds.isDefaultDataspy()).collect(Collectors.toList());
			GridDataspy selectedDataSpy = gridDataspies[0];
			if(!selectedDataSpyList.isEmpty())
				selectedDataSpy = selectedDataSpyList.get(0);
			
			result.setGridCode(gridCode);
			result.setGridDataspies(gridDataspies);
			result.setDataSpyId(selectedDataSpy.getCode());

			// This code is fetching all fields for the dataspy
			GridField[] gridFields = em.createNamedQuery(GridField.GETDDSPYFIELDS, GridField.class)
									   .setParameter("gridid", gridCode) 
									   .setParameter("ddspyid", selectedDataSpy.getCode()) 
									   .setParameter("viewtype", viewType) 
									   .setParameter("language", language != null ? language.toUpperCase() : "EN")
									   .getResultList().toArray(new GridField[0]);

			// Concat with custom fields
			if (JPAGrids.USE_CUSTOM_FIELDS) {
				gridFields = Stream.of(gridFields, gridCustomFieldHandler.getCustomFieldsAsGridFields(selectedDataSpy.getCode())).flatMap(Stream::of).toArray(GridField[]::new);
			}
			
			result.setGridFields(gridFields);
			
			return result;
		} catch (Exception e) {
			logger.error("Error while fetching grid metadata for gridCode " + gridCode, e);
			throw tools.generateFault("Couldn't fetch the metadata for this grid.");
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public GridDDSpyFieldsResult getDDspyFields(InforContext context, String gridCode, String viewType, String ddSpyId, String language) throws InforException {
		tools.demandDatabaseConnection();
		if (gridCode == null || gridCode.trim().equals("")) {
			throw tools.generateFault("Grid code is a mandatory field.");
		}
		if (ddSpyId == null || ddSpyId.trim().equals("")) {
			throw tools.generateFault("DataSpy id is a mandatory field.");
		}
		if (viewType == null || viewType.trim().equals("")) {
			throw tools.generateFault("View Type is a mandatory field.");
		}
		// Fetch fields data
		EntityManager em = tools.getEntityManager();
		try {
			// Grid name
			GridDDSpyFieldsResult result = new GridDDSpyFieldsResult();
			// Grid fields and data spies
			GridField[] gridFields = em.createNamedQuery(GridField.GETDDSPYFIELDS, GridField.class)
					   .setParameter("gridid", gridCode)
					   .setParameter("ddspyid", ddSpyId)
					   .setParameter("viewtype", viewType)
					   .setParameter("language", language != null ? language.toUpperCase() : "EN")
					   .getResultList().toArray(new GridField[0]);

			// Concat with custom fields
			if (JPAGrids.USE_CUSTOM_FIELDS) {
				gridFields = Stream.of(gridFields, gridCustomFieldHandler.getCustomFieldsAsGridFields(ddSpyId)).flatMap(Stream::of).toArray(GridField[]::new);
			}
			
			result.setGridFields(gridFields);
			result.setDataSpyId(ddSpyId);
			return result;
		} catch (Exception e) {
			logger.error("Error", e);
			throw tools.generateFault("Couldn't fetch the metadata for this grid.");
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public GridDataspy getDefaultDataspy(InforContext context, String gridCode, String viewType) throws InforException {
		tools.demandDatabaseConnection();
		if (gridCode == null || gridCode.trim().equals("")) {
			throw tools.generateFault("Grid code is a mandatory field.");
		}
		if (viewType == null || viewType.trim().equals("")) {
			throw tools.generateFault("View Type is a mandatory field.");
		}
		// Fetch grid data
		EntityManager em = tools.getEntityManager();
		try {
			// Check Grid Code
			GridMetadataRequestResult result = em.find(GridMetadataRequestResult.class, gridCode);
			if (result == null) {
				// Just 'forward' the exception to the catch block. 
				throw new Exception();
			}
			// Grid DataSpy
			GridDataspy gridDataspy = em.createNamedQuery(GridDataspy.GETDEFAULTDATASPY, GridDataspy.class)
											.setParameter("gridid", gridCode)
											.setParameter("userid", context.getCredentials().getUsername())
											.getSingleResult();
			
			return gridDataspy;
		} catch (Exception e) {
			logger.error("Error while fetching default dataspy for grid code " + gridCode + " and view type " + viewType, e);
			throw tools.generateFault("Couldn't fetch the metadata for this grid.");
		} finally {
			em.clear();
			em.close();
		}
	}
	
}

