package kuvaldis.play.immutables;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public abstract class Order {

    public abstract List<Item> items();

    @Value.Derived
    public int totalCount() {
        int count = 0;

        for (Item i : items())
            count += i.count();

        return count;
    }
}