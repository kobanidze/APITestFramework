package utils;

import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class APIUtils {
    public static Response sendGetRequest(String endpoint) {
        return given()
                .when()
                .get(endpoint);
    }

    public static Response sendPostRequest(String endpoint, HashMap data) {
        return given()
                .contentType("application/json")
                .body(data)
                .when()
                .post(endpoint);
    }
    public static Response sendPutRequest(String endpoint, HashMap data) {
        return given()
                .contentType("application/json")
                .body(data)
                .when()
                .put(endpoint);
    }
    public static Response sendDeleteRequest(String endpoint) {
        return given()
                .when()
                .delete(endpoint);
    }

    public static void validateJsonSchema(Response response, String pathToSchema){
             response.then().assertThat()
                .body(matchesJsonSchemaInClasspath(pathToSchema));
    }
}
