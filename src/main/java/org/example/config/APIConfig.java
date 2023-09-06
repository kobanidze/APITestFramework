package org.example.config;

import io.restassured.RestAssured;

public class APIConfig {
    public static final String BASE_URL = "https://send-request.me/"; // Ваш базовый URL

    static {
        RestAssured.baseURI = BASE_URL;
    }

    public static final String DEFAULT_LOGIN = "Nya";
    public static final String DEFAULT_PASSWORD = "qwerty12345";
}
