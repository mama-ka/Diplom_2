import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class LoginUserTest {
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
    @DisplayName("Пользователь может авторизоваться")
    public void authorizationUserCheckStatusCode200AndReturnAccessTokenAndEmail() {
        UserLogin bodyLogin = UserLogin.from (user);
        String email = userCreateAndDelete.login(bodyLogin)
                .assertThat()
                .statusCode(200)
                .and()
                .body("user.name", notNullValue())
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue())
                .extract()
                .path("user.email");
        assertEquals("Запрос возвращает другой email", user.email, email);
    }

    @Test
    @DisplayName("Нельзя авторизоваться с некорректным email")
    public void authorizationIncorrectEmailCheckStatusCode401() {
        UserLogin bodyLogin = new UserLogin ("email@ya.ru", user.password);
        String message = userCreateAndDelete.login (bodyLogin)
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");
        assertThat("Можно зарегестрироваться с некорректным логином", message, equalTo("email or password are incorrect"));
    }
    @Test
    @DisplayName("Нельзя авторизоваться с некорректным паролем")
    public void authorizationIncorrectPasswordCheckStatusCode401() {
        UserLogin bodyLogin = new UserLogin (user.email, "password");
        String message = userCreateAndDelete.login (bodyLogin)
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");
        assertThat("Можно зарегестрироваться с некорректным паролем", message, equalTo("email or password are incorrect"));
    }
}
