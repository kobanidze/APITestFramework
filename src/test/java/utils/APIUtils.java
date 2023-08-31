package utils;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class APIUtils {
    public static Response sendGetRequest(String endpoint) {
        return given()
                .when()
                .get(endpoint);
    }

    // Методы для POST, PUT, DELETE
}
