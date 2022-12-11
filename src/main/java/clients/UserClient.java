package clients;

import models.UserCredentials;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.User;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private static final String PATH_REGISTER_USER = "api/auth/register";
    private static final String PATH_LOGIN_USER = "api/auth/login";
    private static final String PATH_UPDATE_USER = "api/auth/user";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(user)
                .when()
                .post(PATH_REGISTER_USER)
                .then()
                .log().all();
    }

    @Step("Логин пользователя в системе")
    public ValidatableResponse loginUser(UserCredentials userCredentials) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(userCredentials)
                .when()
                .post(PATH_LOGIN_USER)
                .then()
                .log().all();
    }

    @Step("Изменение данных пользователя c авторизацией")
    public ValidatableResponse updateUserWithAuthorization(String accessToken, User newUser) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .log().all()
                .body(newUser)
                .when()
                .patch(PATH_UPDATE_USER)
                .then()
                .log().all();
    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse updateUserWithoutAuthorization(User newUser) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(newUser)
                .when()
                .patch(PATH_UPDATE_USER)
                .then()
                .log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .log().all()
                .when()
                .delete(PATH_UPDATE_USER)
                .then()
                .log().all();
    }
}
