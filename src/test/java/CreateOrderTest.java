import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.BurgerOrder;
import praktikum.UserClient;
import praktikum.client.DataGenerator;
import praktikum.pojo.Burger;
import praktikum.pojo.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {
    User user;
    UserClient userClient;
    Burger burger;

    BurgerOrder burgerOrder;

    @Before
    public void setUp() {
        user = DataGenerator.getRandomUser();
        userClient = new UserClient();
        burger = new Burger();
        burgerOrder = new BurgerOrder();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        userClient.delete(user);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderForAuthorizedUser() {
        burger.setIngredients(DataGenerator.getIngredients());
        ValidatableResponse validatableResponse = burgerOrder.createOrder(burger, user);


        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание без авторизации")
    public void createOrderForUnauthorizedUser() {
        user.setAccessToken(null);
        burger.setIngredients(DataGenerator.getIngredients());
        ValidatableResponse validatableResponse = burgerOrder.createOrder(burger, user);


        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    public void createOrderWithIngredients() {
        burger.setIngredients(DataGenerator.getIngredients());

        ValidatableResponse validatableResponse = burgerOrder.createOrder(burger, user);
        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание без ингредиентов")
    public void createEmptyOrder() {
        ValidatableResponse validatableResponse = burgerOrder.createOrder(burger, user);
        validatableResponse.assertThat().statusCode(SC_BAD_REQUEST);
        validatableResponse.assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidHashes() {
        String[] incorrectIngredients = {"test", "test"};
        burger.setIngredients(incorrectIngredients);
        ValidatableResponse validatableResponse = burgerOrder.createOrder(burger, user);
        validatableResponse.assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
