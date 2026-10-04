package com.base.lombok;

import org.junit.jupiter.api.Test;


public class DataExampleTest {

    @Test
    public void test1() {
        Order build = Order.builder()
                .id(2)
                .price(52.44).build();
        Order order = new Order("Pizza");
        System.out.println(build);
    }
}
