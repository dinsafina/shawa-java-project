package com.base.junit;

import org.junit.jupiter.api.*;

@DisplayName("Примеры тестов с аннотациями @DisplayName, @Tag, @Disabled")
public class DisplayNameAndTagsTest {

    @Test
    @Tag("smoke тесты")
    @DisplayName("smoke проверка: сумма работает")
    public void test1() {
        Assertions.assertEquals(4, 2 + 2);
    }

    @Test
    @Tag("regress")
    @DisplayName("Долгая проверка: умножение работает")
    public void test2() {
        Assertions.assertEquals(20, 4 * 5);
    }

    @Test
    @Disabled("Ждем ответа от смежной команды")
    public void test3() {
     ///
    }
}
