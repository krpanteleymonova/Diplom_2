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

public class ChangeUserTest {
    String token;
    String tokenAut;
    int statusCode;
    boolean status;
    String responseEmail;
    String responseName;
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
    @DisplayName("Успешное изменение имени пользователя")
    public void userNameCanBeChange() {
        sendPostRequestCreateUser();
        sendPostRequestLoginUser();
        setNewNameChangeUser();
        patchRequestCheckChangeUser();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCode);
        Assert.assertTrue("Статус не соответствует ожидаемому", status);
        Assert.assertEquals("Email не соответствует ожидаемому", user.getEmail(), responseEmail);
        Assert.assertEquals("Name не соответствует ожидаемому", user.getName(), responseName);
    }

    @Test
    @DisplayName("Успешное изменение пароля пользователя")
    public void userPasswordCanBeChange() {
        sendPostRequestCreateUser();
        sendPostRequestLoginUser();
        setNewPasswordChangeUser();
        patchRequestCheckChangeUser();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCode);
        Assert.assertTrue("Статус не соответствует ожидаемому", status);
        Assert.assertEquals("Email не соответствует ожидаемому", user.getEmail(), responseEmail);
        Assert.assertEquals("Name не соответствует ожидаемому", user.getName(), responseName);
    }

    @Test
    @DisplayName("Успешное изменение email пользователя")
    public void userEmailCanBeChange() {
        sendPostRequestCreateUser();
        sendPostRequestLoginUser();
        setNewEmailChangeUser();
        patchRequestCheckChangeUser();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCode);
        Assert.assertTrue("Статус не соответствует ожидаемому", status);
        Assert.assertEquals("Email не соответствует ожидаемому", user.getEmail(), responseEmail);
        Assert.assertEquals("Name не соответствует ожидаемому", user.getName(), responseName);
    }

    @Test
    @DisplayName("Успешное изменение имени пользователя невозможно без авторизации")
    public void userNameCanNotBeChange() {
        sendPostRequestCreateUser();
        setNewNameChangeUser();
        sendPatchRequestNoAuthorizationChangeUser();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 401, statusCode);
        Assert.assertFalse("Статус не соответствует ожидаемому", status);
        Assert.assertEquals("Сообщение не соответствует ожидаемому", "You should be authorised", message);
    }

    @Test
    @DisplayName("Успешное изменение пароля пользователя  невозможно без авторизации")
    public void userPasswordCanNotBeChange() {
        sendPostRequestCreateUser();
        setNewPasswordChangeUser();
        sendPatchRequestNoAuthorizationChangeUser();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 401, statusCode);
        Assert.assertFalse("Статус не соответствует ожидаемому", status);
        Assert.assertEquals("Сообщение не соответствует ожидаемому", "You should be authorised", message);
    }

    @Test
    @DisplayName("Успешное изменение email пользователя  невозможно без авторизации")
    public void userEmailCanNotBeChange() {
        sendPostRequestCreateUser();
        sendPostRequestLoginUser();
        setNewEmailChangeUser();
        sendPatchRequestNoAuthorizationChangeUser();
        Assert.assertFalse("Статус не соответствует ожидаемому", status);
        Assert.assertEquals("Сообщение не соответствует ожидаемому", "You should be authorised", message);
    }

    @Step("Изменение имени уникального пользователя")
    public void setNewNameChangeUser() {
        user.setName(UserGenerator.newName);
    }

    @Step("Проверка успешного изменения полей пользователя")
    public void patchRequestCheckChangeUser() {
        System.out.println("Name Change:  " + user.getName());
        System.out.println("Email Change: " + user.getEmail());
        ValidatableResponse responseChange = userClient.changeUser(tokenAut, user);
        statusCode = responseChange.extract().statusCode();
        System.out.println("Статус изменения пользователя: " + statusCode);
        status = responseChange.extract().path("success");
        responseEmail = responseChange.extract().path("user.email");
        responseName = responseChange.extract().path("user.name");
        System.out.println("Name и Email после изменения" + responseChange.extract().path("user"));
    }

    @Step("Регистрация уникального пользователя")
    public void sendPostRequestCreateUser() {
        ValidatableResponse responseCreate = userClient.register(user);
        statusCode = responseCreate.extract().statusCode();
        System.out.println("Статус создания пользователя: " + statusCode);
        token = responseCreate.extract().path("accessToken");
    }

    @Step("Авторизация уникального пользователя")
    public void sendPostRequestLoginUser() {
        ValidatableResponse responseLogin = userClient.login(user);
        statusCode = responseLogin.extract().statusCode();
        System.out.println("Статус авторизации пользователя: " + statusCode);
        tokenAut = responseLogin.extract().path("accessToken");
        System.out.println("Токен при авторизации: " + tokenAut);
        System.out.println("Name и Email при авторизации " + responseLogin.extract().path("user"));
    }

    @Step("Изменение пароля уникального пользователя")
    public void setNewPasswordChangeUser() {
        user.setPassword(UserGenerator.newPassword);
    }

    @Step("Изменение email уникального пользователя")
    public void setNewEmailChangeUser() {
        user.setEmail(UserGenerator.newEmail);
    }

    @Step("Проверка изменений уникального пользователя без авторизации")
    public void sendPatchRequestNoAuthorizationChangeUser() {
        ValidatableResponse responseChange = userClient.changeUser("", user);
        statusCode = responseChange.extract().statusCode();
        System.out.println("Статус изменения пользователя: " + statusCode);
        status = responseChange.extract().path("success");
        message = responseChange.extract().path("message");
    }

    @Step("Удаление пользователя из системы")
    public void sendDeleteRequestUser() {
        ValidatableResponse responseDelete = userClient.deleteUser(token);
        int statusDelete = responseDelete.extract().statusCode();
        System.out.println("Статус удаления пользователя: " + statusDelete);
    }
}
