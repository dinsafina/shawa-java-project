package junit_extension;

import java.util.ArrayList;
import java.util.List;

public class TestCart {

    private final long id;
    private final List<String> items = new ArrayList<>();

    public TestCart(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public List<String> getItems() {
        return items;
    }

    void add(String dish) {
        items.add(dish);
    }

    void clear() {
        items.clear();
    }

    @Override
    public String toString() {
        return "TestCart{" +
                "id=" + id +
                ", items=" + items +
                '}';
    }
}
