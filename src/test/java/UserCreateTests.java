
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
    private String token;
    private ValidatableResponse createResponse;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getUser();

    }

    @After
    public void cleanUp() {
        token = createResponse.extract().path("accessToken");
        if(success) {
            userClient.deleteUser(token);
        }

    }

    @Test
    @DisplayName("Проверка создания уникального пользователя")
    public void userCreatedTest() {
        createResponse = userClient.createUser(user);
        success = createResponse.extract().path("success");
        assertTrue("В теле ответа в поле success вернулось значение false", success);

    }

    @Test
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    public void userDoubleCreatedTest() {
        userClient.createUser(user);
        createResponse = userClient.createUser(user);
        success = createResponse.extract().path("success");
        assertFalse("В теле ответа в поле success вернулось значение true", success);

    }

}
