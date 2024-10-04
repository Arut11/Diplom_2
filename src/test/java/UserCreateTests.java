
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.User;
import user.UserClient;
import user.UserGenerator;
import static org.junit.jupiter.api.Assertions.*;

public class UserCreateTests {

    private UserClient userClient;
    private User user;
    private boolean success;
    private String token;
    private ValidatableResponse createResponse;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getUser();

    }

    @AfterEach
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
        assertTrue(success,
                "В теле ответа в поле success вернулось значение false");

    }

    @Test
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    public void userDoubleCreatedTest() {
        userClient.createUser(user);
        createResponse = userClient.createUser(user);
        success = createResponse.extract().path("success");
        assertFalse(success,
                "В теле ответа в поле success вернулось значение true");

    }

}
