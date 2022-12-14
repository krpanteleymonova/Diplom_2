package Orders;

import Ingredients.Ingredient;
import Utils.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private static final String PATH_ORDER = "api/orders/";

    @Step("Отправляем запрос на создание заказа")
    public static ValidatableResponse create(String tokenAut, Ingredient ingredients) {
        return given()
                .spec(Client.getSpec())
                .header(
                        "Authorization"
                        ,
                        tokenAut)
                .body(ingredients)
                .when()
                .post(PATH_ORDER)
                .then();
    }

    @Step("Отправляем запрос на получение заказа")
    public static ValidatableResponse getOrders(String token) {
        return given()
                .spec(Client.getSpec())
                .header(
                        "Authorization"
                        ,
                        token)
                .when()
                .get(PATH_ORDER)
                .then();
    }
}


