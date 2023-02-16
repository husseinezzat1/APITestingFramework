package helpers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;
import static io.restassured.RestAssured.given;
import models.Model;

public class ApiHandler {
    private final String rootUrl;
    public ApiHandler(String root){
        this.rootUrl = root;
    }

    /**
     * Use this method to perform a get request
     * @param serviceName
     * @return
     */
    public Response performGetRequest(String serviceName) {
        String endPoint = rootUrl+serviceName;
        return given().when().get(endPoint);
    }

    /**
     * Use this method to perform a get request with Query Parameters
     * @param serviceName
     * @param queries
     * @return
     */
    public Response performGetRequest_withQueryParams(String serviceName, Map<String, Object> queries){
        String endPoint = rootUrl+serviceName;
        return given().queryParams(queries).when().get(endPoint);
    }

    /**
     * Use this method to perform post request
     * @param serviceName
     * @param model
     * @return
     */
    public Response performPostRequest(String serviceName, Model model){
        String endpoint = rootUrl+serviceName;
        return given().contentType(ContentType.JSON).body(model).when().post(endpoint);
    }

    /**
     * Use this method to perform delete request
     * @param serviceName
     * @return
     */
    public Response performDeleteRequest(String serviceName){
        String endPoint = rootUrl+serviceName;
        return given().when().delete(endPoint);
    }

    /**
     * Use this request to perform patch request
     * @param serviceName
     * @param model
     * @return
     */
    public Response performPatchRequest(String serviceName, Model model){
        String endPoint = rootUrl+ serviceName;
        return given().contentType(ContentType.JSON).body(model).patch(endPoint);
    }
}
