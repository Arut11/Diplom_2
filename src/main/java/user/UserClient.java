package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static restClient.RestClient.getBaseSpec;

public class UserClient {

    private static final String USER_PATH = "api/auth/user";
    public static final String USER_LOGIN = "/api/auth/login";
    public static final String USER_REGISTER = "api/auth/register";

    @Step("Создание курьера")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_REGISTER)
                .then();

    }

    @Step("Логин курьера в системе")
    public ValidatableResponse login(UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_LOGIN)
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse editUser(String accessToken, User user) {
        return given()
                .spec(getBaseSpec())
                .header("authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_PATH)
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse editUserWithoutToken(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(USER_PATH)
                .then();
    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken, User user) {
        given()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .delete(USER_PATH)
                .then();
    }

}
