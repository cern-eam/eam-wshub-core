# EAM WSHub Core

EAM WSHub Core is a Java library that can execute operations on Infor instances.

This library is used internally by EAM WSHub. If you run a JVM-based application, you have
the option of using directly EAM WSHub Core, instead of installing EAM WSHub.

## Installation
To use EAM WSHub Core in your project, you just need to add the following Maven dependency:
```
<dependency>
    <groupId>ch.cern.eam</groupId>
    <artifactId>eam-wshub-core</artifactId>
    <version>11.6-15</version>
</dependency>
```

## Usage
### InforClient
The class **InforClient** is the entry point of this library.
You first need to create an instance of InforClient, with the configuration specific to your environment:

```
InforClient inforClient = new InforClient.Builder(
        "https://<your.domain.name>/axis/services/EWSConnector") 
    .withDefaultTenant("<tenant>")
    .withDefaultOrganizationCode("<your-organization-code>")
    .build();
```
The InforClient instance you get is thread-safe and should be reused across your application.

##### InforClient Options
| Option        | Description           | Required?  |
| ------------- |:-------------:| -----:|
| url           | URL of your Infor instance | **Yes** |
| tenant        | Tenant      |   **Yes** |
| defaultOrganizationCode  | Default organization that will be used in every Infor call in case none is provided in InforContext.      |    No |
| inforInterceptor  | Object that includes callbacks to execute before/after every Infor call.      |    No |
| executorService  | If you run EAM WSHub core within a Java EE container, you need to give InforClient the ManagedExecutorService coming from your container.    |    No |
| dataSource  | To improve performance, EAM WSHub core may execute SQL requests directly on the database. Passing a datasource activates this option.     |    No |

### InforContext
To execute an operation on Infor, you also need an **InforContext** instance, that contains the configuration specific to
your operation. InforContext instance are also thread-safe, and can therefore be reused if needed. 
```
InforContext context = new InforContext.Builder()
    .withCredentials(new Credentials("<username>", "<password>"))
    .build();
```
##### InforContext Options
| Option        | Description           | Required?  |
| ------------- |:-------------:| -----:|
| credentials           | username/password of the Infor user who executes the request. | **Yes <sup>1</sup>** |
| sessionID        | id of the current session.      |   **Yes <sup>1</sup>** |
| organizationCode  | Code of the organization on which the request should be executed.|    No <sup>2</sup> |

<sup>1</sup> You should provide either credentials, or a sessionID.

<sup>2</sup> If the organization code is not provided in inforContext, the default value provided in inforClient will be used. The organization code should be provided in at least one of these two objets.


### Example
Here is a complete example, in which we update a work order with EAM WSHub Core:
```java
import ch.cern.eam.wshub.core.aisws.InforClient;
import ch.cern.eam.wshub.core.aisws.InforContext;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderService;
import ch.cern.eam.wshub.core.services.workorders.entities.WorkOrder;
import ch.cern.eam.wshub.core.tools.InforException;

public class UpdateWorkOrderExample {

    public static void main(String[] args) throws InforException {

        String inforEndpointUrl = "{YOUR-ENDPOINT-URL}";
        String organizationCode = "{YOUR-ORGANIZATION-CODE}";
        String tenant = "{YOUR-TENANT}";
        String username = "{YOUR-USERNAME}";
        String password = "{YOUR-PASSWORD}";
        String workOrderNumber = "{YOUR-WORK-ORDER-NUMBER}";

        InforClient inforClient = new InforClient.Builder(inforEndpointUrl)
                .withDefaultTenant(applicationData.getTenant())
                .withDefaultOrganizationCode(organizationCode)
                .build();

        InforContext context = new InforContext.Builder()
                .withCredentials(new Credentials(username, password))
                .build();

        WorkOrderService workOrderService = inforClient.getWorkOrderService();

        // Fetch workorder
        WorkOrder workOrder = workOrderService.readWorkOrder(context, workOrderNumber);

        // Let's update the description
        workOrder.setDescription("New workOrder description");

        // And synchronize the workorder in Infor
        workOrderService.updateWorkOrder(context, workOrder);

    }
}
```

## License
This software is published under the GNU General Public License v3.0 or later.

