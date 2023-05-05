import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.UserClient;
import praktikum.client.DataGenerator;
import praktikum.pojo.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserEditProfileTest {

    UserClient userClient;
    User user, newUser;

    @Before
    public void setUp() {
        user = DataGenerator.getRandomUser();
        newUser = DataGenerator.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        userClient.delete(user);
    }

    @Test
    @DisplayName("Изменение почты пользователя с авторизацией")
    public void updateUserEmailForAuthorizedUser() {
        ValidatableResponse validatableResponse = userClient.edit(user, newUser);
        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("user.email", equalTo(newUser.getEmail()));
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    public void updateUserNameForAuthorizedUser() {
        ValidatableResponse validatableResponse = userClient.edit(user, newUser);
        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("user.name", equalTo(newUser.getName()));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void updateCredentialsForUnauthorizedUser() {
        user.setAccessToken(null);
        ValidatableResponse validatableResponse = userClient.edit(user, newUser);
        validatableResponse.assertThat().statusCode(SC_UNAUTHORIZED);
        validatableResponse.assertThat().body("message", equalTo("You should be authorised"));
    }
}
