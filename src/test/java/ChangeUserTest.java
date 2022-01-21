import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class ChangeUserTest {
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
    @DisplayName("Пользователь может изменить пароль (авторизованный)")
    public void changeUserPasswordCheckStatusCode200WithToken() {
        UserLogin bodyLogin = UserLogin.from (user);
        String accessTokenAuth = userCreateAndDelete.login(bodyLogin).extract().path("accessToken");
        UserChange bodyChange = UserChange.getRandom();

        //согласно документации можно изменить любые данные, пароль в теле ответа не возвращается,
        //поэтому проверить изменился ли он визуально нельзя,
        // проверка возможна через повторную авторизацию с новым паролем, вернулся 200 ОК

        userCreateAndDelete.change(accessTokenAuth, bodyChange)
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));

        UserLogin bodyLoginChange = UserLogin.from (bodyChange);
        userCreateAndDelete.login(bodyLoginChange)
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Пользователь может изменить почту и имя (авторизованный)")
    public void changeUserNameEmailCheckStatusCode200WithToken() {
        UserLogin bodyLogin = UserLogin.from (user);
        String accessTokenAuth = userCreateAndDelete.login(bodyLogin).extract().path("accessToken");
        UserChange bodyChange = UserChange.getRandom();

        String emailChange = userCreateAndDelete.change(accessTokenAuth, bodyChange)
                .assertThat()
                .statusCode(200)
                .extract()
                .path("user.email");

        String nameChange = userCreateAndDelete.change(accessTokenAuth, bodyChange)
                .assertThat()
                .statusCode(200)
                .extract()
                .path("user.name");

        assertEquals("Запрос возвращает другой email", bodyChange.email, emailChange);
        assertEquals("Запрос возвращает другой name", bodyChange.name, nameChange);
    }


    @Test
    @DisplayName("Пользователь не может изменить пароль без авторизации")
    public void changeUserPasswordCheckStatusCode401WithoutToken() {
        UserChange bodyChange = UserChange.getPassword();
        String message = userCreateAndDelete.changeWithoutToken(bodyChange)
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");
        assertEquals("Можно поменять данные без авторизации", "You should be authorised", message);
    }
    @Test
    @DisplayName("Пользователь не может изменить Email без авторизации")
    public void changeUserEmailCheckStatusCode401WithoutToken() {
        UserChange bodyChange = UserChange.getEmail();
        String message = userCreateAndDelete.changeWithoutToken(bodyChange)
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");
        assertEquals("Можно поменять данные без авторизации", "You should be authorised", message);
    }

    @Test
    @DisplayName("Пользователь не может изменить name без авторизации")
    public void changeUserNameCheckStatusCode401WithoutToken() {
        UserChange bodyChange = UserChange.getName();
        String message = userCreateAndDelete.changeWithoutToken(bodyChange)
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");
        assertEquals("Можно поменять данные без авторизации", "You should be authorised", message);
    }
}
