package tests;

import helpers.PropertyReader;
import io.restassured.response.Response;
import jdk.jfr.Description;
import models.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import pageObjectModels.API_Product;

import static org.hamcrest.Matchers.equalTo;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductTests {
    private API_Product apiProduct ;
    private PropertyReader reader;

    @BeforeAll
    public void beforeAll(){
        reader = new PropertyReader();
        apiProduct = new API_Product(reader.getPropertyValue("rootURL"));

        // Perform prerequisites for test cases

        // Update the product to the original state for test case TC-02
        String originalProductName = "Duracell - AA Batteries (8-Pack)";
        Product originalProduct = new Product(
                originalProductName,
                "HardGood",
                7.49,
                0,
                "041333825014",
                "Compatible with select electronic devices; AA size; DURALOCK Power Preserve technology; 8-pack",
                "Duracell",
                "MN1500B8Z",
                "http://www.bestbuy.com/site/duracell-aa-batteries-8-pack/127687.p?id=1051384045676&skuId=127687&cmp=RMXCC",
                "http://img.bbystatic.com/BestBuy_US/images/products/1276/127687_sa.jpg"
        );
        apiProduct.updateProduct(apiProduct.getProductID_byName(originalProductName), originalProduct);
    }

    @Test
    @Description("TC01 - Creating a new product and check that the product is created successfully")
    public void addNewProduct(){
        String productName = "Duracell - 900A Jump";
        Product product = new Product(
                productName,
                "Battery",
                20,
                3.9,
                "041333415017",
                "Get back on the road quickly with the Duracell Jump-Starter 900",
                "Duracell",
                "DRJS30",
                "https://www.bestbuy.com/site/duracell-900a",
                "https://www.bestbuy.com/site/duracell-900a-image"
        );
        Response response = apiProduct.addNewProduct(product);
        int newProductId = response.then().extract().path("id");

        apiProduct.verifyThat_productExists(newProductId, productName);
    }

    @Test
    @Description("TC-02 : Update existing product and check that the product is updated successfully")
    public void updateProduct(){
        String productName = "Duracell - AA Batteries (8-Pack)";
        int productId = apiProduct.getProductID_byName(productName);

        System.out.println("Verifying on the product's price before updating it");
        Response response = apiProduct.getProduct(productId);
        float price = response.then().extract().path("price");
        Assertions.assertEquals(7.489999771118164, price);

        Product updateProduct = new Product(
                productName,
                "HardGood",
                11.75,
                0,
                "041333825014",
                "Compatible with select electronic devices; AA size; DURALOCK Power Preserve technology; 8-pack",
                "Duracell",
                "MN1500B8Z",
                "http://www.bestbuy.com/site/duracell-aa-batteries-8-pack/127687.p?id=1051384045676&skuId=127687&cmp=RMXCC",
                "http://img.bbystatic.com/BestBuy_US/images/products/1276/127687_sa.jpg"
        );

        apiProduct.updateProduct(productId, updateProduct);

        System.out.println("Verifying on the product's price after updating it");
        response = apiProduct.getProduct(productId);
        price = response.then().extract().path("price");
        Assertions.assertEquals(11.75, price);
    }

    @Test
    @Description("TC-03 : Delete an existing product and check that the product is deleted successfully")
    public void deleteProduct(){
        int productId = apiProduct.getProductID_byName("Energizer - MAX Batteries AA (4-Pack)");
        apiProduct.deleteProduct(productId);

        apiProduct.verifyThat_productIsNotExist(productId);
    }

    @ParameterizedTest()
    @NullSource
    @ValueSource(strings = {""})
    @Description("TC-04 : Add new product with null, or Empty product name parameter")
    public void addProduct_withInvalidParameter(String productName){
        Product product = new Product(
                productName,
                "Charger",
                6,
                0,
                "041333415017",
                "Super charging wireless charger with 25 Watt",
                "Anker",
                "DRJS30",
                "https://www.bestbuy.com/site/duracell-900a",
                "https://www.bestbuy.com/site/duracell-900a-image"
        );
        Response response = apiProduct.addNewProduct(product, API_Product.statusCode.badRequestStatusCode.getStatusCode());
        System.out.println("Verifying on the add product response error message");
        response.then().assertThat().body("message", equalTo("Invalid Parameters"));
    }

    @AfterAll
    public void afterAll(){
        // Perform cleansing activities

        // Delete the added product from test case TC-01
        apiProduct.deleteProduct(apiProduct.getProductID_byName("Duracell - 900A Jump"));

        // Add the product that deleted in the test case TC-03
        Product toBeDeletedProduct = new Product(
                "Energizer - MAX Batteries AA (4-Pack)",
                "HardGood",
                4.99,
                0,
                "039800011329",
                "4-pack AA alkaline batteries; battery tester included",
                "Energizer",
                "E91BP-4",
                "http://www.bestbuy.com/site/energizer-max-batteries-aa-4-pack/150115.p?id=1051384046217&skuId=150115&cmp=RMXCC",
                "http://img.bbystatic.com/BestBuy_US/images/products/1501/150115_sa.jpg"

        );
        apiProduct.addNewProduct(toBeDeletedProduct);
    }
}
