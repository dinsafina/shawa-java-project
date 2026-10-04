package junit_extension;

import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestCartExtension.class)
public class TestCartTest {

    private final static Faker faker = new Faker();

    @Test
    void cartIsInjectedFresh(TestCart cart) {
        Assertions.assertThat(cart.getId()).isPositive();
        Assertions.assertThat(cart.getItems()).isEmpty();
    }

    @Test
    public void checkItemsInCart(TestCart cart) {

        List<String> expected = new ArrayList<>();
        String firstDish = faker.food().dish();
        String secondDish = faker.food().dish();
        String thirdDish = faker.food().dish();

        cart.add(firstDish);
        cart.add(secondDish);
        cart.add(thirdDish);

        expected.add(firstDish);
        expected.add(secondDish);
        expected.add(thirdDish);

        Assertions.assertThat(cart.getItems()).hasSize(3);
        Assertions.assertThat(cart.getItems()).isEqualTo(expected);
    }
}
