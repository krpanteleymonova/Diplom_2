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

public class OrderCreateTest {
    String token;
    int statusCode;
    boolean status;
    int orderNumber;
    String name;
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
    @DisplayName("Успешное создание заказа с авторизацией и ингридиентами")
    public void orderCanBeCreated() {
        sendPostRequestCreateUser();
        sendPostRequestCreateOrder();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCode);
        Assert.assertTrue("Статус не соответствует ожидаемому", status);
        Assert.assertNotNull("Номер не найден", orderNumber);
        Assert.assertEquals("Имя не соответствует ожидаемому", user.getName(), name);
    }

    @Test
    @DisplayName("Успешное создание заказа без авторизации и ингридиентами")
    public void orderCanBeCreatedNoAuthorization() {
        sendPostRequestCreateUser();
        sendPostRequestCreateOrderNoAuthorization();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCode);
        Assert.assertTrue("Статус не соответствует ожидаемому", status);
        Assert.assertNotNull("Номер не найден", orderNumber);
        Assert.assertEquals("Имя не соответствует ожидаемому", null, name);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией без ингридиентов")
    public void orderCanNotBeCreated() {
        sendPostRequestCreateUser();
        sendPostRequestCreateOrderNoIngredients();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 400, statusCode);
        Assert.assertFalse("Статус не соответствует ожидаемому", status);
        Assert.assertEquals("Собщение не соответствует ожидаемому", "Ingredient ids must be provided", message);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией c некорректными hash")
    public void orderCanNotBeCreatedIncorrectHash() {
        sendPostRequestCreateUser();
        sendPostRequestCreateOrderErrorIngredients();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 400, statusCode);
        Assert.assertFalse("Статус не соответствует ожидаемому", status);
        Assert.assertEquals("Собщение не соответствует ожидаемому", "One or more ids provided are incorrect", message);
    }

    @Step("Создание заказа")
    public void sendPostRequestCreateOrder() {
        ingredients = IngredientGenerator.getDefault();
        ValidatableResponse responseCreate = OrderClient.create(token, ingredients);
        statusCode = responseCreate.extract().statusCode();
        System.out.println("Статус создания заказа: " + statusCode);
        status = responseCreate.extract().path("success");
        System.out.println("Заказ создан: " + status);
        orderNumber = responseCreate.extract().path("order.number");
        System.out.println("Номер заказа: " + orderNumber);
        name = responseCreate.extract().path("order.owner.name");
        System.out.println("Name: " + name);
    }

    @Step("Создание заказа без авторизации")
    public void sendPostRequestCreateOrderNoAuthorization() {
        ingredients = IngredientGenerator.getDefault();
        ValidatableResponse responseCreate = OrderClient.create("", ingredients);
        statusCode = responseCreate.extract().statusCode();
        System.out.println("Статус создания заказа: " + statusCode);
        status = responseCreate.extract().path("success");
        System.out.println("Заказ создан: " + status);
        orderNumber = responseCreate.extract().path("order.number");
        System.out.println("Номер заказа: " + orderNumber);
        name = responseCreate.extract().path("order.owner.name");
        System.out.println("Name: " + name);
    }

    @Step("Создание заказа без ингридиентов")
    public void sendPostRequestCreateOrderNoIngredients() {
        ingredients = IngredientGenerator.getEmpty();
        ValidatableResponse responseCreate = OrderClient.create(token, ingredients);
        statusCode = responseCreate.extract().statusCode();
        System.out.println("Статус создания заказа: " + statusCode);
        status = responseCreate.extract().path("success");
        System.out.println("Заказ создан: " + status);
        message = responseCreate.extract().path("message");
    }

    @Step("Создание заказа c некорректными hash")
    public void sendPostRequestCreateOrderErrorIngredients() {
        ingredients = IngredientGenerator.getErrorHash();
        ValidatableResponse responseCreate = OrderClient.create(token, ingredients);
        statusCode = responseCreate.extract().statusCode();
        System.out.println("Статус создания заказа: " + statusCode);
        status = responseCreate.extract().path("success");
        System.out.println("Заказ создан: " + status);
        message = responseCreate.extract().path("message");
    }

    @Step("Регистрация пользователя")
    public void sendPostRequestCreateUser() {
        ValidatableResponse responseCreate = userClient.register(user);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус создания пользователя: " + statusCreate);
        boolean okCreate = responseCreate.extract().path("success");
        System.out.println("Пользователь создан: " + okCreate);
        token = responseCreate.extract().path("accessToken");
    }

    @Step("Удаление пользователя из системы")
    public void sendDeleteRequestUser() {
        ValidatableResponse responseDelete = userClient.deleteUser(token);
        int statusDelete = responseDelete.extract().statusCode();
        boolean okDelete = responseDelete.extract().path("success");
        String messageDelete = responseDelete.extract().path("message");
        System.out.println("Статус удаления пользователя: " + statusDelete);
    }
}


