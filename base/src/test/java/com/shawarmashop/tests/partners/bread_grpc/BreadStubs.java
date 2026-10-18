package com.shawarmashop.tests.partners.bread_grpc;

import com.shawarmashop.tests.partners.bread_grpc.dto.BreadConfirmationDto;
import com.shawarmashop.tests.partners.bread_grpc.dto.BreadErrorDto;
import com.shawarmashop.tests.partners.bread_grpc.dto.BreadOrderDto;
import com.shawarmashop.tests.partners.wiremock.WireMockAdminClient;
import com.shawarmashop.tests.partners.wiremock.WiremockStubBase;
import com.shawarmashop.tests.support.Json;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.shawarmashop.tests.partners.wiremock.StubBuilder.post;

public class BreadStubs extends WiremockStubBase {

    private final static String GRPC_PATH = "/BreadBakery/orderBatch";

    public BreadStubs(WireMockAdminClient admin) {
        super(admin);
    }

    /** Регистрирует мок успешного подтверждения бронирования партии хлеба с переданным DTO. */
    public void respondWith(BreadConfirmationDto confirmationDto) {
        admin.addMapping(post(GRPC_PATH).willReturnJson(200, confirmationDto));
    }

    /**Неудачное обращение в пекарню*/
    public void respondUnavailable() {
        admin.addMapping(post(GRPC_PATH).willReturnJson(502, BreadErrorDto.builder()
                .error("bad_gateway")
                .message("Bread bakery service in unavailable")
                .occurredAt(Instant.now())
                .build()));
    }

    /**Подсчет кол-ва вызовов во внешнюю интеграцию*/
    public List<BreadOrderDto> recordedCalls(){
        Map<String, Object> filter = Map.of("method", "POST", "url", GRPC_PATH);
        return admin.findRequests(filter).stream()
                .map(x->x.get("body"))
                .filter(Objects::nonNull)
                .map(x-> Json.fromJson(x.toString(), BreadOrderDto.class))
                .toList();
    }
}
