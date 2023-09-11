package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import utils.APIUtils;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.HashMap;

import static config.APIConfig.*;

@TestMethodOrder(OrderAnnotation.class)
public class APITests {
    private static int id;
    static APIUtils apiUtils;


    @BeforeAll
    public static void initAPI() {
        apiUtils = new APIUtils(BASE_URL, DEFAULT_LOGIN, DEFAULT_PASSWORD);
    }


    @Test
    @Order(1)
    public void testGetReq(){
        apiUtils.sendGetRequest("api/companies/");
    }
    @Test
    @Order(2)
    @DisplayName("post-запрос")
    @Description("Тут описание теста")
    @Step("Проверка отправки пост запроса")
    public void testPostReq(){
        HashMap data = new HashMap();
        data.put("first_name", "Sam");
        data.put("last_name", "Watson");
        data.put("company_id", 3);
        this.id = apiUtils.sendPostRequest( "api/users/", data)
                .then().statusCode(201).extract()
                .jsonPath().getInt("user_id");
    }

    @Test
    @Order(3)
    public void testPutReq(){
        HashMap data = new HashMap();
        data.put("first_name", "Samuel");
        data.put("last_name", "Jackson");
        data.put("company_id", 3);
        String uri = "api/users/" + id;
        apiUtils.sendPutRequest(uri,data)
                .then().statusCode(200);
    }

    @Test
    @Order(4)
    public void testDeleteReq(){
        apiUtils.sendDeleteRequest("api/users/" + id)
                .then()
                .statusCode(202)
                .log()
                .all();
    }

    @Test
    @Order(5)
    public void testAuthReq(){
        apiUtils.sendGetRequestWithAuth("api/auth/me")
                .then()
                .statusCode(200);
    }


}
