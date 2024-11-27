package io.github.desynq.commandsurvival.system.economy;

import io.github.desynq.commandsurvival.util.data.money.Money;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

import static io.github.desynq.commandsurvival.system.economy.MarketableItemSerializer.*;

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
    public final ItemStack itemStack;
    public final Money basePrice;
    public final @Nullable MarketableItemPredicate<Player, CompoundTag> predicate;
    public final @Nullable Integer scaleQuantity;
    public final String itemCategory;
    public final String itemName;
    public final @Nullable Money sellPriceFloor;
    public final @Nullable Money buyPriceCeiling;
    public final Float buyModifier;
    public final int startingCirculation;

    public static Set<MarketableItem> instances = new HashSet<>();

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

    public int getAmountSold() {
        if (!hasCirculation(this)) {
            setCirculation(this, startingCirculation);
        }
        return getCirculation(this);
    }

    public Money getSellPrice() {
        if (scaleQuantity == null) {
            return basePrice;
        }
        // basePrice * (0.5 ** (amountSold / scaleQuantity))
        double scale = (double) getAmountSold() / scaleQuantity;
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



    public boolean buy(Player player) {
        return true;
    }
}
