import Ingredients.Ingredient;
import Ingredients.IngredientGenerator;
import Orders.OrderClient;
import Users.User;
import Users.UserClient;
import Users.UserGenerator;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderGetTest {
    String token;
    int orderNumber;
    int statusCode;
    boolean status;
    int orders;
    String message;
    private User user;
    private UserClient userClient;
    private Ingredient ingredients;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefaultUser();
    }

    @After
    public void cleanUp() {
        sendDeleteRequestUser();
    }

    @Test
    @DisplayName("Получение списка заказов с авторизацией")
    public void orderCanBeCreated() {
        sendPostRequestCreateUser();
        sendPostRequestCreateOrder();
        sendGetOrderRequest();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCode);
        Assert.assertEquals("Статус код не соответствует ожидаемому", orderNumber, orders);
    }

    @Test
    @DisplayName("олучение списка заказов без авторизации")
    public void orderCanBeCreatedNoAuthorization() {
        sendPostRequestCreateUser();
        sendGetOrderRequestNoAuthorization();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 401, statusCode);
        Assert.assertFalse("Статус не соответствует ожидаемому", status);
        Assert.assertEquals("Собщение не соответствует ожидаемому", "You should be authorised", message);
    }

    @Step("Получение списка заказов")
    public void sendGetOrderRequest() {
        ValidatableResponse responseCreate = OrderClient.getOrders(token);
        statusCode = responseCreate.extract().statusCode();
        System.out.println("Статус олучения списка заказов: " + statusCode);
        orders = responseCreate.extract().path("orders[0].number");
    }

    @Step("Получение списка заказов без авторизации")
    public void sendGetOrderRequestNoAuthorization() {
        ValidatableResponse responseCreate = OrderClient.getOrders("");
        statusCode = responseCreate.extract().statusCode();
        System.out.println("Статус олучения списка заказов: " + statusCode);
        status = responseCreate.extract().path("success");
        message = responseCreate.extract().path("message");
    }

    @Step("Создание заказа")
    public void sendPostRequestCreateOrder() {
        ingredients = IngredientGenerator.getDefault();
        ValidatableResponse responseCreate = OrderClient.create(token, ingredients);
        statusCode = responseCreate.extract().statusCode();
        System.out.println("Статус создания заказа: " + statusCode);
        status = responseCreate.extract().path("success");
        orderNumber = responseCreate.extract().path("order.number");
        System.out.println("Номер заказа: " + orderNumber);
        String name = responseCreate.extract().path("order.owner.name");
    }

    @Step("Регистрация пользователя")
    public void sendPostRequestCreateUser() {
        ValidatableResponse responseCreate = userClient.register(user);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус создания пользователя: " + statusCreate);
        token = responseCreate.extract().path("accessToken");
    }

    @Step("Удаление пользователя из системы")
    public void sendDeleteRequestUser() {
        ValidatableResponse responseDelete = userClient.deleteUser(token);
        int statusDelete = responseDelete.extract().statusCode();
        System.out.println("Статус удаления пользователя: " + statusDelete);
    }
}


