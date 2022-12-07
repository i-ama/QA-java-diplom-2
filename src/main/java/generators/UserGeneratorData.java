package generators;

import models.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Locale;

public class UserGeneratorData {

    static private String email = RandomStringUtils.randomAlphabetic(7).toLowerCase(Locale.ROOT) + "@ru.ru";
    static private String password = RandomStringUtils.randomAlphabetic(7);
    static private String name = RandomStringUtils.randomAlphabetic(7);
    static private String secondEmail = RandomStringUtils.randomAlphabetic(7).toLowerCase(Locale.ROOT) + "@ru.ru";
    static private String secondPassword = RandomStringUtils.randomAlphabetic(7);
    static private String secondName = RandomStringUtils.randomAlphabetic(7);

    public static User getDefault() {
        return new User(email, password, name);
    }

    public static User getDefaultSecond() {
        return new User(secondEmail, secondPassword, secondName);
    }

    public static User getDefaultChangedEmail() {
        return new User(secondEmail, password, name);
    }

    public static User getDefaultChangedPassword() {
        return new User(email, secondPassword, name);
    }

    public static User getDefaultChangedPasswordAndEmail() {
        return new User(secondEmail, secondPassword, name);
    }

    public static User getDefaultWithTheSameEmail() {
        return new User(email, secondPassword, secondName);
    }

    public static User getWithoutLogin() {
        return new User(null, password, name);
    }

    public static User getWithoutPassword() {
        return new User(email, null, name);
    }

    public static User getWithoutName() {
        return new User(email, password, null);
    }
}
