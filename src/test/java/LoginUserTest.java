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

public class LoginUserTest {

    String token;
    int statusCode;
    boolean status;
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
    @DisplayName("Успешное авторизация пользователя")
    public void userCanBeLogin() {
        sendPostRequestCreateUser();
        sendPostRequestLoginUser();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCode);
        Assert.assertTrue("Статус не соответствует ожидаемому", status);
    }

    @Test
    @DisplayName("Неуспешная авторизация пользователя , некорректный пароль")
    public void userCanNotBeLoginIncorrectPassword() {
        sendPostRequestCreateUser();
        sendPostRequestLoginIncorrectPasswordUser();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 401, statusCode);
        Assert.assertFalse("Статус не соответствует ожидаемому", status);
    }

    @Test
    @DisplayName("Неуспешная авторизация пользователя , некорректный логин")
    public void userCanNotBeLoginIncorrectEmail() {
        sendPostRequestCreateUser();
        sendPostRequestLoginIncorrectEmailUser();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 401, statusCode);
        Assert.assertFalse("Статус не соответствует ожидаемому", status);
    }

    @Step("Регистрация уникального пользователя")
    public void sendPostRequestCreateUser() {
        ValidatableResponse responseCreate = userClient.register(user);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус создания пользователя: " + statusCreate);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCreate);
        token = responseCreate.extract().path("accessToken");
        System.out.println("Токен: " + token);
    }

    @Step("Авторизация уникального пользователя")
    public void sendPostRequestLoginUser() {
        ValidatableResponse responseLogin = userClient.login(user);
        statusCode = responseLogin.extract().statusCode();
        status = responseLogin.extract().path("success");
        System.out.println("Статус создания пользователя: " + statusCode);
    }

    @Step("Авторизация пользователя с некорректным паролем")
    public void sendPostRequestLoginIncorrectPasswordUser() {
        user.setPassword("123456");
        ValidatableResponse responseLogin = userClient.login(user);
        statusCode = responseLogin.extract().statusCode();
        status = responseLogin.extract().path("success");
        System.out.println("Статус создания пользователя: " + statusCode);
    }

    @Step("Авторизация пользователя с некорректным паролем")
    public void sendPostRequestLoginIncorrectEmailUser() {
        user.setEmail("IncorrectEmailUser@yandex.ru");
        ValidatableResponse responseLogin = userClient.login(user);
        statusCode = responseLogin.extract().statusCode();
        status = responseLogin.extract().path("success");
        System.out.println("Статус создания пользователя: " + statusCode);
    }

    @Step("Удаление пользователя из системы")
    public void sendDeleteRequestUser() {
        ValidatableResponse responseDelete = userClient.deleteUser(token);
        int statusDelete = responseDelete.extract().statusCode();
        System.out.println("Статус удаления пользователя: " + statusDelete);
    }
}
