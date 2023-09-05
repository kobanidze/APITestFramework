package utils;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class APIUtils {

    String baseUrl;
    String authToken;

    public APIUtils(String base, String login, String password) {
        this.baseUrl = base;
        this.authToken = given()
                    .contentType("application/json")
                    .body(Map.of(
                            "login", login,
                            "password", password
                    ))
                    .when()
                    .post(baseUrl + "api/auth/authorize")
                    .jsonPath()
                    .getString("token");
    }

    public Response sendGetRequest(String endpoint) {
        return given()
                .when()
                .get(baseUrl + endpoint);
    }


    public Response sendPostRequest(String endpoint, HashMap data) {
        return given()
                .contentType("application/json")
                .body(data)
                .when()
                .post(baseUrl + endpoint);
    }

    public Response sendPutRequest(String endpoint, HashMap data) {
        System.out.println(baseUrl + endpoint);
        return given()
                .contentType("application/json")
                .body(data)
                .when()
                .put(baseUrl + endpoint);

    }

    public Response sendDeleteRequest(String endpoint) {
        return given()
                .when()
                .delete(baseUrl + endpoint);
    }
}
