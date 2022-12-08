import clients.UserClient;
import generators.UserGeneratorData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import models.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class UserUpdateDataTests {
    private UserClient userClient;
    private User userOld;
    private User userNew;
    private String accessToken;
    private boolean actualUpdateUserResponseSuccess;
    private int actualUpdateUserStatusCode;
    private String actualUpdateUserResponseMessage;
    private String expectedUpdateUserResponseMessage = "You should be authorised";
    private boolean actualLoginUserResponseSuccess;
    private int actualLoginStatusCode;
    private String actualName;

    @Before
    public void setUp() {
        userClient = new UserClient();
        userOld = UserGeneratorData.getDefault();
        userNew = UserGeneratorData.getDefaultSecond();
    }

    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Успешное изменение данных пользователя с авторизацией")
    public void UserCanNotToBeUpdateWithoutAuthorization() {
        ValidatableResponse responseCreateUser = userClient.createUser(userOld);
        accessToken = responseCreateUser.extract().path("accessToken");
        ValidatableResponse responseUpdateUser = userClient.updateUserWithoutAuthorization(userNew);
        actualUpdateUserStatusCode = responseUpdateUser.extract().statusCode();
        actualUpdateUserResponseSuccess = responseUpdateUser.extract().path("success");
        actualUpdateUserResponseMessage = responseUpdateUser.extract().path("message");
        assertEquals("Incorrect success message on update", SC_UNAUTHORIZED, actualUpdateUserStatusCode);
        assertFalse("Incorrect status code on update", actualUpdateUserResponseSuccess);
        assertEquals("Incorrect message on update", expectedUpdateUserResponseMessage, actualUpdateUserResponseMessage);
    }
    @Test
    @DisplayName("Успешное изменение данных пользователя с авторизацией: проверка изменений путем логина с помощью обновленных данных")
    public void UserCanBeUpdateWithAuthorization() {
        ValidatableResponse responseCreateUser = userClient.createUser(userOld);
        accessToken = responseCreateUser.extract().path("accessToken");
        ValidatableResponse responseUpdateUser = userClient.updateUserWithAuthorization(accessToken, userNew);
        actualUpdateUserStatusCode = responseUpdateUser.extract().statusCode();
        actualUpdateUserResponseSuccess = responseUpdateUser.extract().path("success");
        assertEquals("Incorrect success message on update", SC_OK, actualUpdateUserStatusCode);
        assertTrue("Incorrect status code on update", actualUpdateUserResponseSuccess);
        ValidatableResponse responseLoginUserWithUpdatedCredential = userClient.loginUser(UserCredentials.from(userNew));
        actualLoginStatusCode = responseLoginUserWithUpdatedCredential.extract().statusCode();
        actualUpdateUserResponseSuccess = responseLoginUserWithUpdatedCredential.extract().path("success");
        assertEquals("Incorrect success message on login", SC_OK, actualLoginStatusCode);
        assertTrue("Incorrect status code on update", actualUpdateUserResponseSuccess);
        actualLoginUserResponseSuccess = responseLoginUserWithUpdatedCredential.extract().path("success");
        actualName = responseLoginUserWithUpdatedCredential.extract().path("user.name");
        assertTrue("Incorrect status code", actualLoginUserResponseSuccess);
        assertEquals("Incorrect success message", userNew.getName(), actualName);
    }

}
