package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.config.RestAssuredConfig;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderClient {

    public static final String INGREDIENTS = "/api/ingredients";
    public static final String ORDERS = "/api/orders";

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(RestAssuredConfig.baseSpec())
                .when()
                .get(INGREDIENTS);
    }

    @Step("Создание заказа с ингредиентами: {ingredientIds}")
    public Response createOrder(String accessToken, List<String> ingredientIds) {
        return given()
                .spec(RestAssuredConfig.authorizedSpec(accessToken))
                .body(Map.of("ingredients", ingredientIds))
                .when()
                .post(ORDERS);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(List<String> ingredientIds) {
        return given()
                .spec(RestAssuredConfig.baseSpec())
                .body(Map.of("ingredients", ingredientIds))
                .when()
                .post(ORDERS);
    }

    @Step("Создание заказа без ингредиентов")
    public Response createOrderWithoutIngredients(String accessToken) {
        return given()
                .spec(RestAssuredConfig.authorizedSpec(accessToken))
                .body("{\"ingredients\":[]}")
                .when()
                .post(ORDERS);
    }

    @Step("Получение заказов авторизованного пользователя")
    public Response getUserOrders(String accessToken) {
        return given()
                .spec(RestAssuredConfig.authorizedSpec(accessToken))
                .when()
                .get(ORDERS);
    }

    @Step("Получение заказов без авторизации")
    public Response getUserOrdersWithoutAuth() {
        return given()
                .spec(RestAssuredConfig.baseSpec())
                .when()
                .get(ORDERS);
    }
}