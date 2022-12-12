import Users.User;
import Users.UserClient;
import Users.UserGenerator;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateUserNegativeTest {
    ValidatableResponse responseCreate;
    int statusCode;
    boolean status;
    String message;
    private User user;
    private UserClient userClient;

    public CreateUserNegativeTest(User user) {
        this.user = user;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {UserGenerator.getUserNoEmail()},
                {UserGenerator.getUserNoPassword()},
                {UserGenerator.getUserNoName()},
                {UserGenerator.getUserNoParameters()},
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Cоздание учетной записи без заполнения обязательных параметров")
    public void userCanNotBeCreated() {
        sendPostRequestCreateUserNoParameters();
        Assert.assertEquals("Статус код не соответствует ожидаемому", 403, statusCode);
        Assert.assertFalse("Статус не соответствует ожидаемому", status);
        Assert.assertEquals("Собщение не соответствует ожидаемому", "Email, password and name are required fields", message);
    }

    @Step("Регистрация пользователя без заполнения обязательных параметров")
    public void sendPostRequestCreateUserNoParameters() {
        responseCreate = userClient.register(user);
        statusCode = responseCreate.extract().statusCode();
        System.out.println("Статус создания пользователя: " + statusCode);
        boolean status = responseCreate.extract().path("success");
        System.out.println("Пользователь создан: " + status);
        message = responseCreate.extract().path("message");
    }
}
