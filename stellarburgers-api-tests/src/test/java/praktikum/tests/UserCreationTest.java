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
import praktikum.model.RegisterResponse;
import praktikum.model.User;
import praktikum.util.UserGenerator;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Stellar Burgers API")
@Feature("Создание пользователя")
@ExtendWith(AllureJunit5.class)
@DisplayName("Тесты создания пользователя")
class UserCreationTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @BeforeEach
    void setUp() {
        userClient = new UserClient();
        user = UserGenerator.randomUser();
    }

    @AfterEach
    void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создать уникального пользователя")
    @Description("POST /api/auth/register с валидными данными возвращает 200 и токены")
    void createUniqueUser() {
        Response response = userClient.register(user);

        assertEquals(200, response.statusCode());

        RegisterResponse body = response.as(RegisterResponse.class);
        assertTrue(body.success);
        assertEquals(user.email, body.user.email);
        assertEquals(user.name, body.user.name);
        assertNotNull(body.accessToken);
        assertNotNull(body.refreshToken);

        accessToken = body.accessToken;
    }

    @Test
    @DisplayName("Создать уже зарегистрированного пользователя")
    @Description("Повторная регистрация того же пользователя возвращает 403 и сообщение 'User already exists'")
    void createDuplicateUser() {
        Response first = userClient.register(user);
        assertEquals(200, first.statusCode());
        accessToken = first.as(LoginResponse.class).accessToken;

        Response second = userClient.register(user);

        assertEquals(403, second.statusCode());
        assertFalse(second.as(RegisterResponse.class).success);
        assertEquals("User already exists", second.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Создать пользователя без email")
    @Description("POST /api/auth/register без email возвращает 403")
    void createUserWithoutEmail() {
        User invalid = UserGenerator.userWithNullEmail();
        Response response = userClient.register(invalid);

        assertEquals(403, response.statusCode());
        assertEquals("Email, password and name are required fields",
                response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Создать пользователя без пароля")
    @Description("POST /api/auth/register без password возвращает 403")
    void createUserWithoutPassword() {
        User invalid = UserGenerator.userWithNullPassword();
        Response response = userClient.register(invalid);

        assertEquals(403, response.statusCode());
        assertEquals("Email, password and name are required fields",
                response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Создать пользователя без имени")
    @Description("POST /api/auth/register без name возвращает 403")
    void createUserWithoutName() {
        User invalid = UserGenerator.userWithNullName();
        Response response = userClient.register(invalid);

        assertEquals(403, response.statusCode());
        assertEquals("Email, password and name are required fields",
                response.jsonPath().getString("message"));
    }
}