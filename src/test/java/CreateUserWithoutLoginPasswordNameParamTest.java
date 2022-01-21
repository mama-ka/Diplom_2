import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)

public class CreateUserWithoutLoginPasswordNameParamTest {
    private UserCreateAndDelete userCreateAndDelete;
    private String name;
    private String email;
    private String password;

    @Before
    public void setUp() {
        userCreateAndDelete = new UserCreateAndDelete();
    }

    public CreateUserWithoutLoginPasswordNameParamTest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

    }
    @Parameterized.Parameters
    public static Object[] getCreateData() {
        return new Object[][]{
                {"", "email@ya.ru", "password"},
                {"name", "", "password"},
                {"name", "email@ya.ru", ""}
        };
    }
    @Test
    @DisplayName("Нельзя создать пользователя без заполнения одного из полей")
    public void createUserWithoutEmailNamePasswordCheckStatusCode403() {
        UserRegister user = new UserRegister(name, email, password);

        String withoutField = userCreateAndDelete.create(user)
                .assertThat()
                .statusCode(403)
                .extract()
                .path("message");
        assertThat("Пользователь создан", withoutField, equalTo("Email, password and name are required fields"));

    }
}
