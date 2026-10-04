import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
class Cart {
    private final long userId;

    private final List<Item> items = new ArrayList<>();

    void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }

    }

    @Data
    static class Item {
        private String dish;
        private int quantity;
        private double price;
    }
}