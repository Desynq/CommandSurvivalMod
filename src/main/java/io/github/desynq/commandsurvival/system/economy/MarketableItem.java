package io.github.desynq.commandsurvival.system.economy;

import io.github.desynq.commandsurvival.util.MathHelper;
import io.github.desynq.commandsurvival.util.data.money.Money;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

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
public class MarketableItem {
    //------------------------------------------------------------------------------------------------------------------
    // Instance Fields
    //------------------------------------------------------------------------------------------------------------------

    public final ItemStack itemStack;
    public final Money basePrice;
    public final @Nullable MarketableItemPredicate<Player, CompoundTag> predicate;
    public final @Nullable Integer scaleQuantity;
    public final String itemCategory;
    public final String itemName;
    public final @Nullable Money sellPriceFloor;
    public final @Nullable Money buyPriceCeiling;
    public final Float buyModifier;
    public final double startingCirculation;

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
        this.sellPriceFloor = builder.sellPriceFloor;
        this.buyPriceCeiling = builder.buyPriceCeiling;
        this.buyModifier = builder.buyModifier;
        this.startingCirculation = builder.startingCirculation;

        instances.add(this);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Static Methods and Fields
    //------------------------------------------------------------------------------------------------------------------

    public static Set<MarketableItem> instances = new HashSet<>();

    public static void diminishAll(double percentage) {
        instances.forEach(marketableItem -> {
            marketableItem.fluctuateCirculation(percentage);
        });
    }

    public static double getBiasedFluctuationPercentage() {
        // mean gain/loss = 2.5%
        return MathHelper.getBiasedRandom(0, 0.5, 20);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Circulation
    //------------------------------------------------------------------------------------------------------------------

    public double getCirculation() {
        if (!MarketableItemSerializer.hasCirculation(this)) {
            MarketableItemSerializer.setCirculation(this, startingCirculation);
        }
        return MarketableItemSerializer.getCirculation(this);
    }

    public void setCirculation(double amount) {
        MarketableItemSerializer.setCirculation(this, amount);
    }

    public void addToCirculation(double amount) {
        setCirculation(getCirculation() + amount);
    }

    public void fluctuateCirculation(double percentage) {
        double circulation = getCirculation();
        circulation *= 1 + (circulation < startingCirculation ? 1 : -1) * percentage;
        setCirculation(circulation);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Pricing
    //------------------------------------------------------------------------------------------------------------------

    public Money getSellPrice() {
        if (scaleQuantity == null) {
            return basePrice;
        }
        // basePrice * (0.5 ** (circulation / (scaleQuantity + startingCirculation)))
        double scale = getCirculation() / (scaleQuantity + startingCirculation);
        double realPrice = basePrice.getRaw() * Math.pow(0.5, scale);
        if (sellPriceFloor != null) {
            realPrice = Math.max(realPrice, sellPriceFloor.getRaw());
        }
        return Money.fromCents(realPrice);
    }

    public Money getBuyPrice() {
        if (buyModifier == null || buyModifier < 1) {
            return null; // item is not buyable
        }
        double realPrice = getSellPrice().getRaw() * buyModifier;
        if (buyPriceCeiling != null) {
            realPrice = Math.min(realPrice, buyPriceCeiling.getRaw());
        }
        return Money.fromCents(realPrice);
    }

    public Money estimate(int day) {
        return null;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Side Effects
    //------------------------------------------------------------------------------------------------------------------

    public boolean buy(Player player) {
        return true;
    }
}
