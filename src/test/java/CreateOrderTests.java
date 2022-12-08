import clients.IngredientClient;
import clients.OrderClient;
import clients.UserClient;
import generators.UserGeneratorData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Ingredient;
import models.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreateOrderTests {
    private OrderClient orderClient;
    private IngredientClient ingredientClient;
    private List<String> listOfIngredientsId;

    private UserClient userClient;
    private User user;
    private String accessToken;

    private int expectedStatusCode;

    private int actualStatusCode;

    private int randomIngredientsSelector;
    private String wrongHashIngredient;

    public CreateOrderTests(User user, int randomIngredientsSelector, int expectedStatusCode, String wrongHashIngredient) {
        this.user = user;
        this.randomIngredientsSelector = randomIngredientsSelector;
        this.expectedStatusCode = expectedStatusCode;

        this.wrongHashIngredient = wrongHashIngredient;
    }

    @Parameterized.Parameters
    public static Object[][]  getTestData() {
        return new Object[][] {
                {UserGeneratorData.getDefault(), (int)(Math.random()*13), SC_OK, null},
                {UserGeneratorData.getDefaultSecond(), -1, SC_BAD_REQUEST, null},
                {UserGeneratorData.getDefaultThird(), (int)(Math.random()*13), SC_OK, null},
                {UserGeneratorData.getDefaultForth(), (int)(Math.random()*13), SC_INTERNAL_SERVER_ERROR, RandomStringUtils.randomAlphabetic(24)}
        };
    }

    //Выбираем рандомный список ингредиентов для создания заказа
    @Before
    public void setUp() {
        ingredientClient = new IngredientClient();
        ValidatableResponse ingredients = ingredientClient.getIngredients();
        listOfIngredientsId = ingredients.extract().path("data._id");
        for (int i = 14; i > randomIngredientsSelector; i--) {
            listOfIngredientsId.remove(i);
        }
        listOfIngredientsId.add(wrongHashIngredient);
        System.out.println(listOfIngredientsId);
    }

    @Test
    @DisplayName("Параметризованный тест для успешного создания заказа без авторизацией")
    public void CreateOrderWithoutAuthorization() {
        orderClient = new OrderClient();
        Ingredient ingredient = new Ingredient(listOfIngredientsId);
        ValidatableResponse responseCreateOrder = orderClient.createOrderWithoutAuthorization(ingredient);
        actualStatusCode = responseCreateOrder.extract().statusCode();
        assertEquals("Incorrect success message", expectedStatusCode, actualStatusCode);
    }

    @Test
    @DisplayName("Параметризованный тест для успешного создания заказа с авторизацией")
    public void CreateOrderWithAuthorization() {
        userClient = new UserClient();
        ValidatableResponse responseCreateUser = userClient.createUser(user);
        accessToken = responseCreateUser.extract().path("accessToken");

        orderClient = new OrderClient();
        Ingredient ingredient = new Ingredient(listOfIngredientsId);
        ValidatableResponse responseCreateOrder = orderClient.createOrderWithAuthorization(accessToken, ingredient);
        actualStatusCode = responseCreateOrder.extract().statusCode();
        assertEquals("Incorrect success message", expectedStatusCode, actualStatusCode);
        userClient.deleteUser(accessToken);
    }
}
