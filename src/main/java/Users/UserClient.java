package Users;

import Utils.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private static final String PATH_REGISTER = "api/auth/register/";
    private static final String PATH_LOGIN = "api/auth/login/";
    private static final String PATH_LOGOUT = "/api/auth/logout/";
    private static final String PATH_USER = "/api/auth/user/";

    @Step("Отправляем запрос на регистрацию клиента")
    public ValidatableResponse register(User user) {
        return given()
                .spec(Client.getSpec())
                .body(user)
                .when()
                .post(PATH_REGISTER)
                .then();
    }

    @Step("Отправляем запрос на вход в личный кабинет")
    public ValidatableResponse login(User user) {
        return given()
                .spec(Client.getSpec())
                .body(user)
                .when()
                .post(PATH_LOGIN)
                .then();
    }

    @Step("Отправляем запрос на удаление клиента")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(Client.getSpec())
                .header(
                        "Authorization"
                        ,
                        token)
                .when()
                .delete(PATH_USER)
                .then();
    }

    @Step("Отправляем запрос на изменение клиента")
    public ValidatableResponse changeUser(String tokenAut, User user) {
        return given()
                .spec(Client.getSpec())
                .header(
                        "Authorization"
                        ,
                        tokenAut)
                .body(user)
                .when()
                .patch(PATH_USER)
                .then();
    }
}


