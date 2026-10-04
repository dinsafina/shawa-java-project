package com.base.awaitility;

import com.base.restassured.JwtResponse;
import com.base.restassured.LoginRequest;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

public class HelloAwaitTest {

    private final static String BASE_URL = "https://shawarma.threadqa.ru";

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(BASE_URL)
            .setBasePath("/api/v1")
            .addFilters(List.of(new AllureRestAssured(), new ResponseLoggingFilter()))
            .build();

    @Test
    public void test1() {
        LoginRequest loginRequest = new LoginRequest("owner", "owner123");
        JwtResponse jwtResponse = given(spec)
                .body(loginRequest)
                .post("/auth/login")
                .then()
                .extract().body().as(JwtResponse.class);
        int orderId = given(spec)
                .auth().oauth2(jwtResponse.getToken())
                .body("{\"recipeId\":4,\"qty\":1,\"payment\":{\"method\":\"CASH\"}}")
                .post("/orders")
                .then()
                .extract().jsonPath().getInt("id");
        System.out.println("orderId: " + orderId);

        UUID uuid = UUID.randomUUID();
        System.out.println("Idempotency-Key" + uuid);
        given(spec)
                .auth().oauth2(jwtResponse.getToken())
                .pathParam("id", orderId)
                .body("{\"method\": \"CASH\"}")
                .header("Idempotency-Key", uuid)
                .post("/orders/{id}/pay")
                .then().statusCode(202);

        await("Ожидание готовности заказа" + orderId)
                .atMost(Duration.ofSeconds(57))
                .pollInterval(Duration.ofSeconds(5))
                .untilAsserted(()->
                        given(spec)
                                .auth().oauth2(jwtResponse.getToken())
                                .pathParam("id", orderId)
                                .get("/orders/{id}/preparation")
                                .then()
                                .body("status", equalTo("DONE"))
                );
    }
}
