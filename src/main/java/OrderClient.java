import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


public class OrderClient extends Client {

    private static final String PATH_ORDER = "api/orders/";



    public static ValidatableResponse create(String tokenAut,Ingredient ingredients) {
        return given()
                .spec(getSpec())
                .header(
                        "Authorization"
                        ,
                        tokenAut)
                .body(ingredients)
                .when()
                .post(PATH_ORDER)
                .then();
    }

    public static ValidatableResponse getOrders (String token) {
        return given()
                .spec(getSpec())
                .header(
                        "Authorization"
                        ,
                        token)
                .when()
                .get(PATH_ORDER)
                .then();
    }
}


