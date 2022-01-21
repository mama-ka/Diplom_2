import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
public class OrderCreate extends RestAssuredClient {

    @Step ("Создание заказа")
    public ValidatableResponse createOrder(String accessToken, String ingredients) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken.substring(7))
                .body(ingredients)
                .when()
                .post("/api/orders")
                .then();
    }
    @Step ("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutToken(String ingredients) {
        return given()
                .spec(getBaseSpec())
                .body(ingredients)
                .when()
                .post("/api/orders")
                .then();
    }

    @Step ("Получение заказов конкретного пользователя")
    public Response orderResponseBody(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken.substring(7))
                .get("/api/orders");
    }

    @Step ("Получение заказов конкретного пользователя без авторизции")
    public Response orderResponseBodyWithoutToken() {
        return given()
                .spec(getBaseSpec())
                .get("/api/orders");
    }
}

