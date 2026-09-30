package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import praktikum.client.OrderClient;
import praktikum.client.UserClient;
import praktikum.model.*;
import praktikum.util.UserGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Stellar Burgers API")
@Feature("Создание заказа")
@ExtendWith(AllureJunit5.class)
@DisplayName("Тесты создания заказа")
class OrderCreationTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;
    private List<String> validIngredientIds;

    @BeforeEach
    void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();

        Response ingredientsResponse = orderClient.getIngredients();
        assertEquals(200, ingredientsResponse.statusCode());
        validIngredientIds = ingredientsResponse.jsonPath()
                .getList("data._id", String.class);

        User user = UserGenerator.randomUser();
        Response reg = userClient.register(user);
        accessToken = reg.as(LoginResponse.class).accessToken;
    }

    @AfterEach
    void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создать заказ с авторизацией и ингредиентами")
    void createOrderWithAuthAndIngredients() {
        Response response = orderClient.createOrder(accessToken, validIngredientIds);

        assertEquals(200, response.statusCode());
        OrderResponse body = response.as(OrderResponse.class);
        assertTrue(body.success);
        assertNotNull(body.name);
        assertTrue(body.order.number > 0);
    }

    @Test
    @DisplayName("Создать заказ без авторизации — 401")
    void createOrderWithoutAuth() {
        Response response = orderClient.createOrderWithoutAuth(validIngredientIds);

        assertEquals(401, response.statusCode());
        assertEquals("You should be authorised",
                response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Создать заказ без ингредиентов — 400")
    void createOrderWithoutIngredients() {
        Response response = orderClient.createOrderWithoutIngredients(accessToken);

        assertEquals(400, response.statusCode());
        assertFalse(response.as(ErrorResponse.class).success);
        assertEquals("Ingredient ids must be provided",
                response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Создать заказ с неверным хешем ингредиента — 500")
    void createOrderWithInvalidIngredientHash() {
        List<String> invalidIds = List.of("invalid_hash_12345");

        Response response = orderClient.createOrder(accessToken, invalidIds);

        assertEquals(500, response.statusCode());
    }
}