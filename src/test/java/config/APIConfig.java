package config;

import io.restassured.RestAssured;

public class APIConfig {
    public static final String BASE_URL = "https://send-request.me/"; // Ваш базовый URL

    static {
        RestAssured.baseURI = BASE_URL;
    }
}
