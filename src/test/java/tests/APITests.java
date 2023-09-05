package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import utils.APIUtils;
import java.util.HashMap;

public class APITests {
    String BASE_URL = "https://send-request.me";
    int id;


    @Test
    public void testGetReq(){
        Response response = APIUtils.sendGetRequest(BASE_URL + "/api/users/21178");
        APIUtils.validateJsonSchema(response, "getService.json");
    }
    @Test
    @DisplayName("post-запрос")
    @Description("Тут описание теста")
    @Step("Проверка отправки пост запроса")
    public void testPostReq() {
        HashMap data = new HashMap();
        data.put("first_name", "Sam");
        data.put("last_name", "Watson");
        data.put("company_id", 3);
        id = APIUtils.sendPostRequest(BASE_URL + "/api/users/", data )
                .then().statusCode(201).extract()
                .jsonPath().getInt("user_id");
    }
    @Test
    public void testPutReq(){
        HashMap data = new HashMap();
        data.put("first_name", "Samuel");
        data.put("last_name", "Jackson");
        data.put("company_id", 3);
        id=21037;
        String uri = BASE_URL + "/api/users/"+id;
        Response response = APIUtils.sendPutRequest(uri,data);
        response.then().statusCode(200);
        System.out.println(this.getClass().getResource("/").getPath());
        APIUtils.validateJsonSchema(response, "getService.json");

    }
    @Test
    public void testDeleteReq(){
        id=20835;
        APIUtils.sendDeleteRequest(BASE_URL + "/api/users/"+id)
                .then().statusCode(202).log().all();
    }

}
