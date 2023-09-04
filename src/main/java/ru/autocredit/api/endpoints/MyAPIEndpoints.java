package ru.autocredit.api.endpoints;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyAPIEndpoints {
    @Value("${api.baseurl}")
    private String baseUrl; // Используем значение из application.properties

    public Response getCompanies() {
        return RestAssured.get(baseUrl + "/companies");
    }

    public Response getCompaniesByStatus(String status) {
        return RestAssured.get(baseUrl + "/companies?status=" + status);
    }

    public Response getCompaniesWithPagination(int limit, int offset) {
        return RestAssured.get(baseUrl + "/companies?limit=" + limit + "&offset=" + offset);
    }

    public Response createUser(String requestBody) {
        return RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .post(baseUrl + "/users");
    }

    public Response updateUser(int userId, String requestBody) {
        return RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .put(baseUrl + "/users/" + userId);
    }

    public Response deleteSomething(String something,int userId) {
        return RestAssured
                .delete(baseUrl + something + userId);
    }
}

