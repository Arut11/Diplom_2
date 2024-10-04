
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;
import user.UserGenerator;
import static org.junit.jupiter.api.Assertions.*;

public class EditUserTest {

    private UserClient userClient;
    private User user;
    private ValidatableResponse createResponse;
    private boolean success;
    private String token;


    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getUser();
        createResponse = userClient.createUser(user);
        token = createResponse.extract().path("accessToken");

    }

    @AfterEach
    public void cleanUp() {
        if(success) {
            userClient.deleteUser(token);
        }

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
        assertTrue(success,
                "В теле ответа в поле success вернулось значение false");
        assertEquals(newUser.getName(), name,
                "Поле name не изменилось");
        assertEquals(newUser.getEmail(), email,
                "Поле email не изменилось");

    }

    @Test
    @DisplayName("Проверка изменения данных пользователя без авторизации")
    public void editUserWithoutAuthorization() {
        ValidatableResponse responseEdit = userClient.editUserWithoutToken(user);
        int statusCode = responseEdit.extract().statusCode();
        success = responseEdit.extract().path("success");
        String message = responseEdit.extract().path("message");
        assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode,
                "Статус код при изменение данных пользователя (без авторизации) не 401");
        assertFalse(success,
                "В теле ответа в поле success вернулось значение true");
        assertEquals("You should be authorised", message,
                "Некорректное сообщение об ошибке");

    }

}
