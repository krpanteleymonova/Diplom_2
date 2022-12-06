import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ChangeUserTest {

    private User user;
    private UserClient userClient;
    String token;
    String tokenAut;

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
    }

    @Test
    @DisplayName("Успешное изменение пароля пользователя")
    public void userPasswordCanBeChange() {
        sendPostRequestCreateUser();
        sendPostRequestLoginUser();
        setNewPasswordChangeUser();
        patchRequestCheckChangeUser();
    }

    @Test
    @DisplayName("Успешное изменение email пользователя")
    public void userEmailCanBeChange() {
        sendPostRequestCreateUser();
        sendPostRequestLoginUser();
        setNewEmailChangeUser();
        patchRequestCheckChangeUser();
    }

    @Test
    @DisplayName("Успешное изменение имени пользователя невозможно без авторизации")
    public void userNameCanNotBeChange() {
        sendPostRequestCreateUser();
        sendPostRequestLoginUser();
        setNewNameChangeUser();
    }

    @Test
    @DisplayName("Успешное изменение пароля пользователя  невозможно без авторизации")
    public void userPasswordCanNotBeChange() {
        sendPostRequestCreateUser();
        sendPostRequestLoginUser();
        setNewPasswordChangeUser();
        sendPatchRequestNoAuthorizationChangeUser();
    }

    @Test
    @DisplayName("Успешное изменение email пользователя  невозможно без авторизации")
    public void userEmailCanNotBeChange() {
        sendPostRequestCreateUser();
        sendPostRequestLoginUser();
        setNewEmailChangeUser();
        sendPatchRequestNoAuthorizationChangeUser();
    }


    @Step("Изменение имени уникального пользователя")
    public void setNewNameChangeUser() {
        user.setName(UserGenerator.newName);
    }

    @Step("Проверка успешного изменения полей пользователя")
    public void patchRequestCheckChangeUser() {
        System.out.println("Name Change:  " + user.getName());
        System.out.println("Email Change: " + user.getEmail());
        ValidatableResponse responseChange = userClient.ChangeUser(tokenAut, user);
        int statusChange = responseChange.extract().statusCode();
        System.out.println("Статус изменения пользователя: " + statusChange);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusChange);
        boolean okChange = responseChange.extract().path("success");
        Assert.assertTrue("Статус не соответствует ожидаемому", okChange);
        System.out.println("Name и Email после изменения" + responseChange.extract().path("user"));
        Assert.assertEquals("Email не соответствует ожидаемому", user.getEmail(), responseChange.extract().path("user.email"));
        Assert.assertEquals("Name не соответствует ожидаемому", user.getName(), responseChange.extract().path("user.name"));
    }

    @Step("Регистрация уникального пользователя")
    public void sendPostRequestCreateUser() {
        ValidatableResponse responseCreate = userClient.register(user);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус создания пользователя: " + statusCreate);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCreate);
        token = responseCreate.extract().path("accessToken");
    }

    @Step("Авторизация уникального пользователя")
    public void sendPostRequestLoginUser() {
        ValidatableResponse responseLogin = userClient.login(user);
        int statusCreate = responseLogin.extract().statusCode();
        System.out.println("Статус авторизации пользователя: " + statusCreate);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCreate);
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
        ValidatableResponse responseChange = userClient.ChangeUser("", user);
        int statusChange = responseChange.extract().statusCode();
        System.out.println("Статус изменения пользователя: " + statusChange);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 401, statusChange);
        boolean okChange = responseChange.extract().path("success");
        Assert.assertFalse("Статус не соответствует ожидаемому", okChange);
        String messageChange = responseChange.extract().path("message");
        Assert.assertEquals("Сообщение не соответствует ожидаемому", "You should be authorised", messageChange);
    }

    @Step("Удаление пользователя из системы")
    public void sendDeleteRequestUser() {
        ValidatableResponse responseDelete = userClient.deleteUser(token);
        int statusDelete = responseDelete.extract().statusCode();
        System.out.println("Статус удаления пользователя: " + statusDelete);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 202, statusDelete);
    }
}
