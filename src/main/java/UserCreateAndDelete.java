import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserCreateAndDelete extends RestAssuredClient {
    @Step("Создание пользователя")
    public ValidatableResponse create(UserRegister user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register")
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete (String accessToken){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken.substring(7))
                .when()
                .delete("/api/auth/user") // отправка DELETE-запроса
                .then();
    }

    @Step ("Авторизация пользователя")
    public ValidatableResponse login(UserLogin bodyLogin) {
        return given()
                .spec(getBaseSpec())
                .body(bodyLogin)
                .when()
                .post("/api/auth/login")
                .then();
    }

    @Step("Изменение пользователя с токеном")
    public ValidatableResponse change (String accessToken, UserChange bodyChange){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken.substring(7))
                .body(bodyChange)
                .when()
                .patch("/api/auth/user")
                .then();
    }

    @Step("Изменение пользователя без токена")
    public ValidatableResponse changeWithoutToken (UserChange bodyChange){
        return given()
                .spec(getBaseSpec())
                .body(bodyChange)
                .when()
                .patch("/api/auth/user")
                .then();
    }
}
