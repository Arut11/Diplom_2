import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.OrderClient;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserGenerator;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class OrderViewTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private User user;
    private ValidatableResponse createResponse;
    private int totalToday;
    private int total;
    private ArrayList<String> orders;
    private String token;
    private boolean success;
    private String message;
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
    @DisplayName("Проверка получения заказов конкретного пользователя")
    public void createGetOrderTest() {
        ValidatableResponse createResponse = orderClient.getOrders(token);
        orders = createResponse.extract().path("orders");
        total = createResponse.extract().path("total");
        totalToday = createResponse.extract().path("totalToday");
        assertNotNull("Поле orders пустое", orders);
        assertNotNull("Поле total пустое", total);
        assertNotNull("Поле totalToday пустое", totalToday);

    }

    @Test
    @DisplayName("Проверка получения заказов конкретного пользователя")
    public void createGetOrderWithoutAuthTest() {
        ValidatableResponse createResponse = orderClient.getOrdersWithoutAuth();
        success = createResponse.extract().path("success");
        message = createResponse.extract().path("message");
        statusCode = createResponse.extract().statusCode();
        assertEquals("Статус код вернулся не 401",
                HttpStatus.SC_UNAUTHORIZED, statusCode);
        assertFalse("В теле ответа в поле success вернулось значение true", success);
        assertEquals("Поле message пустое",
                "You should be authorised", message);

    }

}
