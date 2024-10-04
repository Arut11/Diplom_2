
import io.restassured.response.ValidatableResponse;
import order.OrderClient;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.User;
import user.UserClient;
import user.UserGenerator;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = UserGenerator.getUser();
        createResponse = userClient.createUser(user);
        token = createResponse.extract().path("accessToken");

    }

    @AfterEach
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
        assertNotNull(orders,
                "Поле orders пустое");
        assertNotNull(total,
                "Поле total пустое");
        assertNotNull(totalToday,
                "Поле totalToday пустое");

    }

    @Test
    @DisplayName("Проверка получения заказов конкретного пользователя")
    public void createGetOrderWithoutAuthTest() {
        ValidatableResponse createResponse = orderClient.getOrdersWithoutAuth();
        success = createResponse.extract().path("success");
        message = createResponse.extract().path("message");
        statusCode = createResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode,
                "Статус код вернулся не 401");
        assertFalse(success,
                "В теле ответа в поле success вернулось значение true");
        assertEquals("You should be authorised", message,
                "Поле message пустое");

    }

}
