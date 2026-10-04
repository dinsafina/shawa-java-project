package com.base.kafka;

import com.base.jackson.OrderResponse;
import com.base.restassured.JwtResponse;
import com.base.restassured.LoginRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class HelloKafkaTest {

    private final static String KAFKA_SERVER = "shawarma.threadqa.ru:9094";
    private final static String TOPIC = "order.events";

    private final static String BASE_URL = "https://shawarma.threadqa.ru";

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(BASE_URL)
            .setBasePath("/api/v1")
            .addFilters(List.of(new AllureRestAssured(), new ResponseLoggingFilter()))
            .build();

    @Test
    @SneakyThrows
    public void test1() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-" + UUID.randomUUID());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(List.of(TOPIC));

            LoginRequest loginRequest = new LoginRequest("owner", "owner123");
            JwtResponse jwtResponse = given(spec)
                    .body(loginRequest)
                    .post("/auth/login")
                    .then()
                    .extract().body().as(JwtResponse.class);
            OrderResponse orderResponse = given(spec)
                    .auth().oauth2(jwtResponse.getToken())
                    .body("{\"recipeId\": 14, \"qty\": 1, \"payment\": {\"method\": \"CASH\"}}")
                    .post("/orders")
                    .then()
                    .statusCode(201)
                    .extract().body().as(OrderResponse.class);
            int createdOrderId = orderResponse.getId(); // Сохраняем ID созданного заказа
            System.out.println("Создан заказ с ID: " + createdOrderId);

            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofSeconds(20));
            OrderPlacedEvent foundEvent = null;
            ObjectMapper objectMapper = new ObjectMapper();

            for (ConsumerRecord<String, String> record : poll) {
                // 1. Десериализуем ВСЁ событие
                KafkaEvent kafkaEvent = objectMapper.readValue(record.value(), KafkaEvent.class);

                // 🔥 Фильтруем только нужные события
                if (!"ORDER_PLACED".equals(kafkaEvent.getType())) {
                    continue; // Пропускаем все, кроме ORDER_PLACED
                }
                // 2. Извлекаем payload
                OrderPlacedEvent event = kafkaEvent.getPayload();
                System.out.println("  → ORDER_PLACED для orderId=" + event.getOrderId());

                // 3. Ищем наше событие
                if (event.getOrderId() != null && event.getOrderId() == createdOrderId) {
                    foundEvent = event;
                    System.out.println("Найдено событие для заказа " + createdOrderId);
                    break;
                }
            }
            // ПРОВЕРЯЕМ результат ПОСЛЕ поиска
            assertThat(foundEvent)
                    .as("Должно быть найдено событие для заказа " + createdOrderId)
                    .isNotNull();
            assertThat(foundEvent.getOrderId()).isEqualTo(14);
            assertThat(foundEvent.getRecipeId()).isEqualTo(14);
            assertThat(foundEvent.getRecipeName()).isEqualTo("Комбо Босс L");
            assertThat(foundEvent.getQty()).isEqualTo(1);
            assertThat(foundEvent.getTotalPrice()).isEqualTo(680.00);
        }
    }
}
