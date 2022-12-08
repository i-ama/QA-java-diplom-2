import clients.UserClient;
import generators.UserGeneratorData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class UserCanNotBeCreateWithExistDataTest {
    private UserClient userClient;
    private User firstUser;
    private User secondUser;
    private String accessFirstToken;
    private String accessSecondToken;
    private String actualSecondResponseMessage;
    private String expectedResponseMessage = "User already exists";
    private int actualSecondStatusCode;

    @Before
    public void setUp() {
        userClient = new UserClient();
        firstUser = UserGeneratorData.getDefault();
        secondUser = UserGeneratorData.getDefaultWithTheSameEmail();
    }

    @After
    public void cleanUp() {
        userClient.deleteUser(accessFirstToken);
    }

    @Test
    @DisplayName("Проверка, что нельзя создать двух пользователей с одинаковым email")
    public void userCanNotBeCreatedWithExistEmail() {
        ValidatableResponse responseCreateFirstUser = userClient.createUser(firstUser);
        ValidatableResponse responseCreateSecondUser = userClient.createUser(secondUser);
        actualSecondStatusCode = responseCreateSecondUser.extract().statusCode();
        actualSecondResponseMessage = responseCreateSecondUser.extract().path("message");
        assertEquals("Incorrect success message", SC_FORBIDDEN, actualSecondStatusCode);
        assertEquals("Incorrect message",expectedResponseMessage, actualSecondResponseMessage);

        if (actualSecondStatusCode == SC_OK) {
            accessSecondToken = responseCreateSecondUser.extract().path("accessToken");
            userClient.deleteUser(accessSecondToken);
        }

        accessFirstToken = responseCreateFirstUser.extract().path("accessToken");
        userClient.deleteUser(accessFirstToken);
    }

}
