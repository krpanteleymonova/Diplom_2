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

public class CreateUserTest {

    String token;
    int statusCode;
    boolean status;
    String message;
    private User user;
    private UserClient userClient;

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
    @DisplayName("Успешное создание учетной записи c заполнением всех параметров")
    public void userCanBeCreated() {
        sendPostRequestCreateUser();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCode);
        Assert.assertTrue("Статус не соответствует ожидаемому", status);
    }

    @Test
    @DisplayName("Повторное создание учетной записи")
    public void userAlreadyExistCanNotBeCreated() {
        sendPostRequestCreateUser();
        sendSecondPostRequestCreateUser();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 403, statusCode);
        Assert.assertFalse("Статус не соответствует ожидаемому", status);
        Assert.assertEquals("Собщение не соответствует ожидаемому", "User already exists", message);
    }

    @Step("Регистрация уникального пользователя")
    public void sendPostRequestCreateUser() {
        ValidatableResponse responseCreate = userClient.register(user);
        statusCode = responseCreate.extract().statusCode();
        System.out.println("Статус создания пользователя: " + statusCode);
        status = responseCreate.extract().path("success");
        System.out.println("Пользователь создан: " + status);
        token = responseCreate.extract().path("accessToken");
        System.out.println("Токен: " + token);
    }

    @Step("Регистрация существующего пользователя")
    public void sendSecondPostRequestCreateUser() {
        ValidatableResponse responseCreate = userClient.register(user);
        statusCode = responseCreate.extract().statusCode();
        System.out.println("Статус создания пользователя: " + statusCode);
        status = responseCreate.extract().path("success");
        System.out.println("Пользователь создан: " + status);
        message = responseCreate.extract().path("message");
    }

    @Step("Удаление пользователя из системы")
    public void sendDeleteRequestUser() {
        ValidatableResponse responseDelete = userClient.deleteUser(token);
        int statusDelete = responseDelete.extract().statusCode();
        System.out.println("Статус удаления пользователя: " + statusDelete);
    }
}
