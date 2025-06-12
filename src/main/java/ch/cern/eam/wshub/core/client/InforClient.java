package ch.cern.eam.wshub.core.client;

import ch.cern.eam.wshub.core.interceptors.InforInterceptor;
import ch.cern.eam.wshub.core.interceptors.InforInvocationHandler;
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
import ch.cern.eam.wshub.core.tools.CacheKey;
import ch.cern.eam.wshub.core.tools.Tools;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.Getter;
import lombok.Setter;
import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Client for Infor services
 * This class is thread-safe. On single instance can and should be reused to handle multiple calls,
 * even if it involves different Infor users.
 */
@Getter
public class InforClient implements Serializable {

    public static Map<CacheKey, Cache<String, Object>> cacheMap = new ConcurrentHashMap<>();

    private Tools tools;
    private InforWebServicesPT inforWebServicesToolkitClient;
    private BindingProvider bindingProvider;

    private CommentService commentService;
    private WorkOrderService workOrderService;
    private StandardWorkOrderService standardWorkOrderService;
    @Setter
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
    private NonConformityObservationService nonConformityObservationService;

    private Store2StoreTransferService store2StoreTransferService;

    @Setter
    private CaseManagementService caseManagementService;

    @Setter
    private EquipmentReservationAdjustmentService equipmentReservationAdjustmentService;

    // Prevent initializing the class without the builder
    private InforClient() {}

    public static class Builder {
        private String url;
        private String tenant;
        private String defaultOrganizationCode;
        private InforInterceptor inforInterceptor;
        private HandlerResolver soapHandlerResolver;
        private ExecutorService executorService;
        private DataSource dataSource;
        private EntityManagerFactory entityManagerFactory;
        private Logger logger;
        private Boolean withJPAGridsAuthentication = false;

        private Boolean localizeResults = true;
        private Map<CacheKey, Cache<String, Object>> cacheMap;

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

        public Builder withInforInterceptor(InforInterceptor inforInterceptor) {
            this.inforInterceptor = inforInterceptor;
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

        public Builder withCache(Map<CacheKey, Cache<String, Object>> cacheMap) {
            this.cacheMap = cacheMap;
            return this;
        }

        public Builder localizeResults(Boolean localizeResults) {
            this.localizeResults = localizeResults;
            return this;
        }

        private <T> T proxy(Class<T> targetClass, T target, InforInterceptor inforInterceptor, Tools tools) {
            return (T) Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[] { targetClass }, new InforInvocationHandler<>(target, inforInterceptor, tools));
        }

        public InforClient build() {
            InforClient inforClient = new InforClient();

            // Application Data
            ApplicationData applicationData = new ApplicationData();
            applicationData.setUrl(this.url);
            applicationData.setOrganization(this.defaultOrganizationCode);
            applicationData.setTenant(this.tenant);
            applicationData.setWithJPAGridsAuthentication(withJPAGridsAuthentication);
            ApplicationData.localizeResults = localizeResults;

            InforClient.cacheMap = this.cacheMap;

            // Infor Web Services Client
            Service service = Service.create(new QName("inforws"));
            if (this.soapHandlerResolver != null) {
                service.setHandlerResolver(soapHandlerResolver);
            }

            InforWebServicesPT inforWebServicesToolkitClient = service.getPort(InforWebServicesPT.class);
            inforClient.bindingProvider = (BindingProvider) inforWebServicesToolkitClient;
            inforClient.bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, applicationData.getUrl());
            inforClient.bindingProvider.getRequestContext().put("set-jaxb-validation-event-handler", false);

            List<Handler> handlerChain = inforClient.bindingProvider.getBinding().getHandlerChain();
            handlerChain.add(0, new AuthenticationHandler());
            inforClient.bindingProvider.getBinding().setHandlerChain(handlerChain);

            if (this.executorService != null) {
                // Init new Executor Service
            }

            // Tools
            Tools tools = new Tools(applicationData,
                    inforWebServicesToolkitClient,
                    this.executorService,
                    this.dataSource,
                    this.entityManagerFactory,
                    this.logger);
            inforClient.tools = tools;

