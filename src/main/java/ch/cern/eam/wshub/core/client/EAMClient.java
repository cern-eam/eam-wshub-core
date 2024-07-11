package ch.cern.eam.wshub.core.client;

import ch.cern.eam.wshub.core.interceptors.EAMInterceptor;
import ch.cern.eam.wshub.core.interceptors.EAMInvocationHandler;
import ch.cern.eam.wshub.core.services.administration.DataspyService;
import ch.cern.eam.wshub.core.services.administration.ScreenLayoutService;
import ch.cern.eam.wshub.core.services.administration.UserGroupMenuService;
import ch.cern.eam.wshub.core.services.administration.UserSetupService;
import ch.cern.eam.wshub.core.services.administration.impl.DataspyServiceImpl;
import ch.cern.eam.wshub.core.services.administration.impl.ScreenLayoutServiceImpl;
import ch.cern.eam.wshub.core.services.administration.impl.UserGroupMenuServiceImpl;
import ch.cern.eam.wshub.core.services.administration.impl.UserSetupServiceImpl;
import ch.cern.eam.wshub.core.services.casemanagement.CaseManagementService;
import ch.cern.eam.wshub.core.services.casemanagement.impl.CaseManagementServiceImpl;
import ch.cern.eam.wshub.core.services.comments.CommentService;
import ch.cern.eam.wshub.core.services.comments.impl.CommentServiceImpl;
import ch.cern.eam.wshub.core.services.contractmanagement.EquipmentReservationAdjustmentService;
import ch.cern.eam.wshub.core.services.contractmanagement.impl.EquipmentReservationAdjustmentServiceImpl;
import ch.cern.eam.wshub.core.services.documents.DocumentsService;
import ch.cern.eam.wshub.core.services.documents.impl.DocumentsServiceImpl;
import ch.cern.eam.wshub.core.services.equipment.*;
import ch.cern.eam.wshub.core.services.equipment.impl.*;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.material.*;
import ch.cern.eam.wshub.core.services.material.impl.*;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedListService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedScreenService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.UserDefinedTableService;
import ch.cern.eam.wshub.core.services.userdefinedscreens.impl.UserDefinedListServiceImpl;
import ch.cern.eam.wshub.core.services.userdefinedscreens.impl.UserDefinedScreenServiceImpl;
import ch.cern.eam.wshub.core.services.userdefinedscreens.impl.UserDefinedTableServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.*;
import ch.cern.eam.wshub.core.services.workorders.impl.*;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.Tools;
import ch.cern.eam.wshub.core.tools.soaphandler.SOAPHandlerResolver;
import lombok.Getter;
import lombok.Setter;
import net.datastream.wsdls.eamws.EAMWebServicesPT;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.xml.namespace.QName;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.handler.HandlerResolver;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Client for EAM services
 * This class is thread-safe. On single instance can and should be reused to handle multiple calls,
 * even if it involves different EAM users.
 */
@Getter
@Setter
public class EAMClient implements Serializable {

    private Tools tools;
    private InvocationHandler invocationHandler;
    private EAMWebServicesPT eamWebServicesToolkitClient;
    private BindingProvider bindingProvider;

    private CommentService commentService;
    private WorkOrderService workOrderService;
    private StandardWorkOrderService standardWorkOrderService;
    private StandardWorkOrderChildService standardWorkOrderChildService;
    private CaseService caseService;
    private CaseTaskService caseTaskService;
    private LaborBookingService laborBookingService;
    private WorkOrderMiscService workOrderMiscService;
    private EmployeeService employeeService;
    private ChecklistService checklistService;
    private InspectionService inspectionService;
    private RouteService routeService;
    private TaskPlanService taskPlanService;
    private SalesPriceService salesPriceService;

    private AssetService assetService;
    private PositionService positionService;
    private SystemService systemService;
    private LocationService locationService;
    private EquipmentFacadeService equipmentFacadeService;

    private EquipmentStructureService equipmentStructureService;
    private LinearReferenceService linearReferenceService;
    private PMScheduleService pmScheduleService;
    private EquipmentWarrantyCoverageService equipmentWarrantyCoverageService;
    private EquipmentOtherService equipmentOtherService;

    private PartService partService;
    private PartKitService partKitService;
    private PartMiscService partMiscService;
    private PartStoreService partStoreService;
    private PartManufacturerService partManufacturerService;
    private PartBinStockService partBinStockService;
    private PartLotService partLotService;
    private PurchaseOrdersService purchaseOrdersService;
    private PickTicketService pickTicketService;
    private PhysicalInventoryService physicalInventoryService;

    private UserSetupService userSetupService;
    private GridsService gridsService;
    private DocumentsService documentsService;
    private DataspyService dataspyService;
    private UserGroupMenuService userGroupMenuService;
    private ScreenLayoutService screenLayoutService;

    private EquipmentGenerationService equipmentGenerationService;
    private EquipmentConfigurationService equipmentConfigurationService;

    private UserDefinedTableService userDefinedTableServices;
    private UserDefinedListService userDefinedListService;
    private UserDefinedScreenService userDefinedScreenService;

    private MECService mecService;

