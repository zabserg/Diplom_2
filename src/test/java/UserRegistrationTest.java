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

public class UserRegistrationTest {

    UserClient userClient;
    User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = DataGenerator.getRandomUser();
    }

    @After
    public void tearDown() {
        userClient.delete(user);
    }


    @Test
    @DisplayName("Регистрация пользователя с корректными данными")
    public void registrationWithValidCredentials() {
        ValidatableResponse createResponse = userClient.create(user);
        createResponse.assertThat().statusCode(SC_OK);
        createResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Регистрация пользователя, который уже зарегистрирован")
    public void registrationSecondUserWithEqualCredentials() {
        userClient.create(user);
        ValidatableResponse createResponse = userClient.create(user);
        createResponse.assertThat().statusCode(SC_FORBIDDEN);
        createResponse.assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Регистрация пользователя без пароля")
    public void registrationWithoutPassword() {
        user.setPassword(null);
        ValidatableResponse createResponse = userClient.create(user);
        createResponse.assertThat().statusCode(SC_FORBIDDEN);
        createResponse.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация пользователя без email")
    public void registrationWithoutEmail() {
        user.setEmail(null);
        ValidatableResponse createResponse = userClient.create(user);
        createResponse.assertThat().statusCode(SC_FORBIDDEN);
        createResponse.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация пользователя без имени")
    public void registrationWithoutName() {
        user.setName(null);
        ValidatableResponse createResponse = userClient.create(user);
        createResponse.assertThat().statusCode(SC_FORBIDDEN);
        createResponse.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}
