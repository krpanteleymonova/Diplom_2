import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderCreateTest {
    private User user;
    private UserClient userClient;
    private Ingredient ingredients;
    String token;

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
    }
    @Test
    @DisplayName("Успешное создание заказа без авторизации и ингридиентами")
    public void orderCanBeCreatedNoAuthorization() {
        sendPostRequestCreateUser();
        sendPostRequestCreateOrderNoAuthorization();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией без ингридиентов")
    public void orderCanNotBeCreated() {
        sendPostRequestCreateUser();
        sendPostRequestCreateOrderNoIngredients();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией c некорректными hash")
    public void orderCanNotBeCreatedIncorrectHash() {
        sendPostRequestCreateUser();
        sendPostRequestCreateOrderErrorIngredients();
    }

    @Step("Создание заказа")
    public void sendPostRequestCreateOrder() {
        ingredients= IngredientGenerator.getDefault();
        ValidatableResponse responseCreate = OrderClient.create(token,ingredients);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус создания заказа: " + statusCreate);
        boolean okCreate = responseCreate.extract().path("success");
        System.out.println("Заказ создан: " + okCreate);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCreate);
        Assert.assertTrue("Статус не соответствует ожидаемому", okCreate);
        int OrderNumber = responseCreate.extract().path("order.number");
        System.out.println("Номер заказа: " + OrderNumber);
        Assert.assertNotNull("Номер не найден", OrderNumber);
        String name=responseCreate.extract().path("order.owner.name");
        System.out.println("Name: " + name);
        Assert.assertEquals("Имя не соответствует ожидаемому",user.getName(),name );
    }
    @Step("Создание заказа без авторизации")
    public void sendPostRequestCreateOrderNoAuthorization() {
        ingredients= IngredientGenerator.getDefault();
        ValidatableResponse responseCreate = OrderClient.create("",ingredients);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус создания заказа: " + statusCreate);
        boolean okCreate = responseCreate.extract().path("success");
        System.out.println("Заказ создан: " + okCreate);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCreate);
        Assert.assertTrue("Статус не соответствует ожидаемому", okCreate);
        int OrderNumber = responseCreate.extract().path("order.number");
        System.out.println("Номер заказа: " + OrderNumber);
        Assert.assertNotNull("Номер не найден", OrderNumber);
        String name=responseCreate.extract().path("order.owner.name");
        System.out.println("Name: " + name);
        Assert.assertEquals("Имя не соответствует ожидаемому",null,name );
    }



    @Step("Создание заказа без ингридиентов")
    public void sendPostRequestCreateOrderNoIngredients() {
        ingredients= IngredientGenerator.getEmpty();
        ValidatableResponse responseCreate = OrderClient.create(token,ingredients);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус создания заказа: " + statusCreate);
        boolean okCreate = responseCreate.extract().path("success");
        System.out.println("Заказ создан: " + okCreate);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 400, statusCreate);
        Assert.assertFalse("Статус не соответствует ожидаемому", okCreate);
        String messageCreate = responseCreate.extract().path("message");
        Assert.assertEquals("Собщение не соответствует ожидаемому", "Ingredient ids must be provided", messageCreate);
    }

    @Step("Создание заказа c некорректными hash")
    public void sendPostRequestCreateOrderErrorIngredients() {
        ingredients= IngredientGenerator.getErrorHash();
        ValidatableResponse responseCreate = OrderClient.create(token,ingredients);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус создания заказа: " + statusCreate);
        boolean okCreate = responseCreate.extract().path("success");
        System.out.println("Заказ создан: " + okCreate);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 400, statusCreate);
        Assert.assertFalse("Статус не соответствует ожидаемому", okCreate);
        String messageCreate = responseCreate.extract().path("message");
        Assert.assertEquals("Собщение не соответствует ожидаемому", "One or more ids provided are incorrect", messageCreate);
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
        Assert.assertEquals("Статус код не соответствует ожидаемому", 202, statusDelete);

    }

}


