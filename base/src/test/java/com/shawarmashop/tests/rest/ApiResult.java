package com.shawarmashop.tests.rest;

import com.shawarmashop.tests.dto.authentication.ApiError;
import com.shawarmashop.tests.support.Json;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public class ApiResult<T> {

    private final int status;
    private final T body;
    private final String rawBody;

    /** Десериализует JSON-ответ в указанный класс*/
    public static <T> ApiResult<T> from(Response response, Class<T> type){
        return parse(response, raw -> type == Void.class ? null : Json.fromJson(raw, type));
    }

    /** Упаковывает ответ в ApiResult, пропуская парсинг, если тело пустое. */
    public static <T> ApiResult<T> from(Response response, TypeReference<T> type){
        return parse(response, raw -> Json.fromJson(raw, type));
    }

    private static <T> ApiResult<T> parse(Response response, Function<String, T> parser) {
        String raw = response.getBody().asString();
        boolean parseable = response.statusCode() / 100 == 2 && raw != null && !raw.isBlank();
        T body = parseable ? parser.apply(raw) : null;
        return new ApiResult<>(response.statusCode(), body, raw);
    }

    /**Проверка статус кода, возврат тела ответа*/
    public T expect(int expectStatus) {
        require(status == expectStatus, "Ожидался статус:" + expectStatus);
        return body;
    }

    /**Сериализация ответа в dto в случае ошибки (например, ошибка авторизации)*/
    public ApiError error(){
        return (rawBody == null || rawBody.isBlank()) ? null : Json.fromJson(rawBody, ApiError.class);
    }

    /**Проверка статуса без тела ответа*/
    public ApiResult<T> assertStatus(int expectStatus) {
        require(status == expectStatus, "Ожидался статус:" + expectStatus);
        return this;
    }

    /**При статусе 2хх возвращает тело ответа*/
    public T success() {
        require(status / 100 == 2, "Ожидался 2xx статус");
        return body;
    }

    private void require(boolean condition, String message) {
        if(!condition) {
            throw new AssertionError(message + ", получен, " + status + ". Тело \n" + rawBody);
        }
    }
}
