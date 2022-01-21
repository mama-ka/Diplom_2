import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class CreateUserTest {
    private UserCreateAndDelete userCreateAndDelete;
    private String accessToken;

    @Before
    public void setUp() {
        userCreateAndDelete = new UserCreateAndDelete();
    }

    @After
    public void deletedUser(){
        userCreateAndDelete.delete(accessToken);
    }

    @Test
    @DisplayName("Создание уникального пользователя, токен")
    public void createUserAndCheckToken(){
        UserRegister user = UserRegister.getRandom();
        accessToken = userCreateAndDelete.create(user)
                .assertThat()
                .statusCode(200)
                .extract()
                .path("accessToken");
        assertThat("Для пользователя не создан токен", accessToken, is(not(0)));
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегестрирован")
    public void createUserAndCheckStatusCode403(){
        UserRegister user = UserRegister.getRandom();
        accessToken = userCreateAndDelete.create(user)
                .assertThat()
                .statusCode(200)
                .extract()
                .path("accessToken");

        String isNotUserCreated = userCreateAndDelete.create(user)
                .assertThat()
                .statusCode(403)
                .extract()
                .path("message");
        assertEquals("Создались два одинаковых пользователя", "User already exists", isNotUserCreated);
    }
}
