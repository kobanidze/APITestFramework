package utils;

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

    RequestSpecification prepareRequest(
            HashMap data, HashMap<String, Object> headers, boolean withToken, String contentType
    ) {
        RequestSpecification request = given();
        if (contentType != null) request = request.contentType(contentType);
        if (withToken) request.header("x-token", authToken);
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                request.header(entry.getKey(), entry.getValue());
            }
        }
        if (data != null) request.body(data);
        return request;
    }


    public Response sendGetRequest(String endpoint) {
        return prepareRequest(null, null, false, null).get(baseUrl + endpoint);
    }

    public Response sendGetRequestWithAuth(String endpoint) {
        return prepareRequest(null, null, true, null).get(baseUrl + endpoint);
    }

    public Response sendPostRequest(String endpoint, HashMap data) {
        return prepareRequest(data, null, false, "application/json").post(baseUrl + endpoint);
    }

    public Response sendPutRequest(String endpoint, HashMap data) {
        return prepareRequest(data, null, false, "application/json").get(baseUrl + endpoint);
    }

    public Response sendDeleteRequest(String endpoint) {
        return prepareRequest(null, null, false, null).get(baseUrl + endpoint);
    }

}
