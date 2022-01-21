public class UserLogin {
    public final String email;
    public final String password;


    public UserLogin (String email, String password) {
        this.email = email;
        this.password = password;
    }

    static public UserLogin from (UserRegister user){
        return new UserLogin (user.email, user.password);
    }


    static public UserLogin from (UserChange bodyChange){
        return new UserLogin (bodyChange.email, bodyChange.password);
    }

}
