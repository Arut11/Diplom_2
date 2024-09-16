package user;

import com.github.javafaker.Faker;

public class UserGenerator {

    public static Faker faker = new Faker();

    public static User getUser() {
        return new User()
                .setEmail(faker.internet().emailAddress())
                .setPassword(faker.internet().password())
                .setName(faker.name().firstName());

    }

}
