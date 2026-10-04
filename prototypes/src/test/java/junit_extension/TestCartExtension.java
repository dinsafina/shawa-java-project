package junit_extension;

import net.datafaker.Faker;
import org.junit.jupiter.api.extension.*;

public class TestCartExtension implements BeforeEachCallback,
        AfterEachCallback, ParameterResolver {

    private final static Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) {
        long id = faker.number().numberBetween(1L, 1_000_000L);
        TestCart testCart = new TestCart(id);
        store(context).put("cart", testCart);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        TestCart cart = store(context).remove("cart", TestCart.class);
        if (cart == null) return;
        cart.clear();
        System.out.println("Корзина после теста: " + cart);
        System.out.println("Размер после очистки: " + cart.getItems().size());
    }

    @Override
    public boolean supportsParameter(ParameterContext pc,
                                     ExtensionContext ec) {
        return pc.getParameter().getType() == TestCart.class;
    }

    @Override
    public Object resolveParameter(ParameterContext pc,
                                   ExtensionContext ec) {
        return store(ec).get("cart", TestCart.class);
    }

    private static ExtensionContext.Store store(
            ExtensionContext context) {
        var namespace = ExtensionContext.Namespace
                .create(TestCartExtension.class);
        return context.getStore(namespace);
    }
}
