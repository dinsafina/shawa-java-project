package com.base.allure;

import io.qameta.allure.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Заказы")
@Feature("Создание заказа")
public class AllureAnnotationTest {

    @Test
    @Story("Покупатель создает заказ из меню")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание валидного заказа возвращает 201")
    @Description("""
            Гость выбирает рецепт, отправляет POST запрос на api/orders,
            бекенд возвращает 201 Created и отдает id нового заказа
            """)
    public void test1() {
        int code = 201;
        Assertions.assertThat(code).isEqualTo(201);
    }


}
