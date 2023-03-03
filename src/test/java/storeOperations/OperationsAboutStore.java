package storeOperations;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import static org.testng.Assert.*;

import static io.restassured.RestAssured.given;

public class OperationsAboutStore {

    public static final String BASE_URL = "https://petstore.swagger.io/v2";
    static int expectedStatusCode = 200;
    static int expectedOrderId = 10;
    static  int expectedPetId = 100;
    static int expectedQty = 20;

    boolean expectedComplete = true;
    @Test(priority = 1)
    public void placeAOrder (){
//
        Map<String, Object> storeRequestPayload = new HashMap<String, Object>();
        storeRequestPayload.put("id","10");
        storeRequestPayload.put("petId","100");
        storeRequestPayload.put("quantity","20");
        storeRequestPayload.put("shipDate","2023-02-26T00:06:30.855Z");
        storeRequestPayload.put("status","placed");
        storeRequestPayload.put("complete","true");

        JSONObject requestPayload = new JSONObject(storeRequestPayload);
        System.out.println(requestPayload);

        Response response =
                given()
                .body(requestPayload.toString())
                .contentType("application/json")
                .when().post(BASE_URL+"/store/order")
                .then()
                .assertThat().statusCode(expectedStatusCode)
                .log().all().extract().response();

        JsonPath jsonPath = response.jsonPath();

        int actualOrderId = jsonPath.get("id");
        int actualPetId = jsonPath.get("petId");
        int actualQty = jsonPath.get("quantity");
        String actualShippingDate = jsonPath.get("shipDate");
        String actualStatus = jsonPath.get("status");
        boolean actualComplete = jsonPath.get("complete");

        assertEquals(actualOrderId,expectedOrderId);
        assertEquals(actualPetId, expectedPetId);
        assertEquals(actualQty,expectedQty);
        assertEquals(actualShippingDate,"2023-02-26T00:06:30.855+0000");
        assertEquals(actualStatus, "placed");
        assertEquals(actualComplete, expectedComplete);

    }

    @Test(priority = 2)
    public void getOrderDetails (){

        Response response =
                given().pathParam("orderId", expectedOrderId)
                .contentType("application/json")
                .when().get(BASE_URL+"/store/order/{orderId}")
                .then().assertThat().statusCode(expectedStatusCode)
                .log().all().extract().response();

        JsonPath jsonPath = response.jsonPath();
        int actualOrderId = jsonPath.get("id");
        int actualPetId = jsonPath.get("petId");
        int actualQty = jsonPath.get("quantity");
        String actualShippingDate = jsonPath.get("shipDate");
        String actualStatus = jsonPath.get("status");
        boolean actualComplete = jsonPath.get("complete");

        assertEquals(actualOrderId,expectedOrderId);
        assertEquals(actualPetId, expectedPetId);
        assertEquals(actualQty,expectedQty);
        assertEquals(actualShippingDate,"2023-02-26T00:06:30.855+0000");
        assertEquals(actualStatus, "placed");
        assertEquals(actualComplete, expectedComplete);
    }

    @Test(priority = 3)
    public void deleteByOrderId(){
        Response response =
                given()
                        .pathParam("orderId", expectedOrderId).contentType("application/json")
                        .when().delete(BASE_URL+"/store/order/{orderId}")
                        .then()
                        .assertThat().statusCode(expectedStatusCode).
                        log().all().extract().response();

        JsonPath jsonPath = response.jsonPath();

        int actualCode  = jsonPath.get("code");
        String actualMessage = jsonPath.get("message");

        assertEquals(actualCode, expectedStatusCode);
        assertEquals(actualMessage, Integer.toString(expectedOrderId));

    }

    @Test(priority = 4)
    public void inventoryStatus(){

        Response response =
                given().contentType("application/json")
                        .when().get(BASE_URL+"/store/inventory")
                        .then()
                        .assertThat().statusCode(expectedStatusCode)
                        .log().all().extract().response();

        JsonPath jsonPath = response.jsonPath();

        int actualSold = jsonPath.get("sold");
        int actualString = jsonPath.get("string");

        int actualAvailable = jsonPath.get("available");

//        assertEquals(actualSold,20);
//        assertEquals(actualString,625);
//        //assertEquals(actualPending,2);
//        assertEquals(actualAvailable,355);
    }
}
