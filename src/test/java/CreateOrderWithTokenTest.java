import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class CreateOrderWithTokenTest {
    private UserCreateAndDelete userCreateAndDelete;
    private String accessToken;
    UserRegister user = UserRegister.getRandom();
    String ingredients;

    @Before
    public void setUp() {
        userCreateAndDelete = new UserCreateAndDelete();
        accessToken = userCreateAndDelete.create(user).extract().path("accessToken");
    }

    @After
    public void deletedUser(){
        userCreateAndDelete.delete(accessToken);
    }

    @Test
    @DisplayName("Можно создать заказа с авторизацией")
    public void orderCreateStatusCode200WithToken() {
        UserLogin bodyLogin = UserLogin.from (user);
        String accessTokenAuth = userCreateAndDelete.login(bodyLogin).extract().path("accessToken");

        ingredients = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6f\"] }";
        OrderCreate orderCreate = new OrderCreate();
        int orderNumber = orderCreate.createOrder(accessTokenAuth, ingredients)
                .assertThat()
                .statusCode(200)
                .extract()
                .path("order.number");
        assertThat("Не возвращается номер заказа", orderNumber, is(not(0)));
    }

    @Test
    @DisplayName("Нельзя создать заказ без ингридиентов")
    public void orderCreateEmptyIngredientsStatusCode400WithToken() {
        UserLogin bodyLogin = UserLogin.from (user);
        String accessTokenAuth = userCreateAndDelete.login(bodyLogin).extract().path("accessToken");

        ingredients = "{\"ingredients\": [] }";
        OrderCreate orderCreate = new OrderCreate();
        String massage = orderCreate.createOrder(accessTokenAuth, ingredients)
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");
        assertEquals("Запрос возвращает другой massage", "Ingredient ids must be provided", massage);
    }

    @Test
    @DisplayName("Нельзя создать заказ c невалидным хеш ингредиента")
    public void orderCreateNonValidIngredientsStatusCode500WithToken() {
        UserLogin bodyLogin = UserLogin.from (user);
        String accessTokenAuth = userCreateAndDelete.login(bodyLogin).extract().path("accessToken");

        ingredients = "{\"ingredients\": [\"ррррррррррррррр\"] }";
        OrderCreate orderCreate = new OrderCreate();
        orderCreate.createOrder(accessTokenAuth, ingredients)
                .assertThat()
                .statusCode(500);
    }
}
