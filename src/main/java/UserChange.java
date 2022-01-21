import org.apache.commons.lang3.RandomStringUtils;

public class UserChange {
    public static final String EMAIL_POSTFIX = "@yandex.ru";
    public String email;
    public String name;
    public String password;


    public UserChange (String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static UserChange getRandom(){
        String name = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphabetic(10).toLowerCase() + EMAIL_POSTFIX;
        String password = RandomStringUtils.randomAlphabetic(10);
        return new UserChange (name, email, password);
    }

    public UserChange(){
    }

    public UserChange setEmail(String email){
        this.email = email;
        return this;
    }
    public UserChange setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserChange setName(String name){
        this.name = name;
        return this;
    }

    public static UserChange getPassword() {
        return new UserChange().setPassword(RandomStringUtils.randomAlphabetic(10));
    }

    public static UserChange getName() {
        return new UserChange().setName(RandomStringUtils.randomAlphabetic(10));
    }

    public static UserChange getEmail() {
        return new UserChange().setEmail(RandomStringUtils.randomAlphabetic(10) + EMAIL_POSTFIX);
    }



}
