package com.shawarmashop.tests.partners.wiremock;

import com.shawarmashop.tests.support.Json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StubBuilder {

    /**
     * Подготавливает структуру маппинга (request/response) для API WireMock.
     */
    private final Map<String, Object> requests = new LinkedHashMap<>();
    private final Map<String, Object> responses = new LinkedHashMap<>();
    private final Map<String, Object> mappings = new LinkedHashMap<>();

    private final List<Map<String, Object>> bodyPatterns = new ArrayList<>();

    /**
     * Инициализирует билдер, задавая базовые параметры перехватываемого HTTP-запроса (метод и URL).
     */
    private StubBuilder(String method, String url) {
        requests.put("method", method);
        requests.put("url", url);
    }

    /**
     * Создает новый экземпляр билдера для перехвата запроса с указанным методом и URL.
     */
    public static StubBuilder request(String method, String url) {
        return new StubBuilder(method, url);
    }

    /**
     * Создает билдер для перехвата POST-запроса по указанному URL.
     */
    public static StubBuilder post(String url) {
        return request("POST", url);
    }

    public static StubBuilder get(String url) {
        return request("GET", url);
    }

    public static StubBuilder delete(String url) {
        return request("DELETE", url);
    }

    /**Через сколько вызовов сработает мок, приоритетность*/
    public StubBuilder withPriority(int priority) {
        mappings.put("priority", priority);
        return this;
    }

    /**
     * Добавляет условие срабатывания мока: проверка точного совпадения значения
     * по заданному JSON-пути в теле запроса. Что пришло от клиента
     */
    public StubBuilder withJsonPath(String jsonPath, String expectedValue) {
        bodyPatterns.add(Map.of(
                "matchesJsonPath", Map.of(
                        "expression", jsonPath,
                        "equalTo", expectedValue
                )
        ));
        return this;
    }

    /**
     * Для SOAP интеграции
     */
    public StubBuilder withXpath(String xpath) {
        bodyPatterns.add(Map.of(
                "matchesXPath", xpath));
        return this;
    }

    /**
     * Формирует JSON-ответ с указанным HTTP-статусом, заголовком Content-Type
     * и сериализованным телом из переданного Java-объекта. Что WireMock вернет клиенту в ответ
     */
    public StubBuilder willReturnJson(int status, Object object) {
        responses.put("status", status);
        responses.put("headers", Map.of("Content-type", "application/json"));
        //в качестве объекта маппим в тело
        responses.put("jsonBody", Json.MAPPER.convertValue(object, Map.class));
        return this;
    }

    public StubBuilder willReturnXml(int status, String xml) {
        responses.put("status", status);
        responses.put("headers", Map.of("Content-type", "text/xml; charset=UTF-8"));
        //в качестве объекта маппим в тело
        responses.put("body", xml);
        return this;
    }

    /**
     * Собирает и возвращает Map-структуру, описывающую только условия входящего запроса
     * (без блока response).
     */
    public Map<String, Object> buildRequest() {
        if (!bodyPatterns.isEmpty()) {
            requests.put("bodyPatterns", bodyPatterns);
        }
        return requests;
    }

    /**
     * Собирает и возвращает полную Map-структуру маппинга (request + response)
     * для отправки в WireMock.
     */
    public Map<String, Object> build() {
        if (!bodyPatterns.isEmpty()) {
            requests.put("bodyPatterns", bodyPatterns);
        }
        mappings.put("request", requests);
        mappings.put("response", responses);
        return mappings;
    }
}
