package ru.autocredit.api.MyAPITest;

import ru.autocredit.api.endpoints.MyAPIEndpoints;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class APITest {

    @Autowired
    private MyAPIEndpoints apiEndpoints;

    @BeforeEach
    public void setUp() {
        // Можно добавить настройку перед каждым тестом, например, загрузку начальных данных
    }

    @Test
    public void testGetCompanies() {
        // GET запрос для получения списка компаний
        Response response = apiEndpoints.getCompanies();

        // Проверка статус кода
        assertEquals(200, response.getStatusCode());

        // Проверка структуры объектов
        // Здесь вы можете добавить логику для проверки структуры JSON объектов

        // Проверка фильтрации по статусу
        Response filteredResponse = apiEndpoints.getCompaniesByStatus("active");
        // Здесь вы можете добавить логику для проверки фильтрации

        // Проверка фильтрации с использованием лимита и оффсета
        Response paginatedResponse = apiEndpoints.getCompaniesWithPagination(10, 0);
        // Здесь вы можете добавить логику для проверки пагинации
    }

    @Test
    public void testPostUser() {
        // Создание пользователя без привязки к компании
        Response response1 = apiEndpoints.createUser("{ \"first_name\": \"John\", \"last_name\": \"Doe\" }");
        assertEquals(201, response1.getStatusCode());

        // Создание пользователя с привязкой к компании
        Response response2 = apiEndpoints.createUser("{ \"first_name\": \"Alice\", \"last_name\": \"Smith\", \"company_id\": 1 }");
        assertEquals(201, response2.getStatusCode());

        // Попытка создать пользователя без обязательного поля
        Response response3 = apiEndpoints.createUser("{ \"first_name\": \"Bob\" }");
        assertEquals(400, response3.getStatusCode());

        // Попытка создать пользователя с привязкой к неактивной компании
        Response response4 = apiEndpoints.createUser("{ \"first_name\": \"Eve\", \"last_name\": \"Johnson\", \"company_id\": 2 }");
        assertEquals(400, response4.getStatusCode());
    }

    @Test
    public void testPutUser() {
        // Обновление полей у существующего юзера
        Response response1 = apiEndpoints.updateUser(1, "{ \"first_name\": \"Updated\", \"last_name\": \"User\" }");
        assertEquals(200, response1.getStatusCode());

        // Попытка обновить пользователя и привязать его к неактивной компании
        Response response2 = apiEndpoints.updateUser(1, "{ \"company_id\": 2 }");
        assertEquals(400, response2.getStatusCode());

        // Попытка обновить несуществующего юзера
        Response response3 = apiEndpoints.updateUser(999, "{ \"first_name\": \"Not\", \"last_name\": \"Found\" }");
        assertEquals(404, response3.getStatusCode());
    }

    @Test
    public void testDeleteUser() {
        // Удаление существующего юзера
        Response response1 = apiEndpoints.deleteUser(1);
        assertEquals(204, response1.getStatusCode());

        // Попытка удалить несуществующего юзера
        Response response2 = apiEndpoints.deleteUser(999);
        assertEquals(404, response2.getStatusCode());
    }
}
