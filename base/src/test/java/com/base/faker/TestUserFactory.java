package com.base.faker;

import net.datafaker.Faker;

public class TestUserFactory {

    private final static Faker faker = new Faker();

    public static TestUser.TestUserBuilder newUser(){
        return TestUser.builder()
                .email(faker.internet().emailAddress())
                .password(faker.credentials().password(8, 12))
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName());
    }
}
