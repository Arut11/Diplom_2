
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserGenerator;

import static org.junit.Assert.*;


public class UserCreateTests {

    private UserClient userClient;
    private User user;
    private boolean success;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getUser();

    }

    @After
    public void cleanUp() {

    }

    @Test
    @DisplayName("Проверка создания уникального пользователя")
    public void userCreatedTest() {
        ValidatableResponse createResponse = userClient.createUser(user);
        boolean created = createResponse.extract().path("success");
        assertTrue("В теле ответа в поле success вернулось значение false", created);

    }

    @Test
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    public void userDoubleCreatedTest() {
        userClient.createUser(user);
        ValidatableResponse createSecondResponse = userClient.createUser(user);
        boolean created = createSecondResponse.extract().path("success");
        assertFalse("В теле ответа в поле success вернулось значение true", created);

    }

}
