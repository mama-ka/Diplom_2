import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetOrdersTest {
    private UserCreateAndDelete userCreateAndDelete;
    private String accessToken;
    UserRegister user = UserRegister.getRandom();

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
    @DisplayName("Можно получить заказ конкретного пользователя с авторизацией")
    public void getOrdersCheckStatusCode200WithToken() {
        UserLogin bodyLogin = UserLogin.from (user);
        String accessTokenAuth = userCreateAndDelete.login(bodyLogin).extract().path("accessToken");
        OrderCreate orderCreate = new OrderCreate();
        int numberTotal = orderCreate.orderResponseBody(accessTokenAuth)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .extract().path("total");
        assertThat("Не возвращается номер", numberTotal, is(not(0)));
    }

    @Test
    @DisplayName("Получение заказа без токена")
    public void orderCreateStatusCode401WithoutToken(){
        OrderCreate orderCreate = new OrderCreate();
        String message = orderCreate.orderResponseBodyWithoutToken()
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .extract().path("message");
        assertThat("Можно зарегестрироваться с некорректным логином", message, equalTo("You should be authorised"));
    }
}
