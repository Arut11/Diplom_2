import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;
import user.UserGenerator;
import static org.junit.Assert.*;

public class LoginUserTests {

    private UserClient userClient;
    private User user;
    private int createStatusCode;
    private ValidatableResponse createResponse;
    private int userId;
    private boolean success;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getUser();
        createResponse = userClient.createUser(user);
        createStatusCode = createResponse.extract().statusCode();

    }

    @After
    public void cleanUp() {

    }

    @Test
    @DisplayName("Проверка авторизации под существующим пользователем")
    public void LoginUserTest() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Статус код при авторизации пользователя не 200",
                HttpStatus.SC_OK, loginStatusCode);
        success = loginResponse.extract().path("success");
        assertTrue("В теле ответа в поле success вернулось значение false", success);

    }

    @Test
    @DisplayName("Проверка авторизации пользователя в системе без пароля")
    public void CourierLoginWithoutPasswordTest() {
        UserCredentials userCredentials = new UserCredentials(user.getEmail(), null);
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Статус код при авторизации пользователя без пароля не 401",
                HttpStatus.SC_UNAUTHORIZED, loginStatusCode);
        success = loginResponse.extract().path("success");
        assertFalse("В теле ответа в поле success вернулось значение true", success);

    }

    @Test
    @DisplayName("Проверка авторизации пользователя в системе без логина")
    public void CourierLoginWithoutLoginTest() {
        UserCredentials userCredentials = new UserCredentials(null, user.getPassword());
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Статус код при авторизации пользователя без пароля не 401",
                HttpStatus.SC_UNAUTHORIZED, loginStatusCode);
        success = loginResponse.extract().path("success");
        assertFalse("В теле ответа в поле success вернулось значение true", success);

    }

    @Test
    @DisplayName("Проверка авторизации пользователя в системе без логина и пароля")
    public void CourierLoginWithoutLoginAndPasswordTest() {
        UserCredentials userCredentials = new UserCredentials(null, null);
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Статус код при авторизации пользователя без пароля не 401",
                HttpStatus.SC_UNAUTHORIZED, loginStatusCode);
        success = loginResponse.extract().path("success");
        assertFalse("В теле ответа в поле success вернулось значение true", success);

    }

    @Test
    @DisplayName("Проверка авторизации с несуществующим логином пользователя в системе")
    public void LoginNonExistentLoginCourierTest() {
        UserCredentials userCredentials = new UserCredentials(RandomStringUtils.randomAlphanumeric(7), user.getPassword());
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Статус код при авторизации пользователя без пароля не 401",
                HttpStatus.SC_UNAUTHORIZED, loginStatusCode);
        success = loginResponse.extract().path("success");
        assertFalse("В теле ответа в поле success вернулось значение true", success);

    }

    @Test
    @DisplayName("Проверка авторизации с несуществующим паролем пользователя в системе")
    public void LoginNonExistentPasswordCourierTest() {
        UserCredentials userCredentials = new UserCredentials(user.getEmail(), RandomStringUtils.randomAlphanumeric(7));
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Статус код при авторизации пользователя без пароля не 401",
                HttpStatus.SC_UNAUTHORIZED, loginStatusCode);
        success = loginResponse.extract().path("success");
        assertFalse("В теле ответа в поле success вернулось значение true", success);

    }

    @Test
    @DisplayName("Проверка авторизации с несуществующим паролем и логином пользователя в системе")
    public void LoginNonExistentCourierTest() {
        UserCredentials userCredentials = new UserCredentials(RandomStringUtils.randomAlphanumeric(7), RandomStringUtils.randomAlphanumeric(7));
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Статус код при авторизации пользователя без пароля не 401",
                HttpStatus.SC_UNAUTHORIZED, loginStatusCode);
        success = loginResponse.extract().path("success");
        assertFalse("В теле ответа в поле success вернулось значение true", success);

    }

}
