package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

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

    RequestSpecification prepareRequest(Parameters parameter) {
        RequestSpecification request = given();
        if (parameter.isSet("contentType")) request = request.contentType(parameter.get("contentType", String.class));
        if (((Boolean) true).equals(parameter.get("withToken"))) request.header("x-token", authToken);
        if (parameter.isSet("headers")) {
            Map<String, Object> headers = parameter.get("headers", Map.class);
            for (String key : headers.keySet()) {
                request.header(key, headers.get(key));
            }
        }
        if (parameter.isSet("data")) request.body(parameter.get("data"));
        return request;
    }


    public Response sendGetRequest(String endpoint) {
        return prepareRequest(new Parameters(new HashMap<>()))
                .get(baseUrl + endpoint);
    }

    public Response sendGetRequestWithAuth(String endpoint) {
        return prepareRequest(new Parameters(Map.of("withToken", true))).get(baseUrl + endpoint);
    }

    public Response sendPostRequest(String endpoint, HashMap data) {
        return prepareRequest(new Parameters(Map.of(
                "data", data,
                "contentType", "application/json")))
                .post(baseUrl + endpoint);
    }

    public Response sendPutRequest(String endpoint, HashMap data) {
        return prepareRequest(new Parameters(Map.of(
                "data", data,
                "contentType", "application/json")))
                .put(baseUrl + endpoint);
    }

    public Response sendDeleteRequest(String endpoint) {
        return prepareRequest(new Parameters(new HashMap<>()))
                .delete(baseUrl + endpoint);
    }
}
