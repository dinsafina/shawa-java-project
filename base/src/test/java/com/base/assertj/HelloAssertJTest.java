package com.base.assertj;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloAssertJTest {

    @Test
    public void testInt() {
        int quantity = 5;
        Assertions.assertThat(quantity).isPositive()
                .isGreaterThan(1)
                .isEqualTo(5)
                .isLessThan(10);
    }

    @Test
    public void testString() {
        String jwtToken = "Bearer ds5d5v45sfv45fv3";
        Assertions.assertThat(jwtToken).contains("Bearer")
                .isNotBlank();
    }

    @Test
    public void methodsChainTest() {
        String status = "PAID";
        Assertions.assertThat(status).isNotNull()
                .isEqualTo("PAID")
                .hasSize(4);
    }
}
