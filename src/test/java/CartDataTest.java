import org.aeonbits.owner.ConfigFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CartDataTest {

    @Test
    void configReturnsDefaults() {
        CartConfig config = ConfigFactory.create(CartConfig.class);
        Assertions.assertThat(config.defaultSize()).isEqualTo(3);
        Assertions.assertThat(config.maxSize()).isEqualTo(20);
    }

    @Test
    void factoryCreatesCartOfDefaultSize() {
        CartConfig config = ConfigFactory.create(CartConfig.class);
        Cart cart = CartFactory.newCart(config.defaultSize());

        Assertions.assertThat(cart.getUserId()).isPositive();
        List<Cart.Item> items = cart.getItems();
        Assertions.assertThat(items)
                .allSatisfy(item -> {
                    Assertions.assertThat(item.getDish()).isNotEmpty();
                    Assertions.assertThat(item.getQuantity()).isBetween(1, 4);
                    Assertions.assertThat(item.getPrice()).isBetween(100.0, 1000.0);
                });
    }

    @Test
    void factoryRespectsExplicitSize() {
        Cart cart = CartFactory.newCart(7);
        Assertions.assertThat(cart.getItems()).hasSize(7);
    }
}
