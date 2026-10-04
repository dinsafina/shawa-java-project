package com.base.faker;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class HelloFakerTest {

    private static final Faker faker = new Faker(Locale.forLanguageTag("ru"));

    @Test
    public void test1() {
        System.out.println(faker.address().cityName());
        System.out.println(faker.random().nextInt(50, 142));
    }

    @Test
    public void test2() {
        TestUser build = TestUserFactory.newUser().build();
        System.out.println(build);
    }
}
