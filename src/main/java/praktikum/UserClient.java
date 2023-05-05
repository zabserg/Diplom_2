package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.client.RestClient;
import praktikum.pojo.User;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {

    protected final String ROOT = "/auth";


    @Step("Авторизация пользователя")
    public ValidatableResponse login(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(ROOT + "/login")
                .then();

    }

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        ValidatableResponse validatableResponse = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(ROOT + "/register")
                .then();
        user.setAccessToken(validatableResponse.extract().response().jsonPath().getString("accessToken"));
        return validatableResponse;
    }

    @Step("Удаление пользователя")
    public void delete(User user) {
        if (user.getAccessToken() != null) {
            given()
                    .spec(getBaseSpec())
                    .header("Authorization", user.getAccessToken())
                    .when()
                    .delete(ROOT + "/user")
                    .then();
        }
    }

    @Step("Редактирование пользователя")
    public ValidatableResponse edit(User oldUser, User newUserCredentials) {
        if (oldUser.getAccessToken() != null) {
            return given()
                    .spec(getBaseSpec())
                    .header("Authorization", oldUser.getAccessToken())
                    .when()
                    .body(newUserCredentials)
                    .patch(ROOT + "/user")
                    .then();
        } else {
            return given()
                    .spec(getBaseSpec())
                    .when()
                    .body(newUserCredentials)
                    .patch(ROOT + "/user")
                    .then();
        }
    }
}
