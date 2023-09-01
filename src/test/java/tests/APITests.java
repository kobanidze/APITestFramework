package tests;

import org.junit.jupiter.api.Test;
import utils.APIUtils;

import java.util.HashMap;

public class APITests {
    String BASE_URL = "https://send-request.me";
    int id;


    @Test
    public void testGetReq(){
        APIUtils.sendGetRequest(BASE_URL + "/api/companies/");
    }
    @Test
    public void testPostReq(){
        HashMap data = new HashMap();
        data.put("first_name", "Sam");
        data.put("last_name", "Watson");
        data.put("company_id", 3);
        id = APIUtils.sendPostRequest(BASE_URL + "/api/users/", data )
                .then().statusCode(201).extract()
                .jsonPath().getInt("user_id");
        System.out.println(id);
    }
    @Test
    public void testPutReq(){
        HashMap data = new HashMap();
        data.put("first_name", "Samuel");
        data.put("last_name", "Jackson");
        data.put("company_id", 3);
        id=20835;
        String uri = BASE_URL + "/api/users/"+id;
        APIUtils.sendPutRequest(uri,data)
                .then().statusCode(200);
        System.out.println(uri);
    }
    @Test
    public void testDeleteReq(){
        id=20835;
        APIUtils.sendDeleteRequest(BASE_URL + "/api/users/"+id)
                .then().statusCode(202).log().all();
    }

}
