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
import praktikum.model.UserResponse;
import praktikum.util.UserGenerator;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Stellar Burgers API")
@Feature("Изменение данных пользователя")
@ExtendWith(AllureJunit5.class)
@DisplayName("Тесты изменения данных пользователя")
class UserUpdateTest {

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
    @DisplayName("Изменить email с авторизацией")
    void updateEmailWithAuth() {
        User update = new User();
        update.email = "new_" + System.currentTimeMillis() + "@yandex.ru";

        Response response = userClient.updateUser(accessToken, update);

        assertEquals(200, response.statusCode());
        UserResponse body = response.as(UserResponse.class);
        assertTrue(body.success);
        assertEquals(update.email, body.user.email);
    }

    @Test
    @DisplayName("Изменить name с авторизацией")
    void updateNameWithAuth() {
        User update = new User();
        update.name = "NewName_" + System.currentTimeMillis();

        Response response = userClient.updateUser(accessToken, update);

        assertEquals(200, response.statusCode());
        UserResponse body = response.as(UserResponse.class);
        assertTrue(body.success);
        assertEquals(update.name, body.user.name);
    }

    @Test
    @DisplayName("Изменить password с авторизацией")
    void updatePasswordWithAuth() {
        User update = new User();
        update.password = "newPassword_" + System.currentTimeMillis();

        Response response = userClient.updateUser(accessToken, update);

        assertEquals(200, response.statusCode());
        assertTrue(response.as(UserResponse.class).success);
    }

    @Test
    @DisplayName("Изменить данные без авторизации — ошибка 401")
    void updateWithoutAuth() {
        User update = new User();
        update.name = "ShouldNotUpdate";

        Response response = userClient.updateUserWithoutAuth(update);

        assertEquals(401, response.statusCode());
        assertEquals("You should be authorised",
                response.jsonPath().getString("message"));
    }
}