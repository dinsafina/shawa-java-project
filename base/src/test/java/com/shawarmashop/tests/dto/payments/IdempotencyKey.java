package com.shawarmashop.tests.dto.payments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdempotencyKey {

    private String value;

    /**Генерация ключа идемпотентности при тестовой оплате*/
    public static IdempotencyKey random(){
        return new IdempotencyKey("test-" + UUID.randomUUID());
    }

    @Override
    public String toString() {
        return  value;
    }
}
