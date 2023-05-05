package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.client.RestClient;
import praktikum.pojo.Burger;
import praktikum.pojo.User;

import static io.restassured.RestAssured.given;

public class BurgerOrder extends RestClient {

    protected final String ROOT = "/orders";

    @Step("Создание Космобургера")
    public ValidatableResponse createOrder(Burger burger, User user) {
        if (user.getAccessToken() != null) {
            return given()
                    .spec(getBaseSpec())
                    .header("Authorization", user.getAccessToken())
                    .when()
                    .body(burger)
                    .post(ROOT)
                    .then();
        } else {
            return given()
                    .spec(getBaseSpec())
                    .when()
                    .body(burger)
                    .post(ROOT)
                    .then();
        }

    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrders(User user) {
        if (user.getAccessToken() != null) {
            return given()
                    .spec(getBaseSpec())
                    .header("Authorization", user.getAccessToken())
                    .when()
                    .get(ROOT)
                    .then();
        } else {
            return given()
                    .spec(getBaseSpec())
                    .when()
                    .get(ROOT)
                    .then();
        }
    }
}
