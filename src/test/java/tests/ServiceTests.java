package tests;

import helpers.PropertyReader;
import io.restassured.response.Response;
import jdk.jfr.Description;
import models.Service;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import pageObjectModels.API;
import pageObjectModels.API_Service;

import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTests {
    private API_Service apiService;
    private PropertyReader reader;

    @BeforeAll
    public void berforeAll(){
        reader = new PropertyReader();
        apiService = new API_Service(reader.getPropertyValue("rootURL"));

        // Perform prerequisites for test cases

        // Update the service to the original state for test case : TC-06
        Service service = new Service("Apple Shop");
        apiService.updateService(4, service);
    }

    @Test
    @Description("TC-05 : Add a new service and check that the service is created successfully")
    public void addNewService(){
        String serviceName = "LG Shop";

        Service service = new Service(serviceName);
        Response response = apiService.addNewService(service);
        int serviceId = response.then().extract().path("id");

        apiService.verifyThat_serviceExists(serviceId, serviceName);
    }

    @Test
    @Description("TC-06 : Update an existing service and check that the service is updated successfully")
    public void updateService(){
        String serviceName = "Apple Shop";
        String newServiceName = " Apple Online Store";
        int serviceId = apiService.getServiceId_byServiceName(serviceName);

        Service service = new Service(newServiceName);
        apiService.updateService(serviceId, service);

        System.out.println("Verifying on the service's name after updating the service");
        Response response = apiService.getService(serviceId);
        response.then().assertThat().body("name", equalTo(newServiceName));
    }

    @Test
    @Description("TC-07 : Delete an existing service and check that the service is deleted successfully")
    public void deleteService(){
        int serviceId = apiService.getServiceId_byServiceName("Magnolia Home Theater");
        apiService.deleteService(serviceId);

        apiService.verifyThat_serviceIsNotExist(serviceId);
    }

    @Test
    @Description("Tc-08 : Get the service with limit 8 and skip 2 and verify on the response")
    public void limitAndSkipServices(){
        System.out.print("Get services by limit -> 8 and skip -> 2 ");
        Response response = apiService.getServices_byLimitAndSkipArgs(8,2);
        System .out.println("Filtered services \n" + response.asString());

        System.out.print("Verifying that the limit -> 8 and skip -> 2");
        response.then().assertThat().body("limit", equalTo(8)).body("skip", equalTo(2));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @Description("TC-09 : Add new service with null, or empty name")
    public void addNewService_withInvalidParameters(String servieName){
        Service service = new Service(servieName);
        Response response = apiService.addNewService(service, API.statusCode.badRequestStatusCode.getStatusCode());

        System.out.println("Verifying on the add service response error message");
        response.then().assertThat().body("message", equalTo("Invalid Parameters"));
    }

    @AfterAll
    public void afterAll(){
        // Perform cleansing activities

        // Deleting the added service by the test case : TC-05
        apiService.deleteService(apiService.getServiceId_byServiceName("LG Shop"));

        // Add Service to be deleted in test case : TC-07
        Service service = new Service("Magnolia Home Theater");
        apiService.addNewService(service);
    }
}
