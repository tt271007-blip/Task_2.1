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
import praktikum.model.LoginResponse;
import praktikum.model.OrdersResponse;
import praktikum.model.User;
import praktikum.util.UserGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Stellar Burgers API")
@Feature("Получение заказов пользователя")
@ExtendWith(AllureJunit5.class)
@DisplayName("Тесты получения заказов пользователя")
class UserOrdersTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;
    private List<String> validIngredientIds;

    @BeforeEach
    void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();

        validIngredientIds = orderClient.getIngredients()
                .jsonPath().getList("data._id", String.class);

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
    @DisplayName("Получить заказы авторизованного пользователя")
    void getOrdersForAuthorizedUser() {
        orderClient.createOrder(accessToken, validIngredientIds);

        Response response = orderClient.getUserOrders(accessToken);

        assertEquals(200, response.statusCode());
        OrdersResponse body = response.as(OrdersResponse.class);
        assertTrue(body.success);
        assertNotNull(body.orders);
        assertFalse(body.orders.isEmpty());
        assertTrue(body.total >= 1);
    }

    @Test
    @DisplayName("Получить заказы без авторизации — 401")
    void getOrdersWithoutAuth() {
        Response response = orderClient.getUserOrdersWithoutAuth();

        assertEquals(401, response.statusCode());
        assertEquals("You should be authorised",
                response.jsonPath().getString("message"));
    }
}