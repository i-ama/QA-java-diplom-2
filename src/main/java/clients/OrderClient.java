package clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client{

    private static final String PATH_ORDER = "api/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(String ingredients) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(ingredients)
                .when()
                .post(PATH_ORDER)
                .then()
                .log().all();
    }

    @Step("Создание заказа")
    public ValidatableResponse getUserOrders(String ingredients) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(ingredients)
                .when()
                .get(PATH_ORDER)
                .then()
                .log().all();
    }
}
