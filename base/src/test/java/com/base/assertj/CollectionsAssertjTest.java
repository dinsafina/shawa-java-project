package com.base.assertj;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class CollectionsAssertjTest {

    @Test
    public void testCollections(){
        List<String> paid = List.of("PAID", "CREATED", "PREPARING");
        Assertions.assertThat(paid).as("Список с доступными статусами")
                .hasSize(3)
                .doesNotContain("DONE")
                .contains("PAID")
                .allMatch(x -> x.toUpperCase().equals(x));
    }

    @Test
    public void softAssertions(){
        List<String> paid = List.of("PAID", "CREATED", "PREPARING");
        assertSoftly(x -> {
            x.assertThat(paid).hasSize(4)
                    .contains("DONE")
                    .doesNotContain("CANCELLED");
        });
    }
}
