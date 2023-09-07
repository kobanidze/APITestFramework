package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class APIUtils {

    /**Базовый URL*/
    String baseUrl;
    /**Токен аунтефикации*/
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
        if (parameter.isSet(Options.CONTENT_TYPE)) request = request.contentType(parameter.get(Options.CONTENT_TYPE, String.class));
        if (((Boolean) true).equals(parameter.get(Options.WITH_TOKEN))) request.header("x-token", authToken);
        if (parameter.isSet(Options.HEADERS)) {
            Map<String, Object> headers = parameter.get(Options.HEADERS, Map.class);
            for (String key : headers.keySet()) {
                request.header(key, headers.get(key));
            }
        }
        if (parameter.isSet(Options.DATA)) request.body(parameter.get(Options.DATA));
        return request;
    }


    public Response sendGetRequest(String endpoint) {
        return prepareRequest(new Parameters(new HashMap<>()))
                .get(baseUrl + endpoint);
    }

    public Response sendGetRequestWithAuth(String endpoint) {
        return prepareRequest(new Parameters(Map.of(Options.WITH_TOKEN, true))).get(baseUrl + endpoint);
    }

    public Response sendPostRequest(String endpoint, HashMap data) {
        return prepareRequest(new Parameters(Map.of(
                Options.DATA, data,
                Options.CONTENT_TYPE, "application/json")))
                .post(baseUrl + endpoint);
    }

    public Response sendPutRequest(String endpoint, HashMap data) {
        return prepareRequest(new Parameters(Map.of(
                Options.DATA, data,
                Options.CONTENT_TYPE, "application/json")))
                .put(baseUrl + endpoint);
    }

    public Response sendDeleteRequest(String endpoint) {
        return prepareRequest(new Parameters(new HashMap<>()))
                .delete(baseUrl + endpoint);
    }
}
