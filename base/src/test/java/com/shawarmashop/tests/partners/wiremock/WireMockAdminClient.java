package com.shawarmashop.tests.partners.wiremock;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
public class WireMockAdminClient {
    private final String baseUrl;

    private RequestSpecification spec() {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .build();
    }

    /** Отправляет собранный маппинг в WireMock и возвращает его уникальный ID. */
    public String addMapping(StubBuilder builder) {
        String id = given(spec())
                .body(builder.build())
                .post("/__admin/mappings")
                .then().statusCode(201)
                .extract().jsonPath().getString("id");
        if (id == null || id.isBlank()) {
            throw new IllegalStateException("Wiremock не вернул id маппинга");
        }
        return id;
    }

    /** Сбрасывает все временные маппинги и восстанавливает базовые правила с диска контейнера. */
    public void resetToDefaults() {
        given(spec())
                .post("/__admin/mappings/reset")
                .then().statusCode(200);
    }

    /** Удаляет все зарегистрированные маппинги из памяти WireMock. */
    public void clearRecorded() {
        given(spec())
                .delete("/__admin/mappings")
                .then().statusCode(200);
    }

    /** Ищет в истории WireMock запросы, соответствующие переданным критериям (метод, URL и т.д.). */
    public List<Map<String, Object>> findRequests(Map<String, Object> criteria) {
        List<Map<String, Object>> requests = given(spec())
                .post("/__admin/requests/find")
                .then().statusCode(200)
                .extract().jsonPath().getList("requests");
        return requests  == null ? List.of() : requests;
    }
}
