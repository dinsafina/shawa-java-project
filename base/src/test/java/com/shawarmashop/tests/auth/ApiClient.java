package com.shawarmashop.tests.auth;

import com.shawarmashop.tests.rest.domain.AuthClient;

import com.shawarmashop.tests.rest.domain.EventClient;
import com.shawarmashop.tests.rest.domain.IngredientClient;
import com.shawarmashop.tests.rest.domain.OrderClient;
import io.qameta.allure.Description;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiClient {

    private final static ApiClient ANONYMOUS = new ApiClient(null, null);

    private final String fixedToken;
    private final Credentials credentials;

    private String cachedToken;

    /** Если нужен api клиент под к-л конкретным пользователем*/
    public static ApiClient asUser(Credentials credentials) {
        return new ApiClient(null, credentials);
    }

    /** Для негативных тестов без авторизации*/
    public static ApiClient withToken(String token) {
        return new ApiClient(token, null);
    }

    @Description("Клиент без авторизации: без токена и без credentials")
    public static ApiClient getAnonymous() {
        return ANONYMOUS;
    }

   /**Отдаёт токен для подстановки в заголовок: фиксированный, полученный при логине,
            "либо null, если клиент анонимный*/
    public synchronized String tokenOrNull() {
        if (fixedToken != null) {
            return fixedToken;
        }
        if (credentials == null) {
            return null;
        }
        if (cachedToken == null) {
            cachedToken = login();
        }
        return cachedToken;
    }

    /**Авторизация, в случае успеха возвращается токен*/
    private String login() {
        String token = getAnonymous()
                .auth()
                .login(credentials.getUsername(), credentials.getPassword())
                .expect(200)
                .getToken();
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("POST /auth/login вернул пустой JWT для пользователя " + credentials.getUsername());
        }
        return token;
    }

    /**Точки входа к эндпоинтам доменных областей*/
    public AuthClient auth() {
        return new AuthClient(this);
    }

    public EventClient events(){
        return new EventClient(this);
    }

    public IngredientClient ingredients(){
        return new IngredientClient(this);
    }

    public OrderClient orders(){
        return new OrderClient(this);
    }
}
