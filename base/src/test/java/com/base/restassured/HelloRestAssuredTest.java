package com.base.restassured;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class HelloRestAssuredTest {

    private final static String BASE_URL = "https://shawarma.threadqa.ru";

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(BASE_URL)
            .setBasePath("/api/v1")
            .addFilter(new AllureRestAssured())
            .build();

    @Test
    public void test1() {
        String jsonBody =
                """
                  {
                    "username": "owner",
                    "password": "owner123"
                  }
                 """;
        given()
                .baseUri(BASE_URL)
                .basePath("/api/v1")
                .body(jsonBody)
                .contentType(ContentType.JSON)
                .when()
                .post("/auth/login")
                .then()
                .log().all()
                .statusCode(200)
                .body("token", Matchers.not(Matchers.emptyOrNullString()))
                .body("type", Matchers.equalTo("Bearer"))
                .body("username", Matchers.equalTo("owner"));
    }

    @Test
    public void test2() {
        LoginRequest loginRequest = new LoginRequest("owner", "owner123");
        JwtResponse jwtResponse = given()
                .baseUri(BASE_URL)
                .basePath("/api/v1")
                .body(loginRequest)
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .post("/auth/login")
                .then()
                .log().all()
                .extract().body().as(JwtResponse.class);
        Assertions.assertThat(jwtResponse.getToken()).isNotBlank();
        Assertions.assertThat(jwtResponse.getType()).isEqualTo("Bearer");
        Assertions.assertThat(jwtResponse.getUsername()).isEqualTo("owner");
        Assertions.assertThat(jwtResponse.getExpiresIn()).isPositive();

        given()
                .baseUri(BASE_URL)
                .basePath("/api/v1")
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + jwtResponse.getToken()).get("/auth/me")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test3() {
        LoginRequest loginRequest = new LoginRequest("owner", "owner123");
        JwtResponse jwtResponse = given(spec)
                .body(loginRequest)
                .post("/auth/login")
                .then()
                .extract().body().as(JwtResponse.class);

        int comboColaId = given(spec)
                .log().all()
                .queryParam("query", "комбо")
                .queryParam("recipeSize", "MEDIUM")
                .header("Authorization", "Bearer " + jwtResponse.getToken())
                .get("/recipes")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getInt("content[0].id");

        given(spec)
                .log().all()
                .pathParam("id", comboColaId)
                .header("Authorization", "Bearer " + jwtResponse.getToken())
                .get("/recipes/{id}")
                .then().log().all()
                .statusCode(200)
                .body("name", Matchers.equalTo("Комбо Кола M"));
    }
}
