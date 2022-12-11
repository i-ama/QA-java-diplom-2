package clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Ingredient;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client{

    private static final String PATH_ORDER = "api/orders";

    @Step("Создание заказа с авторизованным пользователем")
    public ValidatableResponse createOrderWithAuthorization(String accessToken, Ingredient ingredient) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .log().all()
                .body(ingredient)
                .when()
                .post(PATH_ORDER)
                .then()
                .log().all();
    }

    @Step("Создание заказа с не авторизованным пользователем")
    public ValidatableResponse createOrderWithoutAuthorization(Ingredient ingredient) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(ingredient)
                .when()
                .post(PATH_ORDER)
                .then()
                .log().all();
    }

    @Step("Получение заказа с не авторизованным пользователем")
    public ValidatableResponse getUserOrdersWithoutAuthorization() {
        return given()
                .spec(getSpec())
                .log().all()
                .when()
                .get(PATH_ORDER)
                .then()
                .log().all();
    }

    @Step("Получение заказа с авторизованным пользователем")
    public ValidatableResponse getUserOrdersWithAuthorization(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .log().all()
                .when()
                .get(PATH_ORDER)
                .then()
                .log().all();
    }
}
