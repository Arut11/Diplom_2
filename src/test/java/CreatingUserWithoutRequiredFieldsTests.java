import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import user.User;
import user.UserClient;
import static org.junit.jupiter.api.Assertions.*;

public class CreatingUserWithoutRequiredFieldsTests {

    static Faker faker = new Faker();
    private UserClient userClient;
    private ValidatableResponse createResponse;
    private String token;
    private boolean success;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
    }

    @AfterEach
    public void cleanUp() {
        if(success) {
            userClient.deleteUser(token);
        }
    }

    public static Object[][] getUserTest() {
        return new Object[][] {
                {"", faker.internet().password(), faker.name().firstName()},
                {faker.internet().emailAddress(), "", faker.name().firstName()},
                {faker.internet().emailAddress(), faker.internet().password(), ""},
                {"", "", ""}
        };
    }

    @ParameterizedTest
    @MethodSource("getUserTest")
    @DisplayName("Проверка создания пользователя без одного из обязательных полей")
    public void creatingUserWithoutRequiredFieldsTest(String email, String password, String name) {
        User user = new User()
                .setEmail(email)
                .setPassword(password)
                .setName(name);

        createResponse = userClient.createUser(user);
        token = createResponse.extract().path("accessToken");
        int createStatusCode = createResponse.extract().statusCode();
        success = createResponse.extract().path("success");

        assertEquals(HttpStatus.SC_FORBIDDEN, createStatusCode,
                "Статус код вернулся не 403 при создании пользователя без одного из обязательных полей");
        assertFalse(success,
                "В теле ответа в поле success вернулось значение true");
    }

}

