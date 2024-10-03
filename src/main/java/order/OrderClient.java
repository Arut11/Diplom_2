package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import static restClient.RestClient.getBaseSpec;

public class OrderClient {

    public static final String ORDER_PATH = "/api/orders";
    public static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrders(String accessToken, Order order) {
        return given()
                .header("authorization", accessToken)
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получение списка заказов авторизованным пользователем")
    public ValidatableResponse getOrders(String accessToken){
        return given()
                .header("authorization", accessToken)
                .spec(getBaseSpec())
                .get(ORDER_PATH)
                .then();
    }

    @Step("Получение списка заказов не авторизованным пользователем")
    public ValidatableResponse getOrdersWithoutAuth(){
        return given()
                .spec(getBaseSpec())
                .get(ORDER_PATH)
                .then();

    }

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getAllIngredients() {
        return given()
                .spec(getBaseSpec())
                .get(INGREDIENTS_PATH)
                .then();

    }

}
