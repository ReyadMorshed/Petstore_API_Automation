package userOperations;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;


public class OperationsAboutUser {
    public static final String BASE_URL = "https://petstore.swagger.io/v2";
    static int valueOfId = 1000;
    static String valueOfUsername = "test_karim";
    static String valueOfFirstName = "Mohammad";
    static String valueOfLastName = "Karim";
    static String valueOfEmail = "karim@gmail.com";
    static String valueOfPassword = "123456";
    static String valueOfPhone = "256686456";
    static int valueOfUserStatus = 1;

    @Test
    public void creteUser () {
        Map<String, Object> userCreateRequestPayload = new HashMap<String, Object>();
        userCreateRequestPayload.put("id", valueOfId);
        userCreateRequestPayload.put("username", valueOfUsername);
        userCreateRequestPayload.put("firstName", valueOfFirstName);
        userCreateRequestPayload.put("lastName", valueOfLastName);
        userCreateRequestPayload.put("email", valueOfEmail);
        userCreateRequestPayload.put("password", valueOfPassword);
        userCreateRequestPayload.put("phone", valueOfPhone);
        userCreateRequestPayload.put("userStatus", valueOfUserStatus);

        JSONObject requestPayload = new JSONObject(userCreateRequestPayload);
        System.out.println(requestPayload);

        Response response = given()
                .body(requestPayload.toJSONString())
                .contentType("application/json")
                .when()
                .post(BASE_URL+"/user")
                .then()
                .assertThat().statusCode(200)
                .log().all().extract().response();

        JsonPath jsonPath = response.jsonPath();
        int actualvalueofCode = jsonPath.get("code");

        assertEquals(actualvalueofCode, 200);



    }

}
