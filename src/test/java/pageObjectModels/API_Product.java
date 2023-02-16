package pageObjectModels;

import helpers.ApiHandler;
import io.restassured.response.Response;
import models.Product;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

public class API_Product extends API{

    //endpoints
    private String productsServiceName = "products/";

    private final ApiHandler apiHandler;
    public API_Product(String root){
        apiHandler = new ApiHandler(root);
    }

    /**
     * Use this method to get products
     * @return
     */
    public Response getProducts() {
        System.out.println("Getting all products");
        return apiHandler.performGetRequest(productsServiceName);
    }

    /**
     * Use this method to get the details of specific product by product id
     * @param ProductId
     * @return
     */
    public Response getProduct(int ProductId) {
        System.out.println("Getting a product's details");
        return apiHandler.performGetRequest(productsServiceName + ProductId);
    }

    /**
     * Use this method to get the details of specific product by product name
     * @param productName
     * @return
     */
    public Response getProduct_byName(String productName){
        Map<String, Object> queries = new HashMap<String, Object>();
        queries.put("name", productName);
        return apiHandler.performGetRequest_withQueryParams(productsServiceName, queries);
    }

    /**
     * Use this method to get the product id by passing the product name
     * @param productName
     * @return
     */
    public int getProductID_byName(String productName){
        Response response = getProduct_byName(productName);
        return response.then().extract().path("data[0].id");
    }

    /**
     * Use this method to add a new product with the positive scenario
     * @param product
     * @return
     */
    public Response addNewProduct(Product product){
        return addNewProduct(product, statusCode.succcessStatusCode.getStatusCode());
    }

    /**
     * Use this method to add a new product with expected negative scenario
     * @param product
     * @param expectedStatusCode
     * @return
     */
    public Response addNewProduct(Product product, int expectedStatusCode){
        System.out.println("Creating a new product");
        Response response = apiHandler.performPostRequest(productsServiceName, product);
        response.then().assertThat().statusCode(expectedStatusCode);
        return response;
    }

    /**
     * Use this method to update the product with the positive scenario
     * @param productId
     * @param product
     * @return
     */
    public Response updateProduct(int productId, Product product){
        return updateProduct(productId, product, statusCode.okStatusCode.getStatusCode());
    }

    /**
     * Use this method to update the product with expected negative scenario
     * @param productId
     * @param product
     * @param expectedStatusCode
     * @return
     */
    public Response updateProduct(int productId, Product product, int expectedStatusCode){
        System.out.println("Updating an existing product");
        Response response = apiHandler.performPatchRequest(productsServiceName+productId, product);
        response.then().assertThat().statusCode(expectedStatusCode);
        return response;
    }

    /**
     * Use this method to delete the product by passing the product id
     * @param ProductId
     * @return
     */
    public Response deleteProduct(int ProductId){
        System.out.println("Deleting product");
        return apiHandler.performDeleteRequest(productsServiceName+ProductId);
    }

    /**
     * Use this method to verify that a specific product is exists
     * @param productId
     * @param productName
     */
    public void verifyThat_productExists(int productId, String productName){
        Response response = getProduct(productId);
        System.out.println("Verifying that the product : " + productName + " is exist");
        response.then().assertThat().body("name", equalTo(productName));
    }

    /**
     * Use this method to verify that a specific product is not exists
     * @param productId
     */
    public void verifyThat_productIsNotExist(int productId) {
        Response response = getProduct(productId);
        System.out.println("Verifying that the product of id: " + productId + " is not exist");
        response.then().assertThat().statusCode(statusCode.notFoundStatusCode.getStatusCode());
    }
}
