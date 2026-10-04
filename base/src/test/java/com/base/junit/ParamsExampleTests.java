package com.base.junit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ParamsExampleTests {

    @ParameterizedTest(name = "Статус заказа {0}")
    @ValueSource(strings = {"PAID", "PREPARED", "READY", "COMPLETED"} )
    public void successStatuses(String s){
        System.out.println(s);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10})
    public void numbersPositive(int number) {
        Assertions.assertTrue(number > 0);
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "1, 1, 2",
            "2, 3, 5",
            "10, 10, 20",
            "0, 0, 0"
    })
    public void sumTest(int a, int b, int result) {
        Assertions.assertEquals(a + b, result);
    }

    @Test
            public void te(){
        Assertions.assertThrows(NumberFormatException.class, () ->  Integer.parseInt("nan"));
    }
}
