import clients.IngredientClient;
import clients.OrderClient;
import clients.UserClient;
import generators.UserGeneratorData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Ingredient;
import models.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetOrderTest {
    private OrderClient orderClient;
    private IngredientClient ingredientClient;
    private UserClient userClient;
    private User user;
    private String accessToken;
    private int actualStatusCode;
    private List<String> listOfIngredientsId;
    private int randomIngredientsSelector = (int)(Math.random()*13);
    private boolean isResponseSuccessTrue;

    //Выбираем рандомный список ингредиентов для создания заказа
    @Before
    public void setUp() {
        ingredientClient = new IngredientClient();
        ValidatableResponse ingredients = ingredientClient.getIngredients();
        listOfIngredientsId = ingredients.extract().path("data._id");
        for (int i = 14; i > randomIngredientsSelector; i--) {
            listOfIngredientsId.remove(i);
        }
        System.out.println(listOfIngredientsId);
        user = UserGeneratorData.getDefault();
    }

    @Test
    @DisplayName("Тест для попытки получить заказы пользователя без авторизацией")
    public void GetOrderWithoutAuthorization() {
        orderClient = new OrderClient();
        Ingredient ingredient = new Ingredient(listOfIngredientsId);
        orderClient.createOrderWithoutAuthorization(ingredient);
        ValidatableResponse responseGetOrder = orderClient.getUserOrdersWithoutAuthorization();
        actualStatusCode = responseGetOrder.extract().statusCode();
        assertEquals("Incorrect success message", SC_UNAUTHORIZED, actualStatusCode);
    }

    @Test
    @DisplayName("Тест для получения заказов пользователя с авторизацией")
    public void GetOrderWithAuthorization() {
        userClient = new UserClient();
        ValidatableResponse responseCreateUser = userClient.createUser(user);
        accessToken = responseCreateUser.extract().path("accessToken");
        orderClient = new OrderClient();
        Ingredient ingredient = new Ingredient(listOfIngredientsId);
        orderClient.createOrderWithAuthorization(accessToken, ingredient);
        ValidatableResponse responseGetOrder = orderClient.getUserOrdersWithAuthorization(accessToken);
        actualStatusCode = responseGetOrder.extract().statusCode();
        isResponseSuccessTrue = responseGetOrder.extract().path("success");
        assertEquals("Incorrect success message", SC_OK, actualStatusCode);
        assertTrue("Response success false", isResponseSuccessTrue);
        userClient.deleteUser(accessToken);
    }
}