    private SafetyService safetyService;
    private CategoryService categoryService;

    private EquipmentReservationService equipmentReservationService;

    private EquipmentMeterReadingService equipmentMeterReadingService;

    private NonconformityService nonconformityService;

    private Store2StoreTransferService store2StoreTransferService;

    private CaseManagementService caseManagementService;

    private EquipmentReservationAdjustmentService equipmentReservationAdjustmentService;

    // Prevent initializing the class without the builder
    private EAMClient() {}

    public static class Builder {
        private String url;
        private String tenant;
        private String defaultOrganizationCode;
        private EAMInterceptor eamInterceptor;
        private HandlerResolver soapHandlerResolver;
        private ExecutorService executorService;
        private DataSource dataSource;
        private EntityManagerFactory entityManagerFactory;
        private Logger logger;
        private Boolean withJPAGridsAuthentication = false;

        private Boolean localizeResults = true;

        public Builder(String url) {
            this.url = url;
        }

        public Builder withDefaultTenant(String defaultTenant) {
            this.tenant = defaultTenant;
            return this;
        }

        public Builder withDefaultOrganizationCode(String defaultOrganizationCode) {
            this.defaultOrganizationCode = defaultOrganizationCode;
            return this;
        }

        public Builder withEAMInterceptor(EAMInterceptor eamInterceptor) {
            this.eamInterceptor = eamInterceptor;
            return this;
        }

        public Builder withSOAPHandlerResolver(HandlerResolver soapHandlerResolver) {
            this.soapHandlerResolver = soapHandlerResolver;
            return this;
        }

        public Builder withExecutorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public Builder withDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder withEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
            this.entityManagerFactory = entityManagerFactory;
            return this;
        }

        public Builder withLogger(Logger logger) {
            this.logger = logger;
            return this;
        }
        
        public Builder withJPAGridsAuthentication() {
        	this.withJPAGridsAuthentication = true;
        	return this;
        }

        public Builder localizeResults(Boolean localizeResults) {
            this.localizeResults = localizeResults;
            return this;
        }

