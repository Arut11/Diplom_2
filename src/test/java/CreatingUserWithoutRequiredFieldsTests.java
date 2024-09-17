import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.UserClient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class CreatingUserWithoutRequiredFieldsTests {

    static Faker faker = new Faker();
    private UserClient userClient;
    private User user;
    private int createStatusCode;
    private ValidatableResponse createResponse;
    private String token;
    private boolean success;

    @Before
    public void setUp() {
        userClient = new UserClient();

    }

    @After
    public void cleanUp() {
        token = createResponse.extract().path("accessToken");
        if(success) {
            userClient.deleteUser(token);
        }

    }

    public CreatingUserWithoutRequiredFieldsTests(String email, String password, String name) {
        user = new User()
                .setEmail(email)
                .setPassword(password)
                .setName(name);

    }


    @Parameterized.Parameters
    public static Object[][] getUserTest() {
        return new Object[][] {
                {"", faker.internet().password(), faker.name().firstName()},
                {faker.internet().emailAddress(), "", faker.name().firstName()},
                {faker.internet().emailAddress(), faker.internet().password(), ""},
                {"", "", ""}
        };

    }

    @Test
    @DisplayName("Проверка создания пользователя без одного из обязательных полей")
    public void CreatingUserWithoutRequiredFieldsTest() {
        createResponse = userClient.createUser(user);
        token = createResponse.extract().path("accessToken");
        createStatusCode = createResponse.extract().statusCode();
        success = createResponse.extract().path("success");
        assertEquals("Статус код вернулся не 403 при создании пользователя без одного из обязательных полей",
                HttpStatus.SC_FORBIDDEN, createStatusCode);
        assertFalse("В теле ответа в поле success вернулось значение true", success);

    }

}
