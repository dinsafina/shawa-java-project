package com.shawarmashop.tests.partners.payments;

import com.shawarmashop.tests.partners.payments.dto.*;
import com.shawarmashop.tests.partners.wiremock.WireMockAdminClient;
import com.shawarmashop.tests.partners.wiremock.WiremockStubBase;
import com.shawarmashop.tests.support.Json;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.shawarmashop.tests.partners.wiremock.StubBuilder.post;

/** Коллекция готовых сценариев мокинга (стабов) для внешнего платежного шлюза. */
public class PaymentsStubs extends WiremockStubBase {

    private static final String CHARGE_PATH = "/payments/charge";
    private static final String CURRENCY_RUB = "RUB";
    private static final BigDecimal DEFAULT_FEE = new BigDecimal("5.0");

    /** Инициализирует класс, передавая клиент администрирования WireMock в родительский класс. */
    public PaymentsStubs(WireMockAdminClient admin) {
        super(admin);
    }

    /** Регистрирует мок успешной оплаты, возвращающий статус SUCCEEDED и указанный ID транзакции. */
    public void responseSuccess(String tnxId) {
        admin.addMapping(post(CHARGE_PATH)
                .willReturnJson(200, ChargeResponseDto.builder()
                        .txnId(tnxId)
                        .status(ChargeStatus.SUCCEEDED)
                        .processedAt(Instant.now())
                        .fee(new ChargeMoney(DEFAULT_FEE, CURRENCY_RUB))
                        .message("OK")
                        .build()));
    }

    public void responseFailed(String reason) {
        admin.addMapping(post(CHARGE_PATH)
                .willReturnJson(200, ChargeResponseDto.builder()
                        .txnId(shortId())
                        .status(ChargeStatus.FAILED)
                        .processedAt(Instant.now())
                        .fee(new ChargeMoney(BigDecimal.ZERO, CURRENCY_RUB))
                        .message(reason)
                        .build()));
    }

    public void response500() {
        admin.addMapping(post(CHARGE_PATH)
                .willReturnJson(503, PaymentsErrorDto.builder()
                        .error("service_unvailable")
                        .message("Что то пошло не так")
                        .attempt(1)
                        .build()));
    }

    /** Возвращает список всех реальных requests POST, отправленных бэкендом в WireMock,
     *  распарсенных в DTO. */
    public List<ChargeRequestDto> recordedRequests(){
        Map<String, Object> filter = Map.of("method", "POST", "url", CHARGE_PATH);
        return admin.findRequests(filter).stream()
                .map(x->x.get("body"))
                .filter(Objects::nonNull)
                .map(x-> Json.fromJson(x.toString(), ChargeRequestDto.class))
                .toList();
    }

        public void respondPending(String txnId) {
        admin.addMapping(post(CHARGE_PATH)
                .withPriority(1)
                .willReturnJson(200, ChargeResponseDto.builder()
                        .txnId(txnId)
                        .status(ChargeStatus.PENDING)
                        .processedAt(Instant.now())
                        .fee(new ChargeMoney(BigDecimal.ZERO, CURRENCY_RUB))
                        .message("Awaiting webhook")
                        .build()));
    }

    public void respondFailed(String reason) {
        admin.addMapping(post(CHARGE_PATH)
                .withPriority(1)
                .willReturnJson(200, ChargeResponseDto.builder()
                        .txnId("txn_failed_" + shortId())
                        .status(ChargeStatus.FAILED)
                        .processedAt(Instant.now())
                        .fee(new ChargeMoney(BigDecimal.ZERO, CURRENCY_RUB))
                        .message(reason)
                        .build()));
    }
}
