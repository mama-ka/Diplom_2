import org.apache.commons.lang3.RandomStringUtils;

public class UserRegister {
    public static final String EMAIL_POSTFIX = "@yandex.ru";
    public final  String name;
    public final  String email;
    public final  String password;

    public UserRegister(String name, String email, String password ) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public static UserRegister getRandom(){
        String name = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphabetic(10).toLowerCase() + EMAIL_POSTFIX;
        String password = RandomStringUtils.randomAlphabetic(10);
        return new UserRegister(name, email, password);
    }
  }