            //
            // Init Service Classes
            //
            inforClient.workOrderService = proxy(WorkOrderService.class, new WorkOrderServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.standardWorkOrderService = proxy(StandardWorkOrderService.class, new StandardWorkOrderServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.standardWorkOrderChildService = proxy(StandardWorkOrderChildService.class, new StandardWorkOrderChildServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.commentService = proxy(CommentService.class, new CommentServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.caseService = proxy(CaseService.class, new CaseServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.caseTaskService = proxy(CaseTaskService.class, new CaseTaskServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.checklistService = proxy(ChecklistService.class, new ChecklistServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.inspectionService = proxy(InspectionService.class, new InspectionServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.laborBookingService = proxy(LaborBookingService.class, new LaborBookingServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.workOrderMiscService = proxy(WorkOrderMiscService.class, new WorkOrderMiscServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.employeeService = proxy(EmployeeService.class, new EmployeeServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.categoryService = proxy(CategoryService.class, new CategoryServiceImpl(tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.assetService = proxy(AssetService.class, new AssetServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.positionService = proxy(PositionService.class, new PositionServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.systemService = proxy(SystemService.class, new SystemServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.equipmentFacadeService = proxy(EquipmentFacadeService.class, new EquipmentFacadeServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.equipmentStructureService = proxy(EquipmentStructureService.class, new EquipmentStructureServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.linearReferenceService = proxy(LinearReferenceService.class, new LinearReferenceServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.pmScheduleService = proxy(PMScheduleService.class, new PMScheduleServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.equipmentWarrantyCoverageService = proxy(EquipmentWarrantyCoverageService.class, new EquipmentWarrantyCoverageServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.equipmentOtherService = proxy(EquipmentOtherService.class, new EquipmentOtherServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partService = proxy(PartService.class, new PartServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partMiscService = proxy(PartMiscService.class, new PartMiscServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partStoreService = proxy(PartStoreService.class, new PartStoreServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partManufacturerService = proxy(PartManufacturerService.class, new PartManufacturerServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partBinStockService = proxy(PartBinStockService.class, new PartBinStockServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partLotService = proxy(PartLotService.class, new PartLotServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.locationService = proxy(LocationService.class, new LocationServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partKitService = proxy(PartKitService.class, new PartKitServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.purchaseOrdersService = proxy(PurchaseOrdersService.class, new PurchaseOrdersImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.userSetupService = proxy(UserSetupService.class, new UserSetupServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.gridsService = proxy(GridsService.class, new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.documentsService = proxy(DocumentsService.class, new DocumentsServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.pickTicketService = proxy(PickTicketService.class, new PickTicketServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.physicalInventoryService = proxy(PhysicalInventoryService.class, new PhysicalInventoryServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.equipmentGenerationService = proxy(EquipmentGenerationService.class, new EquipmentGenerationServiceImpl(applicationData, tools, inforWebServicesToolkitClient),inforInterceptor, tools);
            inforClient.equipmentConfigurationService = proxy(EquipmentConfigurationService.class, new EquipmentConfigurationServiceImpl(applicationData, tools, inforWebServicesToolkitClient),inforInterceptor, tools);
            inforClient.dataspyService = proxy(DataspyService.class, new DataspyServiceImpl(applicationData, tools, inforWebServicesToolkitClient),inforInterceptor, tools);
            inforClient.userGroupMenuService = proxy(UserGroupMenuService.class, new UserGroupMenuServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.screenLayoutService = proxy(ScreenLayoutService.class, new ScreenLayoutServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.userDefinedTableServices = proxy(UserDefinedTableService.class, new UserDefinedTableServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.userDefinedListService = proxy(UserDefinedListService.class, new UserDefinedListServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.routeService = proxy(RouteService.class, new RouteServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.mecService = proxy(MECService.class, new MECServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.inforWebServicesToolkitClient = inforWebServicesToolkitClient;
            inforClient.safetyService = proxy(SafetyService.class, new SafetyServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.taskPlanService = proxy(TaskPlanService.class, new TaskPlanServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.salesPriceService = proxy(SalesPriceService.class, new SalesPricesImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.userDefinedScreenService = proxy(UserDefinedScreenService.class, new UserDefinedScreenServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.equipmentReservationService = proxy(EquipmentReservationService.class, new EquipmentReservationServiceImpl(applicationData, tools, inforWebServicesToolkitClient),inforInterceptor, tools);
            inforClient.nonconformityService = proxy(NonconformityService.class, new NonconformityServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.nonConformityObservationService = proxy(NonConformityObservationService.class, new NonConformityObservationServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);

            inforClient.store2StoreTransferService = proxy(Store2StoreTransferService.class, new Store2StoreTransferServiceImpl(
                    applicationData,
                    tools,
                    inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.caseManagementService = proxy(CaseManagementService.class, new CaseManagementServiceImpl(
                    applicationData,
                    tools,
                    inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.equipmentMeterReadingService = proxy(EquipmentMeterReadingService.class,
                    new EquipmentMeterReadingServiceImpl(applicationData, tools, inforWebServicesToolkitClient),
                    inforInterceptor, tools);
            inforClient.equipmentReservationAdjustmentService = proxy(EquipmentReservationAdjustmentService.class, new EquipmentReservationAdjustmentServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            if (!tools.isDatabaseConnectionConfigured()) {
                logger.log(Level.WARNING, "Some of the services might require a database connection.");
            }
            return inforClient;
        }

    }
}
