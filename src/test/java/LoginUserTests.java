
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
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

public class LoginUserTests {

    private UserClient userClient;
    private User user;
    private int createStatusCode;
    private ValidatableResponse createResponse;
    private boolean success;
    private String token;


    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getUser();
        createResponse = userClient.createUser(user);
        createStatusCode = createResponse.extract().statusCode();
        token = createResponse.extract().path("accessToken");

    }

    @AfterEach
    public void cleanUp() {
        if(success) {
            userClient.deleteUser(token);
        }

    }

    @Test
    @DisplayName("Проверка авторизации под существующим пользователем")
    public void LoginUserTest() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_OK, loginStatusCode,
                "Статус код при авторизации пользователя не 200");
        success = loginResponse.extract().path("success");
        assertTrue(success,
                "В теле ответа в поле success вернулось значение false");

    }

    @Test
    @DisplayName("Проверка авторизации пользователя в системе без пароля")
    public void CourierLoginWithoutPasswordTest() {
        UserCredentials userCredentials = new UserCredentials(user.getEmail(), null);
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_UNAUTHORIZED, loginStatusCode,
                "Статус код при авторизации пользователя без пароля не 401");
        success = loginResponse.extract().path("success");
        assertFalse(success,
                "В теле ответа в поле success вернулось значение true");

    }

    @Test
    @DisplayName("Проверка авторизации пользователя в системе без логина")
    public void CourierLoginWithoutLoginTest() {
        UserCredentials userCredentials = new UserCredentials(null, user.getPassword());
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_UNAUTHORIZED, loginStatusCode,
                "Статус код при авторизации пользователя без пароля не 401");
        success = loginResponse.extract().path("success");
        assertFalse(success,
                "В теле ответа в поле success вернулось значение true");

    }

    @Test
    @DisplayName("Проверка авторизации пользователя в системе без логина и пароля")
    public void CourierLoginWithoutLoginAndPasswordTest() {
        UserCredentials userCredentials = new UserCredentials(null, null);
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_UNAUTHORIZED, loginStatusCode,
                "Статус код при авторизации пользователя без пароля не 401");
        success = loginResponse.extract().path("success");
        assertFalse(success,
                "В теле ответа в поле success вернулось значение true");

    }

    @Test
    @DisplayName("Проверка авторизации с несуществующим логином пользователя в системе")
    public void LoginNonExistentLoginCourierTest() {
        UserCredentials userCredentials = new UserCredentials(RandomStringUtils.randomAlphanumeric(7), user.getPassword());
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_UNAUTHORIZED, loginStatusCode,
                "Статус код при авторизации пользователя без пароля не 401");
        success = loginResponse.extract().path("success");
        assertFalse(success,
                "В теле ответа в поле success вернулось значение true");

    }

    @Test
    @DisplayName("Проверка авторизации с несуществующим паролем пользователя в системе")
    public void LoginNonExistentPasswordCourierTest() {
        UserCredentials userCredentials = new UserCredentials(user.getEmail(), RandomStringUtils.randomAlphanumeric(7));
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_UNAUTHORIZED, loginStatusCode,
                "Статус код при авторизации пользователя без пароля не 401");
        success = loginResponse.extract().path("success");
        assertFalse(success,
                "В теле ответа в поле success вернулось значение true");

    }

    @Test
    @DisplayName("Проверка авторизации с несуществующим паролем и логином пользователя в системе")
    public void LoginNonExistentCourierTest() {
        UserCredentials userCredentials = new UserCredentials(RandomStringUtils.randomAlphanumeric(7), RandomStringUtils.randomAlphanumeric(7));
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_UNAUTHORIZED, loginStatusCode,
                "Статус код при авторизации пользователя без пароля не 401");
        success = loginResponse.extract().path("success");
        assertFalse(success,
                "В теле ответа в поле success вернулось значение true");

    }

}
