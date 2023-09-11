package utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class APIUtils {

    //логер
    private static final Logger logger = LogManager.getLogger(APIUtils.class);

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
        RequestSpecification request =
                given()
                    .filter(new AllureRestAssured());
        if (parameter.isSet(Options.CONTENT_TYPE)) {
            String contentType = parameter.get(Options.CONTENT_TYPE, String.class);
            request = request.contentType(contentType);
            logger.debug("CONTENT TYPE: {}", contentType);
        }
        if (((Boolean) true).equals(parameter.get(Options.WITH_TOKEN))) {
            request.header("x-token", authToken);
            logger.debug("Token: {}", authToken);
        }
        if (parameter.isSet(Options.HEADERS)) {
            Map<String, Object> headers = parameter.get(Options.HEADERS, Map.class);
            for (String key : headers.keySet()) {
                request.header(key, headers.get(key));
            }
            logger.debug("Headers: {}", headers);
        }
        if (parameter.isSet(Options.DATA)) {
            Object data = parameter.get(Options.DATA);
            request.body(data);
            logger.debug("Data: {}", data);
        }
        return request;
    }

    public Response sendGetRequest(String endpoint) {
        try {
            logger.info("Отправка GET запроса на: {}", baseUrl + endpoint);

            Response response = prepareRequest(new Parameters()).get(baseUrl + endpoint);

            logger.info("Получили ответ с кодом: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            logger.error("Ошибка во время GET запроса", e);
            throw e;
        }
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

    public static void validateJsonSchema(Response response, String pathToSchema) {
        try {
            response.then().assertThat()
                    .body(matchesJsonSchemaInClasspath(pathToSchema));
        } catch (Exception e) {
            logger.error("Ошибка при валидации JSON схемы", e);
        }
    }


}
