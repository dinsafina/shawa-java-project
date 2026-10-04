package com.base.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JacksonTest {

   private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @SneakyThrows
    public void testDeserealization() {
        String json = """
        {"id": 15, "status": "PAID", "price": 99.90}
        """;
        OrderResponse orderResponse = objectMapper.readValue(json, OrderResponse.class);
    }

    @Test
    @SneakyThrows
    public void testDeserealizationInList(){
        String json = """
        [
        {"id": 1, "status": "PAID", "price": 99.90},
        {"id": 2, "status": "CREATED", "price": 155.90}
        ]
        """;
        List<OrderResponse> orderResponses = objectMapper
                .readValue(json, new TypeReference<List<OrderResponse>>() {
        });
    }

    @Test
    @SneakyThrows
    public void testSerialization(){
        OrderRequest build = OrderRequest.builder().quantity(1).recipeId(10).build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(build);
        System.out.println(json);
    }
}
