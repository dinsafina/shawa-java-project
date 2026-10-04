package com.base.junit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloJunitTest {

    @Test
    public void sumTwoNums() {
        //arrage Подготовка тестовых данных
        int a = 2;
        int b = 3;
        //act Действие
        int result = a + b;
        //assert Проверка
        Assertions.assertEquals(5, result);
    }

    @Test
    public void wordContainsAnotherWord() {
        String a = "Cotton";
        boolean word = a.contains("ton");
        Assertions.assertTrue(word, "Исходная строка %s не содержит ton".formatted(a));
    }
}
