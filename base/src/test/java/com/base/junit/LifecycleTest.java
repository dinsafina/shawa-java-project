package com.base.junit;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LifecycleTest {

    private int counter;

    @BeforeAll
    public static void beforeAll(){
        System.out.println("Before all");
    }

    @BeforeEach
    public void setup(){
        System.out.println("Сброс счетчика до дефолтного значения");
        counter = 10;
    }

    @AfterEach
    public void tearDown(){
        System.out.println("After each");
    }

    @Test
    public void firstTest(){
        System.out.println("Это первый тест");
        counter = counter + 5;
        Assertions.assertEquals(15, counter);
    }

    @Test
    public void secondTest(){
        System.out.println("Это второй тест");
        Assertions.assertEquals(10, counter);
    }

    @Test
    void checkThrow() {
        int i = Integer.parseInt("nan");
        assertThrows(NumberFormatException.class, () -> Integer.parseInt("nan"));

    }
}
