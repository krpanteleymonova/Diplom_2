import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderGetTest {
    private User user;
    private UserClient userClient;
    private Ingredient ingredients;
    String token;
    int orderNumber;

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
    }

    @Test
    @DisplayName("олучение списка заказов без авторизации")
    public void orderCanBeCreatedNoAuthorization() {
        sendPostRequestCreateUser();
        sendGetOrderRequestNoAuthorization();
    }

    @Step("Получение списка заказов")
    public void sendGetOrderRequest() {
        ValidatableResponse responseCreate = OrderClient.getOrders(token);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус олучения списка заказов: " + statusCreate);
        boolean okCreate = responseCreate.extract().path("success");
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCreate);
        int orders = responseCreate.extract().path("orders[0].number");
        Assert.assertEquals("Статус код не соответствует ожидаемому", orderNumber, orders);
    }

    @Step("Получение списка заказов без авторизации")
    public void sendGetOrderRequestNoAuthorization() {
        ValidatableResponse responseCreate = OrderClient.getOrders("");
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус олучения списка заказов: " + statusCreate);
        boolean okCreate = responseCreate.extract().path("success");
        Assert.assertEquals("Статус код не соответствует ожидаемому", 401, statusCreate);
        Assert.assertFalse("Статус не соответствует ожидаемому", okCreate);
        String messageCreate = responseCreate.extract().path("message");
        Assert.assertEquals("Собщение не соответствует ожидаемому", "You should be authorised", messageCreate);
    }

    @Step("Создание заказа")
    public void sendPostRequestCreateOrder() {
        ingredients = IngredientGenerator.getDefault();
        ValidatableResponse responseCreate = OrderClient.create(token, ingredients);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус создания заказа: " + statusCreate);
        boolean okCreate = responseCreate.extract().path("success");
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