        private <T> T proxy(Class<T> targetClass, T target, EAMInterceptor eamInterceptor, Tools tools) {
            return (T) Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[] { targetClass }, new EAMInvocationHandler<>(target, eamInterceptor, tools));
        }

        public EAMClient build() {
            EAMClient eamClient = new EAMClient();

            // Application Data
            ApplicationData applicationData = new ApplicationData();
            applicationData.setUrl(this.url);
            applicationData.setOrganization(this.defaultOrganizationCode);
            applicationData.setTenant(this.tenant);
            applicationData.setWithJPAGridsAuthentication(withJPAGridsAuthentication);
            ApplicationData.localizeResults = localizeResults;

            // EAM Web Services Client
            Service service = Service.create(new QName("eamws"));
            if (this.soapHandlerResolver == null) {
                this.soapHandlerResolver = new SOAPHandlerResolver();
            }
            service.setHandlerResolver(soapHandlerResolver);

            EAMWebServicesPT eamWebServicesToolkitClient = service.getPort(EAMWebServicesPT.class);
            eamClient.bindingProvider = (BindingProvider) eamWebServicesToolkitClient;
            eamClient.bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, applicationData.getUrl());
            eamClient.bindingProvider.getRequestContext().put("set-jaxb-validation-event-handler", false);

            List handlerChain = eamClient.bindingProvider.getBinding().getHandlerChain();
            handlerChain.add(0, new AuthenticationHandler());
            eamClient.bindingProvider.getBinding().setHandlerChain(handlerChain);

            if (this.executorService != null) {
                // Init new Executor Service
            }

            // Tools
            Tools tools = new Tools(applicationData,
                    eamWebServicesToolkitClient,
                    this.executorService,
                    this.dataSource,
                    this.entityManagerFactory,
                    this.logger);
            eamClient.tools = tools;

            //
            // Init Service Classes
            //
            eamClient.workOrderService = proxy(WorkOrderService.class, new WorkOrderServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.standardWorkOrderService = proxy(StandardWorkOrderService.class, new StandardWorkOrderServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.standardWorkOrderChildService = proxy(StandardWorkOrderChildService.class, new StandardWorkOrderChildServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.commentService = proxy(CommentService.class, new CommentServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.caseService = proxy(CaseService.class, new CaseServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.caseTaskService = proxy(CaseTaskService.class, new CaseTaskServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.checklistService = proxy(ChecklistService.class, new ChecklistServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.inspectionService = proxy(InspectionService.class, new InspectionServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.laborBookingService = proxy(LaborBookingService.class, new LaborBookingServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.workOrderMiscService = proxy(WorkOrderMiscService.class, new WorkOrderMiscServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.employeeService = proxy(EmployeeService.class, new EmployeeServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.categoryService = proxy(CategoryService.class, new CategoryServiceImpl(tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.assetService = proxy(AssetService.class, new AssetServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.positionService = proxy(PositionService.class, new PositionServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.systemService = proxy(SystemService.class, new SystemServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.equipmentFacadeService = proxy(EquipmentFacadeService.class, new EquipmentFacadeServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.equipmentStructureService = proxy(EquipmentStructureService.class, new EquipmentStructureServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.linearReferenceService = proxy(LinearReferenceService.class, new LinearReferenceServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.pmScheduleService = proxy(PMScheduleService.class, new PMScheduleServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.equipmentWarrantyCoverageService = proxy(EquipmentWarrantyCoverageService.class, new EquipmentWarrantyCoverageServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.equipmentOtherService = proxy(EquipmentOtherService.class, new EquipmentOtherServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.partService = proxy(PartService.class, new PartServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.partMiscService = proxy(PartMiscService.class, new PartMiscServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.partStoreService = proxy(PartStoreService.class, new PartStoreServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.partManufacturerService = proxy(PartManufacturerService.class, new PartManufacturerServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.partBinStockService = proxy(PartBinStockService.class, new PartBinStockServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.partLotService = proxy(PartLotService.class, new PartLotServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.locationService = proxy(LocationService.class, new LocationServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.partKitService = proxy(PartKitService.class, new PartKitServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.purchaseOrdersService = proxy(PurchaseOrdersService.class, new PurchaseOrdersImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.userSetupService = proxy(UserSetupService.class, new UserSetupServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.gridsService = proxy(GridsService.class, new GridsServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.documentsService = proxy(DocumentsService.class, new DocumentsServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.pickTicketService = proxy(PickTicketService.class, new PickTicketServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.physicalInventoryService = proxy(PhysicalInventoryService.class, new PhysicalInventoryServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.equipmentGenerationService = proxy(EquipmentGenerationService.class, new EquipmentGenerationServiceImpl(applicationData, tools, eamWebServicesToolkitClient),eamInterceptor, tools);
            eamClient.equipmentConfigurationService = proxy(EquipmentConfigurationService.class, new EquipmentConfigurationServiceImpl(applicationData, tools, eamWebServicesToolkitClient),eamInterceptor, tools);
            eamClient.dataspyService = proxy(DataspyService.class, new DataspyServiceImpl(applicationData, tools, eamWebServicesToolkitClient),eamInterceptor, tools);
            eamClient.userGroupMenuService = proxy(UserGroupMenuService.class, new UserGroupMenuServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.screenLayoutService = proxy(ScreenLayoutService.class, new ScreenLayoutServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.userDefinedTableServices = proxy(UserDefinedTableService.class, new UserDefinedTableServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.userDefinedListService = proxy(UserDefinedListService.class, new UserDefinedListServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.routeService = proxy(RouteService.class, new RouteServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.mecService = proxy(MECService.class, new MECServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.eamWebServicesToolkitClient = eamWebServicesToolkitClient;
            eamClient.safetyService = proxy(SafetyService.class, new SafetyServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.taskPlanService = proxy(TaskPlanService.class, new TaskPlanServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.salesPriceService = proxy(SalesPriceService.class, new SalesPricesImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.userDefinedScreenService = proxy(UserDefinedScreenService.class, new UserDefinedScreenServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.equipmentReservationService = proxy(EquipmentReservationService.class, new EquipmentReservationServiceImpl(applicationData, tools, eamWebServicesToolkitClient),eamInterceptor, tools);
            eamClient.nonconformityService = proxy(NonconformityService.class, new NonconformityServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.store2StoreTransferService = proxy(Store2StoreTransferService.class, new Store2StoreTransferServiceImpl(
                    applicationData,
                    tools,
                    eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.caseManagementService = proxy(CaseManagementService.class, new CaseManagementServiceImpl(
                    applicationData,
                    tools,
                    eamWebServicesToolkitClient), eamInterceptor, tools);
            eamClient.equipmentMeterReadingService = proxy(EquipmentMeterReadingService.class,
                    new EquipmentMeterReadingServiceImpl(applicationData, tools, eamWebServicesToolkitClient),
                    eamInterceptor, tools);
            eamClient.equipmentReservationAdjustmentService = proxy(EquipmentReservationAdjustmentService.class, new EquipmentReservationAdjustmentServiceImpl(applicationData, tools, eamWebServicesToolkitClient), eamInterceptor, tools);
            if (!tools.isDatabaseConnectionConfigured()) {
                logger.log(Level.WARNING, "Some of the services might require a database connection.");
            }
            return eamClient;
        }

    }

    public void setStandardWorkOrderChildService(StandardWorkOrderChildService standardWorkOrderChildService) {
        this.standardWorkOrderChildService = standardWorkOrderChildService;
    }

    public EAMWebServicesPT getEAMWebServicesToolkitClient() {return eamWebServicesToolkitClient; }

    public void setCaseManagementService(CaseManagementService caseManagementService) {
        this.caseManagementService = caseManagementService;
    }

    public EquipmentReservationAdjustmentService getEquipmentReservationAdjustmentService() {
        return equipmentReservationAdjustmentService;
    }

    public void setEquipmentReservationAdjustmentService(EquipmentReservationAdjustmentService equipmentReservationAdjustmentService) {
        this.equipmentReservationAdjustmentService = equipmentReservationAdjustmentService;
    }
}
