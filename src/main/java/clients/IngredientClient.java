package clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class IngredientClient extends Client{

    private static final String PATH_GET_INGREDIENTS = "api/ingredients";

    @Step("Получение данных об ингредиентах")
    public ValidatableResponse createOrder() {
        return given()
                .spec(getSpec())
                .log().all()
                .when()
                .post(PATH_GET_INGREDIENTS)
                .then()
                .log().all();
    }
}
