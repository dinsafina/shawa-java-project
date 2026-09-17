import io.qameta.allure.*;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

@Epic("Заказы")
@Feature("Оформление заказа")
class CheckoutAllureTest {

    private static final Faker faker = new Faker(Locale.forLanguageTag("ru"));

    @Test
    @Story("Успешная покупка")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Успешное оформление заказа")
    @Description("Проверка логина, создания заказа, оплаты и статуса")
    void successfulCheckoutFlow() {
        String email = faker.internet().emailAddress();
        String password = faker.credentials().password();
        long recipeId = faker.number().numberBetween(1L, 1_000_000L);
        int qty = faker.number().numberBetween(1, 100);
        String cardNumber = faker.finance().creditCard();

        loginAs(email, password);

        long orderId = createOrder(recipeId, qty);
        payByCard(orderId, cardNumber);

        String actualStatus = fetchStatus(orderId);
        assertStatus(actualStatus, "COMPLETED");
    }

    @Step("Логинимся как {0}")
    void loginAs(String email, String password) {
        // TODO (заглушка)
    }

    @Step("Создаем заказ с id= {0} и количеством {1}")
    long createOrder(long recipeId, int qty) {
        return faker.number().numberBetween(1L, 1_000_000L);
    }

    @Step("Оплачиваем заказ {0} картой {1}")
    void payByCard(long orderId, String cardNumber) {
       // TODO (заглушка)
    }

    @Step("Получаем текущий статус заказа {0}")
    String fetchStatus(long orderId) {
        return "COMPLETED";
    }

    @Step("Проверяем, что статус '{0}' равен ожидаемому '{1}'")
    void assertStatus(String actual, String expected) {
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}