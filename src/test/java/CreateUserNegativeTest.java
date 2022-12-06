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

    private User user;
    private UserClient userClient;

    public CreateUserNegativeTest(User user) {
        this.user = user;
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
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

    @Test
    @DisplayName("Cоздание учетной записи без заполнения обязательных параметров")
    public void userCanNotBeCreated() {
        sendPostRequestCreateUserNoParameters();
    }

    @Step("Регистрация пользователя без заполнения обязательных параметров")
    public void sendPostRequestCreateUserNoParameters() {
        ValidatableResponse responseCreate = userClient.register(user);
        int statusCreate = responseCreate.extract().statusCode();
        System.out.println("Статус создания пользователя: " + statusCreate);
        boolean okCreate = responseCreate.extract().path("success");
        System.out.println("Пользователь создан: " + okCreate);
        Assert.assertEquals("Статус код не соответствует ожидаемому", 403, statusCreate);
        Assert.assertFalse("Статус не соответствует ожидаемому", okCreate);
        String messageCreate = responseCreate.extract().path("message");
        Assert.assertEquals("Собщение не соответствует ожидаемому", "Email, password and name are required fields", messageCreate);
    }
}
