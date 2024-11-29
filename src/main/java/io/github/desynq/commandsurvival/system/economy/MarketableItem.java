package io.github.desynq.commandsurvival.system.economy;

import io.github.desynq.commandsurvival.system.economy.builder.MarketableItemBuilder;
import io.github.desynq.commandsurvival.data.Money;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Ties {@link Marketable} to an item with {@link MarketableItem#itemStack}
 */
public class MarketableItem extends Marketable {
    //------------------------------------------------------------------------------------------------------------------
    // Instance Fields
    //------------------------------------------------------------------------------------------------------------------

    public final @NotNull ItemStack itemStack;
    public final @NotNull String itemCategory;
    public final @NotNull String itemName;
    public final @Nullable MarketableItemPredicate<Player, CompoundTag> predicate;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    public MarketableItem(@NotNull MarketableItemBuilder builder) {
        super(builder.basePrice, builder.scaleQuantity, builder.priceFloor, builder.priceCeiling, builder.buyModifier);
        this.itemStack = builder.itemStack;
        this.predicate = builder.predicate;
        this.itemCategory = builder.itemCategory;
        this.itemName = builder.itemName;

        instances.put(this.itemName, this);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Inheritance
    //------------------------------------------------------------------------------------------------------------------

    public Money getSellPrice() {
        return getSellPrice(getCirculation());
    }

    public Money getBuyPrice() {
        return getBuyPrice(getCirculation());
    }

    public Money estimate(int days) {
        return estimate(days, getCirculation());
    }

    //------------------------------------------------------------------------------------------------------------------
    // Static Methods and Fields
    //------------------------------------------------------------------------------------------------------------------

    private static final Map<String, MarketableItem> instances = new HashMap<>();

    public static void diminishAll(double percentage) {
        instances.forEach((name, marketableItem) -> {
            marketableItem.fluctuateCirculation(percentage);
        });
    }

    public static Collection<MarketableItem> getMarketableItems() {
        return instances.values();
    }

    public static @Nullable MarketableItem getFromName(String name) {
        return instances.get(name);
    }

    public static Set<String> getCategories() {
        Set<String> categories = new HashSet<>();

        for (MarketableItem marketableItem : getMarketableItems()) {
            categories.add(marketableItem.itemCategory);
        }
        return categories;
    }

    public static Set<String> getNames() {
        return instances.keySet();
    }

    public static Set<String> getNamesFromCategory(String category) {
        Set<String> itemNames = new HashSet<>();

        for (MarketableItem marketableItem : getMarketableItems()) {
            if (marketableItem.itemCategory.equals(category)) {
                itemNames.add(marketableItem.itemName);
            }
        }
        return itemNames;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Circulation
    //------------------------------------------------------------------------------------------------------------------

    public double getCirculation() {
        return MarketableItemSerializer.getCirculation(this);
    }

    public void setCirculation(double amount) {
        MarketableItemSerializer.setCirculation(this, amount);
    }

    public void addToCirculation(double amount) {
        setCirculation(getCirculation() + amount);
    }

    public void fluctuateCirculation(double percentage) {
        double fluctuatedCirculation = MarketHelper.getFluctuatedCirculation(getCirculation(), percentage);
        setCirculation(fluctuatedCirculation);
    }
}
