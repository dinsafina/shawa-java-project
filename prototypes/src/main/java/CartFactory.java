import net.datafaker.Faker;

class CartFactory {
    private static final Faker FAKER = new Faker();

    static Cart newCart(int size) {
        long userId = FAKER.number().numberBetween(1L, Long.MAX_VALUE);
        Cart cart = new Cart(userId);

        for (int i = 0; i < size; i++) {
            Cart.Item item = new Cart.Item();
            item.setDish(FAKER.food().dish());
            item.setQuantity(FAKER.number().numberBetween(1, 5));
            item.setPrice(FAKER.number().randomDouble(2, 100, 1000));
            cart.addItem(item);
        }
        return cart;
    }
}