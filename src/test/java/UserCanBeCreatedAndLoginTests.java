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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserCanBeCreatedAndLoginTests {

    private UserClient userClient;
    private User user;
    private String accessToken;
    private boolean actualResponseSuccess;
    private int actualStatusCode;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGeneratorData.getDefault();
    }

    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Успешное создание нового пользователя")
    public void UserCanBeCreated() {
        ValidatableResponse responseCreateUser = userClient.createUser(user);
        actualStatusCode = responseCreateUser.extract().statusCode();
        actualResponseSuccess = responseCreateUser.extract().path("success");
        accessToken = responseCreateUser.extract().path("accessToken");
        assertEquals("Incorrect success message", SC_OK, actualStatusCode);
        assertTrue("Incorrect status code", actualResponseSuccess);
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Успешная авторизация пользователя")
    public void UserCanBeLogin() {
        userClient.createUser(user);
        ValidatableResponse responseCreateUser = userClient.loginUser(UserCredentials.from(user));
        actualStatusCode = responseCreateUser.extract().statusCode();
        actualResponseSuccess = responseCreateUser.extract().path("success");
        accessToken = responseCreateUser.extract().path("accessToken");
        assertEquals("Incorrect success message", SC_OK, actualStatusCode);
        assertTrue("Incorrect status code", actualResponseSuccess);
        userClient.deleteUser(accessToken);
    }
}
