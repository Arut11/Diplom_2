
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;
import user.UserGenerator;
import static org.junit.Assert.*;

public class EditUserTest {

    private UserClient userClient;
    private User user;
    private ValidatableResponse createResponse;
    private boolean success;
    private String token;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getUser();
        createResponse = userClient.createUser(user);
        token = createResponse.extract().path("accessToken");

    }

    @After
    public void tearDown() {

    }

    @Test
    @DisplayName("Проверка изменения данных пользователя с авторизацией")
    public void editUser() {
        userClient.login(UserCredentials.from(user));
        User newUser = UserGenerator.getUser();
        ValidatableResponse responseEdit = userClient.editUser(token, newUser);
        success = responseEdit.extract().path("success");
        String name = responseEdit.extract().path("user.name");
        String email = responseEdit.extract().path("user.email");
        assertTrue("В теле ответа в поле success вернулось значение false", success);
        assertEquals("Поле name не изменилось",
                newUser.getName(), name);
        assertEquals("Поле email не изменилось",
                newUser.getEmail(), email);

    }

    @Test
    @DisplayName("Проверка изменения данных пользователя без авторизации")
    public void editUserWithoutAuthorization() {
        ValidatableResponse responseEdit = userClient.editUserWithoutToken(user);
        int statusCode = responseEdit.extract().statusCode();
        success = responseEdit.extract().path("success");
        String message = responseEdit.extract().path("message");
        assertEquals("Статус код при изменение данных пользователя (без авторизации) не 401",
                HttpStatus.SC_UNAUTHORIZED, statusCode);
        assertFalse("В теле ответа в поле success вернулось значение true", success);
        assertEquals("Некорректное сообщение об ошибке",
                "You should be authorised", message);

    }

}
