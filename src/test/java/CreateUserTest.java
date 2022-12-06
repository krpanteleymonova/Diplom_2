import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreateUserTest {

    private User user;
    private UserClient userClient;
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
    @DisplayName("Успешное создание учетной записи c заполнением всех параметров")
    public void userCanBeCreated() {
        sendPostRequestCreateUser();
    }

    @Test
    @DisplayName("Повторное создание учетной записи")
    public void userAlreadyExistCanNotBeCreated() {
        sendPostRequestCreateUser();
        sendSecondPostRequestCreateUser();
    }

    @Step("Регистрация уникального пользователя")
    public void sendPostRequestCreateUser() {
        ValidatableResponse responseCreate = userClient.register(user);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус создания пользователя: " + statusCreate);
        boolean okCreate = responseCreate.extract().path("success");
        System.out.println("Пользователь создан: " + okCreate);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 200, statusCreate);
        Assert.assertTrue("Статус не соответствует ожидаемому", okCreate);
        token = responseCreate.extract().path("accessToken");
        System.out.println("Токен: " + token);
    }

    @Step("Регистрация существующего пользователя")
    public void sendSecondPostRequestCreateUser() {
        ValidatableResponse responseCreate = userClient.register(user);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус создания пользователя: " + statusCreate);
        boolean okCreate = responseCreate.extract().path("success");
        System.out.println("Пользователь создан: " + okCreate);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 403, statusCreate);
        Assert.assertFalse("Статус не соответствует ожидаемому", okCreate);
        String messageCreate = responseCreate.extract().path("message");
        Assert.assertEquals("Собщение не соответствует ожидаемому", "User already exists", messageCreate);
    }

    @Step("Удаление пользователя из системы")
    public void sendDeleteRequestUser() {
        ValidatableResponse responseDelete = userClient.deleteUser(token);
        int statusDelete = responseDelete.extract().statusCode();
        boolean okDelete = responseDelete.extract().path("success");
        String messageDelete = responseDelete.extract().path("message");
        System.out.println("Статус удаления пользователя: " + statusDelete);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 202, statusDelete);
        Assert.assertTrue("Статус не соответствует ожидаемому", okDelete);
        Assert.assertEquals("Собщение не соответствует ожидаемому", "User successfully removed", messageDelete);
    }
}
