import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    private static final String PATH_REGISTER_USER = "api/auth/register";+
    private static final String PATH_LOGIN_USER = "api/auth/login";+
    private static final String PATH_LOGOUT_USER = "api/auth/logout";+
    private static final String PATH_UPDATE_USER = "api/auth/user"; +
    private static final String PATH_REFRESH_TOKEN = "api/auth/token";+

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
    public ValidatableResponse loginUser(UserCredentials credentials) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(credentials)
                .when()
                .post(PATH_LOGIN_USER)
                .then()
                .log().all();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse updateUser(Token token) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(token)
                .when()
                .patch(PATH_UPDATE_USER)
                .then()
                .log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse updateUser(Token token) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(token)
                .when()
                .delete(PATH_UPDATE_USER)
                .then()
                .log().all();
    }

    @Step("Выход пользователя из системы")
    public ValidatableResponse updateUser(Token token) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(token)
                .when()
                .post(PATH_LOGOUT_USER)
                .then()
                .log().all();
    }

    @Step("Обновление токена")
    public ValidatableResponse updateUser(Token token) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(token)
                .when()
                .post(PATH_REFRESH_TOKEN)
                .then()
                .log().all();
    }
}
