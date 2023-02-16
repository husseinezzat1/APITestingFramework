package pageObjectModels;

import helpers.ApiHandler;
import io.restassured.response.Response;
import models.Service;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class API_Service extends API{
    private String servicesServiceName = "services/";
    private final ApiHandler apiHandler;
    public API_Service(String root){
        apiHandler = new ApiHandler(root);
    }

    /**
     * Use this method to get the services
     * @return
     */
    public Response getServices(){
        System.out.print("Getting Services");
        return apiHandler.performGetRequest(servicesServiceName);
    }

    /**
     * Use this method to get a service's details by passing the service id
     * @param serviceId
     * @return
     */
    public Response getService(int serviceId){
        System.out.println("Getting service's details");
        return apiHandler.performGetRequest(servicesServiceName + serviceId);
    }

    /**
     * Use this method to get a service's details by passing the service name
     * @param serviceName
     * @return
     */
    public Response getService_byName(String serviceName){
        Map<String, Object> queries = new HashMap<String, Object>();
        queries.put("name", serviceName);
        return apiHandler.performGetRequest_withQueryParams(servicesServiceName, queries);
    }

    /**
     * Use this method to filter the services by the limit and skip args
     * @param limit
     * @param skip
     * @return
     */
    public Response getServices_byLimitAndSkipArgs(int limit, int skip){
        Map<String, Object> queries = new HashMap<String, Object>();
        queries.put("$limit", limit);
        queries.put("$skip", skip);
        return apiHandler.performGetRequest_withQueryParams(servicesServiceName, queries);
    }

    /**
     * Use this method to get the service id by passing service name
     * @param serviceName
     * @return
     */
    public int getServiceId_byServiceName(String serviceName){
        Response response = getService_byName(serviceName);
        return response.then().extract().path("data[0].id");
    }

    /**
     * Use this method to add a new service with a positive scenario
     * @param service
     * @return
     */
    public Response addNewService(Service service){
        return addNewService(service, statusCode.succcessStatusCode.getStatusCode());
    }

    /**
     * Use this method to add a new service with expected negative scenario
     * @param service
     * @param expectedStatusCode
     * @return
     */
    public Response addNewService(Service service, int expectedStatusCode){
        System.out.println("Add new service");
        Response response = apiHandler.performPostRequest(servicesServiceName, service);
        response.then().assertThat().statusCode(expectedStatusCode);
        return response;
    }

    /**
     * Use this method to update service
     * @param serviceId
     * @param service
     * @return
     */
    public Response updateService(int serviceId, Service service){
        System.out.print("Update an existing service");
        String fullServiceName = servicesServiceName+ serviceId;
        return apiHandler.performPatchRequest(fullServiceName, service);
    }

    /**
     * Use this method to delete service
     * @param serviceId
     * @return
     */
    public Response deleteService(int serviceId){
        System.out.println("Delete service");
        return apiHandler.performDeleteRequest(servicesServiceName+serviceId);
    }

    /**
     * Use this method to verify that a specific service is exists
     * @param serviceId
     * @param serviceName
     */
    public void verifyThat_serviceExists(int serviceId, String serviceName){
        Response response = getService(serviceId);
        System.out.println("Verifying that service : " + serviceName+ " exists");
        response.then().assertThat().body("name", equalTo(serviceName));
    }

    /**
     * Use this method to verify that a specific service is not exists
     * @param serviceId
     */
    public void verifyThat_serviceIsNotExist(int serviceId){
        Response response = getService(serviceId);
        System.out.println("verifying that service of id : " + serviceId + " is not exist");
        response.then().assertThat().statusCode(statusCode.notFoundStatusCode.getStatusCode());
    }
}
