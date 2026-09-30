package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.config.RestAssuredConfig;
import praktikum.model.User;

import static io.restassured.RestAssured.given;

public class UserClient {

    public static final String REGISTER = "/api/auth/register";
    public static final String LOGIN = "/api/auth/login";
    public static final String USER = "/api/auth/user";
    public static final String LOGOUT = "/api/auth/logout";

    @Step("Регистрация пользователя: {user.email}")
    public Response register(User user) {
        return given()
                .spec(RestAssuredConfig.baseSpec())
                .body(user)
                .when()
                .post(REGISTER);
    }

    @Step("Логин пользователя: {user.email}")
    public Response login(User user) {
        return given()
                .spec(RestAssuredConfig.baseSpec())
                .body(user)
                .when()
                .post(LOGIN);
    }

    @Step("Получение данных пользователя")
    public Response getUser(String accessToken) {
        return given()
                .spec(RestAssuredConfig.authorizedSpec(accessToken))
                .when()
                .get(USER);
    }

    @Step("Получение данных пользователя без авторизации")
    public Response getUserWithoutAuth() {
        return given()
                .spec(RestAssuredConfig.baseSpec())
                .when()
                .get(USER);
    }

    @Step("Обновление данных пользователя")
    public Response updateUser(String accessToken, User user) {
        return given()
                .spec(RestAssuredConfig.authorizedSpec(accessToken))
                .body(user)
                .when()
                .patch(USER);
    }

    @Step("Обновление данных пользователя без авторизации")
    public Response updateUserWithoutAuth(User user) {
        return given()
                .spec(RestAssuredConfig.baseSpec())
                .body(user)
                .when()
                .patch(USER);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .spec(RestAssuredConfig.authorizedSpec(accessToken))
                .when()
                .delete(USER);
    }

    @Step("Выход из системы")
    public Response logout(String refreshToken) {
        return given()
                .spec(RestAssuredConfig.baseSpec())
                .body("{\"token\":\"" + refreshToken + "\"}")
                .when()
                .post(LOGOUT);
    }
}