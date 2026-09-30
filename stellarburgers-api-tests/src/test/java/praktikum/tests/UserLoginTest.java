package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import praktikum.client.UserClient;
import praktikum.model.LoginResponse;
import praktikum.model.User;
import praktikum.util.UserGenerator;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Stellar Burgers API")
@Feature("Логин пользователя")
@ExtendWith(AllureJunit5.class)
@DisplayName("Тесты логина пользователя")
class UserLoginTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @BeforeEach
    void setUp() {
        userClient = new UserClient();
        user = UserGenerator.randomUser();
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
    @DisplayName("Логин под существующим пользователем")
    @Description("POST /api/auth/login с валидными данными возвращает 200 и токены")
    void loginExistingUser() {
        Response response = userClient.login(user);

        assertEquals(200, response.statusCode());

        LoginResponse body = response.as(LoginResponse.class);
        assertTrue(body.success);
        assertEquals(user.email, body.user.email);
        assertNotNull(body.accessToken);
        assertNotNull(body.refreshToken);
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    @Description("POST /api/auth/login с неверными данными возвращает 401")
    void loginWithWrongCredentials() {
        User wrong = new User("wrong_" + System.currentTimeMillis() + "@mail.ru",
                "wrongpass", "Wrong");

        Response response = userClient.login(wrong);

        assertEquals(401, response.statusCode());
        assertFalse(response.as(LoginResponse.class).success);
        assertEquals("email or password are incorrect",
                response.jsonPath().getString("message"));
    }
}