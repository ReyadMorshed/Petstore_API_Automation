package userOperations;

import static io.restassured.RestAssured.*;

import com.google.gson.Gson;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.*;

import org.json.simple.JSONObject;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    static String updatedValueOfPassword = "123@#";
    static String updatedValueOfFirstName = "Fazle";
    static int updatedValueOfUserStatus = 0;

    static int expectedStatusCode = 200;

    @Test(priority = 1)
    public void createUser() {
        // Request body payload

//        {
//            "id": 1000,
//            "username": "test_karim",
//            "firstName": "Mohammad",
//            "lastName": "Karim",
//            "email": "karim@gmail.com",
//            "password": "123456",
//            "phone": "256686456",
//            "userStatus": 1
//        }

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

        Response response = given().
                body(requestPayload.toJSONString()).
                contentType("application/json").
                when().
                post(BASE_URL + "/user").
                then().
                assertThat().
                statusCode(expectedStatusCode).
                log().all().
                extract().response();

        ValidatableResponse validatableResponse = response.then();

        // Are we getting 3 keys such as "code", "type" and "message"?
        validatableResponse.body("$", hasKey("code"));
        validatableResponse.body("$", hasKey("type"));
        validatableResponse.body("$", hasKey("message"));

        // Are we getting 3 values for 3 keys such as 200, "unknown" and "1000"?
        validatableResponse.body("code", is(notNullValue()));
        validatableResponse.body("type", is(notNullValue()));
        validatableResponse.body("message", is(notNullValue()));

        JsonPath jsonPath = response.jsonPath();
        int actualValueOfCode = jsonPath.get("code");
        String actualValueOfType = jsonPath.get("type");
        String actualValueOfMessage = jsonPath.get("message");

        // Are we getting expected values for the keys?
        assertEquals(actualValueOfCode, expectedStatusCode);
        assertEquals(actualValueOfType, "unknown");
        assertEquals(actualValueOfMessage, Integer.toString(valueOfId));

//        {
//            "code": 200,
//            "type": "unknown",
//            "message": "1000"
//        }

    }

    @Test(priority = 2)
    public void getUserByUsername() {

        Response response = given().
                pathParam("username", valueOfUsername).
                when().
                get(BASE_URL + "/user/{username}").
                then().
                assertThat().statusCode(expectedStatusCode).
                log().all().
                extract().response();

        JsonPath jsonPath = response.jsonPath();
        String actualValueOfUsername = jsonPath.get("username");
        assertEquals(actualValueOfUsername, valueOfUsername, "User not found");
    }

    @Test(priority = 3)
    public void updateUser() {

        Map<String, Object> userUpdateRequestPayload = new HashMap<String, Object>();
        userUpdateRequestPayload.put("id", valueOfId);
        userUpdateRequestPayload.put("username", valueOfUsername);
        userUpdateRequestPayload.put("firstName", updatedValueOfFirstName);
        userUpdateRequestPayload.put("lastName", valueOfLastName);
        userUpdateRequestPayload.put("email", valueOfEmail);
        userUpdateRequestPayload.put("password", updatedValueOfPassword);
        userUpdateRequestPayload.put("phone", valueOfPhone);
        userUpdateRequestPayload.put("userStatus", updatedValueOfUserStatus);

        JSONObject requestPayload = new JSONObject(userUpdateRequestPayload);
        System.out.println(requestPayload);

        Response response = given().
                pathParam("username", valueOfUsername).
                body(requestPayload.toJSONString()).
                contentType("application/json").
                when().
                put(BASE_URL + "/user/{username}").
                then().
                assertThat().statusCode(expectedStatusCode).
                log().all().
                extract().response();

        JsonPath jsonPath = response.jsonPath();

        String actualValueOfMessage = jsonPath.get("message");
        assertEquals(actualValueOfMessage, Integer.toString(valueOfId), "User information not updated");

//        {
//            "code": 200,
//            "type": "unknown",
//            "message": "1000"
//        }
    }

    @Test(priority = 4)
    public void getUpdatedUserInformationByUsername() {

        Response response = given().
                pathParam("username", valueOfUsername).
                when().
                get(BASE_URL + "/user/{username}").
                then().
                assertThat().statusCode(expectedStatusCode).
                log().all().
                extract().response();

        JsonPath jsonPath = response.jsonPath();
        String actualValueOfUsername = jsonPath.get("username");
        assertEquals(actualValueOfUsername, valueOfUsername);

        String actualValueOfFirstName = jsonPath.get("firstName");
        assertEquals(actualValueOfFirstName, updatedValueOfFirstName);

        String actualValueOfPassword = jsonPath.get("password");
        assertEquals(actualValueOfPassword, updatedValueOfPassword);

        int actualValueOfUserStatus = jsonPath.get("userStatus");
        assertEquals(actualValueOfUserStatus, updatedValueOfUserStatus);
    }

    @Test (priority = 5)
    public void deleteUser() {

        Response response = given().
                pathParam("username", valueOfUsername).
                when().
                delete(BASE_URL + "/user/{username}").
                then().
                assertThat().statusCode(expectedStatusCode).
                log().all().
                extract().response();

        JsonPath jsonPath = response.jsonPath();
        String actualValueOfMessage = jsonPath.get("message");
        assertEquals(actualValueOfMessage, valueOfUsername);
    }

    @Test (priority = 6)
    public void createListOfUserWithArray() {

//        [
//            {
//                    "id":1001,
//                    "username":"test001",
//                    "firstName":"Mohammad",
//                    "lastName":"Karim",
//                    "email":"karim@gmail.com",
//                    "password":"123456",
//                    "phone":"256686456",
//                    "userStatus":1
//            },
//            {
//                    "id":1002,
//                    "username":"test002",
//                    "firstName":"Mohammad",
//                    "lastName":"Rahim",
//                    "email":"rahim@gmail.com",
//                    "password":"123456",
//                    "phone":"256686457",
//                    "userStatus":1
//            }
//        ]

        Map<String, Object> firstUserCreatePayload = new HashMap<String, Object>();
        firstUserCreatePayload.put("id", 1001);
        firstUserCreatePayload.put("username", "test001");
        firstUserCreatePayload.put("firstName", "Mohammad");
        firstUserCreatePayload.put("lastName", "Karim");
        firstUserCreatePayload.put("email", "karim@gmail.com");
        firstUserCreatePayload.put("password", "123456");
        firstUserCreatePayload.put("phone", "256686456");
        firstUserCreatePayload.put("userStatus", 1);

        Map<String, Object> secondUserCreatePayload = new HashMap<String, Object>();
        secondUserCreatePayload.put("id", 1002);
        secondUserCreatePayload.put("username", "test002");
        secondUserCreatePayload.put("firstName", "Mohammad");
        secondUserCreatePayload.put("lastName", "Rahim");
        secondUserCreatePayload.put("email", "rahim@gmail.com");
        secondUserCreatePayload.put("password", "123456");
        secondUserCreatePayload.put("phone", "256686457");
        secondUserCreatePayload.put("userStatus", 1);

        List<Map<String, Object>> arrayOfListOfUsers = new ArrayList<>();
        arrayOfListOfUsers.add(firstUserCreatePayload);
        arrayOfListOfUsers.add(secondUserCreatePayload);

        Gson gson = new Gson();
        String requestBodyPayload = gson.toJson(arrayOfListOfUsers);
        System.out.println(requestBodyPayload);

        Response response = given().
                body(requestBodyPayload).
                contentType("application/json").
                when().
                post(BASE_URL + "/user/createWithArray").
                then().
                assertThat().statusCode(expectedStatusCode).
                log().all().
                extract().response();

        JsonPath jsonPath = response.jsonPath();
        int actualValueOfCode = jsonPath.get("code");
        assertEquals(actualValueOfCode, expectedStatusCode);

        String actualValueOfMessage = jsonPath.get("message");
        assertEquals(actualValueOfMessage, "ok");

//        {
//            "code": 200,
//            "type": "unknown",
//            "message": "ok"
//        }

    }

    @Test (priority = 7)
    public void createListOfUserWithList() {

//        [
//            {
//                "id":1003,
//                "username":"test003",
//                "firstName":"Mohammad",
//                "lastName":"Reyad",
//                "email":"reyad@gmail.com",
//                "password":"123456",
//                "phone":"256686451",
//                "userStatus":1
//            },
//            {
//                "id":1004,
//                "username":"test004",
//                "firstName":"Mohammad",
//                "lastName":"Mahmud",
//                "email":"mahmud@gmail.com",
//                "password":"123456",
//                "phone":"256686452",
//                "userStatus":1
//            }
//        ]

        Map<String, Object> firstUserCreatePayload = new HashMap<String, Object>();
        firstUserCreatePayload.put("id", 1003);
        firstUserCreatePayload.put("username", "test003");
        firstUserCreatePayload.put("firstName", "Mohammad");
        firstUserCreatePayload.put("lastName", "Reyad");
        firstUserCreatePayload.put("email", "reyad@gmail.com");
        firstUserCreatePayload.put("password", "123456");
        firstUserCreatePayload.put("phone", "256686451");
        firstUserCreatePayload.put("userStatus", 1);

        Map<String, Object> secondUserCreatePayload = new HashMap<String, Object>();
        secondUserCreatePayload.put("id", 1004);
        secondUserCreatePayload.put("username", "test004");
        secondUserCreatePayload.put("firstName", "Mohammad");
        secondUserCreatePayload.put("lastName", "Mahmud");
        secondUserCreatePayload.put("email", "mahmud@gmail.com");
        secondUserCreatePayload.put("password", "123456");
        secondUserCreatePayload.put("phone", "256686452");
        secondUserCreatePayload.put("userStatus", 1);

        List<Map<String, Object>> arrayOfListOfUsers = new ArrayList<>();
        arrayOfListOfUsers.add(firstUserCreatePayload);
        arrayOfListOfUsers.add(secondUserCreatePayload);

        Gson gson = new Gson();
        String requestBodyPayload = gson.toJson(arrayOfListOfUsers);
        System.out.println(requestBodyPayload);

        Response response = given().
                body(requestBodyPayload).
                contentType("application/json").
                when().
                post(BASE_URL + "/user/createWithList").
                then().
                assertThat().statusCode(expectedStatusCode).
                log().all().
                extract().response();

        JsonPath jsonPath = response.jsonPath();
        int actualValueOfCode = jsonPath.get("code");
        assertEquals(actualValueOfCode, expectedStatusCode);

        String actualValueOfMessage = jsonPath.get("message");
        assertEquals(actualValueOfMessage, "ok");

//        {
//            "code": 200,
//            "type": "unknown",
//            "message": "ok"
//        }

    }

}
