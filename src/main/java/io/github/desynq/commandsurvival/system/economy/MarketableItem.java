package io.github.desynq.commandsurvival.system.economy;

import io.github.desynq.commandsurvival.CommandSurvival;
import io.github.desynq.commandsurvival.system.economy.builder.MarketableItemBuilder;
import io.github.desynq.commandsurvival.util.MathHelper;
import io.github.desynq.commandsurvival.util.data.money.Money;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * <pre>
 * Defined by:
 * - {@link MarketableItem#itemStack}
 *   - ID is used to determine whether the player can sell the item or not
 *   - ID and NBT is used for giving the item with its NBT to the player
 * - {@link MarketableItem#basePrice}
 *   - Determines the base price of the marketable item
 * - {@link MarketableItem#predicate}
 *   - Optionally used to determine whether the player can sell the item if it matches the provided NBT tag
 * - {@link MarketableItem#scaleQuantity}
 *   - How many of the item needs to be sold for the price to decrease by 50%
 *   - Conversely, how many of the item needs to be bought for the price to increase by 100%
 * </pre>
 */
public class MarketableItem implements MarketableItemInterface {
    //------------------------------------------------------------------------------------------------------------------
    // Instance Fields
    //------------------------------------------------------------------------------------------------------------------

    public final @NotNull ItemStack itemStack;
    public final @NotNull Money basePrice;
    public final @NotNull String itemCategory;
    public final @NotNull String itemName;
    public final @Nullable MarketableItemPredicate<Player, CompoundTag> predicate;
    public final @Nullable Integer scaleQuantity;
    public final @Nullable Money priceFloor;
    public final @Nullable Money priceCeiling;
    public final @Nullable Double buyModifier;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    public MarketableItem(@NotNull MarketableItemBuilder builder) {
        this.itemStack = builder.itemStack;
        this.basePrice = builder.basePrice;
        this.predicate = builder.predicate;
        this.scaleQuantity = builder.scaleQuantity;
        this.itemCategory = builder.itemCategory;
        this.itemName = builder.itemName;
        this.priceFloor = builder.priceFloor;
        this.priceCeiling = builder.priceCeiling;
        this.buyModifier = builder.buyModifier;

        instances.put(this.itemName, this);
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

    public static double getBiasedFluctuationPercentage() {
        // mean gain/loss = 2.5%
        return MathHelper.getBiasedRandom(0, 0.5, 20);
    }

    public static Collection<MarketableItem> getMarketableItems() {
        return instances.values();
    }

    public static MarketableItem getFromName(String name) {
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
        double fluctuatedCirculation = getFluctuatedCirculation(getCirculation(), percentage);
        setCirculation(fluctuatedCirculation);
    }

    /**
     * Extracted code from fluctuateCirculation() to allow for isolated simulation
     */
    public static double getFluctuatedCirculation(double circulation, double percentage) {
        return circulation * (1 + (circulation < 0 ? 1 : -1) * percentage);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Pricing
    //------------------------------------------------------------------------------------------------------------------

    public Money getSellPrice() {
        return MarketableItemInterface.getSellPrice(
                basePrice,
                getCirculation(),
                scaleQuantity,
                priceFloor,
                priceCeiling
        );
    }

    /**
     * max = priceCeiling * buyModifier<br>
     * min = priceFloor * buyModifier
     */
    public Money getBuyPrice() {
        if (buyModifier == null || buyModifier < 1) {
            return null; // item is not buyable
        }
        double realPrice = getSellPrice().getRaw() * buyModifier;
        return Money.fromCents(realPrice);
    }

    /**
     * Estimates what the item's circulation might be after x amount of days
     */
    public Money estimate(int days) {
        double circulation = getCirculation();
        double percentage;

        for (int day = days; day >= 0; day--) {
            percentage = getBiasedFluctuationPercentage();
            circulation = getFluctuatedCirculation(circulation, percentage);
        }

        return MarketableItemInterface.getSellPrice(
                basePrice,
                circulation,
                scaleQuantity,
                priceFloor,
                priceCeiling
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    // Side Effects
    //------------------------------------------------------------------------------------------------------------------

    public boolean buy(Player player) {
        return true;
    }
}
