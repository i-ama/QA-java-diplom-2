import clients.UserClient;
import generators.UserGeneratorData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import models.UserCredentials;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class UserCanNotBeLoginWithExistDataTest {

    private UserClient userClient;
    private User user;
    private User changedUser;
    private String accessToken;
    private int actualStatusCode;
    private int expectedStatusCode;
    private boolean actualResponseSuccess;
    private String actualResponseMessage;
    private String expectedResponseMessage;

    public UserCanNotBeLoginWithExistDataTest(User user, User changedUser, int expectedStatusCode, String expectedResponseMessage) {
        this.user = user;
        this.changedUser = changedUser;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedResponseMessage = expectedResponseMessage;
    }

    @Parameterized.Parameters
    public static Object[][]  getTestData() {
        return new Object[][] {
                {UserGeneratorData.getDefault(), UserGeneratorData.getDefaultChangedEmail(), SC_UNAUTHORIZED, "email or password are incorrect"},
                {UserGeneratorData.getDefault(), UserGeneratorData.getDefaultChangedPassword(), SC_UNAUTHORIZED, "email or password are incorrect"},
                {UserGeneratorData.getDefault(), UserGeneratorData.getDefaultChangedPasswordAndEmail(), SC_UNAUTHORIZED, "email or password are incorrect"}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Параметризированный тест для попытки авторизации с невалидными password или email")
    public void UserCanNotBeLoginWithExistData() {
        ValidatableResponse responseCreateUser = userClient.createUser(user);
        accessToken = responseCreateUser.extract().path("accessToken");

        ValidatableResponse responseLoginUser = userClient.loginUser(UserCredentials.from(changedUser));
        actualStatusCode = responseLoginUser.extract().statusCode();
        actualResponseSuccess = responseLoginUser.extract().path("success");
        actualResponseMessage = responseLoginUser.extract().path("message");

        assertEquals("Incorrect success status", expectedStatusCode, actualStatusCode);
        assertFalse("Incorrect status code", actualResponseSuccess);
        assertEquals("Incorrect response message", expectedResponseMessage, actualResponseMessage);

        userClient.deleteUser(accessToken);
    }
}
