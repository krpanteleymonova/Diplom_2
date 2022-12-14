package Users;

import com.github.javafaker.Faker;

import java.sql.Timestamp;

public class UserGenerator {
    public static String newName = "pokemonchik";
    public static String newEmail = "pokemonchik@ya.ru";
    public static String newPassword = "pokemonchik123456";
    static Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    static Faker faker = new Faker();
    private static final String email = faker.internet().emailAddress();
    private static final String name = faker.name().firstName();
    private static final String password = name + timestamp.getTime();

    public static User getDefaultUser() {
        System.out.println("Создаем пользователя: " + email + ", " + password + ", " + name);
        return new User(email, password, name);
    }

    public static User getUserNoName() {
        System.out.println("создаем пользователя: " + email + ", " + password);
        return new User(email, password, null);
    }

    public static User getUserNoPassword() {
        System.out.println("создаем пользователя: " + email + ", " + name);
        return new User(email, null, name);
    }

    public static User getUserNoEmail() {
        System.out.println("создаем пользователя: " + password + ", " + name);
        return new User(null, password, name);
    }

    public static User getUserNoParameters() {
        System.out.println("создаем пользователя: без заполнения параметров");
        return new User(null, null, null);
    }
}
