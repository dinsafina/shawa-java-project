package com.shawarmashop.tests.e2e_tests;

import com.shawarmashop.tests.auth.ApiClient;
import com.shawarmashop.tests.auth.Users;
import com.shawarmashop.tests.dto.authentication.ApiError;
import com.shawarmashop.tests.dto.authentication.UserResponse;
import com.shawarmashop.tests.dto.events.EventLogResponse;
import com.shawarmashop.tests.dto.orders.CreateOrderRequest;
import com.shawarmashop.tests.dto.orders.OrderFilter;
import com.shawarmashop.tests.dto.orders.OrderResponse;
import com.shawarmashop.tests.dto.orders.Page;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AuthTests {

    @Test
    public void test1() {
        ApiClient user = ApiClient.asUser(Users.OWNER);
        UserResponse expect = user.auth().me().expect(200);
        Assertions.assertThat(expect.getUsername()).isEqualTo("owner");
    }

    @Test
    public void test2() {
        ApiClient user = ApiClient.getAnonymous();
        ApiError error = user.auth()
                .me()
                .assertStatus(401)
                .error();
        Assertions.assertThat(error.getDetail())
                .isEqualTo("Full authentication is required to access this resource");
    }

    @Test
    public void test3() {
        ApiClient user = ApiClient.asUser(Users.OWNER);
        user.events().list().expect(200);
    }

    @Test
    public void test4() {
        ApiClient apiClient = ApiClient.asUser(Users.OWNER);
        List<EventLogResponse> expect = apiClient.events().list().expect(200);
        OrderResponse card = apiClient.orders()
                .create(CreateOrderRequest.of(1, 1, "CARD"))
                .success();
        Page<OrderResponse> success = apiClient.orders()
                .list(OrderFilter.builder()
                        .recipeId(1)
                        .build())
                .success();
        Assertions.assertThat(success.getContent()).hasSize(1);
    }
}
