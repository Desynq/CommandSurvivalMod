package io.github.desynq.commandsurvival.systems.market.item;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class MarketableItemInstancesManager {

    private static final Map<String, MarketableItem> instances = new HashMap<>();

    /**
     * @return {@code true} if item is not already being managed
     */
    public static boolean addInstance(MarketableItem marketableItem) {
        return instances.put(marketableItem.itemName, marketableItem) == null;
    }



    public static void diminishAll(double percentage) {
        instances.values().forEach(item -> item.fluctuateCirculation(percentage));
    }

    public static @NotNull Collection<MarketableItem> getMarketableItems() {
        return instances.values();
    }

    public static @Nullable MarketableItem getFromName(String name) {
        return instances.get(name);
    }

    public static @NotNull Set<String> getCategories() {
        return getMarketableItems()
                .stream()
                .map(marketableItem -> marketableItem.itemCategory)
                .collect(Collectors.toSet());
    }

    @Contract(pure = true)
    public static @NotNull Set<String> getNames() {
        return instances.keySet();
    }

    public static @NotNull Set<String> getNamesFromCategory(String category) {
        return getMarketableItems()
                .stream()
                .filter(marketableItem -> marketableItem.itemCategory.equals(category))
                .map(marketableItem -> marketableItem.itemName)
                .collect(Collectors.toSet());
    }
}
