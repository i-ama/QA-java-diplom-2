import clients.UserClient;
import generators.UserGeneratorData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class NotEnoughDataToCreateUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;
    private int actualStatusCode;
    private int expectedStatusCode;
    private boolean actualResponseSuccess;
    private String actualResponseMessage;
    private String expectedResponseMessage;

    public NotEnoughDataToCreateUserTest(User user, int expectedStatusCode, String expectedResponseMessage) {
        this.user = user;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedResponseMessage = expectedResponseMessage;
    }

    @Parameterized.Parameters
    public static Object[][]  getTestData() {
        return new Object[][] {
                {UserGeneratorData.getWithoutPassword(), SC_FORBIDDEN, "Email, password and name are required fields"},
                {UserGeneratorData.getWithoutLogin(), SC_FORBIDDEN, "Email, password and name are required fields"},
                {UserGeneratorData.getWithoutName(), SC_FORBIDDEN, "Email, password and name are required fields"}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Параметризированный тест по созданию пользователя с неполными данными")
    public void NotEnoughDataToCreateUser() {
        ValidatableResponse responseCreateUser = userClient.createUser(user);
        actualStatusCode = responseCreateUser.extract().statusCode();
        actualResponseSuccess = responseCreateUser.extract().path("success");
        actualResponseMessage = responseCreateUser.extract().path("message");

        if (actualStatusCode == SC_OK) {
            accessToken = responseCreateUser.extract().path("accessToken");
            userClient.deleteUser(accessToken);
        }

        assertEquals("Incorrect success status", expectedStatusCode, actualStatusCode);
        assertFalse("Incorrect status code", actualResponseSuccess);
        assertEquals("Incorrect response message", expectedResponseMessage, actualResponseMessage);
    }
}
