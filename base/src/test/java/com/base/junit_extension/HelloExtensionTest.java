package com.base.junit_extension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

public class HelloExtensionTest {

    @Test
    @ExtendWith(LogTestNameExtension.class)
    public void test1() {
        System.out.println("Название test1");
        Assertions.assertEquals(5, 10);
    }

    @Test
    public void test2() {
        System.out.println("Название test2");
        Assertions.assertEquals(5, 10);
    }

    @Test
    @Garage(cars = 5)
    @ExtendWith(GarageParamResolver.class)
    public void test3(UserWithGarage userWithGarage) {
        System.out.println("Название test2");
        Assertions.assertEquals(5, 10);
    }
}
