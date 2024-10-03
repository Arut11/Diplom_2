import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Order;
import order.OrderClient;
import order.OrderGenerator;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserGenerator;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class OrderCreateTests {

    private UserClient userClient;
    private OrderClient orderClient;
    private User user;
    private ValidatableResponse createResponse;
    private boolean success;
    private String name;
    private int orderNumber;
    private String message;
    private String token;
    private int statusCode;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = UserGenerator.getUser();
        createResponse = userClient.createUser(user);
        token = createResponse.extract().path("accessToken");

    }

    @After
    public void cleanUp() {
        if(success) {
            userClient.deleteUser(token);
        }

    }

    @Test
    @DisplayName("Проверка создание заказа с авторизацией")
    public void createOrderTest() {
        ValidatableResponse ingredientsResponse = orderClient.getAllIngredients();
        ArrayList<String> ingredientsId = ingredientsResponse.extract().path("data._id");
        createResponse = orderClient.createOrders(token, OrderGenerator.getOrder(ingredientsId));
        name = createResponse.extract().path("name");
        orderNumber = createResponse.extract().path("order.number");
        success = createResponse.extract().path("success");
        assertNotNull("Поле name пустое", name);
        assertNotNull("Поле order пустое", orderNumber);
        assertTrue("В теле ответа в поле success вернулось значение false", success);

    }

    @Test
    @DisplayName("Проверка создание заказа без авторизацией")
    public void createOrderWithoutAuthorizationTest() {
        ValidatableResponse ingredientsResponse = orderClient.getAllIngredients();
        ArrayList<String> ingredientsId = ingredientsResponse.extract().path("data._id");
        createResponse = orderClient.createOrderWithoutAuth(OrderGenerator.getOrder(ingredientsId));
        name = createResponse.extract().path("name");
        orderNumber = createResponse.extract().path("order.number");
        success = createResponse.extract().path("success");
        assertNotNull("Поле name пустое", name);
        assertNotNull("Поле order пустое", orderNumber);
        assertTrue("В теле ответа в поле success вернулось значение false", success);

    }

    @Test
    @DisplayName("Проверка создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        createResponse = orderClient.createOrders(token, new Order());
        message = createResponse.extract().path("message");
        success = createResponse.extract().path("success");
        assertEquals("Поле message пустое", "Ingredient ids must be provided", message);
        assertFalse("В теле ответа в поле success вернулось значение true", success);

    }

    @Test
    @DisplayName("Проверка создание заказа с неверным хешем ингредиентов")
    public void createOrderWithIncorrectIngredientsTest() {
        Order order = new Order();
        order.addIngredient("1");
        createResponse = orderClient.createOrders(token, order);
        statusCode = createResponse.extract().statusCode();
        assertEquals("Статус код вернулся не 500",
                HttpStatus.SC_INTERNAL_SERVER_ERROR, statusCode);

    }



}
