package xblydxj.qq.utils;

/**
 * Created by 46321 on 2016/7/7/007.
 */
public class StringUtils {
    private static String usernameRegular = "^[a-zA-Z]\\w{2,19}$";
    private static String passwordRegular = "^[0-9]{3,20}$";

    public static boolean validateUsername(String username) {
        return username == null ? false : username.matches(usernameRegular);
    }

    public static boolean validatePassword(String password) {
        return password == null ?false:password.matches(passwordRegular);
    }
}
